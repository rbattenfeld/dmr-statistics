package org.statistic.dmr.client;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.as.controller.client.ModelControllerClient;
import org.jboss.as.controller.client.helpers.ClientConstants;
import org.jboss.dmr.ModelNode;

public class DmrClient implements Closeable {
	private static final Log _Logger = LogFactory.getLog(DmrClient.class);
	private static final String _JBOSS_NODE_HOST = System.getProperty("jboss.node.host", "localhost");
	private static final String _JBOSS_NODE_PORT = System.getProperty("jboss.node.port", "9990");
	private final ModelControllerClient _client;
	
	public DmrClient() throws UnknownHostException {
		_client = connect(_JBOSS_NODE_HOST, Integer.valueOf(_JBOSS_NODE_PORT));
	}
	
	public DmrClient(final boolean enableEjb3Statistics) throws UnknownHostException {
		_client = connect(_JBOSS_NODE_HOST, Integer.valueOf(_JBOSS_NODE_PORT));
		if (enableEjb3Statistics) {
			enableEjb3Statistics(_client);
		}
	}
	
	public DmrClient(final String host, final int port) throws UnknownHostException {
		_client = connect(host, port);
	}
	
	public ModelControllerClient getModelController() {
		return _client;
	}
	
	public void enableEjb3Statistics() {
		enableEjb3Statistics(_client);
	}
		
	public void enableEjb3Statistics(final ModelControllerClient client) {
		try {
			final ModelNode request = new ModelNode();			
			request.get(ClientConstants.OP_ADDR).add(ClientConstants.SUBSYSTEM, "ejb3"); 
		    request.get(ClientConstants.OP).set(ClientConstants.WRITE_ATTRIBUTE_OPERATION);
		    request.get("name").set("enable-statistics");
			request.get("value").set("true");
			final ModelNode response = client.execute(request);
			reportFailure(response);
		} catch (IOException ex) {
			throw new RuntimeException(ex.getMessage(), ex);
		}
	}
	

//	/subsystem=logging/file-handler=fh:add(level=INFO, file={"relative-to"=>"jboss.server.log.dir", "path"=>"fh.log"}, append=false, autoflush=true)
//	/subsystem=logging/logger=org.your.company:add(use-parent-handlers=false,handlers=\["fh"\])
	
	public void enableStatisticLogFile(final ModelControllerClient client, final String logFileName) {
//		try {
//			
//			final ModelNode request = new ModelNode();
//			request.get(ClientConstants.OP_ADDR).add(ClientConstants.SUBSYSTEM, "logging");
//			request.get(ClientConstants.OP).set(ClientConstants.WRITE_ATTRIBUTE_OPERATION);
//			request.get(ClientConstants.ADD).add("file-handler", "fhxx");
//			request.get(ClientConstants.OP_ADDR).add("file-handler", "fhxx");
//			request.get("level").set("INFO");
//			request.get("file").add("relative-to", "jboss.server.log.dir");
//			request.get("file").add("path", logFileName);
//			request.get("append").set("false");
//			request.get("autoflush").set("true");
			
//			_Logger.info(op.toJSONString(false));
//			
//			final ModelNode response = client.execute(op);
//			reportFailure(response);
			
//			final ModelNode request2 = new ModelNode();
//			request2.get(ClientConstants.OP_ADDR).add(ClientConstants.SUBSYSTEM, "logging");
//			request2.get("logger", "org.statistic.dmr");
//			request2.get("use-parent-handlers").add("false");
//			request2.get("handlers").add("STATFILE");
//			final ModelNode response2 = client.execute(request2);
////			reportFailure(response2);
//		} catch (IOException ex) {
//			throw new RuntimeException(ex.getMessage(), ex);
//		}				
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

	private ModelControllerClient connect(final String host, final int port) throws UnknownHostException {
		return ModelControllerClient.Factory.create(InetAddress.getByName(host), port);
	}
	
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
