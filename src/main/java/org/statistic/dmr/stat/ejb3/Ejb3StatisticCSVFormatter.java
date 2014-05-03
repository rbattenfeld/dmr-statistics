package org.statistic.dmr.stat.ejb3;

import java.util.List;

import org.statistic.dmr.api.DmrStatisticFormatter;

public class Ejb3StatisticCSVFormatter implements DmrStatisticFormatter<List<? extends Ejb3StatisticModel>, String> {
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
	
	private void formatKeyValues(final Ejb3StatisticModel detail, final StringBuffer buf) {
		if (detail.getKeyValues() != null) {
			for (final String value : detail.getKeyValues()) {
				add(buf, value, _SEPARATOR);
			}
		} else {
			for (int i = 0; i < detail.getKeys().length; i++) {
				add(buf, "", _SEPARATOR);
			}
		}
	}
	
	private void formatMethodValues(final Ejb3StatisticModel detail, final StringBuffer buf) {
		if (detail.getMethodValues() != null) {
			for (final String value : detail.getMethodValues()) {
				for (String stat : value.split(":", -1)) {
					add(buf, stat, _SEPARATOR);
				}
			}
		} else {
			for (int i = 0; i < detail.getMethods().length; i++) {
				add(buf, "", _SEPARATOR);
				add(buf, "", _SEPARATOR);
				add(buf, "", _SEPARATOR);
			}
		}
	}
}
