package org.jrbsoft.statistic.model;

import java.util.HashMap;
import java.util.Map;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;

public abstract class AbstractElement {

    @Attribute(name = "name", required = false)
    private String _name;

    @Attribute(name = "abbreviation")
    private String _abbreviation;

    @Element(name = "keys")
    private String[] _keys;

    private Map<String, AbstractMinMaxType<?>> _statisticMap = new HashMap<>();

    public String getName() {
        return _name;
    }

    public void setName(String name) {
        _name = name;
    }

    public String getAbbreviation() {
        return _abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        _abbreviation = abbreviation;
    }

    public String[] getKeys() {
        return _keys;
    }

    public void setKeys(String[] keys) {
        _keys = keys;
    }

    public void addStatistics(final String key, final Object value) {    	
    	final AbstractMinMaxType<?> type = (AbstractMinMaxType<?>)_statisticMap.get(key);
    	if (type != null) {
    		type.set(AbstractMinMaxType.asMinMaxType(value).getCurrent());
    	} else {
    		_statisticMap.put(key, AbstractMinMaxType.asMinMaxType(value));
    	}
    }

    public boolean containsStatistics(final String key) {
    	return _statisticMap.containsKey(key);
    }
    
    public Object getCurrentValue(final String key) {
    	final AbstractMinMaxType<?> type = (AbstractMinMaxType<?>)_statisticMap.get(key);
    	if (type != null) {
    		return type.getCurrent();
    	} else {
    		return null; 
    	}
    }
    
    public Object getMinValue(final String key) {
    	final AbstractMinMaxType<?> type = (AbstractMinMaxType<?>)_statisticMap.get(key);
    	if (type != null) {
    		return type.getMin();
    	} else {
    		return null; 
    	}
    }

    public Object getMaxValue(final String key) {
    	final AbstractMinMaxType<?> type = (AbstractMinMaxType<?>)_statisticMap.get(key);
    	if (type != null) {
    		return type.getMax();
    	} else {
    		return null; 
    	}
    }

    public Object getAvgValue(final String key) {
    	final AbstractMinMaxType<?> type = (AbstractMinMaxType<?>)_statisticMap.get(key);
    	if (type != null) {
    		return type.getAvg();
    	} else {
    		return null; 
    	}
    }
    
    public String toString() {
        final StringBuffer buf = new StringBuffer();
           buf.append(String.format("%-25s : %s\n", "Statistic for:", _abbreviation));
           for (final Map.Entry<String, AbstractMinMaxType<?>> entry : _statisticMap.entrySet()) {
               buf.append(String.format("%-25s : %s\n", _abbreviation + "-" + entry.getKey(), entry.getValue()));
           }
           return buf.toString();
    }
    
}
