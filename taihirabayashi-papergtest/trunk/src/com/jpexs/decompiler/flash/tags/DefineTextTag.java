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

import com.jpexs.decompiler.flash.AppStrings;
import com.jpexs.decompiler.flash.SWF;
import com.jpexs.decompiler.flash.SWFInputStream;
import com.jpexs.decompiler.flash.SWFOutputStream;
import com.jpexs.decompiler.flash.exporters.commonshape.Matrix;
import com.jpexs.decompiler.flash.exporters.commonshape.SVGExporter;
import com.jpexs.decompiler.flash.tags.base.FontTag;
import com.jpexs.decompiler.flash.tags.base.MissingCharacterHandler;
import com.jpexs.decompiler.flash.tags.base.TextTag;
import com.jpexs.decompiler.flash.tags.text.ParseException;
import com.jpexs.decompiler.flash.tags.text.ParsedSymbol;
import com.jpexs.decompiler.flash.tags.text.TextLexer;
import com.jpexs.decompiler.flash.timeline.DepthState;
import com.jpexs.decompiler.flash.types.BasicType;
import com.jpexs.decompiler.flash.types.ColorTransform;
import com.jpexs.decompiler.flash.types.GLYPHENTRY;
import com.jpexs.decompiler.flash.types.MATRIX;
import com.jpexs.decompiler.flash.types.RECT;
import com.jpexs.decompiler.flash.types.RGB;
import com.jpexs.decompiler.flash.types.TEXTRECORD;
import com.jpexs.decompiler.flash.types.annotations.SWFType;
import com.jpexs.helpers.Helper;
import com.jpexs.helpers.SerializableImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 *
 * @author JPEXS
 */
public class DefineTextTag extends TextTag {

    @SWFType(BasicType.UI16)
    public int characterID;
    public RECT textBounds;
    public MATRIX textMatrix;
    public List<TEXTRECORD> textRecords;
    public static final int ID = 11;

    @Override
    public MATRIX getTextMatrix() {
        return textMatrix;
    }

    @Override
    public RECT getBounds() {
        return textBounds;
    }

    @Override
    public void setBounds(RECT r) {
        textBounds = r;
    }

    @Override
    public String getText(String separator) {
        FontTag fnt = null;
        String ret = "";
        boolean first = true;
        for (TEXTRECORD rec : textRecords) {
            if (!first) {
                ret += Helper.newLine + separator + Helper.newLine;
            }
            first = false;
            if (rec.styleFlagsHasFont) {
                for (Tag t : swf.tags) {
                    if (t instanceof FontTag) {
                        if (((FontTag) t).getFontId() == rec.fontId) {
                            fnt = ((FontTag) t);
                            break;
                        }
                    }
                }
            }
            if (rec.styleFlagsHasXOffset || rec.styleFlagsHasYOffset) {
                /*if (!ret.isEmpty()) {
                 ret += "\r\n";
                 }*/
            }
            ret += rec.getText(fnt);
        }
        return ret;
    }

    @Override
    public List<Integer> getFontIds() {
        List<Integer> ret = new ArrayList<>();
        for (TEXTRECORD rec : textRecords) {
            if (rec.styleFlagsHasFont) {
                ret.add(rec.fontId);
            }
        }
        return ret;
    }

    @Override
    public String getFormattedText() {
        FontTag fnt = null;
        String ret = "";
        ret += "[\r\nxmin " + textBounds.Xmin + "\r\nymin " + textBounds.Ymin + "\r\nxmax " + textBounds.Xmax + "\r\nymax " + textBounds.Ymax;
        if (textMatrix.translateX != 0) {
            ret += "\r\ntranslatex " + textMatrix.translateX;
        }
        if (textMatrix.translateY != 0) {
            ret += "\r\ntranslatey " + textMatrix.translateY;
        }
        if (textMatrix.hasScale) {
            ret += "\r\nscalex " + textMatrix.scaleX;
            ret += "\r\nscaley " + textMatrix.scaleY;
        }
        if (textMatrix.hasRotate) {
            ret += "\r\nrotateskew0 " + textMatrix.rotateSkew0;
            ret += "\r\nrotateskew1 " + textMatrix.rotateSkew1;
        }
        ret += "\r\n]";
        for (TEXTRECORD rec : textRecords) {
            String params = "";
            if (rec.styleFlagsHasFont) {
                for (Tag t : swf.tags) {
                    if (t instanceof FontTag) {
                        if (((FontTag) t).getFontId() == rec.fontId) {
                            fnt = ((FontTag) t);
                            break;
                        }
                    }
                }
                params += "\r\nfont " + rec.fontId + "\r\nheight " + rec.textHeight;
            }
            if (rec.styleFlagsHasColor) {
                params += "\r\ncolor " + rec.textColor.toHexRGB();
            }
            if (rec.styleFlagsHasXOffset) {
                params += "\r\nx " + rec.xOffset;
            }
            if (rec.styleFlagsHasYOffset) {
                params += "\r\ny " + rec.yOffset;
            }
            if (params.length() > 0) {
                ret += "[" + params + "\r\n]";
            }

            if (fnt == null) {
                ret += AppStrings.translate("fontNotFound").replace("%fontId%", Integer.toString(rec.fontId));
            } else {
                ret += Helper.escapeString(rec.getText(fnt)).replace("[", "\\[").replace("]", "\\]");
            }
        }
        return ret;
    }

