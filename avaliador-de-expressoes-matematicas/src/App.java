import domain.Pilha;
import domain.Fila;
import domain.Variavel;
import services.CalculaPosfixa;
import services.EntradaDados;

import java.util.Scanner;

public class App {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        EntradaDados.executaAEntradaDeDados();
        Variavel[] variaveis = EntradaDados.getVariaveis();
        Fila <String> gravador = new Fila<>();
        boolean gravando = false;

        while (true) {

            try {
                String entrada = scanner.nextLine().trim().toUpperCase();

                if (entrada == null) {
                    System.out.println("Erro: Expressão inválida.");
                }

                if (entrada.equals("EXIT")) {
                    System.out.println("Saindo...");
                    break;
                }

                Pilha<Character> expressaoPilha = new Pilha<>();
                for (int i = 0; i < entrada.length(); i++) {
                    expressaoPilha.push(entrada.charAt(i));
                }

                try {
                    Double resultado = CalculaPosfixa.calculaExpressao(expressaoPilha, variaveis, gravador, gravando);

                    if (resultado == null) {
                        System.out.println("Erro: Expressão inválida.");
                    } else {
                        System.out.println(resultado);
                    }
                } catch (Exception e) {
                    System.out.println("Erro ao calcular expressão: " + e.getMessage());
                }
            }
            catch(Exception e){
                System.out.println(e.toString());
            }


        }

        scanner.close();
    }
}