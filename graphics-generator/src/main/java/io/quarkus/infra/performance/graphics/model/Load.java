package io.quarkus.infra.performance.graphics.model;

import java.util.List;

import io.quarkus.infra.performance.graphics.model.units.Memory;
import io.quarkus.infra.performance.graphics.model.units.TransactionsPerMb;
import io.quarkus.infra.performance.graphics.model.units.TransactionsPerSecond;

public record Load(
        List<TransactionsPerSecond> throughput,
        List<Memory> rss,
        List<TransactionsPerMb> throughputDensity,
        TransactionsPerSecond avThroughput,
        Memory avMaxRss,
        TransactionsPerMb maxThroughputDensity) {
}
