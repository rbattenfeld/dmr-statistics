package org.statistic.dmr.stat.platform;

import java.io.IOException;
import java.util.List;

import org.jboss.as.controller.client.ModelControllerClient;
import org.jboss.as.controller.client.OperationBuilder;
import org.jboss.as.controller.client.helpers.ClientConstants;
import org.jboss.dmr.ModelNode;
import org.statistic.dmr.api.DmrStatisticExtractor;

public class PlatformStatisticExtractor implements DmrStatisticExtractor<List<? extends PlatformStatisticModel>> {
		
	public void updateModel(final ModelControllerClient client, final List<? extends PlatformStatisticModel> platformDetails) throws IOException {
		for (final PlatformStatisticModel details : platformDetails) {
			ModelNode node = null;
			if (details.getSubType() != null && !details.getSubType().isEmpty()) {
				node = getPlatformOperation(client, details.getType()).get(ClientConstants.RESULT).get(details.getSubType());
			} else {
				node = getPlatformOperation(client, details.getType()).get(ClientConstants.RESULT);
			}
			
			final String[] values = new String[details.getKeys().length];
			for (int i = 0; i <  details.getKeys().length ; i++) {
				if (node.has(details.getKeys()[i])) {
					values[i] = node.get(details.getKeys()[i]).toString();
				}
			}			
			details.setValues(values);
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
