package domain;


public class Fila <T>{
	// Constante e Atributos Privados
	private static int TAM_DEFAULT = 10;
	private int inicio, fim;
	private T e[ ];
	// Métodos públicos
	public Fila(int tamanho) {// construtor 1 (com tamanho)
		  this.inicio = this.fim = 0;
		  e = (T[]) new Object[tamanho];
	}
	
	public Fila() {  // construtor 2 (sem parâmetros).
		this(TAM_DEFAULT);
	}

	// verifica se a fila está vazia
	public boolean isEmpty() {
		return (this.inicio == this.fim);	
	}
	
	// Verifica se a fila está cheia
    public boolean isFull() {
    	return (this.fim == TAM_DEFAULT);
    }
    // insere um elemento no final da fila
	public T enqueue(T e) throws Exception {
		if (! isFull( )){
			    this.e[this.fim++] = e;
				return e;
		} else throw new Exception("Oveflow - Estouro de Fila");	
	}

	// remove um elemento do final da fila
    public T dequeue() throws Exception {
    	  if (! isEmpty( )){
    		  return this.e[ this.inicio++];
    	  }else{
    		  throw new Exception("underflow - Esvaziamento de Fila");
    	  }
    }
   

    // retorna quem está no início da fila
    // caso a fila não esteja vazia
	public T front() throws Exception {
		if (! isEmpty())
			return e[inicio];
		else{
			throw new Exception("underflow - Esvaziamento de Fila");
		}			
	}

	// retorna quem está no final da fila caso ela não esteja vazia
	public T rear() throws Exception {
		if (! isEmpty()){
			  int pfinal = this.fim - 1;
			  return this.e[pfinal];
		}else{
			 throw new Exception("underflow - Esvaziamento de Fila");
		}			
	}
	

	// Retorna o total de elementos da fila
	public	int totalElementos(){
		if (!isEmpty()) return this.fim - this.inicio;
		else return 0;

	}

	
}

