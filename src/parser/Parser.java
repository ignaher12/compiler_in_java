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
   20,   20,   20,   20,   27,   27,   27,   27,   28,   28,
   28,   28,   28,   28,   28,   29,   29,   29,   29,   29,
   29,   29,   22,   22,   22,   22,   22,   22,   22,   22,
   22,   22,   22,   22,   22,   22,   22,   22,   30,   31,
   26,   26,   26,   26,   26,   26,   26,   26,   26,   26,
   26,   26,   26,   26,   26,   26,   33,   33,   33,   33,
   33,   33,   33,   33,   33,   33,   33,   33,   33,   35,
   35,   35,   36,   36,   36,   36,   36,   36,   37,   37,
   37,   32,   32,   32,   38,   38,   34,   34,   34,   23,
   23,   23,   23,   23,   23,   23,   23,   23,   39,   24,
   24,   24,   24,    5,    5,   25,   25,   25,
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
    3,    3,    3,    1,    1,    1,    1,    1,    1,    1,
    1,    2,    2,    2,    2,    4,    5,    7,    6,    4,
    7,    9,    3,    6,    8,    5,    3,    5,    7,    5,
    7,    5,    6,    4,    4,    5,    6,    6,    4,    2,
    7,    7,    9,    9,    7,    9,    8,    6,    8,    6,
    7,    5,    7,    9,    9,    9,   11,    7,    9,   11,
   11,   11,   11,   11,   11,    3,    3,    3,    3,    3,
    1,    3,    1,    1,    1,    1,    1,    1,    3,    2,
    2,    2,    2,    3,    3,    2,    2,    2,    3,    6,
    6,    6,    6,    5,    6,    6,    6,    3,    1,    3,
    3,    3,    2,    2,    1,    4,    4,    4,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,   49,    0,    0,
    0,    0,    0,    0,   47,  179,    0,   48,  185,    0,
    8,    9,    0,   11,    0,    0,    0,    0,   26,    0,
   67,   68,   69,   70,   71,    0,    0,    0,    2,    0,
    0,   13,   27,    0,    0,    0,  183,  184,    0,    0,
    0,   55,    0,    0,   90,    0,   84,   91,    0,    0,
   28,    0,   24,   25,    0,   46,    0,    0,    0,    5,
    7,   12,   10,   14,    0,    0,   21,   20,   23,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    4,    1,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  156,
  157,  158,   85,   86,   87,   88,  153,  154,  155,    0,
    0,   92,    0,   93,   94,   95,   56,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  182,  181,  180,    0,   15,    0,   22,
    0,    0,    0,    0,  120,  107,  103,    0,    0,    0,
    0,    0,  163,  162,  178,    0,   40,    0,    0,    0,
    0,   41,    0,    0,    0,    0,   54,   83,    0,    0,
    0,    0,    0,    0,    0,  119,    0,    0,    0,    0,
   82,   81,    0,    0,    0,  114,    0,  188,  187,  186,
    0,   66,   19,    0,   52,    0,   58,   59,   57,    0,
    0,  115,    0,    0,    0,    0,    0,  161,  160,  164,
    0,    0,    0,   38,    0,   39,    0,    0,   31,    0,
    0,   30,  100,    0,    0,    0,   96,    0,    0,    0,
  110,    0,    0,    0,  112,    0,    0,    0,   17,    0,
   16,    0,    0,    0,    0,    0,  116,  108,  106,    0,
    0,    0,  159,    0,    0,    0,   37,   34,   36,   33,
   35,   32,   29,   53,    0,    0,   97,    0,    0,    0,
    0,  104,    0,    0,    0,    0,    0,    0,    0,  113,
    0,   18,   51,   50,    0,    0,    0,   62,   64,   76,
    0,   61,    0,   72,   73,   74,   75,   77,  118,  117,
  177,  176,  171,  175,  172,  173,  170,    0,    0,   99,
    0,    0,    0,    0,  109,    0,    0,  111,    0,    0,
    0,   45,    0,    0,   42,   60,   63,    0,   98,  101,
    0,    0,    0,    0,    0,  105,    0,    0,   43,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   44,    0,    0,    0,    0,    0,    0,    0,  102,
    0,    0,    0,    0,    0,  139,    0,    0,    0,    0,
    0,  168,  167,  132,    0,    0,    0,    0,    0,    0,
    0,    0,  128,    0,    0,    0,  166,  169,    0,    0,
  130,    0,  140,  142,  144,  141,  143,  145,  137,  122,
    0,    0,  133,    0,  125,  121,    0,  165,  131,    0,
    0,  127,    0,    0,    0,    0,  129,  123,  136,  135,
  134,  126,  124,
};
final static short yydgoto[] = {                          3,
   20,   21,   22,   86,   24,  290,   26,  196,   27,   28,
   29,   43,  166,   30,  143,  291,   55,  292,  357,  179,
  294,   32,  295,  296,  297,  298,  123,   57,   58,   36,
   82,  145,   59,  358,  180,  111,  152,  371,   37,
};
final static short yysindex[] = {                       156,
 -149,  601,    0,  601,  700,  -44,  391,    0, -181,  -38,
  -29, -182,  105,    9,    0,    0,  191,    0,    0,  722,
    0,    0,  -51,    0,    6, -145,  -35,   17,    0,   69,
    0,    0,    0,    0,    0, -100, 1030,  744,    0,  771,
  429,    0,    0,  -77,  283,  -53,    0,    0,   99,  656,
  101,    0,   38,  -31,    0,  650,    0,    0,   25,   35,
    0,   71,    0,    0,  196,    0,   28,  181,    7,    0,
    0,    0,    0,    0,   54,  192,    0,    0,    0,  210,
 1043,  -86,   82,  388,  586,  -22,  -66,  -44,    0,    0,
   -3,  465,  126,   28,  144,  258,  144,   28,  -12,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  -16,
   28,    0,  362,    0,    0,    0,    0,   28,  583,   27,
   28,   28,   47,   49,  948,  165, -124,  795,  392, 1142,
 -181,  210, 1207,    0,    0,    0,  394,    0,  227,    0,
  200,  277,   -1,  -85,    0,    0,    0, 1058,  -36,   28,
   10, 1089,    0,    0,    0,   20,    0,  220,  413,   83,
  426,    0,  144,  -15,  144,  -21,    0,    0,  144, 1325,
  -18,  696,  684,   28,  165,    0,   89,  144,  144,  483,
    0,    0,  144,  144,   98,    0,  948,    0,    0,    0,
  457,    0,    0,  446,    0,  100,    0,    0,    0,  247,
  250,    0, 1071,  254,   60,  -31,   -9,    0,    0,    0,
  463,  -31,    2,    0,  300,    0,  308,  334,    0,  -19,
  -12,    0,    0,   28, 1317,  269,    0,   28,  629,  124,
    0,  948,   55,   13,    0,  948,  280,  284,    0,  494,
    0,  353,  517,  882,  296,  303,    0,    0,    0,  318,
  615,  535,    0,  -24,  622,  544,    0,    0,    0,    0,
    0,    0,    0,    0, 1332,  336,    0,  555,   61,  619,
  647,    0,  948,  332,  144,  144,   28,  731,  335,    0,
  905,    0,    0,    0,  339, -110,   16,    0,    0,    0,
  793,    0,  553,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   26,  581,    0,
   22,  589,   39,  357,    0, 1338,   28,    0,  352,  815,
  905,    0,  -31,   44,    0,    0,    0,  376,    0,    0,
   28,   28,   28,   28,   28,    0,   28,  602,    0,  838,
  593,   46,  515,  386,  611, 1344, 1350, 1356, 1362, 1368,
  348,    0,  398,  515,  403,  285,  109,  157,  515,    0,
   28,   28,   28,   28,   28,    0,  515,  178,  969,  613,
 1074,    0,    0,    0,  515,  185,  378,  441,  449,   86,
  102,  208,    0,  515,  214,  -72,    0,    0,  626,  408,
    0,  515,    0,    0,    0,    0,    0,    0,    0,    0,
  515,  412,    0, 1000,    0,    0, 1015,    0,    0,  430,
  436,    0,  438,  439,  447,  158,    0,    0,    0,    0,
    0,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  714,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   76,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  715,    0,    0,
    0,    0,    0,    0,    0,    0,  492,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  530,    0,  557,    0,    0,    0,  117,    0,
    0,    0,    0,    0,    0,    0,    0,  141,  169,    0,
    0,    0,  202,  261,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  674,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  239,  310,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  860,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  337,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
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
  252,   43,  444, 1166,  490, 1194,  841,    0,    0,  693,
  708,  712,  566,    0,  614, -249,  -73, -163,  897,  -10,
    1,    0,    8,   14,  127,    0,  366,   75,    0,    0,
   -4,  218,  -34,  854,  829,  -55,    0,    0,    0,
};
final static int YYTABLESIZE=1415;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         56,
  124,   54,   31,  207,   31,   31,   53,   73,  118,   33,
   60,   33,   33,   53,   42,   34,  305,   34,   34,  120,
   31,  224,  221,   78,  221,  167,   53,   33,   53,   53,
  118,  320,   53,   34,   97,   53,  154,   31,   31,  201,
   31,  118,  200,  119,   33,   33,   53,   33,   67,  130,
   34,   34,  277,   34,  127,  323,  133,   53,  158,  213,
   53,  332,   71,  124,   74,  126,  329,  176,  209,  328,
  136,  340,   53,   61,   62,   79,    8,    9,  335,   53,
   71,   31,   71,  163,  344,   31,  355,  165,   33,   15,
  167,   53,   33,   53,   34,   18,   41,  139,   34,   53,
  169,  271,  172,  222,  233,  263,    4,  173,   80,  219,
  178,   75,  138,  184,    5,  177,   89,   89,   89,   89,
   89,  185,   89,   13,  234,   31,  397,  326,   35,  233,
   35,   35,   33,   89,   89,   89,   89,   89,   34,   97,
  113,  186,  399,  242,  217,  233,   35,  264,   31,  114,
  115,  116,   31,  187,  322,   33,  326,  147,  241,   33,
  225,   34,   81,   35,   35,   34,   35,  373,   41,  146,
  230,  250,  252,  234,  147,  147,  326,  254,  256,  147,
  202,  149,   94,  405,  168,  105,  103,   31,  104,  155,
  106,  148,  203,  406,   33,   56,   56,  182,  149,  149,
   34,   56,   56,   31,   72,  407,   98,   35,  156,  151,
   33,   35,  151,  265,  311,  313,   34,   50,   51,  206,
   77,   99,  276,  278,   50,   51,  151,  151,  151,  151,
  151,  304,   31,  153,   41,  132,   31,   95,   51,   33,
   51,   52,  148,   33,  134,   34,  251,   51,   52,   34,
  140,   35,  324,  157,   87,   38,   40,  255,   51,  148,
  148,   52,  135,   52,   52,  208,  316,   52,   95,   51,
   52,   50,   51,   31,   35,  212,   56,  331,   35,  152,
   33,   52,  152,   95,   51,  124,   34,  125,  342,  125,
  128,   51,   52,  129,  334,   52,  152,  152,  152,  152,
  152,  146,  181,   51,  183,   51,  343,   52,  354,  137,
  275,   51,  341,   35,   52,  248,  270,  117,  146,  146,
  346,  347,  348,  349,  350,  249,   52,   53,   52,   35,
  131,   89,   89,   89,   52,   89,   89,   89,   89,  216,
   89,  396,   89,   89,   89,   89,   89,   89,   89,   89,
  150,   89,   89,  150,  231,  240,  149,  398,   35,  112,
   65,   66,   35,  235,  372,  205,  232,  150,  150,  150,
  150,  150,  147,  147,  147,  236,  147,  138,  147,  147,
  164,  147,  162,  147,  147,  147,  147,  147,  366,  272,
  147,  233,  147,  147,  138,  138,  149,  149,  149,   35,
  149,  273,  149,  149,  237,  149,   53,  149,  149,  149,
  149,  149,    1,  422,  149,  110,  149,  149,  393,    2,
  246,  233,  374,  423,  151,  151,  151,  125,  151,  151,
  151,  151,  189,  151,  375,  151,  151,  151,  151,  151,
  151,  151,  151,  383,  151,  151,   68,   69,   48,  274,
  391,   47,  193,  279,   47,  384,  197,  148,  148,  148,
  110,  148,  392,  148,  148,  141,  148,    8,  148,  148,
  148,  148,  148,  400,  215,  148,  214,  148,  148,  403,
   15,  394,  194,  195,  233,  401,   18,  218,   92,  395,
  314,  404,  233,  110,  152,  152,  152,  238,  152,  152,
  152,  152,  243,  152,  239,  152,  152,  152,  152,  152,
  152,  152,  152,  244,  152,  152,  146,  146,  146,  247,
  146,  253,  146,  146,  268,  146,  233,  146,  146,  146,
  146,  146,  198,  199,  146,  110,  146,  146,   95,   51,
    8,   84,  107,  109,  108,  280,  287,  281,  110,   78,
   78,   11,  282,   15,   14,  257,  258,  285,   16,   18,
   17,  299,   52,  259,  260,  150,  150,  150,  300,  150,
  150,  150,  150,  301,  150,  303,  150,  150,  150,  150,
  150,  150,  150,  150,  307,  150,  150,   79,   79,  261,
  262,  309,  138,  138,  138,  310,  138,  315,  138,  138,
  318,  138,  321,  138,  138,  138,  138,  138,  283,  284,
  138,  327,  138,  138,   80,   80,  110,  170,   51,    8,
  110,  330,  336,  175,  105,  103,  174,  104,  333,  106,
   41,  345,   15,  353,  105,  103,  174,  104,   18,  106,
  110,   52,  107,  109,  108,  233,   44,  150,  359,   44,
   45,  360,  107,  109,  108,  302,  105,  103,   19,  104,
  367,  106,  306,  105,  103,  369,  104,   46,  106,  271,
   46,  387,  233,  409,  107,  109,  108,  412,  107,  109,
  108,  107,  109,  108,  408,   91,    8,  288,  107,  109,
  108,  105,  103,  122,  104,  417,  106,  105,  103,   15,
  104,  418,  106,  419,  420,   18,  107,  109,  108,  107,
  109,  108,  421,    3,    6,  107,  109,  108,   76,   63,
  159,  160,    8,   64,  288,  105,  103,  228,  104,  220,
  106,  174,  174,  289,  288,   15,  227,  105,  103,  226,
  104,   18,  106,  100,    0,  191,    0,   78,   78,   78,
    0,   78,    0,   78,  101,  102,   78,   19,   78,   78,
   78,   78,   78,  288,  288,   78,    0,   78,   78,    0,
  289,   84,  105,  103,  317,  104,  287,  106,  356,   19,
  289,   11,    0,  288,   14,   79,   79,   79,   16,   79,
   17,   79,    0,    0,   79,    0,   79,   79,   79,   79,
   79,   19,    0,   79,    0,   79,   79,    0,    0,  289,
  289,    0,   80,   80,   80,    0,   80,    0,   80,    0,
    0,   80,    0,   80,   80,   80,   80,   80,   19,  289,
   80,    0,   80,   80,    0,  188,  105,  103,  121,  104,
    0,  106,   84,  100,    0,    0,    0,   10,  121,   49,
   19,    0,   11,  100,  101,  102,    6,    7,    8,   16,
    9,   17,   10,    0,  101,  102,    0,   11,   12,   13,
   14,   15,   19,    0,   16,  100,   17,   18,    0,  100,
    0,   93,  100,    0,  270,   96,  101,  102,    0,  100,
  101,  102,    0,  101,  102,   19,    0,    0,    0,    0,
  101,  102,  312,    0,    0,  121,    0,  100,    0,    0,
  100,    0,    0,    0,    0,    0,  100,   65,  101,  102,
  142,  101,  102,    0,    0,    0,    0,  101,  102,  174,
  174,  174,  161,  174,    0,  174,    0,    0,  174,   19,
  174,  174,  174,  174,  174,    0,    0,  174,    0,  174,
  174,    0,    0,  171,    0,    6,    7,    8,    0,    9,
    0,   10,   19,    0,   39,    0,   11,   12,   13,   14,
   15,   96,  142,   16,    0,   17,   18,    6,    7,    8,
    0,    9,    0,   10,    0,    0,   70,    0,   11,   12,
   13,   14,   15,    0,    0,   16,    0,   17,   18,   88,
    7,    8,  229,    9,    0,   10,    0,    0,   89,    0,
   11,   12,   13,   14,   15,    0,    0,   16,    0,   17,
   18,    0,    0,    0,    0,    0,    6,    7,    8,    0,
    9,    0,   10,    0,    0,   90,    0,   11,   12,   13,
   14,   15,    0,    0,   16,    0,   17,   18,  319,    7,
    8,    0,    9,    0,  287,    0,  269,  325,    0,   11,
   12,   13,   14,   15,    0,    0,   16,    0,   17,   18,
  319,    7,    8,    0,    9,    0,  287,    0,    0,  339,
    0,   11,   12,   13,   14,   15,    0,    0,   16,    0,
   17,   18,    0,  319,    7,    8,    0,    9,    0,  287,
    0,    0,  352,    0,   11,   12,   13,   14,   15,    0,
    0,   16,    0,   17,   18,   65,   65,   65,    0,   65,
    0,   65,    0,    0,   65,    0,   65,   65,   65,   65,
   65,    0,    0,   65,    0,   65,   65,  286,    7,    8,
  293,    9,    0,  287,    0,  338,    0,    0,   11,   12,
   13,   14,   15,    0,    0,   16,    0,   17,   18,    0,
  319,    7,    8,    0,    9,  351,  287,   23,    0,   23,
   23,   11,   12,   13,   14,   15,    0,  293,   16,    0,
   17,   18,  190,  105,  103,   23,  104,  293,  106,  377,
  378,  379,  380,  381,    0,   25,    0,   25,   25,    0,
    0,    0,    0,   23,   84,   23,    0,  368,    0,   10,
    0,   85,  376,   25,   11,    0,  293,  293,    0,    0,
  382,   16,  386,   17,  385,   84,    0,    0,  390,    0,
  287,   25,  356,   25,    0,   11,  293,  402,   14,    0,
    0,    0,   16,    0,   17,  410,    0,  192,  105,  103,
  151,  104,  370,  106,  411,  413,   84,  414,    0,    0,
  416,  287,    0,  356,    0,    0,   11,  389,    0,   14,
  415,   84,    0,   16,    0,   17,  287,    0,  356,    0,
    0,   11,    0,    0,   14,   83,   84,    0,   16,    0,
   17,   10,    0,   85,    0,    0,   11,    0,  144,   84,
    0,    0,    0,   16,   10,   17,   85,    0,    0,   11,
    0,    0,    0,  204,   84,    0,   16,  211,   17,   10,
    0,   85,    0,    0,   11,    0,  245,   84,    0,    0,
   84,   16,   10,   17,   85,  287,    0,   11,  388,    0,
   11,    0,    0,   14,   16,   84,   17,   16,    0,   17,
   10,    0,    0,  210,    0,   11,    0,  267,  105,  103,
  266,  104,   16,  106,   17,  223,  105,  103,    0,  104,
    0,  106,  308,  105,  103,    0,  104,    0,  106,  105,
  103,  337,  104,    0,  106,  105,  103,  361,  104,    0,
  106,  105,  103,  362,  104,    0,  106,  105,  103,  363,
  104,    0,  106,  105,  103,  364,  104,    0,  106,  105,
  103,  365,  104,    0,  106,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         10,
   56,   40,    2,   40,    4,    5,   45,   59,   40,    2,
   40,    4,    5,   45,   59,    2,   41,    4,    5,   54,
   20,   40,   44,   59,   44,   99,   45,   20,   45,   45,
   40,  281,   45,   20,   45,   45,   59,   37,   38,   41,
   40,   40,   44,   54,   37,   38,   45,   40,   40,   60,
   37,   38,   40,   40,   59,   40,   67,   45,   62,   40,
   45,   40,   20,  119,   59,   41,   41,   41,   59,   44,
   64,  321,   45,  256,  257,   59,  258,  260,   40,   45,
   38,   81,   40,   94,   41,   85,   41,   98,   81,  271,
  164,   45,   85,   45,   81,  277,  279,   44,   85,   45,
  111,   41,  113,  125,   44,  125,  256,  118,   40,  125,
  121,  257,   59,  124,  264,  120,   41,   42,   43,   44,
   45,  126,   47,  269,  180,  125,   41,  291,    2,   44,
    4,    5,  125,   58,   59,   60,   61,   62,  125,  150,
   40,  266,   41,   44,   62,   44,   20,  221,  148,   49,
   50,   51,  152,  278,  265,  148,  320,   41,   59,  152,
  171,  148,  263,   37,   38,  152,   40,   59,  279,  256,
  175,  206,  207,  229,   58,   59,  340,  212,  213,  266,
  266,   41,  260,  256,  110,   42,   43,  187,   45,  256,
   47,  278,  278,  266,  187,  206,  207,  123,   58,   59,
  187,  212,  213,  203,  256,  278,  260,   81,  275,   41,
  203,   85,   44,  224,  270,  271,  203,  256,  257,  256,
  256,  123,  233,  234,  256,  257,   58,   59,   60,   61,
   62,  256,  232,  256,  279,   40,  236,  256,  257,  232,
  257,  280,   41,  236,   64,  232,  256,  257,  280,  236,
   59,  125,  287,  257,   37,    4,    5,  256,  257,   58,
   59,  280,  256,  280,  280,  256,  277,  280,  256,  257,
  280,  256,  257,  273,  148,  256,  287,  256,  152,   41,
  273,  280,   44,  256,  257,  341,  273,  263,  323,  263,
  256,  257,  280,  259,  256,  280,   58,   59,   60,   61,
   62,   41,  256,  257,  256,  257,  263,  280,  263,  256,
  256,  257,  323,  187,  280,  256,  256,  280,   58,   59,
  331,  332,  333,  334,  335,  266,  280,   45,  280,  203,
  260,  256,  257,  258,  280,  260,  261,  262,  263,  257,
  265,  256,  267,  268,  269,  270,  271,  272,  273,  274,
   41,  276,  277,   44,  266,  256,  275,  256,  232,  259,
  256,  257,  236,  266,  256,  148,  278,   58,   59,   60,
   61,   62,  256,  257,  258,  278,  260,   41,  262,  263,
  123,  265,  257,  267,  268,  269,  270,  271,   41,  266,
  274,   44,  276,  277,   58,   59,  256,  257,  258,  273,
  260,  278,  262,  263,  187,  265,   45,  267,  268,  269,
  270,  271,  257,  256,  274,   50,  276,  277,   41,  264,
  203,   44,  266,  266,  256,  257,  258,  263,  260,  261,
  262,  263,   41,  265,  278,  267,  268,  269,  270,  271,
  272,  273,  274,  266,  276,  277,  256,  257,   58,  232,
  266,   64,   59,  236,   64,  278,  257,  256,  257,  258,
   95,  260,  278,  262,  263,  256,  265,  258,  267,  268,
  269,  270,  271,  266,   62,  274,  257,  276,  277,  266,
  271,   41,  256,  257,   44,  278,  277,   62,   60,   41,
  273,  278,   44,  128,  256,  257,  258,   41,  260,  261,
  262,  263,  256,  265,   59,  267,  268,  269,  270,  271,
  272,  273,  274,  264,  276,  277,  256,  257,  258,  266,
  260,   59,  262,  263,  256,  265,   44,  267,  268,  269,
  270,  271,  256,  257,  274,  170,  276,  277,  256,  257,
  258,  257,   60,   61,   62,  266,  262,  264,  183,   58,
   59,  267,   59,  271,  270,  256,  257,   41,  274,  277,
  276,  266,  280,  256,  257,  256,  257,  258,  266,  260,
  261,  262,  263,  256,  265,   41,  267,  268,  269,  270,
  271,  272,  273,  274,   41,  276,  277,   58,   59,  256,
  257,  256,  256,  257,  258,   41,  260,  266,  262,  263,
  266,  265,  264,  267,  268,  269,  270,  271,  256,  257,
  274,   59,  276,  277,   58,   59,  251,  256,  257,  258,
  255,   41,  266,   41,   42,   43,   44,   45,   40,   47,
  279,  256,  271,   41,   42,   43,   44,   45,  277,   47,
  275,  280,   60,   61,   62,   44,  259,  260,  263,  259,
  260,   41,   60,   61,   62,   41,   42,   43,   58,   45,
  263,   47,   41,   42,   43,  263,   45,  280,   47,   41,
  280,   59,   44,  266,   60,   61,   62,  266,   60,   61,
   62,   60,   61,   62,   59,  257,  258,  244,   60,   61,
   62,   42,   43,   44,   45,  266,   47,   42,   43,  271,
   45,  266,   47,  266,  266,  277,   60,   61,   62,   60,
   61,   62,  266,    0,    0,   60,   61,   62,   26,   12,
  256,  257,  258,   12,  281,   42,   43,   44,   45,  164,
   47,   58,   59,  244,  291,  271,   41,   42,   43,   44,
   45,  277,   47,  261,   -1,  132,   -1,  256,  257,  258,
   -1,  260,   -1,  262,  272,  273,  265,   58,  267,  268,
  269,  270,  271,  320,  321,  274,   -1,  276,  277,   -1,
  281,  257,   42,   43,   44,   45,  262,   47,  264,   58,
  291,  267,   -1,  340,  270,  256,  257,  258,  274,  260,
  276,  262,   -1,   -1,  265,   -1,  267,  268,  269,  270,
  271,   58,   -1,  274,   -1,  276,  277,   -1,   -1,  320,
  321,   -1,  256,  257,  258,   -1,  260,   -1,  262,   -1,
   -1,  265,   -1,  267,  268,  269,  270,  271,   58,  340,
  274,   -1,  276,  277,   -1,   41,   42,   43,  256,   45,
   -1,   47,  257,  261,   -1,   -1,   -1,  262,  256,    9,
   58,   -1,  267,  261,  272,  273,  256,  257,  258,  274,
  260,  276,  262,   -1,  272,  273,   -1,  267,  268,  269,
  270,  271,   58,   -1,  274,  261,  276,  277,   -1,  261,
   -1,   41,  261,   -1,  256,   45,  272,  273,   -1,  261,
  272,  273,   -1,  272,  273,   58,   -1,   -1,   -1,   -1,
  272,  273,  256,   -1,   -1,  256,   -1,  261,   -1,   -1,
  261,   -1,   -1,   -1,   -1,   -1,  261,   58,  272,  273,
   80,  272,  273,   -1,   -1,   -1,   -1,  272,  273,  256,
  257,  258,   92,  260,   -1,  262,   -1,   -1,  265,   58,
  267,  268,  269,  270,  271,   -1,   -1,  274,   -1,  276,
  277,   -1,   -1,  113,   -1,  256,  257,  258,   -1,  260,
   -1,  262,   58,   -1,  265,   -1,  267,  268,  269,  270,
  271,  131,  132,  274,   -1,  276,  277,  256,  257,  258,
   -1,  260,   -1,  262,   -1,   -1,  265,   -1,  267,  268,
  269,  270,  271,   -1,   -1,  274,   -1,  276,  277,  256,
  257,  258,  174,  260,   -1,  262,   -1,   -1,  265,   -1,
  267,  268,  269,  270,  271,   -1,   -1,  274,   -1,  276,
  277,   -1,   -1,   -1,   -1,   -1,  256,  257,  258,   -1,
  260,   -1,  262,   -1,   -1,  265,   -1,  267,  268,  269,
  270,  271,   -1,   -1,  274,   -1,  276,  277,  256,  257,
  258,   -1,  260,   -1,  262,   -1,  228,  265,   -1,  267,
  268,  269,  270,  271,   -1,   -1,  274,   -1,  276,  277,
  256,  257,  258,   -1,  260,   -1,  262,   -1,   -1,  265,
   -1,  267,  268,  269,  270,  271,   -1,   -1,  274,   -1,
  276,  277,   -1,  256,  257,  258,   -1,  260,   -1,  262,
   -1,   -1,  265,   -1,  267,  268,  269,  270,  271,   -1,
   -1,  274,   -1,  276,  277,  256,  257,  258,   -1,  260,
   -1,  262,   -1,   -1,  265,   -1,  267,  268,  269,  270,
  271,   -1,   -1,  274,   -1,  276,  277,  256,  257,  258,
  244,  260,   -1,  262,   -1,  317,   -1,   -1,  267,  268,
  269,  270,  271,   -1,   -1,  274,   -1,  276,  277,   -1,
  256,  257,  258,   -1,  260,  337,  262,    2,   -1,    4,
    5,  267,  268,  269,  270,  271,   -1,  281,  274,   -1,
  276,  277,   41,   42,   43,   20,   45,  291,   47,  361,
  362,  363,  364,  365,   -1,    2,   -1,    4,    5,   -1,
   -1,   -1,   -1,   38,  257,   40,   -1,  354,   -1,  262,
   -1,  264,  359,   20,  267,   -1,  320,  321,   -1,   -1,
  367,  274,  369,  276,  256,  257,   -1,   -1,  375,   -1,
  262,   38,  264,   40,   -1,  267,  340,  384,  270,   -1,
   -1,   -1,  274,   -1,  276,  392,   -1,   41,   42,   43,
   85,   45,  356,   47,  401,  256,  257,  404,   -1,   -1,
  407,  262,   -1,  264,   -1,   -1,  267,  371,   -1,  270,
  256,  257,   -1,  274,   -1,  276,  262,   -1,  264,   -1,
   -1,  267,   -1,   -1,  270,  256,  257,   -1,  274,   -1,
  276,  262,   -1,  264,   -1,   -1,  267,   -1,  256,  257,
   -1,   -1,   -1,  274,  262,  276,  264,   -1,   -1,  267,
   -1,   -1,   -1,  256,  257,   -1,  274,  152,  276,  262,
   -1,  264,   -1,   -1,  267,   -1,  256,  257,   -1,   -1,
  257,  274,  262,  276,  264,  262,   -1,  267,  265,   -1,
  267,   -1,   -1,  270,  274,  257,  276,  274,   -1,  276,
  262,   -1,   -1,  265,   -1,  267,   -1,   41,   42,   43,
   44,   45,  274,   47,  276,   41,   42,   43,   -1,   45,
   -1,   47,   41,   42,   43,   -1,   45,   -1,   47,   42,
   43,   44,   45,   -1,   47,   42,   43,   44,   45,   -1,
   47,   42,   43,   44,   45,   -1,   47,   42,   43,   44,
   45,   -1,   47,   42,   43,   44,   45,   -1,   47,   42,
   43,   44,   45,   -1,   47,
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
"expresion : expresion operador operando",
"expresion : expresion operador error",
"expresion : error operador operando",
"expresion : operando",
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

