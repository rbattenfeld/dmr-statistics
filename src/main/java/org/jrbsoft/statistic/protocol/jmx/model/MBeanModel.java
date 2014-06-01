package org.jrbsoft.statistic.protocol.jmx.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.management.ObjectName;

import org.jrbsoft.statistic.model.AbstractElement;
import org.jrbsoft.statistic.model.IProtocolModel;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;

public class MBeanModel implements IProtocolModel, Serializable {
    private static final long serialVersionUID = 7771617395302880088L;

    @Attribute(name = "name")
    private String _name;

    @Attribute(name = "logCategory", required = false)
    private String _logCategory;

    @Attribute(name = "object-name")
    private String _objectName;

    @ElementList(entry = "mbean-element", inline = true, required = false)
    private List<MBeanElement> _mbeanElements = new ArrayList<MBeanElement>();

    private ObjectName _mbeanObjectName = null;
    
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

    public ObjectName getMBeanObjectName() {
        return _mbeanObjectName;
    }

    public void setMBeanObjectName(final ObjectName objectName) {
    	_mbeanObjectName = objectName;
    }
    
    public void updateNotification(final String key, final Object value) {
    	for (final MBeanElement element : getMBeanElements()) {
    		element.updateNotification(key, value);
    	}
    }
    
    @Override
    public List<AbstractElement> getStatisticElements() {
    	final List<AbstractElement> elements = new ArrayList<AbstractElement>();
    	elements.addAll(_mbeanElements);
        return elements;
    }

    public String toString() {
        final StringBuffer buf = new StringBuffer();
        buf.append(String.format("%-25s : %s\n", "Statistic for", _name));
        for (final MBeanElement element : _mbeanElements) {
        	buf.append(element);
        }
        return buf.toString();
    }

}
