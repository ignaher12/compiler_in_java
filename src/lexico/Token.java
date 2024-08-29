package lexico;

public class Token {
    private int token;
    private int atributo; //LEXEMA

    public Token(){
        this.token = 0;
        this.atributo = -1;
    }
    public Token(int token, int atributo){
        this.atributo = atributo;
        this.token = token;
    }
    
    public int getToken() {
        return token;
    }

    public void setToken(int token) {
        this.token = token;
    }

    public int getAtributo() {
        return atributo;
    }

    public void setAtributo(int atributo) {
        this.atributo = atributo;
    }

    public String toString(){
        return "[" + token + "," + atributo + "]";
    }
}
