package lexico;

public class Token {
    private int identificador;
    private Integer referencia; //LEXEMA
    private boolean error = false;
    
    public Token(){
        this.identificador = 0;
        this.referencia = -1;
    }
    public Token(int identificador, int referencia){
        this.identificador = identificador;
        this.referencia = referencia;
    }
    public Token(int identificador){
        this.identificador = identificador;
        this.referencia = -1;
    }
    
    public int getIdentificador() {
        return identificador;
    }

    public void setIdentificador(int identificador) {
        this.identificador = identificador;
    }

    public Integer getReferencia() {
        return referencia;
    }

    public void setReferencia(int referencia) {
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
