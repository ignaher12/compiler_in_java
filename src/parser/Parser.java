package parser;
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

import lexico.AnalizadorLexico;
import lexico.AccionesSemanticas.Accion;
import utils.MatrizAccion;
import utils.MatrizTransicion;

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
public final static short CONSTANTE=258;
public final static short HEXADECIMAL=259;
public final static short FLOAT=260;
public final static short CADENA_MULTI=261;
public final static short SIMASIGNACION=262;
public final static short DISTINTO=263;
public final static short IF=264;
public final static short THEN=265;
public final static short BEGIN=266;
public final static short END=267;
public final static short END_IF=268;
public final static short OUTF=269;
public final static short TYPEDEF=270;
public final static short FUN=271;
public final static short RET=272;
public final static short SINGLE=273;
public final static short MENOR_IGUAL=274;
public final static short MAYOR_IGUAL=275;
public final static short REPEAT=276;
public final static short WHILE=277;
public final static short GOTO=278;
public final static short LONGINT=279;
public final static short ELSE=280;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    1,    1,    2,    2,    3,    3,    6,    6,    5,
    5,    5,    8,    8,    7,    7,    9,   10,   10,   11,
    4,    4,    4,    4,    4,   12,   17,   17,   19,   19,
   19,   19,   18,   18,   18,   20,   20,   13,   13,   21,
   23,   23,   24,   24,   24,   24,   24,   24,   22,   22,
   14,   15,   16,   16,
};
final static short yylen[] = {                            2,
    4,    1,    2,    2,    2,    2,    7,    1,    8,    1,
    1,    1,    3,    1,    3,    1,    2,    1,    2,    1,
    1,    1,    1,    1,    1,    3,    1,    3,    1,    1,
    1,    1,    1,    1,    1,    4,    5,    7,    9,    3,
    1,    3,    1,    1,    1,    1,    1,    1,    1,    3,
    4,    3,    4,    4,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,   10,    0,    0,
   11,    0,    2,    0,    0,    0,   21,   22,   23,   24,
   25,    0,    0,    0,    0,    0,   49,    0,    0,    1,
    3,    4,    5,   14,    0,    6,    0,    0,   34,    0,
   27,   35,    0,    0,    0,    0,    0,    0,    0,    0,
   52,    0,    0,    0,   29,   30,   31,   32,    0,    0,
   46,   47,   48,    0,   43,   44,   45,    0,   54,   53,
   12,    0,   50,   51,    0,   13,    0,    0,    0,   28,
    0,    0,    0,    0,    0,    0,    0,   36,    0,   16,
    0,   17,    0,   37,   38,    0,    7,    0,    0,    0,
   15,   20,    0,   18,   39,    9,   19,
};
final static short yydgoto[] = {                          2,
   12,   27,   14,   15,   16,   36,   91,   37,   86,  103,
  104,   17,   18,   19,   20,   21,   43,   41,   59,   42,
   44,   28,   45,   68,
};
final static short yysindex[] = {                      -240,
 -218,    0, -170, -212,   37,   42, -172,    0, -215, -164,
    0, -198,    0,   53,   58, -211,    0,    0,    0,    0,
    0, -243, -243, -115, -128, -170,    0, -139,   76,    0,
    0,    0,    0,    0, -113,    0,  101,  107,    0,   94,
    0,    0,   94,  108,  -28,  109,   73, -183, -181, -243,
    0,  111, -109, -147,    0,    0,    0,    0, -243, -112,
    0,    0,    0, -243,    0,    0,    0, -243,    0,    0,
    0,   61,    0,    0, -183,    0,  107, -243,   80,    0,
 -215,   94,  110, -103, -101,  116,   88,    0, -228,    0,
  -37,    0, -108,    0,    0, -215,    0,  -99, -200, -107,
    0,    0, -166,    0,    0,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0, -152,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  -49,  -41,    0,  103,
    0,    0,  -36,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   -6,    0,    0,    0,
    0,  -32,   43,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
  134,   19,   25,    0,  -10,    0,    0,    0,    0,    0,
   60,    0,    0,    0,    0,    0,  -11,  105,    0,    0,
  115,  -34,   98,    0,
};
final static int YYTABLESIZE=252;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         33,
   33,   33,   33,   33,   41,   33,   98,   41,   42,    8,
   40,   42,   47,   38,   39,   64,    1,   33,   33,   33,
   33,   13,   41,   41,   41,   41,   42,   42,   42,   42,
   31,   65,   67,   66,   33,   33,   33,   72,   33,   95,
   33,    4,   79,   78,   13,   34,   89,    3,    5,   22,
   26,   96,   82,    6,    7,   97,   71,    8,    4,   35,
    9,  100,   10,   11,   85,    5,   87,   31,   30,    7,
    6,    7,    8,   71,    8,    4,   23,    9,   11,   10,
   11,   24,    5,   40,   25,   73,    4,    6,    7,    8,
   71,    8,   29,    5,    9,   11,   10,   11,    6,    7,
  106,   40,    8,    7,   12,    9,    8,   10,   11,   77,
   39,   32,   11,   70,   57,   55,   33,   56,   12,   58,
   88,   57,   55,  102,   56,    8,   58,  102,   94,   57,
   55,   11,   56,   48,   58,   57,   55,   50,   56,   51,
   58,   38,   39,   52,   53,   46,   54,   76,   60,   69,
   75,   84,   81,   64,   90,   92,   93,   99,  101,   49,
  105,   26,  107,   80,   74,   83,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    8,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    8,    0,    0,
    8,   33,    0,    8,    0,    0,   41,    0,    0,    8,
   42,    0,   33,   33,   61,    0,    0,   41,   41,    0,
    0,   42,   42,    0,    0,   62,   63,    0,    0,    0,
   12,   12,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         41,
   42,   43,   44,   45,   41,   47,   44,   44,   41,   59,
   22,   44,   24,  257,  258,   44,  257,   59,   60,   61,
   62,    3,   59,   60,   61,   62,   59,   60,   61,   62,
   12,   60,   61,   62,   41,   42,   43,   48,   45,  268,
   47,  257,   54,   54,   26,  257,   81,  266,  264,  262,
  266,  280,   64,  269,  270,   93,  257,  273,  257,  271,
  276,   96,  278,  279,   75,  264,   78,   49,  267,  270,
  269,  270,  273,  257,  273,  257,   40,  276,  279,  278,
  279,   40,  264,   41,  257,  267,  257,  269,  270,  273,
  257,  273,  257,  264,  276,  279,  278,  279,  269,  270,
  267,   59,  273,  270,  257,  276,  273,  278,  279,  257,
  258,   59,  279,   41,   42,   43,   59,   45,  271,   47,
   41,   42,   43,   99,   45,  273,   47,  103,   41,   42,
   43,  279,   45,  262,   47,   42,   43,  277,   45,   64,
   47,  257,  258,  257,   44,  261,   40,  257,   41,   41,
   40,   91,  265,   44,  258,  257,   41,  266,  258,   26,
  268,   59,  103,   59,   50,   68,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,  257,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,  267,   -1,   -1,
  270,  263,   -1,  273,   -1,   -1,  263,   -1,   -1,  279,
  263,   -1,  274,  275,  263,   -1,   -1,  274,  275,   -1,
   -1,  274,  275,   -1,   -1,  274,  275,   -1,   -1,   -1,
  257,  258,
};
}
final static short YYFINAL=2;
final static short YYMAXTOKEN=280;
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
null,null,null,null,null,null,null,"IDENTIFICADOR","CONSTANTE","HEXADECIMAL",
"FLOAT","CADENA_MULTI","SIMASIGNACION","DISTINTO","IF","THEN","BEGIN","END",
"END_IF","OUTF","TYPEDEF","FUN","RET","SINGLE","MENOR_IGUAL","MAYOR_IGUAL",
"REPEAT","WHILE","GOTO","LONGINT","ELSE",
};
final static String yyrule[] = {
"$accept : programa",
"programa : IDENTIFICADOR BEGIN cuerpo END",
"cuerpo : sentencia",
"cuerpo : cuerpo sentencia",
"sentencia : sentenciaDeclarativa ';'",
"sentencia : sentenciaEjecutable ';'",
"sentenciaDeclarativa : tipoDato declaracion",
"sentenciaDeclarativa : TYPEDEF IDENTIFICADOR SIMASIGNACION tipoDato '[' listaConstante ']'",
"declaracion : listaVariable",
"declaracion : FUN IDENTIFICADOR '(' parametro ')' BEGIN cuerpoDeLaFuncion END",
"tipoDato : SINGLE",
"tipoDato : LONGINT",
"tipoDato : IDENTIFICADOR",
"listaVariable : listaVariable ',' IDENTIFICADOR",
"listaVariable : IDENTIFICADOR",
"listaConstante : listaConstante ',' CONSTANTE",
"listaConstante : CONSTANTE",
"parametro : tipoDato IDENTIFICADOR",
"cuerpoDeLaFuncion : sentenciaFuncion",
"cuerpoDeLaFuncion : cuerpoDeLaFuncion sentenciaFuncion",
"sentenciaFuncion : sentenciaDeclarativa",
"sentenciaEjecutable : asignacion",
"sentenciaEjecutable : clausulaSeleccion",
"sentenciaEjecutable : clausulaBucle",
"sentenciaEjecutable : goto",
"sentenciaEjecutable : mensajeSalida",
"asignacion : IDENTIFICADOR SIMASIGNACION expresion",
"expresion : operando",
"expresion : expresion operador operando",
"operador : '+'",
"operador : '-'",
"operador : '*'",
"operador : '/'",
"operando : IDENTIFICADOR",
"operando : CONSTANTE",
"operando : invocacionFuncion",
"invocacionFuncion : IDENTIFICADOR '(' expresion ')'",
"invocacionFuncion : IDENTIFICADOR '(' tipoDato expresion ')'",
"clausulaSeleccion : IF '(' condicion ')' THEN bloqueIF END_IF",
"clausulaSeleccion : IF '(' condicion ')' THEN bloqueIF ELSE bloqueIF END_IF",
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
"bloqueIF : BEGIN cuerpo END",
"clausulaBucle : REPEAT bloqueIF WHILE condicion",
"goto : GOTO IDENTIFICADOR '@'",
"mensajeSalida : OUTF '(' expresion ')'",
"mensajeSalida : OUTF '(' CADENA_MULTI ')'",
};

