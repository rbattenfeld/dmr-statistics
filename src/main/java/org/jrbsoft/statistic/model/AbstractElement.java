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

    private Map<String, Object> _statisticMap = new HashMap<>();

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
        _statisticMap.put(key, value);
    }

    public Map<String, Object> getStatisticMap() {
        return _statisticMap;
    }

    public void setStatisticMap(final Map<String, Object> statisticMap) {
        _statisticMap = statisticMap;
    }

    public String toString() {
        final StringBuffer buf = new StringBuffer();
           buf.append(String.format("%-25s : %s\n", "Statistic for:", _abbreviation));
           for (final Map.Entry<String, Object> entry : _statisticMap.entrySet()) {
               buf.append(String.format("%-25s : %s\n", _abbreviation + "-" + entry.getKey(), entry.getValue()));
           }
           return buf.toString();
    }
}
