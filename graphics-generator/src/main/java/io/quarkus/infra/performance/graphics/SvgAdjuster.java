package io.quarkus.infra.performance.graphics;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

public class SvgAdjuster {

    public static final String GRAPHICS_ELEMENT = "g";
    public static final String STYLE = "style";
    public static final String FONT_FAMILY_DECLARATION = "font-family:\\s*'" + Theme.FONT.getName() + "'";

    public static String adjustSvg(String svg) {
        String fallbackString = "font-family: " + Theme.FONT.getFamilyDeclaration();
        Document svgDoc = Jsoup.parse(svg, "", Parser.xmlParser());

        Elements gElements = svgDoc.select(GRAPHICS_ELEMENT);

        for (Element g : gElements) {
            String style = g.attr(STYLE);
            style = style.replaceAll(FONT_FAMILY_DECLARATION, fallbackString);
            g.attr(STYLE, style);
        }
        return svgDoc.outerHtml();
    }
}
