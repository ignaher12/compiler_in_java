//### This file created by BYACC 1.8(/Java extension  1.15)
//### Java capabilities added 7 Jan 97, Bob Jamison
//### Updated : 27 Nov 97  -- Bob Jamison, Joe Nieten
//###           01 Jan 98  -- Bob Jamison -- fixed generic semantic constructor
//###           01 Jun 99  -- Bob Jamison -- added Runnable support
//###           06 Aug 00  -- Bob Jamison -- made state variables class-global
//###           03 Jan 01  -- Bob Jamison -- improved flags, tracing
//###           16 May 01  -- Bob Jamison -- added custom stack sizing
//###           04 Mar 02  -- Yuval Oren  -- improved java performance, added options
//###           14 Mar 02  -- Tomas Hurka -- -d support, static initializer workaround
//### Please send bug reports to tom@hukatronic.cz
//### static char yysccsid[] = "@(#)yaccpar	1.8 (Berkeley) 01/20/90";



package parser;



//#line 2 "gramatica.y"

import lexico.AnalizadorLexico;
import lexico.TablaDeSimbolos;
import lexico.TablaTipoToken;
import lexico.AccionesSemanticas.Accion;
import parser.Error.Tipo;
import utils.MatrizAccion;
import utils.MatrizTransicion;
import java.util.ArrayList;
import java.util.List;
import lexico.TablaDeSimbolos.Contexto;
import parser.Terceto;
import java.util.Stack;
//#line 31 "Parser.java"




