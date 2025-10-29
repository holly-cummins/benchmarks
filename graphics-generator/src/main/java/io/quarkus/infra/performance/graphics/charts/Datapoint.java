package io.quarkus.infra.performance.graphics.charts;

import io.quarkus.infra.performance.graphics.model.Framework;
import io.quarkus.infra.performance.graphics.model.units.DimensionalNumber;

public record Datapoint(Framework framework, DimensionalNumber value) {

}
