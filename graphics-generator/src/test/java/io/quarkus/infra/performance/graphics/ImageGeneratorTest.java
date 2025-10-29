package io.quarkus.infra.performance.graphics;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import jakarta.inject.Inject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.quarkus.infra.performance.graphics.model.BenchmarkData;
import io.quarkus.infra.performance.graphics.model.Framework;
import io.quarkus.infra.performance.graphics.model.Load;
import io.quarkus.infra.performance.graphics.model.Result;
import io.quarkus.infra.performance.graphics.model.Results;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
class ImageGeneratorTest {

    private static final Double THROUGHPUT = 456.78;

    @Inject
    ImageGenerator imageGenerator;

    File image;

    @BeforeEach
    public void setup() throws IOException {

        Path targetDir = Paths.get("target"); // adjust path if needed

        if (Files.exists(targetDir) && Files.isDirectory(targetDir)) {
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(targetDir, "*.svg")) {
                for (Path file : stream) {
                    Files.deleteIfExists(file);
                }
            }

            BenchmarkData data = mock(BenchmarkData.class);
            Results results = new Results();
            when(data.results()).thenReturn(results);
            addDatapoint(data, Framework.QUARKUS3_JVM, THROUGHPUT);
            addDatapoint(data, Framework.SPRING3_JVM, 267.87);
            imageGenerator.generate(data, new File("target/images/test1.svg"));
            image = new File("target/images/test1.svg");
        } else {
            throw new RuntimeException("How can this be? Target directory not found: " + targetDir.toAbsolutePath());
        }
    }

    private static void addDatapoint(BenchmarkData data, Framework framework, Double throughput) {
        Result result = mock(Result.class);
        data.results().addFramework(framework.getName(), result);
        Load load = mock(Load.class);
        when(result.load()).thenReturn(load);
        when(load.avThroughput()).thenReturn(throughput);
    }

    @Test
    public void testGeneration() {

        assertTrue(image.exists());
        assertTrue(image.length() > 300, "Expected a size bigger than " + image.length());

    }

    @Test
    public void testFrameworkLabels() throws IOException {
        String contents = Files.readString(image.toPath());
        assertTrue(contents.contains("quarkus3-jvm"), contents);
        assertTrue(contents.contains("spring3-jvm"), contents);
    }

    @Test
    public void testDataLabels() throws IOException {
        String contents = Files.readString(image.toPath());
        assertTrue(contents.contains("456"), contents);
    }

}
