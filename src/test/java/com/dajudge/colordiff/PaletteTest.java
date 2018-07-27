/*
 * @author Markus Ekholm
 *
 * @copyright 2012-2016 (c) Markus Ekholm <markus at botten dot org>
 *
 * @license Copyright (c) 2012-2018, Markus Ekholm
 *
 * All rights reserved.
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * * Neither the name of the author nor the
 * names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL MARKUS EKHOLM BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.dajudge.colordiff;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.dajudge.colordiff.Convert.rgb_to_lab;
import static com.dajudge.colordiff.Palette.MapType.CLOSEST;
import static com.dajudge.colordiff.Palette.MapType.FURTHEST;
import static com.dajudge.colordiff.Palette.lab_palette_map_key;
import static com.dajudge.colordiff.Palette.map_palette;
import static com.dajudge.colordiff.Palette.map_palette_lab;
import static com.dajudge.colordiff.Palette.match_palette_lab;
import static com.dajudge.colordiff.Palette.palette_map_key;
import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for {@link Palette}.
 *
 * @author Alex Stockinger
 */
class PaletteTest {

    private static final RgbColor white = new RgbColor(255, 255, 255);
    private static final RgbColor black = new RgbColor(0, 0, 0);
    private static final RgbColor navy = new RgbColor(0, 0, 128);
    private static final RgbColor blue = new RgbColor(0, 0, 255);
    private static final RgbColor yellow = new RgbColor(255, 255, 0);
    private static final RgbColor gold = new RgbColor(255, 215, 0);

    private static final LabColor white_lab = rgb_to_lab(white);
    private static final LabColor black_lab = rgb_to_lab(black);
    private static final LabColor navy_lab = rgb_to_lab(navy);
    private static final LabColor blue_lab = rgb_to_lab(blue);
    private static final LabColor yellow_lab = rgb_to_lab(yellow);
    private static final LabColor gold_lab = rgb_to_lab(gold);

    private static final List<RgbColor> colors1 = asList(new RgbColor[]{white, black, navy, blue, yellow, gold});
    private static final List<LabColor> colors1_lab = asList(new LabColor[]{white_lab, black_lab, navy_lab, blue_lab, yellow_lab, gold_lab});
    private static final List<RgbColor> colors2 = asList(new RgbColor[]{white, black, blue, gold});
    private static final List<LabColor> colors2_lab = asList(new LabColor[]{white_lab, black_lab, blue_lab, gold_lab});
    private static final List<RgbColor> colors3 = asList(new RgbColor[]{white, black, yellow, blue});
    private static final List<LabColor> colors3_lab = asList(new LabColor[]{white_lab, black_lab, yellow_lab, blue_lab});

    private static final RgbColor white_a = new RgbColor(255, 255, 255, 1.0);
    private static final RgbColor black_a = new RgbColor(0, 0, 0, 1.0);
    private static final RgbColor navy_a = new RgbColor(0, 0, 128, 1.0);
    private static final RgbColor blue_a = new RgbColor(0, 0, 255, 1.0);
    private static final RgbColor yellow_a = new RgbColor(255, 255, 0, 1.0);
    private static final RgbColor gold_a = new RgbColor(255, 215, 0, 1.0);
    private static final List<RgbColor> colors1_a = asList(new RgbColor[]{white_a, black_a, navy_a, blue_a, yellow_a, gold_a});

    private static final LabColor white_a_lab = rgb_to_lab(white_a);
    private static final LabColor black_a_lab = rgb_to_lab(black_a);
    private static final LabColor navy_a_lab = rgb_to_lab(navy_a);
    private static final LabColor blue_a_lab = rgb_to_lab(blue_a);
    private static final LabColor yellow_a_lab = rgb_to_lab(yellow_a);
    private static final LabColor gold_a_lab = rgb_to_lab(gold_a);
    private static final List<LabColor> colors1_a_lab = asList(new LabColor[]{white_a_lab, black_a_lab, navy_a_lab, blue_a_lab, yellow_a_lab, gold_a_lab});

    @Test
    public void should_map_all_colors_to_themselves_when_possible_1() {
        final Map<String, RgbColor> expected1_1 = new HashMap<>();
        expected1_1.put(palette_map_key(white, false), white);
        expected1_1.put(palette_map_key(black, false), black);
        expected1_1.put(palette_map_key(navy, false), navy);
        expected1_1.put(palette_map_key(blue, false), blue);
        expected1_1.put(palette_map_key(yellow, false), yellow);
        expected1_1.put(palette_map_key(gold, false), gold);
        assertEquals(expected1_1, map_palette(colors1, false, colors1, false));
    }

    @Test
    public void should_map_all_colors_to_themselves_when_possible_2() {
        final Map<String, RgbColor> expected1_2 = new HashMap<>();
        expected1_2.put(palette_map_key(white_a, true), white_a);
        expected1_2.put(palette_map_key(black_a, true), black_a);
        expected1_2.put(palette_map_key(navy_a, true), navy_a);
        expected1_2.put(palette_map_key(blue_a, true), blue_a);
        expected1_2.put(palette_map_key(yellow_a, true), yellow_a);
        expected1_2.put(palette_map_key(gold_a, true), gold_a);
        assertEquals(expected1_2, map_palette(colors1_a, true, colors1_a, true));
    }

