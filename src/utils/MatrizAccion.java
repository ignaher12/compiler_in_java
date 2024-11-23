package utils;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import lexico.AccionesSemanticas.*;

public class MatrizAccion {
    public static Accion[][] leerMatrizDesdeCSV(String path) {
        String separator = ",";
        int numFilas = 18;
        int numColumnas = 33;
        String line;
    
        // Inicializar la matriz de acciones
        Accion[][] matriz_accion = new Accion[numFilas][numColumnas]; // -1 para ignorar la primera columna (índice de fila)
    
        try (InputStream inputStream = MatrizTransicion.class.getClassLoader().getResourceAsStream(path)) {
            if (inputStream == null) {
                throw new FileNotFoundException("El archivo no fue encontrado: " + path);
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

            // Saltar encabezado
            br.readLine();

            int row = 0;
            while ((line = br.readLine()) != null && row < numFilas) {
                String[] values = line.split(separator);
                for (int col = 0; col < numColumnas && col < values.length; col++) {
                    try {
                        matriz_accion[row][col] = mapToASObject(values[col].trim());
                    } catch (NumberFormatException e) {
                        System.err.println("Valor no entero en fila " + (row + 1) + ", columna " + (col + 1) + ". Se establece en null.");
                        matriz_accion[row][col] = null;
                    }
                }
                row++;
            }

            // Avisar si el archivo tiene menos filas de las esperadas
            if (row < numFilas) {
                System.out.println("Advertencia: El archivo tiene menos de " + numFilas + " filas. Las filas restantes se llenarán con ceros.");
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo: " + e.getMessage());
            e.printStackTrace();
        }
        return matriz_accion;
    }
    
    // Función para mapear los valores del CSV a objetos Accion
    public static Accion mapToASObject(String value) {
        switch (value) {
            case "AS1":
                return new AS1();
            case "AS2":
                return new AS2();
            case "AS3_F":
                return new AS3_F();
            case "AS4_F":
                return new AS4_F();
            case "AS5_F":
                return new AS5_F();
            case "AS6_F":
                return new AS6_F();    
            case "AS7_F":
                return new AS7_F();
            case "AS8_F":
                return new AS8_F();
            case "AS9_F":
                return new AS9_F();
            case "AS10_F":
                return new AS10_F();
            case "ASE":
                return new ASE();
            default:
                return null; // O manejar el caso por defecto
        }
    }
}