package org.statistic.connectors;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.jboss.as.controller.client.ModelControllerClient;

public interface IDmrControllerClient extends Closeable {
	
	ModelControllerClient getModelController();
	void enableStatistics(final boolean enable);
	void enableEjb3Statistics(final boolean enable);
	void enableUndertow(final boolean enable);
	void close() throws IOException;
	
	class Factory {
		
		public static IDmrControllerClient create() throws UnknownHostException {
			final String host = System.getProperty("jboss.node.host", "localhost");
			final String port = System.getProperty("jboss.node.port", "9990");
			final ModelControllerClient client = ModelControllerClient.Factory.create(InetAddress.getByName(host), Integer.valueOf(port));
			return new RemoteClientImpl(client);
		}
		
		public static IDmrControllerClient create(final String host, final int port) throws UnknownHostException {
			final ModelControllerClient client = ModelControllerClient.Factory.create(InetAddress.getByName(host), Integer.valueOf(port));
			return new RemoteClientImpl(client);
		}
		
	}
}
