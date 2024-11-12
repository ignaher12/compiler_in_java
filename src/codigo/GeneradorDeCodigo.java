package codigo;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import lexico.TablaDeSimbolos;
import lexico.TablaTipoToken;
import lexico.TablaDeSimbolos.Contexto;
import parser.Terceto;
import parser.Parser;

public class GeneradorDeCodigo {
    public static FileWriter escritor;
    public static StringBuilder data = new StringBuilder();
    private static boolean DEBUG = false;
    public static Stack<String> ultimosOperadoresLogicos = new Stack<String>();

    public static List<Terceto> tercetos;
    public static int contadorAux = 0;

    public static void generarCodigoAssembler(String nombreArchivo){
       
        try{
            escritor = new FileWriter(nombreArchivo);
            
            //StringBuilder data = new StringBuilder();

            for (Terceto terceto : Parser.tercetos) {
                switch (terceto.getT1()) {
                    case ":=":
                        if (DEBUG) data.append("\t" + "\t" + terceto+ "\n");
                        procesarAsignacion(terceto);
                        break;
                    case "+":
                        if (DEBUG) data.append("\t" + "\t" + terceto+ "\n");
                        procesarSuma(terceto);
                        break;
                    case "-":
                        if (DEBUG) data.append("\t" + "\t" + terceto+ "\n");
                        procesarResta(terceto);
                        break;
                    case "*":
                        if (DEBUG) data.append("\t" + "\t" + terceto+ "\n");
                        procesarMultiplicacion(terceto);
                        break;
                    case "/":
                        if (DEBUG) data.append("\t" + "\t" + terceto+ "\n");
                        procesarDivision(terceto);
                        break;
                    case ">":
                        if (DEBUG) data.append("\t" + "\t" + terceto+ "\n");
                        //procesarMayor(terceto);
                        procesarComparacion(">", terceto);
                        break;
                    case ">=":
                        if (DEBUG) data.append("\t" + "\t" + terceto+ "\n");
                        //procesarMayorIgual(terceto);
                        procesarComparacion(">=", terceto);
                        break;
                    case "<":
                        if (DEBUG) data.append("\t" + "\t" + terceto+ "\n");
                        //procesarMenor(terceto);
                        procesarComparacion("<", terceto);
                        break;
                    case "<=":
                        if (DEBUG) data.append("\t" + "\t" + terceto+ "\n");
                        //procesarMenorIgual(terceto);
                        procesarComparacion("<=", terceto);
                        break;
                    case "=":
                        if (DEBUG) data.append("\t" + "\t" + terceto+ "\n");
                        //procesarIgual(terceto);
                        procesarComparacion("=", terceto);
                        break;
                    case "BI":
                        if (DEBUG) data.append("\t" + "\t" + terceto+ "\n");
                        data.append("\t" + "JMP " + terceto.getT3()+ "\n");
                        break;
                    case "BF":
                        if (DEBUG) data.append("\t" + "\t" + terceto+ "\n");
                        procesarSaltoCondicion(terceto, ultimosOperadoresLogicos.pop());
                        break;
                    case "ETIQUETA":
                        if (DEBUG) data.append("\t" + "\t" + terceto+ "\n");
                        data.append(terceto.getT3() + ":" + "\n");
                        break;
                    case "AND":
                        if (DEBUG) data.append("\t" + "\t" + terceto+ "\n");
                        procesarAnd(terceto);
                        break;
                    case "OR":
                        if (DEBUG) data.append("\t" + "\t" + terceto+ "\n");
                        procesarOr(terceto);
                        break;
                    case "OUTF":                                            //NO FUNCA
                        if (DEBUG) data.append("\t" + "\t" + terceto+ "\n");
                        procesarSalida(terceto);
                        break;
                    default:
                        break;
                }
            }
            generarHeader();
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
            escritor.append("include \\masm32\\include\\windows.inc"+ "\n");
            escritor.append("include \\masm32\\include\\kernel32.inc"+ "\n");
            escritor.append("include \\masm32\\include\\masm32.inc"+ "\n");
            escritor.append("include \\masm32\\include\\user32.inc"+ "\n");
            escritor.append("includelib \\masm32\\lib\\kernel32.lib"+ "\n");
            escritor.append("includelib \\masm32\\lib\\masm32.lib"+ "\n");
            escritor.append("includelib \\masm32\\lib\\user32.lib"+ "\n");
            escritor.write(".DATA\n");
            Iterator<Map.Entry<String, Contexto>> iterator = TablaDeSimbolos.getElementos().entrySet().iterator();
            while(iterator.hasNext()) {
                Map.Entry<String, Contexto> par = iterator.next();
                String lexema = par.getKey();
                if(!par.getValue().getUso().isEmpty() && !par.getValue().getUso().equals("nombre de funcion")){        
                    if (lexema.startsWith("@"))escritor.write("\t" + lexema + " DD ?\n");
                    else escritor.write("\t" + "_" + lexema.replace(":", "_") + " DD ?\n");
                }
                if (lexema.contains("[")) escritor.write("\t" + "_" + lexema.replace(":", "_").replace("[", "").replace("]","") + " DB \"" + lexema.replaceAll("[_\\[\\]]", "") +"\", 0\n");
            }
            escritor.append(".CODE"+ "\n");
            escritor.append("START:"+ "\n");
        } catch (Exception e) {
            
        }
    }
    // Procesar tercetos de asignación
    private static void procesarAsignacion(Terceto terceto) {
        String variable = "_" + terceto.getT2() + Parser.cargarAmbito();
        String valor = obtenerValor(terceto.getT3());
        if(valor.startsWith("@") || valor.startsWith("_")){
            data.append("\t" +"MOV EAX, " + valor.replace(":", "_") + "\n");
            data.append("\t" +"MOV " + variable.replace(":", "_") + ", EAX" + "\n");
        }else{
            data.append("\t" +"MOV " + variable.replace(":", "_") + ", " + valor.replace(":", "_") + "\n");
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
            data.append("\t" +"FLD " + operando1.replace(":", "_") + "\n");      // Cargar operando1 en ST(0)
            data.append("\t" +"FLD " + operando2.replace(":", "_") + "\n");      // Cargar operando2 en ST(0)
            data.append("\t" +"F"+ operacion + "\n"); // Realizar opercion entre operando2 y ST(0), guarda resultado en ST(0)
            data.append("\t" +"FSTP " + variableAux + "\n");  // Guardar ST(0) en variable auxiliar y vacia la pila
        }
        

        return variableAux;
    }

