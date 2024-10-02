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
    3,    3,    3,    3,    7,    7,    7,    7,    7,    9,
    9,   10,   10,   10,   10,   10,   10,    8,    5,    5,
    5,    6,    6,   11,   11,   14,   14,   12,   13,   13,
   15,   15,   15,   15,   17,    4,    4,    4,    4,    4,
   16,   16,   16,   16,   16,   16,   19,   19,   18,   18,
   26,   26,   26,   26,   25,   25,   25,   25,   27,   27,
   20,   20,   20,   20,   24,   24,   28,   31,   31,   33,
   33,   32,   32,   32,   32,   32,   32,   34,   34,   34,
   29,   29,   29,   35,   35,   30,   30,   21,   22,   23,
   23,
};
final static short yylen[] = {                            2,
    4,    3,    3,    3,    2,    1,    2,    2,    2,    2,
    2,    1,    2,    2,    2,    2,    2,    2,    2,    6,
    6,    5,    5,    5,    5,    5,    5,    8,    1,    1,
    1,    3,    1,    3,    1,    1,    2,    2,    2,    1,
    2,    2,    1,    1,    4,    1,    1,    1,    1,    1,
    1,    1,    1,    1,    1,    1,    3,    4,    1,    3,
    1,    1,    1,    1,    1,    1,    1,    2,    4,    5,
    7,    9,    7,    9,    7,    9,    3,    3,    1,    3,
    1,    1,    1,    1,    1,    1,    1,    3,    2,    2,
    2,    2,    3,    3,    2,    2,    3,    6,    4,    4,
    4,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,    0,    0,   31,    0,    0,    0,
   29,    0,    0,   30,    0,    6,    0,    0,    0,   12,
   46,   47,   48,   49,   50,    2,    0,    0,    0,    0,
   17,   18,    0,    0,    0,    0,    0,   15,   16,    0,
    0,    0,    0,    4,    5,    9,    7,   10,    8,   14,
   33,    0,    0,   13,    1,    0,    0,    0,    0,    0,
   36,    0,   66,    0,   59,   67,    0,    0,    0,    0,
    0,    0,    0,    0,   92,   91,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   68,    0,   37,   61,
   62,   63,   64,    0,    0,    0,    0,   85,   86,   87,
   82,   83,   84,    0,  101,  100,   90,   89,   93,    0,
    0,   99,    0,   32,    0,    0,    0,    0,    0,    0,
    0,   60,   78,    0,    0,   77,   88,    0,    0,    0,
    0,   35,    0,   27,   24,   26,   23,   25,   22,    0,
   69,    0,    0,   98,   38,    0,   21,    0,   20,   70,
   73,   71,    0,    0,   34,    0,    0,    0,    0,    0,
   40,    0,   55,   51,   52,   53,   54,   56,   74,   72,
    0,    0,   41,   28,   39,   42,    0,    0,    0,   45,
    0,    0,    0,    0,    0,    0,   96,   75,    0,   95,
   97,    0,    0,   94,   76,
};
final static short yydgoto[] = {                          3,
   15,   16,   17,   18,   19,   53,   20,   54,   31,   32,
  131,  130,  160,   63,  161,  162,  163,   68,   21,   22,
   23,   24,   25,  168,   65,   94,   66,   69,   42,  184,
   70,  104,   96,   74,  186,
};
final static short yysindex[] = {                      -166,
 -206,  151,    0,  -72, -213, -223,    0,   66,   71, -186,
    0,  195, -176,    0,   85,    0,  -45,  -44, -170,    0,
    0,    0,    0,    0,    0,    0,  107, -128, -126,   59,
    0,    0, -118,  -32,  -40,  -28, -128,    0,    0,  221,
  -43, -125,   89,    0,    0,    0,    0,    0,    0,    0,
    0, -104,  110,    0,    0, -183, -183,  158,  -32,  -39,
    0, -124,    0,  395,    0,    0,  -32,  395,  119,  -58,
  123,  449,  -38,  199,    0,    0,  125,  104,  131,  -88,
   51,   54,  116,  118,  121,  395,    0,  -14,    0,    0,
    0,    0,    0,  -32,  395,   69,  -75,    0,    0,    0,
    0,    0,    0,  -40,    0,    0,    0,    0,    0,  128,
  -40,    0, -183,    0,  -34,  -34, -160, -116, -112,  -32,
  457,    0,    0,  -32,  195,    0,    0,  148,  -66,  153,
  -24,    0,  -22,    0,    0,    0,    0,    0,    0,  464,
    0,  395, -204,    0,    0,  -67,    0,  -34,    0,    0,
    0,    0,  195,  129,    0, -211,  152,  160,  139, -141,
    0,  142,    0,    0,    0,    0,    0,    0,    0,    0,
  -40,  -32,    0,    0,    0,    0,  165,  471,  -56,    0,
  169,  210,  149, -224,  150,  184,    0,    0,  169,    0,
    0,  157,  -47,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  224,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   -3,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   19,    0,    0,    0,    0,    0,    0,  -35,
    0,    0,    0,   41,    0,    0,    0,  -12,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   63,    0,    0,    0,    0,
    0,    0,    0,    0,   79,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   84,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0, -119,    0,
    0,  -95,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
  206,   80,  -52,    6,  -23,    0,    0,    0,  218,  235,
  134,    0,    0,  -76,   74,  212,    0,   -6, -122,    0,
 -113,  -97,  -77,    0,  162,    0,    0,  -92, -102,   58,
  147,    0,    0,    0,    0,
};
final static int YYTABLESIZE=518;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         67,
   88,  101,  103,  102,   62,   65,   65,   65,   65,   65,
   62,   65,   62,   47,   49,   76,   62,   41,  128,  148,
  108,  148,  143,   65,   65,   65,   65,   64,   79,   72,
   62,  164,   81,   82,   85,   33,   34,  164,  132,  132,
  165,  188,   28,   29,  169,   73,  165,   79,   79,   79,
  156,  151,   86,  189,  170,   19,  166,    4,  164,  164,
   95,  152,  166,  164,  120,   30,  164,  165,  165,   37,
   29,  155,  165,  153,    7,  165,  167,   11,  177,  110,
   43,  121,  167,  166,  166,   50,   51,   11,  166,  129,
    1,  166,   30,   14,   45,  134,  135,    2,   52,   57,
  147,  159,  149,  167,  167,   35,   45,  159,  167,  123,
   36,  167,  124,  140,    5,    6,    7,  142,   58,   81,
  157,   58,   81,  174,   80,    9,   10,   80,  158,   11,
   41,   56,   12,   57,   13,   14,   43,   43,   43,  136,
  137,   59,   43,  138,  139,   43,   78,   43,   43,   77,
   43,   43,   79,   80,   43,   89,   43,   43,   41,   97,
   44,   44,   44,  105,  111,  178,   44,  112,  114,   44,
  113,   44,   44,  115,   44,   44,  116,  117,   44,  118,
   44,   44,  119,    5,    6,    7,  127,  125,  144,    8,
  145,  171,   26,  146,    9,   10,  154,  173,   11,  172,
  176,   12,   98,   13,   14,  179,  181,  187,  190,   27,
   46,   48,   75,   99,  100,  194,   60,  107,  195,   87,
   65,   65,   65,    3,   60,   65,   65,   38,   60,   65,
   71,   65,   65,  175,   65,   65,   65,   65,   65,   61,
   65,   65,   60,    7,   39,   61,  193,   61,   79,  133,
  126,   61,   19,   19,   19,  122,   11,    0,   19,   79,
   79,   19,   14,   19,   19,   61,   19,   19,    0,    0,
   19,    0,   19,   19,   11,   11,   11,    0,    0,    0,
   11,    0,    0,   11,    0,   11,   11,    0,   11,   11,
    0,    0,   11,    0,   11,   11,   57,   57,   57,    0,
    0,    0,   57,    0,    0,   57,    0,   57,   57,    0,
   57,   57,    0,    0,   57,    0,   57,   57,   58,   58,
   58,    0,    0,    0,   58,    0,    0,   58,    0,   58,
   58,    0,   58,   58,    0,    0,   58,    0,   58,   58,
    5,    6,    7,    0,    0,    0,    8,    0,    0,   44,
    0,    9,   10,    0,    0,   11,    0,    0,   12,    0,
   13,   14,    5,    6,    7,    0,    0,    0,    8,    0,
    0,   55,    0,    9,   10,    0,    0,   11,    0,    0,
   12,    0,   13,   14,    5,    6,    7,    0,    0,    0,
  157,    0,  183,  185,    0,    9,   10,  192,  158,   11,
  183,    0,   12,    0,   13,   14,    5,    6,    7,    0,
    0,    0,    8,   83,   84,    7,    0,    9,   10,    0,
    0,   11,    0,    0,   12,    6,   13,   14,   11,    0,
  157,    0,  182,    0,   14,    9,   92,   90,  158,   91,
    6,   93,   12,    0,   13,  157,    0,    0,  191,    0,
    9,    6,    0,  158,    0,    6,    8,   12,   40,   13,
    8,    9,    0,  109,    0,    9,    6,    0,   12,    0,
   13,  157,   12,    0,   13,    0,    9,    6,    0,  158,
    0,    0,    8,   12,    0,   13,    0,    9,    0,  106,
   92,   90,    0,   91,   12,   93,   13,  141,   92,   90,
    0,   91,    0,   93,  150,   92,   90,    0,   91,    0,
   93,  180,   92,   90,    0,   91,    0,   93,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         40,
   40,   60,   61,   62,   45,   41,   42,   43,   44,   45,
   45,   47,   45,   59,   59,   59,   45,   12,  111,   44,
   59,   44,  125,   59,   60,   61,   62,   34,   41,   36,
   45,  154,   56,   57,   58,  259,  260,  160,  115,  116,
  154,  266,  256,  257,  256,   40,  160,   60,   61,   62,
  153,  256,   59,  278,  266,   59,  154,  264,  181,  182,
   67,  266,  160,  186,   88,  279,  189,  181,  182,  256,
  257,  148,  186,  278,  258,  189,  154,   59,  171,   74,
  257,   88,  160,  181,  182,  256,  257,  271,  186,  113,
  257,  189,  279,  277,   15,  256,  257,  264,  269,   59,
  125,  154,  125,  181,  182,   40,   27,  160,  186,   41,
   40,  189,   44,  120,  256,  257,  258,  124,   60,   41,
  262,   59,   44,  265,   41,  267,  268,   44,  270,  271,
  125,  260,  274,  260,  276,  277,  256,  257,  258,  256,
  257,  260,  262,  256,  257,  265,   58,  267,  268,  275,
  270,  271,  257,   44,  274,  280,  276,  277,  153,   41,
  256,  257,  258,   41,   40,  172,  262,   64,  257,  265,
   40,  267,  268,  123,  270,  271,  123,   62,  274,   62,
  276,  277,   62,  256,  257,  258,   59,  263,   41,  262,
  257,   40,  265,   41,  267,  268,  264,   59,  271,   40,
   59,  274,  261,  276,  277,   41,  263,   59,   59,    4,
  256,  256,  256,  272,  273,   59,  257,  256,  266,  259,
  256,  257,  258,    0,  257,  261,  262,   10,  257,  265,
  259,  267,  268,  160,  270,  271,  272,  273,  274,  280,
  276,  277,  257,  258,   10,  280,  189,  280,  261,  116,
  104,  280,  256,  257,  258,   94,  271,   -1,  262,  272,
  273,  265,  277,  267,  268,  280,  270,  271,   -1,   -1,
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
  262,   -1,  181,  182,   -1,  267,  268,  186,  270,  271,
  189,   -1,  274,   -1,  276,  277,  256,  257,  258,   -1,
   -1,   -1,  262,  256,  257,  258,   -1,  267,  268,   -1,
   -1,  271,   -1,   -1,  274,  257,  276,  277,  271,   -1,
  262,   -1,  264,   -1,  277,  267,   42,   43,  270,   45,
  257,   47,  274,   -1,  276,  262,   -1,   -1,  265,   -1,
  267,  257,   -1,  270,   -1,  257,  262,  274,  264,  276,
  262,  267,   -1,  265,   -1,  267,  257,   -1,  274,   -1,
  276,  262,  274,   -1,  276,   -1,  267,  257,   -1,  270,
   -1,   -1,  262,  274,   -1,  276,   -1,  267,   -1,   41,
   42,   43,   -1,   45,  274,   47,  276,   41,   42,   43,
   -1,   45,   -1,   47,   41,   42,   43,   -1,   45,   -1,
   47,   41,   42,   43,   -1,   45,   -1,   47,
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
"goto : GOTO IDENTIFICADOR ':' '@'",
"mensajeSalida : OUTF '(' expresion ')'",
"mensajeSalida : OUTF '(' CADENA_MULTI ')'",
};

