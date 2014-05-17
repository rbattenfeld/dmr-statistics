package org.statistic.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;

public class DmrModel implements Serializable {
	private static final long serialVersionUID = 7771617395302880088L;
	
	@Attribute(name = "name")
	private String _name;
	
	@Attribute(name = "path-address")
	private String _pathAddress;
		
	@ElementList(entry = "child-element", inline = true, required = false)
	private List<DmrChildElement> _childElements = new ArrayList<DmrChildElement>();
	
	@ElementList(entry = "path-element", inline = true, required = false)
	private List<DmrPathElement> _pathElements = new ArrayList<DmrPathElement>();
	
	public String getName() {
		return _name;
	}

	public void setName(final String name) {
		_name = name;
	}
	
	public String getPathAddress() {
		return _pathAddress;
	}
	
	public void setPathAddress(final String pathAddress) {
		_pathAddress = pathAddress;
	}
	
	public List<DmrPathElement> getPathElements() {
		return _pathElements;
	}

	public void setPathElements(final List<DmrPathElement> pathElements) {
		_pathElements = pathElements;
	}

	public List<DmrChildElement> getChildElements() {
		return _childElements;
	}

	public void setChildElements(final List<DmrChildElement> pathElements) {
		_childElements = pathElements;
	}
	
	public String toString() {
		final StringBuffer buf = new StringBuffer();
		buf.append(String.format("%-25s : %s\n", "Statistic for", _name));	   	
		for (final DmrPathElement element : _pathElements) {
		   	for (final Map.Entry<String, Object> entry : element.getStatisticMap().entrySet()) {
		   		buf.append(String.format("%-25s : %s\n", element.getAbbreviation() + "-" + entry.getKey(), entry.getValue()));
		   	}	   			
		}		
		for (final DmrChildElement element : _childElements) {
			buf.append(element);   			
		}
   	    return buf.toString();
    }
	
}
