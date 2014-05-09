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
package com.jpexs.decompiler.flash;

import com.jpexs.decompiler.flash.action.Action;
import com.jpexs.decompiler.flash.action.model.ConstantPool;
import com.jpexs.decompiler.flash.action.special.ActionEnd;
import com.jpexs.decompiler.flash.action.special.ActionNop;
import com.jpexs.decompiler.flash.action.swf3.ActionGetURL;
import com.jpexs.decompiler.flash.action.swf3.ActionGoToLabel;
import com.jpexs.decompiler.flash.action.swf3.ActionGotoFrame;
import com.jpexs.decompiler.flash.action.swf3.ActionNextFrame;
import com.jpexs.decompiler.flash.action.swf3.ActionPlay;
import com.jpexs.decompiler.flash.action.swf3.ActionPrevFrame;
import com.jpexs.decompiler.flash.action.swf3.ActionSetTarget;
import com.jpexs.decompiler.flash.action.swf3.ActionStop;
import com.jpexs.decompiler.flash.action.swf3.ActionStopSounds;
import com.jpexs.decompiler.flash.action.swf3.ActionToggleQuality;
import com.jpexs.decompiler.flash.action.swf3.ActionWaitForFrame;
import com.jpexs.decompiler.flash.action.swf4.ActionAdd;
import com.jpexs.decompiler.flash.action.swf4.ActionAnd;
import com.jpexs.decompiler.flash.action.swf4.ActionAsciiToChar;
import com.jpexs.decompiler.flash.action.swf4.ActionCall;
import com.jpexs.decompiler.flash.action.swf4.ActionCharToAscii;
import com.jpexs.decompiler.flash.action.swf4.ActionCloneSprite;
import com.jpexs.decompiler.flash.action.swf4.ActionDivide;
import com.jpexs.decompiler.flash.action.swf4.ActionEndDrag;
import com.jpexs.decompiler.flash.action.swf4.ActionEquals;
import com.jpexs.decompiler.flash.action.swf4.ActionGetProperty;
import com.jpexs.decompiler.flash.action.swf4.ActionGetTime;
import com.jpexs.decompiler.flash.action.swf4.ActionGetURL2;
import com.jpexs.decompiler.flash.action.swf4.ActionGetVariable;
import com.jpexs.decompiler.flash.action.swf4.ActionGotoFrame2;
import com.jpexs.decompiler.flash.action.swf4.ActionIf;
import com.jpexs.decompiler.flash.action.swf4.ActionJump;
import com.jpexs.decompiler.flash.action.swf4.ActionLess;
import com.jpexs.decompiler.flash.action.swf4.ActionMBAsciiToChar;
import com.jpexs.decompiler.flash.action.swf4.ActionMBCharToAscii;
import com.jpexs.decompiler.flash.action.swf4.ActionMBStringExtract;
import com.jpexs.decompiler.flash.action.swf4.ActionMBStringLength;
import com.jpexs.decompiler.flash.action.swf4.ActionMultiply;
import com.jpexs.decompiler.flash.action.swf4.ActionNot;
import com.jpexs.decompiler.flash.action.swf4.ActionOr;
import com.jpexs.decompiler.flash.action.swf4.ActionPop;
import com.jpexs.decompiler.flash.action.swf4.ActionPush;
import com.jpexs.decompiler.flash.action.swf4.ActionRandomNumber;
import com.jpexs.decompiler.flash.action.swf4.ActionRemoveSprite;
import com.jpexs.decompiler.flash.action.swf4.ActionSetProperty;
import com.jpexs.decompiler.flash.action.swf4.ActionSetTarget2;
import com.jpexs.decompiler.flash.action.swf4.ActionSetVariable;
import com.jpexs.decompiler.flash.action.swf4.ActionStartDrag;
import com.jpexs.decompiler.flash.action.swf4.ActionStringAdd;
import com.jpexs.decompiler.flash.action.swf4.ActionStringEquals;
import com.jpexs.decompiler.flash.action.swf4.ActionStringExtract;
import com.jpexs.decompiler.flash.action.swf4.ActionStringLength;
import com.jpexs.decompiler.flash.action.swf4.ActionStringLess;
import com.jpexs.decompiler.flash.action.swf4.ActionSubtract;
import com.jpexs.decompiler.flash.action.swf4.ActionToInteger;
import com.jpexs.decompiler.flash.action.swf4.ActionTrace;
import com.jpexs.decompiler.flash.action.swf4.ActionWaitForFrame2;
import com.jpexs.decompiler.flash.action.swf5.ActionAdd2;
import com.jpexs.decompiler.flash.action.swf5.ActionBitAnd;
import com.jpexs.decompiler.flash.action.swf5.ActionBitLShift;
import com.jpexs.decompiler.flash.action.swf5.ActionBitOr;
import com.jpexs.decompiler.flash.action.swf5.ActionBitRShift;
import com.jpexs.decompiler.flash.action.swf5.ActionBitURShift;
import com.jpexs.decompiler.flash.action.swf5.ActionBitXor;
import com.jpexs.decompiler.flash.action.swf5.ActionCallFunction;
import com.jpexs.decompiler.flash.action.swf5.ActionCallMethod;
import com.jpexs.decompiler.flash.action.swf5.ActionConstantPool;
import com.jpexs.decompiler.flash.action.swf5.ActionDecrement;
import com.jpexs.decompiler.flash.action.swf5.ActionDefineFunction;
import com.jpexs.decompiler.flash.action.swf5.ActionDefineLocal;
import com.jpexs.decompiler.flash.action.swf5.ActionDefineLocal2;
import com.jpexs.decompiler.flash.action.swf5.ActionDelete;
import com.jpexs.decompiler.flash.action.swf5.ActionDelete2;
import com.jpexs.decompiler.flash.action.swf5.ActionEnumerate;
import com.jpexs.decompiler.flash.action.swf5.ActionEquals2;
import com.jpexs.decompiler.flash.action.swf5.ActionGetMember;
import com.jpexs.decompiler.flash.action.swf5.ActionIncrement;
import com.jpexs.decompiler.flash.action.swf5.ActionInitArray;
import com.jpexs.decompiler.flash.action.swf5.ActionInitObject;
import com.jpexs.decompiler.flash.action.swf5.ActionLess2;
import com.jpexs.decompiler.flash.action.swf5.ActionModulo;
import com.jpexs.decompiler.flash.action.swf5.ActionNewMethod;
import com.jpexs.decompiler.flash.action.swf5.ActionNewObject;
import com.jpexs.decompiler.flash.action.swf5.ActionPushDuplicate;
import com.jpexs.decompiler.flash.action.swf5.ActionReturn;
import com.jpexs.decompiler.flash.action.swf5.ActionSetMember;
import com.jpexs.decompiler.flash.action.swf5.ActionStackSwap;
import com.jpexs.decompiler.flash.action.swf5.ActionStoreRegister;
import com.jpexs.decompiler.flash.action.swf5.ActionTargetPath;
import com.jpexs.decompiler.flash.action.swf5.ActionToNumber;
import com.jpexs.decompiler.flash.action.swf5.ActionToString;
import com.jpexs.decompiler.flash.action.swf5.ActionTypeOf;
import com.jpexs.decompiler.flash.action.swf5.ActionWith;
import com.jpexs.decompiler.flash.action.swf6.ActionEnumerate2;
import com.jpexs.decompiler.flash.action.swf6.ActionGreater;
import com.jpexs.decompiler.flash.action.swf6.ActionInstanceOf;
import com.jpexs.decompiler.flash.action.swf6.ActionStrictEquals;
import com.jpexs.decompiler.flash.action.swf6.ActionStringGreater;
import com.jpexs.decompiler.flash.action.swf7.ActionCastOp;
import com.jpexs.decompiler.flash.action.swf7.ActionDefineFunction2;
import com.jpexs.decompiler.flash.action.swf7.ActionExtends;
import com.jpexs.decompiler.flash.action.swf7.ActionImplementsOp;
import com.jpexs.decompiler.flash.action.swf7.ActionThrow;
import com.jpexs.decompiler.flash.action.swf7.ActionTry;
import com.jpexs.decompiler.flash.configuration.Configuration;
import com.jpexs.decompiler.flash.tags.CSMTextSettingsTag;
import com.jpexs.decompiler.flash.tags.DebugIDTag;
import com.jpexs.decompiler.flash.tags.DefineBinaryDataTag;
import com.jpexs.decompiler.flash.tags.DefineBitsJPEG2Tag;
import com.jpexs.decompiler.flash.tags.DefineBitsJPEG3Tag;
import com.jpexs.decompiler.flash.tags.DefineBitsJPEG4Tag;
import com.jpexs.decompiler.flash.tags.DefineBitsLossless2Tag;
import com.jpexs.decompiler.flash.tags.DefineBitsLosslessTag;
import com.jpexs.decompiler.flash.tags.DefineBitsTag;
import com.jpexs.decompiler.flash.tags.DefineButton2Tag;
import com.jpexs.decompiler.flash.tags.DefineButtonCxformTag;
import com.jpexs.decompiler.flash.tags.DefineButtonSoundTag;
import com.jpexs.decompiler.flash.tags.DefineButtonTag;
import com.jpexs.decompiler.flash.tags.DefineEditTextTag;
import com.jpexs.decompiler.flash.tags.DefineFont2Tag;
import com.jpexs.decompiler.flash.tags.DefineFont3Tag;
import com.jpexs.decompiler.flash.tags.DefineFont4Tag;
import com.jpexs.decompiler.flash.tags.DefineFontAlignZonesTag;
import com.jpexs.decompiler.flash.tags.DefineFontInfo2Tag;
import com.jpexs.decompiler.flash.tags.DefineFontInfoTag;
import com.jpexs.decompiler.flash.tags.DefineFontNameTag;
import com.jpexs.decompiler.flash.tags.DefineFontTag;
import com.jpexs.decompiler.flash.tags.DefineMorphShape2Tag;
import com.jpexs.decompiler.flash.tags.DefineMorphShapeTag;
import com.jpexs.decompiler.flash.tags.DefineScalingGridTag;
import com.jpexs.decompiler.flash.tags.DefineSceneAndFrameLabelDataTag;
import com.jpexs.decompiler.flash.tags.DefineShape2Tag;
import com.jpexs.decompiler.flash.tags.DefineShape3Tag;
import com.jpexs.decompiler.flash.tags.DefineShape4Tag;
import com.jpexs.decompiler.flash.tags.DefineShapeTag;
import com.jpexs.decompiler.flash.tags.DefineSoundTag;
import com.jpexs.decompiler.flash.tags.DefineSpriteTag;
import com.jpexs.decompiler.flash.tags.DefineText2Tag;
import com.jpexs.decompiler.flash.tags.DefineTextTag;
import com.jpexs.decompiler.flash.tags.DefineVideoStreamTag;
import com.jpexs.decompiler.flash.tags.DoABCDefineTag;
import com.jpexs.decompiler.flash.tags.DoABCTag;
import com.jpexs.decompiler.flash.tags.DoActionTag;
import com.jpexs.decompiler.flash.tags.DoInitActionTag;
import com.jpexs.decompiler.flash.tags.EnableDebugger2Tag;
import com.jpexs.decompiler.flash.tags.EnableDebuggerTag;
import com.jpexs.decompiler.flash.tags.EnableTelemetryTag;
import com.jpexs.decompiler.flash.tags.EndTag;
import com.jpexs.decompiler.flash.tags.ExportAssetsTag;
import com.jpexs.decompiler.flash.tags.FileAttributesTag;
import com.jpexs.decompiler.flash.tags.FrameLabelTag;
import com.jpexs.decompiler.flash.tags.ImportAssets2Tag;
import com.jpexs.decompiler.flash.tags.ImportAssetsTag;
import com.jpexs.decompiler.flash.tags.JPEGTablesTag;
import com.jpexs.decompiler.flash.tags.MetadataTag;
import com.jpexs.decompiler.flash.tags.PlaceObject2Tag;
import com.jpexs.decompiler.flash.tags.PlaceObject3Tag;
import com.jpexs.decompiler.flash.tags.PlaceObject4Tag;
import com.jpexs.decompiler.flash.tags.PlaceObjectTag;
import com.jpexs.decompiler.flash.tags.ProductInfoTag;
import com.jpexs.decompiler.flash.tags.ProtectTag;
import com.jpexs.decompiler.flash.tags.RemoveObject2Tag;
import com.jpexs.decompiler.flash.tags.RemoveObjectTag;
import com.jpexs.decompiler.flash.tags.ScriptLimitsTag;
import com.jpexs.decompiler.flash.tags.SetBackgroundColorTag;
import com.jpexs.decompiler.flash.tags.SetTabIndexTag;
import com.jpexs.decompiler.flash.tags.ShowFrameTag;
import com.jpexs.decompiler.flash.tags.SoundStreamBlockTag;
import com.jpexs.decompiler.flash.tags.SoundStreamHead2Tag;
import com.jpexs.decompiler.flash.tags.SoundStreamHeadTag;
import com.jpexs.decompiler.flash.tags.StartSound2Tag;
import com.jpexs.decompiler.flash.tags.StartSoundTag;
import com.jpexs.decompiler.flash.tags.SymbolClassTag;
import com.jpexs.decompiler.flash.tags.Tag;
import com.jpexs.decompiler.flash.tags.UnknownTag;
import com.jpexs.decompiler.flash.tags.VideoFrameTag;
import com.jpexs.decompiler.flash.tags.gfx.DefineCompactedFont;
import com.jpexs.decompiler.flash.tags.gfx.DefineExternalGradient;
import com.jpexs.decompiler.flash.tags.gfx.DefineExternalImage;
import com.jpexs.decompiler.flash.tags.gfx.DefineExternalImage2;
import com.jpexs.decompiler.flash.tags.gfx.DefineExternalSound;
import com.jpexs.decompiler.flash.tags.gfx.DefineExternalStreamSound;
import com.jpexs.decompiler.flash.tags.gfx.DefineGradientMap;
import com.jpexs.decompiler.flash.tags.gfx.DefineSubImage;
import com.jpexs.decompiler.flash.tags.gfx.ExporterInfoTag;
import com.jpexs.decompiler.flash.tags.gfx.FontTextureInfo;
import com.jpexs.decompiler.flash.timeline.Timelined;
import com.jpexs.decompiler.flash.types.ALPHABITMAPDATA;
import com.jpexs.decompiler.flash.types.ALPHACOLORMAPDATA;
import com.jpexs.decompiler.flash.types.ARGB;
import com.jpexs.decompiler.flash.types.BITMAPDATA;
import com.jpexs.decompiler.flash.types.BUTTONCONDACTION;
import com.jpexs.decompiler.flash.types.BUTTONRECORD;
import com.jpexs.decompiler.flash.types.CLIPACTIONRECORD;
import com.jpexs.decompiler.flash.types.CLIPACTIONS;
import com.jpexs.decompiler.flash.types.CLIPEVENTFLAGS;
import com.jpexs.decompiler.flash.types.COLORMAPDATA;
import com.jpexs.decompiler.flash.types.CXFORM;
import com.jpexs.decompiler.flash.types.CXFORMWITHALPHA;
import com.jpexs.decompiler.flash.types.FILLSTYLE;
import com.jpexs.decompiler.flash.types.FILLSTYLEARRAY;
import com.jpexs.decompiler.flash.types.FOCALGRADIENT;
import com.jpexs.decompiler.flash.types.GLYPHENTRY;
import com.jpexs.decompiler.flash.types.GRADIENT;
import com.jpexs.decompiler.flash.types.GRADRECORD;
import com.jpexs.decompiler.flash.types.KERNINGRECORD;
import com.jpexs.decompiler.flash.types.LANGCODE;
import com.jpexs.decompiler.flash.types.LINESTYLE;
import com.jpexs.decompiler.flash.types.LINESTYLE2;
import com.jpexs.decompiler.flash.types.LINESTYLEARRAY;
import com.jpexs.decompiler.flash.types.MATRIX;
import com.jpexs.decompiler.flash.types.MORPHFILLSTYLE;
import com.jpexs.decompiler.flash.types.MORPHFILLSTYLEARRAY;
import com.jpexs.decompiler.flash.types.MORPHFOCALGRADIENT;
import com.jpexs.decompiler.flash.types.MORPHGRADIENT;
import com.jpexs.decompiler.flash.types.MORPHGRADRECORD;
import com.jpexs.decompiler.flash.types.MORPHLINESTYLE;
import com.jpexs.decompiler.flash.types.MORPHLINESTYLE2;
import com.jpexs.decompiler.flash.types.MORPHLINESTYLEARRAY;
import com.jpexs.decompiler.flash.types.PIX15;
import com.jpexs.decompiler.flash.types.PIX24;
import com.jpexs.decompiler.flash.types.RECT;
import com.jpexs.decompiler.flash.types.RGB;
import com.jpexs.decompiler.flash.types.RGBA;
import com.jpexs.decompiler.flash.types.SHAPE;
import com.jpexs.decompiler.flash.types.SHAPEWITHSTYLE;
import com.jpexs.decompiler.flash.types.SOUNDENVELOPE;
import com.jpexs.decompiler.flash.types.SOUNDINFO;
import com.jpexs.decompiler.flash.types.TEXTRECORD;
import com.jpexs.decompiler.flash.types.ZONEDATA;
import com.jpexs.decompiler.flash.types.ZONERECORD;
import com.jpexs.decompiler.flash.types.filters.BEVELFILTER;
import com.jpexs.decompiler.flash.types.filters.BLURFILTER;
import com.jpexs.decompiler.flash.types.filters.COLORMATRIXFILTER;
import com.jpexs.decompiler.flash.types.filters.CONVOLUTIONFILTER;
import com.jpexs.decompiler.flash.types.filters.DROPSHADOWFILTER;
import com.jpexs.decompiler.flash.types.filters.FILTER;
import com.jpexs.decompiler.flash.types.filters.GLOWFILTER;
import com.jpexs.decompiler.flash.types.filters.GRADIENTBEVELFILTER;
import com.jpexs.decompiler.flash.types.filters.GRADIENTGLOWFILTER;
import com.jpexs.decompiler.flash.types.shaperecords.CurvedEdgeRecord;
import com.jpexs.decompiler.flash.types.shaperecords.EndShapeRecord;
import com.jpexs.decompiler.flash.types.shaperecords.SHAPERECORD;
import com.jpexs.decompiler.flash.types.shaperecords.StraightEdgeRecord;
import com.jpexs.decompiler.flash.types.shaperecords.StyleChangeRecord;
import com.jpexs.helpers.Helper;
import com.jpexs.helpers.ProgressListener;
import com.jpexs.helpers.streams.SeekableInputStream;
import com.jpexs.helpers.utf8.Utf8Helper;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.InflaterInputStream;

