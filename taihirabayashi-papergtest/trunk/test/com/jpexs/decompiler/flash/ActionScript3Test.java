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
import com.jpexs.decompiler.flash.configuration.Configuration;
import com.jpexs.decompiler.flash.exporters.modes.ScriptExportMode;
import com.jpexs.decompiler.flash.gui.Main;
import com.jpexs.decompiler.flash.helpers.CodeFormatting;
import com.jpexs.decompiler.flash.helpers.HilightedTextWriter;
import com.jpexs.decompiler.flash.helpers.NulWriter;
import com.jpexs.decompiler.flash.tags.DoABCDefineTag;
import com.jpexs.decompiler.flash.tags.Tag;
import com.jpexs.decompiler.graph.GraphTargetItem;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 *
 * @author JPEXS
 */
public class ActionScript3Test {

    private SWF swf;
    private int clsIndex;
    private ABC abc;

    @BeforeClass
    public void init() throws IOException, InterruptedException {
        Main.initLogging(false);
        swf = new SWF(new BufferedInputStream(new FileInputStream("testdata/as3/as3.swf")), false);
        DoABCDefineTag tag = null;
        for (Tag t : swf.tags) {
            if (t instanceof DoABCDefineTag) {
                tag = (DoABCDefineTag) t;
                break;
            }
        }
        assertNotNull(tag);
        clsIndex = tag.getABC().findClassByName("classes.Test");
        assertTrue(clsIndex > -1);
        this.abc = tag.getABC();
        Configuration.autoDeobfuscate.set(false);
        Configuration.decompile.set(true);
        Configuration.registerNameFormat.set("_loc%d_");
    }

    private void decompileMethod(String methodName, String expectedResult, boolean isStatic) {
        int bodyIndex = abc.findMethodBodyByName(clsIndex, methodName);
        assertTrue(bodyIndex > -1);
        HilightedTextWriter writer = null;
        try {
            abc.bodies.get(bodyIndex).convert(methodName, ScriptExportMode.AS, isStatic, -1/*FIX?*/, clsIndex, abc, null, abc.constants, abc.method_info, new Stack<GraphTargetItem>(), false, new NulWriter(), new ArrayList<String>(), abc.instance_info.get(clsIndex).instance_traits, true);
            writer = new HilightedTextWriter(new CodeFormatting(), false);
            abc.bodies.get(bodyIndex).toString(methodName, ScriptExportMode.AS, isStatic, -1/*FIX?*/, clsIndex, abc, null, abc.constants, abc.method_info, new Stack<GraphTargetItem>(), false, writer, new ArrayList<String>(), abc.instance_info.get(clsIndex).instance_traits);
        } catch (InterruptedException ex) {
            fail();
        }
        String actualResult = writer.toString().replaceAll("[ \r\n]", "");
        expectedResult = expectedResult.replaceAll("[ \r\n]", "");
        assertEquals(actualResult, expectedResult);
    }

    @Test
    public void testHello() {
        decompileMethod("testHello", "trace(\"hello\");\r\n", false);
    }

    @Test
    public void testIncDec() {
        decompileMethod("testIncDec", "var a:* = 5;\r\n"
                + "var b:* = 0;\r\n"
                + "trace(\"++var\");\r\n"
                + "b = ++a;\r\n"
                + "trace(\"var++\");\r\n"
                + "b = a++;\r\n"
                + "trace(\"--var\");\r\n"
                + "b = --a;\r\n"
                + "trace(\"var--\");\r\n"
                + "b = a--;\r\n"
                + "var c:* = [1,2,3,4,5];\r\n"
                + "trace(\"++arr\");\r\n"
                + "b = ++c[2];\r\n"
                + "trace(\"arr++\");\r\n"
                + "b = c[2]++;\r\n"
                + "trace(\"--arr\");\r\n"
                + "b = --c[2];\r\n"
                + "trace(\"arr--\");\r\n"
                + "b = c[2]--;\r\n"
                + "var d:* = new TestClass1();\r\n"
                + "trace(\"++property\");\r\n"
                + "trace(++d.attrib);\r\n"
                + "trace(\"property++\");\r\n"
                + "trace(d.attrib++);\r\n"
                + "trace(\"--property\");\r\n"
                + "trace(--d.attrib);\r\n"
                + "trace(\"property--\");\r\n"
                + "trace(d.attrib--);\r\n"
                + "trace(\"arr[e++]\");\r\n"
                + "var chars:Array = new Array(36);\r\n"
                + "var index:uint = 0;\r\n"
                + "chars[index++] = 5;\r\n"
                + "trace(\"arr[++e]\");\r\n"
                + "chars[++index] = 5;\r\n", false);
    }

