package lexico.AccionesSemanticas;

import java.util.concurrent.atomic.AtomicInteger;

import lexico.Token;

public class AS1 implements Accion{
    public void activar(Token token, StringBuilder cadena, AtomicInteger pos, String linea) {
        pos.incrementAndGet();
    }
}
