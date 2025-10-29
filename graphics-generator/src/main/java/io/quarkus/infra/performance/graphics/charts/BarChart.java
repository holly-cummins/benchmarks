package io.quarkus.infra.performance.graphics.charts;

import static java.lang.Math.round;

import java.awt.Font;
import java.awt.geom.Rectangle2D;
import java.util.List;

import org.apache.batik.svggen.SVGGraphics2D;

import io.quarkus.infra.performance.graphics.Theme;

public class BarChart implements Chart {
    private static final int BAR_WIDTH = 70;
    private final SVGGraphics2D g;
    private final int canvasHeight;
    private final int canvasWidth;
    private final Theme theme;
    private int labelSize = 24;

    public BarChart(SVGGraphics2D g, Theme theme) {
        this.g = g;
        this.theme = theme;
        this.canvasHeight = g.getSVGCanvasSize().height;
        this.canvasWidth = g.getSVGCanvasSize().width;

    }

    @Override
    public void draw(List<Datapoint> data) {

        g.setPaint(theme.background());
        g.fill(new Rectangle2D.Double(0, 0, canvasWidth, canvasWidth));

        // --- Draw section titles ---
        g.setPaint(theme.text());
        g.setFont(new Font("Arial", Font.BOLD, 24));

        double maxValue = data.stream().map(Datapoint::value).max(Double::compare).orElse(1.0);

        int barSpacing = canvasHeight / data.size() - BAR_WIDTH;
        int y = barSpacing / 2;

        int labelAllowance = 250;
        int leftMargin = 20;
        int barWidth = canvasWidth - 2 * labelAllowance;
        for (Datapoint d : data) {
            // If this framework isn't found, it will just be the text colour, which is fine
            g.setPaint(theme.chartElements().get(d.framework()));
            double val = d.value();
            int x = labelAllowance - 40; // Fudge factor for asymmetric margins
            double scale = barWidth / maxValue;
            int length = (int) (val * scale);
            g.fillRect(x, y, length, BAR_WIDTH);

            g.setPaint(theme.text());
            // Vertically align text with the centre of the bars
            // The SVG attribute alignment-baseline="middle" is not supported by Batik.
            // Why do we divide by 4 instead of 2? I don't know. :)
            int labelY = y + labelSize / 4 + BAR_WIDTH / 2;
            g.drawString(d.framework().getName(), leftMargin, labelY);
            g.drawString(java.lang.String.format("%d %s", round(val), "transactions/sec"), x + length + 20, labelY);

            y += BAR_WIDTH + barSpacing;

        }
    }

}
