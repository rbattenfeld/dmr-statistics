package org.statistic.models;

import java.io.IOException;
import java.util.List;

import org.jboss.as.controller.PathAddress;
import org.jboss.as.controller.client.ModelControllerClient;
import org.jboss.as.controller.client.OperationBuilder;
import org.jboss.as.controller.client.helpers.ClientConstants;
import org.jboss.dmr.ModelNode;

public class DmrModelUpdater implements IModelUpdater {
	private final ModelControllerClient _client; 
	
	public DmrModelUpdater(final ModelControllerClient client) {
		_client = client;
	}
	
	@Override
	public void updateModel(final IRootModel rootModel) throws IOException {
		updateModel(rootModel.getDmrModels());
	}
	
	public void updateModel(final List<DmrModel> models) throws IOException {
		for (final DmrModel model : models) {
			final PathAddress address = DmrUtil.createPathAddress(model.getPathAddress(), new String[] {});
			final ModelNode operation = DmrUtil.createOperation(address);
			final ModelNode response = _client.execute(new OperationBuilder(operation).build());
			if (response.get(ClientConstants.OUTCOME).asString().equals(ClientConstants.SUCCESS)) {
				extractValues(model, response.get(ClientConstants.RESULT));
			}
		}
	}
	
	// -----------------------------------------------------------------------||
	// -- Private Methods ----------------------------------------------------||
	// -----------------------------------------------------------------------||

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
		
//		for (final MBeanElement mbeanElement : model.getMBeanElements()) {
//			ModelNode childNode = response;
//			for (final String child : mbeanElement.getMBeanElement().getChilds()) {
//				childNode = childNode.get(child);
//			}
//			DmrUtil.updateStatistics(childNode, pathElement.getKeys(), pathElement.getStatisticMap());
//		}
	}
}
