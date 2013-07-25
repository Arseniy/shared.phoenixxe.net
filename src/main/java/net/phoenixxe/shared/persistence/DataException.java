package net.phoenixxe.shared.persistence;

import net.phoenixxe.shared.ExtendedException;

/**
 * Generic exception for data layer.
 * @author Arseniy Kaleshnyk, 14.04.2010, ver. 1.0
 */
public class DataException extends ExtendedException  {
	private static final long serialVersionUID = -4754031113499907288L;

	public DataException() {
		super("Unknown cause");
	}

	public DataException(String msg) {
		super(msg);
	}

	public DataException(Exception e) {
		super(e);
	}
}
