package lexico;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner; 
import java.util.concurrent.atomic.AtomicInteger;
import lexico.AccionesSemanticas.Accion;
import parser.Parser;
import parser.ParserVal;

public class AnalizadorLexico {
    private File fuente;
    private Scanner lector;
    private String linea;
    private static int numeroLinea;
    private AtomicInteger index;

    private int estadoActual;

    private int[][] matrizTransicion;
    private Accion[][] matrizAcciones;

    private int ESTADO_FINAL = 11;

    public static final int MAXLENGHTINDENTIFICADOR = 15;
    public static final int MAXLONGINT = 2147483647;                // 2^31 – 1
    public static final int MINLONGINT = -2147483648;                     // -231
    public static final double MAXHEXADECIMAL = 0x7FFFFFFF;
    public static final double MINHEXADECIMAL = -0x80000000 ;
    public static final float MAXFLOATPOSITIVO = 3.40282347e38f;
    public static final float MINFLOATPOSITIVO = 1.17549435e-38f;
    public static final float MAXFLOATNEGATIVO = -1.17549435e-38f;
    public static final float MINFLOATNEGATIVO = -3.40282347e+38f;

    public AnalizadorLexico(String nombreArchivo, int[][] matrizTransicion, Accion[][] matrizAcciones) {
        this.index = new AtomicInteger(0);
        this.linea = null;
        this.numeroLinea = 1;
        this.estadoActual = 0;
        this.matrizTransicion = matrizTransicion;
        this.matrizAcciones = matrizAcciones;
        abrirArchivo(nombreArchivo);
    }

    public int getNextToken(ParserVal yylval){
        StringBuilder cadenaCaracteres = new StringBuilder();
        estadoActual = 0;
        Token token = new Token(-1);

        while ( (lector.hasNextLine() || (index.get() < linea.length())) && estadoActual != ESTADO_FINAL && !token.isError()) {       

            if (linea.length() != 0){
                char actual = linea.charAt(index.get());
                int columnaMatriz = MapeoCaracteres.getConversion(actual);
                System.out.println("leo: " + actual);
                System.out.println("estadoatual:" + estadoActual + "columna: " + columnaMatriz);
                //System.out.println(matrizAcciones[estadoActual][columnaMatriz]);
                matrizAcciones[estadoActual][columnaMatriz].activar(token, cadenaCaracteres, index, linea); // Activar accion semantica 
                estadoActual = matrizTransicion[estadoActual][columnaMatriz]; // Avanzar al siguiente estadoActual
                System.out.println("estadonuevo:" + estadoActual );
            }   
            
            if (index.get() == linea.length() && !token.isError()) {     // Salto de línea
                // Intentar una transición con el carácter de fin de línea (\n)
                if (estadoActual != ESTADO_FINAL ){
                    matrizAcciones[estadoActual][MapeoCaracteres.getConversion('\n')].activar(token, cadenaCaracteres, index, linea);
                    estadoActual = matrizTransicion[estadoActual][MapeoCaracteres.getConversion('\n')];
                }

                if (lector.hasNextLine()){
                    linea = lector.nextLine();
                    numeroLinea = numeroLinea + 1;
                    index.set(0);
                };
            }
        };

        
        // Verifica quee no se haya llegado a estado final y hace transicion con salto de linea
        if (estadoActual != ESTADO_FINAL && !token.isError()) {
            matrizAcciones[estadoActual][MapeoCaracteres.getConversion('\n')].activar(token, cadenaCaracteres, index, linea);
            estadoActual = matrizTransicion[estadoActual][MapeoCaracteres.getConversion('\n')];
        };
        System.out.println("LEX: Token detectado -> " + token + "| " + TablaTipoToken.getClavePorValor(token.getIdentificador()) + " | " + numeroLinea);
        if (token.getIdentificador() == (int)Parser.IDENTIFICADOR || token.getIdentificador() == (int)Parser.HEXADECIMAL || token.getIdentificador() == Parser.CONSTANTE || token.getIdentificador() == (int)Parser.CADENA_MULTI){ 
            if(!token.isError()){
                yylval.ival= token.getReferencia();
                System.out.println("ival pasado | " + token.getReferencia());
            }
        };
        yylval.ival= token.getReferencia();
        return token.getIdentificador();
    }

    private void abrirArchivo(String nombre){
        try {
            this.fuente = new File("tests/"+nombre);
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

    public static int getNumeroLinea(){
        return numeroLinea;
    }

    public boolean end(){
        if (!lector.hasNextLine() && index.get() >=  linea.length()) 
            return true;
        return false;
    }

}
