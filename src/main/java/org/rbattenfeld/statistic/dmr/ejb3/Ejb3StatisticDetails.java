package org.rbattenfeld.statistic.dmr.ejb3;

import java.io.Serializable;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementArray;

/**
 * This class defines the EJB3 bean statistical details which have to be extracted.
 */
public class Ejb3StatisticDetails implements IEjb3StatisticDetails, Serializable {
	private static final long serialVersionUID = -1592685802037096782L;
	
	@Attribute(name = "beanName")
	private final String _beanName;
	
	@Attribute(name = "beanNameAbbr")
	private final String _beanNameAbbr;
	
	@Attribute(name = "type")
	private final EjbType _type;
	
	@ElementArray(name = "keys")
	private final String[] _keys;
	
	@ElementArray(name = "methods")
	private final String[] _methods;
	
	/**
	 * Constructs a new instance of the class.
	 * @param beanName the name of bean.
	 * @param type the type of the bean.
	 * @param keys defines which core statistics to extract. 
	 * @param methods defined which methods statistics to extract.
	 */
	public Ejb3StatisticDetails(
			@Attribute(name="beanName") final String beanName,
			@Attribute(name="beanNameAbbr") final String beanNameAbbr,  
			@Attribute(name="type") final EjbType type, 
			@ElementArray(name = "keys") final String[] keys,
			@ElementArray(name = "methods") final String[] methods) {
		_beanName = beanName;
		_beanNameAbbr = beanNameAbbr;
		_type = type;		
		_keys = keys;
		_methods = methods;
	}
	
	@Override
	public String getBeanName() {
		return _beanName;
	}

	@Override
	public String getBeanNameAbbr() {
		return _beanNameAbbr;
	}
	
	@Override
	public String[] getKeys() {
		return _keys;
	}

	@Override
	public String[] getMethods() {
		return _methods;
	}

	@Override
	public EjbType getEjbType() {
		return _type;
	}

}
