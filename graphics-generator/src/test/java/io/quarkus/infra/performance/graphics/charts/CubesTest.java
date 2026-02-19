package io.quarkus.infra.performance.graphics.charts;

import java.util.Random;

import io.quarkus.infra.performance.graphics.model.Framework;
import io.quarkus.infra.performance.graphics.model.units.Memory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class CubesTest {

    @Test
    public void testBoundsOnDimensionsForFrameworkWithShortNameAndHighValue() {
        Memory m = new Memory(315);
        Datapoint d = new Datapoint(Framework.SPRING3_JVM, m);

        Cubes chart = new Cubes(d);

        int preferredWidth = chart.getPreferredHorizontalSize();
        int preferredHeight = chart.getPreferredVerticalSize();

        int minimumWidth = chart.getMinimumHorizontalSize();
        int minimumHeight = chart.getMinimumVerticalSize();

        int maximumWidth = chart.getMaximumHorizontalSize();
        int maximumHeight = chart.getMaximumVerticalSize();

        assertTrue(preferredWidth <= maximumWidth, preferredWidth + " > " + maximumWidth);
        assertTrue(preferredWidth >= minimumWidth, preferredWidth + " > " + minimumWidth);

        assertTrue(preferredHeight <= maximumHeight, preferredHeight + " > " + maximumHeight);
        assertTrue(preferredHeight >= minimumHeight, preferredHeight + " > " + minimumHeight);
    }

    @Test
    public void testBoundsOnDimensionsForFrameworkWithLongNameAndLowValue() {
        Memory m = new Memory(70);
        Datapoint d = new Datapoint(Framework.QUARKUS3_VIRTUAL, m);

        Cubes chart = new Cubes(d);

        int preferredWidth = chart.getPreferredHorizontalSize();
        int preferredHeight = chart.getPreferredVerticalSize();

        int minimumWidth = chart.getMinimumHorizontalSize();
        int minimumHeight = chart.getMinimumVerticalSize();

        int maximumWidth = chart.getMaximumHorizontalSize();
        int maximumHeight = chart.getMaximumVerticalSize();

        assertTrue(preferredWidth <= maximumWidth, preferredWidth + " > " + maximumWidth);
        assertTrue(preferredWidth >= minimumWidth, preferredWidth + " > " + minimumWidth);

        assertTrue(preferredHeight <= maximumHeight, preferredHeight + " > " + maximumHeight);
        assertTrue(preferredHeight >= minimumHeight, preferredHeight + " > " + minimumHeight);
    }

    private static Memory getRandomMemory() {
        return new Memory(new Random().nextDouble() * 1000);
    }

}
