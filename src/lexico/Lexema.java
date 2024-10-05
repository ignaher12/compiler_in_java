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

    @Override
    public int hashCode() {
        int result = atributo != null ? atributo.hashCode() : 0;
        result = 31 * result + Boolean.hashCode(reservada);
        result = 31 * result + Integer.hashCode(tipo);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Lexema lexema = (Lexema) obj;
        return reservada == lexema.reservada &&
               tipo == lexema.tipo &&
               (atributo != null ? atributo.equals(lexema.atributo) : lexema.atributo == null);
    }
}
