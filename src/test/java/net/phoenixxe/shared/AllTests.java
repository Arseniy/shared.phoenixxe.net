package net.phoenixxe.shared;

import net.phoenixxe.shared.persistence.PersistenceTest;
import net.phoenixxe.shared.tasks.TasksTests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	UtilsTest.class,
	ValidatorTest.class,
	PersistenceTest.class,
	TasksTests.class})
public class AllTests {
}
