package net.phoenixxe.shared;

/**
 * Exception.
 * @author Arseniy Kaleshnyk, 18.05.2010, ver. 1.0
 */
public class ExtendedException extends RuntimeException {
	private static final long serialVersionUID = -158756803545478637L;
	protected Exception exception;
	protected String msg;
	
	// TOOD: should be protected
	@Deprecated
	public ExtendedException() {
		this.exception = null;
		msg = null;
	}

	public ExtendedException(String msg) {
		this.exception = null;
		this.msg = msg;
	}

	public ExtendedException(Exception exception) {
		this.exception = exception;
		if (exception == null) {
			msg = null;
			return; 
		}
		
		if ((exception.getMessage() == null) 
				|| (exception.getMessage().trim().isEmpty())) {
			msg = exception.toString();
		} else {
			msg = exception.getMessage();
		}
	}

	public Exception getException() {
		return exception;
	}

	public String getMessage() {
		return msg;
	}
	
	public boolean isEmpty() {
		return (exception == null) && (msg == null);
	}
}