package io.quarkus.infra.performance.graphics.charts;

import java.awt.Color;

import org.apache.batik.svggen.SVGGraphics2D;

// we won't implement all of Graphics2D, since we only use a few methods
public class Subcanvas {
    private final SVGGraphics2D g;
    private final int width;
    private final int height;
    private final int xOffset;
    private final int yOffset;

    public Subcanvas(SVGGraphics2D g, int width, int height, int xOffset, int yOffset) {
        this.g = g;
        this.width = width;
        this.height = height;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
    }

    public Subcanvas(Subcanvas subcanvas, int width, int height, int xOffset, int yOffset) {
        this.g = subcanvas.g;
        this.width = width;
        this.height = height;
        this.xOffset = subcanvas.xOffset + xOffset;
        this.yOffset = subcanvas.yOffset + yOffset;
    }

    public void setPaint(Color color) {
        g.setPaint(color);
    }

    public void fillRect(int x, int y, int width, int height) {
        g.fillRect(x + xOffset, y + yOffset, width, height);
    }

    public void drawString(String name, int x, int y) {
        g.drawString(name, x + xOffset, y + yOffset);
    }

    // Access to the underlying graphics object, when we don't have the right abstracted method in place
    public SVGGraphics2D getGraphics() {
        return g;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    // This shouldn't need to be used much
    public int getXOffset() {
        return xOffset;
    }

    // This shouldn't need to be used much
    public int getYOffset() {
        return yOffset;
    }
}
