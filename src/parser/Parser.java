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
    0,    0,    0,    0,    1,    1,    2,    2,    2,    2,
    2,    2,    3,    3,    3,    3,    8,    8,    8,    8,
    8,   10,   10,   11,   11,   11,   11,   11,   11,   11,
   11,   11,   11,    9,    6,    6,    6,    7,    7,   12,
   12,   15,   15,   13,   14,   14,   16,   16,   16,   16,
   16,   18,    4,    4,    4,    4,    4,   17,   17,   17,
   17,   17,   17,   20,   20,   19,   19,   27,   27,   27,
   27,   26,   26,   26,   26,   28,   28,   28,   21,   21,
   21,   21,   21,   21,   21,   21,   21,   21,   21,   21,
   25,   25,   25,   25,   25,   25,   25,   25,   25,   25,
   25,   25,   29,   29,   32,   32,   34,   34,   34,   34,
   33,   33,   33,   33,   33,   33,   35,   35,   35,   30,
   30,   30,   36,   36,   31,   31,   22,   22,   22,   22,
   23,   23,   23,    5,    5,    5,   24,   24,   24,
};
final static short yylen[] = {                            2,
    4,    3,    3,    3,    2,    1,    2,    2,    1,    2,
    2,    2,    2,    1,    2,    2,    2,    2,    2,    2,
    2,    6,    6,    5,    5,    5,    5,    5,    5,    4,
    4,    3,    3,    8,    1,    1,    1,    3,    1,    3,
    1,    1,    2,    2,    2,    1,    2,    2,    1,    1,
    1,    4,    1,    1,    1,    1,    1,    1,    1,    1,
    1,    1,    1,    3,    4,    1,    3,    1,    1,    1,
    1,    1,    1,    1,    2,    4,    5,    7,    7,    9,
    7,    9,    8,    6,    8,    6,    7,    5,    7,    9,
    7,    9,    7,    9,    8,    6,    8,    6,    7,    5,
    7,    9,    3,    3,    3,    1,    3,    1,    3,    3,
    1,    1,    1,    1,    1,    1,    3,    2,    2,    2,
    2,    3,    3,    2,    2,    3,    6,    6,    6,    6,
    3,    3,    2,    2,    2,    2,    4,    4,    4,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,    0,    0,   37,    0,    0,    0,
   35,    0,    0,   36,    0,    6,    0,    0,    0,    0,
   14,   53,   54,   55,   56,   57,    2,    0,    0,    0,
    0,   12,  135,   19,   20,  136,    0,    0,  134,    0,
    0,   42,    0,    0,   73,    0,   66,   74,    0,    0,
    0,    0,    0,   17,   18,    0,    0,    0,    0,    0,
    0,    0,    0,    4,    5,   10,    7,   11,    8,  133,
   16,   39,    0,    0,   15,    1,    0,    0,    0,    0,
    0,    0,    0,    0,   75,    0,   43,    0,    0,    0,
  114,  115,  116,   68,   69,   70,   71,  111,  112,  113,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  121,  120,    0,  132,  131,    0,    0,    0,
    0,   32,    0,    0,    0,    0,   33,    0,    0,    0,
    0,    0,    0,    0,    0,  105,   67,    0,    0,    0,
  103,  110,    0,  139,  138,  137,    0,  119,  118,  122,
    0,    0,    0,   38,    0,    0,   30,    0,   31,    0,
    0,    0,    0,   76,    0,    0,   88,    0,    0,    0,
    0,  117,    0,    0,    0,    0,    0,   41,    0,   29,
   26,   28,   25,   27,   24,    0,   77,   84,    0,    0,
    0,    0,   86,    0,  130,  128,  129,  127,   44,    0,
   23,    0,   22,    0,    0,   89,    0,   81,   79,    0,
   87,    0,    0,   40,   78,   83,    0,    0,   85,    0,
    0,    0,    0,    0,    0,   46,    0,   62,   58,   59,
   60,   61,   63,   90,   82,   80,    0,    0,    0,   47,
   34,   45,   48,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   52,    0,    0,    0,    0,  125,  100,
    0,    0,   96,    0,    0,    0,  124,  126,    0,    0,
   98,    0,    0,  101,    0,   93,   91,    0,  123,   99,
    0,   95,    0,    0,   97,  102,   94,   92,
};
final static short yydgoto[] = {                          3,
   15,   16,   17,   58,   59,   20,   74,   21,   75,   34,
   35,  177,  176,  225,   45,  226,  251,  228,   46,   22,
   23,   24,   25,   26,  233,   47,  101,   48,   49,   60,
  252,   50,  102,   51,  112,  258,
};
final static short yysindex[] = {                      -188,
 -243,  217,    0,  348,   63,   42,    0,  -40,  -16, -159,
    0,  445, -227,    0,  370,    0,  -23,   18,   35,  -79,
    0,    0,    0,    0,    0,    0,    0,  392, -216, -157,
  141,    0,    0,    0,    0,    0, -134,  -29,    0,  101,
    2,    0, -133,  -38,    0,   -8,    0,    0,  -18,  147,
  105,  -25, -216,    0,    0,   -2,  502,   20,   35, -104,
  144,  -17,   48,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  -60,  160,    0,    0, -220, -220,  -31, -119,
  -51,  -29,  454,  -29,    0,  -28,    0,  -42,   -9,  100,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  -29,  -29,  458,   -7,  -38,  -20,  264,  266,  489,  269,
   33,  461,    0,    0,  271,    0,    0,  273,   66,  205,
  214,    0,   91,  292,   25,  302,    0,  454,  454,  -30,
  632,  454,  100,  458,  104,    0,    0,  454, -170,  458,
    0,    0,  454,    0,    0,    0,  -36,    0,    0,    0,
  309,  -34, -220,    0,  -33,  -33,    0, -196,    0, -155,
   89,  -29,  725,    0, -132,  474,    0,  458, -123,  122,
  329,    0,  314,  335,  123,  342,    3,    0,    6,    0,
    0,    0,    0,    0,    0,  732,    0,    0,  458,    5,
 -223,  115,    0,  458,    0,    0,    0,    0,    0,  125,
    0,  -33,    0,  345,  124,    0,  146,    0,    0,  458,
    0,  134,  414,    0,    0,    0,  137, -116,    0,  -44,
  -32,  365,  352,   35,  251,    0,  355,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  -38,   -1,  -29,    0,
    0,    0,    0,    4,  -71,  162,  739,  -71,  164,  487,
  364,  -86,  -71,    0,  -84,  174,  373,  430,    0,    0,
  -71,   58,    0,  -71,   37, -207,    0,    0,  375,  169,
    0,  -71,  171,    0,  183,    0,    0,  -71,    0,    0,
  177,    0,  181,  -56,    0,    0,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    1,    0,
    0,    0,    0,    0,    0,    0,    0,  440,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   23,    0,    0,    0,    0,  315,    0,    0,    0,    0,
   53,    0,   73,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   95,    0,    0,    0,    0,    0,    0,
    0,    0,  117,    0,    0,    0,    0,    0,    0,  272,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  139,   45,    0,
    0,  -22,    0,    0,    0,    0,    0,    7,    0,    0,
    0,    0,   49,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  281,  304,    0,    0,  326,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
  447,  102, -167,   76,  114,   -5,    0,    0,    0,  449,
  453,  321,    0,    0, -128,  246,  -90,    0,  340,  -89,
    0,  204,  208,  242,    0,  377,    0,    0,  140,  -15,
  -91,  376,  433,  -26,    0,    0,
};
final static int YYTABLESIZE=786;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         44,
    9,   88,   43,   88,   43,   88,   43,  237,   43,  162,
   43,   43,   43,   33,   43,   43,   43,   90,  108,   43,
    4,  108,  104,   52,   43,   81,  178,  178,   61,   62,
  123,  135,  208,   96,   94,   67,   95,    7,   97,  246,
   39,   86,  209,   77,  249,  223,  202,  104,  276,  202,
   11,   98,  100,   99,  210,   33,   14,  223,  277,  180,
  181,  133,   33,   72,   72,   72,   72,   72,    1,   72,
  278,  120,  121,  214,  126,    2,   69,   18,  114,   18,
  130,   72,   72,   72,   72,  109,  160,  139,  109,  107,
   18,  149,  107,  106,   33,  167,   53,   30,   70,   39,
  182,  183,   78,   18,  109,  109,  109,  168,  107,  107,
  107,  117,  106,  106,  106,   19,   65,   19,  165,   31,
   33,   32,  227,  229,  169,   82,   63,  201,   19,   65,
  203,   21,  111,  188,  227,  229,  124,  125,    7,  235,
  136,   19,  193,  106,   84,  189,   87,  175,  106,  236,
  191,   11,  192,   13,  194,  229,  255,   14,  229,  257,
  229,  262,  195,  229,  266,   84,  229,  269,  229,  270,
  115,  229,  273,  205,  229,   64,   71,   72,  212,  260,
  281,  263,  229,   89,   61,    6,  284,  151,  229,   73,
  221,  261,  250,  264,  218,    9,  118,   65,  222,  287,
   80,   33,   12,  119,   13,  127,   98,  100,   99,  288,
   90,   29,   30,   40,   41,   40,   41,   40,   41,  170,
   41,  173,   41,   40,   41,  122,   41,   41,   41,    7,
  107,   41,   66,  108,   31,  142,   41,   42,   36,   42,
  108,   42,   11,   42,  103,   42,   42,   42,   14,   42,
   42,   42,   91,  134,   42,  140,    9,    9,    9,   42,
   85,  245,    9,   92,   93,    9,  248,    9,    9,  104,
  206,    9,  110,   68,    9,  113,    9,    9,   72,   72,
   72,  159,  207,   72,   72,   72,  171,   72,  148,   72,
   72,  174,   72,   72,   72,   72,   72,   36,   72,   72,
   37,   38,  274,  116,  144,  109,  145,  109,  147,  107,
  152,  107,  153,  106,  275,  106,  109,  109,   29,   30,
  107,  107,  154,  271,  106,  106,  224,  155,   21,   21,
   21,  106,  106,  106,   21,  272,  156,   21,  224,   21,
   21,   31,   21,   21,  184,  185,   21,  157,   21,   21,
   13,   13,   13,  158,  197,  108,   13,   84,  108,   13,
  238,   13,   13,  161,   13,   13,  166,  172,   13,  196,
   13,   13,   64,   64,   64,  198,  244,   83,   64,  199,
  211,   64,  200,   64,   64,  215,   64,   64,  213,  216,
   64,  109,   64,   64,   65,   65,   65,   79,    7,  219,
   65,  217,  234,   65,  239,   65,   65,   91,   65,   65,
  240,   11,   65,  243,   65,   65,  230,   14,   92,   93,
  231,  128,  259,  129,  253,  131,  256,  132,  230,  265,
    6,  267,  231,  279,  280,  221,  282,  250,  283,    3,
    9,  138,  285,  222,  132,  143,  286,   12,  230,   13,
   28,  230,  231,  230,  232,  231,  230,  231,   54,  230,
  231,  230,   55,  231,  230,  231,  232,  230,  231,  163,
  242,  231,    5,    6,    7,  230,  179,  137,    8,  231,
  141,  230,  105,    9,   10,  231,  232,   11,    0,  232,
   12,  232,   13,   14,  232,   96,   94,  232,   95,  232,
   97,  186,  232,    0,    0,  232,  220,    6,    7,    0,
    0,    0,  221,  232,    0,  241,    0,    9,   10,  232,
  222,   11,    0,    0,   12,    0,   13,   14,    0,  146,
   96,   94,  106,   95,    0,   97,   50,   50,   50,    0,
    0,    0,   50,  106,  106,   50,    0,   50,   50,    0,
   50,   50,    0,    0,   50,    0,   50,   50,    0,   49,
   49,   49,    0,    0,    0,   49,    0,    0,   49,    0,
   49,   49,    0,   49,   49,    0,    0,   49,  247,   49,
   49,   51,   51,   51,    0,    0,    0,   51,    0,    0,
   51,    0,   51,   51,    0,   51,   51,    0,    0,   51,
    0,   51,   51,    5,    6,    7,    0,    0,    0,    8,
    0,    0,   27,    0,    9,   10,    0,    0,   11,    0,
    0,   12,    0,   13,   14,    5,    6,    7,    0,    0,
    0,    8,    0,    0,   64,    0,    9,   10,    0,    0,
   11,    0,    0,   12,    0,   13,   14,    5,    6,    7,
    0,    0,    0,    8,    0,    0,   76,    0,    9,   10,
    0,    0,   11,    0,    0,   12,    0,   13,   14,  220,
    6,    7,  164,   96,   94,  221,   95,    0,   97,    0,
    9,   10,    0,  222,   11,   61,    6,   12,    0,   13,
   14,  221,    0,    0,  268,    0,    9,    0,    0,  222,
   56,    6,    0,   12,    0,   13,    8,    0,   57,    0,
    0,    9,    0,   61,    6,    0,   61,    6,   12,    8,
   13,   57,    8,    0,    9,  150,    0,    9,    0,  190,
    6,   12,    0,   13,   12,    8,   13,   57,    0,    0,
    9,    0,   61,    6,    0,    0,    0,   12,  221,   13,
    0,    0,    0,    9,    0,    0,  222,   61,    6,    0,
   12,    0,   13,    8,    0,  187,   96,   94,    9,   95,
    0,   97,  204,   96,   94,   12,   95,   13,   97,  254,
   96,   94,    0,   95,    0,   97,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         40,
    0,   40,   45,   40,   45,   40,   45,   40,   45,   40,
   45,   45,   45,   58,   45,   45,   45,   44,   41,   45,
  264,   44,   41,   40,   45,   31,  155,  156,  256,  257,
   62,   41,  256,   42,   43,   59,   45,  258,   47,   41,
   58,   40,  266,  260,   41,  213,   44,   41,  256,   44,
  271,   60,   61,   62,  278,   58,  277,  225,  266,  256,
  257,   88,   58,   41,   42,   43,   44,   45,  257,   47,
  278,   77,   78,  202,   80,  264,   59,    2,   59,    4,
   86,   59,   60,   61,   62,   41,   62,  103,   44,   41,
   15,   59,   44,   41,   58,  266,  256,  257,   64,   58,
  256,  257,  260,   28,   60,   61,   62,  278,   60,   61,
   62,   64,   60,   61,   62,    2,   15,    4,  134,  279,
   58,   59,  213,  213,  140,  260,   13,  125,   15,   28,
  125,   59,   57,  266,  225,  225,  256,  257,  258,  256,
   41,   28,  266,   44,   44,  278,  280,  153,   44,  266,
  166,  271,  168,   59,  278,  245,  248,  277,  248,  250,
  250,  253,   41,  253,  256,   44,  256,  258,  258,  261,
  275,  261,  264,  189,  264,   59,  256,  257,  194,  266,
  272,  266,  272,   44,  256,  257,  278,  112,  278,  269,
  262,  278,  264,  278,  210,  267,  257,   59,  270,  256,
   60,   58,  274,   44,  276,  257,   60,   61,   62,  266,
  237,  256,  257,  256,  257,  256,  257,  256,  257,  256,
  257,  256,  257,  256,  257,  257,  257,  257,  257,  258,
  256,  257,  256,  259,  279,  256,  257,  280,  256,  280,
  263,  280,  271,  280,  263,  280,  280,  280,  277,  280,
  280,  280,  261,  263,  280,  263,  256,  257,  258,  280,
  259,  263,  262,  272,  273,  265,  263,  267,  268,  263,
  266,  271,  275,  256,  274,  256,  276,  277,  256,  257,
  258,  257,  278,  261,  262,  263,  147,  265,  256,  267,
  268,  152,  270,  271,  272,  273,  274,  256,  276,  277,
  259,  260,  266,  256,   41,  261,   41,  263,   40,  261,
   40,  263,   40,  261,  278,  263,  272,  273,  256,  257,
  272,  273,  257,  266,  272,  273,  213,  123,  256,  257,
  258,   60,   61,   62,  262,  278,  123,  265,  225,  267,
  268,  279,  270,  271,  256,  257,  274,  257,  276,  277,
  256,  257,  258,   62,   41,   41,  262,   44,   44,  265,
  221,  267,  268,   62,  270,  271,  263,   59,  274,   41,
  276,  277,  256,  257,  258,   41,  237,   38,  262,  257,
  266,  265,   41,  267,  268,   41,  270,  271,  264,  266,
  274,   52,  276,  277,  256,  257,  258,  257,  258,  266,
  262,  256,  266,  265,   40,  267,  268,  261,  270,  271,
   59,  271,  274,   59,  276,  277,  213,  277,  272,  273,
  213,   82,   59,   84,  263,   86,  263,   88,  225,  256,
  257,   59,  225,   59,  266,  262,  266,  264,  256,    0,
  267,  102,  266,  270,  105,  106,  266,  274,  245,  276,
    4,  248,  245,  250,  213,  248,  253,  250,   10,  256,
  253,  258,   10,  256,  261,  258,  225,  264,  261,  130,
  225,  264,  256,  257,  258,  272,  156,  101,  262,  272,
  105,  278,   50,  267,  268,  278,  245,  271,   -1,  248,
  274,  250,  276,  277,  253,   42,   43,  256,   45,  258,
   47,  162,  261,   -1,   -1,  264,  256,  257,  258,   -1,
   -1,   -1,  262,  272,   -1,  265,   -1,  267,  268,  278,
  270,  271,   -1,   -1,  274,   -1,  276,  277,   -1,   41,
   42,   43,  261,   45,   -1,   47,  256,  257,  258,   -1,
   -1,   -1,  262,  272,  273,  265,   -1,  267,  268,   -1,
  270,  271,   -1,   -1,  274,   -1,  276,  277,   -1,  256,
  257,  258,   -1,   -1,   -1,  262,   -1,   -1,  265,   -1,
  267,  268,   -1,  270,  271,   -1,   -1,  274,  239,  276,
  277,  256,  257,  258,   -1,   -1,   -1,  262,   -1,   -1,
  265,   -1,  267,  268,   -1,  270,  271,   -1,   -1,  274,
   -1,  276,  277,  256,  257,  258,   -1,   -1,   -1,  262,
   -1,   -1,  265,   -1,  267,  268,   -1,   -1,  271,   -1,
   -1,  274,   -1,  276,  277,  256,  257,  258,   -1,   -1,
   -1,  262,   -1,   -1,  265,   -1,  267,  268,   -1,   -1,
  271,   -1,   -1,  274,   -1,  276,  277,  256,  257,  258,
   -1,   -1,   -1,  262,   -1,   -1,  265,   -1,  267,  268,
   -1,   -1,  271,   -1,   -1,  274,   -1,  276,  277,  256,
  257,  258,   41,   42,   43,  262,   45,   -1,   47,   -1,
  267,  268,   -1,  270,  271,  256,  257,  274,   -1,  276,
  277,  262,   -1,   -1,  265,   -1,  267,   -1,   -1,  270,
  256,  257,   -1,  274,   -1,  276,  262,   -1,  264,   -1,
   -1,  267,   -1,  256,  257,   -1,  256,  257,  274,  262,
  276,  264,  262,   -1,  267,  265,   -1,  267,   -1,  256,
  257,  274,   -1,  276,  274,  262,  276,  264,   -1,   -1,
  267,   -1,  256,  257,   -1,   -1,   -1,  274,  262,  276,
   -1,   -1,   -1,  267,   -1,   -1,  270,  256,  257,   -1,
  274,   -1,  276,  262,   -1,   41,   42,   43,  267,   45,
   -1,   47,   41,   42,   43,  274,   45,  276,   47,   41,
   42,   43,   -1,   45,   -1,   47,
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
"declaracionTriple : TRIPLE IDENTIFICADOR '>' IDENTIFICADOR",
"declaracionTriple : TRIPLE '<' IDENTIFICADOR IDENTIFICADOR",
"declaracionTriple : TRIPLE IDENTIFICADOR IDENTIFICADOR",
"declaracionTriple : TRIPLE tipoDato IDENTIFICADOR",
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
"condicion : expresion comparador expresion",
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
"bloqueSentenciaEjecutableConRet : sentenciaEjecutableConRet ';'",
"bloqueSentenciaEjecutableConRet : BEGIN cuerpoEjecutableConRet END",
"clausulaBucle : REPEAT bloqueSentenciaEjecutable WHILE '(' condicion ')'",
"clausulaBucle : REPEAT error WHILE '(' condicion ')'",
"clausulaBucle : REPEAT bloqueSentenciaEjecutable WHILE '(' error ')'",
"clausulaBucle : REPEAT error WHILE '(' error ')'",
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

