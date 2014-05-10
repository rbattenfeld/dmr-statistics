package org.statistic.dmr.stat.datasource;

import org.statistic.dmr.api.IDmrModel;
import org.statistic.dmr.api.IDmrStatisticFormatter;
import org.statistic.dmr.stat.StatisticUtil;

public class DataSourcesStatisticCSVFormatter implements IDmrStatisticFormatter<String> {
	
	@Override
	public String formatHeader(final IDmrModel rootModel) {
		final StringBuffer buf = new StringBuffer();		
		for (final DatasourceStatisticModel model : rootModel.getDataSourceStatisticModels()) {	
			final String dsName = model.getDatasource();
			if (model.getJdbcKeys() != null) {
				for (final String key : model.getJdbcKeys()) {
					StatisticUtil.add(buf, dsName + "-" + key, rootModel.getCsvSeparator());
				}			
			} else {
				for (final String key : model.getPoolStatistics().keySet()) {
					StatisticUtil.add(buf, dsName + "-" + key, rootModel.getCsvSeparator());
				}
			}
			
			if (model.getPoolKeys() != null) {
				for (final String key : model.getPoolKeys()) {
					StatisticUtil.add(buf, dsName + "-" + key, rootModel.getCsvSeparator());
				}			
			} else {
				for (final String key : model.getJdbcStatistics().keySet()) {
					StatisticUtil.add(buf, dsName + "-" + key, rootModel.getCsvSeparator());
				}
			}
		}		
		return buf.toString();
	}
	
	@Override
	public String formatLine(final IDmrModel rootModel) {
		final StringBuffer buf = new StringBuffer();		
		for (final DatasourceStatisticModel model : rootModel.getDataSourceStatisticModels()) {	
			if (model.getJdbcKeys() != null) {
				for (final String key : model.getJdbcKeys()) {
					if (model.getJdbcStatistics().containsKey(key)) {
						StatisticUtil.add(buf, model.getJdbcStatistics().get(key), rootModel.getCsvSeparator());
					} else {
						StatisticUtil.add(buf, "", rootModel.getCsvSeparator());
					}
				}			
			} else {
				for (final String key : model.getJdbcStatistics().keySet()) {
					StatisticUtil.add(buf, model.getJdbcStatistics().get(key), rootModel.getCsvSeparator());
				}
			}
			
			if (model.getPoolKeys() != null) {
				for (final String key : model.getPoolKeys()) {
					if (model.getPoolStatistics().containsKey(key)) {
						StatisticUtil.add(buf, model.getPoolStatistics().get(key), rootModel.getCsvSeparator());
					} else {
						StatisticUtil.add(buf, "", rootModel.getCsvSeparator());
					}
				}			
			} else {
				for (final String key : model.getPoolStatistics().keySet()) {
					StatisticUtil.add(buf, model.getPoolStatistics().get(key), rootModel.getCsvSeparator());
				}
			}
		}		
		return buf.toString();
	}
	
}
