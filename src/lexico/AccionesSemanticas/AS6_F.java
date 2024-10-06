package lexico.AccionesSemanticas;
import java.util.concurrent.atomic.AtomicInteger;
import lexico.AnalizadorLexico;
import lexico.TablaDeSimbolos;
import lexico.TablaDeSimbolos.Contexto;
import lexico.TablaTipoToken;
import lexico.Token;
import parser.Error.Tipo;
import parser.Error;
import parser.Parser;

public class AS6_F implements Accion{
    public void activar(Token token, StringBuilder cadena, AtomicInteger pos, String linea, int nro_linea) {
        token.setIdentificador(TablaTipoToken.getTipoToken(TablaTipoToken.CONSTANTE));
        
        
        double valor  = Double.parseDouble(cadena.toString());
        double rango = AnalizadorLexico.MINLONGINT;
        if (valor > -rango){//VALOR MAS ALTO POSITIVO
            //ERROR - Constante de tipo LongInt fuera de rango
            Parser.erroresLexico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.ERROR, "Constante de tipo LongInt fuera de rango"));
        }
        if (TablaDeSimbolos.existe(cadena.toString()) == null){
            System.out.println("VALOR DE CONSTANTE" + cadena.toString());
            token.setReferencia(TablaDeSimbolos.agregarSimbolo(cadena.toString(), TablaTipoToken.getTipoToken(TablaTipoToken.LONGINT), cadena.toString(), nro_linea));
        }else{
            Contexto context = TablaDeSimbolos.getContexto(cadena.toString());
            context.setValor(cadena.toString());
            TablaDeSimbolos.agregarReferencia(cadena.toString(), nro_linea);
            if (TablaDeSimbolos.getContexto(cadena.toString()).isReservada()){
                token.setIdentificador(TablaTipoToken.getTipoToken(cadena.toString()));
            }
            token.setReferencia(cadena.toString());
        }
    }
}
