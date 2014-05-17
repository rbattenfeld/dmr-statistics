package org.statistic.connectors;

import java.io.Closeable;
import java.io.IOException;
import java.net.UnknownHostException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.as.controller.client.ModelControllerClient;
import org.jboss.as.controller.client.helpers.ClientConstants;
import org.jboss.dmr.ModelNode;

class RemoteClientImpl implements IDmrControllerClient, Closeable {
	private static final Log _Logger = LogFactory.getLog(RemoteClientImpl.class);
	private final ModelControllerClient _client;
	
	/**
	 * Creates a new instance by connecting to the default host and port configured through the system properties
	 * <code>jboss.node.host</code> and <code>jboss.node.port</code>.
	 * 
	 * @throws UnknownHostException if a connection error occurs.
	 */
	public RemoteClientImpl(ModelControllerClient client) {
		_client = client;
	}
		
	/**
	 * Returns the controller.
	 * @return
	 */
	@Override
	public ModelControllerClient getModelController() {
		return _client;
	}
			
	/**
	 * Enables statistics on or off for those ones which are not active by default.
	 * @param client
	 * @param enable turns on or off statistics for currently EJB3 and Undertow.
	 */
	public void enableStatistics(final boolean enable) {
		enableEjb3Statistics(enable);
		enableUndertow(enable);
	}
	
	/**
	 * Activate or deactivates EJB3 statistics.
	 * @param client
	 * @param enable
	 */
	@Override
	public void enableEjb3Statistics(final boolean enable) {
		try {
			final ModelNode request = new ModelNode();			
			request.get(ClientConstants.OP_ADDR).add(ClientConstants.SUBSYSTEM, "ejb3"); 
		    request.get(ClientConstants.OP).set(ClientConstants.WRITE_ATTRIBUTE_OPERATION);
		    request.get("name").set("enable-statistics");
			request.get("value").set(Boolean.toString(enable));
			final ModelNode response = _client.execute(request);
			reportFailure(response);
		} catch (IOException ex) {
			throw new RuntimeException(ex.getMessage(), ex);
		}
	}
	
	/**
	 * Activate or deactivates Undertow statistics.
	 * @param client
	 * @param enable
	 */
	@Override
	public void enableUndertow(final boolean enable) {
		try {
			final ModelNode request = new ModelNode();			
			request.get(ClientConstants.OP_ADDR).add(ClientConstants.SUBSYSTEM, "undertow"); 
		    request.get(ClientConstants.OP).set(ClientConstants.WRITE_ATTRIBUTE_OPERATION);
		    request.get("name").set("statistics-enabled");
		    request.get("value").set(Boolean.toString(enable));
			final ModelNode response = _client.execute(request);
			reportFailure(response);
		} catch (IOException ex) {
			throw new RuntimeException(ex.getMessage(), ex);
		}
	}
	
	@Override
	public void close() throws IOException {
		if (_client != null) {
			_client.close();
		}		
	}
	
	//-----------------------------------------------------------------------||
	//-- Private Methods ----------------------------------------------------||
	//-----------------------------------------------------------------------||
	
	private void reportFailure(final ModelNode node) {
        if (!node.get(ClientConstants.OUTCOME).asString().equals(ClientConstants.SUCCESS)) {
            final String msg;
            if (node.hasDefined(ClientConstants.FAILURE_DESCRIPTION)) {
                if (node.hasDefined(ClientConstants.OP)) {
                    msg = String.format("Operation '%s' at address '%s' failed: %s", 
                    		node.get(ClientConstants.OP), 
                    		node.get(ClientConstants.OP_ADDR), 
                    		node.get(ClientConstants.FAILURE_DESCRIPTION));
                } else {
                    msg = String.format("Operation failed: %s", 
                    		node.get(ClientConstants.FAILURE_DESCRIPTION));
                }
            } else {
                msg = String.format("Operation failed: %s", node);
            }
            _Logger.info(msg);
        }
    }
}
