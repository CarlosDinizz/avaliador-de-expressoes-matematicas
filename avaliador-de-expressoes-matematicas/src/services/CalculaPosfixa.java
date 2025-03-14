package services;

import domain.Pilha;
import domain.Variavel;



public class CalculaPosfixa {
    
    //Faz o calculo da expressao posfixa
    //recebe uma pilha os caracteres da expressao
    //e um vetor com os valores das variaveis
    public static Integer calculaExpressao(Pilha<Character> expressao, Variavel[] variaveis){
        Pilha<Integer> valores = new Pilha<>();
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
                    throw new RuntimeException("Operação inválida");
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
                    throw new RuntimeException("Variavel " + expressaoInversa.topo() + " não definida");
                }


                //varre o vetor de variaveis
                for (Variavel variavel: variaveis){

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

    //vê se a pilha tem pelo menos 2 valores
    private static Boolean ehOperacaoValida(Pilha<Integer> valores){
        return valores.sizeElements() + 1 >= 2;
    }

    //vê se o caractere é igual a letra da variavel
    private static Boolean estaDeAcordoComOCaractere(Character character, Variavel variavel){
        return character.equals(variavel.getLetra());
    }

    //vê se o caractere está presente no array
    private static Boolean letraEstaPresente(Character letraVariavel, Variavel[] variaveis){
        for (Variavel variavel: variaveis){
            if (variavel.getLetra().equals(letraVariavel)){
                return true;
            }
        }
        return false;
    }

    //vê se é uma operação aritmética
    private static Boolean ehOperacaoAritmetica(Character charactere){
        return charactere.equals('/') || charactere.equals('+') || charactere.equals('*') || charactere.equals('-');
    }
}