public class Parser
{

boolean yydebug;        //do I want debug output?
int yynerrs;            //number of errors so far
int yyerrflag;          //was there an error?
int yychar;             //the current working character

//########## MESSAGES ##########
//###############################################################
// method: debug
//###############################################################
void debug(String msg)
{
  if (yydebug)
    System.out.println(msg);
}

//########## STATE STACK ##########
final static int YYSTACKSIZE = 500;  //maximum stack size
int statestk[] = new int[YYSTACKSIZE]; //state stack
int stateptr;
int stateptrmax;                     //highest index of stackptr
int statemax;                        //state when highest index reached
//###############################################################
// methods: state stack push,pop,drop,peek
//###############################################################
final void state_push(int state)
{
  try {
		stateptr++;
		statestk[stateptr]=state;
	 }
	 catch (ArrayIndexOutOfBoundsException e) {
     int oldsize = statestk.length;
     int newsize = oldsize * 2;
     int[] newstack = new int[newsize];
     System.arraycopy(statestk,0,newstack,0,oldsize);
     statestk = newstack;
     statestk[stateptr]=state;
  }
}
final int state_pop()
{
  return statestk[stateptr--];
}
final void state_drop(int cnt)
{
  stateptr -= cnt; 
}
final int state_peek(int relative)
{
  return statestk[stateptr-relative];
}
//###############################################################
// method: init_stacks : allocate and prepare stacks
//###############################################################
final boolean init_stacks()
{
  stateptr = -1;
  val_init();
  return true;
}
//###############################################################
// method: dump_stacks : show n levels of the stacks
//###############################################################
void dump_stacks(int count)
{
int i;
  System.out.println("=index==state====value=     s:"+stateptr+"  v:"+valptr);
  for (i=0;i<count;i++)
    System.out.println(" "+i+"    "+statestk[i]+"      "+valstk[i]);
  System.out.println("======================");
}


//########## SEMANTIC VALUES ##########
//public class ParserVal is defined in ParserVal.java


String   yytext;//user variable to return contextual strings
ParserVal yyval; //used to return semantic vals from action routines
ParserVal yylval;//the 'lval' (result) I got from yylex()
ParserVal valstk[];
int valptr;
//###############################################################
// methods: value stack push,pop,drop,peek.
//###############################################################
void val_init()
{
  valstk=new ParserVal[YYSTACKSIZE];
  yyval=new ParserVal();
  yylval=new ParserVal();
  valptr=-1;
}
void val_push(ParserVal val)
{
  if (valptr>=YYSTACKSIZE)
    return;
  valstk[++valptr]=val;
}
ParserVal val_pop()
{
  if (valptr<0)
    return new ParserVal();
  return valstk[valptr--];
}
void val_drop(int cnt)
{
int ptr;
  ptr=valptr-cnt;
  if (ptr<0)
    return;
  valptr = ptr;
}
ParserVal val_peek(int relative)
{
int ptr;
  ptr=valptr-relative;
  if (ptr<0)
    return new ParserVal();
  return valstk[ptr];
}
final ParserVal dup_yyval(ParserVal val)
{
  ParserVal dup = new ParserVal();
  dup.ival = val.ival;
  dup.dval = val.dval;
  dup.sval = val.sval;
  dup.obj = val.obj;
  return dup;
}
//#### end semantic value section ####
public final static short IDENTIFICADOR=257;
public final static short HEXADECIMAL=258;
public final static short CADENA_MULTI=259;
public final static short SIMASIGNACION=260;
public final static short DISTINTO=261;
public final static short IF=262;
public final static short THEN=263;
public final static short BEGIN=264;
public final static short END=265;
public final static short END_IF=266;
public final static short OUTF=267;
public final static short TYPEDEF=268;
public final static short FUN=269;
public final static short RET=270;
public final static short SINGLE=271;
public final static short MENOR_IGUAL=272;
public final static short MAYOR_IGUAL=273;
public final static short REPEAT=274;
public final static short WHILE=275;
public final static short GOTO=276;
public final static short LONGINT=277;
public final static short ELSE=278;
public final static short TRIPLE=279;
public final static short CONSTANTE=280;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    0,    0,    0,    0,    0,    1,    1,    2,    2,
    2,    2,    2,    2,    3,    3,    3,    3,    3,    3,
    3,    3,    3,    9,    9,    9,    9,    9,   11,   11,
   11,   12,   12,   12,   12,   12,   12,   12,   12,   12,
   12,   10,   10,   10,   10,   14,    7,    7,    7,    8,
    8,    8,   13,   13,   17,   17,   15,   15,   15,   16,
   16,   18,   18,   18,   18,    6,    4,    4,    4,    4,
    4,   19,   19,   19,   19,   19,   19,   21,   21,   21,
   20,   20,   20,   20,   20,   20,   20,   28,   28,   28,
   28,   28,   28,   28,   27,   27,   27,   27,   27,   27,
   27,   29,   29,   29,   29,   29,   29,   29,   22,   22,
   22,   22,   22,   22,   22,   22,   22,   22,   22,   22,
   22,   22,   22,   22,   30,   31,   26,   26,   26,   26,
   26,   26,   26,   26,   26,   26,   26,   26,   26,   26,
   26,   26,   33,   33,   33,   33,   33,   33,   33,   33,
   33,   33,   33,   33,   33,   35,   35,   35,   36,   36,
   36,   36,   36,   36,   37,   37,   37,   32,   32,   32,
   38,   38,   34,   34,   34,   23,   23,   23,   23,   23,
   23,   23,   23,   23,   39,   24,   24,   24,   24,    5,
    5,   25,   25,   25,
};
final static short yylen[] = {                            2,
    4,    3,    3,    4,    3,    4,    2,    1,    1,    2,
    1,    2,    2,    2,    3,    5,    5,    6,    4,    2,
    2,    3,    2,    2,    2,    1,    2,    2,    6,    5,
    5,    5,    5,    5,    5,    5,    5,    4,    4,    3,
    3,    7,    8,    9,    7,    2,    1,    1,    1,    3,
    3,    1,    3,    1,    1,    2,    2,    2,    2,    2,
    1,    1,    2,    1,    1,    4,    1,    1,    1,    1,
    1,    1,    1,    1,    1,    1,    1,    3,    4,    4,
    3,    3,    3,    3,    3,    3,    1,    3,    3,    3,
    3,    3,    3,    1,    1,    1,    1,    2,    2,    2,
    2,    4,    5,    7,    6,    4,    7,    9,    3,    6,
    8,    5,    3,    5,    7,    5,    7,    5,    6,    4,
    4,    5,    6,    6,    4,    2,    7,    7,    9,    9,
    7,    9,    8,    6,    8,    6,    7,    5,    7,    9,
    9,    9,   11,    7,    9,   11,   11,   11,   11,   11,
   11,    3,    3,    3,    3,    3,    1,    3,    1,    1,
    1,    1,    1,    1,    3,    2,    2,    2,    2,    3,
    3,    2,    2,    2,    3,    6,    6,    6,    6,    5,
    6,    6,    6,    3,    1,    3,    3,    3,    2,    2,
    1,    4,    4,    4,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,   49,    0,    0,
    0,    0,    0,    0,   47,  185,    0,   48,  191,    0,
    8,    9,    0,   11,    0,    0,    0,    0,   26,    0,
   67,   68,   69,   70,   71,    0,    0,    0,    2,    0,
    0,   13,   27,    0,    0,    0,  189,  190,    0,    0,
    0,   55,    0,    0,   96,    0,   94,    0,   97,    0,
    0,   28,    0,   24,   25,    0,   46,    0,    0,    0,
    5,    7,   12,   10,   14,    0,    0,   21,   20,   23,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    4,
    1,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  162,  163,  164,    0,    0,    0,    0,  159,  160,  161,
    0,   98,    0,   99,  100,  101,   56,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  188,  187,  186,    0,
   15,    0,   22,    0,    0,    0,    0,  126,  113,  109,
    0,    0,    0,    0,    0,  169,  168,  184,    0,   40,
    0,    0,    0,    0,   41,    0,    0,    0,    0,   54,
   83,   86,   90,   93,    0,    0,    0,    0,    0,    0,
    0,  125,    0,    0,   82,   81,   85,   84,    0,    0,
    0,    0,   89,   88,   92,   91,    0,  120,    0,  194,
  193,  192,    0,   66,   19,    0,   52,    0,   58,   59,
   57,    0,    0,  121,    0,    0,    0,    0,    0,  167,
  166,  170,    0,    0,    0,   38,    0,   39,    0,    0,
   31,    0,    0,   30,  106,    0,    0,    0,  102,    0,
    0,    0,  116,    0,    0,    0,  118,    0,    0,    0,
   17,    0,   16,    0,    0,    0,    0,    0,  122,  114,
  112,    0,    0,    0,  165,    0,    0,    0,   37,   34,
   36,   33,   35,   32,   29,   53,    0,    0,  103,    0,
    0,    0,    0,  110,    0,    0,    0,    0,    0,    0,
    0,  119,    0,   18,   51,   50,    0,    0,    0,   62,
   64,   76,    0,   61,    0,   72,   73,   74,   75,   77,
  124,  123,  183,  182,  177,  181,  178,  179,  176,    0,
    0,  105,    0,    0,    0,    0,  115,    0,    0,  117,
    0,    0,    0,   45,    0,    0,   42,   60,   63,    0,
  104,  107,    0,    0,    0,    0,    0,  111,    0,    0,
   43,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   44,    0,    0,    0,    0,    0,    0,
    0,  108,    0,    0,    0,    0,    0,  145,    0,    0,
    0,    0,    0,  174,  173,  138,    0,    0,    0,    0,
    0,    0,    0,    0,  134,    0,    0,    0,  172,  175,
    0,    0,  136,    0,  146,  148,  150,  147,  149,  151,
  143,  128,    0,    0,  139,    0,  131,  127,    0,  171,
  137,    0,    0,  133,    0,    0,    0,    0,  135,  129,
  142,  141,  140,  132,  130,
};
final static short yydgoto[] = {                          3,
   20,   21,   22,   87,   24,  302,   26,  208,   27,   28,
   29,   43,  169,   30,  146,  303,   55,  304,  369,  189,
  306,   32,  307,  308,  309,  310,   57,   58,   59,   36,
   83,  148,   60,  370,  190,  111,  155,  383,   37,
};
final static short yysindex[] = {                      -106,
 -133,  916,    0,  916,  658,  -26,  233,    0,  -86,  -38,
  -21,  118,   85,    3,    0,    0,  129,    0,    0,  754,
    0,    0,  -36,    0,   50, -176,  -34,   74,    0,   20,
    0,    0,    0,    0,    0, -233, 1054,  776,    0,  800,
  544,    0,    0, -185,   82, -135,    0,    0,   18,  736,
   55,    0, -119,  -16,    0,  728,    0,  115,    0,  -30,
  125,    0, -113,    0,    0,  131,    0,  -19,  111,    7,
    0,    0,    0,    0,    0,   54,  123,    0,    0,    0,
  393, 1067, -144,  -87,  193,  478,    4, -201,  -26,    0,
    0,  -47,  509,  -61,  -19,  919,  151,  528,  -19,  -29,
    0,    0,    0,  -25,  -25,  -25,  -25,    0,    0,    0,
  -19,    0,  100,    0,    0,    0,    0,  -19,  681,   12,
  -19,   21,   31,  -19,   75,  144,  150,  703,  -58,  117,
  843,  261,  456,  -86,  393,  624,    0,    0,    0,  259,
    0,  146,    0,   70,  353,   47,  126,    0,    0,    0,
 1080,  -32,  -19,   57, 1117,    0,    0,    0,  -22,    0,
  107,  310,  -40,  314,    0,  528,  -41,  528,   -4,    0,
    0,    0,    0,    0,  528,  898,  -13,  594,  718,  -19,
  -58,    0,  154,  528,    0,    0,    0,    0,  528,   88,
  919,  528,    0,    0,    0,    0,  159,    0,  703,    0,
    0,    0,  370,    0,    0,  335,    0,   94,    0,    0,
    0,  179,  186,    0, 1093,  191, -174,  -16,    2,    0,
    0,    0,  409,  -16,   14,    0,  418,    0,  543,  547,
    0,    5,  -29,    0,    0,  -19,  949,  215,    0,  -19,
  137,  216,    0,  703,  166,   16,    0,  703,  224,  227,
    0,  447,    0,  582,  466,  938,  245,  251,    0,    0,
    0,  265,  686,  499,    0,   39,  708,  503,    0,    0,
    0,    0,    0,    0,    0,    0,  689,  295,    0,  519,
   56,  399,  677,    0,  703,  300,  919,  528,  -19,  912,
  316,    0,  960,    0,    0,    0,  324, -248,   28,    0,
    0,    0,  822,    0,  534,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   66,
  554,    0,   25,  574,   27,  364,    0,  959,  -19,    0,
  340,  849,  960,    0,  -16,   36,    0,    0,    0,  365,
    0,    0,  -19,  -19,  -19,  -19,  -19,    0,  -19,  596,
    0,  872,  715,   37, 1101,  381,  605,  962, 1136, 1158,
 1198, 1229,  162,    0,  403, 1101,  422,  406,   60,  232,
 1101,    0,  -19,  -19,  -19,  -19,  -19,    0, 1101,  234,
 1001,  628, 1116,    0,    0,    0, 1101,  248,  377,  410,
  414,   96,   98,  250,    0, 1101,  271, -101,    0,    0,
  640,  437,    0, 1101,    0,    0,    0,    0,    0,    0,
    0,    0, 1101,  442,    0, 1024,    0,    0, 1039,    0,
    0,  451,  453,    0,  457,  470,  498, -170,    0,    0,
    0,    0,    0,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  710,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  262,    0,    0,    0,    0,    0,    0,  285,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  744,    0,
    0,    0,    0,    0,    0,    0,    0,  549,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  575,    0,  599,    0,    0,
    0,    0,    0,    0,  171,    0,    0,    0,    0,    0,
    0,    0,    0,  207,    0,    0,    0,    0,  307,    0,
  385,  421,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  635,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  329,  355,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  894,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  444,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
  264,  106, -139,  858,  163,  790,  838,    0,    0,  748,
  772,  773,  615,    0,  652, -231,  -43, -253,  770,  -10,
    1,    0,    8,   32,  164,    0,  382,    0,    0,    0,
  576,  -23,  -45,  874,  875,  -55,    0,    0,    0,
};
final static int YYTABLESIZE=1393;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         56,
  125,   54,   31,   53,   31,   31,   53,  219,  120,   33,
  129,   33,   33,   88,  161,   53,  334,  225,   61,   53,
   31,  229,   74,  118,   79,   53,  236,   33,   53,   82,
   41,   53,   42,   34,   98,   34,   34,   31,   31,  233,
   31,  118,   68,  119,   33,   33,   53,   33,  233,  338,
  133,   34,  182,  118,  158,  289,  170,  136,   53,   81,
   53,  332,  157,  125,  344,   53,  347,  335,   34,   34,
  139,   34,   53,  159,   95,   53,  356,  367,  338,  317,
   76,  260,   31,  231,  166,  434,   31,  213,  168,   33,
  212,  261,   13,   33,  113,  435,  283,  142,  338,  245,
  175,  352,  178,  114,  115,  116,  341,  179,   75,  340,
  184,  149,  141,   34,  192,  221,  300,   34,  385,   53,
  234,  150,    4,  170,   99,   72,   53,  217,   31,  275,
    5,  245,   80,  151,  246,   33,  409,  254,  411,  245,
  100,  245,   98,   72,   53,   72,  134,  108,  110,  109,
    1,   31,  253,  300,  417,   31,  126,    2,   33,   34,
  117,  127,   33,  300,  418,   35,  237,   35,   35,   53,
  135,    8,  262,  264,  137,  249,  419,  283,  266,  268,
  245,  143,   34,   35,   15,  246,   34,  152,   53,  276,
   18,  258,  300,  300,   53,  165,  108,  110,  109,   31,
   35,   35,  378,   35,  128,  245,   33,   56,   56,  160,
   53,  153,  300,   56,   56,   31,  228,   50,   51,   73,
  286,   78,   33,  218,  291,  277,  323,  325,  153,  153,
   34,   51,  128,  224,  288,  290,   96,   51,   52,   50,
   51,   52,   96,   51,   31,   35,   34,  155,   31,   35,
   52,   33,   41,  336,   52,   33,   47,  263,   51,  156,
   52,  326,  138,   52,  155,  155,   52,   38,   40,  267,
   51,   96,   51,  167,  128,   34,  185,   51,  328,   34,
  343,   52,  346,   50,   51,   31,  187,   51,   56,  354,
   48,   35,   33,   52,  316,   52,   47,  125,  355,  366,
   52,  201,   95,   95,   95,   95,   95,   52,   95,  140,
   52,  282,  220,  112,   35,  384,   34,  205,   35,   95,
   95,   95,   95,   95,  353,   87,  209,   87,   87,   87,
  191,   51,  358,  359,  360,  361,  362,   96,   51,    8,
   66,   67,   87,   87,   87,   87,   87,  157,  101,  252,
  157,  408,   15,  410,   52,  176,   51,    8,   18,  102,
  103,   52,   35,  226,  157,  157,  157,  157,  157,  158,
   15,  227,  158,   62,   63,  230,   18,    9,   35,   52,
  131,   51,  198,  132,   69,   70,  158,  158,  158,  158,
  158,  214,  282,  251,  199,  156,   41,  101,  156,  193,
   51,  206,  207,  215,   52,  195,   51,   35,  102,  103,
  250,   35,  156,  156,  156,  156,  156,  405,  301,  243,
  245,  287,   51,   52,  247,  154,  153,  153,  153,   52,
  153,  244,  153,  153,  255,  153,  248,  153,  153,  153,
  153,  153,  154,  154,  153,   52,  153,  153,   35,  256,
  406,   44,  153,  245,  407,  301,  259,  245,  108,  110,
  109,  152,  155,  155,  155,  301,  155,  265,  155,  155,
  280,  155,   46,  155,  155,  155,  155,  155,  152,  152,
  155,  284,  155,  155,  144,  171,  172,  173,  174,  292,
  293,   44,   45,  285,  301,  301,  202,  386,  122,  395,
  123,  144,  144,  186,  188,  294,  297,  194,  196,  387,
  311,  396,   46,  403,  301,  412,  312,   95,   95,   95,
  313,   95,   95,   95,   95,  404,   95,  413,   95,   95,
   95,   95,   95,   95,   95,   95,  415,   95,   95,  315,
   87,   87,   87,  319,   87,   87,   87,   87,  416,   87,
  321,   87,   87,   87,   87,   87,   87,   87,   87,  322,
   87,   87,  157,  157,  157,  327,  157,  157,  157,  157,
  122,  157,  123,  157,  157,  157,  157,  157,  157,  157,
  157,  330,  157,  157,  158,  158,  158,  333,  158,  158,
  158,  158,  339,  158,  342,  158,  158,  158,  158,  158,
  158,  158,  158,   93,  158,  158,   78,   78,  210,  211,
  156,  156,  156,  345,  156,  156,  156,  156,   41,  156,
  357,  156,  156,  156,  156,  156,  156,  156,  156,  348,
  156,  156,   79,   79,  239,  130,  122,  238,  123,  245,
  154,  154,  154,  371,  154,  372,  154,  154,  144,  154,
    8,  154,  154,  154,  154,  154,   80,   80,  154,  101,
  154,  154,   85,   15,  204,  379,  122,  299,  123,   18,
  102,  103,   11,  269,  270,   14,  152,  152,  152,   16,
  152,   17,  152,  152,  381,  152,  399,  152,  152,  152,
  152,  152,  180,  180,  152,  183,  152,  152,  420,  144,
  144,  144,  421,  144,  197,  144,  144,  424,  144,    3,
  144,  144,  144,  144,  144,   19,  429,  144,  430,  144,
  144,  181,  431,  122,  180,  123,  314,  106,  104,  320,
  105,  122,  107,  123,   85,  432,  108,  110,  109,   10,
  108,  110,  109,    6,   11,  108,  110,  109,  318,  106,
  104,   16,  105,   17,  107,  365,  242,  122,  180,  123,
  122,  240,  123,  433,  162,  163,    8,  108,  110,  109,
  122,  124,  123,   77,  108,  110,  109,  106,  104,   15,
  105,  232,  107,   64,   65,   18,  203,  108,  110,  109,
    0,   25,    0,   25,   25,  108,  110,  109,  271,  272,
   92,    8,  273,  274,   78,   78,   78,    0,   78,   25,
   78,   19,    0,   78,   15,   78,   78,   78,   78,   78,
   18,    0,   78,    0,   78,   78,    0,   25,    0,   25,
   79,   79,   79,   19,   79,    0,   79,  295,  296,   79,
    0,   79,   79,   79,   79,   79,   49,    0,   79,    0,
   79,   79,    0,    0,   80,   80,   80,   19,   80,   23,
   80,   23,   23,   80,    0,   80,   80,   80,   80,   80,
    0,    0,   80,    0,   80,   80,    0,   23,   94,   19,
    0,    0,   97,  200,  106,  104,    0,  105,    0,  107,
  180,  180,  180,    0,  180,   23,  180,   23,    0,  180,
    0,  180,  180,  180,  180,  180,   19,    0,  180,    0,
  180,  180,    0,    6,    7,    8,    0,    9,  145,   10,
    0,    0,   39,    0,   11,   12,   13,   14,   15,   19,
  164,   16,  324,   17,   18,    0,  121,  101,  235,  106,
  104,  101,  105,  154,  107,    0,  101,    0,  102,  103,
  177,   65,  102,  103,  122,  329,  123,  102,  103,   85,
  106,  104,    0,  105,   10,  107,   86,    0,  101,   11,
  121,   97,  145,   19,    0,  101,   16,    0,   17,  102,
  103,    0,    0,  121,    0,    0,  102,  103,  101,  279,
    0,  122,  278,  123,    0,   19,  101,    0,    0,  102,
  103,  122,  349,  123,  122,  373,  123,  102,  103,    6,
    7,    8,  223,    9,    0,   10,    0,   19,   71,    0,
   11,   12,   13,   14,   15,  305,    0,   16,    0,   17,
   18,   89,    7,    8,    0,    9,    0,   10,    0,    0,
   90,    0,   11,   12,   13,   14,   15,    0,    0,   16,
    0,   17,   18,    0,  241,    6,    7,    8,    0,    9,
    0,   10,  305,    0,   91,    0,   11,   12,   13,   14,
   15,    0,  305,   16,    0,   17,   18,  331,    7,    8,
    0,    9,    0,  299,    0,    0,  337,    0,   11,   12,
   13,   14,   15,    0,    0,   16,    0,   17,   18,    0,
    0,  305,  305,    0,  331,    7,    8,    0,    9,    0,
  299,    0,    0,  351,  281,   11,   12,   13,   14,   15,
    0,  305,   16,    0,   17,   18,    0,  331,    7,    8,
    0,    9,    0,  299,    0,    0,  364,  382,   11,   12,
   13,   14,   15,    0,    0,   16,    0,   17,   18,   65,
   65,   65,  401,   65,    0,   65,    0,    0,   65,    0,
   65,   65,   65,   65,   65,    0,    0,   65,    0,   65,
   65,    6,    7,    8,    0,    9,    0,   10,  122,  374,
  123,    0,   11,   12,   13,   14,   15,    0,    0,   16,
    0,   17,   18,  298,    7,    8,    0,    9,    0,  299,
  122,  375,  123,  350,   11,   12,   13,   14,   15,    0,
    0,   16,    0,   17,   18,  331,    7,    8,    0,    9,
    0,  299,    0,  363,    0,    0,   11,   12,   13,   14,
   15,    0,    0,   16,    0,   17,   18,    0,    0,  380,
  122,  376,  123,    0,  388,    0,    0,  389,  390,  391,
  392,  393,  394,    0,  398,    0,  397,   85,    0,    0,
  402,    0,  299,    0,  368,    0,    0,   11,    0,  414,
   14,  122,  377,  123,   16,    0,   17,  422,    0,  425,
   85,    0,    0,    0,    0,  299,  423,  368,    0,  426,
   11,    0,  428,   14,  427,   85,    0,   16,    0,   17,
  299,    0,  368,    0,    0,   11,    0,    0,   14,   84,
   85,    0,   16,    0,   17,   10,    0,   86,    0,    0,
   11,    0,  147,   85,    0,    0,    0,   16,   10,   17,
   86,    0,    0,   11,    0,  216,   85,    0,    0,    0,
   16,   10,   17,   86,    0,    0,   11,    0,  257,   85,
    0,    0,    0,   16,   10,   17,   86,   85,    0,   11,
    0,    0,  299,    0,  368,    0,   16,   11,   17,    0,
   14,    0,   85,   85,   16,    0,   17,  299,   10,    0,
  400,  222,   11,   11,    0,   14,    0,    0,    0,   16,
   16,   17,   17,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         10,
   56,   40,    2,   45,    4,    5,   45,   40,   54,    2,
   41,    4,    5,   37,   62,   45,  265,   40,   40,   45,
   20,   62,   59,   40,   59,   45,   40,   20,   45,  263,
  279,   45,   59,    2,   45,    4,    5,   37,   38,   44,
   40,   40,   40,   54,   37,   38,   45,   40,   44,  303,
   61,   20,   41,   40,  256,   40,  100,   68,   45,   40,
   45,  293,   59,  119,   40,   45,   40,   40,   37,   38,
   64,   40,   45,  275,  260,   45,   41,   41,  332,   41,
  257,  256,   82,  125,   95,  256,   86,   41,   99,   82,
   44,  266,  269,   86,   40,  266,   41,   44,  352,   44,
  111,  333,  113,   49,   50,   51,   41,  118,   59,   44,
  121,  256,   59,   82,  125,   59,  256,   86,   59,   45,
  125,  266,  256,  167,  260,   20,   45,  151,  128,  125,
  264,   44,   59,  278,  190,  128,   41,   44,   41,   44,
  123,   44,  153,   38,   45,   40,  260,   60,   61,   62,
  257,  151,   59,  293,  256,  155,   42,  264,  151,  128,
  280,   47,  155,  303,  266,    2,  177,    4,    5,   45,
   40,  258,  218,  219,   64,  199,  278,   41,  224,  225,
   44,   59,  151,   20,  271,  241,  155,  275,   45,  233,
  277,  215,  332,  333,   45,  257,   60,   61,   62,  199,
   37,   38,   41,   40,  263,   44,  199,  218,  219,  257,
   45,   41,  352,  224,  225,  215,  257,  256,  257,  256,
  244,  256,  215,  256,  248,  236,  282,  283,   58,   59,
  199,  257,  263,  256,  245,  246,  256,  257,  280,  256,
  257,  280,  256,  257,  244,   82,  215,   41,  248,   86,
  280,  244,  279,  299,  280,  248,   64,  256,  257,  256,
  280,  285,  256,  280,   58,   59,  280,    4,    5,  256,
  257,  256,  257,  123,  263,  244,  256,  257,  289,  248,
  256,  280,  256,  256,  257,  285,  256,  257,  299,  335,
   58,  128,  285,  280,  256,  280,   64,  353,  263,  263,
  280,   41,   41,   42,   43,   44,   45,  280,   47,  256,
  280,  256,  256,  259,  151,  256,  285,   59,  155,   58,
   59,   60,   61,   62,  335,   41,  257,   43,   44,   45,
  256,  257,  343,  344,  345,  346,  347,  256,  257,  258,
  256,  257,   58,   59,   60,   61,   62,   41,  261,  256,
   44,  256,  271,  256,  280,  256,  257,  258,  277,  272,
  273,  280,  199,  257,   58,   59,   60,   61,   62,   41,
  271,   62,   44,  256,  257,   62,  277,  260,  215,  280,
  256,  257,  266,  259,  256,  257,   58,   59,   60,   61,
   62,  266,  256,   59,  278,   41,  279,  261,   44,  256,
  257,  256,  257,  278,  280,  256,  257,  244,  272,  273,
   41,  248,   58,   59,   60,   61,   62,   41,  256,  266,
   44,  256,  257,  280,  266,   41,  256,  257,  258,  280,
  260,  278,  262,  263,  256,  265,  278,  267,  268,  269,
  270,  271,   58,   59,  274,  280,  276,  277,  285,  264,
   41,  259,  260,   44,   41,  293,  266,   44,   60,   61,
   62,   41,  256,  257,  258,  303,  260,   59,  262,  263,
  256,  265,  280,  267,  268,  269,  270,  271,   58,   59,
  274,  266,  276,  277,   41,  104,  105,  106,  107,  266,
  264,  259,  260,  278,  332,  333,   41,  266,   43,  266,
   45,   58,   59,  122,  123,   59,   41,  126,  127,  278,
  266,  278,  280,  266,  352,  266,  266,  256,  257,  258,
  256,  260,  261,  262,  263,  278,  265,  278,  267,  268,
  269,  270,  271,  272,  273,  274,  266,  276,  277,   41,
  256,  257,  258,   41,  260,  261,  262,  263,  278,  265,
  256,  267,  268,  269,  270,  271,  272,  273,  274,   41,
  276,  277,  256,  257,  258,  266,  260,  261,  262,  263,
   43,  265,   45,  267,  268,  269,  270,  271,  272,  273,
  274,  266,  276,  277,  256,  257,  258,  264,  260,  261,
  262,  263,   59,  265,   41,  267,  268,  269,  270,  271,
  272,  273,  274,   60,  276,  277,   58,   59,  256,  257,
  256,  257,  258,   40,  260,  261,  262,  263,  279,  265,
  256,  267,  268,  269,  270,  271,  272,  273,  274,  266,
  276,  277,   58,   59,   41,   60,   43,   44,   45,   44,
  256,  257,  258,  263,  260,   41,  262,  263,  256,  265,
  258,  267,  268,  269,  270,  271,   58,   59,  274,  261,
  276,  277,  257,  271,   41,  263,   43,  262,   45,  277,
  272,  273,  267,  256,  257,  270,  256,  257,  258,  274,
  260,  276,  262,  263,  263,  265,   59,  267,  268,  269,
  270,  271,   58,   59,  274,  120,  276,  277,   59,  256,
  257,  258,  266,  260,  129,  262,  263,  266,  265,    0,
  267,  268,  269,  270,  271,   58,  266,  274,  266,  276,
  277,   41,  266,   43,   44,   45,   41,   42,   43,   41,
   45,   43,   47,   45,  257,  266,   60,   61,   62,  262,
   60,   61,   62,    0,  267,   60,   61,   62,   41,   42,
   43,  274,   45,  276,   47,   41,  181,   43,   44,   45,
   43,   44,   45,  266,  256,  257,  258,   60,   61,   62,
   43,   44,   45,   26,   60,   61,   62,   42,   43,  271,
   45,  167,   47,   12,   12,  277,  135,   60,   61,   62,
   -1,    2,   -1,    4,    5,   60,   61,   62,  256,  257,
  257,  258,  256,  257,  256,  257,  258,   -1,  260,   20,
  262,   58,   -1,  265,  271,  267,  268,  269,  270,  271,
  277,   -1,  274,   -1,  276,  277,   -1,   38,   -1,   40,
  256,  257,  258,   58,  260,   -1,  262,  256,  257,  265,
   -1,  267,  268,  269,  270,  271,    9,   -1,  274,   -1,
  276,  277,   -1,   -1,  256,  257,  258,   58,  260,    2,
  262,    4,    5,  265,   -1,  267,  268,  269,  270,  271,
   -1,   -1,  274,   -1,  276,  277,   -1,   20,   41,   58,
   -1,   -1,   45,   41,   42,   43,   -1,   45,   -1,   47,
  256,  257,  258,   -1,  260,   38,  262,   40,   -1,  265,
   -1,  267,  268,  269,  270,  271,   58,   -1,  274,   -1,
  276,  277,   -1,  256,  257,  258,   -1,  260,   81,  262,
   -1,   -1,  265,   -1,  267,  268,  269,  270,  271,   58,
   93,  274,  256,  276,  277,   -1,  256,  261,   41,   42,
   43,  261,   45,   86,   47,   -1,  261,   -1,  272,  273,
  113,   58,  272,  273,   43,   44,   45,  272,  273,  257,
   42,   43,   -1,   45,  262,   47,  264,   -1,  261,  267,
  256,  134,  135,   58,   -1,  261,  274,   -1,  276,  272,
  273,   -1,   -1,  256,   -1,   -1,  272,  273,  261,   41,
   -1,   43,   44,   45,   -1,   58,  261,   -1,   -1,  272,
  273,   43,   44,   45,   43,   44,   45,  272,  273,  256,
  257,  258,  155,  260,   -1,  262,   -1,   58,  265,   -1,
  267,  268,  269,  270,  271,  256,   -1,  274,   -1,  276,
  277,  256,  257,  258,   -1,  260,   -1,  262,   -1,   -1,
  265,   -1,  267,  268,  269,  270,  271,   -1,   -1,  274,
   -1,  276,  277,   -1,  180,  256,  257,  258,   -1,  260,
   -1,  262,  293,   -1,  265,   -1,  267,  268,  269,  270,
  271,   -1,  303,  274,   -1,  276,  277,  256,  257,  258,
   -1,  260,   -1,  262,   -1,   -1,  265,   -1,  267,  268,
  269,  270,  271,   -1,   -1,  274,   -1,  276,  277,   -1,
   -1,  332,  333,   -1,  256,  257,  258,   -1,  260,   -1,
  262,   -1,   -1,  265,  240,  267,  268,  269,  270,  271,
   -1,  352,  274,   -1,  276,  277,   -1,  256,  257,  258,
   -1,  260,   -1,  262,   -1,   -1,  265,  368,  267,  268,
  269,  270,  271,   -1,   -1,  274,   -1,  276,  277,  256,
  257,  258,  383,  260,   -1,  262,   -1,   -1,  265,   -1,
  267,  268,  269,  270,  271,   -1,   -1,  274,   -1,  276,
  277,  256,  257,  258,   -1,  260,   -1,  262,   43,   44,
   45,   -1,  267,  268,  269,  270,  271,   -1,   -1,  274,
   -1,  276,  277,  256,  257,  258,   -1,  260,   -1,  262,
   43,   44,   45,  329,  267,  268,  269,  270,  271,   -1,
   -1,  274,   -1,  276,  277,  256,  257,  258,   -1,  260,
   -1,  262,   -1,  349,   -1,   -1,  267,  268,  269,  270,
  271,   -1,   -1,  274,   -1,  276,  277,   -1,   -1,  366,
   43,   44,   45,   -1,  371,   -1,   -1,  373,  374,  375,
  376,  377,  379,   -1,  381,   -1,  256,  257,   -1,   -1,
  387,   -1,  262,   -1,  264,   -1,   -1,  267,   -1,  396,
  270,   43,   44,   45,  274,   -1,  276,  404,   -1,  256,
  257,   -1,   -1,   -1,   -1,  262,  413,  264,   -1,  416,
  267,   -1,  419,  270,  256,  257,   -1,  274,   -1,  276,
  262,   -1,  264,   -1,   -1,  267,   -1,   -1,  270,  256,
  257,   -1,  274,   -1,  276,  262,   -1,  264,   -1,   -1,
  267,   -1,  256,  257,   -1,   -1,   -1,  274,  262,  276,
  264,   -1,   -1,  267,   -1,  256,  257,   -1,   -1,   -1,
  274,  262,  276,  264,   -1,   -1,  267,   -1,  256,  257,
   -1,   -1,   -1,  274,  262,  276,  264,  257,   -1,  267,
   -1,   -1,  262,   -1,  264,   -1,  274,  267,  276,   -1,
  270,   -1,  257,  257,  274,   -1,  276,  262,  262,   -1,
  265,  265,  267,  267,   -1,  270,   -1,   -1,   -1,  274,
  274,  276,  276,
};
}
final static short YYFINAL=3;
final static short YYMAXTOKEN=280;
final static String yyname[] = {
"end-of-file",null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,"'('","')'","'*'","'+'","','",
"'-'",null,"'/'",null,"'1'","'2'","'3'",null,null,null,null,null,null,"':'",
"';'","'<'","'='","'>'",null,"'@'",null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,"'{'",null,"'}'",null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,"IDENTIFICADOR","HEXADECIMAL",
"CADENA_MULTI","SIMASIGNACION","DISTINTO","IF","THEN","BEGIN","END","END_IF",
"OUTF","TYPEDEF","FUN","RET","SINGLE","MENOR_IGUAL","MAYOR_IGUAL","REPEAT",
"WHILE","GOTO","LONGINT","ELSE","TRIPLE","CONSTANTE",
};
final static String yyrule[] = {
"$accept : programa",
"programa : IDENTIFICADOR BEGIN cuerpo END",
"programa : IDENTIFICADOR BEGIN END",
"programa : IDENTIFICADOR BEGIN cuerpo",
"programa : IDENTIFICADOR error cuerpo END",
"programa : BEGIN cuerpo END",
"programa : IDENTIFICADOR error cuerpo error",
"cuerpo : cuerpo sentencia",
"cuerpo : sentencia",
"sentencia : sentenciaDeclarativa",
"sentencia : sentenciaEjecutable ';'",
"sentencia : etiqueta",
"sentencia : sentenciaEjecutable error",
"sentencia : error ';'",
"sentencia : sentenciaRet ';'",
"sentenciaDeclarativa : tipoDato IDENTIFICADOR ';'",
"sentenciaDeclarativa : tipoDato IDENTIFICADOR ',' listaVariable ';'",
"sentenciaDeclarativa : tipoDato IDENTIFICADOR ',' error ';'",
"sentenciaDeclarativa : tipoDato IDENTIFICADOR ',' listaVariable error ';'",
"sentenciaDeclarativa : tipoDato IDENTIFICADOR error ';'",
"sentenciaDeclarativa : typedefDeclaracion ';'",
"sentenciaDeclarativa : typedefDeclaracion error",
"sentenciaDeclarativa : tipoDato funDeclaracion ';'",
"sentenciaDeclarativa : funDeclaracion ';'",
"typedefDeclaracion : TYPEDEF declaracionSubtipo",
"typedefDeclaracion : TYPEDEF declaracionTriple",
"typedefDeclaracion : declaracionSubtipo",
"typedefDeclaracion : error declaracionTriple",
"typedefDeclaracion : TYPEDEF error",
"declaracionSubtipo : IDENTIFICADOR SIMASIGNACION tipoDato '{' listaConstante '}'",
"declaracionSubtipo : SIMASIGNACION tipoDato '{' listaConstante '}'",
"declaracionSubtipo : IDENTIFICADOR SIMASIGNACION tipoDato '{' '}'",
"declaracionTriple : TRIPLE '<' tipoDato '>' IDENTIFICADOR",
"declaracionTriple : TRIPLE '<' IDENTIFICADOR '>' IDENTIFICADOR",
"declaracionTriple : TRIPLE '<' error '>' IDENTIFICADOR",
"declaracionTriple : TRIPLE '<' tipoDato '>' error",
"declaracionTriple : TRIPLE '<' IDENTIFICADOR '>' error",
"declaracionTriple : TRIPLE '<' error '>' error",
"declaracionTriple : TRIPLE IDENTIFICADOR '>' IDENTIFICADOR",
"declaracionTriple : TRIPLE '<' IDENTIFICADOR IDENTIFICADOR",
"declaracionTriple : TRIPLE IDENTIFICADOR IDENTIFICADOR",
"declaracionTriple : TRIPLE tipoDato IDENTIFICADOR",
"funDeclaracion : funComienzo '(' parametro ')' BEGIN cuerpoFuncion END",
"funDeclaracion : FUN error '(' parametro ')' BEGIN cuerpoFuncion END",
"funDeclaracion : funComienzo '(' parametro ',' error ')' BEGIN cuerpoFuncion END",
"funDeclaracion : funComienzo '(' parametro ')' BEGIN error END",
"funComienzo : FUN IDENTIFICADOR",
"tipoDato : SINGLE",
"tipoDato : LONGINT",
"tipoDato : HEXADECIMAL",
"listaVariable : listaVariable ',' IDENTIFICADOR",
"listaVariable : listaVariable ',' error",
"listaVariable : IDENTIFICADOR",
"listaConstante : listaConstante ',' constante",
"listaConstante : constante",
"constante : CONSTANTE",
"constante : '-' CONSTANTE",
"parametro : tipoDato IDENTIFICADOR",
"parametro : error IDENTIFICADOR",
"parametro : tipoDato error",
"cuerpoFuncion : cuerpoFuncion sentenciaConRet",
"cuerpoFuncion : sentenciaConRet",
"sentenciaConRet : sentenciaDeclarativa",
"sentenciaConRet : sentenciaEjecutableConRet ';'",
"sentenciaConRet : etiqueta",
"sentenciaConRet : sentenciaEjecutableConRet",
"sentenciaRet : RET '(' expresion ')'",
"sentenciaEjecutable : asignacion",
"sentenciaEjecutable : clausulaSeleccion",
"sentenciaEjecutable : clausulaBucle",
"sentenciaEjecutable : goto",
"sentenciaEjecutable : mensajeSalida",
"sentenciaEjecutableConRet : asignacion",
"sentenciaEjecutableConRet : clausulaBucle",
"sentenciaEjecutableConRet : goto",
"sentenciaEjecutableConRet : mensajeSalida",
"sentenciaEjecutableConRet : sentenciaRet",
"sentenciaEjecutableConRet : clausulaSeleccionConRet",
"asignacion : IDENTIFICADOR SIMASIGNACION expresion",
"asignacion : IDENTIFICADOR CADENA_MULTI SIMASIGNACION expresion",
"asignacion : IDENTIFICADOR CONSTANTE SIMASIGNACION expresion",
"expresion : expresion '+' operando",
"expresion : expresion '+' error",
"expresion : error '+' operando",
"expresion : expresion '-' operando",
"expresion : expresion '-' error",
"expresion : error '-' operando",
"expresion : termino",
"termino : termino '*' operando",
"termino : termino '*' error",
"termino : error '*' operando",
"termino : termino '/' operando",
"termino : termino '/' error",
"termino : error '/' operando",
"termino : operando",
"operando : IDENTIFICADOR",
"operando : constante",
"operando : invocacionFuncion",
"operando : IDENTIFICADOR CADENA_MULTI",
"operando : IDENTIFICADOR '1'",
"operando : IDENTIFICADOR '2'",
"operando : IDENTIFICADOR '3'",
"invocacionFuncion : IDENTIFICADOR '(' expresion ')'",
"invocacionFuncion : IDENTIFICADOR '(' tipoDato expresion ')'",
"invocacionFuncion : IDENTIFICADOR '(' tipoDato '(' expresion ')' ')'",
"invocacionFuncion : IDENTIFICADOR '(' expresion ',' error ')'",
"invocacionFuncion : IDENTIFICADOR '(' error ')'",
"invocacionFuncion : IDENTIFICADOR '(' tipoDato expresion ',' error ')'",
"invocacionFuncion : IDENTIFICADOR '(' tipoDato '(' expresion ')' ',' error ')'",
"clausulaSeleccion : inicioClausulaSeleccion cuerpoThen END_IF",
"clausulaSeleccion : IF '(' expresion ')' cuerpoThen END_IF",
"clausulaSeleccion : IF '(' expresion ')' cuerpoThen ELSE bloqueSentenciaEjecutable END_IF",
"clausulaSeleccion : inicioClausulaSeleccion cuerpoThen ELSE bloqueSentenciaEjecutable END_IF",
"clausulaSeleccion : inicioClausulaSeleccion cuerpoThen error",
"clausulaSeleccion : inicioClausulaSeleccion cuerpoThen ELSE bloqueSentenciaEjecutable error",
"clausulaSeleccion : IF '(' condicion cuerpoThen ELSE bloqueSentenciaEjecutable END_IF",
"clausulaSeleccion : IF '(' condicion cuerpoThen END_IF",
"clausulaSeleccion : IF condicion ')' cuerpoThen ELSE bloqueSentenciaEjecutable END_IF",
"clausulaSeleccion : IF condicion ')' cuerpoThen END_IF",
"clausulaSeleccion : IF condicion cuerpoThen ELSE bloqueSentenciaEjecutable END_IF",
"clausulaSeleccion : IF condicion cuerpoThen END_IF",
"clausulaSeleccion : inicioClausulaSeleccion THEN error END_IF",
"clausulaSeleccion : inicioClausulaSeleccion cuerpoThen ELSE error END_IF",
"clausulaSeleccion : inicioClausulaSeleccion THEN error ELSE bloqueSentenciaEjecutable END_IF",
"clausulaSeleccion : inicioClausulaSeleccion THEN error ELSE error END_IF",
"inicioClausulaSeleccion : IF '(' condicion ')'",
"cuerpoThen : THEN bloqueSentenciaEjecutable",
"clausulaSeleccionConRet : IF '(' condicion ')' THEN bloqueSentenciaEjecutableConRet END_IF",
"clausulaSeleccionConRet : IF '(' expresion ')' THEN bloqueSentenciaEjecutableConRet END_IF",
"clausulaSeleccionConRet : IF '(' expresion ')' THEN bloqueSentenciaEjecutableConRet ELSE bloqueSentenciaEjecutableConRet END_IF",
"clausulaSeleccionConRet : IF '(' condicion ')' THEN bloqueSentenciaEjecutableConRet ELSE bloqueSentenciaEjecutableConRet END_IF",
"clausulaSeleccionConRet : IF '(' condicion ')' THEN bloqueSentenciaEjecutableConRet error",
"clausulaSeleccionConRet : IF '(' condicion ')' THEN bloqueSentenciaEjecutableConRet ELSE bloqueSentenciaEjecutableConRet error",
"clausulaSeleccionConRet : IF '(' condicion THEN bloqueSentenciaEjecutableConRet ELSE bloqueSentenciaEjecutableConRet END_IF",
"clausulaSeleccionConRet : IF '(' condicion THEN bloqueSentenciaEjecutableConRet END_IF",
"clausulaSeleccionConRet : IF condicion ')' THEN bloqueSentenciaEjecutableConRet ELSE bloqueSentenciaEjecutableConRet END_IF",
"clausulaSeleccionConRet : IF condicion ')' THEN bloqueSentenciaEjecutableConRet END_IF",
"clausulaSeleccionConRet : IF condicion THEN bloqueSentenciaEjecutableConRet ELSE bloqueSentenciaEjecutableConRet END_IF",
"clausulaSeleccionConRet : IF condicion THEN bloqueSentenciaEjecutableConRet END_IF",
"clausulaSeleccionConRet : IF '(' condicion ')' THEN error END_IF",
"clausulaSeleccionConRet : IF '(' condicion ')' THEN bloqueSentenciaEjecutableConRet ELSE error END_IF",
"clausulaSeleccionConRet : IF '(' condicion ')' THEN error ELSE bloqueSentenciaEjecutableConRet END_IF",
"clausulaSeleccionConRet : IF '(' condicion ')' THEN error ELSE error END_IF",
"condicion : '(' expresion ',' listaExpresiones ')' comparador '(' expresion ',' listaExpresiones ')'",
"condicion : expresion ',' listaExpresiones comparador expresion ',' listaExpresiones",
"condicion : expresion ',' listaExpresiones comparador '(' expresion ',' listaExpresiones ')'",
"condicion : '(' expresion ',' listaExpresiones error comparador error expresion ',' listaExpresiones ')'",
"condicion : '(' expresion ',' listaExpresiones ')' comparador error expresion ',' listaExpresiones error",
"condicion : '(' expresion ',' listaExpresiones error comparador '(' expresion ',' listaExpresiones ')'",
"condicion : '(' expresion ',' listaExpresiones ')' comparador error expresion ',' listaExpresiones ')'",
"condicion : '(' expresion ',' listaExpresiones ')' error '(' expresion ',' listaExpresiones ')'",
"condicion : '(' expresion ',' listaExpresiones ')' comparador '(' expresion ',' listaExpresiones error",
"condicion : expresion comparador expresion",
"condicion : error comparador expresion",
"condicion : expresion comparador error",
"condicion : expresion error expresion",
"listaExpresiones : listaExpresiones ',' expresion",
"listaExpresiones : expresion",
"listaExpresiones : listaExpresiones ',' error",
"comparador : '<'",
"comparador : '>'",
"comparador : '='",
"comparador : DISTINTO",
"comparador : MENOR_IGUAL",
"comparador : MAYOR_IGUAL",
"cuerpoEjecutable : cuerpoEjecutable sentenciaEjecutable ';'",
"cuerpoEjecutable : sentenciaEjecutable ';'",
"cuerpoEjecutable : sentenciaEjecutable error",
"bloqueSentenciaEjecutable : sentenciaEjecutable ';'",
"bloqueSentenciaEjecutable : sentenciaEjecutable error",
"bloqueSentenciaEjecutable : BEGIN cuerpoEjecutable END",
"cuerpoEjecutableConRet : cuerpoEjecutableConRet sentenciaEjecutableConRet ';'",
"cuerpoEjecutableConRet : sentenciaEjecutableConRet ';'",
"bloqueSentenciaEjecutableConRet : sentenciaEjecutableConRet ';'",
"bloqueSentenciaEjecutableConRet : sentenciaEjecutableConRet error",
"bloqueSentenciaEjecutableConRet : BEGIN cuerpoEjecutableConRet END",
"clausulaBucle : repeat bloqueSentenciaEjecutable WHILE '(' condicion ')'",
"clausulaBucle : repeat error WHILE '(' condicion ')'",
"clausulaBucle : repeat bloqueSentenciaEjecutable WHILE error condicion ')'",
"clausulaBucle : repeat bloqueSentenciaEjecutable WHILE '(' error ')'",
"clausulaBucle : repeat bloqueSentenciaEjecutable WHILE '(' condicion",
"clausulaBucle : repeat bloqueSentenciaEjecutable WHILE error condicion error",
"clausulaBucle : repeat error WHILE '(' error ')'",
"clausulaBucle : repeat error WHILE error condicion error",
"clausulaBucle : repeat bloqueSentenciaEjecutable error",
"repeat : REPEAT",
"goto : GOTO IDENTIFICADOR '@'",
"goto : GOTO IDENTIFICADOR error",
"goto : GOTO error '@'",
"goto : IDENTIFICADOR '@'",
"etiqueta : IDENTIFICADOR ':'",
"etiqueta : ':'",
"mensajeSalida : OUTF '(' expresion ')'",
"mensajeSalida : OUTF '(' CADENA_MULTI ')'",
"mensajeSalida : OUTF '(' error ')'",
};