    @Test
    public void testDoWhile() {
        decompileMethod("testDoWhile", "var a:* = 8;\r\n"
                + "do\r\n"
                + "{\r\n"
                + "trace(\"a=\" + a);\r\n"
                + "a++;\r\n"
                + "}\r\n"
                + "while(a < 20);\r\n"
                + "\r\n", false);
    }

    @Test
    public void testInnerTry() {
        decompileMethod("testInnerTry", "try\r\n"
                + "{\r\n"
                + "try\r\n"
                + "{\r\n"
                + "trace(\"try body 1\");\r\n"
                + "}\r\n"
                + "catch(e:DefinitionError)\r\n"
                + "{\r\n"
                + "trace(\"catched DefinitionError\");\r\n"
                + "}\r\n"
                + "trace(\"after try 1\");\r\n"
                + "}\r\n"
                + "catch(e:Error)\r\n"
                + "{\r\n"
                + "trace(\"catched Error\");\r\n"
                + "}\r\n"
                + "finally\r\n"
                + "{\r\n"
                + "trace(\"finally block\");\r\n"
                + "}\r\n", false);
    }

    @Test
    public void testWhileContinue() {
        decompileMethod("testWhileContinue", "var a:* = 5;\r\n"
                + "while(true)\r\n"
                + "{\r\n"
                + "if(a == 9)\r\n"
                + "{\r\n"
                + "if(a == 8)\r\n"
                + "{\r\n"
                + "continue;\r\n"
                + "}\r\n"
                + "if(a == 9)\r\n"
                + "{\r\n"
                + "break;\r\n"
                + "}\r\n"
                + "trace(\"hello 1\");\r\n"
                + "}\r\n"
                + "trace(\"hello2\");\r\n"
                + "}\r\n", false);
    }

    @Test
    public void testPrecedence() {
        decompileMethod("testPrecedence", "var a:* = 0;\r\n"
                + "a = (5 + 6) * 7;\r\n"
                + "a = 5 * (2 + 3);\r\n"
                + "a = 5 + 6 * 7;\r\n"
                + "a = 5 * 2 + 2;\r\n"
                + "a = 5 * (25 % 3);\r\n"
                + "a = 5 % (24 * 307);\r\n"
                + "a = 1 / (2 / 3);\r\n"
                + "a = 1 / (2 * 3);\r\n"
                + "a = 1 * 2 * 3;\r\n"
                + "a = 1 * 2 / 3;\r\n"
                + "trace(\"a=\" + a);\r\n", false);
    }

    @Test
    public void testStrings() {
        decompileMethod("testStrings", "trace(\"hello\");\r\n"
                + "trace(\"quotes:\\\"hello!\\\"\");\r\n"
                + "trace(\"backslash: \\\\ \");\r\n"
                + "trace(\"single quotes: \\'hello!\\'\");\r\n"
                + "trace(\"new line \\r\\n hello!\");\r\n", false);
    }

