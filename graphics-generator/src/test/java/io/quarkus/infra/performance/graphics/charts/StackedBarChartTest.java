package io.quarkus.infra.performance.graphics.charts;

import io.quarkus.infra.performance.graphics.PlotDefinition;
import io.quarkus.infra.performance.graphics.StackedBarChart;
import io.quarkus.infra.performance.graphics.model.BenchmarkData;
import io.quarkus.infra.performance.graphics.model.KnownFramework;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class StackedBarChartTest extends BarChartTest {

    @Override
    protected StackedBarChart createChart(PlotDefinition plotDefinition, BenchmarkData data) {
        return new StackedBarChart(plotDefinition, data);
    }


    @Test
    public void testMinimumDimensionsAreSmallerThanForBarChart() {
        BenchmarkData data = mockBenchmarkData(KnownFramework.values().length);
        PlotDefinition plotDefinition = createPlotDefinition();

        StackedBarChart stackedChart = createChart(plotDefinition, data);
        BarChart barChart = super.createChart(plotDefinition, data);

        int barHeight = barChart.getMinimumVerticalSize();
        int stackedHeight = stackedChart.getMinimumVerticalSize();
        assertTrue(stackedHeight < barHeight - 200, "Stacking did not reduce height enough: " + stackedHeight + " vs " + barHeight);
    }
}