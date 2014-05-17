package org.statistic.test.incontainer;


public class TestDeSerializer {

//	@Test
//	public void testSerializer() throws Exception {
//		final DmrStatisticConfiguration configurer = new DmrStatisticConfiguration();
//		
//		final List<Ejb3StatisticModel> details = new ArrayList<>();	
//    	final Ejb3StatisticModel testBeanDetails = new Ejb3StatisticModel("TestBean", "T1", EjbType.StatelessBean, new String[] {"execution-time"}, new String[] {"testMe"});    	
//    	final Ejb3StatisticModel testSingletonDetails = new Ejb3StatisticModel("TestSingleton", "T2", EjbType.SingletonBean, new String[] {"execution-time"}, new String[] {"callMe", "callMeAgain"});	
//    	details.add(testBeanDetails);  
//    	details.add(testSingletonDetails);
//    	
//    	final List<PlatformStatisticModel> platformDetails = new ArrayList<PlatformStatisticModel>();
//    	final PlatformStatisticModel heapDetails = new PlatformStatisticModel("memory", "heap-memory-usage", new String[] {"init", "used", "committed", "max"});
//    	final PlatformStatisticModel nonHeapDetails = new PlatformStatisticModel("memory", "non-heap-memory-usage", new String[] {"init", "used", "committed", "max"});
//    	final PlatformStatisticModel osDetails = new PlatformStatisticModel("operating-system", "", new String[] {"available-processors", "system-load-average"});
//    	platformDetails.add(heapDetails);
//    	platformDetails.add(nonHeapDetails);
//    	platformDetails.add(osDetails);
//    	
//    	final List<DatasourceStatisticModel> dataSourceModels = new ArrayList<DatasourceStatisticModel>();
//    	final DatasourceStatisticModel dataSource = new DatasourceStatisticModel();
//    	dataSource.setDatasource("ExampleDS");
//    	dataSource.setJdbcKeys(new String[] {"PreparedStatementCacheAccessCount", "PreparedStatementCacheHitCount", "PreparedStatementCacheMissCount"});
//    	dataSource.setPoolKeys(new String[] {"ActiveCount", "AvailableCount", "InUseCount", "MaxUsedCount", "MaxWaitCount",  "MaxWaitTime"});
//    	dataSourceModels.add(dataSource);
//    	
//    	final List<PathElement> elements = new ArrayList<PathElement>();
//    	final List<GenericModel> genericModels = new ArrayList<GenericModel>();  	
//    	final PathElement element = new PathElement();
//    	element.setAbbreviation("gs-adapter");
//    	element.setPathElement("connection-definitions=java:global/DefaultGigaSpaceConnectionFactory");
//    	element.setKeys(new String[] {"ActiveCount", "AvailableCount", "InUseCount", "MaxUsedCount", "MaxWaitCount",  "MaxWaitTime"});
//    	elements.add(element);
//    	
//    	final GenericModel adapterModel = new GenericModel();
//    	adapterModel.setName("seni-req-portal-gs-adapter");
//    	adapterModel.setPathAddress("/deployment=seni-req-portal-gs-adapter-9.0.0.4.rar/subsystem=resource-adapters/statistics=statistics/resource-adapter=seni-req-portal-gs-adapter-9.0.0.4.rar");
//    	adapterModel.setPathElements(elements);    	
//    	genericModels.add(adapterModel);
//    	
//    	configurer.setDeploymentName("test.war");
//    	configurer.setCsvSeparator(';');
//    	configurer.setIntervall(5000);
//    	configurer.setLogCategory("ejb3stat");
//    	configurer.setEjbStatisticModels(details);
//    	configurer.setPlatformStatisticModels(platformDetails);
//    	configurer.setDataSourceStatisticModels(dataSourceModels);
//    	configurer.setGenericStatisticModels(genericModels);
//    	final Serializer serializer = new Persister();
//    	final File file = new File("stat444.xml");
//        serializer.write(configurer, file);
//	}
}
