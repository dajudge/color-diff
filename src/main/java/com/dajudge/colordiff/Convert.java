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

import static java.lang.Math.pow;

/**
 * Color conversion.
 *
 * @author Alex Stockinger
 */
public class Convert {
    /**
     * Returns c converted to labcolor using white as background color.
     *
     * @param color should have fields R,G,B,A
     * @return <code>color</code> converted to labcolor
     */
    public static LabColor rgba_to_lab(final RgbColor color) {
        return rgba_to_lab(color, new RgbColor(255, 255, 255));
    }

    /**
     * Returns c converted to labcolor.
     *
     * @param c  should have fields R,G,B,A
     * @param bc should have fields R,G,B
     * @return <code>c</code> converted to labcolor
     */
    public static LabColor rgba_to_lab(final RgbColor c, final RgbColor bc) {
        final RgbColor nc = new RgbColor(
                alphaScale(c.r, bc.r, c.a),
                alphaScale(c.g, bc.g, c.a),
                alphaScale(c.b, bc.b, c.a)
        );
        return rgb_to_lab(nc);
    }

    private static double alphaScale(final double v1, final double v2, final double a) {
        return v2 + (v1 - v2) * a;
    }

    /**
     * Returns c converted to labcolor.
     *
     * @param c should have fields R,G,B
     * @return <code>c</code> converted to labcolor
     */
    public static LabColor rgb_to_lab(final Color c) {
        return rgb_to_lab(new RgbColor(c));
    }

    /**
     * Returns c converted to labcolor.
     *
     * @param c should have fields R,G,B
     * @return <code>c</code> converted to labcolor
     */
    public static LabColor rgb_to_lab(final RgbColor c) {
        return xyz_to_lab(rgb_to_xyz(c));
    }

    private static XyzColor rgb_to_xyz(final RgbColor c) {
        // Based on http://www.easyrgb.com/index.php?X=MATH&H=02
        double r = (c.r / 255.0);
        double g = (c.g / 255.0);
        double b = (c.b / 255.0);

        if (r > 0.04045) {
            r = pow(((r + 0.055) / 1.055), 2.4);
        } else {
            r = r / 12.92;
        }
        if (g > 0.04045) {
            g = pow(((g + 0.055) / 1.055), 2.4);
        } else {
            g = g / 12.92;
        }
        if (b > 0.04045) {
            b = pow(((b + 0.055) / 1.055), 2.4);
        } else {
            b = b / 12.92;
        }

        r *= 100.0;
        g *= 100.0;
        b *= 100.0;

        // Observer. = 2°, Illuminant = D65
        final double x = r * 0.4124 + g * 0.3576 + b * 0.1805;
        final double y = r * 0.2126 + g * 0.7152 + b * 0.0722;
        final double z = r * 0.0193 + g * 0.1192 + b * 0.9505;
        return new XyzColor(x, y, z);
    }

    private static LabColor xyz_to_lab(final XyzColor c) {
        // Based on http://www.easyrgb.com/index.php?X=MATH&H=07
        final double ref_Y = 100.000;
        final double ref_Z = 108.883;
        final double ref_X = 95.047; // Observer= 2°, Illuminant= D65
        double y = c.y / ref_Y;
        double z = c.z / ref_Z;
        double x = c.x / ref_X;
        if (x > 0.008856) {
            x = pow(x, 1.0 / 3.0);
        } else {
            x = (7.787 * x) + (16.0 / 116.0);
        }
        if (y > 0.008856) {
            y = pow(y, 1.0 / 3.0);
        } else {
            y = (7.787 * y) + (16.0 / 116.0);
        }
        if (z > 0.008856) {
            z = pow(z, 1.0 / 3.0);
        } else {
            z = (7.787 * z) + (16.0 / 116.0);
        }
        final double l = (116.0 * y) - 16.0;
        final double a = 500.0 * (x - y);
        final double b = 200.0 * (y - z);
        return new LabColor(l, a, b);
    }

    private static class XyzColor {
        private final double x, y, z;

        private XyzColor(final double x, final double y, final double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }
}
