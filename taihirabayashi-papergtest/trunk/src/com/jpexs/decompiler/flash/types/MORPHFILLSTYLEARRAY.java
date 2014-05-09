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

import com.jpexs.decompiler.flash.tags.base.NeedsCharacters;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author JPEXS
 */
public class MORPHFILLSTYLEARRAY implements NeedsCharacters, Serializable {

    public MORPHFILLSTYLE[] fillStyles;

    @Override
    public Set<Integer> getNeededCharacters() {
        HashSet<Integer> ret = new HashSet<>();
        for (MORPHFILLSTYLE fs : fillStyles) {
            ret.addAll(fs.getNeededCharacters());
        }
        return ret;
    }

    public FILLSTYLEARRAY getFillStylesAt(int ratio) {
        FILLSTYLEARRAY ret = new FILLSTYLEARRAY();
        ret.fillStyles = new FILLSTYLE[fillStyles.length];
        for (int m = 0; m < fillStyles.length; m++) {
            ret.fillStyles[m] = fillStyles[m].getFillStyleAt(ratio);
        }
        return ret;
    }

    public FILLSTYLEARRAY getStartFillStyles() {
        FILLSTYLEARRAY ret = new FILLSTYLEARRAY();
        ret.fillStyles = new FILLSTYLE[fillStyles.length];
        for (int m = 0; m < fillStyles.length; m++) {
            ret.fillStyles[m] = fillStyles[m].getStartFillStyle();
        }
        return ret;
    }

    public FILLSTYLEARRAY getEndFillStyles() {
        FILLSTYLEARRAY ret = new FILLSTYLEARRAY();
        ret.fillStyles = new FILLSTYLE[fillStyles.length];
        for (int m = 0; m < fillStyles.length; m++) {
            ret.fillStyles[m] = fillStyles[m].getEndFillStyle();
        }
        return ret;
    }
}
