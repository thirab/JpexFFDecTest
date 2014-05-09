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
package com.jpexs.decompiler.flash.abc.types.traits;

import com.jpexs.decompiler.flash.abc.ABC;
import com.jpexs.decompiler.flash.abc.avm2.ConstantPool;
import com.jpexs.decompiler.flash.abc.types.Multiname;
import com.jpexs.decompiler.flash.abc.types.Namespace;
import com.jpexs.decompiler.flash.abc.types.ValueKind;
import com.jpexs.decompiler.flash.exporters.modes.ScriptExportMode;
import com.jpexs.decompiler.flash.helpers.GraphTextWriter;
import com.jpexs.decompiler.flash.helpers.NulWriter;
import com.jpexs.decompiler.flash.tags.ABCContainerTag;
import com.jpexs.decompiler.graph.GraphTargetItem;
import com.jpexs.decompiler.graph.model.LocalData;
import com.jpexs.helpers.Helper;
import java.util.HashMap;
import java.util.List;

public class TraitSlotConst extends Trait implements TraitWithSlot {

    public int slot_id;
    public int type_index;
    public int value_index;
    public int value_kind;
    public GraphTargetItem assignedValue;

    @Override
    public int getSlotIndex() {
        return slot_id;
    }

    @Override
    public String toString(ABC abc, List<String> fullyQualifiedNames) {
        String typeStr = "*";
        if (type_index > 0) {
            typeStr = abc.constants.getMultiname(type_index).toString(abc.constants, fullyQualifiedNames);
        }
        return "0x" + Helper.formatAddress(fileOffset) + " " + Helper.byteArrToString(bytes) + " SlotConst " + abc.constants.getMultiname(name_index).toString(abc.constants, fullyQualifiedNames) + " slot=" + slot_id + " type=" + typeStr + " value=" + (new ValueKind(value_index, value_kind)).toString(abc.constants) + " metadata=" + Helper.intArrToString(metadata);
    }

    public String getType(ConstantPool constants, List<String> fullyQualifiedNames) {
        String typeStr = "*";
        if (type_index > 0) {
            typeStr = constants.getMultiname(type_index).getName(constants, fullyQualifiedNames);
        }
        return typeStr;
    }

    public GraphTextWriter getNameStr(GraphTextWriter writer, ABC abc, List<String> fullyQualifiedNames) {
        String typeStr = getType(abc.constants, fullyQualifiedNames);
        if (typeStr.equals("*")) {
            typeStr = "";
        } else {
            typeStr = ":" + typeStr;
        }
        ValueKind val = null;
        if (value_kind != 0) {
            val = new ValueKind(value_index, value_kind);
        }

        String slotconst = "var";
        if (kindType == TRAIT_CONST) {
            slotconst = "const";
        }
        if (val != null && val.isNamespace()) {
            slotconst = "namespace";
        }
        writer.hilightSpecial(slotconst + " ", "traittype");
        writer.hilightSpecial(getName(abc).getName(abc.constants, fullyQualifiedNames), "traitname");
        writer.hilightSpecial(typeStr, "traittypename");
        return writer;
    }

    public void getValueStr(Trait parent, GraphTextWriter writer, ABC abc, List<String> fullyQualifiedNames) throws InterruptedException {
        if (assignedValue != null) {
            if (parent instanceof TraitClass) {
                TraitClass tc = (TraitClass) parent;
                int traitInitId = abc.class_info.get(tc.class_info).static_traits.traits.size()
                        + abc.instance_info.get(tc.class_info).instance_traits.traits.size() + 1;
                int initMethod = abc.class_info.get(tc.class_info).cinit_index;
                writer.startTrait(traitInitId);
                writer.startMethod(initMethod);
            }
            assignedValue.toString(writer, LocalData.create(abc.constants, new HashMap<Integer, String>(), fullyQualifiedNames));
            if (parent instanceof TraitClass) {
                writer.endMethod();
                writer.endTrait();
            }
            return;
        }

        if (value_kind != 0) {
            ValueKind val = new ValueKind(value_index, value_kind);
            writer.hilightSpecial(val.toString(abc.constants), "traitvalue");
        }
    }

    public boolean isNamespace() {
        if (value_kind != 0) {
            ValueKind val = new ValueKind(value_index, value_kind);
            return val.isNamespace();
        }
        return false;
    }

    @Override
    public GraphTextWriter toString(Trait parent, String path, List<ABCContainerTag> abcTags, ABC abc, boolean isStatic, ScriptExportMode exportMode, int scriptIndex, int classIndex, GraphTextWriter writer, List<String> fullyQualifiedNames, boolean parallel) throws InterruptedException {
        String modifier = getModifiers(abcTags, abc, isStatic) + " ";
        if (modifier.equals(" ")) {
            modifier = "";
        }
        Multiname n = getName(abc);
        boolean showModifier = true;
        if ((classIndex == -1) && (n != null)) {
            Namespace ns = n.getNamespace(abc.constants);
            if (ns == null) {
                showModifier = false;
            } else {
                if ((ns.kind != Namespace.KIND_PACKAGE) && (ns.kind != Namespace.KIND_PACKAGE_INTERNAL)) {
                    showModifier = false;
                }
            }
        }
        if (!showModifier) {
            modifier = "";
        }
        writer.appendNoHilight(modifier);
        getNameStr(writer, abc, fullyQualifiedNames);
        if (assignedValue != null || value_kind != 0) {
            writer.appendNoHilight(" = ");
            getValueStr(parent, writer, abc, fullyQualifiedNames);
        }
        return writer.appendNoHilight(";").newLine();
    }

    @Override
    public void convert(Trait parent, String path, List<ABCContainerTag> abcTags, ABC abc, boolean isStatic, ScriptExportMode exportMode, int scriptIndex, int classIndex, NulWriter writer, List<String> fullyQualifiedNames, boolean parallel) throws InterruptedException {
        getNameStr(writer, abc, fullyQualifiedNames);
        if (assignedValue != null || value_kind != 0) {
            getValueStr(parent, writer, abc, fullyQualifiedNames);
        }
    }

    public boolean isConst() {
        return kindType == TRAIT_CONST;
    }

    public boolean isVar() {
        return kindType == TRAIT_SLOT;
    }

    @Override
    public int removeTraps(int scriptIndex, int classIndex, boolean isStatic, ABC abc, String path) {
        //do nothing
        return 0;
    }
}
