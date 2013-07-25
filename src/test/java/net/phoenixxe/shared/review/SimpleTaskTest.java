package net.phoenixxe.shared.review;


import static org.junit.Assert.*;

import net.phoenixxe.shared.Callback;
import net.phoenixxe.shared.tasks.SimpleTask;
import net.phoenixxe.shared.tasks.TaskException;

import org.junit.Test;

/**
 * Tests for {@link SimpleTask} object.<br>
 * Tests are run consecutively. Every test have its own SimpleTask <br>
 * object and test only one its method and properties that can be <br>
 * tested or witch change their value after execution of tested method.<br>
 * full list of tests:<br>
 * {@link #completeTest()}, 
 * {@link #equalsTest()},  
 * {@link #dropStateTest()},
 * {@link #setDelayTest()},
 * {@link #completeTest()},
 * {@link #failTest()},
 * {@link #fail1Test()},
 * {@link #isAvailableTest()},
 * {@link #executeTest()}.<br><p>
 * For tests there was created classes {@link BuffSimpleTask} and {@link TestCallback}<br>
 * that inherits {@link SimpleTask} and {@link Callback} for gaining access<br>
 * to protected methods and properties.<p>
 * For clarity, we moved some values outside of the test and included them<br>
 * in the class as static objects<br>
 * {@link #DEFAULT_DELAY},
 * {@link #TEST_STRING},
 * {@link #BASE_DELAY},
 * {@link #SKIP_DELAY},
 * {@link #WAIT_INTERVAL}
 * @see BuffSimpleTask
 * @see TestCallback
 */
public class SimpleTaskTest {
	
	/**
	 * Test string for exceptions
	 * */
	public static final String TEST_STRING="exception";
	
	/**
	 * Arbitrary value of SimpleTask.executionDelay for testing<br>
	 * Uses only in current tests cases
	 * @see SimpleTask#setDelay(long)
	 */
	public static final int DEFAULT_DELAY = 100;
	
	/**
	 * Default value for SimpleTask.executionDelay that was described <br>
	 * in class {@link SimpleTask}. Mast be equal to this value for correct testing
	 * @see SimpleTask#setDelay(long)
	 */
	public static final int BASE_DELAY = 0;
	
	/**
	 * This value needs to skip SimpleTask.executionDelay <br>
	 * and SimpleTask.modificationDelay checks in<br>
	 * SimpleTask.isAvailable()
	 * @see SimpleTask#setDelay(long)
	 * @see SimpleTask#isAvailable(long, long)
	 */
	public static final int SKIP_DELAY = -1;
	
	/**
	 * This value used in {@link #pauseTest(int)} method to set main thread in sleep to ensure<br>
	 * that thread pass 100 msec delay interval
	 */
	public static final int WAIT_INTERVAL = 110;
		
	/**
	 * Tests the correct construct of a {@link SimpleTask} instance:<br/>
	 * -  if property [<b>completed</b>] == [<b>false</b>] after an instance was created<br/>
	 * -  if property [<b>failed</b>] == [<b>false</b>] after an instance was created<br>
	 * -  if property [<b>executionDelay</b>] >= [<b>{@link #DEFAULT_DELAY}</b>] after an instance was created<br>
	 * -  if property [<b>modificationTime</b>] >= [<b>current Time</b>] after an instance was created<br>
	 * -  if the method <b>toString()</b> will return string with length more the 4 characters
	 */
	@Test
	public void constructTest() {

		// Create an instance of SimpleTask for testing
		SimpleTask<String> simpleTask = getSimpleTask(null);
		
		// Tests if property [completed] == [false] after an instance of SimpleTask was created
		assertFalse("After an instance of SimpleTask was created method ..isCompleted() returned [true] but must [false] ",simpleTask.isCompleted());
		
		// Tests if property [failed] == [false] after an instance of SimpleTask was created
		assertFalse("After an instance of SimpleTask was created method ..isFailed() returned [true] but must [false]",simpleTask.isFailed());
		
		// Tests if property [executionDelay] == [DEFAULT_DELAY] after an instance of SimpleTask was created
		assertEquals("After an instance of SimpleTask was created method ..getDelay() returned value not equal to ["+BASE_DELAY+"].\n" +
				     "Maybe default value was changed from ["+BASE_DELAY+"]", BASE_DELAY, simpleTask.getDelay());
		
		
		// Tests if property [modificationTime] >= [current time] after an instance of SimpleTask was created
		assertTrue("After an instance of SimpleTask was created method ..getTimestamp() returned long representation \n" +
				   "of the creation time bigger then the current time. That is impossible.", simpleTask.getTimestamp()<=System.currentTimeMillis());
		
		// Tests if the method toString() will return string with length more the 4 characters  
		assertTrue("After an instance of SimpleTask was created method toString() return string that have length less the 4 characters", simpleTask.toString().length()>=4);
	
	}
	
