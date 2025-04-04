// Arthur Roldan Slikta 10353847
// Carlos Eduardo Diniz de Almeida 10444407
// Guilherme Clauz Morlina da Silva 10436477

package services;

import domain.Pilha;
import domain.FilaCircular;

public class Conversor {

    public static String infixaParaPosfixa(String expressaoInfixa, FilaCircular <String> gravador, boolean gravando) {
        StringBuilder saida = new StringBuilder();
        Pilha<Character> pilha = new Pilha<>(); //cria uma pilha para armazenar os operadores
        int contAbre = 0;
        int contFecha = 0;

        //percorre todos os caracteres da expressao
        for (int i = 0; i < expressaoInfixa.length(); i++) {
            char caractere = expressaoInfixa.charAt(i);
            
            //se for uma letra, adiciona direto na saida
            //ex: A+B -> A vai direto para a saida
            if (Character.isLetter(caractere)) {
                saida.append(caractere);
                
            //se for um parenteses abrindo, coloca na pilha
            //ex: (A+B) guarda o ( na pilha
            } else if (caractere == '(') {
                contAbre++;
                pilha.push(caractere);

            //se for um parenteses fechando, remove da pilha ate achar o (
            //ex: (A+B) ao encontrar ), tira + e coloca na saida
            } else if (caractere == ')') {
                contFecha++;
                while (!pilha.isEmpty() && pilha.topo() != '(') {
                    saida.append(pilha.pop());
                }
                pilha.pop(); // remove o ( da pilha, pois nao vai para a saida

            } else if (CalculaPosfixa.ehOperacaoAritmetica(caractere)) {  // se for um operador (+, -, *, /, ^)
                //enquanto a pilha não estiver vazia e a prioridade do topo da pilha for maior que a prioridade do caractere, remove e adiciona na saida
                //ex: A+B*C ao chegar no +, * esta na pilha e tem prioridade maior
                while (!pilha.isEmpty() && CalculaPosfixa.prioridade(pilha.topo()) > CalculaPosfixa.prioridade(caractere)) {
                    saida.append(pilha.pop());
                }
                pilha.push(caractere); //adiciona o operador na pilha
            }

        }

        if (contAbre != contFecha) {
            String mensagemErro = "Erro: expressão inválida.";
            if (gravando) {
                try {
                    gravador.enqueue(mensagemErro);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
            System.out.println(mensagemErro);
            return null;
        }

        //remove todos os operadores restantes da pilha e coloca na saida
        while (!pilha.isEmpty()) {
            saida.append(pilha.pop());
        }

        //retorna a expressao convertida para posfixa
        return saida.toString();
    }
}