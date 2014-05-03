package org.statistic.dmr.stat.platform;

import java.util.List;

import org.statistic.dmr.api.DmrStatisticFormatter;

public class PlatformStatisticCSVFormatter implements DmrStatisticFormatter<List<? extends PlatformStatisticModel>, String> {
	public static final char _SEPARATOR = ',';	
	

	@Override
	public String formatHeader(List<? extends PlatformStatisticModel> source) {
		final StringBuffer buf = new StringBuffer();		
		for (final PlatformStatisticModel detail : source) {	
			final String type = detail.getType();
			final String subType = detail.getSubType();
			for (final String key : detail.getKeys()) {
				add(buf, getHeader(type, subType) + "-" + key, _SEPARATOR);
			}
		}		
		return buf.toString();
	}

	@Override
	public String formatLine(List<? extends PlatformStatisticModel> source) {
		final StringBuffer buf = new StringBuffer();		
		for (final PlatformStatisticModel detail : source) {
			for (final String value : detail.getValues()) {
				add(buf, value, _SEPARATOR);
			}
		}		
		return buf.toString();
	}

	//-----------------------------------------------------------------------||
	//-- Private Methods ----------------------------------------------------||
	//-----------------------------------------------------------------------||
	
	private String getHeader(final String type, final String subType) {
		if (subType != null && !subType.isEmpty()) {
			return subType;
		} else {
			return type;
		}
	}

	private void add(final StringBuffer buffer, final Object value, final char separator) {
		if (buffer.length() > 0) {
			buffer.append(separator);
		}
		buffer.append(value);
	}	
}
