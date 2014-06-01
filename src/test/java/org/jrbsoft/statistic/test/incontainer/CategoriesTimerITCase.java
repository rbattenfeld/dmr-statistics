package org.jrbsoft.statistic.test.incontainer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jrbsoft.statistic.logging.CsvCategoriesTimer;
import org.jrbsoft.statistic.model.IModelUpdater;
import org.jrbsoft.statistic.model.IRootModel;
import org.jrbsoft.statistic.protocol.dmr.connecting.IDmrControllerClient;
import org.jrbsoft.statistic.protocol.dmr.model.DmrModelUpdater;
import org.jrbsoft.statistic.protocol.jmx.connecting.IMBeanControllerClient;
import org.jrbsoft.statistic.protocol.jmx.model.MBeanModelUpdater;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public final class CategoriesTimerITCase {
	
    @Deployment
    public static JavaArchive createDeployment() {
    	return TestUtil.getDeployment();
    }

    @EJB(mappedName = "java:app/resourceMonitor/TestBean!org.jrbsoft.statistic.test.incontainer.TestBean")
    private TestBean _testBean;
        
    @EJB(mappedName = "java:app/resourceMonitor/TestSingleton!org.jrbsoft.statistic.test.incontainer.TestSingleton")
    private TestSingleton _testSingleton;   
         
    @EJB(mappedName = "java:app/resourceMonitor/CsvCategoriesTimer!org.jrbsoft.statistic.logging.CsvCategoriesTimer")
    private CsvCategoriesTimer _categoriesTimerBean;
       
    @Test
    public void testPeriodicLogger() throws Exception {
    	try (final IDmrControllerClient client = IDmrControllerClient.Factory.create()) { 
			_categoriesTimerBean.startLogging(getModels(), getUpdaters(client));
	    	for (int i = 0; i < 100; i++) {
	    		_testBean.testMe();
	        	_testSingleton.callMe();
	        	_testSingleton.callMe();
	        	_testSingleton.callMeAgain();
	    		sleep(1000);
	    	}
	    	_categoriesTimerBean.stopLogging();
    	}
    }
    
	//-----------------------------------------------------------------------||
	//-- Private Methods ----------------------------------------------------||
	//-----------------------------------------------------------------------||

    private List<IRootModel> getModels() throws IOException {
    	final List<IRootModel> models = new ArrayList<IRootModel>();
    	final IRootModel rootModel1 = IRootModel.Factory.createFromResource("META-INF/generic-stat.xml");
    	final IRootModel rootModel2 = IRootModel.Factory.createFromResource("META-INF/generic-stat-2.xml");
	    models.add(rootModel1);
	    models.add(rootModel2);
	    return models;
    }

    private List<IModelUpdater> getUpdaters(final IDmrControllerClient client) {
    	final List<IModelUpdater> updaters = new ArrayList<IModelUpdater>();
        final DmrModelUpdater dmrUpdater = new DmrModelUpdater(client.getModelController());
	    final MBeanModelUpdater mbeanUpdater = new MBeanModelUpdater(IMBeanControllerClient.Factory.connectEmbeddedMBeanServer().getMBeanServer());
	    updaters.add(dmrUpdater);
	    updaters.add(mbeanUpdater);
	    return updaters;
    }

    private void sleep(final long timeout) {
    	try {
    		Thread.currentThread();
			Thread.sleep(timeout);
		} catch (InterruptedException e) {
			// ignore
		}
    }
    
}
