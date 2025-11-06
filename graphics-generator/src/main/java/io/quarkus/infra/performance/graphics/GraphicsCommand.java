package io.quarkus.infra.performance.graphics;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.function.Function;

import jakarta.inject.Inject;

import io.quarkus.infra.performance.graphics.model.BenchmarkData;
import io.quarkus.infra.performance.graphics.model.Result;
import io.quarkus.infra.performance.graphics.model.units.DimensionalNumber;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(name = "graphics", mixinStandardHelpOptions = true)
public class GraphicsCommand implements Runnable {

    private static final Function<Result, ? extends DimensionalNumber> THROUGHPUT = framework -> framework.load()
            .avThroughput();

    @Parameters(paramLabel = "<filename>", defaultValue = "latest.json", description = "A filename of json-formatted data, or a directory. For directories, .json files in the directory will be processed recursively.")
    Path filename;

    @Parameters(paramLabel = "<outputDir>", defaultValue = ".", description = "The directory for generated images")
    File outputDirectory;

    @Parameters(paramLabel = "<tolerateMissingData>", defaultValue = "true", description = "Whether to suppress exceptions caused by missing data")
    boolean tolerateMissingData;

    @Inject
    DataIngester ingester;

    @Inject
    ImageGenerator generator;

    @Override
    public void run() {

        processPath(filename.toFile());

    }

    private void processPath(File inputFile) {
        if (inputFile.isDirectory()) {
            processDirectory(inputFile);
        } else {
            processFile(inputFile);
        }
    }

    private void processDirectory(File directory) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    if (file.getName().endsWith(".json")) {
                        processFile(file);
                    } else {
                        System.out.printf("Ignoring file %s (unexpected extension)\n", file.getName());
                    }
                } else if (file.isDirectory()) {
                    processDirectory(file);
                }
            }
        }
    }

    private void processFile(File file) {
        BenchmarkData data = ingester.ingest(file);

        File qualifiedOutputDir;
        Path pathRelative = filename.relativize(file.getParentFile().toPath());
        //        If relativize leads to "..", use current dir
        if (pathRelative.toString().endsWith("..")) {
            qualifiedOutputDir = outputDirectory;
        } else {
            qualifiedOutputDir = new File(outputDirectory, pathRelative.toString());
        }
        try {
            {
                File outFile = new File(qualifiedOutputDir, file.getName().replace(".json", "-light.svg"));
                generator.generate(data, THROUGHPUT, outFile, Theme.LIGHT);
            }
            {
                File outFile = new File(qualifiedOutputDir, file.getName().replace(".json", "-dark.svg"));
                generator.generate(data, THROUGHPUT, outFile, Theme.DARK);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (MissingDataException e) {
            if (!tolerateMissingData) {
                throw e;
            }
        }
    }
}
