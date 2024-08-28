package lexico;

import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.Scanner; // Import the Scanner class to read text files
import java.util.HashMap;

public class AnalizadorLexico {
    private File fuente;
    private Scanner lector; // va?????
    private String linea;
    private int index;

    private int estadoActual;

    private int[][] matrizTransicion;
    private HashMap<Integer, Integer> estadosFinales; //HACER SOLO UN ESTADO FINAL

    public AnalizadorLexico(String nombreArchivo, int[][] matrizTransicion, HashMap<Integer, Integer> estadosFinales) {
        this.index = 0;
        this.linea = null;
        this.estadoActual = 0;
        this.matrizTransicion = matrizTransicion;
        this.estadosFinales = estadosFinales;
        abrirArchivo(nombreArchivo);
    }

    public Token getNextToken(){
        StringBuilder cadenaCaracteres = new StringBuilder();
        int identificador = -1;
        estadoActual = 0;
        
        while ( (lector.hasNextLine() || index < linea.length()) && !estadosFinales.containsKey(estadoActual)) {   
            if (index == linea.length()) {     // Salto de línea
                // Intentar una transición con el carácter de fin de línea (\n)
                estadoActual = matrizTransicion[estadoActual][12];
                if (estadoActual == -1) {
                    throw new IllegalArgumentException("Error: Estado -1 alcanzado en la transición.");
                }
                linea = lector.nextLine();
                index = 0;
            }
            
            char actual = linea.charAt(index);
            int tipo = Character.getType(actual); // 
            
            System.out.println(linea.charAt(index));
            System.out.println(tipo);
            
            cadenaCaracteres.append(actual);
    
            // Avanzar al siguiente estadoActual
            estadoActual = matrizTransicion[estadoActual][CharTypes.devolverConversion(tipo)];

            
            
            if (estadoActual == -1) {
                throw new IllegalArgumentException("Error: Estado -1 alcanzado en la transición.");
            }
    
            index++; // Avanzar al siguiente carácter

        };

        Token token = new Token();
        // Verificar si el estadoActual actual es un estadoActual final
        if (estadosFinales.containsKey(estadoActual)) {
            identificador = estadosFinales.get(estadoActual);

            // Eliminar el último carácter agregado si es necesario
            if (index <= linea.length()) {
                cadenaCaracteres.setLength(cadenaCaracteres.length() - 1);
                //index--; // Retroceder una posición en la línea
            }
        }else{
            estadoActual = matrizTransicion[estadoActual][12];
            if (estadoActual == -1) {
                throw new IllegalArgumentException("Error: Estado -1 alcanzado en la transición.");
            }
        };
        
    
        token.setIdentificador(identificador);
        token.setValor(cadenaCaracteres.toString());

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
