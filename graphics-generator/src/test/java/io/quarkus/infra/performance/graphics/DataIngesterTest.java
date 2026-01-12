package io.quarkus.infra.performance.graphics;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.File;
import java.time.Instant;

import jakarta.inject.Inject;

import org.junit.jupiter.api.Test;

import io.quarkus.infra.performance.graphics.model.BenchmarkData;
import io.quarkus.infra.performance.graphics.model.Framework;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
class DataIngesterTest {

    @Inject
    DataIngester dataIngester;

    @Test
    public void testIngest() {
        File file = new File("src/test/resources/data.json");
        BenchmarkData data = dataIngester.ingest(file);
        assertNotNull(data);

        assertNotNull(data.results());
        assertNotNull(data.timing());
        assertNotNull(data.config());

        // Don't check every value, but do some drilling down to sense check

        // Config
        assertEquals("25.0.1-tem", data.config().jvm().version());
        assertEquals("25.0.1-graalce", data.config().jvm().graalVM().version());
        assertEquals("3.29.3", data.config().quarkus().version());

        // Resources
        assertNotNull(data.config().resources());
        assertNotNull(data.config().resources().cpu());
        assertEquals(4, data.config().resources().appCpus());
        assertEquals("0-3", data.config().resources().cpu().app());
        assertEquals("10", data.config().resources().cpu().firstRequest());
        assertEquals("7-9", data.config().resources().cpu().loadGenerator());
        assertEquals("4-6", data.config().resources().cpu().db());

        // Results
        assertEquals(18927.38, data.results().framework(Framework.QUARKUS3_JVM).load().avThroughput().getValue());
        assertEquals(7.183333333333334, data.results().framework(Framework.SPRING3_NATIVE).build().avNativeRSS().getValue());
        assertEquals(35173, data.results().framework(Framework.SPRING3_NATIVE).build().classCount().getValue());

        // Timing
        assertEquals(Instant.parse("2025-11-18T22:28:52Z"), data.timing().start());
        assertEquals(Instant.parse("2025-11-19T00:19:44Z"), data.timing().stop());
        assertEquals(8.81,
                data.results().framework(Framework.QUARKUS3_JVM).build().avBuildTime().getValue());

        // Native
        assertEquals(11629, data.results().framework(Framework.SPRING3_NATIVE).build().reflectionClassCount().getValue());
    }

}