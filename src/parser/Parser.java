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
    0,    0,    1,    1,    2,    2,    2,    2,    3,    3,
    3,    3,    7,    7,    7,    7,    8,    5,    5,    5,
    5,    6,    6,    9,    9,   12,   12,   10,   11,   11,
   13,   13,   13,   13,   15,    4,    4,    4,    4,    4,
   14,   14,   14,   14,   14,   14,   17,   17,   17,   17,
   16,   16,   24,   24,   24,   24,   23,   23,   23,   23,
   23,   23,   25,   25,   18,   18,   22,   22,   26,   29,
   29,   30,   30,   30,   30,   30,   30,   27,   27,   28,
   28,   19,   20,   21,   21,
};
final static short yylen[] = {                            2,
    4,    3,    2,    1,    2,    2,    1,    1,    2,    1,
    2,    2,    7,    6,    6,    6,    8,    1,    1,    1,
    1,    3,    1,    3,    1,    1,    2,    2,    2,    1,
    2,    2,    1,    1,    4,    1,    1,    1,    1,    1,
    1,    1,    1,    1,    1,    1,    3,    6,    6,    6,
    1,    3,    1,    1,    1,    1,    1,    1,    1,    4,
    4,    4,    4,    5,    7,    9,    7,    9,    3,    1,
    3,    1,    1,    1,    1,    1,    1,    1,    3,    1,
    3,    4,    3,    4,    4,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,   20,    0,    2,    0,    0,   18,
    0,    0,   19,   21,    0,    4,    0,    0,    0,   10,
   36,   37,   38,   39,   40,    0,    0,    0,    0,    0,
    0,    0,   78,    0,    0,    1,    3,    5,    6,   12,
   23,    0,    0,   11,    0,   26,    0,   58,    0,   51,
   59,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   83,    0,    0,    0,    0,   27,   53,
   54,   55,   56,    0,    0,    0,    0,    0,   75,   76,
   77,   72,   73,    0,   74,    0,   85,   84,    0,    0,
    0,    0,   79,   82,    0,   22,    0,    0,    0,    0,
    0,   52,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   60,   61,   62,    0,   63,    0,
    0,    0,    0,    0,   25,   16,   15,   14,   28,    0,
   64,   65,    0,   13,    0,    0,    0,   24,    0,    0,
    0,    0,   30,    0,   45,   41,   42,   43,   44,   46,
   66,    0,    0,   31,   17,   29,   32,    0,    0,    0,
   35,    0,    0,   80,    0,    0,   67,    0,   81,    0,
   68,
};
final static short yydgoto[] = {                          2,
   15,   33,   17,   18,   19,   43,   20,   44,  124,  114,
  142,   48,  143,  144,  145,   55,   21,   22,   23,   24,
   25,  150,   50,   74,   51,   56,   34,  165,   57,   86,
};
final static short yysindex[] = {                      -233,
 -239,    0,  289,  -86,    0,  -13,    0,   10, -240,    0,
  312, -202,    0,    0,  335,    0,   -2,    2, -162,    0,
    0,    0,    0,    0,    0,  -38,   34,  -38,   30, -197,
    8,  404,    0, -204,   14,    0,    0,    0,    0,    0,
    0, -165,   57,    0,  -32,    0, -176,    0,  530,    0,
    0,   12,   21,   22,  530,   76,   36,   83,  -12, -149,
 -145,  358,  -38,    0,   89, -126,   94,   43,    0,    0,
    0,    0,    0,  -38, -127, -125, -122, -117,    0,    0,
    0,    0,    0,  -38,    0,  -38,    0,    0,   61,   93,
   99,  100,    0,    0, -149,    0,   63,   72,   85,  -38,
  154,    0,  -38,  -38,  -38,  312,  530,  131,  -35,  -81,
  -78,  -73,  -72,  147,    0,    0,    0,  161,    0,  530,
  530,  530, -250,  -31,    0,    0,    0,    0,    0,  -66,
    0,    0,  312,    0,  -35,  381,  -61,    0,  160,  167,
  150,  220,    0,  152,    0,    0,    0,    0,    0,    0,
    0,  -38,  -38,    0,    0,    0,    0,  171,  461,  -50,
    0,  243,  381,    0, -218,  266,    0,  243,    0,  -51,
    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0, -108,  -85,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   59,    0,  -41,    0,    0,    0,   82,    0,
    0,    0,    0,    0,  -18,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    5,   28,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  105,
  128,  151,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  174,    0,    0,  197,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,
};
final static short yygindex[] = {                         0,
  182,   19,  -89,    0,  -23,    0,    0,    0,    0,    0,
   55,  -95, -110,    0,    0,  -14,  -60,    0,  -43,  -26,
  -15,    0,  148,    0,    0,  -52,  -97,   60,  137,    0,
};
final static int YYTABLESIZE=683;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         57,
   57,   57,   57,   57,   27,   57,   47,   68,  123,   47,
   94,   49,  135,  125,   59,  132,   30,   57,   57,   57,
   57,   16,   70,    1,    3,   70,   28,  133,   88,   72,
   70,  156,   71,   37,   73,  137,   89,   92,   31,  138,
   70,   70,   70,   70,  100,   71,  141,  167,   71,   29,
   16,  164,  141,  101,   35,  156,   38,  164,   67,  168,
   39,  134,   60,   71,   71,   71,   71,   61,   69,  107,
   63,  113,  141,  141,   47,  146,  141,   64,  141,   84,
   37,  146,   52,   53,   54,  118,   69,   47,  120,  121,
  122,   65,  147,   40,   41,   82,   85,   83,  147,  158,
   66,  146,  146,   69,   75,  146,   42,  146,    5,  148,
   90,   91,    5,   76,   77,  148,   78,    9,  147,  147,
  149,   10,  147,   87,  147,   10,  149,   13,   95,   14,
   96,   13,  103,   14,  104,  148,  148,  105,  159,  148,
   47,  148,   97,   98,   99,  106,  149,  149,    7,    7,
  149,  109,  149,    7,  110,  115,    7,    7,    7,    7,
  111,  112,    7,   48,  116,    7,    7,    7,    7,    7,
    7,    8,    8,   26,   84,  126,    8,  117,  127,    8,
    8,    8,    8,  128,  129,    8,   49,  130,    8,    8,
    8,    8,    8,    8,  119,   72,   70,  136,   71,  152,
   73,  131,   72,   70,  151,   71,  153,   73,  154,   50,
  157,  160,  162,   62,  171,   57,   57,  166,   45,   57,
   57,  102,  108,   57,   57,   57,   57,  170,   57,   57,
   57,   57,   57,   57,   57,   57,   57,   57,   70,   70,
    0,   46,   70,   70,   46,    0,   70,   70,   70,   70,
    0,   70,   70,   70,   70,   70,   70,   70,   70,   70,
   70,   71,   71,    0,    0,   71,   71,    0,    0,   71,
   71,   71,   71,    0,   71,   71,   71,   71,   71,   71,
   71,   71,   71,   71,   69,   69,   45,    0,   58,   69,
    0,    0,   69,   69,   69,   69,   79,   69,   69,   45,
    5,   69,   69,   69,   69,   69,   69,   80,   81,   46,
    0,    0,    0,   10,    0,    9,    9,    0,    0,   13,
    9,   14,   46,    9,    9,    9,    9,    0,    9,    9,
    0,    0,    9,    9,    9,    9,    9,    9,   47,   47,
    0,    0,    0,   47,    0,    0,   47,   47,   47,   47,
    0,   47,   47,    0,    0,   47,   47,   47,   47,   47,
   47,   48,   48,    0,    0,    0,   48,    0,    0,   48,
   48,   48,   48,    0,   48,   48,    0,    0,   48,   48,
   48,   48,   48,   48,   49,   49,    0,    0,    0,   49,
    0,    0,   49,   49,   49,   49,    0,   49,   49,    0,
    0,   49,   49,   49,   49,   49,   49,   50,   50,    0,
    0,    0,   50,    0,    0,   50,   50,   50,   50,    0,
   50,   50,    0,    0,   50,   50,   50,   50,   50,   50,
   33,   33,    0,    0,    0,   33,    0,    0,   33,   33,
   33,   33,    0,   33,   33,    0,    0,   33,    0,   33,
   33,   33,   33,   34,   34,    0,    0,    0,   34,    0,
    0,   34,   34,   34,   34,    0,   34,   34,    0,    0,
   34,    0,   34,   34,   34,   34,    4,    5,    0,    0,
    0,  139,    0,    0,  155,    0,    8,    9,    0,  140,
   10,    0,    0,   11,    0,   12,   13,    0,   14,    4,
    5,  161,   72,   70,  139,   71,  163,   73,    0,    8,
    9,    0,  140,   10,    0,    0,   11,    0,   12,   13,
    0,   14,    4,    5,    0,    0,    0,  139,    0,    0,
  169,    0,    8,    9,    0,  140,   10,    0,    0,   11,
    0,   12,   13,    0,   14,    4,    5,    0,    0,    0,
    6,    0,    0,    7,    0,    8,    9,    0,    0,   10,
    0,    0,   11,    0,   12,   13,    0,   14,    4,    5,
    0,   72,   70,    6,   71,   32,   73,    0,    8,    9,
    0,    0,   10,    0,    0,   11,    0,   12,   13,    0,
   14,    4,    5,    0,    0,    0,    6,    0,    0,   36,
    0,    8,    9,    0,    0,   10,    0,    0,   11,    0,
   12,   13,    0,   14,    4,    5,    0,    0,    0,    6,
    0,    0,   93,    0,    8,    9,    0,    0,   10,    0,
    0,   11,    0,   12,   13,    0,   14,    4,    5,    0,
    0,    0,  139,    0,    0,    0,    0,    8,    9,    0,
  140,   10,    0,    0,   11,    0,   12,   13,    0,   14,
    4,    5,    0,    0,    0,    6,    0,    0,    0,    0,
    8,    9,    0,    0,   10,    0,    0,   11,    0,   12,
   13,    0,   14,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         41,
   42,   43,   44,   45,   91,   47,   45,   40,  106,   45,
   63,   26,   44,  109,   29,  266,  257,   59,   60,   61,
   62,    3,   41,  257,  264,   44,   40,  278,   41,   42,
   43,  142,   45,   15,   47,  133,   60,   61,  279,  135,
   59,   60,   61,   62,   68,   41,  136,  266,   44,   40,
   32,  162,  142,   68,  257,  166,   59,  168,   91,  278,
   59,   93,  260,   59,   60,   61,   62,   60,   41,   84,
  275,   95,  162,  163,   45,  136,  166,   64,  168,   44,
   62,  142,   49,   50,   51,  100,   59,   45,  103,  104,
  105,  257,  136,  256,  257,   60,   61,   62,  142,  152,
   44,  162,  163,  280,   93,  166,  269,  168,  258,  136,
  256,  257,  258,   93,   93,  142,   41,   59,  162,  163,
  136,  271,  166,   41,  168,  271,  142,  277,   40,  279,
  257,  277,  260,  279,  260,  162,  163,  260,  153,  166,
   59,  168,   49,   50,   51,  263,  162,  163,  257,  258,
  166,   91,  168,  262,   62,   93,  265,  266,  267,  268,
   62,   62,  271,   59,   93,  274,  275,  276,  277,  278,
  279,  257,  258,  260,   44,  257,  262,   93,  257,  265,
  266,  267,  268,  257,  257,  271,   59,   41,  274,  275,
  276,  277,  278,  279,   41,   42,   43,  264,   45,   40,
   47,   41,   42,   43,  266,   45,   40,   47,   59,   59,
   59,   41,  263,   32,  266,  257,  258,  163,  257,  261,
  262,   74,   86,  265,  266,  267,  268,  168,  270,  271,
  272,  273,  274,  275,  276,  277,  278,  279,  257,  258,
   -1,  280,  261,  262,  280,   -1,  265,  266,  267,  268,
   -1,  270,  271,  272,  273,  274,  275,  276,  277,  278,
  279,  257,  258,   -1,   -1,  261,  262,   -1,   -1,  265,
  266,  267,  268,   -1,  270,  271,  272,  273,  274,  275,
  276,  277,  278,  279,  257,  258,  257,   -1,  259,  262,
   -1,   -1,  265,  266,  267,  268,  261,  270,  271,  257,
  258,  274,  275,  276,  277,  278,  279,  272,  273,  280,
   -1,   -1,   -1,  271,   -1,  257,  258,   -1,   -1,  277,
  262,  279,  280,  265,  266,  267,  268,   -1,  270,  271,
   -1,   -1,  274,  275,  276,  277,  278,  279,  257,  258,
   -1,   -1,   -1,  262,   -1,   -1,  265,  266,  267,  268,
   -1,  270,  271,   -1,   -1,  274,  275,  276,  277,  278,
  279,  257,  258,   -1,   -1,   -1,  262,   -1,   -1,  265,
  266,  267,  268,   -1,  270,  271,   -1,   -1,  274,  275,
  276,  277,  278,  279,  257,  258,   -1,   -1,   -1,  262,
   -1,   -1,  265,  266,  267,  268,   -1,  270,  271,   -1,
   -1,  274,  275,  276,  277,  278,  279,  257,  258,   -1,
   -1,   -1,  262,   -1,   -1,  265,  266,  267,  268,   -1,
  270,  271,   -1,   -1,  274,  275,  276,  277,  278,  279,
  257,  258,   -1,   -1,   -1,  262,   -1,   -1,  265,  266,
  267,  268,   -1,  270,  271,   -1,   -1,  274,   -1,  276,
  277,  278,  279,  257,  258,   -1,   -1,   -1,  262,   -1,
   -1,  265,  266,  267,  268,   -1,  270,  271,   -1,   -1,
  274,   -1,  276,  277,  278,  279,  257,  258,   -1,   -1,
   -1,  262,   -1,   -1,  265,   -1,  267,  268,   -1,  270,
  271,   -1,   -1,  274,   -1,  276,  277,   -1,  279,  257,
  258,   41,   42,   43,  262,   45,  264,   47,   -1,  267,
  268,   -1,  270,  271,   -1,   -1,  274,   -1,  276,  277,
   -1,  279,  257,  258,   -1,   -1,   -1,  262,   -1,   -1,
  265,   -1,  267,  268,   -1,  270,  271,   -1,   -1,  274,
   -1,  276,  277,   -1,  279,  257,  258,   -1,   -1,   -1,
  262,   -1,   -1,  265,   -1,  267,  268,   -1,   -1,  271,
   -1,   -1,  274,   -1,  276,  277,   -1,  279,  257,  258,
   -1,   42,   43,  262,   45,  264,   47,   -1,  267,  268,
   -1,   -1,  271,   -1,   -1,  274,   -1,  276,  277,   -1,
  279,  257,  258,   -1,   -1,   -1,  262,   -1,   -1,  265,
   -1,  267,  268,   -1,   -1,  271,   -1,   -1,  274,   -1,
  276,  277,   -1,  279,  257,  258,   -1,   -1,   -1,  262,
   -1,   -1,  265,   -1,  267,  268,   -1,   -1,  271,   -1,
   -1,  274,   -1,  276,  277,   -1,  279,  257,  258,   -1,
   -1,   -1,  262,   -1,   -1,   -1,   -1,  267,  268,   -1,
  270,  271,   -1,   -1,  274,   -1,  276,  277,   -1,  279,
  257,  258,   -1,   -1,   -1,  262,   -1,   -1,   -1,   -1,
  267,  268,   -1,   -1,  271,   -1,   -1,  274,   -1,  276,
  277,   -1,  279,
};
}
final static short YYFINAL=2;
final static short YYMAXTOKEN=280;
final static String yyname[] = {
"end-of-file",null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,"'('","')'","'*'","'+'","','",
"'-'",null,"'/'",null,"'1'","'2'","'3'",null,null,null,null,null,null,null,
"';'","'<'","'='","'>'",null,"'@'",null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,"'['",null,"']'",null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
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
"cuerpo : cuerpo sentencia",
"cuerpo : sentencia",
"sentencia : sentenciaDeclarativa ';'",
"sentencia : sentenciaEjecutable ';'",
"sentencia : sentenciaDeclarativa",
"sentencia : sentenciaEjecutable",
"sentenciaDeclarativa : tipoDato listaVariable",
"sentenciaDeclarativa : typedefDeclaracion",
"sentenciaDeclarativa : tipoDato funDeclaracion",
"sentenciaDeclarativa : tipoDato error",
"typedefDeclaracion : TYPEDEF IDENTIFICADOR SIMASIGNACION tipoDato '[' listaConstante ']'",
"typedefDeclaracion : TYPEDEF TRIPLE '<' tipoDato '>' IDENTIFICADOR",
"typedefDeclaracion : TYPEDEF TRIPLE '<' IDENTIFICADOR '>' IDENTIFICADOR",
"typedefDeclaracion : TYPEDEF TRIPLE '<' error '>' IDENTIFICADOR",
"funDeclaracion : FUN IDENTIFICADOR '(' parametro ')' BEGIN cuerpoFuncion END",
"tipoDato : SINGLE",
"tipoDato : LONGINT",
"tipoDato : HEXADECIMAL",
"tipoDato : TRIPLE",
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
"asignacion : IDENTIFICADOR '[' '1' ']' SIMASIGNACION expresion",
"asignacion : IDENTIFICADOR '[' '2' ']' SIMASIGNACION expresion",
"asignacion : IDENTIFICADOR '[' '3' ']' SIMASIGNACION expresion",
"expresion : operando",
"expresion : expresion operador operando",
"operador : '+'",
"operador : '-'",
"operador : '*'",
"operador : '/'",
"operando : IDENTIFICADOR",
"operando : constante",
"operando : invocacionFuncion",
"operando : IDENTIFICADOR '[' '1' ']'",
"operando : IDENTIFICADOR '[' '2' ']'",
"operando : IDENTIFICADOR '[' '3' ']'",
"invocacionFuncion : IDENTIFICADOR '(' expresion ')'",
"invocacionFuncion : IDENTIFICADOR '(' tipoDato expresion ')'",
"clausulaSeleccion : IF '(' condicion ')' THEN bloqueSeleccion END_IF",
"clausulaSeleccion : IF '(' condicion ')' THEN bloqueSeleccion ELSE bloqueSeleccion END_IF",
"clausulaSeleccionConRet : IF '(' condicion ')' THEN bloqueSeleccionConRet END_IF",
"clausulaSeleccionConRet : IF '(' condicion ')' THEN bloqueSeleccionConRet ELSE bloqueSeleccionConRet END_IF",
"condicion : listaExpresion comparador listaExpresion",
"listaExpresion : expresion",
"listaExpresion : listaExpresion ',' expresion",
"comparador : '<'",
"comparador : '>'",
"comparador : '='",
"comparador : DISTINTO",
"comparador : MENOR_IGUAL",
"comparador : MAYOR_IGUAL",
"bloqueSeleccion : sentencia",
"bloqueSeleccion : BEGIN cuerpo END",
"bloqueSeleccionConRet : sentenciaConRet",
"bloqueSeleccionConRet : BEGIN cuerpoFuncion END",
"clausulaBucle : REPEAT bloqueSeleccion WHILE condicion",
"goto : GOTO IDENTIFICADOR '@'",
"mensajeSalida : OUTF '(' expresion ')'",
"mensajeSalida : OUTF '(' CADENA_MULTI ')'",
};

