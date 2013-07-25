package net.phoenixxe.shared;

import org.junit.Test;

public abstract class AsynchronousTest {
	protected final int lastDelay;
	
	public AsynchronousTest() {
		lastDelay = 1000;
	}
	
	public AsynchronousTest(int delay) {
		lastDelay = delay;
	}
	
	@Test
	public void finish() {
		pauseTest(lastDelay);
	}
	
	/**
	 * Activate wait interval for testing
	 * @param timeout - wait timeout in msec
	 */
	public synchronized void pauseTest(int timeout){
		try{
			wait(timeout);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
