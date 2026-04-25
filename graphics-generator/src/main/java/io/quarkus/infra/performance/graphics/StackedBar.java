package io.quarkus.infra.performance.graphics;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import io.quarkus.infra.performance.graphics.charts.Bar;
import io.quarkus.infra.performance.graphics.charts.Datapoint;
import io.quarkus.infra.performance.graphics.charts.Label;
import io.quarkus.infra.performance.graphics.charts.LabelGroup;
import io.quarkus.infra.performance.graphics.charts.ScaleGroup;
import io.quarkus.infra.performance.graphics.charts.Subcanvas;
import io.quarkus.infra.performance.graphics.charts.fonts.Alignment;
import io.quarkus.infra.performance.graphics.charts.fonts.VAlignment;
import io.quarkus.infra.performance.graphics.model.Category;

import static io.quarkus.infra.performance.graphics.charts.Datapoint.ascending;

public class StackedBar extends Bar {

    // Inlined labelled look better if they're snugged a bit closer to the end of the bar
    private static final int SMALL_LABEL_PADDING = 3;
    public static final int STACK_OFFSET = 4;
    private List<Datapoint> ds;
    private Map<Datapoint, Label> valueLabels;

    public StackedBar(Category key, Collection<Datapoint> values, LabelGroup frameworkLabelGroup, LabelGroup valueLabelGroup, ScaleGroup scaleGroup) {
        // We know the collection must have at least one entry, so there will be a max
        super(values.stream().max(ascending()).orElse(values.iterator().next()), frameworkLabelGroup, valueLabelGroup, scaleGroup, key.prettyName(), "");

        // Use a smaller target height since we think there will only be one row
        frameworkLabel.setTargetHeight(BAR_THICKNESS / 2);

        // Sort, so that the bars never overwrite each other
        ds = values.stream().sorted(Datapoint.ascending().reversed()).collect(Collectors.toUnmodifiableList());
        valueLabels = new HashMap<>();
        for (Datapoint d : ds) {
            // TODO this can be simpler when we have frameworks and modes
            Label label = new Label(d.framework().getExpandedName().split("\n")[0] + "\n" + d.prettyValue(), valueLabelGroup);
            label.setHorizontalAlignment(Alignment.RIGHT);
            label.setVerticalAlignment(VAlignment.MIDDLE);
            valueLabels.put(d, label);
        }
    }

    @Override
    public int getMinimumVerticalSize() {
        return MINIMUM_BAR_THICKNESS + barSpacing + (ds.size() - 1) * STACK_OFFSET;
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
            y += STACK_OFFSET;
        }
    }

    @Override
    protected void drawValueLabel(Subcanvas barSubcanvas, Theme theme) {
        int previousPosition = Integer.MAX_VALUE;
        int y = barSubcanvas.getHeight() / 2;
        int i = 0;
        for (Datapoint sd : ds) {
            barSubcanvas.setPaint(theme.annotationText(sd.framework()));
            double val = sd.value().getValue();
            int length = (int) (val * scaleGroup.getScale());
            Label label = valueLabels.get(sd);
            label.setTargetHeight(BAR_THICKNESS * 2 / 3);

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

            previousPosition = x - label.calculateWidth();
            y += STACK_OFFSET;
            i++;
        }
    }

}
