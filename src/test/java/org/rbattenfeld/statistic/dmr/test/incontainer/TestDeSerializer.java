package org.rbattenfeld.statistic.dmr.test.incontainer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.rbattenfeld.statistic.dmr.DmrStatisticConfigurer;
import org.rbattenfeld.statistic.dmr.ejb3.Ejb3StatisticDetails;
import org.rbattenfeld.statistic.dmr.ejb3.EjbType;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

public class TestDeSerializer {

	@Test
	public void testSerializer() throws Exception {
		final DmrStatisticConfigurer configurer = new DmrStatisticConfigurer();
		final List<Ejb3StatisticDetails> details = new ArrayList<Ejb3StatisticDetails>();
    	final Ejb3StatisticDetails testBeanDetails = new Ejb3StatisticDetails("TestBean", "T1", EjbType.StatelessBean, new String[] {"execution-time"}, new String[] {"testMe"});    	
    	final Ejb3StatisticDetails testSingletonDetails = new Ejb3StatisticDetails("TestSingleton", "T2", EjbType.SingletonBean, new String[] {"execution-time"}, new String[] {"callMe", "callMeAgain"});	
    	details.add(testBeanDetails);  
    	details.add(testSingletonDetails);
    	configurer.setDeploymentName("test.war");
    	configurer.setEjbStatisticDetailList(details);
		
    	final Serializer serializer = new Persister();
    	final File file = new File("ejbStat.xml");
        serializer.write(configurer, file);
	}
}
