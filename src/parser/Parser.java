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






//#line 2 "gramatica.y"
package parser;

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
    0,    0,    0,    0,    1,    1,    2,    2,    2,    2,
    2,    2,    3,    3,    3,    3,    8,    8,    8,    8,
    8,   10,   10,   11,   11,   11,   11,   11,   11,    9,
    6,    6,    6,    7,    7,   12,   12,   15,   15,   13,
   14,   14,   16,   16,   16,   16,   16,   18,    4,    4,
    4,    4,    4,   17,   17,   17,   17,   17,   17,   20,
   20,   19,   19,   27,   27,   27,   27,   26,   26,   26,
   26,   28,   28,   28,   21,   21,   21,   21,   21,   21,
   21,   21,   21,   21,   21,   21,   25,   25,   25,   25,
   25,   25,   25,   25,   25,   25,   25,   25,   29,   32,
   32,   34,   34,   34,   34,   33,   33,   33,   33,   33,
   33,   35,   35,   35,   30,   30,   30,   36,   36,   31,
   31,   22,   22,   22,   22,   22,   23,   23,   23,    5,
    5,    5,   24,   24,   24,
};
final static short yylen[] = {                            2,
    4,    3,    3,    3,    2,    1,    2,    2,    1,    2,
    2,    2,    2,    1,    2,    2,    2,    2,    2,    2,
    2,    6,    6,    5,    5,    5,    5,    5,    5,    8,
    1,    1,    1,    3,    1,    3,    1,    1,    2,    2,
    2,    1,    2,    2,    1,    1,    1,    4,    1,    1,
    1,    1,    1,    1,    1,    1,    1,    1,    1,    3,
    4,    1,    3,    1,    1,    1,    1,    1,    1,    1,
    2,    4,    5,    7,    7,    9,    7,    9,    8,    6,
    8,    6,    7,    5,    7,    9,    7,    9,    7,    9,
    8,    6,    8,    6,    7,    5,    7,    9,    3,    3,
    1,    3,    1,    3,    3,    1,    1,    1,    1,    1,
    1,    3,    2,    2,    2,    2,    3,    3,    2,    2,
    3,    6,    6,    6,    6,    2,    3,    3,    2,    2,
    2,    2,    4,    4,    4,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,    0,    0,   33,    0,    0,    0,
   31,    0,    0,   32,    0,    6,    0,    0,    0,    0,
   14,   49,   50,   51,   52,   53,    2,    0,    0,    0,
    0,   12,  131,   19,   20,  132,    0,    0,  130,    0,
   38,    0,    0,   69,    0,   62,   70,    0,    0,    0,
    0,   17,   18,    0,    0,    0,    0,    0,    0,    0,
    0,    4,    5,   10,    7,   11,    8,  129,   16,   35,
    0,    0,   15,    1,    0,    0,    0,    0,    0,   71,
    0,   39,    0,    0,    0,    0,    0,   64,   65,   66,
   67,    0,    0,    0,  109,  110,  111,  106,  107,  108,
    0,    0,    0,    0,    0,    0,    0,  116,  115,    0,
  128,  127,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  100,    0,   63,    0,
    0,   99,  135,  134,  133,    0,  114,  113,  117,    0,
    0,    0,   34,    0,    0,    0,    0,    0,    0,    0,
   72,    0,    0,    0,  105,    0,   84,    0,    0,    0,
    0,  112,    0,    0,    0,    0,    0,   37,    0,   29,
   26,   28,   25,   27,   24,    0,   73,   80,    0,    0,
    0,    0,   82,    0,  125,  123,  124,  122,   40,    0,
   23,    0,   22,    0,    0,   85,    0,   77,   75,    0,
   83,    0,    0,   36,   74,   79,    0,    0,   81,    0,
    0,    0,    0,    0,    0,   42,    0,   58,   54,   55,
   56,   57,   59,   86,   78,   76,    0,    0,    0,   43,
   30,   41,   44,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   48,    0,    0,    0,    0,  120,   96,
    0,    0,   92,    0,    0,    0,  119,  121,    0,    0,
   94,    0,    0,   97,    0,   89,   87,    0,  118,   95,
    0,   91,    0,    0,   93,   98,   90,   88,
};
final static short yydgoto[] = {                          3,
   15,   16,   17,   56,   57,   20,   72,   21,   73,   34,
   35,  167,  166,  215,   44,  216,  241,  218,   45,   22,
   23,   24,   25,   26,  223,   46,   92,   47,   48,   58,
  242,   49,  101,   87,  107,  248,
};
final static short yysindex[] = {                      -120,
 -169,  418,    0,  330,   52,   11,    0,  -34,   63, -204,
    0,  471, -233,    0,  352,    0,  -22,  -15,   10, -178,
    0,    0,    0,    0,    0,    0,    0,  374, -137, -101,
  115,    0,    0,    0,    0,    0,  -79,  -31,    0,  -24,
    0, -109,  -40,    0,  142,    0,    0,   -8,  141,  -20,
 -137,    0,    0,  -11,  528,  -13,   10,  -81,  135,  -23,
  -44,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  -62,  153,    0,    0, -216, -216,  -47,  -31,  142,    0,
  -26,    0,  154,  -42,  142,   -7,   13,    0,    0,    0,
    0,  -31,  484,  -63,    0,    0,    0,    0,    0,    0,
  -32,  164,  167,  401,  188,   -9,  487,    0,    0,  225,
    0,    0,  233,  -53,  151,  166,  214,  230,  236,  142,
  -30,  409,  -31,  142,  484,   24,    0,   26,    0, -190,
  484,    0,    0,    0,    0,  -38,    0,    0,    0,  246,
  -36, -216,    0,  -27,  -27, -158,  -83,  -78,  -31,  614,
    0,  142, -174,  500,    0,  142,    0,  484, -148,  276,
  279,    0,  280,  285,   65,  287,   -3,    0,    7,    0,
    0,    0,    0,    0,    0,  751,    0,    0,  484,   36,
 -235,   64,    0,  484,    0,    0,    0,    0,    0,   72,
    0,  -27,    0,  298,   74,    0,   86,    0,    0,  484,
    0,   82,  396,    0,    0,    0,   83, -193,    0,   54,
  -28,  310,  293,   10,  237,    0,  294,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  -40,   -2,  -31,    0,
    0,    0,    0,   -1,  426,   92,  758,  426,   95,  513,
  302, -147,  426,    0, -142,  441,  303,  456,    0,    0,
  426, -131,    0,  426,   38, -128,    0,    0,  305,  104,
    0,  426,  105,    0,  118,    0,    0,  426,    0,    0,
  109,    0,  114,  -89,    0,    0,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    1,    0,
    0,    0,    0,    0,    0,    0,    0,  383,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   23,
    0,    0,    0,    0,   40,    0,    0,    0,    0,    0,
   67,    0,    0,    0,    0,    0,    0,   89,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  111,    0,    0,    0,    0,    0,    0,  133,    0,
    0,    0,    0,    0,   46,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  163,
    0,    0,    0,   15,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  101,    0,    0,    0,  102,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  259,  281,    0,    0,  308,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
  382,   30,  -52,   34,  112,  -49,    0,    0,    0,  384,
  386,  247,    0,    0, -115,  178, -143,    0,  334,  -86,
    0,  -82,  223,  238,    0,  307,    0,    0,  -21,  -45,
  -55,  296,    0,    0,    0,    0,
};
final static int YYTABLESIZE=805;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         84,
    9,   84,   42,   84,   42,   43,   42,   84,   42,  149,
   42,  227,   42,   42,   42,   81,   42,   42,   42,  112,
  198,   86,   59,   60,   42,  115,  116,  119,  168,  168,
  199,  121,   94,  126,   39,   18,   65,   18,  236,  239,
  192,    7,  200,   67,   63,  109,   33,  130,   18,  138,
  192,   51,   30,  127,   11,  103,  128,   63,  103,  217,
   14,   18,  225,   68,   68,   68,   68,   68,   39,   68,
   42,  217,  226,   68,   31,  157,  204,   69,   70,  153,
  101,   68,   68,   68,   68,  159,  103,  158,  106,  103,
   71,  178,  165,   33,    4,   33,  247,  170,  171,  101,
  101,  101,   50,  179,  259,  101,  101,  101,  181,   33,
   32,   33,  182,   19,  161,   19,  219,  183,  250,  164,
  220,  191,   75,  253,   61,   21,   19,  266,  219,  184,
  251,  193,  220,  195,  261,  254,    1,  267,  202,   19,
  140,  104,  102,    2,  104,  102,  262,  126,  219,  268,
  213,  219,  220,  219,  208,  220,  219,  220,   76,  219,
  220,  219,  213,  220,  219,  220,  277,  219,  220,   13,
   82,  220,  172,  173,   77,  219,  278,  174,  175,  220,
   78,  219,  245,   90,   88,  220,   89,  252,   91,  228,
  256,   60,   33,  110,  113,  260,  114,  123,  263,  131,
   98,  100,   99,  143,  133,  234,  271,  134,  117,  118,
    7,  111,  274,   83,   40,   83,   40,  160,   40,  163,
   40,   61,   40,   11,   40,   40,   40,  136,   40,   14,
   40,    7,   36,   64,   80,  102,   40,   41,  103,   41,
   66,   41,  108,   41,   11,   41,  137,   41,   41,   41,
   14,   41,   41,   41,   93,  125,    9,    9,    9,   41,
  235,  238,    9,  105,  141,    9,   36,    9,    9,   37,
   38,    9,  142,  144,    9,  146,    9,    9,   68,   68,
   68,  155,   40,   68,   68,   68,  154,   68,  145,   68,
   68,  147,   68,   68,   68,   68,   68,  148,   68,   68,
  101,  196,  101,  264,  162,   41,  101,   29,   30,   29,
   30,  101,  101,  197,  214,  265,  185,  101,  101,  186,
  187,  189,   21,   21,   21,  188,  214,  190,   21,  201,
   31,   21,   31,   21,   21,  203,   21,   21,  205,  206,
   21,  207,   21,   21,  126,  126,  126,  209,  224,  229,
  126,  230,  233,  126,  243,  126,  126,  246,  126,  126,
  249,  257,  126,  269,  126,  126,   13,   13,   13,  270,
  272,   79,   13,  273,  275,   13,   85,   13,   13,  276,
   13,   13,    3,  104,   13,   28,   13,   13,   60,   60,
   60,  169,  232,   52,   60,   53,  132,   60,  129,   60,
   60,   95,   60,   60,    0,    0,   60,    0,   60,   60,
    0,  120,   96,   97,  122,    0,    0,  124,   61,   61,
   61,    0,    0,    0,   61,  221,    0,   61,    0,   61,
   61,    0,   61,   61,    0,    0,   61,  221,   61,   61,
  222,  135,   90,   88,    0,   89,    0,   91,    0,  151,
   90,   88,  222,   89,  150,   91,  152,  221,    0,    0,
  221,  156,  221,    0,    0,  221,    0,    0,  221,    0,
  221,    0,  222,  221,    0,  222,  221,  222,    0,    0,
  222,    0,  176,  222,  221,  222,    0,    0,  222,    0,
  221,  222,  210,    6,    7,    0,    0,    0,  211,  222,
    0,  231,    0,    9,   10,  222,  212,   11,    0,    0,
   12,    0,   13,   14,   46,   46,   46,    0,    0,    0,
   46,    0,    0,   46,    0,   46,   46,    0,   46,   46,
    0,    0,   46,    0,   46,   46,   45,   45,   45,    0,
    0,    0,   45,    0,    0,   45,    0,   45,   45,    0,
   45,   45,    0,    0,   45,    0,   45,   45,    0,    0,
   85,    0,  237,   47,   47,   47,    0,    0,    0,   47,
    0,    0,   47,    0,   47,   47,    0,   47,   47,    0,
    0,   47,    0,   47,   47,    5,    6,    7,    0,    0,
    0,    8,    0,    0,   27,    0,    9,   10,    0,    0,
   11,    0,    0,   12,    0,   13,   14,    5,    6,    7,
    0,    0,    0,    8,    0,    0,   62,    0,    9,   10,
    0,    0,   11,    0,    0,   12,    0,   13,   14,    5,
    6,    7,    0,    0,    0,    8,    0,    0,   74,    0,
    9,   10,    0,    0,   11,    0,    0,   12,    0,   13,
   14,  210,    6,    7,  177,   90,   88,  211,   89,    0,
   91,    0,    9,   10,    0,  212,   11,    0,    0,   12,
    0,   13,   14,    5,    6,    7,    0,    0,    0,    8,
    0,   59,    6,    0,    9,   10,    0,  211,   11,  240,
    0,   12,    9,   13,   14,  212,  255,    6,    0,   12,
    0,   13,  211,    0,  240,    0,    0,    9,    0,    0,
  212,   59,    6,    0,   12,    0,   13,  211,    0,    0,
  258,    0,    9,    0,    0,  212,   54,    6,    0,   12,
    0,   13,    8,    0,   55,    0,    0,    9,    0,   59,
    6,    0,   59,    6,   12,    8,   13,   55,    8,    0,
    9,  139,    0,    9,    0,  180,    6,   12,    0,   13,
   12,    8,   13,   55,    0,    0,    9,    0,   59,    6,
    0,    0,    0,   12,  211,   13,    0,    0,    0,    9,
    0,    0,  212,   59,    6,    0,   12,    0,   13,    8,
    0,  194,   90,   88,    9,   89,    0,   91,  244,   90,
   88,   12,   89,   13,   91,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         40,
    0,   40,   45,   40,   45,   40,   45,   40,   45,   40,
   45,   40,   45,   45,   45,   40,   45,   45,   45,   64,
  256,   43,  256,  257,   45,   75,   76,   77,  144,  145,
  266,   81,   41,   41,   58,    2,   59,    4,   41,   41,
   44,  258,  278,   59,   15,   59,   58,   93,   15,   59,
   44,  256,  257,   41,  271,   41,   44,   28,   44,  203,
  277,   28,  256,   41,   42,   43,   44,   45,   58,   47,
   45,  215,  266,   64,  279,  266,  192,  256,  257,  125,
   41,   59,   60,   61,   62,  131,   41,  278,   55,   44,
  269,  266,  142,   58,  264,   58,  240,  256,  257,   60,
   61,   62,   40,  278,  248,   60,   61,   62,  154,   58,
   59,   58,  158,    2,  136,    4,  203,  266,  266,  141,
  203,  125,  260,  266,   13,   59,   15,  256,  215,  278,
  278,  125,  215,  179,  266,  278,  257,  266,  184,   28,
  107,   41,   41,  264,   44,   44,  278,   59,  235,  278,
  203,  238,  235,  240,  200,  238,  243,  240,  260,  246,
  243,  248,  215,  246,  251,  248,  256,  254,  251,   59,
  280,  254,  256,  257,   60,  262,  266,  256,  257,  262,
  260,  268,  238,   42,   43,  268,   45,  243,   47,  211,
  246,   59,   58,  275,  257,  251,   44,   44,  254,  263,
   60,   61,   62,  257,   41,  227,  262,   41,  256,  257,
  258,  256,  268,  256,  257,  256,  257,  256,  257,  256,
  257,   59,  257,  271,  257,  257,  257,   40,  257,  277,
  257,  258,  256,  256,  259,  256,  257,  280,  259,  280,
  256,  280,  256,  280,  271,  280,  256,  280,  280,  280,
  277,  280,  280,  280,  263,  263,  256,  257,  258,  280,
  263,  263,  262,  275,   40,  265,  256,  267,  268,  259,
  260,  271,   40,  123,  274,   62,  276,  277,  256,  257,
  258,  256,  257,  261,  262,  263,  263,  265,  123,  267,
  268,   62,  270,  271,  272,  273,  274,   62,  276,  277,
  261,  266,  263,  266,   59,  280,  261,  256,  257,  256,
  257,  272,  273,  278,  203,  278,   41,  272,  273,   41,
   41,  257,  256,  257,  258,   41,  215,   41,  262,  266,
  279,  265,  279,  267,  268,  264,  270,  271,   41,  266,
  274,  256,  276,  277,  256,  257,  258,  266,  266,   40,
  262,   59,   59,  265,  263,  267,  268,  263,  270,  271,
   59,   59,  274,   59,  276,  277,  256,  257,  258,  266,
  266,   38,  262,  256,  266,  265,   43,  267,  268,  266,
  270,  271,    0,   50,  274,    4,  276,  277,  256,  257,
  258,  145,  215,   10,  262,   10,  101,  265,   92,  267,
  268,  261,  270,  271,   -1,   -1,  274,   -1,  276,  277,
   -1,   78,  272,  273,   81,   -1,   -1,   84,  256,  257,
  258,   -1,   -1,   -1,  262,  203,   -1,  265,   -1,  267,
  268,   -1,  270,  271,   -1,   -1,  274,  215,  276,  277,
  203,   41,   42,   43,   -1,   45,   -1,   47,   -1,   41,
   42,   43,  215,   45,  121,   47,  123,  235,   -1,   -1,
  238,  128,  240,   -1,   -1,  243,   -1,   -1,  246,   -1,
  248,   -1,  235,  251,   -1,  238,  254,  240,   -1,   -1,
  243,   -1,  149,  246,  262,  248,   -1,   -1,  251,   -1,
  268,  254,  256,  257,  258,   -1,   -1,   -1,  262,  262,
   -1,  265,   -1,  267,  268,  268,  270,  271,   -1,   -1,
  274,   -1,  276,  277,  256,  257,  258,   -1,   -1,   -1,
  262,   -1,   -1,  265,   -1,  267,  268,   -1,  270,  271,
   -1,   -1,  274,   -1,  276,  277,  256,  257,  258,   -1,
   -1,   -1,  262,   -1,   -1,  265,   -1,  267,  268,   -1,
  270,  271,   -1,   -1,  274,   -1,  276,  277,   -1,   -1,
  227,   -1,  229,  256,  257,  258,   -1,   -1,   -1,  262,
   -1,   -1,  265,   -1,  267,  268,   -1,  270,  271,   -1,
   -1,  274,   -1,  276,  277,  256,  257,  258,   -1,   -1,
   -1,  262,   -1,   -1,  265,   -1,  267,  268,   -1,   -1,
  271,   -1,   -1,  274,   -1,  276,  277,  256,  257,  258,
   -1,   -1,   -1,  262,   -1,   -1,  265,   -1,  267,  268,
   -1,   -1,  271,   -1,   -1,  274,   -1,  276,  277,  256,
  257,  258,   -1,   -1,   -1,  262,   -1,   -1,  265,   -1,
  267,  268,   -1,   -1,  271,   -1,   -1,  274,   -1,  276,
  277,  256,  257,  258,   41,   42,   43,  262,   45,   -1,
   47,   -1,  267,  268,   -1,  270,  271,   -1,   -1,  274,
   -1,  276,  277,  256,  257,  258,   -1,   -1,   -1,  262,
   -1,  256,  257,   -1,  267,  268,   -1,  262,  271,  264,
   -1,  274,  267,  276,  277,  270,  256,  257,   -1,  274,
   -1,  276,  262,   -1,  264,   -1,   -1,  267,   -1,   -1,
  270,  256,  257,   -1,  274,   -1,  276,  262,   -1,   -1,
  265,   -1,  267,   -1,   -1,  270,  256,  257,   -1,  274,
   -1,  276,  262,   -1,  264,   -1,   -1,  267,   -1,  256,
  257,   -1,  256,  257,  274,  262,  276,  264,  262,   -1,
  267,  265,   -1,  267,   -1,  256,  257,  274,   -1,  276,
  274,  262,  276,  264,   -1,   -1,  267,   -1,  256,  257,
   -1,   -1,   -1,  274,  262,  276,   -1,   -1,   -1,  267,
   -1,   -1,  270,  256,  257,   -1,  274,   -1,  276,  262,
   -1,   41,   42,   43,  267,   45,   -1,   47,   41,   42,
   43,  274,   45,  276,   47,
};
}
final static short YYFINAL=3;
final static short YYMAXTOKEN=280;
final static String yyname[] = {
"end-of-file",null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,"'('","')'","'*'","'+'","','",
"'-'",null,"'/'",null,null,null,null,null,null,null,null,null,null,"':'","';'",
"'<'","'='","'>'",null,"'@'",null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
"'{'",null,"'}'",null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,"IDENTIFICADOR","HEXADECIMAL","CADENA_MULTI",
"SIMASIGNACION","DISTINTO","IF","THEN","BEGIN","END","END_IF","OUTF","TYPEDEF",
"FUN","RET","SINGLE","MENOR_IGUAL","MAYOR_IGUAL","REPEAT","WHILE","GOTO",
"LONGINT","ELSE","TRIPLE","CONSTANTE",
};
final static String yyrule[] = {
"$accept : programa",
"programa : IDENTIFICADOR BEGIN cuerpo END",
"programa : IDENTIFICADOR BEGIN END",
"programa : IDENTIFICADOR BEGIN cuerpo",
"programa : BEGIN cuerpo END",
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
"funDeclaracion : FUN IDENTIFICADOR '(' parametro ')' BEGIN cuerpoFuncion END",
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
"expresion : operando",
"expresion : expresion operador operando",
"operador : '+'",
"operador : '-'",
"operador : '*'",
"operador : '/'",
"operando : IDENTIFICADOR",
"operando : constante",
"operando : invocacionFuncion",
"operando : IDENTIFICADOR CADENA_MULTI",
"invocacionFuncion : IDENTIFICADOR '(' expresion ')'",
"invocacionFuncion : IDENTIFICADOR '(' tipoDato expresion ')'",
"invocacionFuncion : IDENTIFICADOR '(' tipoDato '(' expresion ')' ')'",
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
"clausulaSeleccionConRet : IF '(' condicion ')' THEN error ELSE error END_IF",
"condicion : listaExpresiones comparador listaExpresiones",
"listaExpresiones : '(' listaExpresion ')'",
"listaExpresiones : expresion",
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
"bloqueSentenciaEjecutableConRet : sentenciaEjecutableConRet ';'",
"bloqueSentenciaEjecutableConRet : BEGIN cuerpoEjecutableConRet END",
"clausulaBucle : REPEAT bloqueSentenciaEjecutable WHILE '(' condicion ')'",
"clausulaBucle : REPEAT error WHILE '(' condicion ')'",
"clausulaBucle : REPEAT bloqueSentenciaEjecutable WHILE '(' error ')'",
"clausulaBucle : REPEAT error WHILE '(' error ')'",
"clausulaBucle : REPEAT bloqueSentenciaEjecutable",
"goto : GOTO etiqueta '@'",
"goto : GOTO etiqueta error",
"goto : etiqueta '@'",
"etiqueta : IDENTIFICADOR ':'",
"etiqueta : error ':'",
"etiqueta : IDENTIFICADOR error",
"mensajeSalida : OUTF '(' expresion ')'",
"mensajeSalida : OUTF '(' CADENA_MULTI ')'",
"mensajeSalida : OUTF '(' error ')'",
};

