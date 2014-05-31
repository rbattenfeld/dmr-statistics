package org.jrbsoft.statistic.protocol.dmr.model;

import org.jboss.as.controller.client.helpers.ClientConstants;
import org.jboss.dmr.ModelNode;

public class ModeNodelUtil {

    public static String getFailureDescription(final ModelNode node) {
        String msg = null;
        if (!node.get(ClientConstants.OUTCOME).asString().equals(ClientConstants.SUCCESS)) {
            if (node.hasDefined(ClientConstants.FAILURE_DESCRIPTION)) {
                if (node.hasDefined(ClientConstants.OP)) {
                    msg = String.format("Operation '%s' at address '%s' failed: %s",
                            node.get(ClientConstants.OP),
                            node.get(ClientConstants.OP_ADDR),
                            node.get(ClientConstants.FAILURE_DESCRIPTION));
                } else {
                    msg = String.format("Operation failed: %s",
                            node.get(ClientConstants.FAILURE_DESCRIPTION));
                }
            } else {
                msg = String.format("Operation failed: %s", node);
            }
        }
        return msg;
    }
}
