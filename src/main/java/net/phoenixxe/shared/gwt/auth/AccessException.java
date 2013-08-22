package net.phoenixxe.shared.gwt.auth;

import net.phoenixxe.shared.gwt.ClientException;

/**
 * Used if logged in user wasn't logged in properly and cannot be identified. 
 * @author Arseniy Kaleshnyk, 03.05.2011, ver. 1.4
 */
public class AccessException extends ClientException {
	private static final long serialVersionUID = 8830064737357678991L;

	public AccessException() {
		super();
	}
	
	public AccessException(String message) {
		super(message);
	}
}