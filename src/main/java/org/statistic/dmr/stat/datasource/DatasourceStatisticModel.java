package org.statistic.dmr.stat.datasource;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.simpleframework.xml.Attribute;

public class DatasourceStatisticModel implements Serializable {
	private static final long serialVersionUID = 7771617395302880088L;
	
	@Attribute(name = "datasource")
	private String _datasource;	

	@Attribute(name = "jdbckeys", required = false)
	private String[] _jdbcKeys;

	@Attribute(name = "poolkeys", required = false)
	private String[] _poolKeys;

    private Map<String, String> _jdbcStatisticMap = new HashMap<>();
	
	private Map<String, String> _poolStatisticMap = new HashMap<>();
	
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
	
	public void addJdbcStatistics(final String key, final String value) {
		_jdbcStatisticMap.put(key, value);
	}
	
	public void addPoolStatistics(final String methodName, final String value) {
		_poolStatisticMap.put(methodName, value);
	}
	
	public Map<String, String> getJdbcStatistics() {
		return _jdbcStatisticMap;
	}
	
	public Map<String, String> getPoolStatistics() {
		return _poolStatisticMap;
	}
	
	public String toString() {
		final StringBuffer buf = new StringBuffer();
	   	buf.append(String.format("%-25s : %s\n", "DataSource", getDatasource()));
	   	
	   	for (final Map.Entry<String, String> entry : _poolStatisticMap.entrySet()) {
	   		buf.append(String.format("%-25s : %s\n", "pool-" + entry.getKey(), entry.getValue()));
	   	}
	   	
	   	for (final Map.Entry<String, String> entry : _jdbcStatisticMap.entrySet()) {
	   		buf.append(String.format("%-25s : %s\n", "jdbc-" + entry.getKey(), entry.getValue()));
	   	}
   	    return buf.toString();
    }
}
