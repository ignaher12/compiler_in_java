package lexico;

import java.util.HashMap;

public class MapeoCaracteres {
    private static final HashMap<Character, Integer> conversion;
    
    private static final char DIGITO = '0';
    private static final char MINUSCULA = 'a';
    private static final char MAYUSCULA = 'A';
    public static final char NUEVA_LINEA = '\n';
    public static final char ESPACIO = ' ';
    
    static {
        conversion = new HashMap<Character, Integer>();
        conversion.put(MINUSCULA, 0);
        conversion.put(MAYUSCULA, 0);
        conversion.put(DIGITO, 1);
        conversion.put(NUEVA_LINEA, 12);
        conversion.put(ESPACIO, 11);
    }

    public static int getConversion(char caracter){
        if (Character.isDigit(caracter)) {
            return conversion.get(DIGITO);
        } else if (Character.isLowerCase(caracter)) {
            return conversion.get(MINUSCULA);
        } else if ( Character.isUpperCase(caracter)) {
            return conversion.get(MAYUSCULA);
        } else {
            return conversion.get(caracter);
        }
    }
}
