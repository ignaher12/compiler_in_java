package codigo;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import lexico.MapeoCaracteres;
import lexico.TablaDeSimbolos;
import lexico.TablaTipoToken;
import lexico.TablaDeSimbolos.Contexto;
import parser.Terceto;
import parser.Parser;

public class GeneradorDeCodigo {
    public static FileWriter escritor;
    public static StringBuilder seccionStart = new StringBuilder();
    public static Stack<StringBuilder> pilaFunciones = new Stack<StringBuilder>();
    public static StringBuilder data = seccionStart;
    public static List<StringBuilder> funciones = new ArrayList<StringBuilder>();
    public static StringBuilder aux = new StringBuilder();
    private static boolean DEBUG = false;
    public static Stack<String> ultimosOperadoresLogicos = new Stack<String>();
    public static List<String> ambitos = new ArrayList<String>();
    public static List<String> lexemasTS = new ArrayList<String>();
    public static boolean etiquetaFuncion = false;


    public static int cantidadOperacionesLogicas = 1; //ANDs

    public static List<Terceto> tercetos;
    public static int contadorAux = 0;

    public static void generarCodigoAssembler(String nombreArchivo){
       
        try{
            escritor = new FileWriter(nombreArchivo);
            ambitos.add("main");
            //StringBuilder data = new StringBuilder();
            Iterator<Map.Entry<String, Contexto>> iterator = TablaDeSimbolos.getElementos().entrySet().iterator();
            while(iterator.hasNext()) {
                Map.Entry<String, Contexto> par = iterator.next();
                String lexema = par.getKey();
                Contexto contexto = par.getValue();
                if((contexto.getUso()!= "") && !(contexto.getUso().equals("nombre de funcion")) || (!contexto.getDeclarado() && contexto.getTipo() == TablaTipoToken.getTipoToken("SINGLE"))){        
                    lexemasTS.add(lexema);
                }
            }
            for (Terceto terceto : Parser.tercetos) {
                if (DEBUG) data.append("\t" + "\t" + terceto+ "\n");
                switch (terceto.getT1()) {
                    case ":=":
                        
                        procesarAsignacion(terceto);
                        break;
                    case "+":
                        procesarSuma(terceto);
                        break;
                    case "-":
                        procesarResta(terceto);
                        break;
                    case "*":
                        procesarMultiplicacion(terceto);
                        break;
                    case "/":
                        procesarDivision(terceto);
                        break;
                    case ">":
                        //procesarMayor(terceto);
                        procesarComparacion(">", terceto);
                        break;
                    case ">=":
                        //procesarMayorIgual(terceto);
                        procesarComparacion(">=", terceto);
                        break;
                    case "<":
                        //procesarMenor(terceto);
                        procesarComparacion("<", terceto);
                        break;
                    case "<=":
                        //procesarMenorIgual(terceto);
                        procesarComparacion("<=", terceto);
                        break;
                    case "=":
                        //procesarIgual(terceto);
                        procesarComparacion("=", terceto);
                        break;
                    case "BI":
                        data.append("\t" + "JMP " + terceto.getT3()+ "\n");
                        break;
                    case "BF":
                        procesarSaltoCondicion(terceto, ultimosOperadoresLogicos.pop());
                        break;
                    case "ETIQUETA":
                        if (etiquetaFuncion){
                            ambitos.add(terceto.getT3());
                            etiquetaFuncion = false;
                        }
                        data.append(terceto.getT3() + ":" + "\n");
                        break;
                    case "AND": // NO FUNCA
                        procesarAnd(terceto);
                        break;
                    /* case "OR": // NO FUNCA
                        if (DEBUG) data.append("\t" + "\t" + terceto+ "\n");
                        procesarOr(terceto);
                        break; */
                    case "INICIOFUN":
                        aux = new StringBuilder();
                        pilaFunciones.add(new StringBuilder(data.toString()));
                        etiquetaFuncion = true;
                        data = aux;
                        procesarAnd(terceto);
                        break;
                    case "FINFUN":
                        funciones.add(new StringBuilder(aux.toString()));
                        ambitos.remove(ambitos.size()-1);
                        data = pilaFunciones.pop();
                        procesarAnd(terceto);
                        break;
                    case "OUTF": 
                        procesarSalida(terceto);
                        break;
                    default:
                        break;
                }
            }
            generarHeader();
            for (StringBuilder datos : funciones) {
                escritor.append(datos);
            }
            escritor.append("START:"+ "\n");
            escritor.append(data);
            escritor.append("\t" + "INVOKE ExitProcess, 0" + "\n");
            escritor.append("END START");
            escritor.flush();
        }catch(IOException e){
            System.out.println("Error al abrir el archivo: " + e.getMessage());
        };    
    }
    private static void generarHeader(){
        try {
            escritor.append(".386"+ "\n");
            escritor.append(".MODEL flat, stdcall"+ "\n");
            escritor.append(".STACK 200h"+ "\n");
            escritor.append("option casemap :none"+ "\n");
            escritor.append("include \\masm32\\include\\masm32rt.inc"+ "\n");
            escritor.append("includelib \\masm32\\lib\\kernel32.lib"+ "\n");
            escritor.append("includelib \\masm32\\lib\\masm32.lib"+ "\n");
            escritor.append("includelib \\masm32\\lib\\user32.lib"+ "\n");
            escritor.write("dll_dllcrt0 PROTO C\n");
            escritor.write("printf PROTO C : VARARG\n");
            escritor.write(".DATA\n");
            Iterator<Map.Entry<String, Contexto>> iterator = TablaDeSimbolos.getElementos().entrySet().iterator();
            while(iterator.hasNext()) {
                Map.Entry<String, Contexto> par = iterator.next();
                String lexema = par.getKey();
                Contexto contexto = par.getValue();
                if((contexto.getUso()!= "") && !(contexto.getUso().equals("nombre de funcion")) || (!contexto.getDeclarado() && contexto.getTipo() == TablaTipoToken.getTipoToken("SINGLE"))){        
                    String valorInicializacion = "?";
                    String tipoMemoria;
                    if (contexto.getTipo() == TablaTipoToken.getTipoToken("SINGLE")){
                        tipoMemoria = "DQ";
                    }else{
                        tipoMemoria = "DD";
                    }
                    if (lexema.startsWith("@")){                                //auxiliar
                        escritor.write("\t" + lexema + " "+ tipoMemoria + " ?\n");}
                    else {
                        if (contexto.getTipo() == TablaTipoToken.getTipoToken("SINGLE") && !contexto.getDeclarado()) //se deben declarar los floats para operar
                            valorInicializacion = lexema;                                                   
                        escritor.write("\t" + "_" + lexema.replace(":", "_").replace(".","f") + " "+ tipoMemoria + " " + valorInicializacion + "\n");
                    }
                }
                if (lexema.contains("[")) escritor.write("\t" + "_" + lexema.replace(":", "_").replaceAll("[\\[\\] ]","") + " DB \"" + lexema.replaceAll("[_\\[\\]]", "") +"\", 0\n");
            }
            escritor.append("\t" + "__new_line__ DB 13, 10, 0"+ "\n");
            escritor.append(".CODE"+ "\n");
        } catch (Exception e) {
            
        }
    }
    // Procesar tercetos de asignación
    private static void procesarAsignacion(Terceto terceto) {
        String variable = "_" + encontrarAmbito(terceto.getT2());
        String valor = obtenerValor(terceto.getT3());
        if (terceto.getTipo()  == TablaTipoToken.getTipoToken("LONGINT") || terceto.getTipo() == TablaTipoToken.getTipoToken("HEXADECIMAL")){
            if (valor.startsWith("@") || valor.startsWith("_")){
                data.append("\t" +"MOV EAX, " + valor.replace(":", "_") + "\n");
                data.append("\t" +"MOV " + variable.replace(":", "_") + ", EAX" + "\n");
            } else{
                data.append("\t" +"MOV " + variable.replace(":", "_") + ", " + valor.replace(":", "_") + "\n");
            };
        } else{
            if(valor.startsWith("@") || valor.startsWith("_")){
                data.append("\t" +"FLD " + valor.replace(":", "_").replace(".","f") + "\n");
            }else{
                data.append("\t" +"FLD _" + valor.replace(":", "_").replace(".","f") + "\n");
            }
            data.append("\t" +"FSTP " + variable.replace(":", "_") + "\n");
        }
    }

