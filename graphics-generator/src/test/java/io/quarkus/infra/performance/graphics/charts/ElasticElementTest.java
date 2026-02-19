package io.quarkus.infra.performance.graphics.charts;

import java.awt.Dimension;
import java.io.StringWriter;

import io.quarkus.infra.performance.graphics.Theme;
import org.apache.batik.anim.dom.SVGDOMImplementation;
import org.apache.batik.svggen.SVGGraphics2D;
import org.apache.batik.svggen.SVGGraphics2DIOException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;

import static org.apache.batik.util.SVGConstants.SVG_NAMESPACE_URI;
import static org.junit.jupiter.api.Assertions.fail;

public class ElasticElementTest {
    protected static final String svgNS = SVGDOMImplementation.SVG_NAMESPACE_URI;

    protected String drawSvg(ElasticElement e) {

        SVGGraphics2D g = getSvgGraphics2D(e.getPreferredHorizontalSize(), e.getPreferredVerticalSize());

        e.draw(new Subcanvas(g, e.getPreferredHorizontalSize(), e.getPreferredVerticalSize(), 0, 0), Theme.DARK);

        StringWriter writer = new StringWriter();
        try {
            g.stream(writer, true);
        } catch (SVGGraphics2DIOException ex) {
            fail(ex);
        }

        String svgString = writer.toString();
        return svgString;

    }

    protected static SVGGraphics2D getSvgGraphics2D(int width, int height) {
        DOMImplementation impl = SVGDOMImplementation.getDOMImplementation();
        Document doc = impl.createDocument(SVG_NAMESPACE_URI, "svg", null);

        SVGGraphics2D svgGenerator = new SVGGraphics2D(doc);
        svgGenerator.setSVGCanvasSize(new Dimension(width, height));
        return svgGenerator;
    }
}
