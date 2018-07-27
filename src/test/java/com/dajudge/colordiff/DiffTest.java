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

import static com.dajudge.colordiff.Diff.ciede2000;
import static java.lang.Double.NaN;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests for {@link Diff}.
 *
 * @author Alex Stockinger
 */
class DiffTest {
    static void assert_ciede2000_diff(final double expected, final LabColor c1, final LabColor c2) {
        assertEquals(expected, round(ciede2000(c1, c2)));
        assertEquals(expected, round(ciede2000(c2, c1)));
    }

    private LabColor l(final double l, final double a, final double b) {
        return new LabColor(l, a, b);
    }

    private static double round(final double n) {
        return Math.round(n * 10000.0) / 10000.0;
    }

    @Test
    public void should_use_true_chroma_difference_1() {
        assert_ciede2000_diff(2.0425, l(50.0000, 2.6772, -79.7751), l(50.0000, 0.0000, -82.7485));
    }

    @Test
    public void should_use_true_chroma_difference_2() {
        assert_ciede2000_diff(2.8615, l(50.0000, 3.1571, -77.2803), l(50.0000, 0.0000, -82.7485));
    }

    @Test
    public void should_use_true_chroma_difference_3() {
        assert_ciede2000_diff(3.4412, l(50.0000, 2.8361, -74.0200), l(50.0000, 0.0000, -82.7485));
    }

    @Test
    public void should_use_the_true_hue_difference_4() {
        assert_ciede2000_diff(1.0000, l(50.0000, -1.3802, -84.2814), l(50.0000, 0.0000, -82.7485));
    }

    @Test
    public void should_use_the_true_hue_difference_5() {
        assert_ciede2000_diff(1.0000, l(50.0000, -1.1848, -84.8006), l(50.0000, 0.0000, -82.7485));
    }

    @Test
    public void should_use_the_true_hue_difference_6() {
        assert_ciede2000_diff(1.0000, l(50.0000, -0.9009, -85.5211), l(50.0000, 0.0000, -82.7485));
    }

    @Test
    public void should_use_the_correct_arctangent_computation_7() {
        assert_ciede2000_diff(2.3669, l(50.0000, 0.0000, 0.0000), l(50.0000, -1.0000, 2.0000));
    }

    @Test
    public void should_use_the_correct_arctangent_computation_8() {
        assert_ciede2000_diff(2.3669, l(50.0000, -1.0000, 2.0000), l(50.0000, 0.0000, 0.0000));
    }

    @Test
    public void should_use_the_correct_arctangent_computation_9() {
        assert_ciede2000_diff(7.1792, l(50.0000, 2.4900, -0.0010), l(50.0000, -2.4900, 0.0009));
    }

    @Test
    public void should_use_the_correct_arctangent_computation_10() {
        assert_ciede2000_diff(7.1792, l(50.0000, 2.4900, -0.0010), l(50.0000, -2.4900, 0.0010));
    }

    @Test
    public void should_use_the_correct_arctangent_computation_11() {
        assert_ciede2000_diff(7.2195, l(50.0000, 2.4900, -0.0010), l(50.0000, -2.4900, 0.0011));
    }

    @Test
    public void should_use_the_correct_arctangent_computation_12() {
        assert_ciede2000_diff(7.2195, l(50.0000, 2.4900, -0.0010), l(50.0000, -2.4900, 0.0012));
    }

    @Test
    public void should_use_the_correct_arctangent_computation_13() {
        assert_ciede2000_diff(4.8045, l(50.0000, -0.0010, 2.4900), l(50.0000, 0.0009, -2.4900));
    }

    @Test
    public void should_use_the_correct_arctangent_computation_14() {
        assert_ciede2000_diff(4.8045, l(50.0000, -0.0010, 2.4900), l(50.0000, 0.0010, -2.4900));
    }

    @Test
    public void should_use_the_correct_arctangent_computation_15() {
        assert_ciede2000_diff(4.7461, l(50.0000, -0.0010, 2.4900), l(50.0000, 0.0011, -2.4900));
    }

    @Test
    public void should_use_the_correct_arctangent_computation_16() {
        assert_ciede2000_diff(4.3065, l(50.0000, 2.5000, 0.0000), l(50.0000, 0.0000, -2.5000));
    }

    @Test
    public void should_work_for_large_color_differences_17() {
        assert_ciede2000_diff(27.1492, l(50.0000, 2.5000, 0.0000), l(73.0000, 25.0000, -18.0000));
    }

    @Test
    public void should_work_for_large_color_differences_18() {
        assert_ciede2000_diff(22.8977, l(50.0000, 2.5000, 0.0000), l(61.0000, -5.0000, 29.0000));
    }

