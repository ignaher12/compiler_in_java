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






//#line 2 "C:\Users\pipig\Documents\GitHub\compiler_in_java\src\parser\gramatica.y"
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
    0,    1,    1,    2,    2,    3,    3,    3,    3,    7,
    7,    7,    8,    5,    5,    5,    5,    6,    6,    9,
    9,   12,   12,   10,   11,   11,   11,   13,    4,    4,
    4,    4,    4,   15,   15,   15,   15,   14,   14,   21,
   21,   21,   21,   20,   20,   20,   20,   20,   20,   22,
   22,   16,   16,   23,   25,   25,   26,   26,   26,   26,
   26,   26,   24,   24,   24,   17,   18,   19,   19,
};
final static short yylen[] = {                            2,
    4,    2,    1,    1,    1,    3,    2,    3,    3,    7,
    6,    6,    8,    1,    1,    1,    1,    3,    1,    3,
    1,    1,    2,    2,    2,    1,    1,    5,    1,    1,
    1,    1,    1,    4,    7,    7,    7,    1,    3,    1,
    1,    1,    1,    1,    1,    1,    4,    4,    4,    4,
    5,    8,   10,    3,    1,    3,    1,    1,    1,    1,
    1,    1,    1,    1,    3,    5,    4,    5,    5,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,   16,    0,    0,    0,   14,    0,
    0,   15,   17,    0,    3,    4,    5,    0,    0,   29,
   30,   31,   32,   33,    0,    0,    0,    0,    0,    0,
    0,    0,   63,   64,    0,    0,    1,    2,    0,   19,
    0,    0,    0,    7,    0,   22,    0,   45,    0,   38,
   46,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   26,    0,    0,    0,    9,    0,    6,
    0,    8,    0,    0,   23,   40,   41,   42,   43,   34,
    0,    0,    0,    0,    0,   60,   61,   62,   57,   58,
    0,   59,    0,    0,    0,    0,    0,    0,   25,   65,
    0,    0,   67,    0,   18,    0,    0,    0,    0,    0,
   39,    0,    0,    0,    0,    0,    0,   69,   68,    0,
    0,    0,    0,   66,    0,    0,   47,   48,   49,    0,
   50,    0,    0,    0,    0,    0,   21,   12,   11,   28,
   24,    0,   51,   35,   36,   37,    0,    0,   10,    0,
    0,   52,    0,   20,    0,    0,   13,   53,
};
final static short yydgoto[] = {                          2,
   62,   15,   16,   17,   18,   42,   19,   43,  136,  126,
   63,   48,   34,   55,   20,   21,   22,   23,   24,   50,
   81,   51,   56,   35,   57,   93,
};
final static short yysindex[] = {                      -213,
 -219,    0, -108,  -87,    0,   13,   14, -214,    0, -182,
 -196,    0,    0, -155,    0,    0,    0, -198,    4,    0,
    0,    0,    0,    0,  -42,   28,  -42,  -43, -187,   24,
 -131,   34,    0,    0, -211,   23,    0,    0,   31,    0,
 -166,   -9,   37,    0,  -22,    0, -172,    0,  132,    0,
    0,   16,   18,   21,  164,   52,  -14,   74,   87, -209,
 -101, -131, -148,    0,  -42,  -42,   59,    0,   83,    0,
 -124,    0,   49,  -45,    0,    0,    0,    0,    0,    0,
  -42, -125, -122, -119, -121,    0,    0,    0,    0,    0,
  -42,    0,  -42,   85,   88,   60,   90,   93,    0,    0,
  158,   99,    0, -209,    0,   68,   69,   71,  -42,  176,
    0,  -42,  -42,  -42, -182,  164,  121,    0,    0,  -44,
  -90,  -85,  123,    0,  -73,  148,    0,    0,    0,  219,
    0,  138,  145,  151, -206,  -38,    0,    0,    0,    0,
    0,  -78,    0,    0,    0,    0,  136, -182,    0,  -44,
 -131,    0,  -64,    0,  -57,  161,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  -33,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  -28,    0,    0,    0,    0,    0,
    0,  -41,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  -20,  -34,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
  222,    5,    0,    0,  -23,    0,    0,    0,    0,    0,
   76,  -98,  -26,   -8,    0,    0,    0,    0,    0,  141,
    0,    0,  163,  -92,  137,    0,
};
final static int YYTABLESIZE=266;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         47,
   47,   47,   47,   26,   64,  150,   54,   44,   44,   44,
   44,   44,   55,   44,   33,   55,   49,   74,   38,   59,
   56,  137,  135,   56,   54,   44,   44,   44,   44,   91,
   55,   55,   55,   55,   71,   99,   96,   98,   56,   56,
   56,   56,   29,    1,    3,   89,   92,   90,    5,   70,
  109,  154,   27,   28,  149,  153,  101,   39,   40,  147,
   36,    9,   44,   66,   30,  110,   38,   12,   73,   13,
   41,  148,   60,   65,    4,    5,   52,   53,   54,    6,
  125,   31,  116,   61,    7,    8,   67,   32,    9,   68,
   69,   10,   85,   11,   12,   72,   13,  106,  107,  108,
  130,    4,    5,  132,  133,  134,    6,   75,   82,   37,
   83,    7,    8,   84,   94,    9,  100,  103,   10,   33,
   11,   12,  104,   13,   64,    4,    5,   95,   78,   76,
    6,   77,  105,   79,  112,    7,    8,  113,   32,    9,
  114,  115,   10,  118,   11,   12,  119,   13,    4,    5,
  120,  121,   33,    6,  122,   97,    5,  124,    7,    8,
  127,  128,    9,  129,   91,   10,  138,   11,   12,    9,
   13,  139,   25,   78,   76,   12,   77,   13,   79,   78,
   76,  140,   77,  141,   79,  151,   78,   76,  142,   77,
   80,   79,   78,   76,  152,   77,  144,   79,  123,   78,
   76,  156,   77,  145,   79,   78,   76,  157,   77,  146,
   79,   45,    5,   45,   45,   58,  131,   78,   76,  158,
   77,  111,   79,   27,   14,    9,  155,   44,  102,  117,
    0,   12,   55,   13,   46,   46,   46,   46,   44,   44,
   56,    0,    0,   55,   55,    0,   86,    0,    0,    0,
    0,   56,   56,    0,    0,    0,    0,   87,   88,  143,
   78,   76,    0,   77,    0,   79,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         45,
   45,   45,   45,   91,   31,   44,   41,   41,   42,   43,
   44,   45,   41,   47,   10,   44,   25,   40,   14,   28,
   41,  120,  115,   44,   59,   59,   60,   61,   62,   44,
   59,   60,   61,   62,   44,   62,   60,   61,   59,   60,
   61,   62,  257,  257,  264,   60,   61,   62,  258,   59,
   74,  150,   40,   40,   93,  148,   65,  256,  257,  266,
  257,  271,   59,  275,  279,   74,   62,  277,   91,  279,
  269,  278,  260,   40,  257,  258,   49,   50,   51,  262,
  104,  264,   91,   60,  267,  268,   64,  270,  271,   59,
  257,  274,   41,  276,  277,   59,  279,   49,   50,   51,
  109,  257,  258,  112,  113,  114,  262,  280,   93,  265,
   93,  267,  268,   93,   41,  271,  265,   59,  274,  115,
  276,  277,   40,  279,  151,  257,  258,   41,   42,   43,
  262,   45,  257,   47,  260,  267,  268,  260,  270,  271,
  260,  263,  274,   59,  276,  277,   59,  279,  257,  258,
   91,   62,  148,  262,   62,  257,  258,   59,  267,  268,
   93,   93,  271,   93,   44,  274,  257,  276,  277,  271,
  279,  257,  260,   42,   43,  277,   45,  279,   47,   42,
   43,   59,   45,  257,   47,  264,   42,   43,   41,   45,
   59,   47,   42,   43,   59,   45,   59,   47,   41,   42,
   43,  266,   45,   59,   47,   42,   43,  265,   45,   59,
   47,  257,  258,  257,  257,  259,   41,   42,   43,   59,
   45,   81,   47,  265,    3,  271,  151,  261,   66,   93,
   -1,  277,  261,  279,  280,  280,  280,  280,  272,  273,
  261,   -1,   -1,  272,  273,   -1,  261,   -1,   -1,   -1,
   -1,  272,  273,   -1,   -1,   -1,   -1,  272,  273,   41,
   42,   43,   -1,   45,   -1,   47,
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
"cuerpo : cuerpo sentencia",
"cuerpo : sentencia",
"sentencia : sentenciaDeclarativa",
"sentencia : sentenciaEjecutable",
"sentenciaDeclarativa : tipoDato listaVariable ';'",
"sentenciaDeclarativa : typedefDeclaracion ';'",
"sentenciaDeclarativa : tipoDato funDeclaracion ';'",
"sentenciaDeclarativa : tipoDato error ';'",
"typedefDeclaracion : TYPEDEF IDENTIFICADOR SIMASIGNACION tipoDato '[' listaConstante ']'",
"typedefDeclaracion : TYPEDEF TRIPLE '<' tipoDato '>' IDENTIFICADOR",
"typedefDeclaracion : TYPEDEF TRIPLE '<' IDENTIFICADOR '>' IDENTIFICADOR",
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
"cuerpoFuncion : cuerpo sentenciaRet",
"cuerpoFuncion : sentenciaRet",
"cuerpoFuncion : cuerpo",
"sentenciaRet : RET '(' expresion ')' ';'",
"sentenciaEjecutable : asignacion",
"sentenciaEjecutable : clausulaSeleccion",
"sentenciaEjecutable : clausulaBucle",
"sentenciaEjecutable : goto",
"sentenciaEjecutable : mensajeSalida",
"asignacion : IDENTIFICADOR SIMASIGNACION expresion ';'",
"asignacion : IDENTIFICADOR '[' '1' ']' SIMASIGNACION expresion ';'",
"asignacion : IDENTIFICADOR '[' '2' ']' SIMASIGNACION expresion ';'",
"asignacion : IDENTIFICADOR '[' '3' ']' SIMASIGNACION expresion ';'",
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
"clausulaSeleccion : IF '(' condicion ')' THEN bloqueIF END_IF ';'",
"clausulaSeleccion : IF '(' condicion ')' THEN bloqueIF ELSE bloqueIF END_IF ';'",
"condicion : listaExpresion comparador listaExpresion",
"listaExpresion : expresion",
"listaExpresion : listaExpresion ',' expresion",
"comparador : '<'",
"comparador : '>'",
"comparador : '='",
"comparador : DISTINTO",
"comparador : MENOR_IGUAL",
"comparador : MAYOR_IGUAL",
"bloqueIF : sentencia",
"bloqueIF : sentenciaRet",
"bloqueIF : BEGIN cuerpoFuncion END",
"clausulaBucle : REPEAT bloqueIF WHILE condicion ';'",
"goto : GOTO IDENTIFICADOR '@' ';'",
"mensajeSalida : OUTF '(' expresion ')' ';'",
"mensajeSalida : OUTF '(' CADENA_MULTI ')' ';'",
};