//#line 327 "gramatica.y"
//FUNCIONES
private static AnalizadorLexico lex;
public static List<Error> erroresLexico = new ArrayList<Error>();
public static List<Error> erroresSintactico = new ArrayList<Error>();
public static List<Error> erroresSemanticos = new ArrayList<Error>();
public static List<String> estructuras = new ArrayList<String>();
public static List<Terceto> tercetos = new ArrayList<Terceto>();
public static Stack<String> tercetosIncompletos = new Stack<String>();
public static Stack<Integer> inicioBucle = new Stack<Integer>();

public static List<String> ambitos = new ArrayList<String>();
public static int numeroLineaError = -1;
public static void main(String[] args) {
    String filePath = "src/MATRIZ DE TRANSICIONES - Hoja 1.csv";

    int[][] matriz = MatrizTransicion.leerMatrizDesdeCSV(filePath);
    filePath = "src/MATRIZ DE TRANSICIONES - Hoja 2.csv";

    Accion[][] matrizAcciones = MatrizAccion.leerMatrizDesdeCSV(filePath);
    Parser parser = new Parser(true);
    Parser.lex = new AnalizadorLexico("--", matriz, matrizAcciones);
    ambitos.add("main");
    if (args.length > 1) {
        Parser.lex = new AnalizadorLexico(args[0], matriz, matrizAcciones);

        parser.run();
        for (Error error: erroresLexico){System.out.println(error);}
    } else {
        Parser.lex = new AnalizadorLexico("tests3/bucles", matriz, matrizAcciones);
        
        parser.run();
        System.out.println("v---------------------------v");
        for (Error error: erroresSintactico){System.out.println(error);}
        for (Error error: erroresLexico){System.out.println(error);}
        for (String estructura: estructuras){System.out.println(estructura);}
        System.out.println("^---------------------------^");
        for (Error error: erroresSemanticos){System.out.println(error);}
        System.out.println("^---------------------------^");
        for (int i = 0; i < tercetos.size(); i++){System.out.println(i + " - " + tercetos.get(i));}
        //System.out.println("No se especifico el archivo a compilar");
    }
    System.out.println(TablaDeSimbolos.imprimir());
}
private int yylex(){
  yylval = new ParserVal();
  int idToken = -1;
  if (!lex.end()){
    idToken = lex.getNextToken(yylval);
  }
  System.out.println("PARSER: " + yyval.ival);
  return idToken;
}

