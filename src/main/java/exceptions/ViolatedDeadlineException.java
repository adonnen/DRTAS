package exceptions;

public class ViolatedDeadlineException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7528852370926240574L;

	public ViolatedDeadlineException(String message) {
		super(message);
	}

}