	/**
	 * Tests method  {@link SimpleTask#equals(Object)} :<br>
	 * (This test creates two instance of SimpleTask: object <b>A</b> and object <b>B</b>)<br>
	 * -  Compare object <b>A</b> with itself. Object <b>A</b> must be equal to itself<br>
	 * -  Compare object <b>A</b> with object <b>B</b>. Object <b>A</b> must NOT be equal to object <b>B</b><br>
	 */
	@Test
	public void equalsTest() {
		
		// Create an instance of SimpleTask for testing. Object A;
		SimpleTask<String> aTask = getSimpleTask(null);
		
		// Create an instance of SimpleTask for testing. Object B;
		SimpleTask<String> bTask = getSimpleTask(null);
		
		// Compare object A with itself. Object A must be equal to itself.
		assertTrue("After compare instance of a SimpleTask with itself via method ..equals() \n" +
				  "we have result that they are not the the same", aTask.equals(aTask));
		
		// Compare object A with object B. Object A must NOT be equal to object B.
		assertFalse("After compare instance of a SimpleTask with enother instance of SimpleTask\n" +
				"via method ..equals() we have result that they a the same",aTask.equals(bTask));
		
	}
	
	/**
	 * Tests method {@link SimpleTask#dropState()} and state of the properties after its execution:<br>
	 * -  if property [<b>completed</b>] == <b>false</b> after execution of ..dropState()<br>
	 * -  if property [<b>failed</b>] == <b>false</b> after execution of ..dropState()<br>
	 * -  if property [<b>exception</b>] == <b>null</b> after execution of ..dropState()<br>
	 * -  if property [<b>modificationTime</b>] >= [<b>current time</b>] after execution SimpleTask.dropState()
	 * */
	@Test
	public void dropStateTest() {

		// Create an instance of SimpleTask for testing.
		SimpleTask<String> simpleTask = getSimpleTask(null);
		
		// Execute ..fail( Exception(exceptionMessage) ) method to change state and value of inner parameters: exception, failed, completed etc.
		try{
			simpleTask.fail(new Exception("This message will be cleared after execution of ..dropState()"));
		}catch (TaskException e) {}
		
		// Execute tested method 
		simpleTask.dropState();

		// Execute all needed test for this test case
		dropStateInnerTest(simpleTask);
		
	}
	
