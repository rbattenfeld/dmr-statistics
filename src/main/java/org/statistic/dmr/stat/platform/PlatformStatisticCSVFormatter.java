package org.statistic.dmr.stat.platform;

import org.statistic.dmr.api.IDmrModel;
import org.statistic.dmr.api.IDmrStatisticFormatter;
import org.statistic.dmr.stat.StatisticUtil;

public class PlatformStatisticCSVFormatter implements IDmrStatisticFormatter<String> {	

	@Override
	public String formatHeader(final IDmrModel model) {
		final StringBuffer buf = new StringBuffer();		
		for (final PlatformStatisticModel detail : model.getPlatformStatisticModels()) {	
			final String type = detail.getType();
			final String subType = detail.getSubType();
			for (final String key : detail.getKeys()) {
				StatisticUtil.add(buf, getHeader(type, subType) + "-" + key, model.getCsvSeparator());
			}
		}		
		return buf.toString();
	}

	@Override
	public String formatLine(final IDmrModel rootModel) {
		final StringBuffer buf = new StringBuffer();		
		for (final PlatformStatisticModel model : rootModel.getPlatformStatisticModels()) {
			for (final String key : model.getKeys()) {
				StatisticUtil.add(buf, model.getStatisticMap().get(key), rootModel.getCsvSeparator());
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

}
