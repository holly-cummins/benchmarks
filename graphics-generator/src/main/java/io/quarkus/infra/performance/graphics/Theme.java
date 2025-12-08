package io.quarkus.infra.performance.graphics;

import static java.awt.Color.decode;

import java.awt.Color;
import java.util.Map;

import io.quarkus.infra.performance.graphics.charts.EmbeddableFont;
import io.quarkus.infra.performance.graphics.model.Framework;

public record Theme(String name, Color background, Color text, Map<Framework, Color> chartElements) {

    public static final EmbeddableFont FONT = EmbeddableFont.OPENSANS;

    public static Theme LIGHT = new Theme("light",
            java.awt.Color.WHITE,
            Color.BLACK,
            Map.ofEntries(
                    Map.entry(Framework.QUARKUS3_JVM, decode("#4695EB")),
                    Map.entry(Framework.QUARKUS3_NATIVE, decode("#FF0000")),
                    Map.entry(Framework.QUARKUS3_SPRING_COMPAT, decode("#0D1C2C")),

                    // Greyscale values for Spring
                    Map.entry(Framework.SPRING3_JVM, decode("#EEEEEE")),
                    Map.entry(Framework.SPRING3_NATIVE, decode("#D2D2D2")),
                    Map.entry(Framework.SPRING3_JVM_AOT, decode("#B6B6B6")),
                    Map.entry(Framework.SPRING4_JVM, decode("#9A9A9A")),
                    Map.entry(Framework.SPRING4_NATIVE, decode("#7E7E7E")),
                    Map.entry(Framework.SPRING4_JVM_AOT, decode("#626262")),
                    Map.entry(Framework.SPRING_JVM, decode("#464646")),
                    Map.entry(Framework.SPRING_NATIVE, decode("#2A2A2A")),
                    Map.entry(Framework.SPRING_JVM_AOT, decode("#0E0E0E"))));

    public static final Theme DARK = new Theme("dark",
            Color.BLACK,
            Color.WHITE,
            Map.ofEntries(
                    Map.entry(Framework.QUARKUS3_JVM, decode("#71AEEF")),
                    Map.entry(Framework.QUARKUS3_NATIVE, decode("#70CA8D")),
                    Map.entry(Framework.QUARKUS3_SPRING_COMPAT, decode("#0D1C2C")),

                    // Lightish grey values for Spring
                    Map.entry(Framework.SPRING3_JVM, decode("#F2F2F2")),
                    Map.entry(Framework.SPRING3_NATIVE, decode("#E4E4E4")),
                    Map.entry(Framework.SPRING3_JVM_AOT, decode("#D6D6D6")),
                    Map.entry(Framework.SPRING4_JVM, decode("#C8C8C8")),
                    Map.entry(Framework.SPRING4_NATIVE, decode("#BABABA")),
                    Map.entry(Framework.SPRING4_JVM_AOT, decode("#ACACAC")),
                    Map.entry(Framework.SPRING_JVM, decode("#9E9E9E")),
                    Map.entry(Framework.SPRING_NATIVE, decode("#909090")),
                    Map.entry(Framework.SPRING_JVM_AOT, decode("#888888"))));

}
