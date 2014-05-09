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
package com.jpexs.decompiler.flash.action.swf3;

import com.jpexs.decompiler.flash.SWF;
import com.jpexs.decompiler.flash.SWFInputStream;
import com.jpexs.decompiler.flash.SWFOutputStream;
import com.jpexs.decompiler.flash.action.Action;
import com.jpexs.decompiler.flash.action.ActionGraph;
import com.jpexs.decompiler.flash.action.model.ConstantPool;
import com.jpexs.decompiler.flash.action.model.DirectValueActionItem;
import com.jpexs.decompiler.flash.action.model.clauses.IfFrameLoadedActionItem;
import com.jpexs.decompiler.flash.action.parser.ParseException;
import com.jpexs.decompiler.flash.action.parser.pcode.FlasmLexer;
import com.jpexs.decompiler.flash.action.special.ActionStore;
import com.jpexs.decompiler.flash.exporters.modes.ScriptExportMode;
import com.jpexs.decompiler.graph.GraphSourceItem;
import com.jpexs.decompiler.graph.GraphTargetItem;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

public class ActionWaitForFrame extends Action implements ActionStore {

    public int frame;
    public int skipCount;
    public List<Action> skipped;

    public ActionWaitForFrame(int actionLength, SWFInputStream sis, ConstantPool cpool) throws IOException {
        super(0x8A, actionLength);
        frame = sis.readUI16();
        skipCount = sis.readUI8();
        skipped = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "WaitForFrame";
    }

    @Override
    public String getASMSource(List<? extends GraphSourceItem> container, List<Long> knownAddreses, List<String> constantPool, int version, ScriptExportMode exportMode) {
        String ret = "WaitForFrame " + frame + " " + skipCount;
        return ret;
    }

    @Override
    public byte[] getBytes(int version) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        SWFOutputStream sos = new SWFOutputStream(baos, version);
        try {
            sos.writeUI16(frame);
            sos.writeUI8(skipCount);
            sos.close();
        } catch (IOException e) {
        }
        return surroundWithAction(baos.toByteArray(), version);
    }

    public ActionWaitForFrame(FlasmLexer lexer) throws IOException, ParseException {
        super(0x8A, -1);
        frame = (int) lexLong(lexer);
        skipCount = (int) lexLong(lexer);
        skipped = new ArrayList<>();
    }

    @Override
    public void translate(Stack<GraphTargetItem> stack, List<GraphTargetItem> output, java.util.HashMap<Integer, String> regNames, HashMap<String, GraphTargetItem> variables, HashMap<String, GraphTargetItem> functions, int staticOperation, String path) throws InterruptedException {
        GraphTargetItem frameTi = new DirectValueActionItem(null, 0, new Long(frame), new ArrayList<String>());
        List<GraphTargetItem> body = ActionGraph.translateViaGraph(regNames, variables, functions, skipped, SWF.DEFAULT_VERSION, staticOperation, path);
        output.add(new IfFrameLoadedActionItem(frameTi, body, this));
    }

    @Override
    public int getStoreSize() {
        return skipCount;
    }

    @Override
    public void setStore(List<Action> store) {
        skipped = store;
        skipCount = store.size();
    }
}
