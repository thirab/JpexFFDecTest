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

import com.jpexs.decompiler.flash.SWF;
import com.jpexs.decompiler.flash.SWFInputStream;
import com.jpexs.decompiler.flash.SWFOutputStream;
import com.jpexs.decompiler.flash.types.BasicType;
import com.jpexs.decompiler.flash.types.annotations.Reserved;
import com.jpexs.decompiler.flash.types.annotations.SWFType;
import com.jpexs.helpers.utf8.Utf8Helper;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 *
 *
 * @author JPEXS
 */
public class DefineFontInfoTag extends Tag {

    @SWFType(BasicType.UI16)
    public int fontId;
    public String fontName;
    @Reserved
    @SWFType(value = BasicType.UB, count = 2)
    public int reserved;
    public boolean fontFlagsSmallText;
    public boolean fontFlagsShiftJIS;
    public boolean fontFlagsANSI;
    public boolean fontFlagsItalic;
    public boolean fontFlagsBold;
    public boolean fontFlagsWideCodes;
    @SWFType(value = BasicType.UI8, alternateValue = BasicType.UI16, alternateCondition = "fontFlagsWideCodes")
    public List<Integer> codeTable;
    public static final int ID = 13;

    /**
     * Gets data bytes
     *
     * @return Bytes of data
     */
    @Override
    public byte[] getData() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OutputStream os = baos;
        SWFOutputStream sos = new SWFOutputStream(os, getVersion());
        try {
            sos.writeUI16(fontId);
            byte[] fontNameBytes = Utf8Helper.getBytes(fontName);
            sos.writeUI8(fontNameBytes.length);
            sos.write(fontNameBytes);
            sos.writeUB(2, reserved);
            sos.writeUB(1, fontFlagsSmallText ? 1 : 0);
            sos.writeUB(1, fontFlagsShiftJIS ? 1 : 0);
            sos.writeUB(1, fontFlagsANSI ? 1 : 0);
            sos.writeUB(1, fontFlagsItalic ? 1 : 0);
            sos.writeUB(1, fontFlagsBold ? 1 : 0);
            sos.writeUB(1, fontFlagsWideCodes ? 1 : 0);
            for (int code : codeTable) {
                if (fontFlagsWideCodes) {
                    sos.writeUI16(code);
                } else {
                    sos.writeUI8(code);
                }
            }
        } catch (IOException e) {
        }
        return baos.toByteArray();
    }

    /**
     * Constructor
     *
     * @param swf
     * @param headerData
     * @param data Data bytes
     * @param pos
     * @throws IOException
     */
    public DefineFontInfoTag(SWF swf, byte[] headerData, byte[] data, long pos) throws IOException {
        super(swf, ID, "DefineFontInfo", headerData, data, pos);
        SWFInputStream sis = new SWFInputStream(new ByteArrayInputStream(data), swf.version);
        fontId = sis.readUI16();
        int fontNameLen = sis.readUI8();
        if (swf.version >= 6) {
            fontName = new String(sis.readBytesEx(fontNameLen), Utf8Helper.charset);
        } else {
            fontName = new String(sis.readBytesEx(fontNameLen));
        }
        reserved = (int) sis.readUB(2);
        fontFlagsSmallText = sis.readUB(1) == 1;
        fontFlagsShiftJIS = sis.readUB(1) == 1;
        fontFlagsANSI = sis.readUB(1) == 1;
        fontFlagsItalic = sis.readUB(1) == 1;
        fontFlagsBold = sis.readUB(1) == 1;
        fontFlagsWideCodes = sis.readUB(1) == 1;
        codeTable = new ArrayList<>();
        do {
            if (fontFlagsWideCodes) {
                codeTable.add(sis.readUI16());
            } else {
                codeTable.add(sis.readUI8());
            }
        } while (sis.available() > 0);
    }
}
