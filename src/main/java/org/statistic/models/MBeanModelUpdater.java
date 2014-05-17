package org.statistic.models;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.ReflectionException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.statistic.connectors.IMBeanControllerClient;

public class MBeanModelUpdater implements IModelUpdater {
	private static final Log _Logger = LogFactory.getLog(MBeanModelUpdater.class);
	private final MBeanServer _mbeanServer;
	
	public MBeanModelUpdater(final IMBeanControllerClient client) {
		_mbeanServer = client.getEmbeddedMBeanServer();
	}
	
	@Override
	public void updateModel(final IRootModel model)	throws IOException {
		updateModel(model.getMBeanModels());
	}

	public void updateModel(final List<MBeanModel> models) throws IOException {
		for (final MBeanModel model : models) {
			try {
				final ObjectName mbean = new ObjectName(model.getObjectName());
				if (_mbeanServer.isRegistered(mbean)) {
					for (final MBeanElement element : model.getMBeanElements()) {
						updateStatistics(mbean, element.getKeys(), element.getStatisticMap());
					}
				} else {
					_Logger.warn("Not registered: " + model.getObjectName());
				}
			} catch (MalformedObjectNameException | AttributeNotFoundException | InstanceNotFoundException | MBeanException | ReflectionException ex) {
				_Logger.debug("Error by extracting mbean value. Reason: " + ex.getMessage());
			}
		}
	}
	
	//-----------------------------------------------------------------------||
	//-- Private Methods ----------------------------------------------------||
	//-----------------------------------------------------------------------||
	
	private void updateStatistics(final ObjectName mbean, final String[] keys, final Map<String, Object> valuesMap) throws AttributeNotFoundException, InstanceNotFoundException, MBeanException, ReflectionException {
		if (keys != null) {
			for (final String key : keys) {
				valuesMap.put(key, _mbeanServer.getAttribute(mbean, key));
			}
		} 
	}
}
