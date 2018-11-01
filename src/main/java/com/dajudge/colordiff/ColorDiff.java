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

import java.awt.*;
import java.util.List;
import java.util.Map;

import static com.dajudge.colordiff.Palette.MapType.CLOSEST;
import static com.dajudge.colordiff.Palette.MapType.FURTHEST;
import static java.util.Arrays.asList;

/**
 * Utility class for easy access to color-diff functionality.
 *
 * @author Alex Stockinger
 */
public class ColorDiff {
    public static String palette_map_key(final RgbColor c) {
        return Palette.palette_map_key(c);
    }

    public static String lab_palette_map_key(final LabColor c) {
        return Palette.lab_palette_map_key(c);
    }

    public static Map<String, RgbColor> map_palette(
            final List<RgbColor> a,
            final List<RgbColor> b
    ) {
        return Palette.map_palette(a, b);
    }

    public static Map<String, RgbColor> map_palette(
            final List<RgbColor> a,
            final List<RgbColor> b,
            final Palette.MapType type
    ) {
        return Palette.map_palette(a, b, type);
    }

    public static Map<String, RgbColor> map_palette(
            final List<RgbColor> a,
            final List<RgbColor> b,
            final Palette.MapType type,
            final RgbColor bc
    ) {
        return Palette.map_palette(a, b, type, bc);
    }

    public static LabColor match_palette_lab(final LabColor target_color, List<LabColor> palette, boolean find_furthest) {
        return Palette.match_palette_lab(target_color, palette, find_furthest);
    }

    public static Map<String, LabColor> map_palette_lab(final List<LabColor> a, final List<LabColor> b, final Palette.MapType type) {
        return Palette.map_palette_lab(a, b, type);
    }

    public static LabColor rgba_to_lab(final RgbColor color) {
        return Convert.rgba_to_lab(color);
    }

    public static LabColor rgba_to_lab(final RgbColor c, final RgbColor bc) {
        return Convert.rgba_to_lab(c, bc);
    }

    public static LabColor rgb_to_lab(final Color c) {
        return Convert.rgb_to_lab(c);
    }

    public static LabColor rgb_to_lab(final RgbColor c) {
        return Convert.rgb_to_lab(c);
    }

    public static double diff(final LabColor c1, final LabColor c2) {
        return Diff.ciede2000(c1, c2);
    }

    public static RgbColor closest(
            final RgbColor color,
            final List<RgbColor> palette
    ) {
        return closest(color, palette, new RgbColor(255, 255, 255));
    }

    public static RgbColor closest(
            final RgbColor color,
            final List<RgbColor> palette,
            final RgbColor bc
    ) {
        final String key = palette_map_key(color);
        final Map<String, RgbColor> result = map_palette(
                asList(color),
                palette,
                CLOSEST,
                bc
        );
        return result.get(key);
    }

    public static RgbColor furthest(
            final RgbColor color,
            final List<RgbColor> palette
    ) {
        return furthest(color, palette, new RgbColor(255, 255, 255));
    }

    public static RgbColor furthest(
            final RgbColor color,
            final List<RgbColor> palette,
            final RgbColor bc
    ) {
        final String key = palette_map_key(color);
        final Map<String, RgbColor> result = map_palette(
                asList(color),
                palette,
                FURTHEST,
                bc
        );
        return result.get(key);
    }

    public static LabColor closest_lab(final LabColor color, final List<LabColor> palette) {
        return match_palette_lab(color, palette, false);
    }

    public static LabColor furthest_lab(final LabColor color, final List<LabColor> palette) {
        return match_palette_lab(color, palette, true);
    }
}
