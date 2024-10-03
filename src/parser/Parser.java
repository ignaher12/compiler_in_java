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
//#line 29 "Parser.java"




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
   11,    9,    9,    9,    9,    6,    6,    6,    7,    7,
   12,   12,   15,   15,   13,   13,   13,   14,   14,   16,
   16,   16,   16,   16,   18,    4,    4,    4,    4,    4,
   25,   17,   26,   17,   27,   17,   28,   17,   17,   17,
   20,   20,   19,   19,   31,   31,   31,   31,   30,   30,
   30,   30,   32,   32,   32,   32,   32,   32,   32,   21,
   21,   21,   21,   21,   21,   21,   21,   21,   21,   21,
   21,   21,   29,   29,   29,   29,   29,   29,   29,   29,
   29,   29,   29,   29,   29,   33,   36,   36,   38,   38,
   38,   38,   37,   37,   37,   37,   37,   37,   39,   39,
   39,   34,   34,   34,   40,   40,   35,   35,   22,   22,
   22,   22,   23,   23,   23,    5,    5,    5,   24,   24,
   24,
};
final static short yylen[] = {                            2,
    4,    3,    3,    4,    3,    4,    2,    1,    2,    2,
    1,    2,    2,    2,    2,    1,    2,    2,    2,    2,
    2,    2,    2,    6,    6,    5,    5,    5,    5,    5,
    5,    8,    8,   10,    8,    1,    1,    1,    3,    1,
    3,    1,    1,    2,    2,    2,    2,    2,    1,    2,
    2,    1,    1,    1,    4,    1,    1,    1,    1,    1,
    0,    2,    0,    2,    0,    2,    0,    2,    1,    1,
    3,    4,    1,    3,    1,    1,    1,    1,    1,    1,
    1,    2,    4,    5,    7,    6,    4,    7,    9,    7,
    9,    7,    9,    8,    6,    8,    6,    7,    5,    7,
    8,    9,    7,    9,    7,    9,    8,    6,    8,    6,
    7,    5,    7,    8,    9,    3,    3,    1,    3,    1,
    3,    3,    1,    1,    1,    1,    1,    1,    3,    2,
    2,    2,    2,    3,    3,    2,    1,    3,    6,    6,
    6,    6,    3,    3,    2,    2,    2,    2,    4,    4,
    4,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,   38,    0,    0,
    0,   36,    0,    0,   37,    0,    8,    0,    0,    0,
    0,   16,   56,   57,   58,   59,   60,    0,    2,    0,
    0,    0,    0,   14,  147,   21,   22,  148,    0,    0,
  146,    0,   43,    0,    0,   80,    0,   73,   81,    0,
    0,    0,    0,   19,   20,    0,    0,    0,    0,    0,
    0,    0,    0,    5,    7,   12,    9,   13,   10,  145,
   18,   40,    0,    0,   17,    0,    4,    1,    0,    0,
    0,    0,    0,   82,    0,   44,    0,    0,    0,    0,
    0,   75,   76,   77,   78,    0,    0,    0,  126,  127,
  128,  123,  124,  125,    0,    0,    0,    0,    0,    0,
    0,  133,  132,    0,  144,  143,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  117,    0,   74,    0,    0,  116,  151,  150,
  149,    0,  131,  130,  134,    0,    0,    0,    0,   39,
    0,    0,    0,    0,    0,   87,    0,    0,   83,    0,
    0,    0,    0,  122,    0,   99,    0,    0,    0,    0,
  129,    0,    0,    0,    0,    0,    0,    0,   42,    0,
   31,   28,   30,   27,   29,   26,    0,   84,    0,    0,
   95,    0,    0,    0,    0,   97,    0,  142,  140,  141,
  139,   46,   47,   45,    0,    0,    0,   25,    0,   24,
    0,    0,   86,    0,  100,    0,   92,   90,    0,   98,
    0,    0,    0,    0,   41,   85,    0,   88,   94,    0,
  101,    0,   96,    0,    0,    0,    0,    0,    0,   49,
    0,   69,   61,   63,   65,   67,   70,    0,    0,    0,
    0,  102,   93,   91,    0,    0,    0,   50,   33,   48,
   51,   62,   64,   66,   68,   35,   32,    0,   89,    0,
    0,    0,    0,    0,    0,    0,    0,  137,    0,    0,
   55,   34,    0,    0,    0,    0,  112,    0,    0,  108,
    0,    0,    0,  136,  138,    0,    0,  110,    0,    0,
  113,    0,  105,  103,    0,  135,  111,    0,  107,    0,
  114,    0,  109,  115,  106,  104,
};
final static short yydgoto[] = {                          3,
   16,   17,   18,   58,   59,   21,   74,   22,   75,   36,
   37,  178,  176,  239,   46,  240,  278,  242,   47,   23,
   24,   25,   26,   27,  262,  263,  264,  265,  247,   48,
   96,   49,   50,   60,  279,   51,  105,   91,  111,  286,
};
final static short yysindex[] = {                      -104,
 -201,  536,    0,  536,  262,   55,  -56,    0,  -28,    6,
 -217,    0,  625, -152,    0,  426,    0,  -21,  -11,   -5,
  -72,    0,    0,    0,    0,    0,    0,  448,    0,  470,
 -172, -110,  101,    0,    0,    0,    0,    0,  -93,  -36,
    0,  -17,    0, -108,  -40,    0,  389,    0,    0,   -7,
   66,   49, -172,    0,    0,   -1,  675,   31,   -5,  -76,
  155,  -23,  -42,    0,    0,    0,    0,    0,    0,    0,
    0,    0, -134,  162,    0,   55,    0,    0, -113, -113,
  -62,  -36,  389,    0,   46,    0,  174,  -34,  389,    4,
  148,    0,    0,    0,    0,  -36,  638,  -35,    0,    0,
    0,    0,    0,    0,  -26,  189,  191,  403,  209,   36,
 -127,    0,    0,  267,    0,    0,  275,  285,   78,  225,
  227,  295,  297,  300,  389,  326,  -20,  903,  -36,  389,
  638,  110,    0,   -9,    0, -237,  638,    0,    0,    0,
    0,  -37,    0,    0,    0,  322,  -30, -184, -184,    0,
  -18,  -18,   63,  114,  146,    0,  -36,  911,    0,  128,
  389, -186,  651,    0,  389,    0,  638, -124,  349,  352,
    0,  355,  363,  152,  159,  371,  164,  -16,    0,  -13,
    0,    0,    0,    0,    0,    0,  710,    0,  169,  377,
    0,  638,   50, -177,  160,    0,  638,    0,    0,    0,
    0,    0,    0,    0,  165,  171,  172,    0,  -18,    0,
  195,  396,    0,  183,    0,  182,    0,    0,  567,    0,
  185,  492,  514,  412,    0,    0,  198,    0,    0,  194,
    0, -135,    0,  -45,  -19,  422,  398,   -5,  293,    0,
  406,    0,    0,    0,    0,    0,    0,   57,  315,  199,
  433,    0,    0,    0,  -40,   10,  -36,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  492,    0,   19,
  580,  204,  732,  338,  580,  214,  654,    0, -100,  580,
    0,    0,  -91,  595,  419,  610,    0,  580,  -68,    0,
  580,   52, -160,    0,    0,  423,  215,    0,  580,  218,
    0,  230,    0,    0,  552,    0,    0,  221,    0,  224,
    0,  -75,    0,    0,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    1,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  495,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   23,    0,    0,    0,    0,   -8,    0,    0,    0,
    0,    0,  121,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  143,    0,  498,    0,    0,    0,    0,
    0,    0,   75,    0,    0,    0,    0,    0,   37,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   98,    0,    0,    0,    0,  280,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  303,    0,    0,    0,  317,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  360,  382,    0,    0,
  404,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
  417,   28, -106,   45,    2,  362,    0,    0,    0,  489,
  491,  357,  358, -199, -109, -163, -103,    0,  342,  -98,
    0,  184,  217,  257,    0,    0,    0,    0,    0,  408,
    0,    0,  238,  -60, -136,  407,    0,    0,    0,    0,
};
final static int YYTABLESIZE=958;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         88,
   11,   41,   88,   20,   44,   20,   20,   44,   44,   88,
   44,   45,   35,   88,   44,   63,   44,   20,   44,  157,
  255,  116,   85,  249,   44,   44,   44,  209,  166,   20,
  209,   20,  118,   98,   41,   44,  136,   67,   53,   32,
  167,  179,  179,   65,  132,   52,   19,   69,   19,   19,
  272,  118,  118,  118,    4,   65,   35,   65,   70,  276,
   19,   33,    5,   79,   79,   79,   79,   79,  274,   79,
  162,  174,   19,    8,   19,  260,  168,  120,  217,  191,
  120,   79,   79,   79,   79,  260,   12,   79,  218,  113,
   44,  192,   15,   44,  144,  303,  118,  118,  118,  225,
  219,  110,  194,   61,   62,  304,  195,   35,  208,   35,
  260,  210,   35,   34,   35,  237,  237,  305,  241,  241,
  253,  117,  118,  243,  243,  102,  104,  103,   61,    7,
  254,  214,  237,   71,    9,  241,  221,  145,  283,   10,
  243,  196,  237,  289,    8,  241,   13,  293,   14,   80,
  243,  297,    1,  197,  300,  146,   72,   12,  232,    2,
   81,  237,  308,   15,  241,  287,   82,  237,  312,  243,
  241,   86,  243,  285,  290,  243,  243,  288,  243,   23,
  315,  243,  296,   71,   72,  243,  291,  243,  133,  243,
  316,  134,  243,  122,  123,    8,   73,  298,  114,   38,
  243,   15,   39,   40,  206,  119,  243,  207,   12,  299,
   31,   32,   35,  115,   15,   87,   42,  129,  169,   42,
   42,   87,   42,  238,  238,  172,   42,  137,   42,  139,
   42,  140,   38,   33,   66,  226,   42,   42,  227,   43,
  238,   84,   43,   43,   68,   43,  164,   42,  142,   43,
  238,   43,  118,   43,  118,   97,   11,   11,   11,   43,
   43,   43,   11,  118,  118,   11,  131,   11,   11,  238,
   43,   11,  271,  109,   11,  238,   11,   11,   79,   79,
   79,  275,   90,   79,   79,   79,  112,   79,   79,   79,
   79,  143,   79,   79,   79,   79,   79,  118,   79,   79,
   79,  126,   42,    8,  106,   42,  147,  107,  118,  118,
   31,   32,   31,   32,  148,  215,   12,  301,  181,  182,
  120,  266,   15,  120,  149,   43,   99,  216,   43,  302,
   71,   71,   71,   33,  150,   33,   71,  100,  101,   71,
   71,   71,   71,  121,   71,   71,  121,  151,   71,  152,
   71,   71,   71,   72,   72,   72,  153,  119,  154,   72,
  119,  155,   72,   72,   72,   72,  156,   72,   72,  183,
  184,   72,  163,   72,   72,   72,   23,   23,   23,  170,
  171,   83,   23,  190,  173,   23,   89,   23,   23,  198,
   23,   23,  199,  108,   23,  200,   23,   23,   15,   15,
   15,  185,  186,  201,   15,  244,  244,   15,  202,   15,
   15,  205,   15,   15,  203,  204,   15,  213,   15,   15,
   28,   30,  244,  125,  212,  220,  128,  224,  222,  130,
   94,   92,  244,   93,  223,   95,  228,  230,  245,  245,
  120,  121,  124,  141,   94,   92,  127,   93,  229,   95,
  233,  244,  250,  251,  244,  245,  258,  244,  244,  252,
  244,  257,  268,  244,  261,  245,  280,  244,  158,  244,
  161,  244,  256,  269,  244,  165,  284,  294,  246,  246,
  307,  306,  244,  309,  245,  310,  313,  245,  244,  314,
  245,  245,  270,  245,    3,  246,  245,    6,  187,   54,
  245,   55,  245,  135,  245,  246,  177,  245,  180,  175,
  175,  138,    0,    0,    0,  245,    0,    6,    7,    8,
    0,  245,    0,    9,  246,    0,   29,  246,   10,   11,
  246,  246,   12,  246,    0,   13,  246,   14,   15,    0,
  246,    0,  246,    0,  246,    0,    0,  246,  234,    7,
    8,    0,    0,    0,  235,  246,    0,  259,    0,   10,
   11,  246,  236,   12,    0,    0,   13,    0,   14,   15,
  234,    7,    8,    0,    0,    0,  235,    0,    0,  267,
    0,   10,   11,    0,  236,   12,    0,    0,   13,    0,
   14,   15,    0,  234,    7,    8,   89,    0,  273,  235,
    0,    0,  282,    0,   10,   11,    0,  236,   12,    0,
    0,   13,    0,   14,   15,   53,   53,   53,    0,    0,
    0,   53,    0,    0,   53,    0,   53,   53,    0,   53,
   53,    0,    0,   53,    0,   53,   53,   52,   52,   52,
    0,    0,    0,   52,    0,    0,   52,    0,   52,   52,
    0,   52,   52,    0,    0,   52,    0,   52,   52,   54,
   54,   54,    0,    0,    0,   54,    0,    0,   54,    0,
   54,   54,    0,   54,   54,    0,    0,   54,    0,   54,
   54,    6,    7,    8,    0,    0,    0,    9,    0,    0,
   64,    0,   10,   11,    0,    0,   12,    0,    0,   13,
    0,   14,   15,   76,    7,    8,    0,    0,    0,    9,
    0,    0,   77,    0,   10,   11,    0,    0,   12,    0,
    0,   13,    0,   14,   15,    6,    7,    8,    0,    0,
    0,    9,    0,    0,   78,    0,   10,   11,    0,    0,
   12,    0,    0,   13,    0,   14,   15,  234,    7,    8,
  211,   94,   92,  235,   93,    0,   95,    0,   10,   11,
    0,  236,   12,    0,    0,   13,    0,   14,   15,  248,
    7,    8,  281,   94,   92,  235,   93,    0,   95,    0,
   10,   11,    0,  236,   12,    0,    0,   13,    0,   14,
   15,    6,    7,    8,    0,    0,    0,    9,    0,    0,
    0,    0,   10,   11,    0,    0,   12,   61,    7,   13,
    0,   14,   15,  235,    0,  277,    0,  311,   10,    0,
    0,  236,   61,    7,    0,   13,    0,   14,    9,    0,
   57,    0,  231,   10,    0,   61,    7,    0,    0,    0,
   13,  235,   14,  277,    0,    0,   10,    0,    0,  236,
  292,    7,    0,   13,    0,   14,  235,    0,  277,    0,
    0,   10,    0,    0,  236,   61,    7,    0,   13,    0,
   14,  235,    0,    0,  295,    0,   10,    0,    0,  236,
   56,    7,    0,   13,    0,   14,    9,    0,   57,    0,
    0,   10,    0,   61,    7,    0,    0,    0,   13,    9,
   14,   57,    0,    0,   10,    0,  193,    7,    0,   61,
    7,   13,    9,   14,   57,  235,    0,   10,    0,    0,
   10,    0,    0,  236,   13,    0,   14,   13,    0,   14,
   61,    7,    0,    0,    0,    0,    9,    0,    0,    0,
    0,   10,    0,  159,   94,   92,  160,   93,   13,   95,
   14,  188,   94,   92,  189,   93,    0,   95,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         40,
    0,   58,   40,    2,   45,    4,    5,   45,   45,   40,
   45,   40,   58,   40,   45,   14,   45,   16,   45,   40,
   40,   64,   40,  223,   45,   45,   45,   44,  266,   28,
   44,   30,   41,   41,   58,   45,   97,   59,  256,  257,
  278,  151,  152,   16,   41,   40,    2,   59,    4,    5,
   41,   60,   61,   62,  256,   28,   58,   30,   64,   41,
   16,  279,  264,   41,   42,   43,   44,   45,  268,   47,
  131,  256,   28,  258,   30,  239,  137,   41,  256,  266,
   44,   59,   60,   61,   62,  249,  271,  260,  266,   59,
   45,  278,  277,   45,   59,  256,   60,   61,   62,  209,
  278,   57,  163,  256,  257,  266,  167,   58,  125,   58,
  274,  125,   58,   59,   58,  222,  223,  278,  222,  223,
  256,  256,  257,  222,  223,   60,   61,   62,  256,  257,
  266,  192,  239,   59,  262,  239,  197,  265,  275,  267,
  239,  266,  249,  280,  258,  249,  274,  284,  276,  260,
  249,  288,  257,  278,  291,  111,   59,  271,  219,  264,
   60,  268,  299,  277,  268,  266,  260,  274,  305,  268,
  274,  280,  271,  277,  266,  274,  275,  278,  277,   59,
  256,  280,  286,  256,  257,  284,  278,  286,   41,  288,
  266,   44,  291,  256,  257,  258,  269,  266,  275,  256,
  299,   59,  259,  260,   41,   44,  305,   44,  271,  278,
  256,  257,   58,  256,  277,  256,  257,   44,  256,  257,
  257,  256,  257,  222,  223,  256,  257,  263,  257,   41,
  257,   41,  256,  279,  256,   41,  257,  257,   44,  280,
  239,  259,  280,  280,  256,  280,  256,  257,   40,  280,
  249,  280,  261,  280,  263,  263,  256,  257,  258,  280,
  280,  280,  262,  272,  273,  265,  263,  267,  268,  268,
  280,  271,  263,  275,  274,  274,  276,  277,  256,  257,
  258,  263,   45,  261,  262,  263,  256,  265,  266,  267,
  268,  256,  270,  271,  272,  273,  274,  261,  276,  277,
  278,  256,  257,  258,  256,  257,   40,  259,  272,  273,
  256,  257,  256,  257,   40,  266,  271,  266,  256,  257,
   41,  265,  277,   44,   40,  280,  261,  278,  280,  278,
  256,  257,  258,  279,  257,  279,  262,  272,  273,  265,
  266,  267,  268,   41,  270,  271,   44,  123,  274,  123,
  276,  277,  278,  256,  257,  258,   62,   41,   62,  262,
   44,   62,  265,  266,  267,  268,   41,  270,  271,  256,
  257,  274,  263,  276,  277,  278,  256,  257,  258,  142,
   59,   40,  262,  256,  147,  265,   45,  267,  268,   41,
  270,  271,   41,   52,  274,   41,  276,  277,  256,  257,
  258,  256,  257,   41,  262,  222,  223,  265,  257,  267,
  268,   41,  270,  271,  256,  257,  274,   41,  276,  277,
    4,    5,  239,   82,  256,  266,   85,  256,  264,   88,
   42,   43,  249,   45,  264,   47,   41,  256,  222,  223,
   79,   80,   81,   41,   42,   43,   85,   45,  266,   47,
  266,  268,   41,  256,  271,  239,   59,  274,  275,  266,
  277,   40,  264,  280,   59,  249,  263,  284,  127,  286,
  129,  288,  235,   41,  291,  134,  263,   59,  222,  223,
  266,   59,  299,  266,  268,  256,  266,  271,  305,  266,
  274,  275,  255,  277,    0,  239,  280,    0,  157,   11,
  284,   11,  286,   96,  288,  249,  149,  291,  152,  148,
  149,  105,   -1,   -1,   -1,  299,   -1,  256,  257,  258,
   -1,  305,   -1,  262,  268,   -1,  265,  271,  267,  268,
  274,  275,  271,  277,   -1,  274,  280,  276,  277,   -1,
  284,   -1,  286,   -1,  288,   -1,   -1,  291,  256,  257,
  258,   -1,   -1,   -1,  262,  299,   -1,  265,   -1,  267,
  268,  305,  270,  271,   -1,   -1,  274,   -1,  276,  277,
  256,  257,  258,   -1,   -1,   -1,  262,   -1,   -1,  265,
   -1,  267,  268,   -1,  270,  271,   -1,   -1,  274,   -1,
  276,  277,   -1,  256,  257,  258,  255,   -1,  257,  262,
   -1,   -1,  265,   -1,  267,  268,   -1,  270,  271,   -1,
   -1,  274,   -1,  276,  277,  256,  257,  258,   -1,   -1,
   -1,  262,   -1,   -1,  265,   -1,  267,  268,   -1,  270,
  271,   -1,   -1,  274,   -1,  276,  277,  256,  257,  258,
   -1,   -1,   -1,  262,   -1,   -1,  265,   -1,  267,  268,
   -1,  270,  271,   -1,   -1,  274,   -1,  276,  277,  256,
  257,  258,   -1,   -1,   -1,  262,   -1,   -1,  265,   -1,
  267,  268,   -1,  270,  271,   -1,   -1,  274,   -1,  276,
  277,  256,  257,  258,   -1,   -1,   -1,  262,   -1,   -1,
  265,   -1,  267,  268,   -1,   -1,  271,   -1,   -1,  274,
   -1,  276,  277,  256,  257,  258,   -1,   -1,   -1,  262,
   -1,   -1,  265,   -1,  267,  268,   -1,   -1,  271,   -1,
   -1,  274,   -1,  276,  277,  256,  257,  258,   -1,   -1,
   -1,  262,   -1,   -1,  265,   -1,  267,  268,   -1,   -1,
  271,   -1,   -1,  274,   -1,  276,  277,  256,  257,  258,
   41,   42,   43,  262,   45,   -1,   47,   -1,  267,  268,
   -1,  270,  271,   -1,   -1,  274,   -1,  276,  277,  256,
  257,  258,   41,   42,   43,  262,   45,   -1,   47,   -1,
  267,  268,   -1,  270,  271,   -1,   -1,  274,   -1,  276,
  277,  256,  257,  258,   -1,   -1,   -1,  262,   -1,   -1,
   -1,   -1,  267,  268,   -1,   -1,  271,  256,  257,  274,
   -1,  276,  277,  262,   -1,  264,   -1,  266,  267,   -1,
   -1,  270,  256,  257,   -1,  274,   -1,  276,  262,   -1,
  264,   -1,  266,  267,   -1,  256,  257,   -1,   -1,   -1,
  274,  262,  276,  264,   -1,   -1,  267,   -1,   -1,  270,
  256,  257,   -1,  274,   -1,  276,  262,   -1,  264,   -1,
   -1,  267,   -1,   -1,  270,  256,  257,   -1,  274,   -1,
  276,  262,   -1,   -1,  265,   -1,  267,   -1,   -1,  270,
  256,  257,   -1,  274,   -1,  276,  262,   -1,  264,   -1,
   -1,  267,   -1,  256,  257,   -1,   -1,   -1,  274,  262,
  276,  264,   -1,   -1,  267,   -1,  256,  257,   -1,  256,
  257,  274,  262,  276,  264,  262,   -1,  267,   -1,   -1,
  267,   -1,   -1,  270,  274,   -1,  276,  274,   -1,  276,
  256,  257,   -1,   -1,   -1,   -1,  262,   -1,   -1,   -1,
   -1,  267,   -1,   41,   42,   43,   44,   45,  274,   47,
  276,   41,   42,   43,   44,   45,   -1,   47,
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
"clausulaSeleccion : IF '(' condicion ')' THEN bloqueSentenciaEjecutable ELSE END_IF",
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
"clausulaSeleccionConRet : IF '(' condicion ')' THEN bloqueSentenciaEjecutableConRet ELSE END_IF",
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
"etiqueta : error ':'",
"etiqueta : IDENTIFICADOR error",
"mensajeSalida : OUTF '(' expresion ')'",
"mensajeSalida : OUTF '(' CADENA_MULTI ')'",
"mensajeSalida : OUTF '(' error ')'",
};