private void chequearRango(Contexto contexto){
  if (contexto != null){
    if(contexto.getTipo() == TablaTipoToken.getTipoToken("longint")){
      if(Long.parseLong(contexto.getValor()) > AnalizadorLexico.MAXLONGINT){
        erroresSintactico.add(new Error(
          AnalizadorLexico.getNumeroLinea(),
          Tipo.ERROR,
          "ERROR SINTACTICO excede rangos."
        ));
      }
    } else if (contexto.getTipo() == TablaTipoToken.getTipoToken("hexadecimal")){
      long valor = Long.parseLong(contexto.getValor().substring(2), 16);
      System.out.println(valor);
      if(valor > AnalizadorLexico.MAXHEXADECIMAL){
        erroresSintactico.add(new Error(
          AnalizadorLexico.getNumeroLinea(),
          Tipo.ERROR,
          "ERROR SINTACTICO excede rangos."
        ));
      }
    }
  };
}
private void yyerror(String string) {
  numeroLineaError = AnalizadorLexico.getNumeroLinea();
  System.out.println("Error: " + string);
}
private void declararVariable(String refTipo, ArrayList<String> referenciasIdentificador){
  for (String refIdentificador: referenciasIdentificador){
    String newRef = TablaDeSimbolos.agregarSimbolo(refIdentificador + cargarAmbito(), -1, TablaDeSimbolos.getContexto(refIdentificador).getRef());
    Contexto conIdentificador = TablaDeSimbolos.getContexto(newRef);
    if (conIdentificador.getTipo() == -1){
      if (conIdentificador.getUso().equals("") ){
        conIdentificador.setTipo(TablaDeSimbolos.getContexto(refTipo).getTipo());
        conIdentificador.setUso("nombre de variable");
      }else{
        erroresSemanticos.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SEMANTICO identificador ya posee otro uso"));
      }
    }else{
      erroresSemanticos.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SEMANTICO " + refIdentificador + " ya declarada"));
    };
  };
}

