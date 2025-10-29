package io.quarkus.infra.performance.graphics.model;

import java.util.List;

import io.quarkus.infra.performance.graphics.model.units.Memory;

public record Rss(
        List<Memory> startup,
        List<Memory> firstRequest,
        Memory avStartupRss,
        Memory avFirstRequestRss) {
}
