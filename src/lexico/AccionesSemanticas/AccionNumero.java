package lexico.AccionesSemanticas;

import lexico.TablaTipoToken;
import lexico.Token;

public class AccionNumero implements Accion{
    public void activar(Token token, StringBuilder cadena, Integer pos){
    };

    static class ASN1{
        public void activar(Token token, StringBuilder cadena, Integer pos){
            token = new Token(TablaTipoToken.getTipoToken(TablaTipoToken.CONSTANTE)); //(getNUMERO DE TOKEN PARA CONSTANTE)
        };
    }
    static class ASN2{
        public void activar(Token token, StringBuilder cadena, Integer pos){
        };
    }
    static class ASN3{
        public void activar(Token token, StringBuilder cadena, Integer pos){
        };
    }
    static class ASN4{
        public void activar(Token token, StringBuilder cadena, Integer pos){
        };
    }
}
