package domain;

import exceptions.StackUnderflowError;

public class Pilha<T> {

	private T[] elements;
	private int pos;
	
	
	public Pilha() {
		elements = (T[]) new Object[50];
		pos = -1;
	}
	
	public Pilha(int size) {
		elements = (T[]) new Object[size];
		this.pos = -1;
	}

	public T[] getElements() {
		return elements;
	}

	public void setElements(T[] elements) {
		this.elements = elements;
	}

	public int getPos() {
		return pos;
	}

	public void setPos(int size) {
		this.pos = size;
	}

	
	public boolean isEmpty() {
        return pos == -1;
    }

	
	public T push(T n) {
		if(isFull()) {
			throw new StackOverflowError("A pilha está cheia");
		}
		
		pos++;
		elements[pos] = n;
		
		return n;
	}

	
	public T pop() {
		if(isEmpty()) {
			throw new StackUnderflowError("A pilha está vazia");
		}

		T n = elements[pos];
		
		elements[pos] = null;
		pos--;
		
		return n;
	}

	
	public T topo() {
		if (!isEmpty()){
			return elements[pos];
		}
		return null;
	}

	
	public boolean isFull() {
        return (pos + 1) == (elements.length - 1);
    }

	
	public int sizeElements() {
		return pos+1;
	}

	@Override
	public String toString(){
		StringBuilder builder = new StringBuilder();

		for (int i = 0; i < sizeElements(); i++){
			builder.append(elements[i]);
		}

		return builder.toString();
	}

}