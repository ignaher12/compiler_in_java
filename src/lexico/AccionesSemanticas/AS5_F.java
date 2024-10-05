package lexico.AccionesSemanticas;
import java.util.concurrent.atomic.AtomicInteger;
import lexico.AnalizadorLexico;
import lexico.TablaDeSimbolos;
import lexico.TablaDeSimbolos.Contexto;
import lexico.TablaTipoToken;
import lexico.Token;
import parser.Parser;
import parser.Error.Tipo;
import parser.Error;

public class AS5_F implements Accion{
    public void activar(Token token, StringBuilder cadena, AtomicInteger pos, String linea, int nro_linea) {
        token.setIdentificador(TablaTipoToken.getTipoToken(TablaTipoToken.CONSTANTE));
        //chequear rangos
        String cadenaExponente = cadena.toString().replace('s', 'e');
        float numero = Float.parseFloat(cadenaExponente);
        if (numero > AnalizadorLexico.MAXFLOATPOSITIVO){
            //Error - Constante de tipo Float fuera de rango
            Parser.erroresLexico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "Constante de tipo Float fuera de rango"));
            token.setError();
        } else if (numero < AnalizadorLexico.MINFLOATPOSITIVO){
            //Error - Constante de tipo Float fuera de rango
            Parser.erroresLexico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "Constante de tipo Float fuera de rango"));
            token.setError();
        } else {
            if (TablaDeSimbolos.existe(cadena.toString()) == null){
                token.setReferencia(TablaDeSimbolos.agregarSimbolo(cadena.toString(), TablaTipoToken.getTipoToken(TablaTipoToken.SINGLE), nro_linea));
            }else{
                Contexto context = TablaDeSimbolos.getContexto(cadena.toString());
                context.setValor(cadena.toString());
                TablaDeSimbolos.setContexto(cadena.toString(), context);
                if (context.isReservada()){
                    token.setIdentificador(TablaTipoToken.getTipoToken(cadena.toString()));
                }
                token.setReferencia(cadena.toString());
            }
        }
    }
}