package org.statistic.test.incontainer;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.statistic.connectors.IMBeanControllerClient;
import org.statistic.csvloggers.CsvFormatter;
import org.statistic.models.IRootModel;
import org.statistic.models.MBeanElement;
import org.statistic.models.MBeanModel;
import org.statistic.models.MBeanModelUpdater;

@RunWith(Arquillian.class)
public final class MBeanStatisticsITCase {
	private static final Log _Logger = LogFactory.getLog(MBeanStatisticsITCase.class);
	
    @Deployment
    public static JavaArchive createDeployment() {
    	return TestUtil.getDeployment();
    }

    @EJB(mappedName = "java:app/resourceMonitor/TestBean!com.swx.ptp.kernel.statistic.test.incontainer.dmr.TestBean")
    private TestBean _testBean;
        
    @EJB(mappedName = "java:app/resourceMonitor/TestSingleton!com.swx.ptp.kernel.statistic.test.incontainer.dmr.TestSingleton")
    private TestSingleton _testSingleton;   
        
    @Test
    public void testMBeanServerAccess() throws Exception { 
    	final List<MBeanModel> mbeanModels = new ArrayList<MBeanModel>();
    	final MBeanModel mbeanModel = new MBeanModel();
    	final MBeanElement element = new MBeanElement();
    	final CsvFormatter formatter = new CsvFormatter();
    	
    	mbeanModel.setName("exampleDS MBean");
    	mbeanModel.setObjectName("jboss.as:subsystem=datasources,data-source=ExampleDS,statistics=pool");    	
    	
    	element.setAbbreviation("pool");
    	element.setKeys(new String[] {"ActiveCount", "AvailableCount", "InUseCount", "MaxUsedCount", "MaxWaitCount", "MaxWaitTime"});
    	
    	mbeanModel.getMBeanElements().add(element);
    	mbeanModels.add(mbeanModel);
    	
    	final MBeanModelUpdater updater = new MBeanModelUpdater(IMBeanControllerClient.Factory.connectEmbeddedMBeanServer()); 
    	updater.updateModel(mbeanModels);
    	
    	final IRootModel rootModel = IRootModel.Factory.create();
    	rootModel.setCsvSeparator(';');
    	rootModel.setMBeanModels(mbeanModels);
    	formatter.formatHeader(rootModel);
    	_Logger.info(formatter.formatHeader(rootModel));
		_Logger.info(formatter.formatLine(rootModel));	    	
    }
    
   
}