    @Test
    public void testContinueLevels() {
        decompileMethod("testContinueLevels", "var b:* = undefined;\r\n"
                + "var c:* = undefined;\r\n"
                + "var d:* = undefined;\r\n"
                + "var e:* = undefined;\r\n"
                + "var a:* = 5;\r\n"
                + "loop3:\r\n"
                + "switch(a)\r\n"
                + "{\r\n"
                + "case 57 * a:\r\n"
                + "trace(\"fiftyseven multiply a\");\r\n"
                + "b = 0;\r\n"
                + "while(b < 50)\r\n"
                + "{\r\n"
                + "if(b == 10)\r\n"
                + "{\r\n"
                + "break;\r\n"
                + "}\r\n"
                + "if(b == 15)\r\n"
                + "{\r\n"
                + "break loop3;\r\n"
                + "}\r\n"
                + "b = b + 1;\r\n"
                + "}\r\n"
                + "break;\r\n"
                + "case 13:\r\n"
                + "trace(\"thirteen\");\r\n"
                + "case 14:\r\n"
                + "trace(\"fourteen\");\r\n"
                + "break;\r\n"
                + "case 89:\r\n"
                + "trace(\"eightynine\");\r\n"
                + "break;\r\n"
                + "default:\r\n"
                + "trace(\"default clause\");\r\n"
                + "}\r\n"
                + "c = 0;\r\n"
                + "loop1:\r\n"
                + "for(;c < 8;c = c + 1)\r\n"
                + "{\r\n"
                + "d = 0;\r\n"
                + "while(d < 25)\r\n"
                + "{\r\n"
                + "e = 0;\r\n"
                + "if(e < 50)\r\n"
                + "{\r\n"
                + "if(e == 9)\r\n"
                + "{\r\n"
                + "break;\r\n"
                + "}\r\n"
                + "if(e == 20)\r\n"
                + "{\r\n"
                + "continue loop1;\r\n"
                + "}\r\n"
                + "if(e != 8)\r\n"
                + "{\r\n"
                + "break loop1;\r\n"
                + "}\r\n"
                + "}\r\n"
                + "d++;\r\n"
                + "}\r\n"
                + "trace(\"hello\");\r\n"
                + "}\r\n", false);
    }

    @Test
    public void testSwitchDefault() {
        decompileMethod("testSwitchDefault", "var a:* = 5;\r\n"
                + "switch(a)\r\n"
                + "{\r\n"
                + "case 57 * a:\r\n"
                + "trace(\"fiftyseven multiply a\");\r\n"
                + "break;\r\n"
                + "case 13:\r\n"
                + "trace(\"thirteen\");\r\n"
                + "case 14:\r\n"
                + "trace(\"fourteen\");\r\n"
                + "break;\r\n"
                + "case 89:\r\n"
                + "trace(\"eightynine\");\r\n"
                + "break;\r\n"
                + "default:\r\n"
                + "trace(\"default clause\");\r\n"
                + "}\r\n", false);
    }

    @Test
    public void testMultipleCondition() {
        decompileMethod("testMultipleCondition", "var a:* = 5;\r\n"
                + "var b:* = 8;\r\n"
                + "var c:* = 9;\r\n"
                + "if((a <= 4 || b <= 8) && c == 7)\r\n"
                + "{\r\n"
                + "trace(\"onTrue\");\r\n"
                + "}\r\n"
                + "else\r\n"
                + "{\r\n"
                + "trace(\"onFalse\");\r\n"
                + "}\r\n", false);
    }

    @Test
    public void testForBreak() {
        decompileMethod("testForBreak", "var a:* = 0;\r\n"
                + "while(a < 10)\r\n"
                + "{\r\n"
                + "if(a == 5)\r\n"
                + "{\r\n"
                + "break;\r\n"
                + "}\r\n"
                + "trace(\"hello:\" + a);\r\n"
                + "a++;\r\n"
                + "}\r\n", false);
    }

    @Test
    public void testIf() {
        decompileMethod("testIf", "var a:* = 5;\r\n"
                + "if(a == 7)\r\n"
                + "{\r\n"
                + "trace(\"onTrue\");\r\n"
                + "}\r\n", false);
    }