//#line 227 "gramatica.y"
//FUNCIONES
private static AnalizadorLexico lex;
public static List<Error> erroresLexico = new ArrayList<Error>();
public static List<Error> erroresSintactico = new ArrayList<Error>();
public static List<String> estructuras = new ArrayList<String>();
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
        Parser.lex = new AnalizadorLexico("returnDentroIF", matriz, matrizAcciones);
        
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
private void yyerror(String string) {
  System.out.println("Error: " + string);
}
//#line 525 "Parser.java"
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
case 7:
//#line 45 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta ';'."));}
break;
case 8:
//#line 46 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta ';'."));}
break;
case 9:
//#line 49 "gramatica.y"
{estructuras.add("Declaracion");}
break;
case 10:
//#line 50 "gramatica.y"
{estructuras.add("Declaracion de typedef");}
break;
case 11:
//#line 51 "gramatica.y"
{estructuras.add("Declaracion de funcion");}
break;
case 12:
//#line 52 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera 'objeto a declarar'."));}
break;
case 16:
//#line 58 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera 'objeto a declarar'."));}
break;
case 26:
//#line 80 "gramatica.y"
{ Lexema lex = TablaDeSimbolos.getByID(val_peek(0).ival);
                        System.out.println(val_peek(0).ival);
                        System.out.println(lex);
                        System.out.println(TablaDeSimbolos.imprimir());
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
break;
case 27:
//#line 119 "gramatica.y"
{
                            Lexema lex = TablaDeSimbolos.getByID(val_peek(0).ival);
                            int newLexRef = TablaDeSimbolos.agregarSimbolo("-"+lex.getAtributo(), lex.getTipo());
                            /*$2.sval = newLex.*/
                          }
break;
case 33:
//#line 141 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta ';'."));}
break;
case 34:
//#line 142 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta ';'."));}
break;
case 35:
//#line 146 "gramatica.y"
{estructuras.add("Retorno");}
break;
case 36:
//#line 150 "gramatica.y"
{estructuras.add("Asignacion");}
break;
case 37:
//#line 151 "gramatica.y"
{estructuras.add("IF");}
break;
case 38:
//#line 152 "gramatica.y"
{estructuras.add("WHILE");}
break;
case 39:
//#line 153 "gramatica.y"
{estructuras.add("GOTO");}
break;
case 40:
//#line 154 "gramatica.y"
{estructuras.add("OUTF");}
break;
case 41:
//#line 156 "gramatica.y"
{estructuras.add("Asignacion");}
break;
case 42:
//#line 157 "gramatica.y"
{estructuras.add("WHILE");}
break;
case 43:
//#line 158 "gramatica.y"
{estructuras.add("GOTO");}
break;
case 44:
//#line 159 "gramatica.y"
{estructuras.add("OUTF");}
break;
case 46:
//#line 161 "gramatica.y"
{estructuras.add("IF");}
break;
//#line 808 "Parser.java"
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