	/**
	 * Tests {@link SimpleTask#setDelay(long)} and {@link SimpleTask#getDelay()} methods:<br>
	 * -  if the method <b>..getDelay()</b> of a SimpleTask instance return value [<b>newDelayValue = 50</b>] after <b>..setDelay(newDelayValue = 50)</b> was executed
	 * */
	@Test
	public void setDelayTest() {
		
		// executionDelay property of SimpleTask instance will be set in this value
		int newDelayValue = 50;
		
		// Creating an instance of SimpleTask for testing.
		SimpleTask<String> simpleTask = getSimpleTask(null);

		// Execute tested method
		simpleTask.setDelay(newDelayValue);
		
		// Tests if the method ..getDelay() of a SimpleTask instance return value [newDelayValue = 50] after ..setDelay(newDelayValue = 50) was execute  
		assertTrue("Method ..getDelay() of a SimpleTask instance return value not equal to ["+newDelayValue+"] after ..setDelay("+newDelayValue+") was executed ", simpleTask.getDelay()==newDelayValue);
	}
	
	
	/**
	 * Tests {@link SimpleTask#complete(Object)} and states of properties: <br>
	 * <p>(In this test we create inner class that extends <b>Callback</b> class for <br>
	 * overriding <b>onSuccess()</b> and <b>onFail()</b> methods, create instance of the new <br>
	 * <b>Callback</b> class with overridden methods, create an instance of <b>SimpleTask</b> <br>
	 * for testing with new Callback class as entering parameter)<br><p>
	 * -  if property [<b>complete</b>] == [<b>true</b>] after execution of SimpleTask.complete() method<br>
	 * -  if a reason message of Callback that will be generated after calling ..complete(reasonMessage = "Success") will be equal to "Success"<br> 
	 * -  if the modification time property [<b>modificationTime</b>] has correct value after calling a ..complete() method<br>
	 * -  if  after execution of the SimpleTask.complete() method there will be no TaskException thrown<br>
	 * -  execution of a tested method with null pointer parameter
	 * */
	@Test
	public void completeTest() {
		// Message that we mast obtain after execution of the tested method via CallBack and TaskException
		String reasonMessage = "Success";
		
		// Create instance of the new TestCallback class with overridden methods
		TestCallback<String> testCallBack = new TestCallback<String>();
		
		// Creating an instance of SimpleTask for testing with new Callback class as entering parameter
		SimpleTask<String> simpleTask = getSimpleTask(testCallBack);
		
		try{
			
			// Execution of a tested method
			simpleTask.complete(reasonMessage);
		
			// After execution of the SimpleTask.complete() method there must be a TaskException thrown, but it didn't
			fail("After execution of the SimpleTask.complete() method there must be a TaskException thrown, but it didn't");
			
		}catch(TaskException e) {
			
			// Tests if property [complete] == true after execution of SimpleTask.complete() method
			assertTrue("property [complete] of an SimpleTask instance must be [true] after ..complete() method was called, but it dont", simpleTask.isCompleted());
			
			// Tests if a reason message of Callback that will be generated after calling ..complete(reasonMessage = "Success") will be equal to "Success" 
			assertTrue("A reason message of Callback that was generated after calling SimpleTask.complete(reasonMessage = \"Success\") was NOT equal to \"Success\"",testCallBack.reasonMessage.equals(reasonMessage));
			
			// Tests if the modification time parameter [modificationTime] has correct value after calling a ..complete() method
			assertTrue("property [modificationTime] of SimpleTask must have long representation of time less or equal to current time\n" +
					"asfter execution of ..complete() method, but it don't", simpleTask.getTimestamp()<=System.currentTimeMillis());
	
		}
		
		try{
			// Execution of a tested method with null pointer paramenter
			simpleTask.complete(null);
		} catch (NullPointerException e) {
			fail("After execution of SimpleTask.coplete() method with null pointer parameter an NullPointerException have been thrown\n"
					+ e.getMessage());
		} catch(TaskException e) {
			
		}
	}
	
	/**
	 * Tests {@link SimpleTask#fail(String)} method:
	 * <p>(In this test we create inner class that extends <b>Callback</b> class for <br>
	 * overriding <b>onSuccess()</b> and <b>onFail()</b> methods, create instance of the new <br>
	 * <b>Callback</b> class with overridden methods, create an instance of <b>SimpleTask</b> <br>
	 * for testing with new Callback class as entering parameter)<br><p>
	 * -  if property [complete] == true after execution of SimpleTask.fail() method<br>
	 * -  if property [failed] == true after execution of SimpleTask.fail() method<br>
	 * -  if the modification time property [modificationTime] has correct value after calling a ..failed() method<br>
	 * -  if a reason message of Callback that will be generated after calling ..fail(reasonMessage = "Fail") will be equal to "Fail"<br>
	 * -  if there is no TaskException thrown after execution of the SimpleTask.fail() method<br>
	 * -  if there will be uncatched NullPointerException after call SimpleTask.fail(null)
	 * */
	@Test
	public void failTest() {
		
		// Message that we mast obtain after execution of the tested method via CallBack and TaskException
		String reasonMessage = "fail";
		
		// Create instance of the new Callback class with overridden methods
		TestCallback<String> buffCallBack = new TestCallback<String>();
		
		// Create an instance of SimpleTask for testing with new Callback class as entering parameter
		SimpleTask<String> simpleTask = getSimpleTask(buffCallBack);
				
		try {
			
			// Execute method with the reasonMessage 
			simpleTask.fail(reasonMessage);
			
			// Fail if there is no TaskException thrown after execution of the SimpleTask.fail() method
			fail("After execution of the SimpleTask.fail(String) method there must be a TaskException thrown, but it didn't");
			
		} catch (TaskException e) {
			
			// Tests if an inner parameter [complete] == true after execution of SimpleTask.fail() method
			assertTrue("property [complete] of an SimpleTask instance must be [true] after ..fail(String) method was called, but it dont", simpleTask.isCompleted());
			
			// Tests if property [failed] == true after execution of SimpleTask.fail() method
			assertTrue("property [failed] of an SimpleTask instance must be [true] after ..fail(String) method was called, but it dont", simpleTask.isFailed());
			
			// Tests if the modification time parameter [modificationTime] has correct value after calling a ..failed() method
			assertTrue("property [modificationTime] of SimpleTask must have long representation of time less or equal to current time\n" +
					"after execution of ..fail() method, but it don't", simpleTask.getTimestamp()<=System.currentTimeMillis());
	
			// Tests if a reason message of Callback that will be generated after calling ..fail(reasonMessage = "Fail") will be equal to "Fail" 
			assertTrue("A reason message of Callback that was generated after calling SimpleTask.complete(reasonMessage = \"Success\") was NOT equal to \"Success\"",buffCallBack.reasonMessage.equals(reasonMessage));
		
		}
	
		try {
			
			// Null pointer object
			String nullString = null;
			
			// Tests method with null pointer object 
			simpleTask.fail(nullString);
			
		} catch (NullPointerException e) {
			
			// Uncatched NullPointerException after call SimpleTask.fail(null) 
			fail("Uncatched NullPointerException after call SimpleTask.fail(null)");
		}
		catch (TaskException e) {}  
	}
	
