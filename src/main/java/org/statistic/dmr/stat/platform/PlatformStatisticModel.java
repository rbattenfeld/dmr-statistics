package org.statistic.dmr.stat.platform;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;

public class PlatformStatisticModel implements Serializable {
	private static final long serialVersionUID = 7771617395302880088L;
	
	@Attribute(name = "type")
	private String _type;	

	@Attribute(name = "sub-type")
	private String _subType;

	@Element(name = "keys")
	private String[] _keys;

	private Map<String, Object> _statisticMap = new HashMap<>();

	public PlatformStatisticModel(
			@Attribute(name = "type") final String type, 
			@Attribute(name = "sub-type") final String subType, 
			@Element(name = "keys") final String[] keys) {
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
	
	public void addStatistics(final String key, final Object value) {
		_statisticMap.put(key, value);
	}
	
	public Map<String, Object> getStatisticMap() {
		return _statisticMap;
	}

	public void setStatisticMap(final Map<String, Object> statisticMap) {
		_statisticMap = statisticMap;
	}

}
