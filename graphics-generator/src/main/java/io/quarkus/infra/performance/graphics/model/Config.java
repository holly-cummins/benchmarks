package io.quarkus.infra.performance.graphics.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Config(
        Jvm jvm,

        @JsonProperty("CMD_PREFIX") String cmdPrefix,

        @JsonProperty("num_iterations") int numIterations,

        FrameworkBuild quarkus,

        @JsonProperty("repo") Repo repo,

        @JsonProperty("profiler") Profiler profiler,

        Resources resources,

        // Present for backwards compatibility
        FrameworkBuild springboot,

        FrameworkBuild springboot3,

        FrameworkBuild springboot4

) {
}