    @Override
    public boolean setFormattedText(MissingCharacterHandler missingCharHandler, String formattedText, String[] texts) throws ParseException {
        List<TEXTRECORD> oldTextRecords = textRecords;
        try {
            TextLexer lexer = new TextLexer(new StringReader(formattedText));
            ParsedSymbol s = null;
            textRecords = new ArrayList<>();
            RGB color = null;
            int fontId = -1;
            int textHeight = -1;
            FontTag font = null;
            String fontName = null;
            Integer x = null;
            Integer y = null;
            int currentX = 0;
            int currentY = 0;
            int maxX = Integer.MIN_VALUE;
            int minX = Integer.MAX_VALUE;
            MATRIX textMatrix = new MATRIX();
            textMatrix.hasRotate = false;
            textMatrix.hasScale = false;
            RECT textBounds = new RECT();
            int textIdx = 0;
            while ((s = lexer.yylex()) != null) {
                switch (s.type) {
                    case PARAMETER:
                        String paramName = (String) s.values[0];
                        String paramValue = (String) s.values[1];
                        switch (paramName) {
                            case "color":
                                Matcher m = Pattern.compile("#([0-9a-f][0-9a-f])([0-9a-f][0-9a-f])([0-9a-f][0-9a-f])").matcher(paramValue);
                                if (m.matches()) {
                                    color = new RGB(Integer.parseInt(m.group(1), 16), Integer.parseInt(m.group(2), 16), Integer.parseInt(m.group(3), 16));
                                } else {
                                    throw new ParseException("Invalid color. Valid format is #rrggbb.", lexer.yyline());
                                }
                                break;
                            case "font":
                                try {
                                    fontId = Integer.parseInt(paramValue);

                                    for (Tag t : swf.tags) {
                                        if (t instanceof FontTag) {
                                            if (((FontTag) t).getFontId() == fontId) {
                                                font = (FontTag) t;
                                                fontName = font.getSystemFontName();
                                                break;
                                            }
                                        }
                                    }
                                    if (font == null) {
                                        throw new ParseException("Font not found", lexer.yyline());
                                    }
                                } catch (NumberFormatException nfe) {
                                    throw new ParseException("Invalid font id - number expected.", lexer.yyline());
                                }
                                break;
                            case "height":
                                try {
                                    textHeight = Integer.parseInt(paramValue);
                                } catch (NumberFormatException nfe) {
                                    throw new ParseException("Invalid font height - number expected.", lexer.yyline());
                                }
                                break;
                            case "x":
                                try {
                                    x = Integer.parseInt(paramValue);
                                    currentX = x;
                                } catch (NumberFormatException nfe) {
                                    throw new ParseException("Invalid x position - number expected.", lexer.yyline());
                                }
                                break;
                            case "y":
                                try {
                                    y = Integer.parseInt(paramValue);
                                    currentY = y;
                                } catch (NumberFormatException nfe) {
                                    throw new ParseException("Invalid y position - number expected.", lexer.yyline());
                                }
                                break;
                            case "xmin":
                                try {
                                    textBounds.Xmin = Integer.parseInt(paramValue);
                                } catch (NumberFormatException nfe) {
                                    throw new ParseException("Invalid xmin position - number expected.", lexer.yyline());
                                }
                                break;
                            case "xmax":
                                try {
                                    textBounds.Xmax = Integer.parseInt(paramValue);
                                } catch (NumberFormatException nfe) {
                                    throw new ParseException("Invalid xmax position - number expected.", lexer.yyline());
                                }
                                break;
                            case "ymin":
                                try {
                                    textBounds.Ymin = Integer.parseInt(paramValue);
                                } catch (NumberFormatException nfe) {
                                    throw new ParseException("Invalid ymin position - number expected.", lexer.yyline());
                                }
                                break;
                            case "ymax":
                                try {
                                    textBounds.Ymax = Integer.parseInt(paramValue);
                                } catch (NumberFormatException nfe) {
                                    throw new ParseException("Invalid ymax position - number expected.", lexer.yyline());
                                }
                                break;
                            case "scalex":
                                try {
                                    textMatrix.scaleX = Integer.parseInt(paramValue);
                                    textMatrix.hasScale = true;
                                } catch (NumberFormatException nfe) {
                                    throw new ParseException("Invalid scalex value - number expected.", lexer.yyline());
                                }
                                break;
                            case "scaley":
                                try {
                                    textMatrix.scaleY = Integer.parseInt(paramValue);
                                    textMatrix.hasScale = true;
                                } catch (NumberFormatException nfe) {
                                    throw new ParseException("Invalid scalex value - number expected.", lexer.yyline());
                                }
                                break;
                            case "rotateskew0":
                                try {
                                    textMatrix.rotateSkew0 = Integer.parseInt(paramValue);
                                    textMatrix.hasRotate = true;
                                } catch (NumberFormatException nfe) {
                                    throw new ParseException("Invalid rotateskew0 value - number expected.", lexer.yyline());
                                }
                                break;
                            case "rotateskew1":
                                try {
                                    textMatrix.rotateSkew1 = Integer.parseInt(paramValue);
                                    textMatrix.hasRotate = true;
                                } catch (NumberFormatException nfe) {
                                    throw new ParseException("Invalid rotateskew1 value - number expected.", lexer.yyline());
                                }
                                break;
                            case "translatex":
                                try {
                                    textMatrix.translateX = Integer.parseInt(paramValue);
                                } catch (NumberFormatException nfe) {
                                    throw new ParseException("Invalid translatex value - number expected.", lexer.yyline());
                                }
                                break;
                            case "translatey":
                                try {
                                    textMatrix.translateY = Integer.parseInt(paramValue);
                                } catch (NumberFormatException nfe) {
                                    throw new ParseException("Invalid translatey value - number expected.", lexer.yyline());
                                }
                                break;
                        }
                        break;
                    case TEXT:
                        if (font == null) {
                            throw new ParseException("Font not defined", lexer.yyline());
                        }
                        TEXTRECORD tr = new TEXTRECORD();
                        textRecords.add(tr);
                        if (fontId > -1) {
                            tr.fontId = fontId;
                            tr.textHeight = textHeight;
                            fontId = -1;
                            tr.styleFlagsHasFont = true;
                        }
                        if (color != null) {
                            tr.textColor = color;
                            tr.styleFlagsHasColor = true;
                            color = null;
                        }
                        if (x != null) {
                            tr.xOffset = x;
                            tr.styleFlagsHasXOffset = true;
                            x = null;
                        }
                        if (y != null) {
                            tr.yOffset = y;
                            tr.styleFlagsHasYOffset = true;
                            y = null;
                        }
                        String txt = (texts == null || textIdx >= texts.length) ? (String) s.values[0] : texts[textIdx++];
                        tr.glyphEntries = new GLYPHENTRY[txt.length()];
                        for (int i = 0; i < txt.length(); i++) {
                            char c = txt.charAt(i);
                            Character nextChar = null;
                            if (i + 1 < txt.length()) {
                                nextChar = txt.charAt(i + 1);
                            }

                            if (!font.containsChar(c)) {
                                if (!missingCharHandler.handle(font, c)) {
                                    return false;
                                }
                            }
                            if (nextChar != null && !font.containsChar(nextChar)) {
                                if (!missingCharHandler.handle(font, nextChar)) {
                                    return false;
                                }
                            }
                            tr.glyphEntries[i] = new GLYPHENTRY();
                            tr.glyphEntries[i].glyphIndex = font.charToGlyph(c);

                            int advance;
                            if (font.hasLayout()) {
                                int kerningAdjustment = 0;
                                if (nextChar != null) {
                                    kerningAdjustment = font.getGlyphKerningAdjustment(tr.glyphEntries[i].glyphIndex, font.charToGlyph(nextChar));
                                }
                                advance = (int) Math.round(font.getDivider() * Math.round(((double) textHeight * font.getGlyphAdvance(tr.glyphEntries[i].glyphIndex) + kerningAdjustment) / (font.getDivider() * 1024.0)));
                            } else {
                                advance = (int) Math.round(SWF.unitDivisor * FontTag.getSystemFontAdvance(fontName, font.getFontStyle(), (int) (textHeight / SWF.unitDivisor), c, nextChar));
                            }
                            tr.glyphEntries[i].glyphAdvance = advance;

                            currentX += advance;

                        }

                        if (currentX > maxX) {
                            maxX = currentX;
                        }
                        if (currentX < minX) {
                            minX = currentX;
                        }
                        break;
                }

            }
            this.textMatrix = textMatrix;
            this.textBounds = textBounds;
            //this.textBounds.Xmin = minX;
            //this.textBounds.Xmax = maxX;
        } catch (IOException ex) {
            textRecords = oldTextRecords;
            return false;
        } catch (ParseException ex) {
            textRecords = oldTextRecords;
            throw ex;
        }
        return true;
    }

