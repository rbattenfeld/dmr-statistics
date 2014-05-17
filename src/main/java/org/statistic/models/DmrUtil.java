package org.statistic.models;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jboss.as.controller.PathAddress;
import org.jboss.as.controller.PathElement;
import org.jboss.as.controller.client.helpers.ClientConstants;
import org.jboss.dmr.ModelNode;

public class DmrUtil {

	public static void updateStatistics(final ModelNode poolNode, final String[] keys, final Map<String, Object> valuesMap) {
		if (keys != null) {
			for (final String key : keys) {
				valuesMap.put(key, poolNode.get(key));
			}
		} 
	}
		
	public static ModelNode createOperation(final PathAddress address) {
        final  ModelNode operation = new ModelNode();
        operation.get(ClientConstants.OP).set(ClientConstants.READ_RESOURCE_OPERATION);
        operation.get(ClientConstants.RECURSIVE).set(true);
		operation.get(ClientConstants.INCLUDE_RUNTIME).set(true);
        operation.get(ClientConstants.OP_ADDR).set(address.toModelNode());
        return operation;
    }
	
	public static PathAddress createPathAddress(final String pathAddress, final String[] replacements) throws IOException {
		final List<PathElement> addressList = new ArrayList<PathElement>();
		final String[] items = pathAddress.split("/", -1);
		int replacementCounter = 0;
		for (String addressKeyValue : items) {
			final String[] adressItems = addressKeyValue.split("=", -1);
			if (adressItems.length == 2) {
				if (adressItems[1].equals("?")) {
					if (replacements != null && replacementCounter < replacements.length) {
						addressList.add(PathElement.pathElement(adressItems[0], replacements[replacementCounter++]));
					} else {
						throw new IllegalArgumentException("Invalid number of arguments");
					}
				} else {
					addressList.add(PathElement.pathElement(adressItems[0], adressItems[1].replaceAll("###", "/")));
				}
			}
		}
		return PathAddress.pathAddress(addressList);
	}
				
}
