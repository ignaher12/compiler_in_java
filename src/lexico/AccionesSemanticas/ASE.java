package lexico.AccionesSemanticas;
import java.util.concurrent.atomic.AtomicInteger;

import lexico.AnalizadorLexico;
import lexico.Token;
import parser.Error;
import parser.Error.Tipo;
import parser.Parser;

public class ASE implements Accion{
        public void activar(Token token, StringBuilder cadena, AtomicInteger pos, String linea) {
            token.setError();
            Parser.erroresLexico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "ÉRROR LEXICO en la posicion "+pos.get()+"+-1"));
            pos.incrementAndGet();
        }
}
