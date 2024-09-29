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
        token.setIdentificador(TablaTipoToken.getTipoToken(TablaTipoToken.CONSTANTE));
        
        
        double valor  = Double.parseDouble(cadena.toString());
        if (valor > -AnalizadorLexico.MINLONGINT){//VALOR MAS ALTO POSITIVO
            //ERROR - Constante de tipo LongInt fuera de rango
            Parser.erroresLexico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "Constante de tipo LongInt fuera de rango"));
            token.setError();
        } else {
            int ref = TablaDeSimbolos.existe(cadena.toString());
            if (ref == -1){
                token.setReferencia(TablaDeSimbolos.agregarSimbolo(cadena.toString(), TablaTipoToken.getTipoToken(TablaTipoToken.LONGINT)));
            }else{
                if (TablaDeSimbolos.getByID(ref).isReservada()){
                    token.setIdentificador(TablaTipoToken.getTipoToken(cadena.toString()));
                }
                token.setReferencia(ref);
            }
        }


        

        
        
        
        
        
        
        
        
    }
}