    // Procesar tercetos de suma
    private static void procesarSuma(Terceto terceto) {
        String resultado = procesarOperacionBinaria("ADD", terceto);
        terceto.setResultado(resultado);
        TablaDeSimbolos.agregarSimbolo(resultado, terceto.getTipo(), "variable auxiliar");
    }

    // Procesar tercetos de resta
    private static void procesarResta(Terceto terceto) {
        String resultado = procesarOperacionBinaria("SUB", terceto);
        terceto.setResultado(resultado);
        TablaDeSimbolos.agregarSimbolo(resultado, terceto.getTipo(), "variable auxiliar");
    }

    // Procesar tercetos de multiplicación
    private static void procesarMultiplicacion(Terceto terceto) {
        String resultado = procesarOperacionBinaria("MUL", terceto);
        terceto.setResultado(resultado);
        TablaDeSimbolos.agregarSimbolo(resultado, terceto.getTipo(), "variable auxiliar");
    }

    // Procesar tercetos de división
    private static void procesarDivision(Terceto terceto) {
        String resultado = procesarOperacionBinaria("DIV", terceto);
        terceto.setResultado(resultado);
        TablaDeSimbolos.agregarSimbolo(resultado, terceto.getTipo(), "variable auxiliar");
    }

    // Procesar operaciones binarias usando un tipo de operación assembler (ADD, SUB, MUL, DIV)
    private static String procesarOperacionBinaria(String operacion, Terceto terceto) {
        String operando1 = obtenerValor(terceto.getT2());
        String operando2 = obtenerValor(terceto.getT3());
        String variableAux = "@aux" + (++contadorAux);
        if (terceto.getTipo() == TablaTipoToken.getTipoToken("LONGINT") || terceto.getTipo() == TablaTipoToken.getTipoToken("HEXADECIMAL")){
            data.append("\t" +"MOV EAX, " + operando1.replace(":", "_") + "\n");      // Cargar arg1 en AX
            data.append("\t" +operacion + " EAX, " + operando2.replace(":", "_") + "\n"); // Realizar operación en AX
            data.append("\t" +"MOV " + variableAux + ", EAX" + "\n");  // Guardar en variable temporal
        }else{
            if (operando1.contains(".")) operando1 = "_" + operando1.replace(".", "f");
            if (operando2.contains(".")) operando2 = "_" + operando2.replace(".", "f");
            data.append("\t" +"FLD " + operando1.replace(":", "_") + "\n");      // Cargar operando1 en ST(0)
            data.append("\t" +"FLD " + operando2.replace(":", "_") + "\n");      // Cargar operando2 en ST(0)
            data.append("\t" +"F"+ operacion + "\n"); // Realizar opercion entre operando2 y ST(0), guarda resultado en ST(0)
            data.append("\t" +"FSTP " + variableAux + "\n");  // Guardar ST(0) en variable auxiliar y vacia la pila
        }
        

        return variableAux;
    }

