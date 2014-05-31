package org.jrbsoft.statistic.protocol.jmx.connecting;

import javax.management.MBeanServer;

class EmbeddedMBeanServerClientImpl implements IMBeanControllerClient {

	private MBeanServer _mbeanServer; 
	
	EmbeddedMBeanServerClientImpl(final MBeanServer server) {
		_mbeanServer = server;
	}

	@Override
	public MBeanServer getMBeanServer() {
		return _mbeanServer;
	}

}
