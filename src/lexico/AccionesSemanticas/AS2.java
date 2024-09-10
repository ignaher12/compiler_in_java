package lexico.AccionesSemanticas;
import java.util.concurrent.atomic.AtomicInteger;

import lexico.MapeoCaracteres;
import lexico.Token;

public class AS2 implements Accion{
    //Lee y concatena a la cadena
    public void activar(Token token, StringBuilder cadena, AtomicInteger pos, String linea) {
        char actual = linea.charAt(pos.get());
        if (actual != MapeoCaracteres.NUEVA_LINEA)////////check si Scanner lee saltos
            cadena.append(actual);
        pos.incrementAndGet(); 
    }
}