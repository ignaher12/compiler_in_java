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
    2,    3,    3,    3,    3,    8,    8,    8,    8,    8,
   10,   10,   11,   11,   11,   11,   11,   11,    9,    6,
    6,    6,    7,    7,   12,   12,   15,   15,   13,   14,
   14,   16,   16,   16,   16,   16,   18,    4,    4,    4,
    4,    4,   17,   17,   17,   17,   17,   17,   20,   20,
   19,   19,   27,   27,   27,   27,   26,   26,   26,   26,
   28,   28,   21,   21,   21,   21,   25,   25,   29,   32,
   32,   34,   34,   33,   33,   33,   33,   33,   33,   35,
   35,   35,   30,   30,   30,   36,   36,   31,   31,   22,
   23,   23,   23,    5,    5,    5,   24,   24,
};
final static short yylen[] = {                            2,
    4,    3,    3,    3,    2,    1,    2,    2,    1,    2,
    2,    2,    1,    2,    2,    2,    2,    2,    2,    2,
    6,    6,    5,    5,    5,    5,    5,    5,    8,    1,
    1,    1,    3,    1,    3,    1,    1,    2,    2,    2,
    1,    2,    2,    1,    1,    1,    4,    1,    1,    1,
    1,    1,    1,    1,    1,    1,    1,    1,    3,    4,
    1,    3,    1,    1,    1,    1,    1,    1,    1,    2,
    4,    5,    7,    9,    7,    9,    7,    9,    3,    3,
    1,    3,    1,    1,    1,    1,    1,    1,    1,    3,
    2,    2,    2,    2,    3,    3,    2,    2,    3,    6,
    3,    3,    2,    2,    2,    2,    4,    4,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,    0,    0,   32,    0,    0,    0,
   30,    0,    0,   31,    0,    6,    0,    0,    0,    0,
   13,   48,   49,   50,   51,   52,    2,    0,    0,    0,
    0,  105,   18,   19,  106,    0,    0,  104,    0,    0,
    0,   16,   17,    0,    0,    0,    0,    0,    0,    0,
    4,    5,   10,    7,   11,    8,  103,   15,   34,    0,
    0,   14,    1,    0,    0,    0,    0,    0,   37,    0,
   68,    0,   61,   69,    0,    0,    0,    0,    0,    0,
    0,    0,   94,   93,    0,  102,  101,    0,    0,    0,
    0,    0,    0,    0,    0,   70,    0,   38,   63,   64,
   65,   66,    0,    0,    0,    0,   87,   88,   89,   84,
   85,   86,    0,  108,  107,   92,   91,   95,    0,    0,
    0,   33,    0,    0,    0,    0,    0,    0,    0,   62,
   80,    0,    0,   79,   90,    0,    0,    0,    0,   36,
    0,   28,   25,   27,   24,   26,   23,    0,   71,    0,
    0,  100,   39,    0,   22,    0,   21,   72,   75,   73,
    0,    0,   35,    0,    0,    0,    0,    0,    0,   41,
    0,   57,   53,   54,   55,   56,   58,   76,   74,    0,
    0,   42,   29,   40,   43,    0,    0,    0,   47,    0,
    0,    0,    0,    0,    0,   98,   77,    0,   97,   99,
    0,    0,   96,   78,
};
final static short yydgoto[] = {                          3,
   15,   16,   17,   18,   47,   20,   61,   21,   62,   33,
   34,  139,  138,  169,   71,  170,  171,  172,   76,   22,
   23,   24,   25,   26,  177,   73,  103,   74,   77,   48,
  193,   78,  113,  105,   82,  195,
};
final static short yysindex[] = {                      -218,
 -227,  221,    0,  133,  -55,  -56,    0,   10,   26, -184,
    0,  259, -140,    0,  155,    0,  -41,  -38,   11, -113,
    0,    0,    0,    0,    0,    0,    0,  177, -168, -157,
   48,    0,    0,    0,    0, -115,  -29,    0,  -40,  -28,
 -168,    0,    0,   92,  300,  -37,   11, -122,  -45,  -60,
    0,    0,    0,    0,    0,    0,    0,    0,    0, -110,
  113,    0,    0, -162, -162,  -65,  -29,  -25,    0, -121,
    0,  465,    0,    0,  -29,  465,  121,   -8,  128,  417,
  -31,  272,    0,    0,  137,    0,    0,  140,  -76,   60,
   63,  125,  126,  145,  465,    0,  -10,    0,    0,    0,
    0,    0,  -29,  465,   33,  -68,    0,    0,    0,    0,
    0,    0,  -40,    0,    0,    0,    0,    0,  149,  -40,
 -162,    0,  -34,  -34, -134,  -89,  -85,  -29,  523,    0,
    0,  -29,  259,    0,    0,  158,  -48,  169,  -24,    0,
  -15,    0,    0,    0,    0,    0,    0,  530,    0,  465,
 -195,    0,    0,  -51,    0,  -34,    0,    0,    0,    0,
  259,  199,    0, -167,  174,  176,  161,   11, -138,    0,
  185,    0,    0,    0,    0,    0,    0,    0,    0,  -40,
  -29,    0,    0,    0,    0,  204,  537,  -14,    0,  229,
  285,  191, -230,  197,  244,    0,    0,  229,    0,    0,
  201,   -4,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    1,    0,
    0,    0,    0,    0,    0,    0,    0,  271,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   23,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   45,    0,    0,    0,    0,    0,    0,  -35,    0,    0,
    0,   67,    0,    0,    0,  -18,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   89,    0,    0,    0,    0,    0,
    0,    0,    0,   87,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   93,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0, -116,  -92,    0,    0,
  111,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
  269,   17,  -69,    2,   36,   -9,    0,    0,    0,  264,
  266,  159,    0,    0,  -43,  115,   -1,    0,   -7, -128,
    0, -104,  -93,  -84,    0,  179,    0,    0, -101, -102,
   88,  182,    0,    0,    0,    0,
};
final static int YYTABLESIZE=584;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         75,
    9,   38,   32,   87,   70,   67,   67,   67,   67,   67,
   70,   67,   38,   46,   97,   70,   70,   54,  136,  156,
   56,   84,   81,   67,   67,   67,   67,  117,  156,   72,
  151,   52,   80,  173,   70,  197,    4,   19,    1,   19,
  173,   81,   81,   81,   52,    2,   81,  198,   50,   39,
   19,  110,  112,  111,   90,   91,   94,  174,  164,   95,
  159,  173,  173,   19,  174,   40,  173,  104,  175,  173,
  160,   41,   30,  131,   57,  175,  132,  176,  186,  140,
  140,   20,  161,  119,  176,  174,  174,  128,  178,  129,
  174,   64,  167,  174,   31,    7,  175,  175,  179,  167,
  155,  175,   65,   12,  175,  176,  176,   66,   11,  157,
  176,  137,  163,  176,   14,   44,   49,    5,    6,    7,
  148,  142,  143,  165,  150,   59,  183,   83,    9,   10,
   83,  166,   11,   82,   46,   12,   82,   13,   14,   45,
   45,   45,   58,   59,   67,   45,   88,   60,   45,   32,
   45,   45,   85,   45,   45,   60,   89,   45,   98,   45,
   45,  106,   46,   44,   44,   44,  144,  145,  114,   44,
  146,  147,   44,  187,   44,   44,  120,   44,   44,  121,
  122,   44,  123,   44,   44,  124,  125,  126,  192,  194,
   92,   93,    7,  201,  133,   86,  192,  168,  152,   35,
   29,   30,   36,   37,  168,   11,  127,  135,  153,  154,
   35,   14,  162,  180,   53,  181,   68,   55,   83,  182,
   67,   67,   67,   31,  116,   67,   67,   68,   68,   67,
   79,   67,   67,   96,   67,   67,   67,   67,   67,   69,
   67,   67,   81,  185,  188,   69,   68,    7,  190,  196,
   69,   69,  107,   81,   81,  199,    9,    9,    9,  203,
   11,  204,    9,  108,  109,    9,   14,    9,    9,   69,
    3,    9,   28,   42,    9,   43,    9,    9,   20,   20,
   20,  130,  141,  184,   20,  202,    0,   20,    0,   20,
   20,    0,   20,   20,  134,    0,   20,    0,   20,   20,
   12,   12,   12,    0,    0,    0,   12,    0,    0,   12,
    0,   12,   12,    0,   12,   12,    0,    0,   12,    0,
   12,   12,   59,   59,   59,    0,    0,    0,   59,    0,
    0,   59,    0,   59,   59,    0,   59,   59,    0,    0,
   59,    0,   59,   59,   60,   60,   60,    0,    0,    0,
   60,    0,    0,   60,    0,   60,   60,    0,   60,   60,
    0,    0,   60,    0,   60,   60,   46,   46,   46,    0,
    0,    0,   46,    0,    0,   46,    0,   46,   46,    0,
   46,   46,    0,    0,   46,    0,   46,   46,    5,    6,
    7,    0,    0,    0,    8,    0,    0,   27,    0,    9,
   10,    0,    0,   11,    0,    0,   12,    0,   13,   14,
    5,    6,    7,    0,    0,    0,    8,    0,    0,   51,
    0,    9,   10,    0,    0,   11,    0,    0,   12,    0,
   13,   14,    5,    6,    7,    0,    0,    0,    8,    0,
    0,   63,    0,    9,   10,    0,    0,   11,    0,    0,
   12,    0,   13,   14,    5,    6,    7,  115,  101,   99,
  165,  100,    0,  102,    0,    9,   10,    0,  166,   11,
    0,    0,   12,    0,   13,   14,    5,    6,    7,    0,
    0,    0,    8,    0,   44,    6,    0,    9,   10,    0,
  165,   11,  191,    0,   12,    9,   13,   14,  166,   44,
    6,    0,   12,    0,   13,  165,  101,   99,  200,  100,
    9,  102,    0,  166,   44,    6,    0,   12,    0,   13,
    8,    0,   45,    0,    0,    9,    0,   44,    6,    0,
    0,    0,   12,    8,   13,    0,  118,    0,    9,    0,
   44,    6,    0,    0,    0,   12,  165,   13,    0,    0,
    0,    9,    0,    0,  166,   44,    6,    0,   12,    0,
   13,    8,    0,  149,  101,   99,    9,  100,    0,  102,
  158,  101,   99,   12,  100,   13,  102,  189,  101,   99,
    0,  100,    0,  102,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         40,
    0,   58,   58,   64,   45,   41,   42,   43,   44,   45,
   45,   47,   58,   12,   40,   45,   45,   59,  120,   44,
   59,   59,   41,   59,   60,   61,   62,   59,   44,   37,
  133,   15,   40,  162,   45,  266,  264,    2,  257,    4,
  169,   60,   61,   62,   28,  264,   45,  278,   13,   40,
   15,   60,   61,   62,   64,   65,   66,  162,  161,   67,
  256,  190,  191,   28,  169,   40,  195,   75,  162,  198,
  266,  256,  257,   41,   64,  169,   44,  162,  180,  123,
  124,   59,  278,   82,  169,  190,  191,   97,  256,   97,
  195,  260,  162,  198,  279,  258,  190,  191,  266,  169,
  125,  195,  260,   59,  198,  190,  191,   60,  271,  125,
  195,  121,  156,  198,  277,  256,  257,  256,  257,  258,
  128,  256,  257,  262,  132,   59,  265,   41,  267,  268,
   44,  270,  271,   41,  133,  274,   44,  276,  277,  256,
  257,  258,  256,  257,  260,  262,  257,   59,  265,   58,
  267,  268,  275,  270,  271,  269,   44,  274,  280,  276,
  277,   41,  161,  256,  257,  258,  256,  257,   41,  262,
  256,  257,  265,  181,  267,  268,   40,  270,  271,   40,
  257,  274,  123,  276,  277,  123,   62,   62,  190,  191,
  256,  257,  258,  195,  263,  256,  198,  162,   41,  256,
  256,  257,  259,  260,  169,  271,   62,   59,  257,   41,
  256,  277,  264,   40,  256,   40,  257,  256,  256,   59,
  256,  257,  258,  279,  256,  261,  262,  257,  257,  265,
  259,  267,  268,  259,  270,  271,  272,  273,  274,  280,
  276,  277,  261,   59,   41,  280,  257,  258,  263,   59,
  280,  280,  261,  272,  273,   59,  256,  257,  258,   59,
  271,  266,  262,  272,  273,  265,  277,  267,  268,  280,
    0,  271,    4,   10,  274,   10,  276,  277,  256,  257,
  258,  103,  124,  169,  262,  198,   -1,  265,   -1,  267,
  268,   -1,  270,  271,  113,   -1,  274,   -1,  276,  277,
  256,  257,  258,   -1,   -1,   -1,  262,   -1,   -1,  265,
   -1,  267,  268,   -1,  270,  271,   -1,   -1,  274,   -1,
  276,  277,  256,  257,  258,   -1,   -1,   -1,  262,   -1,
   -1,  265,   -1,  267,  268,   -1,  270,  271,   -1,   -1,
  274,   -1,  276,  277,  256,  257,  258,   -1,   -1,   -1,
  262,   -1,   -1,  265,   -1,  267,  268,   -1,  270,  271,
   -1,   -1,  274,   -1,  276,  277,  256,  257,  258,   -1,
   -1,   -1,  262,   -1,   -1,  265,   -1,  267,  268,   -1,
  270,  271,   -1,   -1,  274,   -1,  276,  277,  256,  257,
  258,   -1,   -1,   -1,  262,   -1,   -1,  265,   -1,  267,
  268,   -1,   -1,  271,   -1,   -1,  274,   -1,  276,  277,
  256,  257,  258,   -1,   -1,   -1,  262,   -1,   -1,  265,
   -1,  267,  268,   -1,   -1,  271,   -1,   -1,  274,   -1,
  276,  277,  256,  257,  258,   -1,   -1,   -1,  262,   -1,
   -1,  265,   -1,  267,  268,   -1,   -1,  271,   -1,   -1,
  274,   -1,  276,  277,  256,  257,  258,   41,   42,   43,
  262,   45,   -1,   47,   -1,  267,  268,   -1,  270,  271,
   -1,   -1,  274,   -1,  276,  277,  256,  257,  258,   -1,
   -1,   -1,  262,   -1,  256,  257,   -1,  267,  268,   -1,
  262,  271,  264,   -1,  274,  267,  276,  277,  270,  256,
  257,   -1,  274,   -1,  276,  262,   42,   43,  265,   45,
  267,   47,   -1,  270,  256,  257,   -1,  274,   -1,  276,
  262,   -1,  264,   -1,   -1,  267,   -1,  256,  257,   -1,
   -1,   -1,  274,  262,  276,   -1,  265,   -1,  267,   -1,
  256,  257,   -1,   -1,   -1,  274,  262,  276,   -1,   -1,
   -1,  267,   -1,   -1,  270,  256,  257,   -1,  274,   -1,
  276,  262,   -1,   41,   42,   43,  267,   45,   -1,   47,
   41,   42,   43,  274,   45,  276,   47,   41,   42,   43,
   -1,   45,   -1,   47,
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
"clausulaSeleccion : IF '(' condicion ')' THEN bloqueSentenciaEjecutable END_IF",
"clausulaSeleccion : IF '(' condicion ')' THEN bloqueSentenciaEjecutable ELSE bloqueSentenciaEjecutable END_IF",
"clausulaSeleccion : IF '(' condicion ')' THEN bloqueSentenciaEjecutable error",
"clausulaSeleccion : IF '(' condicion ')' THEN bloqueSentenciaEjecutable ELSE bloqueSentenciaEjecutable error",
"clausulaSeleccionConRet : IF '(' condicion ')' THEN bloqueSentenciaEjecutableConRet END_IF",
"clausulaSeleccionConRet : IF '(' condicion ')' THEN bloqueSentenciaEjecutableConRet ELSE bloqueSentenciaEjecutableConRet END_IF",
"condicion : listaExpresiones comparador listaExpresiones",
"listaExpresiones : '(' listaExpresion ')'",
"listaExpresiones : expresion",
"listaExpresion : listaExpresion ',' expresion",
"listaExpresion : expresion",
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
"goto : GOTO etiqueta '@'",
"goto : GOTO etiqueta error",
"goto : etiqueta '@'",
"etiqueta : IDENTIFICADOR ':'",
"etiqueta : error ':'",
"etiqueta : IDENTIFICADOR error",
"mensajeSalida : OUTF '(' expresion ')'",
"mensajeSalida : OUTF '(' CADENA_MULTI ')'",
};

