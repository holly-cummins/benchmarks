package io.quarkus.infra.performance.graphics.charts;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import io.quarkus.infra.performance.graphics.PlotDefinition;
import io.quarkus.infra.performance.graphics.Theme;
import io.quarkus.infra.performance.graphics.model.BenchmarkData;

import static io.quarkus.infra.performance.graphics.charts.Cubes.MAXIMUM_CUBE_SIZE;

public class CubeChart extends Chart {

    //    The ideal ratio between space for cubes and space for labels
    private static final int TARGET_CUBE_LABEL_RATIO = 4;
    private final FinePrint fineprint;
    private final List<Cubes> cubes = new ArrayList<>();
    private final Optional<Cubes> tallestCube;
    private final CubeGroup cubeGroup;

    public CubeChart(PlotDefinition plotDefinition, List<Datapoint> data, BenchmarkData bmData) {
        super(plotDefinition, data, bmData);

        this.fineprint = new FinePrint(bmData);
        children.add(fineprint);

        cubeGroup = new CubeGroup();
        cubeGroup.setNumCubesPerColumn(16);

        for (Datapoint d : data) {
            Cubes c = new Cubes(d, cubeGroup);
            cubes.add(c);

        }

        // Add the tallest cube to the children, for height calculations
        tallestCube = cubes.stream()
                .max(Comparator.comparing(Cubes::getPreferredVerticalSize));

        tallestCube.ifPresent(children::add);
    }

    @Override
    public int getMaximumHorizontalSize() {
        return Math.max(Math.max(fineprint.getMaximumHorizontalSize(), cubes.stream().mapToInt(ElasticElement::getMaximumHorizontalSize).sum()), title.getMaximumHorizontalSize()) + 2 * xmargins;
    }

    @Override
    public int getMinimumHorizontalSize() {
        return Math.max(Math.max(fineprint.getMinimumHorizontalSize(), cubes.stream().mapToInt(ElasticElement::getMinimumHorizontalSize).sum()), title.getMinimumHorizontalSize()) + 2 * xmargins;
    }

    @Override
    public int getPreferredHorizontalSize() {
        return Math.max(Math.max(fineprint.getPreferredHorizontalSize(), cubes.stream().mapToInt(ElasticElement::getPreferredHorizontalSize).sum()), title.getPreferredHorizontalSize()) + 2 * xmargins;
    }

    @Override
    protected void drawNoCheck(Subcanvas canvasWithMargins, Theme theme) {

        canvasWithMargins.setPaint(theme.text());

        // Start by assuming we can have the preferred size for title and fine print
        int finePrintHeight = fineprint.getPreferredVerticalSize();
        int titleHeight = title.getPreferredVerticalSize();

        // ... but then check if that actually fits
        int plotHeight = canvasWithMargins.getHeight() - titleHeight - finePrintHeight;

        int minimumCubesSize = tallestCube.map(Cubes::getMinimumVerticalSize).orElse(0);

        // If it doesn't fit, shrink the fine print and title so the actual plot has the minimum it needs
        if (plotHeight < minimumCubesSize) {
            int delta = minimumCubesSize - plotHeight;
            finePrintHeight -= delta / 2;
            titleHeight -= delta / 2;
            plotHeight = canvasWithMargins.getHeight() - titleHeight - finePrintHeight;
        }

        // Ok, hopefully we're good and can start drawing

        Subcanvas titleCanvas = new Subcanvas(canvasWithMargins, canvasWithMargins.getWidth(), titleHeight,
                0, 0);

        int x = 0;

        title.draw(titleCanvas, theme);

        System.out.println("HOLLY drawing into width " + canvasWithMargins.getWidth());
        Subcanvas plotArea = new Subcanvas(canvasWithMargins, canvasWithMargins.getWidth(),
                plotHeight, 0, titleCanvas.getHeight());

        // Now check in the cube bits themselves to adjust cube sizes and font sizes
        int gutterSize = workOutCubeSizes(plotArea);

        for (Cubes c : cubes) {
            int width = c.getActualHorizontalSize();
            Subcanvas dataArea = new Subcanvas(plotArea, width, plotArea.getHeight(), x, 0);
            c.draw(dataArea, theme);
            x += dataArea.getWidth() + gutterSize;
        }

        drawFinePrint(canvasWithMargins, theme, finePrintHeight, titleCanvas.getHeight() + plotArea.getHeight(), fineprint);
    }

    private int workOutCubeSizes(Subcanvas plotArea) {

        // Here's the algorithm we follow to try and fit everything in, when everything affects everything else.
        // 1. Does the text fit in horizontally? If not, shrink it until it fits – we can do this first.
        // 2. Shrink the cubes until all the cubes fits in horizontally
        // 3. Now check if things fit in vertically? If not, we have a choice between shrinking cubes, or shrinking text. That’s a decision we make based on how much vertical space each occupies.

        int minimumGutter = 8;

        int totalColumnsContributingToWidth = 0;

        int unitsPerCube = 1; // For now, assume 1mb per square


        int maxColumns = (int) Math.ceil(maxValue / (cubeGroup.getNumCubesPerColumn() * unitsPerCube));

        int numGutters = data.size() - 1;
        int gutterPadding = minimumGutter * numGutters;

        // Step 1 - make sure all the labels fit horizontally
        int totalTextWidth = cubes.stream().mapToInt(Cubes::getTextWidth).sum();

        int availableWidth = plotArea.getWidth() - gutterPadding;

        while (totalTextWidth > availableWidth) {
            decrementFonts();
            totalTextWidth = cubes.stream().mapToInt(Cubes::getTextWidth).sum();
        }

        // Now make sure all the cubes fit horizontally
        int cubeWithPaddingSize = MAXIMUM_CUBE_SIZE;
        cubeGroup.setTotalCubeSize(cubeWithPaddingSize);
        int totalWidth = cubes.stream().mapToInt(Cubes::getActualHorizontalSize).sum();

        while (totalWidth > availableWidth) {
            cubeGroup.decrementTotalCubeSize();
            totalWidth = cubes.stream().mapToInt(Cubes::getActualHorizontalSize).sum();
        }

        if (tallestCube.isPresent()) {
            // Almost there, but things might still not fit vertically
            int height = tallestCube.map(Cubes::getActualVerticalSize).orElse(0);

            // If it's too tall, decide whether to shrink the fonts or the cubes to get the best-looking result
            while (height > plotArea.getHeight()) {
                int cubesHeight = tallestCube.get().getActualCubesHeight();
                int labelHeight = tallestCube.get().getActualLabelHeight();

                if ((cubesHeight / labelHeight) > TARGET_CUBE_LABEL_RATIO) {
                    cubeGroup.decrementTotalCubeSize();
                } else {
                    decrementFonts();
                }
                height = tallestCube.map(Cubes::getActualVerticalSize).orElse(0);
            }
        }

        int actualOccupiedArea = 0;

        // Work out how much space is used, so we can space the sections evenly
        for (Cubes c : cubes) {
            int width = c.getActualHorizontalSize();
            actualOccupiedArea += width;
        }

        int actualGutterPadding = numGutters > 0 ? (plotArea.getWidth() - actualOccupiedArea) / numGutters:0;

        return actualGutterPadding;
    }

    private void decrementFonts() {
        // If it doesn't fit, shrink fonts
        for (Cubes c : cubes) {
            c.decrementFonts();
        }
    }

    @Override
    public Collection<InlinedSVG> getInlinedSVGs() {
        return fineprint.getInlinedSVGs();
    }
}
