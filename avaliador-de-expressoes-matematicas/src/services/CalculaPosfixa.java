package services;

import domain.Pilha;
import domain.Variavel;

public class CalculaPosfixa {
    
    //Faz o calculo da expressao posfixa
    //recebe uma pilha os caracteres da expressao
    //e um vetor com os valores das variaveis
    public static Double calculaExpressao(Pilha<Character> expressao, Variavel[] variaveis){
        Pilha<Double> valores = new Pilha<>();
        Pilha<Character> expressaoInversa = new Pilha<>();

        //inverte a pilha
        while (!expressao.isEmpty()){
            expressaoInversa.push(expressao.pop());
        }

        //faz o calculo
        while (!expressaoInversa.isEmpty()){

            //pega o caractere do topo
            Character caractere = expressaoInversa.topo();

            //verifica se o caractere é uma operacao aritmetica
            if (ehOperacaoAritmetica(caractere)){

                //verifica se é possivel fazer a operação. Precisa ter no minimo 2 valores
                if (!ehOperacaoValida(valores)){
                    throw new RuntimeException("Operação inválida.");
                }

                //faz a operação e coloca na pilha
                valores.push(realizaOperacaoAritmetica(expressaoInversa.topo(), valores));

                //tira o caractere da expressao
                expressaoInversa.pop();
            }


            //vê se não é uma operação de +, -, * ou /
            if (!ehOperacaoAritmetica(caractere)) {

                //procura se a letra está no vetor de variaveis
                if (!letraEstaPresente(expressaoInversa.topo(), variaveis)) {
                    System.out.println("Variavel " + expressaoInversa.topo() + " não definida.");
                }

                //varre o vetor de variaveis
                for (Variavel variavel: variaveis){
                    if (variavel == null) continue;

                    //vê se o caractere é igual a letra
                    if (estaDeAcordoComOCaractere(expressaoInversa.topo(), variavel)){
                        
                        //adiciona o valor da variavel na pilha
                        valores.push(variavel.getValor());

                        //remove da pilha de caracteres
                        expressaoInversa.pop();
                    }
                }
            }
        }

        //retorna o valor
        return valores.pop();
    }

    //vê se é uma operação aritmetica
    private static Double realizaOperacaoAritmetica(Character topo, Pilha<Double> valores) {
        Double numero;
        Double resultado;

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
                if (numero == 0) {
                    System.out.println("Erro: Divisão por zero.");
                    resultado = null;
                } else {
                    resultado =  valores.pop() / numero;
                }
                break;
            case '^':
                numero = valores.pop();
                double base = valores.pop();
                resultado = 1.0;

                for (int i = 0; i < numero; i++) {
                    resultado *= base;
                }
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + topo);
        }
        return resultado;
    }

    //vê se a pilha tem pelo menos 2 valores
    private static Boolean ehOperacaoValida(Pilha<Double> valores){
        return valores.sizeElements() >= 2;
    }

    //vê se o caractere é igual a letra da variavel
    private static Boolean estaDeAcordoComOCaractere(Character character, Variavel variavel){
        return character.equals(variavel.getLetra());
    }

    //vê se o caractere está presente no array
    private static Boolean letraEstaPresente(Character letraVariavel, Variavel[] variaveis){
        for (Variavel variavel: variaveis){
            if (variavel == null) continue;
            if (variavel.getLetra().equals(letraVariavel)){
                return true;
            }
        }
        return false;
    }

    //vê se é uma operação aritmética
    static Boolean ehOperacaoAritmetica(Character charactere){
        return charactere.equals('/') || charactere.equals('+') || charactere.equals('*') || charactere.equals('-') || charactere.equals('^');
    }

    //vê a prioridade das operações, ^ tem prioridade maior, */ prioridade depois do ^ e +- são os últimos
    public static int prioridade(Character topo) {
        String somaESub = "+-";
        String multEDiv = "*/";
        String expo = "^";

        if (expo.indexOf(topo) != -1) {
            return 3;
        }
        if (multEDiv.indexOf(topo) != -1) {
            return 2;
        }
        if (somaESub.indexOf(topo) != -1){
            return 1; 
        }
        return 0;
    }
}

