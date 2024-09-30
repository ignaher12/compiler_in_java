%{
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
%} 
//DECLARACIONES
//NUMERO DE TOKEN DE CARACTER ASCII (0-256)
//MAYOR MENOR IGUAL SUMA RESTA DIVISION MULTIPLICACION PUNTO PUNTO_COMA PARENTESIS_I PARENTESIS_D

// NUMERO DE TOKEN DE DEFINIDOS (257-...)
%token IDENTIFICADOR HEXADECIMAL CADENA_MULTI SIMASIGNACION DISTINTO IF THEN BEGIN
       END END_IF OUTF TYPEDEF FUN RET SINGLE MENOR_IGUAL MAYOR_IGUAL REPEAT WHILE GOTO LONGINT ELSE
       TRIPLE CONSTANTE
       
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

sentenciaDeclarativa : tipoDato listaVariable ';' {estructuras.add("Declaracion");}
                     | typedefDeclaracion ';' {estructuras.add("Declaracion de typedef");}
                     | tipoDato funDeclaracion ';' {estructuras.add("Declaracion de funcion");}

typedefDeclaracion : TYPEDEF IDENTIFICADOR SIMASIGNACION tipoDato '[' listaConstante ']'  // TEMA 11
                   | TYPEDEF TRIPLE '<' tipoDato '>' IDENTIFICADOR  // TEMA 22
                   | TYPEDEF TRIPLE '<' IDENTIFICADOR '>' IDENTIFICADOR  // TEMA 22 //CHECK
;

funDeclaracion : FUN IDENTIFICADOR '(' parametro ')' BEGIN cuerpoFuncion END
;


tipoDato : SINGLE 
         | LONGINT
         | HEXADECIMAL
         //| IDENTIFICADOR //PARA typedef  // TEMA 11 ..check
         | TRIPLE  // TEMA 22
;

listaVariable : listaVariable ',' IDENTIFICADOR
              | IDENTIFICADOR
;

listaConstante : listaConstante ',' constante    // TEMA 11
               | constante                       // TEMA 11
;

constante : CONSTANTE { Lexema lex = TablaDeSimbolos.getByID($1.ival);
                        System.out.println($1.ival);
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
          | '-' CONSTANTE {
                            Lexema lex = TablaDeSimbolos.getByID($2.ival);
                            int newLexRef = TablaDeSimbolos.agregarSimbolo("-"+lex.getAtributo(), lex.getTipo());
                            //$2.sval = newLex.
                          }
;


parametro : tipoDato IDENTIFICADOR
;

cuerpoFuncion : cuerpo sentenciaRet     //PUEDE NO TENER RET LA FUNCION PERSE?
              | sentenciaRet
              | cuerpo
;

//IF EN FUNCIONES 
sentenciaRet : RET '(' expresion ')' ';' {estructuras.add("Retorno");}
;
 

sentenciaEjecutable : asignacion {estructuras.add("Asignacion");}
                    | clausulaSeleccion {estructuras.add("IF");}
                    | clausulaBucle {estructuras.add("WHILE");}
                    | goto {estructuras.add("GOTO");}              //TEMA 23
                    | mensajeSalida {estructuras.add("OUTF");} 
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
        Parser.lex = new AnalizadorLexico("CP3.txt", matriz, matrizAcciones);
        
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