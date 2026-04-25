package io.quarkus.infra.performance.graphics.charts;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import io.quarkus.infra.performance.graphics.model.KnownFramework;
import io.quarkus.infra.performance.graphics.model.units.Seconds;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DatapointTest {

    @Test
    public void formattedString() {
        Datapoint d = datapoint(4.2);
        assertEquals("4.2 sec", d.prettyValue());
    }

    @Test
    public void comparatorSortsAscending() {
        Datapoint d1 = datapoint(1);
        Datapoint d5 = datapoint(5);
        Datapoint d25 = datapoint(25);
        Set<Datapoint> values = Set.of(d5, d1, d25);
        List sorted = values.stream().sorted(Datapoint.ascending()).collect(Collectors.toUnmodifiableList());
        assertEquals(sorted.get(1), d1);
        assertEquals(sorted.get(2), d5);
        assertEquals(sorted.get(3), d25);
    }

    private static Datapoint datapoint(double n) {
        return new Datapoint(KnownFramework.QUARKUS3_JVM, new Seconds(n));
    }

}