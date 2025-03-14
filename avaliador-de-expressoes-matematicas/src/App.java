import domain.Pilha;
import domain.Variavel;
import services.CalculaPosfixa;

public class App {
    public static void main(String[] args) throws Exception {

        //Pilha de variáveis
        Variavel[] variaveis = {
                new Variavel('A', 7),
                new Variavel('B', 3),
                new Variavel('C', 6),
                new Variavel('D', 4),
                new Variavel('E', 9)
        };

        //Pilha de Character com a expressão posfixa
        Pilha<Character> expressao = new Pilha<>();
        expressao.push('A');
        expressao.push('B');
        expressao.push('+');
        expressao.push('C');
        expressao.push('D');
        expressao.push('-');
        expressao.push('/');
        expressao.push('E');
        expressao.push('*');

        System.out.println(expressao);

        System.out.println(CalculaPosfixa.calculaExpressao(expressao, variaveis));
    }
}