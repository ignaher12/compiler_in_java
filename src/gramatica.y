//DECLARACIONES
//NUMERO DE TOKEN DE CARACTER ASCII (0-256)
//MAYOR MENOR IGUAL SUMA RESTA DIVISION MULTIPLICACION PUNTO PUNTO_COMA PARENTESIS_I PARENTESIS_D

// NUMERO DE TOKEN DE DEFINIDOS (257-...)
%token IDENTIFICADOR HEXADECIMAL FLOAT CADENA_MULTI SIMASIGNACION DISTINTO IF THEN BEGIN
       END END_IF OUTF TYPEDEF FUN RET SINGLE MENOR_IGUAL MAYOR_IGUAL REPEAT WHILE GOTO LONGINT ELSE
       TRIPLE
       
%start programa

%left '+' '-'
%left '*' '/'

%%
//REGLAS GRAMATICALES
//no terminal : DEFINICION('caracter') {accion}
//            ;

programa : IDENTIFICADOR BEGIN cuerpo END
;

cuerpo : cuerpo sentencia
       | sentencia
;

sentencia : sentenciaDeclarativa
          | sentenciaEjecutable 

sentenciaDeclarativa : tipoDato declaracion ';'
                     | TYPEDEF IDENTIFICADOR SIMASIGNACION tipoDato '[' listaConstante ']' ';'  // TEMA 11
                     | TYPEDEF TRIPLE '<' tipoDato '>' IDENTIFICADOR ';'  // TEMA 22
;

declaracion : listaVariable
            | FUN IDENTIFICADOR '(' parametro ')' BEGIN cuerpoFuncion END
;

tipoDato : SINGLE 
         | LONGINT
         | IDENTIFICADOR //PARA typedef  // TEMA 11
         | TRIPLE  // TEMA 22
;

listaVariable : listaVariable ',' IDENTIFICADOR
              | IDENTIFICADOR
;

listaConstante : listaConstante ',' constante    // TEMA 11
               | constante                       // TEMA 11
;

constante : cte
          | '-' cte
;

cte : LONGINT 
    | FLOAT 
    | HEXADECIMAL 

parametro : tipoDato IDENTIFICADOR
;

cuerpoFuncion : cuerpo sentenciaRet     //PUEDE NO TENER RET LA FUNCION PERSE?
              | sentenciaRet
              | cuerpo
;

//IF EN FUNCIONES 
sentenciaRet : RET '(' expresion ')' ';'
;
 

sentenciaEjecutable : asignacion
                    | clausulaSeleccion
                    | clausulaBucle
                    | goto              //TEMA 23
                    | mensajeSalida
;

asignacion : IDENTIFICADOR SIMASIGNACION expresion ';'
           | IDENTIFICADOR '[' '1' ']' SIMASIGNACION expresion ';'  //TEMA 22
           | IDENTIFICADOR '[' '2' ']' SIMASIGNACION expresion ';'  //TEMA 22
           | IDENTIFICADOR '[' '3' ']' SIMASIGNACION expresion ';'  //TEMA 22
;

expresion : operando 
          | expresion operador operando 

operador : '+' | '-' | '*' | '/'
;

operando : IDENTIFICADOR
         | constante
         | invocacionFuncion
         | IDENTIFICADOR '[' '1' ']' //TEMA 22
         | IDENTIFICADOR '[' '2' ']' //TEMA 22
         | IDENTIFICADOR '[' '3' ']'  //TEMA 22
;

invocacionFuncion : IDENTIFICADOR '(' expresion ')'
                  | IDENTIFICADOR '(' tipoDato expresion ')' //TEMA 27
;

clausulaSeleccion : IF '(' condicion ')' THEN bloqueIF END_IF ';'
                  | IF '(' condicion ')' THEN bloqueIF ELSE bloqueIF END_IF ';'
;

condicion : listaExpresion comparador listaExpresion        
;

listaExpresion : expresion          //TEMA 19
               | listaExpresion ',' expresion //TEMA 19
;

comparador : '<' | '>' | '=' | DISTINTO | MENOR_IGUAL | MAYOR_IGUAL 
;

bloqueIF : sentencia        //RENOMBRE A BLOQUECONTROL
         | sentenciaRet
         | BEGIN cuerpoFuncion END
;

clausulaBucle : REPEAT bloqueIF WHILE condicion ';'
;

goto : GOTO IDENTIFICADOR '@' ';'
;

mensajeSalida : OUTF '(' expresion ')' ';'
              | OUTF '(' CADENA_MULTI ')' ';'
;


%%
//FUNCIONES
private static AnalizadorLexico lex;
public static List<Error> erroresLexico = new ArrayList<Error>();
public static void main(String[] args) {
    String filePath = "src/MATRIZ DE TRANSICIONES - Hoja 1.csv";

    int[][] matriz = MatrizTransicion.leerMatrizDesdeCSV(filePath);
    filePath = "src/MATRIZ DE TRANSICIONES - Hoja 2.csv";

    Accion[][] matrizAcciones = MatrizAccion.leerMatrizDesdeCSV(filePath);
    Parser parser = new Parser();
    Parser.lex = new AnalizadorLexico("codigoFuente.txt", matriz, matrizAcciones);
    if (args.length > 1) {
        Parser.lex = new AnalizadorLexico(args[0], matriz, matrizAcciones);

        parser.run();
        for (Error error: erroresLexico){System.out.println(error);}
    } else {
        Parser.lex = new AnalizadorLexico("CP1.txt", matriz, matrizAcciones);
        
        parser.run();
        for (Error error: erroresLexico){System.out.println(error);}
        //System.out.println("No se especifico el archivo a compilar");
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