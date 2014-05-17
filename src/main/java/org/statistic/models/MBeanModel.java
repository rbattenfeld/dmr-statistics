package org.statistic.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;

public class MBeanModel implements Serializable {
	private static final long serialVersionUID = 7771617395302880088L;
	
	@Attribute(name = "name")
	private String _name;
	
	@Attribute(name = "object-name")
	private String _objectName;
		
	@ElementList(entry = "mbean-element", inline = true, required = false)
	private List<MBeanElement> _mbeanElements = new ArrayList<MBeanElement>();
	
	public String getName() {
		return _name;
	}

	public void setName(final String name) {
		_name = name;
	}
	
	public String getObjectName() {
		return _objectName;
	}
	
	public void setObjectName(final String objectName) {
		_objectName = objectName;
	}
	

	public List<MBeanElement> getMBeanElements() {
		return _mbeanElements;
	}

	public void setMBeanElements(final List<MBeanElement> mbeanElements) {
		_mbeanElements = mbeanElements;
	}
	
//	public String toString() {
//		final StringBuffer buf = new StringBuffer();
//		buf.append(String.format("%-25s : %s\n", "Statistic for", _name));	   	
//		for (final PathElement element : _pathElements) {
//		   	for (final Map.Entry<String, Object> entry : element.getStatisticMap().entrySet()) {
//		   		buf.append(String.format("%-25s : %s\n", element.getAbbreviation() + "-" + entry.getKey(), entry.getValue()));
//		   	}	   			
//		}		
//		for (final ChildElement element : _childElements) {
//			buf.append(element);   			
//		}
//   	    return buf.toString();
//    }
	
}