    @Test
    public void should_work_for_large_color_differences_19() {
        assert_ciede2000_diff(31.9030, l(50.0000, 2.5000, 0.0000), l(56.0000, -27.0000, -3.0000));
    }

    @Test
    public void should_work_for_large_color_differences_20() {
        assert_ciede2000_diff(19.4535, l(50.0000, 2.5000, 0.0000), l(58.0000, 24.0000, 15.0000));
    }

    @Test
    public void should_produce_numbers_found_in_the_CIE_technical_report_21() {
        assert_ciede2000_diff(1.0000, l(50.0000, 2.5000, 0.0000), l(50.0000, 3.1736, 0.5854));
    }

    @Test
    public void should_produce_numbers_found_in_the_CIE_technical_report_22() {
        assert_ciede2000_diff(1.0000, l(50.0000, 2.5000, 0.0000), l(50.0000, 3.2972, 0.0000));
    }

    @Test
    public void should_produce_numbers_found_in_the_CIE_technical_report_23() {
        assert_ciede2000_diff(1.0000, l(50.0000, 2.5000, 0.0000), l(50.0000, 1.8634, 0.5757));
    }

    @Test
    public void should_produce_numbers_found_in_the_CIE_technical_report_24() {
        assert_ciede2000_diff(1.0000, l(50.0000, 2.5000, 0.0000), l(50.0000, 3.2592, 0.3350));
    }

    @Test
    public void should_produce_numbers_found_in_the_CIE_technical_report_25() {
        assert_ciede2000_diff(1.2644, l(60.2574, -34.0099, 36.2677), l(60.4626, -34.1751, 39.4387));
    }

    @Test
    public void should_produce_numbers_found_in_the_CIE_technical_report_26() {
        assert_ciede2000_diff(1.2630, l(63.0109, -31.0961, -5.8663), l(62.8187, -29.7946, -4.0864));
    }

    @Test
    public void should_produce_numbers_found_in_the_CIE_technical_report_27() {
        assert_ciede2000_diff(1.8731, l(61.2901, 3.7196, -5.3901), l(61.4292, 2.2480, -4.9620));
    }

    @Test
    public void should_produce_numbers_found_in_the_CIE_technical_report_28() {
        assert_ciede2000_diff(1.8645, l(35.0831, -44.1164, 3.7933), l(35.0232, -40.0716, 1.5901));
    }

    @Test
    public void should_produce_numbers_found_in_the_CIE_technical_report_29() {
        assert_ciede2000_diff(2.0373, l(22.7233, 20.0904, -46.6940), l(23.0331, 14.9730, -42.5619));
    }

    @Test
    public void should_produce_numbers_found_in_the_CIE_technical_report_30() {
        assert_ciede2000_diff(1.4146, l(36.4612, 47.8580, 18.3852), l(36.2715, 50.5065, 21.2231));
    }

    @Test
    public void should_produce_numbers_found_in_the_CIE_technical_report_31() {
        assert_ciede2000_diff(1.4441, l(90.8027, -2.0831, 1.4410), l(91.1528, -1.6435, 0.0447));
    }

    @Test
    public void should_produce_numbers_found_in_the_CIE_technical_report_32() {
        assert_ciede2000_diff(1.5381, l(90.9257, -0.5406, -0.9208), l(88.6381, -0.8985, -0.7239));
    }

    @Test
    public void should_produce_numbers_found_in_the_CIE_technical_report_33() {
        assert_ciede2000_diff(0.6377, l(6.7747, -0.2908, -2.4247), l(5.8714, -0.0985, -2.2286));
    }

    @Test
    public void should_produce_numbers_found_in_the_CIE_technical_report_34() {
        assert_ciede2000_diff(0.9082, l(2.0776, 0.0795, -1.1350), l(0.9033, -0.0636, -0.5514));
    }

    @Test
    public void same_color_should_have_0_difference_1() {
        assert_ciede2000_diff(0.0, l(100, 0.005, -0.010), l(100, 0.005, -0.010));
    }

    @Test
    public void same_color_should_have_0_difference_2() {
        assert_ciede2000_diff(0.0, l(0.0, 0.0, 0.0), l(0.0, 0.0, 0.0));
    }

    @Test
    public void black_and_white_are_very_different() {
        assert_ciede2000_diff(100.0, l(100, 0.005, -0.010), l(0.0, 0.0, 0.0));
    }

    @Test
    public void throws_error() {
        assertThrows(IllegalArgumentException.class, () -> {
            ciede2000(l(NaN, NaN, NaN), l(0, 0, 0));
        });
    }
}