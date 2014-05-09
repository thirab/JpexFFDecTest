/*
 *  Copyright (C) 2010-2014 Paolo Cancedda
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
package com.jpexs.decompiler.flash.gui.abc;

import com.jpexs.decompiler.flash.abc.ScriptPack;
import com.jpexs.decompiler.flash.gui.abc.treenodes.AS3PackageNode;
import com.jpexs.decompiler.flash.gui.abc.treenodes.TreeElement;
import com.jpexs.decompiler.flash.treeitems.AS3PackageNodeItem;
import java.util.StringTokenizer;

public class Tree {

    private final TreeElement ROOT = new AS3PackageNode("", "", new AS3PackageNodeItem(null, null), null);

    public void add(String name, String path, ScriptPack item) {
        StringTokenizer st = new StringTokenizer(path, ".");
        TreeElement parent = ROOT;
        while (st.hasMoreTokens()) {
            String pathElement = st.nextToken();
            parent = parent.getBranch(pathElement, item.getSwf());
        }
        parent.addLeaf(name, item);
    }

    public TreeElement getRoot() {
        return ROOT;
    }

    public void visit(TreeVisitor visitor) {
        ROOT.visitLeafs(visitor);
        ROOT.visitBranches(visitor);
    }
}
