package io.quarkus.infra.performance.graphics.model;

import java.util.List;

import io.quarkus.infra.performance.graphics.MissingDataException;
import io.quarkus.infra.performance.graphics.charts.Datapoint;
import io.quarkus.infra.performance.graphics.model.units.TransactionsPerSecond;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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

    @Test
    void getDatasetsAdjustsFrameworksWhenOnlyOnePresent() {
        Results results = new Results();
        addDatapoint(results, Framework.QUARKUS3_JVM, 589.21);
        addDatapoint(results, Framework.SPRING3_JVM, 467.87);

        List<Datapoint> datapoints = results.getDatasets(f -> f.load().avThroughput());
        assertEquals(2, datapoints.size());
        assertEquals(589.21, datapoints.get(0).value().getValue());
        // The second framework should be a synthetic one to reflect the fact there's only one Spring version
        assertEquals(Framework.SPRING_JVM, datapoints.get(1).framework());
        assertEquals(467.87, datapoints.get(1).value().getValue());
    }

    @Test
    void getDatasetsDoesNotAdjustFrameworksWhenMultiplesPresent() {
        Results results = new Results();
        addDatapoint(results, Framework.SPRING4_JVM, 42.1);
        addDatapoint(results, Framework.QUARKUS3_JVM, 589.21);
        addDatapoint(results, Framework.SPRING3_JVM, 467.87);

        List<Datapoint> datapoints = results.getDatasets(f -> f.load().avThroughput());
        assertEquals(3, datapoints.size());
        assertEquals(589.21, datapoints.get(0).value().getValue());
        assertEquals(Framework.QUARKUS3_JVM, datapoints.get(0).framework());

        assertEquals(Framework.SPRING4_JVM, datapoints.get(1).framework());
        assertEquals(42.1, datapoints.get(1).value().getValue());

        assertEquals(Framework.SPRING3_JVM, datapoints.get(2).framework());
        assertEquals(467.87, datapoints.get(2).value().getValue());

    }

    @Test
    void getDatasetsForMissingData() {
        Results results = new Results();
        addDatapoint(results, Framework.SPRING3_JVM, 589.21);

        Exception exception = assertThrows(MissingDataException.class,
                () -> results.getDatasets(f -> f.rss().avFirstRequestRss()));
        assertTrue(exception.getMessage().contains("Rss"), exception.getMessage());
        assertTrue(exception.getMessage().contains("pring"), exception.getMessage());

    }

    @Test
    void subgroup() {
        Results results = new Results();
        addDatapoint(results, Framework.QUARKUS3_JVM, 589.21);
        addDatapoint(results, Framework.SPRING3_JVM, 467.87);
        addDatapoint(results, Framework.SPRING4_JVM, 467.87);

        Results subgroup = results.subgroup(Group.MAIN_COMPARISON);
        List<Datapoint> datapoints = subgroup.getDatasets(f -> f.load().avThroughput());
        assertEquals(2, datapoints.size());
        assertEquals(Framework.QUARKUS3_JVM, datapoints.get(0).framework());
        assertEquals(Framework.SPRING_JVM, datapoints.get(1).framework());
    }

    private static void addDatapoint(Results results, Framework framework, Double throughput) {
        Result result = mock(Result.class);
        results.addFramework(framework.getName(), result);
        Load load = mock(Load.class);
        when(result.load()).thenReturn(load);
        when(load.avThroughput()).thenReturn(new TransactionsPerSecond(throughput));
    }

}