package utils;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import lexico.MapeoCaracteres;

public class MatrizTransicion {

    public static int[][] leerMatrizDesdeCSV(String filePath) {
        String line;
        String separator = ",";
        int numFilas = 18;  // Cantidad de filas del archivo
        int numColumnas = 33;  // Cantidad de columnas del archivo
        int[][] matrizTransicion = new int[numFilas][numColumnas];

        try (InputStream inputStream = MatrizTransicion.class.getClassLoader().getResourceAsStream(filePath)) {
            if (inputStream == null) {
                throw new FileNotFoundException("El archivo no fue encontrado: " + filePath);
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

            // Saltar encabezado
            br.readLine();

            int row = 0;
            while ((line = br.readLine()) != null && row < numFilas) {
                String[] values = line.split(separator);
                for (int col = 0; col < numColumnas && col < values.length; col++) {
                    try {
                        matrizTransicion[row][col] = Integer.parseInt(values[col].trim());
                    } catch (NumberFormatException e) {
                        System.err.println("Valor no entero en fila " + (row + 1) + ", columna " + (col + 1) + ". Se establece en 0.");
                        matrizTransicion[row][col] = 0;
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
        imprimirMatriz(matrizTransicion);
        return matrizTransicion;
    }
    public static void imprimirMatriz(int[][] matriz) {
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[i].length; j++) {
                System.out.print(matriz[i][j] + ",");  // Tabulación para alinear columnas
            }
            System.out.println();  // Nueva línea al final de cada fila
        }
    }
}
