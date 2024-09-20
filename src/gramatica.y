//DECLARACIONES
//NUMERO DE TOKEN DE CARACTER ASCII (0-256)
//MAYOR MENOR IGUAL SUMA RESTA DIVISION MULTIPLICACION PUNTO PUNTO_COMA PARENTESIS_I PARENTESIS_D

// NUMERO DE TOKEN DE DEFINIDOS (257-...)
%token IDENTIFICADOR CONSTANTE HEXADECIMAL FLOAT CADENA_MULTI SIMASIGNACION DISTINTO IF THEN BEGIN
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

cuerpo : sentencia
       | cuerpo sentencia
;

sentencia : sentenciaDeclarativa ';'
          | sentenciaEjecutable ';'

sentenciaDeclarativa : tipoDato declaracion
                     | TYPEDEF IDENTIFICADOR SIMASIGNACION tipoDato '[' listaConstante ']'   // TEMA 11
;

declaracion : listaVariable
            | FUN IDENTIFICADOR '(' parametro ')' BEGIN cuerpoDeLaFuncion END
;

tipoDato : SINGLE 
         | LONGINT
         | IDENTIFICADOR //PARA typedef  // TEMA 11
;

listaVariable : listaVariable ',' IDENTIFICADOR
              | IDENTIFICADOR
;

listaConstante : listaConstante ',' CONSTANTE    // TEMA 11
               | CONSTANTE                       // TEMA 11
;

parametro : tipoDato IDENTIFICADOR
;

cuerpoDeLaFuncion : sentenciaFuncion
                  | cuerpoDeLaFuncion sentenciaFuncion
;

sentenciaFuncion : sentenciaDeclarativa
//                 | sentenciaEjecutableConRet
;
//IF EN FUNCIONES 
//sentenciaRet : RET '(' expresion ')' ';'
//;
 

sentenciaEjecutable : asignacion
                    | clausulaSeleccion
                    | clausulaBucle
                    | goto              //TEMA 23
                    | mensajeSalida
;

asignacion : IDENTIFICADOR SIMASIGNACION expresion
;

expresion : operando 
          | expresion operador operando 

operador : '+' | '-' | '*' | '/'
;

operando : IDENTIFICADOR
         | CONSTANTE
         | invocacionFuncion
;

invocacionFuncion : IDENTIFICADOR '(' expresion ')'
                  | IDENTIFICADOR '(' tipoDato expresion ')' //TEMA 27
;

clausulaSeleccion : IF '(' condicion ')' THEN bloqueIF END_IF
                  | IF '(' condicion ')' THEN bloqueIF ELSE bloqueIF END_IF
;

condicion : listaExpresion comparador listaExpresion        
;

listaExpresion : expresion          //TEMA 19
               | listaExpresion ',' expresion //TEMA 19
;

comparador : '<' | '>' | '=' | DISTINTO | MENOR_IGUAL | MAYOR_IGUAL 
;

bloqueIF : sentencia        //RENOMBRE A BLOQUECONTROL
         | BEGIN cuerpo END
;

clausulaBucle : REPEAT bloqueIF WHILE condicion
;

goto : GOTO IDENTIFICADOR '@'
;

mensajeSalida : OUTF '(' expresion ')'
              | OUTF '(' CADENA_MULTI ')'
;

//sentenciaEjecutableConRet : sentenciaRet
//                          | sentenciaEjecutable
//;

%%
//FUNCIONES
public static void main(String[] args) {
        if (args.length > 1) {
                String archivo_a_leer = args[0];
                this.run();
        } else {
                System.out.println("No se especifico el archivo a compilar");
        }
}
private int yylex(){}
