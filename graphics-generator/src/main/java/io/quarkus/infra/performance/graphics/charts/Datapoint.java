package io.quarkus.infra.performance.graphics.charts;

import java.util.Comparator;

import io.quarkus.infra.performance.graphics.model.Framework;
import io.quarkus.infra.performance.graphics.model.units.DimensionalNumber;

public record Datapoint(Framework framework, DimensionalNumber value) {

    public static Comparator<Datapoint> ascending() {
        return (d1, d2) -> (int) (d1.value().getValue() - d2.value().getValue());
    }

    public String prettyValue() {
        return String.format("%d %s", Math.round(value.getValue()), value.getUnits());
    }
}
