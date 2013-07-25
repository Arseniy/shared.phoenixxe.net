package net.phoenixxe.shared;

/**
 * Callback for Task's execution
 * @author Arseniy Kaleshnyk, 14.05.2010, ver. 1.0
 */
public abstract class Callback<T> {
	public Callback() {
	}
	public abstract void onSuccess(T result);
	public abstract void onFail(String reason);
}