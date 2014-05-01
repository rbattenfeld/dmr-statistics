package org.rbattenfeld.statistic.dmr.platform;

import java.util.List;

import org.rbattenfeld.statistic.dmr.DmrStatisticConfigurer;
import org.rbattenfeld.statistic.dmr.DmrStatisticFormatter;

public class PlatformStatisticFormatter implements DmrStatisticFormatter<DmrStatisticConfigurer, List<? extends IPlatformStatisticDetails>> {
	public static final char _SEPARATOR = ',';	
	

	@Override
	public String formatHeader(DmrStatisticConfigurer configurer) {
		final StringBuffer buf = new StringBuffer();		
		for (final IPlatformStatisticDetails detail : configurer.getPlatformDetailsList()) {	
			final String type = detail.getType();
			final String subType = detail.getSubType();
			for (final String key : detail.getKeys()) {
				add(buf, getHeader(type, subType) + "-" + key, _SEPARATOR);
			}
		}		
		return buf.toString();
	}

	@Override
	public String formatLine(DmrStatisticConfigurer configurer,	List<? extends IPlatformStatisticDetails> source) {
		final StringBuffer buf = new StringBuffer();		
		for (final IPlatformStatisticDetails detail : configurer.getPlatformDetailsList()) {
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
