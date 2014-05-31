package org.jrbsoft.statistic.test.incontainer;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jrbsoft.statistic.model.IProtocolModel;
import org.jrbsoft.statistic.model.IRootModel;
import org.jrbsoft.statistic.protocol.jmx.connecting.IMBeanControllerClient;
import org.jrbsoft.statistic.protocol.jmx.model.MBeanElement;
import org.jrbsoft.statistic.protocol.jmx.model.MBeanModel;
import org.jrbsoft.statistic.protocol.jmx.model.MBeanModelUpdater;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public final class MBeanStatisticsITCase {
    private static final Log _Logger = LogFactory.getLog(MBeanStatisticsITCase.class);
	
    @Deployment
    public static JavaArchive createDeployment() {
    	return TestUtil.getDeployment();
    }

    @Test
    public void testFromFile() throws Exception { 
    	final IRootModel rootModel = IRootModel.Factory.createFromResource("META-INF/mbean-stat.xml");    	
    	final MBeanModelUpdater updater = new MBeanModelUpdater(IMBeanControllerClient.Factory.connectEmbeddedMBeanServer().getMBeanServer()); 
		updater.updateModel(rootModel);
		_Logger.info(rootModel.getModels().get(0));
    }
    
    @Test
    public void testNotification() throws Exception { 
    	final IRootModel rootModel = IRootModel.Factory.createFromResource("META-INF/mbean-notification-stat.xml");    	
    	final MBeanModelUpdater updater = new MBeanModelUpdater(IMBeanControllerClient.Factory.connectEmbeddedMBeanServer().getMBeanServer()); 
    	for (int i = 0; i < 10; i++) {
			updater.updateModel(rootModel);
			_Logger.info(rootModel.getModels().get(0));
			sleep(1000);
    	}
    }
    
    @Test
    public void testMBeanServerAccess() throws Exception { 
    	final List<IProtocolModel> mbeanModels = new ArrayList<IProtocolModel>();
    	final MBeanModel mbeanModel = new MBeanModel();
    	final MBeanElement element = new MBeanElement();
    	element.setAbbreviation("pool");
    	element.setKeys(new String[] {"ActiveCount", "AvailableCount", "InUseCount", "MaxUsedCount", "MaxWaitCount", "MaxWaitTime"});
    	
    	mbeanModel.setName("exampleDS MBean");
    	mbeanModel.setObjectName("jboss.as:subsystem=datasources,data-source=ExampleDS,statistics=pool");	
    	mbeanModel.getMBeanElements().add(element);
    	mbeanModels.add(mbeanModel);
    	
    	final MBeanModelUpdater updater = new MBeanModelUpdater(IMBeanControllerClient.Factory.connectEmbeddedMBeanServer().getMBeanServer());
    	
    	final long startTime = System.currentTimeMillis();
    	for (int i = 0 ; i < 10; i++) {
    		updater.updateModels(mbeanModels);
    	}
    	final long totalTime = System.currentTimeMillis() - startTime;
    	_Logger.info("Total time: " + totalTime);
    	
    	final IRootModel rootModel = IRootModel.Factory.create();
    	rootModel.setCsvSeparator(';');
    	rootModel.setModels(mbeanModels);
    }
    
    private void sleep(final int timeout) throws InterruptedException {
    	Thread.currentThread();
    	Thread.sleep(timeout);
    }
}
