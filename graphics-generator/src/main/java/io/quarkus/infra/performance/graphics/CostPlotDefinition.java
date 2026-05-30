package io.quarkus.infra.performance.graphics;

import java.util.function.Function;

import io.quarkus.infra.performance.graphics.model.Ec2Pricing;
import io.quarkus.infra.performance.graphics.model.Result;
import io.quarkus.infra.performance.graphics.model.units.DimensionalNumber;

public record CostPlotDefinition(String title, String filename, String subtitle,
                                  int schedulableMemoryMiB, double maxLoadTps,
                                  Function<Result, ? extends DimensionalNumber> throughputFun,
                                  Function<Result, ? extends DimensionalNumber> rssFun,
                                  Ec2Pricing pricing) implements LoadDensityPlotFields {
}
