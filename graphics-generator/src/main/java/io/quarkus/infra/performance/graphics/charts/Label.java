package io.quarkus.infra.performance.graphics.charts;

import java.awt.Font;
import java.awt.FontMetrics;
import java.util.Arrays;
import java.util.Comparator;

import io.quarkus.infra.performance.graphics.Theme;
import io.quarkus.infra.performance.graphics.VAlignment;

public class Label {

    private static final int DEFAULT_TARGET_HEIGHT = 24;

    private final String[] strings;
    private int targetHeight = 24; // Arbitrary default
    private final double lineSpacing = 1;
    private int style = Font.PLAIN;
    private Alignment alignment = Alignment.LEFT;
    private VAlignment valignment = VAlignment.MIDDLE;
    private int lineHeight;
    private FontMetrics fontMetrics;
    private Font font;

    /**
     * @param text Use \n for multiline text
     */
    public Label(String text) {
        this(text.split("\n"));

    }

    public Label(String[] lines) {
        this.strings = lines;
        setTargetHeight(DEFAULT_TARGET_HEIGHT);
    }

    public void draw(Subcanvas g) {
        draw(g, 0, 0);
    }

    public void draw(Subcanvas g, int x, int y) {

        g.getGraphics().setFont(font);

        // The SVG attribute alignment-baseline="middle" is not supported by Batik.
        // The value we pass in to drawString is the position of the bottom baseline

        // Four variables describe the height of a font: leading (pronounced like the metal), ascent, descent, and height. Leading is the amount of space required between lines of the same font. Ascent is the space above the baseline required by the tallest character in the font. Descent is the space required below the baseline by the lowest descender (the "tail" of a character like "y"). Height is the total of the three: ascent, baseline, and descent. 

        // Should be the same as the targetHeight, but recalculate in case of rounding errors
        fontMetrics = g.getGraphics().getFontMetrics();
        lineHeight = (int) (fontMetrics.getHeight() * lineSpacing);
        int textBlockHeight = lineHeight * strings.length;

        // Compute starting y to vertically center the text block
        int yPosition = switch (valignment) {
            case TOP -> y + g.getGraphics().getFontMetrics().getAscent();
            case BOTTOM -> y - getAscent();
            case MIDDLE -> y - textBlockHeight / 2 + getAscent();
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

    int getAscent() {
        return fontMetrics.getAscent();
    }

    // We will need to have a labelGroup abstraction to keep sizes consistent, and perhaps also set a maximum width
    public Label setTargetHeight(int height) {
        this.targetHeight = height;
        int size = strings.length > 1 ? Sizer.calculateFontSize((int) (targetHeight / (strings.length * lineSpacing)))
                : Sizer.calculateFontSize(targetHeight);
        font = new Font(Theme.FONT.getName(), style, size);
        return this;
    }

    public int getTargetHeight() {
        return targetHeight;
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

    public int getLineHeight() {
        return lineHeight;
    }

    public int calculateWidth(String s) {

        if (fontMetrics != null) {
            return fontMetrics.stringWidth(s);
        } else {
            return Sizer.calculateWidth(s, font.getSize());
        }
    }

    public int getDescent() {
        return fontMetrics.getDescent();
    }

    public int calculateWidth() {
        return calculateWidth(getLongestText());
    }

    private String getLongestText() {
        // It would be cheaper to just count characters, but sometimes the longest string isn't the fattest – including for framework labels we are using
        return Arrays.stream(strings).max(Comparator.comparingInt(this::calculateWidth)).orElse("");
    }

    public String toString() {
        return "Label[" + getLongestText() + "]";
    }
}
