package org.statistic.models;

import org.simpleframework.xml.Element;

public class DmrPathElement extends AbstractElement {

	@Element(name = "path-element")
	private String _pathElement;
	
	public String getPathElement() {
		return _pathElement;
	}
	
	public void setPathElement(String pathElement) {
		_pathElement = pathElement;
	}

}
