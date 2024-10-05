package lexico.AccionesSemanticas;

import java.util.concurrent.atomic.AtomicInteger;
import lexico.AnalizadorLexico;
import lexico.TablaDeSimbolos;
import lexico.TablaTipoToken;
import lexico.Token;
import parser.Error.Tipo;
import parser.Parser;
import parser.Error;

public class AS3_F implements Accion{
    //Verifica si es palabra reservada, sino devuelve token con identificador
    public void activar(Token token, StringBuilder cadena, AtomicInteger pos, String linea, int nro_linea) {
        token.setIdentificador(TablaTipoToken.getTipoToken(TablaTipoToken.IDENTIFICADOR));
        
        if (cadena.length() > AnalizadorLexico.MAXLENGHTINDENTIFICADOR){ //truncar
            StringBuilder cadenaLarga = cadena;
            cadena = new StringBuilder(cadena.substring(0, AnalizadorLexico.MAXLENGHTINDENTIFICADOR));
            Parser.erroresLexico.add(new Error(AnalizadorLexico.getNumeroLinea(), Tipo.WARNING, "El identificador" + cadenaLarga + " fue truncado a: " + cadena));
        }
        System.out.println(cadena.toString());
        System.out.println(TablaDeSimbolos.existe(cadena.toString()));
        if (TablaDeSimbolos.existe(cadena.toString()) == null){
            token.setReferencia(TablaDeSimbolos.agregarSimbolo(cadena.toString(), token.getIdentificador(), nro_linea));
        }else{
            System.out.println(TablaDeSimbolos.getContexto(cadena.toString()).isReservada());
            if (TablaDeSimbolos.getContexto(cadena.toString()).isReservada()){
                token.setIdentificador(TablaTipoToken.getTipoToken(cadena.toString()));
            }
            token.setReferencia(cadena.toString());
        }

    }
}
