package lexico.AccionesSemanticas;
import java.util.concurrent.atomic.AtomicInteger;
import lexico.MapeoCaracteres;
import lexico.TablaTipoToken;
import lexico.Token;


public class AS9_F implements Accion{
    //myor, menor, mayor_igual, menor_igual
    public void activar(Token token, StringBuilder cadena, AtomicInteger pos, String linea, int nro_linea) {
        //System.out.println(linea);
        char actual;
        if (pos.get() < linea.length()){
            actual = linea.charAt(pos.get());
        }else{
            actual = '\n';
        }
        if (actual == MapeoCaracteres.IGUAL){
            cadena.append(actual);
            pos.incrementAndGet();
        }
        //System.out.println(cadena);
        token.setReferencia(null);
        token.setIdentificador(TablaTipoToken.getTipoToken(cadena.toString()));
    }
}
