package exceptions;

public class StackUnderflowError extends Error{

	private static final long serialVersionUID = 1L;

	public StackUnderflowError(String message) {
		super(message);
	}

}