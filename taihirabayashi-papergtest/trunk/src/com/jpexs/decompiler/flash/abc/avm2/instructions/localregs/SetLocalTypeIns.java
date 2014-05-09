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
package com.jpexs.decompiler.flash.abc.avm2.instructions.localregs;

import com.jpexs.decompiler.flash.abc.ABC;
import com.jpexs.decompiler.flash.abc.avm2.AVM2Code;
import com.jpexs.decompiler.flash.abc.avm2.ConstantPool;
import com.jpexs.decompiler.flash.abc.avm2.instructions.AVM2Instruction;
import com.jpexs.decompiler.flash.abc.avm2.instructions.InstructionDefinition;
import com.jpexs.decompiler.flash.abc.avm2.instructions.SetTypeIns;
import com.jpexs.decompiler.flash.abc.avm2.model.AVM2Item;
import com.jpexs.decompiler.flash.abc.avm2.model.DecrementAVM2Item;
import com.jpexs.decompiler.flash.abc.avm2.model.FindPropertyAVM2Item;
import com.jpexs.decompiler.flash.abc.avm2.model.IncrementAVM2Item;
import com.jpexs.decompiler.flash.abc.avm2.model.LocalRegAVM2Item;
import com.jpexs.decompiler.flash.abc.avm2.model.NewActivationAVM2Item;
import com.jpexs.decompiler.flash.abc.avm2.model.PostDecrementAVM2Item;
import com.jpexs.decompiler.flash.abc.avm2.model.PostIncrementAVM2Item;
import com.jpexs.decompiler.flash.abc.avm2.model.SetLocalAVM2Item;
import com.jpexs.decompiler.flash.abc.avm2.model.operations.PreDecrementAVM2Item;
import com.jpexs.decompiler.flash.abc.avm2.model.operations.PreIncrementAVM2Item;
import com.jpexs.decompiler.flash.abc.types.MethodBody;
import com.jpexs.decompiler.flash.abc.types.MethodInfo;
import com.jpexs.decompiler.graph.GraphTargetItem;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

public abstract class SetLocalTypeIns extends InstructionDefinition implements SetTypeIns {

    public SetLocalTypeIns(int instructionCode, String instructionName, int[] operands) {
        super(instructionCode, instructionName, operands);
    }

    @Override
    public void translate(boolean isStatic, int scriptIndex, int classIndex, java.util.HashMap<Integer, GraphTargetItem> localRegs, Stack<GraphTargetItem> stack, java.util.Stack<GraphTargetItem> scopeStack, ConstantPool constants, AVM2Instruction ins, List<MethodInfo> method_info, List<GraphTargetItem> output, MethodBody body, ABC abc, HashMap<Integer, String> localRegNames, List<String> fullyQualifiedNames, String path, HashMap<Integer, Integer> regAssignCount, int ip, HashMap<Integer, List<Integer>> refs, AVM2Code code) {
        int regId = getRegisterId(ins);
        GraphTargetItem value = (GraphTargetItem) stack.pop();
        /*if (localRegs.containsKey(regId)) {
         localRegs.put(regId, new NotCompileTimeAVM2Item(ins, value));
         } else {
         localRegs.put(regId, value);
         }*/
        localRegs.put(regId, value);
        if (!regAssignCount.containsKey(regId)) {
            regAssignCount.put(regId, 0);
        }
        regAssignCount.put(regId, regAssignCount.get(regId) + 1);
        //localRegsAssignmentIps.put(regId, ip);
        if (value instanceof NewActivationAVM2Item) {
            return;
        }
        if (value instanceof FindPropertyAVM2Item) {
            return;
        }
        if (value.getNotCoerced() instanceof IncrementAVM2Item) {
            GraphTargetItem inside = ((IncrementAVM2Item) value.getNotCoerced()).value.getNotCoerced().getThroughDuplicate();
            if (inside instanceof LocalRegAVM2Item) {
                if (((LocalRegAVM2Item) inside).regIndex == regId) {
                    if (stack.size() > 0) {
                        GraphTargetItem top = stack.peek().getNotCoerced().getThroughDuplicate();
                        if (top == inside) {
                            stack.pop();
                            stack.push(new PostIncrementAVM2Item(ins, inside));
                        } else if ((top instanceof IncrementAVM2Item) && (((IncrementAVM2Item) top).value == inside)) {
                            stack.pop();
                            stack.push(new PreIncrementAVM2Item(ins, inside));
                        } else {
                            output.add(new PostIncrementAVM2Item(ins, inside));
                        }
                    } else {
                        output.add(new PostIncrementAVM2Item(ins, inside));
                    }
                    return;
                }
            }
        }

        if (value.getNotCoerced() instanceof DecrementAVM2Item) {
            GraphTargetItem inside = ((DecrementAVM2Item) value.getNotCoerced()).value.getNotCoerced().getThroughDuplicate();
            if (inside instanceof LocalRegAVM2Item) {
                if (((LocalRegAVM2Item) inside).regIndex == regId) {
                    if (stack.size() > 0) {
                        GraphTargetItem top = stack.peek().getNotCoerced().getThroughDuplicate();
                        if (top == inside) {
                            stack.pop();
                            stack.push(new PostDecrementAVM2Item(ins, inside));
                        } else if ((top instanceof DecrementAVM2Item) && (((DecrementAVM2Item) top).value == inside)) {
                            stack.pop();
                            stack.push(new PreDecrementAVM2Item(ins, inside));
                        } else {
                            output.add(new PostDecrementAVM2Item(ins, inside));
                        }
                    } else {
                        output.add(new PostDecrementAVM2Item(ins, inside));
                    }
                    return;
                }
            }
        }

        //if(val.startsWith("catchscope ")) return;
        //if(val.startsWith("newactivation()")) return;
        output.add(new SetLocalAVM2Item(ins, regId, value));
    }

    @Override
    public String getObject(Stack<AVM2Item> stack, ABC abc, AVM2Instruction ins, List<AVM2Item> output, MethodBody body, HashMap<Integer, String> localRegNames, List<String> fullyQualifiedNames) {
        return AVM2Item.localRegName(localRegNames, getRegisterId(ins));
    }

    @Override
    public int getStackDelta(AVM2Instruction ins, ABC abc) {
        return -1;
    }

    public abstract int getRegisterId(AVM2Instruction ins);
}
