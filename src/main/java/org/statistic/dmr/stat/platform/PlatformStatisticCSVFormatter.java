package org.statistic.dmr.stat.platform;

import org.statistic.dmr.api.IDmrModel;
import org.statistic.dmr.api.IDmrStatisticFormatter;

public class PlatformStatisticCSVFormatter implements IDmrStatisticFormatter<String> {
	public static final char _SEPARATOR = ',';	
	

	@Override
	public String formatHeader(final IDmrModel model) {
		final StringBuffer buf = new StringBuffer();		
		for (final PlatformStatisticModel detail : model.getPlatformStatisticModels()) {	
			final String type = detail.getType();
			final String subType = detail.getSubType();
			for (final String key : detail.getKeys()) {
				add(buf, getHeader(type, subType) + "-" + key, _SEPARATOR);
			}
		}		
		return buf.toString();
	}

	@Override
	public String formatLine(final IDmrModel model) {
		final StringBuffer buf = new StringBuffer();		
		for (final PlatformStatisticModel detail : model.getPlatformStatisticModels()) {
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
