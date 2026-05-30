package io.quarkus.infra.performance.graphics;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.quarkus.infra.performance.graphics.charts.Bar;
import io.quarkus.infra.performance.graphics.charts.Datapoint;
import io.quarkus.infra.performance.graphics.charts.Label;
import io.quarkus.infra.performance.graphics.charts.LabelGroup;
import io.quarkus.infra.performance.graphics.charts.ScaleGroup;
import io.quarkus.infra.performance.graphics.charts.Subcanvas;
import io.quarkus.infra.performance.graphics.charts.fonts.Alignment;
import io.quarkus.infra.performance.graphics.charts.fonts.FontStyle;
import io.quarkus.infra.performance.graphics.charts.fonts.VAlignment;
import io.quarkus.infra.performance.graphics.model.Category;

import static io.quarkus.infra.performance.graphics.charts.Datapoint.ascending;
import static io.quarkus.infra.performance.graphics.charts.fonts.FontStyle.BOLD;
import static io.quarkus.infra.performance.graphics.charts.fonts.FontStyle.PLAIN;

public class StackedBar extends Bar {

    // Inlined labelled look better if they're snugged a bit closer to the end of the bar
    private static final int SMALL_LABEL_PADDING = 3;
    private static final int VALUE_LABEL_PADDING = 10;
    private final List<Datapoint> ds;
    private final Map<Datapoint, Label> valueLabels;

    public StackedBar(Category key, Collection<Datapoint> values, LabelGroup frameworkLabelGroup, LabelGroup valueLabelGroup, ScaleGroup scaleGroup) {
        // We know the collection must have at least one entry, so there will be a max
        super(values.stream().max(ascending()).orElse(values.iterator().next()), frameworkLabelGroup, valueLabelGroup, scaleGroup, key.prettyName(), "");

        // Use a smaller target height since we think there will only be one row
        frameworkLabel.setTargetHeight(BAR_THICKNESS / 2);

        // Sort, so that the bars never overwrite each other
        ds = values.stream().sorted(Datapoint.ascending().reversed()).toList();
        valueLabels = new HashMap<>();
        for (Datapoint d : ds) {
            // TODO this can be simpler when we have frameworks and modes
            Label label = new Label(d.framework().getExpandedName().split("\n")[0] + "\n" + d.prettyValue(), valueLabelGroup)
                    .setHorizontalAlignment(Alignment.RIGHT)
                    .setVerticalAlignment(VAlignment.TOP)
                    .setStyles(new FontStyle[]{BOLD, PLAIN});

            valueLabels.put(d, label);
        }
    }

    @Override
    public int getMaximumVerticalSize() {
        return MAXIMUM_BAR_THICKNESS + barSpacing + getLabelTargetHeight() + VALUE_LABEL_PADDING;
    }

    @Override
    public int getMinimumVerticalSize() {
        return MINIMUM_BAR_THICKNESS + barSpacing + getLabelTargetHeight() + VALUE_LABEL_PADDING;
    }

    @Override
    protected void drawBar(Theme theme, Subcanvas barSubcanvas) {
        int y = 0;
        for (Datapoint sd : ds) {
            double val = sd.value().getValue();

            // If this framework isn't found, it will just be the text colour, which is fine
            barSubcanvas.setPaint(theme.chartElements().get(sd.framework()));
            int length = (int) (val * scaleGroup.getScale());
            barSubcanvas.fillRect(0, y, length, BAR_THICKNESS);
            barSubcanvas.setPaint(theme.text());
        }
    }

    protected void drawBarTerminator(Theme theme, Subcanvas barSubcanvas, double val) {
        // If this framework isn't found, it will just be the text colour, which is fine
        barSubcanvas.setPaint(theme.text());
        int x = (int) (val * scaleGroup.getScale());

        barSubcanvas.drawLine(x, 0, x, BAR_THICKNESS + getLabelTargetHeight() + VALUE_LABEL_PADDING);

        barSubcanvas.setPaint(theme.text());
    }

    @Override
    protected void drawValueLabel(Subcanvas barSubcanvas, Theme theme) {
        int previousPosition = Integer.MAX_VALUE;
        int y = barSubcanvas.getHeight() + VALUE_LABEL_PADDING;
        int i = 0;
        for (Datapoint sd : ds) {
            double val = sd.value().getValue();
            int length = (int) (val * scaleGroup.getScale());
            Label label = valueLabels.get(sd);
            label.setTargetHeight(getLabelTargetHeight());

            // Always give the first bar full padding
            int padding = i == 0 ? SMALL_LABEL_PADDING:LABEL_PADDING;
            int x = length - padding;

            // If we don't fit without overlapping with the previous text, left align instead
            if (x > previousPosition) {
                double nextVal = i < ds.size() - 1 ? ds.get(i + 1).value().getValue():0;
                x = (int) (nextVal * scaleGroup.getScale());
                x += LABEL_PADDING;
                label.setHorizontalAlignment(Alignment.LEFT);
            }
            label.draw(barSubcanvas, x, y);
            drawBarTerminator(theme, barSubcanvas, val);

            previousPosition = x - label.calculateWidth();
            i++;
        }
    }

}
