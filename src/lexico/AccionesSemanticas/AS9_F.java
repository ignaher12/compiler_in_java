package lexico.AccionesSemanticas;
import java.util.concurrent.atomic.AtomicInteger;

import lexico.MapeoCaracteres;
import lexico.TablaTipoToken;
import lexico.Token;


public class AS9_F implements Accion{
    public void activar(Token token, StringBuilder cadena, AtomicInteger pos, String linea) {
        char actual = linea.charAt(pos.get());
        if (actual == MapeoCaracteres.IGUAL){
            cadena.append(actual);
            pos.incrementAndGet();
        }
        
        token.setAtributo(null);
        token.setToken(TablaTipoToken.getTipoToken(cadena.toString()));
    }
}
