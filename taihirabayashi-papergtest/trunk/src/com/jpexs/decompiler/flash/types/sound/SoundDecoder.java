/*
 * Copyright (C) 2014 JPEXS
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.jpexs.decompiler.flash.types.sound;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 * @author JPEXS
 */
public abstract class SoundDecoder {

    public SoundFormat soundFormat;

    public SoundDecoder(SoundFormat soundFormat) {
        this.soundFormat = soundFormat;
    }

    public byte[] decode(byte[] data) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        decode(new ByteArrayInputStream(data), baos);
        return baos.toByteArray();
    }

    public abstract void decode(InputStream is, OutputStream os) throws IOException;
}
