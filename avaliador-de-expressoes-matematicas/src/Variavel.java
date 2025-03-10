public class Variavel {
    
    private Character letra;
    
    private Integer valor;

    public Variavel(){}

    public Variavel(Character letra, Integer valor){
        this.letra = letra;
        this.valor = valor;
    }

    public Character getLetra(){
        return letra;
    }

    public Integer getValor(){
        return valor;
    }

    public void setLetra(Character letra){
        this.letra = letra;
    }

    public void setValor(Integer valor){
        this.valor = valor;
    }
}
