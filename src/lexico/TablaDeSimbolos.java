package lexico;

import java.util.HashMap;
import java.util.Map;

public class TablaDeSimbolos {
    private static HashMap<Integer, Lexema> tabla = new HashMap<Integer, Lexema>();
    private static int cantidad = 0;
    public static Lexema agregarSimbolo(String atributo) {
        cantidad++;
        Lexema aux = new Lexema(atributo);
        tabla.put(cantidad, aux);
        return aux;
    }
    public static void editarSimbolo(Lexema lexema, String nuevoLexema){
        lexema.setAtributo(nuevoLexema);
    }
    
    public static String imprimir() {
        StringBuilder sb = new StringBuilder();
        sb.append("Tabla de Símbolos:\n");
        sb.append("-------------------\n");

        for (Map.Entry<Integer,Lexema> par : tabla.entrySet()) {
            sb.append(par.getKey()).append(" - ").append(par.getValue()).append('\n');
        }

        return sb.toString();
    }
}
