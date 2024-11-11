package codigo;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import lexico.TablaDeSimbolos;
import lexico.TablaTipoToken;
import lexico.TablaDeSimbolos.Contexto;
import parser.Terceto;
import parser.Parser;

public class GeneradoDeCodigo {
    public static FileWriter escritor;

    public static List<Terceto> tercetos;
    public static int contadorAux = 0;

    public static void generarCodigoAssembler(String nombreArchivo){
       
        try{
            escritor = new FileWriter(nombreArchivo);
            
            //generarHeader();
            for (Terceto terceto : Parser.tercetos) {
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
    
            // Agrega la instrucción de asignación en la sección de código
            escritor.append("MOV " + variable + ", " + valor + "\n");
        } catch (IOException e) {
            System.out.println("Error al escribir la asignación en el archivo: " + e.getMessage());
        }
    }

    // Procesar tercetos de suma
    private static void procesarSuma(Terceto terceto) {
        String resultado = procesarOperacionBinaria("ADD", terceto);
        terceto.setResultado(resultado);
        System.out.println(terceto.getResultado());
        TablaDeSimbolos.agregarSimbolo(resultado, "variable auxiliar");
    }

    // Procesar tercetos de resta
    private static void procesarResta(Terceto terceto) {
        String resultado = procesarOperacionBinaria("SUB", terceto);
        terceto.setResultado(resultado);
        TablaDeSimbolos.agregarSimbolo(resultado, "variable auxiliar");
    }

    // Procesar tercetos de multiplicación
    private static void procesarMultiplicacion(Terceto terceto) {
        String resultado = procesarOperacionBinaria("MUL", terceto);
        terceto.setResultado(resultado);
        TablaDeSimbolos.agregarSimbolo(resultado, "variable auxiliar");
    }

    // Procesar tercetos de división
    private static void procesarDivision(Terceto terceto) {
        String resultado = procesarOperacionBinaria("DIV", terceto);
        terceto.setResultado(resultado);
        TablaDeSimbolos.agregarSimbolo(resultado, "variable auxiliar");
    }

    // Procesar operaciones binarias usando un tipo de operación assembler (ADD, SUB, MUL, DIV)
    private static String procesarOperacionBinaria(String operacion, Terceto terceto) {
        String operando1 = obtenerValor(terceto.getT2());
        String operando2 = obtenerValor(terceto.getT3());
        String variableAux = "@aux" + (++contadorAux);
        try {
            if (terceto.getTipo() == TablaTipoToken.getTipoToken("LONGINT") || terceto.getTipo() == TablaTipoToken.getTipoToken("HEXADECIMAL")){
                System.out.println("fafa");
                escritor.append("MOV EAX, " + operando1 + "\n");      // Cargar arg1 en AX
                escritor.append(operacion + " EAX, " + operando2 + "\n"); // Realizar operación en AX
                escritor.append("MOV " + variableAux + ", EAX" + "\n");  // Guardar en variable temporal
            }else{
                escritor.append("FLD " + operando1 + "\n");      // Cargar operando1 en ST(0)
                escritor.append("F"+ operacion + " " + operando2 + "\n"); // Realizar opercion entre operando2 y ST(0), guarda resultado en ST(0)
                escritor.append("FSTP " + variableAux + "\n");  // Guardar ST(0) en variable auxiliar y vacia la pila
            }
        } catch (IOException e) {
            System.out.println("Error al escribir la asignación en el archivo: " + e.getMessage());
        }
        

        return variableAux;
    }

    // Obtener el valor directo o de la tabla de símbolos si es una referencia
    private static String obtenerValor(String valor) {
        if (valor.startsWith("^")) return Parser.tercetos.get(Integer.parseInt(valor.substring(1))).getResultado();

        if (!valor.matches("^[0-9].*")) return "_" + valor + Parser.cargarAmbito();

        return valor;
    }
}
