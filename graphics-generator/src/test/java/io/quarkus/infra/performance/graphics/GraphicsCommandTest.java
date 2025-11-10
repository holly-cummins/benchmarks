package io.quarkus.infra.performance.graphics;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.main.Launch;
import io.quarkus.test.junit.main.LaunchResult;
import io.quarkus.test.junit.main.QuarkusMainLauncher;
import io.quarkus.test.junit.main.QuarkusMainTest;

@QuarkusMainTest
public class GraphicsCommandTest {

    @BeforeEach
    public void setup() throws IOException {

        Path targetDir = Paths.get("target"); // adjust path if needed

        if (Files.exists(targetDir) && Files.isDirectory(targetDir)) {
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(targetDir, "*-light.svg")) {
                for (Path file : stream) {
                    Files.deleteIfExists(file);
                }
            }
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(targetDir, "*-dark.svg")) {
                for (Path file : stream) {
                    Files.deleteIfExists(file);
                }
            }
        } else {
            throw new RuntimeException("How can this be? Target directory not found: " + targetDir.toAbsolutePath());
        }
    }

    @Test
    public void testLaunchWithNoArguments(QuarkusMainLauncher launcher) {

        // It would be nice to suppress output but redirecting stderr doesn't suppress the stack trace

        LaunchResult result = launcher.launch();
        assertTrue(result.getOutput().contains("latest.json"), result.getOutput());
        assertEquals(1, result.exitCode());
    }

    @Test
    @Launch({ "src/test/resources/data.json", "target/test-output/filename" })
    public void testLaunchWithFilename(LaunchResult result) {
        String output = result.getOutput();
        assertTrue(output.contains("data.json"), output);

        File image = new File("target/test-output/filename/data-throughput-light.svg");
        assertTrue(image.exists());
    }

    @Test
    @Launch({ "src/test/resources", "target/test-output/directory" })
    public void testLaunchWithDirectory(LaunchResult result) {
        String output = result.getOutput();
        assertTrue(output.contains("data.json"), output);
        assertTrue(output.contains("data2.json"), output);

        File dir = new File("target/test-output/directory/");
        File image1 = new File(dir, "data-throughput-light.svg");
        assertTrue(image1.exists());
        File image2 = new File(dir, "data2-throughput-light.svg");
        assertTrue(image2.exists());

        File nestedDir = new File("target/test-output/directory/nested/more-nested");
        assertTrue(nestedDir.exists());
        File image3 = new File(nestedDir, "data3-throughput-light.svg");
        assertTrue(image3.exists());
    }

    @Test
    @Launch({ "src/test/resources/data.json", "target/test-output/filename" })
    public void testDarkMode(LaunchResult result) {
        String output = result.getOutput();
        assertTrue(output.contains("data.json"), output);

        File image = new File("target/test-output/filename/data-throughput-dark.svg");
        assertTrue(image.exists());
    }

}
