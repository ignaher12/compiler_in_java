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
import lexico.Lexema;
import java.util.HexFormat;
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
    2,    2,    2,    2,    3,    3,    3,    3,    8,    8,
    8,    8,    8,   10,   10,   11,   11,   11,   11,   11,
   11,   11,   11,   11,   11,    9,    9,    9,    9,    6,
    6,    6,    7,    7,   12,   12,   15,   15,   13,   13,
   13,   14,   14,   16,   16,   16,   16,   16,   18,    4,
    4,    4,    4,    4,   25,   17,   26,   17,   27,   17,
   28,   17,   17,   17,   20,   20,   19,   19,   19,   19,
   19,   30,   30,   30,   30,   30,   31,   31,   31,   31,
   31,   31,   31,   32,   32,   32,   32,   32,   32,   32,
   21,   21,   21,   21,   21,   21,   21,   21,   21,   21,
   21,   21,   21,   21,   21,   21,   29,   29,   29,   29,
   29,   29,   29,   29,   29,   29,   29,   29,   29,   29,
   29,   29,   33,   33,   33,   33,   33,   33,   33,   33,
   33,   33,   33,   33,   33,   36,   36,   36,   37,   37,
   37,   37,   37,   37,   38,   38,   38,   34,   34,   34,
   39,   39,   35,   35,   22,   22,   22,   22,   23,   23,
   23,    5,   24,   24,   24,
};
final static short yylen[] = {                            2,
    4,    3,    3,    4,    3,    4,    2,    1,    2,    2,
    1,    2,    2,    2,    2,    1,    2,    2,    2,    2,
    2,    2,    2,    6,    6,    5,    5,    5,    5,    5,
    5,    4,    4,    3,    3,    8,    8,   10,    8,    1,
    1,    1,    3,    1,    3,    1,    1,    2,    2,    2,
    2,    2,    1,    2,    2,    1,    1,    1,    4,    1,
    1,    1,    1,    1,    0,    2,    0,    2,    0,    2,
    0,    2,    1,    1,    3,    4,    3,    3,    3,    3,
    1,    3,    3,    3,    3,    1,    1,    1,    1,    2,
    2,    2,    2,    4,    5,    7,    6,    4,    7,    9,
    7,    7,    9,    9,    7,    9,    8,    6,    8,    6,
    7,    5,    7,    9,    9,    9,    7,    7,    9,    9,
    7,    9,    8,    6,    8,    6,    7,    5,    7,    9,
    9,    9,   11,    7,    9,   11,   11,   11,   11,   11,
   11,    3,    3,    3,    3,    3,    1,    3,    1,    1,
    1,    1,    1,    1,    3,    2,    2,    2,    2,    3,
    3,    2,    1,    3,    6,    6,    6,    6,    3,    3,
    2,    2,    4,    4,    4,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,   42,    0,    0,
    0,   40,    0,    0,   41,    0,    8,    0,    0,    0,
    0,   16,   60,   61,   62,   63,   64,    0,    2,    0,
    0,    0,    0,   14,   21,   22,    0,    0,  172,    0,
    0,   47,    0,    0,   88,    0,    0,   86,   89,    0,
    0,    0,   19,   20,    0,    0,    0,    0,    0,    0,
    0,    5,    7,   12,    9,   13,   10,  171,   18,   44,
    0,    0,   17,    0,    4,    1,    0,    0,    0,    0,
    0,    0,    0,  152,  153,  154,  149,  150,  151,    0,
   90,    0,   91,   92,   93,   48,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  159,  158,    0,  170,  169,    0,
    0,    0,    0,    0,   34,    0,    0,    0,    0,   35,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   79,    0,   80,    0,    0,    0,  144,    0,   84,
   82,   85,   83,    0,    0,  175,  174,  173,    0,  157,
  156,  160,    0,    0,    0,    0,   43,    0,    0,   32,
    0,   33,    0,    0,   98,    0,    0,   94,    0,    0,
    0,    0,    0,    0,    0,    0,  112,    0,    0,    0,
    0,  155,    0,    0,    0,    0,    0,    0,    0,   46,
    0,   31,   28,   30,   27,   29,   26,    0,   95,    0,
    0,    0,    0,    0,    0,  108,    0,    0,    0,  148,
    0,    0,    0,    0,  110,    0,  168,  166,  167,  165,
   50,   51,   49,    0,    0,    0,   25,    0,   24,    0,
    0,   97,  102,    0,    0,    0,    0,    0,  113,    0,
  105,  101,    0,    0,    0,  111,    0,    0,    0,    0,
   45,   96,    0,   99,    0,    0,    0,    0,    0,    0,
  107,    0,    0,    0,    0,    0,    0,  109,    0,    0,
    0,    0,    0,    0,   53,    0,   73,   65,   67,   69,
   71,   74,    0,    0,    0,    0,  103,    0,    0,    0,
    0,    0,  116,  115,  114,  106,  104,    0,    0,    0,
    0,   54,   37,   52,   55,   66,   68,   70,   72,   39,
   36,    0,  100,    0,    0,    0,    0,    0,  135,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  163,    0,    0,   59,   38,  136,
  138,  140,  137,  139,  141,  133,    0,    0,    0,    0,
    0,  128,    0,    0,    0,  124,    0,    0,    0,  162,
  164,    0,    0,  126,    0,  118,    0,    0,  129,    0,
  121,  117,    0,  161,  127,    0,    0,  123,    0,    0,
    0,    0,  125,  119,  132,  131,  130,  122,  120,
};
final static short yydgoto[] = {                          3,
   16,   17,   18,   57,   58,   21,   72,   22,   73,   35,
   36,  199,  197,  284,   45,  285,  345,  287,  146,   23,
   24,   25,   26,   27,  316,  317,  318,  319,  292,   47,
   48,   49,   50,   59,  346,  147,   90,  114,  361,
};
final static short yysindex[] = {                       -95,
 -188,  909,    0,  909,  777,  210,  310,    0,  -34,    2,
 -218,    0,  443, -213,    0,  799,    0,  -35,  -28,   -7,
 -166,    0,    0,    0,    0,    0,    0,  821,    0,  843,
 -181, -141,  278,    0,    0,    0, -136,  -26,    0,  411,
   57,    0, -138,  -32,    0,  418,   -1,    0,    0,  -20,
  -27, -181,    0,    0, -129,  430,  -22,   -7, -100,  109,
  -48,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  -57,  147,    0,  210,    0,    0, -205, -205,  -17,  357,
  -40,  -26,   55,    0,    0,    0,    0,    0,    0,  -26,
    0,  -38,    0,    0,    0,    0,  -26,  151,  -16,  -26,
  -12,   33,  -26,   38,   41,   43,  619,   15,  174,  267,
  100,  277,   63,  -91,    0,    0,  284,    0,    0,  304,
  349,  138,  299,  303,    0,  184,  391,   71,  395,    0,
   55,   55,  436,   22,  463,  499,  221,  -26,  619,  231,
   55,    0,   -1,    0,   -1,   55,  313,    0,   55,    0,
    0,    0,    0, -249,  619,    0,    0,    0,  -30,    0,
    0,    0,  439,   -5,  417,  417,    0,  -25,  -25,    0,
  -50,    0,   96,  123,    0,  -26,  472,    0,  249,  -26,
  619,  394, -157,  932,   49,   24,    0,  619, -143,  175,
  473,    0,  250,  477,  275,  146,  497,   29,  -21,    0,
  -10,    0,    0,    0,    0,    0,    0,  532,    0,  292,
  521,   58, -134,  411,  408,    0,  619,  -77, -139,    0,
   55,  -26,  566,  298,    0,  619,    0,    0,    0,    0,
    0,    0,    0,  308,  312,  328,    0,  -25,    0,  149,
  548,    0,    0,  619,  -18,  551,   40,  334,    0,  945,
    0,    0,  958,  581,  -26,    0,  337,  865,  887,  553,
    0,    0,  352,    0,  339,  -26,  -26,  -26,  -26,  -26,
    0,  351,  361,  386, -191,  -26,  595,    0, -198,    7,
  625,  609,   -7,  645,    0,  617,    0,    0,    0,    0,
    0,    0, -202,  667,  421,  641,    0,  593,  598,  604,
  680,  683,    0,    0,    0,    0,    0,  399,  -32,  -14,
  -26,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  865,    0,  -26,  -26,  -26,  -26,  -26,    0,  384,
   10,  966,  433,  590,  689,  444,  456,  517,  106,  116,
  435,  966,  438,  982,    0,  -69,  966,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  966,  -68,  503,  636,
  981,    0,  966,  171,  198,    0,  966,  208, -108,    0,
    0,  643,  442,    0,  966,    0,  966,  448,    0,  615,
    0,    0,  917,    0,    0,  452,  455,    0,  467,  468,
  469, -107,    0,    0,    0,    0,    0,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    9,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  709,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   69,    0,    0,    0,    0,    0,   93,    0,    0,    0,
    0,  289,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  325,    0,  729,    0,    0,    0,    0,    0,    0,
    0,    0,  225,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  263,   30,    0,    0,    0,    0,    0,    0,    0,    0,
   44,    0,  120,    0,  143,  390,    0,    0,   46,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  398,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   52,    0,    0,    0,
    0,  711,  733,    0,    0,  755,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
  522,  402,  294,  602,   -2,  432,    0,    0,    0,  727,
  728,  573,  577, -227, -120, -234,  565,    0,   -8,  419,
    0,  453,  487,  531,    0,    0,    0,    0,    0,  466,
  474,    0,  -39,  505,  373,  295,  -42,    0,    0,
};
final static int YYTABLESIZE=1258;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         20,
   46,   20,   20,  104,   99,   44,   43,   97,   11,   97,
   43,   61,   43,   20,   43,  119,  187,   43,   43,   43,
  108,  267,  238,   65,  140,   20,  333,   20,  188,   83,
   67,  294,   43,  238,   97,   98,  116,   52,   32,   43,
  105,   51,  111,   60,  126,  106,  309,  200,  200,  314,
  343,   43,    8,   31,   32,  104,   68,   31,   32,  314,
   33,  176,  320,  222,  306,   12,   43,    4,   43,  235,
  143,   15,  236,  131,  307,    5,   33,   43,   77,  270,
   33,  132,   43,  135,  145,   43,  142,   43,  136,   69,
   70,  141,  134,   43,  335,  149,   92,  101,  215,  102,
  314,  185,   71,  237,  186,   93,   94,   95,  216,   87,
   87,   87,   87,   87,  239,   87,  251,  261,   78,  191,
  217,  161,  225,   82,  194,  177,  252,   87,   87,   87,
   87,  243,  173,   81,  226,   81,   81,   81,  253,  186,
  158,   96,  101,  244,  102,  112,  354,  381,  398,  185,
   46,   81,   81,   81,   81,   46,  356,  382,  399,  185,
   77,    1,   77,   77,   77,    7,   39,  208,    2,  383,
    9,  245,  247,  162,  117,   10,  221,  223,   77,   77,
   77,   77,   13,   78,   14,   78,   78,   78,  249,  262,
  122,  137,  263,  101,  138,  102,  362,  366,  120,  121,
  250,   78,   78,   78,   78,  202,  203,  118,  363,  367,
   87,   89,   88,  254,  156,  227,  130,  133,   41,    8,
   64,   40,   41,   40,   41,  190,   41,   66,  109,   41,
   41,  110,   12,  115,   87,   89,   88,  266,   15,  125,
  310,   42,  107,  142,   41,   42,  139,   42,  332,   42,
  193,   41,   42,   42,   42,  283,  283,  298,  299,  300,
  301,  302,   40,   41,   11,   11,   11,   42,   34,  331,
   11,   46,  342,   11,   42,   11,   11,  155,   41,   11,
   41,  283,   11,   75,   11,   11,   42,  104,  144,   41,
  229,  283,  143,  148,   41,  269,  150,   41,  152,   41,
  330,   42,  334,   42,  220,   41,  145,  157,  142,   87,
   89,   88,   42,  214,  134,   91,  159,   42,  160,  283,
   42,   76,   42,  164,   87,   87,   87,  172,   42,   87,
   87,   87,  283,   87,   87,   87,   87,   80,   87,   87,
   87,   87,   87,  165,   87,   87,   87,   23,   81,   81,
   81,  204,  205,   81,   81,   81,  185,   81,   81,   81,
   81,  353,   81,   81,   81,   81,   81,   39,   81,   81,
   81,  355,   87,   89,   88,   77,   77,   77,  206,  207,
   77,   77,   77,   15,   77,   77,   77,   77,  166,   77,
   77,   77,   77,   77,  167,   77,   77,   77,   78,   78,
   78,  232,  233,   78,   78,   78,  100,   78,   78,   78,
   78,   84,   78,   78,   78,   78,   78,   63,   78,   78,
   78,  168,   85,   86,  341,  169,  101,  138,  102,   63,
  147,   63,  182,  147,  215,   84,  374,  185,  146,  329,
  170,  146,  185,   87,   89,   88,   85,   86,  375,  147,
  147,  147,  171,   87,   89,   88,  174,  146,  146,  146,
  101,  103,  102,  376,   81,   31,   32,   87,   89,   88,
   87,   89,   88,  379,  212,  377,  175,   87,   89,   88,
   75,   75,   75,  181,  350,  380,   75,  185,   33,   75,
   75,   75,   75,  184,   75,   75,  351,  192,   75,  185,
   75,   75,   75,  178,  211,  101,  179,  102,  123,  124,
   84,  129,  209,  228,  101,  210,  102,  230,   76,   76,
   76,   85,   86,  134,   76,   28,   30,   76,   76,   76,
   76,  231,   76,   76,   79,    8,   76,  234,   76,   76,
   76,  101,  180,  102,   23,   23,   23,  241,   12,  277,
   23,  282,  282,   23,   15,   23,   23,  352,   23,   23,
  185,  242,   23,  256,   23,   23,  143,  145,   37,   38,
  308,  258,  240,   84,  101,  259,  102,  282,  151,  153,
   15,   15,   15,  260,   85,   86,   15,  282,  264,   15,
  268,   15,   15,  295,   15,   15,  196,  196,   15,  271,
   15,   15,  278,   19,  297,   19,   19,  296,  101,  255,
  102,  154,  127,  128,    8,  282,  303,   19,  336,  337,
  338,  339,  340,  101,  276,  102,  304,   12,  282,   19,
  348,   19,  101,   15,  102,  101,  324,  102,  185,  100,
  101,  325,  102,  183,   84,  147,  101,  326,  102,  214,
  147,  305,  147,  146,   84,   85,   86,  113,  146,  189,
  146,  147,  147,  246,  311,   85,   86,  312,   84,  146,
  146,   84,  195,  100,    8,  315,  288,  288,   84,   85,
   86,  323,   85,   86,  322,  213,    7,   12,  219,   85,
   86,    9,  224,   15,  370,  347,   10,  357,   55,    7,
  359,  384,  288,   13,    9,   14,   56,  385,    3,   10,
  289,  289,  288,  388,  358,  163,   13,  393,   14,  364,
  394,  248,  101,  327,  102,  101,  328,  102,    6,  365,
  257,  369,  395,  396,  397,  373,  289,   53,   54,  378,
  288,  201,  198,    0,  290,  290,  289,  386,  265,  387,
  288,    0,  390,  288,  273,  392,    0,  275,  368,    7,
  288,    0,  288,    0,  280,  288,  344,    0,    0,   10,
  290,    0,  281,    0,  289,  288,   13,  288,   14,  288,
  290,  288,    0,    0,  289,  288,    0,  289,  291,  291,
    0,    0,    0,  288,  289,  288,  289,    0,  288,  289,
    0,  288,    0,    0,    0,    0,    0,    0,  290,  289,
    0,  289,    0,  289,  291,  289,    0,    0,  290,  289,
    0,  290,  286,  286,  291,    0,    0,  289,  290,  289,
  290,    0,  289,  290,    0,  289,    0,    0,    0,    0,
    0,    0,    0,  290,    0,  290,    0,  290,  286,  290,
    0,    0,  291,  290,    0,    0,    0,    0,  286,    0,
    0,  290,  291,  290,    0,  291,  290,    0,    0,  290,
  389,    7,  291,    0,  291,    7,  280,  291,  344,    0,
    9,   10,   56,    0,  281,   10,  286,  291,   13,  291,
   14,  291,   13,  291,   14,    0,    0,  291,    0,  286,
  279,    7,    8,    0,    0,  291,  280,  291,  360,  313,
  291,   10,   11,  291,  281,   12,    0,    0,   13,    0,
   14,   15,  279,    7,    8,  372,    0,    0,  280,    0,
    0,  321,    0,   10,   11,    0,  281,   12,    0,    0,
   13,    0,   14,   15,  279,    7,    8,    0,    0,    0,
  280,    0,    0,  349,    0,   10,   11,    0,  281,   12,
    0,    0,   13,    0,   14,   15,   57,   57,   57,    0,
    0,    0,   57,    0,    0,   57,    0,   57,   57,    0,
   57,   57,    0,    0,   57,    0,   57,   57,   56,   56,
   56,    0,    0,    0,   56,    0,    0,   56,    0,   56,
   56,    0,   56,   56,    0,    0,   56,    0,   56,   56,
   58,   58,   58,    0,    0,    0,   58,    0,    0,   58,
    0,   58,   58,    0,   58,   58,    0,    0,   58,    0,
   58,   58,    6,    7,    8,    0,    0,    0,    9,    0,
    0,   29,    0,   10,   11,    0,    0,   12,    0,    0,
   13,    0,   14,   15,    6,    7,    8,    0,    0,    0,
    9,    0,    0,   62,    0,   10,   11,    0,    0,   12,
    0,    0,   13,    0,   14,   15,   74,    7,    8,    0,
    0,    0,    9,    0,    0,   75,    0,   10,   11,    0,
    0,   12,    0,    0,   13,    0,   14,   15,    6,    7,
    8,    0,    0,    0,    9,    0,    0,   76,    0,   10,
   11,    0,    0,   12,    0,    0,   13,    0,   14,   15,
  279,    7,    8,    0,    0,    0,  280,    0,    0,    0,
    0,   10,   11,    0,  281,   12,    0,    0,   13,    0,
   14,   15,  293,    7,    8,    0,    0,    0,  280,    0,
    0,    0,    0,   10,   11,    0,  281,   12,    0,    0,
   13,    0,   14,   15,    6,    7,    8,    0,    0,    0,
    9,    0,  391,    7,    0,   10,   11,    0,  280,   12,
  344,    0,   13,   10,   14,   15,  281,  218,    7,    0,
   13,    0,   14,    9,    0,   56,    0,    0,   10,    0,
  272,    7,    0,    0,    0,   13,    9,   14,   56,    0,
    0,   10,    0,  274,    7,    0,    0,    0,   13,    9,
   14,   56,    7,    0,   10,    0,    0,  280,    0,  344,
    0,   13,   10,   14,    0,  281,    0,    7,    7,   13,
    0,   14,  280,  280,    0,  371,    0,   10,   10,    0,
  281,  281,    0,    0,   13,   13,   14,   14,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                          2,
    9,    4,    5,   46,   44,   40,   45,   40,    0,   40,
   45,   14,   45,   16,   45,   64,  266,   45,   45,   45,
   41,   40,   44,   59,   41,   28,   41,   30,  278,   38,
   59,  259,   45,   44,   40,   44,   59,  256,  257,   45,
   42,   40,   51,  257,   62,   47,   40,  168,  169,  284,
   41,   45,  258,  256,  257,   98,   64,  256,  257,  294,
  279,   40,  265,   40,  256,  271,   45,  256,   45,   41,
   41,  277,   44,   82,  266,  264,  279,   45,  260,   40,
  279,   90,   45,   92,   41,   45,   41,   45,   97,  256,
  257,  100,   41,   45,  322,  104,   40,   43,   41,   45,
  335,   44,  269,  125,  147,   49,   50,   51,  266,   41,
   42,   43,   44,   45,  125,   47,  256,  238,  260,  159,
  278,   59,  266,  260,  164,  134,  266,   59,   60,   61,
   62,  266,   62,   41,  278,   43,   44,   45,  278,  182,
   41,  280,   43,  278,   45,  275,   41,  256,  256,   44,
  159,   59,   60,   61,   62,  164,   41,  266,  266,   44,
   41,  257,   43,   44,   45,  257,   58,  176,  264,  278,
  262,  214,  215,  265,  275,  267,  185,  186,   59,   60,
   61,   62,  274,   41,  276,   43,   44,   45,  266,   41,
   44,   41,   44,   43,   44,   45,  266,  266,  256,  257,
  278,   59,   60,   61,   62,  256,  257,  256,  278,  278,
   60,   61,   62,  222,   41,   41,  257,  256,  257,  258,
  256,  256,  257,  256,  257,  256,  257,  256,  256,  257,
  257,  259,  271,  256,   60,   61,   62,  256,  277,  257,
  280,  280,  263,  256,  257,  280,  263,  280,  263,  280,
  256,  257,  280,  280,  280,  258,  259,  266,  267,  268,
  269,  270,  256,  257,  256,  257,  258,  280,   59,  309,
  262,  280,  263,  265,  280,  267,  268,  263,  257,  271,
  257,  284,  274,   59,  276,  277,  280,  330,  256,  257,
   41,  294,  263,  256,  257,  256,  256,  257,  256,  257,
  309,  280,  311,  280,  256,  257,  263,   41,  263,   60,
   61,   62,  280,  256,  263,  259,   40,  280,  256,  322,
  280,   59,  280,   40,  256,  257,  258,  257,  280,  261,
  262,  263,  335,  265,  266,  267,  268,   60,  270,  271,
  272,  273,  274,   40,  276,  277,  278,   59,  256,  257,
  258,  256,  257,  261,  262,  263,   44,  265,  266,  267,
  268,  256,  270,  271,  272,  273,  274,   58,  276,  277,
  278,  256,   60,   61,   62,  256,  257,  258,  256,  257,
  261,  262,  263,   59,  265,  266,  267,  268,   40,  270,
  271,  272,  273,  274,  257,  276,  277,  278,  256,  257,
  258,  256,  257,  261,  262,  263,  256,  265,  266,  267,
  268,  261,  270,  271,  272,  273,  274,   16,  276,  277,
  278,  123,  272,  273,   41,  123,   43,   44,   45,   28,
   41,   30,  138,   44,   41,  261,  266,   44,   41,   41,
  257,   44,   44,   60,   61,   62,  272,  273,  278,   60,
   61,   62,   62,   60,   61,   62,   62,   60,   61,   62,
   43,   44,   45,  266,   33,  256,  257,   60,   61,   62,
   60,   61,   62,  266,  180,  278,   41,   60,   61,   62,
  256,  257,  258,  263,   41,  278,  262,   44,  279,  265,
  266,  267,  268,  263,  270,  271,   41,   59,  274,   44,
  276,  277,  278,   41,  256,   43,   44,   45,   77,   78,
  261,   80,   41,   41,   43,   44,   45,   41,  256,  257,
  258,  272,  273,   92,  262,    4,    5,  265,  266,  267,
  268,  257,  270,  271,  257,  258,  274,   41,  276,  277,
  278,   43,   44,   45,  256,  257,  258,  256,  271,  255,
  262,  258,  259,  265,  277,  267,  268,   41,  270,  271,
   44,   41,  274,  266,  276,  277,  101,  102,  259,  260,
  276,  264,   41,  261,   43,  264,   45,  284,  105,  106,
  256,  257,  258,  256,  272,  273,  262,  294,   41,  265,
   40,  267,  268,   41,  270,  271,  165,  166,  274,  266,
  276,  277,  266,    2,  266,    4,    5,  256,   43,   44,
   45,  107,  256,  257,  258,  322,  266,   16,  324,  325,
  326,  327,  328,   43,   44,   45,  266,  271,  335,   28,
   41,   30,   43,  277,   45,   43,   44,   45,   44,  256,
   43,   44,   45,  139,  261,  256,   43,   44,   45,  256,
  261,  266,  263,  256,  261,  272,  273,   56,  261,  155,
  263,  272,  273,  256,   40,  272,  273,   59,  261,  272,
  273,  261,  256,  256,  258,   59,  258,  259,  261,  272,
  273,   41,  272,  273,  264,  181,  257,  271,  184,  272,
  273,  262,  188,  277,   59,  263,  267,  263,  256,  257,
  263,   59,  284,  274,  262,  276,  264,  266,    0,  267,
  258,  259,  294,  266,  342,  114,  274,  266,  276,  347,
  266,  217,   43,   44,   45,   43,   44,   45,    0,  357,
  226,  359,  266,  266,  266,  363,  284,   11,   11,  367,
  322,  169,  166,   -1,  258,  259,  294,  375,  244,  377,
  332,   -1,  380,  335,  250,  383,   -1,  253,  256,  257,
  342,   -1,  344,   -1,  262,  347,  264,   -1,   -1,  267,
  284,   -1,  270,   -1,  322,  357,  274,  359,  276,  361,
  294,  363,   -1,   -1,  332,  367,   -1,  335,  258,  259,
   -1,   -1,   -1,  375,  342,  377,  344,   -1,  380,  347,
   -1,  383,   -1,   -1,   -1,   -1,   -1,   -1,  322,  357,
   -1,  359,   -1,  361,  284,  363,   -1,   -1,  332,  367,
   -1,  335,  258,  259,  294,   -1,   -1,  375,  342,  377,
  344,   -1,  380,  347,   -1,  383,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,  357,   -1,  359,   -1,  361,  284,  363,
   -1,   -1,  322,  367,   -1,   -1,   -1,   -1,  294,   -1,
   -1,  375,  332,  377,   -1,  335,  380,   -1,   -1,  383,
  256,  257,  342,   -1,  344,  257,  262,  347,  264,   -1,
  262,  267,  264,   -1,  270,  267,  322,  357,  274,  359,
  276,  361,  274,  363,  276,   -1,   -1,  367,   -1,  335,
  256,  257,  258,   -1,   -1,  375,  262,  377,  344,  265,
  380,  267,  268,  383,  270,  271,   -1,   -1,  274,   -1,
  276,  277,  256,  257,  258,  361,   -1,   -1,  262,   -1,
   -1,  265,   -1,  267,  268,   -1,  270,  271,   -1,   -1,
  274,   -1,  276,  277,  256,  257,  258,   -1,   -1,   -1,
  262,   -1,   -1,  265,   -1,  267,  268,   -1,  270,  271,
   -1,   -1,  274,   -1,  276,  277,  256,  257,  258,   -1,
   -1,   -1,  262,   -1,   -1,  265,   -1,  267,  268,   -1,
  270,  271,   -1,   -1,  274,   -1,  276,  277,  256,  257,
  258,   -1,   -1,   -1,  262,   -1,   -1,  265,   -1,  267,
  268,   -1,  270,  271,   -1,   -1,  274,   -1,  276,  277,
  256,  257,  258,   -1,   -1,   -1,  262,   -1,   -1,  265,
   -1,  267,  268,   -1,  270,  271,   -1,   -1,  274,   -1,
  276,  277,  256,  257,  258,   -1,   -1,   -1,  262,   -1,
   -1,  265,   -1,  267,  268,   -1,   -1,  271,   -1,   -1,
  274,   -1,  276,  277,  256,  257,  258,   -1,   -1,   -1,
  262,   -1,   -1,  265,   -1,  267,  268,   -1,   -1,  271,
   -1,   -1,  274,   -1,  276,  277,  256,  257,  258,   -1,
   -1,   -1,  262,   -1,   -1,  265,   -1,  267,  268,   -1,
   -1,  271,   -1,   -1,  274,   -1,  276,  277,  256,  257,
  258,   -1,   -1,   -1,  262,   -1,   -1,  265,   -1,  267,
  268,   -1,   -1,  271,   -1,   -1,  274,   -1,  276,  277,
  256,  257,  258,   -1,   -1,   -1,  262,   -1,   -1,   -1,
   -1,  267,  268,   -1,  270,  271,   -1,   -1,  274,   -1,
  276,  277,  256,  257,  258,   -1,   -1,   -1,  262,   -1,
   -1,   -1,   -1,  267,  268,   -1,  270,  271,   -1,   -1,
  274,   -1,  276,  277,  256,  257,  258,   -1,   -1,   -1,
  262,   -1,  256,  257,   -1,  267,  268,   -1,  262,  271,
  264,   -1,  274,  267,  276,  277,  270,  256,  257,   -1,
  274,   -1,  276,  262,   -1,  264,   -1,   -1,  267,   -1,
  256,  257,   -1,   -1,   -1,  274,  262,  276,  264,   -1,
   -1,  267,   -1,  256,  257,   -1,   -1,   -1,  274,  262,
  276,  264,  257,   -1,  267,   -1,   -1,  262,   -1,  264,
   -1,  274,  267,  276,   -1,  270,   -1,  257,  257,  274,
   -1,  276,  262,  262,   -1,  265,   -1,  267,  267,   -1,
  270,  270,   -1,   -1,  274,  274,  276,  276,
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
"sentencia : sentenciaDeclarativa ';'",
"sentencia : sentenciaEjecutable ';'",
"sentencia : etiqueta",
"sentencia : sentenciaDeclarativa error",
"sentencia : sentenciaEjecutable error",
"sentencia : error ';'",
"sentenciaDeclarativa : tipoDato listaVariable",
"sentenciaDeclarativa : typedefDeclaracion",
"sentenciaDeclarativa : tipoDato funDeclaracion",
"sentenciaDeclarativa : tipoDato error",
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
"$$1 :",
"sentenciaEjecutableConRet : asignacion $$1",
"$$2 :",
"sentenciaEjecutableConRet : clausulaBucle $$2",
"$$3 :",
"sentenciaEjecutableConRet : goto $$3",
"$$4 :",
"sentenciaEjecutableConRet : mensajeSalida $$4",
"sentenciaEjecutableConRet : sentenciaRet",
"sentenciaEjecutableConRet : clausulaSeleccionConRet",
"asignacion : IDENTIFICADOR SIMASIGNACION expresion",
"asignacion : IDENTIFICADOR CADENA_MULTI SIMASIGNACION expresion",
"expresion : expresion '+' termino",
"expresion : expresion '-' termino",
"expresion : expresion '+' error",
"expresion : expresion '-' error",
"expresion : termino",
"termino : termino '*' operando",
"termino : termino '/' operando",
"termino : termino '*' error",
"termino : termino '/' error",
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
"clausulaSeleccion : IF '(' condicion ')' THEN bloqueSentenciaEjecutable END_IF",
"clausulaSeleccion : IF '(' expresion ')' THEN bloqueSentenciaEjecutable END_IF",
"clausulaSeleccion : IF '(' expresion ')' THEN bloqueSentenciaEjecutable ELSE bloqueSentenciaEjecutable END_IF",
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
"bloqueSentenciaEjecutableConRet : sentenciaEjecutableConRet",
"bloqueSentenciaEjecutableConRet : BEGIN cuerpoEjecutableConRet END",
"clausulaBucle : REPEAT bloqueSentenciaEjecutable WHILE '(' condicion ')'",
"clausulaBucle : REPEAT error WHILE '(' condicion ')'",
"clausulaBucle : REPEAT bloqueSentenciaEjecutable WHILE '(' error ')'",
"clausulaBucle : REPEAT error WHILE '(' error ')'",
"goto : GOTO etiqueta '@'",
"goto : GOTO etiqueta error",
"goto : etiqueta '@'",
"etiqueta : IDENTIFICADOR ':'",
"mensajeSalida : OUTF '(' expresion ')'",
"mensajeSalida : OUTF '(' CADENA_MULTI ')'",
"mensajeSalida : OUTF '(' error ')'",
};

