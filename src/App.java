import lexico.AccionesSemanticas.Accion;
import utils.MatrizAccion;
import utils.MatrizTransicion;
import lexico.AnalizadorLexico;
import lexico.TablaDeSimbolos;
import lexico.TablaTipoToken;

public class App {
    public static void main(String[] args) throws Exception {
        String filePath = "src/MATRIZ DE TRANSICIONES - Hoja 1.csv";

        int[][] matriz = MatrizTransicion.leerMatrizDesdeCSV(filePath);
        filePath = "src/MATRIZ DE TRANSICIONES - Hoja 2.csv";

        Accion[][] matrizAcciones = MatrizAccion.leerMatrizDesdeCSV(filePath);
        
        AnalizadorLexico lex = new AnalizadorLexico("codigoFuente.txt", matriz, matrizAcciones);
        while (!lex.end()){
            System.out.println(lex.getNextToken());
        }

        System.out.println(TablaDeSimbolos.imprimir());

    }
}
