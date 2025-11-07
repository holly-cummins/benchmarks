package io.quarkus.infra.performance.graphics.model;

import java.util.List;

import io.quarkus.infra.performance.graphics.model.units.Milliseconds;
import io.quarkus.infra.performance.graphics.model.units.Seconds;

public record Startup(
        List<Seconds> timings,
        Milliseconds avStartTime) {
}