//#line 196 "C:\Users\pipig\Documents\GitHub\compiler_in_java\src\parser\gramatica.y"
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
    Parser.lex = new AnalizadorLexico("codigoFuente.txt", matriz, matrizAcciones);
    if (args.length > 1) {
        Parser.lex = new AnalizadorLexico(args[0], matriz, matrizAcciones);

        parser.run();
        for (Error error: erroresLexico){System.out.println(error);}
    } else {
        Parser.lex = new AnalizadorLexico("codigoFuente.txt", matriz, matrizAcciones);
        
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
//#line 414 "Parser.java"
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
case 6:
//#line 44 "C:\Users\pipig\Documents\GitHub\compiler_in_java\src\parser\gramatica.y"
{estructuras.add("Declaracion");}
break;
case 7:
//#line 45 "C:\Users\pipig\Documents\GitHub\compiler_in_java\src\parser\gramatica.y"
{estructuras.add("Declaracion de typedef");}
break;
case 8:
//#line 46 "C:\Users\pipig\Documents\GitHub\compiler_in_java\src\parser\gramatica.y"
{estructuras.add("Declaracion de funcion");}
break;
case 9:
//#line 47 "C:\Users\pipig\Documents\GitHub\compiler_in_java\src\parser\gramatica.y"
{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO en declaracion."));
                     System.out.println("gafgsfagsfdvbasdihybavsduljhbas");}
break;
case 22:
//#line 74 "C:\Users\pipig\Documents\GitHub\compiler_in_java\src\parser\gramatica.y"
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
                            if(Double.parseDouble(lex.getAtributo()) > AnalizadorLexico.MAXHEXADECIMAL){
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
case 23:
//#line 113 "C:\Users\pipig\Documents\GitHub\compiler_in_java\src\parser\gramatica.y"
{
                            Lexema lex = TablaDeSimbolos.getByID(val_peek(0).ival);
                            int newLexRef = TablaDeSimbolos.agregarSimbolo("-"+lex.getAtributo(), lex.getTipo());
                            /*$2.sval = newLex.*/
                          }
break;
case 28:
//#line 130 "C:\Users\pipig\Documents\GitHub\compiler_in_java\src\parser\gramatica.y"
{estructuras.add("Retorno");}
break;
case 29:
//#line 134 "C:\Users\pipig\Documents\GitHub\compiler_in_java\src\parser\gramatica.y"
{estructuras.add("Asignacion");}
break;
case 30:
//#line 135 "C:\Users\pipig\Documents\GitHub\compiler_in_java\src\parser\gramatica.y"
{estructuras.add("IF");}
break;
case 31:
//#line 136 "C:\Users\pipig\Documents\GitHub\compiler_in_java\src\parser\gramatica.y"
{estructuras.add("WHILE");}
break;
case 32:
//#line 137 "C:\Users\pipig\Documents\GitHub\compiler_in_java\src\parser\gramatica.y"
{estructuras.add("GOTO");}
break;
case 33:
//#line 138 "C:\Users\pipig\Documents\GitHub\compiler_in_java\src\parser\gramatica.y"
{estructuras.add("OUTF");}
break;
//#line 654 "Parser.java"
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