//#line 229 "gramatica.y"
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
    Parser.lex = new AnalizadorLexico("--", matriz, matrizAcciones);
    if (args.length > 1) {
        Parser.lex = new AnalizadorLexico(args[0], matriz, matrizAcciones);

        parser.run();
        for (Error error: erroresLexico){System.out.println(error);}
    } else {
        Parser.lex = new AnalizadorLexico("---", matriz, matrizAcciones);
        
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
  System.out.println("Error: " + string);
}
//#line 556 "Parser.java"
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
case 9:
//#line 47 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta ';'."));}
break;
case 10:
//#line 48 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta ';'."));}
break;
case 11:
//#line 51 "gramatica.y"
{estructuras.add("Declaracion");}
break;
case 12:
//#line 52 "gramatica.y"
{estructuras.add("Declaracion de typedef");}
break;
case 13:
//#line 53 "gramatica.y"
{estructuras.add("Declaracion de funcion");}
break;
case 14:
//#line 54 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera 'objeto a declarar'."));}
break;
case 17:
//#line 59 "gramatica.y"
{ erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera 'TYPEDEF antes de la declaracion del subtipo'.")); }
break;
case 18:
//#line 60 "gramatica.y"
{ erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera 'TYPEDEF antes de la declaracion del triple'.")); }
break;
case 19:
//#line 61 "gramatica.y"
{ erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera 'declaracion de subtipo o triple'.")); }
break;
case 21:
//#line 65 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera 'nombre del tipo de dato nuevo'."));}
break;
case 24:
//#line 69 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera 'tipo de objeto a declarar'."));}
break;
case 25:
//#line 70 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera 'nombre de objeto a declarar'."));}
break;
case 26:
//#line 71 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera 'nombre de objeto a declarar'."));}
break;
case 27:
//#line 72 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera 'nombre y tipo de objeto a declarar'."));}
break;
case 36:
//#line 93 "gramatica.y"
{ Lexema lex = TablaDeSimbolos.getByID(val_peek(0).ival);
                        System.out.println(val_peek(0).ival);
                        System.out.println(lex);
                        System.out.println(TablaDeSimbolos.imprimir());
                        chequearRango(lex);                             /*SOLO SE CHEQUEA EN POSITIVO YA QUE EL MAXIMO DE NEGATIVOS ES MAYOR AL MAXIMO DE POSITIVOS Y YA LO CHEQUEA EL PARSER*/
                      }
break;
case 37:
//#line 99 "gramatica.y"
{
                            Lexema lex = TablaDeSimbolos.getByID(val_peek(0).ival);
                            int newLexRef = TablaDeSimbolos.agregarSimbolo("-"+lex.getAtributo(), lex.getTipo());
                            /*$2.sval = newLex.*/
                          }
break;
case 43:
//#line 121 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta ';'."));}
break;
case 44:
//#line 122 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta ';'."));}
break;
case 45:
//#line 126 "gramatica.y"
{estructuras.add("Retorno");}
break;
case 46:
//#line 130 "gramatica.y"
{estructuras.add("Asignacion");}
break;
case 47:
//#line 131 "gramatica.y"
{estructuras.add("IF");}
break;
case 48:
//#line 132 "gramatica.y"
{estructuras.add("WHILE");}
break;
case 49:
//#line 133 "gramatica.y"
{estructuras.add("GOTO");}
break;
case 50:
//#line 134 "gramatica.y"
{estructuras.add("OUTF");}
break;
case 51:
//#line 136 "gramatica.y"
{estructuras.add("Asignacion");}
break;
case 52:
//#line 137 "gramatica.y"
{estructuras.add("WHILE");}
break;
case 53:
//#line 138 "gramatica.y"
{estructuras.add("GOTO");}
break;
case 54:
//#line 139 "gramatica.y"
{estructuras.add("OUTF");}
break;
case 56:
//#line 141 "gramatica.y"
{estructuras.add("IF");}
break;
case 73:
//#line 173 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta 'end_if;'."));}
break;
case 74:
//#line 174 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta 'end_if;'."));}
break;
case 90:
//#line 199 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta ';'."));}
break;
case 92:
//#line 204 "gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta ';'."));}
break;
//#line 858 "Parser.java"
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
