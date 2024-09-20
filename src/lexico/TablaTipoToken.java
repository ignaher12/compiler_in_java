package lexico;

import java.util.HashMap;

public class TablaTipoToken {
    private static HashMap<String, Integer> tabla;
    public static final String IDENTIFICADOR = "identificador";
    public static final String CONSTANTE = "constante";
    public static final String HEXADECIMAL = "hexadecimal";
    public static final String FLOAT = "float";
    public static final String CADENA_MULTI = "cadena_multi";
    public static final String MAYOR = ">";
    public static final String MENOR = "<";
    public static final String IGUAL = "=";
    public static final String MENOR_IGUAL = "<=";
    public static final String MAYOR_IGUAL = ">=";
    public static final String SUMA = "+";
    public static final String RESTA = "-";
    public static final String DIVISION = "/";
    public static final String MULTIPLICACION = "*";
    public static final String PUNTO = ".";
    public static final String PUNTO_COMA = ";";
    public static final String PARENTESIS_I = "(";
    public static final String PARENTESIS_D = ")";
    public static final String ASIGNACION = ":=";
    public static final String DISTINTO = "!=";
    public static final String IF = "if";
    public static final String THEN = "then";
    public static final String BEGIN = "begin";
    public static final String END = "end";
    public static final String END_IF = "end_if";
    public static final String OUTF = "outf";
    public static final String TYPEDEF = "typedef";
    public static final String FUN = "fun";
    public static final String RET = "ret";
    public static final String SINGLE = "single";
    public static final String REPEAT = "repeat"; //AGREGAR
    public static final String WHILE = "while"; //AGREGAR
    public static final String GOTO = "goto"; //AGREGAR
    public static final String ARROBA = "@"; //AGREGAR
    public static final String LONGINT = "longint"; //AGREGAR
    public static final String ELSE = "else"; //AGREGAR
    
    static{
        tabla = new HashMap<String, Integer>();
        tabla.put(IDENTIFICADOR, 257);  // IDENTIFICADOR
        tabla.put(CONSTANTE, 258);      // CONSTANTE
        tabla.put(HEXADECIMAL, 259);    // HEXADECIMAL
        tabla.put(FLOAT, 260);          // FLOAT
        tabla.put(CADENA_MULTI, 261);   // CADENA_MULTI
        tabla.put(MAYOR, 6);            // No existe en los shorts, valor sin mapeo
        tabla.put(MENOR, 7);            // No existe en los shorts, valor sin mapeo
        tabla.put(MENOR_IGUAL, 274);    // MENOR_IGUAL
        tabla.put(MAYOR_IGUAL, 275);    // MAYOR_IGUAL
        tabla.put(SUMA, 10);            // No existe en los shorts, valor sin mapeo
        tabla.put(RESTA, 11);           // No existe en los shorts, valor sin mapeo
        tabla.put(DIVISION, 12);        // No existe en los shorts, valor sin mapeo
        tabla.put(MULTIPLICACION, 13);  // No existe en los shorts, valor sin mapeo
        tabla.put(PUNTO, 14);           // No existe en los shorts, valor sin mapeo
        tabla.put(PARENTESIS_I, 15);    // No existe en los shorts, valor sin mapeo
        tabla.put(PARENTESIS_D, 16);    // No existe en los shorts, valor sin mapeo
        tabla.put(ASIGNACION, 262);     // SIMASIGNACION
        tabla.put(DISTINTO, 263);       // DISTINTO
        tabla.put(IF, 264);             // IF
        tabla.put(THEN, 265);           // THEN
        tabla.put(BEGIN, 266);          // BEGIN
        tabla.put(END, 267);            // END
        tabla.put(END_IF, 268);         // END_IF
        tabla.put(OUTF, 269);           // OUTF
        tabla.put(TYPEDEF, 270);        // TYPEDEF
        tabla.put(FUN, 271);            // FUN
        tabla.put(RET, 272);            // RET
        tabla.put(SINGLE, 273);         // SINGLE
        tabla.put(PUNTO_COMA, 59);      // No existe en los shorts, valor sin mapeo
        tabla.put(IGUAL, 30);           // No existe en los shorts, valor sin mapeo
    }

    public static int getTipoToken(String cadena){
        return tabla.get(cadena.toLowerCase()).intValue();
    }
}
