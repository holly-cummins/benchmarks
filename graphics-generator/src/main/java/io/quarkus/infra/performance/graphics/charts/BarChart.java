package io.quarkus.infra.performance.graphics.charts;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import io.quarkus.infra.performance.graphics.PlotDefinition;
import io.quarkus.infra.performance.graphics.Theme;
import io.quarkus.infra.performance.graphics.model.BenchmarkData;
import io.quarkus.infra.performance.graphics.model.Category;

public class BarChart extends Chart {

    private static final int MINIMUM_PARTITION_SIZE = 2;
    private final FinePrint fineprint;
    private final List<Bar> bars = new ArrayList<>();
    private final List<ElasticElement> barsAndPartitions = new ArrayList<>();

    public BarChart(PlotDefinition plotDefinition, List<Datapoint> data, BenchmarkData bmData) {
        super(plotDefinition, data, bmData);

        this.fineprint = new FinePrint(bmData);
        children.add(fineprint);

        Category previousCategory = null;
        int partitions = countPartitions(data) + 1;
        int averagePartitionSize = data.size() / partitions;
        // Do an extra check, so we don't get stupidly small partitions; technically maybe we'd prefer to check the ratio
        boolean shouldUsePartitions = (averagePartitionSize >= MINIMUM_PARTITION_SIZE);

        for (Datapoint d : data) {
            Category newCategory = d.framework().getPartitionableCategory();
            if (shouldUsePartitions) {
                if (newCategory != null && previousCategory != null && ! newCategory.equals(previousCategory)) {
                    Divider e1 = new Divider();
                    barsAndPartitions.add(e1);
                    children.add(e1);
                }
            }

            Bar e = new Bar(d);
            bars.add(e);
            barsAndPartitions.add(e);
            children.add(e);
            previousCategory = newCategory;
        }

    }

    private int countPartitions(List<Datapoint> data) {
        Category previousCategory = null;
        int partitions = 0;
        for (Datapoint d : data) {
            Category newCategory = d.framework().getPartitionableCategory();
            if (newCategory != null && previousCategory != null && ! newCategory.equals(previousCategory)) {
                partitions++;
            }
            previousCategory = newCategory;
        }
        return partitions;
    }

    @Override
    protected void drawNoCheck(Subcanvas canvasWithMargins, Theme theme) {

        int finePrintHeight = fineprint.getPreferredVerticalSize();

        canvasWithMargins.setPaint(theme.text());
        Subcanvas titleCanvas = new Subcanvas(canvasWithMargins, canvasWithMargins.getWidth(), title.getPreferredVerticalSize(),
                0, 0);
        title.draw(titleCanvas, theme);

        Subcanvas barArea = new Subcanvas(canvasWithMargins, canvasWithMargins.getWidth(),
                canvasWithMargins.getHeight() - titleCanvas.getHeight() - finePrintHeight, 0, titleCanvas.getHeight());

        int leftLabelWidth = Sizer.calculateWidth(bars.stream().map(Bar::getLeftLabelText).collect(Collectors.toSet()),
                Bar.LEFT_LABEL_SIZE);

        // Set a common left label width before trying to calculate a scale
        for (Bar bar : bars) {
            bar.setLeftLabelWidth(leftLabelWidth);
        }

        double scale = bars.stream().mapToDouble(bar -> bar.getMaximumScale(barArea)).min().orElse(1);

        int y = 0;

        canvasWithMargins.setPaint(theme.text());

        for (Bar bar : bars) {
            bar.setScale(scale);
        }

        for (ElasticElement bar : barsAndPartitions) {
            // TODO slight hack, assuming the min and max are always equal
            Subcanvas individualBarArea = new Subcanvas(barArea, barArea.getWidth(), bar.getMaximumVerticalSize(), 0, y);
            bar.draw(individualBarArea, theme);
            y += individualBarArea.getHeight();

        }

        drawFinePrint(canvasWithMargins, theme, finePrintHeight, barArea.getHeight() + titleCanvas.getHeight(), fineprint);
    }

    @Override
    public Collection<InlinedSVG> getInlinedSVGs() {
        return fineprint.getInlinedSVGs();
    }

}
