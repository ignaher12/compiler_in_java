package lexico.AccionesSemanticas;
import java.util.concurrent.atomic.AtomicInteger;

import lexico.AnalizadorLexico;
import lexico.Lexema;
import lexico.TablaDeSimbolos;
import lexico.TablaTipoToken;
import lexico.Token;

public class AS6_F implements Accion{
    public void activar(Token token, StringBuilder cadena, AtomicInteger pos, String linea) {
        token.setToken(TablaTipoToken.getTipoToken(TablaTipoToken.LONGINT));
        
        
        double valor  = Double.parseDouble(cadena.toString());
        if (valor > AnalizadorLexico.MAXLONGINT){
            //WARNING
            cadena = new StringBuilder(Integer.toString(AnalizadorLexico.MAXLONGINT));
        }if (valor > AnalizadorLexico.MAXLONGINT){
            cadena = new StringBuilder(Integer.toString(AnalizadorLexico.MAXLONGINT));
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
