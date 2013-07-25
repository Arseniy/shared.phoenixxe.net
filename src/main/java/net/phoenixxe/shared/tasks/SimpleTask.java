package net.phoenixxe.shared.tasks;

import net.phoenixxe.shared.Callback;
import net.phoenixxe.shared.Utils;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * Added protected property logFailure.
 * It's set in constructor and used to define should failure-without-exception be logged as error level  
 */

/**
 * Task is used for guaranteed execution of custom code,<br>
 * specified in <b>perform()</b>.<br>
 * After successful execution custom <b>onSuccess()</b> called.<br>
 * If execution meets non-fatal troubles, it can be run again after<br>
 * some delay. If troubles are fatal, or total count of executions<br>
 * exceeds predefined level, task fails and custom <b>onFail()</b> called.<br>
 * @see Task
 * @author Arseniy Kaleshnyk, 15.03.2011, ver. 1.0
 */
public abstract class SimpleTask<Result> implements Task<Result> {
	public static final long DEFAULT_DELAY = 100;
	protected Logger log = Logger.getLogger(getClass().getName());
	protected Callback<Result> callback;
	
	private boolean completed = false;
	private boolean failed = false;
	protected int attempts = 0;
	protected long createTime;
	protected long modificationTime;
	protected long executionDelay = 0; // 0.1 seconds by default
	
	protected Level logFailureLevel = Level.ERROR;
	
	/**
	 * Last exception caught in execution. 
	 */
	protected Exception exception = null;
	
	/**
	 * Basic constructor.
	 * @param callback callback containing custom <b>onFail()</b> and <b>onSuccess()</b> methods.
	 */
	public SimpleTask(Callback<Result> callback) {
		this.callback = callback;
		createTime = getTime();
		modificationTime = createTime;
	}
	
	@Override
	public synchronized void execute(int maxAttempts) 
			throws IllegalStateException {
		exception = null;
		if (completed) return;
		
		if (attempts <= maxAttempts) {
			attempts++;
			modificationTime = getTime();
			
			try {
				String performPrefix = "\t" + toString() + "perform()";
				try {
					if (log.isTraceEnabled()) log.trace(performPrefix);
					perform();
				} catch (IllegalStateException ise) {
					if (log.isDebugEnabled()) log.debug(
							performPrefix + ", caught IllegalStateException " + ise.getMessage());
					throw ise;
				} catch (IllegalArgumentException iae) {
					exception = iae;
					fail(iae);
				} catch (TaskException gde) {
					// throw TaskException to external catch, see below
					throw gde;
				} catch (Exception e) {
					if (log.isTraceEnabled()) log.debug(
							performPrefix + ", caught general Exception " + e.getMessage());
					try {
						analyzeException(e);
					} catch (TaskException gee) {
						// throw TaskException to external catch, see below
						throw gee;
					} catch (Exception ee) {
						log.warn("Task, nested catch", ee);
						throw new TaskException(ee);
					}
				}
			} catch (TaskException te) {
				// TaskException is thrown when complete or fail methods are called
				
				// execution completed successfully
				if (te.isEmpty()) return;
				
				if (logFailureLevel.isGreaterOrEqual(log.getEffectiveLevel())) {
					String message = Utils.isEmpty(te.getMessage()) ? "N/A" : te.getMessage();
					logTrouble(message, false);
				}
			}
		}
		if (attempts > maxAttempts) try {
			fail("run unsuccessfully " + attempts + " times");
		} catch (TaskException gde) {
			// expected and ignored
		}
	}
	
	@Override
	public boolean isAvailable(long sequence_delay, long modificationDelay) {
		if (completed || failed) return false;
		long time = getTime();
		
		if ((executionDelay > 0) && (time - createTime < executionDelay))
			return false;
		
		if ((modificationDelay > 0) && (time - modificationTime < modificationDelay))
			return false;
	
		return true;
	}
	
	@Override
	public boolean isCompleted() {
		return completed;
	}

	@Override
	public boolean isFailed() {
		return failed;
	}
	
//	@Override
	public Exception getException() {
		return exception;
	}
	