//#line 122 "C:\Users\pipig\Documents\GitHub\compiler_in_java\src\gramatica.y"
//FUNCIONES
private static AnalizadorLexico lex;
public static void main(String[] args) {
    String filePath = "src/MATRIZ DE TRANSICIONES - Hoja 1.csv";

    int[][] matriz = MatrizTransicion.leerMatrizDesdeCSV(filePath);
    filePath = "src/MATRIZ DE TRANSICIONES - Hoja 2.csv";

    Accion[][] matrizAcciones = MatrizAccion.leerMatrizDesdeCSV(filePath);
    
    Parser.lex = new AnalizadorLexico("codigoFuente.txt", matriz, matrizAcciones);
    if (args.length > -1) {
            //String archivo_a_leer = args[0];
            Parser parser = new Parser(true);
            parser.run();
    } else {
            System.out.println("No se especifico el archivo a compilar");
    }
}
private int yylex(){
  int idToken = -1;
  if (!lex.end()){
    idToken = lex.getNextToken(yyval);
    System.out.println(idToken);
  }
  System.out.println("hola");
  System.out.println(yyval.toString());
  return idToken;
}

void yyerror(String mensaje) {
  // funcion utilizada para imprimir errores que produce yacc
  System.out.println("Error yacc: " + mensaje);
}
//#line 346 "Parser.java"
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
