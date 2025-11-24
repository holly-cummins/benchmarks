package io.quarkus.infra.performance.graphics.charts;

import static java.lang.Math.round;

import java.awt.Font;
import java.awt.geom.Rectangle2D;
import java.util.Collection;
import java.util.List;

import org.apache.batik.svggen.SVGGraphics2D;

import io.quarkus.infra.performance.graphics.Theme;
import io.quarkus.infra.performance.graphics.model.Config;

public class BarChart implements Chart {
    private static final int BAR_THICKNESS = 44;
    private static final int barSpacing = 12;
    private static final int labelPadding = 6;

    private final SVGGraphics2D g;
    private final int canvasHeight;
    private final int canvasWidth;
    private final Theme theme;
    private FinePrint fineprint;

    public BarChart(SVGGraphics2D g, Theme theme) {
        this.g = g;
        this.theme = theme;
        this.canvasHeight = g.getSVGCanvasSize().height;
        this.canvasWidth = g.getSVGCanvasSize().width;

    }

    @Override
    public void draw(String title, List<Datapoint> data, Config metadata) {

        g.setPaint(theme.background());
        g.fill(new Rectangle2D.Double(0, 0, canvasWidth, canvasWidth));

        g.setPaint(theme.text());

        double maxValue = data.stream().map(d -> d.value().getValue()).max(Double::compare).orElse(1.0);

        int labelAllowance = 180;
        int leftMargin = 20;
        int barWidth = canvasWidth - 2 * labelAllowance;

        int titleTextSize = 48;
        Subcanvas titleCanvas = new Subcanvas(g, canvasWidth, titleTextSize * 2, leftMargin, 0);
        new Label(title, 0, titleTextSize).setTargetHeight(titleTextSize).setStyle(Font.BOLD).draw(titleCanvas);

        int plotWidth = canvasWidth - 2 * leftMargin;
        int finePrintHeight = 80;
        int plotHeight = canvasHeight - titleCanvas.getHeight() - finePrintHeight;
        Subcanvas chartArea = new Subcanvas(g, plotWidth, plotHeight, leftMargin, titleCanvas.getHeight());

        int y = 0;

        // Fudge factor for asymmetric margins
        int fudge = 40;
        Subcanvas labelArea = new Subcanvas(chartArea, labelAllowance - fudge, plotHeight, 0, 0);
        Subcanvas barArea = new Subcanvas(chartArea, barWidth, plotHeight, labelArea.getWidth(), 0);
        int finePrintPadding = 300;
        Subcanvas finePrintArea = new Subcanvas(chartArea, barWidth - 2 * finePrintPadding, finePrintHeight, finePrintPadding,
                barArea.getHeight() - 50); // TODO Arbitrary fudge padding, remove when scaling work is done

        for (Datapoint d : data) {
            // If this framework isn't found, it will just be the text colour, which is fine
            barArea.setPaint(theme.chartElements().get(d.framework()));
            double val = d.value().getValue();
            double scale = barWidth / maxValue;
            int length = (int) (val * scale);
            barArea.fillRect(0, y, length, BAR_THICKNESS);

            labelArea.setPaint(theme.text());
            // Vertically align text with the centre of the bars
            int labelY = y + BAR_THICKNESS / 2;
            new Label(d.framework().getExpandedName(), labelArea.getWidth() - labelPadding, labelY)
                    .setHorizontalAlignment(Alignment.RIGHT)
                    .setTargetHeight(BAR_THICKNESS).draw(labelArea);
            new Label(java.lang.String.format("%d %s", round(val), d.value().getUnits()),
                    length + labelPadding,
                    labelY).setStyle(Font.BOLD).setTargetHeight(BAR_THICKNESS * 2 / 3).draw(barArea);

            y += BAR_THICKNESS + barSpacing;

        }

        this.fineprint = new FinePrint(finePrintArea, theme);
        fineprint.draw(metadata);
    }

    @Override
    public Collection<InlinedSVG> getInlinedSVGs() {
        return fineprint.getInlinedSVGs();
    }

}
