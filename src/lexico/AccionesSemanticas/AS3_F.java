package lexico.AccionesSemanticas;

import java.util.concurrent.atomic.AtomicInteger;

import lexico.AnalizadorLexico;
import lexico.Lexema;
import lexico.TablaDeSimbolos;
import lexico.TablaTipoToken;
import lexico.Token;

public class AS3_F implements Accion{
    //Verifica si es palabra reservada, sino devuelve token con identificador
    public void activar(Token token, StringBuilder cadena, AtomicInteger pos, String linea) {
        token.setToken(TablaTipoToken.getTipoToken(TablaTipoToken.IDENTIFICADOR));
        Lexema res = TablaDeSimbolos.existe(cadena.toString());
        
        if (cadena.length() > AnalizadorLexico.MAXLENGHTINDENTIFICADOR){ //truncar
            cadena = new StringBuilder(cadena.substring(0, AnalizadorLexico.MAXLENGHTINDENTIFICADOR));
        }

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
