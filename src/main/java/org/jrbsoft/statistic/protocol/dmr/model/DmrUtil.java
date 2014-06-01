package org.jrbsoft.statistic.protocol.dmr.model;

import java.util.ArrayList;
import java.util.List;

import org.jboss.as.controller.PathAddress;
import org.jboss.as.controller.PathElement;
import org.jboss.as.controller.client.helpers.ClientConstants;
import org.jboss.dmr.ModelNode;
import org.jrbsoft.statistic.model.AbstractElement;

public class DmrUtil {

    public static void updateStatistics(final ModelNode poolNode, final String[] keys, final AbstractElement element) {
        if (keys != null) {
            for (final String key : keys) {
            	element.addStatistics(key, poolNode.get(key));
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

    public static PathAddress createPathAddress(final String pathAddress) {
        if (pathAddress == null || pathAddress.isEmpty() || pathAddress.length() < 3) {
            throw new IllegalArgumentException("Illegal pathAddress: cannot be null or empty, and must have a length >= 3");
        }
        final List<PathElement> addressList = new ArrayList<PathElement>();
        final String[] items = getStrippedPathAddress(pathAddress).split("/", -1);
        for (final String addressKeyValue : items) {
            final String[] adressItems = addressKeyValue.split("=", -1);
            if (adressItems.length == 2 && !adressItems[0].isEmpty() && !adressItems[1].isEmpty()) {
                addressList.add(PathElement.pathElement(adressItems[0], adressItems[1]));
            } else {
                throw new IllegalArgumentException("Illegal key value pair: " + addressKeyValue);
            }
        }
        return PathAddress.pathAddress(addressList);
    }

    //-----------------------------------------------------------------------||
    //-- Private Methods ----------------------------------------------------||
    //-----------------------------------------------------------------------||

    private static String getStrippedPathAddress(final String pathAddress) {
        if (pathAddress.startsWith("/")) {
            return pathAddress.substring(1);
        } else {
            return pathAddress;
        }
    }

}
