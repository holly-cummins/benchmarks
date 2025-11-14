package io.quarkus.infra.performance.graphics;

import static java.lang.Math.round;

import java.awt.Font;
import java.awt.geom.Rectangle2D;
import java.util.List;

import org.apache.batik.svggen.SVGGraphics2D;

import io.quarkus.infra.performance.graphics.charts.Alignment;
import io.quarkus.infra.performance.graphics.charts.Chart;
import io.quarkus.infra.performance.graphics.charts.Datapoint;
import io.quarkus.infra.performance.graphics.charts.Label;
import io.quarkus.infra.performance.graphics.charts.Subcanvas;

public class CubeChart implements Chart {
    private static final int BAR_THICKNESS = 44;
    private static final int barSpacing = 12;
    private static final int labelPadding = 6;

    private final SVGGraphics2D g;
    private final int canvasHeight;
    private final int canvasWidth;
    private final Theme theme;

    public CubeChart(SVGGraphics2D g, Theme theme) {
        this.g = g;
        this.theme = theme;
        this.canvasHeight = g.getSVGCanvasSize().height;
        this.canvasWidth = g.getSVGCanvasSize().width;

    }

    @Override
    public void draw(String title, List<Datapoint> data) {

        g.setPaint(theme.background());
        g.fill(new Rectangle2D.Double(0, 0, canvasWidth, canvasWidth));

        // --- Draw section titles ---
        g.setPaint(theme.text());

        double maxValue = data.stream().map(d -> d.value().getValue()).max(Double::compare).orElse(1.0);

        int leftMargin = 20;

        int titleTextSize = 48;
        Subcanvas titleCanvas = new Subcanvas(g, canvasWidth, titleTextSize * 2, leftMargin, 0);
        new Label(title, 0, titleTextSize).setTargetHeight(titleTextSize).setStyle(Font.BOLD).draw(titleCanvas);

        int plotWidth = canvasWidth - 2 * leftMargin;
        int plotHeight = canvasHeight - titleCanvas.getHeight();

        Subcanvas plotArea = new Subcanvas(g, plotWidth, plotHeight, leftMargin, titleCanvas.getHeight());

        int cubePadding = 1;

        int dataPadding = 8;
        int subSectionWidth = plotArea.getWidth() / data.size() - dataPadding;
        int numCubesPerColumn = 16;
        int unitsPerCube = 1; // For now, assume 1mb per square
        int maxColumns = (int) Math.ceil(maxValue / (numCubesPerColumn * unitsPerCube));
        int cubeSize = subSectionWidth / maxColumns - cubePadding;
        int totalCubeSize = cubeSize + cubePadding;

        int x = 0;

        for (Datapoint d : data) {
            Subcanvas dataArea = new Subcanvas(plotArea, subSectionWidth, plotArea.getHeight(), x, 0);

            // If this framework isn't found, it will just be the text colour, which is fine
            plotArea.setPaint(theme.chartElements().get(d.framework()));
            double val = d.value().getValue();

            Subcanvas cubeArea = new Subcanvas(dataArea, dataArea.getWidth(), numCubesPerColumn * totalCubeSize, 0, 0);

            Subcanvas labelArea = new Subcanvas(dataArea, dataArea.getWidth(), dataArea.getHeight() - cubeArea.getHeight(), 0,
                    cubeArea.getHeight());

            int startingCy = cubeArea.getHeight() - totalCubeSize;
            int cx = 0, cy = startingCy;
            int numCubes = (int) Math.round(val / unitsPerCube);

            for (int i = 0; i < numCubes; i++) {
                cubeArea.fillRect(cx, cy, cubeSize, cubeSize);
                cy -= totalCubeSize;
                if ((i + 1) % numCubesPerColumn == 0) {
                    cx += totalCubeSize;
                    cy = startingCy;
                }

            }

            labelArea.setPaint(theme.text());
            int labelY = 0;
            new Label(d.framework().getExpandedName(), labelArea.getWidth() / 2, labelY)
                    .setHorizontalAlignment(Alignment.CENTER)
                    .setVerticalAlignment(VAlignment.TOP)
                    .setTargetHeight(BAR_THICKNESS).draw(labelArea);
            new Label(java.lang.String.format("%d %s", round(val), d.value().getUnits()),
                    labelArea.getWidth() / 2,
                    labelY + BAR_THICKNESS).setHorizontalAlignment(Alignment.CENTER)
                    .setVerticalAlignment(VAlignment.TOP)
                    .setStyle(Font.BOLD).setTargetHeight(BAR_THICKNESS * 2 / 3).draw(labelArea);

            x += dataArea.getWidth() + dataPadding;

        }
    }

}