    @Test
    public void testIfElse() {
        decompileMethod("testIfElse", "var a:* = 5;\r\n"
                + "if(a == 7)\r\n"
                + "{\r\n"
                + "trace(\"onTrue\");\r\n"
                + "}\r\n"
                + "else\r\n"
                + "{\r\n"
                + "trace(\"onFalse\");\r\n"
                + "}\r\n", false);
    }

    @Test
    public void testFor() {
        decompileMethod("testFor", "var a:* = 0;\r\n"
                + "while(a < 10)\r\n"
                + "{\r\n"
                + "trace(\"a=\" + a);\r\n"
                + "a++;\r\n"
                + "}\r\n", false);
    }

    @Test
    public void testForContinue() {
        decompileMethod("testForContinue", "var a:* = 0;\r\n"
                + "for(;a < 10;a = a + 1)\r\n"
                + "{\r\n"
                + "if(a == 9)\r\n"
                + "{\r\n"
                + "if(a == 5)\r\n"
                + "{\r\n"
                + "trace(\"part1\");\r\n"
                + "continue;\r\n"
                + "}\r\n"
                + "trace(\"a=\" + a);\r\n"
                + "if(a == 7)\r\n"
                + "{\r\n"
                + "trace(\"part2\");\r\n"
                + "continue;\r\n"
                + "}\r\n"
                + "trace(\"part3\");\r\n"
                + "}\r\n"
                + "else\r\n"
                + "{\r\n"
                + "trace(\"part4\");\r\n"
                + "}\r\n"
                + "trace(\"part5\");\r\n"
                + "}\r\n", false);
    }

    @Test
    public void testTry() {
        decompileMethod("testTry", "var i:int = 0;\r\n"
                + "i = 7;\r\n"
                + "try\r\n"
                + "{\r\n"
                + "trace(\"try body\");\r\n"
                + "}\r\n"
                + "catch(e:DefinitionError)\r\n"
                + "{\r\n"
                + "trace(\"catched DefinitionError\");\r\n"
                + "}\r\n"
                + "catch(e:Error)\r\n"
                + "{\r\n"
                + "trace(\"Error message:\" + e.message);\r\n"
                + "trace(\"Stacktrace:\" + e.getStackTrace());\r\n"
                + "}\r\n"
                + "finally\r\n"
                + "{\r\n"
                + "trace(\"Finally part\");\r\n"
                + "}\r\n"
                + "trace(\"end\");\r\n", false);
    }

    @Test
    public void testSwitch() {
        decompileMethod("testSwitch", "var a:* = 5;\r\n"
                + "switch(a)\r\n"
                + "{\r\n"
                + "case 57 * a:\r\n"
                + "trace(\"fiftyseven multiply a\");\r\n"
                + "break;\r\n"
                + "case 13:\r\n"
                + "trace(\"thirteen\");\r\n"
                + "case 14:\r\n"
                + "trace(\"fourteen\");\r\n"
                + "break;\r\n"
                + "case 89:\r\n"
                + "trace(\"eightynine\");\r\n"
                + "break;\r\n"
                + "}\r\n", false);
    }

    @Test
    public void testTernarOperator() {
        decompileMethod("testTernarOperator", "var a:* = 5;\r\n"
                + "var b:* = 4;\r\n"
                + "var c:* = 4;\r\n"
                + "var d:* = 78;\r\n"
                + "var e:* = a == b?c == d?1:7:3;\r\n"
                + "trace(\"e=\" + e);\r\n", false);
    }

    @Test
    public void testInnerIf() {
        decompileMethod("testInnerIf", "var a:* = 5;\r\n"
                + "var b:* = 4;\r\n"
                + "if(a == 5)\r\n"
                + "{\r\n"
                + "if(b == 6)\r\n"
                + "{\r\n"
                + "trace(\"b==6\");\r\n"
                + "}\r\n"
                + "else\r\n"
                + "{\r\n"
                + "trace(\"b!=6\");\r\n"
                + "}\r\n"
                + "}\r\n"
                + "else if(b == 7)\r\n"
                + "{\r\n"
                + "trace(\"b==7\");\r\n"
                + "}\r\n"
                + "else\r\n"
                + "{\r\n"
                + "trace(\"b!=7\");\r\n"
                + "}\r\n"
                + "\r\n"
                + "trace(\"end\");\r\n", false);
    }

