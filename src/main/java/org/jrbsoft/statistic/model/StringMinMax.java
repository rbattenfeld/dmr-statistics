package org.jrbsoft.statistic.model;

public class StringMinMax extends AbstractMinMaxType<String> {

	public StringMinMax(final String value) {
		set(value);
	}
	
	@Override
	public String getMinTypeValue() {
		return "";
	}

	@Override
	public String getMaxTypeValue() {
		return "";
	}

	@Override
	public Double getAvg() {
		return 0.0;
	}

	@Override
	public boolean isType(Object obj) {
		return (obj instanceof String);
	}
}
