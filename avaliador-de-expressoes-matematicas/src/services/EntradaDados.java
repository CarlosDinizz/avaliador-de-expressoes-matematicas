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
                //se o usuario digitar vars, sera listada todas as variaveis ja atribuidas
                if (gravando){
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
                gravando = true;

            } else if (entrada.equals("STOP")){
                gravando = false;

            } else if (entrada.equals("PLAY")){
                exibirGravacao(gravador);

            } else if (entrada.equals("ERASE")){
                apagarGravacao(gravador);

            } else {
                //se for qualquer outra coisa, vamos processar como uma expressao
                executaAExpressao(entrada);
            }
        }
        scanner.close();
    }

    //metodo para listar todas as variaveis que foram criadas
    private static void listarVariaveis() {
        //passa por todas as variaveis
        for (Variavel variavel : variaveis) {
            //se a variavel nao for null, ou seja, se foi definida, imprime o valor dela
            if (variavel != null) {
                System.out.println(variavel);
            }
        }
    }

    //metodo para resetar todas as variaveis
    private static void resetarVariaveis() {
        variaveis = new Variavel[26]; //cria um novo array vazio
        System.out.println("Variáveis resetadas.");
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
                    int indice = Character.toUpperCase(variavel) - 'A';
                    variaveis[indice] = new Variavel(variavel, valor); //atribui a variavel com o valor
                    System.out.println("Variável " + variavel + " armazenada com valor " + valor);
                } else {
                    System.out.println("Entrada inválida");
                }
            //se o valor nao for um numero valido
            } catch (NumberFormatException e) {
                System.out.println("Valor inválido");
            }
        //se a entrada for invalida (sem o igual ou com algo errado)
        } else {
            System.out.println("Erro");
        }
    }


    public static void gravacao (String entrada, Fila<String> gravador) throws Exception {
        
        if (gravador.isFull()){
            gravando = false;
        }
        if (gravador.isEmpty()){
            System.out.println("Iniciando gravação... (REC: 0/10");
        }
        System.out.println("(REC: " + gravador.totalElementos() + "/10)");
        
        if (gravando == true){
            gravador.enqueue(entrada);
        }
    }


    private static void exibirGravacao (Fila<String> gravador){
        Fila<String> auxiliar = new Fila<>();

        try {
            while(!gravador.isEmpty()){
                System.out.println(gravador.front());
                auxiliar.enqueue(gravador.dequeue());
            }

            while (!auxiliar.isEmpty()) {
                gravador.enqueue(auxiliar.dequeue());
            }
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    
    private static void apagarGravacao (Fila<String> gravador){
        clear(gravador);
    }

    //metodo que vai executar a expressao
    private static void executaAExpressao(String expressao) {
        System.out.println("Expressão infixa: " + expressao);

        //converte a expressao infixa para posfixa
        String expressaoPosfixa = Conversor.infixaParaPosfixa(expressao);
        System.out.println("Expressão pós-fixa: " + expressaoPosfixa);

        //cria uma pilha para armazenar os caracteres da expressao
        Pilha<Character> expressaoPilha = new Pilha<>();
        for (int i = 0; i < expressaoPosfixa.length(); i++) {
            expressaoPilha.push(expressaoPosfixa.charAt(i)); //coloca cada caractere na pilha
        }

        //calcula o resultado da expressao posfixa
        Double resultado = CalculaPosfixa.calculaExpressao(expressaoPilha, EntradaDados.getVariaveis());
        System.out.println("Resultado: " + resultado);
    }

    //metodo para pegar o array de variaveis
    public static Variavel[] getVariaveis() {
        return variaveis;
    }


    public static void vars(Fila<String> gravador) throws Exception{
        StringBuilder sb = new StringBuilder();
        
        for(Variavel variavel: getVariaveis()){
            if(variavel == null){
                break;
            }
            sb.append(variavel.toString() + "\n");
            
        }

        gravador.enqueue(sb.toString());
    }

    private static void clear(Fila<String> gravador){
        gravador = null;
        gravador = new Fila<>();
    }
}