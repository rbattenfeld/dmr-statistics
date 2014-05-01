package org.rbattenfeld.statistic.dmr.ejb3;

public class Ejb3MethodStatistic {
	private final String _methodName;
	private final Long _executionTime;
	private final Long _invocations;
	private final Long _waitTime;
	
	public Ejb3MethodStatistic(final String methodName, final Long executionTime, final Long invocations, final Long waitTime) {
		_methodName = methodName;
		_executionTime = executionTime;
		_invocations = invocations;
		_waitTime = waitTime;
	}

	public String getMethodName() {
		return _methodName;
	}
	
	public Long getExecutionTime() {
		return _executionTime;
	}

	public Long getInvocations() {
		return _invocations;
	}

	public Long getWaitTime() {
		return _waitTime;
	}
}