    private static void procesarSaltoCondicion(Terceto terceto, String operadorLogico){
        for (int i = 0; cantidadOperacionesLogicas > i; i++){
            data.append("\t" + "POPF"+ "\n");       //Saca los flags almacenados por la ultima comparacion
            if ((operadorLogico.equals("AND")) || (operadorLogico.equals("OR"))){
                data.append("\t" + "JZ " + terceto.getT3()+ "\n");
            }else if (operadorLogico.equals(">")){
                data.append("\t" + "JNG " + terceto.getT3()+ "\n");
            }else if (operadorLogico.equals("<")){
                data.append("\t" + "JNL " + terceto.getT3()+ "\n");
            }else if (operadorLogico.equals("=")){
                data.append("\t" + "JNE " + terceto.getT3()+ "\n");
            }else if (operadorLogico.equals("<=")){
                data.append("\t" + "JNLE " + terceto.getT3()+ "\n");
            }else if (operadorLogico.equals(">=")){
                data.append("\t" + "JNGE " + terceto.getT3()+ "\n");
            }else{
                System.out.println("error en procesar salto condicion");
            }
        };
        cantidadOperacionesLogicas = 1;
            
    }
    private static void procesarAnd(Terceto terceto){
        /* String operacion = "AND";
        String operadorLogico = ultimosOperadoresLogicos.pop();
        String operadorLogico2 = ultimosOperadoresLogicos.pop();
        String operando2 = obtenerValor(terceto.getT3());
        String variableAux = "@aux" + (++contadorAux); */
        cantidadOperacionesLogicas = cantidadOperacionesLogicas + 1;
        /* data.append("\t" + "POPF"+ "\n");       //Saca los flags almacenados por la ultima comparacion
        data.append("\t" +operacion + " EAX, " + operando2.replace(":", "_") + "\n"); // Realizar operación en AX
        data.append("\t" +"MOV " + variableAux + ", EAX" + "\n");  // Guardar en variable temporal
        terceto.setResultado(variableAux);
        TablaDeSimbolos.agregarSimbolo(variableAux, terceto.getTipo(), "variable auxiliar");
        ultimosOperadoresLogicos.push(operacion); */
    }
    /* private static void procesarOr(Terceto terceto){
        String operacion = "OR";
        String operando1 = obtenerValor(terceto.getT2());
        String operando2 = obtenerValor(terceto.getT3());
        String variableAux = "@aux" + (++contadorAux);
        data.append("\t" +"MOV EAX, " + operando1.replace(":", "_") + "\n");      // Cargar arg1 en AX
        data.append("\t" +operacion + " EAX, " + operando2.replace(":", "_") + "\n"); // Realizar operación en AX
        data.append("\t" +"PUSHF" + "\n");  // Guarda flags en la pila
        //escritor.append("\t" +"MOV " + variableAux + ", EAX" + "\n");  // Guardar en variable temporal
        terceto.setResultado(variableAux);
        TablaDeSimbolos.agregarSimbolo(variableAux, terceto.getTipo(), "variable auxiliar");
        ultimosOperadoresLogicos.push(operacion);
    } */

