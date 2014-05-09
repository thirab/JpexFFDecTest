/*
 * Copyright (C) 2014  * @author JPEXS

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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 * @author JPEXS
 */
public class NoDecoder extends SoundDecoder {

    public NoDecoder(SoundFormat soundFormat) {
        super(soundFormat);
    }

    @Override
    public void decode(InputStream is, OutputStream os) throws IOException {
        byte buf[] = new byte[1024];
        int cnt;
        while ((cnt = is.read(buf)) > 0) {
            os.write(buf, 0, cnt);
        }
    }

}
