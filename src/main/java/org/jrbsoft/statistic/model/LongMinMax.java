package org.jrbsoft.statistic.model;

public class LongMinMax extends AbstractMinMaxType<Long> {

	public LongMinMax(final Long value) {
		set(value);
	}
	
	@Override
	public Long getMinTypeValue() {
		return Long.MIN_VALUE;
	}

	@Override
	public Long getMaxTypeValue() {
		return Long.MAX_VALUE;
	}

	@Override
	public Double getAvg() {
		return null;
	}

	@Override
	public boolean isType(Object obj) {
		return (obj instanceof Long);
	}
}
