package io.quarkus.infra.performance.graphics.charts;

import io.quarkus.infra.performance.graphics.Theme;
import io.quarkus.infra.performance.graphics.charts.fonts.Alignment;
import io.quarkus.infra.performance.graphics.charts.fonts.FontStyle;
import io.quarkus.infra.performance.graphics.charts.fonts.Sizer;
import io.quarkus.infra.performance.graphics.charts.fonts.VAlignment;

import static io.quarkus.infra.performance.graphics.charts.fonts.FontStyle.BOLD;
import static io.quarkus.infra.performance.graphics.charts.fonts.FontStyle.PLAIN;

public class Bar extends ScaledElement {
    protected static final int BAR_THICKNESS = 44;
    protected static final int MINIMUM_BAR_THICKNESS = 44;
    protected static final int MAXIMUM_BAR_THICKNESS = 44;
    private static final int MINIMUM_BAR_LENGTH = 200;

    public static final int LEFT_LABEL_SIZE = Sizer.calculateFontSize(BAR_THICKNESS / 2);
    public static final int LABEL_PADDING = 12;

    protected static final int barSpacing = 12;

    protected final Label valueLabel;
    protected final Label frameworkLabel;
    protected final Datapoint d;

    private final String frameworkLabelText;

    public Bar(Datapoint d, LabelGroup frameworkLabelGroup, LabelGroup valueLabelGroup, ScaleGroup scaleGroup) {
        this(d, frameworkLabelGroup, valueLabelGroup, scaleGroup, d.framework().getExpandedName(), d.prettyValue());
    }

    protected Bar(Datapoint d, LabelGroup frameworkLabelGroup, LabelGroup valueLabelGroup, ScaleGroup scaleGroup, String frameworkLabelText, String valueLabelText) {
        super(scaleGroup);
        this.d = d;
        this.frameworkLabelText = frameworkLabelText;
        frameworkLabel = new Label(frameworkLabelText, frameworkLabelGroup)
                .setHorizontalAlignment(Alignment.RIGHT)
                .setVerticalAlignment(VAlignment.MIDDLE)
                .setStyles(new FontStyle[]{BOLD, PLAIN})
                .setTargetHeight(BAR_THICKNESS);
        valueLabel = new Label(valueLabelText, valueLabelGroup).setStyle(BOLD).setTargetHeight(getLabelTargetHeight());

        // This will probably be overridden, but set a value
        offset = Sizer.calculateWidth(frameworkLabelText, LEFT_LABEL_SIZE) + LABEL_PADDING;
    }

    @Override
    public int getMaximumVerticalSize() {
        return MAXIMUM_BAR_THICKNESS + barSpacing;
    }

    @Override
    public int getMaximumHorizontalSize() {
        // Arbitrary; we can go big, but this will contribute to the preferred size
        return getMinimumHorizontalSize() + 1000;
    }

    @Override
    public int getMinimumVerticalSize() {
        return MINIMUM_BAR_THICKNESS + barSpacing;
    }

    @Override
    public int getMinimumHorizontalSize() {
        return offset + MINIMUM_BAR_LENGTH + getValueLabelWidth() + LABEL_PADDING;
    }

    protected int getValueLabelWidth() {
        return valueLabel.calculateWidth();
    }

    public String getLeftLabelText() {
        return frameworkLabelText;
    }

    @Override
    public void draw(Subcanvas barArea, Theme theme) {
        // Vertically align text with the centre of the bars
        int labelY = barArea.getHeight() / 2;

        barArea.setPaint(theme.text());
        int leftLabelWidth = offset - LABEL_PADDING;

        Subcanvas frameworkSubcanvas = new Subcanvas(barArea, leftLabelWidth, frameworkLabel.getTargetHeight(), 0, 0);
        frameworkLabel.draw(frameworkSubcanvas, leftLabelWidth, labelY);
        int y = (barArea.getHeight() - BAR_THICKNESS) / 2;
        Subcanvas barSubcanvas = new Subcanvas(barArea, barArea.getWidth() - offset, Math.max(frameworkLabel.getTargetHeight(), BAR_THICKNESS), offset, y);

        drawBar(theme, barSubcanvas);
        drawValueLabel(barSubcanvas, theme);
    }

    protected void drawValueLabel(Subcanvas barSubcanvas, Theme theme) {
        int labelY = barSubcanvas.getHeight() / 2;
        int length = (int) (d.value().getValue() * scaleGroup.getScale());

        valueLabel.setTargetHeight(getLabelTargetHeight()).draw(barSubcanvas, length + LABEL_PADDING, labelY);
    }

    protected void drawBar(Theme theme, Subcanvas barSubcanvas) {
        double val = d.value().getValue();
        // If this framework isn't found, it will just be the text colour, which is fine
        barSubcanvas.setPaint(theme.chartElements().get(d.framework()));
        int length = (int) (val * scaleGroup.getScale());
        barSubcanvas.fillRect(0, 0, length, BAR_THICKNESS);

        barSubcanvas.setPaint(theme.text());
    }

    public int getMaximumBarWidth(Subcanvas barArea) {
        return barArea.getWidth() - offset - LABEL_PADDING - getValueLabelWidth();
    }

    public double getMaximumScale(Subcanvas barArea) {
        return getMaximumBarWidth(barArea) / d.value().getValue();
    }

    protected static int getLabelTargetHeight() {
        return BAR_THICKNESS * 2 / 3;
    }
}
