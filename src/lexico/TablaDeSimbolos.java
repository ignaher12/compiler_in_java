package lexico;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class TablaDeSimbolos {
    private static HashMap<Integer, Lexema> tabla = new HashMap<Integer, Lexema>();
    private static HashMap<Integer, Lexema> tablaReservada = new HashMap<Integer, Lexema>();
    private static int id = 0;
    
    static {
        TablaDeSimbolos.agregarReservada(TablaTipoToken.IF);
        TablaDeSimbolos.agregarReservada(TablaTipoToken.THEN);
        TablaDeSimbolos.agregarReservada(TablaTipoToken.BEGIN);
        TablaDeSimbolos.agregarReservada(TablaTipoToken.END);
        TablaDeSimbolos.agregarReservada(TablaTipoToken.END_IF);
        TablaDeSimbolos.agregarReservada(TablaTipoToken.OUTF);
        TablaDeSimbolos.agregarReservada(TablaTipoToken.TYPEDEF);
        TablaDeSimbolos.agregarReservada(TablaTipoToken.FUN);
        TablaDeSimbolos.agregarReservada(TablaTipoToken.RET);
        TablaDeSimbolos.agregarReservada(TablaTipoToken.SINGLE);
        TablaDeSimbolos.agregarReservada(TablaTipoToken.REPEAT);
        TablaDeSimbolos.agregarReservada(TablaTipoToken.WHILE);
        TablaDeSimbolos.agregarReservada(TablaTipoToken.GOTO);
        TablaDeSimbolos.agregarReservada(TablaTipoToken.LONGINT);
        TablaDeSimbolos.agregarReservada(TablaTipoToken.ELSE);
    }

    public static Lexema agregarSimbolo(String atributo, int tipo) {
        id++;
        Lexema aux = new Lexema(atributo, false, tipo);
        tabla.put(id, aux);
        return aux;
    }

    public static Lexema agregarReservada(String atributo) {
        id++;
        Lexema aux = new Lexema(atributo, true, 1);
        aux.setAtributo(atributo);
        tablaReservada.put(id, aux);
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

        sb.append("Tabla de Reservadas:\n");
        sb.append("-------------------\n");

        for (Map.Entry<Integer,Lexema> par : tablaReservada.entrySet()) {
            sb.append(par.getKey()).append(" - ").append(par.getValue()).append('\n');
        }

        return sb.toString();
    }

    //VER TEMA MAYUSCULAS
    public static Lexema existe(String cadena){
        Iterator<Map.Entry<Integer, Lexema>> iterator = tablaReservada.entrySet().iterator();
        
        while (iterator.hasNext()) {
            Map.Entry<Integer, Lexema> par = iterator.next();
        
            if (par.getValue().getAtributo().toLowerCase().equals(cadena.toLowerCase())) {
                return par.getValue();
            }
        }

        iterator = tabla.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer, Lexema> par = iterator.next();
        
            if (par.getValue().getAtributo().equals(cadena)) {
        
                return par.getValue();
            }
        }
        return null;
        
    }
}
