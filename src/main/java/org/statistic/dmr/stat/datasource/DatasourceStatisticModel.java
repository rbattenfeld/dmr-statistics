package org.statistic.dmr.stat.datasource;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;

public class DatasourceStatisticModel implements Serializable {
	private static final long serialVersionUID = 7771617395302880088L;
	
	@Attribute(name = "name")
	private String _datasource;	

	@Element(name = "jdbc", required = false)
	private String[] _jdbcKeys;

	@Element(name = "pool", required = false)
	private String[] _poolKeys;

    private Map<String, Object> _jdbcStatisticMap = new HashMap<>();
	
	private Map<String, Object> _poolStatisticMap = new HashMap<>();
	
	public String getDatasource() {
		return _datasource;
	}

	public void setDatasource(String datasource) {
		_datasource = datasource;
	}

	public String[] getJdbcKeys() {
		return _jdbcKeys;
	}

	public void setJdbcKeys(String[] jdbckeys) {
		_jdbcKeys = jdbckeys;
	}

	public String[] getPoolKeys() {
		return _poolKeys;
	}

	public void setPoolKeys(String[] poolkeys) {
		_poolKeys = poolkeys;
	}
	
	public void addJdbcStatistics(final String key, final Object value) {
		_jdbcStatisticMap.put(key, value);
	}
	
	public void addPoolStatistics(final String methodName, final Object value) {
		_poolStatisticMap.put(methodName, value);
	}
	
	public Map<String, Object> getJdbcStatistics() {
		return _jdbcStatisticMap;
	}
	
	public Map<String, Object> getPoolStatistics() {
		return _poolStatisticMap;
	}
	
	public String toString() {
		final StringBuffer buf = new StringBuffer();
	   	buf.append(String.format("%-25s : %s\n", "DataSource", getDatasource()));
	   	
	   	for (final Map.Entry<String, Object> entry : _poolStatisticMap.entrySet()) {
	   		buf.append(String.format("%-25s : %s\n", "pool-" + entry.getKey(), entry.getValue()));
	   	}
	   	
	   	for (final Map.Entry<String, Object> entry : _jdbcStatisticMap.entrySet()) {
	   		buf.append(String.format("%-25s : %s\n", "jdbc-" + entry.getKey(), entry.getValue()));
	   	}
   	    return buf.toString();
    }
}
