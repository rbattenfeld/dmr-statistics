package org.statistic.dmr.stat.datasource;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.jboss.as.controller.client.ModelControllerClient;
import org.jboss.as.controller.client.OperationBuilder;
import org.jboss.as.controller.client.helpers.ClientConstants;
import org.jboss.dmr.ModelNode;
import org.statistic.dmr.api.IDmrModel;
import org.statistic.dmr.api.IDmrStatisticUpdater;

public class DatasourceStatisticUpdater implements IDmrStatisticUpdater {		
	
	@Override
	public void updateModel(final ModelControllerClient client, final IDmrModel model) throws IOException {
		updateModel(client, model.getDataSourceStatisticModels());
	}
	
	public void updateModel(final ModelControllerClient client, final List<? extends DatasourceStatisticModel> models) throws IOException {
		for (final DatasourceStatisticModel model : models) {
			try {
				final ModelNode operation = getDatasourcesModelOperation(model.getDatasource());
				final ModelNode response = client.execute(new OperationBuilder(operation).build());
				final ModelNode statisticsNode = response.get(ClientConstants.RESULT).get("statistics");
				updateStatistics(statisticsNode.get("pool"), model.getPoolKeys(), model.getPoolStatistics());
				updateStatistics(statisticsNode.get("jdbc"), model.getJdbcKeys(), model.getJdbcStatistics());
			} catch (final IllegalArgumentException ex) {
				ex.printStackTrace(); // TODO
			}			
		}
	}
	
	//-----------------------------------------------------------------------||
	//-- Private Methods ----------------------------------------------------||
	//-----------------------------------------------------------------------||
		
	private ModelNode getDatasourcesModelOperation(final String datasourceName) {
		final ModelNode operation = new ModelNode();
		operation.get(ClientConstants.OP).set(ClientConstants.READ_RESOURCE_OPERATION);
		operation.get(ClientConstants.RECURSIVE).set(true);
		operation.get(ClientConstants.INCLUDE_RUNTIME).set(true);
		operation.get(ClientConstants.OP_ADDR).add(ClientConstants.SUBSYSTEM, "datasources");
		operation.get(ClientConstants.OP_ADDR).add("data-source", datasourceName);
		
		return operation;
	}
		
	private void updateStatistics(final ModelNode poolNode, final String[] keys, final Map<String, String> valuesMap) {
		if (keys != null) {
			for (final String key : keys) {
				valuesMap.put(key, poolNode.get(key).toString());
			}
		} else {
			for (int i = 0; i < poolNode.asPropertyList().size(); i++) {
				final org.jboss.dmr.Property prop = poolNode.asPropertyList().get(i);
				valuesMap.put(prop.getName(), prop.getValue().toString());
			}
		}
	}
		
}
