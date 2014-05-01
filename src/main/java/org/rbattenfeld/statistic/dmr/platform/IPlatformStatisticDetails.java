package org.rbattenfeld.statistic.dmr.platform;

public interface IPlatformStatisticDetails {
	
	public String getType();

	public void setType(String type);

	public String getSubType();

	public void setSubType(String subType);

	public String[] getKeys();

	public void setKeys(String[] keys);

	public String[] getValues();

	public void setValues(String[] values);
}
