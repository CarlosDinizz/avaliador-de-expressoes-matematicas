import exceptions.StackUnderflowError;

public class Pilha {

	private Integer[] elements;
	private int pos;
	
	
	public Pilha() {
		elements = new Integer[50];
		pos = -1;
	}
	
	public Pilha(int size) {
		elements = new Integer[size];
		this.pos = -1;
	}

	public Integer[] getElements() {
		return elements;
	}

	public void setElements(Integer[] elements) {
		this.elements = elements;
	}

	public int getPos() {
		return pos;
	}

	public void setPos(int size) {
		this.pos = size;
	}

	
	public boolean isEmpty() {
		if(pos == -1) {
			return true;
		}
		return false;
	}

	
	public int push(int n) {
		if(isFull()) {
			throw new StackOverflowError("A pilha está cheia");
		}
		
		pos++;
		elements[pos] = n;
		
		return n;
	}

	
	public int pop() {
		if(isEmpty()) {
			throw new StackUnderflowError("A pilha está vazia");
		}

		int n = elements[pos];
		
		elements[pos] = null;
		pos--;
		
		return n;
	}

	
	public Integer topo() {
		if(pos == -1) {
			return null;
		}
		return elements[pos];
	}

	
	public boolean isFull() {
		if((pos + 1) == (elements.length - 1)) {
			return true;
		}
		return false;
	}

	
	public int sizeElements() {
		return pos+1;
	}

}