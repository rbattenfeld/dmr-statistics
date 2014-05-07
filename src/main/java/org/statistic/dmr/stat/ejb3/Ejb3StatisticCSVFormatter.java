package org.statistic.dmr.stat.ejb3;

import org.statistic.dmr.api.IDmrModel;
import org.statistic.dmr.api.IDmrStatisticFormatter;

public class Ejb3StatisticCSVFormatter implements IDmrStatisticFormatter<String> {
	
	@Override
	public String formatHeader(final IDmrModel model) {
		final StringBuffer buf = new StringBuffer();		
		for (final Ejb3StatisticModel detail : model.getEjbStatisticModels()) {	
			final String beanAbbr = detail.getBeanNameAbbr();
			if (detail.getKeys() != null) {
				for (final String key : detail.getKeys()) {
					add(buf, beanAbbr + "-" + key, model.getCsvSeparator());
				}
			}
			if (detail.getKeys() != null) {
				for (final String method : detail.getMethods()) {
					add(buf, beanAbbr + "-" + method + "-executionTime", model.getCsvSeparator());
					add(buf, beanAbbr + "-" + method + "-invocations", model.getCsvSeparator());
					add(buf, beanAbbr + "-" + method + "-waitTime", model.getCsvSeparator());
				}
			}
		}		
		return buf.toString();
	}
	
	@Override
	public String formatLine(final IDmrModel model) {
		final StringBuffer buf = new StringBuffer();		
		for (final Ejb3StatisticModel detail : model.getEjbStatisticModels()) {
			formatKeyValues(detail, buf, model.getCsvSeparator());
			formatMethodValues(detail, buf, model.getCsvSeparator());
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
	
	private void formatKeyValues(final Ejb3StatisticModel model, final StringBuffer buf, final char separator) {	
		if (model.getKeys() != null) {
			for (final String key : model.getKeys()) {
				final String value = model.getClassStatistics().get(key);
				if (value == null) {
					add(buf, "", separator);
				} else {
					add(buf, value, separator);
				}
			}
		} else {
			for (final String key : model.getClassStatistics().keySet()) {
				final String value = model.getClassStatistics().get(key);
				if (value == null) {
					add(buf, "", separator);
				} else {
					add(buf, value, separator);
				}
			}
		}
	}
	
	private void formatMethodValues(final Ejb3StatisticModel model, final StringBuffer buf, final char separator) {
		if (model.getMethods() != null) {
			for (final String methodName : model.getMethods()) {
				final Ejb3MethodStatistics methodStat = model.getMethodStatistics().get(methodName);
				if (methodStat == null) {
					add(buf, "", separator);
					add(buf, "", separator);
					add(buf, "", separator);
				} else {
					add(buf, methodStat.getExecutionTime(), separator);
					add(buf, methodStat.getInvocations(), separator);
					add(buf, methodStat.getWaitTime(), separator);
				}
			}
		} else {
			for (final String methodName : model.getMethodStatistics().keySet()) {
				final Ejb3MethodStatistics methodStat = model.getMethodStatistics().get(methodName);
				if (methodStat == null) {
					add(buf, "", separator);
					add(buf, "", separator);
					add(buf, "", separator);
				} else {
					add(buf, methodStat.getExecutionTime(), separator);
					add(buf, methodStat.getInvocations(), separator);
					add(buf, methodStat.getWaitTime(), separator);
				}
			}
		}
	}
}
