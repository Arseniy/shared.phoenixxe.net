package net.phoenixxe.shared.review;


import net.phoenixxe.shared.Callback;
import net.phoenixxe.shared.tasks.SimpleTask;
import net.phoenixxe.shared.tasks.TaskQueue;

import org.junit.Test;

/**
 */
public class ExampleTest {

	@Test
	public void constructTest() {
		Callback<String> callback = new Callback<String>() {
			@Override
			public void onSuccess(String result) {
				System.out.println("success - " + result);
			}
			@Override
			public void onFail(String reason) {
				System.out.println("failure - " + reason);
			}
		};
		
		SimpleTask<String> task = new SimpleTask<String>(callback) {
			@Override 
			protected void perform() {
				complete("4455");
			}
		};
//		task.execute(2);
		
		task.setDelay(3000);
		TaskQueue<SimpleTask<?>> queue = new TaskQueue<SimpleTask<?>>() {
			@Override
			protected void prepareTask(SimpleTask<?> task) {}
		};
		queue.start();
		queue.put(task);
	}
}