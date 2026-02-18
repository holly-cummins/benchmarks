package io.quarkus.infra.performance.graphics.charts;

import java.awt.Font;
import java.util.Set;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SizerTest {

    @Test
    void calculateWidthForSingleString() {
        // It's hard to reason about the exact right values here, so hardcode some expectations
        assertEquals(70, Sizer.calculateWidth("some string", 12));
        assertEquals(106, Sizer.calculateWidth("some string", 18));
    }

    @Test
    void calculateWidthForSingleStringWithStyle() {
        // It's hard to reason about the exact right values here, so hardcode some expectations
        assertEquals(66, Sizer.calculateWidth("some string", 12, Font.PLAIN));
        assertEquals(70, Sizer.calculateWidth("some string", 12, Font.BOLD));
        assertEquals(100, Sizer.calculateWidth("some string", 18, Font.PLAIN));
        assertEquals(106, Sizer.calculateWidth("some string", 18, Font.BOLD));
    }

    @Test
    void calculateWidthForCollectionOfStrings() {
        assertEquals(Sizer.calculateWidth("some string", 12),
                Sizer.calculateWidth(Set.of("short", "some string", "tiddly"), 12));
    }

    @Test
    void calculateWidthForCollectionOfStringsWithLineBreaks() {
        assertEquals(Sizer.calculateWidth("some string", 12),
                Sizer.calculateWidth(Set.of("short\ns", "yup\nsome string\nok", "tiddly\nwinks"), 12));
    }

    @Test
    void calculateHeight() {
        // The actual space occupied by a font is bigger than the notional size, because of descents and other font parts
        assertEquals(17, Sizer.calculateHeight(12));
    }

    @Test
    void calculateFontSize() {
        assertEquals(12, Sizer.calculateFontSize(17));
    }

}