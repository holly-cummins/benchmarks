package io.quarkus.infra.performance.graphics;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import io.quarkus.infra.performance.graphics.charts.BarChart;
import io.quarkus.infra.performance.graphics.charts.Datapoint;
import io.quarkus.infra.performance.graphics.model.BenchmarkData;
import io.quarkus.infra.performance.graphics.model.Category;

public class StackedBarChart extends BarChart {

    public StackedBarChart(PlotDefinition plotDefinition, BenchmarkData bmData) {
        super(plotDefinition, bmData);
    }

    public StackedBarChart(PlotDefinition plotDefinition, BenchmarkData bmData, boolean isEmbedded) {
        super(plotDefinition, bmData, isEmbedded);
    }

    @Override
    protected void initialiseBars() {

        int partitionCount = countPartitions(data) + 1;
        int averagePartitionSize = data.size() / partitionCount;
        // Do an extra check, so we don't get stupidly small partitions; technically maybe we'd prefer to check the ratio

        Map<Category, Collection<Datapoint>> groupedBars = new HashMap<>();

        // TODO should preserve order

        for (Datapoint d : data) {
            Category newCategory = d.framework().getPartitionableCategory();
            Collection set = groupedBars.get(newCategory);
            if (set == null) {
                set = new ArrayList<Datapoint>();
                groupedBars.put(newCategory, set);
            }
            set.add(d);
        }

        for (Map.Entry<Category, Collection<Datapoint>> entry : groupedBars.entrySet()) {


            StackedBar e = new StackedBar(entry.getKey(), entry.getValue(), frameworkLabelGroup, valueLabelGroup, scaleGroup);
            bars.add(e);
            barsAndPartitions.add(e);
            children.add(e);
        }
    }
}


