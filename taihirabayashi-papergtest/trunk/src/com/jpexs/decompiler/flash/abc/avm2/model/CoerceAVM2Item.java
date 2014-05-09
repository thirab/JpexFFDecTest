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
package com.jpexs.decompiler.flash.abc.avm2.model;

import com.jpexs.decompiler.flash.SourceGeneratorLocalData;
import com.jpexs.decompiler.flash.abc.avm2.instructions.AVM2Instruction;
import com.jpexs.decompiler.flash.abc.avm2.instructions.types.CoerceAIns;
import com.jpexs.decompiler.flash.abc.avm2.instructions.types.CoerceIns;
import com.jpexs.decompiler.flash.abc.avm2.instructions.types.CoerceSIns;
import com.jpexs.decompiler.flash.abc.avm2.instructions.types.ConvertBIns;
import com.jpexs.decompiler.flash.abc.avm2.instructions.types.ConvertDIns;
import com.jpexs.decompiler.flash.abc.avm2.instructions.types.ConvertIIns;
import com.jpexs.decompiler.flash.abc.avm2.instructions.types.ConvertUIns;
import com.jpexs.decompiler.flash.abc.avm2.parser.script.AVM2SourceGenerator;
import com.jpexs.decompiler.flash.ecma.Null;
import com.jpexs.decompiler.flash.ecma.Undefined;
import com.jpexs.decompiler.flash.helpers.GraphTextWriter;
import com.jpexs.decompiler.graph.CompilationException;
import com.jpexs.decompiler.graph.GraphSourceItem;
import com.jpexs.decompiler.graph.GraphTargetItem;
import com.jpexs.decompiler.graph.SourceGenerator;
import com.jpexs.decompiler.graph.TypeItem;
import com.jpexs.decompiler.graph.model.LocalData;
import java.util.List;
import java.util.Set;

public class CoerceAVM2Item extends AVM2Item {

    //public GraphTargetItem value;
    //public GraphTargetItem type;
    public GraphTargetItem typeObj;

    /*public CoerceAVM2Item(AVM2Instruction instruction, GraphTargetItem value, String type) {
        super(instruction, value.getPrecedence());
        this.value = value;
        this.type = type;
    }*/

    public CoerceAVM2Item(AVM2Instruction instruction, GraphTargetItem value, GraphTargetItem typeObj) {
        super(instruction, value.getPrecedence());
        this.value = value;
        this.typeObj = typeObj;
    }

    @Override
    public GraphTextWriter appendTo(GraphTextWriter writer, LocalData localData) throws InterruptedException {
        //return hilight("("+type+")", highlight)+
        return value.toString(writer, localData);
    }

    @Override
    public GraphTargetItem getNotCoerced() {
        return value.getNotCoerced();
    }

    @Override
    public boolean isCompileTime(Set<GraphTargetItem> dependencies) {
        if (dependencies.contains(value)) {
            return false;
        }
        dependencies.add(value);
        return value.isCompileTime(dependencies);
    }

    @Override
    public Object getResult() {
        Object ret = value.getResult();
        switch (typeObj.toString()) {
            case "String":
                if (ret instanceof Null) {
                    return ret;
                }
                if (ret instanceof Undefined) {
                    return new Null();
                }
                return ret.toString();
            case "*":
                break;
        }
        return ret;

    }

    @Override
    public GraphTargetItem returnType() {
        return new TypeItem(typeObj.toString());
    }

    @Override
    public boolean hasReturnValue() {
        return true;
    }

    @Override
    public List<GraphSourceItem> toSource(SourceGeneratorLocalData localData, SourceGenerator generator) throws CompilationException {

        /*if (value.returnType().toString().equals(type)) {
         return toSourceMerge(localData, generator, value);
         }*/
        AVM2Instruction ins;
        switch (typeObj.toString()) {
            case "*":
                ins = new AVM2Instruction(0, new CoerceAIns(), new int[]{}, new byte[0]);
                break;
            case "String":
                ins = new AVM2Instruction(0, new CoerceSIns(), new int[]{}, new byte[0]);
                break;
            case "Boolean":
                ins = new AVM2Instruction(0, new ConvertBIns(), new int[]{}, new byte[0]);
                break;
            case "int":
                ins = new AVM2Instruction(0, new ConvertIIns(), new int[]{}, new byte[0]);
                break;
            case "uint":
                ins = new AVM2Instruction(0, new ConvertUIns(), new int[]{}, new byte[0]);
                break;
            case "Number":
                ins = new AVM2Instruction(0, new ConvertDIns(), new int[]{}, new byte[0]);
                break;
            default:
                int type_index = AVM2SourceGenerator.resolveType(localData, typeObj, ((AVM2SourceGenerator) generator).abc,(((AVM2SourceGenerator) generator).allABCs));
                ins = new AVM2Instruction(0, new CoerceIns(), new int[]{type_index}, new byte[0]);
                break;
        }
        return toSourceMerge(localData, generator, value, ins);
    }

}
