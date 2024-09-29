package lexico;

public class Token {
    private int identificador;
    private Lexema lexema; //LEXEMA
    private boolean error = false;
    
    public Token(){
        this.identificador = 0;
        this.lexema = null;
    }
    public Token(int identificador, Lexema lexema){
        this.identificador = identificador;
        this.lexema = lexema;
    }
    public Token(int identificador){
        this.identificador = identificador;
        this.lexema = null;
    }
    
    public int getIdentificador() {
        return identificador;
    }

    public void setIdentificador(int identificador) {
        this.identificador = identificador;
    }

    public Lexema getLexema() {
        return lexema;
    }

    public void setLexema(Lexema lexema) {
        this.lexema = lexema;
    }

    public String toString(){
        return "[" + identificador + "," + lexema + "]";
    }

    public void setError(){
        this.error = true;
    }

    public boolean isError(){
        return error;
    }
}
