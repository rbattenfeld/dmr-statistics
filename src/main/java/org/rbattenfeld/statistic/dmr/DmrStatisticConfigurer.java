package org.rbattenfeld.statistic.dmr;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.rbattenfeld.statistic.dmr.ejb3.Ejb3StatisticDetails;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

@Root
public class DmrStatisticConfigurer {
	
	@Attribute(name = "deploymentName")
	private String _deploymentName;
	
	@ElementList(entry = "ejbStatistics", inline = true)
	private List<Ejb3StatisticDetails> _ejbStatisticDetailList;

	public String getDeploymentName() {
		return _deploymentName;
	}

	public void setDeploymentName(final String deploymentName) {
		_deploymentName = deploymentName;
	}

	public List<Ejb3StatisticDetails> getEjbStatistics() {
		return _ejbStatisticDetailList;
	}

	public void setEjbStatisticDetailList(final List<Ejb3StatisticDetails> getEjbStatistics) {
		_ejbStatisticDetailList = getEjbStatistics;
	}
	
	public static DmrStatisticConfigurer loadFromResource(final String resourceName) throws IOException {
		final Serializer serializer = new Persister();
		final InputStream resourceStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(resourceName);
		try {
			return serializer.read(DmrStatisticConfigurer.class, resourceStream);
		} catch (final Exception ex) {
			throw new IOException(ex);
		}
	}
}
