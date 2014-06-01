package org.jrbsoft.statistic.model;

public class IntegerMinMax extends AbstractMinMaxType<Integer> {

	public IntegerMinMax(final Integer value) {
		set(value);
	}
	
	@Override
	public Integer getMinTypeValue() {
		return Integer.MIN_VALUE;
	}

	@Override
	public Integer getMaxTypeValue() {
		return Integer.MAX_VALUE;
	}

	@Override
	public Double getAvg() {
		return null;
	}

	@Override
	public boolean isType(Object obj) {
		return (obj instanceof Integer);
	}
}
