package lexico;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class TablaDeSimbolos {
    public static class Contexto{
        private List<Integer> refs;
        private List<Integer> refsAux;
        private int tipo;
        private int limite_inf;
        private int limite_sup;
        private String uso = "";
        private String valor;
        private boolean reservada;
        private boolean declarado;
        private String typedef = null;
        private int tipo_parametro;
        private String nombre_parametro;

        public Contexto(int linea, String valor, int tipo){
            this.refs = new ArrayList<Integer>();   //UTILIDAD?
            this.refsAux = new ArrayList<Integer>();
            this.refs.add(linea);
            this.refsAux.add(linea);
            this.tipo = tipo;
            this.valor = valor;
            this.declarado = false;
            this.reservada = false;
        }
        public Contexto(int linea, int tipo){
            this.refs = new ArrayList<Integer>();
            this.refsAux = new ArrayList<Integer>();
            this.refs.add(linea);
            this.refsAux.add(linea);
            this.tipo = tipo;
            this.valor = null;
            this.declarado = false;
            this.reservada = false;
        }
        public Contexto(int tipo){
            this.refs = new ArrayList<Integer>();
            this.refsAux = new ArrayList<Integer>();
            this.tipo = tipo;
            this.valor = null;
            this.declarado = false;
            this.reservada = false;
        }
        public Contexto(List<Integer> linea, int tipo){
            this.refs = new ArrayList<Integer>();
            this.refsAux = new ArrayList<Integer>();
            this.refs.addAll(linea);
            this.refsAux.addAll(linea);
            this.tipo = tipo;
            this.valor = null;
            this.declarado = false;
            this.reservada = false;
        }
        public Contexto(List<Integer> linea, String valor, int tipo){
            this.refs = new ArrayList<Integer>();
            this.refsAux = new ArrayList<Integer>();
            this.refs.addAll(linea);
            this.refsAux.addAll(linea);
            this.tipo = tipo;
            this.valor = valor;
            this.declarado = false;
            this.reservada = false;
        }
        public Contexto(Contexto c){
            this.refs = c.getRefs();
            this.refsAux = new ArrayList<Integer>();
            this.tipo = c.getTipo();
            this.valor = c.getValor();
            this.declarado = c.getDeclarado();
            this.reservada = c.isReservada();
        }
        public void addRef(int linea){
            this.refs.add(linea);
            this.refsAux.add(linea);
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
        public void setTipo(int tipo){
            this.tipo = tipo;
        }
        public void setNombreParametro(String nombre){
            this.nombre_parametro = nombre;
        }
        public void setTipoParametro(int tipo){
            this.tipo_parametro = tipo;
        }
        public int getTipoParametro(){
            return this.tipo_parametro;
        }
        public String getNombreParametro(){
            return this.nombre_parametro;
        }
        public String getUso(){
            return this.uso;
        }
        public void setUso(String uso){
            this.uso = uso;
        }
        public void setLimiteSup(int limiteSup){
            this.limite_sup = limiteSup;
        }
        public void setLimiteInf(int limiteInf){
            this.limite_inf = limiteInf;
        }
        public void setTypedef(String typedef){
            this.typedef = typedef;
        }
        public int getLimiteSup(){
            return this.limite_sup;
        }
        public int getLimiteInf(){
            return this.limite_inf;
        }
        public String getTypedef(){
            return this.typedef;
        }
        public String getValor(){
            return this.valor;
        }
        public boolean getDeclarado(){
            return this.declarado;
        }
        public List<Integer> getRefs(){
            return this.refs;            
        }
        public Integer getRef(){
            Integer ref = this.refsAux.get(refsAux.size()-1);
            return ref;            
        }
        public Integer popRef(){
            Integer ref = this.refsAux.get(refsAux.size()-1);
            this.refsAux.remove(refsAux.size()-1);
            return ref;            
        }
        public Integer popRefUso(){
            if ((refs.size()-1) >= 0){
                Integer ref = this.refs.get(refs.size()-1);
                this.refs.remove(refs.size()-1);
                return ref;  
            }else{
                return (-1);
            }        
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
    public static String agregarSimbolo(String atributo, int tipo, String valor, int linea) {
        if (tabla.get(atributo) != null){
            tabla.get(atributo).addRef(linea);
        }else{
            Contexto con = new Contexto(linea, valor, tipo);
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
    public static String agregarSimbolo(String atributo, int tipo, String uso) {
        Contexto con = new Contexto(-1);
        con.setUso(uso);
        con.setTipo(tipo);
        tabla.put(atributo, con);
        return atributo;
    }
    public static String agregarSimbolo(String atributo, int tipo, String valor, List<Integer> refs) {
        if (tabla.get(atributo) != null){
            tabla.get(atributo).getRefs().addAll(refs);
        }else{
            Contexto con = new Contexto(refs, valor, tipo);
            tabla.put(atributo, con);
        }
        return atributo;
    }

    public static void borrarSimbolo(String atributo) {	
        tabla.remove(atributo);
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
    public static String agregarReservada(String atributo) {
        if (tablaReservada.get(atributo.toLowerCase()) != null){
            
        }else{
            Contexto con = new Contexto(TablaTipoToken.getTipoToken(atributo));
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
            sb.append(par.getKey()).append(" - ").append(par.getValue().valor).append(" - ").append(par.getValue().declarado).append(" - ").append(par.getValue().tipo).append(" - ").append(par.getValue().getRefs()).append(par.getValue().getUso()).append(par.getValue().getTipoParametro()).append(par.getValue().getNombreParametro()).append(par.getValue().getTypedef()).append('\n');
        }

        

        return sb.toString();
    }
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
        //System.out.println(atributo);
        if (tabla.get(atributo) == null) { 
            return tablaReservada.get(atributo.toLowerCase());}

        return tabla.get(atributo);
    }

    public static void setContexto(String atributo, Contexto contexto){
        tabla.remove(atributo);
        tabla.put(atributo, contexto);
    }

    public static HashMap<String, Contexto> getElementos(){
        return tabla;
    }
    public static HashMap<String, Contexto> getElementos(String ambito){
        HashMap<String, Contexto> result =  new HashMap<>();

        Iterator<Map.Entry<String, Contexto>> iterator2 = tabla.entrySet().iterator();
        while (iterator2.hasNext()) {
            Map.Entry<String, Contexto> par = iterator2.next();
            int indexKey = par.getKey().indexOf(":");
            if (indexKey > 0){
                String ambitoKey = par.getKey().substring(indexKey, par.getKey().length());
                if (ambitoKey.equals(ambito)){
                    result.put(par.getKey(), par.getValue());
                }
            }
        }
        
        return result;
    }
}
