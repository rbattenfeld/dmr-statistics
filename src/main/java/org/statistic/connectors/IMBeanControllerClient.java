package org.statistic.connectors;

import java.lang.management.ManagementFactory;

import javax.management.MBeanServer;

public interface IMBeanControllerClient {

	MBeanServer getEmbeddedMBeanServer();
	
	public class Factory {
		
		public static IMBeanControllerClient connectEmbeddedMBeanServer() {
			return new EmbeddedMBeanServerClientImpl(ManagementFactory.getPlatformMBeanServer());
		}
	}
	
}
