package org.statistic.models;

import java.io.IOException;


public interface IModelUpdater {
	public void updateModel(final IRootModel Model) throws IOException;
}
