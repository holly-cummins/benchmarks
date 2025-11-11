package io.quarkus.infra.performance.graphics;

import java.awt.Dimension;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

import jakarta.enterprise.context.ApplicationScoped;

import org.apache.batik.anim.dom.SVGDOMImplementation;
import org.apache.batik.svggen.SVGGraphics2D;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import io.quarkus.infra.performance.graphics.charts.BarChart;
import io.quarkus.infra.performance.graphics.charts.Chart;
import io.quarkus.infra.performance.graphics.model.BenchmarkData;

@ApplicationScoped
public class ImageGenerator {
    private static final String svgNS = SVGDOMImplementation.SVG_NAMESPACE_URI;

    public void generate(BenchmarkData data, PlotDefinition plotDefinition, File outFile, Theme theme)
            throws IOException {
        DOMImplementation impl = SVGDOMImplementation.getDOMImplementation();
        Document doc = impl.createDocument(svgNS, "svg", null);

        SVGGraphics2D svgGenerator = new SVGGraphics2D(doc);
        svgGenerator.setSVGCanvasSize(new Dimension(1200, 600));

        Chart chart = new BarChart(svgGenerator, theme);
        chart.draw(plotDefinition.title(), data.results().getDatasets(plotDefinition.fun()));

        Element root = svgGenerator.getRoot();
        initialiseFonts(doc, root);

        outFile.getParentFile().mkdirs();
        try (Writer out = new OutputStreamWriter(new FileOutputStream(outFile), StandardCharsets.UTF_8)) {
            svgGenerator.stream(root, out, true, false);
            System.out.printf("\uD83C\uDFA8 Wrote SVG image to %s\n", outFile.getAbsolutePath());
        }
    }

    private static void initialiseFonts(Document doc, Element root) {
        Element style = doc.createElementNS(svgNS, "style");
        style.setTextContent(Theme.FONT.getCss());
        root.insertBefore(style, root.getFirstChild());
    }

}
