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
package com.jpexs.decompiler.flash.action.parser.pcode;

import com.jpexs.decompiler.flash.action.Action;
import com.jpexs.decompiler.flash.action.flashlite.ActionFSCommand2;
import com.jpexs.decompiler.flash.action.flashlite.ActionStrictMode;
import com.jpexs.decompiler.flash.action.parser.ParseException;
import com.jpexs.decompiler.flash.action.special.ActionDeobfuscatePop;
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
import com.jpexs.decompiler.graph.GraphSourceItemContainer;
import com.jpexs.helpers.Helper;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ASMParser {

    public static List<Action> parse(long containerSWFOffset, boolean ignoreNops, List<Label> labels, long address, FlasmLexer lexer, List<String> constantPool, int version) throws IOException, ParseException {
        List<Action> list = new ArrayList<>();
        Stack<GraphSourceItemContainer> containers = new Stack<>();

        ActionConstantPool cpool = new ActionConstantPool(constantPool);
        cpool.containerSWFOffset = containerSWFOffset;
        cpool.setAddress(address, version, false);
        address += cpool.getBytes(version).length;
        list.add(cpool);

        while (true) {
            ASMParsedSymbol symb = lexer.yylex();
            if (symb.type == ASMParsedSymbol.TYPE_LABEL) {
                labels.add(new Label((String) symb.value, address));
            } else if (symb.type == ASMParsedSymbol.TYPE_COMMENT) {
                if (!list.isEmpty()) {
                    String cmt = (String) symb.value;
                    if (cmt.equals("compileTimeJump")) {
                        Action a = list.get(list.size() - 1);
                        if (a instanceof ActionIf) {
                            ((ActionIf) a).ignoreUsed = false;
                        }
                    } else if (cmt.equals("compileTimeIgnore")) {
                        Action a = list.get(list.size() - 1);
                        if (a instanceof ActionIf) {
                            ((ActionIf) a).jumpUsed = false;
                        }
                    }
                }
            } else if (symb.type == ASMParsedSymbol.TYPE_BLOCK_END) {
                if (containers.isEmpty()) {
                    throw new ParseException("Block end without start", lexer.yyline());
                }
                GraphSourceItemContainer a = containers.peek();
                if (!a.parseDivision(address - ((Action) a).getAddress(), lexer)) {
                    containers.pop();
                }
            } else if (symb.type == ASMParsedSymbol.TYPE_INSTRUCTION_NAME) {
                String instructionName = ((String) symb.value).toLowerCase();
                Action a = parseAction(instructionName, lexer, constantPool, version);
                if (ignoreNops && a instanceof ActionNop) {
                    a = null;
                }
                if (a instanceof ActionConstantPool) {
                    a = null;
                }
                if (a instanceof ActionNop) {
                    a.containerSWFOffset = containerSWFOffset;
                    a.setAddress(address, version, false);
                    address += 1;
                } else if (a != null) {
                    a.containerSWFOffset = containerSWFOffset;
                    a.setAddress(address, version, false);
                    address += a.getBytes(version).length;
                }
                if (a instanceof GraphSourceItemContainer) {
                    containers.push((GraphSourceItemContainer) a);
                }
                if (a != null) {
                    list.add(a);
                }
            } else if (symb.type == ASMParsedSymbol.TYPE_EOL) {
            } else if ((symb.type == ASMParsedSymbol.TYPE_BLOCK_END) || (symb.type == ASMParsedSymbol.TYPE_EOF)) {
                return list;
            } else {
                throw new ParseException("Label or Instruction name expected, found:" + symb.type + " " + symb.value, lexer.yyline());
            }
        }
    }

    private static Action parseAction(String instructionName, FlasmLexer lexer, List<String> constantPool, int version) throws IOException, ParseException {
        Action a = null;
        if (instructionName.equals("GetURL".toLowerCase())) {
            a = new ActionGetURL(lexer);
        } else if (instructionName.equals("GoToLabel".toLowerCase())) {
            a = (new ActionGoToLabel(lexer));
        } else if (instructionName.equals("GotoFrame".toLowerCase())) {
            a = (new ActionGotoFrame(lexer));
        } else if (instructionName.equals("NextFrame".toLowerCase())) {
            a = (new ActionNextFrame());
        } else if (instructionName.equals("Play".toLowerCase())) {
            a = (new ActionPlay());
        } else if (instructionName.equals("PrevFrame".toLowerCase())) {
            a = (new ActionPrevFrame());
        } else if (instructionName.equals("SetTarget".toLowerCase())) {
            a = (new ActionSetTarget(lexer));
        } else if (instructionName.equals("Stop".toLowerCase())) {
            a = (new ActionStop());
        } else if (instructionName.equals("StopSounds".toLowerCase())) {
            a = (new ActionStopSounds());
        } else if (instructionName.equals("ToggleQuality".toLowerCase())) {
            a = (new ActionToggleQuality());
        } else if (instructionName.equals("WaitForFrame".toLowerCase())) {
            a = (new ActionWaitForFrame(lexer));
        } else if (instructionName.equals("Add".toLowerCase())) {
            a = (new ActionAdd());
        } else if (instructionName.equals("And".toLowerCase())) {
            a = (new ActionAnd());
        } else if (instructionName.equals("AsciiToChar".toLowerCase())) {
            a = (new ActionAsciiToChar());
        } else if (instructionName.equals("Call".toLowerCase())) {
            a = (new ActionCall());
        } else if (instructionName.equals("CharToAscii".toLowerCase())) {
            a = (new ActionCharToAscii());
        } else if (instructionName.equals("CloneSprite".toLowerCase())) {
            a = (new ActionCloneSprite());
        } else if (instructionName.equals("Divide".toLowerCase())) {
            a = (new ActionDivide());
        } else if (instructionName.equals("EndDrag".toLowerCase())) {
            a = (new ActionEndDrag());
        } else if (instructionName.equals("Equals".toLowerCase())) {
            a = (new ActionEquals());
        } else if (instructionName.equals("GetProperty".toLowerCase())) {
            a = (new ActionGetProperty());
        } else if (instructionName.equals("GetTime".toLowerCase())) {
            a = (new ActionGetTime());
        } else if (instructionName.equals("GetURL2".toLowerCase())) {
            a = (new ActionGetURL2(lexer));
        } else if (instructionName.equals("GetVariable".toLowerCase())) {
            a = (new ActionGetVariable());
        } else if (instructionName.equals("GotoFrame2".toLowerCase())) {
            a = (new ActionGotoFrame2(lexer));
        } else if (instructionName.equals("If".toLowerCase())) {
            a = (new ActionIf(lexer));
        } else if (instructionName.equals("Jump".toLowerCase())) {
            a = (new ActionJump(lexer));
        } else if (instructionName.equals("Less".toLowerCase())) {
            a = (new ActionLess());
        } else if (instructionName.equals("MBAsciiToChar".toLowerCase())) {
            a = (new ActionMBAsciiToChar());
        } else if (instructionName.equals("MBCharToAscii".toLowerCase())) {
            a = (new ActionMBCharToAscii());
        } else if (instructionName.equals("MBStringExtract".toLowerCase())) {
            a = (new ActionMBStringExtract());
        } else if (instructionName.equals("MBStringLength".toLowerCase())) {
            a = (new ActionMBStringLength());
        } else if (instructionName.equals("Multiply".toLowerCase())) {
            a = (new ActionMultiply());
        } else if (instructionName.equals("Not".toLowerCase())) {
            a = (new ActionNot());
        } else if (instructionName.equals("Or".toLowerCase())) {
            a = (new ActionOr());
        } else if (instructionName.equals("Pop".toLowerCase())) {
            a = (new ActionPop());
        } else if (instructionName.equals("Push".toLowerCase())) {
            a = (new ActionPush(lexer, constantPool));
        } else if (instructionName.equals("RandomNumber".toLowerCase())) {
            a = (new ActionRandomNumber());
        } else if (instructionName.equals("RemoveSprite".toLowerCase())) {
            a = (new ActionRemoveSprite());
        } else if (instructionName.equals("SetProperty".toLowerCase())) {
            a = (new ActionSetProperty());
        } else if (instructionName.equals("SetTarget2".toLowerCase())) {
            a = (new ActionSetTarget2());
        } else if (instructionName.equals("SetVariable".toLowerCase())) {
            a = (new ActionSetVariable());
        } else if (instructionName.equals("StartDrag".toLowerCase())) {
            a = (new ActionStartDrag());
        } else if (instructionName.equals("StringAdd".toLowerCase())) {
            a = (new ActionStringAdd());
        } else if (instructionName.equals("StringEquals".toLowerCase())) {
            a = (new ActionStringEquals());
        } else if (instructionName.equals("StringExtract".toLowerCase())) {
            a = (new ActionStringExtract());
        } else if (instructionName.equals("StringLength".toLowerCase())) {
            a = (new ActionStringLength());
        } else if (instructionName.equals("StringLess".toLowerCase())) {
            a = (new ActionStringLess());
        } else if (instructionName.equals("Subtract".toLowerCase())) {
            a = (new ActionSubtract());
        } else if (instructionName.equals("ToInteger".toLowerCase())) {
            a = (new ActionToInteger());
        } else if (instructionName.equals("Trace".toLowerCase())) {
            a = (new ActionTrace());
        } else if (instructionName.equals("WaitForFrame2".toLowerCase())) {
            a = (new ActionWaitForFrame2(lexer));
        } else if (instructionName.equals("Add2".toLowerCase())) {
            a = (new ActionAdd2());
        } else if (instructionName.equals("BitAnd".toLowerCase())) {
            a = (new ActionBitAnd());
        } else if (instructionName.equals("BitLShift".toLowerCase())) {
            a = (new ActionBitLShift());
        } else if (instructionName.equals("BitOr".toLowerCase())) {
            a = (new ActionBitOr());
        } else if (instructionName.equals("BitRShift".toLowerCase())) {
            a = (new ActionBitRShift());
        } else if (instructionName.equals("BitURShift".toLowerCase())) {
            a = (new ActionBitURShift());
        } else if (instructionName.equals("BitXor".toLowerCase())) {
            a = (new ActionBitXor());
        } else if (instructionName.equals("CallFunction".toLowerCase())) {
            a = (new ActionCallFunction());
        } else if (instructionName.equals("CallMethod".toLowerCase())) {
            a = (new ActionCallMethod());
        } else if (instructionName.equals("ConstantPool".toLowerCase())) {
            a = new ActionConstantPool(lexer);
        } else if (instructionName.equals("Decrement".toLowerCase())) {
            a = (new ActionDecrement());
        } else if (instructionName.equals("DefineFunction".toLowerCase())) {
            a = (new ActionDefineFunction(lexer));
        } else if (instructionName.equals("DefineLocal".toLowerCase())) {
            a = (new ActionDefineLocal());
        } else if (instructionName.equals("DefineLocal2".toLowerCase())) {
            a = (new ActionDefineLocal2());
        } else if (instructionName.equals("Delete".toLowerCase())) {
            a = (new ActionDelete());
        } else if (instructionName.equals("Delete2".toLowerCase())) {
            a = (new ActionDelete2());
        } else if (instructionName.equals("Enumerate".toLowerCase())) {
            a = (new ActionEnumerate());
        } else if (instructionName.equals("Equals2".toLowerCase())) {
            a = (new ActionEquals2());
        } else if (instructionName.equals("GetMember".toLowerCase())) {
            a = (new ActionGetMember());
        } else if (instructionName.equals("Increment".toLowerCase())) {
            a = (new ActionIncrement());
        } else if (instructionName.equals("InitArray".toLowerCase())) {
            a = (new ActionInitArray());
        } else if (instructionName.equals("InitObject".toLowerCase())) {
            a = (new ActionInitObject());
        } else if (instructionName.equals("Less2".toLowerCase())) {
            a = (new ActionLess2());
        } else if (instructionName.equals("Modulo".toLowerCase())) {
            a = (new ActionModulo());
        } else if (instructionName.equals("NewMethod".toLowerCase())) {
            a = (new ActionNewMethod());
        } else if (instructionName.equals("NewObject".toLowerCase())) {
            a = (new ActionNewObject());
        } else if (instructionName.equals("PushDuplicate".toLowerCase())) {
            a = (new ActionPushDuplicate());
        } else if (instructionName.equals("Return".toLowerCase())) {
            a = (new ActionReturn());
        } else if (instructionName.equals("SetMember".toLowerCase())) {
            a = (new ActionSetMember());
        } else if (instructionName.equals("StackSwap".toLowerCase())) {
            a = (new ActionStackSwap());
        } else if (instructionName.equals("StoreRegister".toLowerCase())) {
            a = (new ActionStoreRegister(lexer));
        } else if (instructionName.equals("TargetPath".toLowerCase())) {
            a = (new ActionTargetPath());
        } else if (instructionName.equals("ToNumber".toLowerCase())) {
            a = (new ActionToNumber());
        } else if (instructionName.equals("ToString".toLowerCase())) {
            a = (new ActionToString());
        } else if (instructionName.equals("TypeOf".toLowerCase())) {
            a = (new ActionTypeOf());
        } else if (instructionName.equals("With".toLowerCase())) {
            a = (new ActionWith(lexer));
        } else if (instructionName.equals("Enumerate2".toLowerCase())) {
            a = (new ActionEnumerate2());
        } else if (instructionName.equals("Greater".toLowerCase())) {
            a = (new ActionGreater());
        } else if (instructionName.equals("InstanceOf".toLowerCase())) {
            a = (new ActionInstanceOf());
        } else if (instructionName.equals("StrictEquals".toLowerCase())) {
            a = (new ActionStrictEquals());
        } else if (instructionName.equals("StringGreater".toLowerCase())) {
            a = (new ActionStringGreater());
        } else if (instructionName.equals("CastOp".toLowerCase())) {
            a = (new ActionCastOp());
        } else if (instructionName.equals("DefineFunction2".toLowerCase())) {
            a = (new ActionDefineFunction2(lexer));
        } else if (instructionName.equals("Extends".toLowerCase())) {
            a = (new ActionExtends());
        } else if (instructionName.equals("ImplementsOp".toLowerCase())) {
            a = (new ActionImplementsOp());
        } else if (instructionName.equals("Throw".toLowerCase())) {
            a = (new ActionThrow());
        } else if (instructionName.equals("Try".toLowerCase())) {
            a = (new ActionTry(lexer, version));
        } else if (instructionName.equals("FSCommand2".toLowerCase())) {
            a = (new ActionFSCommand2());
        } else if (instructionName.equals("StrictMode".toLowerCase())) {
            a = (new ActionStrictMode(lexer));
        } else if (instructionName.equals("Nop".toLowerCase())) {
            a = (new ActionNop());
        } else if (instructionName.equals("FFDec_DeobfuscatePop".toLowerCase())) {
            a = (new ActionDeobfuscatePop());
        } else {
            throw new ParseException("Unknown instruction name :" + instructionName, lexer.yyline());
        }
        return a;
    }

    private static List<Action> parseAllActions(FlasmLexer lexer, int version) throws IOException, ParseException {
        List<Action> list = new ArrayList<>();
        Stack<GraphSourceItemContainer> containers = new Stack<>();
        List<String> emptyList = new ArrayList<>();
        while (true) {
            ASMParsedSymbol symb = lexer.yylex();
            if (symb.type == ASMParsedSymbol.TYPE_BLOCK_END) {
                if (containers.isEmpty()) {
                    throw new ParseException("Block end without start", lexer.yyline());
                }
                GraphSourceItemContainer a = containers.peek();
                if (!a.parseDivision(0, lexer)) {
                    containers.pop();
                }
            } else if (symb.type == ASMParsedSymbol.TYPE_INSTRUCTION_NAME) {
                String instructionName = ((String) symb.value).toLowerCase();
                Action a = parseAction(instructionName, lexer, emptyList, version);
                if (a instanceof GraphSourceItemContainer) {
                    containers.push((GraphSourceItemContainer) a);
                }
                if (a != null) {
                    list.add(a);
                }
            } else if ((symb.type == ASMParsedSymbol.TYPE_BLOCK_END) || (symb.type == ASMParsedSymbol.TYPE_EOF)) {
                return list;
            }
        }
    }

    public static List<Action> parse(long address, long containerSWFOffset, boolean ignoreNops, String source, int version, boolean throwOnError) throws IOException, ParseException {
        FlasmLexer lexer = new FlasmLexer(new StringReader(source));
        List<Action> list = parseAllActions(lexer, version);

        List<String> constantPool = new ArrayList<>();
        for (Action a : list) {
            if (a instanceof ActionConstantPool) {
                constantPool.addAll(((ActionConstantPool) a).constantPool);
            }
        }

        lexer = new FlasmLexer(new StringReader(source));
        List<Label> labels = new ArrayList<>();
        List<Action> ret = parse(containerSWFOffset, ignoreNops, labels, address, lexer, constantPool, version);
        List<Action> links = Action.getActionsAllIfsOrJumps(ret);
        //Action.setActionsAddresses(ret, address, version);
        for (Action link : links) {
            boolean found = false;
            String identifier = null;
            if (link instanceof ActionJump) {
                identifier = ((ActionJump) link).identifier;

                for (Label label : labels) {

                    if (((ActionJump) link).identifier.equals(label.name)) {
                        ((ActionJump) link).setJumpOffset((int) (label.address - (((ActionJump) link).getAddress() + ((ActionJump) link).getBytes(version).length)));
                        found = true;
                        break;
                    }
                }
            }
            if (link instanceof ActionIf) {
                identifier = ((ActionIf) link).identifier;

                for (Label label : labels) {
                    if (((ActionIf) link).identifier.equals(label.name)) {
                        ((ActionIf) link).setJumpOffset((int) (label.address - (((ActionIf) link).getAddress() + ((ActionIf) link).getBytes(version).length)));
                        found = true;
                        break;
                    }
                }
            }
            if ((link instanceof ActionJump) || (link instanceof ActionIf)) {
                if (!found) {
                    if (throwOnError) {
                        throw new ParseException("TARGET NOT FOUND - identifier:" + identifier + " addr: ofs" + Helper.formatAddress(link.getAddress()), -1);
                    } else {
                        Logger.getLogger(ASMParser.class.getName()).log(Level.SEVERE, "TARGET NOT FOUND - identifier:" + identifier + " addr: ofs" + Helper.formatAddress(link.getAddress()));
                    }
                }
            }
        }
        return ret;
    }
}