private String cargarAmbito(){
  String aux = "";
  for(String ambito: ambitos){
    aux = aux + ":" + ambito;
  }
  return aux;
}

private void declararFuncion(String ref){
  String newRef = TablaDeSimbolos.agregarSimbolo(ref + cargarAmbito(), -1, TablaDeSimbolos.getContexto(ref).getRef());
  Contexto con = TablaDeSimbolos.getContexto(newRef);
  if (con.getUso().equals("") ){
    con.setUso("nombre de funcion");
    con.setTipo(TablaTipoToken.getTipoToken(TablaTipoToken.FUN));
    con.setDeclarado();
  }else{
    erroresSemanticos.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SEMANTICO el identificador " + ref + " ya posee otro uso"));
  };
}

private void declaracionTriple(String refTipo, String refIdentificador){
  String newRef = TablaDeSimbolos.agregarSimbolo(refIdentificador + cargarAmbito(), -1, TablaDeSimbolos.getContexto(refIdentificador).getRef());
  Contexto conIdentificador = TablaDeSimbolos.getContexto(newRef);
  if (conIdentificador.getTipo() == -1){
    if (conIdentificador.getUso().equals("") ){
      conIdentificador.setTipo(TablaDeSimbolos.getContexto(refTipo).getTipo());
      conIdentificador.setUso("nombre de variable triple");
      conIdentificador.setDeclarado();
    }else{
      erroresSemanticos.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SEMANTICO identificador ya posee otro uso"));
    }
  }else{
    erroresSemanticos.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SEMANTICO " + refIdentificador + " ya declarada"));
  };
}
private void declaracionSubtipo(String refTipo, String refIdentificador){
  String newRef = TablaDeSimbolos.agregarSimbolo(refIdentificador + cargarAmbito(), -1, TablaDeSimbolos.getContexto(refIdentificador).getRef());
  Contexto conIdentificador = TablaDeSimbolos.getContexto(newRef);
  if (conIdentificador.getTipo() == -1){
    if (conIdentificador.getUso().equals("") ){
      conIdentificador.setTipo(TablaDeSimbolos.getContexto(refTipo).getTipo());
      conIdentificador.setUso("nombre de subtipo");
      //FALTA Límite inferior o Límite superior
    }else{
      erroresSemanticos.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SEMANTICO identificador ya posee otro uso"));
    }
  }else{
    erroresSemanticos.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SEMANTICO " + refIdentificador + " ya declarada"));
  };
}
private void declaracionEtiqueta(String refIdentificador){
  String newRef = TablaDeSimbolos.agregarSimbolo(refIdentificador + cargarAmbito(), -1, TablaDeSimbolos.getContexto(refIdentificador).getRef());
  Contexto conIdentificador = TablaDeSimbolos.getContexto(newRef);
  if (conIdentificador.getTipo() == -1){
    if (conIdentificador.getUso().equals("") ){
      //conIdentificador.setTipo(TablaDeSimbolos.getTipoToken());  ETIQUETA ??
      conIdentificador.setUso("nombre de etiqueta");
    }else{
      erroresSemanticos.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SEMANTICO identificador ya posee otro uso"));
    }
  }else{
    erroresSemanticos.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SEMANTICO " + refIdentificador + " ya declarada"));
  };
}
private void declaracionParametro(String refTipo, String refIdentificador){
  String newRef = TablaDeSimbolos.agregarSimbolo(refIdentificador + cargarAmbito(), -1, TablaDeSimbolos.getContexto(refIdentificador).getRef());
  Contexto conIdentificador = TablaDeSimbolos.getContexto(newRef);
  if (conIdentificador.getTipo() == -1){
    if (conIdentificador.getUso().equals("") ){
      conIdentificador.setTipo(TablaDeSimbolos.getContexto(refTipo).getTipo());
    conIdentificador.setUso("nombre de parametro");
      //FALTA Límite inferior o Límite superior
    }else{
      erroresSemanticos.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SEMANTICO identificador ya posee otro uso"));
    }
  }else{
    erroresSemanticos.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SEMANTICO " + refIdentificador + " ya declarada"));
  };
}
private boolean chequearDeclarado(String id){
  String ambito = cargarAmbito();
  ambito = id + ambito;
  System.out.println("-------------------------------------IDENTIFICARDOR A BUSCAR: " + ambito);
  while(ambito.lastIndexOf(":") != -1){
    if (TablaDeSimbolos.getContexto(ambito) != null){
      if (TablaDeSimbolos.getContexto(ambito).getDeclarado()){    //CON UNA VARIABLE BASE (SIN AMBITO) EN LA T.S. ESTE IF PUEDE NO ESTAR. SI AGREGAMOS A LA T.S. (DESDE EL LEXER) CON AMBITO ENTONCES NECESITAMOS DEL ATRIBUTO "DECLARADO"
        return true;
      }
    }
    int ultAmbito = ambito.lastIndexOf(":");
    ambito = ambito.substring(0, ultAmbito);
  }
  erroresSemanticos.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SEMANTICO " + id + " nunca fue declarado")); 
  return false;
}
private String agregarTerceto(String operador1, String op2, String op3){
  Terceto terceto = new Terceto(operador1, op2, op3);
  tercetos.add(terceto);

  return "^" + (tercetos.size()-1);
}
private String agregaListaExpresionTercetos(String operador1, ArrayList<String> op2, ArrayList<String> op3){
  
  Stack<String> aux = new Stack<String>();
  for(int i = 0; i < op2.size(); i++){
    aux.add(agregarTerceto(operador1, op2.get(i), op3.get(i)));
  };

  String comp = aux.pop();
  while(aux.size() > 0){
    comp = agregarTerceto("AND", comp, aux.pop());
  }
  return comp;
}
public int conversionIndexStoI(String aux){
  return Integer.parseInt(aux.substring(1,aux.length()));
}
public void completaTercetosEtiqueta(String etiqueta, int index){
  for(Terceto terceto: tercetos){
    if (terceto.getT3().equals(etiqueta)){
      terceto.setT3(((Integer)index).toString());
    }
  }
}
public void completarUltimoTercetoIncompleto(){
  if (tercetosIncompletos.size() > 0){
    String aux = tercetosIncompletos.pop();
    tercetos.get(conversionIndexStoI(aux)).setT3(((Integer)tercetos.size()).toString());
  }else{
    System.out.println("SE INTENTO COMPLETAR UN TERCETO PERO NO HABIA NADA EN LA PILA");
  }
  agregarTerceto("ETIQUETA","",";etiqueta"+ (tercetos.size()));
}
//#line 1065 "Parser.java"
//###############################################################
// method: yylexdebug : check lexer state
//###############################################################
void yylexdebug(int state,int ch)
{
String s=null;
  if (ch < 0) ch=0;
  if (ch <= YYMAXTOKEN) //check index bounds
     s = yyname[ch];    //now get it
  if (s==null)
    s = "illegal-symbol";
  debug("state "+state+", reading "+ch+" ("+s+")");
}





