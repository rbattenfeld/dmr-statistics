package org.rbattenfeld.statistic.dmr.test.incontainer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.rbattenfeld.statistic.dmr.DmrStatisticConfigurer;
import org.rbattenfeld.statistic.dmr.ejb3.Ejb3StatisticDetails;
import org.rbattenfeld.statistic.dmr.ejb3.EjbType;
import org.rbattenfeld.statistic.dmr.platform.IPlatformStatisticDetails;
import org.rbattenfeld.statistic.dmr.platform.PlatformStatisticDetails;
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
    	
    	final List<PlatformStatisticDetails> platformDetails = new ArrayList<PlatformStatisticDetails>();
    	final PlatformStatisticDetails heapDetails = new PlatformStatisticDetails("memory", "heap-memory-usage", new String[] {"init", "used", "committed", "max"});
    	final PlatformStatisticDetails nonHeapDetails = new PlatformStatisticDetails("memory", "non-heap-memory-usage", new String[] {"init", "used", "committed", "max"});
    	final PlatformStatisticDetails osDetails = new PlatformStatisticDetails("operating-system", "", new String[] {"available-processors", "system-load-average"});
    	platformDetails.add(heapDetails);
    	platformDetails.add(nonHeapDetails);
    	platformDetails.add(osDetails);
    	
    	configurer.setDeploymentName("test.war");
    	configurer.setEjbStatisticDetailList(details);
    	configurer.setPlatformDetailsList(platformDetails);
    	final Serializer serializer = new Persister();
    	final File file = new File("stat.xml");
        serializer.write(configurer, file);
	}
}
