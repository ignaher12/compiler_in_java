package lexico.AccionesSemanticas;
import java.util.concurrent.atomic.AtomicInteger;
import lexico.TablaDeSimbolos;
import lexico.TablaTipoToken;
import lexico.Token;

public class AS7_F implements Accion{
    public void activar(Token token, StringBuilder cadena, AtomicInteger pos, String linea, int nro_linea) {
        cadena.append(linea.charAt(pos.get()));
        pos.incrementAndGet();
        token.setIdentificador(TablaTipoToken.getTipoToken(TablaTipoToken.CADENA_MULTI));
        if (TablaDeSimbolos.existe(cadena.toString()) == null){
            token.setReferencia(TablaDeSimbolos.agregarSimbolo(cadena.toString(), token.getIdentificador(), nro_linea));
        }else{
            token.setReferencia(cadena.toString());
        }

    }
}
