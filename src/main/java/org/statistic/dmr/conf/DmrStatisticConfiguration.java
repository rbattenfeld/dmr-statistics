package org.statistic.dmr.conf;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.statistic.dmr.api.IDmrModel;
import org.statistic.dmr.stat.datasource.DatasourceStatisticModel;
import org.statistic.dmr.stat.ejb3.Ejb3StatisticModel;
import org.statistic.dmr.stat.platform.PlatformStatisticModel;

@Root
public class DmrStatisticConfiguration implements IDmrModel {
	
	@Attribute(name = "deploymentName")
	private String _deploymentName;
	
	@Attribute(name = "logCategory")
	private String _logCategory;
	
	@Attribute(name = "csvSeparator")
	private char _csvSeparator;
	
	@ElementList(entry = "ejbStatistics", inline = true)
	private List<Ejb3StatisticModel> _ejbStatisticModels;

	@ElementList(entry = "platformStatistics", inline = true)
	private List<PlatformStatisticModel> _platformStatisticModels;
	
	@ElementList(entry = "datasourceStatistics", inline = true)
	private List<DatasourceStatisticModel> _datasourceStatisticModels;
	
	public String getDeploymentName() {
		return _deploymentName;
	}

	@Override
	public void setDeploymentName(final String deploymentName) {
		_deploymentName = deploymentName;
	}

	@Override
	public String getLogCategory() {
		return _logCategory;
	}

	@Override
	public void setLogCategory(final String logCategory) {
		_logCategory = logCategory;
	}

	@Override
	public char getCsvSeparator() {
		return _csvSeparator;
	}

	@Override
	public void setCsvSeparator(final char csvSeparator) {
		_csvSeparator = csvSeparator;
	}

	@Override
	public List<Ejb3StatisticModel> getEjbStatisticModels() {
		return _ejbStatisticModels;
	}

	@Override
	public void setEjbStatisticModels(final List<Ejb3StatisticModel> ejbStatisticModels) {
		_ejbStatisticModels = ejbStatisticModels;
	}

	@Override
	public List<PlatformStatisticModel> getPlatformStatisticModels() {
		return _platformStatisticModels;
	}

	@Override
	public void setPlatformStatisticModels(final List<PlatformStatisticModel> platformStatisticModels) {
		_platformStatisticModels = platformStatisticModels;
	}


	@Override
	public List<DatasourceStatisticModel> getDataSourceStatisticModels() {
		return _datasourceStatisticModels;
	}

	@Override
	public void setDataSourceStatisticModels(final List<DatasourceStatisticModel> datasourceStatisticModels) {
		_datasourceStatisticModels = datasourceStatisticModels;
	}
	
	/**
	 * Returns a <code>DmrStatisticConfiguration</code> instance read from the given resource file.
	 * @param resourceName
	 * @return
	 * @throws IOException
	 */
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