/**
 * Class for reading data from SWF file
 *
 * @author JPEXS
 */
public class SWFInputStream extends InputStream {

    private InputStream is;
    private long pos;
    private int version;
    private static final Logger logger = Logger.getLogger(SWFInputStream.class.getName());
    private final List<ProgressListener> listeners = new ArrayList<>();
    private long percentMax;
    private final List<byte[]> buffered = new ArrayList<>();
    private ByteArrayOutputStream buffer;

    public int getVersion() {
        return version;
    }

    public int getBufferLength() {
        return buffer.size();
    }

    public void startBuffer() {
        stopBuffer();
        buffer = new ByteArrayOutputStream();
    }

    public byte[] getBuffer() {
        return buffer.toByteArray();
    }

    public byte[] stopBuffer() {
        if (buffer != null) {
            byte[] ret = buffer.toByteArray();
            buffered.add(ret);
            buffer = null;
            return ret;
        }
        return null;
    }

    public void addPercentListener(ProgressListener listener) {
        listeners.add(listener);
    }

    public void removePercentListener(ProgressListener listener) {
        int index = listeners.indexOf(listener);
        if (index > -1) {
            listeners.remove(index);
        }
    }

    public void setPercentMax(long percentMax) {
        this.percentMax = percentMax;
    }

    /**
     * Constructor
     *
     * @param is Existing inputstream
     * @param version Version of SWF to read
     * @param startingPos
     */
    public SWFInputStream(InputStream is, int version, long startingPos) {
        this.version = version;
        this.is = is;
        pos = startingPos;
    }

    /**
     * Constructor
     *
     * @param is Existing inputstream
     * @param version Version of SWF to read
     */
    public SWFInputStream(InputStream is, int version) {
        this(is, version, 0L);
    }

    /**
     * Gets position in bytes in the stream
     *
     * @return Number of bytes
     */
    public long getPos() {
        return pos;
    }

    /**
     * Sets position in bytes in the stream
     *
     * @param pos Number of bytes
     * @throws java.io.IOException
     */
    public void seek(long pos) throws IOException {
        if (is instanceof SeekableInputStream) {
            SeekableInputStream sis = (SeekableInputStream) is;
            sis.seek(pos);
        } else {
            throw new IOException("Underlying stream is not a SeekableInputStream");
        }
    }

