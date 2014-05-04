package org.statistic.dmr.stat.ejb3;

public class Ejb3MethodStatistics {
	public static final String EXECUTION_TIME = "execution-time";
	public static final String INVOCATIONS = "invocations";
	public static final String WAIT_TIME = "wait-time";
	private String _executionTime; 
	private String _invocations; 
	private String _waitTime;
	
	public Ejb3MethodStatistics(final String executionTime, final String invocations, final String waitTime) {
		setExecutionTime(executionTime);
		setInvocations(invocations);
		setWaitTime(waitTime);
	}
	
	public String getExecutionTime() {
		return _executionTime;
	}
	
	public void setExecutionTime(final String executionTime) {
		_executionTime = executionTime;
	}
	
	public String getInvocations() {
		return _invocations;
	}
	
	public void setInvocations(final String invocations) {
		_invocations = invocations;
	}
	
	public String getWaitTime() {
		return _waitTime;
	}
	
	public void setWaitTime(final String waitTime) {
		_waitTime = waitTime;
	}		
}
