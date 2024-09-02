package lexico;

import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.Scanner; // Import the Scanner class to read text files

import lexico.AccionesSemanticas.Accion;

import java.util.ArrayList;
import java.util.HashMap;

public class AnalizadorLexico {
    private File fuente;
    private Scanner lector; // va?????
    private String linea;
    private int index;

    private int estadoActual;

    private int[][] matrizTransicion;
    private Accion[][] matrizAcciones;

    private HashMap<Integer, Integer> estadosFinales; //HACER SOLO UN ESTADO FINAL
    //private ArrayList<String> palabrasReservadas;

    public AnalizadorLexico(String nombreArchivo, int[][] matrizTransicion, Accion[][] matrizAcciones, HashMap<Integer, Integer> estadosFinales) {
        this.index = 0;
        this.linea = null;
        this.estadoActual = 0;
        this.matrizTransicion = matrizTransicion;
        this.matrizAcciones = matrizAcciones;
        this.estadosFinales = estadosFinales;
        abrirArchivo(nombreArchivo);
    }

    public Token getNextToken(){
        StringBuilder cadenaCaracteres = new StringBuilder();
        estadoActual = 0;
        Token token = new Token(-1);

        while ( (lector.hasNextLine() || index < linea.length()) && !estadosFinales.containsKey(estadoActual)) {       
            char actual = linea.charAt(index);
            cadenaCaracteres.append(actual);
            index++; // Avanzar al siguiente carácter
            
            
            int columnaMatriz = MapeoCaracteres.getConversion(actual);
            //matrizAcciones[estadoActual][columnaMatriz].activar(token, cadenaCaracteres, index); // Activar accion semantica 
            System.out.println(actual);
            System.out.println(columnaMatriz);
            estadoActual = matrizTransicion[estadoActual][columnaMatriz]; // Avanzar al siguiente estadoActual

            
            
            if (estadoActual == -1) {
                throw new IllegalArgumentException("Error: Estado -1 alcanzado en la transición.");
            }
            
            if (index == linea.length()) {     // Salto de línea
                // Intentar una transición con el carácter de fin de línea (\n)
                estadoActual = matrizTransicion[estadoActual][MapeoCaracteres.getConversion('\n')];
                System.out.println("salto");
                if (estadoActual == -1) {
                    throw new IllegalArgumentException("Error: Estado -1 alcanzado en la transición del estado " + estadoActual + "con /n");
                }
                if (lector.hasNextLine()){linea = lector.nextLine();};
                index = 0;
            }
        };

        
        // Verifica quee no se haya llegado a estado final y hace transicion con salto de linea
        if (!estadosFinales.containsKey(estadoActual)) {
            estadoActual = matrizTransicion[estadoActual][MapeoCaracteres.getConversion('\n')];
            if (estadoActual == -1) {
                throw new IllegalArgumentException("Error: Estado -1 alcanzado en la transición.");
            }
        };
        
        
        token.setToken(TablaTipoToken.getTipoToken(TablaTipoToken.IDENTIFICADOR));
        if (token.getToken() == TablaTipoToken.getTipoToken(TablaTipoToken.IDENTIFICADOR)){
            token.setAtributo(TablaDeSimbolos.agregarSimbolo(cadenaCaracteres.toString()));
        }
        System.out.println(token);
        //SI ID O CONST AGREGAR A TABLA DE SIMBOLOS Y SET ATRIBUTO
        //token.setAtributo(cadenaCaracteres.toString());

        return token;
    }

    private void abrirArchivo(String nombre){
        try {
            this.fuente = new File(nombre);
            generarLector();
          } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
          }
    }

    private void generarLector() throws FileNotFoundException{
        this.lector = new Scanner(getFuente());

        linea = lector.nextLine();//Inicializar lectura
    }

    public File getFuente() {
        return fuente;
    }

    public int getEstado() {
        return estadoActual;
    }

}