    /**
     * Reads one byte from the stream
     *
     * @return byte or -1 on error
     * @throws IOException
     */
    @Override
    public int read() throws IOException {
        bitPos = 0;
        try {
            return readNoBitReset();
        } catch (EOFException | EndOfStreamException ex) {
            Logger.getLogger(SWFInputStream.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    public int readEx() throws IOException {
        bitPos = 0;
        return readNoBitReset();
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        int bytesRead = super.read(b, off, len);
        bitPos = 0;
        //pos += bytesRead;
        return bytesRead;
    }

    public void alignByte() {
        bitPos = 0;
    }
    private int lastPercent = -1;

    private int readNoBitReset() throws IOException, EndOfStreamException {
        pos++;
        if (percentMax > 0) {
            int percent = (int) (pos * 100 / percentMax);
            if (lastPercent != percent) {
                for (ProgressListener pl : listeners) {
                    pl.progress(percent);
                }
                lastPercent = percent;
            }
        }
        int r = is.read();
        if (r == -1) {
            throw new EndOfStreamException();
        }
        return r;
    }

    /**
     * Reads one UI8 (Unsigned 8bit integer) value from the stream
     *
     * @return UI8 value or -1 on error
     * @throws IOException
     */
    public int readUI8() throws IOException {
        return readEx();
    }

    /**
     * Reads one string value from the stream
     *
     * @return String value
     * @throws IOException
     */
    public String readString() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int r;
        while (true) {
            r = readEx();
            if (r == 0) {
                return new String(baos.toByteArray(), Utf8Helper.charset);
            }
            baos.write(r);
        }
    }

    /**
     * Reads one UI32 (Unsigned 32bit integer) value from the stream
     *
     * @return UI32 value
     * @throws IOException
     */
    public long readUI32() throws IOException {
        return (readEx() + (readEx() << 8) + (readEx() << 16) + (readEx() << 24)) & 0xffffffff;
    }

    /**
     * Reads one UI16 (Unsigned 16bit integer) value from the stream
     *
     * @return UI16 value
     * @throws IOException
     */
    public int readUI16() throws IOException {
        return readEx() + (readEx() << 8);
    }

    public int readUI24() throws IOException {
        return readEx() + (readEx() << 8) + (readEx() << 16);
    }

    /**
     * Reads one SI32 (Signed 32bit integer) value from the stream
     *
     * @return SI32 value
     * @throws IOException
     */
    public long readSI32() throws IOException {
        long uval = readEx() + (readEx() << 8) + (readEx() << 16) + (readEx() << 24);
        if (uval >= 0x80000000) {
            return -(((~uval) & 0xffffffff) + 1);
        } else {
            return uval;
        }
    }

    /**
     * Reads one SI16 (Signed 16bit integer) value from the stream
     *
     * @return SI16 value
     * @throws IOException
     */
    public int readSI16() throws IOException {
        int uval = readEx() + (readEx() << 8);
        if (uval >= 0x8000) {
            return -(((~uval) & 0xffff) + 1);
        } else {
            return uval;
        }
    }

    /**
     * Reads one SI8 (Signed 8bit integer) value from the stream
     *
     * @return SI8 value
     * @throws IOException
     */
    public int readSI8() throws IOException {
        int uval = readEx();
        if (uval >= 0x80) {
            return -(((~uval) & 0xff) + 1);
        } else {
            return uval;
        }
    }

    /**
     * Reads one FIXED (Fixed point 16.16) value from the stream
     *
     * @return FIXED value
     * @throws IOException
     */
    public double readFIXED() throws IOException {
        int afterPoint = readUI16();
        int beforePoint = readUI16();
        return ((double) ((beforePoint << 16) + afterPoint)) / 65536;
    }

    /**
     * Reads one FIXED8 (Fixed point 8.8) value from the stream
     *
     * @return FIXED8 value
     * @throws IOException
     */
    public float readFIXED8() throws IOException {
        int afterPoint = readEx();
        int beforePoint = readEx();
        return beforePoint + (((float) afterPoint) / 256);
    }

    private long readLong() throws IOException {
        byte[] readBuffer = readBytesEx(8);
        return (((long) readBuffer[3] << 56)
                + ((long) (readBuffer[2] & 255) << 48)
                + ((long) (readBuffer[1] & 255) << 40)
                + ((long) (readBuffer[0] & 255) << 32)
                + ((long) (readBuffer[7] & 255) << 24)
                + ((readBuffer[6] & 255) << 16)
                + ((readBuffer[5] & 255) << 8)
                + ((readBuffer[4] & 255)));
    }

    /**
     * Reads one DOUBLE (double precision floating point value) value from the
     * stream
     *
     * @return DOUBLE value
     * @throws IOException
     */
    public double readDOUBLE() throws IOException {
        long el = readLong();
        double ret = Double.longBitsToDouble(el);
        return ret;
    }

    /**
     * Reads one FLOAT (single precision floating point value) value from the
     * stream
     *
     * @return FLOAT value
     * @throws IOException
     */
    public float readFLOAT() throws IOException {
        int val = (int) readUI32();
        float ret = Float.intBitsToFloat(val);
        /*int sign = val >> 31;
         int mantisa = val & 0x3FFFFF;
         int exp = (val >> 22) & 0xFF;
         float ret =(sign == 1 ? -1 : 1) * (float) Math.pow(2, exp)*  (1+((mantisa)/ (float)(1<<23)));*/
        return ret;
    }

    /**
     * Reads one FLOAT16 (16bit floating point value) value from the stream
     *
     * @return FLOAT16 value
     * @throws IOException
     */
    public float readFLOAT16() throws IOException {
        int val = readUI16();
        int sign = val >> 15;
        int mantisa = val & 0x3FF;
        int exp = (val >> 10) & 0x1F;
        float ret = (sign == 1 ? -1 : 1) * (float) Math.pow(2, exp) * (1 + ((mantisa) / (float) (1 << 10)));
        return ret;
    }

    /**
     * Reads bytes from the stream
     *
     * @param count Number of bytes to read
     * @return Array of read bytes
     * @throws IOException
     */
    public byte[] readBytesEx(long count) throws IOException {
        if (count <= 0) {
            return new byte[0];
        }
        byte[] ret = new byte[(int) count];
        for (int i = 0; i < count; i++) {
            ret[i] = (byte) readEx();
        }
        return ret;
    }

    /**
     * Reads bytes from the stream
     *
     * @param count Number of bytes to read
     * @return Array of read bytes
     * @throws IOException
     */
    public byte[] readBytes(int count) throws IOException {
        if (count <= 0) {
            return new byte[0];
        }
        byte[] ret = new byte[count];
        int i = 0;
        try {
            for (i = 0; i < count; i++) {
                ret[i] = (byte) readEx();
            }
        } catch (EOFException | EndOfStreamException ex) {
            ret = Arrays.copyOf(ret, i); // truncate array
            Logger.getLogger(SWFInputStream.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ret;
    }

    public byte[] readBytesZlib(long count) throws IOException {
        byte[] data = readBytesEx(count);
        InflaterInputStream dis = new InflaterInputStream(new ByteArrayInputStream(data));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buf = new byte[4096];
        int c = 0;
        while ((c = dis.read(buf)) > 0) {
            baos.write(buf, 0, c);
        }
        return baos.toByteArray();
    }

    /**
     * Reads one EncodedU32 (Encoded unsigned 32bit value) value from the stream
     *
     * @return U32 value
     * @throws IOException
     */
    public long readEncodedU32() throws IOException {
        int result = readEx();
        if ((result & 0x00000080) == 0) {
            return result;
        }
        result = (result & 0x0000007f) | (readEx()) << 7;
        if ((result & 0x00004000) == 0) {
            return result;
        }
        result = (result & 0x00003fff) | (readEx()) << 14;
        if ((result & 0x00200000) == 0) {
            return result;
        }
        result = (result & 0x001fffff) | (readEx()) << 21;
        if ((result & 0x10000000) == 0) {
            return result;
        }
        result = (result & 0x0fffffff) | (readEx()) << 28;
        return result;
    }
    private int bitPos = 0;
    private int tempByte = 0;

    /**
     * Reads UB[nBits] (Unsigned-bit value) value from the stream
     *
     * @param nBits Number of bits which represent value
     * @return Unsigned value
     * @throws IOException
     */
    public long readUB(int nBits) throws IOException {
        if (nBits == 0) {
            return 0;
        }
        long ret = 0;
        if (bitPos == 0) {
            tempByte = readNoBitReset();
        }
        for (int bit = 0; bit < nBits; bit++) {
            int nb = (tempByte >> (7 - bitPos)) & 1;
            ret += (nb << (nBits - 1 - bit));
            bitPos++;
            if (bitPos == 8) {
                bitPos = 0;
                if (bit != nBits - 1) {
                    tempByte = readNoBitReset();
                }
            }
        }
        return ret;
    }

    /**
     * Reads SB[nBits] (Signed-bit value) value from the stream
     *
     * @param nBits Number of bits which represent value
     * @return Signed value
     * @throws IOException
     */
    public long readSB(int nBits) throws IOException {
        int uval = (int) readUB(nBits);

        int shift = 32 - nBits;
        // sign extension
        uval = (uval << shift) >> shift;
        return uval;
    }

    /**
     * Reads FB[nBits] (Signed fixed-point bit value) value from the stream
     *
     * @param nBits Number of bits which represent value
     * @return Fixed-point value
     * @throws IOException
     */
    public float readFB(int nBits) throws IOException {
        if (nBits == 0) {
            return 0;
        }
        float val = readSB(nBits);
        float ret = val / 0x10000;
        return ret;
    }

    /**
     * Reads one RECT value from the stream
     *
     * @return RECT value
     * @throws IOException
     */
    public RECT readRECT() throws IOException {
        RECT ret = new RECT();
        int NBits = (int) readUB(5);
        ret.Xmin = (int) readSB(NBits);
        ret.Xmax = (int) readSB(NBits);
        ret.Ymin = (int) readSB(NBits);
        ret.Ymax = (int) readSB(NBits);
        ret.nbits = NBits;
        alignByte();
        return ret;
    }

    private static void dumpTag(PrintStream out, int version, Tag tag, int level) {
        StringBuilder sb = new StringBuilder();
        sb.append(Helper.formatHex((int) tag.getPos(), 8));
        sb.append(": ");
        sb.append(Helper.indent(level, "", "  "));
        sb.append(Helper.format(tag.toString(), 25 - 2 * level));
        sb.append(" tagId=");
        sb.append(Helper.formatInt(tag.getId(), 3));
        sb.append(" len=");
        sb.append(Helper.formatInt((int) tag.getOrigDataLength(), 8));
        sb.append("  ");
        sb.append(Helper.bytesToHexString(64, tag.getData(), 0));
        out.println(sb.toString());
//        out.println(Utils.formatHex((int)tag.getPos(), 8) + ": " + Utils.indent(level, "") + Utils.format(tag.toString(), 25 - 2*level) + " tagId="+tag.getId()+" len="+tag.getOrigDataLength()+": "+Utils.bytesToHexString(64, tag.getData(version), 0));
        if (tag.hasSubTags()) {
            for (Tag subTag : tag.getSubTags()) {
                dumpTag(out, version, subTag, level + 1);
            }
        }
    }

    private class TagResolutionTask implements Callable<Tag> {

        private final Tag tag;
        private final int level;
        private final boolean parallel;
        private final boolean skipUnusualTags;
        private final SWF swf;
        private final boolean gfx;

        public TagResolutionTask(SWF swf, Tag tag, int level, boolean parallel, boolean skipUnusualTags, boolean gfx) {
            this.tag = tag;
            this.level = level;
            this.parallel = parallel;
            this.skipUnusualTags = skipUnusualTags;
            this.swf = swf;
            this.gfx = gfx;
        }

        @Override
        public Tag call() throws Exception {
            try {
                return SWFInputStream.resolveTag(swf, tag, level, parallel, skipUnusualTags, gfx);
            } catch (EndOfStreamException ex) {
                Logger.getLogger(SWFInputStream.class.getName()).log(Level.SEVERE, null, ex);
                return tag;
            }
        }
    }

    /**
     * Reads list of tags from the stream. Reading ends with End tag(=0) or end
     * of the stream. Optionally can skip AS1/2 tags when file is AS3
     *
     * @param swf
     * @param timelined
     * @param level
     * @param parallel
     * @param skipUnusualTags
     * @param parseTags
     * @param gfx
     * @return List of tags
     * @throws IOException
     * @throws java.lang.InterruptedException
     */
    public List<Tag> readTagList(SWF swf, Timelined timelined, int level, boolean parallel, boolean skipUnusualTags, boolean parseTags, boolean gfx) throws IOException, InterruptedException {
        ExecutorService executor = null;
        List<Future<Tag>> futureResults = new ArrayList<>();
        if (parallel) {
            executor = Executors.newFixedThreadPool(Configuration.parallelThreadCount.get());
            futureResults = new ArrayList<>();
        }
        List<Tag> tags = new ArrayList<>();
        Tag tag;
        Tag previousTag = null;
        boolean isAS3 = false;
        while (true) {
            long pos = getPos();
            try {
                tag = readTag(swf, level, pos, parseTags && !parallel, parallel, skipUnusualTags, gfx);
            } catch (EOFException | EndOfStreamException ex) {
                tag = null;
            }
            if (tag == null) {
                break;
            }

            tag.setTimelined(timelined);
            if (!parallel) {
                tags.add(tag);
            }
            if (Configuration.dumpTags.get() && level == 0) {
                dumpTag(System.out, version, tag, level);
            }
            tag.previousTag = previousTag;
            previousTag = tag;

            boolean doParse;
            if (!skipUnusualTags) {
                doParse = true;
            } else {
                switch (tag.getId()) {
                    case FileAttributesTag.ID: //FileAttributes
                        FileAttributesTag fileAttributes = (FileAttributesTag) resolveTag(swf, tag, level, parallel, skipUnusualTags, gfx);
                        if (fileAttributes.actionScript3) {
                            isAS3 = true;
                        }
                        doParse = true;
                        break;
                    case DoActionTag.ID:
                    case DoInitActionTag.ID:
                        if (isAS3) {
                            doParse = false;
                        } else {
                            doParse = true;
                        }
                        break;
                    case ShowFrameTag.ID:
                    case PlaceObjectTag.ID:
                    case PlaceObject2Tag.ID:
                    case RemoveObjectTag.ID:
                    case RemoveObject2Tag.ID:
                    case PlaceObject3Tag.ID: //?
                    case StartSoundTag.ID:
                    case FrameLabelTag.ID:
                    case SoundStreamHeadTag.ID:
                    case SoundStreamHead2Tag.ID:
                    case SoundStreamBlockTag.ID:
                    case VideoFrameTag.ID:
                    case EndTag.ID:
                        doParse = true;
                        break;
                    default:
                        if (level > 0) { //No such tags in DefineSprite allowed
                            Logger.getLogger(SWFInputStream.class.getName()).log(Level.FINE, "Tag({0}) found in DefineSprite => Ignored", tag.getId());
                            doParse = false;
                        } else {
                            doParse = true;
                        }

                }
            }
            if (!parseTags) {
                doParse = false;
            }

            if (doParse) {
                if (parallel) {
                    Future<Tag> future = executor.submit(new TagResolutionTask(swf, tag, level, parallel, skipUnusualTags, gfx));
                    futureResults.add(future);
                }
            }

            if (tag.getId() == EndTag.ID) {
                break;
            }
        }

        if (parallel) {
            for (Future<Tag> future : futureResults) {
                try {
                    tags.add(future.get());
                } catch (InterruptedException ex) {
                    future.cancel(true);
                } catch (ExecutionException e) {
                    Logger.getLogger(SWFInputStream.class.getName()).log(Level.SEVERE, "Error during tag reading", e);
                }
            }

            executor.shutdown();
        }
        return tags;
    }

    public static Tag resolveTag(SWF swf, Tag tag, int level, boolean parallel, boolean skipUnusualTags, boolean gfx) throws InterruptedException {
        Tag ret;

        byte[] data = tag.getData();
        byte[] headerData = tag.getOriginalHeaderData();
        long pos = tag.getPos();
        try {
            switch (tag.getId()) {
                case 0:
                    ret = new EndTag(swf, headerData, data, pos);
                    break;
                case 1:
                    ret = new ShowFrameTag(swf, headerData, data, pos);
                    break;
                case 2:
                    ret = new DefineShapeTag(swf, headerData, data, pos);
                    break;
                //case 3: FreeCharacter
                case 4:
                    ret = new PlaceObjectTag(swf, headerData, data, pos);
                    break;
                case 5:
                    ret = new RemoveObjectTag(swf, headerData, data, pos);
                    break;
                case 6:
                    ret = new DefineBitsTag(swf, headerData, data, pos);
                    break;
                case 7:
                    ret = new DefineButtonTag(swf, headerData, data, pos);
                    break;
                case 8:
                    ret = new JPEGTablesTag(swf, headerData, data, pos);
                    break;
                case 9:
                    ret = new SetBackgroundColorTag(swf, headerData, data, pos);
                    break;
                case 10:
                    ret = new DefineFontTag(swf, headerData, data, pos);
                    break;
                case 11:
                    ret = new DefineTextTag(swf, headerData, data, pos);
                    break;
                case 12:
                    ret = new DoActionTag(swf, headerData, data, pos);
                    break;
                case 13:
                    ret = new DefineFontInfoTag(swf, headerData, data, pos);
                    break;
                case 14:
                    ret = new DefineSoundTag(swf, headerData, data, pos);
                    break;
                case 15:
                    ret = new StartSoundTag(swf, headerData, data, pos);
                    break;
                //case 16:
                case 17:
                    ret = new DefineButtonSoundTag(swf, headerData, data, pos);
                    break;
                case 18:
                    ret = new SoundStreamHeadTag(swf, headerData, data, pos);
                    break;
                case 19:
                    ret = new SoundStreamBlockTag(swf, headerData, data, pos);
                    break;
                case 21:
                    ret = new DefineBitsJPEG2Tag(swf, headerData, data, pos);
                    break;
                case 20:
                    ret = new DefineBitsLosslessTag(swf, headerData, data, pos);
                    break;
                case 22:
                    ret = new DefineShape2Tag(swf, headerData, data, pos);
                    break;
                case 23:
                    ret = new DefineButtonCxformTag(swf, headerData, data, pos);
                    break;
                case 24:
                    ret = new ProtectTag(swf, headerData, data, pos);
                    break;
                //case 25: PathsArePostscript
                case 26:
                    ret = new PlaceObject2Tag(swf, headerData, data, pos);
                    break;
                //case 27: 
                case 28:
                    ret = new RemoveObject2Tag(swf, headerData, data, pos);
                    break;
                //case 29: SyncFrame
                //case 30:
                //case 31: FreeAll
                case 32:
                    ret = new DefineShape3Tag(swf, headerData, data, pos);
                    break;
                case 33:
                    ret = new DefineText2Tag(swf, headerData, data, pos);
                    break;
                case 34:
                    ret = new DefineButton2Tag(swf, headerData, data, pos);
                    break;
                case 35:
                    ret = new DefineBitsJPEG3Tag(swf, headerData, data, pos);
                    break;
                case 36:
                    ret = new DefineBitsLossless2Tag(swf, headerData, data, pos);
                    break;
                case 37:
                    ret = new DefineEditTextTag(swf, headerData, data, pos);
                    break;
                //case 38: DefineVideo
                case 39:
                    ret = new DefineSpriteTag(swf, headerData, data, level, pos, parallel, skipUnusualTags);
                    break;
                //case 40: NameCharacter
                case 41:
                    ret = new ProductInfoTag(swf, headerData, data, pos);
                    break;
                //case 42: DefineTextFormat
                case 43:
                    ret = new FrameLabelTag(swf, headerData, data, pos);
                    break;
                //case 44:
                case 45:
                    ret = new SoundStreamHead2Tag(swf, headerData, data, pos);
                    break;
                case 46:
                    ret = new DefineMorphShapeTag(swf, headerData, data, pos);
                    break;
                //case 47: GenerateFrame
                case 48:
                    ret = new DefineFont2Tag(swf, headerData, data, pos);
                    break;
                //case 49: GeneratorCommand
                //case 50: DefineCommandObject
                //case 51: CharacterSet
                //case 52: ExternalFont
                //case 53-55
                case 56:
                    ret = new ExportAssetsTag(swf, headerData, data, pos);
                    break;
                case 57:
                    ret = new ImportAssetsTag(swf, headerData, data, pos);
                    break;
                case 58:
                    ret = new EnableDebuggerTag(swf, headerData, data, pos);
                    break;
                case 59:
                    ret = new DoInitActionTag(swf, headerData, data, pos);
                    break;
                case 60:
                    ret = new DefineVideoStreamTag(swf, headerData, data, pos);
                    break;
                case 61:
                    ret = new VideoFrameTag(swf, headerData, data, pos);
                    break;
                case 62:
                    ret = new DefineFontInfo2Tag(swf, headerData, data, pos);
                    break;
                case 63:
                    ret = new DebugIDTag(swf, headerData, data, pos);
                    break;
                case 64:
                    ret = new EnableDebugger2Tag(swf, headerData, data, pos);
                    break;
                case 65:
                    ret = new ScriptLimitsTag(swf, headerData, data, pos);
                    break;
                case 66:
                    ret = new SetTabIndexTag(swf, headerData, data, pos);
                    break;
                //case 67-68:
                case 69:
                    ret = new FileAttributesTag(swf, headerData, data, pos);
                    break;
                case 70:
                    ret = new PlaceObject3Tag(swf, headerData, data, pos);
                    break;
                case 71:
                    ret = new ImportAssets2Tag(swf, headerData, data, pos);
                    break;
                case 72:
                    ret = new DoABCTag(swf, headerData, data, pos);
                    break;
                case 73:
                    ret = new DefineFontAlignZonesTag(swf, headerData, data, pos);
                    break;
                case 74:
                    ret = new CSMTextSettingsTag(swf, headerData, data, pos);
                    break;
                case 75:
                    ret = new DefineFont3Tag(swf, headerData, data, pos);
                    break;
                case 76:
                    ret = new SymbolClassTag(swf, headerData, data, pos);
                    break;
                case 77:
                    ret = new MetadataTag(swf, headerData, data, pos);
                    break;
                case 78:
                    ret = new DefineScalingGridTag(swf, headerData, data, pos);
                    break;
                //case 79-81:
                case 82:
                    ret = new DoABCDefineTag(swf, headerData, data, pos);
                    break;
                case 83:
                    ret = new DefineShape4Tag(swf, headerData, data, pos);
                    break;
                case 84:
                    ret = new DefineMorphShape2Tag(swf, headerData, data, pos);
                    break;
                //case 85:
                case 86:
                    ret = new DefineSceneAndFrameLabelDataTag(swf, headerData, data, pos);
                    break;
                case 87:
                    ret = new DefineBinaryDataTag(swf, headerData, data, pos);
                    break;
                case 88:
                    ret = new DefineFontNameTag(swf, headerData, data, pos);
                    break;
                case 89:
                    ret = new StartSound2Tag(swf, headerData, data, pos);
                    break;
                case 90:
                    ret = new DefineBitsJPEG4Tag(swf, headerData, data, pos);
                    break;
                case 91:
                    ret = new DefineFont4Tag(swf, headerData, data, pos);
                    break;
                //case 92: certificate
                case 93:
                    ret = new EnableTelemetryTag(swf, headerData, data, pos);
                    break;
                case 94:
                    ret = new PlaceObject4Tag(swf, headerData, data, pos);
                    break;
                default:
                    if (gfx) {   //GFX tags only in GFX files. There may be incorrect GFX tags in non GFX files
                        switch (tag.getId()) {
                            case 1000:
                                ret = new ExporterInfoTag(swf, headerData, data, pos);
                                break;
                            case 1001:
                                ret = new DefineExternalImage(swf, headerData, data, pos);
                                break;
                            case 1002:
                                ret = new FontTextureInfo(swf, headerData, data, pos);
                                break;
                            case 1003:
                                ret = new DefineExternalGradient(swf, headerData, data, pos);
                                break;
                            case 1004:
                                ret = new DefineGradientMap(swf, headerData, data, pos);
                                break;
                            case 1005:
                                ret = new DefineCompactedFont(swf, headerData, data, pos);
                                break;
                            case 1006:
                                ret = new DefineExternalSound(swf, headerData, data, pos);
                                break;
                            case 1007:
                                ret = new DefineExternalStreamSound(swf, headerData, data, pos);
                                break;
                            case 1008:
                                ret = new DefineSubImage(swf, headerData, data, pos);
                                break;
                            case 1009:
                                ret = new DefineExternalImage2(swf, headerData, data, pos);
                                break;
                            default:
                                ret = new UnknownTag(swf, tag.getId(), headerData, data, pos);
                        }
                    } else {
                        ret = new UnknownTag(swf, tag.getId(), headerData, data, pos);
                    }
            }
        } catch (IOException ex) {
            Logger.getLogger(SWFInputStream.class.getName()).log(Level.SEVERE, "Error during tag reading", ex);
            ret = new Tag(swf, tag.getId(), "ErrorTag", headerData, data, pos);
        }
        ret.previousTag = tag.previousTag;
        ret.forceWriteAsLong = tag.forceWriteAsLong;
        ret.setTimelined(tag.getTimelined());
        return ret;
    }

    /**
     * Reads one Tag from the stream with optional resolving (= reading tag
     * content)
     *
     * @param swf
     * @param level
     * @param pos
     * @param resolve
     * @param parallel
     * @param skipUnusualTags
     * @param gfx
     * @return Tag or null when End tag
     * @throws IOException
     * @throws java.lang.InterruptedException
     */
    public Tag readTag(SWF swf, int level, long pos, boolean resolve, boolean parallel, boolean skipUnusualTags, boolean gfx) throws IOException, InterruptedException {
        int tagIDTagLength = readUI16();
        int tagID = (tagIDTagLength) >> 6;

        logger.log(Level.INFO, "Reading tag. ID={0}, position: {1}", new Object[]{tagID, pos});

        long tagLength = (tagIDTagLength & 0x003F);
        boolean readLong = false;
        if (tagLength == 0x3f) {
            tagLength = readSI32();
            readLong = true;
        }
        byte[] data = readBytes((int) tagLength);
        Tag ret = new Tag(swf, tagID, "Unresolved", Tag.getTagHeader(tagIDTagLength, tagLength, readLong, swf.version), data, pos);
        ret.forceWriteAsLong = readLong;

        if (resolve) {
            try {
                ret = resolveTag(swf, ret, level, parallel, skipUnusualTags, gfx);
            } catch (EndOfStreamException ex) {
                Logger.getLogger(SWFInputStream.class.getName()).log(Level.SEVERE, null, ex);
            }

            if (Configuration.debugMode.get()) {
                byte[] dataNew = ret.getData();
                int ignoreFirst = 0;
                for (int i = 0; i < data.length; i++) {
                    if (i >= dataNew.length) {
                        break;
                    }
                    if (dataNew[i] != data[i]) {
                        if (ignoreFirst > 0) {
                            ignoreFirst--;
                            continue;
                        }
                        String e = "TAG " + ret.toString() + " WRONG, ";
                        for (int j = i - 10; j <= i + 5; j++) {
                            while (j < 0) {
                                j++;
                            }
                            if (j >= data.length) {
                                break;
                            }
                            if (j >= dataNew.length) {
                                break;
                            }
                            if (j >= i) {
                                e += (Long.toHexString(data[j] & 0xff) + " ( is " + Long.toHexString(dataNew[j] & 0xff) + ") ");
                            } else {
                                e += (Long.toHexString(data[j] & 0xff) + " ");
                            }
                        }
                        logger.fine(e);
                    }
                }
            }
        }
        return ret;
    }

    /**
     * Reads one Action from the stream
     *
     * @param cpool
     * @return Action or null when ActionEndFlag or end of the stream
     * @throws IOException
     */
    public Action readAction(ConstantPool cpool) throws IOException {
        int actionCode = -1;

        try {
            actionCode = readUI8();
            if (actionCode == 0) {
                return new ActionEnd();
            }
            if (actionCode == -1) {
                return null;
            }
            int actionLength = 0;
            if (actionCode >= 0x80) {
                actionLength = readUI16();
            }
            switch (actionCode) {
                //SWF3 Actions
                case 0x81:
                    return new ActionGotoFrame(actionLength, this);
                case 0x83:
                    return new ActionGetURL(actionLength, this, version);
                case 0x04:
                    return new ActionNextFrame();
                case 0x05:
                    return new ActionPrevFrame();
                case 0x06:
                    return new ActionPlay();
                case 0x07:
                    return new ActionStop();
                case 0x08:
                    return new ActionToggleQuality();
                case 0x09:
                    return new ActionStopSounds();
                case 0x8A:
                    return new ActionWaitForFrame(actionLength, this, cpool);
                case 0x8B:
                    return new ActionSetTarget(actionLength, this, version);
                case 0x8C:
                    return new ActionGoToLabel(actionLength, this, version);
                //SWF4 Actions
                case 0x96:
                    return new ActionPush(actionLength, this, version);
                case 0x17:
                    return new ActionPop();
                case 0x0A:
                    return new ActionAdd();
                case 0x0B:
                    return new ActionSubtract();
                case 0x0C:
                    return new ActionMultiply();
                case 0x0D:
                    return new ActionDivide();
                case 0x0E:
                    return new ActionEquals();
                case 0x0F:
                    return new ActionLess();
                case 0x10:
                    return new ActionAnd();
                case 0x11:
                    return new ActionOr();
                case 0x12:
                    return new ActionNot();
                case 0x13:
                    return new ActionStringEquals();
                case 0x14:
                    return new ActionStringLength();
                case 0x21:
                    return new ActionStringAdd();
                case 0x15:
                    return new ActionStringExtract();
                case 0x29:
                    return new ActionStringLess();
                case 0x31:
                    return new ActionMBStringLength();
                case 0x35:
                    return new ActionMBStringExtract();
                case 0x18:
                    return new ActionToInteger();
                case 0x32:
                    return new ActionCharToAscii();
                case 0x33:
                    return new ActionAsciiToChar();
                case 0x36:
                    return new ActionMBCharToAscii();
                case 0x37:
                    return new ActionMBAsciiToChar();
                case 0x99:
                    return new ActionJump(actionLength, this);
                case 0x9D:
                    return new ActionIf(actionLength, this);
                case 0x9E:
                    return new ActionCall(actionLength);
                case 0x1C:
                    return new ActionGetVariable();
                case 0x1D:
                    return new ActionSetVariable();
                case 0x9A:
                    return new ActionGetURL2(actionLength, this);
                case 0x9F:
                    return new ActionGotoFrame2(actionLength, this);
                case 0x20:
                    return new ActionSetTarget2();
                case 0x22:
                    return new ActionGetProperty();
                case 0x23:
                    return new ActionSetProperty();
                case 0x24:
                    return new ActionCloneSprite();
                case 0x25:
                    return new ActionRemoveSprite();
                case 0x27:
                    return new ActionStartDrag();
                case 0x28:
                    return new ActionEndDrag();
                case 0x8D:
                    return new ActionWaitForFrame2(actionLength, this, cpool);
                case 0x26:
                    return new ActionTrace();
                case 0x34:
                    return new ActionGetTime();
                case 0x30:
                    return new ActionRandomNumber();
                //SWF5 Actions
                case 0x3D:
                    return new ActionCallFunction();
                case 0x52:
                    return new ActionCallMethod();
                case 0x88:
                    return new ActionConstantPool(actionLength, this, version);
                case 0x9B:
                    return new ActionDefineFunction(actionLength, this, version);
                case 0x3C:
                    return new ActionDefineLocal();
                case 0x41:
                    return new ActionDefineLocal2();
                case 0x3A:
                    return new ActionDelete();
                case 0x3B:
                    return new ActionDelete2();
                case 0x46:
                    return new ActionEnumerate();
                case 0x49:
                    return new ActionEquals2();
                case 0x4E:
                    return new ActionGetMember();
                case 0x42:
                    return new ActionInitArray();
                case 0x43:
                    return new ActionInitObject();
                case 0x53:
                    return new ActionNewMethod();
                case 0x40:
                    return new ActionNewObject();
                case 0x4F:
                    return new ActionSetMember();
                case 0x45:
                    return new ActionTargetPath();
                case 0x94:
                    return new ActionWith(actionLength, this, version);
                case 0x4A:
                    return new ActionToNumber();
                case 0x4B:
                    return new ActionToString();
                case 0x44:
                    return new ActionTypeOf();
                case 0x47:
                    return new ActionAdd2();
                case 0x48:
                    return new ActionLess2();
                case 0x3F:
                    return new ActionModulo();
                case 0x60:
                    return new ActionBitAnd();
                case 0x63:
                    return new ActionBitLShift();
                case 0x61:
                    return new ActionBitOr();
                case 0x64:
                    return new ActionBitRShift();
                case 0x65:
                    return new ActionBitURShift();
                case 0x62:
                    return new ActionBitXor();
                case 0x51:
                    return new ActionDecrement();
                case 0x50:
                    return new ActionIncrement();
                case 0x4C:
                    return new ActionPushDuplicate();
                case 0x3E:
                    return new ActionReturn();
                case 0x4D:
                    return new ActionStackSwap();
                case 0x87:
                    return new ActionStoreRegister(actionLength, this);
                //SWF6 Actions
                case 0x54:
                    return new ActionInstanceOf();
                case 0x55:
                    return new ActionEnumerate2();
                case 0x66:
                    return new ActionStrictEquals();
                case 0x67:
                    return new ActionGreater();
                case 0x68:
                    return new ActionStringGreater();
                //SWF7 Actions
                case 0x8E:
                    return new ActionDefineFunction2(actionLength, this, version);
                case 0x69:
                    return new ActionExtends();
                case 0x2B:
                    return new ActionCastOp();
                case 0x2C:
                    return new ActionImplementsOp();
                case 0x8F:
                    return new ActionTry(actionLength, this, version);
                case 0x2A:
                    return new ActionThrow();
                default:
                    /*if (actionLength > 0) {
                     //skip(actionLength);
                     }*/
                    //throw new UnknownActionException(actionCode);
                    Action r = new ActionNop();
                    r.actionCode = actionCode;
                    r.actionLength = actionLength;
                    return r;
                //return new Action(actionCode, actionLength);
            }
        } catch (EndOfStreamException | ArrayIndexOutOfBoundsException eos) {
            return null;
        }
    }

    /**
     * Reads one MATRIX value from the stream
     *
     * @return MATRIX value
     * @throws IOException
     */
    public MATRIX readMatrix() throws IOException {
        MATRIX ret = new MATRIX();
        ret.hasScale = readUB(1) == 1;
        if (ret.hasScale) {
            int NScaleBits = (int) readUB(5);
            ret.scaleX = (int) readSB(NScaleBits);
            ret.scaleY = (int) readSB(NScaleBits);
            ret.nScaleBits = NScaleBits;
        }
        ret.hasRotate = readUB(1) == 1;
        if (ret.hasRotate) {
            int NRotateBits = (int) readUB(5);
            ret.rotateSkew0 = (int) readSB(NRotateBits);
            ret.rotateSkew1 = (int) readSB(NRotateBits);
            ret.nRotateBits = NRotateBits;
        }
        int NTranslateBits = (int) readUB(5);
        ret.translateX = (int) readSB(NTranslateBits);
        ret.translateY = (int) readSB(NTranslateBits);
        ret.nTranslateBits = NTranslateBits;
        alignByte();
        return ret;
    }

    /**
     * Reads one CXFORMWITHALPHA value from the stream
     *
     * @return CXFORMWITHALPHA value
     * @throws IOException
     */
    public CXFORMWITHALPHA readCXFORMWITHALPHA() throws IOException {
        CXFORMWITHALPHA ret = new CXFORMWITHALPHA();
        ret.hasAddTerms = readUB(1) == 1;
        ret.hasMultTerms = readUB(1) == 1;
        int Nbits = (int) readUB(4);
        ret.nbits = Nbits;
        if (ret.hasMultTerms) {
            ret.redMultTerm = (int) readSB(Nbits);
            ret.greenMultTerm = (int) readSB(Nbits);
            ret.blueMultTerm = (int) readSB(Nbits);
            ret.alphaMultTerm = (int) readSB(Nbits);
        }
        if (ret.hasAddTerms) {
            ret.redAddTerm = (int) readSB(Nbits);
            ret.greenAddTerm = (int) readSB(Nbits);
            ret.blueAddTerm = (int) readSB(Nbits);
            ret.alphaAddTerm = (int) readSB(Nbits);
        }
        alignByte();
        return ret;
    }

    /**
     * Reads one CXFORM value from the stream
     *
     * @return CXFORM value
     * @throws IOException
     */
    public CXFORM readCXFORM() throws IOException {
        CXFORM ret = new CXFORM();
        ret.hasAddTerms = readUB(1) == 1;
        ret.hasMultTerms = readUB(1) == 1;
        int Nbits = (int) readUB(4);
        ret.nbits = Nbits;
        if (ret.hasMultTerms) {
            ret.redMultTerm = (int) readSB(Nbits);
            ret.greenMultTerm = (int) readSB(Nbits);
            ret.blueMultTerm = (int) readSB(Nbits);
        }
        if (ret.hasAddTerms) {
            ret.redAddTerm = (int) readSB(Nbits);
            ret.greenAddTerm = (int) readSB(Nbits);
            ret.blueAddTerm = (int) readSB(Nbits);
        }
        alignByte();
        return ret;
    }

    /**
     * Reads one CLIPEVENTFLAGS value from the stream
     *
     * @return CLIPEVENTFLAGS value
     * @throws IOException
     */
    public CLIPEVENTFLAGS readCLIPEVENTFLAGS() throws IOException {
        CLIPEVENTFLAGS ret = new CLIPEVENTFLAGS();
        ret.clipEventKeyUp = readUB(1) == 1;
        ret.clipEventKeyDown = readUB(1) == 1;
        ret.clipEventMouseUp = readUB(1) == 1;
        ret.clipEventMouseDown = readUB(1) == 1;
        ret.clipEventMouseMove = readUB(1) == 1;
        ret.clipEventUnload = readUB(1) == 1;
        ret.clipEventEnterFrame = readUB(1) == 1;
        ret.clipEventLoad = readUB(1) == 1;
        ret.clipEventDragOver = readUB(1) == 1;
        ret.clipEventRollOut = readUB(1) == 1;
        ret.clipEventRollOver = readUB(1) == 1;
        ret.clipEventReleaseOutside = readUB(1) == 1;
        ret.clipEventRelease = readUB(1) == 1;
        ret.clipEventPress = readUB(1) == 1;
        ret.clipEventInitialize = readUB(1) == 1;
        ret.clipEventData = readUB(1) == 1;
        if (version >= 6) {
            ret.reserved = (int) readUB(5);
            ret.clipEventConstruct = readUB(1) == 1;
            ret.clipEventKeyPress = readUB(1) == 1;
            ret.clipEventDragOut = readUB(1) == 1;
            ret.reserved2 = (int) readUB(8);
        }
        return ret;
    }

    /**
     * Reads one CLIPACTIONRECORD value from the stream
     *
     * @param swf
     * @param tag
     * @return CLIPACTIONRECORD value
     * @throws IOException
     */
    public CLIPACTIONRECORD readCLIPACTIONRECORD(SWF swf, Tag tag) throws IOException {
        CLIPACTIONRECORD ret = new CLIPACTIONRECORD(swf, this, getPos(), tag);
        if (ret.eventFlags.isClear()) {
            return null;
        }
        //ret.actions = (new SWFInputStream(new ByteArrayInputStream(readBytes(actionRecordSize)), version)).readActionList();
        return ret;
    }

    /**
     * Reads one CLIPACTIONS value from the stream
     *
     * @param swf
     * @param tag
     * @return CLIPACTIONS value
     * @throws IOException
     */
    public CLIPACTIONS readCLIPACTIONS(SWF swf, Tag tag) throws IOException {
        CLIPACTIONS ret = new CLIPACTIONS();
        ret.reserved = readUI16();
        ret.allEventFlags = readCLIPEVENTFLAGS();
        CLIPACTIONRECORD cr;
        ret.clipActionRecords = new ArrayList<>();
        while ((cr = readCLIPACTIONRECORD(swf, tag)) != null) {
            ret.clipActionRecords.add(cr);
        }
        return ret;
    }

    /**
     * Reads one COLORMATRIXFILTER value from the stream
     *
     * @return COLORMATRIXFILTER value
     * @throws IOException
     */
    public COLORMATRIXFILTER readCOLORMATRIXFILTER() throws IOException {
        COLORMATRIXFILTER ret = new COLORMATRIXFILTER();
        ret.matrix = new float[20];
        for (int i = 0; i < 20; i++) {
            ret.matrix[i] = readFLOAT();
        }
        return ret;
    }

    /**
     * Reads one RGBA value from the stream
     *
     * @return RGBA value
     * @throws IOException
     */
    public RGBA readRGBA() throws IOException {
        RGBA ret = new RGBA();
        ret.red = readUI8();
        ret.green = readUI8();
        ret.blue = readUI8();
        ret.alpha = readUI8();
        return ret;
    }

    /**
     * Reads one ARGB value from the stream
     *
     * @return ARGB value
     * @throws IOException
     */
    public ARGB readARGB() throws IOException {
        ARGB ret = new ARGB();
        ret.alpha = readUI8();
        ret.red = readUI8();
        ret.green = readUI8();
        ret.blue = readUI8();
        return ret;
    }

    /**
     * Reads one RGB value from the stream
     *
     * @return RGB value
     * @throws IOException
     */
    public RGB readRGB() throws IOException {
        RGB ret = new RGB();
        ret.red = readUI8();
        ret.green = readUI8();
        ret.blue = readUI8();
        return ret;
    }

    /**
     * Reads one CONVOLUTIONFILTER value from the stream
     *
     * @return CONVOLUTIONFILTER value
     * @throws IOException
     */
    public CONVOLUTIONFILTER readCONVOLUTIONFILTER() throws IOException {
        CONVOLUTIONFILTER ret = new CONVOLUTIONFILTER();
        ret.matrixX = readUI8();
        ret.matrixY = readUI8();
        ret.divisor = readFLOAT();
        ret.bias = readFLOAT();
        ret.matrix = new float[ret.matrixX][ret.matrixY];
        for (int x = 0; x < ret.matrixX; x++) {
            for (int y = 0; y < ret.matrixY; y++) {
                ret.matrix[x][y] = readFLOAT();
            }
        }
        ret.defaultColor = readRGBA();
        ret.reserved = (int) readUB(6);
        ret.clamp = readUB(1) == 1;
        ret.preserveAlpha = readUB(1) == 1;
        return ret;
    }

    /**
     * Reads one BLURFILTER value from the stream
     *
     * @return BLURFILTER value
     * @throws IOException
     */
    public BLURFILTER readBLURFILTER() throws IOException {
        BLURFILTER ret = new BLURFILTER();
        ret.blurX = readFIXED();
        ret.blurY = readFIXED();
        ret.passes = (int) readUB(5);
        ret.reserved = (int) readUB(3);
        return ret;
    }

    /**
     * Reads one DROPSHADOWFILTER value from the stream
     *
     * @return DROPSHADOWFILTER value
     * @throws IOException
     */
    public DROPSHADOWFILTER readDROPSHADOWFILTER() throws IOException {
        DROPSHADOWFILTER ret = new DROPSHADOWFILTER();
        ret.dropShadowColor = readRGBA();
        ret.blurX = readFIXED();
        ret.blurY = readFIXED();
        ret.angle = readFIXED();
        ret.distance = readFIXED();
        ret.strength = readFIXED8();
        ret.innerShadow = readUB(1) == 1;
        ret.knockout = readUB(1) == 1;
        ret.compositeSource = readUB(1) == 1;
        ret.passes = (int) readUB(5);
        return ret;
    }

    /**
     * Reads one GLOWFILTER value from the stream
     *
     * @return GLOWFILTER value
     * @throws IOException
     */
    public GLOWFILTER readGLOWFILTER() throws IOException {
        GLOWFILTER ret = new GLOWFILTER();
        ret.glowColor = readRGBA();
        ret.blurX = readFIXED();
        ret.blurY = readFIXED();
        ret.strength = readFIXED8();
        ret.innerGlow = readUB(1) == 1;
        ret.knockout = readUB(1) == 1;
        ret.compositeSource = readUB(1) == 1;
        ret.passes = (int) readUB(5);
        return ret;
    }

    /**
     * Reads one BEVELFILTER value from the stream
     *
     * @return BEVELFILTER value
     * @throws IOException
     */
    public BEVELFILTER readBEVELFILTER() throws IOException {
        BEVELFILTER ret = new BEVELFILTER();
        ret.highlightColor = readRGBA(); //Highlight color first. It it opposite of the documentation
        ret.shadowColor = readRGBA();
        ret.blurX = readFIXED();
        ret.blurY = readFIXED();
        ret.angle = readFIXED();
        ret.distance = readFIXED();
        ret.strength = readFIXED8();
        ret.innerShadow = readUB(1) == 1;
        ret.knockout = readUB(1) == 1;
        ret.compositeSource = readUB(1) == 1;
        ret.onTop = readUB(1) == 1;
        ret.passes = (int) readUB(4);
        return ret;
    }

    /**
     * Reads one GRADIENTGLOWFILTER value from the stream
     *
     * @return GRADIENTGLOWFILTER value
     * @throws IOException
     */
    public GRADIENTGLOWFILTER readGRADIENTGLOWFILTER() throws IOException {
        GRADIENTGLOWFILTER ret = new GRADIENTGLOWFILTER();
        int numColors = readUI8();
        ret.gradientColors = new RGBA[numColors];
        ret.gradientRatio = new int[numColors];
        for (int i = 0; i < numColors; i++) {
            ret.gradientColors[i] = readRGBA();
        }
        for (int i = 0; i < numColors; i++) {
            ret.gradientRatio[i] = readUI8();
        }
        ret.blurX = readFIXED();
        ret.blurY = readFIXED();
        ret.angle = readFIXED();
        ret.distance = readFIXED();
        ret.strength = readFIXED8();
        ret.innerShadow = readUB(1) == 1;
        ret.knockout = readUB(1) == 1;
        ret.compositeSource = readUB(1) == 1;
        ret.onTop = readUB(1) == 1;
        ret.passes = (int) readUB(4);
        return ret;
    }

    /**
     * Reads one GRADIENTBEVELFILTER value from the stream
     *
     * @return GRADIENTBEVELFILTER value
     * @throws IOException
     */
    public GRADIENTBEVELFILTER readGRADIENTBEVELFILTER() throws IOException {
        GRADIENTBEVELFILTER ret = new GRADIENTBEVELFILTER();
        int numColors = readUI8();
        ret.gradientColors = new RGBA[numColors];
        ret.gradientRatio = new int[numColors];
        for (int i = 0; i < numColors; i++) {
            ret.gradientColors[i] = readRGBA();
        }
        for (int i = 0; i < numColors; i++) {
            ret.gradientRatio[i] = readUI8();
        }
        ret.blurX = readFIXED();
        ret.blurY = readFIXED();
        ret.angle = readFIXED();
        ret.distance = readFIXED();
        ret.strength = readFIXED8();
        ret.innerShadow = readUB(1) == 1;
        ret.knockout = readUB(1) == 1;
        ret.compositeSource = readUB(1) == 1;
        ret.onTop = readUB(1) == 1;
        ret.passes = (int) readUB(4);
        return ret;
    }

    /**
     * Reads list of FILTER values from the stream
     *
     * @return List of FILTER values
     * @throws IOException
     */
    public List<FILTER> readFILTERLIST() throws IOException {
        List<FILTER> ret = new ArrayList<>();
        int numberOfFilters = readUI8();
        for (int i = 0; i < numberOfFilters; i++) {
            ret.add(readFILTER());
        }
        return ret;
    }

    /**
     * Reads one FILTER value from the stream
     *
     * @return FILTER value
     * @throws IOException
     */
    public FILTER readFILTER() throws IOException {
        int filterId = readUI8();
        switch (filterId) {
            case 0:
                return readDROPSHADOWFILTER();
            case 1:
                return readBLURFILTER();
            case 2:
                return readGLOWFILTER();
            case 3:
                return readBEVELFILTER();
            case 4:
                return readGRADIENTGLOWFILTER();
            case 5:
                return readCONVOLUTIONFILTER();
            case 6:
                return readCOLORMATRIXFILTER();
            case 7:
                return readGRADIENTBEVELFILTER();
            default:
                return null;
        }
    }

    /**
     * Reads list of BUTTONRECORD values from the stream
     *
     * @param inDefineButton2 Whether read from inside of DefineButton2Tag or
     * not
     * @return List of BUTTONRECORD values
     * @throws IOException
     */
    public List<BUTTONRECORD> readBUTTONRECORDList(boolean inDefineButton2) throws IOException {
        List<BUTTONRECORD> ret = new ArrayList<>();
        BUTTONRECORD br;
        while ((br = readBUTTONRECORD(inDefineButton2)) != null) {
            ret.add(br);
        }
        return ret;
    }

    /**
     * Reads one BUTTONRECORD value from the stream
     *
     * @param inDefineButton2 True when in DefineButton2
     * @return BUTTONRECORD value
     * @throws IOException
     */
    public BUTTONRECORD readBUTTONRECORD(boolean inDefineButton2) throws IOException {
        BUTTONRECORD ret = new BUTTONRECORD();
        ret.reserved = (int) readUB(2);
        ret.buttonHasBlendMode = readUB(1) == 1;
        ret.buttonHasFilterList = readUB(1) == 1;
        ret.buttonStateHitTest = readUB(1) == 1;
        ret.buttonStateDown = readUB(1) == 1;
        ret.buttonStateOver = readUB(1) == 1;
        ret.buttonStateUp = readUB(1) == 1;

        if (!ret.buttonHasBlendMode && !ret.buttonHasFilterList
                && !ret.buttonStateHitTest && !ret.buttonStateDown
                && !ret.buttonStateOver && !ret.buttonStateUp && ret.reserved == 0) {
            return null;
        }

        ret.characterId = readUI16();
        ret.placeDepth = readUI16();
        ret.placeMatrix = readMatrix();
        if (inDefineButton2) {
            ret.colorTransform = readCXFORMWITHALPHA();
            if (ret.buttonHasFilterList) {
                ret.filterList = readFILTERLIST();
            }
            if (ret.buttonHasBlendMode) {
                ret.blendMode = readUI8();
            }
        }
        return ret;
    }

    /**
     * Reads list of BUTTONCONDACTION values from the stream
     *
     * @param swf
     * @param tag
     * @return List of BUTTONCONDACTION values
     * @throws IOException
     */
    public List<BUTTONCONDACTION> readBUTTONCONDACTIONList(SWF swf, Tag tag) throws IOException {
        List<BUTTONCONDACTION> ret = new ArrayList<>();
        BUTTONCONDACTION bc;
        while (!(bc = readBUTTONCONDACTION(swf, tag)).isLast) {
            ret.add(bc);
        }
        ret.add(bc);
        return ret;
    }

    /**
     * Reads one BUTTONCONDACTION value from the stream
     *
     * @param swf
     * @param tag
     * @return BUTTONCONDACTION value
     * @throws IOException
     */
    public BUTTONCONDACTION readBUTTONCONDACTION(SWF swf, Tag tag) throws IOException {
        BUTTONCONDACTION ret = new BUTTONCONDACTION(swf, this, getPos(), tag);
        //ret.actions = readActionList();
        return ret;
    }

    /**
     * Reads one GRADRECORD value from the stream
     *
     * @param shapeNum 1 in DefineShape, 2 in DefineShape2...
     * @return GRADRECORD value
     * @throws IOException
     */
    public GRADRECORD readGRADRECORD(int shapeNum) throws IOException {
        GRADRECORD ret = new GRADRECORD();
        ret.ratio = readUI8();
        if (shapeNum >= 3) {
            ret.color = readRGBA();
        } else {
            ret.color = readRGB();
        }
        return ret;
    }

    /**
     * Reads one GRADIENT value from the stream
     *
     * @param shapeNum 1 in DefineShape, 2 in DefineShape2...
     * @return GRADIENT value
     * @throws IOException
     */
    public GRADIENT readGRADIENT(int shapeNum) throws IOException {
        GRADIENT ret = new GRADIENT();
        ret.spreadMode = (int) readUB(2);
        ret.interpolationMode = (int) readUB(2);
        int numGradients = (int) readUB(4);
        ret.gradientRecords = new GRADRECORD[numGradients];
        for (int i = 0; i < numGradients; i++) {
            ret.gradientRecords[i] = readGRADRECORD(shapeNum);

        }
        return ret;
    }

    /**
     * Reads one FOCALGRADIENT value from the stream
     *
     * @param shapeNum 1 in DefineShape, 2 in DefineShape2...
     * @return FOCALGRADIENT value
     * @throws IOException
     */
    public FOCALGRADIENT readFOCALGRADIENT(int shapeNum) throws IOException {
        FOCALGRADIENT ret = new FOCALGRADIENT();
        ret.spreadMode = (int) readUB(2);
        ret.interpolationMode = (int) readUB(2);
        int numGradients = (int) readUB(4);
        ret.gradientRecords = new GRADRECORD[numGradients];
        for (int i = 0; i < numGradients; i++) {
            ret.gradientRecords[i] = readGRADRECORD(shapeNum);
        }
        ret.focalPoint = readFIXED8();
        return ret;
    }

    /**
     * Reads one FILLSTYLE value from the stream
     *
     * @param shapeNum 1 in DefineShape, 2 in DefineShape2...
     * @return FILLSTYLE value
     * @throws IOException
     */
    public FILLSTYLE readFILLSTYLE(int shapeNum) throws IOException {
        FILLSTYLE ret = new FILLSTYLE();
        ret.fillStyleType = readUI8();
        if (ret.fillStyleType == FILLSTYLE.SOLID) {
            if (shapeNum >= 3) {
                ret.color = readRGBA();
            } else {
                ret.color = readRGB();
            }
        }
        if ((ret.fillStyleType == FILLSTYLE.LINEAR_GRADIENT)
                || (ret.fillStyleType == FILLSTYLE.RADIAL_GRADIENT)
                || (ret.fillStyleType == FILLSTYLE.FOCAL_RADIAL_GRADIENT)) {
            ret.gradientMatrix = readMatrix();
        }
        if ((ret.fillStyleType == FILLSTYLE.LINEAR_GRADIENT)
                || (ret.fillStyleType == FILLSTYLE.RADIAL_GRADIENT)) {
            ret.gradient = readGRADIENT(shapeNum);
        }
        if (ret.fillStyleType == FILLSTYLE.FOCAL_RADIAL_GRADIENT) {
            ret.gradient = readFOCALGRADIENT(shapeNum);
        }

        if ((ret.fillStyleType == FILLSTYLE.REPEATING_BITMAP)
                || (ret.fillStyleType == FILLSTYLE.CLIPPED_BITMAP)
                || (ret.fillStyleType == FILLSTYLE.NON_SMOOTHED_REPEATING_BITMAP)
                || (ret.fillStyleType == FILLSTYLE.NON_SMOOTHED_CLIPPED_BITMAP)) {
            ret.bitmapId = readUI16();
            ret.bitmapMatrix = readMatrix();
        }
        return ret;
    }

    /**
     * Reads one FILLSTYLEARRAY value from the stream
     *
     * @param shapeNum 1 in DefineShape, 2 in DefineShape2...
     * @return FILLSTYLEARRAY value
     * @throws IOException
     */
    public FILLSTYLEARRAY readFILLSTYLEARRAY(int shapeNum) throws IOException {

        FILLSTYLEARRAY ret = new FILLSTYLEARRAY();
        int fillStyleCount = readUI8();
        if (((shapeNum == 2) || (shapeNum == 3) || (shapeNum == 4/*?*/)) && (fillStyleCount == 0xff)) {
            fillStyleCount = readUI16();
        }
        ret.fillStyles = new FILLSTYLE[fillStyleCount];
        for (int i = 0; i < fillStyleCount; i++) {
            ret.fillStyles[i] = readFILLSTYLE(shapeNum);
        }
        return ret;
    }

    /**
     * Reads one LINESTYLE value from the stream
     *
     * @param shapeNum 1 in DefineShape, 2 in DefineShape2...
     * @return LINESTYLE value
     * @throws IOException
     */
    public LINESTYLE readLINESTYLE(int shapeNum) throws IOException {
        LINESTYLE ret = new LINESTYLE();
        ret.width = readUI16();
        if ((shapeNum == 1) || (shapeNum == 2)) {
            ret.color = readRGB();
        }
        if (shapeNum == 3) {
            ret.color = readRGBA();
        }
        return ret;
    }

    /**
     * Reads one LINESTYLE2 value from the stream
     *
     * @param shapeNum 1 in DefineShape, 2 in DefineShape2...
     * @return LINESTYLE2 value
     * @throws IOException
     */
    public LINESTYLE2 readLINESTYLE2(int shapeNum) throws IOException {
        LINESTYLE2 ret = new LINESTYLE2();
        ret.width = readUI16();
        ret.startCapStyle = (int) readUB(2);
        ret.joinStyle = (int) readUB(2);
        ret.hasFillFlag = (int) readUB(1) == 1;
        ret.noHScaleFlag = (int) readUB(1) == 1;
        ret.noVScaleFlag = (int) readUB(1) == 1;
        ret.pixelHintingFlag = (int) readUB(1) == 1;
        ret.reserved = (int) readUB(5);
        ret.noClose = (int) readUB(1) == 1;
        ret.endCapStyle = (int) readUB(2);
        if (ret.joinStyle == LINESTYLE2.MITER_JOIN) {
            ret.miterLimitFactor = readUI16();
        }
        if (!ret.hasFillFlag) {
            ret.color = readRGBA();
        } else {
            ret.fillType = readFILLSTYLE(shapeNum);
        }
        return ret;
    }

    /**
     * Reads one LINESTYLEARRAY value from the stream
     *
     * @param shapeNum 1 in DefineShape, 2 in DefineShape2...
     * @return LINESTYLEARRAY value
     * @throws IOException
     */
    public LINESTYLEARRAY readLINESTYLEARRAY(int shapeNum) throws IOException {
        LINESTYLEARRAY ret = new LINESTYLEARRAY();
        int lineStyleCount = readUI8();
        if (lineStyleCount == 0xff) {
            lineStyleCount = readUI16();
        }
        if ((shapeNum == 1 || shapeNum == 2 || shapeNum == 3)) {
            ret.lineStyles = new LINESTYLE[lineStyleCount];
            for (int i = 0; i < lineStyleCount; i++) {
                ret.lineStyles[i] = readLINESTYLE(shapeNum);
            }
        } else if (shapeNum == 4) {
            ret.lineStyles = new LINESTYLE2[lineStyleCount];
            for (int i = 0; i < lineStyleCount; i++) {
                ret.lineStyles[i] = readLINESTYLE2(shapeNum);
            }
        }
        return ret;
    }

    /**
     * Reads one SHAPERECORD value from the stream
     *
     * @param fillBits
     * @param lineBits
     * @param shapeNum 1 in DefineShape, 2 in DefineShape2...
     * @return SHAPERECORD value
     * @throws IOException
     */
    private SHAPERECORD readSHAPERECORD(int fillBits, int lineBits, int shapeNum, boolean morphShape) throws IOException {
        SHAPERECORD ret;
        int typeFlag = (int) readUB(1);
        if (typeFlag == 0) {
            boolean stateNewStyles = readUB(1) == 1;
            boolean stateLineStyle = readUB(1) == 1;
            boolean stateFillStyle1 = readUB(1) == 1;
            boolean stateFillStyle0 = readUB(1) == 1;
            boolean stateMoveTo = readUB(1) == 1;
            if ((!stateNewStyles) && (!stateLineStyle) && (!stateFillStyle1) && (!stateFillStyle0) && (!stateMoveTo)) {
                ret = new EndShapeRecord();
            } else {
                StyleChangeRecord scr = new StyleChangeRecord();
                scr.stateNewStyles = stateNewStyles;
                scr.stateLineStyle = stateLineStyle;
                scr.stateFillStyle0 = stateFillStyle0;
                scr.stateFillStyle1 = stateFillStyle1;
                scr.stateMoveTo = stateMoveTo;
                if (stateMoveTo) {
                    scr.moveBits = (int) readUB(5);
                    scr.moveDeltaX = (int) readSB(scr.moveBits);
                    scr.moveDeltaY = (int) readSB(scr.moveBits);
                }
                if (stateFillStyle0) {
                    scr.fillStyle0 = (int) readUB(fillBits);
                }
                if (stateFillStyle1) {
                    scr.fillStyle1 = (int) readUB(fillBits);
                }
                if (stateLineStyle) {
                    scr.lineStyle = (int) readUB(lineBits);
                }
                if (stateNewStyles) {
                    if (morphShape) {
                        //This should never happen
                    } else {
                        scr.fillStyles = readFILLSTYLEARRAY(shapeNum);
                        scr.lineStyles = readLINESTYLEARRAY(shapeNum);
                    }
                    scr.numFillBits = (int) readUB(4);
                    scr.numLineBits = (int) readUB(4);
                }
                ret = scr;
            }
        } else {//typeFlag==1
            int straightFlag = (int) readUB(1);
            if (straightFlag == 1) {
                StraightEdgeRecord ser = new StraightEdgeRecord();
                ser.numBits = (int) readUB(4);
                ser.generalLineFlag = readUB(1) == 1;
                if (!ser.generalLineFlag) {
                    ser.vertLineFlag = readUB(1) == 1;
                }
                if (ser.generalLineFlag || (!ser.vertLineFlag)) {
                    ser.deltaX = (int) readSB(ser.numBits + 2);
                }
                if (ser.generalLineFlag || (ser.vertLineFlag)) {
                    ser.deltaY = (int) readSB(ser.numBits + 2);
                }
                ret = ser;
            } else {
                CurvedEdgeRecord cer = new CurvedEdgeRecord();
                cer.numBits = (int) readUB(4);
                cer.controlDeltaX = (int) readSB(cer.numBits + 2);
                cer.controlDeltaY = (int) readSB(cer.numBits + 2);
                cer.anchorDeltaX = (int) readSB(cer.numBits + 2);
                cer.anchorDeltaY = (int) readSB(cer.numBits + 2);
                ret = cer;
            }
        }
        return ret;
    }

    /**
     * Reads one SHAPE value from the stream
     *
     * @param shapeNum 1 in DefineShape, 2 in DefineShape2...
     * @param morphShape
     * @return SHAPE value
     * @throws IOException
     */
    public SHAPE readSHAPE(int shapeNum, boolean morphShape) throws IOException {
        SHAPE ret = new SHAPE();
        ret.numFillBits = (int) readUB(4);
        ret.numLineBits = (int) readUB(4);
        ret.shapeRecords = readSHAPERECORDS(shapeNum, ret.numFillBits, ret.numLineBits, morphShape);
        return ret;
    }

    /**
     * Reads one SHAPEWITHSTYLE value from the stream
     *
     * @param shapeNum 1 in DefineShape, 2 in DefineShape2...
     * @param morphShape
     * @return SHAPEWITHSTYLE value
     * @throws IOException
     */
    public SHAPEWITHSTYLE readSHAPEWITHSTYLE(int shapeNum, boolean morphShape) throws IOException {
        SHAPEWITHSTYLE ret = new SHAPEWITHSTYLE();
        ret.fillStyles = readFILLSTYLEARRAY(shapeNum);
        ret.lineStyles = readLINESTYLEARRAY(shapeNum);
        ret.numFillBits = (int) readUB(4);
        ret.numLineBits = (int) readUB(4);
        ret.shapeRecords = readSHAPERECORDS(shapeNum, ret.numFillBits, ret.numLineBits, morphShape);
        return ret;
    }

    /**
     * Reads list of SHAPERECORDs from the stream
     *
     * @param shapeNum 1 in DefineShape, 2 in DefineShape2...
     * @param fillBits
     * @param lineBits
     * @return SHAPERECORDs array
     * @throws IOException
     */
    private List<SHAPERECORD> readSHAPERECORDS(int shapeNum, int fillBits, int lineBits, boolean morphShape) throws IOException {
        List<SHAPERECORD> ret = new ArrayList<>();
        SHAPERECORD rec;
        do {
            rec = readSHAPERECORD(fillBits, lineBits, shapeNum, morphShape);
            if (rec instanceof StyleChangeRecord) {
                StyleChangeRecord scRec = (StyleChangeRecord) rec;
                if (scRec.stateNewStyles) {
                    fillBits = scRec.numFillBits;
                    lineBits = scRec.numLineBits;
                }
            }
            ret.add(rec);
        } while (!(rec instanceof EndShapeRecord));
        alignByte();
        return ret;
    }

    /**
     * Reads one SOUNDINFO value from the stream
     *
     * @return SOUNDINFO value
     * @throws IOException
     */
    public SOUNDINFO readSOUNDINFO() throws IOException {
        SOUNDINFO ret = new SOUNDINFO();
        ret.reserved = (int) readUB(2);
        ret.syncStop = readUB(1) == 1;
        ret.syncNoMultiple = readUB(1) == 1;
        ret.hasEnvelope = readUB(1) == 1;
        ret.hasLoops = readUB(1) == 1;
        ret.hasOutPoint = readUB(1) == 1;
        ret.hasInPoint = readUB(1) == 1;
        if (ret.hasInPoint) {
            ret.inPoint = readUI32();
        }
        if (ret.hasOutPoint) {
            ret.outPoint = readUI32();
        }
        if (ret.hasLoops) {
            ret.loopCount = readUI16();
        }
        if (ret.hasEnvelope) {
            int envPoints = readUI8();
            ret.envelopeRecords = new SOUNDENVELOPE[envPoints];
            for (int i = 0; i < envPoints; i++) {
                ret.envelopeRecords[i] = readSOUNDENVELOPE();
            }
        }
        return ret;
    }

    /**
     * Reads one SOUNDENVELOPE value from the stream
     *
     * @return SOUNDENVELOPE value
     * @throws IOException
     */
    public SOUNDENVELOPE readSOUNDENVELOPE() throws IOException {
        SOUNDENVELOPE ret = new SOUNDENVELOPE();
        ret.pos44 = readUI32();
        ret.leftLevel = readUI16();
        ret.rightLevel = readUI16();
        return ret;
    }

    /**
     * Reads one GLYPHENTRY value from the stream
     *
     * @param glyphBits
     * @param advanceBits
     * @return GLYPHENTRY value
     * @throws IOException
     */
    public GLYPHENTRY readGLYPHENTRY(int glyphBits, int advanceBits) throws IOException {
        GLYPHENTRY ret = new GLYPHENTRY();
        ret.glyphIndex = (int) readUB(glyphBits);
        ret.glyphAdvance = (int) readUB(advanceBits);
        return ret;
    }

    /**
     * Reads one TEXTRECORD value from the stream
     *
     * @param inDefineText2
     * @param glyphBits
     * @param advanceBits
     * @return TEXTRECORD value
     * @throws IOException
     */
    public TEXTRECORD readTEXTRECORD(boolean inDefineText2, int glyphBits, int advanceBits) throws IOException {
        TEXTRECORD ret = new TEXTRECORD();
        int first = (int) readUB(1); //always 1
        readUB(3); //always 0
        ret.styleFlagsHasFont = readUB(1) == 1;
        ret.styleFlagsHasColor = readUB(1) == 1;
        ret.styleFlagsHasYOffset = readUB(1) == 1;
        ret.styleFlagsHasXOffset = readUB(1) == 1;
        if ((!ret.styleFlagsHasFont) && (!ret.styleFlagsHasColor) && (!ret.styleFlagsHasYOffset) && (!ret.styleFlagsHasXOffset) && (first == 0)) { //final text record
            return null;
        }
        if (ret.styleFlagsHasFont) {
            ret.fontId = readUI16();
        }
        if (ret.styleFlagsHasColor) {
            if (inDefineText2) {
                ret.textColorA = readRGBA();
            } else {
                ret.textColor = readRGB();
            }
        }
        if (ret.styleFlagsHasXOffset) {
            ret.xOffset = readSI16();
        }
        if (ret.styleFlagsHasYOffset) {
            ret.yOffset = readSI16();
        }
        if (ret.styleFlagsHasFont) {
            ret.textHeight = readUI16();
        }
        int glyphCount = readUI8();
        ret.glyphEntries = new GLYPHENTRY[glyphCount];
        for (int i = 0; i < glyphCount; i++) {
            ret.glyphEntries[i] = readGLYPHENTRY(glyphBits, advanceBits);
        }
        alignByte();
        return ret;
    }

    /**
     * Reads one MORPHGRADRECORD value from the stream
     *
     * @return MORPHGRADRECORD value
     * @throws IOException
     */
    public MORPHGRADRECORD readMORPHGRADRECORD() throws IOException {
        MORPHGRADRECORD ret = new MORPHGRADRECORD();
        ret.startRatio = readUI8();
        ret.startColor = readRGBA();
        ret.endRatio = readUI8();
        ret.endColor = readRGBA();
        return ret;
    }

    /**
     * Reads one MORPHGRADIENT value from the stream
     *
     * @return MORPHGRADIENT value
     * @throws IOException
     */
    public MORPHGRADIENT readMORPHGRADIENT() throws IOException {
        MORPHGRADIENT ret = new MORPHGRADIENT();
        //Despite of documentation (UI8 1-8), there are two fields 
        // spreadMode and interPolationMode which are same as in GRADIENT
        ret.spreadMode = (int) readUB(2);
        ret.interPolationMode = (int) readUB(2);
        int numGradients = (int) readUB(4);
        ret.gradientRecords = new MORPHGRADRECORD[numGradients];
        for (int i = 0; i < numGradients; i++) {
            ret.gradientRecords[i] = readMORPHGRADRECORD();
        }
        return ret;
    }

    /**
     * Reads one MORPHFOCALGRADIENT value from the stream
     *
     * This is undocumented feature
     *
     * @return MORPHGRADIENT value
     * @throws IOException
     */
    public MORPHFOCALGRADIENT readMORPHFOCALGRADIENT() throws IOException {
        MORPHFOCALGRADIENT ret = new MORPHFOCALGRADIENT();
        ret.spreadMode = (int) readUB(2);
        ret.interPolationMode = (int) readUB(2);
        int numGradients = (int) readUB(4);
        ret.gradientRecords = new MORPHGRADRECORD[numGradients];
        for (int i = 0; i < numGradients; i++) {
            ret.gradientRecords[i] = readMORPHGRADRECORD();
        }
        ret.startFocalPoint = readFIXED8();
        ret.endFocalPoint = readFIXED8();
        return ret;
    }

    /**
     * Reads one MORPHFILLSTYLE value from the stream
     *
     * @return MORPHFILLSTYLE value
     * @throws IOException
     */
    public MORPHFILLSTYLE readMORPHFILLSTYLE() throws IOException {
        MORPHFILLSTYLE ret = new MORPHFILLSTYLE();
        ret.fillStyleType = readUI8();
        if (ret.fillStyleType == MORPHFILLSTYLE.SOLID) {
            ret.startColor = readRGBA();
            ret.endColor = readRGBA();
        }
        if ((ret.fillStyleType == MORPHFILLSTYLE.LINEAR_GRADIENT)
                || (ret.fillStyleType == MORPHFILLSTYLE.RADIAL_GRADIENT)
                || (ret.fillStyleType == MORPHFILLSTYLE.FOCAL_RADIAL_GRADIENT)) {
            ret.startGradientMatrix = readMatrix();
            ret.endGradientMatrix = readMatrix();
        }
        if ((ret.fillStyleType == MORPHFILLSTYLE.LINEAR_GRADIENT)
                || (ret.fillStyleType == MORPHFILLSTYLE.RADIAL_GRADIENT)) {
            ret.gradient = readMORPHGRADIENT();
        }
        if (ret.fillStyleType == MORPHFILLSTYLE.FOCAL_RADIAL_GRADIENT) {
            ret.gradient = readMORPHFOCALGRADIENT();
        }

        if ((ret.fillStyleType == MORPHFILLSTYLE.REPEATING_BITMAP)
                || (ret.fillStyleType == MORPHFILLSTYLE.CLIPPED_BITMAP)
                || (ret.fillStyleType == MORPHFILLSTYLE.NON_SMOOTHED_REPEATING_BITMAP)
                || (ret.fillStyleType == MORPHFILLSTYLE.NON_SMOOTHED_CLIPPED_BITMAP)) {
            ret.bitmapId = readUI16();
            ret.startBitmapMatrix = readMatrix();
            ret.endBitmapMatrix = readMatrix();
        }
        return ret;
    }

    /**
     * Reads one MORPHFILLSTYLEARRAY value from the stream
     *
     * @return MORPHFILLSTYLEARRAY value
     * @throws IOException
     */
    public MORPHFILLSTYLEARRAY readMORPHFILLSTYLEARRAY() throws IOException {

        MORPHFILLSTYLEARRAY ret = new MORPHFILLSTYLEARRAY();
        int fillStyleCount = readUI8();
        if (fillStyleCount == 0xff) {
            fillStyleCount = readUI16();
        }
        ret.fillStyles = new MORPHFILLSTYLE[fillStyleCount];
        for (int i = 0; i < fillStyleCount; i++) {
            ret.fillStyles[i] = readMORPHFILLSTYLE();
        }
        return ret;
    }

    /**
     * Reads one MORPHLINESTYLE value from the stream
     *
     * @return MORPHLINESTYLE value
     * @throws IOException
     */
    public MORPHLINESTYLE readMORPHLINESTYLE() throws IOException {
        MORPHLINESTYLE ret = new MORPHLINESTYLE();
        ret.startWidth = readUI16();
        ret.endWidth = readUI16();
        ret.startColor = readRGBA();
        ret.endColor = readRGBA();
        return ret;
    }

    /**
     * Reads one MORPHLINESTYLE2 value from the stream
     *
     * @return MORPHLINESTYLE2 value
     * @throws IOException
     */
    public MORPHLINESTYLE2 readMORPHLINESTYLE2() throws IOException {
        MORPHLINESTYLE2 ret = new MORPHLINESTYLE2();
        ret.startWidth = readUI16();
        ret.endWidth = readUI16();
        ret.startCapStyle = (int) readUB(2);
        ret.joinStyle = (int) readUB(2);
        ret.hasFillFlag = (int) readUB(1) == 1;
        ret.noHScaleFlag = (int) readUB(1) == 1;
        ret.noVScaleFlag = (int) readUB(1) == 1;
        ret.pixelHintingFlag = (int) readUB(1) == 1;
        ret.reserved = (int) readUB(5);
        ret.noClose = (int) readUB(1) == 1;
        ret.endCapStyle = (int) readUB(2);
        if (ret.joinStyle == LINESTYLE2.MITER_JOIN) {
            ret.miterLimitFactor = readUI16();
        }
        if (!ret.hasFillFlag) {
            ret.startColor = readRGBA();
            ret.endColor = readRGBA();
        } else {
            ret.fillType = readMORPHFILLSTYLE();
        }
        return ret;
    }

    /**
     * Reads one MORPHLINESTYLEARRAY value from the stream
     *
     * @param morphShapeNum 1 on DefineMorphShape, 2 on DefineMorphShape2
     * @return MORPHLINESTYLEARRAY value
     * @throws IOException
     */
    public MORPHLINESTYLEARRAY readMORPHLINESTYLEARRAY(int morphShapeNum) throws IOException {
        MORPHLINESTYLEARRAY ret = new MORPHLINESTYLEARRAY();
        int lineStyleCount = readUI8();
        if (lineStyleCount == 0xff) {
            lineStyleCount = readUI16();
        }
        if (morphShapeNum == 1) {
            ret.lineStyles = new MORPHLINESTYLE[lineStyleCount];
            for (int i = 0; i < lineStyleCount; i++) {
                ret.lineStyles[i] = readMORPHLINESTYLE();
            }
        } else if (morphShapeNum == 2) {
            ret.lineStyles2 = new MORPHLINESTYLE2[lineStyleCount];
            for (int i = 0; i < lineStyleCount; i++) {
                ret.lineStyles2[i] = readMORPHLINESTYLE2();
            }
        }
        return ret;
    }

    /**
     * Reads one KERNINGRECORD value from the stream
     *
     * @param fontFlagsWideCodes
     * @return KERNINGRECORD value
     * @throws IOException
     */
    public KERNINGRECORD readKERNINGRECORD(boolean fontFlagsWideCodes) throws IOException {
        KERNINGRECORD ret = new KERNINGRECORD();
        if (fontFlagsWideCodes) {
            ret.fontKerningCode1 = readUI16();
            ret.fontKerningCode2 = readUI16();
        } else {
            ret.fontKerningCode1 = readUI8();
            ret.fontKerningCode2 = readUI8();
        }
        ret.fontKerningAdjustment = readSI16();
        return ret;
    }

    /**
     * Reads one LANGCODE value from the stream
     *
     * @return LANGCODE value
     * @throws IOException
     */
    public LANGCODE readLANGCODE() throws IOException {
        LANGCODE ret = new LANGCODE();
        ret.languageCode = readUI8();
        return ret;
    }

    /**
     * Reads one ZONERECORD value from the stream
     *
     * @return ZONERECORD value
     * @throws IOException
     */
    public ZONERECORD readZONERECORD() throws IOException {
        ZONERECORD ret = new ZONERECORD();
        int numZoneData = readUI8();
        ret.zonedata = new ZONEDATA[numZoneData];
        for (int i = 0; i < numZoneData; i++) {
            ret.zonedata[i] = readZONEDATA();
        }
        readUB(6);
        ret.zoneMaskY = readUB(1) == 1;
        ret.zoneMaskX = readUB(1) == 1;
        return ret;
    }

    /**
     * Reads one ZONEDATA value from the stream
     *
     * @return ZONEDATA value
     * @throws IOException
     */
    public ZONEDATA readZONEDATA() throws IOException {
        ZONEDATA ret = new ZONEDATA();
        ret.alignmentCoordinate = readUI16();
        ret.range = readUI16();
        return ret;
    }

    /**
     * Reads one PIX15 value from the stream
     *
     * @return PIX15 value
     * @throws IOException
     */
    public PIX15 readPIX15() throws IOException {
        PIX15 ret = new PIX15();
        readUB(1);
        ret.red = (int) readUB(5);
        ret.green = (int) readUB(5);
        ret.blue = (int) readUB(5);
        return ret;
    }

    /**
     * Reads one PIX24 value from the stream
     *
     * @return PIX24 value
     * @throws IOException
     */
    public PIX24 readPIX24() throws IOException {
        PIX24 ret = new PIX24();
        ret.reserved = readUI8();
        ret.red = readUI8();
        ret.green = readUI8();
        ret.blue = readUI8();
        return ret;
    }

    /**
     * Reads one COLORMAPDATA value from the stream
     *
     * @param colorTableSize
     * @param bitmapWidth
     * @param bitmapHeight
     * @return COLORMAPDATA value
     * @throws IOException
     */
    public COLORMAPDATA readCOLORMAPDATA(int colorTableSize, int bitmapWidth, int bitmapHeight) throws IOException {
        COLORMAPDATA ret = new COLORMAPDATA();
        ret.colorTableRGB = new RGB[colorTableSize + 1];
        for (int i = 0; i < colorTableSize + 1; i++) {
            ret.colorTableRGB[i] = readRGB();
        }
        int dataLen = 0;
        for (int y = 0; y < bitmapHeight; y++) {
            int x = 0;
            for (; x < bitmapWidth; x++) {
                dataLen++;
            }
            while ((x % 4) != 0) {
                dataLen++;
                x++;
            }
        }
        ret.colorMapPixelData = readBytesEx(dataLen);
        return ret;
    }

    /**
     * Reads one BITMAPDATA value from the stream
     *
     * @param bitmapFormat
     * @param bitmapWidth
     * @param bitmapHeight
     * @return COLORMAPDATA value
     * @throws IOException
     */
    public BITMAPDATA readBITMAPDATA(int bitmapFormat, int bitmapWidth, int bitmapHeight) throws IOException {
        BITMAPDATA ret = new BITMAPDATA();
        List<PIX15> pix15 = new ArrayList<>();
        List<PIX24> pix24 = new ArrayList<>();
        int dataLen = 0;
        for (int y = 0; y < bitmapHeight; y++) {
            int x = 0;
            for (; x < bitmapWidth; x++) {
                if (bitmapFormat == DefineBitsLosslessTag.FORMAT_15BIT_RGB) {
                    dataLen += 2;
                    pix15.add(readPIX15());
                }
                if (bitmapFormat == DefineBitsLosslessTag.FORMAT_24BIT_RGB) {
                    dataLen += 4;
                    pix24.add(readPIX24());
                }
            }
            while ((dataLen % 4) != 0) {
                dataLen++;
                readUI8();
            }
        }
        if (bitmapFormat == DefineBitsLosslessTag.FORMAT_15BIT_RGB) {
            ret.bitmapPixelDataPix15 = pix15.toArray(new PIX15[pix15.size()]);
        } else if (bitmapFormat == DefineBitsLosslessTag.FORMAT_24BIT_RGB) {
            ret.bitmapPixelDataPix24 = pix24.toArray(new PIX24[pix24.size()]);
        }
        return ret;
    }

    /**
     * Reads one BITMAPDATA value from the stream
     *
     * @param bitmapFormat
     * @param bitmapWidth
     * @param bitmapHeight
     * @return COLORMAPDATA value
     * @throws IOException
     */
    public ALPHABITMAPDATA readALPHABITMAPDATA(int bitmapFormat, int bitmapWidth, int bitmapHeight) throws IOException {
        ALPHABITMAPDATA ret = new ALPHABITMAPDATA();
        ret.bitmapPixelData = new ARGB[bitmapWidth * bitmapHeight];
        for (int y = 0; y < bitmapHeight; y++) {
            for (int x = 0; x < bitmapWidth; x++) {
                ret.bitmapPixelData[y * bitmapWidth + x] = readARGB();
            }
        }
        return ret;
    }

    /**
     * Reads one ALPHACOLORMAPDATA value from the stream
     *
     * @param colorTableSize
     * @param bitmapWidth
     * @param bitmapHeight
     * @return ALPHACOLORMAPDATA value
     * @throws IOException
     */
    public ALPHACOLORMAPDATA readALPHACOLORMAPDATA(int colorTableSize, int bitmapWidth, int bitmapHeight) throws IOException {
        ALPHACOLORMAPDATA ret = new ALPHACOLORMAPDATA();
        ret.colorTableRGB = new RGBA[colorTableSize + 1];
        for (int i = 0; i < colorTableSize + 1; i++) {
            ret.colorTableRGB[i] = readRGBA();
        }
        int dataLen = 0;
        for (int y = 0; y < bitmapHeight; y++) {
            int x = 0;
            for (; x < bitmapWidth; x++) {
                dataLen++;
            }
            while ((x % 4) != 0) {
                dataLen++;
                x++;
            }
        }
        ret.colorMapPixelData = readBytesEx(dataLen);
        return ret;
    }

    @Override
    public int available() throws IOException {
        return is.available();
    }

    public long availableBits() throws IOException {
        if (bitPos > 0) {
            return available() * 8 + (8 - bitPos);
        }
        return available() * 8;
    }
}
