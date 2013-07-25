package net.phoenixxe.shared.review;

import java.util.Vector;

import net.phoenixxe.shared.Callback;
import net.phoenixxe.shared.tasks.SimpleTask;
import net.phoenixxe.shared.tasks.TaskException;
import net.phoenixxe.shared.tasks.TaskQueue;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for {@link TaskQueue} object.<br>
 * Tests are run consecutively. Every test have its own TaskQueue <br>
 * object and test only one its method and properties that can be <br>
 * tested or witch change their value after execution of tested method.<br>
 * full list of tests:<br>
 * {@link #constructTest()}, 
 * {@link #shutdownTest()},
 * {@link #putTest()},
 * {@link #getTaskTest()},
 * {@link #removeTest()},
 * {@link #runTaskTest()}.<br><p>
 * For tests there was created classes {@link TTaskQueue} and {@link TSimpleTask}<br>
 * that inherits {@link TaskQueue} and {@link SimpleTask} for gaining access<br>
 * to protected methods and properties.<p>
 * For clarity, we moved some values outside of the test and included them<br>
 * in the class as static objects<br>
 * {@link #EXCEPTION_MESSAGE},
 * {@link #SLEEP_INTERVAL},
 * {@link #TASK_RUN_ATEMPTS},
 * {@link #SEQUENCE_DELAY},
 * {@link #REPEAT_INTERVAL}
 * @see TTaskQueue
 * @see TSimpleTask
 */
public class TaskQueueTest {

	
	/**
	 * All exception that will be thrown in tests have this message
	 */
	public static String EXCEPTION_MESSAGE = "exception";
	
	/**
	 * All exception that will be thrown in tests have this message
	 */
	public static String FAIL_MESSAGE = "fail";
	
	/**
	 * All exception that will be thrown in tests have this message
	 */
	public static String SUCCESS_MESSAGE = "complete";
	
	
	/**
	 * Its time interval between run attempts of SimpleTask object, witch<br>
	 * TaskQueue obtain via {@link TTaskQueue#put(SimpleTask)}  
	 */
	int SLEEP_INTERVAL = 100;
	/**
	 * Run attempts count of SimpleTask objects witch TaskQueue obtain <br>
	 * via {@link TTaskQueue#put(SimpleTask)}  
	 */
	int TASK_RUN_ATEMPTS = 2;
	/**
	 * Delay witch mast be tested in {@link SimpleTask#isAvailable(long, long)},
	 * bat this haven't been implemented yet 
	 */
	int SEQUENCE_DELAY = 2000;
	/**
	 *  Delay between SimpleTask object was created and it can be available to<br>
	 *  call perform()
	 */
	int REPEAT_INTERVAL = 3000;	
	
	/**
	 * extends TaskQueue for testing
	 * @see TTaskQueue
	 */
	private TTaskQueue tTaskQueue;
	/**
	 * extends SimpleTask for testing
	 * @see TSimpleTask
	 */
	TSimpleTask tSimpleTask;

	/**
	 * Initialize {@link TTaskQueue} and {@link TSimpleTask} every time<br>
	 * before each test 
	 */
	@Before
	public void init() {
		// Create an instance of new TaskQueue with new method
		tTaskQueue = new TTaskQueue();
		
		// Disable disabled_interval delay
		tTaskQueue.setDisabled_intervalOff();
		
		// disable logging
		tTaskQueue.getLog().setLevel((Level)Level.OFF);
		
		// An instance of SimpleTask for testnig TaskQueue.put() method
		tSimpleTask = new TSimpleTask(new TCallback<String>());
		
	}
	
	/**
	 * Tests the correct construct of a {@link TaskQueue} instance:<br>
	 * -  default value of TaskQueue.interval via calling TaskQueue.getInterval()<br>
	 * -  if Queued tasks object [vector] not equal to NULL<br>
	 * -  default value of TaskQueue.max_attempts parameter<br>
	 * -  default value of TaskQueue.sequence_delay<br>
	 * -  default value of TaskQueue.repeat_interval<br>
	 * -  default value of TaskQueue.enabled<br>
	 * -  if Queued tasks object [log] not equal to NULL<br>
	 */
	@Test
	public void constructTest() {

		// Test default value of TaskQueue.interval via calling TaskQueue.getInterval()
		assertEquals("After creation of TaskQueue object default value of TaskQueue.interval have changed from \n"
					+SLEEP_INTERVAL+" to " + tTaskQueue.getInterval(),SLEEP_INTERVAL, tTaskQueue.getInterval());
		
		// Test if Queued tasks object [vector] not equal to NULL
		assertNotNull("After creation of TaskQueue property queuedTasks equals to NULL", tTaskQueue.getVector());
		
		// Test if Queued tasks object [vector] empty after creation
		assertTrue("After creation of TaskQueue object TaskQueue.queuedTasks not empty", tTaskQueue.getVector().isEmpty());
		
		// Test default value of TaskQueue.max_attempts parameter
		assertEquals("After creation of TaskQueue object default value of  TaskQueue.max_attempts have changed from \n" +
				+TASK_RUN_ATEMPTS +" to " +  tTaskQueue.getMaxAtempts(),TASK_RUN_ATEMPTS, tTaskQueue.getMaxAtempts());
		
		// Test default value of TaskQueue.sequence_delay
		assertEquals("After creation of TaskQueue object default value of TaskQueue.sequence_delay have changed from \n" +
				SEQUENCE_DELAY + " to "+tTaskQueue.getSequenceDelay(),SEQUENCE_DELAY, tTaskQueue.getSequenceDelay());
		
		// Test default value of TaskQueue.repeat_interval
		assertEquals("After creation of TaskQueue object default value of TaskQueue.repeat_interval have changed from \n" +
				REPEAT_INTERVAL+ " to " + tTaskQueue.getRepeatInterval(),REPEAT_INTERVAL, tTaskQueue.getRepeatInterval());
		
		// Test default value of TaskQueue.enabled
		assertTrue("After creation of TaskQueue object default value of TaskQueue.enabled have changed from \n" +
				"[true] to ["+tTaskQueue.getEnable()+"]", tTaskQueue.getEnable());
		
		// Test if Queued tasks object [log] not equal to NULL
		assertNotNull("After creation of TaskQueue object TaskQueue.log equals to NULL", tTaskQueue.getLog());

	}
	
	/**
	 * Tests {@link TaskQueue#shutdown()} method:
	 * - default value of TaskQueue.enabled<br>
	 * - the value of TaskQueue.enabled after TaskQueue.shutdown() method
	 * Test if Queued tasks object [vector] empty after creation
	 * */
	@Test
	public void shutdownTest() {
		
		// Test default value of TaskQueue.enabled
		assertTrue("After creation of TaskQueue object default value of TaskQueue.enabled have changed from \n" +
						"[true] to ["+tTaskQueue.getEnable()+"]", tTaskQueue.getEnable());
		
		// tested method. Enable must switch from [TRUE] to [FALSE]
		tTaskQueue.shutdown();
		
		// Test the value of TaskQueue.enabled after TaskQueue.shutdown()  
		assertFalse("After execution  of TaskQueue.shutdown()  -  TaskQueue.enabled have value [TRUE] but must [FALSE]", tTaskQueue.getEnable());
	}
	
	
	/**
	 * Test {@link TaskQueue#put(SimpleTask)}:<br>
	 * - method equal() of SimpleTask for using it in our current test case<br>
	 * - if Queued tasks object [vector] empty after creation<br>
	 * - put() method must not add task to queue if TaskQueue is disabled<br>
	 * - the SimpleTask object that we get from TaskQueue.getTask() for  equals to the one, the we put in it via ..put()<>
	 */
	@Test
	public void putTest() {
	
		// Test if Queued tasks object [vector]  empty after creation 
		assertTrue("After creation  of TaskQueue object TaskQueue.queuedTasks not empty", tTaskQueue.getVector().isEmpty());
		
		tTaskQueue.shutdown();
		
		// Put our SimpleTask object into tested method
		tTaskQueue.put(tSimpleTask);
		
		// If TaskQueue is disable method put() must not add task to queue. Tests this
		assertTrue("In disable state TaskQueue mast not add task to queue via put() but it did", tTaskQueue.getVector().isEmpty());

		// Enable queue
		tTaskQueue.setEnable();
		
		// Put our SimpleTask object into tested method
		tTaskQueue.put(tSimpleTask);
		
		// Test if Queued tasks object [vector] is still empty after call of TaskQueue.put(SimpleTask)
		assertFalse("After execution of TaskQueue.put(SimpleTask) object TaskQueue.queuedTasks is still empty", tTaskQueue.getVector().isEmpty());
		
		// Test the SimpleTask object that we get from TaskQueue.getTask() for  equals to the one, the we put in it via ..put()
		assertTrue("After execution of TaskQueue.put(SimpleTask aTask) we get an SimpleTask object from TaskQueue.queuedTasks.\n" +
				"This object mast be equal to aTask but it don't",tTaskQueue.getVector().firstElement().equals(tSimpleTask));
	}
	
	
	/**
	 * Tests TaskQueue.getTask() method:<br>
	 * - result method TaskQurue.getTask() when there no task available<br>
	 * - result method TaskQurue.getTask() return null object when there is one task available<br>
	 * - if the object that we put() in TaskQueue is equal to the same object, that we get from TaskQueue<br>
	 * - equal method of SimpleTask<br>
	 * */
	@Test
	public void getTaskTest() {

		// Test if method TaskQurue.getTask() return null object when there is no task available
		assertNull("TaskQurue.getTask() retrun not null object when there is no task available", tTaskQueue.getTask_());
		
		// ... and lad it in TaskQueue for testing
		tTaskQueue.put(tSimpleTask);
		
		// Test if method TaskQurue.getTask() return null object when there is one task available in queue, but
		// it not available due to inner delay 
		assertNull("TaskQurue.getTask() retrun not null object when when there is one task available in queue, but\n" +
				"it not available due to inner delay", tTaskQueue.getTask_());
		
		// Pass repeat interval check in TaskQueue.getTask() -> SimpleTask.isAvailable()
		pauseTest(REPEAT_INTERVAL+100);
		
		// Test result method TaskQurue.getTask() return null object when there is one task available
		assertNotNull("ERROR: TaskQurue.getTask() retrun null object when there is 1 object available", tTaskQueue.getTask_());
		
		// Test if the object that we put() in TaskQueue is equal to the same object, that we get from TaskQueue
		assertTrue("SimpleTask instance that load to TaskQueue via ..put() method not equal to SimpleTask object /n" +
				" that return via ..getTask() method", tSimpleTask.equals(tTaskQueue.getTask_()));
		
	}	
		
	/**
	 * Tests <b>TaskQueue.remove()</b> method <br>
	 * - if Queued tasks object [vector] empty after creation <br>
	 * - if Queued tasks object [vector] is still empty after call of TaskQueue.put(SimpleTask) <br>
	 * - if TaskQueue.remove(SimpleTask aTask) remove aTask from the queue and we can't get aTask from the queue via ..getTask() method <br>
	 * */
	@Test
	public void removeTest() {
		
		// Tests if Queued tasks object [vector] empty after creation
		assertTrue("After creation  of TaskQueue object TaskQueue.queuedTasks not empty", tTaskQueue.getVector().isEmpty());
				
		// Put task into queue
		tTaskQueue.put(tSimpleTask);
		
		// Tests if Queued tasks object [vector] is still empty after call of TaskQueue.put(SimpleTask)
		assertFalse("After execution of TaskQueue.put(SimpleTask) object TaskQueue.queuedTasks is still empty", tTaskQueue.getVector().isEmpty());
		
		// Execute tested method
		tTaskQueue.testRemove(tSimpleTask);
		
		// Tests if TaskQueue.remove(SimpleTask aTask) remove aTask from the queue and we can't get aTask from the queue via ..getTask() method
		assertNull("TaskQueue.remove(SimpleTask) did't remove an object from queue and we can still get this object\n" +
				"via TaskQueue.getTask()" , tTaskQueue.getTask_());
	}
	
	/**
	 * Tests TaskQueue.runTask method: <br>
	 * - if TaskQueue.prepareTask() executed in TaskQueue.runTask() method if there is no task available<br> 
	 * - if queued tasks object [vector] is still empty after call of TaskQueue.put(SimpleTask)<br>
	 * - if TaskQueue.prepareTask() executed in TaskQueue.runTask() method if there one task <br>
	 *   available but it can't be run due to inner delay in 3000 ms<br>
	 * - if tested method don't throw IllegalStateException when SimpleTask.perform() do<br>
	 * - if exception message match with pattern<br>
	 * - if TaskQueue.prepareTask() executed in TaskQueue.runTask() method when there is one task available<br>
	 * 	 and all delay are skipped<br>
	 * - if queued tasks property [vector] is empty after TaskQueue.runTask() threw IllegalStateException<br>
	 * - if the SimpleTask task was removed from queue after it call ..complete() should <br>
	 * */
	@Test
	public void runTaskTest() {
		
		tTaskQueue.runTask_();
		
		// Test if TaskQueue.prepareTask() executed in TaskQueue.runTask() method if there is no task available 
		assertFalse("TaskQueue.prepareTask() executed in TaskQueue.runTask() method when there is no task available",
				tTaskQueue.prepareTaskFlag);
		
//		// Create an object of new SimpleTask with overridden methods
//		TSimpleTask tSimpleTask = new TSimpleTask(null);
		
		// put task in queue
		tTaskQueue.put(tSimpleTask);
		
		// Test if Queued tasks object [vector] is still empty after call of TaskQueue.put(SimpleTask)
		assertFalse("After execution of TaskQueue.put(SimpleTask) object TaskQueue.queuedTasks is still empty", tTaskQueue.getVector().isEmpty());
		
		// run Task
		tTaskQueue.runTask_();
		
		// Test if TaskQueue.prepareTask() executed in TaskQueue.runTask() method if there one task available but it can't 
		// be run due to inner delay in 3000 ms
		assertFalse("TaskQueue.prepareTask() executed in TaskQueue.runTask() method when there one task available\n" +
				"but it can't run due to inner delay in 3000 ms",tTaskQueue.prepareTaskFlag);
		
		// Wait 3000 + 100 ms to skip this delay
		pauseTest(REPEAT_INTERVAL+100);
				
		// ..perform() method will throw IllegalStateException 
		tSimpleTask.setIllegalStateException();
		try {
			// run Task
			tTaskQueue.runTask_();
			
			// TaskQueue.runTask() don't throw IllegalStateException when SimpleTask.perform() do
			fail("TaskQueue.runTask() don't throw IllegalStateException when SimpleTask.perform() do");
		} catch (IllegalStateException e) {
			
			//  Test if exception message match with pattern
			assertTrue("Exception message thrown by the TaskQueue.runTask() did't match the pattern \n" +
					"witch is send from SimpleTask.perfom() via throw new IllegalStateException("+EXCEPTION_MESSAGE+")\n" +
							"Exception message:"+e.getMessage(),e.getMessage().equals(EXCEPTION_MESSAGE));
		}
		
		// Test if Queued tasks object [vector] is empty after TaskQueue.runTask() threw IllegalStateException 
		assertFalse("After execution of TaskQueue.put(SimpleTask) object TaskQueue.queuedTasks is still empty", tTaskQueue.getVector().isEmpty());
		
		// ..perform() method will call fail() method
		// due to the code logic reaction of TaskQueue for fail() and complete() method
		// in runTask() scope almost the same and there is no need to test those two
		// reaction separately
		tSimpleTask.setStateFail();

		// run Task
		tTaskQueue.runTask_();
		
		// Test if TaskQueue.prepareTask() executed in TaskQueue.runTask() method when there is one task available
		// and all delay are skipped
		assertTrue("There was no attempt to execute TaskQueue.prepareTask() when there is one task available \n" +
				"and all delay are skipped", tTaskQueue.prepareTaskFlag);
		
		// Test if the SimpleTask task was removed from queue after it call ..complete() should
		assertNull("After SimpleTask was executed via TaskQueue.runTask() and call SimpleTask.fail() \n" +
				"it was not removed from queue but it should", tTaskQueue.getTask_());
	
	}
	
	/**
	 * Test {@link TaskQueue#run()}
	 * - if task call its perform() while queue is disable but our task is available<br>
	 * - if task is removed from queue after it call perform() and run it with completed property set in true after execution<br>
	 * - if after task in queue throw Exception TaskQueu is alive or not<br>
	 * - if after task in queue throw IllegalStateException TaskQueu is alive or not <br>
	 */
	@Test
	public void runTest() {
		
		// load task in queue
		tTaskQueue.put(tSimpleTask);

		// disable TaskQueue
		tTaskQueue.shutdown();
		
		// Start TaskQueue.run() 
		tTaskQueue.start();
		
		// pause test for REPEAT_INTERVAL+SLEEP_INTERVAL+2 ms to be sure  to pass 3000+100 ms dead line 
		pauseTest(REPEAT_INTERVAL+SLEEP_INTERVAL+2);
		
		// Tests if task call its perform() while queue is disable but our task is available
		assertTrue("TaskQueue is disable but there is one SimpleTask with isAvailable() == [True] in TaskQueue \n" +
				"and it did't call its perform method, but it should.", tSimpleTask.isPerformCalls);
		
		assertTrue("CallBack must call its onSuccess() method after SimpleTask completed but it wont",
				tSimpleTask.getCallBack().getMessage().equals(SUCCESS_MESSAGE));
		
		// Test if task is removed from queue after it call perform() and run it with completed property set in true after execution
		assertTrue("After SimpleTask that holds in TaskQueue.queuedTasks excuted perform() method \n" +
				"it mast be removed from the queue, but it don't" , tTaskQueue.getVector().isEmpty());
		
		//Enable TaskQueue by set [enable] property to true
		tTaskQueue.setEnable();

		// Drop inner properties for this object
		tSimpleTask.dropState();
		// Task will fail
		tSimpleTask.setStateFail();
		
		tTaskQueue.put(tSimpleTask);
		
		pauseTest(REPEAT_INTERVAL+SLEEP_INTERVAL+2);
		
		assertTrue("CallBack must call its onFail() method after SimpleTask failed but it wont",
				tSimpleTask.getCallBack().getMessage().equals(FAIL_MESSAGE));
		
		
		// Perform will throw exception
		tSimpleTask.setException();
		
		tTaskQueue.put(tSimpleTask);
		
		pauseTest(REPEAT_INTERVAL+SLEEP_INTERVAL+2);
		
		// Test if after task in queue throw Exception TaskQueu must be alive, but it didn't
		assertTrue("After task in queue throw Exception TaskQueu must be alive, but it didn't",
				tSimpleTask.getAttempts()>=1 && tTaskQueue.isAlive());
		
		
		// This exception will killed TaskQueue thread
		tSimpleTask.setIllegalStateException();
		
		tSimpleTask.dropState();
		
		pauseTest(REPEAT_INTERVAL+SLEEP_INTERVAL+2);
		
		// Test if after task in queue throw IllegalStateException TaskQueu must not  be alive, but it did
		assertTrue("After task in queue throw IllegalStateException TaskQueu must not  be alive, but it did",
				tSimpleTask.getAttempts()>=1 && !tTaskQueue.isAlive());
		
		
		System.out.println("finished");
		
	}
	
	/**
	 * Activate wait interval for testing
	 * @param timeout - wait timeout in msec
	 */
	public synchronized void pauseTest(int timeout) {
		try{
			wait(timeout);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Class for testing properties and methods of TaskQueue
	 * @see #getInterval()
	 * @see #getVector()
	 * @see #getMaxAtempts()
	 * @see #getSequenceDelay()
	 * @see #getRepeatInterval()
	 * @see #getEnable()
	 * @see #getLog()
	 * @see #getTask_()
	 * @see #testRemove()
	 * @see #prepareTask()
	 * @see #runTask_()
	 * @see #prepareTaskFlag()
	 * @see #setEnable()
	 * @see #setDisabled_intervalOff()
	 */
	class TTaskQueue extends TaskQueue<SimpleTask<String>>{
		
		/**
		 * Set disabled_interval to 0 to disable this delya
		 */
		public void setDisabled_intervalOff() {
			disabled_interval = SLEEP_INTERVAL;
		}
		
		
		/**
		 * Set property repeat_interval to custom value
		 * @param value - new valur for repeat_interval
		 */
		public void setRepeat_interval(long value) {
			repeat_interval = value;
		}
		
		/**
		 * Return protected property
		 * @return <b>interval</b> property of TaskQueue
		 */
		public int getInterval() {
			return interval;
		}

		/**
		 * Return protected property
		 * @return <b>queuedTasks</b> property of TaskQueue
		 */
		public Vector<SimpleTask<String>> getVector() {
			return queuedTasks;
		}	
		/**
		 * Return protected property
		 * @return <b>max_attempts</b> property of TaskQueue
		 */
		public int getMaxAtempts() {
			return max_attempts;
		}
		/**
		 * Return protected property
		 * @return <b>sequence_delay</b> property of TaskQueue
		 */
		public long getSequenceDelay() {
			return sequence_delay;
		}	
		/**
		 * Return protected property
		 * @return <b>repeat_interval</b> property of TaskQueue
		 */
		public long getRepeatInterval() {
			return repeat_interval;
		}			
		/**
		 * Return protected property
		 * @return <b>enabled</b> property of TaskQueue
		 */
		public boolean getEnable() {
			return enabled;
		}			
		/**
		 * Return protected property
		 * @return <b>log</b> property of TaskQueue
		 */
		public Logger getLog() {
			return log;
		}	
		/**
		 * Return result of calls protected getTask()
		 * @return <b>SimpleTask</b> object if it exist in inner queue
		 */
		public SimpleTask<String> getTask_() {
			return getTask();
		}
		/**
		 * Remove SimpleTask object from inner queue if it present there
		 * @param a - SimpleTask object that must be removed from queue
		 */
		public void testRemove(SimpleTask<String> a ) {
			remove(a);
		}	
		/**
		 *  Indicate if method prepareTask executed
		 */
		public boolean prepareTaskFlag = false;
		
		@Override
		protected void prepareTask(SimpleTask<String> arg0) {
			prepareTaskFlag=true;
		}
		
		/**
		 * Execute protected runTask()
		 */
		public void runTask_() {
			runTask();
		}
		
		/**
		 * Enable TaskQueue after shutdown)
		 */
		public void setEnable() {
			enabled = true;
		}
				
	}
	
	/**
	 * Class for testing properties and methods of TaskQueue. By calling its different methods<br>
	 * TaskQueue object, that holds it, will show different reaction witch we can test  
	 * @see #getInterval()
	 * @see #getVector()
	 * @see #getMaxAtempts()
	 * @see #getSequenceDelay()
	 * @see #getRepeatInterval()
	 * @see #getEnable()
	 * @see #getLog()
	 * @see #getTask_()
	 * @see #testRemove()
	 * @see #prepareTask()
	 * @see #runTask_()
	 * @see #prepareTaskFlag()
	 * 
	 */ 
	class TSimpleTask extends SimpleTask<String> {
		/**
		 * Return run attempts count
		 * @return attempts count
		 */
		public int getAttempts() {
			return attempts;
		}
		/**
		 * Storage for callback
		 */
		private TCallback<String> callback;
		
		/**
		 * Return callback object for tests 
		 * @return {@link TCallback} 
		 */
		public TCallback<String> getCallBack() {return callback;}
		/**
		 * default constructor
		 * @param callback
		 */
		public TSimpleTask(TCallback<String> callback) {
			super(callback);
			this.callback = callback;
		}
		/**
		 * indicates if method perform() was called
		 */
		public boolean isPerformCalls = false;
		/**
		 * indicates what method perform() will do:<br>
		 * <b>1</b> - call complete("complete")<br>
		 * <b>2</b> - call fail("fail")<br>
		 * <b>3</b> - throw IllegalStateException("exception")<br>
		 * <b>other</b> - call complete("complete")<br>
		 */
		public int state = 0;
		/**
		 * After this call method perform() will call complete()
		 * @see #state
		 */
		public void setStateComplete() {state=1;}
		/**
		 * After this call method perform() will call fail()
		 * @see #state
		 */
		public void setStateFail() {state=2;}
		/**
		 * After this call method perform() will throw  IllegalStateException()
		 * @see #state
		 */
		public void setIllegalStateException() {state=3;}
		
		/**
		 * After this call method perform() will start cycle and don't finished it<br>
		 * until {@link #state} value changed
		 * @see #state
		 */
		public void setCycleRun() {state=4;}
		
		/**
		 * 
		 * After this call method perform() will throw  Exception()
		 * until {@link #state} value changed
		 * @see #state
		  */
		public void setException() {state=5;}
		
		/**
		 * Pause task by default for 1 sec
		 * @param timeMs - pause interval
		 * @throws InterruptedException
		 */
		private synchronized void pauseTask(int timeMs) throws InterruptedException{
				wait(timeMs);
		}
		@Override
		protected void perform() throws TaskException, IllegalStateException,Exception,InterruptedException,Exception {
			isPerformCalls=true;
			switch (state) {
			case 1:
				complete(SUCCESS_MESSAGE);
				break;
			case 2:
				fail(FAIL_MESSAGE);
				break;
			case 3:
				throw new IllegalStateException(EXCEPTION_MESSAGE);
			case 4:
				while (state == 4) {
					pauseTask(1000);
				}
				break;
			case 5:
				throw new Exception(EXCEPTION_MESSAGE);
			default:
				complete("complete");
				return;
			}
		}
		
		/**
		 * return how many tries was method SimpleTask.perform() executed
		 * @return tries attempts
		 */
		public int getRunAttempts() {
			return attempts;
		}
		/**
		 * Set property SimpleTask.attempts
		 * @param value - new value for property [attempts]
		 */
		public void setAttempts(int value) {
			attempts = value;
		}
	}
	
	/**
	 * Class for testing SimpleTask method that calls during their execution<br>
	 * onSuccess() or/and onFail() methods of CallBack 
	 *
	 * @param String only for this tests. But CallBack can have<br>
	 * different parameters
	 */
	public class TCallback<Result> extends Callback<Result>{
		
		public String reasonMessage="";
		
		public String getMessage() {
			String buff = reasonMessage;
			reasonMessage = "";
			return buff;
		}
		@Override
		public void onSuccess(Object result) {reasonMessage = (String)result;}
		@Override
		public void onFail(String reason) {reasonMessage = reason;}
	};
}
