package io.quarkus.infra.performance.graphics.model;

public record BenchmarkData(
        Timing timing, Results results, Config config) {

}
