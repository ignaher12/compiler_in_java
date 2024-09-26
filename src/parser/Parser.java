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

import lexico.AnalizadorLexico;
import lexico.TablaDeSimbolos;
import lexico.AccionesSemanticas.Accion;
import utils.MatrizAccion;
import utils.MatrizTransicion;
import java.util.ArrayList;
import java.util.List;

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
public final static short FLOAT=259;
public final static short CADENA_MULTI=260;
public final static short SIMASIGNACION=261;
public final static short DISTINTO=262;
public final static short IF=263;
public final static short THEN=264;
public final static short BEGIN=265;
public final static short END=266;
public final static short END_IF=267;
public final static short OUTF=268;
public final static short TYPEDEF=269;
public final static short FUN=270;
public final static short RET=271;
public final static short SINGLE=272;
public final static short MENOR_IGUAL=273;
public final static short MAYOR_IGUAL=274;
public final static short REPEAT=275;
public final static short WHILE=276;
public final static short GOTO=277;
public final static short LONGINT=278;
public final static short ELSE=279;
public final static short TRIPLE=280;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    1,    1,    2,    2,    3,    3,    3,    6,    6,
    5,    5,    5,    5,    8,    8,    7,    7,   11,   11,
   11,    9,   10,   10,   10,   12,    4,    4,    4,    4,
    4,   14,   14,   14,   14,   13,   13,   20,   20,   20,
   20,   19,   19,   19,   19,   19,   19,   21,   21,   15,
   15,   22,   24,   24,   25,   25,   25,   25,   25,   25,
   23,   23,   23,   16,   17,   18,   18,
};
final static short yylen[] = {                            2,
    4,    2,    1,    1,    1,    3,    8,    7,    1,    8,
    1,    1,    1,    1,    3,    1,    3,    1,    1,    1,
    1,    2,    2,    1,    1,    5,    1,    1,    1,    1,
    1,    4,    7,    7,    7,    1,    3,    1,    1,    1,
    1,    1,    1,    1,    4,    4,    4,    4,    5,    8,
   10,    3,    1,    3,    1,    1,    1,    1,    1,    1,
    1,    1,    3,    5,    4,    5,    5,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,   11,    0,    0,
   12,   14,    0,    3,    4,    5,    0,   27,   28,   29,
   30,   31,    0,    0,    0,    0,    0,    0,    0,    0,
   61,   62,    0,    0,    1,    2,   16,    0,    0,    0,
    0,   21,   20,   19,   43,    0,   36,   44,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   24,    0,    0,    0,    0,    6,    0,    0,    0,   38,
   39,   40,   41,   32,    0,    0,    0,    0,    0,   58,
   59,   60,   55,   56,    0,   57,    0,    0,    0,   13,
    0,    0,   23,   63,    0,    0,   65,    0,   15,    0,
    0,    0,    0,    0,    0,    0,   37,    0,    0,    0,
    0,    0,    0,   67,   66,    0,    0,    0,   64,    0,
    0,   45,   46,   47,    0,   48,    0,    0,    0,    0,
    0,   18,    0,   26,   22,    0,   49,   33,   34,   35,
    0,    0,    0,    0,    8,    0,   50,    0,    7,   17,
    0,    0,   10,   51,
};
final static short yydgoto[] = {                          2,
   59,   14,   15,   16,   17,   39,  131,   40,  121,   60,
   45,   32,   52,   18,   19,   20,   21,   22,   47,   75,
   48,   53,   33,   54,   87,
};
final static short yysindex[] = {                      -199,
 -198,    0, -127,  -88,   20,   28, -226,    0, -191, -181,
    0,    0, -166,    0,    0,    0, -205,    0,    0,    0,
    0,    0, -196,   44, -196,  -96, -182,   30, -153,   58,
    0,    0, -177,   37,    0,    0,    0, -152,   48,   69,
  -35,    0,    0,    0,    0,  124,    0,    0,   24,   27,
   33,  223,   80,  -32,   88,  157, -123, -123, -153, -131,
    0, -196, -196,   84,  104,    0, -111,  164, -120,    0,
    0,    0,    0,    0, -196, -114, -105, -102,  -99,    0,
    0,    0,    0,    0, -196,    0, -196,  109,  111,    0,
   81,  114,    0,    0,  165,  119,    0, -123,    0,   87,
   93,   95,  -35,    0, -196,  186,    0, -196, -196, -196,
 -191,  223,  137,    0,    0, -208,  -67,  135,    0,  -61,
  162,    0,    0,    0,  217,    0,  132,  142,  150, -210,
  -18,    0,  146,    0,    0,  -54,    0,    0,    0,    0,
  160, -191,  161, -208,    0, -153,    0,  -46,    0,    0,
  -44,  167,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0, -174,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  173,
  -27,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  -22,    0,    0,    0,    0,    0,    0,  -30,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  -41,  -34,    0,    0,    0,    0,    0,    0,
    0,  -17,   14,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
  231,   12,    0,    0,  -10,    0,    0,    0,    0,   92,
  -80,   -6,   23,    0,    0,    0,    0,    0,  168,    0,
    0,  176, -101,  163,    0,
};
final static int YYTABLESIZE=270;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         42,
   42,   42,   24,   42,   69,   42,   19,   19,   19,  130,
   19,   85,   19,   42,   42,   42,   42,   42,   53,   42,
   31,   53,   61,   54,   36,  144,   54,   83,   86,   84,
   27,   42,   42,   42,   42,  132,   53,   53,   53,   53,
  148,   54,   54,   54,   54,   46,   91,   92,   56,   42,
   43,   37,   93,   28,   52,   68,  141,    1,  105,   25,
   41,   42,   43,  150,   38,    4,    3,   26,  142,   44,
   36,    5,   52,   29,  143,   34,    6,    7,   57,   30,
    8,   44,   13,    9,   95,   10,   11,  120,   12,   58,
    4,  106,   49,   50,   51,   13,    5,   62,   63,   35,
   64,    6,    7,    4,   65,    8,   66,  112,    9,    5,
   10,   11,   67,   12,    6,    7,   76,   30,    8,   77,
   79,    9,   31,   10,   11,   78,   12,  125,   88,    4,
  127,  128,  129,   90,   94,    5,  103,   42,   43,   61,
    6,    7,   97,   98,    8,   99,  108,    9,    8,   10,
   11,    8,   12,   31,   11,  109,   12,  104,  110,   12,
   41,   42,   43,   55,  111,   72,   70,  114,   71,  115,
   73,  116,   23,   72,   70,  117,   71,  119,   73,  122,
   85,   44,   74,   72,   70,  123,   71,  124,   73,  133,
  138,   72,   70,  134,   71,  135,   73,   89,   72,   70,
  139,   71,  136,   73,  145,  118,   72,   70,  140,   71,
  146,   73,  100,  101,  102,   13,   13,   13,  147,  149,
  152,  153,   12,   12,   12,  154,  126,   72,   70,   80,
   71,    9,   73,   13,   42,   25,   13,  151,   96,   53,
   81,   82,  107,   12,   54,   42,   42,    0,    0,  113,
   53,   53,    0,    0,    0,   54,   54,  137,   72,   70,
    0,   71,    0,   73,   72,   70,    0,   71,    0,   73,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         41,
   42,   43,   91,   45,   40,   47,   41,   42,   43,  111,
   45,   44,   47,   41,   42,   43,   44,   45,   41,   47,
    9,   44,   29,   41,   13,   44,   44,   60,   61,   62,
  257,   59,   60,   61,   62,  116,   59,   60,   61,   62,
  142,   59,   60,   61,   62,   23,   57,   58,   26,  258,
  259,  257,   59,  280,   41,   91,  267,  257,   69,   40,
  257,  258,  259,  144,  270,  257,  265,   40,  279,  278,
   59,  263,   59,  265,   93,  257,  268,  269,  261,  271,
  272,  278,  257,  275,   62,  277,  278,   98,  280,   60,
  257,   69,   49,   50,   51,  270,  263,   40,  276,  266,
   64,  268,  269,  257,  257,  272,   59,   85,  275,  263,
  277,  278,   44,  280,  268,  269,   93,  271,  272,   93,
   41,  275,  111,  277,  278,   93,  280,  105,   41,  257,
  108,  109,  110,  257,  266,  263,  257,  258,  259,  146,
  268,  269,   59,   40,  272,  257,  261,  275,  272,  277,
  278,  272,  280,  142,  278,  261,  280,  278,  261,  280,
  257,  258,  259,  260,  264,   42,   43,   59,   45,   59,
   47,   91,  261,   42,   43,   62,   45,   59,   47,   93,
   44,  278,   59,   42,   43,   93,   45,   93,   47,  257,
   59,   42,   43,   59,   45,  257,   47,   41,   42,   43,
   59,   45,   41,   47,   59,   41,   42,   43,   59,   45,
  265,   47,   49,   50,   51,  257,  258,  259,   59,   59,
  267,  266,  257,  258,  259,   59,   41,   42,   43,  262,
   45,   59,   47,    3,  262,  266,  278,  146,   63,  262,
  273,  274,   75,  278,  262,  273,  274,   -1,   -1,   87,
  273,  274,   -1,   -1,   -1,  273,  274,   41,   42,   43,
   -1,   45,   -1,   47,   42,   43,   -1,   45,   -1,   47,
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
null,null,null,null,null,null,null,null,"IDENTIFICADOR","HEXADECIMAL","FLOAT",
"CADENA_MULTI","SIMASIGNACION","DISTINTO","IF","THEN","BEGIN","END","END_IF",
"OUTF","TYPEDEF","FUN","RET","SINGLE","MENOR_IGUAL","MAYOR_IGUAL","REPEAT",
"WHILE","GOTO","LONGINT","ELSE","TRIPLE",
};
final static String yyrule[] = {
"$accept : programa",
"programa : IDENTIFICADOR BEGIN cuerpo END",
"cuerpo : cuerpo sentencia",
"cuerpo : sentencia",
"sentencia : sentenciaDeclarativa",
"sentencia : sentenciaEjecutable",
"sentenciaDeclarativa : tipoDato declaracion ';'",
"sentenciaDeclarativa : TYPEDEF IDENTIFICADOR SIMASIGNACION tipoDato '[' listaConstante ']' ';'",
"sentenciaDeclarativa : TYPEDEF TRIPLE '<' tipoDato '>' IDENTIFICADOR ';'",
"declaracion : listaVariable",
"declaracion : FUN IDENTIFICADOR '(' parametro ')' BEGIN cuerpoFuncion END",
"tipoDato : SINGLE",
"tipoDato : LONGINT",
"tipoDato : IDENTIFICADOR",
"tipoDato : TRIPLE",
"listaVariable : listaVariable ',' IDENTIFICADOR",
"listaVariable : IDENTIFICADOR",
"listaConstante : listaConstante ',' Constante",
"listaConstante : Constante",
"Constante : LONGINT",
"Constante : FLOAT",
"Constante : HEXADECIMAL",
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
"operando : Constante",
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

//#line 133 "gramatica.y"
//FUNCIONES
private static AnalizadorLexico lex;
public static List<Error> erroresLexico = new ArrayList<Error>();
public static void main(String[] args) {
    String filePath = "src/MATRIZ DE TRANSICIONES - Hoja 1.csv";

    int[][] matriz = MatrizTransicion.leerMatrizDesdeCSV(filePath);
    filePath = "src/MATRIZ DE TRANSICIONES - Hoja 2.csv";

    Accion[][] matrizAcciones = MatrizAccion.leerMatrizDesdeCSV(filePath);

    Parser.lex = new AnalizadorLexico("codigoFuente.txt", matriz, matrizAcciones);
    if (args.length > -1) {
            //String archivo_a_leer = args[0];
            Parser parser = new Parser();
            parser.run();

            for (Error error: erroresLexico){System.out.println(error);}
    } else {
            System.out.println("No se especifico el archivo a compilar");
    }
    System.out.println(TablaDeSimbolos.imprimir());
}
private int yylex(){
  int idToken = -1;
  if (!lex.end()){
    idToken = lex.getNextToken(yyval);
  }
  //System.out.println(yyval.sval);
  return idToken;
}
private void yyerror(String string) {
  throw new UnsupportedOperationException("ERROR");
}
//#line 389 "Parser.java"
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
