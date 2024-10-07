package lexico.AccionesSemanticas;
import java.util.concurrent.atomic.AtomicInteger;
import lexico.AnalizadorLexico;
import lexico.TablaDeSimbolos;
import lexico.TablaTipoToken;
import lexico.Token;
import parser.Error.Tipo;
import parser.Error;
import parser.Parser;

public class AS4_F implements Accion{
    public void activar(Token token, StringBuilder cadena, AtomicInteger pos, String linea, int nro_linea) {
        token.setIdentificador(TablaTipoToken.getTipoToken(TablaTipoToken.CONSTANTE));

        long valor = Long.parseLong(cadena.substring(2), 16);
        if (valor >  -AnalizadorLexico.MINHEXADECIMAL){ //VALOR MAS ALTO POSITIVO
            //ERROR - Constante de tipo Hexadecimal fuera de rango
            Parser.erroresLexico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "Constante de tipo Hexadecimal fuera de rango"));
        } 

        if (TablaDeSimbolos.existe(cadena.toString()) == null){
            token.setReferencia(TablaDeSimbolos.agregarSimbolo(cadena.toString(), TablaTipoToken.getTipoToken(TablaTipoToken.HEXADECIMAL), cadena.toString(), nro_linea));
        }else{
            TablaDeSimbolos.getContexto(cadena.toString()).setValor(cadena.toString());
            if (TablaDeSimbolos.getContexto(cadena.toString()).isReservada()){
                token.setIdentificador(TablaTipoToken.getTipoToken(cadena.toString()));
            }
            token.setReferencia(cadena.toString());
        }
    }
}