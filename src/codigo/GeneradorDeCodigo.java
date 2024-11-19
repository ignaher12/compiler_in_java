package codigo;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
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
    public static StringBuilder seccionStart = new StringBuilder();
    public static Stack<StringBuilder> pilaFunciones = new Stack<StringBuilder>();
    public static StringBuilder data = seccionStart;
    public static List<StringBuilder> funciones = new ArrayList<StringBuilder>();
    public static StringBuilder aux = new StringBuilder();
    private static boolean DEBUG = false;
    public static Stack<String> ultimosOperadoresLogicos = new Stack<String>();
    public static List<String> ambitos = new ArrayList<String>();
    public static List<String> lexemasTS = new ArrayList<String>();

    public static HashMap<String, String> mapeoMultilineaData = new HashMap<String, String>();
    public static int cantMultilinea = 0;
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
                }else if (lexema.startsWith("[")){
                    mapeoMultilineaData.put(lexema, "@multi"+ cantMultilinea++);
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
                        procesarComparacion(">", terceto);
                        break;
                    case ">=":
                        procesarComparacion(">=", terceto);
                        break;
                    case "<":
                        procesarComparacion("<", terceto);
                        break;
                    case "<=":
                        procesarComparacion("<=", terceto);
                        break;
                    case "=":
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
                    case "AND":
                        cantidadOperacionesLogicas = cantidadOperacionesLogicas + 1;
                        break;
                    case "INICIOFUN":
                        aux = new StringBuilder();
                        pilaFunciones.add(new StringBuilder(data.toString()));
                        etiquetaFuncion = true;
                        data = aux;
                        break;
                    case "FINFUN":
                        funciones.add(new StringBuilder(aux.toString()));
                        ambitos.remove(ambitos.size()-1);
                        data = pilaFunciones.pop();
                        break;
                    case "CALL":
                        procesarInvocacion(terceto);
                        break;
                    case "RET":
                        String res = obtenerValor(terceto.getT2());
                        if (TablaDeSimbolos.getContexto(res.replace("_", "")).getTipo()  == TablaTipoToken.getTipoToken("SINGLE")){
                            data.append("\t" + "FLD _" + res.replace(":", "_") + "\n");
                        }else{
                            data.append("\t" + "MOV EAX, _" + res.replace(":", "_") + "\n");
                        }
                        data.append("RET" + "\n");
                        break;
                    case "OUTF": 
                        procesarSalida(terceto);
                        break;
                    case "ItoH":
                        conversionHI(terceto);
                        break;
                    case "HtoI":
                        conversionHI(terceto);
                        break;
                    case "ItoF":
                        toFloat(terceto);
                        break;
                    case "HtoF":
                        toFloat(terceto);
                        break;
                    case "FtoI":
                        floattoHexaorInt(terceto);
                        break;
                    case "FtoH":
                        floattoHexaorInt(terceto);
                        break;
                    default:
                        break;
                }
                System.out.println(terceto);
            }
            generarHeader();
            for (StringBuilder datos : funciones) {
                escritor.append(datos);
            }
            escritor.append("START:"+ "\n");
            escritor.append("\t" + "FINIT" + "\n");
            escritor.append(data);
            escritor.append("FIN:"+ "\n");
            escritor.append("\t" + "INVOKE ExitProcess, 0" + "\n");
            escritor.append("errorDivCero:"+ "\n");
            escritor.append("\t" +"INVOKE printf, ADDR mensajeErrorDivCero"+ "\n");
            escritor.append("\t" +"JMP FIN"+ "\n");
            escritor.append("errorOverflow:"+ "\n");
            escritor.append("\t" +"INVOKE printf, ADDR mensajeErrorOverflow"+ "\n");
            escritor.append("\t" +"JMP FIN"+ "\n");
            escritor.append("errorFueraDeRango:"+ "\n");
            escritor.append("\t" +"INVOKE printf, ADDR mensajeErrorFueraDeRango"+ "\n");
            escritor.append("\t" +"JMP FIN"+ "\n");
            escritor.append("END START");
            escritor.flush();
        }catch(IOException e){
            System.out.println("Error al abrir el archivo de esritura: " + e.getMessage());
            System.exit(1);
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
                        tipoMemoria = "REAL4";
                    }else{
                        tipoMemoria = "DD";
                    }
                    if (lexema.startsWith("@")){                                //auxiliar
                        escritor.write("\t" + lexema + " "+ tipoMemoria + " ?\n");
                    }else if((contexto.getUso().equals("nombre de subtipo"))){
                            escritor.write("\t_rangoInf" + contexto.getTypedef() + " " + tipoMemoria + " " + contexto.getLimiteInf() + "\n");
                            escritor.write("\t_rangoSup" + contexto.getTypedef() + " " + tipoMemoria + " " + contexto.getLimiteSup() + "\n");
                    }else {
                        if (contexto.getTipo() == TablaTipoToken.getTipoToken("SINGLE") && !contexto.getDeclarado()) //se deben declarar los floats para operar
                            valorInicializacion = lexema;                                                   
                        escritor.write("\t" + "_" + lexema.replace(":", "_").replace(".","f").replace("-", "m").replace("+", "M") + " "+ tipoMemoria + " " + valorInicializacion.replace("s", "e") + "\n");
                    }
                }
                if (lexema.contains("[")) escritor.write("\t" + mapeoMultilineaData.get(lexema) + " DB \"" + lexema.replaceAll("[_\\[\\]]", "") +"\", 0\n");
            }
            escritor.append("\t" + "__new_line__ DB 13, 10, 0"+ "\n");
            escritor.append("\t" + "@imprimirFloat DQ ?"+ "\n");
            escritor.append("\t" + "mensajeErrorDivCero db \"Error: Division por cero\", 10, 0" + "\n");
            escritor.append("\t" + "mensajeErrorOverflow db \"Error: Overflow en suma entre puntos flotantes\", 10, 0" + "\n");
            escritor.append("\t" + "maxFloat REAL4 3.402e38" + "\n");
            escritor.append("\t" + "minFloat REAL4 -3.402e38" + "\n");
            escritor.append("\t" + "mensajeErrorFueraDeRango db \"Error: Valor fuera de rango del subtipo\", 10, 0" + "\n");
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
                //if (terceto.getTipo() == TablaTipoToken.getTipoToken("HEXADECIMAL")){//CONSTANTES HEXA            //CHEEECK
                //    if (valor.startsWith("0x")) valor = valor.substring(2) + "h";   
                //    else if (valor.startsWith("-0x")) valor = "-" + valor.substring(3) + "h";
                //}
                data.append("\t" +"MOV " + variable.replace(":", "_") + ", " + valor.replace(":", "_") + "\n");
            };
        } else{
            if(valor.startsWith("@") || valor.startsWith("_")){
                data.append("\t" +"FLD " + valor.replace(":", "_").replace(".","f").replace("-", "m").replace("+", "M") + "\n");
            }else{
                data.append("\t" +"FLD _" + valor.replace(":", "_").replace(".","f").replace("-", "m").replace("+", "M") + "\n");
            }
            data.append("\t" +"FSTP " + variable.replace(":", "_") + "\n");
        }
    }

    // Procesar tercetos de suma
    private static void procesarSuma(Terceto terceto) {
        String resultado = procesarOperacionSumaResta("ADD", terceto);
        terceto.setResultado(resultado);
        TablaDeSimbolos.agregarSimbolo(resultado, terceto.getTipo(), "variable auxiliar");
    }

    // Procesar tercetos de resta
    private static void procesarResta(Terceto terceto) {
        String resultado = procesarOperacionSumaResta("SUB", terceto);
        terceto.setResultado(resultado); 
        TablaDeSimbolos.agregarSimbolo(resultado, terceto.getTipo(), "variable auxiliar");
    }
    // Procesar operaciones binarias usando un tipo de operación assembler (ADD, SUB, MUL, DIV)
    private static String procesarOperacionSumaResta(String operacion, Terceto terceto) {
        String operando1 = obtenerValor(terceto.getT2());
        String operando2 = obtenerValor(terceto.getT3());
        String variableAux = "@aux" + (++contadorAux);
        if (terceto.getTipo() == TablaTipoToken.getTipoToken("LONGINT") || terceto.getTipo() == TablaTipoToken.getTipoToken("HEXADECIMAL")){
            data.append("\t" +"MOV EAX, " + operando1.replace(":", "_") + "\n");      // Cargar arg1 en AX
            data.append("\t" +operacion + " EAX, " + operando2.replace(":", "_") + "\n"); // Realizar operación en AX
            
            if (operando1.charAt(operando1.length()-1) == 'h') operando1 = "0x" + operando1.substring(0, operando1.length()-1);
            if (operando2.charAt(operando2.length()-1) == 'h') operando2 = "0x" + operando2.substring(0, operando2.length()-1);
            if (operando1.contains("-")) operando1 = "-" + operando1.replace("-", "");
            if (operando2.contains("-")) operando2 = "-" + operando2.replace("-", "");
            if (TablaDeSimbolos.getContexto(operando1.replace("_", "")).getTypedef() != null){
                String typedef = TablaDeSimbolos.getContexto(operando1.replace("_", "")).getTypedef();
                data.append("\t" + "CMP EAX, _rangoInf" + typedef + "\n");
                data.append("\t" + "JL errorFueraDeRango" + "\n");
                data.append("\t" + "CMP EAX, _rangoSup" + typedef + "\n");
                data.append("\t" + "JG errorFueraDeRango" + "\n");
            }else if (TablaDeSimbolos.getContexto(operando2.replace("_", "")).getTypedef() != null){
                String typedef = TablaDeSimbolos.getContexto(operando2.replace("_", "")).getTypedef();
                data.append("\t" + "CMP EAX, _rangoInf" + typedef + "\n");
                data.append("\t" + "JL errorFueraDeRango" + "\n");
                data.append("\t" + "CMP EAX, _rangoSup" + typedef + "\n");
                data.append("\t" + "JG errorFueraDeRango" + "\n");
            }
            data.append("\t" +"MOV " + variableAux + ", EAX" + "\n");  // Guardar en variable temporal
        }else{
            //if (operando1.contains(".")) operando1 = "_" + operando1.replace(".", "f").replace("-", "m").replace("+", "");
            //if (operando2.contains(".")) operando2 = "_" + operando2.replace(".", "f").replace("-", "m").replace("+", "");
            data.append("\t" +"FLD " + operando1.replace(":", "_") + "\n");      // Cargar operando1 en ST(0)
            data.append("\t" +"FLD " + operando2.replace(":", "_") + "\n");      // Cargar operando2 en ST(0)
            data.append("\t" +"F"+ operacion + "\n"); // Realizar opercion entre operando2 y ST(0), guarda resultado en ST(0)
            if (operacion.equals("ADD")){
                data.append("\t" + "FCOM maxFloat" + "\n");
                data.append("\t" + "FSTSW ax" + "\n");
                data.append("\t" + "SAHF" + "\n");
                data.append("\t" + "JG errorOverflow" + "\n");
                data.append("\t" + "FCOM minFloat" + "\n");
                data.append("\t" + "FSTSW ax" + "\n");
                data.append("\t" + "SAHF" + "\n");
                data.append("\t" + "JL errorOverflow" + "\n");
            }
            if (operando1.replace("_", "").matches("^[0-9].*")) operando1 = operando1.replace("f", ".").replace("m", "-").replace("M", "+");
            if (operando2.replace("_", "").matches("^[0-9].*")) operando2 = operando2.replace("f", ".").replace("m", "-").replace("M", "+");
            if (!TablaDeSimbolos.getContexto(operando1.replace("_", "")).getTypedef().equals("null")){
                String typedef = TablaDeSimbolos.getContexto(operando1.replace("_", "")).getTypedef();
                data.append("\t" + "FCOM _rangoInf" + typedef + "\n");
                data.append("\t" + "FSTSW ax" + "\n");
                data.append("\t" + "SAHF" + "\n");
                data.append("\t" + "JL errorFueraDeRango" + "\n");
                data.append("\t" + "FCOM _rangoSup" + typedef + "\n");
                data.append("\t" + "FSTSW ax" + "\n");
                data.append("\t" + "SAHF" + "\n");
                data.append("\t" + "JG errorFueraDeRango" + "\n");
            }else if (!TablaDeSimbolos.getContexto(operando2.replace("_", "")).getTypedef().equals("null")){
                String typedef = TablaDeSimbolos.getContexto(operando2.replace("_", "")).getTypedef();
                data.append("\t" + "FCOM _rangoInf" + typedef + "\n");
                data.append("\t" + "FSTSW ax" + "\n");
                data.append("\t" + "SAHF" + "\n");
                data.append("\t" + "JL errorFueraDeRango" + "\n");
                data.append("\t" + "FCOM _rangoSup" + typedef + "\n");
                data.append("\t" + "FSTSW ax" + "\n");
                data.append("\t" + "SAHF" + "\n");
                data.append("\t" + "JG errorFueraDeRango" + "\n");
            }
            data.append("\t" +"FSTP " + variableAux + "\n");  // Guardar ST(0) en variable auxiliar y vacia la pila
        }
        return variableAux;
    }
    

    // Procesar tercetos de multiplicación
    private static void procesarMultiplicacion(Terceto terceto) {
        String resultado = procesarOperacionMultiplicacionDivision("MUL", terceto);
        terceto.setResultado(resultado);
        TablaDeSimbolos.agregarSimbolo(resultado, terceto.getTipo(), "variable auxiliar");
    }

    // Procesar tercetos de división
    private static void procesarDivision(Terceto terceto) {
        String resultado = procesarOperacionMultiplicacionDivision("DIV", terceto);
        terceto.setResultado(resultado);
        TablaDeSimbolos.agregarSimbolo(resultado, terceto.getTipo(), "variable auxiliar");
    }

    private static String procesarOperacionMultiplicacionDivision(String operacion, Terceto terceto) {
        String operando1 = obtenerValor(terceto.getT2());
        String operando2 = obtenerValor(terceto.getT3());
        String variableAux = "@aux" + (++contadorAux);
        if (terceto.getTipo() == TablaTipoToken.getTipoToken("LONGINT") || terceto.getTipo() == TablaTipoToken.getTipoToken("HEXADECIMAL")){
            //if (operando1.startsWith("0x")) operando1 = (operando1.substring(2) + "h");   //CONSTANTE HEXA
            //else if (operando1.startsWith("-0x")) operando1 = ("-" + operando1.substring(3) + "h"); //CONSTANTE HEXA
            //if (operando2.startsWith("0x")) operando2 = (operando2.substring(2) + "h");   //CONSTANTE HEXA
            //else if (operando2.startsWith("-0x")) operando2 =  ("-" + operando2.substring(3) + "h"); //CONSTANTE HEXA
            data.append("\t" +"MOV EAX, " + operando1.replace(":", "_") + "\n");      // Cargar arg1 en AX
            data.append("\t" +"CDQ" + "\n");      // EXTIENDE SIGNO
            data.append("\t" +"MOV ECX, " + operando2.replace(":", "_") + "\n");      // Cargar arg1 en AX
            if (operacion.equals("DIV")){
                data.append("\t" +"CMP ECX, 0" + "\n");
                data.append("\t" +"JE errorDivCero" + "\n");
            }
            data.append("\tI" + operacion + " ECX" + "\n"); // Realizar operación en AX
            if (operando1.charAt(operando1.length()-1) == 'h') operando1 = "0x" + operando1.substring(0, operando1.length()-1);
            if (operando2.charAt(operando2.length()-1) == 'h') operando2 = "0x" + operando2.substring(0, operando2.length()-1);
            if (operando1.contains("-")) operando1 = "-" + operando1.replace("-", "");
            if (operando2.contains("-")) operando2 = "-" + operando2.replace("-", "");
            if (TablaDeSimbolos.getContexto(operando1.replace("_", "")).getTypedef() != null){
                String typedef = TablaDeSimbolos.getContexto(operando1.replace("_", "")).getTypedef();
                data.append("\t" + "CMP EAX, _rangoInf" + typedef + "\n");
                data.append("\t" + "JL errorFueraDeRango" + "\n");
                data.append("\t" + "CMP EAX, _rangoSup" + typedef + "\n");
                data.append("\t" + "JG errorFueraDeRango" + "\n");
            }else if (TablaDeSimbolos.getContexto(operando2.replace("_", "")).getTypedef() != null){
                String typedef = TablaDeSimbolos.getContexto(operando2.replace("_", "")).getTypedef();
                data.append("\t" + "CMP EAX, _rangoInf" + typedef + "\n");
                data.append("\t" + "JL errorFueraDeRango" + "\n");
                data.append("\t" + "CMP EAX, _rangoSup" + typedef + "\n");
                data.append("\t" + "JG errorFueraDeRango" + "\n");
            }
            data.append("\t" +"MOV " + variableAux + ", EAX" + "\n");  // Guardar en variable temporal la parte baja de 32 bits
        }else{
            //if (operando1.contains(".")) operando1 = "_" + operando1.replace(".", "f").replace("-", "m").replace("+", "");
            //if (operando2.contains(".")) operando2 = "_" + operando2.replace(".", "f").replace("-", "m").replace("+", "");
            data.append("\t" +"FLD " + operando1.replace(":", "_") + "\n");      // Cargar operando1 en ST(0)
            data.append("\t" +"FLD " + operando2.replace(":", "_") + "\n");      // Cargar operando2 en ST(0)
            if (operacion.equals("DIV")){
                data.append("\t" +"FTST"+ "\n"); 
                data.append("\t" +"JE errorDivCero" + "\n");
            }
            data.append("\t" +"F"+ operacion + "\n"); // Realizar opercion entre operando2 y ST(0), guarda resultado en ST(0)
            if (operando1.replace("_", "").matches("^[0-9].*")) operando1 = operando1.replace("f", ".").replace("m", "-").replace("M", "+");
            if (operando2.replace("_", "").matches("^[0-9].*")) operando2 = operando2.replace("f", ".").replace("m", "-").replace("M", "+");
            Contexto aux1 = TablaDeSimbolos.getContexto(operando1.replace("_", ""));
            Contexto aux2 = TablaDeSimbolos.getContexto(operando2.replace("_", ""));
            if (aux1.getTypedef() != null){
                System.out.println("dentro1");
                String typedef = aux1.getTypedef();
                data.append("\t" + "FCOM _rangoInf" + typedef + "\n");
                data.append("\t" + "FSTSW ax" + "\n");
                data.append("\t" + "SAHF" + "\n");
                data.append("\t" + "JL errorFueraDeRango" + "\n");
                data.append("\t" + "FCOM _rangoSup" + typedef + "\n");
                data.append("\t" + "FSTSW ax" + "\n");
                data.append("\t" + "SAHF" + "\n");
                data.append("\t" + "JG errorFueraDeRango" + "\n");
            }else if (aux1.getTypedef() != null){
                System.out.println("dentro2");
                String typedef = aux2.getTypedef();
                data.append("\t" + "FCOM _rangoInf" + typedef + "\n");
                data.append("\t" + "FSTSW ax" + "\n");
                data.append("\t" + "SAHF" + "\n");
                data.append("\t" + "JL errorFueraDeRango" + "\n");
                data.append("\t" + "FCOM _rangoSup" + typedef + "\n");
                data.append("\t" + "FSTSW ax" + "\n");
                data.append("\t" + "SAHF" + "\n");
                data.append("\t" + "JG errorFueraDeRango" + "\n");
            }
            System.out.println("afuera");
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

    private static String procesarComparacion(String operacion, Terceto terceto){
        String operando1 = obtenerValor(terceto.getT2());
        String operando2 = obtenerValor(terceto.getT3());
        String variableAux = "@aux" + (++contadorAux);
        if ((TablaDeSimbolos.getContexto(operando1.replace("_", "")).getTipo()  == TablaTipoToken.getTipoToken("SINGLE")) || (TablaDeSimbolos.getContexto(operando2.replace("_", "")).getTipo()  == TablaTipoToken.getTipoToken("SINGLE"))){
            //if (operando1.contains(".")) operando1 = "_" + operando1.replace(".", "f").replace("-", "m").replace("+", "");
            //if (operando2.contains(".")) operando2 = "_" + operando2.replace(".", "f").replace("-", "m").replace("+", "");
            data.append("\t" +"FLD " + operando1.replace(":", "_") + "\n");      // Cargar operando1 en ST(0)
            data.append("\t" +"FLD " + operando2.replace(":", "_") + "\n");      // Cargar operando2 en ST(0)
            data.append("\t" +"FCOMP"+ "\n"); // Realizar opercion entre operando2 y ST(0), guarda resultado en ST(0)
        } else{
            //if (!operando1.startsWith("_")) operando1 = operando1.substring(2) + "h";   //CONSTANTES HEXA
            //if (!operando2.startsWith("_")) operando2 = operando2.substring(2) + "h";   //CONSTANTES HEXA
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
            if ( tipo == TablaTipoToken.getTipoToken("LONGINT")){
                data.append("\t" +"INVOKE printf, cfm$(\"%d\\n\"), "+ valor.replace(":", "_")+ "\n");
            }else if (tipo == TablaTipoToken.getTipoToken("HEXADECIMAL")){
                data.append("\t" +"INVOKE printf, cfm$(\"0x%08X\\n\"), "+ valor.replace(":", "_")+ "\n");
            }else if ( tipo == TablaTipoToken.getTipoToken("SINGLE")){
                data.append("\t" +"FLD  " + valor.replace(":", "_" ) + "\n");
	            data.append("\t" +"FSTP @imprimirFloat" + "\n");
                data.append("\t" +"INVOKE printf, cfm$(\"%.20Lf\\n\"), @imprimirFloat"+ "\n");
            }else{  //debe ser cadena multilinea
                data.append("\t" +"INVOKE printf, ADDR "+ mapeoMultilineaData.get(valor.replace("_", "")) + "\n");
                data.append("\t" +"INVOKE printf, ADDR __new_line__"+ "\n");
            }
        }
    }

    // Obtener el valor directo o de la tabla de símbolos si es una referencia
    private static String obtenerValor(String valor) {
        if (valor.startsWith("^")){
            return Parser.tercetos.get(Integer.parseInt(valor.substring(1))).getResultado();
        }

        if (valor.startsWith("[")) return "_" + valor;
        if (!valor.startsWith("-") && !valor.matches("^[0-9].*")){
            return "_" + encontrarAmbito(valor);
        } 
        //constante
        if (valor.startsWith("0x")) return (valor.substring(2) + "h");   //CONSTANTE HEXA
        else if (valor.startsWith("-0x")) return ("-" + valor.substring(3) + "h"); //CONSTANTE HEXA

        if (valor.contains(".")) return ("_" + valor.replace(".", "f").replace("-", "m").replace("+", "M")); //CONSTANTE FLOAT
        
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

    public static void procesarInvocacion(Terceto terceto){
        String pasaje = obtenerValor(terceto.getT3());
        String aux = pasaje.replace("_","");
        String parametro = obtenerParametro(terceto.getT2());
        if (TablaDeSimbolos.getContexto(aux).getTipo() == TablaTipoToken.getTipoToken("SINGLE")){
            data.append("\t" + "FLD " + pasaje.replace(":", "_") + "\n");
            data.append("\t" + "FSTP _" + parametro.replace(":", "_") + "\n");
        } else{
            data.append("\t" + "MOV EAX, " + pasaje.replace(":", "_") + "\n");
            data.append("\t" + "MOV _" + parametro.replace(":", "_") + ", EAX" + "\n");
        }
        data.append("\t" + "CALL " + terceto.getT2() + "\n");
       
        String variableAux = "@aux" + (++contadorAux);
        int tipoFuncion = TablaDeSimbolos.getContexto(terceto.getT2()+cargarAmbito()).getTipo();
        if (tipoFuncion == TablaTipoToken.getTipoToken("SINGLE")){      
            data.append("\t" + "FSTP " + variableAux.replace(":", "_") + "\n"); ///CHECK
        }else{
            data.append("\t" + "MOV " + variableAux.replace(":", "_") +", EAX " + "\n");
        }
        terceto.setResultado(variableAux);
        TablaDeSimbolos.agregarSimbolo(variableAux, tipoFuncion, "variable auxiliar");
    };
    public static String obtenerParametro(String nombreFuncion){
        nombreFuncion = nombreFuncion + cargarAmbito();

        // GET PARAMETRO
        int index = nombreFuncion.indexOf(":");
        String primeraParte = nombreFuncion.substring(0, index);
        String ambitoParametro = nombreFuncion.substring(index);
        ambitoParametro = ambitoParametro + ":" + primeraParte;
        String parametro = TablaDeSimbolos.getContexto(nombreFuncion).getNombreParametro() + ambitoParametro;
        return parametro;
    }

    public static void toFloat(Terceto terceto){
        String valor = obtenerValor(terceto.getT2());
        data.append("\t" + "FILD " + valor.replace(":", "_") + "\n");
        String variableAux = "@aux" + (++contadorAux);
        data.append("\t" +"FSTP " + variableAux + "\n");
        terceto.setResultado(variableAux);
        TablaDeSimbolos.agregarSimbolo(variableAux, terceto.getTipo(), "variable auxiliar");
    }
    public static void floattoHexaorInt(Terceto terceto){
        String valor = obtenerValor(terceto.getT2());
        data.append("\t" + "FLD " + valor.replace(":", "_") + "\n");  
        String variableAux = "@aux" + (++contadorAux);
        data.append("\t" + "FIST " + variableAux + "\n");
        terceto.setResultado(variableAux);
        TablaDeSimbolos.agregarSimbolo(variableAux, terceto.getTipo(), "variable auxiliar");
    }
    public static void conversionHI(Terceto terceto){
        String valor = obtenerValor(terceto.getT2());
        terceto.setResultado(valor);
    }
}
