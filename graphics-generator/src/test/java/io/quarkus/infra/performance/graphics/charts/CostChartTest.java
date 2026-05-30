package io.quarkus.infra.performance.graphics.charts;

import java.util.function.Function;

import io.quarkus.infra.performance.graphics.CostPlotDefinition;
import io.quarkus.infra.performance.graphics.PlotDefinition;
import io.quarkus.infra.performance.graphics.model.BenchmarkData;
import io.quarkus.infra.performance.graphics.model.Ec2Pricing;
import io.quarkus.infra.performance.graphics.model.Result;
import io.quarkus.infra.performance.graphics.model.units.DimensionalNumber;

public class CostChartTest extends LoadDensityChartTest {

    private static final Ec2Pricing TEST_PRICING = new Ec2Pricing(
            "c6i.xlarge", "us-east-1", 4, 8, 0.170, "USD", "2026-05-30");

    @Override
    protected CostChart createChart(PlotDefinition plotDefinition, BenchmarkData data) {
        return new CostChart(plotDefinition, data);
    }

    @Override
    protected PlotDefinition createPlotDefinition() {
        Function<Result, ? extends DimensionalNumber> throughputFun = framework -> framework.load().avThroughput();
        Function<Result, ? extends DimensionalNumber> rssFun = framework -> framework.rss().avFirstRequestRss();
        return new CostPlotDefinition("test cost", "test-cost", "some subtitle",
                7168, 200_000, throughputFun, rssFun, TEST_PRICING);
    }
}
