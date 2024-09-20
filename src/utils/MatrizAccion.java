package utils;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import lexico.AccionesSemanticas.*;

public class MatrizAccion {
    public static Accion[][] leerMatrizDesdeCSV(String path) {
        String separator = ",";
        int numFilas = 0;
        int numColumnas = 0;
        String line;

        // Leer el archivo CSV para contar filas y columnas
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            boolean firstLine = true;
            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    // Calcular el número de columnas en la primera fila de encabezados
                    String[] columns = line.split(separator);
                    numColumnas = columns.length;
                    firstLine = false;
                } else {
                    numFilas++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        // Inicializar la matriz de acciones
        Accion[][] matriz_accion = new Accion[numFilas][numColumnas - 1];

        // Leer el archivo CSV de nuevo para llenar la matriz de acciones
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            int rowIndex = 0;
            boolean firstLine = true;
            
            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    // Ignorar la primera fila de encabezados
                    firstLine = false;
                    continue;
                }
                
                // Usar la coma como separador
                String[] elements = line.split(separator);

                // Asegurarse de que no se accedan más columnas de las que existen
                for (int colIndex = 0; colIndex < elements.length; colIndex++) { // Ignora la primera columna (número de estado)
                    String element = elements[colIndex].trim();

                    // Mapear el valor del CSV a un objeto AS
                    matriz_accion[rowIndex][colIndex] = mapToASObject(element);
                }
                rowIndex++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return matriz_accion;
    }

    // Function to map CSV values to AS objects
    public static Accion mapToASObject(String value) {

        switch (value) {
            case "AS1":
                return new AS1(); // Assume AS1 is a static object of class AS
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
            default:
                return null; // Or handle default case
        }
    }
}
