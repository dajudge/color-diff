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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static com.dajudge.colordiff.Diff.ciede2000;
import static com.dajudge.colordiff.Palette.MapType.CLOSEST;
import static com.dajudge.colordiff.Palette.MapType.FURTHEST;

/**
 * Palette matching.
 *
 * @author Alex Stockinger
 */
public class Palette {

    /**
     * Returns the hash key used for a {@link Color} in a palette map.
     *
     * @param c        should have fields R,G,B
     * @return the map key.
     */
    public static String palette_map_key(final RgbColor c) {
        String s = "R" + c.r + "B" + c.g + "G" + c.b;
        if (c.a != null) {
            s = s + "A" + c.a;
        }
        return s;
    }

    /**
     * Returns the hash key used for a {@link LabColor} in a lap palette map.
     *
     * @param c should have fields L,a,b
     * @return the map key.
     */
    public static String lab_palette_map_key(final LabColor c) {
        return "L" + c.L + "a" + c.a + "b" + c.b;
    }

    /**
     * Returns a mapping from each color in a to the closest/farthest color in b using white as background color
     * using the 'closest' mapping type.
     *
     * @param a         each element should have fields R,G,B
     * @param b         each element should have fields R,G,B
     * @return palette map
     */
    public static Map<String, RgbColor> map_palette(
            final List<RgbColor> a,
            final List<RgbColor> b
    ) {
        return map_palette(a, b, CLOSEST);
    }

    /**
     * Returns a mapping from each color in a to the closest/farthest color in b using white as background color.
     *
     * @param a         each element should have fields R,G,B
     * @param b         each element should have fields R,G,B
     * @param type      should be the string 'closest' or 'furthest'
     * @return palette map
     */
    public static Map<String, RgbColor> map_palette(
            final List<RgbColor> a,
            final List<RgbColor> b,
            final MapType type
    ) {
        return map_palette(a, b, type, new RgbColor(255, 255, 255, 1.));
    }

    /**
     * Returns a mapping from each color in a to the closest/farthest color in b
     *
     * @param a         each element should have fields R,G,B
     * @param b         each element should have fields R,G,B
     * @param bc        background color used if <code>a</code> or <code>b</code> have an alpha component.
     * @param type      should be the string 'closest' or 'furthest'
     * @return palette map
     */
    public static Map<String, RgbColor> map_palette(
            final List<RgbColor> a,
            final List<RgbColor> b,
            final MapType type,
            final RgbColor bc
    ) {
        final Map<String, RgbColor> c = new HashMap<>();
        for (final RgbColor color1 : a) {
            RgbColor best_color = null;
            Double best_color_diff = null;
            for (final RgbColor color2 : b) {
                final double current_color_diff = diff(color1, color2, bc);

                if ((best_color == null) || ((type == CLOSEST) && (current_color_diff < best_color_diff))) {
                    best_color = color2;
                    best_color_diff = current_color_diff;
                    continue;
                }
                if ((type == FURTHEST) && (current_color_diff > best_color_diff)) {
                    best_color = color2;
                    best_color_diff = current_color_diff;
                }
            }
            c.put(palette_map_key(color1), best_color);
        }
        return c;
    }

    /**
     * Returns the closest (or furthest) color to target_color in palette, operating in the L,a,b
     * colorspace for performance.
     *
     * @param target_color  should have fields L,a,b
     * @param palette       each element should have fields L,a,b
     * @param find_furthest should be falsy to find the closest color
     * @return the closest (or furthest) color in the palette.
     */
    public static LabColor match_palette_lab(final LabColor target_color, List<LabColor> palette, boolean find_furthest) {
        LabColor color2;
        double current_color_diff;
        LabColor best_color = palette.get(0);
        double best_color_diff = ciede2000(target_color, best_color);
        for (int idx2 = 1, l = palette.size(); idx2 < l; idx2 += 1) {
            color2 = palette.get(idx2);
            current_color_diff = ciede2000(target_color, color2);

            if ((!find_furthest && (current_color_diff < best_color_diff)) || (find_furthest && (current_color_diff > best_color_diff))) {
                best_color = color2;
                best_color_diff = current_color_diff;
            }
        }
        return best_color;
    }

    /**
     * Returns a mapping from each color in a to the closest color in b.
     *
     * @param a    each element should have fields L,a,b
     * @param b    each element should have fields L,a,b
     * @param type should be the string 'closest' or 'furthest'
     * @return lab palette map.
     */
    public static Map<String, LabColor> map_palette_lab(final List<LabColor> a, final List<LabColor> b, final MapType type) {
        final Map<String, LabColor> c = new HashMap<>();
        final boolean find_furthest = type == FURTHEST;
        for (final LabColor color1 : a) {
            c.put(lab_palette_map_key(color1), match_palette_lab(color1, b, find_furthest));
        }
        return c;
    }

    private static double diff(
            final RgbColor c1,
            final RgbColor c2,
            final RgbColor bc
    ) {
        Function<RgbColor, LabColor> conv_c1 = Convert::rgb_to_lab;
        Function<RgbColor, LabColor> conv_c2 = Convert::rgb_to_lab;
        Function<RgbColor, LabColor> rgba_conv = c -> Convert.rgba_to_lab(c, bc);
        if (c1.a != null) {
            conv_c1 = rgba_conv;
        }
        if (c2.a != null) {
            conv_c2 = rgba_conv;
        }
        return ciede2000(conv_c1.apply(c1), conv_c2.apply(c2));
    }

    public enum MapType {
        CLOSEST, FURTHEST
    }
}