//#line 307 "gramatica.y"
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
        Parser.lex = new AnalizadorLexico("CP6TP1", matriz, matrizAcciones);
        
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

private void chequearRango(Lexema lex){
  if (lex != null){
    if(lex.getTipo() == TablaTipoToken.getTipoToken("longint")){
      if(Integer.parseInt(lex.getAtributo()) > AnalizadorLexico.MAXLONGINT){
        erroresSintactico.add(new Error(
          AnalizadorLexico.getNumeroLinea(),
          Tipo.ERROR,
          "ERROR SINTACTICO excede rangos."
        ));
      }
    } else if (lex.getTipo() == TablaTipoToken.getTipoToken("single")){
      String numero = lex.getAtributo().toString().replace('s', 'e');
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
      if((HexFormat.fromHexDigits(lex.getAtributo().subSequence(2, lex.getAtributo().length()).toString())) > AnalizadorLexico.MAXHEXADECIMAL){
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
//#line 855 "Parser.java"
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
//#line 50 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta ';'."));}
break;
case 13:
//#line 51 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta ';'."));}
break;
case 14:
//#line 52 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta sentencia."));}
break;
case 15:
//#line 55 "gramatica.y"
{estructuras.add("Declaracion");}
break;
case 16:
//#line 56 "gramatica.y"
{estructuras.add("Declaracion de typedef");}
break;
case 17:
//#line 57 "gramatica.y"
{estructuras.add("Declaracion de funcion");}
break;
case 18:
//#line 58 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera 'objeto a declarar'."));}
break;
case 21:
//#line 63 "gramatica.y"
{ erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera 'TYPEDEF antes de la declaracion del subtipo'.")); }
break;
case 22:
//#line 64 "gramatica.y"
{ erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera 'TYPEDEF antes de la declaracion del triple'.")); }
break;
case 23:
//#line 65 "gramatica.y"
{ erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera 'declaracion de subtipo o triple'.")); }
break;
case 25:
//#line 69 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera 'nombre del tipo de dato nuevo'."));}
break;
case 28:
//#line 73 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera 'tipo de objeto a declarar'."));}
break;
case 29:
//#line 74 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera 'nombre de objeto a declarar'."));}
break;
case 30:
//#line 75 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera 'nombre de objeto a declarar'."));}
break;
case 31:
//#line 76 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera 'nombre y tipo de objeto a declarar'."));}
break;
case 32:
//#line 77 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta '<' al inicio del identificador'."));}
break;
case 33:
//#line 78 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta '>' al final del identificador'."));}
break;
case 34:
//#line 79 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO faltan '<>'."));}
break;
case 35:
//#line 80 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO faltan '<>'."));}
break;
case 36:
//#line 83 "gramatica.y"
{if (val_peek(1).sval.equals("false"))erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta return en el cuerpo de la funcion."));}
break;
case 37:
//#line 84 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta nombre de la funcion."));}
break;
case 38:
//#line 85 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO  no puede tener mas de un parametro."));}
break;
case 39:
//#line 86 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta cuerpo con retorno."));}
break;
case 47:
//#line 104 "gramatica.y"
{ Lexema lex = TablaDeSimbolos.getByID(val_peek(0).ival);
                        System.out.println(val_peek(0).ival);
                        System.out.println(lex);
                        System.out.println(TablaDeSimbolos.imprimir());
                        chequearRango(lex);                             /*SOLO SE CHEQUEA EN POSITIVO YA QUE EL MAXIMO DE NEGATIVOS ES MAYOR AL MAXIMO DE POSITIVOS Y YA LO CHEQUEA EL PARSER*/
                      }
break;
case 48:
//#line 110 "gramatica.y"
{
                            Lexema lex = TablaDeSimbolos.getByID(val_peek(0).ival);
                            int newLexRef = TablaDeSimbolos.agregarSimbolo("-"+lex.getAtributo(), lex.getTipo());
                            /*$2.sval = newLex.*/
                          }
break;
case 50:
//#line 119 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta tipo de dato en el parametro."));}
break;
case 51:
//#line 120 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta nombre en el parametro."));}
break;
case 52:
//#line 124 "gramatica.y"
{ if (val_peek(1).sval.equals("true") || val_peek(0).sval.equals("true")) yyval.sval = "true"; else yyval.sval = "false";}
break;
case 53:
//#line 125 "gramatica.y"
{yyval.sval = val_peek(0).sval;}
break;
case 55:
//#line 129 "gramatica.y"
{yyval.sval = val_peek(1).sval;}
break;
case 57:
//#line 131 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta ';'."));}
break;
case 58:
//#line 132 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta ';'."));}
break;
case 59:
//#line 136 "gramatica.y"
{estructuras.add("Retorno");}
break;
case 60:
//#line 140 "gramatica.y"
{estructuras.add("Asignacion");}
break;
case 61:
//#line 141 "gramatica.y"
{estructuras.add("IF");}
break;
case 62:
//#line 142 "gramatica.y"
{estructuras.add("WHILE");}
break;
case 63:
//#line 143 "gramatica.y"
{estructuras.add("GOTO");}
break;
case 64:
//#line 144 "gramatica.y"
{estructuras.add("OUTF");}
break;
case 65:
//#line 146 "gramatica.y"
{estructuras.add("Asignacion");}
break;
case 66:
//#line 146 "gramatica.y"
{yyval.sval = "false";}
break;
case 67:
//#line 147 "gramatica.y"
{estructuras.add("WHILE");}
break;
case 68:
//#line 147 "gramatica.y"
{yyval.sval = "false";}
break;
case 69:
//#line 148 "gramatica.y"
{estructuras.add("GOTO");}
break;
case 70:
//#line 148 "gramatica.y"
{yyval.sval = "false";}
break;
case 71:
//#line 149 "gramatica.y"
{estructuras.add("OUTF");}
break;
case 72:
//#line 149 "gramatica.y"
{yyval.sval = "false";}
break;
case 73:
//#line 150 "gramatica.y"
{yyval.sval = "true";}
break;
case 74:
//#line 151 "gramatica.y"
{estructuras.add("IF"); yyval.sval = val_peek(0).sval;}
break;
case 97:
//#line 190 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO numero de expresiones invalido en el llamado a funcion."));}
break;
case 98:
//#line 191 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta expresion en el llamado a funcion."));}
break;
case 99:
//#line 192 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO numero de expresiones invalido en el llamado a funcion."));}
break;
case 100:
//#line 193 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO numero de expresion esinvalido en el llamado a funcion."));}
break;
case 102:
//#line 197 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera una condicion valida"));}
break;
case 103:
//#line 198 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera una condicion valida"));}
break;
case 105:
//#line 200 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta 'end_if;'."));}
break;
case 106:
//#line 201 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta 'end_if;'."));}
break;
case 107:
//#line 202 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta ')' al final de la condicion."));}
break;
case 108:
//#line 203 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta ')' al final de la condicion."));}
break;
case 109:
//#line 204 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta '(' al comienzo de la condicion."));}
break;
case 110:
//#line 205 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta '(' al comienzo de la condicion."));}
break;
case 111:
//#line 206 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO faltan '()' en la condicion."));}
break;
case 112:
//#line 207 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO faltan '()' en la condicion."));}
break;
case 113:
//#line 208 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta bloque de sentencias ejecutables valido."));}
break;
case 114:
//#line 209 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta bloque de sentencias ejecutables valido ."));}
break;
case 115:
//#line 210 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta bloque de sentencias ejecutables valido ."));}
break;
case 116:
//#line 211 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO faltan los dos bloques de sentencias ejecutables validas."));}
break;
case 117:
//#line 215 "gramatica.y"
{yyval.sval = "false";}
break;
case 118:
//#line 216 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera una condicion valida")); yyval.sval = "false";}
break;
case 119:
//#line 217 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera una condicion valida"));if (val_peek(3).sval.equals("true") && val_peek(1).sval.equals("true")) yyval.sval = "true"; else yyval.sval = "false";}
break;
case 120:
//#line 218 "gramatica.y"
{ if (val_peek(3).sval.equals("true") && val_peek(1).sval.equals("true")) yyval.sval = "true"; else yyval.sval = "false";}
break;
case 121:
//#line 219 "gramatica.y"
{yyval.sval = "false";erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta 'end_if;'."));}
break;
case 122:
//#line 220 "gramatica.y"
{if (val_peek(3).sval.equals("true") && val_peek(1).sval.equals("true")) yyval.sval = "true"; else yyval.sval = "false";erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta 'end_if;'."));}
break;
case 123:
//#line 221 "gramatica.y"
{if (val_peek(3).sval.equals("true") && val_peek(1).sval.equals("true")) yyval.sval = "true"; else yyval.sval = "false";erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta ')' al final de la condicion."));}
break;
case 124:
//#line 222 "gramatica.y"
{yyval.sval = "false";erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta ')' al final de la condicion."));}
break;
case 125:
//#line 223 "gramatica.y"
{if (val_peek(3).sval.equals("true") && val_peek(1).sval.equals("true")) yyval.sval = "true"; else yyval.sval = "false";erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta '(' al comienzo de la condicion."));}
break;
case 126:
//#line 224 "gramatica.y"
{yyval.sval = "false";erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta '(' al comienzo de la condicion."));}
break;
case 127:
//#line 225 "gramatica.y"
{if (val_peek(3).sval.equals("true") && val_peek(1).sval.equals("true")) yyval.sval = "true"; else yyval.sval = "false";erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO faltan '()' en la condicion."));}
break;
case 128:
//#line 226 "gramatica.y"
{yyval.sval = "false";erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO faltan '()' en la condicion."));}
break;
case 129:
//#line 227 "gramatica.y"
{yyval.sval = "false";erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta bloque de sentencias ejecutables valido."));}
break;
case 130:
//#line 228 "gramatica.y"
{yyval.sval = "false";erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta bloque de sentencias ejecutables valido ."));}
break;
case 131:
//#line 229 "gramatica.y"
{yyval.sval = "false";erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta bloque de sentencias ejecutables valido ."));}
break;
case 132:
//#line 230 "gramatica.y"
{yyval.sval = "false";erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO faltan los dos bloques de sentencias ejecutables validas."));}
break;
case 134:
//#line 234 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO faltan '( )' a las listas de expresiones."));}
break;
case 135:
//#line 235 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO faltan parentesis en las listas de expresiones."));}
break;
case 136:
//#line 236 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO faltan parentesis en las listas de expresiones."));}
break;
case 137:
//#line 237 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO faltan parentesis en las listas de expresiones."));}
break;
case 138:
//#line 238 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO faltan parentesis en las listas de expresiones."));}
break;
case 139:
//#line 239 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO faltan parentesis en las listas de expresiones."));}
break;
case 140:
//#line 240 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera un comparador."));}
break;
case 141:
//#line 241 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO faltan parentesis en las listas de expresiones."));}
break;
case 143:
//#line 244 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera una expresion a la izquierda del comparador."));}
break;
case 144:
//#line 245 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera una expresion a la derecha del comparador."));}
break;
case 145:
//#line 246 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera un comparador."));}
break;
case 148:
//#line 258 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera una expresion despues de la coma."));}
break;
case 157:
//#line 266 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta ';'."));}
break;
case 159:
//#line 271 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta ';'."));}
break;
case 161:
//#line 275 "gramatica.y"
{ if (val_peek(2).sval.equals("true") || val_peek(1).sval.equals("true")) yyval.sval = "true"; else yyval.sval = "false";}
break;
case 162:
//#line 276 "gramatica.y"
{yyval.sval = val_peek(1).sval;}
break;
case 163:
//#line 279 "gramatica.y"
{yyval.sval = val_peek(0).sval;}
break;
case 164:
//#line 280 "gramatica.y"
{yyval.sval = val_peek(1).sval;}
break;
case 166:
//#line 285 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta bloque de sentencias ejecutables."));}
break;
case 167:
//#line 286 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta condicion."));}
break;
case 168:
//#line 287 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta condicion y bloque de sentencias ejecutables."));}
break;
case 170:
//#line 291 "gramatica.y"
{erroresSintactico.add(new Error(numeroLineaError, Tipo.ERROR, "ERROR SINTACTICO falta '@' luego de los dospuntos."));}
break;
case 171:
//#line 292 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta 'goto' luego de los dospuntos."));}
break;
case 175:
//#line 302 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera cadena multilinea o expresion en el mensaje de salida."));}
break;
//#line 1457 "Parser.java"
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
