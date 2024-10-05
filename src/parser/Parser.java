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
import lexico.TablaDeSimbolos.Contexto;
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
   20,   20,   19,   19,   19,   19,   31,   31,   31,   31,
   30,   30,   30,   30,   30,   30,   30,   32,   32,   32,
   32,   32,   32,   32,   21,   21,   21,   21,   21,   21,
   21,   21,   21,   21,   21,   21,   21,   21,   29,   29,
   29,   29,   29,   29,   29,   29,   29,   29,   29,   29,
   29,   29,   33,   33,   33,   33,   36,   36,   38,   38,
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
    3,    4,    1,    3,    3,    3,    1,    1,    1,    1,
    1,    1,    1,    2,    2,    2,    2,    4,    5,    7,
    6,    4,    7,    9,    7,    9,    7,    9,    8,    6,
    8,    6,    7,    5,    7,    9,    9,    9,    7,    9,
    7,    9,    8,    6,    8,    6,    7,    5,    7,    9,
    9,    9,    3,    3,    3,    2,    3,    1,    3,    1,
    3,    3,    1,    1,    1,    1,    1,    1,    3,    2,
    2,    2,    2,    3,    3,    2,    1,    3,    6,    6,
    6,    6,    3,    3,    2,    2,    2,    2,    4,    4,
    4,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,   38,    0,    0,
    0,   36,    0,    0,   37,    0,    8,    0,    0,    0,
    0,   16,   56,   57,   58,   59,   60,    0,    2,    0,
    0,    0,    0,   14,  157,   21,   22,  158,    0,    0,
  156,    0,    0,   43,    0,    0,   82,    0,   73,   83,
    0,    0,    0,    0,   19,   20,    0,    0,    0,    0,
    0,    0,    0,    0,    5,    7,   12,    9,   13,   10,
  155,   18,   40,    0,    0,   17,    0,    4,    1,    0,
    0,    0,    0,    0,    0,  136,  137,  138,   77,   78,
   79,   80,  133,  134,  135,    0,    0,   84,    0,   85,
   86,   87,   44,    0,    0,    0,    0,    0,    0,    0,
    0,  126,    0,    0,    0,    0,    0,    0,    0,  143,
  142,    0,  154,  153,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   76,  125,    0,    0,    0,    0,    0,
    0,    0,    0,  127,    0,   75,   74,    0,    0,    0,
  123,  161,  160,  159,    0,  141,  140,  144,    0,    0,
    0,    0,   39,    0,    0,    0,    0,    0,   92,    0,
    0,   88,    0,    0,    0,    0,    0,    0,  104,    0,
    0,    0,    0,  139,    0,    0,    0,    0,    0,    0,
    0,   42,    0,   31,   28,   30,   27,   29,   26,    0,
   89,    0,    0,  100,    0,    0,    0,    0,  102,    0,
  152,  150,  151,  149,   46,   47,   45,    0,    0,    0,
   25,    0,   24,    0,    0,   91,    0,  105,    0,   97,
   95,    0,  103,    0,    0,    0,    0,   41,   90,    0,
   93,   99,    0,    0,    0,    0,  101,    0,    0,    0,
    0,    0,    0,   49,    0,   69,   61,   63,   65,   67,
   70,    0,    0,    0,    0,  108,  107,  106,   98,   96,
    0,    0,    0,   50,   33,   48,   51,   62,   64,   66,
   68,   35,   32,    0,   94,    0,    0,    0,    0,    0,
    0,    0,    0,  147,    0,    0,   55,   34,    0,    0,
    0,    0,  118,    0,    0,  114,    0,    0,    0,  146,
  148,    0,    0,  116,    0,    0,  119,    0,  111,  109,
    0,  145,  117,    0,  113,    0,    0,    0,    0,  115,
  122,  121,  120,  112,  110,
};
final static short yydgoto[] = {                          3,
   16,   17,   18,   59,   60,   21,   75,   22,   76,   36,
   37,  191,  189,  253,   47,  254,  294,  256,   48,   23,
   24,   25,   26,   27,  278,  279,  280,  281,  261,   49,
  109,   50,   51,   61,  295,   52,   97,  108,  119,  302,
};
final static short yysindex[] = {                       -65,
 -216,  702,    0,  702,  570,   87,  116,    0,  -36,  -17,
 -218,    0,  292, -197,    0,  592,    0,  -13,   -7,   13,
 -174,    0,    0,    0,    0,    0,    0,  614,    0,  636,
 -209, -168,   48,    0,    0,    0,    0,    0, -124,  -38,
    0,  117,  121,    0, -138,  -34,    0,  340,    0,    0,
    4,  125,   54, -209,    0,    0,  -40,  831,   67,   13,
 -110,  126,  -42,  -47,    0,    0,    0,    0,    0,    0,
    0,    0,    0, -123,  131,    0,   87,    0,    0, -203,
 -203,  -89,  -38,  340,  340,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  -10,  -32,    0,   51,    0,
    0,    0,    0,   96,  -26,  340,    8,  132,   -1,  755,
  -62,    0,  -30,  313,  191,  428,  158,   89,  768,    0,
    0,  280,    0,    0,  289,  298,   83,  223,  228,  291,
  297,  303,  340,    0,    0,  539,  -20,  408,  -38,  417,
  340,  755,  104,    0,   17,    0,    0, -200,  755,  340,
    0,    0,    0,    0,  -18,    0,    0,    0,  314,  -16,
  -67,  -67,    0,  -15,  -15,  -50,  144,  153,    0,  -38,
 1059,    0,  123,  340, -181,  781,  340,  340,    0,  755,
 -160,   60,  355,    0,   69,  382,  161,  160,  383,  156,
  -12,    0,    3,    0,    0,    0,    0,    0,    0,  615,
    0,  173,  389,    0,  755,   59, -202,  166,    0,  755,
    0,    0,    0,    0,    0,    0,    0,  177,  178,  207,
    0,  -15,    0,  167,  433,    0,  210,    0,  794,    0,
    0,  807,    0,  215,  658,  680,  446,    0,    0,  240,
    0,    0,  -53,  236,  -37, -199,    0,   92,   -4,  464,
  447,   13,  437,    0,  448,    0,    0,    0,    0,    0,
    0,  105,  459,  241,  467,    0,    0,    0,    0,    0,
  -34,   24,  -38,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  658,    0,   40,  361,  246,  876,  482,
  361,  251,  810,    0,  -83,  361,    0,    0,  -76,  377,
  457,  710,    0,  361,  -73,    0,  361,   61, -112,    0,
    0,  465,  261,    0,  361,  264,    0,  725,    0,    0,
  740,    0,    0,  269,    0,  -21,  271,   26, -129,    0,
    0,    0,    0,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    1,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  538,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   28,    0,    0,    0,    0,   63,    0,    0,
    0,    0,    0,  221,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  255,    0,  540,    0,    0,    0,
    0,    0,    0,    0,  137,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   91,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  169,    0,    0,    0,    0,    0,    0,    0,
  171,    0,    0,    0,    0,    0,    0,    0,    0,   53,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  274,    0,    0,  333,  347,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  504,  526,    0,    0,  548,    0,    0,    0,    0,    0,
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
  212,   70,  231,  452,   -2,  339,    0,    0,    0,  530,
  531,  386,  391, -205, -131, -210,  435,    0,   10,  283,
    0,  309,  359,  385,    0,    0,    0,    0,    0,   85,
  308,    0,  157,  323,  243,  -55,  505,    0,    0,    0,
};
final static int YYTABLESIZE=1107;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         20,
   11,   20,   20,   46,   35,  105,   45,  105,   45,  105,
   45,   64,   45,   20,   45,   41,  124,   35,   45,  170,
   35,  105,   53,  105,   45,   20,   45,   20,   45,   45,
  263,  222,  192,  192,   45,  271,   35,   54,   32,    4,
   45,  135,  276,   45,  111,   68,  222,    5,  143,   85,
   80,   70,  276,  230,    8,  106,  269,  151,   62,   63,
   33,   45,  116,  231,  288,  179,  270,   12,   81,   81,
   81,   81,   81,   15,   81,  232,   71,  180,  290,  276,
  292,   72,   73,   35,  204,   66,   81,   81,   81,   81,
  238,   81,  133,  124,   74,   45,  205,   66,   45,   66,
  211,   91,   89,  128,   90,  209,   92,   82,  138,  213,
   91,   89,  221,   90,  141,   92,   35,  210,   35,   93,
   95,   94,  128,  128,  128,  121,  334,  223,   93,   95,
   94,  130,  125,  126,  130,   83,  335,   91,   89,  139,
   90,  103,   92,  319,   35,   34,  171,  157,  174,   35,
  128,  128,  128,  320,  178,   93,   95,   94,   91,   89,
   99,   90,   35,   92,  122,  321,  130,  131,    8,  100,
  101,  102,  144,   41,  127,  145,   93,   95,   94,  200,
  134,   12,  303,   35,   93,   95,   94,   15,  187,  306,
    8,    1,  314,  147,  304,   71,  219,  155,    2,  220,
  149,  307,  107,   12,  315,  194,  195,  239,  123,   15,
  240,  130,  266,   38,  130,   28,   30,   84,   43,   42,
   43,  104,   43,   84,   43,  150,   43,   72,  268,  140,
   43,  153,  252,  252,  117,   84,   43,  182,   43,  185,
   43,   44,   67,   44,  331,   44,   43,   44,   69,   44,
  252,   42,   43,   44,  146,   43,   11,   11,   11,   44,
  252,   44,   11,   44,   44,   11,  110,   11,   11,   44,
  142,   11,  177,   43,   11,   44,   11,   11,   44,   23,
  106,  252,  289,   81,   81,   81,  287,  252,   81,   81,
   81,  333,   81,   81,   81,   81,   44,   81,   81,   81,
   81,   81,  291,   81,   81,   81,  136,   43,    8,  114,
   43,  183,  115,   15,  131,  124,  186,  131,  128,  160,
   86,   12,  120,  128,  228,  128,  317,   15,  161,   86,
   44,   87,   88,   44,  128,  128,  229,  162,  318,  163,
   87,   88,   31,   32,  156,  164,  128,   31,   32,   96,
  165,  128,  166,  152,   91,   89,   86,   90,  167,   92,
   31,   32,  128,  128,  168,   33,  176,   87,   88,  282,
   33,   38,  184,  132,   39,   40,  132,   86,  203,   98,
  112,   91,   89,   33,   90,   86,   92,  129,   87,   88,
  129,   96,   71,   71,   71,  212,   87,   88,   71,  196,
  197,   71,   71,   71,   71,  272,   71,   71,  198,  199,
   71,   96,   71,   71,   71,  216,  217,  215,  128,  129,
  132,   96,  214,  218,   72,   72,   72,  286,  225,  226,
   72,  233,  148,   72,   72,   72,   72,  137,   72,   72,
  235,  236,   72,   96,   72,   72,   72,   96,  172,   91,
   89,  173,   90,   19,   92,   19,   19,   96,   91,   89,
  139,   90,  237,   92,  175,  251,  251,   19,  154,   91,
   89,  181,   90,  241,   92,  242,   23,   23,   23,   19,
  247,   19,   23,  251,   96,   23,  264,   23,   23,   96,
   23,   23,   96,  251,   23,  265,   23,   23,  207,  188,
  188,  267,  208,  273,  284,  274,  277,  285,  296,  118,
   15,   15,   15,  300,  251,  310,   15,  257,  257,   15,
  251,   15,   15,  322,   15,   15,  323,  227,   15,  325,
   15,   15,  234,  299,  330,  257,  332,    3,  305,    6,
   55,   56,  309,  258,  258,  257,  313,   57,    7,  316,
  193,  244,  190,    9,  246,   58,  113,  324,   10,    0,
  327,  258,    0,  329,    0,   13,  257,   14,    0,  257,
  159,  258,  257,  257,    0,  257,    0,    0,  257,  169,
   91,   89,  257,   90,  257,   92,  257,    0,    0,  257,
    0,    0,  258,  259,  259,  258,    0,  257,  258,  258,
  257,  258,    0,  257,  258,    0,    0,    0,  258,    0,
  258,  259,  258,    0,    0,  258,   62,    7,    0,  260,
  260,  259,  249,  258,  293,    0,  258,   10,    0,  258,
  250,    0,  308,    7,   13,    0,   14,  260,  249,    0,
  293,    0,  259,   10,    0,  259,  250,  260,  259,  259,
   13,  259,   14,    0,  259,  224,   91,   89,  259,   90,
  259,   92,  259,    0,    0,  259,    0,    0,  260,  255,
  255,  260,    0,  259,  260,  260,  259,  260,    0,  259,
  260,    0,    0,    0,  260,    0,  260,  255,  260,    0,
    0,  260,  248,    7,    8,    0,    0,  255,  249,  260,
    0,  275,  260,   10,   11,  260,  250,   12,    0,    0,
   13,    0,   14,   15,  248,    7,    8,    0,  255,    0,
  249,    0,    0,  283,  255,   10,   11,  301,  250,   12,
    0,    0,   13,    0,   14,   15,  312,  248,    7,    8,
    0,    0,    0,  249,    0,    0,  298,    0,   10,   11,
    0,  250,   12,    0,    0,   13,    0,   14,   15,   53,
   53,   53,    0,    0,    0,   53,    0,    0,   53,    0,
   53,   53,    0,   53,   53,    0,    0,   53,    0,   53,
   53,   52,   52,   52,    0,    0,    0,   52,    0,    0,
   52,    0,   52,   52,    0,   52,   52,    0,    0,   52,
    0,   52,   52,   54,   54,   54,    0,    0,    0,   54,
    0,    0,   54,    0,   54,   54,    0,   54,   54,    0,
    0,   54,    0,   54,   54,    6,    7,    8,    0,    0,
    0,    9,    0,    0,   29,    0,   10,   11,    0,    0,
   12,    0,    0,   13,    0,   14,   15,    6,    7,    8,
    0,    0,    0,    9,    0,    0,   65,    0,   10,   11,
    0,    0,   12,    0,    0,   13,    0,   14,   15,   77,
    7,    8,    0,    0,    0,    9,    0,    0,   78,    0,
   10,   11,    0,    0,   12,    0,    0,   13,    0,   14,
   15,    6,    7,    8,    0,    0,    0,    9,    0,    0,
   79,    0,   10,   11,    0,    0,   12,    0,    0,   13,
    0,   14,   15,  248,    7,    8,  297,   91,   89,  249,
   90,    0,   92,    0,   10,   11,    0,  250,   12,    0,
    0,   13,    0,   14,   15,  262,    7,    8,    0,    0,
    0,  249,    0,    0,    0,    0,   10,   11,    0,  250,
   12,    0,    0,   13,    0,   14,   15,    6,    7,    8,
    0,    0,    0,    9,    0,   62,    7,    0,   10,   11,
    0,  249,   12,    0,  311,   13,   10,   14,   15,  250,
  326,    7,    0,   13,    0,   14,  249,    0,  293,    0,
    0,   10,    0,    0,  250,  328,    7,    0,   13,    0,
   14,  249,    0,  293,    0,    0,   10,    0,    0,  250,
   62,    7,    0,   13,    0,   14,    9,    0,   58,    0,
    0,   10,    0,   62,    7,    0,    0,    0,   13,    9,
   14,    0,  158,    0,   10,    0,  206,    7,    0,    0,
    0,   13,    9,   14,   58,    0,    0,   10,    0,  243,
    7,    0,    0,    0,   13,    9,   14,   58,    0,    0,
   10,    0,  245,    7,    0,   62,    7,   13,    9,   14,
   58,  249,    0,   10,    0,    0,   10,    0,    0,  250,
   13,    0,   14,   13,    0,   14,   62,    7,    0,    0,
    0,    0,    9,    0,    0,    0,    0,   10,    0,  201,
   91,   89,  202,   90,   13,   92,   14,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                          2,
    0,    4,    5,   40,   58,   40,   45,   40,   45,   40,
   45,   14,   45,   16,   45,   58,   64,   58,   45,   40,
   58,   40,   40,   40,   45,   28,   45,   30,   45,   45,
  236,   44,  164,  165,   45,   40,   58,  256,  257,  256,
   45,   97,  253,   45,   41,   59,   44,  264,   41,   40,
  260,   59,  263,  256,  258,   46,  256,  113,  256,  257,
  279,   45,   53,  266,   41,  266,  266,  271,   41,   42,
   43,   44,   45,  277,   47,  278,   64,  278,  284,  290,
   41,  256,  257,   58,  266,   16,   59,   60,   61,   62,
  222,  260,   83,   41,  269,   45,  278,   28,   45,   30,
   41,   42,   43,   41,   45,  266,   47,   60,   99,   41,
   42,   43,  125,   45,  105,   47,   58,  278,   58,   60,
   61,   62,   60,   61,   62,   59,  256,  125,   60,   61,
   62,   41,  256,  257,   44,  260,  266,   42,   43,   44,
   45,  280,   47,  256,   58,   59,  137,   59,  139,   58,
   60,   61,   62,  266,  145,   60,   61,   62,   42,   43,
   40,   45,   58,   47,  275,  278,  256,  257,  258,   49,
   50,   51,   41,   58,   44,   44,   60,   61,   62,  170,
   96,  271,  266,   58,   60,   61,   62,  277,  256,  266,
  258,  257,  266,  109,  278,   59,   41,   40,  264,   44,
  263,  278,   46,  271,  278,  256,  257,   41,  256,  277,
   44,   41,  266,  256,   44,    4,    5,  256,  257,  256,
  257,  256,  257,  256,  257,  256,  257,   59,  266,  256,
  257,   41,  235,  236,  275,  256,  257,  256,  257,  256,
  257,  280,  256,  280,  266,  280,  257,  280,  256,  280,
  253,  256,  257,  280,  256,  257,  256,  257,  258,  280,
  263,  280,  262,  280,  280,  265,  263,  267,  268,  280,
  263,  271,  256,  257,  274,  280,  276,  277,  280,   59,
  271,  284,  273,  256,  257,  258,  263,  290,  261,  262,
  263,  266,  265,  266,  267,  268,  280,  270,  271,  272,
  273,  274,  263,  276,  277,  278,  256,  257,  258,  256,
  257,  155,  259,   59,   41,  263,  160,   44,  256,   40,
  261,  271,  256,  261,  266,  263,  266,  277,   40,  261,
  280,  272,  273,  280,  272,  273,  278,   40,  278,  257,
  272,  273,  256,  257,  256,  123,  256,  256,  257,   42,
  123,  261,   62,   41,   42,   43,  261,   45,   62,   47,
  256,  257,  272,  273,   62,  279,  263,  272,  273,  265,
  279,  256,   59,   41,  259,  260,   44,  261,  256,  259,
  256,   42,   43,  279,   45,  261,   47,   41,  272,  273,
   44,   84,  256,  257,  258,   41,  272,  273,  262,  256,
  257,  265,  266,  267,  268,  249,  270,  271,  256,  257,
  274,  104,  276,  277,  278,  256,  257,  257,   80,   81,
   82,  114,   41,   41,  256,  257,  258,  271,  256,   41,
  262,  266,  110,  265,  266,  267,  268,   99,  270,  271,
  264,  264,  274,  136,  276,  277,  278,  140,   41,   42,
   43,   44,   45,    2,   47,    4,    5,  150,   42,   43,
   44,   45,  256,   47,  142,  235,  236,   16,   41,   42,
   43,  149,   45,   41,   47,  266,  256,  257,  258,   28,
  266,   30,  262,  253,  177,  265,   41,  267,  268,  182,
  270,  271,  185,  263,  274,  256,  276,  277,  176,  161,
  162,  266,  180,   40,  264,   59,   59,   41,  263,   58,
  256,  257,  258,  263,  284,   59,  262,  235,  236,  265,
  290,  267,  268,   59,  270,  271,  266,  205,  274,  266,
  276,  277,  210,  291,  266,  253,  266,    0,  296,    0,
   11,   11,  300,  235,  236,  263,  304,  256,  257,  307,
  165,  229,  162,  262,  232,  264,   52,  315,  267,   -1,
  318,  253,   -1,  321,   -1,  274,  284,  276,   -1,  287,
  119,  263,  290,  291,   -1,  293,   -1,   -1,  296,   41,
   42,   43,  300,   45,  302,   47,  304,   -1,   -1,  307,
   -1,   -1,  284,  235,  236,  287,   -1,  315,  290,  291,
  318,  293,   -1,  321,  296,   -1,   -1,   -1,  300,   -1,
  302,  253,  304,   -1,   -1,  307,  256,  257,   -1,  235,
  236,  263,  262,  315,  264,   -1,  318,  267,   -1,  321,
  270,   -1,  256,  257,  274,   -1,  276,  253,  262,   -1,
  264,   -1,  284,  267,   -1,  287,  270,  263,  290,  291,
  274,  293,  276,   -1,  296,   41,   42,   43,  300,   45,
  302,   47,  304,   -1,   -1,  307,   -1,   -1,  284,  235,
  236,  287,   -1,  315,  290,  291,  318,  293,   -1,  321,
  296,   -1,   -1,   -1,  300,   -1,  302,  253,  304,   -1,
   -1,  307,  256,  257,  258,   -1,   -1,  263,  262,  315,
   -1,  265,  318,  267,  268,  321,  270,  271,   -1,   -1,
  274,   -1,  276,  277,  256,  257,  258,   -1,  284,   -1,
  262,   -1,   -1,  265,  290,  267,  268,  293,  270,  271,
   -1,   -1,  274,   -1,  276,  277,  302,  256,  257,  258,
   -1,   -1,   -1,  262,   -1,   -1,  265,   -1,  267,  268,
   -1,  270,  271,   -1,   -1,  274,   -1,  276,  277,  256,
  257,  258,   -1,   -1,   -1,  262,   -1,   -1,  265,   -1,
  267,  268,   -1,  270,  271,   -1,   -1,  274,   -1,  276,
  277,  256,  257,  258,   -1,   -1,   -1,  262,   -1,   -1,
  265,   -1,  267,  268,   -1,  270,  271,   -1,   -1,  274,
   -1,  276,  277,  256,  257,  258,   -1,   -1,   -1,  262,
   -1,   -1,  265,   -1,  267,  268,   -1,  270,  271,   -1,
   -1,  274,   -1,  276,  277,  256,  257,  258,   -1,   -1,
   -1,  262,   -1,   -1,  265,   -1,  267,  268,   -1,   -1,
  271,   -1,   -1,  274,   -1,  276,  277,  256,  257,  258,
   -1,   -1,   -1,  262,   -1,   -1,  265,   -1,  267,  268,
   -1,   -1,  271,   -1,   -1,  274,   -1,  276,  277,  256,
  257,  258,   -1,   -1,   -1,  262,   -1,   -1,  265,   -1,
  267,  268,   -1,   -1,  271,   -1,   -1,  274,   -1,  276,
  277,  256,  257,  258,   -1,   -1,   -1,  262,   -1,   -1,
  265,   -1,  267,  268,   -1,   -1,  271,   -1,   -1,  274,
   -1,  276,  277,  256,  257,  258,   41,   42,   43,  262,
   45,   -1,   47,   -1,  267,  268,   -1,  270,  271,   -1,
   -1,  274,   -1,  276,  277,  256,  257,  258,   -1,   -1,
   -1,  262,   -1,   -1,   -1,   -1,  267,  268,   -1,  270,
  271,   -1,   -1,  274,   -1,  276,  277,  256,  257,  258,
   -1,   -1,   -1,  262,   -1,  256,  257,   -1,  267,  268,
   -1,  262,  271,   -1,  265,  274,  267,  276,  277,  270,
  256,  257,   -1,  274,   -1,  276,  262,   -1,  264,   -1,
   -1,  267,   -1,   -1,  270,  256,  257,   -1,  274,   -1,
  276,  262,   -1,  264,   -1,   -1,  267,   -1,   -1,  270,
  256,  257,   -1,  274,   -1,  276,  262,   -1,  264,   -1,
   -1,  267,   -1,  256,  257,   -1,   -1,   -1,  274,  262,
  276,   -1,  265,   -1,  267,   -1,  256,  257,   -1,   -1,
   -1,  274,  262,  276,  264,   -1,   -1,  267,   -1,  256,
  257,   -1,   -1,   -1,  274,  262,  276,  264,   -1,   -1,
  267,   -1,  256,  257,   -1,  256,  257,  274,  262,  276,
  264,  262,   -1,  267,   -1,   -1,  267,   -1,   -1,  270,
  274,   -1,  276,  274,   -1,  276,  256,  257,   -1,   -1,
   -1,   -1,  262,   -1,   -1,   -1,   -1,  267,   -1,   41,
   42,   43,   44,   45,  274,   47,  276,
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
"expresion : operando",
"expresion : expresion operador operando",
"expresion : expresion operador error",
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
"condicion : listaExpresiones error",
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

