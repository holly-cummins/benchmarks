package io.quarkus.infra.performance.graphics.model;

import java.util.List;

import io.quarkus.infra.performance.graphics.model.units.Memory;

public record NativeBuild(
        List<Memory> rss,
        Memory binarySize) {
}
