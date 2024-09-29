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
    0,    1,    1,    2,    2,    3,    3,    3,    7,    7,
    7,    8,    5,    5,    5,    5,    6,    6,    9,    9,
   12,   12,   10,   11,   11,   11,   13,    4,    4,    4,
    4,    4,   15,   15,   15,   15,   14,   14,   21,   21,
   21,   21,   20,   20,   20,   20,   20,   20,   22,   22,
   16,   16,   23,   25,   25,   26,   26,   26,   26,   26,
   26,   24,   24,   24,   17,   18,   19,   19,
};
final static short yylen[] = {                            2,
    4,    2,    1,    1,    1,    3,    2,    2,    7,    6,
    6,    8,    1,    1,    1,    1,    3,    1,    3,    1,
    1,    2,    2,    2,    1,    1,    5,    1,    1,    1,
    1,    1,    4,    7,    7,    7,    1,    3,    1,    1,
    1,    1,    1,    1,    1,    4,    4,    4,    4,    5,
    8,   10,    3,    1,    3,    1,    1,    1,    1,    1,
    1,    1,    1,    3,    5,    4,    5,    5,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,   15,    0,    0,    0,    0,   13,
    0,    0,   14,   16,    0,    3,    4,    5,    0,    0,
    0,   28,   29,   30,   31,   32,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   62,   63,    0,    0,    1,
    2,   18,    0,    7,    8,    0,   21,    0,   44,    0,
   37,   45,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   25,    0,    0,    0,    6,
    0,    0,    0,   22,   39,   40,   41,   42,   33,    0,
    0,    0,    0,    0,   59,   60,   61,   56,   57,    0,
   58,    0,    0,    0,    0,    0,    0,    0,    0,   24,
   64,    0,    0,   66,   17,    0,    0,    0,    0,    0,
   38,    0,    0,    0,    0,    0,    0,   68,   67,    0,
    0,    0,   23,    0,    0,   65,   46,   47,   48,    0,
   49,    0,    0,    0,    0,    0,   20,   11,   10,    0,
   27,   50,   34,   35,   36,    0,    0,    9,    0,    0,
   51,    0,   19,   12,    0,   52,
};
final static short yydgoto[] = {                          2,
   64,   16,   17,   18,   19,   43,   20,   21,  136,   99,
   65,   49,   37,   56,   22,   23,   24,   25,   26,   51,
   80,   52,   57,   38,   58,   92,
};
final static short yysindex[] = {                      -213,
 -215,    0,  -99,  -87,    0,   11,   22, -234, -190,    0,
 -192, -184,    0,    0, -145,    0,    0,    0, -183,   24,
   27,    0,    0,    0,    0,    0,  -42,   47,  -42,  -43,
 -179,   30,   52, -122,   60,    0,    0, -174,   42,    0,
    0,    0,   -6,    0,    0,  -22,    0, -173,    0,   46,
    0,    0,   16,   17,   25,  182,   74,  -14,   80,   96,
 -221, -163, -221, -122, -140,    0,  -42,  -42,   68,    0,
 -129,  159,  -45,    0,    0,    0,    0,    0,    0,  -42,
 -130, -127, -118, -119,    0,    0,    0,    0,    0,  -42,
    0,  -42,   91,   94,   65,  103,  105,  -86,  133,    0,
    0,  119,  117,    0,    0,   86,   90,   92,  -42,  160,
    0,  -42,  -42,  -42, -192,  182,  147,    0,    0,  -44,
  -68,  -64,    0,  -58,  137,    0,    0,    0,    0,  176,
    0,  139,  145,  152, -214,  -38,    0,    0,    0, -122,
    0,    0,    0,    0,    0,  141, -192,    0,  -44,  -35,
    0,  -46,    0,    0,  163,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  -33,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  -28,    0,    0,    0,    0,
    0,    0,    0,  -34,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  -20,  -16,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
  239,    4,    0,    0,   -2,    0,    0,    0,    0,    0,
  106, -113,  -29,  -10,    0,    0,    0,    0,    0,  168,
    0,    0,  175,  -93,  157,    0,
};
final static int YYTABLESIZE=259;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         48,
   48,   48,   48,   28,   66,  149,  137,   43,   43,   43,
   43,   43,   54,   43,   36,   54,   50,   73,   41,   60,
   55,  135,   31,   55,   53,   43,   43,   43,   43,   90,
   54,   54,   54,   54,  100,  153,    5,   71,   55,   55,
   55,   55,   53,    1,   32,   88,   91,   89,    3,   10,
   29,  146,   70,  152,  148,   13,  102,   14,   95,   97,
   98,   30,  110,  147,    4,    5,   33,   41,   72,    6,
  109,   34,   39,   42,    7,    8,    9,   35,   10,  116,
   61,   11,   44,   12,   13,   45,   14,   77,   75,   62,
   76,   63,   78,   96,    5,   53,   54,   55,  130,   67,
   68,  132,  133,  134,   79,   69,   74,   10,   81,   82,
   66,    4,    5,   13,   84,   14,    6,   83,   36,   40,
   93,    7,    8,    9,  101,   10,  104,  105,   11,  112,
   12,   13,  113,   14,    4,    5,   94,   77,   75,    6,
   76,  114,   78,  115,    7,    8,    9,   35,   10,  118,
   36,   11,  119,   12,   13,  120,   14,    4,    5,  125,
   77,   75,    6,   76,  121,   78,  122,    7,    8,    9,
  123,   10,   27,  124,   11,  126,   12,   13,  127,   14,
   77,   75,  128,   76,  129,   78,   77,   75,  138,   76,
   90,   78,  139,   77,   75,  141,   76,  143,   78,  151,
  131,   77,   75,  144,   76,  140,   78,  106,  107,  108,
  145,   46,    5,   46,   46,   59,  142,   77,   75,  155,
   76,  156,   78,   77,   75,   10,   76,   43,   78,  154,
   26,   13,   54,   14,   47,   47,   47,   47,   43,   43,
   55,   15,  103,   54,   54,  150,   85,  111,  117,    0,
    0,   55,   55,    0,    0,    0,    0,   86,   87,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         45,
   45,   45,   45,   91,   34,   44,  120,   41,   42,   43,
   44,   45,   41,   47,   11,   44,   27,   40,   15,   30,
   41,  115,  257,   44,   41,   59,   60,   61,   62,   44,
   59,   60,   61,   62,   64,  149,  258,   44,   59,   60,
   61,   62,   59,  257,  279,   60,   61,   62,  264,  271,
   40,  266,   59,  147,   93,  277,   67,  279,   61,   62,
   63,   40,   73,  278,  257,  258,  257,   64,   91,  262,
   73,  264,  257,  257,  267,  268,  269,  270,  271,   90,
  260,  274,   59,  276,  277,   59,  279,   42,   43,   60,
   45,   40,   47,  257,  258,   49,   50,   51,  109,   40,
  275,  112,  113,  114,   59,   64,  280,  271,   93,   93,
  140,  257,  258,  277,   41,  279,  262,   93,  115,  265,
   41,  267,  268,  269,  265,  271,   59,  257,  274,  260,
  276,  277,  260,  279,  257,  258,   41,   42,   43,  262,
   45,  260,   47,  263,  267,  268,  269,  270,  271,   59,
  147,  274,   59,  276,  277,   91,  279,  257,  258,   41,
   42,   43,  262,   45,   62,   47,   62,  267,  268,  269,
  257,  271,  260,   41,  274,   59,  276,  277,   93,  279,
   42,   43,   93,   45,   93,   47,   42,   43,  257,   45,
   44,   47,  257,   42,   43,   59,   45,   59,   47,   59,
   41,   42,   43,   59,   45,  264,   47,   49,   50,   51,
   59,  257,  258,  257,  257,  259,   41,   42,   43,  266,
   45,   59,   47,   42,   43,  271,   45,  261,   47,  265,
  265,  277,  261,  279,  280,  280,  280,  280,  272,  273,
  261,    3,   68,  272,  273,  140,  261,   80,   92,   -1,
   -1,  272,  273,   -1,   -1,   -1,   -1,  272,  273,
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
"sentenciaDeclarativa : funDeclaracion ';'",
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

