package org.statistic.dmr.api;

import java.util.List;

import org.statistic.dmr.stat.ejb3.Ejb3StatisticModel;
import org.statistic.dmr.stat.platform.PlatformStatisticModel;

public interface IDmrModel {
	
	public String getDeploymentName();

	public void setDeploymentName(final String deploymentName);
	
	public void setLogCategory(final String deploymentName);
	
	public String getLogCategory();

	public void setCsvSeparator(final char csvSeparator);

	public char getCsvSeparator();

	public List<Ejb3StatisticModel> getEjbStatisticModels();

	public void setEjbStatisticModels(final List<Ejb3StatisticModel> ejbStatisticModels);
	
	public List<PlatformStatisticModel> getPlatformStatisticModels();

	public void setPlatformStatisticModels(final List<PlatformStatisticModel> platformStatisticModels);
}