	/**
	 * Tests {@link SimpleTask#fail(Exception)} method:
	 * <p>(In this test we create inner class that extends <b>Callback</b> class for <br>
	 * overriding <b>onSuccess()</b> and <b>onFail()</b> methods, creating instance of the new <br>
	 * <b>Callback</b> class with overridden methods, create an instance of <b>SimpleTask</b> <br>
	 * for testing with new Callback class as entering parameter)<br><p>
	 * -  if an inner parameter [complete] == true after execution of SimpleTask.fail() method <br>
	 * -  if an inner parameter [failed] == true after execution of SimpleTask.fail() method
	 * -  if the modification time parameter [modificationTime] has correct value after calling a ..failed() method
	 * -  if there will be uncatched NullPointerException after call SimpleTask.fail(null) <br>
	 * -  if there was no TaskException thrown after execution of testing method <br>
	 * -  if a TaskException message equal to <b>message</b> that we send via SimpleTask.fail(new Exception(<b>message</b>))
	 * */
	@Test
	public void fail1Test() {
		
		// Message that we mast obtain after execution of the tested method via CallBack and TaskException
		String reasonMessage = "fail"; // String must be in low case
	
		// Create instance of the new TestCallback class with overridden methods
		TestCallback<String> testCallBack = new TestCallback<String>();
		
		// Create an instance of SimpleTask for testing with new Callback class as entering parameter
		SimpleTask<String> simpleTask = getSimpleTask(testCallBack);

		try {
			
			// Execute testing method with an Exception parameter
			simpleTask.fail(new Exception(reasonMessage));
			
			// Fail if there was no TaskException thrown after execution of testing method
			fail("There was no TaskException thrown after execution of SimpleTask.fail(Exception) method");
			
		} catch (TaskException e) {
			
			// Tests if property [complete] == true after execution of SimpleTask.fail() method
			assertTrue("property [complete] of an SimpleTask instance must be [true] after ..fail() method was called, but it dont", simpleTask.isCompleted());
			
			// Tests if property [failed] == true after execution of SimpleTask.fail() method
			assertTrue("property [failed] of an SimpleTask instance must be [true] after ..fail() method was called, but it dont", simpleTask.isFailed());
			
			// Tests if the modification time parameter [modificationTime] has correct value after calling a ..failed() method
			assertTrue("property [modificationTime] of SimpleTask must have long representation of time less or equal to current time\n" +
					"after execution of ..fail() method, but it don't", simpleTask.getTimestamp()<=System.currentTimeMillis());
	
			// Tests if a reason message of Callback that will be generated after calling ..fail(reasonMessage = "Fail") will be equal to "Fail" 
			assertTrue("A reason message of Callback that was generated after calling SimpleTask.complete(reasonMessage = \"Success\") was NOT equal to \"Success\"",testCallBack.reasonMessage.equals(reasonMessage));
			
			// Tests if a TaskException message equal to message that we send via SimpleTask.fail(new Exception(message))
			assertEquals("A SimpleTask.fail(Exception(\""+reasonMessage+"\")) method throw excption with \n" +
					"error message that don't equal to \""+reasonMessage+"\"", reasonMessage, e.getMessage());
			
		}
		
		try {
			
			// Null pointer object
			Exception e = null;
			
			// Testing method with null pointer object 
			simpleTask.fail(e);
			
		} catch (NullPointerException e) {
			
			// Uncatched NullPointerException after call SimpleTask.fail(null) 
			fail("Uncatched NullPointerException after call SimpleTask.fail(null)");
		}
		catch (TaskException e) {}
		
		
	}
	
