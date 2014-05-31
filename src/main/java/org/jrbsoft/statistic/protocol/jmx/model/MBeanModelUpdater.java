package org.jrbsoft.statistic.protocol.jmx.model;

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
import org.jrbsoft.statistic.model.IModelUpdater;
import org.jrbsoft.statistic.model.IProtocolModel;
import org.jrbsoft.statistic.model.IRootModel;

public class MBeanModelUpdater implements IModelUpdater {
    private static final Log _Logger = LogFactory.getLog(MBeanModelUpdater.class);
    private final MBeanServer _mbeanServer;

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

    private void updateStatistics(final ObjectName mbean, final String[] keys, final Map<String, Object> valuesMap) throws AttributeNotFoundException, InstanceNotFoundException, MBeanException, ReflectionException {
        if (keys != null) {
            for (final String key : keys) {
                valuesMap.put(key, _mbeanServer.getAttribute(mbean, key));
            }
        }
    }
}