    @Test
    public void testVector() {
        decompileMethod("testVector", "var v:Vector.<String> = new Vector.<String>();\r\n"
                + "v.push(\"hello\");\r\n"
                + "v[0] = \"hi\";\r\n"
                + "v[5 * 8 - 39] = \"hi2\";\r\n"
                + "trace(v[0]);\r\n", false);
    }

    @Test
    public void testProperty() {
        decompileMethod("testProperty", "var d:* = new TestClass1();\r\n"
                + "var k:* = 7 + 8;\r\n"
                + "if(k == 15)\r\n"
                + "{\r\n"
                + "d.method(d.attrib * 5);\r\n"
                + "}\r\n", false);
    }

    @Test
    public void testRest() {
        decompileMethod("testRest", "trace(\"firstRest:\" + restval[0]);\r\n"
                + "return firstp;\r\n", false);
    }

    @Test
    public void testParamNames() {
        decompileMethod("testParamNames", "return firstp + secondp + thirdp;\r\n", false);
    }

    @Test
    public void testForEach() {
        decompileMethod("testForEach", "var list:Array = null;\r\n"
                + "var item:* = undefined;\r\n"
                + "list = new Array();\r\n"
                + "list[0] = \"first\";\r\n"
                + "list[1] = \"second\";\r\n"
                + "list[2] = \"third\";\r\n"
                + "for each(item in list)\r\n"
                + "{\r\n"
                + "trace(\"item #\" + item);\r\n"
                + "}\r\n", false);
    }

    @Test
    public void testForEachObjectArray() {
        decompileMethod("testForEachObjectArray", "var list:Array = null;\r\n"
                + "var test:Array = null;\r\n"
                + "list = new Array();\r\n"
                + "list[0] = \"first\";\r\n"
                + "list[1] = \"second\";\r\n"
                + "list[2] = \"third\";\r\n"
                + "test = new Array();\r\n"
                + "test[0] = 0;\r\n"
                + "for each(test[0] in list)\r\n"
                + "{\r\n"
                + "trace(\"item #\" + test[0]);\r\n"
                + "}\r\n", false);
    }

    @Test
    public void testForEachObjectAttribute() {
        decompileMethod("testForEachObjectAttribute", "var list:Array = null;\r\n"
                + "list = new Array();\r\n"
                + "list[0] = \"first\";\r\n"
                + "list[1] = \"second\";\r\n"
                + "list[2] = \"third\";\r\n"
                + "for each(this.testPriv in list)\r\n"
                + "{\r\n"
                + "trace(\"item #\" + this.testPriv);\r\n"
                + "}\r\n", false);
    }

    @Test
    public void testParamsCount() {
        decompileMethod("testParamsCount", "return firstp;\r\n", false);
    }

    @Test
    public void testInlineFunctions() {
        decompileMethod("testInlineFunctions", "var first:String = null;\r\n"
                + "first = \"value1\";\r\n"
                + "var traceParameter:Function = function(aParam:String):String\r\n"
                + "{\r\n"
                + "var second:String = null;\r\n"
                + "second = \"value2\";\r\n"
                + "second = second + \"cc\";\r\n"
                + "var traceParam2:Function = function(bParam:String):String\r\n"
                + "{\r\n"
                + "trace(bParam + \",\" + aParam);\r\n"
                + "return first + second + aParam + bParam;\r\n"
                + "};\r\n"
                + "trace(second);\r\n"
                + "traceParam2(aParam);\r\n"
                + "return first;\r\n"
                + "};\r\n"
                + "traceParameter(\"hello\");\r\n", false);
    }

