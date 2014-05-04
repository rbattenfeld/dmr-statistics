package org.statistic.dmr.test.incontainer;

import java.io.File;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;

public class TestUtil {

	public static JavaArchive getDeployment() {
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
}
