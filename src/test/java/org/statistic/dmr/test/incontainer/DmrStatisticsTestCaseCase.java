package org.statistic.dmr.test.incontainer;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.inject.Inject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.statistic.dmr.client.DmrClient;
import org.statistic.dmr.conf.DmrStatisticConfiguration;
import org.statistic.dmr.stat.ejb3.Ejb3StatisticCSVFormatter;
import org.statistic.dmr.stat.ejb3.Ejb3StatisticUpdater;
import org.statistic.dmr.stat.ejb3.Ejb3StatisticModel;
import org.statistic.dmr.stat.ejb3.EjbType;
import org.statistic.dmr.stat.platform.PlatformStatisticCSVFormatter;
import org.statistic.dmr.stat.platform.PlatformStatisticUpdater;

@RunWith(Arquillian.class)
public final class DmrStatisticsTestCaseCase {
	private static final Log _Logger = LogFactory.getLog(DmrStatisticsTestCaseCase.class);
	
    @Deployment
    public static JavaArchive createDeployment() {
    	final File manifestMF = new File("../dmr-statistics/src/test/resources/MANIFEST.MF"); 
    	final File statFile = new File("../dmr-statistics/src/test/resources/stat.xml"); 
    	return  ShrinkWrap.create(JavaArchive.class, "resourceMonitor.jar")
			.addPackages(true, "org.statistic.dmr")
			.addPackages(true, "org.simpleframework.xml")
			.addPackage("org.statistic.dmr.test.incontainer")
			.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
    	    .addAsManifestResource(manifestMF, "MANIFEST.MF")
    	    .addAsManifestResource(statFile, "stat.xml");
    }

    @EJB(mappedName = "java:app/resourceMonitor/TestBean!com.swx.ptp.kernel.statistic.test.incontainer.dmr.TestBean")
    private TestBean _testBean;
        
    @EJB(mappedName = "java:app/resourceMonitor/TestSingleton!com.swx.ptp.kernel.statistic.test.incontainer.dmr.TestSingleton")
    private TestSingleton _testSingleton;
        
    @Inject
    private PlatformStatisticUpdater _platformStatisticExtractor;
    
    @Test
    public void testPlatform() throws Exception {
    	final DmrStatisticConfiguration configurer = DmrStatisticConfiguration.loadFromResource("META-INF/stat.xml");
    	try (final DmrClient client = new DmrClient(true)) {    		
    		final PlatformStatisticCSVFormatter formatter = new PlatformStatisticCSVFormatter();
    		_platformStatisticExtractor.updateModel(client.getModelController(), configurer.getPlatformDetailsList());
    		_Logger.info(formatter.formatHeader(configurer.getPlatformDetailsList()));
    		_Logger.info(formatter.formatLine(configurer.getPlatformDetailsList()));
    		assertEquals(
    				formatter.formatHeader(configurer.getPlatformDetailsList()).split(", ", -1).length, 
    				formatter.formatLine(configurer.getPlatformDetailsList()).split(", ", -1).length);
    	}
    }
    
    @Test
    public void testConfigFromResourceWithNoStatAvailable() throws Exception {
    	final DmrStatisticConfiguration configurer = DmrStatisticConfiguration.loadFromResource("META-INF/stat.xml");
    	try (final DmrClient client = new DmrClient(true)) {    
    		final Ejb3StatisticCSVFormatter formatter = new Ejb3StatisticCSVFormatter();
    		final Ejb3StatisticUpdater ejb3StatExtractor = new Ejb3StatisticUpdater(configurer.getDeploymentName());
    		ejb3StatExtractor.updateModel(client.getModelController(),  configurer.getEjbStatistics());   
    		_Logger.info(formatter.formatHeader(configurer.getEjbStatistics()));
    		_Logger.info(formatter.formatLine(configurer.getEjbStatistics()));
    		assertEquals(
    				formatter.formatHeader(configurer.getEjbStatistics()).split(", ", -1).length, 
    				formatter.formatLine(configurer.getEjbStatistics()).split(", ", -1).length);
    	}
    }
    
    @Test
    public void testConfigFromResource() throws Exception {
    	_testBean.testMe();
    	_testSingleton.callMe();
    	_testSingleton.callMe();
    	_testSingleton.callMeAgain();
    	final DmrStatisticConfiguration configurer = DmrStatisticConfiguration.loadFromResource("META-INF/stat.xml");
    	try (final DmrClient client = new DmrClient(true)) {    
    		final Ejb3StatisticCSVFormatter formatter = new Ejb3StatisticCSVFormatter();
    		final Ejb3StatisticUpdater ejb3StatExtractor = new Ejb3StatisticUpdater(configurer.getDeploymentName());
    		ejb3StatExtractor.updateModel(client.getModelController(),  configurer.getEjbStatistics());    		
    		_Logger.info(formatter.formatHeader(configurer.getEjbStatistics()));
    		_Logger.info(formatter.formatLine(configurer.getEjbStatistics()));
    		assertEquals(
    				formatter.formatHeader(configurer.getEjbStatistics()).split(", ", -1).length, 
    				formatter.formatLine(configurer.getEjbStatistics()).split(", ", -1).length);
    	}
    }
    
