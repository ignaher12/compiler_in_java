package lexico;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner; 
import java.util.concurrent.atomic.AtomicInteger;
import lexico.AccionesSemanticas.Accion;
import parser.ParserVal;

public class AnalizadorLexico {
    private File fuente;
    private Scanner lector;
    private String linea;
    private AtomicInteger index;

    private int estadoActual;

    private int[][] matrizTransicion;
    private Accion[][] matrizAcciones;

    private int ESTADO_FINAL = 11;

    public AnalizadorLexico(String nombreArchivo, int[][] matrizTransicion, Accion[][] matrizAcciones) {
        this.index = new AtomicInteger(0);
        this.linea = null;
        this.estadoActual = 0;
        this.matrizTransicion = matrizTransicion;
        this.matrizAcciones = matrizAcciones;
        abrirArchivo(nombreArchivo);
    }

    public int getNextToken(ParserVal yyval){
        StringBuilder cadenaCaracteres = new StringBuilder();
        estadoActual = 0;
        Token token = new Token(-1);
        System.out.println("start " + index.get());
        while ( (lector.hasNextLine() || (index.get() < linea.length())) && estadoActual != ESTADO_FINAL) {       

            if (linea.length() != 0){
                char actual = linea.charAt(index.get());
                int columnaMatriz = MapeoCaracteres.getConversion(actual);
                matrizAcciones[estadoActual][columnaMatriz].activar(token, cadenaCaracteres, index, linea); // Activar accion semantica 
                estadoActual = matrizTransicion[estadoActual][columnaMatriz]; // Avanzar al siguiente estadoActual
                
                
                
                if (estadoActual == -1) {
                    throw new IllegalArgumentException("Error lexico (estadoActual = -1), pos = " + index.get() + ", caracter = " + actual);
                }
            
            }   
            
            if (index.get() == linea.length()) {     // Salto de línea
                // Intentar una transición con el carácter de fin de línea (\n)
                if (estadoActual != ESTADO_FINAL){
                    matrizAcciones[estadoActual][MapeoCaracteres.getConversion('\n')].activar(token, cadenaCaracteres, index, linea);
                    estadoActual = matrizTransicion[estadoActual][MapeoCaracteres.getConversion('\n')];
                }
                if (estadoActual == -1) {
                    throw new IllegalArgumentException("Error: Estado -1 alcanzado en la transición del estado " + estadoActual + "con /n");
                }
                if (lector.hasNextLine()){
                    linea = lector.nextLine();
                    index.set(0);
                };
            }
        };

        
        // Verifica quee no se haya llegado a estado final y hace transicion con salto de linea
        if (estadoActual != ESTADO_FINAL) {
            matrizAcciones[estadoActual][MapeoCaracteres.getConversion('\n')].activar(token, cadenaCaracteres, index, linea);
            estadoActual = matrizTransicion[estadoActual][MapeoCaracteres.getConversion('\n')];
            if (estadoActual == -1) {
                throw new IllegalArgumentException("Error: Estado -1 alcanzado en la transición.");
            }
        };
        System.out.println(token);
        if (token.getToken() == 1) yyval = new ParserVal(token.getAtributo().getAtributo());
        return token.getToken();
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

        linea = lector.nextLine();
    }

    public File getFuente() {
        return fuente;
    }

    public int getEstado() {
        return estadoActual;
    }

    public boolean end(){
        System.out.println("index " + index.get());
        System.out.println("linea " + linea.length());
        if (!lector.hasNextLine() && index.get() >=  linea.length()) 
            return true;
        return false;
    }

}
