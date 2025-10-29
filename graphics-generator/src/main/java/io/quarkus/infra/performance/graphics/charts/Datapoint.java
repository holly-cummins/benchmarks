package io.quarkus.infra.performance.graphics.charts;

import io.quarkus.infra.performance.graphics.model.Framework;

public record Datapoint(Framework framework, double value) {

}
