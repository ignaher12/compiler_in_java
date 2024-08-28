package lexico;

public class Token {
    int identificador;
    String valor;

    public Token(){
        this.identificador = 0;
        this.valor = null;
    }
    public Token(int identificador, String valor){
        this.valor = valor;
        this.identificador = identificador;
    }
    
    public int getIdentificador() {
        return identificador;
    }

    public void setIdentificador(int identificador) {
        this.identificador = identificador;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String toString(){
        return "[" + identificador + "," + valor + "]";
    }
}
