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
package com.jpexs.decompiler.flash.abc.avm2;

import com.jpexs.decompiler.flash.abc.avm2.instructions.AVM2Instruction;

/**
 *
 * @author JPEXS
 */
public class InstructionStats {

    public boolean seen = false;
    public int stackpos = 0;
    public int scopepos = 0;
    public int stackpos_after = 0;
    public int scopepos_after = 0;
    public AVM2Instruction ins;

    public InstructionStats(AVM2Instruction ins) {
        this.ins = ins;
    }
}
