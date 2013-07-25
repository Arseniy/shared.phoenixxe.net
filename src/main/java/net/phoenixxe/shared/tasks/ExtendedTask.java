package net.phoenixxe.shared.tasks;

import java.util.ArrayList;

import net.phoenixxe.shared.Callback;
import net.phoenixxe.shared.Utils;

/**
 * Extended task with multiply callbacks and delay support.
 * @author Arseniy Kaleshnyk, 10.06.2010, ver. 1.0
 */
public abstract class ExtendedTask<Result> extends SimpleTask<Result> {
	private final ArrayList<Callback<Result>> callbacks = 
			new ArrayList<Callback<Result>>();
	private final long modificationDelay = 1000;
	
	public ExtendedTask(Callback<Result> callback) {
		super(null);
		addCallback(callback);
	}
	
	@Override
	public String toString() {
		return new StringBuilder("ExtendedTask[").
				append("] ").
				toString();
	}
	
	public Callback<Result> getCallback() {
		if (Utils.isListEmpty(callbacks)) return null;
		return callbacks.get(0);
	}
	
	public void addCallback(Callback<Result> callback) {
		if (log.isTraceEnabled()) log.trace(
				toString () + "addCallback(), " + callback);
		modificationTime = getTime();
		if (callback == null) return;
		if (callbacks.contains(callback)) {
			if (log.isTraceEnabled()) log.trace("addCallback(), callback already exists in callbacks");
			return;
		}
		callbacks.add(callback);
	}
	
	@Override
	public void fail(String reason) throws TaskException {
		TaskException expectedException = null;
		try {
			super.fail(reason);
		} catch (TaskException te) {
			expectedException = te;
		}
		
		for (Callback<Result> callback : callbacks) {
			try {
				callback.onFail(reason);
			} catch (Exception e) {
				log.error(toString() + "callback #" + callbacks.indexOf(callback) + 
						" onFail execution failed : " + e.getMessage(), e);
			}
		}
		
		if (expectedException != null) throw expectedException;
		else {
			log.error(toString() + " : onFail exception was expected, but wasn't thrown");
			throw new TaskException(reason);
		}
	}
	
	// TODO: #merge #210 Override other fails()
	
	@Override
	public void complete(Result result) {
		for (Callback<Result> callback : callbacks) {
			try {
				callback.onSuccess(result);
			} catch (Exception e) {
				log.error(toString() + "callback #" + callbacks.indexOf(callback) + 
						" onSuccess execution failed : " + e.getMessage(), e);
			}
		}
		super.complete(result);
	}
	
	@Override
	public boolean isAvailable(long sequence_delay, long repeat_interval) {
		return (super.isAvailable(sequence_delay, repeat_interval)
				&& (getTime() - createTime > executionDelay)
				&& (getTime() - modificationTime > modificationDelay));
	}
}