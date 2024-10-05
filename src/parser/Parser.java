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
import java.util.HexFormat;
import lexico.TablaDeSimbolos.Contexto;
//#line 30 "Parser.java"




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
    2,    2,    2,    3,    3,    3,    3,    3,    3,    3,
    8,    8,    8,    8,    8,   10,   10,   11,   11,   11,
   11,   11,   11,   11,   11,   11,   11,    9,    9,    9,
    9,    6,    6,    6,    7,    7,    7,   12,   12,   15,
   15,   13,   13,   13,   14,   14,   16,   16,   16,   16,
   16,   18,    4,    4,    4,    4,    4,   17,   17,   17,
   17,   17,   17,   20,   20,   19,   19,   19,   19,   19,
   27,   27,   27,   27,   26,   26,   26,   26,   26,   26,
   26,   28,   28,   28,   28,   28,   28,   28,   21,   21,
   21,   21,   21,   21,   21,   21,   21,   21,   21,   21,
   21,   21,   25,   25,   25,   25,   25,   25,   25,   25,
   25,   25,   25,   25,   25,   25,   29,   29,   29,   29,
   32,   32,   34,   34,   34,   34,   33,   33,   33,   33,
   33,   33,   35,   35,   35,   30,   30,   30,   36,   36,
   31,   31,   22,   22,   22,   22,   22,   22,   22,   22,
   22,   23,   23,   23,    5,    5,   24,   24,   24,
};
final static short yylen[] = {                            2,
    4,    3,    3,    4,    3,    4,    2,    1,    1,    2,
    1,    2,    2,    3,    5,    5,    6,    4,    2,    3,
    2,    2,    2,    2,    2,    6,    6,    5,    5,    5,
    5,    5,    5,    4,    4,    3,    3,    8,    8,   10,
    8,    1,    1,    1,    3,    3,    1,    3,    1,    1,
    2,    2,    2,    2,    2,    1,    2,    2,    1,    1,
    1,    4,    1,    1,    1,    1,    1,    1,    1,    1,
    1,    1,    1,    3,    4,    3,    3,    4,    3,    3,
    1,    1,    1,    1,    1,    1,    1,    2,    2,    2,
    2,    4,    5,    7,    6,    4,    7,    9,    7,    9,
    7,    9,    8,    6,    8,    6,    7,    5,    7,    9,
    9,    9,    7,    9,    7,    9,    8,    6,    8,    6,
    7,    5,    7,    9,    9,    9,    3,    3,    3,    3,
    3,    1,    3,    1,    3,    3,    1,    1,    1,    1,
    1,    1,    3,    2,    2,    2,    2,    3,    3,    2,
    1,    3,    6,    6,    6,    6,    5,    6,    6,    6,
    3,    3,    3,    2,    2,    2,    4,    4,    4,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,   44,    0,    0,
    0,   42,    0,    0,   43,    0,    8,    9,    0,    0,
    0,    0,   63,   64,   65,   66,   67,    0,    2,    0,
    0,    0,    0,   13,  166,   23,   24,    0,    0,  165,
    0,    0,   50,    0,    0,   86,    0,    0,   87,    0,
    0,    0,    0,    0,   21,   22,    0,    0,    0,    0,
    0,    0,    0,    0,    5,    7,   12,   10,  164,    0,
    0,    0,   19,    0,    4,    1,    0,    0,    0,    0,
    0,    0,    0,    0,  140,  141,  142,   81,   82,   83,
   84,    0,  137,  138,  139,    0,    0,   88,    0,   89,
   90,   91,   51,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  147,  146,  161,    0,  163,  162,    0,   14,    0,    0,
    0,   20,    0,    0,   36,    0,    0,    0,    0,   37,
    0,    0,   80,    0,  129,    0,    0,    0,    0,    0,
    0,  131,   77,    0,   79,   76,    0,    0,  130,    0,
  127,    0,    0,  169,  168,  167,    0,    0,  145,  144,
  148,    0,    0,    0,   18,    0,   47,    0,    0,    0,
    0,    0,   34,    0,   35,    0,    0,   96,    0,    0,
    0,   92,    0,    0,   78,  108,    0,    0,    0,    0,
    0,  143,    0,    0,    0,   16,    0,   15,    0,    0,
    0,    0,    0,    0,   49,    0,   33,   30,   32,   29,
   31,   28,    0,    0,   93,    0,  104,    0,    0,    0,
    0,  106,    0,  160,  159,  154,  158,  155,  156,  153,
   17,   46,   45,   53,   54,   52,    0,    0,    0,    0,
   27,   26,    0,    0,   95,    0,  109,    0,  101,   99,
    0,  107,    0,    0,    0,    0,   48,    0,   94,   97,
  103,    0,    0,    0,    0,  105,    0,    0,    0,    0,
    0,    0,   56,    0,   72,   68,   69,   70,   71,   73,
    0,    0,    0,    0,  112,  111,  110,  102,  100,    0,
    0,    0,   57,   39,   55,   58,    0,   41,   38,   98,
    0,    0,    0,    0,    0,    0,    0,    0,  151,    0,
    0,   62,   40,    0,    0,    0,    0,  122,    0,    0,
  118,    0,    0,    0,  150,  152,    0,    0,  120,    0,
    0,  123,    0,  115,  113,    0,  149,  121,    0,  117,
    0,    0,    0,    0,  119,  126,  125,  124,  116,  114,
};
final static short yydgoto[] = {                          3,
   16,   17,   18,   59,   60,   21,  178,   22,   72,   36,
   37,  214,  212,  282,   46,  283,  319,  285,   47,   23,
   24,   25,   26,   27,  290,   48,  107,   49,   50,   61,
  320,   51,   97,   52,  120,  327,
};
final static short yysindex[] = {                      -208,
 -184,  847,    0,  847,  715,  370,  229,    0,  -32,   53,
 -212,    0,  930, -204,    0,  737,    0,    0,   41,    5,
 -134,   27,    0,    0,    0,    0,    0,  759,    0,  781,
 -152, -143,  281,    0,    0,    0,    0, -138,   43,    0,
  480,   34,    0, -154,  -30,    0,  153,   47,    0,  -18,
  414,   88,  -36, -152,    0,    0,  -41,  572,   46,    5,
 -224,   84,   93,  -44,    0,    0,    0,    0,    0,   -9,
  -54,  113,    0,  370,    0,    0, -216, -216,   95,  -83,
   22,   43,  153,  153,    0,    0,    0,    0,    0,    0,
    0,   43,    0,    0,    0,   -6,  -24,    0,  228,    0,
    0,    0,    0,   80,  -14,  135,   -6,   -6,  126,  943,
  -50,  -24,  -15,  198,  577,  177,  809,  -21,   48,  500,
    0,    0,    0,  -16,    0,    0,  163,    0,   39,  267,
  269,    0,  206,  236,    0,  112,  325,  107,  330,    0,
  153,  153,    0, 1220,    0,  831,   -4,  653,  135,  943,
  142,    0,    0,  154,    0,    0, -123,  943,    0, 1220,
    0,  153,  153,    0,    0,    0,  -30,   14,    0,    0,
    0,  356,  -30,   26,    0,  362,    0,   32,  -78,  -78,
  -34,  -34,    0,  192,    0,  258,  277,    0,   43,  661,
  203,    0, -108,  956,    0,    0,  943,  -74,  212,  449,
  429,    0,  -19,  459,  436,    0,  424,    0,  280,  222,
  290,  446,  423,   -1,    0,    4,    0,    0,    0,    0,
    0,    0, 1021,  241,    0,  475,    0,  943,   20, -126,
  252,    0,  943,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  243,  270,  279,  -34,
    0,    0,  454,  490,    0,  285,    0,  969,    0,    0,
  982,    0,  289,  803,  520,  825,    0,  300,    0,    0,
    0,  -51,  298,  -27, -179,    0,  256,   28,  525,  511,
    5,  292,    0,  512,    0,    0,    0,    0,    0,    0,
  311,  288,  397,  539,    0,    0,    0,    0,    0,  -30,
   -8,   43,    0,    0,    0,    0,  803,    0,    0,    0,
   29,  855,  322, 1043,  627,  855,  326,  985,    0,  -69,
  855,    0,    0,  172,  870,  529,  885,    0,  855,  178,
    0,  855,   23, -110,    0,    0,  532,  333,    0,  855,
  334,    0,  900,    0,    0,  915,    0,    0,  336,    0,
   24,  337,   52, -147,    0,    0,    0,    0,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    1,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  608,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   54,    0,    0,    0,    0,   77,    0,    0,    0,
    0,  169,    0,  552,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  612,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  316,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  420,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  339,  100,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  195,
    0,  123,  146,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  374,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  649,
  671,    0,    0,  693,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
  144,   30, -162,  161,   -2,  499,    0,    0,    0,  614,
  617,  441,  453, -232, -144, -253,  419,    0,  -35,  443,
    0,  478,  515,  550,    0,  421,  573,    0,  -40,  -47,
  466,  104,  578,  -39,    0,    0,
};
final static int YYTABLESIZE=1267;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         20,
   11,   20,   20,   84,  105,  106,   35,   45,   44,  104,
   44,   64,   44,   20,   44,  104,   35,  117,  168,  126,
   44,  238,  111,  174,  104,   20,  151,   20,  305,   44,
   35,  123,  313,  293,  129,  189,  215,  215,   44,  305,
   44,    8,  250,   54,   32,   66,  141,  250,    1,  128,
  124,   62,   63,  104,   12,    2,  142,   66,   44,   66,
   15,  305,  157,  148,  149,  104,   33,  300,   69,  317,
   44,    4,   44,   99,  315,  209,  298,   35,  163,    5,
   35,   35,  100,  101,  102,   73,  299,   44,   90,   88,
  208,   89,   53,   91,   85,   85,   85,   85,   85,   68,
   85,  280,  193,  280,  122,  267,  170,   77,  359,   35,
  198,  190,   85,   85,   85,   85,   78,  134,  360,  280,
  134,   82,   70,  251,   44,  103,  199,  201,  252,  259,
  280,  114,  203,  205,   71,  134,  134,  134,  134,  260,
  135,   35,  196,  135,  280,  344,  230,   28,   30,  231,
   40,  261,  280,  223,  197,  345,  136,  227,  135,  135,
  135,  135,   19,  136,   19,   19,  136,  346,  186,  228,
   44,  132,  137,  138,    8,  152,   19,  210,  114,    8,
  256,  136,  136,  136,  136,  263,  133,   12,   19,  133,
   19,  232,   12,   15,   90,   88,  328,   89,   15,   91,
  145,  130,  131,  233,  133,  133,  133,  133,  329,  132,
  273,  125,  158,  275,  295,  159,  161,  165,  119,  115,
   42,  175,  116,   41,   42,   41,   42,  132,  132,  132,
  132,  144,   42,  118,  167,  128,  237,  301,  297,  173,
  160,   42,   44,   43,  110,   43,  127,   43,  150,   43,
   42,   83,   42,  128,  312,   43,   11,   11,   11,  311,
  106,  281,   11,  281,   43,   11,  314,   11,   11,  200,
   42,   11,   44,   43,   11,   43,   11,   11,  140,  281,
  172,  204,   42,   41,   42,  257,   40,  207,  342,  356,
  281,  316,   98,   43,  176,  177,   67,  258,   83,   42,
  343,  121,  108,  169,  281,   43,  179,   43,  180,   85,
   85,   85,  281,   35,   85,   85,   85,  358,   85,   85,
   85,   85,   43,   85,   85,   85,   85,   85,  181,   85,
   85,   85,  134,  134,  134,  144,   42,  134,  134,  134,
   80,  134,  134,  134,  134,   35,  134,  134,  134,  134,
  134,  135,  134,  134,  134,  135,  135,  135,  182,   43,
  135,  135,  135,  185,  135,  135,  135,  135,  183,  135,
  135,  135,  135,  135,   74,  135,  135,  135,  136,  136,
  136,  155,   42,  136,  136,  136,  184,  136,  136,  136,
  136,  187,  136,  136,  136,  136,  136,   75,  136,  136,
  136,  133,  133,  133,  194,   43,  133,  133,  133,  195,
  133,  133,  133,  133,  202,  133,  133,  133,  133,  133,
  206,  133,  133,  133,  132,  132,  132,   35,   34,  132,
  132,  132,  157,  132,  132,  132,  132,  331,  132,  132,
  132,  132,  132,  339,  132,  132,  132,  217,  218,  332,
  128,  128,  128,  162,   42,  340,  128,  128,  226,  128,
  128,  128,  128,  249,  128,  128,  248,  234,  128,  236,
  128,  128,  128,   93,   95,   94,  240,   43,  244,  132,
  132,  132,  241,  146,   42,    8,  247,   38,   39,  235,
   90,   88,   92,   89,  269,   91,  254,  268,   12,  239,
   90,   88,   92,   89,   15,   91,  264,   43,   93,   95,
   94,   31,   32,  219,  220,  255,  143,  262,   93,   95,
   94,   90,   88,   92,   89,  265,   91,  153,  154,  156,
  270,   81,  221,  222,   33,  242,  243,   79,    8,   93,
   95,   94,  266,   31,   32,  245,  246,  277,    7,    8,
  271,   12,  308,  278,  276,  294,  304,   15,   10,   11,
  291,  279,   12,  296,  302,   13,   33,   14,   15,  303,
  306,   74,   74,   74,  307,  133,  134,   74,  139,  310,
   74,   74,   74,   74,  321,   74,   74,  335,  325,   74,
  347,   74,   74,   74,   75,   75,   75,  147,  348,  350,
   75,  355,  357,   75,   75,   75,   75,    3,   75,   75,
   25,    6,   75,   96,   75,   75,   75,  164,   90,   88,
  109,   89,  216,   91,   55,   31,   32,   56,  113,  157,
  157,  157,  213,    0,    0,  157,    0,    0,  157,  157,
  157,  157,    0,  157,  157,    0,    0,  157,   33,  157,
  157,  157,  277,    7,    8,   96,    0,    0,  278,    0,
    0,  309,    0,   10,   11,    0,  279,   12,    0,  112,
   13,    0,   14,   15,   85,  132,    0,  211,  211,    0,
  132,    0,  284,    0,  284,   86,   87,   96,    0,    0,
    0,  132,  132,  192,   90,   88,  191,   89,    0,   91,
  284,  225,   90,   88,  224,   89,  286,   91,  286,   85,
    0,  284,    0,    0,    0,    0,   96,    0,   96,   85,
   86,   87,    0,    0,  286,  284,    0,    0,    0,    0,
   86,   87,   96,  284,   96,  286,  326,    0,    0,    0,
   85,  287,    0,  287,    0,  337,    0,    0,    0,  286,
    0,   86,   87,    0,  286,   62,    7,  286,  286,  287,
  286,    9,    0,  286,  171,    0,   10,  286,    0,  286,
  287,  286,   96,   13,  286,   14,   96,    0,  288,    0,
  288,  324,  286,    0,  287,  286,  330,    0,  286,  287,
  334,    0,  287,  287,  338,  287,  288,  341,  287,    0,
    0,    0,  287,    0,  287,  349,  287,  288,  352,  287,
    0,  354,    0,  289,    0,  289,    0,  287,    0,    0,
  287,  288,    0,  287,    0,    0,  288,   62,    7,  288,
  288,  289,  288,    9,    0,  288,    0,    0,   10,  288,
    0,  288,  289,  288,    0,   13,  288,   14,    0,  166,
   90,   88,    0,   89,  288,   91,  289,  288,    0,    0,
  288,  289,    0,    0,  289,  289,    0,  289,    0,    0,
  289,  188,   90,   88,  289,   89,  289,   91,  289,    0,
    0,  289,  277,    7,    8,    0,    0,    0,  278,  289,
    0,  323,  289,   10,   11,  289,  279,   12,    0,    0,
   13,    0,   14,   15,   60,   60,   60,    0,    0,    0,
   60,    0,    0,   60,    0,   60,   60,    0,   60,   60,
    0,    0,   60,    0,   60,   60,   59,   59,   59,    0,
    0,    0,   59,    0,    0,   59,    0,   59,   59,    0,
   59,   59,    0,    0,   59,    0,   59,   59,   61,   61,
   61,    0,    0,    0,   61,    0,    0,   61,    0,   61,
   61,    0,   61,   61,    0,    0,   61,    0,   61,   61,
    6,    7,    8,    0,    0,    0,    9,    0,    0,   29,
    0,   10,   11,    0,    0,   12,    0,    0,   13,    0,
   14,   15,    6,    7,    8,    0,    0,    0,    9,    0,
    0,   65,    0,   10,   11,    0,    0,   12,    0,    0,
   13,    0,   14,   15,   74,    7,    8,    0,    0,    0,
    9,    0,    0,   75,    0,   10,   11,    0,    0,   12,
    0,    0,   13,    0,   14,   15,    6,    7,    8,    0,
    0,    0,    9,    0,    0,   76,    0,   10,   11,    0,
    0,   12,    0,    0,   13,    0,   14,   15,  277,    7,
    8,  253,   90,   88,  278,   89,    0,   91,    0,   10,
   11,    0,  279,   12,    0,    0,   13,    0,   14,   15,
  292,    7,    8,  322,   90,   88,  278,   89,    0,   91,
    0,   10,   11,    0,  279,   12,    0,    0,   13,    0,
   14,   15,    6,    7,    8,    0,    0,    0,    9,    0,
   62,    7,    0,   10,   11,    0,  278,   12,  318,    0,
   13,   10,   14,   15,  279,  333,    7,    0,   13,    0,
   14,  278,    0,  318,    0,    0,   10,    0,    0,  279,
   62,    7,    0,   13,    0,   14,  278,    0,    0,  336,
    0,   10,    0,    0,  279,  351,    7,    0,   13,    0,
   14,  278,    0,  318,    0,    0,   10,    0,    0,  279,
  353,    7,    0,   13,    0,   14,  278,    0,  318,    0,
    0,   10,    0,    0,  279,   57,    7,    0,   13,    0,
   14,    9,    0,   58,    0,    0,   10,    0,   62,    7,
    0,    0,    0,   13,    9,   14,   58,    0,    0,   10,
    0,  229,    7,    0,    0,    0,   13,    9,   14,   58,
    0,    0,   10,    0,  272,    7,    0,    0,    0,   13,
    9,   14,   58,    0,    0,   10,    0,  274,    7,    0,
   62,    7,   13,    9,   14,   58,  278,    0,   10,    0,
    0,   10,    0,    0,  279,   13,    0,   14,   13,    0,
   14,   90,   88,   92,   89,    0,   91,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                          2,
    0,    4,    5,   39,   45,   45,   58,   40,   45,   40,
   45,   14,   45,   16,   45,   40,   58,   53,   40,   64,
   45,   41,   41,   40,   40,   28,   41,   30,  282,   45,
   58,  256,   41,  266,   44,   40,  181,  182,   45,  293,
   45,  258,   44,  256,  257,   16,   82,   44,  257,   59,
  275,  256,  257,   40,  271,  264,   92,   28,   45,   30,
  277,  315,  110,   99,  104,   40,  279,   40,   64,   41,
   45,  256,   45,   40,  307,   44,  256,   58,  114,  264,
   58,   58,   49,   50,   51,   59,  266,   45,   42,   43,
   59,   45,   40,   47,   41,   42,   43,   44,   45,   59,
   47,  264,  150,  266,   59,  250,   59,  260,  256,   58,
  158,  147,   59,   60,   61,   62,  260,   41,  266,  282,
   44,  260,  257,  125,   45,  280,  167,  168,  125,  256,
  293,   44,  173,  174,  269,   59,   60,   61,   62,  266,
   41,   58,  266,   44,  307,  256,  194,    4,    5,  197,
   58,  278,  315,  189,  278,  266,   62,  266,   59,   60,
   61,   62,    2,   41,    4,    5,   44,  278,   62,  278,
   45,   59,  256,  257,  258,   41,   16,  256,   44,  258,
  228,   59,   60,   61,   62,  233,   41,  271,   28,   44,
   30,  266,  271,  277,   42,   43,  266,   45,  277,   47,
   97,  256,  257,  278,   59,   60,   61,   62,  278,   41,
  258,  256,  263,  261,  266,  112,  113,   41,   58,  256,
  257,   59,  259,  256,  257,  256,  257,   59,   60,   61,
   62,  256,  257,  275,  256,   41,  256,  278,  266,  256,
  256,  257,   45,  280,  263,  280,  256,  280,  263,  280,
  257,  256,  257,   59,  263,  280,  256,  257,  258,  300,
  300,  264,  262,  266,  280,  265,  302,  267,  268,  256,
  257,  271,   45,  280,  274,  280,  276,  277,  257,  282,
  120,  256,  257,  256,  257,  266,   58,  256,  266,  266,
  293,  263,  259,  280,  256,  257,  256,  278,  256,  257,
  278,  256,  256,  256,  307,  280,   40,  280,   40,  256,
  257,  258,  315,   58,  261,  262,  263,  266,  265,  266,
  267,  268,  280,  270,  271,  272,  273,  274,  123,  276,
  277,  278,  256,  257,  258,  256,  257,  261,  262,  263,
   60,  265,  266,  267,  268,   58,  270,  271,  272,  273,
  274,  257,  276,  277,  278,  256,  257,  258,  123,  280,
  261,  262,  263,  257,  265,  266,  267,  268,  257,  270,
  271,  272,  273,  274,   59,  276,  277,  278,  256,  257,
  258,  256,  257,  261,  262,  263,   62,  265,  266,  267,
  268,   62,  270,  271,  272,  273,  274,   59,  276,  277,
  278,  256,  257,  258,  263,  280,  261,  262,  263,  256,
  265,  266,  267,  268,   59,  270,  271,  272,  273,  274,
   59,  276,  277,  278,  256,  257,  258,   58,   59,  261,
  262,  263,   59,  265,  266,  267,  268,  266,  270,  271,
  272,  273,  274,  266,  276,  277,  278,  256,  257,  278,
  256,  257,  258,  256,  257,  278,  262,  263,  256,  265,
  266,  267,  268,   41,  270,  271,   44,  256,  274,   41,
  276,  277,  278,   60,   61,   62,   41,  280,  257,   60,
   61,   62,   59,  256,  257,  258,   41,  259,  260,   41,
   42,   43,   44,   45,   41,   47,  256,   44,  271,   41,
   42,   43,   44,   45,  277,   47,  264,  280,   60,   61,
   62,  256,  257,  256,  257,   41,   96,  266,   60,   61,
   62,   42,   43,   44,   45,  256,   47,  107,  108,  109,
   41,   33,  256,  257,  279,  256,  257,  257,  258,   60,
   61,   62,  264,  256,  257,  256,  257,  256,  257,  258,
  266,  271,  265,  262,  266,  256,  265,  277,  267,  268,
   41,  270,  271,  266,   40,  274,  279,  276,  277,   59,
   59,  256,  257,  258,  264,   77,   78,  262,   80,   41,
  265,  266,  267,  268,  263,  270,  271,   59,  263,  274,
   59,  276,  277,  278,  256,  257,  258,   99,  266,  266,
  262,  266,  266,  265,  266,  267,  268,    0,  270,  271,
   59,    0,  274,   41,  276,  277,  278,   41,   42,   43,
   48,   45,  182,   47,   11,  256,  257,   11,   51,  256,
  257,  258,  180,   -1,   -1,  262,   -1,   -1,  265,  266,
  267,  268,   -1,  270,  271,   -1,   -1,  274,  279,  276,
  277,  278,  256,  257,  258,   83,   -1,   -1,  262,   -1,
   -1,  265,   -1,  267,  268,   -1,  270,  271,   -1,  256,
  274,   -1,  276,  277,  261,  256,   -1,  179,  180,   -1,
  261,   -1,  264,   -1,  266,  272,  273,  115,   -1,   -1,
   -1,  272,  273,   41,   42,   43,   44,   45,   -1,   47,
  282,   41,   42,   43,   44,   45,  264,   47,  266,  261,
   -1,  293,   -1,   -1,   -1,   -1,  144,   -1,  146,  261,
  272,  273,   -1,   -1,  282,  307,   -1,   -1,   -1,   -1,
  272,  273,  160,  315,  162,  293,  318,   -1,   -1,   -1,
  261,  264,   -1,  266,   -1,  327,   -1,   -1,   -1,  307,
   -1,  272,  273,   -1,  312,  256,  257,  315,  316,  282,
  318,  262,   -1,  321,  265,   -1,  267,  325,   -1,  327,
  293,  329,  200,  274,  332,  276,  204,   -1,  264,   -1,
  266,  316,  340,   -1,  307,  343,  321,   -1,  346,  312,
  325,   -1,  315,  316,  329,  318,  282,  332,  321,   -1,
   -1,   -1,  325,   -1,  327,  340,  329,  293,  343,  332,
   -1,  346,   -1,  264,   -1,  266,   -1,  340,   -1,   -1,
  343,  307,   -1,  346,   -1,   -1,  312,  256,  257,  315,
  316,  282,  318,  262,   -1,  321,   -1,   -1,  267,  325,
   -1,  327,  293,  329,   -1,  274,  332,  276,   -1,   41,
   42,   43,   -1,   45,  340,   47,  307,  343,   -1,   -1,
  346,  312,   -1,   -1,  315,  316,   -1,  318,   -1,   -1,
  321,   41,   42,   43,  325,   45,  327,   47,  329,   -1,
   -1,  332,  256,  257,  258,   -1,   -1,   -1,  262,  340,
   -1,  265,  343,  267,  268,  346,  270,  271,   -1,   -1,
  274,   -1,  276,  277,  256,  257,  258,   -1,   -1,   -1,
  262,   -1,   -1,  265,   -1,  267,  268,   -1,  270,  271,
   -1,   -1,  274,   -1,  276,  277,  256,  257,  258,   -1,
   -1,   -1,  262,   -1,   -1,  265,   -1,  267,  268,   -1,
  270,  271,   -1,   -1,  274,   -1,  276,  277,  256,  257,
  258,   -1,   -1,   -1,  262,   -1,   -1,  265,   -1,  267,
  268,   -1,  270,  271,   -1,   -1,  274,   -1,  276,  277,
  256,  257,  258,   -1,   -1,   -1,  262,   -1,   -1,  265,
   -1,  267,  268,   -1,   -1,  271,   -1,   -1,  274,   -1,
  276,  277,  256,  257,  258,   -1,   -1,   -1,  262,   -1,
   -1,  265,   -1,  267,  268,   -1,   -1,  271,   -1,   -1,
  274,   -1,  276,  277,  256,  257,  258,   -1,   -1,   -1,
  262,   -1,   -1,  265,   -1,  267,  268,   -1,   -1,  271,
   -1,   -1,  274,   -1,  276,  277,  256,  257,  258,   -1,
   -1,   -1,  262,   -1,   -1,  265,   -1,  267,  268,   -1,
   -1,  271,   -1,   -1,  274,   -1,  276,  277,  256,  257,
  258,   41,   42,   43,  262,   45,   -1,   47,   -1,  267,
  268,   -1,  270,  271,   -1,   -1,  274,   -1,  276,  277,
  256,  257,  258,   41,   42,   43,  262,   45,   -1,   47,
   -1,  267,  268,   -1,  270,  271,   -1,   -1,  274,   -1,
  276,  277,  256,  257,  258,   -1,   -1,   -1,  262,   -1,
  256,  257,   -1,  267,  268,   -1,  262,  271,  264,   -1,
  274,  267,  276,  277,  270,  256,  257,   -1,  274,   -1,
  276,  262,   -1,  264,   -1,   -1,  267,   -1,   -1,  270,
  256,  257,   -1,  274,   -1,  276,  262,   -1,   -1,  265,
   -1,  267,   -1,   -1,  270,  256,  257,   -1,  274,   -1,
  276,  262,   -1,  264,   -1,   -1,  267,   -1,   -1,  270,
  256,  257,   -1,  274,   -1,  276,  262,   -1,  264,   -1,
   -1,  267,   -1,   -1,  270,  256,  257,   -1,  274,   -1,
  276,  262,   -1,  264,   -1,   -1,  267,   -1,  256,  257,
   -1,   -1,   -1,  274,  262,  276,  264,   -1,   -1,  267,
   -1,  256,  257,   -1,   -1,   -1,  274,  262,  276,  264,
   -1,   -1,  267,   -1,  256,  257,   -1,   -1,   -1,  274,
  262,  276,  264,   -1,   -1,  267,   -1,  256,  257,   -1,
  256,  257,  274,  262,  276,  264,  262,   -1,  267,   -1,
   -1,  267,   -1,   -1,  270,  274,   -1,  276,  274,   -1,
  276,   42,   43,   44,   45,   -1,   47,
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
"sentenciaDeclarativa : tipoDato IDENTIFICADOR ';'",
"sentenciaDeclarativa : tipoDato IDENTIFICADOR ',' listaVariable ';'",
"sentenciaDeclarativa : tipoDato IDENTIFICADOR ',' error ';'",
"sentenciaDeclarativa : tipoDato IDENTIFICADOR ',' listaVariable error ';'",
"sentenciaDeclarativa : tipoDato IDENTIFICADOR error ';'",
"sentenciaDeclarativa : typedefDeclaracion ';'",
"sentenciaDeclarativa : tipoDato funDeclaracion ';'",
"typedefDeclaracion : TYPEDEF declaracionSubtipo",
"typedefDeclaracion : TYPEDEF declaracionTriple",
"typedefDeclaracion : error declaracionSubtipo",
"typedefDeclaracion : error declaracionTriple",
"typedefDeclaracion : TYPEDEF error",
"declaracionSubtipo : IDENTIFICADOR SIMASIGNACION tipoDato '{' listaConstante '}'",
"declaracionSubtipo : error SIMASIGNACION tipoDato '{' listaConstante '}'",
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
"funDeclaracion : FUN IDENTIFICADOR '(' parametro ')' BEGIN cuerpoFuncion END",
"funDeclaracion : FUN error '(' parametro ')' BEGIN cuerpoFuncion END",
"funDeclaracion : FUN IDENTIFICADOR '(' parametro ',' error ')' BEGIN cuerpoFuncion END",
"funDeclaracion : FUN IDENTIFICADOR '(' parametro ')' BEGIN error END",
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
"sentenciaConRet : sentenciaDeclarativa ';'",
"sentenciaConRet : sentenciaEjecutableConRet ';'",
"sentenciaConRet : etiqueta",
"sentenciaConRet : sentenciaDeclarativa",
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
"expresion : operando operador operando",
"expresion : expresion operador operando",
"expresion : operando error operando error",
"expresion : operando operador error",
"expresion : error operador operando",
"operador : '+'",
"operador : '-'",
"operador : '*'",
"operador : '/'",
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
"clausulaSeleccion : IF '(' condicion ')' THEN bloqueSentenciaEjecutable END_IF",
"clausulaSeleccion : IF '(' condicion ')' THEN bloqueSentenciaEjecutable ELSE bloqueSentenciaEjecutable END_IF",
"clausulaSeleccion : IF '(' condicion ')' THEN bloqueSentenciaEjecutable error",
"clausulaSeleccion : IF '(' condicion ')' THEN bloqueSentenciaEjecutable ELSE bloqueSentenciaEjecutable error",
"clausulaSeleccion : IF '(' condicion THEN bloqueSentenciaEjecutable ELSE bloqueSentenciaEjecutable END_IF",
"clausulaSeleccion : IF '(' condicion THEN bloqueSentenciaEjecutable END_IF",
"clausulaSeleccion : IF condicion ')' THEN bloqueSentenciaEjecutable ELSE bloqueSentenciaEjecutable END_IF",
"clausulaSeleccion : IF condicion ')' THEN bloqueSentenciaEjecutable END_IF",
"clausulaSeleccion : IF condicion THEN bloqueSentenciaEjecutable ELSE bloqueSentenciaEjecutable END_IF",
"clausulaSeleccion : IF condicion THEN bloqueSentenciaEjecutable END_IF",
"clausulaSeleccion : IF '(' condicion ')' THEN error END_IF",
"clausulaSeleccion : IF '(' condicion ')' THEN bloqueSentenciaEjecutable ELSE error END_IF",
"clausulaSeleccion : IF '(' condicion ')' THEN error ELSE bloqueSentenciaEjecutable END_IF",
"clausulaSeleccion : IF '(' condicion ')' THEN error ELSE error END_IF",
"clausulaSeleccionConRet : IF '(' condicion ')' THEN bloqueSentenciaEjecutableConRet END_IF",
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
"condicion : listaExpresiones comparador listaExpresiones",
"condicion : listaExpresiones comparador error",
"condicion : error comparador listaExpresiones",
"condicion : listaExpresiones error listaExpresiones",
"listaExpresiones : '(' listaExpresion ')'",
"listaExpresiones : listaExpresion",
"listaExpresion : listaExpresion ',' expresion",
"listaExpresion : expresion",
"listaExpresion : error ',' expresion",
"listaExpresion : listaExpresion ',' error",
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
"bloqueSentenciaEjecutableConRet : sentenciaEjecutableConRet",
"bloqueSentenciaEjecutableConRet : BEGIN cuerpoEjecutableConRet END",
"clausulaBucle : REPEAT bloqueSentenciaEjecutable WHILE '(' condicion ')'",
"clausulaBucle : REPEAT error WHILE '(' condicion ')'",
"clausulaBucle : REPEAT bloqueSentenciaEjecutable WHILE error condicion ')'",
"clausulaBucle : REPEAT bloqueSentenciaEjecutable WHILE '(' error ')'",
"clausulaBucle : REPEAT bloqueSentenciaEjecutable WHILE '(' condicion",
"clausulaBucle : REPEAT bloqueSentenciaEjecutable WHILE error condicion error",
"clausulaBucle : REPEAT error WHILE '(' error ')'",
"clausulaBucle : REPEAT error WHILE error condicion error",
"clausulaBucle : REPEAT bloqueSentenciaEjecutable error",
"goto : GOTO etiqueta '@'",
"goto : GOTO etiqueta error",
"goto : etiqueta '@'",
"etiqueta : IDENTIFICADOR ':'",
"etiqueta : error ':'",
"mensajeSalida : OUTF '(' expresion ')'",
"mensajeSalida : OUTF '(' CADENA_MULTI ')'",
"mensajeSalida : OUTF '(' error ')'",
};

