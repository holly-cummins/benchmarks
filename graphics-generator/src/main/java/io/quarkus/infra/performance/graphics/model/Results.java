package io.quarkus.infra.performance.graphics.model;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import com.fasterxml.jackson.annotation.JsonAnySetter;

import io.quarkus.infra.performance.graphics.charts.Datapoint;
import io.quarkus.infra.performance.graphics.model.units.DimensionalNumber;

public class Results {

    private final Map<Framework, Result> frameworks = new EnumMap<>(Framework.class);

    @JsonAnySetter
    public void addFramework(String key, Result result) {
        try {
            Framework type = Framework.valueOfIgnoreCase(key);
            frameworks.put(type, result);
        } catch (IllegalArgumentException e) {
            // Optionally log or ignore unknown frameworks
        }
    }

    public Result framework(Framework type) {
        return frameworks.get(type);
    }

    public List<Datapoint> getDatasets(Function<Result, ? extends DimensionalNumber> fun) {
        return frameworks.entrySet().stream().map(e -> new Datapoint(e.getKey(), fun.apply(e.getValue())))
                .toList();

    }
}