    @Test
    public void should_map_navy_to_blue_and_yellow_to_gold_when_navy_and_yellow_are_missing() {
        final Map<String, RgbColor> expected2 = new HashMap<>();
        expected2.put(palette_map_key(white, false), white);
        expected2.put(palette_map_key(black, false), black);
        expected2.put(palette_map_key(navy, false), blue);
        expected2.put(palette_map_key(blue, false), blue);
        expected2.put(palette_map_key(yellow, false), gold);
        expected2.put(palette_map_key(gold, false), gold);
        assertEquals(expected2, map_palette(colors1, false, colors2, false));
    }

    @Test
    public void should_map_white_to_black_and_black_and_navy_and_blue_to_yellow_and_yellow_and_gold_to_blue() {
        final Map<String, RgbColor> expected3 = new HashMap<>();
        expected3.put(palette_map_key(white, false), black);
        expected3.put(palette_map_key(black, false), yellow);
        expected3.put(palette_map_key(navy, false), yellow);
        expected3.put(palette_map_key(blue, false), yellow);
        expected3.put(palette_map_key(yellow, false), blue);
        expected3.put(palette_map_key(gold, false), blue);
        assertEquals(expected3, map_palette(colors1, false, colors3, false, FURTHEST));
    }

    @Test
    public void should_map_all_colors_to_themselves_when_possible_2_lab() {
        final Map<String, LabColor> expected1_2 = new HashMap<>();
        expected1_2.put(lab_palette_map_key(white_a_lab), white_a_lab);
        expected1_2.put(lab_palette_map_key(black_a_lab), black_a_lab);
        expected1_2.put(lab_palette_map_key(navy_a_lab), navy_a_lab);
        expected1_2.put(lab_palette_map_key(blue_a_lab), blue_a_lab);
        expected1_2.put(lab_palette_map_key(yellow_a_lab), yellow_a_lab);
        expected1_2.put(lab_palette_map_key(gold_a_lab), gold_a_lab);
        assertEquals(expected1_2, map_palette_lab(colors1_a_lab, colors1_a_lab, CLOSEST));
    }

    @Test
    public void should_map_navy_to_blue_and_yellow_to_gold_when_navy_and_yellow_are_missing_lab() {
        final Map<String, LabColor> expected2 = new HashMap<>();
        expected2.put(lab_palette_map_key(white_lab), white_lab);
        expected2.put(lab_palette_map_key(black_lab), black_lab);
        expected2.put(lab_palette_map_key(navy_lab), blue_lab);
        expected2.put(lab_palette_map_key(blue_lab), blue_lab);
        expected2.put(lab_palette_map_key(yellow_lab), gold_lab);
        expected2.put(lab_palette_map_key(gold_lab), gold_lab);
        assertEquals(expected2, map_palette_lab(colors1_lab, colors2_lab, CLOSEST));
    }

    @Test
    public void should_map_white_and_black_and_black_and_navy_and_blue_to_yellow_and_yellow_and_gold_to_blue_lab() {
        final Map<String, LabColor> expected3 = new HashMap<>();
        expected3.put(lab_palette_map_key(white_lab), black_lab);
        expected3.put(lab_palette_map_key(black_lab), yellow_lab);
        expected3.put(lab_palette_map_key(navy_lab), yellow_lab);
        expected3.put(lab_palette_map_key(blue_lab), yellow_lab);
        expected3.put(lab_palette_map_key(yellow_lab), blue_lab);
        expected3.put(lab_palette_map_key(gold_lab), blue_lab);
        assertEquals(expected3, map_palette_lab(colors1_lab, colors3_lab, FURTHEST));
    }

    @Test
    public void should_match_map_palette_results_for_closest() {
        assertEquals(match_palette_lab(white_lab, colors1_lab, false), white_lab);
        assertEquals(match_palette_lab(black_lab, colors1_lab, false), black_lab);
        assertEquals(match_palette_lab(navy_lab, colors1_lab, false), navy_lab);
        assertEquals(match_palette_lab(blue_lab, colors1_lab, false), blue_lab);
        assertEquals(match_palette_lab(yellow_lab, colors1_lab, false), yellow_lab);
        assertEquals(match_palette_lab(gold_lab, colors1_lab, false), gold_lab);

        assertEquals(match_palette_lab(white_lab, colors2_lab, false), white_lab);
        assertEquals(match_palette_lab(black_lab, colors2_lab, false), black_lab);
        assertEquals(match_palette_lab(navy_lab, colors2_lab, false), blue_lab);
        assertEquals(match_palette_lab(blue_lab, colors2_lab, false), blue_lab);
        assertEquals(match_palette_lab(yellow_lab, colors2_lab, false), gold_lab);
        assertEquals(match_palette_lab(gold_lab, colors2_lab, false), gold_lab);
    }

    @Test
    public void should_match_map_palette_results_for_furthest() {
        assertEquals(match_palette_lab(white_lab, colors3_lab, true), black_lab);
        assertEquals(match_palette_lab(black_lab, colors3_lab, true), yellow_lab);
        assertEquals(match_palette_lab(navy_lab, colors3_lab, true), yellow_lab);
        assertEquals(match_palette_lab(blue_lab, colors3_lab, true), yellow_lab);
        assertEquals(match_palette_lab(yellow_lab, colors3_lab, true), blue_lab);
        assertEquals(match_palette_lab(gold_lab, colors3_lab, true), blue_lab);
    }
}