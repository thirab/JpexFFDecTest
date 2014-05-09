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
package com.jpexs.decompiler.flash.action.swf5;

import com.jpexs.decompiler.flash.SWFInputStream;
import com.jpexs.decompiler.flash.SWFOutputStream;
import com.jpexs.decompiler.flash.action.Action;
import com.jpexs.decompiler.flash.action.model.FunctionActionItem;
import com.jpexs.decompiler.flash.action.parser.ParseException;
import com.jpexs.decompiler.flash.action.parser.pcode.FlasmLexer;
import com.jpexs.decompiler.flash.action.parser.script.VariableActionItem;
import com.jpexs.decompiler.flash.exporters.modes.ScriptExportMode;
import com.jpexs.decompiler.flash.helpers.GraphTextWriter;
import com.jpexs.decompiler.graph.GraphSourceItem;
import com.jpexs.decompiler.graph.GraphSourceItemContainer;
import com.jpexs.decompiler.graph.GraphTargetItem;
import com.jpexs.helpers.Helper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

public class ActionDefineFunction extends Action implements GraphSourceItemContainer {

    public String functionName;
    public String replacedFunctionName;
    public List<String> paramNames = new ArrayList<>();
    public List<String> replacedParamNames;
    //public List<Action> code;
    public int codeSize;
    private int version;
    public List<String> constantPool;
    private long hdrSize;

    public ActionDefineFunction(String functionName, List<String> paramNames, int codeSize, int version) {
        super(0x9B, 0);
        this.functionName = functionName;
        this.codeSize = codeSize;
        this.version = version;
        this.paramNames = paramNames;
    }

    public ActionDefineFunction(int actionLength, SWFInputStream sis, int version) throws IOException {
        super(0x9B, actionLength);
        this.version = version;
        //byte[] data=sis.readBytes(actionLength);
        //sis=new SWFInputStream(new ByteArrayInputStream(data),version);
        long startPos = sis.getPos();
        functionName = sis.readString();
        int numParams = sis.readUI16();
        for (int i = 0; i < numParams; i++) {
            paramNames.add(sis.readString());
        }
        codeSize = sis.readUI16();
        long endPos = sis.getPos();
        //code = new ArrayList<Action>();
        hdrSize = endPos - startPos;
        //int posBef2 = (int) rri.getPos();
        //code = sis.readActionList(rri.getPos(), getFileAddress() + hdrSize, rri, codeSize);
        //rri.setPos(posBef2 + codeSize);
    }

    public ActionDefineFunction(FlasmLexer lexer) throws IOException, ParseException {
        super(0x9B, -1);
        functionName = lexString(lexer);
        int numParams = (int) lexLong(lexer);
        for (int i = 0; i < numParams; i++) {
            paramNames.add(lexString(lexer));
        }
        lexBlockOpen(lexer);
    }

