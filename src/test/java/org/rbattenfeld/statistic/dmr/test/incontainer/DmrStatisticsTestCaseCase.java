package org.rbattenfeld.statistic.dmr.test.incontainer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
import org.rbattenfeld.statistic.dmr.DmrClient;
import org.rbattenfeld.statistic.dmr.DmrStatisticConfigurer;
import org.rbattenfeld.statistic.dmr.ejb3.Ejb3ClassStatistic;
import org.rbattenfeld.statistic.dmr.ejb3.Ejb3MethodStatistic;
import org.rbattenfeld.statistic.dmr.ejb3.Ejb3StatisticDetails;
import org.rbattenfeld.statistic.dmr.ejb3.Ejb3StatisticExtractor;
import org.rbattenfeld.statistic.dmr.ejb3.Ejb3StatisticFormatter;
import org.rbattenfeld.statistic.dmr.ejb3.EjbType;
import org.rbattenfeld.statistic.dmr.ejb3.IEjb3StatisticDetails;
import org.rbattenfeld.statistic.dmr.platform.PlatformStatisticExtractor;

@RunWith(Arquillian.class)
public final class DmrStatisticsTestCaseCase {
	private static final Log _Logger = LogFactory.getLog(DmrStatisticsTestCaseCase.class);
	
    @Deployment
    public static JavaArchive createDeployment() {
    	final File manifestMF = new File("../dmr-statistics/src/test/resources/MANIFEST.MF"); 
    	final File ejb3StatFile = new File("../dmr-statistics/src/test/resources/ejbStat.xml"); 
    	return  ShrinkWrap.create(JavaArchive.class, "resourceMonitor.jar")
			.addPackages(true, "org.rbattenfeld.statistic.dmr")
			.addPackages(true, "org.simpleframework.xml")
			.addPackage("org.rbattenfeld.statistic.dmr.test.incontainer")
			.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
    	    .addAsManifestResource(manifestMF, "MANIFEST.MF")
    	    .addAsManifestResource(ejb3StatFile, "ejbStat.xml");
    }

    @EJB(mappedName = "java:app/resourceMonitor/TestBean!com.swx.ptp.kernel.statistic.test.incontainer.dmr.TestBean")
    private TestBean _testBean;
        
    @EJB(mappedName = "java:app/resourceMonitor/TestSingleton!com.swx.ptp.kernel.statistic.test.incontainer.dmr.TestSingleton")
    private TestSingleton _testSingleton;
    
    @Inject
    private Ejb3StatisticExtractor _ejb3StatExtractor;
    
    @Inject
    private PlatformStatisticExtractor _platformStatisticExtractor;
    
    @Test
    public void testPlatform() throws Exception {
    	try (final DmrClient client = new DmrClient(true)) {
    		_platformStatisticExtractor.getStatistic(client.getModelController());
    	}
    }
    
    @Test
    public void testConfigFromResourceWithNoStatAvailable() throws Exception {
    	final DmrStatisticConfigurer configurer = DmrStatisticConfigurer.loadFromResource("META-INF/ejbStat.xml");
    	try (final DmrClient client = new DmrClient(true)) {    
    		final Ejb3StatisticFormatter formatter = new Ejb3StatisticFormatter();
    		final List<Ejb3ClassStatistic> ejb3StatList = _ejb3StatExtractor.getStatistic(client.getModelController(), configurer.getDeploymentName(),  configurer.getEjbStatistics());
    		final String headerLine = formatter.formatHeader(configurer);
    		final String bodyLine = formatter.formatLine(configurer, ejb3StatList);
    		_Logger.info(headerLine);
    		_Logger.info(bodyLine);
    	}
    }
    
    @Test
    public void testConfigFromResource() throws Exception {
    	_testBean.testMe();
    	_testSingleton.callMe();
    	_testSingleton.callMe();
    	_testSingleton.callMeAgain();
    	final DmrStatisticConfigurer configurer = DmrStatisticConfigurer.loadFromResource("META-INF/ejbStat.xml");
    	try (final DmrClient client = new DmrClient(true)) {    
    		final List<Ejb3ClassStatistic> ejb3StatList = _ejb3StatExtractor.getStatistic(client.getModelController(), configurer.getDeploymentName(),  configurer.getEjbStatistics());
    		final Ejb3StatisticFormatter formatter = new Ejb3StatisticFormatter();
    		final String headerLine = formatter.formatHeader(configurer);
    		final String bodyLine = formatter.formatLine(configurer, ejb3StatList);
    		_Logger.info(headerLine);
    		_Logger.info(bodyLine);
    	}
    }
    
