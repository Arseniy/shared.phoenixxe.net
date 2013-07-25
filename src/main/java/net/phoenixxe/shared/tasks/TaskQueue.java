package net.phoenixxe.shared.tasks;

import java.util.Vector;

import net.phoenixxe.shared.SUtils;

import org.apache.log4j.Logger;

/**
 * Queue for tasks. Provides processing and execution of supplied tasks.<br>
 * Basic method for queue is put, by this task is analyzed and put in queue.<br>
 * Queue extends Thread, so <b>start()</b> should be called after construction.<br>
 * In general<br>
 *  * queue launches first available task<br>
 *  * processes results of it's execution<br>
 *  * sleep for some interval<br>
 * @author Arseniy Kaleshnyk, 15.03.2011, ver. 1.0
 */
public abstract class TaskQueue<T extends SimpleTask<?>> extends Thread {
	protected final Logger log;

	/**
	 * Sleep interval, can be changed on fly.<br>
	 * Default value -100 (1/10 sec.).
	 */
	protected int interval = 100;

	/**
	 * Queued tasks.
	 */
	protected Vector<T> queuedTasks = new Vector<T>();
	/**
	 * Maximum count of attempts before task fails.
	 */
	protected int max_attempts = 2;
	// TODO: set intervals to 0 to meet TooFastException
	/**
	 * Delay between task's creation and first execution.
	 */
	protected long sequence_delay = 2000;
	/**
	 * Delay between executions of same task
	 */
	protected long repeat_interval = 3000;
	/**
	 * Disabled thread interval
	 */
	protected int disabled_interval = 3600000;
	/**
	 * Enabled thread interval
	 */
	protected int enabled_interval = 100;
	
	/**
	 * Enabled.
	 */
	protected volatile boolean enabled = true;
	
	// TODO: #acl make getter for this task
	public T activeTask = null;
	
	/**
	 * Basic constructor.
	 */
	public TaskQueue() {
		log = Logger.getLogger(getClass().getName());
	}

	/**
	 * Puts specified task to queue, if the queue is enabled.
	 * @param task task to be put
	 */
	public void put(T task) {
		if (!enabled) return;
		
		logTask("+", task);
		queuedTasks.add(task);
	}
	
	/**
	 * Basic Thread method, called after queue is started<br>
	 * It's Infinite loop calling <b>runTask()</b>, and can be interrupted<br>
	 * by queue stop or by caught <b>IllegalStateException</b><br>
	 * during <b>runTask()</b> execution.<br>
	 * After this sleeps for <b>interval</b><br>
	 * If <b>shutdown()</b> was called, the interval increases to 1 hour, after execution of all tasks 
	 */
	@Override
	public void run() {
		while (true) {
			if (enabled || !queuedTasks.isEmpty())
				try {
					runTask();
				} catch (IllegalStateException ise) {
					System.out.println(toString() + " IllegalStateException caught, queue stopped");
					return;
				} catch (Exception e) {
					log.warn("run() : "
							+ (e != null ? e.getMessage() : " NULL"));
				}

			try {
				if (!enabled && queuedTasks.isEmpty()) {
					interval = disabled_interval;
				}
				sleep(interval);
			} catch (InterruptedException ie) {
				// this means the CmsModule waked up this TaskQueue
				if (!enabled && (interval == disabled_interval)) {
					interval = enabled_interval;
				} else {
					log.warn("run(), InterruptedException : " + ie.getMessage());
				}
			} catch (IllegalMonitorStateException imse) {
				log.error("run(), IllegalMonitorStateException : "
						+ imse.getMessage());
			}
		}
	}
	
	/**
	 * Stops queue. It's not an immediate process. Technically, it forces TaskQueue to sleep for a specified interval after execution of all tasks. 
	 * Use TaskQueue.interrupt() to awake the Queue.
	 */
	public void shutdown() {
		//stop();
		enabled = false;
	}
	
	/**
	 * Gets first available task.
	 * @return available task
	 */
	protected T getTask() {
		if (queuedTasks.isEmpty())
			return null;
		for (T task : queuedTasks) {
			if (task.isAvailable(sequence_delay, repeat_interval)) {
				return task;
			}
		}
		return null;
	}
	
	/**
	 * Logs task with specified prefix and current time.
	 * @param prefix 1 or few symbols before task's string.
	 * @param task task to be logged, should have nice <b>toString()</b>
	 */
	protected void logTask(String prefix, T task) {
		if (log.isTraceEnabled()) log.trace(
				new StringBuilder("\t\t ")
						.append(prefix).append(" ")
						.append(task.toString()).append("\t")
						.append(SUtils.shortTime())
						.toString());
	}
	
	/**
	 * Prepares task for execution.<br>
	 * It should be overridden with actual code.<br>
	 * Usually it supplies some context variables required for task's execution. 
	 * @param task
	 */
	protected abstract void prepareTask(T task);
	
	/**
	 * Provides some custom handling of custom exceptions
	 * @param task not null
	 * @param taskException not null, == task.getException
	 */
	protected void handleException(final T task, final Exception taskException) {
		// do nothing, should be overriden in real queues
	}
	
	/**
	 * Performs actions after task is failed.
	 * @param task failed task
	 */
	protected void postFail(T task) {
		logTask("!", task);
	}
	
	/**
	 * Performs actions after task is completed.
	 * @param task completed task
	 */
	protected void postComplete(T task) {
		remove(task);
	}

	/**
	 * Removes specified task from queue. 
	 * @param task task to be removed
	 */
	protected void remove(T task) {
		if (task == null)
			return;
		// what if it's running now ?
		queuedTasks.remove(task);
	}

	/**
	 * In general, <b>runTask</b> is called regularly, by timer,<br>
	 * gets first available task and executes it.<br>
	 * Basic implementation performs following:<br>
	 *  * gets first available task<br>
	 *  * prepares it for execution<br>
	 *  * calls <b>task.execute</b><br>
	 *  * catch any exception thrown by task except IllegalStateException<br>  
	 *  * removes task from queue if it's completed or failed<br>
	 *  * runs <b>postComplete()</b> for completed tasks<br>
	 * @throws IllegalStateException if task's execution was interrupted by server shutdown
	 */
	protected synchronized void runTask() throws IllegalStateException {
		T task = getTask();
		if (task == null)
			return;
		
		logTask("*", task);
		activeTask = task;
		prepareTask(task);
		try {
			task.execute(max_attempts);
			if (task.getException() != null)
					handleException(task, task.getException());
			
			if (task.isCompleted()) {
				postComplete(task);
			}

			if (task.isFailed()) {
				logTask("!", task);
				postFail(task);
			}
		} catch (IllegalStateException ise) {
			throw ise;
		} catch (Exception e) {
			log.error(toString() + ", " + task + ", nested catch", e);
		}
	}
}