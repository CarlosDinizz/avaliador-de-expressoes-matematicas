package services;
import domain.Fila;
import domain.Pilha;
import domain.Variavel;
import java.util.Scanner;

public class EntradaDados {
    public static Variavel[] variaveis = new Variavel[26]; //array de variaveis, que vai armazenar valores de A a Z
    private static boolean gravando = false;
    private static Fila<String> gravador = new Fila <>();

    public static void executaAEntradaDeDados() throws Exception{
        Scanner scanner = new Scanner(System.in);

        //loop vai parar apenas quando o usuario digitar exit
        while (true) {
            System.out.print("> ");
            String entrada = scanner.nextLine().trim().toUpperCase();

            if (entrada.equals("EXIT")) {
                break;

            } else if (entrada.equals("VARS")) {
                if (gravando) {
                    System.out.println("(REC: " + (gravador.totalElementos() + 1) + "/10)");
                    vars(gravador);
                }

                listarVariaveis();

            } else if (entrada.equals("RESET")) {
                //se o usuario digitar reset, a lista de variaveis sera resetada
                if (gravando){
                    gravacao(entrada, gravador);
                }

                resetarVariaveis();

            } else if (entrada.contains("=")) {
                //se a entrada tiver o simbolo de igual, significa que o usuario quer atribuir um valor a uma variavel
                if (gravando){
                    gravacao(entrada, gravador);
                }

                atribuirValor(entrada);

            } else if (entrada.equals("REC")){
                if (gravando == false) { // Verifica se já não está gravando
                    gravando = true;
                    System.out.println("Iniciando gravação... (REC: 0/10)");
                } else {
                    System.out.println("Erro: gravação já está ativa.");
                }

            } else if (entrada.equals("STOP")){
                if (gravando) {
                    gravando = false;
                    System.out.println("Encerrando gravação...");
                } else {
                    System.out.println("Erro: gravação não está ativa.");
                }

            } else if (entrada.equals("PLAY")){
                if (gravando == true) {
                    //se estiver gravando, ignora
                    System.out.println("Erro: comando inválido para gravação.");
                } else {
                    exibirGravacao(gravador);
                }

            } else if (entrada.equals("ERASE")){
                if (gravando == true) {
                    //se estiver gravando, ignora
                    System.out.println("Erro: comando inválido para gravação.");
                } else {
                    apagarGravacao(gravador);
                }

            } else {
                //se for qualquer outra coisa, processa como uma expressao
                executaAExpressao(entrada);
            }
        }
        scanner.close();
    }

    //metodo para listar todas as variaveis que foram criadas
    private static void listarVariaveis() {
        boolean variavelAtiva = false;
        //passa por todas as variaveis
        for (Variavel variavel : variaveis) {
            //se a variavel nao for null, ou seja, se foi definida, imprime o valor dela
            if (variavel != null) {
                System.out.println(variavel);
                variavelAtiva = true;
            }
            
        if (variavelAtiva = false) {
            System.out.println("Nenhuma variável definida.");
        }
        }
    }

    //metodo para resetar todas as variaveis
    private static void resetarVariaveis() {
        variaveis = new Variavel[26]; //cria um novo array vazio
        System.out.println("Variáveis reiniciadas.");
    }

    //metodo para atribuir um valor a uma variavel
    private static void atribuirValor(String entrada) {
        entrada = entrada.replace(" ", ""); //remove os espacos
        int simboloIgual = entrada.indexOf('='); //encontra a posicao do sinal de igual

        //se a variavel for valida e tiver algo apos o igual, faz o processo de atribuir
        if (simboloIgual > 0 && simboloIgual < entrada.length() - 1) {
            char variavel = entrada.charAt(0); //a primeira letra eh a variavel
            String valorDaVariavel = entrada.substring(simboloIgual + 1); //o que vem depois do igual e o valor

            try {
                //tenta converter o valor para double
                double valor = Double.parseDouble(valorDaVariavel);
                //verifica se o caractere que representa a variavel eh uma letra
                if (Character.isLetter(variavel)) {
                    //calcula o indice da variavel (A=0, B=1, ..., Z=25)
                    int localVariavel = Character.toUpperCase(variavel) - 'A';
                    variaveis[localVariavel] = new Variavel(variavel, valor); //atribui a variavel com o valor
                    System.out.println(variavel + " = " + valor);
                } else {
                    System.out.println("Erro: comando inválido.");
                }
            //se o valor nao for um numero valido
            } catch (NumberFormatException e) {
                System.out.println("Erro: valor inválido.");
            }
        //se a entrada for invalida (sem o igual ou com algo errado)
        } else {
            System.out.println("Erro: expressão inválida.");
        }
    }

    //metodo que vai executar a expressao
    private static void executaAExpressao(String expressao) {
        try {
            String expressaoPosfixa = Conversor.infixaParaPosfixa(expressao);
            Pilha<Character> expressaoPilha = new Pilha<>();
    
            for (int i = 0; i < expressaoPosfixa.length(); i++) {
                expressaoPilha.push(expressaoPosfixa.charAt(i));
            }
    
            Double resultado = CalculaPosfixa.calculaExpressao(expressaoPilha, EntradaDados.getVariaveis());
    
            if (resultado != null) {
                System.out.println(resultado);
    
                // Adiciona a equação com o resultado na fila de gravação
                if (gravando) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(expressao).append(" ").append(resultado);
                    gravador.enqueue(sb.toString());
                    System.out.println("(REC: " + gravador.totalElementos() + "/10)");
                }
            }
        } catch (Exception e) {
            System.out.println("Erro ao calcular a expressão: " + e.getMessage());
        }
    }
    
    
    
    //metodo para pegar o array de variaveis
    public static Variavel[] getVariaveis() {
        return variaveis;
    }
    
    public static void vars(Fila<String> gravador) throws Exception{
        StringBuilder sb = new StringBuilder();
        
        for(Variavel variavel: getVariaveis()){
            if(variavel != null){
                sb.append(variavel.toString() + "\n");;
            }   
        }
        gravador.enqueue(sb.toString());
    }
    
    private static void clear (Fila<String> gravador){
        gravador = null;
        gravador = new Fila<>();
    }

    public static void gravacao (String entrada, Fila<String> gravador) throws Exception {
        if (gravador.isFull()){
            gravando = false;
            return;
        }

        if (gravando && gravador.totalElementos() >= 0) {
            System.out.println("(REC: " + (gravador.totalElementos() + 1) + "/10)");
        }

        if (gravando) {
            gravador.enqueue(entrada);

        }
    }

    private static void exibirGravacao(Fila<String> gravador) {
        Fila<String> auxiliar = new Fila<>();
    
        try {
            if (gravador.isEmpty()) {
                System.out.println("Não há gravação para ser reproduzida.");
                return;
            }
    
            System.out.println("Reproduzindo gravação...");
    
            while (!gravador.isEmpty()) {
                String expressaoComResultado = gravador.dequeue();
                
                // Substitui o espaço antes do resultado por uma quebra de linha
                expressaoComResultado = expressaoComResultado.replaceFirst(" (?=[^ ]+$)", "\n");
                
                System.out.println(expressaoComResultado);
                auxiliar.enqueue(expressaoComResultado);
            }
    
            while (!auxiliar.isEmpty()) {
                gravador.enqueue(auxiliar.dequeue());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    
    

    private static void apagarGravacao (Fila<String> gravador){
        clear(gravador);
    }
}