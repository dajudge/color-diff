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

import static java.lang.Math.PI;
import static java.lang.Math.abs;
import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.exp;
import static java.lang.Math.pow;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;

/**
 * Implementation of CIEDE2000.
 *
 * @author Alex Stockinger
 */
public class Diff {
    /**
     * Returns diff between c1 and c2 using the CIEDE2000 algorithm
     *
     * @param c1 Should have fields L,a,b
     * @param c2 Should have fields L,a,b
     * @return Difference between c1 and c2
     */
    public static double ciede2000(final LabColor c1, final LabColor c2) {
        /*
         * Implemented as in "The CIEDE2000 Color-Difference Formula:
         * Implementation Notes, Supplementary Test Data, and Mathematical Observations"
         * by Gaurav Sharma, Wencheng Wu and Edul N. Dalal.
         */

        // Get L,a,b values for color 1
        final double L1 = c1.L;
        final double a1 = c1.a;
        final double b1 = c1.b;

        // Get L,a,b values for color 2
        final double L2 = c2.L;
        final double a2 = c2.a;
        final double b2 = c2.b;

        // Weight factors
        final double kL = 1;
        final double kC = 1;
        final double kH = 1;

        /*
         * Step 1: Calculate C1p, C2p, h1p, h2p
         */
        final double C1 = sqrt(pow(a1, 2) + pow(b1, 2)); //(2)
        final double C2 = sqrt(pow(a2, 2) + pow(b2, 2)); //(2)

        final double a_C1_C2 = (C1 + C2) / 2.0;             //(3)

        final double G = 0.5 * (1 - sqrt(pow(a_C1_C2, 7.0) / (pow(a_C1_C2, 7.0) + pow(25.0, 7.0)))); //(4)

        final double a1p = (1.0 + G) * a1; //(5)
        final double a2p = (1.0 + G) * a2; //(5)

        final double C1p = sqrt(pow(a1p, 2) + pow(b1, 2)); //(6)
        final double C2p = sqrt(pow(a2p, 2) + pow(b2, 2)); //(6)

        final double h1p = hp_f(b1, a1p); //(7)
        final double h2p = hp_f(b2, a2p); //(7)

        /*
         * Step 2: Calculate dLp, dCp, dHp
         */
        final double dLp = L2 - L1; //(8)
        final double dCp = C2p - C1p; //(9)

        final double dhp = dhp_f(C1, C2, h1p, h2p); //(10)
        final double dHp = 2 * sqrt(C1p * C2p) * sin(radians(dhp) / 2.0); //(11)

        /*
         * Step 3: Calculate CIEDE2000 Color-Difference
         */
        final double a_L = (L1 + L2) / 2.0; //(12)
        final double a_Cp = (C1p + C2p) / 2.0; //(13)

        final double a_hp = a_hp_f(C1, C2, h1p, h2p); //(14)
        final double T = 1 - 0.17 * cos(radians(a_hp - 30)) + 0.24 * cos(radians(2 * a_hp)) + 0.32 * cos(radians(3 * a_hp + 6)) - 0.20 * cos(radians(4 * a_hp - 63)); //(15)
        final double d_ro = 30 * exp(-(pow((a_hp - 275) / 25, 2))); //(16)
        final double RC = sqrt((pow(a_Cp, 7.0)) / (pow(a_Cp, 7.0) + pow(25.0, 7.0)));//(17)
        final double SL = 1 + ((0.015 * pow(a_L - 50, 2)) / sqrt(20 + pow(a_L - 50, 2.0)));//(18)
        final double SC = 1 + 0.045 * a_Cp;//(19)
        final double SH = 1 + 0.015 * a_Cp * T;//(20)
        final double RT = -2 * RC * sin(radians(2 * d_ro));//(21)
        return sqrt(pow(dLp / (SL * kL), 2) + pow(dCp / (SC * kC), 2) + pow(dHp / (SH * kH), 2) + RT * (dCp / (SC * kC)) * (dHp / (SH * kH))); //(22)
    }

    private static double degrees(final double n) {
        return n * (180 / PI);
    }

    private static double radians(final double n) {
        return n * (PI / 180);
    }

    private static double hp_f(final double x, final double y) { //(7)
        if (x == 0 && y == 0) return 0;
        else {
            final double tmphp = degrees(atan2(x, y));
            if (tmphp >= 0) return tmphp;
            else return tmphp + 360;
        }
    }

    private static double dhp_f(final double C1, final double C2, final double h1p, final double h2p) { //(10)
        if (C1 * C2 == 0) return 0;
        else if (abs(h2p - h1p) <= 180) return h2p - h1p;
        else if ((h2p - h1p) > 180) return (h2p - h1p) - 360;
        else if ((h2p - h1p) < -180) return (h2p - h1p) + 360;
        else throw (new IllegalArgumentException());
    }

    private static double a_hp_f(final double C1, final double C2, final double h1p, final double h2p) { //(14)
        if (C1 * C2 == 0) return h1p + h2p;
        else if (abs(h1p - h2p) <= 180) return (h1p + h2p) / 2.0;
        else if ((abs(h1p - h2p) > 180) && ((h1p + h2p) < 360)) return (h1p + h2p + 360) / 2.0;
        else if ((abs(h1p - h2p) > 180) && ((h1p + h2p) >= 360)) return (h1p + h2p - 360) / 2.0;
        else throw (new IllegalArgumentException());
    }

}