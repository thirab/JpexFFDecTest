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
package com.jpexs.decompiler.flash.action.swf7;

import com.jpexs.decompiler.flash.action.Action;
import com.jpexs.decompiler.flash.action.model.ImplementsOpActionItem;
import com.jpexs.decompiler.graph.GraphTargetItem;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

public class ActionImplementsOp extends Action {

    public ActionImplementsOp() {
        super(0x2C, 0);
    }

    @Override
    public String toString() {
        return "ImplementsOp";
    }

    @Override
    public void translate(Stack<GraphTargetItem> stack, List<GraphTargetItem> output, java.util.HashMap<Integer, String> regNames, HashMap<String, GraphTargetItem> variables, HashMap<String, GraphTargetItem> functions, int staticOperation, String path) {
        GraphTargetItem subclass = stack.pop();
        long inCount = popLong(stack);
        List<GraphTargetItem> superclasses = new ArrayList<>();
        for (long l = 0; l < inCount; l++) {
            superclasses.add(stack.pop());
        }
        output.add(new ImplementsOpActionItem(this, subclass, superclasses));
    }
}
