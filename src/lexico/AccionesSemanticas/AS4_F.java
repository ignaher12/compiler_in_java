package lexico.AccionesSemanticas;
import java.util.HexFormat;
import java.util.concurrent.atomic.AtomicInteger;

import lexico.AnalizadorLexico;
import lexico.Lexema;
import lexico.TablaDeSimbolos;
import lexico.TablaTipoToken;
import lexico.Token;
import parser.Error.Tipo;
import parser.Error;
import parser.Parser;

public class AS4_F implements Accion{
    public void activar(Token token, StringBuilder cadena, AtomicInteger pos, String linea) {
        token.setToken(TablaTipoToken.getTipoToken(TablaTipoToken.HEXADECIMAL));

        double valor  = HexFormat.fromHexDigits(cadena.subSequence(2, cadena.length()).toString());
        if (valor > AnalizadorLexico.MAXHEXADECIMAL){
            //WARNING
            Parser.erroresLexico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.WARNING, "Constante de tipo Hexadecimal fuera de rango"));
            cadena = new StringBuilder(Double.toHexString(AnalizadorLexico.MAXHEXADECIMAL));
        }if (valor < AnalizadorLexico.MINHEXADECIMAL){
            //WARNING
            Parser.erroresLexico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.WARNING, "Constante de tipo Hexadecimal fuera de rango"));
            cadena = new StringBuilder(Double.toHexString(AnalizadorLexico.MINHEXADECIMAL));
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