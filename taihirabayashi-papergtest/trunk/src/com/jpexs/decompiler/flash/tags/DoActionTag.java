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
package com.jpexs.decompiler.flash.tags;

import com.jpexs.decompiler.flash.DisassemblyListener;
import com.jpexs.decompiler.flash.SWF;
import com.jpexs.decompiler.flash.action.Action;
import com.jpexs.decompiler.flash.action.ActionListReader;
import com.jpexs.decompiler.flash.exporters.modes.ScriptExportMode;
import com.jpexs.decompiler.flash.helpers.GraphTextWriter;
import com.jpexs.decompiler.flash.tags.base.ASMSource;
import com.jpexs.decompiler.flash.types.annotations.Internal;
import com.jpexs.helpers.Helper;
import com.jpexs.helpers.MemoryInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Instructs Flash Player to perform a list of actions when the current frame is
 * complete.
 */
public class DoActionTag extends Tag implements ASMSource {

    /**
     * List of actions to perform
     */
    //public List<Action> actions = new ArrayList<Action>();
    @Internal
    public byte[] actionBytes;
    public static final int ID = 12;

    /**
     * Constructor
     *
     * @param swf
     * @param headerData
     * @param data Data bytes
     * @param pos
     */
    public DoActionTag(SWF swf, byte[] headerData, byte[] data, long pos) {
        super(swf, ID, "DoAction", headerData, data, pos);
        actionBytes = data;
    }

    /**
     * Gets data bytes
     *
     * @return Bytes of data
     */
    @Override
    public byte[] getData() {
        return actionBytes;//Action.actionsToBytes(actions, true, version);
    }

    /**
     * Converts actions to ASM source
     *
     * @param actions
     * @param writer
     * @return ASM source
     * @throws java.lang.InterruptedException
     */
    @Override
    public GraphTextWriter getASMSource(ScriptExportMode exportMode, GraphTextWriter writer, List<Action> actions) throws InterruptedException {
        if (actions == null) {
            actions = getActions();
        }
        return Action.actionsToString(listeners, 0, actions, null, swf.version, exportMode, writer, getPos(), toString()/*FIXME?*/);
    }

    /**
     * Whether or not this object contains ASM source
     *
     * @return True when contains
     */
    @Override
    public boolean containsSource() {
        return true;
    }

    /**
     * Returns string representation of the object
     *
     * @return String representation of the object
     */
    @Override
    public String toString() {
        return "DoAction";
    }

    @Override
    public List<Action> getActions() throws InterruptedException {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int prevLength = 0;
            if (previousTag != null) {
                byte[] prevData = previousTag.getData();
                baos.write(prevData);
                prevLength = prevData.length;
                byte[] header = getHeader(data);
                baos.write(header);
                prevLength += header.length;
            }
            baos.write(actionBytes);
            MemoryInputStream rri = new MemoryInputStream(baos.toByteArray());
            rri.seek(prevLength);
            List<Action> list = ActionListReader.readActionListTimeout(listeners, getPos() - prevLength, rri, getVersion(), prevLength, -1, toString()/*FIXME?*/);
            return list;
        } catch (InterruptedException ex) {
            throw ex;
        } catch (Exception ex) {
            Logger.getLogger(DoActionTag.class.getName()).log(Level.SEVERE, null, ex);
            return new ArrayList<>();
        }
    }

    @Override
    public void setActions(List<Action> actions) {
        actionBytes = Action.actionsToBytes(actions, true, swf.version);
    }

    @Override
    public byte[] getActionBytes() {
        return actionBytes;
    }

    @Override
    public void setActionBytes(byte[] actionBytes) {
        this.actionBytes = actionBytes;
    }

    @Override
    public void setModified() {
        setModified(true);
    }

    @Override
    public GraphTextWriter getActionBytesAsHex(GraphTextWriter writer) {
        return Helper.byteArrayToHexWithHeader(writer, actionBytes);
    }

    List<DisassemblyListener> listeners = new ArrayList<>();

    @Override
    public void addDisassemblyListener(DisassemblyListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeDisassemblyListener(DisassemblyListener listener) {
        listeners.remove(listener);
    }

    @Override
    public GraphTextWriter getActionSourcePrefix(GraphTextWriter writer) {
        return writer;
    }

    @Override
    public GraphTextWriter getActionSourceSuffix(GraphTextWriter writer) {
        return writer;
    }

    @Override
    public int getPrefixLineCount() {
        return 0;
    }

    @Override
    public String removePrefixAndSuffix(String source) {
        return source;
    }
}
