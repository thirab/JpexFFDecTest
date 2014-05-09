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
package com.jpexs.decompiler.flash;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author JPEXS
 */
public class SWC extends ZippedSWFBundle {

    public SWC(InputStream is) throws IOException {
        super(is);
        keySet.clear();
        this.is.reset();
        ZipInputStream zip = new ZipInputStream(this.is);
        ZipEntry entry;
        try {
            while ((entry = zip.getNextEntry()) != null) {
                if (entry.getName().equals("catalog.xml")) {
                    try {
                        SAXParserFactory factory = SAXParserFactory.newInstance();
                        SAXParser saxParser = factory.newSAXParser();
                        DefaultHandler handler = new DefaultHandler() {

                            @Override
                            public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
                                if (qName.equalsIgnoreCase("library")) {
                                    String path = attributes.getValue("path");
                                    if (path != null) {
                                        keySet.add(path);
                                    }
                                }
                            }

                        };
                        saxParser.parse(zip, handler);
                    } catch (Exception ex) {

                    }
                    return;
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(ZippedSWFBundle.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public String getExtension() {
        return "swc";
    }

}
