import domain.Pilha;

public class App {
    public static void main(String[] args) throws Exception {
        //Teste pilha genérica

        //Pilha de inteiros
        Pilha<Integer> integerPilha = new Pilha<>();
        integerPilha.push(12);
        integerPilha.push(33);
        integerPilha.push(55);
        integerPilha.push(55);

        while (!integerPilha.isEmpty()){
            System.out.println(integerPilha.pop());
        }

        //Pilha de Character
        Pilha<Character> characterPilha = new Pilha<>();
        characterPilha.push('W');
        characterPilha.push('R');
        characterPilha.push('T');
        characterPilha.push('M');
        characterPilha.push('Ç');

        while (!characterPilha.isEmpty()){
            System.out.println(characterPilha.pop());
        }
    }
}
