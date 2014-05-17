package org.statistic.test.incontainer;

import java.io.File;
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
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.statistic.connectors.IDmrControllerClient;
import org.statistic.logging.CsvFormatter;
import org.statistic.models.DmrChildElement;
import org.statistic.models.DmrModel;
import org.statistic.models.DmrModelUpdater;
import org.statistic.models.DmrPathElement;
import org.statistic.models.IRootModel;

@RunWith(Arquillian.class)
public final class DmrStatisticsITCase {
	private static final Log _Logger = LogFactory.getLog(DmrStatisticsITCase.class);
	
    @Deployment
    public static JavaArchive createDeployment() {
    	return TestUtil.getDeployment();
    }

    @EJB(mappedName = "java:app/resourceMonitor/TestBean!com.swx.ptp.kernel.statistic.test.incontainer.dmr.TestBean")
    private TestBean _testBean;
        
    @EJB(mappedName = "java:app/resourceMonitor/TestSingleton!com.swx.ptp.kernel.statistic.test.incontainer.dmr.TestSingleton")
    private TestSingleton _testSingleton;   
    
    @Test
    public void testEnableStatistics() throws Exception {
    	try (final IDmrControllerClient client = IDmrControllerClient.Factory.create()) {  
    		client.enableStatistics(true);
    	}
    }
    
