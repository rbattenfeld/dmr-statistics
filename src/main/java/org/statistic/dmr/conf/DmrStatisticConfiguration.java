package org.statistic.dmr.conf;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.statistic.dmr.stat.ejb3.Ejb3StatisticModel;
import org.statistic.dmr.stat.platform.PlatformStatisticModel;

@Root
public class DmrStatisticConfiguration {
	
	@Attribute(name = "deploymentName")
	private String _deploymentName;
	
	@ElementList(entry = "ejbStatistics", inline = true)
	private List<Ejb3StatisticModel> _ejbStatisticDetailList;

	@ElementList(entry = "platformStatistics", inline = true)
	private List<PlatformStatisticModel> _platformDetailsList;
	
	public String getDeploymentName() {
		return _deploymentName;
	}

	public void setDeploymentName(final String deploymentName) {
		_deploymentName = deploymentName;
	}

	public List<Ejb3StatisticModel> getEjbStatistics() {
		return _ejbStatisticDetailList;
	}

	public void setEjbStatisticDetailList(final List<Ejb3StatisticModel> getEjbStatistics) {
		_ejbStatisticDetailList = getEjbStatistics;
	}
	
	public List<PlatformStatisticModel> getPlatformDetailsList() {
		return _platformDetailsList;
	}

	public void setPlatformDetailsList(final List<PlatformStatisticModel> platformDetailsList) {
		_platformDetailsList = platformDetailsList;
	}

	public static DmrStatisticConfiguration loadFromResource(final String resourceName) throws IOException {
		final Serializer serializer = new Persister();
		final InputStream resourceStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(resourceName);
		try {
			return serializer.read(DmrStatisticConfiguration.class, resourceStream);
		} catch (final Exception ex) {
			throw new IOException(ex);
		}
	}
}
