package domain;

import java.util.Scanner;

public class EntradaDados {
    Scanner entrada = new Scanner (System.in);
    char variavel = entrada.nextChar().split("");
    System.out.println(variavel);
    entrada.close();
}