    @Override
    public int getCharacterId() {
        return characterID;
    }

    public DefineTextTag(SWF swf, int characterID, RECT textBounds, MATRIX textMatrix, List<TEXTRECORD> textRecords) {
        super(swf, ID, "DefineText", null, null, 0);
        this.characterID = characterID;
        this.textBounds = textBounds;
        this.textMatrix = textMatrix;
        this.textRecords = textRecords;
    }

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
            sos.writeUI16(characterID);
            sos.writeRECT(textBounds);
            sos.writeMatrix(textMatrix);

            int glyphBits = 0;
            int advanceBits = 0;
            for (TEXTRECORD tr : textRecords) {
                for (GLYPHENTRY ge : tr.glyphEntries) {
                    glyphBits = SWFOutputStream.enlargeBitCountU(glyphBits, ge.glyphIndex);
                    advanceBits = SWFOutputStream.enlargeBitCountS(advanceBits, ge.glyphAdvance);
                }
            }

            sos.writeUI8(glyphBits);
            sos.writeUI8(advanceBits);
            for (TEXTRECORD tr : textRecords) {
                sos.writeTEXTRECORD(tr, false, glyphBits, advanceBits);
            }
            sos.writeUI8(0);
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
    public DefineTextTag(SWF swf, byte[] headerData, byte[] data, long pos) throws IOException {
        super(swf, ID, "DefineText", headerData, data, pos);
        SWFInputStream sis = new SWFInputStream(new ByteArrayInputStream(data), swf.version);
        characterID = sis.readUI16();
        textBounds = sis.readRECT();
        textMatrix = sis.readMatrix();
        int glyphBits = sis.readUI8();
        int advanceBits = sis.readUI8();
        textRecords = new ArrayList<>();
        TEXTRECORD tr;
        while ((tr = sis.readTEXTRECORD(false, glyphBits, advanceBits)) != null) {
            textRecords.add(tr);
        }
    }

    @Override
    public RECT getRect() {
        return textBounds;
    }

    @Override
    public Set<Integer> getNeededCharacters() {
        Set<Integer> ret = new HashSet<>();
        for (TEXTRECORD tr : textRecords) {
            if (tr.styleFlagsHasFont) {
                ret.add(tr.fontId);
            }
        }
        return ret;
    }

    @Override
    public void toImage(int frame, int time, int ratio, DepthState stateUnderCursor, int mouseButton, SerializableImage image, Matrix transformation, ColorTransform colorTransform) {
        staticTextToImage(swf, textRecords, 1, image, getTextMatrix(), transformation, colorTransform);
    }

    @Override
    public void toSVG(SVGExporter exporter, int ratio, ColorTransform colorTransform, int level) {
        staticTextToSVG(swf, textRecords, 1, exporter, getRect(), getTextMatrix(), colorTransform);
    }

    @Override
    public int getNumFrames() {
        return 1;
    }

    @Override
    public boolean isSingleFrame() {
        return true;
    }

    @Override
    public String toHtmlCanvas(double unitDivisor) {
        return staticTextToHtmlCanvas(unitDivisor, swf, textRecords, 1, textBounds, textMatrix, new ColorTransform());
    }
}
