package org.jrbsoft.statistic.model;

import java.math.BigDecimal;

public class BigDecimalMinMax extends AbstractMinMaxType<BigDecimal> {

	public BigDecimalMinMax(final BigDecimal value) {
		set(value);
	}
	
	@Override
	public BigDecimal getMinTypeValue() {
		return BigDecimal.valueOf(Double.MIN_VALUE);
	}

	@Override
	public BigDecimal getMaxTypeValue() {
		return BigDecimal.valueOf(Double.MAX_VALUE);
	}

	@Override
	public Double getAvg() {
		return null;
	}

	@Override
	public boolean isType(Object obj) {
		return (obj instanceof BigDecimal);
	}
}
