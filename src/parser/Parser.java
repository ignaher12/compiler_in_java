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
import utils.*;
import lexico.*;
import lexico.AccionesSemanticas.Accion;


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
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    1,    1,    2,    2,    3,    3,    6,    6,    5,
    5,    5,    8,    8,    7,    7,   11,   11,   11,    9,
   10,   10,   10,   12,    4,    4,    4,    4,    4,   14,
   13,   13,   20,   20,   20,   20,   19,   19,   19,   21,
   21,   15,   15,   22,   24,   24,   25,   25,   25,   25,
   25,   25,   23,   23,   23,   16,   17,   18,   18,
};
final static short yylen[] = {                            2,
    4,    2,    1,    1,    1,    3,    8,    1,    8,    1,
    1,    1,    3,    1,    3,    1,    1,    1,    1,    2,
    2,    1,    1,    5,    1,    1,    1,    1,    1,    4,
    1,    3,    1,    1,    1,    1,    1,    1,    1,    4,
    5,    8,   10,    3,    1,    3,    1,    1,    1,    1,
    1,    1,    1,    1,    3,    5,    4,    5,    5,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,   10,    0,    0,
   11,    0,    3,    4,    5,    0,   25,   26,   27,   28,
   29,    0,    0,    0,    0,    0,    0,   53,   54,    0,
    0,    1,    2,   14,    0,    0,    0,    0,   19,   18,
   17,   38,    0,   31,   39,    0,    0,    0,    0,    0,
    0,    0,    0,   22,    0,    0,    0,    0,    6,    0,
    0,   33,   34,   35,   36,   30,    0,    0,   50,   51,
   52,    0,   47,   48,   49,    0,    0,    0,   12,    0,
   21,   55,    0,    0,   57,    0,   13,    0,    0,    0,
    0,   32,    0,    0,    0,   59,   58,    0,    0,   56,
    0,    0,    0,   40,    0,    0,   16,   24,   20,    0,
   41,    0,    0,    0,    0,    0,   42,    0,    7,   15,
    0,    0,    9,   43,
};
final static short yydgoto[] = {                          2,
   52,   13,   14,   15,   16,   36,  106,   37,  102,   53,
   42,   29,   46,   17,   18,   19,   20,   21,   44,   67,
   45,   47,   30,   48,   76,
};
final static short yysindex[] = {                      -254,
 -255,    0, -153, -236,    9,   10, -200,    0, -210, -175,
    0, -194,    0,    0,    0, -184,    0,    0,    0,    0,
    0, -126, -126, -167, -174, -169,   49,    0,    0, -171,
   48,    0,    0,    0, -137,   62,   79,   96,    0,    0,
    0,    0,   92,    0,    0,  132,   97,  -32,  104,   99,
 -201, -169, -119,    0, -126, -126,   89,  110,    0, -101,
 -129,    0,    0,    0,    0,    0, -126, -106,    0,    0,
    0, -126,    0,    0,    0, -126,  106,  111,    0,   72,
    0,    0,  112,  113,    0, -201,    0,   96,    0, -126,
  119,    0, -210,  132,  134,    0,    0, -161,  117,    0,
  -77,  140,  126,    0, -215,  -23,    0,    0,    0,  -83,
    0,  124, -210,  125, -161, -169,    0,  -82,    0,    0,
  -80,  128,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0, -144,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  129,  -27,    0,    0,
    0,    0,    0,    0,    0,  -22,    0,    0,    0,    0,
    0,  -76,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  -41,  -34,    0,
    0,    0,    0,  -17,   -5,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
  186,   14,    0,    0,  -10,    0,    0,    0,    0,   75,
    3,  -21,   24,    0,    0,    0,    0,    0,  127,    0,
    0,  136,  -33,  120,    0,
};
final static int YYTABLESIZE=257;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         37,
   37,   37,    1,   37,   54,   37,   17,   17,   17,    3,
   17,   72,   17,   37,   37,   37,   37,   37,   45,   37,
  115,   45,   28,   46,   22,   33,   46,   73,   75,   74,
   81,   37,   37,   37,   37,   44,   45,   45,   45,   45,
   80,   46,   46,   46,   46,   43,    4,   50,   23,   24,
   90,  112,    5,   44,   26,   79,   25,    6,    7,  105,
   27,    8,    4,  113,    9,   33,   10,   11,    5,  114,
    8,   32,   34,    6,    7,  101,   11,    8,   83,  118,
    9,   31,   10,   11,   91,   35,   51,    4,   55,   38,
   39,   40,   49,    5,   54,   94,   39,   40,    6,    7,
  107,   27,    8,    4,   56,    9,   28,   10,   11,    5,
   41,   57,   12,  103,    6,    7,   41,  120,    8,   58,
   59,    9,   60,   10,   11,   12,   28,   88,   39,   40,
   38,   39,   40,   64,   62,   61,   63,   68,   65,   78,
   64,   62,    8,   63,   77,   65,   82,   85,   89,   86,
   66,   41,   99,   64,   62,   87,   63,   93,   65,  104,
   64,   62,   98,   63,   96,   65,  111,   64,   62,   97,
   63,  100,   65,   64,   62,  108,   63,   72,   65,  109,
  110,  116,  117,  119,  122,  123,  124,    8,   12,   23,
  121,   84,    0,   92,    0,   95,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   12,   12,   12,    0,    0,
    0,    0,   11,   11,   11,    0,    0,    0,    0,   69,
    0,    0,    0,    0,   37,    0,   12,    0,    0,   45,
   70,   71,    0,   11,   46,   37,   37,    0,    0,    0,
   45,   45,    0,    0,    0,   46,   46,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         41,
   42,   43,  257,   45,   26,   47,   41,   42,   43,  265,
   45,   44,   47,   41,   42,   43,   44,   45,   41,   47,
   44,   44,    9,   41,  261,   12,   44,   60,   61,   62,
   52,   59,   60,   61,   62,   41,   59,   60,   61,   62,
   51,   59,   60,   61,   62,   22,  257,   24,   40,   40,
   61,  267,  263,   59,  265,  257,  257,  268,  269,   93,
  271,  272,  257,  279,  275,   52,  277,  278,  263,   93,
  272,  266,  257,  268,  269,   86,  278,  272,   55,  113,
  275,  257,  277,  278,   61,  270,  261,  257,   40,  257,
  258,  259,  260,  263,  116,   72,  258,  259,  268,  269,
   98,  271,  272,  257,  276,  275,   93,  277,  278,  263,
  278,   64,  257,   90,  268,  269,  278,  115,  272,  257,
   59,  275,   44,  277,  278,  270,  113,  257,  258,  259,
  257,  258,  259,   42,   43,   40,   45,   41,   47,   41,
   42,   43,  272,   45,   41,   47,  266,   59,  278,   40,
   59,  278,   41,   42,   43,  257,   45,  264,   47,   41,
   42,   43,   91,   45,   59,   47,   41,   42,   43,   59,
   45,   59,   47,   42,   43,   59,   45,   44,   47,  257,
   41,  265,   59,   59,  267,  266,   59,   59,    3,  266,
  116,   56,   -1,   67,   -1,   76,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,  257,  258,  259,   -1,   -1,
   -1,   -1,  257,  258,  259,   -1,   -1,   -1,   -1,  262,
   -1,   -1,   -1,   -1,  262,   -1,  278,   -1,   -1,  262,
  273,  274,   -1,  278,  262,  273,  274,   -1,   -1,   -1,
  273,  274,   -1,   -1,   -1,  273,  274,
};
}
final static short YYFINAL=2;
final static short YYMAXTOKEN=279;
final static String yyname[] = {
"end-of-file",null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,"'('","')'","'*'","'+'","','",
"'-'",null,"'/'",null,null,null,null,null,null,null,null,null,null,null,"';'",
"'<'","'='","'>'",null,"'@'",null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
"'['",null,"']'",null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,"IDENTIFICADOR","HEXADECIMAL","FLOAT",
"CADENA_MULTI","SIMASIGNACION","DISTINTO","IF","THEN","BEGIN","END","END_IF",
"OUTF","TYPEDEF","FUN","RET","SINGLE","MENOR_IGUAL","MAYOR_IGUAL","REPEAT",
"WHILE","GOTO","LONGINT","ELSE",
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
"declaracion : listaVariable",
"declaracion : FUN IDENTIFICADOR '(' parametro ')' BEGIN cuerpoFuncion END",
"tipoDato : SINGLE",
"tipoDato : LONGINT",
"tipoDato : IDENTIFICADOR",
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
"expresion : operando",
"expresion : expresion operador operando",
"operador : '+'",
"operador : '-'",
"operador : '*'",
"operador : '/'",
"operando : IDENTIFICADOR",
"operando : Constante",
"operando : invocacionFuncion",
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

//#line 124 "C:\Users\pipig\Documents\GitHub\compiler_in_java\src\gramatica.y"
//FUNCIONES
private static AnalizadorLexico lex;

public static void main(String[] args) {
    String filePath = "src/MATRIZ DE TRANSICIONES - Hoja 1.csv";

    int[][] matriz = MatrizTransicion.leerMatrizDesdeCSV(filePath);
    filePath = "src/MATRIZ DE TRANSICIONES - Hoja 2.csv";

    Accion[][] matrizAcciones = MatrizAccion.leerMatrizDesdeCSV(filePath);
    System.out.println(matrizAcciones.length);
    Parser.lex = new AnalizadorLexico("codigoFuente.txt", matriz, matrizAcciones);
    if (args.length > -1) {
            //String archivo_a_leer = args[0];
            Parser parser = new Parser(true);
            parser.run();
    } else {
            System.out.println("No se especifico el archivo a compilar");
    }
    System.out.println(TablaDeSimbolos.imprimir());
}
private int yylex(){
  int idToken = -1;
  if (!lex.end()){
    idToken = lex.getNextToken(yyval);
    System.out.println(idToken);
  }
  System.out.println(yyval.toString());
  return idToken;
}
private void yyerror(String string) {
  throw new UnsupportedOperationException("ERROR");
}
//#line 366 "Parser.java"
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
