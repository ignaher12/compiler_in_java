package parser;

public class Terceto {
    private String t1;
    private String t2;
    private String t3;
    private int tipo = -1;
    private String resultado = null;
  
    public Terceto(String t1, String t2, String t3){
      this.t1 = t1;
      this.t2 = t2;
      this.t3 = t3;
    }

    public String getT1(){
        return this.t1;
    }
    public String getT2(){
        return this.t2;
    }
    public void setT2(String t2){
        this.t2 = t2;
    }
    public int getTipo(){
        return this.tipo;
    }
    public void setTipo(int tipo){
        this.tipo = tipo;
    }
    public String getResultado(){
        return this.resultado;
    }
    public void setResultado(String resultado){
        this.resultado = resultado;
    }
    public String getT3(){
        return this.t3;
    }
    public void setT3(String t3){
        this.t3 = t3;
    }
    public String toString(){
        return "(" + t1 + ", " + t2 + ", " + t3 + ") tipo: " + tipo + " resultado: " + resultado;
    }
}
