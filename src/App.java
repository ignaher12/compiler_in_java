import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

import lexico.AnalizadorLexico;
import lexico.Lexema;
import lexico.TablaDeSimbolos;
import lexico.AccionesSemanticas.Accion;
import lexico.AccionesSemanticas.AccionPalabra;

public class App {
    public static void main(String[] args) throws Exception {
        System.out.println("Helslo, World!");
        System.out.println("Helslo, World!"+ Character.getType(' '));

        //Hashtable<String> tablaDeSimbolos = new Hashtable<String>()
        //c == letra, numero, otro ////// #, [ ..
        //  CHECK COLUMNAS DE MATRIZ Y CHARTYPES
        int[][] matrizTransicion = {
            //     L,  d, '[', ']',  #,  'x', '.', 's', '+', '-', A..F, ' ',  \n
            /*0*/ {17,  1, 14, -1, 19, -1, -1, -1, -1, -1, -1, -1, -1},
            /*1*/ { 2,  1,  2,  2,  2, 11,  3,  2,  2,  2,  2, 2,  2},
            /*2*/ {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
            /*3*/ {-1,  4, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
            /*4*/ {-1,  4, -1, -1, -1, -1, -1,  5, -1, -1, -1, -1, -1},
            /*5*/ {-1,  6, -1, -1, -1, -1, -1, -1,  8,  8, -1, -1, -1},
            /*6*/ {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
            /*7*/ {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
            /*8*/ {-1,  6, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
            /*9*/ {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
            /*10*/{-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
            /*11*/{-1, 13, -1, -1, -1, -1, -1, -1, -1, -1, 12, -1, -1},
            /*12*/{13, 12, 13, 13, 13, 13, 13, 13, 13, 13, 12, 13, 13},
            /*13*/{-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
            /*14*/{-1, -1, -1, 16, -1, -1, -1, -1, -1, -1, -1, 15, 15},
            /*15*/{15, 15, 15, 16, 15, 15, 15, 15, 15, 15, 15, 15, 15},
            /*16*/{-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
            /*17*/{17, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18},
            /*18*/{-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
            /*19*/{-1, -1, -1, -1, 20, -1, -1, -1, -1, -1, -1, -1, -1},
            /*20*/{20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 21, 21},
            /*21*/{-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1}
        };
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
        AnalizadorLexico lex = new AnalizadorLexico("codigoFuente.txt", matrizTransicion, matrizAcciones, estadosFinales);
        System.out.println(lex.getNextToken());
        System.out.println(lex.getNextToken());
        System.out.println(lex.getNextToken());
        System.out.println(lex.getNextToken());
        System.out.println(lex.getNextToken());
        System.out.println(TablaDeSimbolos.imprimir());

    }
}
