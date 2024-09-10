import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import lexico.MapeoCaracteres;

public class MatrizTransicion {

    public static int[][] leerMatrizDesdeCSV(String filePath) {
        String separator = ","; 
        int numFilas = 0;
        int numColumnas = 0;
        
        // Leer el archivo CSV para contar filas y columnas
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
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
        
        // Inicializar la matriz de transiciones
        int[][] matriz_transicion = new int[numFilas][numColumnas - 1];

        // Leer los datos y almacenarlos en la matriz, ignorando encabezados
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            int row = 0;
            boolean firstLine = true;
            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    // Saltar la primera fila (encabezados de columna)
                    firstLine = false;
                    continue;
                }
                
                String[] columns = line.split(separator);
                for (int col = 0; col < columns.length; col++) { // Iniciar desde la columna 1 (ignorar la primera columna)
                    String value = columns[col].trim();
                    char cvalue = columns[col].charAt(0);
                    if (!value.isEmpty()) {
                        try {
                            matriz_transicion[row][col] = Integer.parseInt(value);
                        } catch (NumberFormatException e) {
                            matriz_transicion[row][col] = MapeoCaracteres.getConversion(cvalue); // Manejar valores no numéricos
                        }
                    } else {
                        matriz_transicion[row][col] = -1; // Valor predeterminado para celdas vacías
                    }
                }
                row++;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return matriz_transicion;
    }
}
