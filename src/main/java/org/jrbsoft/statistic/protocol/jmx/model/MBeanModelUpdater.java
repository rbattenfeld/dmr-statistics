package org.jrbsoft.statistic.protocol.jmx.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.management.Attribute;
import javax.management.AttributeChangeNotification;
import javax.management.AttributeList;
import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.Notification;
import javax.management.NotificationBroadcaster;
import javax.management.NotificationFilter;
import javax.management.NotificationListener;
import javax.management.ObjectName;
import javax.management.ReflectionException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jrbsoft.statistic.model.IModelUpdater;
import org.jrbsoft.statistic.model.IProtocolModel;
import org.jrbsoft.statistic.model.IRootModel;

public class MBeanModelUpdater implements IModelUpdater {
    private static final Log _Logger = LogFactory.getLog(MBeanModelUpdater.class);
    private final MBeanServer _mbeanServer;
    private final List<MBeanModel> _models = new ArrayList<MBeanModel>();
    private final AttributeChangeListener _attrChangeListener = new AttributeChangeListener();
    private final AttributeChangeFilter _attrChangeFilter = new AttributeChangeFilter();

    public MBeanModelUpdater(final MBeanServer mbeanServer) {
        _mbeanServer = mbeanServer;
    }

    @Override
    public void updateModel(final IRootModel model)    throws IOException {
        updateModels(model.getModels());
    }

    public void updateModels(final List<IProtocolModel> models) throws IOException {
        if (models != null) {
            for (final IProtocolModel protocolModel : models) {
                if (protocolModel instanceof MBeanModel) {
                    update((MBeanModel)protocolModel);
                }
            }
        }
    }

    public void updateMBeanModels(final List<MBeanModel> models) throws IOException {
        if (models != null) {
            for (final MBeanModel model : models) {
                update(model);
            }
        }
    }

    //-----------------------------------------------------------------------||
    //-- Private Methods ----------------------------------------------------||
    //-----------------------------------------------------------------------||

    private void update(final MBeanModel model) {
        try {
        	if (!_models.contains(model)) {        		
	            final ObjectName mbean = getObjectName(model);
	            if (_mbeanServer.isRegistered(mbean)) {
	                for (final MBeanElement element : model.getMBeanElements()) {
	                    updateStatistics(mbean, element.getKeys(), element.getStatisticMap());
	                }
	                if (mbean instanceof NotificationBroadcaster) {
	                	_models.add(model);
	                	register(mbean);
	                }
	            } else {
	                _Logger.warn("Not registered: " + model.getObjectName());
	            }
        	}
        } catch (MalformedObjectNameException | AttributeNotFoundException | InstanceNotFoundException | MBeanException | ReflectionException ex) {
            _Logger.debug("Error by extracting mbean value. Reason: " + ex.getMessage());
        }
    }

    private void updateStatistics(final ObjectName mbean, final String[] keys, final Map<String, Object> valuesMap) throws AttributeNotFoundException, InstanceNotFoundException, MBeanException, ReflectionException {
        if (keys != null) {
        	final AttributeList values = _mbeanServer.getAttributes(mbean, keys);
        	for (Attribute attr : values.asList()) {
        		valuesMap.put(attr.getName(), attr.getValue());
        	}
        }
    }
    
    private ObjectName getObjectName(final MBeanModel model) throws MalformedObjectNameException {
    	if (model.getMBeanObjectName() == null) {
    		model.setMBeanObjectName(new ObjectName(model.getObjectName()));
        }
    	return model.getMBeanObjectName();
    }
    
    private void register(final ObjectName mbean) throws InstanceNotFoundException {
    	_mbeanServer.addNotificationListener(mbean, _attrChangeListener, _attrChangeFilter, null);
    }

    //-----------------------------------------------------------------------||
    //--Private Classes -----------------------------------------------------||
    //-----------------------------------------------------------------------||

    private class AttributeChangeFilter implements NotificationFilter {
		private static final long serialVersionUID = 1L;

		public boolean isNotificationEnabled(final Notification notification) {
			if (notification instanceof AttributeChangeNotification) {
				return true;
			} else {
				return false;
			}
		}
	}

	private class AttributeChangeListener implements NotificationListener {

		public void handleNotification(final Notification notification, final Object handback) {
			if (notification instanceof AttributeChangeNotification) {
				final AttributeChangeNotification changeNotification = (AttributeChangeNotification) notification;
				for (final MBeanModel model : _models) {
					model.updateNotification(changeNotification.getAttributeName(), changeNotification.getNewValue());
				}				
			}
		}

	}
}
