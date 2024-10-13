%{

import lexico.AnalizadorLexico;
import lexico.TablaDeSimbolos;
import lexico.TablaTipoToken;
import lexico.AccionesSemanticas.Accion;
import parser.Error.Tipo;
import utils.MatrizAccion;
import utils.MatrizTransicion;
import java.util.ArrayList;
import java.util.List;
import java.util.HexFormat;
import lexico.TablaDeSimbolos.Contexto;
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

programa  : IDENTIFICADOR BEGIN cuerpo END
          | IDENTIFICADOR BEGIN END {erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta 'cuerpo'."));}
          | IDENTIFICADOR BEGIN cuerpo {erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta 'END al final del programa'."));}
          | IDENTIFICADOR error cuerpo END {erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta BEGIN del programa."));}
          | BEGIN cuerpo END {erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta 'nombre del programa'."));}
          | IDENTIFICADOR error cuerpo error {erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO delimitadores de programa."));}

;

cuerpo : cuerpo sentencia
       | sentencia
;

sentencia : sentenciaDeclarativa 
          | sentenciaEjecutable  ';'
          | etiqueta
          | sentenciaEjecutable error {erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta ';'."));}
          | error ';' {erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta sentencia."));}
          | sentenciaRet ';'{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO no se puede retornar en el cuerpo del programa."));}
          //| sentenciaDeclarativa error {erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta ';'."));}
;

sentenciaDeclarativa : tipoDato IDENTIFICADOR  ';'                      {ArrayList<String> referenciasIden = new ArrayList<String>(); referenciasIden.add($2.sval); declararVariable($1.sval, referenciasIden); estructuras.add("Linea "+ TablaDeSimbolos.getContexto($1.sval).popRef() +": "+"Declaracion");}
                     | tipoDato IDENTIFICADOR ',' listaVariable ';'     {Contexto contexto = TablaDeSimbolos.getContexto($1.sval);estructuras.add("Linea "+ contexto.popRef() +": "+"Declaracion");}
                     | tipoDato IDENTIFICADOR ',' error ';' {Contexto contexto = TablaDeSimbolos.getContexto($1.sval);erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera IDENTIFICADOR.")); estructuras.add("Linea "+ contexto.popRef() +": "+"Declaracion");}
                     | tipoDato IDENTIFICADOR ',' listaVariable error ';' {Contexto contexto = TablaDeSimbolos.getContexto($1.sval);erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta ','.")); estructuras.add("Linea "+ contexto.popRef() +": "+"Declaracion");}
                     | tipoDato IDENTIFICADOR error ';' {erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta ','.")); }    
                     | typedefDeclaracion ';'
                     | typedefDeclaracion error {erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera ';'.")); }
                     | tipoDato funDeclaracion ';'  {estructuras.add("Declaracion de funcion");}
                     //| tipoDato error {erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera 'objeto a declarar'."));}

;

typedefDeclaracion : TYPEDEF declaracionSubtipo {Integer lastRef = TablaDeSimbolos.getContexto($1.sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"Declaracion de subtipo");}
                   | TYPEDEF declaracionTriple {Integer lastRef = TablaDeSimbolos.getContexto($1.sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"Declaracion de triple");}
                   | declaracionSubtipo{ erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera 'TYPEDEF antes de la declaracion del subtipo'.")); estructuras.add("Linea "+ ": "+"Declaracion de subtipo");}
                   | error declaracionTriple { erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera 'TYPEDEF antes de la declaracion del triple'.")); estructuras.add("Linea "+ ": "+"Declaracion de triple");}
                   | TYPEDEF error { erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera 'declaracion de subtipo o triple'.")); Integer lastRef = TablaDeSimbolos.getContexto($1.sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"Declaracion de subtipo"); }
;

declaracionSubtipo : IDENTIFICADOR SIMASIGNACION tipoDato '{' listaConstante '}'  {declaracionSubtipo($3.sval,$1.sval);}// TEMA 11
                   | SIMASIGNACION tipoDato '{' listaConstante '}'  {erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera 'nombre del tipo de dato nuevo'."));}
                   | IDENTIFICADOR SIMASIGNACION tipoDato '{' '}' {erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera declaracion de subrangos."));}
;
declaracionTriple : TRIPLE '<' tipoDato '>' IDENTIFICADOR {declaracionTriple($3.sval,$5.sval);}  // TEMA 22
                  |  TRIPLE '<' IDENTIFICADOR '>' IDENTIFICADOR  // TEMA 22 //CHECK
                  |  TRIPLE '<' error '>' IDENTIFICADOR {erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera 'tipo de objeto a declarar'."));}
                  |  TRIPLE '<' tipoDato '>' error {erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera 'nombre de objeto a declarar'."));}
                  |  TRIPLE '<' IDENTIFICADOR '>' error {erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera 'nombre de objeto a declarar'."));}
                  |  TRIPLE '<' error '>' error {erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera 'nombre y tipo de objeto a declarar'."));}
                  |  TRIPLE IDENTIFICADOR '>' IDENTIFICADOR {erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta '<' al inicio del identificador'."));} // TEMA 22
                  |  TRIPLE '<' IDENTIFICADOR IDENTIFICADOR {erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta '>' al final del identificador'."));} // TEMA 22
                  |  TRIPLE IDENTIFICADOR IDENTIFICADOR {erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO faltan '<>'."));} // TEMA 22
                  |  TRIPLE tipoDato IDENTIFICADOR {erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO faltan '<>'."));} // TEMA 22
;

funDeclaracion : funComienzo '(' parametro ')' BEGIN cuerpoFuncion END {if ($7.sval.equals("false"))erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta return en el cuerpo de la funcion.")); Integer lastRef = TablaDeSimbolos.getContexto($1.sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"Declaracion de funcion"); ambitos.remove(ambitos.size()-1);}
               | FUN error '(' parametro ')' BEGIN cuerpoFuncion END  {erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta nombre de la funcion."));  Integer lastRef = TablaDeSimbolos.getContexto($1.sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"Declaracion de funcion");}
               | funComienzo '(' parametro ','  error ')' BEGIN cuerpoFuncion END  {erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO  no puede tener mas de un parametro."));  Integer lastRef = TablaDeSimbolos.getContexto($1.sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"Declaracion de funcion");}
               | funComienzo '(' parametro ')' BEGIN error END  {erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta cuerpo con retorno."));  Integer lastRef = TablaDeSimbolos.getContexto($1.sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"Declaracion de funcion");}
;

funComienzo : FUN IDENTIFICADOR  {declararFuncion($2.sval); ambitos.add($2.sval);} //PROBLEMA CON FUN error de fundeclaracion??
;
tipoDato : SINGLE      
         | LONGINT     
         | HEXADECIMAL 
;

listaVariable : listaVariable ',' IDENTIFICADOR
              | listaVariable ',' error {erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta identificador despues de la coma."));}
              | IDENTIFICADOR
;
               

listaConstante : listaConstante ',' constante    // TEMA 11
               | constante                       // TEMA 11
;

constante : CONSTANTE { Contexto contexto = TablaDeSimbolos.getContexto($1.sval);
                        chequearRango(contexto);                             //SOLO SE CHEQUEA EN POSITIVO YA QUE EL MAXIMO DE NEGATIVOS ES MAYOR AL MAXIMO DE POSITIVOS Y YA LO CHEQUEA EL PARSER
                      }
          | '-' CONSTANTE {
                            Contexto contexto = TablaDeSimbolos.getContexto($2.sval);
                            String newLexRef = TablaDeSimbolos.agregarSimbolo("-"+val_peek(0).sval, contexto.getTipo(), "-"+val_peek(0).sval, AnalizadorLexico.getNumeroLinea());
                          }
;


parametro : tipoDato IDENTIFICADOR   {declaracionParametro($1.sval, $2.sval);}
          | error IDENTIFICADOR      {erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta tipo de dato en el parametro."));}
          | tipoDato error      {erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta nombre en el parametro."));}
;


cuerpoFuncion : cuerpoFuncion sentenciaConRet   { if ($1.sval.equals("true") || $2.sval.equals("true")) $$.sval = "true"; else $$.sval = "false";}
              | sentenciaConRet                 {$$.sval = $1.sval;}
;

sentenciaConRet : sentenciaDeclarativa       {$$.sval = "false";}
                | sentenciaEjecutableConRet  ';' {$$.sval = $1.sval;}
                | etiqueta                       {$$.sval = "false";}
                | sentenciaEjecutableConRet {erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta ';'." )); $$.sval = $1.sval;}
;

//IF EN FUNCIONES
sentenciaRet : RET '(' expresion ')'  {estructuras.add("Retorno"); Integer lastRef = TablaDeSimbolos.getContexto($1.sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"Sentencia de Retorno");}
;


sentenciaEjecutable : asignacion 
                    | clausulaSeleccion 
                    | clausulaBucle 
                    | goto               //TEMA 23
                    | mensajeSalida 
;
sentenciaEjecutableConRet : asignacion                 {$$.sval = "false";}
                          | clausulaBucle              {$$.sval = "false";}
                          | goto                       {$$.sval = "false";}//TEMA 23
                          | mensajeSalida              {$$.sval = "false";}
                          | sentenciaRet               {$$.sval = "true";}
                          | clausulaSeleccionConRet    {$$.sval = $1.sval;}
;

asignacion : IDENTIFICADOR SIMASIGNACION expresion {Integer lastRef = TablaDeSimbolos.getContexto($1.sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"Sentencia de Asignacion");}
           | IDENTIFICADOR CADENA_MULTI SIMASIGNACION expresion   { if (!$2.sval.equals("[1]") && !$2.sval.equals("[2]") && !$2.sval.equals("[3]")) erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO rango invalido, se espera entre 1 y 3."));Integer lastRef = TablaDeSimbolos.getContexto($1.sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"Sentencia de Asignacion");}//TEMA 22
           | IDENTIFICADOR CONSTANTE SIMASIGNACION expresion   {erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO faltan '[]' en el rango")); Integer lastRef = TablaDeSimbolos.getContexto($1.sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"Sentencia de Asignacion");}
;

expresion : expresion operador operando
          | expresion operador error {erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta operando en la expresion"));}
          | error operador operando {erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta operando en la expresion"));}
          | operando
;

operador : '+' | '-' | '*' | '/'
;

operando : IDENTIFICADOR    //SE TIENE QUE CHEQUEAR QUE EL IDENTIFICADOR NO SEA UN TRIPLE YA QUE FALTARIA EL RANGO
         | constante
         | invocacionFuncion
         | IDENTIFICADOR CADENA_MULTI //TEMA 22  SE TIENE QUE CHEQUEAR QUE EL VALOR DE LA CADENAMULTI ESTA ENTRE 1 Y 3
         | IDENTIFICADOR  '1'  //TEMA 22  {erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO faltan '[]' en el rango"));}
         | IDENTIFICADOR  '2'  //TEMA 22  {erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO faltan '[]' en el rango"));}
         | IDENTIFICADOR  '3'   //TEMA 22 {erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO faltan '[]' en el rango"));}
;

invocacionFuncion : IDENTIFICADOR '(' expresion ')'       { Integer lastRef = TablaDeSimbolos.getContexto($1.sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"Invocacion de Funcion");}
                  | IDENTIFICADOR '(' tipoDato expresion ')' { Integer lastRef = TablaDeSimbolos.getContexto($1.sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"Invocacion de Funcion");}//TEMA 27
                  | IDENTIFICADOR '(' tipoDato '(' expresion ')' ')' { Integer lastRef = TablaDeSimbolos.getContexto($1.sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"Invocacion de Funcion");}//TEMA 27
                  | IDENTIFICADOR '(' expresion ',' error ')'                  {erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO numero de expresiones invalido en el llamado a funcion.")); Integer lastRef = TablaDeSimbolos.getContexto($1.sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"Invocacion de Funcion");} //TEMA 27
                  | IDENTIFICADOR '(' error ')'                                         {erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta expresion en el llamado a funcion.")); Integer lastRef = TablaDeSimbolos.getContexto($1.sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"Invocacion de Funcion");}
                  | IDENTIFICADOR '(' tipoDato expresion ',' error ')'         {erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO numero de expresiones invalido en el llamado a funcion.")); Integer lastRef = TablaDeSimbolos.getContexto($1.sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"Invocacion de Funcion");}//TEMA 27
                  | IDENTIFICADOR '(' tipoDato '(' expresion ')' ',' error')'  {erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO numero de expresion esinvalido en el llamado a funcion.")); Integer lastRef = TablaDeSimbolos.getContexto($1.sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"Invocacion de Funcion");}//TEMA 27
;

clausulaSeleccion : IF '(' condicion ')' THEN bloqueSentenciaEjecutable END_IF {Integer lastRef = TablaDeSimbolos.getContexto($1.sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"IF");}
                  | IF '(' expresion ')' THEN bloqueSentenciaEjecutable END_IF {erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera una condicion valida")); Integer lastRef = TablaDeSimbolos.getContexto($1.sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"IF");}
                  | IF '(' expresion ')' THEN bloqueSentenciaEjecutable ELSE bloqueSentenciaEjecutable END_IF {erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera una condicion valida")); Integer lastRef = TablaDeSimbolos.getContexto($1.sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"IF");;}                  
                  | IF '(' condicion ')' THEN bloqueSentenciaEjecutable ELSE bloqueSentenciaEjecutable END_IF { Integer lastRef = TablaDeSimbolos.getContexto($1.sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"IF");}
                  | IF '(' condicion ')' THEN bloqueSentenciaEjecutable error {erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta 'end_if;'.")); Integer lastRef = TablaDeSimbolos.getContexto($1.sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"IF");}
                  | IF '(' condicion ')' THEN bloqueSentenciaEjecutable ELSE bloqueSentenciaEjecutable error {erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta 'end_if;'.")); Integer lastRef = TablaDeSimbolos.getContexto($1.sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"IF");}
                  | IF '(' condicion  THEN bloqueSentenciaEjecutable ELSE bloqueSentenciaEjecutable END_IF {erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta ')' al final de la condicion.")); Integer lastRef = TablaDeSimbolos.getContexto($1.sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"IF");}
                  | IF '(' condicion  THEN bloqueSentenciaEjecutable END_IF  {erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta ')' al final de la condicion.")); Integer lastRef = TablaDeSimbolos.getContexto($1.sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"IF");}
                  | IF  condicion ')' THEN bloqueSentenciaEjecutable ELSE bloqueSentenciaEjecutable END_IF {erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta '(' al comienzo de la condicion.")); Integer lastRef = TablaDeSimbolos.getContexto($1.sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"IF");}
                  | IF  condicion ')' THEN bloqueSentenciaEjecutable END_IF  {erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta '(' al comienzo de la condicion.")); Integer lastRef = TablaDeSimbolos.getContexto($1.sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"IF");}
                  | IF  condicion  THEN bloqueSentenciaEjecutable ELSE bloqueSentenciaEjecutable END_IF {erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO faltan '()' en la condicion.")); Integer lastRef = TablaDeSimbolos.getContexto($1.sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"IF");}
                  | IF  condicion  THEN bloqueSentenciaEjecutable END_IF  {erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO faltan '()' en la condicion.")); Integer lastRef = TablaDeSimbolos.getContexto($1.sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"IF");}
                  | IF '(' condicion ')' THEN error END_IF  {erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta bloque de sentencias ejecutables valido.")); Integer lastRef = TablaDeSimbolos.getContexto($1.sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"IF");}
                  | IF '(' condicion ')' THEN bloqueSentenciaEjecutable ELSE error END_IF    {erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta bloque de sentencias ejecutables valido .")); Integer lastRef = TablaDeSimbolos.getContexto($1.sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"IF");}
                  | IF '(' condicion ')' THEN error ELSE bloqueSentenciaEjecutable END_IF    {erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta bloque de sentencias ejecutables valido .")); Integer lastRef = TablaDeSimbolos.getContexto($1.sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"IF");}
                  | IF '(' condicion ')' THEN error ELSE error END_IF  {erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO faltan los dos bloques de sentencias ejecutables validas.")); Integer lastRef = TablaDeSimbolos.getContexto($1.sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"IF");}

;

clausulaSeleccionConRet : IF '(' condicion ')' THEN bloqueSentenciaEjecutableConRet END_IF    {$$.sval = "false"; Integer lastRef = TablaDeSimbolos.getContexto($1.sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"IF");}
                        | IF '(' expresion ')' THEN bloqueSentenciaEjecutableConRet END_IF {erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera una condicion valida")); $$.sval = "false";  Integer lastRef = TablaDeSimbolos.getContexto($1.sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"IF");}
                        | IF '(' expresion ')' THEN bloqueSentenciaEjecutableConRet ELSE bloqueSentenciaEjecutableConRet END_IF {erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera una condicion valida"));if ($6.sval.equals("true") && $8.sval.equals("true")) $$.sval = "true"; else $$.sval = "false"; Integer lastRef = TablaDeSimbolos.getContexto($1.sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"IF");}                  
                        | IF '(' condicion ')' THEN bloqueSentenciaEjecutableConRet ELSE bloqueSentenciaEjecutableConRet END_IF   { if ($6.sval.equals("true") && $8.sval.equals("true")) $$.sval = "true"; else $$.sval = "false"; Integer lastRef = TablaDeSimbolos.getContexto($1.sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"IF");}
                        | IF '(' condicion ')' THEN bloqueSentenciaEjecutableConRet error {$$.sval = "false";erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta 'end_if;'.")); Integer lastRef = TablaDeSimbolos.getContexto($1.sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"IF");}
                        | IF '(' condicion ')' THEN bloqueSentenciaEjecutableConRet ELSE bloqueSentenciaEjecutableConRet error {if ($6.sval.equals("true") && $8.sval.equals("true")) $$.sval = "true"; else $$.sval = "false";erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta 'end_if;'.")); Integer lastRef = TablaDeSimbolos.getContexto($1.sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"IF");} 
                        | IF '(' condicion  THEN bloqueSentenciaEjecutableConRet ELSE bloqueSentenciaEjecutableConRet END_IF {if ($5.sval.equals("true") && $7.sval.equals("true")) $$.sval = "true"; else $$.sval = "false";erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta ')' al final de la condicion.")); Integer lastRef = TablaDeSimbolos.getContexto($1.sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"IF");}
                        | IF '(' condicion  THEN bloqueSentenciaEjecutableConRet END_IF  {$$.sval = "false";erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta ')' al final de la condicion.")); Integer lastRef = TablaDeSimbolos.getContexto($1.sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"IF");}
                        | IF  condicion ')' THEN bloqueSentenciaEjecutableConRet ELSE bloqueSentenciaEjecutableConRet END_IF {if ($5.sval.equals("true") && $7.sval.equals("true")) $$.sval = "true"; else $$.sval = "false";erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta '(' al comienzo de la condicion.")); Integer lastRef = TablaDeSimbolos.getContexto($1.sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"IF");}
                        | IF  condicion ')' THEN bloqueSentenciaEjecutableConRet END_IF  {$$.sval = "false";erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta '(' al comienzo de la condicion.")); Integer lastRef = TablaDeSimbolos.getContexto($1.sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"IF");}
                        | IF  condicion  THEN bloqueSentenciaEjecutableConRet ELSE bloqueSentenciaEjecutableConRet END_IF {if ($4.sval.equals("true") && $6.sval.equals("true")) $$.sval = "true"; else $$.sval = "false";erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO faltan '()' en la condicion.")); Integer lastRef = TablaDeSimbolos.getContexto($1.sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"IF");}
                        | IF  condicion  THEN bloqueSentenciaEjecutableConRet END_IF  {$$.sval = "false";erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO faltan '()' en la condicion.")); Integer lastRef = TablaDeSimbolos.getContexto($1.sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"IF");}
                        | IF '(' condicion ')' THEN error END_IF  {$$.sval = "false";erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta bloque de sentencias ejecutables valido.")); Integer lastRef = TablaDeSimbolos.getContexto($1.sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"IF");}
                        | IF '(' condicion ')' THEN bloqueSentenciaEjecutableConRet ELSE error END_IF    {$$.sval = "false";erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta bloque de sentencias ejecutables valido .")); Integer lastRef = TablaDeSimbolos.getContexto($1.sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"IF");}
                        | IF '(' condicion ')' THEN error ELSE bloqueSentenciaEjecutableConRet END_IF    {$$.sval = "false";erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta bloque de sentencias ejecutables valido .")); Integer lastRef = TablaDeSimbolos.getContexto($1.sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"IF");}
                        | IF '(' condicion ')' THEN error ELSE error END_IF {$$.sval = "false";erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO faltan los dos bloques de sentencias ejecutables validas.")); Integer lastRef = TablaDeSimbolos.getContexto($1.sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"IF");}
;

condicion: '(' expresion ',' listaExpresiones ')' comparador '(' expresion ',' listaExpresiones ')'
         | expresion ',' listaExpresiones comparador expresion ',' listaExpresiones {erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO faltan '( )' a las listas de expresiones."));}
         | expresion ',' listaExpresiones comparador '(' expresion ',' listaExpresiones ')' {erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO faltan parentesis en las listas de expresiones."));}
         | '(' expresion ',' listaExpresiones error comparador error expresion ',' listaExpresiones ')' {erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO faltan parentesis en las listas de expresiones."));}
         | '(' expresion ',' listaExpresiones ')' comparador error expresion ',' listaExpresiones error {erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO faltan parentesis en las listas de expresiones."));}
         | '(' expresion ',' listaExpresiones error comparador '(' expresion ',' listaExpresiones ')'{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO faltan parentesis en las listas de expresiones."));}
         | '(' expresion ',' listaExpresiones ')' comparador error expresion ',' listaExpresiones ')'{erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO faltan parentesis en las listas de expresiones."));}
         | '(' expresion ',' listaExpresiones ')' error '(' expresion ',' listaExpresiones ')' {erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera un comparador."));}
         | '(' expresion ',' listaExpresiones ')' comparador '(' expresion ',' listaExpresiones error {erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO faltan parentesis en las listas de expresiones."));}

         | expresion comparador expresion 
         | error comparador expresion {erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera una expresion a la izquierda del comparador."));}
         | expresion comparador error {erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera una expresion a la derecha del comparador."));}
         | expresion error expresion {erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera un comparador."));}
;

listaExpresiones : listaExpresiones ',' expresion
                 | expresion
                 | listaExpresiones ',' error {erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera una expresion despues de la coma."));}
;

comparador : '<' | '>' | '=' | DISTINTO | MENOR_IGUAL | MAYOR_IGUAL
;

cuerpoEjecutable  : cuerpoEjecutable sentenciaEjecutable ';'
                  | sentenciaEjecutable ';'
                  | sentenciaEjecutable error {erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta ';'."));}

;

bloqueSentenciaEjecutable : sentenciaEjecutable ';'
                          | sentenciaEjecutable error {erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta ';'."));}
                          | BEGIN cuerpoEjecutable END
;

cuerpoEjecutableConRet: cuerpoEjecutableConRet sentenciaEjecutableConRet ';' { if ($1.sval.equals("true") || $2.sval.equals("true")) $$.sval = "true"; else $$.sval = "false";}
                      | sentenciaEjecutableConRet ';' {$$.sval = $1.sval;}
;

bloqueSentenciaEjecutableConRet : sentenciaEjecutableConRet ';'   {$$.sval = $1.sval;}
                                | sentenciaEjecutableConRet error {$$.sval = $1.sval; erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta ';'."));}
                                | BEGIN cuerpoEjecutableConRet END {$$.sval = $2.sval;}
;


clausulaBucle : REPEAT bloqueSentenciaEjecutable WHILE '(' condicion ')'     {Integer lastRef = TablaDeSimbolos.getContexto($1.sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"Bucle");}
              | REPEAT error WHILE '(' condicion ')'                         {erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta bloque de sentencias ejecutables.")); Integer lastRef = TablaDeSimbolos.getContexto($1.sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"Bucle");}
              | REPEAT bloqueSentenciaEjecutable WHILE error condicion ')'   {erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta ( de apertura de condicion.")); Integer lastRef = TablaDeSimbolos.getContexto($1.sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"Bucle");}
              | REPEAT bloqueSentenciaEjecutable WHILE '(' error ')'         {erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta condicion.")); Integer lastRef = TablaDeSimbolos.getContexto($1.sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"Bucle");}
              | REPEAT bloqueSentenciaEjecutable WHILE '(' condicion         {erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta ) de cierre de condicion.")); Integer lastRef = TablaDeSimbolos.getContexto($1.sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"Bucle");}
              | REPEAT bloqueSentenciaEjecutable WHILE error condicion error {erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta ( ) englobando la condicion.")); Integer lastRef = TablaDeSimbolos.getContexto($1.sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"Bucle");}
              | REPEAT error WHILE '(' error ')'                             {erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta ( ) englobando la condicion y bloque de sentencias ejecutables.")); Integer lastRef = TablaDeSimbolos.getContexto($1.sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"Bucle");}
              | REPEAT error WHILE error condicion error                     {erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta condicion y bloque de sentencias ejecutables.")); Integer lastRef = TablaDeSimbolos.getContexto($1.sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"Bucle");}
              //| REPEAT error WHILE  error                                  {erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta condicion y bloque de sentencias ejecutables.")); Integer lastRef = TablaDeSimbolos.getContexto($1.sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"Bucle");}
              | REPEAT bloqueSentenciaEjecutable error                       {erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta while")); Integer lastRef = TablaDeSimbolos.getContexto($1.sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"Bucle");}
;

goto : GOTO etiqueta '@'     {Integer lastRef = TablaDeSimbolos.getContexto($1.sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"GOTO");}
     | GOTO etiqueta error   {erroresSintactico.add(new Error(numeroLineaError, Tipo.ERROR, "ERROR SINTACTICO falta '@' luego de los dospuntos.")); Integer lastRef = TablaDeSimbolos.getContexto($1.sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"GOTO");}
     | GOTO error '@' {erroresSintactico.add(new Error(numeroLineaError, Tipo.ERROR, "ERROR SINTACTICO etiqueta invalida.")); Integer lastRef = TablaDeSimbolos.getContexto($1.sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"GOTO");}
     | etiqueta '@'    {erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta 'goto' luego de los dospuntos.")); estructuras.add("Linea "+": "+"GOTO");}
;

etiqueta : IDENTIFICADOR ':'     {declaracionEtiqueta($1.sval);Integer lastRef = TablaDeSimbolos.getContexto($1.sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"Etiqueta");}
         | ':'           {erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta 'etiqueta'."));}
         //| IDENTIFICADOR error {erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO falta ':' luego de la etiqueta."));}
;

mensajeSalida : OUTF '(' expresion ')'   {Integer lastRef = TablaDeSimbolos.getContexto($1.sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"Mensaje de salida");}
              | OUTF '(' CADENA_MULTI ')' {Integer lastRef = TablaDeSimbolos.getContexto($1.sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"Mensaje de salida");}
              | OUTF '(' error ')' {erroresSintactico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SINTACTICO se espera cadena multilinea o expresion en el mensaje de salida.")); Integer lastRef = TablaDeSimbolos.getContexto($1.sval).popRef(); estructuras.add("Linea "+lastRef.toString() +": "+"Mensaje de salida");}
;


%%
//FUNCIONES
private static AnalizadorLexico lex;
public static List<Error> erroresLexico = new ArrayList<Error>();
public static List<Error> erroresSintactico = new ArrayList<Error>();
public static List<Error> erroresSemanticos = new ArrayList<Error>();
public static List<String> estructuras = new ArrayList<String>();

public static List<String> ambitos = new ArrayList<String>();
public static int numeroLineaError = -1;
public static void main(String[] args) {
    String filePath = "src/MATRIZ DE TRANSICIONES - Hoja 1.csv";

    int[][] matriz = MatrizTransicion.leerMatrizDesdeCSV(filePath);
    filePath = "src/MATRIZ DE TRANSICIONES - Hoja 2.csv";

    Accion[][] matrizAcciones = MatrizAccion.leerMatrizDesdeCSV(filePath);
    Parser parser = new Parser(true);
    Parser.lex = new AnalizadorLexico("--", matriz, matrizAcciones);
    ambitos.add("main");
    if (args.length > 1) {
        Parser.lex = new AnalizadorLexico(args[0], matriz, matrizAcciones);

        parser.run();
        for (Error error: erroresLexico){System.out.println(error);}
    } else {
        Parser.lex = new AnalizadorLexico("tests3/test3", matriz, matrizAcciones);
        
        parser.run();
        System.out.println("v---------------------------v");
        for (Error error: erroresSintactico){System.out.println(error);}
        for (Error error: erroresLexico){System.out.println(error);}
        for (String estructura: estructuras){System.out.println(estructura);}
        System.out.println("^---------------------------^");
        for (Error error: erroresSemanticos){System.out.println(error);}
        //System.out.println("No se especifico el archivo a compilar");
    }
    System.out.println(TablaDeSimbolos.imprimir());
}
private int yylex(){
  yylval = new ParserVal();
  int idToken = -1;
  if (!lex.end()){
    idToken = lex.getNextToken(yylval);
  }
  System.out.println("PARSER: " + yyval.ival);
  return idToken;
}

private void chequearRango(Contexto contexto){
  if (contexto != null){
    if(contexto.getTipo() == TablaTipoToken.getTipoToken("longint")){
      if(Long.parseLong(contexto.getValor()) > AnalizadorLexico.MAXLONGINT){
        erroresSintactico.add(new Error(
          AnalizadorLexico.getNumeroLinea(),
          Tipo.ERROR,
          "ERROR SINTACTICO excede rangos."
        ));
      }
    } else if (contexto.getTipo() == TablaTipoToken.getTipoToken("hexadecimal")){
      long valor = Long.parseLong(contexto.getValor().substring(2), 16);
      System.out.println(valor);
      if(valor > AnalizadorLexico.MAXHEXADECIMAL){
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
  numeroLineaError = AnalizadorLexico.getNumeroLinea();
  System.out.println("Error: " + string);
}
private void declararVariable(String refTipo, ArrayList<String> referenciasIdentificador){
  for (String refIdentificador: referenciasIdentificador){
    String newRef = TablaDeSimbolos.agregarSimbolo(refIdentificador + cargarAmbito(), -1, TablaDeSimbolos.getContexto(refIdentificador).getRef());
    Contexto conIdentificador = TablaDeSimbolos.getContexto(newRef);
    if (conIdentificador.getTipo() == -1){
      if (conIdentificador.getUso().equals("") ){
        conIdentificador.setTipo(TablaDeSimbolos.getContexto(refTipo).getTipo());
        conIdentificador.setUso("nombre de variable");
      }else{
        erroresSemanticos.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SEMANTICO identificador ya posee otro uso que no es nombre de variable"));
      }
    }else{
      erroresSemanticos.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SEMANTICO variable ya declarada"));
    };
  };
}

private String cargarAmbito(){
  String aux = "";
  for(String ambito: ambitos){
    aux = aux + ":" + ambito;
  }
  return aux;
}

private void declararFuncion(String ref){
  String newRef = TablaDeSimbolos.agregarSimbolo(ref + cargarAmbito(), -1, TablaDeSimbolos.getContexto(ref).getRef());
  Contexto con = TablaDeSimbolos.getContexto(newRef);
  if (con.getUso().equals("") ){
    con.setUso("nombre de funcion");
    con.setTipo(TablaTipoToken.getTipoToken(TablaTipoToken.FUN));
  }else{
    erroresSemanticos.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SEMANTICO identificador ya posee otro uso que no es nombre de funcion"));
  };
}

private void declaracionTriple(String refTipo, String refIdentificador){
  String newRef = TablaDeSimbolos.agregarSimbolo(refIdentificador + cargarAmbito(), -1, TablaDeSimbolos.getContexto(refIdentificador).getRef());
  Contexto conIdentificador = TablaDeSimbolos.getContexto(newRef);
  if (conIdentificador.getTipo() == -1){
    if (conIdentificador.getUso().equals("") ){
      conIdentificador.setTipo(TablaDeSimbolos.getContexto(refTipo).getTipo());
      conIdentificador.setUso("nombre de variable triple");
    }else{
      erroresSemanticos.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SEMANTICO identificador ya posee otro uso que no es nombre de variable triple"));
    }
  }else{
    erroresSemanticos.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SEMANTICO variable ya declarada"));
  };
}
private void declaracionSubtipo(String refTipo, String refIdentificador){
  String newRef = TablaDeSimbolos.agregarSimbolo(refIdentificador + cargarAmbito(), -1, TablaDeSimbolos.getContexto(refIdentificador).getRef());
  Contexto conIdentificador = TablaDeSimbolos.getContexto(newRef);
  if (conIdentificador.getTipo() == -1){
    if (conIdentificador.getUso().equals("") ){
      conIdentificador.setTipo(TablaDeSimbolos.getContexto(refTipo).getTipo());
      conIdentificador.setUso("nombre de subtipo");
      //FALTA Límite inferior o Límite superior
    }else{
      erroresSemanticos.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SEMANTICO identificador ya posee otro uso que no es nombre de subtipo"));
    }
  }else{
    erroresSemanticos.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SEMANTICO variable ya declarada"));
  };
}
private void declaracionEtiqueta(String refIdentificador){
  String newRef = TablaDeSimbolos.agregarSimbolo(refIdentificador + cargarAmbito(), -1, TablaDeSimbolos.getContexto(refIdentificador).getRef());
  Contexto conIdentificador = TablaDeSimbolos.getContexto(newRef);
  if (conIdentificador.getTipo() == -1){
    if (conIdentificador.getUso().equals("") ){
      //conIdentificador.setTipo(TablaDeSimbolos.getTipoToken());  ETIQUETA ??
      conIdentificador.setUso("nombre de etiqueta");
    }else{
      erroresSemanticos.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SEMANTICO identificador ya posee otro uso que no es nombre de etiqueta"));
    }
  }else{
    erroresSemanticos.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SEMANTICO variable ya declarada"));
  };
}
private void declaracionParametro(String refTipo, String refIdentificador){
  String newRef = TablaDeSimbolos.agregarSimbolo(refIdentificador + cargarAmbito(), -1, TablaDeSimbolos.getContexto(refIdentificador).getRef());
  Contexto conIdentificador = TablaDeSimbolos.getContexto(newRef);
  if (conIdentificador.getTipo() == -1){
    if (conIdentificador.getUso().equals("") ){
      conIdentificador.setTipo(TablaDeSimbolos.getContexto(refTipo).getTipo());
    conIdentificador.setUso("nombre de parametro");
      //FALTA Límite inferior o Límite superior
    }else{
      erroresSemanticos.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SEMANTICO identificador ya posee otro uso que no es nombre de parametro"));
    }
  }else{
    erroresSemanticos.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ERROR SEMANTICO variable ya declarada"));
  };
}