	/**
	 * Tests {@link SimpleTask#isAvailable(long, long)} method:<br>
	 *  - if SimpleTask.isAvailable() method return [false] after we set executionDelay to [SKIP_DELAY] <br>
	 *  and modificationDelay to [SKIP_DELAY] and executed ..complete() method (completed=true, failed=false)<br><br>
	 *  
	 *  - if SimpleTask.isAvailable() method return [false] after we set executionDelay to [SKIP_DELAY] <br>
	 *  and modificationDelay to [SKIP_DELAY] and executed ..fail() method (completed=true, failed=true)<br><br>
	 *  
	 *  - if SimpleTask.isAvailable() method return [true] after we set executionDelay to [SKIP_DELAY] <br>
	 *  and modificationDelay to [SKIP_DELAY] and executed ..dropState() method (completed=false, failed=false)<br><br>
	 *  
	 *  - if SimpleTask.isAvailable() method return [false] after we set executionDelay to [DEFAULT_DELAY], <br>
	 *  modificationDelay to [SKIP_DELAY], executed ..dropState() method (completed=false, failed=false)<br>
	 *  and run test in DEFAULT_DELAY msec interval after SimpleTask.createTime was reinitialized<br><br>
	 *  
	 *  - if SimpleTask.isAvailable() method return [true] after we set executionDelay to [DEFAULT_DELAY], <br>
	 *  modificationDelay to [SKIP_DELAY], executed ..dropState() method (completed=false, failed=false)<br>
	 *  and run test after DEFAULT_DELAY msec interval after SimpleTask.createTime was reinitialized<br><br>
	 *  
	 *  - if SimpleTask.isAvailable() method return [false] after we set executionDelay to [SKIP_DELAY], <br>
	 *  modificationDelay to [DEFAULT_DELAY], executed ..dropState() method (completed=false, failed=false)<br>
	 *  and run test in DEFAULT_DELAY msec interval after SimpleTask.createTime was reinitialized<br><br>
	 *
	 *  Test if SimpleTask.isAvailable() method return [true] after we set executionDelay to [SKIP_DELAY], <br>
	 *  modificationDelay to [DEFAULT_DELAY], executed ..dropState() method (completed=false, failed=false) <br>
	 *  and run test after DEFAULT_DELAY msec interval after SimpleTask.createTime was reinitialized
	 *  
	 *  @see #DEFAULT_DELAY
	 *  @see #WAIT_INTERVAL
	 *  @see #SKIP_DELAY 
	 **/
	@Test
	public void isAvailableTest() {
		
		// Creating an instance of SimpleTask for testing.
		SimpleTask<String> simpleTask = getSimpleTask(null);
				
		
		// Set completed = true, failed = false 
		try{
			simpleTask.complete(null);
		}catch (TaskException e) {}
		
		// Set executionDelay to -1 to skip executionDelay test code part of ..isAvailable() method
		simpleTask.setDelay(SKIP_DELAY);
		
		// Run ..isAvailable(0,modificationDelay = -1)witch set modificationDelay to -1 to  skip modificationDelay test code part of ..isAvailable() method
		// Test if SimpleTask.isAvailable() method return [false] after we set executionDelay to [-1] and modificationDelay to [-1]
		// and executed ..complete() method (completed=true, failed=false)
		assertFalse("SimpleTask.isAvailable() method retrun [true] when executionDelay = ["+SKIP_DELAY+"],modificationDelay = ["+SKIP_DELAY+"] and \n" +
				"..complete() method was executed (completed = true, failed = false)", simpleTask.isAvailable(0, SKIP_DELAY));
		
		// Set completed = false and failed = false;
		simpleTask.dropState();
		
		// Fail task. Set completed = true, failed = true 
		try{simpleTask.fail("");}catch (TaskException e) {}
		
		// Run ..isAvailable(0,modificationDelay = -1). Set modificationDelay to -1 to  skip modificationDelay test code part of ..isAvailable() method
		// Test if SimpleTask.isAvailable() method return [false] after we set executionDelay to [-1] and modificationDelay to [-1]
		// and executed ..fail() method (completed=true, failed=true)
		assertFalse("SimpleTask.isAvailable() method retrun [true] when executionDelay = ["+SKIP_DELAY+"],modificationDelay = ["+SKIP_DELAY+"] and \n" +
				"..fail() method was executed (completed=true, failed=true)", simpleTask.isAvailable(0, SKIP_DELAY));
		
		// Set completed = false and failed = false,  modificationTime = [current Time +- 2 msec];
		simpleTask.dropState();
		
		// Run ..isAvailable(0,modificationDelay = -1) witch set modificationDelay to -1 to  skip modificationDelay 
		// test code part of ..isAvailable() method
		// Test if SimpleTask.isAvailable() method return [true] after we set executionDelay to [-1] and modificationDelay to [-1]
		// and executed ..dropState() method (completed=false, failed=false)
		assertTrue("SimpleTask.isAvailable() method retrun [false] when executionDelay = ["+SKIP_DELAY+"],modificationDelay = ["+SKIP_DELAY+"] and \n" +
				"..dropState() method was executed (completed = false, failed = false)", simpleTask.isAvailable(0, SKIP_DELAY));
		
		// Set executionDelay to 100 msec to test executionDelay test code part of ..isAvailable() method
		simpleTask.setDelay(DEFAULT_DELAY);
		
		// Run ..isAvailable(0,modificationDelay = SKIP_DELAY) witch set modificationDelay to SKIP_DELAY to skip modificationDelay test code part of ..isAvailable() method
		// Test if SimpleTask.isAvailable() method return [false] after we set executionDelay to [100], modificationDelay to [SKIP_DELAY],
		// executed ..dropState() method (completed=false, failed=false) and run test in 100 msec interval after 
		// SimpleTask.createTime was reinitialized
		assertFalse("SimpleTask.isAvailable() method retrun [true] when executionDelay = ["+DEFAULT_DELAY+"],modificationDelay = ["+SKIP_DELAY+"],\n" +
				"..dropState() method was executed (completed = false, failed = false) and test run in 100 msec interval \n" +
				"after SimpleTask.createTime was reinitialized", simpleTask.isAvailable(0, SKIP_DELAY));
		
		// wait 110 msec to ensure that we past 100 msec interval after object was created
		pauseTest(WAIT_INTERVAL);
		
		// Run ..isAvailable(0,modificationDelay = SKIP_DELAY) witch set modificationDelay to SKIP_DELAY to skip modificationDelay test code part of ..isAvailable() method
		// Test if SimpleTask.isAvailable() method return [true] after we set executionDelay to [100], modificationDelay to [-1],
		// executed ..dropState() method (completed=false, failed=false) and run test after 100 msec interval after 
		// SimpleTask.createTime was reinitialized
		assertTrue("SimpleTask.isAvailable() method retrun [false] when executionDelay = ["+DEFAULT_DELAY+"],modificationDelay = "+SKIP_DELAY+"],\n" +
				"..dropState() method was executed (completed = false, failed = false) and test run after 100 msec interval \n" +
				"after SimpleTask.createTime was reinitialized", simpleTask.isAvailable(0, SKIP_DELAY));
		
		
		// Set completed = false and failed = false,  modificationTime = [current Time +- 2 msec];
		simpleTask.dropState();

		// Set value for executionDelay so executionDelay check will be skipped
		simpleTask.setDelay(SKIP_DELAY);
		
		// Run ..isAvailable(0,modificationDelay = DEFAULT_DELAY) witch set modificationDelay to 100
		// Test if SimpleTask.isAvailable() method return [false] after we set executionDelay to [SKIP_DELAY], modificationDelay to [DEFAULT_DELAY],
		// executed ..dropState() method (completed=false, failed=false) and run test in 100 msec interval after 
		// SimpleTask.createTime was reinitialized
		assertFalse("SimpleTask.isAvailable() method retrun [true] when executionDelay = ["+SKIP_DELAY+"],modificationDelay = "+DEFAULT_DELAY+"],\n" +
				"..dropState() method was executed (completed = false, failed = false) and test run in 100 msec interval \n" +
				"after SimpleTask.createTime was reinitialized",simpleTask.isAvailable(0, DEFAULT_DELAY));
		
		// wait 110 msec to ensure that we past 100 msec interval after object was created
		pauseTest(WAIT_INTERVAL);
		
		// Run ..isAvailable(0,modificationDelay = DEFAULT_DELAY) witch set modificationDelay to 100
		// Test if SimpleTask.isAvailable() method return [true] after we set executionDelay to [SKIP_DELAY], modificationDelay to [DEFAULT_DELAY],
		// executed ..dropState() method (completed=false, failed=false) and run test after 100 msec interval after 
		// SimpleTask.createTime was reinitialized
		assertTrue("SimpleTask.isAvailable() method retrun [false] when executionDelay = ["+DEFAULT_DELAY+"],modificationDelay = "+SKIP_DELAY+"],\n" +
				"..dropState() method was executed (completed = false, failed = false) and test run after 100 msec interval \n" +
				"after SimpleTask.createTime was reinitialized", simpleTask.isAvailable(0, SKIP_DELAY));
	}
	
