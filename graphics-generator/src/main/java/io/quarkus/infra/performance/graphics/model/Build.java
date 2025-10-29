package io.quarkus.infra.performance.graphics.model;

import java.util.List;

import io.quarkus.infra.performance.graphics.model.units.ClassCount;
import io.quarkus.infra.performance.graphics.model.units.FieldCount;
import io.quarkus.infra.performance.graphics.model.units.Memory;
import io.quarkus.infra.performance.graphics.model.units.MethodCount;
import io.quarkus.infra.performance.graphics.model.units.Seconds;

public record Build(
        List<Seconds> timings,
        NativeBuild nativeBuild, // maps the "native" JSON object,
        Seconds avBuildTime,
        Memory avNativeRSS, // optional,
        ClassCount classCount,
        FieldCount fieldCount,
        MethodCount methodCount,
        ClassCount reflectionClassCount,
        FieldCount reflectionFieldCount,
        MethodCount reflectionMethodCount) {
}
