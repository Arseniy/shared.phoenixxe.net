package net.phoenixxe.shared.gwt;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author Arseniy Kaleshnyk, ver 1.0
 * @param <T>
 */
public class EmptyCallback<T> implements AsyncCallback<T> {

	public EmptyCallback() {}

	public void onFailure(Throwable throwable) {}

	public void onSuccess(T result) {}
}
