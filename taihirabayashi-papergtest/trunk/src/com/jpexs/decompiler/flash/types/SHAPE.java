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

import com.jpexs.decompiler.flash.exporters.shape.PathExporter;
import com.jpexs.decompiler.flash.tags.base.NeedsCharacters;
import com.jpexs.decompiler.flash.types.annotations.SWFType;
import com.jpexs.decompiler.flash.types.shaperecords.SHAPERECORD;
import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.geom.GeneralPath;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author JPEXS
 */
public class SHAPE implements NeedsCharacters, Serializable {

    @SWFType(value = BasicType.UB, count = 4)
    public int numFillBits;
    @SWFType(value = BasicType.UB, count = 4)
    public int numLineBits;
    public List<SHAPERECORD> shapeRecords;

    @Override
    public Set<Integer> getNeededCharacters() {
        Set<Integer> ret = new HashSet<>();
        for (SHAPERECORD r : shapeRecords) {
            ret.addAll(r.getNeededCharacters());
        }
        return ret;
    }

    public RECT getBounds() {
        return SHAPERECORD.getBounds(shapeRecords);
    }

    public Shape getOutline() {
        List<GeneralPath> paths = PathExporter.export(this);
        Area area = new Area();
        for (GeneralPath path : paths) {
            area.add(new Area(path));
        }

        return area;
    }
}
