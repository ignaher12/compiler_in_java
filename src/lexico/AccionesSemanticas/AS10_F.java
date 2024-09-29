package lexico.AccionesSemanticas;
import java.util.concurrent.atomic.AtomicInteger;

import lexico.TablaTipoToken;
import lexico.Token;

public class AS10_F implements Accion{
    public void activar(Token token, StringBuilder cadena, AtomicInteger pos, String linea) {
        cadena.append(linea.charAt(pos.get()));
        pos.incrementAndGet();
        token.setLexema(null);
        token.setIdentificador(TablaTipoToken.getTipoToken(cadena.toString()));
    }
}
