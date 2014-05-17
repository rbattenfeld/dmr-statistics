package org.statistic.connectors;

import java.io.IOException;

import javax.management.MBeanServer;

class EmbeddedMBeanServerClientImpl implements IMBeanControllerClient {

	private MBeanServer _mbeanServer; 
	
	EmbeddedMBeanServerClientImpl(final MBeanServer server) {
		_mbeanServer = server;
	}
	
	@Override
	public MBeanServer getEmbeddedMBeanServer() {
		return _mbeanServer;
	}


}
