package io.quarkus.infra.performance.graphics;

import java.util.function.Function;

import io.quarkus.infra.performance.graphics.model.Result;
import io.quarkus.infra.performance.graphics.model.units.DimensionalNumber;

public interface LoadDensityPlotFields extends PlotDefinition {
    double maxLoadTps();

    Function<Result, ? extends DimensionalNumber> throughputFun();
}
