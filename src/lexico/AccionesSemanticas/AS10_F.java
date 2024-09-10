package lexico.AccionesSemanticas;
import java.util.concurrent.atomic.AtomicInteger;

import lexico.TablaTipoToken;
import lexico.Token;

public class AS10_F implements Accion{
    public void activar(Token token, StringBuilder cadena, AtomicInteger pos, String linea) {
        System.out.println("kllkk");
        cadena.append(linea.charAt(pos.get()));
        pos.incrementAndGet();
        token.setAtributo(null);
        token.setToken(TablaTipoToken.getTipoToken(cadena.toString()));
    }
}
