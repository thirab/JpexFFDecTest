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

import com.jpexs.decompiler.flash.types.annotations.SWFType;
import java.io.Serializable;

/**
 * Represents 15-bit red, green and blue value
 *
 * @author JPEXS
 */
public class PIX15 implements Serializable {

    /**
     * Red color value
     */
    @SWFType(value = BasicType.UB, count = 5)
    public int red;
    /**
     * Green color value
     */
    @SWFType(value = BasicType.UB, count = 5)
    public int green;
    /**
     * Blue color value
     */
    @SWFType(value = BasicType.UB, count = 5)
    public int blue;
}
