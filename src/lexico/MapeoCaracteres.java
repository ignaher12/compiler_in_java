package lexico;
import java.util.HashMap;

public class MapeoCaracteres {
    private static final HashMap<Character, Integer> conversion;
    
    public static final char ESPACIO = ' ';
    public static final char TAB = '\t';
    public static final char NUEVA_LINEA = '\n';
    private static final char MINUSCULA = 'g';
    private static final char MAYUSCULA = 'G';
    public static final char A = 'A';
    public static final char B = 'B';
    public static final char C = 'C';
    public static final char D = 'D';
    public static final char E = 'E';
    public static final char F = 'F';
    public static final char X = 'x';
    public static final char CERO = '0';
    public static final char DIGITO = '0';
    public static final char CORCHETE_D = '[';
    public static final char X10 = 's';
    public static final char CORCHETE_I = ']';
    public static final char NUMERAL = '#';
    public static final char MENOR = '<';
    public static final char MAYOR = '>';
    public static final char EXCLAMACION = '!';
    public static final char COMA = ',';
    public static final char DOSPUNTOS = ':';
    public static final char MAS = '+';
    public static final char MENOS = '-';
    public static final char ASTERISCO = '*';
    public static final char BARRA = '/';
    public static final char PARENTESIS_I = '(';
    public static final char PARENTESIS_D = ')';
    public static final char PUNTO = '.';
    public static final char IGUAL = '=';
    public static final char PUNTO_COMA = ';';
    public static final char ARROBA = '@'; //AGREGAR

//,-----,esp,tab,\n,L,A..F,x,0,N,[,s,],#,<,>,!,",",:,"""+""",-,*,/,(,),.,"""=""",c

    static {
        conversion = new HashMap<Character, Integer>();
        conversion.put(ESPACIO, 1);
        conversion.put(TAB, 2);
        conversion.put(NUEVA_LINEA, 3);
        conversion.put(MINUSCULA, 4);
        conversion.put(MAYUSCULA, 4);
        conversion.put(A, 5);
        conversion.put(B, 5);
        conversion.put(C, 5);
        conversion.put(D, 5);
        conversion.put(E, 5);
        conversion.put(F, 5);
        conversion.put(X , 6);
        conversion.put(CERO, 7);
        conversion.put(DIGITO, 8);
        conversion.put(CORCHETE_I, 9);
        conversion.put(X10, 10);
        conversion.put(CORCHETE_D, 11);
        conversion.put(NUMERAL, 12);
        conversion.put(MENOR, 13);
        conversion.put(MAYOR, 14);
        conversion.put(EXCLAMACION, 15);
        conversion.put(COMA, 16);
        conversion.put(DOSPUNTOS, 17);
        conversion.put(MAS, 18);
        conversion.put(MENOS, 19);
        conversion.put(ASTERISCO, 20);
        conversion.put(BARRA, 21);
        conversion.put(PARENTESIS_I, 22);
        conversion.put(PARENTESIS_D, 23);
        conversion.put(PUNTO, 24);
        conversion.put(IGUAL, 25);
        conversion.put(PUNTO_COMA, 27);
   }


    public static int getConversion(char caracter){
        if (Character.isDigit(caracter)) {
            if(caracter == '0'){
                return conversion.get(CERO);
            }
            return conversion.get(DIGITO);
        } else if (Character.isLowerCase(caracter)) {
            if(caracter == 'x'){
                return conversion.get(X);
            } else if (caracter == 's'){
                return conversion.get(X10);
            }
            return conversion.get(MINUSCULA);
        } else if ( Character.isUpperCase(caracter)) {
            if(caracter == A ||caracter == B || caracter == C || caracter == D || caracter == E || caracter == F)
                return conversion.get(caracter);
            return conversion.get(MAYUSCULA);
        } else {
            return conversion.get(caracter);
        }
    }
}
