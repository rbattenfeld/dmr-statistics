package org.jrbsoft.statistic.model;

import java.math.BigInteger;

public class BigIntegerMinMax extends AbstractMinMaxType<BigInteger> {

	public BigIntegerMinMax(final BigInteger value) {
		set(value);
	}
	
	@Override
	public BigInteger getMinTypeValue() {
		return BigInteger.valueOf(Long.MIN_VALUE);
	}

	@Override
	public BigInteger getMaxTypeValue() {
		return BigInteger.valueOf(Long.MAX_VALUE);
	}

	@Override
	public Double getAvg() {
		return null;
	}

	@Override
	public boolean isType(Object obj) {
		return (obj instanceof BigInteger);
	}
}
