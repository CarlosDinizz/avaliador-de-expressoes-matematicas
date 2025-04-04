package services;

import domain.FilaCircular;
import domain.Pilha;
import domain.Variavel;
import exceptions.VariavelNaoDefinidaException;

import java.util.Scanner;

public class EntradaDados {
    public static Variavel[] variaveis = new Variavel[26]; //array de variaveis, que vai armazenar valores de A a Z
    private static boolean gravando = false;
    private static FilaCircular<String> gravador = new FilaCircular <>(10);


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
                    System.out.println("Iniciando gravação... (REC: " + gravador.totalElementos() + "/10)");
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
                    System.out.println("Gravação apagada.");
                    apagarGravacao(gravador);
                }
        
            } else if (entrada.matches(".[+-//*^() ].")) {
                //se for qualquer outra coisa, processa como uma expressao
                executaAExpressao(entrada);
            }
            else if(contemOperacaoAritmetica(entrada)){
                executaAExpressao(entrada);

            } else if (entrada.length() == 1 && Character.isLetter(entrada.charAt(0))) {
                char variavel = entrada.charAt(0);
                int indice = Character.toUpperCase(variavel) - 'A';
            
                if (variaveis[indice] != null) {
                    System.out.println(variaveis[indice].getValor()); // Exibe o valor armazenado
                } else {
                    System.out.println("Erro: variável " + variavel + " não definida.");
                }

            } else{
                String mensagemErro = "Erro: comando inválido.";
                if (gravando){
                    try{
                        gravador.enqueue(mensagemErro);
                    }
                    catch(Exception e){
                        System.err.println(e.getMessage());
                    }
                }
                System.out.println(mensagemErro);
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
                if (!gravando) {
                    System.out.println(variavel);
                }
                variavelAtiva = true;
            }
        }
        if (variavelAtiva == false) {
            System.out.println("Nenhuma variável definida.");
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

    public static boolean validarExpressao(String expressao) {
        Pilha<Character> pilhaCaracteres = new Pilha<>();
        boolean ultimaFoiLetra = false; // Flag para detectar duas letras seguidas (ex: "ab" é inválido)
    
        // Empilha os caracteres da expressão (do fim para o início)
        for (int i = expressao.length() - 1; i >= 0; i--) {
            pilhaCaracteres.push(expressao.charAt(i));
        }
    
        // Percorre a pilha analisando a sequência dos caracteres
        while (!pilhaCaracteres.isEmpty()) {
            char caracterDaExpressao = pilhaCaracteres.pop();
    
            if (Character.isLetter(caracterDaExpressao)) {
                // Se já havia uma letra antes, é inválido (duas letras seguidas)
                if (ultimaFoiLetra) {
                    return false;
                }
                ultimaFoiLetra = true;
            } else {
                ultimaFoiLetra = false;
            }
        }
    
        // Se não encontrou duas letras seguidas, a expressão é válida
        return true;
    }
    

    //metodo que vai executar a expressao
    private static void executaAExpressao(String expressao) {
        try {
            // Verifica se a expressão é válida
            if (!validarExpressao(expressao)) {
                System.out.println("Erro: expressão inválida.");
                return;
            }
    
            // Converte a expressão de infixa para pós-fixa
            String expressaoPosfixa = Conversor.infixaParaPosfixa(expressao, gravador, gravando);
    
            // Armazena a expressão pós-fixa em uma pilha de caracteres
            Pilha<Character> expressaoPilha = new Pilha<>();
            for (int i = 0; i < expressaoPosfixa.length(); i++) {
                expressaoPilha.push(expressaoPosfixa.charAt(i));
            }
    
            // Calcula o resultado da expressão pós-fixa
            Double resultado = CalculaPosfixa.calculaExpressao(expressaoPilha, EntradaDados.getVariaveis(), gravador, gravando);
    
            // Exibe o resultado se não estiver gravando
            if (resultado != null && !gravando) {
                System.out.println(resultado);
            }
    
            // Se estiver gravando, armazena a expressão e o resultado
            if (gravando) {
                StringBuilder sb = new StringBuilder();
                sb.append(expressao).append("§").append(resultado);
                try {
                    gravador.enqueue(sb.toString());  // Tenta gravar
                } catch (Exception e) {
                    System.err.println("Erro ao gravar: " + e.getMessage());
                }
                System.out.println(expressao);
                System.out.println("(REC: " + gravador.totalElementos() + "/10)");
            }
    
        } catch (VariavelNaoDefinidaException e) {
            // Erro se alguma variável usada na expressão não foi definida
            System.out.println(e.getMessage());
            return;
        } catch (RuntimeException e) {
            // Captura outros erros de execução
            System.out.println(e.getMessage());
        }
    }
    
    //metodo para pegar o array de variaveis
    public static Variavel[] getVariaveis() {
        return variaveis;
    }
    
    public static void vars(FilaCircular<String> gravador) throws Exception{
        StringBuilder sb = new StringBuilder();
        
        for(Variavel variavel: getVariaveis()){
            if(variavel != null){
                sb.append(variavel.toString() + "\n");
            }   
        }
        gravador.enqueue(sb.toString());
    }


    //
    public static void gravacao (String entrada, FilaCircular<String> gravador) throws Exception {
        if (gravador.qIsFull()){
            System.out.println("Encerrando gravação...");
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

    private static boolean contemOperacaoAritmetica(String valor){
        return valor.contains("+") || valor.contains("-") || valor.contains("*") || valor.contains("/") || valor.contains("^");
    }


    //Imprime o gravador
    private static void exibirGravacao(FilaCircular<String> gravador) {
        FilaCircular<String> auxiliar = new FilaCircular<>();
    
        try {
            if (gravador.qIsEmpty()) {
                System.out.println("Não há gravação para ser reproduzida.");
                return;
            }
    
            System.out.println("Reproduzindo gravação...");
    
            while (!gravador.qIsEmpty()) {
                String expressaoComResultado = gravador.dequeue();

                // Faz uma quebra de linha da equação com o resultado
                expressaoComResultado = expressaoComResultado.replace("§", "\n");
                
                System.out.println(expressaoComResultado);
                auxiliar.enqueue(expressaoComResultado);

            }
    
            while (!auxiliar.qIsEmpty()) {
                gravador.enqueue(auxiliar.dequeue());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Apaga o gravador
    private static void apagarGravacao (FilaCircular<String> gravador){
        gravador.clear();
    }
}