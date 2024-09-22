//DECLARACIONES
//NUMERO DE TOKEN DE CARACTER ASCII (0-256)
//MAYOR MENOR IGUAL SUMA RESTA DIVISION MULTIPLICACION PUNTO PUNTO_COMA PARENTESIS_I PARENTESIS_D

// NUMERO DE TOKEN DE DEFINIDOS (257-...)
%token IDENTIFICADOR HEXADECIMAL FLOAT CADENA_MULTI SIMASIGNACION DISTINTO IF THEN BEGIN
       END END_IF OUTF TYPEDEF FUN RET SINGLE MENOR_IGUAL MAYOR_IGUAL REPEAT WHILE GOTO LONGINT ELSE

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
;

declaracion : listaVariable
            | FUN IDENTIFICADOR '(' parametro ')' BEGIN cuerpoFuncion END
;

tipoDato : SINGLE 
         | LONGINT
         | IDENTIFICADOR //PARA typedef  // TEMA 11
;

listaVariable : listaVariable ',' IDENTIFICADOR
              | IDENTIFICADOR
;

listaConstante : listaConstante ',' Constante    // TEMA 11
               | Constante                       // TEMA 11
;

Constante : LONGINT
          | FLOAT
          | HEXADECIMAL
;

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
;

expresion : operando 
          | expresion operador operando 

operador : '+' | '-' | '*' | '/'
;

operando : IDENTIFICADOR
         | Constante
         | invocacionFuncion
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
