package org.jrbsoft.statistic.protocol.jmx.model;

import org.jrbsoft.statistic.model.AbstractElement;


public class MBeanElement extends AbstractElement {

    public void updateNotification(final String key, final Object value) {
    	if (getStatisticMap().containsKey(key)) {
    		getStatisticMap().put(key, value);
    	}
    }
}
