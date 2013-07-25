package net.phoenixxe.shared.tasks;

import net.phoenixxe.shared.ExtendedException;

/**
 * Exception used in tasks.
 * @author Arseniy Kaleshnyk, 15.03.2011, ver. 1.0 
 */
public class TaskException extends ExtendedException {
	private static final long serialVersionUID = -525449492556345203L;

	/**
	 * Constructor for "success" exception, is used only in GDTask.complete method.
	 */
	@SuppressWarnings("deprecation")
	public TaskException() {
		super();
	}

	public TaskException(String msg) {
		super(msg);
	}

	public TaskException(Exception exception) {
		super(exception);
	}
}