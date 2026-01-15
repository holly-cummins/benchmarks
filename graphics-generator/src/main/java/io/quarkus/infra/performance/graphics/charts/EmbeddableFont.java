package io.quarkus.infra.performance.graphics.charts;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

public class EmbeddableFont {

    // Java GraphicsEnvironment needs a ttf font, not a woff, so read from the github repo
    public static final EmbeddableFont OPENSANS = new EmbeddableFont("Open Sans", List.of("Segoe UI", "Roboto", "Arial", "Noto Sans", "sans-serif"),
            "https://github.com/googlefonts/opensans/raw/refs/heads/main/fonts/variable/OpenSans%5Bwdth,wght%5D.ttf");
    private final String css;
    private final String familyDeclaration;
    private final String fontName;

    private EmbeddableFont(String fontName, List<String> fallbacks, String fontUrl) {

        this.fontName = fontName;
        try {
            // Download the font file
            byte[] fontBytes = downloadFont(fontUrl);

            // To make fonts work, we need a css declaration (below), and we also need to tell Java about the font (done here)
            try (InputStream stream = new ByteArrayInputStream(fontBytes)) {
                Font openSans = java.awt.Font.createFont(Font.TRUETYPE_FONT, stream);
                GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
                ge.registerFont(openSans);
            } catch (FontFormatException | IOException e) {
                throw new RuntimeException(e);
            }

            // Base64 encode the font bytes
            String base64Font = Base64.getEncoder().encodeToString(fontBytes);

            css = generateFontFaceCSS(fontName, base64Font);
            familyDeclaration = "\"" + fontName + "\", " + fallbacks.stream().map(s -> "\"" + s + "\"").collect(Collectors.joining(", ")).replaceAll("\"sans-serif\"", "sans-serif");

        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getCss() {
        return css;
    }

    public String getName() {
        return fontName;
    }

    private static byte[] downloadFont(String fontUrl) throws URISyntaxException, IOException {
        // Determine cache directory inside build folder
        Path cacheDir = Paths.get("target", "fonts");
        Files.createDirectories(cacheDir);

        // Derive a safe file name from the URL
        String fileName = Paths.get(new URI(fontUrl).getPath()).getFileName().toString();
        Path cachedFont = cacheDir.resolve(fileName);

        // If cached font exists, load from disk
        if (Files.exists(cachedFont)) {
            return Files.readAllBytes(cachedFont);
        }

        // Otherwise download and cache it
        byte[] data;
        try (InputStream in = new URI(fontUrl).toURL().openStream()) {
            data = in.readAllBytes();
        }

        // Save to cache
        Files.write(cachedFont, data, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

        return data;
    }

    private static String generateFontFaceCSS(String fontName, String base64Font) {
        // To try out the fallback visually, just mangle what goes into the url: line
        return """
                  @font-face {
                    font-family: '%s';
                    src:
                      local(%s),
                      local(%s),
                      url('data:font/ttf;base64,%s') format('truetype');
                   unicode-range: U+0000-00FF, U+0131, U+0152-0153, U+02BB-02BC, U+02C6, U+02DA, U+02DC, U+2000-206F, U+2074, U+20AC, U+2122, U+2191, U+2193, U+2212, U+2215, U+FEFF, U+FFFD;
                   font-weight: 300 600;
                    font-style: normal;
                  }
                """.formatted(fontName, fontName, fontName.replaceAll(" ", ""), base64Font);
    }

    public String getFamilyDeclaration() {
        return familyDeclaration;
    }
}
