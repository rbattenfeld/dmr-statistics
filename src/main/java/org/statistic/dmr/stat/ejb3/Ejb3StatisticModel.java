package org.statistic.dmr.stat.ejb3;

import java.io.Serializable;

import org.simpleframework.xml.Attribute;

/**
 * This class defines the EJB3 bean statistical details which have to be extracted.
 */
public class Ejb3StatisticModel implements Serializable {
	private static final long serialVersionUID = -1592685802037096782L;
	
	@Attribute(name = "beanName")
	private final String _beanName;
	
	@Attribute(name = "beanNameAbbr")
	private final String _beanNameAbbr;
	
	@Attribute(name = "type")
	private final EjbType _type;
	
	@Attribute(name = "keys")
	private final String[] _keys;
		
	@Attribute(name = "methods")
	private final String[] _methods;
	
	@Attribute(name = "keyValues", required = false)
	private String[] _keyValues;
	
	@Attribute(name = "methodValues", required = false)
	private String[] _methodValues;
	
	/**
	 * Constructs a new instance of the class.
	 * @param beanName the name of bean.
	 * @param type the type of the bean.
	 * @param keys defines which core statistics to extract. 
	 * @param methods defined which methods statistics to extract.
	 */
	public Ejb3StatisticModel(
			@Attribute(name="beanName") final String beanName,
			@Attribute(name="beanNameAbbr") final String beanNameAbbr,  
			@Attribute(name="type") final EjbType type, 
			@Attribute(name = "keys") final String[] keys,
			@Attribute(name = "methods") final String[] methods) {
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

	public String[] getKeyValues() {
		return _keyValues;
	}

	public String[] getMethodValues() {
		return _methodValues;
	}

	public void setKeyValues(String[] keyValues) {
		_keyValues = keyValues;
	}

	public void setMethodValues(String[] methodValues) {
		_methodValues = methodValues;
	}
	
}
