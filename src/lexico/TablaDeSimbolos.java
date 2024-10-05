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
            this.refs = new ArrayList<Integer>(linea);
            this.tipo = tipo;
            this.valor = valor;
            this.declarado = false;
            this.reservada = false;
        }
        public Contexto(int linea, int tipo){
            this.refs = new ArrayList<Integer>(linea);
            this.tipo = tipo;
            this.valor = null;
            this.declarado = false;
            this.reservada = false;
        }
        public Contexto(List<Integer> linea, int tipo){
            this.refs = new ArrayList<Integer>(linea);
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
        TablaDeSimbolos.agregarReservada(TablaTipoToken.TRIPLE);
        TablaDeSimbolos.agregarReservada(TablaTipoToken.HEXADECIMAL);
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

    public static String agregarReservada(String atributo) {
        Contexto con = new Contexto(0, TablaTipoToken.getTipoToken(atributo));
        con.setReservada();
        tablaReservada.put(atributo, con);
        return atributo;
    }
    
    public static String imprimir() {
        StringBuilder sb = new StringBuilder();
        
        sb.append("Tabla de Reservadas:\n");
        sb.append("-------------------\n");

        for (Map.Entry<String,Contexto> par : tablaReservada.entrySet()) {
            sb.append(par.getKey()).append(" - ").append(par.getValue()).append('\n');
        }
        
        sb.append("Tabla de Símbolos:\n");
        sb.append("-------------------\n");

        for (Map.Entry<String, Contexto> par : tabla.entrySet()) {
            sb.append(par.getKey()).append(" - ").append(par.getValue()).append('\n');
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
            System.out.println("ESTA EN TABLA RESERVADA");
            return tablaReservada.get(atributo.toLowerCase());}

        return tabla.get(atributo);
    }

    public static void setContexto(String atributo, Contexto contexto){
        tabla.remove(atributo);
        tabla.put(atributo, contexto);
    }
}