    private static void procesarSaltoCondicion(Terceto terceto, String operadorLogico){
        data.append("\t" + "POPF"+ "\n");       //Saca los flags almacenados por la ultima comparacion
        if ((operadorLogico.equals("AND")) || (operadorLogico.equals("OR"))){
            data.append("\t" + "JZ " + terceto.getT3()+ "\n");
        }else if (operadorLogico.equals(">")){
            data.append("\t" + "JA " + terceto.getT3()+ "\n");
        }else if (operadorLogico.equals("<")){
            data.append("\t" + "JB " + terceto.getT3()+ "\n");
        }else if (operadorLogico.equals("=")){
            data.append("\t" + "JE " + terceto.getT3()+ "\n");
        }else if (operadorLogico.equals("<=")){
            data.append("\t" + "JBE " + terceto.getT3()+ "\n");
        }else if (operadorLogico.equals(">=")){
            data.append("\t" + "JAE " + terceto.getT3()+ "\n");
        }else{
            System.out.println("error en procesar salto condicion");
        }
            
    }
    private static void procesarAnd(Terceto terceto){
        String operacion = "AND";
        String operando1 = obtenerValor(terceto.getT2());
        String operando2 = obtenerValor(terceto.getT3());
        String variableAux = "@aux" + (++contadorAux);
        data.append("\t" +"MOV EAX, " + operando1.replace(":", "_") + "\n");      // Cargar arg1 en AX
        data.append("\t" +operacion + " EAX, " + operando2.replace(":", "_") + "\n"); // Realizar operación en AX
        data.append("\t" +"MOV " + variableAux + ", EAX" + "\n");  // Guardar en variable temporal
        terceto.setResultado(variableAux);
        TablaDeSimbolos.agregarSimbolo(variableAux, terceto.getTipo(), "variable auxiliar");
        ultimosOperadoresLogicos.push(operacion);
    }
    private static void procesarOr(Terceto terceto){
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
    }
    /* private static void procesarMenor(Terceto terceto){
        String resultado = procesarComparacion( "<", terceto);
    }
    private static void procesarMenorIgual(Terceto terceto){
        String resultado = procesarComparacion( "<=", terceto);
    }
    private static void procesarMayor(Terceto terceto){
        String resultado = procesarComparacion( ">", terceto);
    }
    private static void procesarMayorIgual(Terceto terceto){
        String resultado = procesarComparacion( ">=", terceto);
    }
    private static void procesarIgual(Terceto terceto){
        String resultado = procesarComparacion( "=", terceto);
    } */
    private static String procesarComparacion(String operacion, Terceto terceto){
        String operando1 = obtenerValor(terceto.getT2());
        String operando2 = obtenerValor(terceto.getT3());
        String variableAux = "@aux" + (++contadorAux);
        System.out.println(operando1);
        System.out.println(operando2);
        if ((TablaDeSimbolos.getContexto(operando1.replace("_", "")).getTipo()  == TablaTipoToken.getTipoToken("SINGLE")) || (TablaDeSimbolos.getContexto(operando2.replace("_", "")).getTipo()  == TablaTipoToken.getTipoToken("SINGLE"))){
            data.append("\t" +"FLD " + operando1.replace(":", "_") + "\n");      // Cargar operando1 en ST(0)
            data.append("\t" +"FLD " + operando2.replace(":", "_") + "\n");      // Cargar operando2 en ST(0)
            data.append("\t" +"FCOMP"+ "\n"); // Realizar opercion entre operando2 y ST(0), guarda resultado en ST(0)
            //escritor.append("\t" +"FSTP " + variableAux + "\n");  // Guardar ST(0) en variable auxiliar y vacia la pila 
        } else{
            data.append("\t" +"MOV EAX, " + operando1.replace(":", "_") + "\n");      // Cargar arg1 en AX
            data.append("\t" +"MOV EBX, " + operando2.replace(":", "_") + "\n");      // Cargar arg1 en AX
            data.append("\t" + "CMP EAX, EBX" + "\n"); // Realizar operación en AX
        }
        /* if ((terceto.getTipo() == TablaTipoToken.getTipoToken("LONGINT")) || (terceto.getTipo() == TablaTipoToken.getTipoToken("HEXADECIMAL"))){
            escritor.append("\t" +"MOV EAX, " + operando1 + "\n");      // Cargar arg1 en AX
            escritor.append("\t" +"MOV EBX, " + operando2 + "\n");      // Cargar arg1 en AX
            escritor.append("\t" + "CMP EAX, EBX" + "\n"); // Realizar operación en AX
        }else{
            escritor.append("\t" +"FLD " + operando1 + "\n");      // Cargar operando1 en ST(0)
            escritor.append("\t" +"FLD " + operando2 + "\n");      // Cargar operando2 en ST(0)
            escritor.append("\t" +"FCOMP"+ "\n"); // Realizar opercion entre operando2 y ST(0), guarda resultado en ST(0)
            //escritor.append("\t" +"FSTP " + variableAux + "\n");  // Guardar ST(0) en variable auxiliar y vacia la pila 
        } */
        data.append("\t" +"PUSHF" + "\n");  // Guarda flags en la pila
        
        ultimosOperadoresLogicos.push(operacion);
        return variableAux;
    }

    private static void procesarSalida(Terceto terceto){
        String valor = obtenerValor(terceto.getT2());
        if (valor.startsWith("_") ||valor.startsWith("@")){
            data.append("\t" +"INVOKE MessageBox, NULL, addr "+ valor.replace(":", "_").replace("[", "").replace("]","")+", addr "+valor.replace(":", "_").replace("[", "").replace("]","")+", MB_OK" + "\n");
        }
    }

    // Obtener el valor directo o de la tabla de símbolos si es una referencia
    private static String obtenerValor(String valor) {
        if (valor.startsWith("^")) return Parser.tercetos.get(Integer.parseInt(valor.substring(1))).getResultado();

        if (valor.startsWith("[")) return "_" + valor;
        if (!valor.matches("^[0-9].*")) return "_" + valor + Parser.cargarAmbito();

        return valor;
    }
}
