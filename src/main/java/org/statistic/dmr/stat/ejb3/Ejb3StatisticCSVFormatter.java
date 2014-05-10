package org.statistic.dmr.stat.ejb3;

import org.statistic.dmr.api.IDmrModel;
import org.statistic.dmr.api.IDmrStatisticFormatter;
import org.statistic.dmr.stat.StatisticUtil;

public class Ejb3StatisticCSVFormatter implements IDmrStatisticFormatter<String> {
	
	@Override
	public String formatHeader(final IDmrModel model) {
		final StringBuffer buf = new StringBuffer();		
		for (final Ejb3StatisticModel detail : model.getEjbStatisticModels()) {	
			final String beanAbbr = detail.getBeanNameAbbr();
			if (detail.getKeys() != null) {
				for (final String key : detail.getKeys()) {
					StatisticUtil.add(buf, beanAbbr + "-" + key, model.getCsvSeparator());
				}
			}
			if (detail.getKeys() != null) {
				for (final String method : detail.getMethods()) {
					StatisticUtil.add(buf, beanAbbr + "-" + method + "-executionTime", model.getCsvSeparator());
					StatisticUtil.add(buf, beanAbbr + "-" + method + "-invocations", model.getCsvSeparator());
					StatisticUtil.add(buf, beanAbbr + "-" + method + "-waitTime", model.getCsvSeparator());
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
		
	private void formatKeyValues(final Ejb3StatisticModel model, final StringBuffer buf, final char separator) {	
		if (model.getKeys() != null) {
			for (final String key : model.getKeys()) {
				final Object value = model.getClassStatistics().get(key);
				if (value == null) {
					StatisticUtil.add(buf, "", separator);
				} else {
					StatisticUtil.add(buf, value, separator);
				}
			}
		} else {
			for (final String key : model.getClassStatistics().keySet()) {
				final Object value = model.getClassStatistics().get(key);
				if (value == null) {
					StatisticUtil.add(buf, "", separator);
				} else {
					StatisticUtil.add(buf, value, separator);
				}
			}
		}
	}
	
	private void formatMethodValues(final Ejb3StatisticModel model, final StringBuffer buf, final char separator) {
		if (model.getMethods() != null) {
			for (final String methodName : model.getMethods()) {
				final Ejb3MethodStatistics methodStat = model.getMethodStatistics().get(methodName);
				if (methodStat == null) {
					StatisticUtil.add(buf, "", separator);
					StatisticUtil.add(buf, "", separator);
					StatisticUtil.add(buf, "", separator);
				} else {
					StatisticUtil.add(buf, methodStat.getExecutionTime(), separator);
					StatisticUtil.add(buf, methodStat.getInvocations(), separator);
					StatisticUtil.add(buf, methodStat.getWaitTime(), separator);
				}
			}
		} else {
			for (final String methodName : model.getMethodStatistics().keySet()) {
				final Ejb3MethodStatistics methodStat = model.getMethodStatistics().get(methodName);
				if (methodStat == null) {
					StatisticUtil.add(buf, "", separator);
					StatisticUtil.add(buf, "", separator);
					StatisticUtil.add(buf, "", separator);
				} else {
					StatisticUtil.add(buf, methodStat.getExecutionTime(), separator);
					StatisticUtil.add(buf, methodStat.getInvocations(), separator);
					StatisticUtil.add(buf, methodStat.getWaitTime(), separator);
				}
			}
		}
	}
}