    private static String procesarComparacion(String operacion, Terceto terceto){
        String operando1 = obtenerValor(terceto.getT2());
        String operando2 = obtenerValor(terceto.getT3());
        String variableAux = "@aux" + (++contadorAux);
        if ((TablaDeSimbolos.getContexto(operando1.replace("_", "")).getTipo()  == TablaTipoToken.getTipoToken("SINGLE")) || (TablaDeSimbolos.getContexto(operando2.replace("_", "")).getTipo()  == TablaTipoToken.getTipoToken("SINGLE"))){
            data.append("\t" +"FLD " + operando1.replace(":", "_") + "\n");      // Cargar operando1 en ST(0)
            data.append("\t" +"FLD " + operando2.replace(":", "_") + "\n");      // Cargar operando2 en ST(0)
            data.append("\t" +"FCOMP"+ "\n"); // Realizar opercion entre operando2 y ST(0), guarda resultado en ST(0)
        } else{
            data.append("\t" +"MOV EAX, " + operando1.replace(":", "_") + "\n");      // Cargar arg1 en AX
            data.append("\t" +"MOV EBX, " + operando2.replace(":", "_") + "\n");      // Cargar arg1 en AX
            data.append("\t" + "CMP EAX, EBX" + "\n"); // Realizar operación en AX
        }

        data.append("\t" +"PUSHF" + "\n");  // Guarda flags en la pila
        
        ultimosOperadoresLogicos.push(operacion);
        terceto.setResultado(variableAux);
        TablaDeSimbolos.agregarSimbolo(variableAux, terceto.getTipo(), "variable auxiliar");
        return variableAux;
    }

    private static void procesarSalida(Terceto terceto){
        String valor = obtenerValor(terceto.getT2());
        if (valor.startsWith("_") || valor.startsWith("@")){
            int tipo = TablaDeSimbolos.getContexto(valor.replace("_", "")).getTipo();
            if ( tipo == TablaTipoToken.getTipoToken("LONGINT") || tipo == TablaTipoToken.getTipoToken("HEXADECIMAL")){
                data.append("\t" +"INVOKE printf, cfm$(\"%d\\n\"), "+ valor.replace(":", "_")+ "\n");
            }else if ( tipo == TablaTipoToken.getTipoToken("SINGLE")){
                data.append("\t" +"INVOKE printf, cfm$(\"%.5Lf\\n\"), " + valor.replace(":", "_")+ "\n");
            }else{  //debe ser cadena multilinea
                data.append("\t" +"INVOKE printf, ADDR "+ valor.replace(":", "_").replaceAll("[\\[\\] ]","")+ "\n");
                data.append("\t" +"INVOKE printf, ADDR __new_line__"+ "\n");
            }
        }
    }

    // Obtener el valor directo o de la tabla de símbolos si es una referencia
    private static String obtenerValor(String valor) {
        if (valor.startsWith("^")) return Parser.tercetos.get(Integer.parseInt(valor.substring(1))).getResultado();

        if (valor.startsWith("[")) return "_" + valor;
        if (!valor.matches("^[0-9].*")){
            return "_" + encontrarAmbito(valor);
        } 

        return valor;
    }
    public static String cargarAmbito(){
        String aux = "";
        for(String ambito: ambitos){
          aux = aux + ":" + ambito;
        }
        return aux;
    }
    public static String encontrarAmbito(String valor){
        valor =  valor + cargarAmbito();
        int indice = 0;
        boolean encontrado = false;
        while(valor.lastIndexOf(":") != -1 && !encontrado){
            indice = 0;
            while ((indice < lexemasTS.size()) && !encontrado){
                if (valor.equals(lexemasTS.get(indice))) encontrado = true;
                indice = indice + 1;
            };
            if (valor.lastIndexOf(":") != -1 && !encontrado){
                int ultAmbito = valor.lastIndexOf(":");
                valor = valor.substring(0, ultAmbito);
            }
        }
        return valor;
    }
}
