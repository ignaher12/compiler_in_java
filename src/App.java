import java.util.HashMap;
import lexico.AccionesSemanticas.Accion;
import lexico.Lexema;
import lexico.TablaDeSimbolos;

public class App {
    public static void main(String[] args) throws Exception {
        System.out.println("Helslo, World!");
        System.out.println("Helslo, World!"+ Character.getType(' '));
        String filePath = "C:/Users/ignah/Desktop/compiler_in_java/src/matriz_transicion.csv";

        int[][] matriz = MatrizTransicion.leerMatrizDesdeCSV(filePath);

        for (int[] matriz1 : matriz) {
            for (int matriz11 : matriz1) {
                System.out.print(matriz11 + "\t");
            }
            System.out.println();
        }

        Accion[][] matrizAcciones = {};

        HashMap<Integer, Integer> estadosFinales = new HashMap<Integer, Integer>();
        estadosFinales.put(2, 99);
        estadosFinales.put(7, 98);
        estadosFinales.put(13, 97);
        estadosFinales.put(18, 96);
        estadosFinales.put(21, 95);
        estadosFinales.put(16, 94);

        Lexema aux = new Lexema("hola");
        Lexema test = TablaDeSimbolos.agregarSimbolo("hola");
        
        System.out.println(aux);
        System.out.println(test);
        TablaDeSimbolos.editarSimbolo(test, "mono");
        System.out.println(aux);
        System.out.println(test);
        System.out.println(TablaDeSimbolos.imprimir());
        // AnalizadorLexico lex = new AnalizadorLexico("codigoFuente.txt", matriz, matrizAcciones, estadosFinales);
        // System.out.println(lex.getNextToken());
        // System.out.println(lex.getNextToken());
        // System.out.println(lex.getNextToken());
        // System.out.println(lex.getNextToken());
        // System.out.println(lex.getNextToken());
        System.out.println(TablaDeSimbolos.imprimir());

    }
}
