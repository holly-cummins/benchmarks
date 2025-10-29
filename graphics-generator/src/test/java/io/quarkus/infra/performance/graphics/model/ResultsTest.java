package io.quarkus.infra.performance.graphics.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;

import io.quarkus.infra.performance.graphics.charts.Datapoint;
import io.quarkus.infra.performance.graphics.model.units.TransactionsPerSecond;

class ResultsTest {
    @Test
    void getDatasets() {
        Results results = new Results();
        addDatapoint(results, Framework.QUARKUS3_JVM, 589.21);
        addDatapoint(results, Framework.SPRING3_JVM, 467.87);

        List<Datapoint> datapoints = results.getDatasets(f -> f.load().avThroughput());
        assertEquals(2, datapoints.size());
        assertEquals(589.21, datapoints.get(0).value().getValue());
        assertEquals(Framework.QUARKUS3_JVM, datapoints.get(0).framework());
        assertEquals(467.87, datapoints.get(1).value().getValue());

    }

    private static void addDatapoint(Results results, Framework framework, Double throughput) {
        Result result = mock(Result.class);
        results.addFramework(framework.getName(), result);
        Load load = mock(Load.class);
        when(result.load()).thenReturn(load);
        when(load.avThroughput()).thenReturn(new TransactionsPerSecond(throughput));
    }

}