    @Test
    public void testGenericFromFile() throws Exception { 
    	final IRootModel rootModel = IRootModel.Factory.createFromResource("META-INF/generic-stat.xml");    	
		try (final IDmrControllerClient client = IDmrControllerClient.Factory.create()) {  
			final DmrModelUpdater updater = new DmrModelUpdater(client.getModelController());    	
			final CsvFormatter formatter = new CsvFormatter();
    		client.enableEjb3Statistics(true);
    		_testBean.testMe();
        	_testSingleton.callMe();
        	_testSingleton.callMe();
        	_testSingleton.callMeAgain();   	
    		updater.updateModel(rootModel);
    		_Logger.info(formatter.formatHeader(rootModel));
    		_Logger.info(formatter.formatLine(rootModel));	  
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
    	
    	final List<DmrModel> models = new ArrayList<DmrModel>();  	
//    	models.add(adapterModel);
    	models.add(dataSourceModel);
    	
    	try (final IDmrControllerClient client = IDmrControllerClient.Factory.create()) {  
    		final DmrModelUpdater updater = new DmrModelUpdater(client.getModelController());
    		updater.updateModel(models);
//    		_Logger.info(adapterModel);
    		_Logger.info(dataSourceModel);
    	}
    	
    	final IRootModel configurer = IRootModel.Factory.create();
    	configurer.setDeploymentName("test.war");
    	configurer.setCsvSeparator(';');
    	configurer.setIntervall(5000);
    	configurer.setLogCategory("ejb3stat");
    	configurer.setDmrModels(models);
    	
    	final Serializer serializer = new Persister();
    	final File file = new File("stat-datasource.xml");
        serializer.write(configurer, file);
    }
    
    @Test
    public void testGeneric() throws Exception { 
    	final IRootModel rootModel = IRootModel.Factory.createFromResource("META-INF/generic-stat.xml");
    	try (final IDmrControllerClient client = IDmrControllerClient.Factory.create()) {  
    		final DmrModelUpdater updater = new DmrModelUpdater(client.getModelController());    	
    		final CsvFormatter formatter = new CsvFormatter();
    		updater.updateModel(rootModel);
    		_Logger.info(formatter.formatHeader(rootModel));
    		_Logger.info(formatter.formatLine(rootModel));	  
    	}
    }
    
//    @Test
//    public void testRarManual() throws Exception {
//    	final ResourceAdapterStatisticModel model = new ResourceAdapterStatisticModel();
//    	final List<ResourceAdapterStatisticModel> models = new ArrayList<ResourceAdapterStatisticModel>();
//    	model.setDeployment("seni-req-portal-gs-adapter-9.0.0.4.rar");
//    	model.setConnectionDefinition("java:global/DefaultGigaSpaceConnectionFactory");
//    	model.setConnectionDefinitionKeys(new String[] {"ActiveCount", "AvailableCount", "InUseCount", "MaxUsedCount", "MaxWaitCount",  "MaxWaitTime"});
//    	model.setWorkManager("default");
//    	model.setWorkmanagerKeys(new String[] {"work-active", "work-failed", "work-successful"});
//    	models.add(model);
//    	final ResourceAdapterStatisticUpdater updater = new ResourceAdapterStatisticUpdater();
//    	try (final ModelControllerConnector client = new ModelControllerConnector()) {
//    		updater.updateModel(client.getModelController(), models);
//    		_Logger.info(model);
//    	}
//    }
    
//    @Test
//    public void testDataSource() throws Exception {
//    	final DmrStatisticConfiguration rootModel = DmrStatisticConfiguration.loadFromResource("META-INF/stat.xml");
//    	try (final ModelControllerConnector client = new ModelControllerConnector()) {  
//	    	final DatasourceStatisticUpdater updater = new DatasourceStatisticUpdater();	    	
//	    		final DataSourcesStatisticCSVFormatter formatter = new DataSourcesStatisticCSVFormatter();
//	    		updater.updateModel(client.getModelController(), rootModel);
//	    		_Logger.info(formatter.formatHeader(rootModel));
//	    		_Logger.info(formatter.formatLine(rootModel));	  
//    	}
//    }
    
//    @Test
//    public void testDataSourceManual() throws Exception {
//    	final DatasourceStatisticModel model = new DatasourceStatisticModel();
//    	final List<DatasourceStatisticModel> models = new ArrayList<DatasourceStatisticModel>();
//    	model.setDatasource("ExampleDS");
//    	model.setJdbcKeys(new String[] {"PreparedStatementCacheAccessCount", "PreparedStatementCacheHitCount", "PreparedStatementCacheMissCount"});
//    	model.setPoolKeys(new String[] {"ActiveCount", "AvailableCount", "InUseCount", "MaxUsedCount", "MaxWaitCount",  "MaxWaitTime"});
//    	models.add(model);
//    	final DatasourceStatisticUpdater updater = new DatasourceStatisticUpdater();
//    	try (final ModelControllerConnector client = new DmrClient()) {
//    		updater.updateModel(client.getModelController(), models);
//    		_Logger.info(model);
//    	}
//    }
    
//    @Test
//    public void testPlatform() throws Exception {
//    	final DmrStatisticConfiguration rootModel = DmrStatisticConfiguration.loadFromResource("META-INF/stat.xml");
//    	try (final DmrClient client = new DmrClient()) {  
//    		final PlatformStatisticUpdater platformStatisticExtractor = new PlatformStatisticUpdater();
//    		final PlatformStatisticCSVFormatter formatter = new PlatformStatisticCSVFormatter();
//    		platformStatisticExtractor.updateModel(client.getModelController(), rootModel);
//    		_Logger.info(formatter.formatHeader(rootModel));
//    		_Logger.info(formatter.formatLine(rootModel));
//    		assertEquals(
//    				formatter.formatHeader(rootModel).split(", ", -1).length, 
//    				formatter.formatLine(rootModel).split(", ", -1).length);
//    	}
//    }
    
//    @Test
//    public void testConfigFromResourceWithNoStatAvailable() throws Exception {
//    	final DmrStatisticConfiguration rootModel = DmrStatisticConfiguration.loadFromResource("META-INF/stat.xml");
//    	try (final DmrClient client = new DmrClient()) {    
//    		final Ejb3StatisticCSVFormatter formatter = new Ejb3StatisticCSVFormatter();
//    		final Ejb3StatisticUpdater ejb3StatExtractor = new Ejb3StatisticUpdater(rootModel.getDeploymentName());
//    		client.enableEjb3Statistics(true);
//    		ejb3StatExtractor.updateModel(client.getModelController(),  rootModel);   
//    		_Logger.info(formatter.formatHeader(rootModel));
//    		_Logger.info(formatter.formatLine(rootModel));
//    		assertEquals(
//    				formatter.formatHeader(rootModel).split(", ", -1).length, 
//    				formatter.formatLine(rootModel).split(", ", -1).length);
//    	}
//    }
    
//    @Test
//    public void testConfigFromResource() throws Exception {
//    	final DmrStatisticConfiguration rootModel = DmrStatisticConfiguration.loadFromResource("META-INF/stat.xml");
//    	try (final DmrClient client = new DmrClient()) {  
//    		client.enableEjb3Statistics(true);
//    		_testBean.testMe();
//        	_testSingleton.callMe();
//        	_testSingleton.callMe();
//        	_testSingleton.callMeAgain();        	
//    		final Ejb3StatisticCSVFormatter formatter = new Ejb3StatisticCSVFormatter();
//    		final Ejb3StatisticUpdater ejb3StatExtractor = new Ejb3StatisticUpdater(rootModel.getDeploymentName());
//    		ejb3StatExtractor.updateModel(client.getModelController(),  rootModel);     		
//    		_Logger.info(formatter.formatHeader(rootModel));
//    		_Logger.info(formatter.formatLine(rootModel));
//    		assertEquals(
//    				formatter.formatHeader(rootModel).split(", ", -1).length, 
//    				formatter.formatLine(rootModel).split(", ", -1).length);
//    	}
//    }
    
//    @Test
//    public void testNullDeployment() throws IOException {
//    	final DmrStatisticConfiguration rootModel = DmrStatisticConfiguration.loadFromResource("META-INF/stat.xml");
//    	rootModel.setDeploymentName("testXXXX.war");
//    	try (final DmrClient client = new DmrClient()) { 
//    		client.enableEjb3Statistics(true);
//    		_testBean.testMe();
//    		final Ejb3StatisticCSVFormatter formatter = new Ejb3StatisticCSVFormatter();
//    		final Ejb3StatisticUpdater ejb3StatExtractor = new Ejb3StatisticUpdater(rootModel.getDeploymentName());
//    		ejb3StatExtractor.updateModel(client.getModelController(),  rootModel);      
//    		assertEquals(
//    				formatter.formatHeader(rootModel).split(", ", -1).length, 
//    				formatter.formatLine(rootModel).split(", ", -1).length);
//    	}
//    }
        
//    @Test
//    public void testDefaultEJB3Statistics() throws IOException {
//    	try (final DmrClient client = new DmrClient()) {
//    		client.enableEjb3Statistics(true);
//    		_testBean.testMe();
//        	_testSingleton.callMe();
//        	_testSingleton.callMe();
//        	_testSingleton.callMeAgain();    	
//	    	final List<Ejb3StatisticModel> ejbStatModel = new ArrayList<Ejb3StatisticModel>();
//	    	final Ejb3StatisticModel testBeanDetails = new Ejb3StatisticModel("TestBean", "TestBean", EjbType.StatelessBean, null, null);    	
//	    	final Ejb3StatisticModel testSingletonDetails = new Ejb3StatisticModel("TestSingleton", "TestSingleton", EjbType.SingletonBean, null, null);
//	    	final Ejb3StatisticCSVFormatter formatter = new Ejb3StatisticCSVFormatter();
//	    	final DmrStatisticConfiguration rootModel = new DmrStatisticConfiguration();
//	    	ejbStatModel.add(testBeanDetails);  
//	    	ejbStatModel.add(testSingletonDetails);
//	    	rootModel.setEjbStatisticModels(ejbStatModel);
//	    	final Ejb3StatisticUpdater ejb3StatExtractor = new Ejb3StatisticUpdater("test.war");
//    		ejb3StatExtractor.updateModel(client.getModelController(), rootModel); 
//    		
//    		_Logger.info("\n" + testBeanDetails.toString());
//    		_Logger.info("\n" + testSingletonDetails.toString());
//    		
//    		assertEquals(
//    				formatter.formatHeader(rootModel).split(", ", -1).length, 
//    				formatter.formatLine(rootModel).split(", ", -1).length);
//    	}
//    }
    
//    @Test
//    public void testEjb3List() throws IOException {    	   	
//    	try (final DmrClient client = new DmrClient()) {
//    		client.enableEjb3Statistics(true);
//    		_testBean.testMe();
//        	_testSingleton.callMe();
//        	_testSingleton.callMe();
//        	_testSingleton.callMeAgain(); 
//	    	final List<Ejb3StatisticModel> ejbStatModel = new ArrayList<Ejb3StatisticModel>();
//	    	final Ejb3StatisticModel testBeanDetails = new Ejb3StatisticModel("TestBean", "TestBean", EjbType.StatelessBean, new String[] {"execution-time"}, new String[] {"testMe"});    	
//	    	final Ejb3StatisticModel testSingletonDetails = new Ejb3StatisticModel("TestSingleton", "TestSingleton", EjbType.SingletonBean, new String[] {"execution-time"}, new String[] {"callMe", "callMeAgain"});
//	    	final Ejb3StatisticCSVFormatter formatter = new Ejb3StatisticCSVFormatter();
//	    	final DmrStatisticConfiguration rootModel = new DmrStatisticConfiguration();
//	    	ejbStatModel.add(testBeanDetails);  
//	    	ejbStatModel.add(testSingletonDetails);
//	    	rootModel.setEjbStatisticModels(ejbStatModel);
//	    	final Ejb3StatisticUpdater ejb3StatExtractor = new Ejb3StatisticUpdater("test.war");
//    		ejb3StatExtractor.updateModel(client.getModelController(),  rootModel);   
//    		assertEquals(
//    				formatter.formatHeader(rootModel).split(", ", -1).length, 
//    				formatter.formatLine(rootModel).split(", ", -1).length);
//    	}
//    }
    
}