//#line 285 "gramatica.y"
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
    //Parser.lex = new AnalizadorLexico("--", matriz, matrizAcciones);
    if (args.length > 1) {
        Parser.lex = new AnalizadorLexico(args[0], matriz, matrizAcciones);

        parser.run();
        for (Error error: erroresLexico){System.out.println(error);}
    } else {
        Parser.lex = new AnalizadorLexico("CP2", matriz, matrizAcciones);
        
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

private void chequearRango(Contexto con, String atributo){
  if (atributo != null){
    System.out.println(con.getTipo());
    if(con.getTipo() == TablaTipoToken.getTipoToken("longint")){
      if(Long.parseLong(atributo) > AnalizadorLexico.MAXLONGINT){
        erroresSintactico.add(new Error(
          AnalizadorLexico.getNumeroLinea(),
          Tipo.ERROR,
          "ERROR SINTACTICO excede rangos."
        ));
      }
    } else if (con.getTipo() == TablaTipoToken.getTipoToken("single")){
      String numero = atributo.replace('s', 'e');
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
      System.out.println("HOLAHOLAHOLAHOLAHOLAHOLA" + atributo);
      if((HexFormat.fromHexDigits(atributo.subSequence(2, atributo.length()).toString())) > AnalizadorLexico.MAXHEXADECIMAL){
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
//#line 790 "Parser.java"
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
{ Contexto contexto = TablaDeSimbolos.getContexto(val_peek(0).sval);
                        System.out.println(val_peek(0).sval);
                        System.out.println(contexto);
                        System.out.println(TablaDeSimbolos.imprimir());
                        chequearRango(contexto, val_peek(0).sval);                             /*SOLO SE CHEQUEA EN POSITIVO YA QUE EL MAXIMO DE NEGATIVOS ES MAYOR AL MAXIMO DE POSITIVOS Y YA LO CHEQUEA EL PARSER*/
                      }
break;
case 44:
//#line 105 "gramatica.y"
{
                            Contexto con = TablaDeSimbolos.getContexto(val_peek(0).sval);
                            String newLex = TablaDeSimbolos.agregarSimbolo("-"+con.getValor(), con.getTipo(), con.getRefs());
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
case 75:
//#line 163 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta un operando"));}
break;
case 76:
//#line 164 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta un operando"));}
break;
case 91:
//#line 182 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO numero de expresiones invalido en el llamado a funcion."));}
break;
case 92:
//#line 183 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta expresion en el llamado a funcion."));}
break;
case 93:
//#line 184 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO numero de expresiones invalido en el llamado a funcion."));}
break;
case 94:
//#line 185 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO numero de expresion esinvalido en el llamado a funcion."));}
break;
case 97:
//#line 190 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta 'end_if;'."));}
break;
case 98:
//#line 191 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta 'end_if;'."));}
break;
case 99:
//#line 192 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta ')' al final de la condicion."));}
break;
case 100:
//#line 193 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta ')' al final de la condicion."));}
break;
case 101:
//#line 194 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta '(' al comienzo de la condicion."));}
break;
case 102:
//#line 195 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta '(' al comienzo de la condicion."));}
break;
case 103:
//#line 196 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO faltan '()' en la condicion."));}
break;
case 104:
//#line 197 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO faltan '()' en la condicion."));}
break;
case 105:
//#line 198 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta bloque de sentencias ejecutables valido."));}
break;
case 106:
//#line 199 "gramatica.y"
{yyval.sval = "false";erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta bloque de sentencias ejecutables valido ."));}
break;
case 107:
//#line 200 "gramatica.y"
{yyval.sval = "false";erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta bloque de sentencias ejecutables valido ."));}
break;
case 108:
//#line 201 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO faltan los dos bloques de sentencias ejecutables validas."));}
break;
case 109:
//#line 205 "gramatica.y"
{yyval.sval = "false";}
break;
case 110:
//#line 206 "gramatica.y"
{ if (val_peek(3).sval.equals("true") && val_peek(1).sval.equals("true")) yyval.sval = "true"; else yyval.sval = "false";}
break;
case 111:
//#line 207 "gramatica.y"
{yyval.sval = "false";erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta 'end_if;'."));}
break;
case 112:
//#line 208 "gramatica.y"
{if (val_peek(3).sval.equals("true") && val_peek(1).sval.equals("true")) yyval.sval = "true"; else yyval.sval = "false";erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta 'end_if;'."));}
break;
case 113:
//#line 209 "gramatica.y"
{if (val_peek(3).sval.equals("true") && val_peek(1).sval.equals("true")) yyval.sval = "true"; else yyval.sval = "false";erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta ')' al final de la condicion."));}
break;
case 114:
//#line 210 "gramatica.y"
{yyval.sval = "false";erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta ')' al final de la condicion."));}
break;
case 115:
//#line 211 "gramatica.y"
{if (val_peek(3).sval.equals("true") && val_peek(1).sval.equals("true")) yyval.sval = "true"; else yyval.sval = "false";erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta '(' al comienzo de la condicion."));}
break;
case 116:
//#line 212 "gramatica.y"
{yyval.sval = "false";erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta '(' al comienzo de la condicion."));}
break;
case 117:
//#line 213 "gramatica.y"
{if (val_peek(3).sval.equals("true") && val_peek(1).sval.equals("true")) yyval.sval = "true"; else yyval.sval = "false";erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO faltan '()' en la condicion."));}
break;
case 118:
//#line 214 "gramatica.y"
{yyval.sval = "false";erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO faltan '()' en la condicion."));}
break;
case 119:
//#line 215 "gramatica.y"
{yyval.sval = "false";erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta bloque de sentencias ejecutables valido."));}
break;
case 120:
//#line 216 "gramatica.y"
{yyval.sval = "false";erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta bloque de sentencias ejecutables valido ."));}
break;
case 121:
//#line 217 "gramatica.y"
{yyval.sval = "false";erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta bloque de sentencias ejecutables valido ."));}
break;
case 122:
//#line 218 "gramatica.y"
{yyval.sval = "false";erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO faltan los dos bloques de sentencias ejecutables validas."));}
break;
case 124:
//#line 222 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta listaExpresiones a la derecha del comparador."));}
break;
case 125:
//#line 223 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta listaExpresiones a la izquierda del comparador."));}
break;
case 126:
//#line 224 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta listaExpresiones a la izquierda del comparador."));}
break;
case 131:
//#line 235 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta expresion."));}
break;
case 132:
//#line 236 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta ultima expresion."));}
break;
case 141:
//#line 244 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta ';'."));}
break;
case 143:
//#line 249 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta ';'."));}
break;
case 145:
//#line 253 "gramatica.y"
{ if (val_peek(2).sval.equals("true") || val_peek(1).sval.equals("true")) yyval.sval = "true"; else yyval.sval = "false";}
break;
case 146:
//#line 254 "gramatica.y"
{yyval.sval = val_peek(1).sval;}
break;
case 147:
//#line 257 "gramatica.y"
{yyval.sval = val_peek(0).sval;}
break;
case 148:
//#line 258 "gramatica.y"
{yyval.sval = val_peek(1).sval;}
break;
case 150:
//#line 263 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta bloque de sentencias ejecutables."));}
break;
case 151:
//#line 264 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta condicion."));}
break;
case 152:
//#line 265 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta condicion y bloque de sentencias ejecutables."));}
break;
case 154:
//#line 269 "gramatica.y"
{erroresSintactico.add(new Error(numeroLineaError, Tipo.ERROR, "ERROR SINTACTICO falta '@' luego de los dospuntos."));}
break;
case 155:
//#line 270 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta 'goto' luego de los dospuntos."));}
break;
case 157:
//#line 274 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta 'etiqueta'."));}
break;
case 158:
//#line 275 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta ':' luego de la etiqueta."));}
break;
case 161:
//#line 280 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera cadena multilinea o expresion en el mensaje de salida."));}
break;
//#line 1348 "Parser.java"
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
