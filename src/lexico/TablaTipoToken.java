package lexico;

import java.util.HashMap;

import parser.Parser;

public class TablaTipoToken {
    private static HashMap<String, Integer> tabla;
    public static final String IDENTIFICADOR = "identificador";
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
        tabla.put(IDENTIFICADOR, (int)Parser.IDENTIFICADOR);  
        tabla.put(HEXADECIMAL, (int)Parser.HEXADECIMAL);   
        tabla.put(FLOAT, (int)Parser.FLOAT);          
        tabla.put(CADENA_MULTI, (int)Parser.CADENA_MULTI); 
        tabla.put(MENOR_IGUAL, (int)Parser.MENOR_IGUAL);   
        tabla.put(MAYOR_IGUAL, (int)Parser.MAYOR_IGUAL);   
        tabla.put(ASIGNACION, (int)Parser.SIMASIGNACION);  
        tabla.put(DISTINTO, (int)Parser.DISTINTO); 
        tabla.put(IF, (int)Parser.IF);             
        tabla.put(THEN, (int)Parser.THEN);         
        tabla.put(BEGIN, (int)Parser.BEGIN);       
        tabla.put(END, (int)Parser.END);           
        tabla.put(END_IF, (int)Parser.END_IF);     
        tabla.put(OUTF, (int)Parser.OUTF);         
        tabla.put(TYPEDEF, (int)Parser.TYPEDEF);   
        tabla.put(FUN, (int)Parser.FUN);           
        tabla.put(RET, (int)Parser.RET);           
        tabla.put(SINGLE, (int)Parser.SINGLE);     
        tabla.put(REPEAT, (int)Parser.REPEAT);     
        tabla.put(WHILE, (int)Parser.WHILE);       
        tabla.put(GOTO, (int)Parser.GOTO);         
        tabla.put(LONGINT, (int)Parser.LONGINT);   
        tabla.put(ELSE, (int)Parser.ELSE);         
        tabla.put(MAYOR, (int) '>');            // No existe en los shorts, valor sin mapeo
        tabla.put(MENOR, (int) '<');            // No existe en los shorts, valor sin mapeo
        tabla.put(SUMA, (int) '+');            // No existe en los shorts, valor sin mapeo
        tabla.put(RESTA, (int) '-');           // No existe en los shorts, valor sin mapeo
        tabla.put(DIVISION, (int) '/');        // No existe en los shorts, valor sin mapeo
        tabla.put(MULTIPLICACION, (int) '*');  // No existe en los shorts, valor sin mapeo
        tabla.put(PUNTO, (int) '.');           // No existe en los shorts, valor sin mapeo
        tabla.put(PARENTESIS_I, (int) '(');    // No existe en los shorts, valor sin mapeo
        tabla.put(PARENTESIS_D, (int) ')');    // No existe en los shorts, valor sin mapeo
        tabla.put(PUNTO_COMA, (int) ';');      // No existe en los shorts, valor sin mapeo
        tabla.put(IGUAL, (int) '=');           // No existe en los shorts, valor sin mapeo
        tabla.put(ARROBA, (int) '@');           // No existe en los shorts, valor sin mapeo
    }

    public static int getTipoToken(String cadena){
        return tabla.get(cadena.toLowerCase()).intValue();
    }
}
