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
import com.jpexs.decompiler.flash.abc.types.Multiname;
import com.jpexs.decompiler.flash.abc.types.Namespace;
import com.jpexs.decompiler.flash.exporters.modes.ScriptExportMode;
import com.jpexs.decompiler.flash.helpers.GraphTextWriter;
import com.jpexs.decompiler.flash.helpers.NulWriter;
import com.jpexs.decompiler.flash.tags.ABCContainerTag;
import com.jpexs.helpers.Helper;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class Trait implements Serializable {

    public int name_index;
    public int kindType;
    public int kindFlags;
    public int[] metadata = new int[0];
    public long fileOffset;
    public byte[] bytes;
    public static final int ATTR_Final = 0x1;
    public static final int ATTR_Override = 0x2;
    public static final int ATTR_Metadata = 0x4;
    public static final int TRAIT_SLOT = 0;
    public static final int TRAIT_METHOD = 1;
    public static final int TRAIT_GETTER = 2;
    public static final int TRAIT_SETTER = 3;
    public static final int TRAIT_CLASS = 4;
    public static final int TRAIT_FUNCTION = 5;
    public static final int TRAIT_CONST = 6;

    public String getModifiers(List<ABCContainerTag> abcTags, ABC abc, boolean isStatic) {
        String ret = "";
        if ((kindFlags & ATTR_Override) > 0) {
            ret += "override";
        }
        Multiname m = getName(abc);
        if (m != null) {
            String nsname = "";
            //if (abc.constants.getNamespace(m.namespace_index).kind == Namespace.KIND_NAMESPACE) {
            {
                for (ABCContainerTag abcTag : abcTags) {
                    if (m.namespace_index == -1) {
                        break;
                    }
                    nsname = abcTag.getABC().nsValueToName(abc.constants.getNamespace(m.namespace_index).getName(abc.constants));
                    if (nsname == null) {
                        break;
                    }
                    if (nsname.contains(".")) {
                        nsname = nsname.substring(nsname.lastIndexOf('.') + 1);
                    }
                    if (!nsname.isEmpty()) {
                        break;
                    }
                }
            }
            Namespace ns = m.getNamespace(abc.constants);

            if (nsname.contains(":")) {
                nsname = "";
            }

            if ((!nsname.isEmpty()) && (!nsname.equals("-"))) {
            } else {
                if (ns != null) {
                    if (ns.kind == Namespace.KIND_NAMESPACE) {
                        nsname = ns.getName(abc.constants);
                    }
                }
            }

            if (nsname != null && (!nsname.contains(":")) && (!nsname.isEmpty())) {
                ret += " " + nsname;
            }
            if (ns != null) {
                ret += " " + ns.getPrefix(abc);
            }
        }
        if (isStatic) {
            if ((this instanceof TraitSlotConst) && ((TraitSlotConst) this).isNamespace()) {
                //static is automatic
            } else {
                ret += " static";
            }
        }
        if ((kindFlags & ATTR_Final) > 0) {
            if (!isStatic) {
                ret += " final";
            }
        }
        return ret.trim();
    }

    @Override
    public String toString() {
        return "name_index=" + name_index + " kind=" + kindType + " metadata=" + Helper.intArrToString(metadata);
    }

    public String toString(ABC abc, List<String> fullyQualifiedNames) {
        return abc.constants.getMultiname(name_index).toString(abc.constants, fullyQualifiedNames) + " kind=" + kindType + " metadata=" + Helper.intArrToString(metadata);
    }

    public GraphTextWriter toString(Trait parent, String path, List<ABCContainerTag> abcTags, ABC abc, boolean isStatic, ScriptExportMode exportMode, int scriptIndex, int classIndex, GraphTextWriter writer, List<String> fullyQualifiedNames, boolean parallel) throws InterruptedException {
        writer.appendNoHilight(abc.constants.getMultiname(name_index).toString(abc.constants, fullyQualifiedNames) + " kind=" + kindType + " metadata=" + Helper.intArrToString(metadata));
        return writer;
    }

    public void convert(Trait parent, String path, List<ABCContainerTag> abcTags, ABC abc, boolean isStatic, ScriptExportMode exportMode, int scriptIndex, int classIndex, NulWriter writer, List<String> fullyQualifiedNames, boolean parallel) throws InterruptedException {
    }

    public GraphTextWriter toStringPackaged(Trait parent, String path, List<ABCContainerTag> abcTags, ABC abc, boolean isStatic, ScriptExportMode exportMode, int scriptIndex, int classIndex, GraphTextWriter writer, List<String> fullyQualifiedNames, boolean parallel) throws InterruptedException {
        Namespace ns = abc.constants.getMultiname(name_index).getNamespace(abc.constants);
        if ((ns.kind == Namespace.KIND_PACKAGE) || (ns.kind == Namespace.KIND_PACKAGE_INTERNAL)) {
            String nsname = ns.getName(abc.constants);
            writer.appendNoHilight("package " + nsname).newLine(); //assume not null name
            writer.appendNoHilight("{").newLine();
            writer.indent();
            toString(parent, path, abcTags, abc, isStatic, exportMode, scriptIndex, classIndex, writer, fullyQualifiedNames, parallel);
            writer.unindent();
            writer.appendNoHilight("}");
            writer.newLine();
        }
        return writer;
    }

    public void convertPackaged(Trait parent, String path, List<ABCContainerTag> abcTags, ABC abc, boolean isStatic, ScriptExportMode exportMode, int scriptIndex, int classIndex, NulWriter writer, List<String> fullyQualifiedNames, boolean parallel) throws InterruptedException {
        Namespace ns = abc.constants.getMultiname(name_index).getNamespace(abc.constants);
        if ((ns.kind == Namespace.KIND_PACKAGE) || (ns.kind == Namespace.KIND_PACKAGE_INTERNAL)) {
            convert(parent, path, abcTags, abc, isStatic, exportMode, scriptIndex, classIndex, writer, fullyQualifiedNames, parallel);
        }
    }

    public GraphTextWriter toStringHeader(Trait parent, String path, List<ABCContainerTag> abcTags, ABC abc, boolean isStatic, ScriptExportMode exportMode, int scriptIndex, int classIndex, GraphTextWriter writer, List<String> fullyQualifiedNames, boolean parallel) throws InterruptedException {
        toString(parent, path, abcTags, abc, isStatic, exportMode, scriptIndex, classIndex, writer, fullyQualifiedNames, parallel);
        return writer;
    }

    public void convertHeader(Trait parent, String path, List<ABCContainerTag> abcTags, ABC abc, boolean isStatic, ScriptExportMode exportMode, int scriptIndex, int classIndex, NulWriter writer, List<String> fullyQualifiedNames, boolean parallel) throws InterruptedException {
        convert(parent, path, abcTags, abc, isStatic, exportMode, scriptIndex, classIndex, writer, fullyQualifiedNames, parallel);
    }

    public Multiname getName(ABC abc) {
        if (name_index == 0) {
            return null;
        } else {
            return abc.constants.getMultiname(name_index);
        }
    }

    public abstract int removeTraps(int scriptIndex, int classIndex, boolean isStatic, ABC abc, String path) throws InterruptedException;

    public String getPath(ABC abc) {
        Multiname name = getName(abc);
        Namespace ns = name.getNamespace(abc.constants);
        String packageName = ns.getName(abc.constants);
        String objectName = name.getName(abc.constants, new ArrayList<String>());
        return packageName + "." + objectName; //assume not null name
    }
}
