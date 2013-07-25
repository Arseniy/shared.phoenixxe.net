package net.phoenixxe.shared.tasks;

/**
 * Task is used for guaranteed execution of custom code,<br>
 * specified in <b>perform()</b>.<br>
 * After successful execution custom <b>onSuccess()</b> called.<br>
 * If execution meets non-fatal troubles, it can be run again after<br>
 * some delay. If troubles are fatal, or total count of executions<br>
 * exceeds predefined level, task fails and custom <b>onFail()</b> called.<br>
 * @author Arseniy Kaleshnyk, 12.05.2011, ver. 1.0
 */
public interface Task<Result> {
	/**
	 * Called by queue. Performs execution of <code>perform()</code>,<br/>
	 * handles thrown custom exceptions.
	 * @param maxAttempts maximum count of attempts allowed by queue
	 * @throws TaskException if<br> a. required task wasn't completed<br>
	 * b. task wasn't completed for few times<br>
	 * c. <b>onFail</b> was called
	 */
	void execute(int maxAttempts) throws IllegalStateException;
	
	/**
	 * Calculates if task is available taking in account provided
	 * <code>sequence_delay, modificationDelay</code> and internal state of task.
	 * @param sequence_delay
	 * @param modificationDelay
	 * @return
	 */
	boolean isAvailable(long sequence_delay, long modificationDelay);
	
	/**
	 * @return true if task was completed, successfully or not,
	 */
	public boolean isCompleted();
	
	/**
	 * @return true if task was failed.
	 */
	public boolean isFailed();
	
	/**
	 * Interrupts task's execution by throwing TaskException.<br>
	 * Invokes <i>callback's onFail</i> method, if callback is set.
	 * @param errorMsg information message, used as parameter for <i>callback's onFail</i> method
	 * @throws TaskException is thrown to interrupt task's execution.
	 */
	public void fail(String errorMsg) throws TaskException;
	
	/**
	 * Interrupts task's execution by throwing TaskException.<br>
	 * Invokes <i>callback's onFail</i> method, if callback is set.
	 * @param e exception caused failure
	 * @throws TaskException is thrown to interrupt task's execution.
	 */
	public void fail(Exception e) throws TaskException;
	
	/**
	 * Fail without details. Should be used only temporarily.<br>
	 * Markes as <b>deprecated</b> to provide corresponding warning. 
	 * @throws TaskException
	 */
	@Deprecated
	public void fail() throws TaskException;
	
	/**
	 * Returns last exception.
	 * @return exception
	 */
//	public Exception getException();
	
	/**
	 * Sets task as completed.<br>
	 * Invokes <i>callback's onSuccess</i> method, if callback is set.<br>
	 * @param result used as parameter for <i>callback's onSuccess</i> method
	 */
	public void complete(Result result) throws TaskException;
	
	/**
	 * Returns date of last modification or launch.
	 * @return modification date
	 */
	public long getTimestamp();
	
	/**
	 * Returns delay before first execution.
	 * @return delay
	 */
	public long getDelay();
	
	/**
	 * Sets delay before first execution.
	 * @param delay
	 */
	public void setDelay(long delay);
	
	/**
	 * Drops <b>create time</b> to current time. 
	 */
	public void dropTimeStamp();
	
	/**
	 * Lets maintenance tasks to be in queue all the time.
	 * @throws IllegalStateException if task isn't <b>maintenance</b>, 
	 * i.e. type isn't CHECK_RECORDS or CHECK_FOLDERS
	 */
	public void dropState() throws IllegalStateException;
}