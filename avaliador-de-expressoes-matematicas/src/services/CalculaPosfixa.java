package services;

import domain.FilaCircular;
import domain.Pilha;
import domain.Variavel;
import exceptions.VariavelNaoDefinidaException;

public class CalculaPosfixa {
    
    //Faz o calculo da expressao posfixa
    //recebe uma pilha os caracteres da expressao
    //e um vetor com os valores das variaveis
    public static Double calculaExpressao(Pilha<Character> expressao, Variavel[] variaveis, FilaCircular<String> gravador, boolean gravando) {
        Pilha<Double> valores = new Pilha<>();
        Pilha<Character> expressaoInversa = new Pilha<>();
        StringBuilder erros = new StringBuilder();
    
        // Inverte a pilha
        while (!expressao.isEmpty()) {
            expressaoInversa.push(expressao.pop());
        }
    
        // Faz o cálculo
        while (!expressaoInversa.isEmpty()) {
    
            // Pega o caractere do topo
            Character caractere = expressaoInversa.topo();
    
            // Verifica se o caractere é uma operação aritmética
            if (ehOperacaoAritmetica(caractere)) {
    
                // Verifica se é possível fazer a operação. Precisa ter no mínimo 2 valores
                if (erros.length() == 0 && !ehOperacaoValida(valores)) {
                    String mensagemErro = "\nErro: Operação inválida.";
                    if (gravando) {
                        try {
                            gravador.enqueue(mensagemErro);
                        } catch (Exception e) {
                            System.err.println(e.getMessage());
                        }
                    }
                    erros.append(mensagemErro);
                }

                if (erros.length() != 0){
                    throw new RuntimeException(erros.toString());
                }
    
                // Faz a operação e coloca na pilha
                valores.push(realizaOperacaoAritmetica(expressaoInversa.topo(), valores, gravador, gravando));
    
                // Tira o caractere da expressão
                expressaoInversa.pop();
            }
    
            // Vê se não é uma operação de +, -, * ou /
            if (!ehOperacaoAritmetica(caractere)) {

                // Procura se a letra está no vetor de variáveis
                if (!letraEstaPresente(expressaoInversa.topo(), variaveis)) {
                    if (!(expressaoInversa.topo() == '(' || expressaoInversa.topo() == ')')){
                        erros.append("\nVariável ").append(expressaoInversa.topo()).append(" não definida");
                        expressaoInversa.pop();
                    }
                }
    
                // Varre o vetor de variáveis
                for (Variavel variavel : variaveis) {
                    if (variavel == null) continue;
    
                    // Vê se o caractere é igual à letra
                    if (estaDeAcordoComOCaractere(expressaoInversa.topo(), variavel)) {
    
                        // Adiciona o valor da variável na pilha
                        valores.push(variavel.getValor());
    
                        // Remove da pilha de caracteres
                        expressaoInversa.pop();
                    }
                }
            }
        }

        if(erros.length() != 0){
            if (gravando) {
                try {
                    gravador.enqueue(erros.toString());
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            }
            throw new VariavelNaoDefinidaException(erros.toString());
        }

        // Retorna o valor
        return valores.pop();
    }
    
    //vê se é uma operação aritmetica
    private static Double realizaOperacaoAritmetica(Character topo, Pilha<Double> valores, FilaCircular<String> gravador, boolean gravando) {
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
                    String mensagemErro = "Erro: Divisão por zero";
                    if (gravando){
                        try{
                            gravador.enqueue(mensagemErro);
                        }
                        catch(Exception e){
                            System.err.println(e.getMessage());

                        }
                    }
                    System.out.println(mensagemErro);
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