//#line 265 "gramatica.y"
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
        Parser.lex = new AnalizadorLexico("repeatWhile", matriz, matrizAcciones);
        
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
//#line 682 "Parser.java"
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
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta 'nombre del programa'."));}
break;
case 10:
//#line 48 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta ';'."));}
break;
case 11:
//#line 49 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta ';'."));}
break;
case 12:
//#line 50 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta sentencia."));}
break;
case 13:
//#line 53 "gramatica.y"
{estructuras.add("Declaracion");}
break;
case 14:
//#line 54 "gramatica.y"
{estructuras.add("Declaracion de typedef");}
break;
case 15:
//#line 55 "gramatica.y"
{estructuras.add("Declaracion de funcion");}
break;
case 16:
//#line 56 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera 'objeto a declarar'."));}
break;
case 19:
//#line 61 "gramatica.y"
{ erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera 'TYPEDEF antes de la declaracion del subtipo'.")); }
break;
case 20:
//#line 62 "gramatica.y"
{ erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera 'TYPEDEF antes de la declaracion del triple'.")); }
break;
case 21:
//#line 63 "gramatica.y"
{ erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera 'declaracion de subtipo o triple'.")); }
break;
case 23:
//#line 67 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera 'nombre del tipo de dato nuevo'."));}
break;
case 26:
//#line 71 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera 'tipo de objeto a declarar'."));}
break;
case 27:
//#line 72 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera 'nombre de objeto a declarar'."));}
break;
case 28:
//#line 73 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera 'nombre de objeto a declarar'."));}
break;
case 29:
//#line 74 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera 'nombre y tipo de objeto a declarar'."));}
break;
case 38:
//#line 95 "gramatica.y"
{ Lexema lex = TablaDeSimbolos.getByID(val_peek(0).ival);
                        System.out.println(val_peek(0).ival);
                        System.out.println(lex);
                        System.out.println(TablaDeSimbolos.imprimir());
                        chequearRango(lex);                             /*SOLO SE CHEQUEA EN POSITIVO YA QUE EL MAXIMO DE NEGATIVOS ES MAYOR AL MAXIMO DE POSITIVOS Y YA LO CHEQUEA EL PARSER*/
                      }
break;
case 39:
//#line 101 "gramatica.y"
{
                            Lexema lex = TablaDeSimbolos.getByID(val_peek(0).ival);
                            int newLexRef = TablaDeSimbolos.agregarSimbolo("-"+lex.getAtributo(), lex.getTipo());
                            /*$2.sval = newLex.*/
                          }
break;
case 46:
//#line 124 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta ';'."));}
break;
case 47:
//#line 125 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta ';'."));}
break;
case 48:
//#line 129 "gramatica.y"
{estructuras.add("Retorno");}
break;
case 49:
//#line 133 "gramatica.y"
{estructuras.add("Asignacion");}
break;
case 50:
//#line 134 "gramatica.y"
{estructuras.add("IF");}
break;
case 51:
//#line 135 "gramatica.y"
{estructuras.add("WHILE");}
break;
case 52:
//#line 136 "gramatica.y"
{estructuras.add("GOTO");}
break;
case 53:
//#line 137 "gramatica.y"
{estructuras.add("OUTF");}
break;
case 54:
//#line 139 "gramatica.y"
{estructuras.add("Asignacion");}
break;
case 55:
//#line 140 "gramatica.y"
{estructuras.add("WHILE");}
break;
case 56:
//#line 141 "gramatica.y"
{estructuras.add("GOTO");}
break;
case 57:
//#line 142 "gramatica.y"
{estructuras.add("OUTF");}
break;
case 59:
//#line 144 "gramatica.y"
{estructuras.add("IF");}
break;
case 77:
//#line 177 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta 'end_if;'."));}
break;
case 78:
//#line 178 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta 'end_if;'."));}
break;
case 79:
//#line 179 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta ')' al final de la condicion."));}
break;
case 80:
//#line 180 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta ')' al final de la condicion."));}
break;
case 81:
//#line 181 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta '(' al comienzo de la condicion."));}
break;
case 82:
//#line 182 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta '(' al comienzo de la condicion."));}
break;
case 83:
//#line 183 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO faltan '()' en la condicion."));}
break;
case 84:
//#line 184 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO faltan '()' en la condicion."));}
break;
case 85:
//#line 185 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta bloque de sentencias ejecutables valido."));}
break;
case 86:
//#line 186 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta bloque de sentencias ejecutables valido."));}
break;
case 89:
//#line 191 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta 'end_if;'."));}
break;
case 90:
//#line 192 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta 'end_if;'."));}
break;
case 91:
//#line 193 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta ')' al final de la condicion."));}
break;
case 92:
//#line 194 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta ')' al final de la condicion."));}
break;
case 93:
//#line 195 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta '(' al comienzo de la condicion."));}
break;
case 94:
//#line 196 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta '(' al comienzo de la condicion."));}
break;
case 95:
//#line 197 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO faltan '()' en la condicion."));}
break;
case 96:
//#line 198 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO faltan '()' en la condicion."));}
break;
case 97:
//#line 199 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta bloque de sentencias ejecutables valido."));}
break;
case 98:
//#line 200 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta bloque de sentencias ejecutables valido."));}
break;
case 104:
//#line 214 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta expresion."));}
break;
case 105:
//#line 215 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta ultima expresion."));}
break;
case 114:
//#line 223 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta ';'."));}
break;
case 116:
//#line 228 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta ';'."));}
break;
case 123:
//#line 242 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta bloque de sentencias ejecutables."));}
break;
case 124:
//#line 243 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta condicion."));}
break;
case 125:
//#line 244 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta condicion y bloque de sentencias ejecutables."));}
break;
case 126:
//#line 245 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta WHILE."));}
break;
case 128:
//#line 249 "gramatica.y"
{erroresSintactico.add(new Error(numeroLineaError, Tipo.ERROR, "ERROR SINTACTICO falta '@' luego de los dospuntos."));}
break;
case 129:
//#line 250 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta 'goto' luego de los dospuntos."));}
break;
case 131:
//#line 254 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta 'etiqueta'."));}
break;
case 132:
//#line 255 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta ':' luego de la etiqueta."));}
break;
case 135:
//#line 260 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera cadena multilinea o expresion en el mensaje de salida."));}
break;
//#line 1104 "Parser.java"
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
