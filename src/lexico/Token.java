package lexico;

public class Token {
    private int identificador;
    private String referencia; //LEXEMA
    private boolean error = false;
    
    public Token(){
        this.identificador = 0;
        this.referencia = null;
    }
    public Token(int identificador, String referencia){
        this.identificador = identificador;
        this.referencia = referencia;
    }
    public Token(int identificador){
        this.identificador = identificador;
        this.referencia = null;
    }
    
    public int getIdentificador() {
        return identificador;
    }

    public void setIdentificador(int identificador) {
        this.identificador = identificador;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public String toString(){
        return "[" + identificador + "," + referencia + "]";
    }

    public void setError(){
        this.error = true;
    }

    public boolean isError(){
        return error;
    }
}