	/**
	 * Tests {@link SimpleTask#execute(int)} method.<br>
	 * Test state and value of SimpleTask inner parameter after different <br>
	 * exception will be thrown in ..execute() method by the ..perform() method, <br>
	 * normal working of ..execute(0 method
	 * 
	 * */
	@Test
	public void executeTest() {
		
		// Create an instance of new Callback class
		TestCallback<String> simpleCallback = new TestCallback<String>();
		
		// Create an instance of SimpleTask with new CallBask slass parameter  
		BuffSimpleTask simpleTask = new BuffSimpleTask(simpleCallback);
		
		try {
			
			// Execute ..fail() to set SimpleTask.completed = true and execute CallBack.onFial() 
			try{simpleTask.fail(new Exception(TEST_STRING));}catch (TaskException e) {}
			
			// Execute SimpleTask.execute(1) to test it with 5 atempts
			simpleTask.execute(1);
			
			// Test if there was any attempts to run task. attempts == 0, complete == true after call of SimpleTask.execute()
			assertTrue("There was an attempt to run task when SimpleTask.complete == true ", simpleTask.getAttempts() == 0);
		

			// Tests illegalStateException catching
			try{
				
				// Set completed = false and failed = false, attempts = 0;;
				simpleTask.dropState();
				
				// After this call method perform() throw IllegalStateException
				simpleTask.setStateIllegalStateException();
				
				// Execute tested method 
				simpleTask.execute(1);
			
				fail("There was no IllegalStateException thrown on SimpleTask.execute() when throwin of this exception\n" +
						"is garanted");
				
			}catch (IllegalStateException e) {
				
				// Test if there was  1 attempt to run the task if the call of SimpleTask.perform() garanteed IllegalStateException thrown
				assertEquals("When call of SimpleTask.perform() garanted thow IllegalStateException atempts count must be equal to 1",1, simpleTask.getAttempts());
			}
			
			// Set completed = false and failed = false, attempts = 0;;
			simpleTask.dropState();
			
			// After this call method perform() throw IllegalArgumentException
			simpleTask.setStateIllegalArgumentException();


			//exectue tested method
			simpleTask.execute(1);
				
			// Test if there was  1 attempt to run the task if the call of SimpleTask.perform() garanteed IllegalArgumentException thrown
			assertEquals("When call of SimpleTask.perform() garanted thow IllegalArgumentException atempts count must be equal to 1",1, simpleTask.getAttempts());
				
			// Test if there any exception in SimpleTask.exceptin after guaranteed thrown of IllegalArgumentException in ..execute()
			assertFalse("There was no exception in SimpleTask.exceptin after guaranteed thrown of IllegalArgumentException in ..execute()",simpleTask.getException() == null);
			
			// An exception that was thrown into Callback after IllegalArgumentException thrown in ..execute() has wrong message
			assertTrue("An exception that was store in SimpleTask.exceptin after thrown of IllegalArgumentException in ..execute()\n" +
						" has wrong message. It mean that Callback.onFail() don't work correctly or it just siply didn't call",simpleCallback.getMessage().equals(TEST_STRING));

			// Set completed = false and failed = false, attempts = 0;;
			simpleTask.dropState();
		
			// After this call method perform() throw Exception 
			simpleTask.setStateException();
					
			//exectue tested method
			simpleTask.execute(1);
				
			// Test if there was  1 attempt to run the task if the call of SimpleTask.perform() garanteed Exception trown
			assertEquals("When call of SimpleTask.perform() garanted thow TaskException atempts count must be equal to 1",1, simpleTask.getAttempts());
				
			// Test if there any exception in SimpleTask.exceptin after guaranteed thrown of Exception in ..execute()
			assertFalse("There was no exception in SimpleTask.exceptin after guaranteed thrown of Exception /n" +
					" in SimpleTask.perform() from SimpleTask.execute",simpleTask.getException() == null);
		
			// An exception that was store in SimpleTask.exceptin after thrown of Exception in ..execute() has wrong message
			assertTrue("An exception that was store in SimpleTask.exceptin after thrown of Exception in ..execute()\n" +
						" has wrong message. It mean that Callback.onFail() don't work correctly or it just siply didn't call",simpleTask.getException().getMessage().endsWith(TEST_STRING));
			
			// Set completed = false and failed = false, attempts = 0;;
			simpleTask.dropState();
			
			//exectue tested method 1st time to increase attempts count from 0 to 1
			simpleTask.execute(1);
			//exectue tested method 1st time to increase attempts count from 1 to 2
			simpleTask.execute(1);
			
			// Tests correct attempts count increase after double-pass ..execute() method
			assertEquals("after double-pass SimpleTask.execute() method etempts count don't increase fro 0 to 2",2, simpleTask.getAttempts());
			
		} catch (IllegalStateException e) {
			
			// Fail if SimpleTask.execute() throw unexpected IllegalStateException
			fail("SimpleTask.execute() throw unexpected IllegalStateException\n "+e.getMessage());
		}
	}
	
