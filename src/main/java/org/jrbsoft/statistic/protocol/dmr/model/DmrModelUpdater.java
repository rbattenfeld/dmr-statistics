package org.jrbsoft.statistic.protocol.dmr.model;

import java.io.IOException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.as.controller.PathAddress;
import org.jboss.as.controller.client.ModelControllerClient;
import org.jboss.as.controller.client.OperationBuilder;
import org.jboss.as.controller.client.helpers.ClientConstants;
import org.jboss.dmr.ModelNode;
import org.jrbsoft.statistic.model.IModelUpdater;
import org.jrbsoft.statistic.model.IProtocolModel;
import org.jrbsoft.statistic.model.IRootModel;

public class DmrModelUpdater implements IModelUpdater {
    private static final Log _Logger = LogFactory.getLog(DmrModelUpdater.class);
    private final ModelControllerClient _client;

    public DmrModelUpdater(final ModelControllerClient client) {
        _client = client;
    }

    @Override
    public void updateModel(final IRootModel rootModel) throws IOException {
        updateModels(rootModel.getModels());
    }

    public void updateModels(final List<IProtocolModel> models) throws IOException {
        if (models != null) {
            for (final IProtocolModel protocolModel : models) {
                if (protocolModel instanceof DmrModel) {
                    update((DmrModel)protocolModel);
                }
            }
        }
    }

    public void updateDmrModels(final List<DmrModel> models) throws IOException {
        if (models != null) {
            for (final DmrModel model : models) {
                update(model);
            }
        }
    }

    // -----------------------------------------------------------------------||
    // -- Private Methods ----------------------------------------------------||
    // -----------------------------------------------------------------------||

    private void update(final DmrModel model) throws IOException {
        final PathAddress address = DmrUtil.createPathAddress(model.getPathAddress());
        final ModelNode operation = DmrUtil.createOperation(address);
        final ModelNode response = _client.execute(new OperationBuilder(operation).build());
        if (response.get(ClientConstants.OUTCOME).asString().equals(ClientConstants.SUCCESS)) {
            extractValues(model, response.get(ClientConstants.RESULT));
        } else {
            _Logger.debug(ModeNodelUtil.getFailureDescription(response));
            extractValues(model, new ModelNode());
        }
    }

    private void extractValues(final DmrModel model, final ModelNode response) {
        for (final DmrChildElement pathElement : model.getChildElements()) {
            ModelNode childNode = response;
            for (final String child : pathElement.getChilds()) {
                childNode = childNode.get(child);
            }
            DmrUtil.updateStatistics(childNode, pathElement.getKeys(), pathElement.getStatisticMap());
        }

        for (final DmrPathElement childElement : model.getPathElements()) {
            final String[] items = childElement.getPathElement().split("=" , -1);
            if (items.length == 2) {
                final ModelNode result = response.asObject().get(items[0]);
                if (!result.get(ClientConstants.OUTCOME).asString().equals(ClientConstants.UNDEFINE_ATTRIBUTE_OPERATION)) {
                    DmrUtil.updateStatistics(result.get(items[1]), childElement.getKeys(), childElement.getStatisticMap());
                }
            }
        }
    }
}
