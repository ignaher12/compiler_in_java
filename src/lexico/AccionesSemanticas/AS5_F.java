package lexico.AccionesSemanticas;
import java.util.concurrent.atomic.AtomicInteger;

import lexico.AnalizadorLexico;
import lexico.Lexema;
import lexico.TablaDeSimbolos;
import lexico.TablaTipoToken;
import lexico.Token;

public class AS5_F implements Accion{
    public void activar(Token token, StringBuilder cadena, AtomicInteger pos, String linea) {
        token.setToken(TablaTipoToken.getTipoToken(TablaTipoToken.FLOAT));
        //chequear rangos

        float numero = Float.parseFloat(cadena.toString());

        if (numero > AnalizadorLexico.MAXFLOATPOSITIVO){
            //WARNING
            cadena = new StringBuilder(Float.toString(AnalizadorLexico.MAXFLOATPOSITIVO));
        } else if (numero < AnalizadorLexico.MINFLOATNEGATIVO){
            //WARNING
            cadena = new StringBuilder(Float.toString(AnalizadorLexico.MINFLOATNEGATIVO));
        } else if ((numero > AnalizadorLexico.MAXFLOATNEGATIVO) && (numero < AnalizadorLexico.MAXFLOATPOSITIVO)){
            //WARNING
            cadena = new StringBuilder(Float.toString(0));
        }
        
        Lexema res = TablaDeSimbolos.existe(cadena.toString());
        if (res == null){
            token.setAtributo(TablaDeSimbolos.agregarSimbolo(cadena.toString(), token.getToken()));
        }else{
            if (res.isReservada()){
                token.setToken(TablaTipoToken.getTipoToken(cadena.toString()));
            }
            token.setAtributo(res);
        }
    }
}