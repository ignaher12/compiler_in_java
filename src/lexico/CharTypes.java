package lexico;
import java.util.HashMap;

public class CharTypes {                                      
    //     L,  d, '[', ']',  #,  'x', '.', 's', '+', '-', A..F, \n
    private static final HashMap<Integer, Integer> conversion;
    static {
        conversion = new HashMap<Integer, Integer>();
        conversion.put(Character.getType('a'), 0);
        conversion.put(Character.getType('A'), 0);
        conversion.put(Character.getType('1'), 1);
        conversion.put(Character.getType(' '), 12);
        conversion.put(Character.getType('\n'), 12);
    }

    public static int devolverConversion(int tipo){
        if (conversion.get(tipo) != null){
            return conversion.get(tipo);
        }
        throw new IllegalArgumentException("Carácter no válido: " + tipo);
    }
}
