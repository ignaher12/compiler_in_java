package lexico.AccionesSemanticas;
import java.util.concurrent.atomic.AtomicInteger;

import lexico.AnalizadorLexico;
import lexico.Lexema;
import lexico.TablaDeSimbolos;
import lexico.TablaTipoToken;
import lexico.Token;
import parser.Error.Tipo;
import parser.Error;
import parser.Parser;

public class AS6_F implements Accion{
    public void activar(Token token, StringBuilder cadena, AtomicInteger pos, String linea) {
        token.setIdentificador(TablaTipoToken.getTipoToken(TablaTipoToken.LONGINT));
        
        
        double valor  = Double.parseDouble(cadena.toString());
        if (valor > AnalizadorLexico.MAXLONGINT){
            //ERROR - Constante de tipo LongInt fuera de rango
            Parser.erroresLexico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "Constante de tipo LongInt fuera de rango"));
            token.setError();
        }if (valor > AnalizadorLexico.MAXLONGINT){
            //ERROR - Constante de tipo LongInt fuera de rango
            Parser.erroresLexico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "Constante de tipo LongInt fuera de rango"));
            token.setError();
        } else {
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
}