	/**
	 * Contains all needed tests for testing {@link SimpleTask} instance and <br>
	 * value of its inner parameters after execution {@link SimpleTask#dropState()}<br>
	 * @see #dropStateTest() 
	 * @param testObject - testing instance
	 */
	private void dropStateInnerTest(SimpleTask<String> testObject) {
		
		// Tests if the parameter [completed] == false after execution of ..dropState()
		assertFalse("Parameter [copleted] of a SimpleTask instance must be [false] after execution of ..dropState() method",testObject.isCompleted());
		
		// Tests if the parameter [failed] == false after execution of ..dropState()
		assertFalse("Parameter [failed] of a SimpleTask instance must be [false] after execution of ..dropState() method",testObject.isFailed());
		
		// Tests if the parameter [exception] == null after execution of ..dropState()
		assertTrue("Parameter [exception] of a SimpleTask instance must be [null] after execution of ..dropState() method",testObject.getException()== null);
		
		// Tests if the parameter [modificationTime] >= [current time] after execution SimpleTask.dropState() 
		assertTrue("After execution SimpleTask.dropState() method ..getTimestamp() returned long representation \n" +
				   "of the creation time bigger then the current time. That is impossible.", testObject.getTimestamp()<=System.currentTimeMillis());
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
	 * Create new SimpleTask object with custom Callback
	 * @param callback object with custom onFail() and onSuccess() methods<br>
	 * can be <b>NULL</b>
	 * @return SimpleTask instance
	 */
	public static SimpleTask<String> getSimpleTask(Callback<String> callback) {
		return new SimpleTask<String>(callback) {
			@Override 
			protected void perform() throws TaskException, IllegalStateException,Exception {}
		};
	}
	
	
	/**
	 * Class for testing SimpleTask method that calls during their execution<br>
	 * onSuccess() or/and onFail() methods of CallBack 
	 *
	 * @param String only for this tests. But CallBack can have<br>
	 * different parameters
	 */
	public class TestCallback<Result> extends Callback<Result>{
		
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
	
	
	/**
	 * Class for testing SimpleTask methods and properties<br>
	 * @see #getAttempts()
	 * @see #setStateIllegalStateException()
	 * @see #setStateIllegalArgumentException()
	 * @see #setStateException()
	 * @see #setStateComplete()
	 * @see #setStateFail()
	 **/
	public class BuffSimpleTask extends SimpleTask<String> {
		/**
		 * Indicates what will happen in {@link #perform()} method<br>
		 * <b>1</b> - throw IllegalStateException<br>
		 * <b>2</b> - throw IllegalArgumentException<br>
		 * <b>3</b> - throw Exception<br>
		 * <b>other</b> - call SimpleTask.complete() method<br>
		 */
		private int state;
		
		/**
		 * Default constructor
		 * */
		public BuffSimpleTask(Callback<String> callback) {
			super(callback);
		}
		
		/**
		 * Get attempts count
		 * @return attempts count
		 */
		public int getAttempts() {return attempts;}
		
		/**
		 * After this call method perform() throw IllegalStateException 
		 */
		public void setStateIllegalStateException() {state=1;}
		
		/**
		 * After this call method perform() throw IllegalArgumentException
		 */
		public void setStateIllegalArgumentException() {state=2;}
		
		/**
		 * After this call method perform() throw Exception 
		 */
		public void setStateException() {state=3;}
		
		/**
		 * After this call method perform() calls Complete()  
		 */
		public void setStateComplete() {state=4;}
		
		/**
		 * After this call method perform() calls Complete()  
		 */
		public void setStateFail() {state=5;}
		
		@Override
		protected void perform() throws TaskException,
				IllegalStateException, IllegalArgumentException,Exception {
			switch (state) {
			case 1:
				throw new IllegalStateException(TEST_STRING);
			case 2:
				throw new IllegalArgumentException(TEST_STRING);
			case 3:
				throw new Exception(TEST_STRING);
			case 4:
				state = 0;
				perform();
				break;
			case 5:
				fail("fail");
			default:
				complete("complete");
			}
		}
	}
	
}