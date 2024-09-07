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
            while ((line = br.readLine()) != null) {
                numFilas++;
                String[] columns = line.split(separator);
                numColumnas = Math.max(numColumnas, columns.length);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        
        // Inicializar la matriz después de contar filas y columnas
        int[][] matriz_transicion = new int[numFilas - 1][numColumnas - 1];

        // Leer los datos y almacenarlos en la matriz, ignorando encabezados
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            int row = 0;
            while ((line = br.readLine()) != null) {
                if (row == 0) {
                    // Saltar la primera fila (encabezados de columna)
                    row++;
                    continue;
                }
                String[] columns = line.split(separator);
                for (int col = 2; col < columns.length; col++) { // Ignorar la primera columna
                    String value = columns[col].trim();
                    char cvalue = columns[col].charAt(0);
                    if (!value.isEmpty()) {
                        try {
                            matriz_transicion[row - 1][col - 1] = Integer.parseInt(value);
                        } catch (NumberFormatException e) {
                            matriz_transicion[row - 1][col - 1] = MapeoCaracteres.getConversion(cvalue); // Valor predeterminado para valores no numéricos
                        }
                    } else {
                        matriz_transicion[row - 1][col - 1] = -1; // Valor predeterminado para celdas vacías
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

    public static void main(String[] args) {
        String filePath = "C:/Users/ignah/Desktop/compiler_in_java/src/matriz_transicion.csv"; 
        
        // Llamar al método para leer la matriz desde el archivo CSV
        int[][] matriz = leerMatrizDesdeCSV(filePath);
        
        // Verificar si se leyó correctamente la matriz
        if (matriz != null) {
            // Imprimir la matriz
            for (int i = 0; i < matriz.length; i++) {
                for (int j = 0; j < matriz[i].length; j++) {
                    System.out.print(matriz[i][j] + "\t");
                }
                System.out.println();
            }
        } else {
            System.out.println("Error al leer el archivo CSV.");
        }
    }
}
