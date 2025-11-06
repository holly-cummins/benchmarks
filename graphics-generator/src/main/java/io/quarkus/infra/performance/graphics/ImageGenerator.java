package io.quarkus.infra.performance.graphics;

import java.awt.Dimension;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.function.Function;

import jakarta.enterprise.context.ApplicationScoped;

import org.apache.batik.anim.dom.SVGDOMImplementation;
import org.apache.batik.svggen.SVGGraphics2D;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;

import io.quarkus.infra.performance.graphics.charts.BarChart;
import io.quarkus.infra.performance.graphics.charts.Chart;
import io.quarkus.infra.performance.graphics.model.BenchmarkData;
import io.quarkus.infra.performance.graphics.model.Result;
import io.quarkus.infra.performance.graphics.model.units.DimensionalNumber;

@ApplicationScoped
public class ImageGenerator {
    public void generate(BenchmarkData data, Function<Result, ? extends DimensionalNumber> fun, File outFile, Theme theme)
            throws IOException {
        String svgNS = SVGDOMImplementation.SVG_NAMESPACE_URI;
        DOMImplementation impl = SVGDOMImplementation.getDOMImplementation();
        Document doc = impl.createDocument(svgNS, "svg", null);

        SVGGraphics2D svgGenerator = new SVGGraphics2D(doc);
        svgGenerator.setSVGCanvasSize(new Dimension(1200, 600));

        Chart chart = new BarChart(svgGenerator, theme);
        chart.draw(data.results().getDatasets(fun));

        outFile.getParentFile().mkdirs();
        boolean useCSS = true;
        try (Writer out = new OutputStreamWriter(new FileOutputStream(outFile), StandardCharsets.UTF_8)) {
            svgGenerator.stream(out, useCSS);
            System.out.printf("\uD83C\uDFA8 Wrote SVG image to %s\n", outFile.getAbsolutePath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