	/**
	 * Interrupts task's execution by throwing GDException.<br>
	 * Invokes <i>callback's onFail</i> method, if callback is set.
	 * @inheritDoc
	 * @param errorMsg information message, used as parameter for <i>callback's onFail</i> method
	 * @throws GDException is thrown to interrupt task's execution.
	 */
	@Override
	public void fail(String errorMsg) throws TaskException {
		if (log.isTraceEnabled()) log.trace("\t" + toString() + ".fail(), " + errorMsg);
		completed = true;
		failed = true;
		modificationTime = getTime();
		if (callback != null) {
			try {
				callback.onFail(errorMsg);
			} catch (Exception e) {
				log.error(toString() + " onFail execution failed : " + e.getMessage(), e);
			}
		}
		throw new TaskException(errorMsg);
	}
	
	@Override
	public void fail(Exception e) throws TaskException {
		if (e == null) fail("Uknown exception"); 
		
		exception = e;
		try {
			fail(Utils.normaliseString(e.getMessage()));
		} catch (TaskException gde) {
			// expected
		}
		throw new TaskException(e);
	}
	
	@Override
	@Deprecated
	public void fail() throws TaskException {
		try {
			fail("N/A");
		} catch (TaskException gde) {
			throw new TaskException();
		}
	}
	
	/**
	 * Sets task as completed.<br>
	 * Invokes <i>callback's onSuccess</i> method, if callback is set.<br>
	 * @param result used as parameter for <i>callback's onSuccess</i> method
	 */
	@Override
	public void complete(Result result) throws TaskException {
		completed = true;
		modificationTime = getTime();
		if (callback != null) {
			try {
				callback.onSuccess(result);
			} catch (Exception e) {
				log.error(toString() + " onSuccess execution failed : " + e.getMessage(), e);
			}
		}
		throw new TaskException();
	}
	
	@Override
	public long getTimestamp() {
		return modificationTime;
	}
	
	@Override
	public long getDelay() {
		return executionDelay;
	}
	
	@Override
	public void setDelay(long delay) {
		executionDelay = delay;
	}
	
	@Override
	public void dropTimeStamp() {
		createTime = getTime();
	}
	
	/**
	 * @inheritDoc
	 * Lets maintenance tasks to be in queue all the time.
	 * @throws IllegalStateException if task isn't <b>maintenance</b>, 
	 * i.e. type isn't CHECK_RECORDS or CHECK_FOLDERS
	 */
	@Override
	public void dropState() throws IllegalStateException {
		completed = false;
		failed = false;
		attempts = 0;
		exception = null;
		
		dropTimeStamp();
		modificationTime = createTime;
	}
	
	@Override
	public String toString() {
		return "SimpleTask[]";
	}
	
	@Override
	public boolean equals(Object object) {
		if (this == object) return true;
		return false;
	}
	
	/**
	 * Gets current time in <b>long</b> format.
	 * @return current time
	 */
	protected long getTime() {
		return System.currentTimeMillis();
	}
	
	/**
	 * Custom method containing actual code for execution.<br>
	 * In case of success <b>complete()</b> should be called.<br>
	 * In case of error call <b>fail()</b>.<br>
	 * Both methods immediately interrupt execution by throwing TaskException.
	 * @throws TaskException when <b>complete()</b> or <b>fail()</b> methods are called
	 * @throws IllegalStateException when execution is interrupted by server's shutdown
	 */
	protected abstract void perform() throws TaskException, IllegalStateException, Exception;
	
	/**
	 * Checks specified exception, thrown in <b>perform()</b> against known ones.<br>
	 * Overridden method in working implementation should distinguish non-fatal exceptions<br>
	 * from fatal and call <b>fail()</b> for last ones.<br>
	 * By this execution of invalid code, or proper code with invalid data, isn't launched again.<br>
	 * This method throws TaskException in any case.<br><br>
	 * Basically it just wraps exception in TaskException and throws it back.<br>
	 * But overridden method should contain domain-specific exceptions.<br>
	 * @param e exception from <b>perform()</b>
	 * @throws TaskException wrapped exception or TaskException from explicit fail().
	 */
	protected void analyzeException(Exception e) throws TaskException {
		throw new TaskException(e);
	}
	
	protected void logTrouble(final String message, final boolean hideException) {
		if ((exception == null) || (hideException)) log.log(logFailureLevel, toString() + "\t" + message);
		else log.log(logFailureLevel, toString() + "\t" + message, exception);
	}
}