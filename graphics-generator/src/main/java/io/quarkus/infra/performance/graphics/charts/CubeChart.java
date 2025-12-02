package io.quarkus.infra.performance.graphics.charts;

import static java.lang.Math.round;

import java.awt.Font;
import java.awt.geom.Rectangle2D;
import java.util.Collection;
import java.util.List;

import io.quarkus.infra.performance.graphics.Theme;
import io.quarkus.infra.performance.graphics.VAlignment;
import io.quarkus.infra.performance.graphics.model.Config;

public class CubeChart extends Chart {
    private static final int BAR_THICKNESS = 44;

    private FinePrint fineprint;

    public CubeChart(String title, List<Datapoint> data, Config metadata) {
        super(title, data, metadata);

        this.fineprint = new FinePrint(metadata);
        children.add(fineprint);
    }

    @Override
    protected void drawNoCheck(Subcanvas g, Theme theme) {
        int canvasHeight = g.getHeight();
        int canvasWidth = g.getWidth();

        g.setPaint(theme.background());
        g.fill(new Rectangle2D.Double(0, 0, canvasWidth, canvasWidth));

        int margins = 20;
        int ymargins = 20;

        int finePrintHeight = 80;

        Subcanvas canvasWithMargins = new Subcanvas(g, canvasWidth - 2 * margins, canvasHeight - 2 * ymargins, margins,
                ymargins);

        int titleTextSize = 48;
        g.setPaint(theme.text());
        Subcanvas titleCanvas = new Subcanvas(canvasWithMargins, canvasWithMargins.getWidth(), titleTextSize * 2, 0, 0);
        titleLabel.setTargetHeight(titleTextSize).draw(titleCanvas, 0, titleTextSize);

        int plotWidth = canvasWidth - 2 * margins;
        int plotHeight = canvasHeight - titleCanvas.getHeight() - finePrintHeight;

        Subcanvas plotArea = new Subcanvas(g, plotWidth, plotHeight, margins, titleCanvas.getHeight());
        int finePrintPadding = 300; // TODO Arbitrary fudge padding, remove when scaling work is done
        Subcanvas finePrintArea = new Subcanvas(canvasWithMargins, plotArea.getWidth() - 2 * finePrintPadding, finePrintHeight,
                finePrintPadding,
                plotArea.getHeight());

        int cubePadding = 1;

        int dataPadding = 8;
        int subSectionWidth = (plotArea.getWidth() + dataPadding) / data.size() - dataPadding;
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
            new Label(d.framework().getExpandedName())
                    .setHorizontalAlignment(Alignment.CENTER)
                    .setVerticalAlignment(VAlignment.TOP)
                    .setTargetHeight(BAR_THICKNESS).draw(labelArea, labelArea.getWidth() / 2, 0);
            new Label(java.lang.String.format("%d %s", round(val), d.value().getUnits()))
                    .setHorizontalAlignment(Alignment.CENTER)
                    .setVerticalAlignment(VAlignment.TOP)
                    .setStyle(Font.BOLD).setTargetHeight(BAR_THICKNESS * 2 / 3)
                    .draw(labelArea, labelArea.getWidth() / 2, labelY + BAR_THICKNESS);

            x += dataArea.getWidth() + dataPadding;

        }
        fineprint.draw(finePrintArea, theme);
    }

    @Override
    public Collection<InlinedSVG> getInlinedSVGs() {
        return fineprint.getInlinedSVGs();
    }
}