    @Test
    public void testMissingDefault() {
        decompileMethod("testMissingDefault", "var jj:* = 1;\r\n"
                + "switch(jj)\r\n"
                + "{\r\n"
                + "case 1:\r\n"
                + "jj = 1;\r\n"
                + "break;\r\n"
                + "case 2:\r\n"
                + "jj = 2;\r\n"
                + "break;\r\n"
                + "default:\r\n"
                + "jj = 3;\r\n"
                + "}\r\n", false);
    }

    @Test
    public void testChainedAssignments() {
        decompileMethod("testChainedAssignments", "var a:* = 0;\r\n"
                + "var b:* = 0;\r\n"
                + "var c:* = 0;\r\n"
                + "var d:* = 0;\r\n"
                + "d = c = b = a = 5;\r\n"
                + "var e:TestClass2 = TestClass2.createMe(\"test\");\r\n"
                + "e.attrib1 = e.attrib2 = e.attrib3 = this.getCounter();\r\n"
                + "this.traceIt(e.toString());\r\n", false);
    }

    @Test
    public void testFinallyZeroJump() {
        decompileMethod("testFinallyZeroJump", "var str:String = param1;\r\n"
                + "try\r\n"
                + "{\r\n"
                + "}\r\n"
                + "catch(e:Error)\r\n"
                + "{\r\n"
                + "trace(\"error is :\" + e.message);\r\n"
                + "}\r\n"
                + "finally\r\n"
                + "{\r\n"
                + "trace(\"hi \");\r\n"
                + "if(5 == 4)\r\n"
                + "{\r\n"
                + "return str;\r\n"
                + "}\r\n"
                + "return \"hu\" + str;\r\n"
                + "}\r\n", false);
    }

    @Test
    public void testInnerFunctions() {
        decompileMethod("testInnerFunctions", "var s:int = 0;\r\n"
                + "var innerFunc:Function = function(b:String):*\r\n"
                + "{\r\n"
                + "trace(b);\r\n"
                + "};\r\n"
                + "var k:int = 5;\r\n"
                + "if(k == 6)\r\n"
                + "{\r\n"
                + "s = 8;\r\n"
                + "}\r\n"
                + "innerFunc(a);\r\n", false);
    }

    @Test
    public void testDeclarations() {
        decompileMethod("testDeclarations", "var vall:* = undefined;\r\n"
                + "var vstr:String = null;\r\n"
                + "var vint:* = 0;\r\n"
                + "var vuint:uint = 0;\r\n"
                + "var vclass:TestClass1 = null;\r\n"
                + "var vnumber:* = NaN;\r\n"
                + "var vobject:Object = null;\r\n"
                + "vall = 6;\r\n"
                + "vstr = \"hello\";\r\n"
                + "vuint = 7;\r\n"
                + "vint = -4;\r\n"
                + "vclass = new TestClass1();\r\n"
                + "vnumber = 0.5;\r\n"
                + "vnumber = 6;\r\n"
                + "vobject = vclass;\r\n", false);
    }

    @Test
    public void testForIn() {
        decompileMethod("testForIn", "var dic:Dictionary = null;\r\n"
                + "var item:Object = null;\r\n"
                + "for(item in dic)\r\n"
                + "{\r\n"
                + "trace(item);\r\n"
                + "}\r\n"
                + "for each(item in dic)\r\n"
                + "{\r\n"
                + "trace(item);\r\n"
                + "}\r\n", false);
    }

    @Test
    public void testNames() {
        decompileMethod("testNames", "var ns:* = this.getNamespace();\r\n"
                + "var name:* = this.getName();\r\n"
                + "var a:* = ns::unnamespacedFunc();\r\n"
                + "var b:* = ns::[name];\r\n"
                + "trace(b.c);\r\n"
                + "var c:* = myInternal::neco;\r\n", false);
    }

    @Test
    public void testComplexExpressions() {
        decompileMethod("testComplexExpressions", "var i:* = 0;\r\n"
                + "var j:* = 0;\r\n"
                + "j = i = i + (i = i + i++);\r\n", false);
    }

