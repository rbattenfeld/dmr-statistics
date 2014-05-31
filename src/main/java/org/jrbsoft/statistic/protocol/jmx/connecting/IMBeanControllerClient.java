package org.jrbsoft.statistic.protocol.jmx.connecting;

import java.lang.management.ManagementFactory;

import javax.management.MBeanServer;

public interface IMBeanControllerClient {

    MBeanServer getMBeanServer();

    public class Factory {
        public static IMBeanControllerClient connectEmbeddedMBeanServer() {
            return new EmbeddedMBeanServerClientImpl(ManagementFactory.getPlatformMBeanServer());
        }
    }

}
