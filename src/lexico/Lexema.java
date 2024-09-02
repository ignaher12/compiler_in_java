package lexico;

public class Lexema {
    private String atributo;
    public Lexema(String atributo){
        this.atributo = atributo;
    }
    public String getAtributo(){
        return atributo;
    }
    public void setAtributo(String newAtributo){
        this.atributo = newAtributo;
    }
    public String toString(){
        return "[* "+ atributo + " *]";
    }
}
