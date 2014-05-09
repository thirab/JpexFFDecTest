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

import com.jpexs.decompiler.flash.abc.ABC;
import com.jpexs.decompiler.flash.abc.ClassPath;
import com.jpexs.decompiler.flash.abc.NotSameException;
import com.jpexs.decompiler.flash.abc.ScriptPack;
import com.jpexs.decompiler.flash.action.Action;
import com.jpexs.decompiler.flash.action.parser.ParseException;
import com.jpexs.decompiler.flash.action.parser.script.ActionScriptParser;
import com.jpexs.decompiler.flash.configuration.Configuration;
import com.jpexs.decompiler.flash.exporters.modes.ScriptExportMode;
import com.jpexs.decompiler.flash.gui.Main;
import com.jpexs.decompiler.flash.helpers.CodeFormatting;
import com.jpexs.decompiler.flash.helpers.HilightedTextWriter;
import com.jpexs.decompiler.flash.helpers.collections.MyEntry;
import com.jpexs.decompiler.flash.tags.ABCContainerTag;
import com.jpexs.decompiler.flash.tags.base.ASMSource;
import com.jpexs.decompiler.flash.tags.base.ContainerItem;
import com.jpexs.decompiler.flash.treeitems.TreeItem;
import com.jpexs.decompiler.flash.treenodes.TagNode;
import com.jpexs.decompiler.flash.treenodes.TreeNode;
import com.jpexs.decompiler.graph.CompilationException;
import com.jpexs.decompiler.graph.TranslateException;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import static org.testng.Assert.fail;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 *
 * @author JPEXS
 */
public class RecompileTest {

    @BeforeClass
    public void init() {
        Main.initLogging(false);
    }

    public static final String TESTDATADIR = "testdata/recompile";

    @Test(dataProvider = "provideFiles")
    public void testRecompile(String filename) {
        try {
            SWF swf = new SWF(new BufferedInputStream(new FileInputStream(TESTDATADIR + File.separator + filename)), false);
            Configuration.debugCopy.set(true);
            swf.saveTo(new ByteArrayOutputStream());
        } catch (IOException | InterruptedException ex) {
            fail();
        } catch (NotSameException ex) {
            fail("File is different after recompiling: " + filename);
        }
    }

    private void testAS2DirectEditingOneRecursive(List<TreeNode> nodeList) {
        for (TreeNode node : nodeList) {
            if (node.subNodes.isEmpty()) {
                TreeItem item = node.getItem();
                if ((item instanceof ASMSource) && (node.export)) {
                    try {
                        ASMSource asm = ((ASMSource) item);
                        HilightedTextWriter writer = new HilightedTextWriter(new CodeFormatting(), false);
                        Action.actionsToSource(asm, asm.getActions(), asm.toString()/*FIXME?*/, writer);
                        String as = writer.toString();
                        as = asm.removePrefixAndSuffix(as);
                        ActionScriptParser par = new ActionScriptParser();
                        try {
                            asm.setActions(par.actionsFromString(as));
                        } catch (ParseException | CompilationException ex) {
                            fail("Unable to parse: " + item.getSwf().getShortFileName() + "/" + item.toString());
                        }
                        writer = new HilightedTextWriter(new CodeFormatting(), false);
                        Action.actionsToSource(asm, asm.getActions(), asm.toString()/*FIXME?*/, writer);
                        String as2 = writer.toString();
                        as2 = asm.removePrefixAndSuffix(as2);
                        try {
                            asm.setActions(par.actionsFromString(as2));
                        } catch (ParseException | CompilationException ex) {
                            fail("Unable to parse: " + item.getSwf().getShortFileName() + "/" + item.toString());
                        }
                        writer = new HilightedTextWriter(new CodeFormatting(), false);
                        Action.actionsToSource(asm, asm.getActions(), asm.toString()/*FIXME?*/, writer);
                        String as3 = writer.toString();
                        as3 = asm.removePrefixAndSuffix(as3);
                        if (!as3.equals(as2)) {
                            fail("ActionScript is different: " + item.getSwf().getShortFileName() + "/" + item.toString());
                        }
                    } catch (InterruptedException | IOException | OutOfMemoryError | TranslateException | StackOverflowError ex) {
                    }
                }
            } else {
                testAS2DirectEditingOneRecursive(node.subNodes);
            }
        }
    }

    @Test(dataProvider = "provideFiles")
    public void testDirectEditing(String filename) throws IOException, InterruptedException, com.jpexs.decompiler.flash.abc.avm2.parser.ParseException, CompilationException {
        Configuration.autoDeobfuscate.set(false);
        try{SWF swf = new SWF(new BufferedInputStream(new FileInputStream(TESTDATADIR + File.separator + filename)), false);
        if (swf.fileAttributes.actionScript3) {
            boolean dotest=false;
            List<ABC> allAbcs = new ArrayList<>();
            for (ABCContainerTag ct : swf.abcList) {
                allAbcs.add(ct.getABC());
            }
            for (ABC abc : allAbcs) {
                for (int s = 0; s < abc.script_info.size(); s++) {
                    
                    String startAfter=null;
                    HilightedTextWriter htw = new HilightedTextWriter(new CodeFormatting(), false);
                    MyEntry<ClassPath, ScriptPack> en = abc.script_info.get(s).getPacks(abc, s).get(0);
                    if(startAfter==null || en.key.toString().equals(startAfter)){
                        dotest = true;
                    }
                    if(!dotest){
                        System.out.println("Skipped:"+en.key.toString());
                        continue;
                    }
                    
                    System.out.println("Recompiling:"+en.key.toString()+"...");
                    en.value.toSource(htw, swf.abcList, abc.script_info.get(s).traits.traits, ScriptExportMode.AS, false);
                    String original = htw.toString();
                    ABC nabc = new ABC(swf);
                    com.jpexs.decompiler.flash.abc.avm2.parser.script.ActionScriptParser.compile(original, nabc,allAbcs, false, en.key.className + ".as");                                                            
                }
            }
        } else {
            List<ContainerItem> list2 = new ArrayList<>();
            list2.addAll(swf.tags);
            List<TreeNode> list = SWF.createASTagList(list2, null);

            TagNode.setExport(list, true);
            testAS2DirectEditingOneRecursive(list);
        }
        }catch(Exception ex){
            System.out.println("FAIL");
            throw ex;
        }
    }

    @DataProvider(name = "provideFiles")
    public Object[][] provideFiles() {
        File dir = new File(TESTDATADIR);
        if (!dir.exists()) {
            return new Object[0][];
        }
        File[] files = dir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(".swf");
            }
        });
        Object[][] ret = new Object[files.length][1];
        for (int f = 0; f < files.length; f++) {
            ret[f][0] = files[f].getName();
        }
        return ret;
    }
}
