import domain.Pilha;
import domain.FilaCircular;
import domain.Variavel;
import exceptions.VariavelNaoDefinidaException;
import services.CalculaPosfixa;
import services.EntradaDados;

import java.util.Scanner;

public class App {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        EntradaDados.executaAEntradaDeDados();
        Variavel[] variaveis = EntradaDados.getVariaveis();
        FilaCircular <String> gravador = new FilaCircular<>();
        boolean gravando = false;

        while (true) {

                String entrada = scanner.nextLine().trim().toUpperCase();

                if (entrada.equals("EXIT")) {
                    break;
                }

                if (entrada.isEmpty()) {
                    System.out.println("Erro: Expressão inválida.");
                    return;
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
                }
                catch (VariavelNaoDefinidaException e) {
                    System.out.println(e.getMessage());
                }
                catch (Exception e) {
                    System.out.println("Erro ao calcular expressão: " + e.getMessage());
                }
            }
            scanner.close();
        }
}