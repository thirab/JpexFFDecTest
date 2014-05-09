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

import com.jpexs.decompiler.flash.action.Action;
import com.jpexs.decompiler.flash.action.StoreTypeAction;
import com.jpexs.decompiler.flash.action.model.ConstantPool;
import com.jpexs.decompiler.flash.action.model.DecrementActionItem;
import com.jpexs.decompiler.flash.action.model.GetVariableActionItem;
import com.jpexs.decompiler.flash.action.model.IncrementActionItem;
import com.jpexs.decompiler.flash.action.model.PostDecrementActionItem;
import com.jpexs.decompiler.flash.action.model.PostIncrementActionItem;
import com.jpexs.decompiler.flash.action.model.SetVariableActionItem;
import com.jpexs.decompiler.flash.action.model.StoreRegisterActionItem;
import com.jpexs.decompiler.flash.action.model.TemporaryRegister;
import com.jpexs.decompiler.flash.action.model.operations.PreDecrementActionItem;
import com.jpexs.decompiler.flash.action.model.operations.PreIncrementActionItem;
import com.jpexs.decompiler.graph.GraphTargetItem;
import com.jpexs.decompiler.graph.model.LocalData;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

public class ActionSetVariable extends Action implements StoreTypeAction {

    public ActionSetVariable() {
        super(0x1D, 0);
    }

    @Override
    public String toString() {
        return "SetVariable";
    }

    @Override
    public void translate(Stack<GraphTargetItem> stack, List<GraphTargetItem> output, java.util.HashMap<Integer, String> regNames, HashMap<String, GraphTargetItem> variables, HashMap<String, GraphTargetItem> functions, int staticOperation, String path) {
        GraphTargetItem value = stack.pop().getThroughDuplicate();
        GraphTargetItem name = stack.pop();
        variables.put(name.toStringNoQuotes(LocalData.empty), value);
        if (value instanceof IncrementActionItem) {
            GraphTargetItem obj = ((IncrementActionItem) value).object;
            if (!stack.isEmpty()) {
                if (stack.peek().valueEquals(obj)) {
                    stack.pop();
                    stack.push(new PostIncrementActionItem(this, obj));
                    return;
                }
            }
        }
        if (value instanceof DecrementActionItem) {
            GraphTargetItem obj = ((DecrementActionItem) value).object;
            if (!stack.isEmpty()) {
                if (stack.peek().valueEquals(obj)) {
                    stack.pop();
                    stack.push(new PostDecrementActionItem(this, obj));
                    return;
                }
            }
        }
        if (value instanceof IncrementActionItem) {
            if (((IncrementActionItem) value).object instanceof GetVariableActionItem) {
                if (((GetVariableActionItem) ((IncrementActionItem) value).object).name.valueEquals(name)) {
                    output.add(new PostIncrementActionItem(this, ((IncrementActionItem) value).object));
                    return;
                }
            }
        }
        if (value instanceof DecrementActionItem) {
            if (((DecrementActionItem) value).object instanceof GetVariableActionItem) {
                if (((GetVariableActionItem) ((DecrementActionItem) value).object).name.valueEquals(name)) {
                    output.add(new PostDecrementActionItem(this, ((DecrementActionItem) value).object));
                    return;
                }
            }
        }

        GraphTargetItem ret = new SetVariableActionItem(this, name, value);

        if (value instanceof StoreRegisterActionItem) {
            StoreRegisterActionItem sr = (StoreRegisterActionItem) value;
            if (sr.define) {
                value = sr.getValue();
                ((SetVariableActionItem) ret).setValue(value);
                if (value instanceof IncrementActionItem) {
                    if (((IncrementActionItem) value).object instanceof GetVariableActionItem) {
                        if (((GetVariableActionItem) ((IncrementActionItem) value).object).name.valueEquals(name)) {
                            ret = new PreIncrementActionItem(this, ((IncrementActionItem) value).object);
                        }
                    }
                } else if (value instanceof DecrementActionItem) {
                    if (((DecrementActionItem) value).object instanceof GetVariableActionItem) {
                        if (((GetVariableActionItem) ((DecrementActionItem) value).object).name.valueEquals(name)) {
                            ret = new PreDecrementActionItem(this, ((DecrementActionItem) value).object);
                        }
                    }
                } else {
                    sr.temporary = true;
                    ((SetVariableActionItem) ret).setValue(sr);
                }

                variables.put("__register" + sr.register.number, new TemporaryRegister(sr.register.number, ret));
                return;
            }
        }

        output.add(ret);
    }

    @Override
    public String getVariableName(Stack<GraphTargetItem> stack, ConstantPool cpool) {
        if (stack.size() < 2) {
            return null;
        }
        return stack.get(stack.size() - 2).toStringNoQuotes(LocalData.create(cpool));
    }
}
