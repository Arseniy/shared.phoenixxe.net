package net.phoenixxe.shared.gwt;

import net.phoenixxe.shared.Utils;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * General exception for all situations.
 * @author Arseniy Kaleshnyk, 08.04.2010, ver. 1.0
 */
public class ClientException extends Exception implements IsSerializable {
	private static final long serialVersionUID = 146603533486381717L;
	private String msg;

	public ClientException() {
		msg = "Unknown cause";
	}

	public ClientException(String msg) {
		this.msg = (!Utils.isStringEmpty(msg) ? msg : "Unknown cause"); 
	}

	public ClientException(Exception e) {
		this(e != null ? e.getMessage() : "Unknown cause");
	}

	public String getMessage() {
		return msg;
	}
	
	public void setMessage(String msg) {
		this.msg = msg;
	}
}