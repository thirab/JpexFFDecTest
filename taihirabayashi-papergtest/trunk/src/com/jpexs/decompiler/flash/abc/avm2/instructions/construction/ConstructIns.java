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
package com.jpexs.decompiler.flash.abc.avm2.instructions.construction;

import com.jpexs.decompiler.flash.abc.ABC;
import com.jpexs.decompiler.flash.abc.avm2.AVM2Code;
import com.jpexs.decompiler.flash.abc.avm2.ConstantPool;
import com.jpexs.decompiler.flash.abc.avm2.LocalDataArea;
import com.jpexs.decompiler.flash.abc.avm2.instructions.AVM2Instruction;
import com.jpexs.decompiler.flash.abc.avm2.instructions.InstructionDefinition;
import com.jpexs.decompiler.flash.abc.avm2.model.ConstructAVM2Item;
import com.jpexs.decompiler.flash.abc.avm2.model.EscapeXAttrAVM2Item;
import com.jpexs.decompiler.flash.abc.avm2.model.EscapeXElemAVM2Item;
import com.jpexs.decompiler.flash.abc.avm2.model.FindPropertyAVM2Item;
import com.jpexs.decompiler.flash.abc.avm2.model.FullMultinameAVM2Item;
import com.jpexs.decompiler.flash.abc.avm2.model.GetLexAVM2Item;
import com.jpexs.decompiler.flash.abc.avm2.model.GetPropertyAVM2Item;
import com.jpexs.decompiler.flash.abc.avm2.model.StringAVM2Item;
import com.jpexs.decompiler.flash.abc.avm2.model.XMLAVM2Item;
import com.jpexs.decompiler.flash.abc.avm2.model.operations.AddAVM2Item;
import com.jpexs.decompiler.flash.abc.types.MethodBody;
import com.jpexs.decompiler.flash.abc.types.MethodInfo;
import com.jpexs.decompiler.graph.GraphTargetItem;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

public class ConstructIns extends InstructionDefinition {

    public ConstructIns() {
        super(0x42, "construct", new int[]{AVM2Code.DAT_ARG_COUNT});
    }

    @Override
    public void execute(LocalDataArea lda, ConstantPool constants, List<Object> arguments) {
        /*int argCount = (int) ((Long) arguments.get(0)).longValue();
         List<Object> passArguments = new ArrayList<Object>();
         for (int i = argCount - 1; i >= 0; i--) {
         passArguments.set(i, lda.operandStack.pop());
         }
         Object obj = lda.operandStack.pop();*/
        throw new RuntimeException("Cannot call constructor");
        //call construct property of obj
        //push new instance
    }

    public static boolean walkXML(GraphTargetItem item, List<GraphTargetItem> list) {
        boolean ret = true;
        if (item instanceof StringAVM2Item) {
            list.add(item);
        } else if (item instanceof AddAVM2Item) {
            ret = ret && walkXML(((AddAVM2Item) item).leftSide, list);
            ret = ret && walkXML(((AddAVM2Item) item).rightSide, list);
        } else if ((item instanceof EscapeXElemAVM2Item) || (item instanceof EscapeXAttrAVM2Item)) {
            list.add(item);
        } else {
            return false;
        }
        return ret;
    }

    @Override
    public void translate(boolean isStatic, int scriptIndex, int classIndex, java.util.HashMap<Integer, GraphTargetItem> localRegs, Stack<GraphTargetItem> stack, java.util.Stack<GraphTargetItem> scopeStack, ConstantPool constants, AVM2Instruction ins, List<MethodInfo> method_info, List<GraphTargetItem> output, MethodBody body, ABC abc, HashMap<Integer, String> localRegNames, List<String> fullyQualifiedNames, String path, HashMap<Integer, Integer> localRegsAssignmentIps, int ip, HashMap<Integer, List<Integer>> refs, AVM2Code code) throws InterruptedException {
        int argCount = ins.operands[0];
        List<GraphTargetItem> args = new ArrayList<>();
        for (int a = 0; a < argCount; a++) {
            args.add(0, (GraphTargetItem) stack.pop());
        }
        GraphTargetItem obj = (GraphTargetItem) stack.pop();

        FullMultinameAVM2Item xmlMult = null;
        boolean isXML = false;
        if (obj instanceof GetPropertyAVM2Item) {
            GetPropertyAVM2Item gpt = (GetPropertyAVM2Item) obj;
            if (gpt.object instanceof FindPropertyAVM2Item) {
                FindPropertyAVM2Item fpt = (FindPropertyAVM2Item) gpt.object;
                xmlMult = (FullMultinameAVM2Item) fpt.propertyName;
                isXML = xmlMult.isXML(constants, localRegNames, fullyQualifiedNames) && xmlMult.isXML(constants, localRegNames, fullyQualifiedNames);
            }
        }
        if (obj instanceof GetLexAVM2Item) {
            GetLexAVM2Item glt = (GetLexAVM2Item) obj;
            isXML = glt.propertyName.getName(constants, fullyQualifiedNames).equals("XML");
        }

        if (isXML) {
            if (args.size() == 1) {
                GraphTargetItem arg = args.get(0);
                List<GraphTargetItem> xmlLines = new ArrayList<>();
                if (walkXML(arg, xmlLines)) {
                    stack.push(new XMLAVM2Item(ins, xmlLines));
                    return;
                }
            }
        }

        stack.push(new ConstructAVM2Item(ins, obj, args));
    }

    @Override
    public int getStackDelta(AVM2Instruction ins, ABC abc) {
        return -ins.operands[0] - 1 + 1;
    }
}
