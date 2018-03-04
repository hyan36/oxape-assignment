package com.oocode;

import org.junit.Assert;
import org.junit.Test;

public class TimerTest {
	
	@Test
	public void TestNotTimeOut() {
		Timer timer = new Timer();
		timer.startTimer();
		Boolean timeout = timer.TimeOut(2);
		Assert.assertFalse(timeout);
	}
	
	@Test
	public void TestTimeOut() throws Exception {
		Timer timer = new Timer();
		timer.startTimer();
		Thread.sleep(5);
		Boolean timeout = timer.TimeOut(2);
		Assert.assertTrue(timeout);
	}

}
