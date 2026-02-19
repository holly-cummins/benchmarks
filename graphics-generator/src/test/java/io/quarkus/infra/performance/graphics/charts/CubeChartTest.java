package io.quarkus.infra.performance.graphics.charts;

import io.quarkus.infra.performance.graphics.PlotDefinition;
import io.quarkus.infra.performance.graphics.model.BenchmarkData;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class CubeChartTest extends ChartTest {

    @Override
    protected CubeChart createChart(PlotDefinition plotDefinition, BenchmarkData data) {
        return new CubeChart(plotDefinition, data.results().getDatasets(plotDefinition.fun()), data);
    }

    @Override
    @Disabled
    @Test
    public void testCanDrawSmallGroupInMinimumDimensions() {

    }

    @Override
    @Disabled
    @Test
    public void testCanDrawLargeGroupInMinimumDimensions() {

    }

    @Override
    @Disabled
    @Test
    public void testCanDrawLargeGroupInPreferredDimensions() {

    }

    @Override
    @Disabled
    @Test
    public void testCanDrawEmptyGroupInPreferredDimensions() {

    }


}
