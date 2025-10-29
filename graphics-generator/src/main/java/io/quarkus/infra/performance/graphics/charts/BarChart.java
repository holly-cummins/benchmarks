package io.quarkus.infra.performance.graphics.charts;

import static java.lang.Math.round;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.List;

import org.apache.batik.svggen.SVGGraphics2D;

public class BarChart implements Chart {
    private static final int BAR_WIDTH = 70;
    private final SVGGraphics2D g;
    private final int canvasHeight;
    private final int canvasWidth;
    private int labelSize = 24;

    public BarChart(SVGGraphics2D g) {
        this.g = g;
        this.canvasHeight = g.getSVGCanvasSize().height;
        this.canvasWidth = g.getSVGCanvasSize().width;

        g.setPaint(Color.BLACK);
        g.fill(new Rectangle2D.Double(0, 0, canvasWidth, canvasWidth));

        // --- Draw section titles ---
        g.setPaint(Color.ORANGE);
        g.setFont(new Font("Arial", Font.BOLD, 24));

    }

    @Override
    public void draw(List<Datapoint> data) {

        // --- Draw section titles ---
        g.setPaint(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, labelSize));

        double maxValue = data.stream().map(Datapoint::value).max(Double::compare).orElse(1.0);

        int barSpacing = canvasHeight / data.size() - BAR_WIDTH;
        int y = barSpacing / 2;

        int labelAllowance = 250;
        int leftMargin = 20;
        int barWidth = canvasWidth - 2 * labelAllowance;
        for (Datapoint d : data) {
            g.setPaint(Color.WHITE);
            double val = d.value();
            int x = labelAllowance - 40; // Fudge factor for asymmetric margins
            double scale = barWidth / maxValue;
            int length = (int) (val * scale);
            g.fillRect(x, y, length, BAR_WIDTH);
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