//The following are now global, to aid in error reporting
int yyn;       //next next thing to do
int yym;       //
int yystate;   //current parsing state from state table
String yys;    //current token string


//###############################################################
// method: yyparse : parse input and execute indicated items
//###############################################################
int yyparse()
{
boolean doaction;
  init_stacks();
  yynerrs = 0;
  yyerrflag = 0;
  yychar = -1;          //impossible char forces a read
  yystate=0;            //initial state
  state_push(yystate);  //save it
  val_push(yylval);     //save empty value
  while (true) //until parsing is done, either correctly, or w/error
    {
    doaction=true;
    if (yydebug) debug("loop"); 
    //#### NEXT ACTION (from reduction table)
    for (yyn=yydefred[yystate];yyn==0;yyn=yydefred[yystate])
      {
      if (yydebug) debug("yyn:"+yyn+"  state:"+yystate+"  yychar:"+yychar);
      if (yychar < 0)      //we want a char?
        {
        yychar = yylex();  //get next token
        if (yydebug) debug(" next yychar:"+yychar);
        //#### ERROR CHECK ####
        if (yychar < 0)    //it it didn't work/error
          {
          yychar = 0;      //change it to default string (no -1!)
          if (yydebug)
            yylexdebug(yystate,yychar);
          }
        }//yychar<0
      yyn = yysindex[yystate];  //get amount to shift by (shift index)
      if ((yyn != 0) && (yyn += yychar) >= 0 &&
          yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
        {
        if (yydebug)
          debug("state "+yystate+", shifting to state "+yytable[yyn]);
        //#### NEXT STATE ####
        yystate = yytable[yyn];//we are in a new state
        state_push(yystate);   //save it
        val_push(yylval);      //push our lval as the input for next rule
        yychar = -1;           //since we have 'eaten' a token, say we need another
        if (yyerrflag > 0)     //have we recovered an error?
           --yyerrflag;        //give ourselves credit
        doaction=false;        //but don't process yet
        break;   //quit the yyn=0 loop
        }

    yyn = yyrindex[yystate];  //reduce
    if ((yyn !=0 ) && (yyn += yychar) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
      {   //we reduced!
      if (yydebug) debug("reduce");
      yyn = yytable[yyn];
      doaction=true; //get ready to execute
      break;         //drop down to actions
      }
    else //ERROR RECOVERY
      {
      if (yyerrflag==0)
        {
        yyerror("syntax error");
        yynerrs++;
        }
      if (yyerrflag < 3) //low error count?
        {
        yyerrflag = 3;
        while (true)   //do until break
          {
          if (stateptr<0)   //check for under & overflow here
            {
            yyerror("stack underflow. aborting...");  //note lower case 's'
            return 1;
            }
          yyn = yysindex[state_peek(0)];
          if ((yyn != 0) && (yyn += YYERRCODE) >= 0 &&
                    yyn <= YYTABLESIZE && yycheck[yyn] == YYERRCODE)
            {
            if (yydebug)
              debug("state "+state_peek(0)+", error recovery shifting to state "+yytable[yyn]+" ");
            yystate = yytable[yyn];
            state_push(yystate);
            val_push(yylval);
            doaction=false;
            break;
            }
          else
            {
            if (yydebug)
              debug("error recovery discarding state "+state_peek(0)+" ");
            if (stateptr<0)   //check for under & overflow here
              {
              yyerror("Stack underflow. aborting...");  //capital 'S'
              return 1;
              }
            state_pop();
            val_pop();
            }
          }
        }
      else            //discard this token
        {
        if (yychar == 0)
          return 1; //yyabort
        if (yydebug)
          {
          yys = null;
          if (yychar <= YYMAXTOKEN) yys = yyname[yychar];
          if (yys == null) yys = "illegal-symbol";
          debug("state "+yystate+", error recovery discards token "+yychar+" ("+yys+")");
          }
        yychar = -1;  //read another
        }
      }//end error recovery
    }//yyn=0 loop
    if (!doaction)   //any reason not to proceed?
      continue;      //skip action
    yym = yylen[yyn];          //get count of terminals on rhs
    if (yydebug)
      debug("state "+yystate+", reducing "+yym+" by rule "+yyn+" ("+yyrule[yyn]+")");
    if (yym>0)                 //if count of rhs not 'nil'
      yyval = val_peek(yym-1); //get current semantic value
    yyval = dup_yyval(yyval); //duplicate yyval if ParserVal is used as semantic value
    switch(yyn)
      {
//########## USER-SUPPLIED ACTIONS ##########
case 2:
//#line 36 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta 'cuerpo'."));}
break;
case 3:
//#line 37 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta 'END al final del programa'."));}
break;
case 4:
//#line 38 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta BEGIN del programa."));}
break;
case 5:
//#line 39 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta 'nombre del programa'."));}
break;
case 6:
//#line 40 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO delimitadores de programa."));}
break;
case 12:
//#line 51 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta ';'."));}
break;
case 13:
//#line 52 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta sentencia."));}
break;
case 14:
//#line 53 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO no se puede retornar en el cuerpo del programa."));}
break;
case 15:
//#line 57 "gramatica.y"
{ArrayList<String> referenciasIden = new ArrayList<String>(); referenciasIden.add(val_peek(1).sval); declararVariable(val_peek(2).sval, referenciasIden); estructuras.add("Linea "+ TablaDeSimbolos.getContexto(val_peek(2).sval).popRef() +": "+"Declaracion"); TablaDeSimbolos.getContexto(val_peek(1).sval + cargarAmbito()).setDeclarado();}
break;
case 16:
//#line 58 "gramatica.y"
{Contexto contexto = TablaDeSimbolos.getContexto(val_peek(4).sval);estructuras.add("Linea "+ contexto.popRef() +": "+"Declaracion");}
break;
case 17:
//#line 59 "gramatica.y"
{Contexto contexto = TablaDeSimbolos.getContexto(val_peek(4).sval);erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera IDENTIFICADOR.")); estructuras.add("Linea "+ contexto.popRef() +": "+"Declaracion");}
break;
case 18:
//#line 60 "gramatica.y"
{Contexto contexto = TablaDeSimbolos.getContexto(val_peek(5).sval);erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta ','.")); estructuras.add("Linea "+ contexto.popRef() +": "+"Declaracion");}
break;
case 19:
//#line 61 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta ','.")); }
break;
case 21:
//#line 63 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera ';'.")); }
break;
case 22:
//#line 64 "gramatica.y"
{estructuras.add("Declaracion de funcion");}
break;
case 23:
//#line 65 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera 'tipo de dato antes de declaracion de funcion'.")); }
break;
case 24:
//#line 70 "gramatica.y"
{Integer lastRef = TablaDeSimbolos.getContexto(val_peek(1).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"Declaracion de subtipo");}
break;
case 25:
//#line 71 "gramatica.y"
{Integer lastRef = TablaDeSimbolos.getContexto(val_peek(1).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"Declaracion de triple");}
break;
case 26:
//#line 72 "gramatica.y"
{ erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera 'TYPEDEF antes de la declaracion del subtipo'.")); estructuras.add("Linea "+ ": "+"Declaracion de subtipo");}
break;
case 27:
//#line 73 "gramatica.y"
{ erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera 'TYPEDEF antes de la declaracion del triple'.")); estructuras.add("Linea "+ ": "+"Declaracion de triple");}
break;
case 28:
//#line 74 "gramatica.y"
{ erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera 'declaracion de subtipo o triple'.")); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(1).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"Declaracion de subtipo"); }
break;
case 29:
//#line 77 "gramatica.y"
{declaracionSubtipo(val_peek(3).sval,val_peek(5).sval);}
break;
case 30:
//#line 78 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera 'nombre del tipo de dato nuevo'."));}
break;
case 31:
//#line 79 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera declaracion de subrangos."));}
break;
case 32:
//#line 81 "gramatica.y"
{declaracionTriple(val_peek(2).sval,val_peek(0).sval);}
break;
case 34:
//#line 83 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera 'tipo de objeto a declarar'."));}
break;
case 35:
//#line 84 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera 'nombre de objeto a declarar'."));}
break;
case 36:
//#line 85 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera 'nombre de objeto a declarar'."));}
break;
case 37:
//#line 86 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera 'nombre y tipo de objeto a declarar'."));}
break;
case 38:
//#line 87 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta '<' al inicio del identificador'."));}
break;
case 39:
//#line 88 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta '>' al final del identificador'."));}
break;
case 40:
//#line 89 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO faltan '<>'."));}
break;
case 41:
//#line 90 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO faltan '<>'."));}
break;
case 42:
//#line 93 "gramatica.y"
{if (val_peek(0).sval.equals("false"))erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta return en el cuerpo de la funcion.")); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(6).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"Declaracion de funcion"); ambitos.remove(ambitos.size()-1);}
break;
case 43:
//#line 94 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta nombre de la funcion."));  Integer lastRef = TablaDeSimbolos.getContexto(val_peek(7).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"Declaracion de funcion");}
break;
case 44:
//#line 95 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO  no puede tener mas de un parametro."));  Integer lastRef = TablaDeSimbolos.getContexto(val_peek(8).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"Declaracion de funcion");}
break;
case 45:
//#line 96 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta cuerpo con retorno."));  Integer lastRef = TablaDeSimbolos.getContexto(val_peek(6).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"Declaracion de funcion");}
break;
case 46:
//#line 99 "gramatica.y"
{declararFuncion(val_peek(0).sval); ambitos.add(val_peek(0).sval);}
break;
case 51:
//#line 107 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta identificador despues de la coma."));}
break;
case 55:
//#line 116 "gramatica.y"
{ Contexto contexto = TablaDeSimbolos.getContexto(val_peek(0).sval);
                        chequearRango(contexto);                             /*SOLO SE CHEQUEA EN POSITIVO YA QUE EL MAXIMO DE NEGATIVOS ES MAYOR AL MAXIMO DE POSITIVOS Y YA LO CHEQUEA EL PARSER*/
                      }
break;
case 56:
//#line 119 "gramatica.y"
{
                            Contexto contexto = TablaDeSimbolos.getContexto(val_peek(0).sval);
                            String newLexRef = TablaDeSimbolos.agregarSimbolo("-"+val_peek(0).sval, contexto.getTipo(), "-"+val_peek(0).sval, AnalizadorLexico.getNumeroLinea());
                          }
break;
case 57:
//#line 126 "gramatica.y"
{declaracionParametro(val_peek(1).sval, val_peek(0).sval);}
break;
case 58:
//#line 127 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta tipo de dato en el parametro."));}
break;
case 59:
//#line 128 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta nombre en el parametro."));}
break;
case 60:
//#line 132 "gramatica.y"
{ if (val_peek(1).sval.equals("true") || val_peek(0).sval.equals("true")) yyval.sval = "true"; else yyval.sval = "false";}
break;
case 61:
//#line 133 "gramatica.y"
{yyval.sval = val_peek(0).sval;}
break;
case 62:
//#line 136 "gramatica.y"
{yyval.sval = "false";}
break;
case 63:
//#line 137 "gramatica.y"
{yyval.sval = val_peek(1).sval;}
break;
case 64:
//#line 138 "gramatica.y"
{yyval.sval = "false";}
break;
case 65:
//#line 139 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta ';'." )); yyval.sval = val_peek(0).sval;}
break;
case 66:
//#line 143 "gramatica.y"
{agregarTerceto("RET", val_peek(1).sval, ""); estructuras.add("Retorno"); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(3).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"Sentencia de Retorno");}
break;
case 72:
//#line 153 "gramatica.y"
{yyval.sval = "false";}
break;
case 73:
//#line 154 "gramatica.y"
{yyval.sval = "false";}
break;
case 74:
//#line 155 "gramatica.y"
{yyval.sval = "false";}
break;
case 75:
//#line 156 "gramatica.y"
{yyval.sval = "false";}
break;
case 76:
//#line 157 "gramatica.y"
{yyval.sval = "true";}
break;
case 77:
//#line 158 "gramatica.y"
{yyval.sval = val_peek(0).sval;}
break;
case 78:
//#line 161 "gramatica.y"
{chequearDeclarado(val_peek(2).sval); yyval.sval = agregarTerceto(":=", val_peek(2).sval, val_peek(0).sval);Integer lastRef = TablaDeSimbolos.getContexto(val_peek(2).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"Sentencia de Asignacion");}
break;
case 79:
//#line 162 "gramatica.y"
{ if (!val_peek(2).sval.equals("[1]") && !val_peek(2).sval.equals("[2]") && !val_peek(2).sval.equals("[3]")) erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO rango invalido, se espera entre 1 y 3."));Integer lastRef = TablaDeSimbolos.getContexto(val_peek(3).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"Sentencia de Asignacion");}
break;
case 80:
//#line 163 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO faltan '[]' en el rango")); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(3).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"Sentencia de Asignacion");}
break;
case 81:
//#line 166 "gramatica.y"
{yyval.sval = agregarTerceto("+", val_peek(2).sval, val_peek(0).sval);}
break;
case 82:
//#line 167 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta operando en la expresion"));}
break;
case 83:
//#line 168 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta operando en la expresion"));}
break;
case 84:
//#line 169 "gramatica.y"
{yyval.sval = agregarTerceto("-", val_peek(2).sval, val_peek(0).sval);}
break;
case 85:
//#line 170 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta operando en la expresion"));}
break;
case 86:
//#line 171 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta operando en la expresion"));}
break;
case 88:
//#line 175 "gramatica.y"
{yyval.sval = agregarTerceto("*", val_peek(2).sval, val_peek(0).sval);}
break;
case 89:
//#line 176 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta operando en la expresion"));}
break;
case 90:
//#line 177 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta operando en la expresion"));}
break;
case 91:
//#line 178 "gramatica.y"
{yyval.sval = agregarTerceto("/", val_peek(2).sval, val_peek(0).sval);}
break;
case 92:
//#line 179 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta operando en la expresion"));}
break;
case 93:
//#line 180 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta operando en la expresion"));}
break;
case 95:
//#line 184 "gramatica.y"
{yyval.sval = val_peek(0).sval; chequearDeclarado(val_peek(0).sval);}
break;
case 102:
//#line 193 "gramatica.y"
{ chequearDeclarado(val_peek(3).sval); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(3).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"Invocacion de Funcion");}
break;
case 103:
//#line 194 "gramatica.y"
{ chequearDeclarado(val_peek(4).sval); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(4).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"Invocacion de Funcion");}
break;
case 104:
//#line 195 "gramatica.y"
{ chequearDeclarado(val_peek(6).sval); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(6).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"Invocacion de Funcion");}
break;
case 105:
//#line 196 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO numero de expresiones invalido en el llamado a funcion.")); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(5).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"Invocacion de Funcion");}
break;
case 106:
//#line 197 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta expresion en el llamado a funcion.")); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(3).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"Invocacion de Funcion");}
break;
case 107:
//#line 198 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO numero de expresiones invalido en el llamado a funcion.")); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(6).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"Invocacion de Funcion");}
break;
case 108:
//#line 199 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO numero de expresion esinvalido en el llamado a funcion.")); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(8).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"Invocacion de Funcion");}
break;
case 109:
//#line 202 "gramatica.y"
{completarUltimoTercetoIncompleto();  Integer lastRef = TablaDeSimbolos.getContexto(val_peek(2).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"IF");}
break;
case 110:
//#line 203 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera una condicion valida")); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(5).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"IF");}
break;
case 111:
//#line 204 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera una condicion valida")); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(7).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"IF");;}
break;
case 112:
//#line 205 "gramatica.y"
{completarUltimoTercetoIncompleto(); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(4).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"IF");}
break;
case 113:
//#line 206 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta 'end_if;'.")); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(2).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"IF");}
break;
case 114:
//#line 207 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta 'end_if;'.")); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(4).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"IF");}
break;
case 115:
//#line 208 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta ')' al final de la condicion.")); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(6).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"IF");}
break;
case 116:
//#line 209 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta ')' al final de la condicion.")); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(4).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"IF");}
break;
case 117:
//#line 210 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta '(' al comienzo de la condicion.")); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(6).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"IF");}
break;
case 118:
//#line 211 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta '(' al comienzo de la condicion.")); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(4).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"IF");}
break;
case 119:
//#line 212 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO faltan '()' en la condicion.")); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(5).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"IF");}
break;
case 120:
//#line 213 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO faltan '()' en la condicion.")); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(3).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"IF");}
break;
case 121:
//#line 214 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta bloque de sentencias ejecutables valido.")); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(3).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"IF");}
break;
case 122:
//#line 215 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta bloque de sentencias ejecutables valido .")); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(4).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"IF");}
break;
case 123:
//#line 216 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta bloque de sentencias ejecutables valido .")); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(5).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"IF");}
break;
case 124:
//#line 217 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO faltan los dos bloques de sentencias ejecutables validas.")); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(5).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"IF");}
break;
case 125:
//#line 220 "gramatica.y"
{tercetosIncompletos.add(agregarTerceto("BF", val_peek(1).sval, ""));}
break;
case 126:
//#line 223 "gramatica.y"
{String incompleto = agregarTerceto("BI", "", ""); completarUltimoTercetoIncompleto(); tercetosIncompletos.add(incompleto);}
break;
case 127:
//#line 226 "gramatica.y"
{yyval.sval = "false"; Integer lastRef = TablaDeSimbolos.getContexto(val_peek(6).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"IF");}
break;
case 128:
//#line 227 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera una condicion valida")); yyval.sval = "false";  Integer lastRef = TablaDeSimbolos.getContexto(val_peek(6).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"IF");}
break;
case 129:
//#line 228 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera una condicion valida"));if (val_peek(3).sval.equals("true") && val_peek(1).sval.equals("true")) yyval.sval = "true"; else yyval.sval = "false"; Integer lastRef = TablaDeSimbolos.getContexto(val_peek(8).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"IF");}
break;
case 130:
//#line 229 "gramatica.y"
{ if (val_peek(3).sval.equals("true") && val_peek(1).sval.equals("true")) yyval.sval = "true"; else yyval.sval = "false"; Integer lastRef = TablaDeSimbolos.getContexto(val_peek(8).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"IF");}
break;
case 131:
//#line 230 "gramatica.y"
{yyval.sval = "false";erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta 'end_if;'.")); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(6).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"IF");}
break;
case 132:
//#line 231 "gramatica.y"
{if (val_peek(3).sval.equals("true") && val_peek(1).sval.equals("true")) yyval.sval = "true"; else yyval.sval = "false";erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta 'end_if;'.")); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(8).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"IF");}
break;
case 133:
//#line 232 "gramatica.y"
{if (val_peek(3).sval.equals("true") && val_peek(1).sval.equals("true")) yyval.sval = "true"; else yyval.sval = "false";erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta ')' al final de la condicion.")); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(7).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"IF");}
break;
case 134:
//#line 233 "gramatica.y"
{yyval.sval = "false";erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta ')' al final de la condicion.")); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(5).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"IF");}
break;
case 135:
//#line 234 "gramatica.y"
{if (val_peek(3).sval.equals("true") && val_peek(1).sval.equals("true")) yyval.sval = "true"; else yyval.sval = "false";erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta '(' al comienzo de la condicion.")); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(7).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"IF");}
break;
case 136:
//#line 235 "gramatica.y"
{yyval.sval = "false";erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta '(' al comienzo de la condicion.")); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(5).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"IF");}
break;
case 137:
//#line 236 "gramatica.y"
{if (val_peek(3).sval.equals("true") && val_peek(1).sval.equals("true")) yyval.sval = "true"; else yyval.sval = "false";erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO faltan '()' en la condicion.")); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(6).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"IF");}
break;
case 138:
//#line 237 "gramatica.y"
{yyval.sval = "false";erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO faltan '()' en la condicion.")); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(4).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"IF");}
break;
case 139:
//#line 238 "gramatica.y"
{yyval.sval = "false";erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta bloque de sentencias ejecutables valido.")); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(6).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"IF");}
break;
case 140:
//#line 239 "gramatica.y"
{yyval.sval = "false";erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta bloque de sentencias ejecutables valido .")); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(8).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"IF");}
break;
case 141:
//#line 240 "gramatica.y"
{yyval.sval = "false";erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta bloque de sentencias ejecutables valido .")); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(8).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"IF");}
break;
case 142:
//#line 241 "gramatica.y"
{yyval.sval = "false";erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO faltan los dos bloques de sentencias ejecutables validas.")); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(8).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"IF");}
break;
case 143:
//#line 244 "gramatica.y"
{((ArrayList<String>)val_peek(7).obj).add(val_peek(9).sval); ((ArrayList<String>)val_peek(1).obj).add(val_peek(3).sval); yyval.sval = agregaListaExpresionTercetos(val_peek(5).sval, ((ArrayList<String>)val_peek(7).obj), ((ArrayList<String>)val_peek(1).obj));}
break;
case 144:
//#line 245 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO faltan '( )' a las listas de expresiones."));}
break;
case 145:
//#line 246 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO faltan parentesis en las listas de expresiones."));}
break;
case 146:
//#line 247 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO faltan parentesis en las listas de expresiones."));}
break;
case 147:
//#line 248 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO faltan parentesis en las listas de expresiones."));}
break;
case 148:
//#line 249 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO faltan parentesis en las listas de expresiones."));}
break;
case 149:
//#line 250 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO faltan parentesis en las listas de expresiones."));}
break;
case 150:
//#line 251 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera un comparador."));}
break;
case 151:
//#line 252 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO faltan parentesis en las listas de expresiones."));}
break;
case 152:
//#line 254 "gramatica.y"
{yyval.sval = agregarTerceto(val_peek(1).sval, val_peek(2).sval, val_peek(0).sval);}
break;
case 153:
//#line 255 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera una expresion a la izquierda del comparador."));}
break;
case 154:
//#line 256 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera una expresion a la derecha del comparador."));}
break;
case 155:
//#line 257 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera un comparador."));}
break;
case 156:
//#line 260 "gramatica.y"
{((ArrayList<String>)val_peek(2).obj).add(val_peek(0).sval);}
break;
case 157:
//#line 261 "gramatica.y"
{ArrayList<String> aux = new ArrayList<String>(); aux.add(val_peek(0).sval); yyval.obj = aux;}
break;
case 158:
//#line 262 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera una expresion despues de la coma."));}
break;
case 159:
//#line 265 "gramatica.y"
{yyval.sval = "<";}
break;
case 160:
//#line 266 "gramatica.y"
{yyval.sval = ">";}
break;
case 161:
//#line 267 "gramatica.y"
{yyval.sval = "=";}
break;
case 162:
//#line 268 "gramatica.y"
{yyval.sval = TablaTipoToken.DISTINTO;}
break;
case 163:
//#line 269 "gramatica.y"
{yyval.sval = TablaTipoToken.MENOR_IGUAL;}
break;
case 164:
//#line 270 "gramatica.y"
{yyval.sval = TablaTipoToken.MAYOR_IGUAL;}
break;
case 167:
//#line 275 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta ';'."));}
break;
case 169:
//#line 280 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta ';'."));}
break;
case 171:
//#line 284 "gramatica.y"
{ if (val_peek(2).sval.equals("true") || val_peek(1).sval.equals("true")) yyval.sval = "true"; else yyval.sval = "false";}
break;
case 172:
//#line 285 "gramatica.y"
{yyval.sval = val_peek(1).sval;}
break;
case 173:
//#line 288 "gramatica.y"
{yyval.sval = val_peek(1).sval;}
break;
case 174:
//#line 289 "gramatica.y"
{yyval.sval = val_peek(1).sval; erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta ';'."));}
break;
case 175:
//#line 290 "gramatica.y"
{yyval.sval = val_peek(1).sval;}
break;
case 176:
//#line 294 "gramatica.y"
{agregarTerceto("BF", val_peek(1).sval, ((Integer)(tercetos.size()+2)).toString());agregarTerceto("BI", "", (inicioBucle.pop().toString()));Integer lastRef = TablaDeSimbolos.getContexto(val_peek(5).sval).popRef(); agregarTerceto("ETIQUETA", "", ";etiqueta"+ (tercetos.size())); estructuras.add("Linea "+lastRef.toString() +": "+"Bucle");}
break;
case 177:
//#line 295 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta bloque de sentencias ejecutables.")); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(5).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"Bucle");}
break;
case 178:
//#line 296 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta ( de apertura de condicion.")); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(5).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"Bucle");}
break;
case 179:
//#line 297 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta condicion.")); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(5).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"Bucle");}
break;
case 180:
//#line 298 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta ) de cierre de condicion.")); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(4).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"Bucle");}
break;
case 181:
//#line 299 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta ( ) englobando la condicion.")); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(5).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"Bucle");}
break;
case 182:
//#line 300 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta ( ) englobando la condicion y bloque de sentencias ejecutables.")); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(5).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"Bucle");}
break;
case 183:
//#line 301 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta condicion y bloque de sentencias ejecutables.")); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(5).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"Bucle");}
break;
case 184:
//#line 303 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta while")); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(2).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"Bucle");}
break;
case 185:
//#line 306 "gramatica.y"
{inicioBucle.add(tercetos.size()); agregarTerceto("ETIQUETA", "", ";etiqueta"+ (tercetos.size()));}
break;
case 186:
//#line 309 "gramatica.y"
{agregarTerceto("BI", "", val_peek(1).sval);Integer lastRef = TablaDeSimbolos.getContexto(val_peek(2).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"GOTO");}
break;
case 187:
//#line 310 "gramatica.y"
{erroresSintactico.add(new Error(numeroLineaError, Tipo.ERROR, "ERROR SINTACTICO falta '@' luego de los dospuntos.")); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(2).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"GOTO");}
break;
case 188:
//#line 311 "gramatica.y"
{erroresSintactico.add(new Error(numeroLineaError, Tipo.ERROR, "ERROR SINTACTICO etiqueta invalida.")); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(2).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"GOTO");}
break;
case 189:
//#line 312 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta 'goto' luego de los dospuntos.")); estructuras.add("Linea "+": "+"GOTO");}
break;
case 190:
//#line 315 "gramatica.y"
{agregarTerceto("ETIQUETA", val_peek(1).sval, ""); completaTercetosEtiqueta(val_peek(1).sval, tercetos.size());declaracionEtiqueta(val_peek(1).sval);Integer lastRef = TablaDeSimbolos.getContexto(val_peek(1).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"Etiqueta"); yyval.sval = lastRef.toString();}
break;
case 191:
//#line 316 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta 'etiqueta'."));}
break;
case 192:
//#line 320 "gramatica.y"
{agregarTerceto("OUTF", val_peek(1).sval, ""); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(3).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"Mensaje de salida");}
break;
case 193:
//#line 321 "gramatica.y"
{agregarTerceto("OUTF", val_peek(1).sval, ""); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(3).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"Mensaje de salida");}
break;
case 194:
//#line 322 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera cadena multilinea o expresion en el mensaje de salida.")); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(3).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"Mensaje de salida");}
break;
//#line 1867 "Parser.java"
//########## END OF USER-SUPPLIED ACTIONS ##########
    }//switch
    //#### Now let's reduce... ####
    if (yydebug) debug("reduce");
    state_drop(yym);             //we just reduced yylen states
    yystate = state_peek(0);     //get new state
    val_drop(yym);               //corresponding value drop
    yym = yylhs[yyn];            //select next TERMINAL(on lhs)
    if (yystate == 0 && yym == 0)//done? 'rest' state and at first TERMINAL
      {
      if (yydebug) debug("After reduction, shifting from state 0 to state "+YYFINAL+"");
      yystate = YYFINAL;         //explicitly say we're done
      state_push(YYFINAL);       //and save it
      val_push(yyval);           //also save the semantic value of parsing
      if (yychar < 0)            //we want another character?
        {
        yychar = yylex();        //get next character
        if (yychar<0) yychar=0;  //clean, if necessary
        if (yydebug)
          yylexdebug(yystate,yychar);
        }
      if (yychar == 0)          //Good exit (if lex returns 0 ;-)
         break;                 //quit the loop--all DONE
      }//if yystate
    else                        //else not done yet
      {                         //get next state and push, for next yydefred[]
      yyn = yygindex[yym];      //find out where to go
      if ((yyn != 0) && (yyn += yystate) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yystate)
        yystate = yytable[yyn]; //get new state
      else
        yystate = yydgoto[yym]; //else go to new defred
      if (yydebug) debug("after reduction, shifting from state "+state_peek(0)+" to state "+yystate+"");
      state_push(yystate);     //going again, so push state & val...
      val_push(yyval);         //for next action
      }
    }//main loop
  return 0;//yyaccept!!
}
//## end of method parse() ######################################



//## run() --- for Thread #######################################
/**
 * A default run method, used for operating this parser
 * object in the background.  It is intended for extending Thread
 * or implementing Runnable.  Turn off with -Jnorun .
 */
public void run()
{
  yyparse();
}
//## end of method run() ########################################



//## Constructors ###############################################
/**
 * Default constructor.  Turn off with -Jnoconstruct .

 */
public Parser()
{
  //nothing to do
}


/**
 * Create a parser, setting the debug to true or false.
 * @param debugMe true for debugging, false for no debug.
 */
public Parser(boolean debugMe)
{
  yydebug=debugMe;
}
//###############################################################



}
//################### END OF CLASS ##############################