    @Override
    public long getHeaderSize() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        SWFOutputStream sos = new SWFOutputStream(baos, version);
        ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
        try {
            sos.writeString(functionName);
            sos.writeUI16(paramNames.size());
            for (String s : paramNames) {
                sos.writeString(s);
            }
            sos.writeUI16(0);
            sos.close();

            baos2.write(surroundWithAction(baos.toByteArray(), version));
        } catch (IOException e) {
        }
        return baos2.toByteArray().length;
    }

    @Override
    public byte[] getBytes(int version) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        SWFOutputStream sos = new SWFOutputStream(baos, version);
        ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
        try {
            sos.writeString(functionName);
            sos.writeUI16(paramNames.size());
            for (String s : paramNames) {
                sos.writeString(s);
            }
            //byte[] codeBytes = Action.actionsToBytes(code, false, version);
            sos.writeUI16(codeSize); //codeBytes.length);
            sos.close();

            baos2.write(surroundWithAction(baos.toByteArray(), version));
            //baos2.write(codeBytes);
        } catch (IOException e) {
        }
        return baos2.toByteArray();
    }

    private long getPreLen(int version) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        SWFOutputStream sos = new SWFOutputStream(baos, version);
        try {
            sos.writeString(functionName);
            sos.writeUI16(paramNames.size());
            for (String s : paramNames) {
                sos.writeString(s);
            }
            sos.writeUI16(0);
            sos.close();
        } catch (IOException e) {
        }

        return surroundWithAction(baos.toByteArray(), version).length;
    }

    @Override
    public void setAddress(long address, int version, boolean recursive) {
        super.setAddress(address, version, recursive);
        if (recursive) {
            //Action.setActionsAddresses(, address + getPreLen(version), version);
        }
    }

    @Override
    public String getASMSource(List<? extends GraphSourceItem> container, List<Long> knownAddreses, List<String> constantPool, int version, ScriptExportMode exportMode) {
        String paramStr = "";
        for (int i = 0; i < paramNames.size(); i++) {
            paramStr += "\"" + Helper.escapeString(paramNames.get(i)) + "\"";
            paramStr += " ";
        }

        return "DefineFunction \"" + Helper.escapeString(functionName) + "\" " + paramNames.size() + " " + paramStr + " {" + (codeSize == 0 ? "\r\n}" : "");// + "\r\n" +Action.actionsToString(getAddress() + getHeaderLength(),getItems(container) , knownAddreses, constantPool, version, hex, getFileAddress() + hdrSize) + "}";
    }

    @Override
    public GraphTextWriter getASMSourceReplaced(List<? extends GraphSourceItem> container, List<Long> knownAddreses, List<String> constantPool, int version, ScriptExportMode exportMode, GraphTextWriter writer) {
        List<String> oldParamNames = paramNames;
        if (replacedParamNames != null) {
            paramNames = replacedParamNames;
        }
        String oldFunctionName = functionName;
        if (replacedFunctionName != null) {
            functionName = replacedFunctionName;
        }
        String ret = getASMSource(container, knownAddreses, constantPool, version, exportMode);
        paramNames = oldParamNames;
        functionName = oldFunctionName;
        writer.appendNoHilight(ret);
        return writer;

    }

    @Override
    public List<Long> getAllRefs(int version) {
        return super.getAllRefs(version);//Action.getActionsAllRefs(getActions(null), version);
    }

    @Override
    public List<Action> getAllIfsOrJumps() {
        return super.getAllIfsOrJumps(); //Action.getActionsAllIfsOrJumps(code);
    }

    @Override
    public void translate(Stack<GraphTargetItem> stack, List<GraphTargetItem> output, HashMap<Integer, String> regNames, HashMap<String, GraphTargetItem> variables, HashMap<String, GraphTargetItem> functions, int staticOperation, String path) {
    }

    @Override
    public HashMap<Integer, String> getRegNames() {
        return new HashMap<>();
    }

    @Override
    public void translateContainer(List<List<GraphTargetItem>> content, Stack<GraphTargetItem> stack, List<GraphTargetItem> output, HashMap<Integer, String> regNames, HashMap<String, GraphTargetItem> variables, HashMap<String, GraphTargetItem> functions) {
        FunctionActionItem fti = new FunctionActionItem(this, functionName, paramNames, content.get(0), constantPool, 1, new ArrayList<VariableActionItem>());
        //ActionGraph.translateViaGraph(regNames, variables, functions, code, version)
        stack.push(fti);
        functions.put(functionName, fti);
    }

    @Override
    public String toString() {
        return "DefineFunction";
    }

    @Override
    public boolean parseDivision(long size, FlasmLexer lexer) {
        codeSize = (int) (size - getHeaderSize());
        return false;
    }

    @Override
    public List<Long> getContainerSizes() {
        List<Long> ret = new ArrayList<>();
        ret.add((Long) (long) codeSize);
        return ret;
    }

    @Override
    public void setContainerSize(int index, long size) {
        if (index == 0) {
            codeSize = (int) size;
        } else {
            throw new IllegalArgumentException("Index must be 0.");
        }
    }

    @Override
    public String getASMSourceBetween(int pos) {
        return "";
    }

    @Override
    public String getName() {
        return "function";
    }
}
