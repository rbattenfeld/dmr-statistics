package org.statistic.dmr.test.incontainer;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
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
import org.statistic.dmr.client.DmrClient;
import org.statistic.dmr.conf.DmrStatisticConfiguration;
import org.statistic.dmr.stat.datasource.DataSourcesStatisticCSVFormatter;
import org.statistic.dmr.stat.datasource.DatasourceStatisticModel;
import org.statistic.dmr.stat.datasource.DatasourceStatisticUpdater;
import org.statistic.dmr.stat.ejb3.Ejb3StatisticCSVFormatter;
import org.statistic.dmr.stat.ejb3.Ejb3StatisticModel;
import org.statistic.dmr.stat.ejb3.Ejb3StatisticUpdater;
import org.statistic.dmr.stat.ejb3.EjbType;
import org.statistic.dmr.stat.platform.PlatformStatisticCSVFormatter;
import org.statistic.dmr.stat.platform.PlatformStatisticUpdater;

@RunWith(Arquillian.class)
public final class DmrStatisticsTestCase {
	private static final Log _Logger = LogFactory.getLog(DmrStatisticsTestCase.class);
	
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
    	try (final DmrClient client = new DmrClient()) {  
    		client.enableStatistics(true);
    	}
    }
    
    @Test
    public void testDataSource() throws Exception {
    	final DmrStatisticConfiguration rootModel = DmrStatisticConfiguration.loadFromResource("META-INF/stat.xml");
    	try (final DmrClient client = new DmrClient()) {  
	    	final DatasourceStatisticUpdater updater = new DatasourceStatisticUpdater();	    	
	    		final DataSourcesStatisticCSVFormatter formatter = new DataSourcesStatisticCSVFormatter();
	    		updater.updateModel(client.getModelController(), rootModel);
	    		_Logger.info(formatter.formatHeader(rootModel));
	    		_Logger.info(formatter.formatLine(rootModel));	  
    	}
    }
    
    @Test
    public void testDataSourceManual() throws Exception {
    	final DatasourceStatisticModel model = new DatasourceStatisticModel();
    	final List<DatasourceStatisticModel> models = new ArrayList<DatasourceStatisticModel>();
    	model.setDatasource("ExampleDS");
    	model.setJdbcKeys(new String[] {"PreparedStatementCacheAccessCount", "PreparedStatementCacheHitCount", "PreparedStatementCacheMissCount"});
    	model.setPoolKeys(new String[] {"ActiveCount", "AvailableCount", "InUseCount", "MaxUsedCount", "MaxWaitCount",  "MaxWaitTime"});
    	models.add(model);
    	final DatasourceStatisticUpdater updater = new DatasourceStatisticUpdater();
    	try (final DmrClient client = new DmrClient()) {
    		updater.updateModel(client.getModelController(), models);
    		_Logger.info(model);
    	}
    }
    
    @Test
    public void testPlatform() throws Exception {
    	final DmrStatisticConfiguration rootModel = DmrStatisticConfiguration.loadFromResource("META-INF/stat.xml");
    	try (final DmrClient client = new DmrClient()) {  
    		final PlatformStatisticUpdater platformStatisticExtractor = new PlatformStatisticUpdater();
    		final PlatformStatisticCSVFormatter formatter = new PlatformStatisticCSVFormatter();
    		platformStatisticExtractor.updateModel(client.getModelController(), rootModel);
    		_Logger.info(formatter.formatHeader(rootModel));
    		_Logger.info(formatter.formatLine(rootModel));
    		assertEquals(
    				formatter.formatHeader(rootModel).split(", ", -1).length, 
    				formatter.formatLine(rootModel).split(", ", -1).length);
    	}
    }
    
    @Test
    public void testConfigFromResourceWithNoStatAvailable() throws Exception {
    	final DmrStatisticConfiguration rootModel = DmrStatisticConfiguration.loadFromResource("META-INF/stat.xml");
    	try (final DmrClient client = new DmrClient()) {    
    		final Ejb3StatisticCSVFormatter formatter = new Ejb3StatisticCSVFormatter();
    		final Ejb3StatisticUpdater ejb3StatExtractor = new Ejb3StatisticUpdater(rootModel.getDeploymentName());
    		client.enableEjb3Statistics(true);
    		ejb3StatExtractor.updateModel(client.getModelController(),  rootModel);   
    		_Logger.info(formatter.formatHeader(rootModel));
    		_Logger.info(formatter.formatLine(rootModel));
    		assertEquals(
    				formatter.formatHeader(rootModel).split(", ", -1).length, 
    				formatter.formatLine(rootModel).split(", ", -1).length);
    	}
    }
    
    @Test
    public void testConfigFromResource() throws Exception {
    	final DmrStatisticConfiguration rootModel = DmrStatisticConfiguration.loadFromResource("META-INF/stat.xml");
    	try (final DmrClient client = new DmrClient()) {  
    		client.enableEjb3Statistics(true);
    		_testBean.testMe();
        	_testSingleton.callMe();
        	_testSingleton.callMe();
        	_testSingleton.callMeAgain();        	
    		final Ejb3StatisticCSVFormatter formatter = new Ejb3StatisticCSVFormatter();
    		final Ejb3StatisticUpdater ejb3StatExtractor = new Ejb3StatisticUpdater(rootModel.getDeploymentName());
    		ejb3StatExtractor.updateModel(client.getModelController(),  rootModel);     		
    		_Logger.info(formatter.formatHeader(rootModel));
    		_Logger.info(formatter.formatLine(rootModel));
    		assertEquals(
    				formatter.formatHeader(rootModel).split(", ", -1).length, 
    				formatter.formatLine(rootModel).split(", ", -1).length);
    	}
    }
    
    @Test
    public void testNullDeployment() throws IOException {
    	final DmrStatisticConfiguration rootModel = DmrStatisticConfiguration.loadFromResource("META-INF/stat.xml");
    	rootModel.setDeploymentName("testXXXX.war");
    	try (final DmrClient client = new DmrClient()) { 
    		client.enableEjb3Statistics(true);
    		_testBean.testMe();
    		final Ejb3StatisticCSVFormatter formatter = new Ejb3StatisticCSVFormatter();
    		final Ejb3StatisticUpdater ejb3StatExtractor = new Ejb3StatisticUpdater(rootModel.getDeploymentName());
    		ejb3StatExtractor.updateModel(client.getModelController(),  rootModel);      
    		assertEquals(
    				formatter.formatHeader(rootModel).split(", ", -1).length, 
    				formatter.formatLine(rootModel).split(", ", -1).length);
    	}
    }
        
    @Test
    public void testDefaultEJB3Statistics() throws IOException {
    	try (final DmrClient client = new DmrClient()) {
    		client.enableEjb3Statistics(true);
    		_testBean.testMe();
        	_testSingleton.callMe();
        	_testSingleton.callMe();
        	_testSingleton.callMeAgain();    	
	    	final List<Ejb3StatisticModel> ejbStatModel = new ArrayList<Ejb3StatisticModel>();
	    	final Ejb3StatisticModel testBeanDetails = new Ejb3StatisticModel("TestBean", "TestBean", EjbType.StatelessBean, null, null);    	
	    	final Ejb3StatisticModel testSingletonDetails = new Ejb3StatisticModel("TestSingleton", "TestSingleton", EjbType.SingletonBean, null, null);
	    	final Ejb3StatisticCSVFormatter formatter = new Ejb3StatisticCSVFormatter();
	    	final DmrStatisticConfiguration rootModel = new DmrStatisticConfiguration();
	    	ejbStatModel.add(testBeanDetails);  
	    	ejbStatModel.add(testSingletonDetails);
	    	rootModel.setEjbStatisticModels(ejbStatModel);
	    	final Ejb3StatisticUpdater ejb3StatExtractor = new Ejb3StatisticUpdater("test.war");
    		ejb3StatExtractor.updateModel(client.getModelController(), rootModel); 
    		
    		_Logger.info("\n" + testBeanDetails.toString());
    		_Logger.info("\n" + testSingletonDetails.toString());
    		
    		assertEquals(
    				formatter.formatHeader(rootModel).split(", ", -1).length, 
    				formatter.formatLine(rootModel).split(", ", -1).length);
    	}
    }
    
    @Test
    public void testEjb3List() throws IOException {    	   	
    	try (final DmrClient client = new DmrClient()) {
    		client.enableEjb3Statistics(true);
    		_testBean.testMe();
        	_testSingleton.callMe();
        	_testSingleton.callMe();
        	_testSingleton.callMeAgain(); 
	    	final List<Ejb3StatisticModel> ejbStatModel = new ArrayList<Ejb3StatisticModel>();
	    	final Ejb3StatisticModel testBeanDetails = new Ejb3StatisticModel("TestBean", "TestBean", EjbType.StatelessBean, new String[] {"execution-time"}, new String[] {"testMe"});    	
	    	final Ejb3StatisticModel testSingletonDetails = new Ejb3StatisticModel("TestSingleton", "TestSingleton", EjbType.SingletonBean, new String[] {"execution-time"}, new String[] {"callMe", "callMeAgain"});
	    	final Ejb3StatisticCSVFormatter formatter = new Ejb3StatisticCSVFormatter();
	    	final DmrStatisticConfiguration rootModel = new DmrStatisticConfiguration();
	    	ejbStatModel.add(testBeanDetails);  
	    	ejbStatModel.add(testSingletonDetails);
	    	rootModel.setEjbStatisticModels(ejbStatModel);
	    	final Ejb3StatisticUpdater ejb3StatExtractor = new Ejb3StatisticUpdater("test.war");
    		ejb3StatExtractor.updateModel(client.getModelController(),  rootModel);   
    		assertEquals(
    				formatter.formatHeader(rootModel).split(", ", -1).length, 
    				formatter.formatLine(rootModel).split(", ", -1).length);
    	}
    }
    
}
