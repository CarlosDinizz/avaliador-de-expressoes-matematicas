// Arthur Roldan Slikta 10353847
// Carlos Eduardo Diniz de Almeida 10444407
// Guilherme Clauz Morlina da Silva 10436477

package domain;

public class FilaCircular<T> {
    private static final int TAM_DEFAULT = 10;
    private int inicio, fim, qtde;
    private T e[];

    public FilaCircular(int tamanho) {
        this.inicio = this.fim = this.qtde = 0;
        e = (T[]) new Object[tamanho];
    }

    public FilaCircular() {
        this(TAM_DEFAULT);
    }

    public boolean qIsEmpty() {
        return (qtde == 0);
    }

    public boolean qIsFull() {
        return (qtde == e.length);
    }

    public void enqueue(T e) throws Exception {
        if (!qIsFull()) {
            this.e[this.fim++] = e;
            this.fim = this.fim % this.e.length;
            this.qtde++;
        } else {
            throw new Exception("Overflow - Estouro de Fila");
        }
    }

    public T dequeue() throws Exception {
        if (!qIsEmpty()) {
            T aux = this.e[this.inicio];
            this.inicio = ++this.inicio % this.e.length;
            this.qtde--;
            return aux;
        } else {
            throw new Exception("Underflow - Esvaziamento de Fila");
        }
    }

    public T front() throws Exception {
        if (!qIsEmpty()) {
            return e[inicio];
        } else {
            throw new Exception("Underflow - Esvaziamento de Fila");
        }
    }

    public T rear() throws Exception {
        if (!qIsEmpty()) {
            int pfinal;
            if (this.fim != 0) {
                pfinal = this.fim - 1;
            } else {
                pfinal = this.e.length - 1;
            }
            return this.e[pfinal];
        } else {
            throw new Exception("Underflow - Esvaziamento de Fila");
        }
    }

    public int totalElementos() {
        return qtde;
    }

	public void clear() {
        this.inicio = this.fim = this.qtde = 0;
    }

    @Override
    public String toString() {
        try {
            int indiceNovo = (inicio + qtde) % e.length;
            StringBuilder sb = new StringBuilder();

            sb.append("[Fila] quantidade: ").append(qtde)
                    .append(", capacidade: ").append(e.length);

            if (qtde != 0) {
                sb.append(", primeiro: ").append(front())
                        .append(", último: ").append(rear());
            }

            sb.append("\nConteúdo da Fila: [ ");
            if (qtde != 0) {
                if (indiceNovo <= inicio) {
                    for (int i = inicio; i < e.length; ++i)
                        sb.append("[").append(e[i]).append("]");
                    for (int i = 0; i < indiceNovo; ++i)
                        sb.append("[").append(e[i]).append("]");
                } else {
                    for (int i = inicio; i < indiceNovo; ++i)
                        sb.append("[").append(e[i]).append("]");
                }
            }
            sb.append(" ]");

            return sb.toString();
        } catch (Exception e) {
            return "Erro: " + e.getMessage();
        }
    }
}
