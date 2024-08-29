package lexico;

import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.Scanner; // Import the Scanner class to read text files
import java.util.ArrayList;
import java.util.HashMap;

public class AnalizadorLexico {
    private File fuente;
    private Scanner lector; // va?????
    private String linea;
    private int index;

    private int estadoActual;

    private int[][] matrizTransicion;
    private HashMap<Integer, Integer> estadosFinales; //HACER SOLO UN ESTADO FINAL
    //private ArrayList<String> palabrasReservadas;
    private HashMap<String, Integer> identificacionToken;

    public AnalizadorLexico(String nombreArchivo, int[][] matrizTransicion, HashMap<Integer, Integer> estadosFinales, HashMap<String, Integer> identificacionToken) {
        this.index = 0;
        this.linea = null;
        this.estadoActual = 0;
        this.matrizTransicion = matrizTransicion;
        this.estadosFinales = estadosFinales;
        this.identificacionToken = identificacionToken;
        abrirArchivo(nombreArchivo);
    }

    public Token getNextToken(){
        StringBuilder cadenaCaracteres = new StringBuilder();
        int TipoToken = -1;
        estadoActual = 0;
        
        while ( (lector.hasNextLine() || index < linea.length()) && !estadosFinales.containsKey(estadoActual)) {       
            char actual = linea.charAt(index);
            int tipo = Character.getType(actual); // 
            
            System.out.println(linea.charAt(index));
            //System.out.println(tipo);
            
            cadenaCaracteres.append(actual);
    
            // Avanzar al siguiente estadoActual
            estadoActual = matrizTransicion[estadoActual][CharTypes.devolverConversion(tipo)];

            
            
            if (estadoActual == -1) {
                throw new IllegalArgumentException("Error: Estado -1 alcanzado en la transición.");
            }
    
            index++; // Avanzar al siguiente carácter
            
            if (index == linea.length()) {     // Salto de línea
                // Intentar una transición con el carácter de fin de línea (\n)
                estadoActual = matrizTransicion[estadoActual][12];
                System.out.println("salto");
                if (estadoActual == -1) {
                    throw new IllegalArgumentException("Error: Estado -1 alcanzado en la transición del estado " + estadoActual + "con /n");
                }
                if (lector.hasNextLine()){linea = lector.nextLine();};
                index = 0;
            }
        };

        Token token = new Token();
        // Verificar si el estadoActual actual es un estadoActual final
        if (estadosFinales.containsKey(estadoActual)) {
            TipoToken = estadosFinales.get(estadoActual);

            if (cadenaCaracteres.charAt(cadenaCaracteres.length()-1) == ' '){ //elimina espacio
                cadenaCaracteres.setLength(cadenaCaracteres.length() - 1);
            };
        }else{
            estadoActual = matrizTransicion[estadoActual][12];
            if (estadoActual == -1) {
                throw new IllegalArgumentException("Error: Estado -1 alcanzado en la transición.");
            }
        };
        
        
        token.setToken(getTipoToken(cadenaCaracteres.toString()));

        //SI ID O CONST AGREGAR A TABLA DE SIMBOLOS Y SET ATRIBUTO
        //token.setAtributo(cadenaCaracteres.toString());

        return token;
    }

    public int getTipoToken(String cadena){
        System.out.println("La cadena es: " +cadena);
        if (!identificacionToken.containsKey(cadena)){ // entonces identificador o constante
            if (Character.isDigit(cadena.charAt(0))){
                System.out.println(identificacionToken.get("_const"));
                return identificacionToken.get("_const"); //CONSTANTE
            }else{
                System.out.println("id");
                return identificacionToken.get("_id"); //IDENTIFICADOR
            }
        }else {
            System.out.println("pal");
            return identificacionToken.get(cadena.toUpperCase()); //PALABRA RESERVADA O SIMBOLO
        }
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