    @Test
    public void testNullDeployment() throws IOException {
    	_testBean.testMe();
    	final DmrStatisticConfiguration configurer = DmrStatisticConfiguration.loadFromResource("META-INF/stat.xml");
    	configurer.setDeploymentName("testXXXX.war");
    	try (final DmrClient client = new DmrClient(true)) {    
    		final Ejb3StatisticCSVFormatter formatter = new Ejb3StatisticCSVFormatter();
    		final Ejb3StatisticUpdater ejb3StatExtractor = new Ejb3StatisticUpdater(configurer.getDeploymentName());
    		ejb3StatExtractor.updateModel(client.getModelController(),  configurer.getEjbStatistics());      
    		assertEquals(
    				formatter.formatHeader(configurer.getEjbStatistics()).split(", ", -1).length, 
    				formatter.formatLine(configurer.getEjbStatistics()).split(", ", -1).length);
    	}
    }
        
    @Test
    public void testDefaultEJB3Statistics() throws IOException {
    	_testBean.testMe();
    	_testSingleton.callMe();
    	_testSingleton.callMe();
    	_testSingleton.callMeAgain();    	
    	try (final DmrClient client = new DmrClient(true)) {
	    	final List<Ejb3StatisticModel> details = new ArrayList<Ejb3StatisticModel>();
	    	final Ejb3StatisticModel testBeanDetails = new Ejb3StatisticModel("TestBean", "TestBean", EjbType.StatelessBean, null, null);    	
	    	final Ejb3StatisticModel testSingletonDetails = new Ejb3StatisticModel("TestSingleton", "TestSingleton", EjbType.SingletonBean, null, null);
	    	final Ejb3StatisticCSVFormatter formatter = new Ejb3StatisticCSVFormatter();
	    	details.add(testBeanDetails);  
	    	details.add(testSingletonDetails);
	    	final Ejb3StatisticUpdater ejb3StatExtractor = new Ejb3StatisticUpdater("test.war");
    		ejb3StatExtractor.updateModel(client.getModelController(),  details);   
    		assertEquals(
    				formatter.formatHeader(details).split(", ", -1).length, 
    				formatter.formatLine(details).split(", ", -1).length);
    	}
    }
    
    @Test
    public void testEjb3List() throws IOException {
    	_testBean.testMe();
    	_testSingleton.callMe();
    	_testSingleton.callMe();
    	_testSingleton.callMeAgain();    	
    	try (final DmrClient client = new DmrClient(true)) {
	    	final List<Ejb3StatisticModel> details = new ArrayList<Ejb3StatisticModel>();
	    	final Ejb3StatisticModel testBeanDetails = new Ejb3StatisticModel("TestBean", "TestBean", EjbType.StatelessBean, new String[] {"execution-time"}, new String[] {"testMe"});    	
	    	final Ejb3StatisticModel testSingletonDetails = new Ejb3StatisticModel("TestSingleton", "TestSingleton", EjbType.SingletonBean, new String[] {"execution-time"}, new String[] {"callMe", "callMeAgain"});
	    	final Ejb3StatisticCSVFormatter formatter = new Ejb3StatisticCSVFormatter();
	    	details.add(testBeanDetails);  
	    	details.add(testSingletonDetails);
	    	final Ejb3StatisticUpdater ejb3StatExtractor = new Ejb3StatisticUpdater("test.war");
    		ejb3StatExtractor.updateModel(client.getModelController(),  details);   
    		assertEquals(
    				formatter.formatHeader(details).split(", ", -1).length, 
    				formatter.formatLine(details).split(", ", -1).length);
    	}
    }
    
    //-----------------------------------------------------------------------||
    //-- Private Method -----------------------------------------------------||
    //-----------------------------------------------------------------------||
    
//    private void printStatistic(final Ejb3ClassStatistic ejb3Stat) {
//    	_Logger.info(String.format("%-25s : %s", "Deployment", ejb3Stat.getDeployment()));
//    	_Logger.info(String.format("%-25s : %s", "Bean", ejb3Stat.getBeanName()));
//    	
//    	for (final Map.Entry<String, Object> entry : ejb3Stat.getClassStatistic().entrySet()) {
//    		_Logger.info(String.format("%-25s : %s", entry.getKey(), entry.getValue()));
//    	}
//    	
//    	for (final Ejb3MethodStatistic methodStat : ejb3Stat.getMethodStatistic()) {
//    		_Logger.info(String.format("%-25s : %d", methodStat.getMethodName() + " executionTime", methodStat.getExecutionTime()));
//    		_Logger.info(String.format("%-25s : %d", methodStat.getMethodName() + " invocations", methodStat.getInvocations()));
//    		_Logger.info(String.format("%-25s : %d", methodStat.getMethodName() + " waitTime", methodStat.getWaitTime()));
//    	}
//    	
//    	_Logger.info("csv: " +  ejb3Stat.toCSVString(','));
//    }
    
}
