package lexico;

public class Token {
    private int token;
    private Lexema lexema; //LEXEMA
    public Token(){
        this.token = 0;
        this.lexema = null;
    }
    public Token(int token, Lexema lexema){
        this.token = token;
        this.lexema = lexema;
    }
    public Token(int token){
        this.token = token;
        this.lexema = null;
    }
    
    public int getToken() {
        return token;
    }

    public void setToken(int token) {
        this.token = token;
    }

    public Lexema getAtributo() {
        return lexema;
    }

    public void setAtributo(Lexema lexema) {
        this.lexema = lexema;
    }

    public String toString(){
        return "[" + token + "," + lexema + "]";
    }
}