    @Test
    public void testExpressions() {
        decompileMethod("testExpressions", "var arr:Array = null;\r\n"
                + "var i:* = 5;\r\n"
                + "var j:* = 5;\r\n"
                + "if((i = i = i / 2) == 1 || i == 2)\r\n"
                + "{\r\n"
                + "arguments.concat(i);\r\n"
                + "}\r\n"
                + "else if(i == 0)\r\n"
                + "{\r\n"
                + "i = j++;\r\n"
                + "}\r\n"
                + "else\r\n"
                + "{\r\n"
                + "arr[0]();\r\n"
                + "}\r\n"
                + "\r\n"
                + "return i == 0;\r\n", false);
    }

    @Test
    public void testArguments() {
        decompileMethod("testArguments", "return arguments[0];\r\n", false);
    }

    @Test
    public void testLogicalComputing() {
        decompileMethod("testLogicalComputing", "var b:* = false;\r\n"
                + "var i:* = 5;\r\n"
                + "var j:* = 7;\r\n"
                + "if(i > j)\r\n"
                + "{\r\n"
                + "j = 9;\r\n"
                + "b = true;\r\n"
                + "}\r\n"
                + "b = (i == 0 || i == 1) && j == 0;\r\n", false);
    }

    @Test
    public void testInc2() {
        decompileMethod("testInc2", "var a:* = [1];\r\n"
                + "a[this.getInt()]++;\r\n"
                + "var d:* = a[this.getInt()]++;\r\n"
                + "var e:* = ++a[this.getInt()];\r\n"
                + "var b:* = 1;\r\n"
                + "b++;\r\n"
                + "var c:* = 1;\r\n"
                + "b = c++;\r\n", false);
    }

    @Test
    public void testDecl2() {
        decompileMethod("testDecl2", "var k:* = 0;\r\n"
                + "var i:* = 5;\r\n"
                + "i = i + 7;\r\n"
                + "if(i == 5)\r\n"
                + "{\r\n"
                + "if(i < 8)\r\n"
                + "{\r\n"
                + "k = 6;\r\n"
                + "}\r\n"
                + "}\r\n"
                + "k = 7;\r\n", false);
    }

    @Test
    public void testChain2() {
        decompileMethod("testChain2", "var g:Array = null;\r\n"
                + "var h:* = false;\r\n"
                + "var extraLine:* = false;\r\n"
                + "var r:* = 7;\r\n"
                + "var t:* = 0;\r\n"
                + "t = this.getInt();\r\n"
                + "if(t + 1 < g.length)\r\n"
                + "{\r\n"
                + "t++;\r\n"
                + "h = true;\r\n"
                + "}\r\n"
                + "if(t >= 0)\r\n"
                + "{\r\n"
                + "trace(\"ch\");\r\n"
                + "}\r\n", false);
    }

    @Test
    public void testDoWhile2() {
        decompileMethod("testDoWhile2", "var k:* = 5;\r\n"
                + "do\r\n"
                + "{\r\n"
                + "k++;\r\n"
                + "if(k == 7)\r\n"
                + "{\r\n"
                + "k = 5 * k;\r\n"
                + "}\r\n"
                + "else\r\n"
                + "{\r\n"
                + "k = 5 - k;\r\n"
                + "}\r\n"
                + "k--;\r\n"
                + "}\r\n"
                + "while(k < 9);\r\n"
                + "\r\n"
                + "return 2;\r\n", false);
    }

    @Test
    public void testWhileAnd() {
        decompileMethod("testWhileAnd", "var a:* = 5;\r\n"
                + "var b:* = 10;\r\n"
                + "while(a < 10 && b > 1)\r\n"
                + "{\r\n"
                + "a++;\r\n"
                + "b--;\r\n"
                + "}\r\n"
                + "a = 7;\r\n"
                + "b = 9;\r\n", false);
    }

    @Test
    public void testNamedAnonFunctions() {
        decompileMethod("testNamedAnonFunctions", "var test:* = new function testFunc(param1:*, param2:int, param3:Array):Boolean\r\n"
                + "{\r\n"
                + "return (param1 as TestClass2).attrib1 == 5;\r\n"
                + "};\r\n", false);
    }

