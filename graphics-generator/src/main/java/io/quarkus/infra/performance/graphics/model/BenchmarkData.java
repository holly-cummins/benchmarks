package io.quarkus.infra.performance.graphics.model;

public record BenchmarkData(
        Timing timing, Results results, Config config, Group group) {

    public BenchmarkData(Timing timing, Results results, Config config) {
        this(timing, results, config, Group.ALL);
    }

    public BenchmarkData subgroup(Group group) {
        return new BenchmarkData(timing, results != null ? results.subgroup(group):null, config, group);
    }

    public Group group() {
        return group == null ? Group.ALL:group;
    }

}
