package lexico.AccionesSemanticas;
import java.util.concurrent.atomic.AtomicInteger;

import lexico.AnalizadorLexico;
import lexico.Token;
import parser.Error;
import parser.Error.Tipo;
import parser.Parser;

public class ASE implements Accion {
    public void activar(Token token, StringBuilder cadena, AtomicInteger pos, String linea) {
        token.setError();
        
        int errorPos = pos.get();
        
        StringBuilder marcador = new StringBuilder(" ");
        for (int i = 1; i < errorPos; i++) {
            marcador.append(" "); // Añade espacios antes de la flecha
        }
        marcador.append("^"); // La flecha que apunta a la posición del error

        Parser.erroresLexico.add(new Error(
            AnalizadorLexico.getNumeroLinea(), 
            Tipo.ERROR, 
            "ERROR LÉXICO en la posición " + errorPos + ", " + "linea " + AnalizadorLexico.getNumeroLinea() + "\n" +
            linea.toString() + "\n" + marcador.toString()
        ));

        // Incrementa la posición
        //pos.incrementAndGet();
    }
}