//#line 238 "gramatica.y"
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
        Parser.lex = new AnalizadorLexico("goto", matriz, matrizAcciones);
        
        parser.run();
        System.out.println("v---------------------------v");
        for (Error error: erroresSintactico){System.out.println(error);}
        for (Error error: erroresLexico){System.out.println(error);}
        for (String estructura: estructuras){System.out.println(estructura);}
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
//#line 583 "Parser.java"
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
//#line 52 "gramatica.y"
{estructuras.add("Declaracion");}
break;
case 13:
//#line 53 "gramatica.y"
{estructuras.add("Declaracion de typedef");}
break;
case 14:
//#line 54 "gramatica.y"
{estructuras.add("Declaracion de funcion");}
break;
case 15:
//#line 55 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera 'objeto a declarar'."));}
break;
case 18:
//#line 60 "gramatica.y"
{ erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera 'TYPEDEF antes de la declaracion del subtipo'.")); }
break;
case 19:
//#line 61 "gramatica.y"
{ erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera 'TYPEDEF antes de la declaracion del triple'.")); }
break;
case 20:
//#line 62 "gramatica.y"
{ erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera 'declaracion de subtipo o triple'.")); }
break;
case 22:
//#line 66 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera 'nombre del tipo de dato nuevo'."));}
break;
case 25:
//#line 70 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera 'tipo de objeto a declarar'."));}
break;
case 26:
//#line 71 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera 'nombre de objeto a declarar'."));}
break;
case 27:
//#line 72 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera 'nombre de objeto a declarar'."));}
break;
case 28:
//#line 73 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera 'nombre y tipo de objeto a declarar'."));}
break;
case 37:
//#line 94 "gramatica.y"
{ Lexema lex = TablaDeSimbolos.getByID(val_peek(0).ival);
                        System.out.println(val_peek(0).ival);
                        System.out.println(lex);
                        System.out.println(TablaDeSimbolos.imprimir());
                        chequearRango(lex);                             /*SOLO SE CHEQUEA EN POSITIVO YA QUE EL MAXIMO DE NEGATIVOS ES MAYOR AL MAXIMO DE POSITIVOS Y YA LO CHEQUEA EL PARSER*/
                      }
break;
case 38:
//#line 100 "gramatica.y"
{
                            Lexema lex = TablaDeSimbolos.getByID(val_peek(0).ival);
                            int newLexRef = TablaDeSimbolos.agregarSimbolo("-"+lex.getAtributo(), lex.getTipo());
                            /*$2.sval = newLex.*/
                          }
break;
case 45:
//#line 123 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta ';'."));}
break;
case 46:
//#line 124 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta ';'."));}
break;
case 47:
//#line 128 "gramatica.y"
{estructuras.add("Retorno");}
break;
case 48:
//#line 132 "gramatica.y"
{estructuras.add("Asignacion");}
break;
case 49:
//#line 133 "gramatica.y"
{estructuras.add("IF");}
break;
case 50:
//#line 134 "gramatica.y"
{estructuras.add("WHILE");}
break;
case 51:
//#line 135 "gramatica.y"
{estructuras.add("GOTO");}
break;
case 52:
//#line 136 "gramatica.y"
{estructuras.add("OUTF");}
break;
case 53:
//#line 138 "gramatica.y"
{estructuras.add("Asignacion");}
break;
case 54:
//#line 139 "gramatica.y"
{estructuras.add("WHILE");}
break;
case 55:
//#line 140 "gramatica.y"
{estructuras.add("GOTO");}
break;
case 56:
//#line 141 "gramatica.y"
{estructuras.add("OUTF");}
break;
case 58:
//#line 143 "gramatica.y"
{estructuras.add("IF");}
break;
case 75:
//#line 175 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta 'end_if;'."));}
break;
case 76:
//#line 176 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta 'end_if;'."));}
break;
case 92:
//#line 201 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta ';'."));}
break;
case 94:
//#line 206 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta ';'."));}
break;
case 102:
//#line 223 "gramatica.y"
{erroresSintactico.add(new Error(numeroLineaError, Tipo.ERROR, "ERROR SINTACTICO falta '@' luego de los dospuntos."));}
break;
case 103:
//#line 224 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta 'goto' luego de los dospuntos."));}
break;
case 105:
//#line 228 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta 'etiqueta'."));}
break;
case 106:
//#line 229 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta ':' luego de la etiqueta."));}
break;
//#line 901 "Parser.java"
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
