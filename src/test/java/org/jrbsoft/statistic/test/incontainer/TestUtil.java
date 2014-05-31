package org.jrbsoft.statistic.test.incontainer;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;

public class TestUtil {

	public static JavaArchive getDeployment() {
    	return  ShrinkWrap.create(JavaArchive.class, "resourceMonitor.jar")
			.addPackages(true, "org.jrbsoft.statistic.model")
			.addPackages(true, "org.jrbsoft.statistic.protocol")
			.addPackages(true, "org.jrbsoft.statistic.test.incontainer")
			.addPackages(true, "org.simpleframework.xml")
			.addPackages(true, "org.xmlpull")
			.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
    	    .addAsManifestResource("MANIFEST.MF", "MANIFEST.MF")
    	    .addAsManifestResource("mbean-stat.xml", "mbean-stat.xml")
    	    .addAsManifestResource("mbean-notification-stat.xml", "mbean-notification-stat.xml")
    	    .addAsManifestResource("generic-stat.xml", "generic-stat.xml");
    }
}
