package lexico.AccionesSemanticas;
import java.util.concurrent.atomic.AtomicInteger;

import lexico.AnalizadorLexico;
import lexico.Lexema;
import lexico.TablaDeSimbolos;
import lexico.TablaTipoToken;
import lexico.Token;
import parser.Parser;
import parser.Error.Tipo;
import parser.Error;

public class AS5_F implements Accion{
    public void activar(Token token, StringBuilder cadena, AtomicInteger pos, String linea) {
        token.setIdentificador(TablaTipoToken.getTipoToken(TablaTipoToken.FLOAT));
        //chequear rangos
        String cadenaExponente = cadena.toString().replace('s', 'e');
        float numero = Float.parseFloat(cadenaExponente);

        if (numero > AnalizadorLexico.MAXFLOATPOSITIVO){
            //Error - Constante de tipo Float fuera de rango
            Parser.erroresLexico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "Constante de tipo Float fuera de rango"));
            token.setError();
        } else if (numero < AnalizadorLexico.MINFLOATNEGATIVO){
            //Error - Constante de tipo Float fuera de rango
            Parser.erroresLexico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "Constante de tipo Float fuera de rango"));
            token.setError();
        } else if ((numero > AnalizadorLexico.MAXFLOATNEGATIVO) && (numero < AnalizadorLexico.MAXFLOATPOSITIVO)){
            //Error - Constante de tipo Float fuera de rango
            Parser.erroresLexico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "Constante de tipo Float fuera de rango"));
            token.setError();
        } else {

            int ref = TablaDeSimbolos.existe(cadena.toString());
            if (ref == -1){
                token.setReferencia(TablaDeSimbolos.agregarSimbolo(cadena.toString(), token.getIdentificador()));
            }else{
                if (TablaDeSimbolos.getByID(ref).isReservada()){
                    token.setIdentificador(TablaTipoToken.getTipoToken(cadena.toString()));
                }
                token.setReferencia(ref);
            }
        }
    }
}