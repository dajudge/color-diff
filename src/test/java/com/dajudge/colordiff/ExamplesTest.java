package com.dajudge.colordiff;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ExamplesTest {
    @Test
    void closest_example() {
        RgbColor color = new RgbColor(255, 1, 30);

        List<RgbColor> palette = Arrays.asList(
                new RgbColor(255, 0, 0), // red
                new RgbColor(0, 255, 0), // green
                new RgbColor(0, 0, 255)  // blue
        );

        RgbColor closestMatch = ColorDiff.closest(color, palette);
        assertEquals(new RgbColor(255, 0, 0), closestMatch); // red
    }

    @Test
    void furthest_example() {
        RgbColor color = new RgbColor(255, 255, 255); // white

        List<RgbColor> palette = Arrays.asList(
                new RgbColor(0, 0, 0),      // black
                new RgbColor(255, 255, 255) // white
        );

        RgbColor furthestMatch = ColorDiff.furthest(color, palette);
        assertEquals(new RgbColor(0,0,0), furthestMatch); // black
    }
}
