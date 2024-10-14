package parser;

public class Terceto {
    private String t1;
    private String t2;
    private String t3;
  
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
    public String getT3(){
        return this.t3;
    }
    public void setT3(String t3){
        this.t3 = t3;
    }
    public String toString(){
        return "(" + t1 + ", " + t2 + ", " + t3 + ")";
    }
}
