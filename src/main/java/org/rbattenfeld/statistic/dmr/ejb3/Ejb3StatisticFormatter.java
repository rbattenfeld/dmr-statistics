package org.rbattenfeld.statistic.dmr.ejb3;

import java.util.List;

import org.rbattenfeld.statistic.dmr.DmrStatisticConfigurer;
import org.rbattenfeld.statistic.dmr.DmrStatisticFormatter;

public class Ejb3StatisticFormatter implements DmrStatisticFormatter<DmrStatisticConfigurer, List<? extends Ejb3ClassStatistic>> {
	public static final char _SEPARATOR = ',';	
	
	@Override
	public String formatHeader(final DmrStatisticConfigurer configurer) {
		final StringBuffer buf = new StringBuffer();		
		for (final IEjb3StatisticDetails detail : configurer.getEjbStatistics()) {	
			final String beanAbbr = detail.getBeanNameAbbr();			
			for (final String key : detail.getKeys()) {
				add(buf, beanAbbr + "-" + key, _SEPARATOR);
			}
			for (final String method : detail.getMethods()) {
				add(buf, beanAbbr + "-" + method + "-executionTime", _SEPARATOR);
				add(buf, beanAbbr + "-" + method + "-invocations", _SEPARATOR);
				add(buf, beanAbbr + "-" + method + "-waitTime", _SEPARATOR);
			}
		}		
		return buf.toString();
	}
	
	@Override
	public String formatLine(final DmrStatisticConfigurer configurer, final List<? extends Ejb3ClassStatistic> stats) {
		final StringBuffer buf = new StringBuffer();		
		for (final IEjb3StatisticDetails detail : configurer.getEjbStatistics()) {
			final Ejb3ClassStatistic classStat = getClassStatistic(detail.getBeanName(),stats);
			if (classStat != null) {
				add(buf, classStat.toCSVString(_SEPARATOR), _SEPARATOR);
			}
		}		
		return buf.toString();
	}

	//-----------------------------------------------------------------------||
	//-- Private Methods ----------------------------------------------------||
	//-----------------------------------------------------------------------||
	
	private Ejb3ClassStatistic getClassStatistic(final String beanName, final List<? extends Ejb3ClassStatistic> stats) {
		for (final Ejb3ClassStatistic stat : stats) {
			if (beanName.equals(stat.getBeanName())) {
				return stat;
			}
		}
		return null;
	}
		
	private void add(final StringBuffer buffer, final Object value, final char separator) {
		if (buffer.length() > 0) {
			buffer.append(separator);
		}
		buffer.append(value);
	}
	
}
