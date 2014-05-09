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
package com.jpexs.decompiler.flash.abc.types;

import com.jpexs.decompiler.flash.abc.ABC;
import com.jpexs.decompiler.flash.abc.avm2.ConstantPool;
import com.jpexs.decompiler.flash.configuration.Configuration;
import com.jpexs.decompiler.flash.helpers.GraphTextWriter;
import com.jpexs.helpers.Helper;
import java.util.HashMap;
import java.util.List;

public class MethodInfo {

    public int[] param_types;
    public int ret_type;
    public int name_index; //0=no name
    // 1=need_arguments, 2=need_activation, 4=need_rest 8=has_optional 16=ignore_rest, 32=explicit, 64=setsdxns, 128=has_paramnames
    public static int FLAG_NEED_ARGUMENTS = 1;
    public static int FLAG_NEED_ACTIVATION = 2;
    public static int FLAG_NEED_REST = 4;
    public static int FLAG_HAS_OPTIONAL = 8;
    public static int FLAG_IGNORE_REST = 16;
    public static int FLAG_EXPLICIT = 32;
    public static int FLAG_SETSDXNS = 64;
    public static int FLAG_HAS_PARAMNAMES = 128;

    public int flags;
    public ValueKind[] optional;
    public int[] paramNames;
    private MethodBody body;

    public void setFlagIgnore_Rest() {
        flags |= FLAG_IGNORE_REST;
    }

    public void setFlagExplicit() {
        flags |= FLAG_EXPLICIT;
    }

    public void setFlagNeed_Arguments() {
        flags |= FLAG_NEED_ARGUMENTS;
    }

    public void setFlagSetsdxns() {
        flags |= FLAG_SETSDXNS;
    }

    public void setFlagSetsdxns(boolean val) {
        if (val) {
            setFlagSetsdxns();
        } else {
            unsetFlagSetsdxns();
        }
    }

    public void unsetFlagSetsdxns() {
        if (flagSetsdxns()) {
            flags -= FLAG_SETSDXNS;
        }
    }

    public void setFlagNeed_activation() {
        flags |= FLAG_NEED_ACTIVATION;
    }

    public void setFlagNeed_activation(boolean val) {
        if (val) {
            setFlagNeed_activation();
        } else {
            unsetFlagNeed_activation();
        }
    }

    public void unsetFlagNeed_activation() {
        if (flagNeed_activation()) {
            flags -= FLAG_NEED_ACTIVATION;
        }
    }

    public void setFlagNeed_rest() {
        flags |= FLAG_NEED_REST;
    }

    public void unsetFlagNeed_rest() {
        if (flagNeed_rest()) {
            flags -= FLAG_NEED_REST;
        }
    }

    public void setFlagNeed_rest(boolean val) {
        if (val) {
            setFlagNeed_rest();
        } else {
            unsetFlagNeed_rest();
        }
    }

    public void setFlagHas_optional() {
        flags |= FLAG_HAS_OPTIONAL;
    }

    public void unsetFlagHas_optional() {
        if (flagHas_optional()) {
            flags -= FLAG_HAS_OPTIONAL;
        }
    }

    public void setFlagHas_optional(boolean val) {
        if (val) {
            setFlagHas_optional();
        } else {
            unsetFlagHas_optional();
        }
    }

    public void setFlagHas_paramnames() {
        flags |= FLAG_HAS_PARAMNAMES;
    }

    public void unsetFlagHas_paramnames() {
        if (flagHas_paramnames()) {
            flags -= FLAG_HAS_PARAMNAMES;
        }
    }

    public void setFlagHas_paramnames(boolean val) {
        if (val) {
            setFlagHas_paramnames();
        } else {
            unsetFlagHas_paramnames();
        }
    }

    public boolean flagNeed_arguments() {
        return (flags & FLAG_NEED_ARGUMENTS) == FLAG_NEED_ARGUMENTS;
    }

    public boolean flagNeed_activation() {
        return (flags & FLAG_NEED_ACTIVATION) == FLAG_NEED_ACTIVATION;
    }

    public boolean flagNeed_rest() {
        return (flags & FLAG_NEED_REST) == FLAG_NEED_REST;
    }

    public boolean flagHas_optional() {
        return (flags & FLAG_HAS_OPTIONAL) == FLAG_HAS_OPTIONAL;
    }

    public boolean flagIgnore_rest() {
        return (flags & FLAG_IGNORE_REST) == FLAG_IGNORE_REST;
    }

    public boolean flagExplicit() {
        return (flags & FLAG_EXPLICIT) == FLAG_EXPLICIT;
    }

    public boolean flagSetsdxns() {
        return (flags & FLAG_SETSDXNS) == FLAG_SETSDXNS;
    }

