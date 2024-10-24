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

import java.util.HashMap;
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
//#line 32 "Parser.java"




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
   26,   26,   34,   33,   33,   33,   33,   33,   33,   33,
   33,   33,   33,   33,   33,   33,   36,   36,   36,   37,
   37,   37,   37,   37,   37,   38,   38,   38,   32,   32,
   32,   39,   39,   35,   35,   35,   23,   23,   23,   23,
   23,   23,   23,   23,   23,   40,   24,   24,   24,   24,
    5,    5,   25,   25,   25,
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
    4,    5,    6,    6,    4,    2,    3,    6,    8,    5,
    3,    5,    7,    5,    7,    5,    6,    4,    4,    5,
    6,    6,    2,   11,    7,    9,   11,   11,   11,   11,
   11,   11,    3,    3,    3,    3,    3,    1,    3,    1,
    1,    1,    1,    1,    1,    3,    2,    2,    2,    2,
    3,    3,    2,    2,    2,    3,    6,    6,    6,    6,
    5,    6,    6,    6,    3,    1,    3,    3,    3,    2,
    2,    1,    4,    4,    4,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,   49,    0,    0,
    0,    0,    0,    0,   47,  186,    0,   48,  192,    0,
    8,    9,    0,   11,    0,    0,    0,    0,   26,    0,
   67,   68,   69,   70,   71,    0,    0,    0,    2,    0,
    0,   13,   27,    0,    0,    0,  190,  191,    0,    0,
    0,   55,    0,    0,   96,    0,   94,    0,   97,    0,
    0,   28,    0,   24,   25,    0,   46,    0,    0,    0,
    5,    7,   12,   10,   14,    0,    0,   21,   20,   23,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    4,
    1,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  163,  164,  165,    0,    0,    0,    0,  160,  161,  162,
    0,   98,    0,   99,  100,  101,   56,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  189,  188,  187,    0,
   15,    0,   22,    0,    0,    0,    0,  126,  113,  109,
    0,    0,    0,    0,    0,  170,  169,  185,    0,   40,
    0,    0,    0,    0,   41,    0,    0,    0,    0,   54,
   83,   86,   90,   93,    0,    0,    0,    0,    0,    0,
    0,  125,    0,    0,   82,   81,   85,   84,    0,    0,
    0,    0,   89,   88,   92,   91,    0,  120,    0,  195,
  194,  193,    0,   66,   19,    0,   52,    0,   58,   59,
   57,    0,    0,  121,    0,    0,    0,    0,    0,  168,
  167,  171,    0,    0,    0,   38,    0,   39,    0,    0,
   31,    0,    0,   30,  106,    0,    0,    0,  102,    0,
    0,    0,  116,    0,    0,    0,  118,    0,    0,    0,
   17,    0,   16,    0,    0,    0,    0,    0,  122,  114,
  112,    0,    0,    0,  166,    0,    0,    0,   37,   34,
   36,   33,   35,   32,   29,   53,    0,    0,  103,    0,
    0,    0,    0,  110,    0,    0,    0,    0,    0,    0,
    0,  119,    0,   18,   51,   50,    0,    0,    0,   62,
   64,   76,    0,   61,    0,   72,   73,   74,   75,   77,
    0,  124,  123,  184,  183,  178,  182,  179,  180,  177,
    0,    0,  105,    0,    0,    0,    0,  115,    0,    0,
  117,    0,    0,    0,   45,    0,    0,   42,   60,   63,
    0,    0,    0,  104,  107,    0,    0,    0,    0,    0,
  111,    0,    0,   43,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  143,  131,  127,    0,    0,    0,    0,
    0,    0,    0,    0,   44,    0,    0,    0,  138,    0,
  139,    0,    0,    0,  175,  174,    0,    0,  108,    0,
    0,    0,    0,    0,  146,    0,  134,    0,  136,    0,
    0,    0,    0,  173,  176,    0,  140,  132,  130,    0,
    0,    0,    0,    0,  128,    0,    0,    0,  137,  142,
  141,  172,  147,  149,  151,  148,  150,  152,  144,    0,
  133,  135,  129,
};
final static short yydgoto[] = {                          3,
   20,   21,   22,   87,   24,  302,   26,  208,   27,   28,
   29,   43,  169,   30,  146,  303,   55,  304,  363,  189,
   31,   32,   33,   34,   35,  310,   57,   58,   59,   36,
   83,  148,   60,  342,  364,  190,  111,  155,  384,   37,
};
final static short yysindex[] = {                       -74,
 -124,  813,    0,  813,  627,  -42,  398,    0, -235,  -38,
   48,  -68,  -93,   96,    0,    0,   77,    0,    0,  652,
    0,    0,  -53,    0,   82, -231,   31,  132,    0,  164,
    0,    0,    0,    0,    0, -238, 1017,  675,    0,  697,
  174,    0,    0, -211,    3,   -2,    0,    0,  165,  545,
  102,    0,   14,  -35,    0,  551,    0,   45,    0,   -8,
   65,    0,   60,    0,    0,  301,    0,   -5,  300,  -61,
    0,    0,    0,    0,    0,   54,  307,    0,    0,    0,
  501, 1134, -117,   98,  342,  341,   57, -236,  -42,    0,
    0,  -47,  409,  114,   -5,  455,  259,   64,   -5,  -33,
    0,    0,    0,  -14,  -14,  -14,  -14,    0,    0,    0,
   -5,    0,  162,    0,    0,    0,    0,   -5,  451,    2,
   -5,   72,  113,   -5,  167,  172,  178,  907,  121, -232,
  634,  358,  412, -235,  501,  573,    0,    0,    0,  371,
    0,  232,    0,  179,  457,   24, -154,    0,    0,    0,
 1150,  -12,   -5,   67, 1135,    0,    0,    0,    7,    0,
  181,  392,  -44,  401,    0,   64,  -34,   64,   -3,    0,
    0,    0,    0,    0,   64, 1235,  -32,  405,  509,   -5,
  121,    0, -141,   64,    0,    0,    0,    0,   64,  443,
  455,   64,    0,    0,    0,    0, -128,    0,  907,    0,
    0,    0,  427,    0,    0,  416,    0,   62,    0,    0,
    0,  228,  229,    0, 1163,  233, -101,  -35,  -26,    0,
    0,    0,  442,  -35,  -24,    0,  479,    0,  497,  539,
    0,    8,  -33,    0,    0,   -5,  465,  251,    0,   -5,
   58,  -81,    0,  907,  184,  -18,    0,  907,  267,  253,
    0,  480,    0,  572,  483,  835,  282,  293,    0,    0,
    0,  312,   12,  529,    0,   34,  495,  543,    0,    0,
    0,    0,    0,    0,    0,    0,  677,  333,    0,  558,
   25,  399,  489,    0,  907,  338,  455,   64,   -5,  625,
  344,    0,  857,    0,    0,    0,  360, -179,  -16,    0,
    0,    0,  719,    0,  568,    0,    0,    0,    0,    0,
  366,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  291,  597,    0,   20,  604,   21,  380,    0,  639,   -5,
    0,  370,  743,  857,    0,  -35,    9,    0,    0,    0,
 1050,  -84,  395,    0,    0,   -5,   -5,   -5,   -5,   -5,
    0,   -5,  629,    0,  767,  502,   15,  973,  424,  -73,
   49, 1181,   69,    0,    0,    0, 1080,  650,  682,  721,
  726,  760,  770,  478,    0,  424,   99,  149,    0,  973,
    0, 1112,  637, 1166,    0,    0,  439,  -60,    0,   -5,
   -5,   -5,   -5,   -5,    0,  159,    0,  973,    0,  973,
  453,  481,  494,    0,    0,  658,    0,    0,    0,  500,
  517,  556,   26,  116,    0,  973,  510,  516,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  532,
    0,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  693,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   35,    0,    0,    0,    0,    0,    0,   86,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  698,    0,
    0,    0,    0,    0,    0,    0,    0,  472,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  523,    0,  577,    0,    0,
    0,    0,    0,    0,  258,    0,    0,    0,    0,    0,
    0,    0,    0,  309,    0,    0,    0,    0,  118,    0,
  363,  385,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  605,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  140,  209,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  791,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  432,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,
};
final static short yygindex[] = {                         0,
  836,   43,  418, 1116,  887,  782, 1131,    0,    0,  705,
  732,  777,  571,    0,  681, -263,  -63, -271,  844,  -10,
  829,    0,  852,  905,  961,    0,  704,    0,    0, 1005,
  -15,  -28,  -50, -203,  193,  758,  -55,    0,    0,    0,
};
final static int YYTABLESIZE=1457;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         56,
  125,   54,  139,  120,  118,   74,   53,  236,   88,   53,
   53,   53,   53,  118,  161,  118,   42,  229,   53,  158,
   53,  289,    8,  336,   82,   76,   53,  219,   53,  333,
   53,  339,  129,  198,   98,   15,  170,   13,  159,   53,
  233,   18,  182,  119,  130,  199,  225,   53,   95,  359,
  133,  233,  315,  106,  104,  182,  105,  136,  107,  347,
  350,  339,   72,  125,  213,  283,  427,  212,  245,  245,
  355,  108,  110,  109,  318,   95,   95,   95,   95,   95,
   72,   95,   72,  339,  166,  335,  126,   61,  168,   79,
  231,  127,   95,   95,   95,   95,   95,  142,  283,   41,
  175,  245,  178,  170,  183,  254,  122,  179,  123,   53,
  184,  214,  141,  197,  192,  157,   53,  108,  110,  109,
  253,  234,  217,  215,  243,  221,   87,  386,   87,   87,
   87,    4,  275,  360,  246,   68,  244,  247,  149,    5,
   75,  113,   98,   87,   87,   87,   87,   87,  150,  248,
  114,  115,  116,  377,  260,  378,  429,   53,  158,  245,
  151,  158,   66,   67,  261,  242,  237,  262,  264,  276,
  249,  365,  396,  266,  268,  158,  158,  158,  158,  158,
  159,  366,    1,  159,  284,  246,  258,   62,   63,    2,
   80,    9,  379,  367,  138,  408,  285,  159,  159,  159,
  159,  159,   73,   81,  380,  409,   53,   56,   56,  160,
   41,   53,  228,   56,   56,  286,   53,   50,   51,  291,
   50,   51,   53,   96,   51,  277,  324,  326,   53,  263,
   51,  267,   51,   93,  288,  290,   41,   96,   51,   50,
   51,   52,   51,  218,   52,   52,   52,   52,  337,  157,
   96,   51,  157,   52,  128,   52,  327,   99,   96,   51,
    8,   52,  224,   52,  128,   52,  157,  157,  157,  157,
  157,  358,  101,   15,   52,  346,  349,  358,  329,   18,
  282,  426,   52,  102,  103,  357,   78,  100,   56,  317,
   95,   95,   95,  117,   95,   95,   95,   95,  154,   95,
  125,   95,   95,   95,   95,   95,   95,   95,   95,  140,
   95,   95,  156,  282,  381,  154,  154,  252,  101,  134,
  131,   51,  220,  132,  385,  356,  382,  185,   51,  102,
  103,  344,   69,   70,  343,  369,  370,  371,  372,  373,
  135,   87,   87,   87,   52,   87,   87,   87,   87,  156,
   87,   52,   87,   87,   87,   87,   87,   87,   87,   87,
  112,   87,   87,  137,  397,  143,  156,  156,  187,   51,
  165,  428,  152,  158,  158,  158,  398,  158,  158,  158,
  158,  167,  158,  128,  158,  158,  158,  158,  158,  158,
  158,  158,   52,  158,  158,  159,  159,  159,  201,  159,
  159,  159,  159,  155,  159,   47,  159,  159,  159,  159,
  159,  159,  159,  159,  399,  159,  159,  176,   51,    8,
  155,  155,  191,   51,  415,  153,  400,  193,   51,  205,
   92,    8,   15,  195,   51,  209,  416,  226,   18,  287,
   51,   52,  153,  153,   15,  239,   52,  122,  238,  123,
   18,   52,  202,  227,  122,   48,  123,   52,  108,  110,
  109,   47,  230,   52,  157,  157,  157,  250,  157,  157,
  157,  157,  145,  157,  251,  157,  157,  157,  157,  157,
  157,  157,  157,  255,  157,  157,  245,  206,  207,  145,
  145,  181,  256,  122,  180,  123,  106,  104,  259,  105,
  265,  107,  108,  110,  109,  279,  280,  122,  278,  123,
  108,  110,  109,  154,  154,  154,  293,  154,  395,  154,
  154,  245,  154,  297,  154,  154,  154,  154,  154,   78,
   78,  154,  292,  154,  154,  319,  106,  104,  294,  105,
  423,  107,  376,  245,  122,  180,  123,  312,  108,  110,
  109,  122,  240,  123,  108,  110,  109,  424,  313,  388,
  245,  108,  110,  109,  156,  156,  156,  314,  156,  316,
  156,  156,  401,  156,  403,  156,  156,  156,  156,  156,
   79,   79,  156,  320,  156,  156,  106,  104,  322,  105,
  417,  107,  418,  122,  124,  123,  425,   85,  323,  245,
   44,  153,   10,  328,  108,  110,  109,   11,  430,  331,
  108,  110,  109,  204,   16,  122,   17,  123,  155,  155,
  155,   46,  155,  334,  155,  155,  340,  155,  341,  155,
  155,  155,  155,  155,   80,   80,  155,  345,  155,  155,
  153,  153,  153,  348,  153,  351,  153,  153,   41,  153,
  368,  153,  153,  153,  153,  153,   44,   45,  153,  101,
  153,  153,  181,  181,  162,  163,    8,  122,  330,  123,
  102,  103,  245,  300,  200,  106,  104,   46,  105,   15,
  107,  122,  352,  123,   19,   18,  358,  145,  145,  145,
  389,  145,    3,  145,  145,  404,  145,    6,  145,  145,
  145,  145,  145,  101,  407,  145,  121,  145,  145,   19,
  300,  101,  210,  211,  102,  103,  422,  321,  419,  122,
  300,  123,  102,  103,  122,  390,  123,   78,   78,   78,
   77,   78,   19,   78,  269,  270,   78,  232,   78,   78,
   78,   78,   78,   64,  325,   78,  420,   78,   78,  101,
  300,  300,  271,  272,   19,  101,  144,  121,    8,  421,
  102,  103,  101,  122,  391,  123,  102,  103,  122,  392,
  123,   15,  300,  102,  103,  431,   19,   18,   79,   79,
   79,  432,   79,   25,   79,   25,   25,   79,   65,   79,
   79,   79,   79,   79,  273,  274,   79,  433,   79,   79,
   19,   25,  122,  393,  123,  101,  121,  171,  172,  173,
  174,  101,  122,  394,  123,  203,  102,  103,    0,   25,
    0,   25,  102,  103,   19,  186,  188,  295,  296,  194,
  196,    0,   80,   80,   80,    0,   80,    0,   80,   38,
   40,   80,    0,   80,   80,   80,   80,   80,   65,    0,
   80,    0,   80,   80,    0,    0,    0,    0,    0,    0,
  181,  181,  181,    0,  181,    0,  181,    0,    0,  181,
   19,  181,  181,  181,  181,  181,    0,    0,  181,    0,
  181,  181,    6,    7,    8,    0,    9,    0,   10,    0,
    0,   39,   19,   11,   12,   13,   14,   15,    0,    0,
   16,    0,   17,   18,    0,    0,    0,    6,    7,    8,
    0,    9,    0,   10,   19,    0,   71,    0,   11,   12,
   13,   14,   15,    0,    0,   16,    0,   17,   18,    0,
   89,    7,    8,    0,    9,    0,   10,  241,    0,   90,
    0,   11,   12,   13,   14,   15,    0,    0,   16,    0,
   17,   18,    6,    7,    8,    0,    9,    0,   10,    0,
    0,   91,    0,   11,   12,   13,   14,   15,    0,    0,
   16,    0,   17,   18,  332,    7,    8,    0,    9,    0,
  299,    0,    0,  338,    0,   11,   12,   13,   14,   15,
    0,    0,   16,    0,   17,   18,    0,  281,  332,    7,
    8,    0,    9,    0,  299,    0,    0,  354,    0,   11,
   12,   13,   14,   15,    0,    0,   16,    0,   17,   18,
    0,    0,  332,    7,    8,    0,    9,    0,  299,    0,
    0,  375,    0,   11,   12,   13,   14,   15,    0,    0,
   16,    0,   17,   18,    0,    0,   65,   65,   65,    0,
   65,    0,   65,    0,    0,   65,    0,   65,   65,   65,
   65,   65,    0,    0,   65,    0,   65,   65,    6,    7,
    8,    0,    9,    0,   10,    0,    0,    0,    0,   11,
   12,   13,   14,   15,  306,    0,   16,  353,   17,   18,
  298,    7,    8,    0,    9,    0,  299,    0,    0,  305,
    0,   11,   12,   13,   14,   15,    0,  307,   16,  374,
   17,   18,  332,    7,    8,    0,    9,   23,  299,   23,
   23,  306,    0,   11,   12,   13,   14,   15,    0,    0,
   16,  306,   17,   18,    0,   23,  305,    0,    0,   49,
    0,    0,  301,    0,  307,    0,  305,  410,  411,  412,
  413,  414,    0,   23,  307,   23,    0,    0,    0,    0,
  308,  306,  306,   85,    0,    0,    0,    0,   10,  306,
   86,   94,    0,   11,    0,   97,  305,  305,    0,  301,
   16,    0,   17,  306,  307,  307,  306,    0,    0,  301,
  306,    0,  307,    0,    0,  306,    0,  308,  305,    0,
    0,  154,    0,    0,    0,  383,  307,  308,  306,  307,
  306,  145,  306,  307,    0,    0,  309,    0,  307,  301,
  301,    0,    0,  164,    0,    0,  306,  406,  306,   85,
    0,  307,    0,  307,  299,  307,  362,  308,  308,   11,
    0,  301,   14,  177,  306,  308,   16,    0,   17,  307,
    0,  307,    0,  309,    0,    0,    0,    0,    0,  308,
  311,    0,  308,  309,   97,  145,  308,  307,    0,    0,
  223,  308,   84,   85,    0,  235,  106,  104,   10,  105,
   86,  107,    0,   11,  308,    0,  308,    0,  308,    0,
   16,    0,   17,  309,  309,    0,    0,  311,    0,    0,
    0,  309,  308,    0,  308,  361,   85,  311,    0,    0,
    0,  299,    0,  362,    0,  309,   11,    0,  309,   14,
  308,    0,  309,   16,    0,   17,    0,  309,    0,    0,
    0,    0,    0,    0,    0,  387,   85,  311,  311,    0,
  309,  299,  309,  362,  309,  311,   11,    0,    0,   14,
    0,    0,    0,   16,    0,   17,    0,    0,  309,  311,
  309,    0,  311,    0,    0,    0,  311,  402,   85,    0,
    0,  311,    0,  299,    0,  362,  309,    0,   11,    0,
    0,   14,    0,    0,  311,   16,  311,   17,  311,  147,
   85,   85,    0,    0,    0,   10,   10,   86,    0,  222,
   11,   11,  311,    0,  311,  216,   85,   16,   16,   17,
   17,   10,    0,   86,    0,    0,   11,    0,  257,   85,
  311,    0,   85,   16,   10,   17,   86,  299,    0,   11,
  405,    0,   11,    0,    0,   14,   16,   85,   17,   16,
    0,   17,  299,    0,    0,    0,    0,   11,    0,    0,
   14,    0,    0,    0,   16,    0,   17,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         10,
   56,   40,   64,   54,   40,   59,   45,   40,   37,   45,
   45,   45,   45,   40,   62,   40,   59,   62,   45,  256,
   45,   40,  258,   40,  263,  257,   45,   40,   45,  293,
   45,  303,   41,  266,   45,  271,  100,  269,  275,   45,
   44,  277,   41,   54,   60,  278,   40,   45,  260,   41,
   61,   44,   41,   42,   43,   41,   45,   68,   47,   40,
   40,  333,   20,  119,   41,   41,   41,   44,   44,   44,
  334,   60,   61,   62,   41,   41,   42,   43,   44,   45,
   38,   47,   40,  355,   95,  265,   42,   40,   99,   59,
  125,   47,   58,   59,   60,   61,   62,   44,   41,  279,
  111,   44,  113,  167,  120,   44,   43,  118,   45,   45,
  121,  266,   59,  129,  125,   59,   45,   60,   61,   62,
   59,  125,  151,  278,  266,   59,   41,   59,   43,   44,
   45,  256,  125,  337,  190,   40,  278,  266,  256,  264,
   59,   40,  153,   58,   59,   60,   61,   62,  266,  278,
   49,   50,   51,  357,  256,  359,   41,   45,   41,   44,
  278,   44,  256,  257,  266,  181,  177,  218,  219,  233,
  199,  256,  376,  224,  225,   58,   59,   60,   61,   62,
   41,  266,  257,   44,  266,  241,  215,  256,  257,  264,
   59,  260,  266,  278,  256,  256,  278,   58,   59,   60,
   61,   62,  256,   40,  278,  266,   45,  218,  219,  257,
  279,   45,  257,  224,  225,  244,   45,  256,  257,  248,
  256,  257,   45,  256,  257,  236,  282,  283,   45,  256,
  257,  256,  257,   60,  245,  246,  279,  256,  257,  256,
  257,  280,  257,  256,  280,  280,  280,  280,  299,   41,
  256,  257,   44,  280,  263,  280,  285,  260,  256,  257,
  258,  280,  256,  280,  263,  280,   58,   59,   60,   61,
   62,  263,  261,  271,  280,  256,  256,  263,  289,  277,
  256,  256,  280,  272,  273,  336,  256,  123,  299,  256,
  256,  257,  258,  280,  260,  261,  262,  263,   41,  265,
  356,  267,  268,  269,  270,  271,  272,  273,  274,  256,
  276,  277,  256,  256,  266,   58,   59,  256,  261,  260,
  256,  257,  256,  259,  256,  336,  278,  256,  257,  272,
  273,   41,  256,  257,   44,  346,  347,  348,  349,  350,
   40,  256,  257,  258,  280,  260,  261,  262,  263,   41,
  265,  280,  267,  268,  269,  270,  271,  272,  273,  274,
  259,  276,  277,   64,  266,   59,   58,   59,  256,  257,
  257,  256,  275,  256,  257,  258,  278,  260,  261,  262,
  263,  123,  265,  263,  267,  268,  269,  270,  271,  272,
  273,  274,  280,  276,  277,  256,  257,  258,   41,  260,
  261,  262,  263,   41,  265,   64,  267,  268,  269,  270,
  271,  272,  273,  274,  266,  276,  277,  256,  257,  258,
   58,   59,  256,  257,  266,   41,  278,  256,  257,   59,
  257,  258,  271,  256,  257,  257,  278,  257,  277,  256,
  257,  280,   58,   59,  271,   41,  280,   43,   44,   45,
  277,  280,   41,   62,   43,   58,   45,  280,   60,   61,
   62,   64,   62,  280,  256,  257,  258,   41,  260,  261,
  262,  263,   41,  265,   59,  267,  268,  269,  270,  271,
  272,  273,  274,  256,  276,  277,   44,  256,  257,   58,
   59,   41,  264,   43,   44,   45,   42,   43,  266,   45,
   59,   47,   60,   61,   62,   41,  256,   43,   44,   45,
   60,   61,   62,  256,  257,  258,  264,  260,   41,  262,
  263,   44,  265,   41,  267,  268,  269,  270,  271,   58,
   59,  274,  266,  276,  277,   41,   42,   43,   59,   45,
   41,   47,   41,   44,   43,   44,   45,  266,   60,   61,
   62,   43,   44,   45,   60,   61,   62,   41,  266,  367,
   44,   60,   61,   62,  256,  257,  258,  256,  260,   41,
  262,  263,  380,  265,  382,  267,  268,  269,  270,  271,
   58,   59,  274,   41,  276,  277,   42,   43,  256,   45,
  398,   47,  400,   43,   44,   45,   41,  257,   41,   44,
  259,  260,  262,  266,   60,   61,   62,  267,  416,  266,
   60,   61,   62,   41,  274,   43,  276,   45,  256,  257,
  258,  280,  260,  264,  262,  263,   59,  265,  263,  267,
  268,  269,  270,  271,   58,   59,  274,   41,  276,  277,
  256,  257,  258,   40,  260,  266,  262,  263,  279,  265,
  256,  267,  268,  269,  270,  271,  259,  260,  274,  261,
  276,  277,   58,   59,  256,  257,  258,   43,   44,   45,
  272,  273,   44,  256,   41,   42,   43,  280,   45,  271,
   47,   43,   44,   45,   58,  277,  263,  256,  257,  258,
   41,  260,    0,  262,  263,   59,  265,    0,  267,  268,
  269,  270,  271,  261,  266,  274,  256,  276,  277,   58,
  293,  261,  256,  257,  272,  273,   59,   41,  266,   43,
  303,   45,  272,  273,   43,   44,   45,  256,  257,  258,
   26,  260,   58,  262,  256,  257,  265,  167,  267,  268,
  269,  270,  271,   12,  256,  274,  266,  276,  277,  261,
  333,  334,  256,  257,   58,  261,  256,  256,  258,  266,
  272,  273,  261,   43,   44,   45,  272,  273,   43,   44,
   45,  271,  355,  272,  273,  266,   58,  277,  256,  257,
  258,  266,  260,    2,  262,    4,    5,  265,   12,  267,
  268,  269,  270,  271,  256,  257,  274,  266,  276,  277,
   58,   20,   43,   44,   45,  261,  256,  104,  105,  106,
  107,  261,   43,   44,   45,  135,  272,  273,   -1,   38,
   -1,   40,  272,  273,   58,  122,  123,  256,  257,  126,
  127,   -1,  256,  257,  258,   -1,  260,   -1,  262,    4,
    5,  265,   -1,  267,  268,  269,  270,  271,   58,   -1,
  274,   -1,  276,  277,   -1,   -1,   -1,   -1,   -1,   -1,
  256,  257,  258,   -1,  260,   -1,  262,   -1,   -1,  265,
   58,  267,  268,  269,  270,  271,   -1,   -1,  274,   -1,
  276,  277,  256,  257,  258,   -1,  260,   -1,  262,   -1,
   -1,  265,   58,  267,  268,  269,  270,  271,   -1,   -1,
  274,   -1,  276,  277,   -1,   -1,   -1,  256,  257,  258,
   -1,  260,   -1,  262,   58,   -1,  265,   -1,  267,  268,
  269,  270,  271,   -1,   -1,  274,   -1,  276,  277,   -1,
  256,  257,  258,   -1,  260,   -1,  262,  180,   -1,  265,
   -1,  267,  268,  269,  270,  271,   -1,   -1,  274,   -1,
  276,  277,  256,  257,  258,   -1,  260,   -1,  262,   -1,
   -1,  265,   -1,  267,  268,  269,  270,  271,   -1,   -1,
  274,   -1,  276,  277,  256,  257,  258,   -1,  260,   -1,
  262,   -1,   -1,  265,   -1,  267,  268,  269,  270,  271,
   -1,   -1,  274,   -1,  276,  277,   -1,  240,  256,  257,
  258,   -1,  260,   -1,  262,   -1,   -1,  265,   -1,  267,
  268,  269,  270,  271,   -1,   -1,  274,   -1,  276,  277,
   -1,   -1,  256,  257,  258,   -1,  260,   -1,  262,   -1,
   -1,  265,   -1,  267,  268,  269,  270,  271,   -1,   -1,
  274,   -1,  276,  277,   -1,   -1,  256,  257,  258,   -1,
  260,   -1,  262,   -1,   -1,  265,   -1,  267,  268,  269,
  270,  271,   -1,   -1,  274,   -1,  276,  277,  256,  257,
  258,   -1,  260,   -1,  262,   -1,   -1,   -1,   -1,  267,
  268,  269,  270,  271,  256,   -1,  274,  330,  276,  277,
  256,  257,  258,   -1,  260,   -1,  262,   -1,   -1,  256,
   -1,  267,  268,  269,  270,  271,   -1,  256,  274,  352,
  276,  277,  256,  257,  258,   -1,  260,    2,  262,    4,
    5,  293,   -1,  267,  268,  269,  270,  271,   -1,   -1,
  274,  303,  276,  277,   -1,   20,  293,   -1,   -1,    9,
   -1,   -1,  256,   -1,  293,   -1,  303,  390,  391,  392,
  393,  394,   -1,   38,  303,   40,   -1,   -1,   -1,   -1,
  256,  333,  334,  257,   -1,   -1,   -1,   -1,  262,  341,
  264,   41,   -1,  267,   -1,   45,  333,  334,   -1,  293,
  274,   -1,  276,  355,  333,  334,  358,   -1,   -1,  303,
  362,   -1,  341,   -1,   -1,  367,   -1,  293,  355,   -1,
   -1,   86,   -1,   -1,   -1,  362,  355,  303,  380,  358,
  382,   81,  384,  362,   -1,   -1,  256,   -1,  367,  333,
  334,   -1,   -1,   93,   -1,   -1,  398,  384,  400,  257,
   -1,  380,   -1,  382,  262,  384,  264,  333,  334,  267,
   -1,  355,  270,  113,  416,  341,  274,   -1,  276,  398,
   -1,  400,   -1,  293,   -1,   -1,   -1,   -1,   -1,  355,
  256,   -1,  358,  303,  134,  135,  362,  416,   -1,   -1,
  155,  367,  256,  257,   -1,   41,   42,   43,  262,   45,
  264,   47,   -1,  267,  380,   -1,  382,   -1,  384,   -1,
  274,   -1,  276,  333,  334,   -1,   -1,  293,   -1,   -1,
   -1,  341,  398,   -1,  400,  256,  257,  303,   -1,   -1,
   -1,  262,   -1,  264,   -1,  355,  267,   -1,  358,  270,
  416,   -1,  362,  274,   -1,  276,   -1,  367,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,  256,  257,  333,  334,   -1,
  380,  262,  382,  264,  384,  341,  267,   -1,   -1,  270,
   -1,   -1,   -1,  274,   -1,  276,   -1,   -1,  398,  355,
  400,   -1,  358,   -1,   -1,   -1,  362,  256,  257,   -1,
   -1,  367,   -1,  262,   -1,  264,  416,   -1,  267,   -1,
   -1,  270,   -1,   -1,  380,  274,  382,  276,  384,  256,
  257,  257,   -1,   -1,   -1,  262,  262,  264,   -1,  265,
  267,  267,  398,   -1,  400,  256,  257,  274,  274,  276,
  276,  262,   -1,  264,   -1,   -1,  267,   -1,  256,  257,
  416,   -1,  257,  274,  262,  276,  264,  262,   -1,  267,
  265,   -1,  267,   -1,   -1,  270,  274,  257,  276,  274,
   -1,  276,  262,   -1,   -1,   -1,   -1,  267,   -1,   -1,
  270,   -1,   -1,   -1,  274,   -1,  276,
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
"clausulaSeleccionConRet : inicioClausulaSeleccion cuerpoThenConRet END_IF",
"clausulaSeleccionConRet : IF '(' expresion ')' cuerpoThenConRet END_IF",
"clausulaSeleccionConRet : IF '(' expresion ')' cuerpoThenConRet ELSE bloqueSentenciaEjecutableConRet END_IF",
"clausulaSeleccionConRet : inicioClausulaSeleccion cuerpoThenConRet ELSE bloqueSentenciaEjecutableConRet END_IF",
"clausulaSeleccionConRet : inicioClausulaSeleccion cuerpoThenConRet error",
"clausulaSeleccionConRet : inicioClausulaSeleccion cuerpoThenConRet ELSE bloqueSentenciaEjecutableConRet error",
"clausulaSeleccionConRet : IF '(' condicion cuerpoThenConRet ELSE bloqueSentenciaEjecutableConRet END_IF",
"clausulaSeleccionConRet : IF '(' condicion cuerpoThenConRet END_IF",
"clausulaSeleccionConRet : IF condicion ')' cuerpoThenConRet ELSE bloqueSentenciaEjecutableConRet END_IF",
"clausulaSeleccionConRet : IF condicion ')' cuerpoThenConRet END_IF",
"clausulaSeleccionConRet : IF condicion cuerpoThenConRet ELSE bloqueSentenciaEjecutableConRet END_IF",
"clausulaSeleccionConRet : IF condicion cuerpoThenConRet END_IF",
"clausulaSeleccionConRet : inicioClausulaSeleccion THEN error END_IF",
"clausulaSeleccionConRet : inicioClausulaSeleccion cuerpoThenConRet ELSE error END_IF",
"clausulaSeleccionConRet : inicioClausulaSeleccion THEN error ELSE bloqueSentenciaEjecutableConRet END_IF",
"clausulaSeleccionConRet : inicioClausulaSeleccion THEN error ELSE error END_IF",
"cuerpoThenConRet : THEN bloqueSentenciaEjecutableConRet",
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

