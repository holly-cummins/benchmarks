package io.quarkus.infra.performance.graphics;

import java.awt.Color;
import java.util.Random;

public class RandomColor {
    private static final Random rand = new Random();
    private static final Color[] COLORS = {
            Color.BLUE, Color.CYAN, Color.DARK_GRAY, Color.GRAY,
            Color.GREEN, Color.LIGHT_GRAY, Color.MAGENTA, Color.ORANGE,
            Color.PINK, Color.RED, Color.YELLOW
    };

    public static Color nextColor() {
        return COLORS[rand.nextInt(COLORS.length)];
    }
}
