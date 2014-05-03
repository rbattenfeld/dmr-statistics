package org.statistic.dmr.stat.platform;

import java.io.Serializable;

import org.simpleframework.xml.Attribute;

public class PlatformStatisticModel implements Serializable {
	private static final long serialVersionUID = 7771617395302880088L;
	
	@Attribute(name = "type")
	private String _type;	

	@Attribute(name = "subType")
	private String _subType;

	@Attribute(name = "keys")
	private String[] _keys;

	@Attribute(name = "values", required = false)
	private String[] _values;

	public PlatformStatisticModel(
			@Attribute(name = "type") final String type, 
			@Attribute(name = "subType") final String subType, 
			@Attribute(name = "keys") final String[] keys) {
		_type = type;
		_subType = subType;
		_keys = keys;
	}
	
	public String getType() {
		return _type;
	}

	public void setType(String type) {
		_type = type;
	}
	
	public String getSubType() {
		return _subType;
	}

	public void setSubType(String subType) {
		_subType = subType;
	}

	public String[] getKeys() {
		return _keys;
	}

	public void setKeys(String[] keys) {
		_keys = keys;
	}

	public String[] getValues() {
		return _values;
	}

	public void setValues(String[] values) {
		_values = values;
	}
}
