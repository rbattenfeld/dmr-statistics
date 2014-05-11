package org.statistic.dmr.test.incontainer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.statistic.dmr.conf.DmrStatisticConfiguration;
import org.statistic.dmr.stat.datasource.DatasourceStatisticModel;
import org.statistic.dmr.stat.ejb3.Ejb3StatisticModel;
import org.statistic.dmr.stat.ejb3.EjbType;
import org.statistic.dmr.stat.platform.PlatformStatisticModel;

public class TestDeSerializer {

	@Test
	public void testSerializer() throws Exception {
		final DmrStatisticConfiguration configurer = new DmrStatisticConfiguration();
		
		final List<Ejb3StatisticModel> details = new ArrayList<>();	
    	final Ejb3StatisticModel testBeanDetails = new Ejb3StatisticModel("TestBean", "T1", EjbType.StatelessBean, new String[] {"execution-time"}, new String[] {"testMe"});    	
    	final Ejb3StatisticModel testSingletonDetails = new Ejb3StatisticModel("TestSingleton", "T2", EjbType.SingletonBean, new String[] {"execution-time"}, new String[] {"callMe", "callMeAgain"});	
    	details.add(testBeanDetails);  
    	details.add(testSingletonDetails);
    	
    	final List<PlatformStatisticModel> platformDetails = new ArrayList<PlatformStatisticModel>();
    	final PlatformStatisticModel heapDetails = new PlatformStatisticModel("memory", "heap-memory-usage", new String[] {"init", "used", "committed", "max"});
    	final PlatformStatisticModel nonHeapDetails = new PlatformStatisticModel("memory", "non-heap-memory-usage", new String[] {"init", "used", "committed", "max"});
    	final PlatformStatisticModel osDetails = new PlatformStatisticModel("operating-system", "", new String[] {"available-processors", "system-load-average"});
    	platformDetails.add(heapDetails);
    	platformDetails.add(nonHeapDetails);
    	platformDetails.add(osDetails);
    	
    	final List<DatasourceStatisticModel> dataSourceModels = new ArrayList<DatasourceStatisticModel>();
    	final DatasourceStatisticModel dataSource = new DatasourceStatisticModel();
    	dataSource.setDatasource("ExampleDS");
    	dataSource.setJdbcKeys(new String[] {"PreparedStatementCacheAccessCount", "PreparedStatementCacheHitCount", "PreparedStatementCacheMissCount"});
    	dataSource.setPoolKeys(new String[] {"ActiveCount", "AvailableCount", "InUseCount", "MaxUsedCount", "MaxWaitCount",  "MaxWaitTime"});
    	dataSourceModels.add(dataSource);
    	
    	configurer.setDeploymentName("test.war");
    	configurer.setCsvSeparator(';');
    	configurer.setIntervall(5000);
    	configurer.setLogCategory("ejb3stat");
    	configurer.setEjbStatisticModels(details);
    	configurer.setPlatformStatisticModels(platformDetails);
    	configurer.setDataSourceStatisticModels(dataSourceModels);
    	final Serializer serializer = new Persister();
    	final File file = new File("stat.xml");
        serializer.write(configurer, file);
	}
}
