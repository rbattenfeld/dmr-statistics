package org.statistic.dmr.api;

import java.io.IOException;

import org.jboss.as.controller.client.ModelControllerClient;

public interface DmrStatisticExtractor<T> {
	public void updateModel(final ModelControllerClient client, final T source) throws IOException;
}