    public boolean flagHas_paramnames() {
        return (flags & FLAG_HAS_PARAMNAMES) == FLAG_HAS_PARAMNAMES;
    }

    public MethodInfo(int[] param_types, int ret_type, int name_index, int flags, ValueKind[] optional, int[] paramNames) {
        this.param_types = param_types;
        this.ret_type = ret_type;
        this.name_index = name_index;
        this.flags = flags;
        this.optional = optional;
        this.paramNames = paramNames;
    }

    @Override
    public String toString() {
        String optionalStr = "[";
        if (optional != null) {
            for (int i = 0; i < optional.length; i++) {
                if (i > 0) {
                    optionalStr += ",";
                }
                optionalStr += optional[i].toString();
            }
        }
        optionalStr += "]";
        return "MethodInfo: param_types=" + Helper.intArrToString(param_types) + " ret_type=" + ret_type + " name_index=" + name_index + " flags=" + flags + " optional=" + optionalStr + " paramNames=" + Helper.intArrToString(paramNames);
    }

    public String toString(ConstantPool constants, List<String> fullyQualifiedNames) {
        String optionalStr = "[";
        if (optional != null) {
            for (int i = 0; i < optional.length; i++) {
                if (i > 0) {
                    optionalStr += ",";
                }
                optionalStr += optional[i].toString(constants);
            }
        }
        optionalStr += "]";

        String param_typesStr = "";
        for (int i = 0; i < param_types.length; i++) {
            if (i > 0) {
                param_typesStr += ",";
            }
            if (param_types[i] == 0) {
                param_typesStr += "*";
            } else {
                param_typesStr += constants.getMultiname(param_types[i]).toString(constants, fullyQualifiedNames);
            }
        }

        String paramNamesStr = "";
        for (int i = 0; i < paramNames.length; i++) {
            if (i > 0) {
                paramNamesStr += ",";
            }
            paramNamesStr += constants.getString(paramNames[i]);
        }

        String ret_typeStr = "";
        if (ret_type == 0) {
            ret_typeStr += "*";
        } else {
            ret_typeStr += constants.getMultiname(ret_type).toString(constants, fullyQualifiedNames);
        }

        return "param_types=" + param_typesStr + " ret_type=" + ret_typeStr + " name=\"" + constants.getString(name_index) + "\" flags=" + flags + " optional=" + optionalStr + " paramNames=" + paramNamesStr;
    }

    public String getName(ConstantPool constants) {
        if (name_index == 0) {
            return "UNKNOWN";
        }
        return constants.getString(name_index);
    }

    public GraphTextWriter getParamStr(GraphTextWriter writer, ConstantPool constants, MethodBody body, ABC abc, List<String> fullyQualifiedNames) {
        HashMap<Integer, String> localRegNames = new HashMap<>();
        if (body != null) {
            localRegNames = body.code.getLocalRegNamesFromDebug(abc);
        }
        for (int i = 0; i < param_types.length; i++) {
            if (i > 0) {
                writer.appendNoHilight(", ");
            }
            if (!localRegNames.isEmpty()) {
                writer.appendNoHilight(localRegNames.get(i + 1));
            } else if ((paramNames.length > i) && (paramNames[i] != 0) && Configuration.paramNamesEnable.get()) {
                writer.appendNoHilight(constants.getString(paramNames[i]));
            } else {
                writer.appendNoHilight("param" + (i + 1));
            }
            writer.appendNoHilight(":");
            if (param_types[i] == 0) {
                writer.hilightSpecial("*", "param", i);
            } else {
                writer.hilightSpecial(constants.getMultiname(param_types[i]).getName(constants, fullyQualifiedNames), "param", i);
            }
            if (optional != null) {
                if (i >= param_types.length - optional.length) {
                    int optionalIndex = i - (param_types.length - optional.length);
                    writer.appendNoHilight(" = ");
                    writer.hilightSpecial(optional[optionalIndex].toString(constants), "optional", optionalIndex);
                }
            }
        }
        if (flagNeed_rest()) {
            String restAdd = "";
            if ((param_types != null) && (param_types.length > 0)) {
                restAdd += ", ";
            }
            restAdd += "... ";
            if (!localRegNames.isEmpty()) {
                restAdd += localRegNames.get(param_types.length + 1);
            } else {
                restAdd += "rest";
            }
            writer.hilightSpecial(restAdd, "flag.NEED_REST");
        }
        return writer;
    }

    public GraphTextWriter getReturnTypeStr(GraphTextWriter writer, ConstantPool constants, List<String> fullyQualifiedNames) {
        return writer.hilightSpecial(ret_type == 0 ? "*" : constants.getMultiname(ret_type).getName(constants, fullyQualifiedNames), "returns");
    }

    public void setBody(MethodBody body) {
        this.body = body;
    }

    public MethodBody getBody() {
        return body;
    }
}
