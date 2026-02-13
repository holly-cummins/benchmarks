package io.quarkus.infra.performance.graphics.model;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import static io.quarkus.infra.performance.graphics.model.Category.AOT;
import static io.quarkus.infra.performance.graphics.model.Category.COMPATIBILITY;
import static io.quarkus.infra.performance.graphics.model.Category.JVM;
import static io.quarkus.infra.performance.graphics.model.Category.NATIVE;
import static io.quarkus.infra.performance.graphics.model.Category.OLD;
import static io.quarkus.infra.performance.graphics.model.Category.QUARKUS;
import static io.quarkus.infra.performance.graphics.model.Category.SPRING;
import static io.quarkus.infra.performance.graphics.model.Category.VIRTUAL_THREADS;


public enum Framework {
    // The order of these determines the natural order in the charts
    QUARKUS3_JVM("quarkus3-jvm", "Quarkus\nJIT (via OpenJDK)", EnumSet.of(QUARKUS, JVM)),
    SPRING4_JVM("spring4-jvm", "Spring 4\nJIT (via OpenJDK)", EnumSet.of(SPRING, JVM)),
    SPRING_JVM("spring-jvm", "Spring\nJIT (via OpenJDK)", EnumSet.of(SPRING, JVM)),
    SPRING3_JVM("spring3-jvm", "Spring 3\nJIT (via OpenJDK)", EnumSet.of(SPRING, JVM, OLD)),
    SPRING4_JVM_AOT("spring4-jvm-aot", "Spring 4\nAOT (via OpenJDK)", EnumSet.of(SPRING, JVM, AOT)),
    SPRING_JVM_AOT("spring-jvm-aot", "Spring\nAOT (via OpenJDK)", EnumSet.of(SPRING, JVM, AOT)),
    SPRING3_JVM_AOT("spring3-jvm-aot", "Spring 3\nAOT (via OpenJDK)", EnumSet.of(SPRING, JVM, AOT, OLD)),
    QUARKUS3_VIRTUAL("quarkus3-virtual", "Quarkus w/Virtual Threads\nJIT (via OpenJDK)", EnumSet.of(QUARKUS, JVM, VIRTUAL_THREADS)),
    SPRING4_VIRTUAL("spring4-virtual", "Spring 4 w/Virtual Threads\nJIT (via OpenJDK)", EnumSet.of(SPRING, JVM, VIRTUAL_THREADS)),
    SPRING_VIRTUAL("spring-virtual", "Spring w/Virtual Threads\nJIT (via OpenJDK)", EnumSet.of(SPRING, JVM, AOT)),
    SPRING3_VIRTUAL("spring3-virtual", "Spring 3 w/Virtual Threads\nJIT (via OpenJDK)", EnumSet.of(SPRING, JVM, AOT, OLD)),
    QUARKUS3_NATIVE("quarkus3-native", "Quarkus\nNative (via GraalVM)", EnumSet.of(QUARKUS, NATIVE)),
    SPRING4_NATIVE("spring4-native", "Spring 4\nNative (via GraalVM)", EnumSet.of(SPRING, NATIVE)),
    SPRING_NATIVE("spring-native", "Spring\nNative (via GraalVM)", EnumSet.of(SPRING, NATIVE)),
    SPRING3_NATIVE("spring3-native", "Spring 3\nNative (via GraalVM)", EnumSet.of(SPRING, NATIVE, OLD)),
    QUARKUS3_SPRING_COMPAT("quarkus3-spring-compat", "Quarkus\nwith Spring compatibility libraries", EnumSet.of(SPRING, COMPATIBILITY, JVM)),
    QUARKUS3_SPRING4_COMPAT("quarkus3-spring4-compat", "Quarkus\nwith Spring 4 compatibility libraries", EnumSet.of(SPRING, COMPATIBILITY, JVM)),
    QUARKUS3_SPRING3_COMPAT("quarkus3-spring3-compat", "Quarkus\nwith Spring 3 compatibility libraries", EnumSet.of(SPRING, COMPATIBILITY, JVM, OLD));

    private final String name;
    private final String expandedName;
    private static final Map<String, Framework> ENUM_MAP;
    private final Set<Category> categories;

    Framework(String name, String expandedName, Set<Category> categories) {
        this.name = name;
        this.expandedName = expandedName;
        this.categories = categories;
    }

    @JsonValue
    public String getName() {
        return this.name;
    }

    public String getExpandedName() {
        return this.expandedName;
    }

    // Build an immutable map of String name to enum pairs.
    static {
        ENUM_MAP = Arrays.stream(values())
                .collect(Collectors.toUnmodifiableMap(framework -> framework.getName().toLowerCase(), Function.identity()));
    }

    @JsonCreator
    public static Framework valueOfIgnoreCase(String name) {
        return ENUM_MAP.get(name.toLowerCase());
    }

    public boolean isInGroup(Group group) {
        return group.contains(this);
    }

    public boolean hasCategory(Category category) {
        return categories.contains(category);
    }
}
