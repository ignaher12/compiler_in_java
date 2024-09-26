package parser;

public class Error {
    private int linea;
    private Tipo tipo;
    private String descripcion;

    public static enum Tipo {
        ERROR,
        WARNING;
    }

    public Error(int linea, Tipo tipo, String descripcion) {
        this.linea = linea;
        this.tipo = tipo;
        this.descripcion = descripcion;
    }

    public int getPosicion() {
        return linea;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setLinea(int linea) {
        this.linea = linea;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    @Override
    public String toString() {
        return "Linea" + linea +
                ":" + tipo + 
                ":" + descripcion + '\'' ;
    }
}
