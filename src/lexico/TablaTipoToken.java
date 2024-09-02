package lexico;

import java.util.HashMap;

public class TablaTipoToken {
    private static HashMap<String, Integer> tabla;
    public static final String IDENTIFICADOR = "identificador";
    public static final String CONSTANTE = "constante";
    
    static{
        tabla = new HashMap<String, Integer>();
        tabla.put(IDENTIFICADOR, 1);
        tabla.put(CONSTANTE, 2);
        tabla.put("Identificador", 1);
    }

    public static Integer getTipoToken(String cadena){
        return tabla.get(cadena);
    }
}
