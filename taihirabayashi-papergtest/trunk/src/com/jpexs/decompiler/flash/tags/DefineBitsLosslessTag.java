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
import com.jpexs.decompiler.flash.tags.base.AloneTag;
import com.jpexs.decompiler.flash.tags.base.ImageTag;
import com.jpexs.decompiler.flash.types.BITMAPDATA;
import com.jpexs.decompiler.flash.types.BasicType;
import com.jpexs.decompiler.flash.types.COLORMAPDATA;
import com.jpexs.decompiler.flash.types.PIX24;
import com.jpexs.decompiler.flash.types.RGB;
import com.jpexs.decompiler.flash.types.annotations.Conditional;
import com.jpexs.decompiler.flash.types.annotations.Internal;
import com.jpexs.decompiler.flash.types.annotations.SWFType;
import com.jpexs.helpers.SerializableImage;
import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.InflaterInputStream;
import javax.imageio.ImageIO;

public class DefineBitsLosslessTag extends ImageTag implements AloneTag {

    @SWFType(BasicType.UI16)
    public int characterID;

    @SWFType(BasicType.UI8)
    public int bitmapFormat;

    @SWFType(BasicType.UI16)
    public int bitmapWidth;

    @SWFType(BasicType.UI16)
    public int bitmapHeight;

    @SWFType(BasicType.UI8)
    @Conditional(value = "bitmapFormat", options = {FORMAT_8BIT_COLORMAPPED})
    public int bitmapColorTableSize;

    public byte[] zlibBitmapData; //TODO: Parse COLORMAPDATA,BITMAPDATA
    public static final int FORMAT_8BIT_COLORMAPPED = 3;
    public static final int FORMAT_15BIT_RGB = 4;
    public static final int FORMAT_24BIT_RGB = 5;

    @Internal
    private COLORMAPDATA colorMapData;
    @Internal
    private BITMAPDATA bitmapData;
    @Internal
    private boolean decompressed = false;

    public static final int ID = 20;

    @Override
    public void setImage(byte[] data) throws IOException {
        SerializableImage image = new SerializableImage(ImageIO.read(new ByteArrayInputStream(data)));
        bitmapFormat = FORMAT_24BIT_RGB;
        bitmapWidth = image.getWidth();
        bitmapHeight = image.getHeight();
        bitmapData = new BITMAPDATA();
        bitmapData.bitmapPixelDataPix24 = new PIX24[bitmapWidth * bitmapHeight];
        int pos = 0;
        for (int y = 0; y < bitmapHeight; y++) {
            for (int x = 0; x < bitmapWidth; x++) {
                int argb = image.getRGB(x, y);
                //int a = (argb >> 24) & 0xff;
                int r = (argb >> 16) & 0xff;
                int g = (argb >> 8) & 0xff;
                int b = (argb) & 0xff;
                bitmapData.bitmapPixelDataPix24[pos] = new PIX24();
                bitmapData.bitmapPixelDataPix24[pos].red = r;
                bitmapData.bitmapPixelDataPix24[pos].green = g;
                bitmapData.bitmapPixelDataPix24[pos].blue = b;
                bitmapData.bitmapPixelDataPix24[pos].reserved = 0xff; //documentation says 0, but image is sometimes broken with 0, so there is 0xff, which works (maybe alpha?)
                pos++;
            }
        }
        ByteArrayOutputStream bitmapDataOS = new ByteArrayOutputStream();
        SWFOutputStream sos = new SWFOutputStream(bitmapDataOS, getVersion());
        sos.writeBITMAPDATA(bitmapData, bitmapFormat, bitmapWidth, bitmapHeight);
        ByteArrayOutputStream zlibOS = new ByteArrayOutputStream();
        SWFOutputStream sos2 = new SWFOutputStream(zlibOS, getVersion());
        sos2.writeBytesZlib(bitmapDataOS.toByteArray());
        zlibBitmapData = zlibOS.toByteArray();
        decompressed = false;
        setModified(true);
    }

    @Override
    public InputStream getImageData() {
        return null;
    }

