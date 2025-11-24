package io.quarkus.infra.performance.graphics.charts;

import static org.junit.jupiter.api.Assertions.fail;

import java.io.StringWriter;

import org.apache.batik.anim.dom.SVGDOMImplementation;
import org.apache.batik.svggen.SVGGraphics2D;
import org.apache.batik.svggen.SVGGraphics2DIOException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;

import io.quarkus.infra.performance.graphics.Theme;

public class ElasticElementTest {
    protected static final String svgNS = SVGDOMImplementation.SVG_NAMESPACE_URI;

    protected String drawSvg(ElasticElement e) {
        DOMImplementation impl = SVGDOMImplementation.getDOMImplementation();
        Document doc = impl.createDocument(svgNS, "svg", null);

        SVGGraphics2D g = new SVGGraphics2D(doc);

        e.draw(new Subcanvas(g, 900, 900, 0, 0), Theme.DARK);

        StringWriter writer = new StringWriter();
        try {
            g.stream(writer, true);
        } catch (SVGGraphics2DIOException ex) {
            fail(ex);
        }

        String svgString = writer.toString();
        return svgString;

    }
}