//#line 322 "gramatica.y"
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
        Parser.lex = new AnalizadorLexico("tests3/test3", matriz, matrizAcciones);
        
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
//#line 1049 "Parser.java"
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
{ if (!val_peek(2).sval.equals("[1]") && !val_peek(2).sval.equals("[2]") && !val_peek(2).sval.equals("[3]")) erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO rango invalido, se espera entre 1 y 3."));Integer lastRef = TablaDeSimbolos.getContexto(val_peek(3).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"Sentencia de Asignacion");}
break;
case 80:
//#line 164 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO faltan '[]' en el rango")); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(3).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"Sentencia de Asignacion");}
break;
case 81:
//#line 167 "gramatica.y"
{yyval.sval = agregarTerceto(val_peek(1).sval, val_peek(2).sval, val_peek(0).sval);}
break;
case 82:
//#line 168 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta operando en la expresion"));}
break;
case 83:
//#line 169 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta operando en la expresion"));}
break;
case 85:
//#line 173 "gramatica.y"
{yyval.sval = "+";}
break;
case 86:
//#line 174 "gramatica.y"
{yyval.sval = "-";}
break;
case 87:
//#line 175 "gramatica.y"
{yyval.sval = "*";}
break;
case 88:
//#line 176 "gramatica.y"
{yyval.sval = "/";}
break;
case 89:
//#line 179 "gramatica.y"
{yyval.sval = val_peek(0).sval; chequearDeclarado(val_peek(0).sval);}
break;
case 96:
//#line 188 "gramatica.y"
{ chequearDeclarado(val_peek(3).sval); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(3).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"Invocacion de Funcion");}
break;
case 97:
//#line 189 "gramatica.y"
{ chequearDeclarado(val_peek(4).sval); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(4).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"Invocacion de Funcion");}
break;
case 98:
//#line 190 "gramatica.y"
{ chequearDeclarado(val_peek(6).sval); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(6).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"Invocacion de Funcion");}
break;
case 99:
//#line 191 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO numero de expresiones invalido en el llamado a funcion.")); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(5).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"Invocacion de Funcion");}
break;
case 100:
//#line 192 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta expresion en el llamado a funcion.")); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(3).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"Invocacion de Funcion");}
break;
case 101:
//#line 193 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO numero de expresiones invalido en el llamado a funcion.")); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(6).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"Invocacion de Funcion");}
break;
case 102:
//#line 194 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO numero de expresion esinvalido en el llamado a funcion.")); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(8).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"Invocacion de Funcion");}
break;
case 103:
//#line 197 "gramatica.y"
{String aux = tercetosIncompletos.pop();tercetos.get(conversionIndexStoI(aux)).setT3(((Integer)tercetos.size()).toString()); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(2).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"IF");}
break;
case 104:
//#line 198 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera una condicion valida")); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(5).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"IF");}
break;
case 105:
//#line 199 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera una condicion valida")); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(7).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"IF");;}
break;
case 106:
//#line 200 "gramatica.y"
{String aux = tercetosIncompletos.pop();tercetos.get(conversionIndexStoI(aux)).setT3(((Integer)tercetos.size()).toString()); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(4).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"IF");}
break;
case 107:
//#line 201 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta 'end_if;'.")); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(2).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"IF");}
break;
case 108:
//#line 202 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta 'end_if;'.")); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(4).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"IF");}
break;
case 109:
//#line 203 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta ')' al final de la condicion.")); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(6).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"IF");}
break;
case 110:
//#line 204 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta ')' al final de la condicion.")); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(4).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"IF");}
break;
case 111:
//#line 205 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta '(' al comienzo de la condicion.")); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(6).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"IF");}
break;
case 112:
//#line 206 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta '(' al comienzo de la condicion.")); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(4).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"IF");}
break;
case 113:
//#line 207 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO faltan '()' en la condicion.")); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(5).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"IF");}
break;
case 114:
//#line 208 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO faltan '()' en la condicion.")); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(3).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"IF");}
break;
case 115:
//#line 209 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta bloque de sentencias ejecutables valido.")); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(3).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"IF");}
break;
case 116:
//#line 210 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta bloque de sentencias ejecutables valido .")); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(4).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"IF");}
break;
case 117:
//#line 211 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta bloque de sentencias ejecutables valido .")); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(5).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"IF");}
break;
case 118:
//#line 212 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO faltan los dos bloques de sentencias ejecutables validas.")); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(5).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"IF");}
break;
case 119:
//#line 215 "gramatica.y"
{tercetosIncompletos.add(agregarTerceto("BF", val_peek(1).sval, ""));}
break;
case 120:
//#line 218 "gramatica.y"
{String incompleto = agregarTerceto("BI", "", "");String aux = tercetosIncompletos.pop();tercetos.get(conversionIndexStoI(aux)).setT3(((Integer)tercetos.size()).toString()); tercetosIncompletos.add(incompleto);}
break;
case 121:
//#line 221 "gramatica.y"
{yyval.sval = "false"; Integer lastRef = TablaDeSimbolos.getContexto(val_peek(6).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"IF");}
break;
case 122:
//#line 222 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera una condicion valida")); yyval.sval = "false";  Integer lastRef = TablaDeSimbolos.getContexto(val_peek(6).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"IF");}
break;
case 123:
//#line 223 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera una condicion valida"));if (val_peek(3).sval.equals("true") && val_peek(1).sval.equals("true")) yyval.sval = "true"; else yyval.sval = "false"; Integer lastRef = TablaDeSimbolos.getContexto(val_peek(8).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"IF");}
break;
case 124:
//#line 224 "gramatica.y"
{ if (val_peek(3).sval.equals("true") && val_peek(1).sval.equals("true")) yyval.sval = "true"; else yyval.sval = "false"; Integer lastRef = TablaDeSimbolos.getContexto(val_peek(8).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"IF");}
break;
case 125:
//#line 225 "gramatica.y"
{yyval.sval = "false";erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta 'end_if;'.")); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(6).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"IF");}
break;
case 126:
//#line 226 "gramatica.y"
{if (val_peek(3).sval.equals("true") && val_peek(1).sval.equals("true")) yyval.sval = "true"; else yyval.sval = "false";erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta 'end_if;'.")); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(8).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"IF");}
break;
case 127:
//#line 227 "gramatica.y"
{if (val_peek(3).sval.equals("true") && val_peek(1).sval.equals("true")) yyval.sval = "true"; else yyval.sval = "false";erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta ')' al final de la condicion.")); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(7).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"IF");}
break;
case 128:
//#line 228 "gramatica.y"
{yyval.sval = "false";erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta ')' al final de la condicion.")); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(5).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"IF");}
break;
case 129:
//#line 229 "gramatica.y"
{if (val_peek(3).sval.equals("true") && val_peek(1).sval.equals("true")) yyval.sval = "true"; else yyval.sval = "false";erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta '(' al comienzo de la condicion.")); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(7).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"IF");}
break;
case 130:
//#line 230 "gramatica.y"
{yyval.sval = "false";erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta '(' al comienzo de la condicion.")); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(5).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"IF");}
break;
case 131:
//#line 231 "gramatica.y"
{if (val_peek(3).sval.equals("true") && val_peek(1).sval.equals("true")) yyval.sval = "true"; else yyval.sval = "false";erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO faltan '()' en la condicion.")); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(6).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"IF");}
break;
case 132:
//#line 232 "gramatica.y"
{yyval.sval = "false";erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO faltan '()' en la condicion.")); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(4).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"IF");}
break;
case 133:
//#line 233 "gramatica.y"
{yyval.sval = "false";erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta bloque de sentencias ejecutables valido.")); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(6).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"IF");}
break;
case 134:
//#line 234 "gramatica.y"
{yyval.sval = "false";erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta bloque de sentencias ejecutables valido .")); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(8).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"IF");}
break;
case 135:
//#line 235 "gramatica.y"
{yyval.sval = "false";erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta bloque de sentencias ejecutables valido .")); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(8).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"IF");}
break;
case 136:
//#line 236 "gramatica.y"
{yyval.sval = "false";erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO faltan los dos bloques de sentencias ejecutables validas.")); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(8).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"IF");}
break;
case 137:
//#line 239 "gramatica.y"
{((ArrayList<String>)val_peek(7).obj).add(val_peek(9).sval); ((ArrayList<String>)val_peek(1).obj).add(val_peek(3).sval); yyval.sval = agregaListaExpresionTercetos(val_peek(5).sval, ((ArrayList<String>)val_peek(7).obj), ((ArrayList<String>)val_peek(1).obj));}
break;
case 138:
//#line 240 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO faltan '( )' a las listas de expresiones."));}
break;
case 139:
//#line 241 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO faltan parentesis en las listas de expresiones."));}
break;
case 140:
//#line 242 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO faltan parentesis en las listas de expresiones."));}
break;
case 141:
//#line 243 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO faltan parentesis en las listas de expresiones."));}
break;
case 142:
//#line 244 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO faltan parentesis en las listas de expresiones."));}
break;
case 143:
//#line 245 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO faltan parentesis en las listas de expresiones."));}
break;
case 144:
//#line 246 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera un comparador."));}
break;
case 145:
//#line 247 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO faltan parentesis en las listas de expresiones."));}
break;
case 146:
//#line 249 "gramatica.y"
{yyval.sval = agregarTerceto(val_peek(1).sval, val_peek(2).sval, val_peek(0).sval);}
break;
case 147:
//#line 250 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera una expresion a la izquierda del comparador."));}
break;
case 148:
//#line 251 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera una expresion a la derecha del comparador."));}
break;
case 149:
//#line 252 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera un comparador."));}
break;
case 150:
//#line 255 "gramatica.y"
{((ArrayList<String>)val_peek(2).obj).add(val_peek(0).sval);}
break;
case 151:
//#line 256 "gramatica.y"
{ArrayList<String> aux = new ArrayList<String>(); aux.add(val_peek(0).sval); yyval.obj = aux;}
break;
case 152:
//#line 257 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera una expresion despues de la coma."));}
break;
case 153:
//#line 260 "gramatica.y"
{yyval.sval = "<";}
break;
case 154:
//#line 261 "gramatica.y"
{yyval.sval = ">";}
break;
case 155:
//#line 262 "gramatica.y"
{yyval.sval = "=";}
break;
case 156:
//#line 263 "gramatica.y"
{yyval.sval = TablaTipoToken.DISTINTO;}
break;
case 157:
//#line 264 "gramatica.y"
{yyval.sval = TablaTipoToken.MENOR_IGUAL;}
break;
case 158:
//#line 265 "gramatica.y"
{yyval.sval = TablaTipoToken.MAYOR_IGUAL;}
break;
case 161:
//#line 270 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta ';'."));}
break;
case 163:
//#line 275 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta ';'."));}
break;
case 165:
//#line 279 "gramatica.y"
{ if (val_peek(2).sval.equals("true") || val_peek(1).sval.equals("true")) yyval.sval = "true"; else yyval.sval = "false";}
break;
case 166:
//#line 280 "gramatica.y"
{yyval.sval = val_peek(1).sval;}
break;
case 167:
//#line 283 "gramatica.y"
{yyval.sval = val_peek(1).sval;}
break;
case 168:
//#line 284 "gramatica.y"
{yyval.sval = val_peek(1).sval; erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta ';'."));}
break;
case 169:
//#line 285 "gramatica.y"
{yyval.sval = val_peek(1).sval;}
break;
case 170:
//#line 289 "gramatica.y"
{agregarTerceto("BF", val_peek(1).sval, ((Integer)(tercetos.size()+2)).toString());agregarTerceto("BI", "", (inicioBucle.pop().toString()));Integer lastRef = TablaDeSimbolos.getContexto(val_peek(5).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"Bucle");}
break;
case 171:
//#line 290 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta bloque de sentencias ejecutables.")); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(5).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"Bucle");}
break;
case 172:
//#line 291 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta ( de apertura de condicion.")); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(5).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"Bucle");}
break;
case 173:
//#line 292 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta condicion.")); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(5).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"Bucle");}
break;
case 174:
//#line 293 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta ) de cierre de condicion.")); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(4).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"Bucle");}
break;
case 175:
//#line 294 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta ( ) englobando la condicion.")); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(5).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"Bucle");}
break;
case 176:
//#line 295 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta ( ) englobando la condicion y bloque de sentencias ejecutables.")); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(5).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"Bucle");}
break;
case 177:
//#line 296 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta condicion y bloque de sentencias ejecutables.")); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(5).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"Bucle");}
break;
case 178:
//#line 298 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta while")); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(2).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"Bucle");}
break;
case 179:
//#line 301 "gramatica.y"
{inicioBucle.add(tercetos.size());}
break;
case 180:
//#line 304 "gramatica.y"
{agregarTerceto("BI", "", val_peek(1).sval);Integer lastRef = TablaDeSimbolos.getContexto(val_peek(2).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"GOTO");}
break;
case 181:
//#line 305 "gramatica.y"
{erroresSintactico.add(new Error(numeroLineaError, Tipo.ERROR, "ERROR SINTACTICO falta '@' luego de los dospuntos.")); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(2).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"GOTO");}
break;
case 182:
//#line 306 "gramatica.y"
{erroresSintactico.add(new Error(numeroLineaError, Tipo.ERROR, "ERROR SINTACTICO etiqueta invalida.")); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(2).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"GOTO");}
break;
case 183:
//#line 307 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta 'goto' luego de los dospuntos.")); estructuras.add("Linea "+": "+"GOTO");}
break;
case 184:
//#line 310 "gramatica.y"
{agregarTerceto("ETIQUETA", val_peek(1).sval, ""); completaTercetosEtiqueta(val_peek(1).sval, tercetos.size());declaracionEtiqueta(val_peek(1).sval);Integer lastRef = TablaDeSimbolos.getContexto(val_peek(1).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"Etiqueta"); yyval.sval = lastRef.toString();}
break;
case 185:
//#line 311 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta 'etiqueta'."));}
break;
case 186:
//#line 315 "gramatica.y"
{agregarTerceto("OUTF", val_peek(1).sval, ""); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(3).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"Mensaje de salida");}
break;
case 187:
//#line 316 "gramatica.y"
{agregarTerceto("OUTF", val_peek(1).sval, ""); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(3).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"Mensaje de salida");}
break;
case 188:
//#line 317 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera cadena multilinea o expresion en el mensaje de salida.")); Integer lastRef = TablaDeSimbolos.getContexto(val_peek(3).sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"Mensaje de salida");}
break;
//#line 1831 "Parser.java"
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
