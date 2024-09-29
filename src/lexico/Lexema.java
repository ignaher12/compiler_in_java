package lexico;

public class Lexema {
    private String atributo;
    private boolean reservada;
    private int tipo = -3000; 

    public Lexema(String atributo, boolean reservada, int tipo){
        this.atributo = atributo;
        this.reservada = reservada;
        this.tipo = tipo;
    }
    public String getAtributo(){
        return atributo;
    }
    public void setAtributo(String newAtributo){
        this.atributo = newAtributo;
    }
    public boolean isReservada(){
        return reservada;
    }

    public int getTipo(){
        return tipo;
    }
    public void setTipo(int newTipo){
        this.tipo = newTipo;
    }
    public String toString(){
        return "[* "+ atributo + " *]";
    }
}
