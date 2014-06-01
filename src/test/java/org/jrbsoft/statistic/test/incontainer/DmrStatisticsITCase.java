package org.jrbsoft.statistic.test.incontainer;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jrbsoft.statistic.model.IProtocolModel;
import org.jrbsoft.statistic.model.IRootModel;
import org.jrbsoft.statistic.protocol.dmr.connecting.IDmrControllerClient;
import org.jrbsoft.statistic.protocol.dmr.model.DmrChildElement;
import org.jrbsoft.statistic.protocol.dmr.model.DmrModel;
import org.jrbsoft.statistic.protocol.dmr.model.DmrModelUpdater;
import org.jrbsoft.statistic.protocol.dmr.model.DmrPathElement;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public final class DmrStatisticsITCase {
	private static final Log _Logger = LogFactory.getLog(DmrStatisticsITCase.class);
	
    @Deployment
    public static JavaArchive createDeployment() {
    	return TestUtil.getDeployment();
    }

    @EJB(mappedName = "java:app/resourceMonitor/TestBean!org.jrbsoft.statistic.test.incontainer.TestBean")
    private TestBean _testBean;
        
    @EJB(mappedName = "java:app/resourceMonitor/TestSingleton!org.jrbsoft.statistic.test.incontainer.TestSingleton")
    private TestSingleton _testSingleton;   
    
    @Test
    public void testEnableStatistics() throws Exception {
    	try (final IDmrControllerClient client = IDmrControllerClient.Factory.create()) {  
    		client.enableStatistics(true);
    	}
    }

    @Test
    public void testEnableStatisticImpact() throws Exception {
    	try (final IDmrControllerClient client = IDmrControllerClient.Factory.create()) {
//    		final long totalTimeEnableFalse = executePerformanceTest(client, false);
    		final long totalTime  = executePerformanceTest(client, true, 500000);
//    		final long totalTimeEnableFalse = executePerformanceTest(client, false, 100000);
//    		_Logger.info("Total statistics enabled = false : " + totalTimeEnableFalse);
    		_Logger.info("Total3: " + totalTime);
    	}
    }
    
    @Test
    public void testGenericFromFile() throws Exception { 
    	final IRootModel rootModel = IRootModel.Factory.createFromResource("META-INF/generic-stat.xml");    	
		try (final IDmrControllerClient client = IDmrControllerClient.Factory.create("localhost", 9999)) {  
			final DmrModelUpdater updater = new DmrModelUpdater(client.getModelController()); 
    		client.enableEjb3Statistics(true);
    		_testBean.testMe();
        	_testSingleton.callMe();
        	_testSingleton.callMe();
        	_testSingleton.callMeAgain();   	
    		updater.updateModel(rootModel);
    	}
    }
    
    @Test
    public void testGenericManual() throws Exception { 
    	final List<DmrChildElement> childElements = new ArrayList<DmrChildElement>();
    	final DmrChildElement childModel = new DmrChildElement();
    	childModel.setName("jdbc statistics");
    	childModel.setAbbreviation("jdbc");
    	childModel.setChilds(new String[] {"statistics", "jdbc"});
    	childModel.setKeys(new String[] {"PreparedStatementCacheAccessCount", "PreparedStatementCacheHitCount", "PreparedStatementCacheMissCount"});
    	childElements.add(childModel);
    	
    	final DmrModel dataSourceModel = new DmrModel();
    	dataSourceModel.setPathAddress("/subsystem=datasources/data-source=ExampleDS");
    	dataSourceModel.setName("exampleDS");
    	dataSourceModel.setChildElements(childElements);
    	
    	final DmrPathElement pathElement = new DmrPathElement();
    	pathElement.setAbbreviation("gs-adapter");
    	pathElement.setPathElement("connection-definitions=java:global/DefaultGigaSpaceConnectionFactory");
    	pathElement.setKeys(new String[] {"ActiveCount", "AvailableCount", "InUseCount", "MaxUsedCount", "MaxWaitCount",  "MaxWaitTime"});
    	
    	final List<DmrPathElement> pathElements = new ArrayList<DmrPathElement>();
    	pathElements.add(pathElement);
    	    	
    	final DmrModel adapterModel = new DmrModel();
    	adapterModel.setName("seni-req-portal-gs-adapter");
    	adapterModel.setPathAddress("/deployment=seni-req-portal-gs-adapter-9.0.0.4.rar/subsystem=resource-adapters/statistics=statistics/resource-adapter=seni-req-portal-gs-adapter-9.0.0.4.rar");
    	adapterModel.setPathElements(pathElements); 
    	
    	final List<IProtocolModel> models = new ArrayList<IProtocolModel>();  	
//    	models.add(adapterModel);
    	models.add(dataSourceModel);
    	
    	try (final IDmrControllerClient client = IDmrControllerClient.Factory.create()) {  
    		final DmrModelUpdater updater = new DmrModelUpdater(client.getModelController());
    		updater.updateModels(models);
//    		_Logger.info(adapterModel);
    		_Logger.info(dataSourceModel);
    	}
    	
    	final IRootModel configurer = IRootModel.Factory.create();
    	configurer.setDeploymentName("test.war");
    	configurer.setCsvSeparator(';');
    	configurer.setInterval(5000);
    	configurer.setLogCategory("ejb3stat");
    	configurer.setModels(models);
//    	
//    	final Serializer serializer = new Persister();
//    	final File file = new File("stat-datasource.xml");
//        serializer.write(configurer, file);
    }
    
    @Test
    public void testGeneric() throws Exception { 
    	final IRootModel rootModel = IRootModel.Factory.createFromResource("META-INF/generic-stat.xml");
    	try (final IDmrControllerClient client = IDmrControllerClient.Factory.create()) {  
    		final DmrModelUpdater updater = new DmrModelUpdater(client.getModelController());   
    		updater.updateModel(rootModel);
    	}
    }
    
    //-----------------------------------------------------------------------||
    
    private long executePerformanceTest(final IDmrControllerClient client, final boolean isEnabled, final int count) {
//    	client.enableStatistics(isEnabled);		
		long startTime = System.currentTimeMillis();
		_testSingleton.callTestBean(count);
		return System.currentTimeMillis() - startTime;
    }
}
