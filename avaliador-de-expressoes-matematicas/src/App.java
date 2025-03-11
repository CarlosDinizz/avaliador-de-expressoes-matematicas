import domain.Pilha;

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

        System.out.println(calculaExpressao(expressao, variaveis));
    }

    private static Integer calculaExpressao(Pilha<Character> expressao, Variavel[] variaveis){
        Pilha<Integer> valores = new Pilha<>();
        Pilha<Character> expressaoInversa = new Pilha<>();

        while (!expressao.isEmpty()){
            expressaoInversa.push(expressao.pop());
        }

        while (!expressaoInversa.isEmpty()){

            Character caractere = expressaoInversa.topo();

            if (ehOperacaoAritmetica(caractere)){

                if (!ehOperacaoValida(valores)){
                    throw new RuntimeException("Operação inválida");
                }

                valores.push(realizaOperacaoAritmetica(expressaoInversa.topo(), valores));
                expressaoInversa.pop();
            }


            if (!ehOperacaoAritmetica(caractere)) {

                if (!letraEstaPresente(expressaoInversa.topo(), variaveis)) {
                    throw new RuntimeException("Variavel " + expressaoInversa.topo() + " não definida");
                }

                for (Variavel variavel: variaveis){
                    if (estaDeAcordoComOCaractere(expressaoInversa.topo(), variavel)){
                        valores.push(variavel.getValor());
                        expressaoInversa.pop();
                    }
                }
            }
        }

        return valores.pop();
    }

    private static Integer realizaOperacaoAritmetica(Character topo, Pilha<Integer> valores) {
        Integer numero;
        Integer resultado;

        switch (topo){
            case '+':
                numero = valores.pop();
                resultado = numero + valores.pop();
                break;
            case '-':
                numero = valores.pop();
                resultado = valores.pop() - numero;
                break;
            case '*':
                numero = valores.pop();
                resultado = valores.pop() * numero;
                break;
            case '/':
                numero = valores.pop();
                resultado =  valores.pop() / numero;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + topo);
        }
        
        return resultado;
    }

    private static Boolean ehOperacaoValida(Pilha<Integer> valores){
        return valores.sizeElements() + 1 >= 2;
    }

    private static Boolean estaDeAcordoComOCaractere(Character character, Variavel variavel){
        return character.equals(variavel.getLetra());
    }

    private static Boolean letraEstaPresente(Character letraVariavel, Variavel[] variaveis){
        for (Variavel variavel: variaveis){
            if (variavel.getLetra().equals(letraVariavel)){
                return true;
            }
        }
        return false;
    }

    private static Boolean ehOperacaoAritmetica(Character charactere){
        return charactere.equals('/') || charactere.equals('+') || charactere.equals('*') || charactere.equals('-');
    }
}
