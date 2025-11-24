package io.quarkus.infra.performance.graphics;

import java.awt.Dimension;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.function.BiFunction;

import jakarta.enterprise.context.ApplicationScoped;

import org.apache.batik.anim.dom.SAXSVGDocumentFactory;
import org.apache.batik.anim.dom.SVGDOMImplementation;
import org.apache.batik.svggen.SVGGraphics2D;
import org.apache.batik.util.XMLResourceDescriptor;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import io.quarkus.infra.performance.graphics.charts.Chart;
import io.quarkus.infra.performance.graphics.charts.InlinedSVG;
import io.quarkus.infra.performance.graphics.model.BenchmarkData;

@ApplicationScoped
public class ImageGenerator {
    private static final String svgNS = SVGDOMImplementation.SVG_NAMESPACE_URI;

    public void generate(BiFunction<SVGGraphics2D, Theme, Chart> chartConstructor, BenchmarkData data,
            PlotDefinition plotDefinition, File outFile, Theme theme)
            throws IOException {
        DOMImplementation impl = SVGDOMImplementation.getDOMImplementation();
        Document doc = impl.createDocument(svgNS, "svg", null);

        SVGGraphics2D svgGenerator = new SVGGraphics2D(doc);
        svgGenerator.setSVGCanvasSize(new Dimension(1200, 600));

        Chart chart = chartConstructor.apply(svgGenerator, theme);
        chart.draw(plotDefinition.title(), data.results().getDatasets(plotDefinition.fun()), data.config());

        Element root = svgGenerator.getRoot();
        initialiseFonts(doc, root);
        inlineGraphics(doc, root, chart.getInlinedSVGs());

        outFile.getParentFile().mkdirs();
        try (Writer out = new OutputStreamWriter(new FileOutputStream(outFile), StandardCharsets.UTF_8)) {
            svgGenerator.stream(root, out, true, false);
            System.out.printf("\uD83D\uDCCA Wrote SVG image to %s\n", outFile.getAbsolutePath());
        }
    }

    private void inlineGraphics(Document doc, Element root, Collection<InlinedSVG> inlinedSVGs) {
        String parser = XMLResourceDescriptor.getXMLParserClassName();
        SAXSVGDocumentFactory factory = new SAXSVGDocumentFactory(parser);

        for (InlinedSVG inlinedSVG : inlinedSVGs) {
            inlinedSVG.draw(factory, root, doc);
        }
    }

    private static void initialiseFonts(Document doc, Element root) {
        Element style = doc.createElementNS(svgNS, "style");
        style.setTextContent(Theme.FONT.getCss());
        root.insertBefore(style, root.getFirstChild());
    }

}
