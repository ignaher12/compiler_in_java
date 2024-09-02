package lexico.AccionesSemanticas;

import lexico.Token;

public class AccionPalabra implements Accion{
    public void activar(Token token, StringBuilder cadena, Integer pos){
    };

    public static class ASP1{
        public void activar(Token token, StringBuilder cadena, Integer pos){
            token = new Token(1); //(getNUMERO DE TOKEN PARA Palabra)
        };
    }
    public static class ASP2{
        public void activar(Token token, StringBuilder cadena, Integer pos){ //reutilizar
        };
    }
    public static class ASP3{
        public void activar(Token token, StringBuilder cadena, Integer pos){  //reutilizar
            cadena.deleteCharAt(cadena.length() - 1);
            pos--;
        };
    }
}
