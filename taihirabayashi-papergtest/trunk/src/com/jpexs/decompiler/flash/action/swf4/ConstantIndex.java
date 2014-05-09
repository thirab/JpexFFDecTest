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
package com.jpexs.decompiler.flash.action.swf4;

import com.jpexs.decompiler.flash.configuration.Configuration;
import com.jpexs.helpers.Helper;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ConstantIndex implements Serializable {

    public int index;
    public List<String> constantPool;

    public ConstantIndex(int index) {
        this.index = index;
        this.constantPool = new ArrayList<>();
    }

    public ConstantIndex(int index, List<String> constantPool) {
        this.index = index;
        this.constantPool = constantPool;
    }

    public String toStringNoQ() {
        if (Configuration.resolveConstants.get()) {
            if (constantPool != null) {
                if (index < constantPool.size()) {
                    return constantPool.get(index);
                }
            }
        }
        return "constant" + index;
    }

    @Override
    public String toString() {
        if (Configuration.resolveConstants.get()) {
            if (constantPool != null) {
                if (index < constantPool.size()) {
                    return "\"" + Helper.escapeString(constantPool.get(index)) + "\"";
                }
            }
        }
        return "constant" + index;
    }
}