//#line 267 "gramatica.y"
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
        Parser.lex = new AnalizadorLexico("CP2-2", matriz, matrizAcciones);

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
//#line 684 "Parser.java"
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
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta 'nombre del programa'."));}
break;
case 10:
//#line 47 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta ';'."));}
break;
case 11:
//#line 48 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta ';'."));}
break;
case 12:
//#line 49 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta sentencia."));}
break;
case 13:
//#line 52 "gramatica.y"
{estructuras.add("Declaracion");}
break;
case 14:
//#line 53 "gramatica.y"
{estructuras.add("Declaracion de typedef");}
break;
case 15:
//#line 54 "gramatica.y"
{estructuras.add("Declaracion de funcion");}
break;
case 16:
//#line 55 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera 'objeto a declarar'."));}
break;
case 19:
//#line 60 "gramatica.y"
{ erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera 'TYPEDEF antes de la declaracion del subtipo'.")); }
break;
case 20:
//#line 61 "gramatica.y"
{ erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera 'TYPEDEF antes de la declaracion del triple'.")); }
break;
case 21:
//#line 62 "gramatica.y"
{ erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera 'declaracion de subtipo o triple'.")); }
break;
case 23:
//#line 66 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera 'nombre del tipo de dato nuevo'."));}
break;
case 26:
//#line 70 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera 'tipo de objeto a declarar'."));}
break;
case 27:
//#line 71 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera 'nombre de objeto a declarar'."));}
break;
case 28:
//#line 72 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera 'nombre de objeto a declarar'."));}
break;
case 29:
//#line 73 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera 'nombre y tipo de objeto a declarar'."));}
break;
case 30:
//#line 74 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta '<' al inicio del identificador'."));}
break;
case 31:
//#line 75 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta '>' al final del identificador'."));}
break;
case 32:
//#line 76 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO faltan '<>'."));}
break;
case 33:
//#line 77 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO faltan '<>'."));}
break;
case 42:
//#line 99 "gramatica.y"
{ Lexema lex = TablaDeSimbolos.getByID(val_peek(0).ival);
                        System.out.println(val_peek(0).ival);
                        System.out.println(lex);
                        System.out.println(TablaDeSimbolos.imprimir());
                        chequearRango(lex);                             /*SOLO SE CHEQUEA EN POSITIVO YA QUE EL MAXIMO DE NEGATIVOS ES MAYOR AL MAXIMO DE POSITIVOS Y YA LO CHEQUEA EL PARSER*/
                      }
break;
case 43:
//#line 105 "gramatica.y"
{
                            Lexema lex = TablaDeSimbolos.getByID(val_peek(0).ival);
                            int newLexRef = TablaDeSimbolos.agregarSimbolo("-"+lex.getAtributo(), lex.getTipo());
                            /*$2.sval = newLex.*/
                          }
break;
case 50:
//#line 128 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta ';'."));}
break;
case 51:
//#line 129 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta ';'."));}
break;
case 52:
//#line 133 "gramatica.y"
{estructuras.add("Retorno");}
break;
case 53:
//#line 137 "gramatica.y"
{estructuras.add("Asignacion");}
break;
case 54:
//#line 138 "gramatica.y"
{estructuras.add("IF");}
break;
case 55:
//#line 139 "gramatica.y"
{estructuras.add("WHILE");}
break;
case 56:
//#line 140 "gramatica.y"
{estructuras.add("GOTO");}
break;
case 57:
//#line 141 "gramatica.y"
{estructuras.add("OUTF");}
break;
case 58:
//#line 143 "gramatica.y"
{estructuras.add("Asignacion");}
break;
case 59:
//#line 144 "gramatica.y"
{estructuras.add("WHILE");}
break;
case 60:
//#line 145 "gramatica.y"
{estructuras.add("GOTO");}
break;
case 61:
//#line 146 "gramatica.y"
{estructuras.add("OUTF");}
break;
case 63:
//#line 148 "gramatica.y"
{estructuras.add("IF");}
break;
case 81:
//#line 181 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta 'end_if;'."));}
break;
case 82:
//#line 182 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta 'end_if;'."));}
break;
case 83:
//#line 183 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta ')' al final de la condicion."));}
break;
case 84:
//#line 184 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta ')' al final de la condicion."));}
break;
case 85:
//#line 185 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta '(' al comienzo de la condicion."));}
break;
case 86:
//#line 186 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta '(' al comienzo de la condicion."));}
break;
case 87:
//#line 187 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO faltan '()' en la condicion."));}
break;
case 88:
//#line 188 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO faltan '()' en la condicion."));}
break;
case 89:
//#line 189 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta bloque de sentencias ejecutables valido."));}
break;
case 90:
//#line 190 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta bloque de sentencias ejecutables valido."));}
break;
case 93:
//#line 195 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta 'end_if;'."));}
break;
case 94:
//#line 196 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta 'end_if;'."));}
break;
case 95:
//#line 197 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta ')' al final de la condicion."));}
break;
case 96:
//#line 198 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta ')' al final de la condicion."));}
break;
case 97:
//#line 199 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta '(' al comienzo de la condicion."));}
break;
case 98:
//#line 200 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta '(' al comienzo de la condicion."));}
break;
case 99:
//#line 201 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO faltan '()' en la condicion."));}
break;
case 100:
//#line 202 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO faltan '()' en la condicion."));}
break;
case 101:
//#line 203 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta bloque de sentencias ejecutables valido."));}
break;
case 102:
//#line 204 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta bloque de sentencias ejecutables valido."));}
break;
case 106:
//#line 212 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO faltan ()."));}
break;
case 109:
//#line 217 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta expresion."));}
break;
case 110:
//#line 218 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta ultima expresion."));}
break;
case 119:
//#line 226 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta ';'."));}
break;
case 121:
//#line 231 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta ';'."));}
break;
case 128:
//#line 245 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta bloque de sentencias ejecutables."));}
break;
case 129:
//#line 246 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta condicion."));}
break;
case 130:
//#line 247 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta condicion y bloque de sentencias ejecutables."));}
break;
case 132:
//#line 251 "gramatica.y"
{erroresSintactico.add(new Error(numeroLineaError, Tipo.ERROR, "ERROR SINTACTICO falta '@' luego de los dospuntos."));}
break;
case 133:
//#line 252 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta 'goto' luego de los dospuntos."));}
break;
case 135:
//#line 256 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta 'etiqueta'."));}
break;
case 136:
//#line 257 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta ':' luego de la etiqueta."));}
break;
case 139:
//#line 262 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera cadena multilinea o expresion en el mensaje de salida."));}
break;
//#line 1122 "Parser.java"
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
