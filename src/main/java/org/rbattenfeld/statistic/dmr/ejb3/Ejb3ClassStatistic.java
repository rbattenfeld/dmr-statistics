package org.rbattenfeld.statistic.dmr.ejb3;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Ejb3ClassStatistic {
	private final Map<String, Object> _classStatisticMap = new LinkedHashMap<String, Object>();	
	private final List<Ejb3MethodStatistic> _methodStatisticList = new ArrayList<Ejb3MethodStatistic>();
	private final String _deployment;
	private final String _beanName;
	private final EjbType _type;
	
	public Ejb3ClassStatistic(final String deployment, final String beanName, final EjbType type) {
		_deployment = deployment;
		_beanName = beanName;
		_type = type;
	}
	
	public String getDeployment() {
		return _deployment;
	}
		
	public String getBeanName() {
		return _beanName;
	}
	
	public EjbType getBeanType() {
		return _type;
	}
	
	public Map<String, Object> getClassStatistic() {
		return _classStatisticMap;
	}

	public List<Ejb3MethodStatistic> getMethodStatistic() {
		return _methodStatisticList;
	}

	public void addClassStatistic(final String method, final Object obj) {
		_classStatisticMap.put(method, obj);
	}
	
	public void addMethodStatistic(final String method, final Long executionTime, final Long invocations, final Long waitTime) {
		_methodStatisticList.add(new Ejb3MethodStatistic(method, executionTime, invocations, waitTime));
	}
	
	public String toCSVString(final char separator) {
		final StringBuffer buffer = new StringBuffer();		
		for (Map.Entry<String, Object> entry : _classStatisticMap.entrySet()) {
			add(buffer, entry.getValue().toString(), separator);
		}
		for (final Ejb3MethodStatistic methodStat : _methodStatisticList) {
			add(buffer, methodStat.getExecutionTime(), separator);
			add(buffer, methodStat.getInvocations(), separator);
			add(buffer, methodStat.getWaitTime(), separator);
		}		
		return buffer.toString();
	}
	
	//-----------------------------------------------------------------------||
	//-- Private Methods ----------------------------------------------------||
	//-----------------------------------------------------------------------||
	
	private void add(final StringBuffer buffer, final Object value, final char separator) {
		if (buffer.length() > 0) {
			buffer.append(separator);
		}
		buffer.append(value);
	}
		
}
