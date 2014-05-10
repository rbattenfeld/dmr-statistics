package org.statistic.dmr.stat.platform;

import java.io.IOException;

import org.jboss.as.controller.client.ModelControllerClient;
import org.jboss.as.controller.client.OperationBuilder;
import org.jboss.as.controller.client.helpers.ClientConstants;
import org.jboss.dmr.ModelNode;
import org.statistic.dmr.api.IDmrModel;
import org.statistic.dmr.api.IDmrStatisticUpdater;

public class PlatformStatisticUpdater implements IDmrStatisticUpdater {
		
	public void updateModel(final ModelControllerClient client, final IDmrModel model) throws IOException {
		for (final PlatformStatisticModel platformModel : model.getPlatformStatisticModels()) {
			ModelNode node = null;
			if (platformModel.getSubType() != null && !platformModel.getSubType().isEmpty()) {
				node = getPlatformOperation(client, platformModel.getType()).get(ClientConstants.RESULT).get(platformModel.getSubType());
			} else {
				node = getPlatformOperation(client, platformModel.getType()).get(ClientConstants.RESULT);
			}			
			for (String key : platformModel.getKeys()) {
				platformModel.addStatistics(key, node.get(key));
			}
		}
	}
	
	// -----------------------------------------------------------------------||
	// -- Private Methods ----------------------------------------------------||
	// -----------------------------------------------------------------------||

	private ModelNode getPlatformOperation(final ModelControllerClient client, final String type) throws IOException {
		final ModelNode operation = new ModelNode();
		operation.get(ClientConstants.OP).set(ClientConstants.READ_RESOURCE_OPERATION);
		operation.get(ClientConstants.RECURSIVE).set(true);
		operation.get(ClientConstants.INCLUDE_RUNTIME).set(true);
		operation.get(ClientConstants.OP_ADDR).add("core-service", "platform-mbean");
		operation.get(ClientConstants.OP_ADDR).add("type", type);
		return client.execute(new OperationBuilder(operation).build());
	}
}
