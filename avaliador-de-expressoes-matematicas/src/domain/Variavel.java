// Arthur Roldan Slikta 10353847
// Carlos Eduardo Diniz de Almeida 10444407
// Guilherme Clauz Morlina da Silva 10436477

package domain;
public class Variavel {
    
    private Character letra;
    
    private Double valor;

    private Variavel(){}

    public Variavel(Character letra, Double valor){
        this.letra = letra;
        this.valor = valor;
    }

    public Character getLetra(){
        return letra;
    }

    public Double getValor(){
        return valor;
    }

    public void setLetra(Character letra){
        this.letra = letra;
    }

    public void setValor(Double valor){
        this.valor = valor;
    }

    @Override
    public String toString(){
        return this.getLetra() + ": " + this.getValor();
    }
}
