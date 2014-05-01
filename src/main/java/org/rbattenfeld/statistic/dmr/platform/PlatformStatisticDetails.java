package org.rbattenfeld.statistic.dmr.platform;

import java.io.Serializable;

import org.rbattenfeld.statistic.dmr.ejb3.EjbType;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementArray;

public class PlatformStatisticDetails implements IPlatformStatisticDetails, Serializable {
	private static final long serialVersionUID = 7771617395302880088L;
	
	@Attribute(name = "type")
	private String _type;	

	@Attribute(name = "subType")
	private String _subType;

	@Attribute(name = "keys")
	private String[] _keys;

//	@Attribute(name = "values")
	private String[] _values;

	public PlatformStatisticDetails(
			@Attribute(name = "type") final String type, 
			@Attribute(name = "subType") final String subType, 
			@Attribute(name = "keys") final String[] keys) {
		_type = type;
		_subType = subType;
		_keys = keys;
	}
	
	@Override
	public String getType() {
		return _type;
	}

	@Override
	public void setType(String type) {
		_type = type;
	}
	
	@Override
	public String getSubType() {
		return _subType;
	}

	@Override
	public void setSubType(String subType) {
		_subType = subType;
	}

	@Override
	public String[] getKeys() {
		return _keys;
	}

	@Override
	public void setKeys(String[] keys) {
		_keys = keys;
	}

	@Override
	public String[] getValues() {
		return _values;
	}

	@Override
	public void setValues(String[] values) {
		_values = values;
	}
}
