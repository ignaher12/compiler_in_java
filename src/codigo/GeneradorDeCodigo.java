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
    private static boolean DEBUG = false;
    public static Stack<String> ultimosOperadoresLogicos = new Stack<String>();

    public static List<Terceto> tercetos;
    public static int contadorAux = 0;

    public static void generarCodigoAssembler(String nombreArchivo){
       
        try{
            escritor = new FileWriter(nombreArchivo);
            
            //generarHeader();
            for (Terceto terceto : Parser.tercetos) {
                switch (terceto.getT1()) {
                    case ":=":
                        if (DEBUG) escritor.append("\t" + "\t" + terceto+ "\n");
                        procesarAsignacion(terceto);
                        break;
                    case "+":
                        if (DEBUG) escritor.append("\t" + "\t" + terceto+ "\n");
                        procesarSuma(terceto);
                        break;
                    case "-":
                        if (DEBUG) escritor.append("\t" + "\t" + terceto+ "\n");
                        procesarResta(terceto);
                        break;
                    case "*":
                        if (DEBUG) escritor.append("\t" + "\t" + terceto+ "\n");
                        procesarMultiplicacion(terceto);
                        break;
                    case "/":
                        if (DEBUG) escritor.append("\t" + "\t" + terceto+ "\n");
                        procesarDivision(terceto);
                        break;
                    case ">":
                        if (DEBUG) escritor.append("\t" + "\t" + terceto+ "\n");
                        //procesarMayor(terceto);
                        procesarComparacion(">", terceto);
                        break;
                    case ">=":
                        if (DEBUG) escritor.append("\t" + "\t" + terceto+ "\n");
                        //procesarMayorIgual(terceto);
                        procesarComparacion(">=", terceto);
                        break;
                    case "<":
                        if (DEBUG) escritor.append("\t" + "\t" + terceto+ "\n");
                        //procesarMenor(terceto);
                        procesarComparacion("<", terceto);
                        break;
                    case "<=":
                        if (DEBUG) escritor.append("\t" + "\t" + terceto+ "\n");
                        //procesarMenorIgual(terceto);
                        procesarComparacion("<=", terceto);
                        break;
                    case "=":
                        if (DEBUG) escritor.append("\t" + "\t" + terceto+ "\n");
                        //procesarIgual(terceto);
                        procesarComparacion("=", terceto);
                        break;
                    case "BI":
                        if (DEBUG) escritor.append("\t" + "\t" + terceto+ "\n");
                        escritor.append("\t" + "JMP " + terceto.getT3()+ "\n");
                        break;
                    case "BF":
                        if (DEBUG) escritor.append("\t" + "\t" + terceto+ "\n");
                        procesarSaltoCondicion(terceto, ultimosOperadoresLogicos.pop());
                        break;
                    case "ETIQUETA":
                        if (DEBUG) escritor.append("\t" + "\t" + terceto+ "\n");
                        escritor.append(terceto.getT3() + ":" + "\n");
                        break;
                    case "AND":
                        if (DEBUG) escritor.append("\t" + "\t" + terceto+ "\n");
                        procesarAnd(terceto);
                        break;
                    case "OR":
                        if (DEBUG) escritor.append("\t" + "\t" + terceto+ "\n");
                        procesarOr(terceto);
                        break;
                    default:
                        break;
                }
            }
            escritor.flush();
        }catch(IOException e){
            System.out.println("Error al abrir el archivo: " + e.getMessage());
        };    
    }
    /* private void generarHeader(){
        escritor.write(".DATA ; Indica zona de datos\n");
        Iterator<Map.Entry<String, Contexto>> iterator = TablaDeSimbolos.getElementos().entrySet().iterator();
        while(iterator.hasNext()) {
            Map.Entry<String, Contexto> par = iterator.next();
            if(!par.getValue().getUso().equals("") && !par.getValue().getUso().equals("nombre de funcion")){
                escritor.write(par.getValue() + " DW \n");
            }
            
             String nombreSimbolo = entry.getKey();
            Symbol simbolo = entry.getValue();
            
            if (simbolo.enUso()) { // Verifica si el símbolo está en uso
                
            } 
        }
    }  */
    // Procesar tercetos de asignación
    private static void procesarAsignacion(Terceto terceto) {
        try {
            String variable = "_" + terceto.getT2() + Parser.cargarAmbito();
            String valor = obtenerValor(terceto.getT3());
            System.out.println("asignacion");
            // Agrega la instrucción de asignación en la sección de código
            escritor.append("\t" +"MOV " + variable + ", " + valor + "\n");
        } catch (IOException e) {
            System.out.println("Error al escribir la asignación en el archivo: " + e.getMessage());
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
        try {
            if (terceto.getTipo() == TablaTipoToken.getTipoToken("LONGINT") || terceto.getTipo() == TablaTipoToken.getTipoToken("HEXADECIMAL")){
                escritor.append("\t" +"MOV EAX, " + operando1 + "\n");      // Cargar arg1 en AX
                escritor.append("\t" +operacion + " EAX, " + operando2 + "\n"); // Realizar operación en AX
                escritor.append("\t" +"MOV " + variableAux + ", EAX" + "\n");  // Guardar en variable temporal
            }else{
                escritor.append("\t" +"FLD " + operando1 + "\n");      // Cargar operando1 en ST(0)
                escritor.append("\t" +"FLD " + operando2 + "\n");      // Cargar operando2 en ST(0)
                escritor.append("\t" +"F"+ operacion + "\n"); // Realizar opercion entre operando2 y ST(0), guarda resultado en ST(0)
                escritor.append("\t" +"FSTP " + variableAux + "\n");  // Guardar ST(0) en variable auxiliar y vacia la pila
            }
        } catch (IOException e) {
            System.out.println("Error al escribir la asignación en el archivo: " + e.getMessage());
        }
        

        return variableAux;
    }

    private static void procesarSaltoCondicion(Terceto terceto, String operadorLogico){
        try {
            escritor.append("\t" + "POPF"+ "\n");       //Saca los flags almacenados por la ultima comparacion
            if ((operadorLogico.equals("AND")) || (operadorLogico.equals("OR"))){
                escritor.append("\t" + "JZ " + terceto.getT3()+ "\n");
            }else if (operadorLogico.equals(">")){
                escritor.append("\t" + "JA " + terceto.getT3()+ "\n");
            }else if (operadorLogico.equals("<")){
                escritor.append("\t" + "JB " + terceto.getT3()+ "\n");
            }else if (operadorLogico.equals("=")){
                escritor.append("\t" + "JE " + terceto.getT3()+ "\n");
            }else if (operadorLogico.equals("<=")){
                escritor.append("\t" + "JBE " + terceto.getT3()+ "\n");
            }else if (operadorLogico.equals(">=")){
                escritor.append("\t" + "JAE " + terceto.getT3()+ "\n");
            }else{
                System.out.println("error en procesar salto condicion");
            }
        } catch (IOException e) {
            System.out.println("Error al escribir la asignación en el archivo: " + e.getMessage());
        }
            
    }
    private static void procesarAnd(Terceto terceto){
        String operacion = "AND";
        String operando1 = obtenerValor(terceto.getT2());
        String operando2 = obtenerValor(terceto.getT3());
        String variableAux = "@aux" + (++contadorAux);
        try {
            escritor.append("\t" +"MOV EAX, " + operando1 + "\n");      // Cargar arg1 en AX
            escritor.append("\t" +operacion + " EAX, " + operando2 + "\n"); // Realizar operación en AX
            escritor.append("\t" +"MOV " + variableAux + ", EAX" + "\n");  // Guardar en variable temporal
        } catch (IOException e) {
            System.out.println("Error al escribir AND en el archivo: " + e.getMessage());
        }
        terceto.setResultado(variableAux);
        TablaDeSimbolos.agregarSimbolo(variableAux, terceto.getTipo(), "variable auxiliar");
        ultimosOperadoresLogicos.push(operacion);
    }
    private static void procesarOr(Terceto terceto){
        String operacion = "OR";
        String operando1 = obtenerValor(terceto.getT2());
        String operando2 = obtenerValor(terceto.getT3());
        String variableAux = "@aux" + (++contadorAux);
        try {
            escritor.append("\t" +"MOV EAX, " + operando1 + "\n");      // Cargar arg1 en AX
            escritor.append("\t" +operacion + " EAX, " + operando2 + "\n"); // Realizar operación en AX
            escritor.append("\t" +"PUSHF" + "\n");  // Guarda flags en la pila
            //escritor.append("\t" +"MOV " + variableAux + ", EAX" + "\n");  // Guardar en variable temporal
        } catch (IOException e) {
            System.out.println("Error al escribir AND en el archivo: " + e.getMessage());
        }
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
        try {
            if ((TablaDeSimbolos.getContexto(operando1.replace("_", "")).getTipo()  == TablaTipoToken.getTipoToken("SINGLE")) || (TablaDeSimbolos.getContexto(operando2.replace("_", "")).getTipo()  == TablaTipoToken.getTipoToken("SINGLE"))){
                escritor.append("\t" +"FLD " + operando1 + "\n");      // Cargar operando1 en ST(0)
                escritor.append("\t" +"FLD " + operando2 + "\n");      // Cargar operando2 en ST(0)
                escritor.append("\t" +"FCOMP"+ "\n"); // Realizar opercion entre operando2 y ST(0), guarda resultado en ST(0)
                //escritor.append("\t" +"FSTP " + variableAux + "\n");  // Guardar ST(0) en variable auxiliar y vacia la pila 
            } else{
                escritor.append("\t" +"MOV EAX, " + operando1 + "\n");      // Cargar arg1 en AX
                escritor.append("\t" +"MOV EBX, " + operando2 + "\n");      // Cargar arg1 en AX
                escritor.append("\t" + "CMP EAX, EBX" + "\n"); // Realizar operación en AX
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
            escritor.append("\t" +"PUSHF" + "\n");  // Guarda flags en la pila
        } catch (IOException e) {
            System.out.println("Error al escribir la asignación en el archivo: " + e.getMessage());
        }
        
        ultimosOperadoresLogicos.push(operacion);
        return variableAux;
    }

    // Obtener el valor directo o de la tabla de símbolos si es una referencia
    private static String obtenerValor(String valor) {
        if (valor.startsWith("^")) return Parser.tercetos.get(Integer.parseInt(valor.substring(1))).getResultado();

        if (!valor.matches("^[0-9].*")) return "_" + valor + Parser.cargarAmbito();

        return valor;
    }
}
