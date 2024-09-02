package lexico.AccionesSemanticas;
import lexico.Token;

public interface Accion {
    abstract void activar(Token token, StringBuilder cadena, Integer pos);
}