//#line 309 "gramatica.y"
//FUNCIONES
private static AnalizadorLexico lex;
public static List<Error> erroresLexico = new ArrayList<Error>();
public static List<Error> erroresSintactico = new ArrayList<Error>();
public static List<String> estructuras = new ArrayList<String>();

public static int numeroLineaError = -1;
public static void main(String[] args) {
    String filePath = "src/MATRIZ DE TRANSICIONES - Hoja 1.csv";

    int[][] matriz = MatrizTransicion.leerMatrizDesdeCSV(filePath);
    filePath = "src/MATRIZ DE TRANSICIONES - Hoja 2.csv";

    Accion[][] matrizAcciones = MatrizAccion.leerMatrizDesdeCSV(filePath);
    Parser parser = new Parser(true);
    Parser.lex = new AnalizadorLexico("--", matriz, matrizAcciones);
    if (args.length > 1) {
        Parser.lex = new AnalizadorLexico(args[0], matriz, matrizAcciones);

        parser.run();
        for (Error error: erroresLexico){System.out.println(error);}
    } else {
        Parser.lex = new AnalizadorLexico("testeandoErrores7", matriz, matrizAcciones);
        
        parser.run();
        System.out.println("v---------------------------v");
        for (Error error: erroresSintactico){System.out.println(error);}
        for (Error error: erroresLexico){System.out.println(error);}
        for (String estructura: estructuras){System.out.println(estructura);}
        System.out.println("FALTA AGREGAR NUMERO DE LINEA PARA CADA ESTRUCTURA");
        System.out.println("^---------------------------^");
        //System.out.println("No se especifico el archivo a compilar");
    }
    System.out.println(TablaDeSimbolos.imprimir());
}
private int yylex(){
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
      if(Integer.parseInt(contexto.getValor()) > AnalizadorLexico.MAXLONGINT){
        erroresSintactico.add(new Error(
          AnalizadorLexico.getNumeroLinea(),
          Tipo.ERROR,
          "ERROR SINTACTICO excede rangos."
        ));
      }
    } else if (contexto.getTipo() == TablaTipoToken.getTipoToken("single")){
      String numero = contexto.getValor().toString().replace('s', 'e');
      float valor = Float.parseFloat(numero);
      if(valor > AnalizadorLexico.MAXFLOATPOSITIVO){
        erroresSintactico.add(new Error(
          AnalizadorLexico.getNumeroLinea(),
          Tipo.ERROR,
          "ERROR SINTACTICO excede rangos."
        ));
      } else if (valor < AnalizadorLexico.MINFLOATPOSITIVO) {
        erroresSintactico.add(new Error(
          AnalizadorLexico.getNumeroLinea(),
          Tipo.ERROR,
          "ERROR SINTACTICO excede rangos."
        ));}
    } else {
      if((HexFormat.fromHexDigits(contexto.getValor().subSequence(2, contexto.getValor().length()).toString())) > AnalizadorLexico.MAXHEXADECIMAL){
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
//#line 837 "Parser.java"
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
//#line 35 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta 'cuerpo'."));}
break;
case 3:
//#line 36 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta 'END al final del programa'."));}
break;
case 4:
//#line 37 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta BEGIN del programa."));}
break;
case 5:
//#line 38 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta 'nombre del programa'."));}
break;
case 6:
//#line 39 "gramatica.y"
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
//#line 55 "gramatica.y"
{estructuras.add("Declaracion");}
break;
case 15:
//#line 56 "gramatica.y"
{estructuras.add("Declaracion");}
break;
case 16:
//#line 57 "gramatica.y"
{estructuras.add("Declaracion"); erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera IDENTIFICADOR."));}
break;
case 17:
//#line 58 "gramatica.y"
{estructuras.add("Declaracion"); erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta ','."));}
break;
case 18:
//#line 59 "gramatica.y"
{estructuras.add("Declaracion");erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta ','.")); }
break;
case 19:
//#line 61 "gramatica.y"
{estructuras.add("Declaracion de typedef");}
break;
case 20:
//#line 62 "gramatica.y"
{estructuras.add("Declaracion de funcion");}
break;
case 23:
//#line 69 "gramatica.y"
{ erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera 'TYPEDEF antes de la declaracion del subtipo'.")); }
break;
case 24:
//#line 70 "gramatica.y"
{ erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera 'TYPEDEF antes de la declaracion del triple'.")); }
break;
case 25:
//#line 71 "gramatica.y"
{ erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera 'declaracion de subtipo o triple'.")); }
break;
case 27:
//#line 75 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera 'nombre del tipo de dato nuevo'."));}
break;
case 30:
//#line 79 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera 'tipo de objeto a declarar'."));}
break;
case 31:
//#line 80 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera 'nombre de objeto a declarar'."));}
break;
case 32:
//#line 81 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera 'nombre de objeto a declarar'."));}
break;
case 33:
//#line 82 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera 'nombre y tipo de objeto a declarar'."));}
break;
case 34:
//#line 83 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta '<' al inicio del identificador'."));}
break;
case 35:
//#line 84 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta '>' al final del identificador'."));}
break;
case 36:
//#line 85 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO faltan '<>'."));}
break;
case 37:
//#line 86 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO faltan '<>'."));}
break;
case 38:
//#line 89 "gramatica.y"
{if (val_peek(1).sval.equals("false"))erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta return en el cuerpo de la funcion."));}
break;
case 39:
//#line 90 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta nombre de la funcion."));}
break;
case 40:
//#line 91 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO  no puede tener mas de un parametro."));}
break;
case 41:
//#line 92 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta cuerpo con retorno."));}
break;
case 46:
//#line 104 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta identificador despues de la coma."));}
break;
case 50:
//#line 113 "gramatica.y"
{ Contexto contexto = TablaDeSimbolos.getContexto(val_peek(0).sval);
                        System.out.println(val_peek(0).sval);
                        System.out.println(contexto);
                        System.out.println(TablaDeSimbolos.imprimir());
                        chequearRango(contexto);                             /*SOLO SE CHEQUEA EN POSITIVO YA QUE EL MAXIMO DE NEGATIVOS ES MAYOR AL MAXIMO DE POSITIVOS Y YA LO CHEQUEA EL PARSER*/
                      }
break;
case 51:
//#line 119 "gramatica.y"
{
                            Contexto contexto = TablaDeSimbolos.getContexto(val_peek(0).sval);
                            String newLexRef = TablaDeSimbolos.agregarSimbolo("-"+val_peek(0).sval, contexto.getTipo(), contexto.getValor(), contexto.getRefs());
                            /*$2.sval = newLex.*/
                          }
break;
case 53:
//#line 128 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta tipo de dato en el parametro."));}
break;
case 54:
//#line 129 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta nombre en el parametro."));}
break;
case 55:
//#line 132 "gramatica.y"
{ if (val_peek(1).sval.equals("true") || val_peek(0).sval.equals("true")) yyval.sval = "true"; else yyval.sval = "false";}
break;
case 56:
//#line 133 "gramatica.y"
{yyval.sval = val_peek(0).sval;}
break;
case 57:
//#line 136 "gramatica.y"
{yyval.sval = "false";}
break;
case 58:
//#line 137 "gramatica.y"
{yyval.sval = val_peek(1).sval;}
break;
case 59:
//#line 138 "gramatica.y"
{yyval.sval = "false";}
break;
case 60:
//#line 139 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta ';'.")); yyval.sval = "false";}
break;
case 61:
//#line 140 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta ';'." )); yyval.sval = val_peek(0).sval;}
break;
case 62:
//#line 144 "gramatica.y"
{estructuras.add("Retorno");}
break;
case 63:
//#line 148 "gramatica.y"
{estructuras.add("Asignacion");}
break;
case 64:
//#line 149 "gramatica.y"
{estructuras.add("IF");}
break;
case 65:
//#line 150 "gramatica.y"
{estructuras.add("WHILE");}
break;
case 66:
//#line 151 "gramatica.y"
{estructuras.add("GOTO");}
break;
case 67:
//#line 152 "gramatica.y"
{estructuras.add("OUTF");}
break;
case 68:
//#line 154 "gramatica.y"
{estructuras.add("Asignacion"); yyval.sval = "false";}
break;
case 69:
//#line 155 "gramatica.y"
{estructuras.add("WHILE");yyval.sval = "false";}
break;
case 70:
//#line 156 "gramatica.y"
{estructuras.add("GOTO");yyval.sval = "false";}
break;
case 71:
//#line 157 "gramatica.y"
{estructuras.add("OUTF");yyval.sval = "false";}
break;
case 72:
//#line 158 "gramatica.y"
{yyval.sval = "true";}
break;
case 73:
//#line 159 "gramatica.y"
{estructuras.add("IF"); yyval.sval = val_peek(0).sval;}
break;
case 78:
//#line 179 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta un operador"));}
break;
case 79:
//#line 180 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta un operando"));}
break;
case 80:
//#line 181 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta un operando"));}
break;
case 95:
//#line 199 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO numero de expresiones invalido en el llamado a funcion."));}
break;
case 96:
//#line 200 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta expresion en el llamado a funcion."));}
break;
case 97:
//#line 201 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO numero de expresiones invalido en el llamado a funcion."));}
break;
case 98:
//#line 202 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO numero de expresion esinvalido en el llamado a funcion."));}
break;
case 101:
//#line 207 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta 'end_if;'."));}
break;
case 102:
//#line 208 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta 'end_if;'."));}
break;
case 103:
//#line 209 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta ')' al final de la condicion."));}
break;
case 104:
//#line 210 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta ')' al final de la condicion."));}
break;
case 105:
//#line 211 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta '(' al comienzo de la condicion."));}
break;
case 106:
//#line 212 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta '(' al comienzo de la condicion."));}
break;
case 107:
//#line 213 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO faltan '()' en la condicion."));}
break;
case 108:
//#line 214 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO faltan '()' en la condicion."));}
break;
case 109:
//#line 215 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta bloque de sentencias ejecutables valido."));}
break;
case 110:
//#line 216 "gramatica.y"
{yyval.sval = "false";erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta bloque de sentencias ejecutables valido ."));}
break;
case 111:
//#line 217 "gramatica.y"
{yyval.sval = "false";erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta bloque de sentencias ejecutables valido ."));}
break;
case 112:
//#line 218 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO faltan los dos bloques de sentencias ejecutables validas."));}
break;
case 113:
//#line 222 "gramatica.y"
{yyval.sval = "false";}
break;
case 114:
//#line 223 "gramatica.y"
{ if (val_peek(3).sval.equals("true") && val_peek(1).sval.equals("true")) yyval.sval = "true"; else yyval.sval = "false";}
break;
case 115:
//#line 224 "gramatica.y"
{yyval.sval = "false";erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta 'end_if;'."));}
break;
case 116:
//#line 225 "gramatica.y"
{if (val_peek(3).sval.equals("true") && val_peek(1).sval.equals("true")) yyval.sval = "true"; else yyval.sval = "false";erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta 'end_if;'."));}
break;
case 117:
//#line 226 "gramatica.y"
{if (val_peek(3).sval.equals("true") && val_peek(1).sval.equals("true")) yyval.sval = "true"; else yyval.sval = "false";erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta ')' al final de la condicion."));}
break;
case 118:
//#line 227 "gramatica.y"
{yyval.sval = "false";erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta ')' al final de la condicion."));}
break;
case 119:
//#line 228 "gramatica.y"
{if (val_peek(3).sval.equals("true") && val_peek(1).sval.equals("true")) yyval.sval = "true"; else yyval.sval = "false";erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta '(' al comienzo de la condicion."));}
break;
case 120:
//#line 229 "gramatica.y"
{yyval.sval = "false";erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta '(' al comienzo de la condicion."));}
break;
case 121:
//#line 230 "gramatica.y"
{if (val_peek(3).sval.equals("true") && val_peek(1).sval.equals("true")) yyval.sval = "true"; else yyval.sval = "false";erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO faltan '()' en la condicion."));}
break;
case 122:
//#line 231 "gramatica.y"
{yyval.sval = "false";erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO faltan '()' en la condicion."));}
break;
case 123:
//#line 232 "gramatica.y"
{yyval.sval = "false";erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta bloque de sentencias ejecutables valido."));}
break;
case 124:
//#line 233 "gramatica.y"
{yyval.sval = "false";erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta bloque de sentencias ejecutables valido ."));}
break;
case 125:
//#line 234 "gramatica.y"
{yyval.sval = "false";erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta bloque de sentencias ejecutables valido ."));}
break;
case 126:
//#line 235 "gramatica.y"
{yyval.sval = "false";erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO faltan los dos bloques de sentencias ejecutables validas."));}
break;
case 128:
//#line 239 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta listaExpresiones a la derecha del comparador."));}
break;
case 129:
//#line 240 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta listaExpresiones a la izquierda del comparador."));}
break;
case 130:
//#line 242 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta listaExpresiones a la derecha del comparador."));}
break;
case 135:
//#line 253 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta expresion."));}
break;
case 136:
//#line 254 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta ultima expresion."));}
break;
case 145:
//#line 262 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta ';'."));}
break;
case 147:
//#line 267 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta ';'."));}
break;
case 149:
//#line 271 "gramatica.y"
{ if (val_peek(2).sval.equals("true") || val_peek(1).sval.equals("true")) yyval.sval = "true"; else yyval.sval = "false";}
break;
case 150:
//#line 272 "gramatica.y"
{yyval.sval = val_peek(1).sval;}
break;
case 151:
//#line 275 "gramatica.y"
{yyval.sval = val_peek(0).sval;}
break;
case 152:
//#line 276 "gramatica.y"
{yyval.sval = val_peek(1).sval;}
break;
case 154:
//#line 281 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta bloque de sentencias ejecutables."));}
break;
case 155:
//#line 282 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta ( de apertura de condicion."));}
break;
case 156:
//#line 283 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta condicion."));}
break;
case 157:
//#line 284 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta ) de cierre de condicion."));}
break;
case 158:
//#line 285 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta ( ) englobando la condicion."));}
break;
case 159:
//#line 286 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta ( ) englobando la condicion y bloque de sentencias ejecutables."));}
break;
case 160:
//#line 287 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta condicion y bloque de sentencias ejecutables."));}
break;
case 161:
//#line 289 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta while"));}
break;
case 163:
//#line 293 "gramatica.y"
{erroresSintactico.add(new Error(numeroLineaError, Tipo.ERROR, "ERROR SINTACTICO falta '@' luego de los dospuntos."));}
break;
case 164:
//#line 294 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta 'goto' luego de los dospuntos."));}
break;
case 166:
//#line 298 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta 'etiqueta'."));}
break;
case 169:
//#line 304 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera cadena multilinea o expresion en el mensaje de salida."));}
break;
//#line 1435 "Parser.java"
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