    @Test
    public void testNullDeployment() throws IOException {
    	_testBean.testMe();
    	final DmrStatisticConfigurer configurer = DmrStatisticConfigurer.loadFromResource("META-INF/ejbStat.xml");
    	configurer.setDeploymentName("testXXXX.war");
    	try (final DmrClient client = new DmrClient(true)) {    
    		final List<Ejb3ClassStatistic> ejb3StatList = _ejb3StatExtractor.getStatistic(client.getModelController(), configurer.getDeploymentName(),  configurer.getEjbStatistics());
    		final Ejb3StatisticFormatter formatter = new Ejb3StatisticFormatter();
    		final String headerLine = formatter.formatHeader(configurer);
    		final String bodyLine = formatter.formatLine(configurer, ejb3StatList);
    		_Logger.info(headerLine);
    		_Logger.info(bodyLine);
    	}
    }
    
    @Test
    public void testEjb3Extractor() throws IOException {
    	_testBean.testMe();
    	try (final DmrClient client = new DmrClient(true)) {
    		final Ejb3ClassStatistic ejb3Stat = _ejb3StatExtractor.getStatistic(client.getModelController(), "test.war",  EjbType.StatelessBean, "TestBean", null, new String[] {"testMe"});
    		printStatistic(ejb3Stat);
    	}
    }
    
    @Test
    public void testEjb3List() throws IOException {
    	_testBean.testMe();
    	_testSingleton.callMe();
    	_testSingleton.callMe();
    	_testSingleton.callMeAgain();
    	
    	try (final DmrClient client = new DmrClient(true)) {
	    	final List<IEjb3StatisticDetails> details = new ArrayList<IEjb3StatisticDetails>();
	    	final Ejb3StatisticDetails testBeanDetails = new Ejb3StatisticDetails("TestBean", "TestBean", EjbType.StatelessBean, new String[] {"execution-time"}, new String[] {"testMe"});    	
	    	final Ejb3StatisticDetails testSingletonDetails = new Ejb3StatisticDetails("TestSingleton", "TestSingleton", EjbType.SingletonBean, new String[] {"execution-time"}, new String[] {"callMe", "callMeAgain"});
	    	
	    	details.add(testBeanDetails);  
	    	details.add(testSingletonDetails);
	    	final List<Ejb3ClassStatistic> ejb3StatList = _ejb3StatExtractor.getStatistic(client.getModelController(), "test.war",  details);
	    	
	    	assertTrue(ejb3StatList.size() == 2);
	    	assertEquals(ejb3StatList.get(0).getDeployment(), "test.war");
	    	assertEquals(ejb3StatList.get(0).getBeanName(), "TestBean");
	    	assertEquals(ejb3StatList.get(0).getBeanType(), EjbType.StatelessBean);
	    	assertTrue(ejb3StatList.get(0).getMethodStatistic().size() == 1);
	    	assertEquals(ejb3StatList.get(1).getDeployment(), "test.war");
	    	assertEquals(ejb3StatList.get(1).getBeanName(), "TestSingleton");
	    	assertEquals(ejb3StatList.get(1).getBeanType(), EjbType.SingletonBean);
	    	assertTrue(ejb3StatList.get(1).getMethodStatistic().size() == 2);
	    	
	    	for (Ejb3ClassStatistic ejb3Stat : ejb3StatList) {
	    		printStatistic(ejb3Stat);
	    	}
    	}
    }
    
    //-----------------------------------------------------------------------||
    //-- Private Method -----------------------------------------------------||
    //-----------------------------------------------------------------------||
    
    private void printStatistic(final Ejb3ClassStatistic ejb3Stat) {
    	_Logger.info(String.format("%-25s : %s", "Deployment", ejb3Stat.getDeployment()));
    	_Logger.info(String.format("%-25s : %s", "Bean", ejb3Stat.getBeanName()));
    	
    	for (final Map.Entry<String, Object> entry : ejb3Stat.getClassStatistic().entrySet()) {
    		_Logger.info(String.format("%-25s : %s", entry.getKey(), entry.getValue()));
    	}
    	
    	for (final Ejb3MethodStatistic methodStat : ejb3Stat.getMethodStatistic()) {
    		_Logger.info(String.format("%-25s : %d", methodStat.getMethodName() + " executionTime", methodStat.getExecutionTime()));
    		_Logger.info(String.format("%-25s : %d", methodStat.getMethodName() + " invocations", methodStat.getInvocations()));
    		_Logger.info(String.format("%-25s : %d", methodStat.getMethodName() + " waitTime", methodStat.getWaitTime()));
    	}
    	
    	_Logger.info("csv: " +  ejb3Stat.toCSVString(','));
    }
    
}