    @Override
    public SerializableImage getImage() {
        SerializableImage bi = new SerializableImage(bitmapWidth, bitmapHeight, SerializableImage.TYPE_INT_RGB);
        COLORMAPDATA colorMapData = null;
        BITMAPDATA bitmapData = null;
        if (bitmapFormat == DefineBitsLosslessTag.FORMAT_8BIT_COLORMAPPED) {
            colorMapData = getColorMapData();
        }
        if ((bitmapFormat == DefineBitsLosslessTag.FORMAT_15BIT_RGB) || (bitmapFormat == DefineBitsLosslessTag.FORMAT_24BIT_RGB)) {
            bitmapData = getBitmapData();
        }
        int pos32aligned = 0;
        int pos = 0;
        for (int y = 0; y < bitmapHeight; y++) {
            for (int x = 0; x < bitmapWidth; x++) {
                Color c = null;
                if (bitmapFormat == DefineBitsLosslessTag.FORMAT_8BIT_COLORMAPPED) {
                    RGB color = colorMapData.colorTableRGB[colorMapData.colorMapPixelData[pos32aligned] & 0xff];
                    c = (new Color(color.red, color.green, color.blue));
                }
                if (bitmapFormat == DefineBitsLosslessTag.FORMAT_15BIT_RGB) {
                    c = (new Color(bitmapData.bitmapPixelDataPix15[pos].red * 8, bitmapData.bitmapPixelDataPix15[pos].green * 8, bitmapData.bitmapPixelDataPix15[pos].blue * 8));
                }
                if (bitmapFormat == DefineBitsLosslessTag.FORMAT_24BIT_RGB) {
                    c = (new Color(bitmapData.bitmapPixelDataPix24[pos].red, bitmapData.bitmapPixelDataPix24[pos].green, bitmapData.bitmapPixelDataPix24[pos].blue));
                }
                bi.setRGB(x, y, c.getRGB());
                pos32aligned++;
                pos++;
            }
            while ((pos32aligned % 4 != 0)) {
                pos32aligned++;
            }
        }
        return bi;
    }

    @Override
    public int getCharacterId() {
        return characterID;
    }

    public COLORMAPDATA getColorMapData() {
        if (!decompressed) {
            uncompressData();
        }
        return colorMapData;
    }

    public BITMAPDATA getBitmapData() {
        if (!decompressed) {
            uncompressData();
        }
        return bitmapData;
    }

    private void uncompressData() {
        try {
            SWFInputStream sis = new SWFInputStream(new InflaterInputStream(new ByteArrayInputStream(zlibBitmapData)), 10);
            if (bitmapFormat == FORMAT_8BIT_COLORMAPPED) {
                colorMapData = sis.readCOLORMAPDATA(bitmapColorTableSize, bitmapWidth, bitmapHeight);
            }
            if ((bitmapFormat == FORMAT_15BIT_RGB) || (bitmapFormat == FORMAT_24BIT_RGB)) {
                bitmapData = sis.readBITMAPDATA(bitmapFormat, bitmapWidth, bitmapHeight);
            }
        } catch (IOException ex) {
        }
        decompressed = true;
    }

    public DefineBitsLosslessTag(SWF swf, byte[] headerData, byte[] data, long pos) throws IOException {
        super(swf, ID, "DefineBitsLossless", headerData, data, pos);
        SWFInputStream sis = new SWFInputStream(new ByteArrayInputStream(data), swf.version);
        characterID = sis.readUI16();
        bitmapFormat = sis.readUI8();
        bitmapWidth = sis.readUI16();
        bitmapHeight = sis.readUI16();
        if (bitmapFormat == FORMAT_8BIT_COLORMAPPED) {
            bitmapColorTableSize = sis.readUI8();
        }
        zlibBitmapData = sis.readBytesEx(sis.available());
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
            sos.writeUI8(bitmapFormat);
            sos.writeUI16(bitmapWidth);
            sos.writeUI16(bitmapHeight);
            if (bitmapFormat == FORMAT_8BIT_COLORMAPPED) {
                sos.writeUI8(bitmapColorTableSize);
            }
            sos.write(zlibBitmapData);
        } catch (IOException e) {
        }
        return baos.toByteArray();
    }

    @Override
    public String getImageFormat() {
        return "png";
    }
}
