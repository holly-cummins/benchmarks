package io.quarkus.infra.performance.graphics.charts;

import io.quarkus.infra.performance.graphics.Theme;
import io.quarkus.infra.performance.graphics.model.Config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CubeChart extends Chart {

    private FinePrint fineprint;

    public CubeChart(String title, List<Datapoint> data, Config metadata) {
        super(title, data, metadata);

        this.fineprint = new FinePrint(metadata);
        children.add(fineprint);
    }

    @Override
    protected void drawNoCheck(Subcanvas canvasWithMargins, Theme theme) {

        int finePrintHeight = 80;

        int titleTextSize = 48;
        canvasWithMargins.setPaint(theme.text());

        Subcanvas titleCanvas = new Subcanvas(canvasWithMargins, canvasWithMargins.getWidth(), titleTextSize * 2, 0, 0);
        title.draw(titleCanvas, theme);

        Subcanvas plotArea = new Subcanvas(canvasWithMargins, canvasWithMargins.getWidth(),
                canvasWithMargins.getHeight() - titleCanvas.getHeight() - finePrintHeight, 0, titleCanvas.getHeight());
        int finePrintPadding = 300; // TODO Arbitrary fudge padding, remove when scaling work is done
        Subcanvas finePrintArea = new Subcanvas(canvasWithMargins, plotArea.getWidth() - 2 * finePrintPadding, finePrintHeight,
                finePrintPadding,
                plotArea.getHeight());

        int minimumDataPadding = 8;

        int totalColumnsContributingToWidth = 0;

        List<Cubes> cubes = new ArrayList<>();

        int numCubesPerColumn = 16;
        Cubes.setNumCubesPerColumn(numCubesPerColumn);

        int unitsPerCube = 1; // For now, assume 1mb per square
        int maxColumns = (int) Math.ceil(maxValue / (numCubesPerColumn * unitsPerCube));

        int intersectionPadding = minimumDataPadding * (data.size() - 1);
        int availableWidth = canvasWithMargins.getWidth() - intersectionPadding;

        int minColumnWidth = availableWidth / (maxColumns * data.size());
        int thinOnes = 0;

        for (Datapoint d : data) {
            Cubes c = new Cubes(d);
            cubes.add(c);

            // Estimate the likely width of a column, assuming evenly spaced sections
            if (c.getMinimumHorizontalSize() > c.getColumnCount() * minColumnWidth) {
                thinOnes += c.getMinimumHorizontalSize();
            } else {
                totalColumnsContributingToWidth += c.getColumnCount();
            }
        }

        int cubeWithPaddingSize = totalColumnsContributingToWidth > 0
                ? (availableWidth - thinOnes) / totalColumnsContributingToWidth
                : (maxColumns * data.size()) / thinOnes;
        // If no columns go outside the border, then we use the maximum column count and divide it by the average width occupied by captions

        int x = 0;
        int actualOccupiedArea = 0;

        // Work out how much space is used, so we can space the sections evenly
        for (Cubes c : cubes) {
            c.setCubeSize(cubeWithPaddingSize);

            int width = Math.max(c.getMinimumHorizontalSize(), c.getColumnCount() * cubeWithPaddingSize);
            actualOccupiedArea += width;
        }

        int dataPadding = (canvasWithMargins.getWidth() - actualOccupiedArea) / (data.size() - 1);
        for (Cubes c : cubes) {

            int width = Math.max(c.getMinimumHorizontalSize(), c.getColumnCount() * cubeWithPaddingSize);
            Subcanvas dataArea = new Subcanvas(plotArea, width, plotArea.getHeight(), x, 0);
            c.draw(dataArea, theme);

            x += dataArea.getWidth() + dataPadding;

        }
        fineprint.draw(finePrintArea, theme);
    }

    @Override
    public Collection<InlinedSVG> getInlinedSVGs() {
        return fineprint.getInlinedSVGs();
    }
}
