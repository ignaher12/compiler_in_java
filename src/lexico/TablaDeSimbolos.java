package lexico;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class TablaDeSimbolos {
    public static class Contexto{
        private List<Integer> refs;
        private String valor;
        private boolean declarado;
        private int tipo;
        private boolean reservada;

        public Contexto(int linea, String valor, int tipo){
            this.refs = new ArrayList<Integer>();
            this.refs.add(linea);
            this.tipo = tipo;
            this.valor = valor;
            this.declarado = false;
            this.reservada = false;
        }
        public Contexto(int linea, int tipo){
            this.refs = new ArrayList<Integer>();
            this.refs.add(linea);
            this.tipo = tipo;
            this.valor = null;
            this.declarado = false;
            this.reservada = false;
        }
        public Contexto(List<Integer> linea, int tipo){
            this.refs = new ArrayList<Integer>();
            this.refs.addAll(linea);
            this.tipo = tipo;
            this.valor = null;
            this.declarado = false;
            this.reservada = false;
        }
        public void addRef(int linea){
            this.refs.add(linea);
        }
        public void setDeclarado(){
            this.declarado = true;
        }
        public void setReservada(){
            this.reservada = true;
        }
        public void setValor(String valor){
            this.valor = valor;
        };
        public int getTipo(){
            return this.tipo;
        }
        public String getValor(){
            return this.valor;
        }
        public List<Integer> getRefs(){
            return this.refs;            
        }
        public boolean isReservada(){
            return this.reservada;
        }

    }

    private static HashMap<String, Contexto> tabla = new HashMap<String, Contexto>();
    private static HashMap<String, Contexto> tablaReservada = new HashMap<String, Contexto>();
    
    static {
        TablaDeSimbolos.agregarReservada(TablaTipoToken.IF, 0);
        TablaDeSimbolos.agregarReservada(TablaTipoToken.THEN,0);
        TablaDeSimbolos.agregarReservada(TablaTipoToken.BEGIN,0);
        TablaDeSimbolos.agregarReservada(TablaTipoToken.END,0);
        TablaDeSimbolos.agregarReservada(TablaTipoToken.END_IF,0);
        TablaDeSimbolos.agregarReservada(TablaTipoToken.OUTF,0);
        TablaDeSimbolos.agregarReservada(TablaTipoToken.TYPEDEF,0);
        TablaDeSimbolos.agregarReservada(TablaTipoToken.FUN,0);
        TablaDeSimbolos.agregarReservada(TablaTipoToken.RET,0);
        TablaDeSimbolos.agregarReservada(TablaTipoToken.SINGLE,0);
        TablaDeSimbolos.agregarReservada(TablaTipoToken.REPEAT,0);
        TablaDeSimbolos.agregarReservada(TablaTipoToken.WHILE,0);
        TablaDeSimbolos.agregarReservada(TablaTipoToken.GOTO,0);
        TablaDeSimbolos.agregarReservada(TablaTipoToken.LONGINT,0);
        TablaDeSimbolos.agregarReservada(TablaTipoToken.ELSE,0);
        TablaDeSimbolos.agregarReservada(TablaTipoToken.TRIPLE,0);
        TablaDeSimbolos.agregarReservada(TablaTipoToken.HEXADECIMAL,0);
    }

    public static String agregarSimbolo(String atributo, int tipo, int linea) {
        if (tabla.get(atributo) != null){
            tabla.get(atributo).addRef(linea);
        }else{
            Contexto con = new Contexto(linea, tipo);
            tabla.put(atributo, con);
        }
        return atributo;
    }
    public static String agregarSimbolo(String atributo, int tipo, List<Integer> refs) {
        if (tabla.get(atributo) != null){
            tabla.get(atributo).getRefs().addAll(refs);
        }else{
            Contexto con = new Contexto(refs, tipo);
            tabla.put(atributo, con);
        }
        return atributo;
    }

    public static String agregarReservada(String atributo, int linea) {
        if (tablaReservada.get(atributo.toLowerCase()) != null){
            tablaReservada.get(atributo.toLowerCase()).addRef(linea);
        }else{
            Contexto con = new Contexto(linea, TablaTipoToken.getTipoToken(atributo));
            con.setReservada();
            tablaReservada.put(atributo, con);
        }
        return atributo;
    }

    public static void agregarReferencia(String atributo, int linea){
        tabla.get(atributo).addRef(linea);
    } 
    
    public static String imprimir() {
        StringBuilder sb = new StringBuilder();
        
        sb.append("Tabla de Reservadas:\n");
        sb.append("-------------------\n");

        for (Map.Entry<String,Contexto> par : tablaReservada.entrySet()) {
            sb.append(par.getKey()).append(" - ").append(par.getValue().getRefs()).append('\n');
        }
        
        sb.append("Tabla de Símbolos:\n");
        sb.append("-------------------\n");

        for (Map.Entry<String, Contexto> par : tabla.entrySet()) {
            sb.append(par.getKey()).append(" - ").append(par.getValue().valor).append(" - ").append(par.getValue().declarado).append(" - ").append(par.getValue().tipo).append(" - ").append(par.getValue().getRefs()).append('\n');
        }

        

        return sb.toString();
    }

    //VER TEMA MAYUSCULAS
    public static String existe(String cadena){
        Iterator<Map.Entry<String, Contexto>> iterator = tablaReservada.entrySet().iterator();
        
        while (iterator.hasNext()) {
            Map.Entry<String, Contexto> par = iterator.next();
        
            if (par.getKey().toLowerCase().equals(cadena.toLowerCase())) {
                return par.getKey();
            }
        }

        Iterator<Map.Entry<String, Contexto>> iterator2 = tabla.entrySet().iterator();
        while (iterator2.hasNext()) {
            Map.Entry<String, Contexto> par = iterator2.next();
        
            if (par.getKey().equals(cadena)) {
        
                return par.getKey();
            }
        }
        return null;
        
    }

    public static Contexto getContexto(String atributo){
        if (tabla.get(atributo) == null) { 
            return tablaReservada.get(atributo.toLowerCase());}

        return tabla.get(atributo);
    }

    public static void setContexto(String atributo, Contexto contexto){
        tabla.remove(atributo);
        tabla.put(atributo, contexto);
    }
}
