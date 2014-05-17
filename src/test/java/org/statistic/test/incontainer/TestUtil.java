package org.statistic.test.incontainer;

import java.io.File;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;

public class TestUtil {

	public static JavaArchive getDeployment() {
    	final File manifestMF = new File("../dmr-statistics/src/test/resources/MANIFEST.MF"); 
    	final File genericFile = new File("../dmr-statistics/src/test/resources/generic-stat.xml"); 
    	return  ShrinkWrap.create(JavaArchive.class, "resourceMonitor.jar")
			.addPackages(true, "org.statistic.connectors")
			.addPackages(true, "org.statistic.csvloggers")
			.addPackages(true, "org.statistic.models")
			.addPackages(true, "org.simpleframework.xml")
			.addPackages(true, "org.statistic.test.incontainer")
			.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
    	    .addAsManifestResource(manifestMF, "MANIFEST.MF")
    	    .addAsManifestResource(genericFile, "generic-stat.xml");
    }
}
