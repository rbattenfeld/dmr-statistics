package org.statistic.dmr.stat.ejb3;

import java.util.List;

import org.statistic.dmr.api.IDmrStatisticFormatter;

public class Ejb3StatisticCSVFormatter implements IDmrStatisticFormatter<List<? extends Ejb3StatisticModel>, String> {
	public static final char _SEPARATOR = ',';	
	
	@Override
	public String formatHeader(final List<? extends Ejb3StatisticModel> stats) {
		final StringBuffer buf = new StringBuffer();		
		for (final Ejb3StatisticModel detail : stats) {	
			final String beanAbbr = detail.getBeanNameAbbr();
			if (detail.getKeys() != null) {
				for (final String key : detail.getKeys()) {
					add(buf, beanAbbr + "-" + key, _SEPARATOR);
				}
			}
			if (detail.getKeys() != null) {
				for (final String method : detail.getMethods()) {
					add(buf, beanAbbr + "-" + method + "-executionTime", _SEPARATOR);
					add(buf, beanAbbr + "-" + method + "-invocations", _SEPARATOR);
					add(buf, beanAbbr + "-" + method + "-waitTime", _SEPARATOR);
				}
			}
		}		
		return buf.toString();
	}
	
	@Override
	public String formatLine(final List<? extends Ejb3StatisticModel> stats) {
		final StringBuffer buf = new StringBuffer();		
		for (final Ejb3StatisticModel detail : stats) {
			formatKeyValues(detail, buf);
			formatMethodValues(detail, buf);
		}		
		return buf.toString();
	}

	//-----------------------------------------------------------------------||
	//-- Private Methods ----------------------------------------------------||
	//-----------------------------------------------------------------------||
		
	private void add(final StringBuffer buffer, final String value, final char separator) {
		if (buffer.length() > 0 || value == null || value.isEmpty()) {
			buffer.append(separator);
		}
		buffer.append(value);
	}
	
	private void formatKeyValues(final Ejb3StatisticModel model, final StringBuffer buf) {	
		if (model.getKeys() != null) {
			for (final String key : model.getKeys()) {
				final String value = model.getClassStatistics().get(key);
				if (value == null) {
					add(buf, "", _SEPARATOR);
				} else {
					add(buf, value, _SEPARATOR);
				}
			}
		} else {
			for (final String key : model.getClassStatistics().keySet()) {
				final String value = model.getClassStatistics().get(key);
				if (value == null) {
					add(buf, "", _SEPARATOR);
				} else {
					add(buf, value, _SEPARATOR);
				}
			}
		}
	}
	
	private void formatMethodValues(final Ejb3StatisticModel model, final StringBuffer buf) {
		if (model.getMethods() != null) {
			for (final String methodName : model.getMethods()) {
				final Ejb3MethodStatistics methodStat = model.getMethodStatistics().get(methodName);
				if (methodStat == null) {
					add(buf, "", _SEPARATOR);
					add(buf, "", _SEPARATOR);
					add(buf, "", _SEPARATOR);
				} else {
					add(buf, methodStat.getExecutionTime(), _SEPARATOR);
					add(buf, methodStat.getInvocations(), _SEPARATOR);
					add(buf, methodStat.getWaitTime(), _SEPARATOR);
				}
			}
		} else {
			for (final String methodName : model.getMethodStatistics().keySet()) {
				final Ejb3MethodStatistics methodStat = model.getMethodStatistics().get(methodName);
				if (methodStat == null) {
					add(buf, "", _SEPARATOR);
					add(buf, "", _SEPARATOR);
					add(buf, "", _SEPARATOR);
				} else {
					add(buf, methodStat.getExecutionTime(), _SEPARATOR);
					add(buf, methodStat.getInvocations(), _SEPARATOR);
					add(buf, methodStat.getWaitTime(), _SEPARATOR);
				}
			}
		}
	}
}
