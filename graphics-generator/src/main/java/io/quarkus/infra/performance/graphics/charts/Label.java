package io.quarkus.infra.performance.graphics.charts;

import java.awt.Font;
import java.awt.FontMetrics;

import io.quarkus.infra.performance.graphics.Theme;
import io.quarkus.infra.performance.graphics.VAlignment;

public class Label {

    private final String text;
    private final int x;
    private final int y;
    private int targetHeight = 24; // Arbitrary default
    private double lineSpacing = 1;
    // The g.getGraphics().getFontMetrics().getHeight() number is 39-40% bigger than the notional font size; use it to estimate what font size we might need to set
    private static final double realHeightRatio = 1.4;
    private int style = Font.PLAIN;
    private Alignment alignment = Alignment.LEFT;
    private VAlignment valignment = VAlignment.MIDDLE;

    /**
     * @param text Use \n for multiline text
     * @param x
     * @param y The y position of the center of the text
     */
    public Label(String text, int x, int y) {
        this.text = text;
        this.x = x;
        this.y = y;
    }

    public void draw(Subcanvas g) {
        String[] strings = text.split("\n");
        int size = strings.length > 1 ? (int) (targetHeight / (strings.length * lineSpacing * realHeightRatio))
                : (int) (targetHeight / (realHeightRatio));
        Font font = new Font(Theme.FONT.getName(), style, size);
        g.getGraphics().setFont(font);

        // The SVG attribute alignment-baseline="middle" is not supported by Batik.
        // The value we pass in to drawString is the position of the bottom baseline

        // Four variables describe the height of a font: leading (pronounced like the metal), ascent, descent, and height. Leading is the amount of space required between lines of the same font. Ascent is the space above the baseline required by the tallest character in the font. Descent is the space required below the baseline by the lowest descender (the "tail" of a character like "y"). Height is the total of the three: ascent, baseline, and descent. 

        // Should be the same as the targetHeight, but recalculate in case of rounding errors
        FontMetrics fontMetrics = g.getGraphics().getFontMetrics();
        int lineHeight = (int) (fontMetrics.getHeight() * lineSpacing);
        int textBlockHeight = lineHeight * strings.length;

        // Compute starting y to vertically center the text block
        int yPosition = switch (valignment) {
            case TOP -> y + g.getGraphics().getFontMetrics().getHeight();
            case BOTTOM -> y - fontMetrics.getAscent();
            case MIDDLE -> y - textBlockHeight / 2 + fontMetrics.getAscent();
        };

        for (String string : strings) {
            int width = fontMetrics.stringWidth(string);
            int alignedX = switch (alignment) {
                case LEFT -> x;
                case RIGHT -> x - width;
                case CENTER -> x - width / 2;
            };
            g.drawString(string, alignedX, yPosition);
            yPosition += lineHeight;
        }

    }

    // We will need to have a labelGroup abstraction to keep sizes consistent, and perhaps also set a maximum width
    public Label setTargetHeight(int height) {
        this.targetHeight = height;
        return this;
    }

    public Label setHorizontalAlignment(Alignment alignment) {
        this.alignment = alignment;
        return this;
    }

    /**
     * Should be one of the constants declared in #Font or their combination
     */
    public Label setStyle(int style) {
        this.style = style;
        return this;
    }

    public Label setVerticalAlignment(VAlignment vAlignment) {
        this.valignment = vAlignment;
        return this;
    }
}
