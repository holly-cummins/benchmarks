package io.quarkus.infra.performance.graphics.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.quarkus.infra.performance.graphics.model.units.Memory;

public record Cgroup(
        @JsonProperty("mem_max") Memory memMax,
        String name,
        String cpu) {
}