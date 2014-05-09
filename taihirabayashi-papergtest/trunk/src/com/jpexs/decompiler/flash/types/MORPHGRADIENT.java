/*
 *  Copyright (C) 2010-2014 JPEXS
 * 
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 * 
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.jpexs.decompiler.flash.types;

import com.jpexs.decompiler.flash.types.annotations.Internal;
import com.jpexs.decompiler.flash.types.annotations.SWFType;
import java.io.Serializable;

/**
 *
 * @author JPEXS
 */
public class MORPHGRADIENT implements Serializable {

    /**
     * Spread mode. See GRADIENT.SPREAD_* constants
     */
    @SWFType(value = BasicType.UB, count = 2)
    public int spreadMode;
    /**
     * Interpolation mode. See GRADIENT.INTERPOLATION_* constants
     */
    @SWFType(value = BasicType.UB, count = 2)
    public int interPolationMode;
    @Internal
    public int numGradients;

    public MORPHGRADRECORD[] gradientRecords;

    public static RGBA morphColor(RGBA c1, RGBA c2, int ratio) {
        int r = (c1.red + (c2.red - c1.red) * ratio / 65535);
        int g = (c1.green + (c2.green - c1.green) * ratio / 65535);
        int b = (c1.blue + (c2.blue - c1.blue) * ratio / 65535);
        int a = (c1.alpha + (c2.alpha - c1.alpha) * ratio / 65535);
        if (r > 255) {
            r = 255;
        }
        if (g > 255) {
            g = 255;
        }
        if (b > 255) {
            b = 255;
        }
        if (a > 255) {
            a = 255;
        }
        return new RGBA(r, g, b, a);
    }

    public GRADIENT getGradientAt(int ratio) {
        GRADIENT ret = new GRADIENT();
        ret.gradientRecords = new GRADRECORD[gradientRecords.length];
        for (int m = 0; m < gradientRecords.length; m++) {

            int gratio = (gradientRecords[m].startRatio + (gradientRecords[m].endRatio - gradientRecords[m].startRatio) * ratio / 65535);
            ret.gradientRecords[m] = new GRADRECORD();
            ret.gradientRecords[m].color = morphColor(gradientRecords[m].startColor, gradientRecords[m].endColor, ratio);
            ret.gradientRecords[m].ratio = gratio;
        }
        return ret;
    }

    public GRADIENT getStartGradient() {
        GRADIENT ret = new GRADIENT();
        ret.gradientRecords = new GRADRECORD[gradientRecords.length];
        for (int m = 0; m < gradientRecords.length; m++) {
            ret.gradientRecords[m] = gradientRecords[m].getStartRecord();
        }
        return ret;
    }

    public GRADIENT getEndGradient() {
        GRADIENT ret = new GRADIENT();
        ret.gradientRecords = new GRADRECORD[gradientRecords.length];
        for (int m = 0; m < gradientRecords.length; m++) {
            ret.gradientRecords[m] = gradientRecords[m].getEndRecord();
        }
        return ret;
    }
}