//#line 277 "gramatica.y"
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
        Parser.lex = new AnalizadorLexico("testeandoErrores4", matriz, matrizAcciones);
        
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
//#line 742 "Parser.java"
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
//#line 34 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta 'cuerpo'."));}
break;
case 3:
//#line 35 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta 'END al final del programa'."));}
break;
case 4:
//#line 36 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta BEGIN del programa."));}
break;
case 5:
//#line 37 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta 'nombre del programa'."));}
break;
case 6:
//#line 38 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO delimitadores de programa."));}
break;
case 12:
//#line 49 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta ';'."));}
break;
case 13:
//#line 50 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta ';'."));}
break;
case 14:
//#line 51 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta sentencia."));}
break;
case 15:
//#line 54 "gramatica.y"
{estructuras.add("Declaracion");}
break;
case 16:
//#line 55 "gramatica.y"
{estructuras.add("Declaracion de typedef");}
break;
case 17:
//#line 56 "gramatica.y"
{estructuras.add("Declaracion de funcion");}
break;
case 18:
//#line 57 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera 'objeto a declarar'."));}
break;
case 21:
//#line 62 "gramatica.y"
{ erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera 'TYPEDEF antes de la declaracion del subtipo'.")); }
break;
case 22:
//#line 63 "gramatica.y"
{ erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera 'TYPEDEF antes de la declaracion del triple'.")); }
break;
case 23:
//#line 64 "gramatica.y"
{ erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera 'declaracion de subtipo o triple'.")); }
break;
case 25:
//#line 68 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera 'nombre del tipo de dato nuevo'."));}
break;
case 28:
//#line 72 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera 'tipo de objeto a declarar'."));}
break;
case 29:
//#line 73 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera 'nombre de objeto a declarar'."));}
break;
case 30:
//#line 74 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera 'nombre de objeto a declarar'."));}
break;
case 31:
//#line 75 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera 'nombre y tipo de objeto a declarar'."));}
break;
case 32:
//#line 77 "gramatica.y"
{if (val_peek(1).sval.equals("false"))erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta return en el cuerpo de la funcion."));}
break;
case 33:
//#line 78 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta nombre de la funcion."));}
break;
case 34:
//#line 79 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO  no puede tener mas de un parametro."));}
break;
case 35:
//#line 80 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta cuerpo con retorno."));}
break;
case 43:
//#line 99 "gramatica.y"
{ Lexema lex = TablaDeSimbolos.getByID(val_peek(0).ival);
                        System.out.println(val_peek(0).ival);
                        System.out.println(lex);
                        System.out.println(TablaDeSimbolos.imprimir());
                        chequearRango(lex);                             /*SOLO SE CHEQUEA EN POSITIVO YA QUE EL MAXIMO DE NEGATIVOS ES MAYOR AL MAXIMO DE POSITIVOS Y YA LO CHEQUEA EL PARSER*/
                      }
break;
case 44:
//#line 105 "gramatica.y"
{
                            Lexema lex = TablaDeSimbolos.getByID(val_peek(0).ival);
                            int newLexRef = TablaDeSimbolos.agregarSimbolo("-"+lex.getAtributo(), lex.getTipo());
                            /*$2.sval = newLex.*/
                          }
break;
case 46:
//#line 114 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta tipo de dato en el parametro."));}
break;
case 47:
//#line 115 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta nombre en el parametro."));}
break;
case 48:
//#line 123 "gramatica.y"
{ if (val_peek(1).sval.equals("true") || val_peek(0).sval.equals("true")) yyval.sval = "true"; else yyval.sval = "false";}
break;
case 49:
//#line 124 "gramatica.y"
{yyval.sval = val_peek(0).sval;}
break;
case 51:
//#line 128 "gramatica.y"
{yyval.sval = val_peek(1).sval;}
break;
case 53:
//#line 130 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta ';'."));}
break;
case 54:
//#line 131 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta ';'."));}
break;
case 55:
//#line 135 "gramatica.y"
{estructuras.add("Retorno");}
break;
case 56:
//#line 139 "gramatica.y"
{estructuras.add("Asignacion");}
break;
case 57:
//#line 140 "gramatica.y"
{estructuras.add("IF");}
break;
case 58:
//#line 141 "gramatica.y"
{estructuras.add("WHILE");}
break;
case 59:
//#line 142 "gramatica.y"
{estructuras.add("GOTO");}
break;
case 60:
//#line 143 "gramatica.y"
{estructuras.add("OUTF");}
break;
case 61:
//#line 145 "gramatica.y"
{estructuras.add("Asignacion");}
break;
case 62:
//#line 145 "gramatica.y"
{yyval.sval = "false";}
break;
case 63:
//#line 146 "gramatica.y"
{estructuras.add("WHILE");}
break;
case 64:
//#line 146 "gramatica.y"
{yyval.sval = "false";}
break;
case 65:
//#line 147 "gramatica.y"
{estructuras.add("GOTO");}
break;
case 66:
//#line 147 "gramatica.y"
{yyval.sval = "false";}
break;
case 67:
//#line 148 "gramatica.y"
{estructuras.add("OUTF");}
break;
case 68:
//#line 148 "gramatica.y"
{yyval.sval = "false";}
break;
case 69:
//#line 149 "gramatica.y"
{yyval.sval = "true";}
break;
case 70:
//#line 150 "gramatica.y"
{estructuras.add("IF"); yyval.sval = val_peek(0).sval;}
break;
case 86:
//#line 179 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO numero de expresiones invalido en el llamado a funcion."));}
break;
case 87:
//#line 180 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta expresion en el llamado a funcion."));}
break;
case 88:
//#line 181 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO numero de expresiones invalido en el llamado a funcion."));}
break;
case 89:
//#line 182 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO numero de expresion esinvalido en el llamado a funcion."));}
break;
case 92:
//#line 187 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta 'end_if;'."));}
break;
case 93:
//#line 188 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta 'end_if;'."));}
break;
case 94:
//#line 189 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta ')' al final de la condicion."));}
break;
case 95:
//#line 190 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta ')' al final de la condicion."));}
break;
case 96:
//#line 191 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta '(' al comienzo de la condicion."));}
break;
case 97:
//#line 192 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta '(' al comienzo de la condicion."));}
break;
case 98:
//#line 193 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO faltan '()' en la condicion."));}
break;
case 99:
//#line 194 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO faltan '()' en la condicion."));}
break;
case 100:
//#line 195 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta bloque de sentencias ejecutables valido."));}
break;
case 101:
//#line 196 "gramatica.y"
{yyval.sval = "false";erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta bloque de sentencias ejecutables valido."));}
break;
case 102:
//#line 197 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta bloque de sentencias ejecutables valido."));}
break;
case 103:
//#line 201 "gramatica.y"
{yyval.sval = "false";}
break;
case 104:
//#line 202 "gramatica.y"
{ if (val_peek(3).sval.equals("true") && val_peek(1).sval.equals("true")) yyval.sval = "true"; else yyval.sval = "false";}
break;
case 105:
//#line 203 "gramatica.y"
{yyval.sval = "false";erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta 'end_if;'."));}
break;
case 106:
//#line 204 "gramatica.y"
{if (val_peek(3).sval.equals("true") && val_peek(1).sval.equals("true")) yyval.sval = "true"; else yyval.sval = "false";erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta 'end_if;'."));}
break;
case 107:
//#line 205 "gramatica.y"
{if (val_peek(3).sval.equals("true") && val_peek(1).sval.equals("true")) yyval.sval = "true"; else yyval.sval = "false";erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta ')' al final de la condicion."));}
break;
case 108:
//#line 206 "gramatica.y"
{yyval.sval = "false";erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta ')' al final de la condicion."));}
break;
case 109:
//#line 207 "gramatica.y"
{if (val_peek(3).sval.equals("true") && val_peek(1).sval.equals("true")) yyval.sval = "true"; else yyval.sval = "false";erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta '(' al comienzo de la condicion."));}
break;
case 110:
//#line 208 "gramatica.y"
{yyval.sval = "false";erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta '(' al comienzo de la condicion."));}
break;
case 111:
//#line 209 "gramatica.y"
{if (val_peek(3).sval.equals("true") && val_peek(1).sval.equals("true")) yyval.sval = "true"; else yyval.sval = "false";erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO faltan '()' en la condicion."));}
break;
case 112:
//#line 210 "gramatica.y"
{yyval.sval = "false";erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO faltan '()' en la condicion."));}
break;
case 113:
//#line 211 "gramatica.y"
{yyval.sval = "false";erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta bloque de sentencias ejecutables valido."));}
break;
case 114:
//#line 212 "gramatica.y"
{yyval.sval = "false";erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta bloque de sentencias ejecutables valido."));}
break;
case 115:
//#line 213 "gramatica.y"
{yyval.sval = "false";erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO faltan los dos bloques de sentencias ejecutables validas."));}
break;
case 121:
//#line 227 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta expresion."));}
break;
case 122:
//#line 228 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta ultima expresion."));}
break;
case 131:
//#line 236 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta ';'."));}
break;
case 133:
//#line 241 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta ';'."));}
break;
case 135:
//#line 245 "gramatica.y"
{ if (val_peek(2).sval.equals("true") || val_peek(1).sval.equals("true")) yyval.sval = "true"; else yyval.sval = "false";}
break;
case 136:
//#line 246 "gramatica.y"
{yyval.sval = val_peek(1).sval;}
break;
case 137:
//#line 249 "gramatica.y"
{yyval.sval = val_peek(0).sval;}
break;
case 138:
//#line 250 "gramatica.y"
{yyval.sval = val_peek(1).sval;}
break;
case 140:
//#line 255 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta bloque de sentencias ejecutables."));}
break;
case 141:
//#line 256 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta condicion."));}
break;
case 142:
//#line 257 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta condicion y bloque de sentencias ejecutables."));}
break;
case 144:
//#line 261 "gramatica.y"
{erroresSintactico.add(new Error(numeroLineaError, Tipo.ERROR, "ERROR SINTACTICO falta '@' luego de los dospuntos."));}
break;
case 145:
//#line 262 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta 'goto' luego de los dospuntos."));}
break;
case 147:
//#line 266 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta 'etiqueta'."));}
break;
case 148:
//#line 267 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta ':' luego de la etiqueta."));}
break;
case 151:
//#line 272 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera cadena multilinea o expresion en el mensaje de salida."));}
break;
//#line 1272 "Parser.java"
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
