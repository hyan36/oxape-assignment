package com.oocode;

public class Timer implements ITimer {

	long startTime;
	
	@Override
	public void startTimer() {
		startTime = System.currentTimeMillis();
		
	}

	@Override
	public Boolean TimeOut(int millis) {
		return startTime + millis < System.currentTimeMillis();
	}	
}
