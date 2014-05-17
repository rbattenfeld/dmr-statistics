package org.statistic.test.incontainer;

import javax.ejb.EJB;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.statistic.csvloggers.CsvPeriodicLogger;

@RunWith(Arquillian.class)
public final class DefaultStatisticLoggerITCase {
	
    @Deployment
    public static JavaArchive createDeployment() {
    	return TestUtil.getDeployment();
    }

    @EJB(mappedName = "java:app/resourceMonitor/TestBean!org.statistic.test.incontainer.TestBean")
    private TestBean _testBean;
        
    @EJB(mappedName = "java:app/resourceMonitor/TestSingleton!org.statistic.test.incontainer.TestSingleton")
    private TestSingleton _testSingleton;
         
    @EJB(mappedName = "java:app/resourceMonitor/CsvPeriodicLogger!com.swx.ptp.kernel.statistic.csvloggers.CsvPeriodicLogger")
    private CsvPeriodicLogger _statisticLogger;
       
    @Test
    public void testPeriodicLogger() throws Exception {
    	_statisticLogger.startLogging("localhost", 9990, "META-INF/stat.xml");
    	for (int i = 0; i < 200; i++) {
    		_testBean.testMe();
        	_testSingleton.callMe();
        	_testSingleton.callMe();
        	_testSingleton.callMeAgain();
    		sleep(1000);
    	}
    	_statisticLogger.stopLogging();
    }
    
	//-----------------------------------------------------------------------||
	//-- Private Methods ----------------------------------------------------||
	//-----------------------------------------------------------------------||
    
    private void sleep(final long timeout) {
    	try {
    		Thread.currentThread();
			Thread.sleep(timeout);
		} catch (InterruptedException e) {
			// ignore
		}
    }
    
}
