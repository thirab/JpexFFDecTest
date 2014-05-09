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
package com.jpexs.decompiler.flash.abc.avm2.parser.script;

/**
 *
 * @author JPEXS
 */
public enum SymbolType {
    //Keywords

    BREAK,
    CASE,
    CONTINUE,
    DEFAULT,
    DO,
    WHILE,
    ELSE,
    FOR,
    EACH,
    IN,
    IF,
    RETURN,
    SUPER,
    SWITCH,
    THROW,
    TRY,
    CATCH,
    FINALLY,
    WITH,
    DYNAMIC,
    INTERNAL,
    OVERRIDE,
    PRIVATE,
    PROTECTED,
    PUBLIC,
    STATIC,
    CLASS,
    CONST,
    EXTENDS,
    FUNCTION,
    GET,
    IMPLEMENTS,
    INTERFACE,
    NAMESPACE,
    PACKAGE,
    SET,
    VAR,
    IMPORT,
    USE,
    FALSE,
    NULL,
    THIS,
    TRUE,
    //Operators
    PARENT_OPEN,
    PARENT_CLOSE,
    CURLY_OPEN,
    CURLY_CLOSE,
    BRACKET_OPEN,
    BRACKET_CLOSE,
    SEMICOLON,
    COMMA,
    REST,
    DOT,
    ASSIGN,
    GREATER_THAN,
    LOWER_THAN,
    NOT,
    NEGATE,
    TERNAR,
    COLON,
    EQUALS,
    STRICT_EQUALS,
    LOWER_EQUAL,
    GREATER_EQUAL,
    NOT_EQUAL,
    STRICT_NOT_EQUAL,
    AND,
    OR,
    INCREMENT,
    DECREMENT,
    PLUS,
    MINUS,
    MULTIPLY,
    DIVIDE,
    BITAND,
    BITOR,
    XOR,
    MODULO,
    SHIFT_LEFT,
    SHIFT_RIGHT,
    USHIFT_RIGHT,
    ASSIGN_PLUS,
    ASSIGN_MINUS,
    ASSIGN_MULTIPLY,
    ASSIGN_DIVIDE,
    ASSIGN_BITAND,
    ASSIGN_BITOR,
    ASSIGN_XOR,
    ASSIGN_MODULO,
    ASSIGN_SHIFT_LEFT,
    ASSIGN_SHIFT_RIGHT,
    ASSIGN_USHIFT_RIGHT,
    AS,
    DELETE,
    INSTANCEOF,
    IS,
    NAMESPACE_OP,
    NEW,
    TYPEOF,
    VOID,
    ATTRIBUTE,
    //Other
    STRING,
    COMMENT,
    //XML,
    IDENTIFIER,
    INTEGER,
    DOUBLE,
    TYPENAME,
    EOF,
    //TRACE,
    //GETURL,
    //GOTOANDSTOP,
    //NEXTFRAME,
    //PLAY,
    //PREVFRAME,
    //TELLTARGET,
    //STOP,
    //STOPALLSOUNDS,
    //TOGGLEHIGHQUALITY,
    //ORD,
    //CHR,
    //DUPLICATEMOVIECLIP,
    //STOPDRAG,
    //GETTIMER,
    //LOADVARIABLES,
    //LOADMOVIE,
    //GOTOANDPLAY,
    //MBORD,
    //MBCHR,
    //MBLENGTH,
    //MBSUBSTRING,
    //RANDOM,
    //REMOVEMOVIECLIP,
    //STARTDRAG,
    //SUBSTR,
    //LENGTH, //string.length
    INT,
    //TARGETPATH,
    NUMBER_OP,
    STRING_OP,
    //IFFRAMELOADED,
    INFINITY,
    //EVAL,
    UNDEFINED,
    //NEWLINE,
    NAN,
    //GETVERSION,
    //CALL,
    //LOADMOVIENUM,
    //LOADVARIABLESNUM,
    //PRINT,
    //PRINTNUM,
    //PRINTASBITMAP,
    //PRINTASBITMAPNUM,
    //UNLOADMOVIE,
    //UNLOADMOVIENUM,
    FINAL,
    XML_STARTTAG_BEGIN, // <xxx
    XML_STARTVARTAG_BEGIN, // <{
    XML_STARTTAG_END, // >
    XML_FINISHVARTAG_BEGIN, // </{
    XML_FINISHTAG, //  </xxx>
    XML_STARTFINISHTAG_END, // /> 
    XML_COMMENT, // <!-- ... -->
    XML_CDATA, //<![CDATA[ ... ]]>
    XML_INSTR_BEGIN, // <?xxx
    XML_INSTR_END, // ?>
    XML_VAR_BEGIN, // {
    XML_ATTRIBUTENAME, // aaa=
    XML_ATTRIBUTEVALUE, // "vvv"
    XML_TEXT,
    XML_ATTRNAMEVAR_BEGIN, // {...}=
    XML_ATTRVALVAR_BEGIN, // aaa={
    XML_INSTRATTRNAMEVAR_BEGIN, // {...}=
    XML_INSTRATTRVALVAR_BEGIN, // aaa={
    XML_INSTRVARTAG_BEGIN, // <?{
    FILTER,
    DESCENDANTS
}
