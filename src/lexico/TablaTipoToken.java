package lexico;

import java.util.HashMap;

import parser.Parser;

public class TablaTipoToken {
    private static HashMap<String, Integer> tabla;
    public static final String IDENTIFICADOR = "identificador";
    public static final String HEXADECIMAL = "hexadecimal";
    public static final String CADENA_MULTI = "cadena_multi";
    public static final String MENOR_IGUAL = "<=";
    public static final String MAYOR_IGUAL = ">=";
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
    public static final String LONGINT = "longint"; //AGREGAR
    public static final String ELSE = "else"; //AGREGAR
    public static final String TRIPLE = "triple"; //AGREGAR
    public static final String CONSTANTE = "constante"; //AGREGAR
    
    static{
        tabla = new HashMap<String, Integer>();
        tabla.put(IDENTIFICADOR, (int)Parser.IDENTIFICADOR);  
        tabla.put(HEXADECIMAL, (int)Parser.HEXADECIMAL);           
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
        tabla.put(TRIPLE, (int)Parser.TRIPLE);         
        tabla.put(CONSTANTE, (int)Parser.CONSTANTE);         
        tabla.put(String.valueOf(MapeoCaracteres.MAYOR), (int) '>');            // No existe en los shorts, valor sin mapeo
        tabla.put(String.valueOf(MapeoCaracteres.MENOR), (int) '<');            // No existe en los shorts, valor sin mapeo
        tabla.put(String.valueOf(MapeoCaracteres.MAS), (int) '+');            // No existe en los shorts, valor sin mapeo
        tabla.put(String.valueOf(MapeoCaracteres.MENOS), (int) '-');           // No existe en los shorts, valor sin mapeo
        tabla.put(String.valueOf(MapeoCaracteres.BARRA), (int) '/');        // No existe en los shorts, valor sin mapeo
        tabla.put(String.valueOf(MapeoCaracteres.ASTERISCO), (int) '*');  // No existe en los shorts, valor sin mapeo
        tabla.put(String.valueOf(MapeoCaracteres.PUNTO), (int) '.');           // No existe en los shorts, valor sin mapeo
        tabla.put(String.valueOf(MapeoCaracteres.COMA), (int) ',');           // No existe en los shorts, valor sin mapeo
        tabla.put(String.valueOf(MapeoCaracteres.PARENTESIS_I), (int) '(');    // No existe en los shorts, valor sin mapeo
        tabla.put(String.valueOf(MapeoCaracteres.PARENTESIS_D), (int) ')');    // No existe en los shorts, valor sin mapeo
        tabla.put(String.valueOf(MapeoCaracteres.PUNTO_COMA), (int) ';');      // No existe en los shorts, valor sin mapeo
        tabla.put(String.valueOf(MapeoCaracteres.IGUAL), (int) '=');           // No existe en los shorts, valor sin mapeo
        tabla.put(String.valueOf(MapeoCaracteres.ARROBA), (int) '@');           // No existe en los shorts, valor sin mapeo
        tabla.put(String.valueOf(MapeoCaracteres.CORCHETE_I), (int) '[');           // No existe en los shorts, valor sin mapeo
        tabla.put(String.valueOf(MapeoCaracteres.CORCHETE_D), (int) '[');           // No existe en los shorts, valor sin mapeo
        tabla.put(String.valueOf(MapeoCaracteres.LLAVE_I), (int) '{');           // No existe en los shorts, valor sin mapeo
        tabla.put(String.valueOf(MapeoCaracteres.LLAVE_D), (int) '}');           // No existe en los shorts, valor sin mapeo
        tabla.put(String.valueOf(MapeoCaracteres.DOSPUNTOS), (int) ':');           // No existe en los shorts, valor sin mapeo
    }

    public static int getTipoToken(String cadena){
        return tabla.get(cadena.toLowerCase()).intValue();
    }
    public static String getClavePorValor(int valor) {
        for (HashMap.Entry<String, Integer> entry : tabla.entrySet()) {
            if (entry.getValue().equals(valor)) {
                return entry.getKey();
            }
        }
        return null; // O lanzar una excepción si no se encuentra el valor
    }
}
