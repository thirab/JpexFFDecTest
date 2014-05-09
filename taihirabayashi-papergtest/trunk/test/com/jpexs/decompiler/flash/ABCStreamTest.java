/*
 * Copyright (C) 2010-2014 JPEXS
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
package com.jpexs.decompiler.flash;

import com.jpexs.decompiler.flash.abc.ABCInputStream;
import com.jpexs.decompiler.flash.abc.ABCOutputStream;
import com.jpexs.decompiler.flash.gui.Main;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 *
 * @author JPEXS
 */
public class ABCStreamTest {
    @BeforeClass
    public void init(){
        Main.initLogging(false);
    }

    @Test
    public void testU30() {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ABCOutputStream aos = new ABCOutputStream(baos);) {
            long number = 1531;
            aos.writeU30(number);
            aos.close();
            try (ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
                    ABCInputStream ais = new ABCInputStream(bais);) {
                assertEquals(number, ais.readU30());
                assertEquals(0, bais.available());
            }
        } catch (IOException ex) {
            fail();
        }
    }
}
