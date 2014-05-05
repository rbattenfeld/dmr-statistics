package org.statistic.dmr.api;

import java.io.IOException;

import org.jboss.as.controller.client.ModelControllerClient;

public interface IDmrStatisticUpdater {
	public void updateModel(final ModelControllerClient client, final IDmrModel Model) throws IOException;
}
