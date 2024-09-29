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
        token.setIdentificador(TablaTipoToken.getTipoToken(TablaTipoToken.HEXADECIMAL));

        double valor  = HexFormat.fromHexDigits(cadena.subSequence(2, cadena.length()).toString());
        if (valor > AnalizadorLexico.MAXHEXADECIMAL){
            //ERROR - Constante de tipo Hexadecimal fuera de rango
            Parser.erroresLexico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "Constante de tipo Hexadecimal fuera de rango"));
            token.setError();
        }if (valor < AnalizadorLexico.MINHEXADECIMAL){
            //ERROR - Constante de tipo Hexadecimal fuera de rango
            Parser.erroresLexico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "Constante de tipo Hexadecimal fuera de rango"));
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