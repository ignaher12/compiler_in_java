package lexico.AccionesSemanticas;
import java.util.concurrent.atomic.AtomicInteger;
import lexico.Token;

public interface Accion {
    public void activar(Token token, StringBuilder cadena, AtomicInteger pos, String linea, int nro_linea);
};