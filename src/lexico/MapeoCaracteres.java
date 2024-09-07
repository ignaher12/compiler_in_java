package lexico;

import java.lang.reflect.Array;
import java.util.HashMap;

public class MapeoCaracteres {
    private static final HashMap<Character, Integer> conversion;
    
    private static final char DIGITO = '0';
    private static final char MINUSCULA = 'a';
    private static final char MAYUSCULA = 'A';
    public static final char NUEVA_LINEA = '\n';
    public static final char ESPACIO = ' ';
    public static final char tab = ' '; //????
    public static final char COMA = ',';
    public static final char PUNTO = '.';
    public static final char PARENTESIS_I = '(';
    public static final char PARENTESIS_D = ')';
    public static final char MAS = '+';
    public static final char MENOS = '-';
    public static final char X = 'x';
    public static final char CERO = '0';
    public static final char S = 's';

//,-----,esp,tab,\n,L,A..F,x,0,N,[,s,],#,<,>,!,",",:,"""+""",-,*,/,(,),.,"""=""",c

    static {
        conversion = new HashMap<Character, Integer>();
        conversion.put(MINUSCULA, 0);
        conversion.put(MAYUSCULA, 0);
        conversion.put(DIGITO, 1);
        conversion.put(NUEVA_LINEA, 12);
        conversion.put(ESPACIO, 11);

    }

//TENGO QUE TEMRINAR ESTO QUENO SE SI LO ESTOY HACIENDO BIEN

    public static int getConversion(char caracter){
        System.out.println(caracter);
        if (Character.isDigit(caracter)) {
            if(caracter == '0'){
                return conversion.get(CERO);
            }
            return conversion.get(DIGITO);
        } else if (Character.isLowerCase(caracter)) {
            if(caracter == 'x'){
                return conversion.get(X);
            } else if (caracter == 's'){
                return conversion.get(S);
            }
            return conversion.get(MINUSCULA);
        } else if ( Character.isUpperCase(caracter)) {
            return conversion.get(MAYUSCULA);
        } else {
            return 100;
        }
    }
}
