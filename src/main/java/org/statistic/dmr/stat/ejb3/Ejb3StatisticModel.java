package org.statistic.dmr.stat.ejb3;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;

/**
 * This class defines the EJB3 bean statistical details which have to be extracted.
 */
public class Ejb3StatisticModel implements Serializable {
	private static final long serialVersionUID = -1592685802037096782L;
	
	@Attribute(name = "name")
	private final String _beanName;
	
	@Attribute(name = "abbr")
	private final String _beanNameAbbr;
	
	@Attribute(name = "type")
	private final EjbType _type;
	
	@Element(name = "keys")
	private final String[] _keys;
		
	@Element(name = "methods")
	private final String[] _methods;
	
	private Map<String, Object> _classStatisticMap = new HashMap<>();
	
	private Map<String, Ejb3MethodStatistics> _methodStatisticMap = new HashMap<>();
	
	/**
	 * Constructs a new instance of the class.
	 * @param beanName the name of bean.
	 * @param type the type of the bean.
	 * @param keys defines which core statistics to extract. 
	 * @param methods defined which methods statistics to extract.
	 */
	public Ejb3StatisticModel(
			@Attribute(name="name") final String beanName,
			@Attribute(name="abbr") final String beanNameAbbr,  
			@Attribute(name="type") final EjbType type, 
			@Element(name = "keys") final String[] keys,
			@Element(name = "methods") final String[] methods) {
		_beanName = beanName;
		_beanNameAbbr = beanNameAbbr;
		_type = type;		
		_keys = keys;
		_methods = methods;
	}
	
	public String getBeanName() {
		return _beanName;
	}

	public String getBeanNameAbbr() {
		return _beanNameAbbr;
	}
	
	public String[] getKeys() {
		return _keys;
	}

	public String[] getMethods() {
		return _methods;
	}

	public EjbType getEjbType() {
		return _type;
	}
	
	public void addClassStatistics(final String key, final Object value) {
		_classStatisticMap.put(key, value);
	}
	
	public void addMethodStatistics(final String methodName, final String executionTime, final String invocations, final String waitTime) {
		_methodStatisticMap.put(methodName, new Ejb3MethodStatistics(executionTime, invocations, waitTime));
	}
	
	public Map<String, Object> getClassStatistics() {
		return _classStatisticMap;
	}
	
	public Map<String, Ejb3MethodStatistics> getMethodStatistics() {
		return _methodStatisticMap;
	}
		
	public String toString() {
		final StringBuffer buf = new StringBuffer();
	   	buf.append(String.format("%-25s : %s\n", "Bean", getBeanName()));
	   	
	   	for (final Map.Entry<String, Object> entry : _classStatisticMap.entrySet()) {
	   		buf.append(String.format("%-25s : %s\n", entry.getKey(), entry.getValue()));
	   	}
	   	
	   	for (final Map.Entry<String, Ejb3MethodStatistics> entry : _methodStatisticMap.entrySet()) {
	   		buf.append(String.format("%-25s : %s\n", entry.getKey() + " executionTime", entry.getValue().getExecutionTime()));
	   		buf.append(String.format("%-25s : %s\n", entry.getKey() + " invocations", entry.getValue().getInvocations()));
	   		buf.append(String.format("%-25s : %s\n", entry.getKey() + " waitTime", entry.getValue().getWaitTime()));
	   	}   	
   	    return buf.toString();
    }
}
