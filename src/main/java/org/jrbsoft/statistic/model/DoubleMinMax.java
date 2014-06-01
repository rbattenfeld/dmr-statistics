package org.jrbsoft.statistic.model;


public class DoubleMinMax extends AbstractMinMaxType<Double> {

	public DoubleMinMax(final Double value) {
		set(value);
	}
	
	@Override
	public Double getMinTypeValue() {
		return Double.MIN_VALUE;
	}

	@Override
	public Double getMaxTypeValue() {
		return Double.MAX_VALUE;
	}

	@Override
	public Double getAvg() {
		return null;
	}

	@Override
	public boolean isType(Object obj) {
		return (obj instanceof Double);
	}
}
