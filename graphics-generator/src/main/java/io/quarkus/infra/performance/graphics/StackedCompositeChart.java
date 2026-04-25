package io.quarkus.infra.performance.graphics;

import io.quarkus.infra.performance.graphics.charts.Chart;
import io.quarkus.infra.performance.graphics.charts.CompositeChart;
import io.quarkus.infra.performance.graphics.charts.CubeChart;
import io.quarkus.infra.performance.graphics.model.BenchmarkData;

public class StackedCompositeChart extends CompositeChart {
    public StackedCompositeChart(PlotDefinition plotDefinition, BenchmarkData bmData) {
        super(plotDefinition, bmData);
    }

    protected Chart getChartForPlotDefinition(BenchmarkData bmData, PlotDefinition pd) {
        Chart chart;
        if (pd.title().contains("Memory")) {
            chart = new CubeChart(pd, bmData, true);
        } else {
            chart = new StackedBarChart(pd, bmData, true);
        }
        return chart;
    }
}
