package org.acme;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.acme.model.BenchmarkData;
import org.acme.model.Framework;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
class DataIngesterTest {

    @Inject
    DataIngester dataIngester;

    @Test
    public void testIngest() {
        File file = new File("src/test/resources/data.json");
        BenchmarkData data = dataIngester.ingest(file);
        assertNotNull(data);

        assertNotNull(data.getResults());
        assertNotNull(data.getTiming());
        assertNotNull(data.getConfig());

        // Don't check every value, but do some drilling down to sense check

        // Config
        assertEquals("25-tem", data.getConfig().getJavaVersion());
        assertEquals("3.28.4", data.getConfig().getQuarkusVersion());

        // CGroup
        assertEquals("14G", data.getConfig().getCgroup().getMemMax());

        // Results

        assertEquals(27586.713333333333, data.getResults().getFramework(Framework.QUARKUS3_JVM).getLoad().getAvThroughput());
        assertEquals(6.496666666666667, data.getResults().getFramework(Framework.SPRING3_NATIVE).getBuild().getAvNativeRSS());
    }

}