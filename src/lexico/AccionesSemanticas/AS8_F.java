package lexico.AccionesSemanticas;
import java.util.concurrent.atomic.AtomicInteger;

import lexico.Token;


public class AS8_F implements Accion{
    //Comentario de una linea, borra alo leido por cadena e inicializa un nuevo StringBuilder
    public void activar(Token token, StringBuilder cadena, AtomicInteger pos, String linea) {
        cadena.delete(0,cadena.length());
    }
}