//#line 190 "C:\Users\pipig\Documents\GitHub\compiler_in_java\src\parser\gramatica.y"
//FUNCIONES
private static AnalizadorLexico lex;
public static List<Error> erroresLexico = new ArrayList<Error>();
public static List<Error> erroresSemantico = new ArrayList<Error>();
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
        Parser.lex = new AnalizadorLexico("CP3.txt", matriz, matrizAcciones);
        
        parser.run();
        for (Error error: erroresLexico){System.out.println(error);}
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
//#line 406 "Parser.java"
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
case 21:
//#line 72 "C:\Users\pipig\Documents\GitHub\compiler_in_java\src\parser\gramatica.y"
{ Lexema lex = TablaDeSimbolos.getByID(val_peek(0).ival);
                        System.out.println(val_peek(0).ival);
                        System.out.println(lex);
                        System.out.println(TablaDeSimbolos.imprimir());
                        if(lex.getTipo() == TablaTipoToken.getTipoToken("longint")){
                          if(Integer.parseInt(lex.getAtributo()) > AnalizadorLexico.MAXLONGINT){
                            erroresSemantico.add(new Error(
                              AnalizadorLexico.getNumeroLinea(), 
                              Tipo.ERROR, 
                              "ERROR SINTACTICO excede rangos."
                            ));
                          }
                        } else if (lex.getTipo() == TablaTipoToken.getTipoToken("single")){
                          String valor = lex.getAtributo().toString().replace('s', 'e');
                          if(Float.parseFloat(valor) > AnalizadorLexico.MAXFLOATPOSITIVO){
                            erroresSemantico.add(new Error(
                              AnalizadorLexico.getNumeroLinea(), 
                              Tipo.ERROR, 
                              "ERROR SINTACTICO excede rangos."
                            ));
                          } else if (Float.parseFloat(valor) < AnalizadorLexico.MINFLOATPOSITIVO) {
                            erroresSemantico.add(new Error(
                              AnalizadorLexico.getNumeroLinea(), 
                              Tipo.ERROR, 
                              "ERROR SINTACTICO excede rangos."
                            ));}
                        } else {
                          if(Double.parseDouble(lex.getAtributo()) > AnalizadorLexico.MAXHEXADECIMAL){
                            erroresSemantico.add(new Error(
                              AnalizadorLexico.getNumeroLinea(), 
                              Tipo.ERROR, 
                              "ERROR SINTACTICO excede rangos."
                            ));
                          } 
                        }
                      }
break;
case 22:
//#line 107 "C:\Users\pipig\Documents\GitHub\compiler_in_java\src\parser\gramatica.y"
{
                            Lexema lex = TablaDeSimbolos.getByID(val_peek(0).ival);
                            int newLexRef = TablaDeSimbolos.agregarSimbolo("-"+lex.getAtributo(), lex.getTipo());
                            /*$2.sval = newLex.*/
                          }
break;
//#line 601 "Parser.java"
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
