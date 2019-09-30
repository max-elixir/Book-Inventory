package cardemo;

public class ValidationException extends Exception {
	private static final long serialVersionUID = 1L;

	public ValidationException(Exception e) {
		super(e);
	}
	
	public ValidationException(String msg) {
		super(msg);
	}
}
