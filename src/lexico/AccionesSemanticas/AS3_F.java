package lexico.AccionesSemanticas;

import java.util.concurrent.atomic.AtomicInteger;

import lexico.AnalizadorLexico;
import lexico.Lexema;
import lexico.TablaDeSimbolos;
import lexico.TablaTipoToken;
import lexico.Token;
import parser.Error.Tipo;
import parser.Parser;
import parser.Error;

public class AS3_F implements Accion{
    //Verifica si es palabra reservada, sino devuelve token con identificador
    public void activar(Token token, StringBuilder cadena, AtomicInteger pos, String linea) {
        token.setIdentificador(TablaTipoToken.getTipoToken(TablaTipoToken.IDENTIFICADOR));
        
        if (cadena.length() > AnalizadorLexico.MAXLENGHTINDENTIFICADOR){ //truncar
            StringBuilder cadenaLarga = cadena;
            cadena = new StringBuilder(cadena.substring(0, AnalizadorLexico.MAXLENGHTINDENTIFICADOR));
            Parser.erroresLexico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.WARNING, "El identificador" + cadenaLarga + " fue truncado a: " + cadena));
        }

        Lexema res = TablaDeSimbolos.existe(cadena.toString());
        if (res == null){
            token.setLexema(TablaDeSimbolos.agregarSimbolo(cadena.toString(), token.getIdentificador()));
        }else{
            if (res.isReservada()){
                token.setIdentificador(TablaTipoToken.getTipoToken(cadena.toString()));
            }
            token.setLexema(res);
        }

    }
}
