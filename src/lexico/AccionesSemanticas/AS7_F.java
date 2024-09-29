package lexico.AccionesSemanticas;
import java.util.concurrent.atomic.AtomicInteger;

import lexico.Lexema;
import lexico.TablaDeSimbolos;
import lexico.TablaTipoToken;
import lexico.Token;

public class AS7_F implements Accion{
    public void activar(Token token, StringBuilder cadena, AtomicInteger pos, String linea) {
        cadena.append(linea.charAt(pos.get()));
        pos.incrementAndGet();
        token.setIdentificador(TablaTipoToken.getTipoToken(TablaTipoToken.CADENA_MULTI));
        int ref = TablaDeSimbolos.existe(cadena.toString());
        if (ref == -1){
            token.setReferencia(TablaDeSimbolos.agregarSimbolo(cadena.toString(), token.getIdentificador()));
        }else{
            token.setReferencia(ref);
        }

    }
}