//#line 330 "gramatica.y"
//FUNCIONES
private static AnalizadorLexico lex;
public static List<Error> erroresLexico = new ArrayList<Error>();
public static List<Error> erroresSintactico = new ArrayList<Error>();
public static List<Error> erroresSemanticos = new ArrayList<Error>();
public static List<String> estructuras = new ArrayList<String>();
public static List<Terceto> tercetos = new ArrayList<Terceto>();
public static Stack<String> tercetosIncompletos = new Stack<String>();

public static HashMap<String, String> etiquetas = new HashMap<String, String>(); 
public static Stack<String> tercetosGoto = new Stack<String>(); 

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

        completarTercetosEtiqueta();

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
    String newRef = TablaDeSimbolos.agregarSimbolo(refIdentificador + cargarAmbito(), -1, TablaDeSimbolos.getContexto(refIdentificador).popRefUso());
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
  String newRef = TablaDeSimbolos.agregarSimbolo(ref + cargarAmbito(), -1, TablaDeSimbolos.getContexto(ref).popRefUso());
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
  String newRef = TablaDeSimbolos.agregarSimbolo(refIdentificador + cargarAmbito(), -1, TablaDeSimbolos.getContexto(refIdentificador).popRefUso());
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
  String newRef = TablaDeSimbolos.agregarSimbolo(refIdentificador + cargarAmbito(), -1, TablaDeSimbolos.getContexto(refIdentificador).popRefUso());
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
  String newRef = TablaDeSimbolos.agregarSimbolo(refIdentificador + cargarAmbito(), -1, TablaDeSimbolos.getContexto(refIdentificador).popRefUso());
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
  String newRef = TablaDeSimbolos.agregarSimbolo(refIdentificador + cargarAmbito(), -1, TablaDeSimbolos.getContexto(refIdentificador).popRefUso());
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
private boolean chequearDeclarado(String lexemaSinAmbito){
  
  String lexema = lexemaSinAmbito + cargarAmbito();
  System.out.println("-------------------------------------IDENTIFICARDOR A BUSCAR: " + lexema);
  while(lexema.lastIndexOf(":") != -1){
    Contexto contexto = TablaDeSimbolos.getContexto(lexema);
    if (contexto != null){
      if (contexto.getDeclarado()){    //CON UNA VARIABLE BASE (SIN AMBITO) EN LA T.S. ESTE IF PUEDE NO ESTAR. SI AGREGAMOS A LA T.S. (DESDE EL LEXER) CON AMBITO ENTONCES NECESITAMOS DEL ATRIBUTO "DECLARADO"
        System.out.println("saaaaaaaaaaaaaaaaaaaaaaa");
        contexto.addRef(TablaDeSimbolos.getContexto(lexemaSinAmbito).popRefUso());
        return true;
      }
    }
    int ultAmbito = lexema.lastIndexOf(":");
    lexema = lexema.substring(0, ultAmbito);
  }
  erroresSemanticos.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SEMANTICO " + lexemaSinAmbito + " nunca fue declarado")); 
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

public static void completarTercetosEtiqueta(){ //recorre TODOS tercetosGoto y agrega ref a etiqueta //SE EJECUTA CUANDO TERMINA LA GENERACION DEL TERCETO
  while (tercetosGoto.size() > 0){
    Terceto tGoto = tercetos.get(Integer.parseInt(tercetosGoto.pop().replace("^", "")));

    if (etiquetas.containsKey(tGoto.getT2())){
      tGoto.setT3(etiquetas.get(tGoto.getT2()));
      tGoto.setT2("");
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
//#line 1095 "Parser.java"
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
//#line 37 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta 'cuerpo'."));}
break;
case 3:
//#line 38 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta 'END al final del programa'."));}
break;
case 4:
//#line 39 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta BEGIN del programa."));}
break;
case 5:
//#line 40 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta 'nombre del programa'."));}
break;
case 6:
//#line 41 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO delimitadores de programa."));}
break;
case 12:
//#line 52 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta ';'."));}
break;
case 13:
//#line 53 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta sentencia."));}
break;
case 14:
//#line 54 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO no se puede retornar en el cuerpo del programa."));}
break;
case 15:
//#line 58 "gramatica.y"
{ArrayList<String> referenciasIden = new ArrayList<String>(); referenciasIden.add(val_peek(1).sval); declararVariable(val_peek(2).sval, referenciasIden); estructuras.add("Linea "+ TablaDeSimbolos.getContexto(val_peek(2).sval).popRef() +": "+"Declaracion"); TablaDeSimbolos.getContexto(val_peek(1).sval + cargarAmbito()).setDeclarado();}
break;
case 16:
//#line 59 "gramatica.y"
{Contexto contexto = TablaDeSimbolos.getContexto(val_peek(4).sval);estructuras.add("Linea "+ contexto.popRef() +": "+"Declaracion");}
break;
case 17:
//#line 60 "gramatica.y"
{Contexto contexto = TablaDeSimbolos.getContexto(val_peek(4).sval);erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera IDENTIFICADOR.")); estructuras.add("Linea "+ contexto.popRef() +": "+"Declaracion");}
break;
case 18:
//#line 61 "gramatica.y"
{Contexto contexto = TablaDeSimbolos.getContexto(val_peek(5).sval);erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta ','.")); estructuras.add("Linea "+ contexto.popRef() +": "+"Declaracion");}
break;
case 19:
//#line 62 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta ','.")); }
break;
case 21:
//#line 64 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera ';'.")); }
break;
case 22:
//#line 65 "gramatica.y"
{estructuras.add("Declaracion de funcion");}
break;
case 23:
//#line 66 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera 'tipo de dato antes de declaracion de funcion'.")); }
break;
case 24:
//#line 71 "gramatica.y"
{Integer lastRef = TablaDeSimbolos.getContexto(val_peek(1).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"Declaracion de subtipo");}
break;
case 25:
//#line 72 "gramatica.y"
{Integer lastRef = TablaDeSimbolos.getContexto(val_peek(1).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"Declaracion de triple");}
break;
case 26:
//#line 73 "gramatica.y"
{ erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera 'TYPEDEF antes de la declaracion del subtipo'.")); estructuras.add("Linea "+ ": "+"Declaracion de subtipo");}
break;
case 27:
//#line 74 "gramatica.y"
{ erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera 'TYPEDEF antes de la declaracion del triple'.")); estructuras.add("Linea "+ ": "+"Declaracion de triple");}
break;
case 28:
//#line 75 "gramatica.y"
{ erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera 'declaracion de subtipo o triple'.")); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(1).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"Declaracion de subtipo"); }
break;
case 29:
//#line 78 "gramatica.y"
{declaracionSubtipo(val_peek(3).sval,val_peek(5).sval);}
break;
case 30:
//#line 79 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera 'nombre del tipo de dato nuevo'."));}
break;
case 31:
//#line 80 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera declaracion de subrangos."));}
break;
case 32:
//#line 82 "gramatica.y"
{declaracionTriple(val_peek(2).sval,val_peek(0).sval);}
break;
case 34:
//#line 84 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera 'tipo de objeto a declarar'."));}
break;
case 35:
//#line 85 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera 'nombre de objeto a declarar'."));}
break;
case 36:
//#line 86 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera 'nombre de objeto a declarar'."));}
break;
case 37:
//#line 87 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera 'nombre y tipo de objeto a declarar'."));}
break;
case 38:
//#line 88 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta '<' al inicio del identificador'."));}
break;
case 39:
//#line 89 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta '>' al final del identificador'."));}
break;
case 40:
//#line 90 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO faltan '<>'."));}
break;
case 41:
//#line 91 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO faltan '<>'."));}
break;
case 42:
//#line 94 "gramatica.y"
{if (val_peek(0).sval.equals("false"))erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta return en el cuerpo de la funcion.")); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(6).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"Declaracion de funcion"); ambitos.remove(ambitos.size()-1);}
break;
case 43:
//#line 95 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta nombre de la funcion."));  Integer lastRef = TablaDeSimbolos.getContexto(val_peek(7).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"Declaracion de funcion");}
break;
case 44:
//#line 96 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO  no puede tener mas de un parametro."));  Integer lastRef = TablaDeSimbolos.getContexto(val_peek(8).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"Declaracion de funcion");}
break;
case 45:
//#line 97 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta cuerpo con retorno."));  Integer lastRef = TablaDeSimbolos.getContexto(val_peek(6).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"Declaracion de funcion");}
break;
case 46:
//#line 100 "gramatica.y"
{declararFuncion(val_peek(0).sval); ambitos.add(val_peek(0).sval);}
break;
case 51:
//#line 108 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta identificador despues de la coma."));}
break;
case 55:
//#line 117 "gramatica.y"
{ Contexto contexto = TablaDeSimbolos.getContexto(val_peek(0).sval);
                        chequearRango(contexto);                             /*SOLO SE CHEQUEA EN POSITIVO YA QUE EL MAXIMO DE NEGATIVOS ES MAYOR AL MAXIMO DE POSITIVOS Y YA LO CHEQUEA EL PARSER*/
                      }
break;
case 56:
//#line 120 "gramatica.y"
{
                            Contexto contexto = TablaDeSimbolos.getContexto(val_peek(0).sval);
                            String newLexRef = TablaDeSimbolos.agregarSimbolo("-"+val_peek(0).sval, contexto.getTipo(), "-"+val_peek(0).sval, AnalizadorLexico.getNumeroLinea());
                          }
break;
case 57:
//#line 127 "gramatica.y"
{declaracionParametro(val_peek(1).sval, val_peek(0).sval);}
break;
case 58:
//#line 128 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta tipo de dato en el parametro."));}
break;
case 59:
//#line 129 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta nombre en el parametro."));}
break;
case 60:
//#line 133 "gramatica.y"
{ if (val_peek(1).sval.equals("true") || val_peek(0).sval.equals("true")) yyval.sval = "true"; else yyval.sval = "false";}
break;
case 61:
//#line 134 "gramatica.y"
{yyval.sval = val_peek(0).sval;}
break;
case 62:
//#line 137 "gramatica.y"
{yyval.sval = "false";}
break;
case 63:
//#line 138 "gramatica.y"
{yyval.sval = val_peek(1).sval;}
break;
case 64:
//#line 139 "gramatica.y"
{yyval.sval = "false";}
break;
case 65:
//#line 140 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta ';'." )); yyval.sval = val_peek(0).sval;}
break;
case 66:
//#line 144 "gramatica.y"
{agregarTerceto("RET", val_peek(1).sval, ""); estructuras.add("Retorno"); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(3).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"Sentencia de Retorno");}
break;
case 72:
//#line 154 "gramatica.y"
{yyval.sval = "false";}
break;
case 73:
//#line 155 "gramatica.y"
{yyval.sval = "false";}
break;
case 74:
//#line 156 "gramatica.y"
{yyval.sval = "false";}
break;
case 75:
//#line 157 "gramatica.y"
{yyval.sval = "false";}
break;
case 76:
//#line 158 "gramatica.y"
{yyval.sval = "true";}
break;
case 77:
//#line 159 "gramatica.y"
{yyval.sval = val_peek(0).sval;}
break;
case 78:
//#line 162 "gramatica.y"
{chequearDeclarado(val_peek(2).sval); yyval.sval = agregarTerceto(":=", val_peek(2).sval, val_peek(0).sval);Integer lastRef = TablaDeSimbolos.getContexto(val_peek(2).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"Sentencia de Asignacion");}
break;
case 79:
//#line 163 "gramatica.y"
{chequearDeclarado(val_peek(3).sval); if (!val_peek(2).sval.equals("[1]") && !val_peek(2).sval.equals("[2]") && !val_peek(2).sval.equals("[3]")) erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO rango invalido, se espera entre 1 y 3.")); else agregarTerceto(":=", val_peek(3).sval + val_peek(2).sval , val_peek(0).sval); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(3).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"Sentencia de Asignacion");}
break;
case 80:
//#line 164 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO faltan '[]' en el rango")); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(3).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"Sentencia de Asignacion");}
break;
case 81:
//#line 167 "gramatica.y"
{yyval.sval = agregarTerceto("+", val_peek(2).sval, val_peek(0).sval);}
break;
case 82:
//#line 168 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta operando en la expresion"));}
break;
case 83:
//#line 169 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta operando en la expresion"));}
break;
case 84:
//#line 170 "gramatica.y"
{yyval.sval = agregarTerceto("-", val_peek(2).sval, val_peek(0).sval);}
break;
case 85:
//#line 171 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta operando en la expresion"));}
break;
case 86:
//#line 172 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta operando en la expresion"));}
break;
case 88:
//#line 176 "gramatica.y"
{yyval.sval = agregarTerceto("*", val_peek(2).sval, val_peek(0).sval);}
break;
case 89:
//#line 177 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta operando en la expresion"));}
break;
case 90:
//#line 178 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta operando en la expresion"));}
break;
case 91:
//#line 179 "gramatica.y"
{yyval.sval = agregarTerceto("/", val_peek(2).sval, val_peek(0).sval);}
break;
case 92:
//#line 180 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta operando en la expresion"));}
break;
case 93:
//#line 181 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta operando en la expresion"));}
break;
case 95:
//#line 185 "gramatica.y"
{yyval.sval = val_peek(0).sval; chequearDeclarado(val_peek(0).sval);}
break;
case 102:
//#line 194 "gramatica.y"
{ chequearDeclarado(val_peek(3).sval); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(3).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"Invocacion de Funcion");}
break;
case 103:
//#line 195 "gramatica.y"
{ chequearDeclarado(val_peek(4).sval); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(4).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"Invocacion de Funcion");}
break;
case 104:
//#line 196 "gramatica.y"
{ chequearDeclarado(val_peek(6).sval); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(6).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"Invocacion de Funcion");}
break;
case 105:
//#line 197 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO numero de expresiones invalido en el llamado a funcion.")); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(5).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"Invocacion de Funcion");}
break;
case 106:
//#line 198 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta expresion en el llamado a funcion.")); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(3).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"Invocacion de Funcion");}
break;
case 107:
//#line 199 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO numero de expresiones invalido en el llamado a funcion.")); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(6).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"Invocacion de Funcion");}
break;
case 108:
//#line 200 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO numero de expresion esinvalido en el llamado a funcion.")); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(8).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"Invocacion de Funcion");}
break;
case 109:
//#line 203 "gramatica.y"
{completarUltimoTercetoIncompleto();  Integer lastRef = TablaDeSimbolos.getContexto(val_peek(2).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"IF");}
break;
case 110:
//#line 204 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera una condicion valida")); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(5).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"IF");}
break;
case 111:
//#line 205 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera una condicion valida")); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(7).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"IF");;}
break;
case 112:
//#line 206 "gramatica.y"
{completarUltimoTercetoIncompleto(); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(4).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"IF");}
break;
case 113:
//#line 207 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta 'end_if;'.")); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(2).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"IF");}
break;
case 114:
//#line 208 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta 'end_if;'.")); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(4).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"IF");}
break;
case 115:
//#line 209 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta ')' al final de la condicion.")); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(6).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"IF");}
break;
case 116:
//#line 210 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta ')' al final de la condicion.")); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(4).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"IF");}
break;
case 117:
//#line 211 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta '(' al comienzo de la condicion.")); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(6).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"IF");}
break;
case 118:
//#line 212 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta '(' al comienzo de la condicion.")); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(4).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"IF");}
break;
case 119:
//#line 213 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO faltan '()' en la condicion.")); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(5).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"IF");}
break;
case 120:
//#line 214 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO faltan '()' en la condicion.")); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(3).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"IF");}
break;
case 121:
//#line 215 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta bloque de sentencias ejecutables valido.")); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(3).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"IF");}
break;
case 122:
//#line 216 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta bloque de sentencias ejecutables valido .")); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(4).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"IF");}
break;
case 123:
//#line 217 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta bloque de sentencias ejecutables valido .")); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(5).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"IF");}
break;
case 124:
//#line 218 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO faltan los dos bloques de sentencias ejecutables validas.")); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(5).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"IF");}
break;
case 125:
//#line 221 "gramatica.y"
{tercetosIncompletos.add(agregarTerceto("BF", val_peek(1).sval, ""));}
break;
case 126:
//#line 224 "gramatica.y"
{String incompleto = agregarTerceto("BI", "", ""); completarUltimoTercetoIncompleto(); tercetosIncompletos.add(incompleto);}
break;
case 127:
//#line 227 "gramatica.y"
{completarUltimoTercetoIncompleto(); yyval.sval = "false"; Integer lastRef = TablaDeSimbolos.getContexto(val_peek(2).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"IF");}
break;
case 128:
//#line 228 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera una condicion valida")); yyval.sval = "false";  Integer lastRef = TablaDeSimbolos.getContexto(val_peek(5).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"IF");}
break;
case 129:
//#line 229 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera una condicion valida"));if (val_peek(2).sval.equals("true") && val_peek(0).sval.equals("true")) yyval.sval = "true"; else yyval.sval = "false"; Integer lastRef = TablaDeSimbolos.getContexto(val_peek(7).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"IF");}
break;
case 130:
//#line 230 "gramatica.y"
{completarUltimoTercetoIncompleto(); if (val_peek(3).sval.equals("true") && val_peek(1).sval.equals("true")) yyval.sval = "true"; else yyval.sval = "false"; Integer lastRef = TablaDeSimbolos.getContexto(val_peek(4).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"IF");}
break;
case 131:
//#line 231 "gramatica.y"
{yyval.sval = "false";erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta 'end_if;'.")); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(2).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"IF");}
break;
case 132:
//#line 232 "gramatica.y"
{if (val_peek(3).sval.equals("true") && val_peek(1).sval.equals("true")) yyval.sval = "true"; else yyval.sval = "false";erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta 'end_if;'.")); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(4).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"IF");}
break;
case 133:
//#line 233 "gramatica.y"
{if (val_peek(2).sval.equals("true") && val_peek(0).sval.equals("true")) yyval.sval = "true"; else yyval.sval = "false";erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta ')' al final de la condicion.")); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(6).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"IF");}
break;
case 134:
//#line 234 "gramatica.y"
{yyval.sval = "false";erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta ')' al final de la condicion.")); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(4).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"IF");}
break;
case 135:
//#line 235 "gramatica.y"
{if (val_peek(2).sval.equals("true") && val_peek(0).sval.equals("true")) yyval.sval = "true"; else yyval.sval = "false";erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta '(' al comienzo de la condicion.")); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(6).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"IF");}
break;
case 136:
//#line 236 "gramatica.y"
{yyval.sval = "false";erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta '(' al comienzo de la condicion.")); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(4).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"IF");}
break;
case 137:
//#line 237 "gramatica.y"
{if (val_peek(2).sval.equals("true") && val_peek(0).sval.equals("true")) yyval.sval = "true"; else yyval.sval = "false";erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO faltan '()' en la condicion.")); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(5).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"IF");}
break;
case 138:
//#line 238 "gramatica.y"
{yyval.sval = "false";erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO faltan '()' en la condicion.")); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(3).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"IF");}
break;
case 139:
//#line 239 "gramatica.y"
{yyval.sval = "false";erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta bloque de sentencias ejecutables valido.")); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(3).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"IF");}
break;
case 140:
//#line 240 "gramatica.y"
{yyval.sval = "false";erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta bloque de sentencias ejecutables valido .")); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(4).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"IF");}
break;
case 141:
//#line 241 "gramatica.y"
{yyval.sval = "false";erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta bloque de sentencias ejecutables valido .")); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(5).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"IF");}
break;
case 142:
//#line 242 "gramatica.y"
{yyval.sval = "false";erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO faltan los dos bloques de sentencias ejecutables validas.")); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(5).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"IF");}
break;
case 143:
//#line 245 "gramatica.y"
{String incompleto = agregarTerceto("BI", "", ""); completarUltimoTercetoIncompleto(); tercetosIncompletos.add(incompleto); yyval.sval = "true";}
break;
case 144:
//#line 247 "gramatica.y"
{((ArrayList<String>)val_peek(7).obj).add(val_peek(9).sval); ((ArrayList<String>)val_peek(1).obj).add(val_peek(3).sval); yyval.sval = agregaListaExpresionTercetos(val_peek(5).sval, ((ArrayList<String>)val_peek(7).obj), ((ArrayList<String>)val_peek(1).obj));}
break;
case 145:
//#line 248 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO faltan '( )' a las listas de expresiones."));}
break;
case 146:
//#line 249 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO faltan parentesis en las listas de expresiones."));}
break;
case 147:
//#line 250 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO faltan parentesis en las listas de expresiones."));}
break;
case 148:
//#line 251 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO faltan parentesis en las listas de expresiones."));}
break;
case 149:
//#line 252 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO faltan parentesis en las listas de expresiones."));}
break;
case 150:
//#line 253 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO faltan parentesis en las listas de expresiones."));}
break;
case 151:
//#line 254 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera un comparador."));}
break;
case 152:
//#line 255 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO faltan parentesis en las listas de expresiones."));}
break;
case 153:
//#line 257 "gramatica.y"
{yyval.sval = agregarTerceto(val_peek(1).sval, val_peek(2).sval, val_peek(0).sval);}
break;
case 154:
//#line 258 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera una expresion a la izquierda del comparador."));}
break;
case 155:
//#line 259 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera una expresion a la derecha del comparador."));}
break;
case 156:
//#line 260 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera un comparador."));}
break;
case 157:
//#line 263 "gramatica.y"
{((ArrayList<String>)val_peek(2).obj).add(val_peek(0).sval);}
break;
case 158:
//#line 264 "gramatica.y"
{ArrayList<String> aux = new ArrayList<String>(); aux.add(val_peek(0).sval); yyval.obj = aux;}
break;
case 159:
//#line 265 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera una expresion despues de la coma."));}
break;
case 160:
//#line 268 "gramatica.y"
{yyval.sval = "<";}
break;
case 161:
//#line 269 "gramatica.y"
{yyval.sval = ">";}
break;
case 162:
//#line 270 "gramatica.y"
{yyval.sval = "=";}
break;
case 163:
//#line 271 "gramatica.y"
{yyval.sval = TablaTipoToken.DISTINTO;}
break;
case 164:
//#line 272 "gramatica.y"
{yyval.sval = TablaTipoToken.MENOR_IGUAL;}
break;
case 165:
//#line 273 "gramatica.y"
{yyval.sval = TablaTipoToken.MAYOR_IGUAL;}
break;
case 168:
//#line 278 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta ';'."));}
break;
case 170:
//#line 283 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta ';'."));}
break;
case 172:
//#line 287 "gramatica.y"
{ if (val_peek(2).sval.equals("true") || val_peek(1).sval.equals("true")) yyval.sval = "true"; else yyval.sval = "false";}
break;
case 173:
//#line 288 "gramatica.y"
{yyval.sval = val_peek(1).sval;}
break;
case 174:
//#line 291 "gramatica.y"
{yyval.sval = val_peek(1).sval;}
break;
case 175:
//#line 292 "gramatica.y"
{yyval.sval = val_peek(1).sval; erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta ';'."));}
break;
case 176:
//#line 293 "gramatica.y"
{yyval.sval = val_peek(1).sval;}
break;
case 177:
//#line 297 "gramatica.y"
{agregarTerceto("BF", val_peek(1).sval, ((Integer)(tercetos.size()+2)).toString());agregarTerceto("BI", "", (inicioBucle.pop().toString()));Integer lastRef = TablaDeSimbolos.getContexto(val_peek(5).sval).popRef(); agregarTerceto("ETIQUETA", "", ";etiqueta"+ (tercetos.size())); estructuras.add("Linea "+lastRef.toString() +": "+"Bucle");}
break;
case 178:
//#line 298 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta bloque de sentencias ejecutables.")); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(5).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"Bucle");}
break;
case 179:
//#line 299 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta ( de apertura de condicion.")); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(5).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"Bucle");}
break;
case 180:
//#line 300 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta condicion.")); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(5).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"Bucle");}
break;
case 181:
//#line 301 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta ) de cierre de condicion.")); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(4).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"Bucle");}
break;
case 182:
//#line 302 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta ( ) englobando la condicion.")); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(5).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"Bucle");}
break;
case 183:
//#line 303 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta ( ) englobando la condicion y bloque de sentencias ejecutables.")); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(5).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"Bucle");}
break;
case 184:
//#line 304 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta condicion y bloque de sentencias ejecutables.")); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(5).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"Bucle");}
break;
case 185:
//#line 306 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta while")); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(2).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"Bucle");}
break;
case 186:
//#line 309 "gramatica.y"
{inicioBucle.add(tercetos.size()); agregarTerceto("ETIQUETA", "", ";etiqueta"+ (tercetos.size()));}
break;
case 187:
//#line 312 "gramatica.y"
{tercetosGoto.push(agregarTerceto("BI", val_peek(1).sval,  ""));Integer lastRef = TablaDeSimbolos.getContexto(val_peek(2).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"GOTO");}
break;
case 188:
//#line 313 "gramatica.y"
{erroresSintactico.add(new Error(numeroLineaError, Tipo.ERROR, "ERROR SINTACTICO falta '@' luego de los dospuntos.")); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(2).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"GOTO");}
break;
case 189:
//#line 314 "gramatica.y"
{erroresSintactico.add(new Error(numeroLineaError, Tipo.ERROR, "ERROR SINTACTICO etiqueta invalida.")); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(2).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"GOTO");}
break;
case 190:
//#line 315 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta 'goto' luego de los dospuntos.")); estructuras.add("Linea "+": "+"GOTO");}
break;
case 191:
//#line 318 "gramatica.y"
{agregarTerceto("ETIQUETA", "", val_peek(1).sval); etiquetas.put(val_peek(1).sval, "^"+(tercetos.size()-1)); declaracionEtiqueta(val_peek(1).sval);Integer lastRef = TablaDeSimbolos.getContexto(val_peek(1).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"Etiqueta"); yyval.sval = lastRef.toString();}
break;
case 192:
//#line 319 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta 'etiqueta'."));}
break;
case 193:
//#line 323 "gramatica.y"
{agregarTerceto("OUTF", val_peek(1).sval, ""); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(3).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"Mensaje de salida");}
break;
case 194:
//#line 324 "gramatica.y"
{agregarTerceto("OUTF", val_peek(1).sval, ""); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(3).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"Mensaje de salida");}
break;
case 195:
//#line 325 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera cadena multilinea o expresion en el mensaje de salida.")); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(3).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"Mensaje de salida");}
break;
//#line 1901 "Parser.java"
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