    @Test
    public void testStringConcat() {
        decompileMethod("testStringConcat", "var k:* = 8;\r\n"
                + "this.traceIt(\"hello\" + 5 * 6);\r\n"
                + "this.traceIt(\"hello\" + (k - 1));\r\n"
                + "this.traceIt(\"hello\" + 5 + 6);\r\n", false);
    }

    @Test
    public void testWhileTry() {
        decompileMethod("testWhileTry", "while(true)\r\n"
                + "{\r\n"
                + "try\r\n"
                + "{\r\n"
                + "while(true)\r\n"
                + "{\r\n"
                + "trace(\"a\");\r\n"
                + "}\r\n"
                + "}\r\n"
                + "catch(e:EOFError)\r\n"
                + "{\r\n"
                + "continue;\r\n"
                + "}\r\n"
                + "catch(e:Error)\r\n"
                + "{\r\n"
                + "continue;\r\n"
                + "}\r\n"
                + "}\r\n", false);
    }

    @Test
    public void testWhileTry2() {
        decompileMethod("testWhileTry2", "var j:* = undefined;\r\n"
                + "var i:* = 0;\r\n"
                + "for(;i < 100;i++)\r\n"
                + "{\r\n"
                + "try\r\n"
                + "{\r\n"
                + "j = 0;\r\n"
                + "while(j < 20)\r\n"
                + "{\r\n"
                + "trace(\"a\");\r\n"
                + "j++;\r\n"
                + "}\r\n"
                + "}\r\n"
                + "catch(e:EOFError)\r\n"
                + "{\r\n"
                + "continue;\r\n"
                + "}\r\n"
                + "catch(e:Error)\r\n"
                + "{\r\n"
                + "continue;\r\n"
                + "}\r\n"
                + "trace(\"after_try\");\r\n"
                + "}\r\n"
                + "trace(\"end\");\r\n", false);
    }

    @Test
    public void testTryReturn() {
        decompileMethod("testTryReturn", "var i:int = 0;\r\n"
                + "var b:Boolean = false;\r\n"
                + "try\r\n"
                + "{\r\n"
                + "i = 0;\r\n"
                + "b = true;\r\n"
                + "if(i > 0)\r\n"
                + "{\r\n"
                + "while(this.testDoWhile2())\r\n"
                + "{\r\n"
                + "if(b)\r\n"
                + "{\r\n"
                + "return 5;\r\n"
                + "}\r\n"
                + "}\r\n"
                + "}\r\n"
                + "i++;\r\n"
                + "return 2;\r\n"
                + "}\r\n"
                + "catch(e:Error)\r\n"
                + "{\r\n"
                + "}\r\n"
                + "return 4;\r\n", false);
    }

    @Test
    public void testVector2() {
        decompileMethod("testVector2", "var a:Vector.<Vector.<int>> = new Vector.<Vector.<int>>();\r\n"
                + "var b:Vector.<int> = new <int>[10,20,30];\r\n", false);
    }

    @Test
    public void testOptionalParameters() {
        String methodName = "testOptionalParameters";
        int methodInfo = abc.findMethodInfoByName(clsIndex, methodName);
        int bodyIndex = abc.findMethodBodyByName(clsIndex, methodName);
        assertTrue(methodInfo > -1);
        assertTrue(bodyIndex > -1);
        HilightedTextWriter writer = new HilightedTextWriter(new CodeFormatting(), false);
        abc.method_info.get(methodInfo).getParamStr(writer, abc.constants, abc.bodies.get(bodyIndex), abc, new ArrayList<String>());
        String actualResult = writer.toString().replaceAll("[ \r\n]", "");
        String expectedResult = "p1:Event=null,p2:Number=1,p3:Number=-1,p4:Number=-1.1,p5:Number=-1.1,p6:String=\"a\"";
        expectedResult = expectedResult.replaceAll("[ \r\n]", "");
        assertEquals(actualResult, expectedResult);
    }
}
