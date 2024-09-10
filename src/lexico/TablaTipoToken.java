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
    
    static{
        tabla = new HashMap<String, Integer>();
        tabla.put(IDENTIFICADOR, 1);
        tabla.put(CONSTANTE, 2);
        tabla.put(HEXADECIMAL, 3);
        tabla.put(FLOAT, 4);
        tabla.put(CADENA_MULTI, 5);
        tabla.put(MAYOR, 6);
        tabla.put(MENOR, 7);
        tabla.put(MENOR_IGUAL, 8);
        tabla.put(MAYOR_IGUAL, 9);
        tabla.put(SUMA, 10);
        tabla.put(RESTA, 11);
        tabla.put(DIVISION, 12);
        tabla.put(MULTIPLICACION, 13);
        tabla.put(PUNTO, 14);
        tabla.put(PARENTESIS_I, 15);
        tabla.put(PARENTESIS_D, 16);
        tabla.put(ASIGNACION, 17);
        tabla.put(DISTINTO, 18);
        tabla.put(IF, 19);
        tabla.put(THEN, 20);
        tabla.put(BEGIN, 21);
        tabla.put(END, 22);
        tabla.put(END_IF, 23);
        tabla.put(OUTF, 24);
        tabla.put(TYPEDEF, 25);
        tabla.put(FUN, 26);
        tabla.put(RET, 27);
        tabla.put(SINGLE, 28);
        tabla.put(PUNTO_COMA, 29);
        tabla.put(IGUAL, 30);
    }

    public static int getTipoToken(String cadena){
        return tabla.get(cadena.toLowerCase()).intValue();
    }
}
