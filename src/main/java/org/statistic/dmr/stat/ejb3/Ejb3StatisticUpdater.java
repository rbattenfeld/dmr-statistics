package org.statistic.dmr.stat.ejb3;

import java.io.IOException;
import java.util.List;

import org.jboss.as.controller.client.ModelControllerClient;
import org.jboss.as.controller.client.OperationBuilder;
import org.jboss.as.controller.client.helpers.ClientConstants;
import org.jboss.dmr.ModelNode;
import org.jboss.dmr.Property;
import org.statistic.dmr.api.IDmrStatisticUpdater;

public class Ejb3StatisticUpdater implements IDmrStatisticUpdater<List<? extends Ejb3StatisticModel>> {		
	
	private final String _deployment;
	
	public Ejb3StatisticUpdater(final String deployment) {
		_deployment = deployment;
	}

	@Override
	public void updateModel(ModelControllerClient client, List<? extends Ejb3StatisticModel> source) throws IOException {
		update(client, _deployment, source);
	}
	
	//-----------------------------------------------------------------------||
	//-- Private Methods ----------------------------------------------------||
	//-----------------------------------------------------------------------||
	
	private void update(final ModelControllerClient client, final String deployment, final List<? extends Ejb3StatisticModel> models) throws IOException {
		for (final Ejb3StatisticModel model : models) {
			try {
				final ModelNode operation = getEJB3ModelOperation(deployment);
				final ModelNode response = client.execute(new OperationBuilder(operation).build());
				final List<ModelNode> nodeList = response.get(ClientConstants.RESULT).get(model.getEjbType().getName()).asList();		
				for (final ModelNode node : nodeList) {
					if (node.hasDefined(model.getBeanName())) {
						updateClassStatistics(node, model);
						updateMethodsStatistics(node, model);
					}
		        }	
			} catch (final IllegalArgumentException ex) {
				// not called before, ignoring.
			}			
		}
	}
	
	private static ModelNode getEJB3ModelOperation(final String deployment) {
		final ModelNode operation = new ModelNode();
		operation.get(ClientConstants.OP).set(ClientConstants.READ_RESOURCE_OPERATION);
		operation.get(ClientConstants.RECURSIVE).set(true);
		operation.get(ClientConstants.INCLUDE_RUNTIME).set(true);
		operation.get(ClientConstants.OP_ADDR).add(ClientConstants.DEPLOYMENT, deployment);
		operation.get(ClientConstants.OP_ADDR).add(ClientConstants.SUBSYSTEM, "ejb3");
		return operation;
	}
		
	private void updateClassStatistics(final ModelNode node, final Ejb3StatisticModel model) {
		if (model.getKeys() != null) {
			for (final String key : model.getKeys()) {
				model.addClassStatistics(key, node.get(model.getBeanName()).get(key).toString());
			}
		} else {
			final ModelNode beanNode = node.get(model.getBeanName());
			for (int i = 0; i < beanNode.asPropertyList().size(); i++) {
				final org.jboss.dmr.Property prop = beanNode.asPropertyList().get(i);
				if (!prop.getName().equals("methods")) {
					model.addClassStatistics(prop.getName(), prop.getValue().toString());
				}
			}
		}
	}
	
	private void updateMethodsStatistics(final ModelNode node, final Ejb3StatisticModel model) {
		if (model.getMethods() != null) {			
			final ModelNode methodNode = node.get(model.getBeanName()).get("methods");		
			for (final String methodName : model.getMethods()) {
				try {
					model.addMethodStatistics(methodName, 
						methodNode.get(methodName).get(Ejb3MethodStatistics.EXECUTION_TIME).asString(), 
						methodNode.get(methodName).get(Ejb3MethodStatistics.INVOCATIONS).asString(), 
						methodNode.get(methodName).get(Ejb3MethodStatistics.WAIT_TIME).asString());
				} catch (final IllegalArgumentException ex) {
					// TODO log this
					model.addMethodStatistics(methodName, "", "", "");
				}
			}			
		} else {
			final List<Property> methodList = node.get(model.getBeanName()).get("methods").asPropertyList();
			for (final Property prop : methodList) {
				final String methodName = prop.getName();
				try {					
					model.addMethodStatistics(methodName, 
							prop.getValue().get(Ejb3MethodStatistics.EXECUTION_TIME).asString(), 
							prop.getValue().get(Ejb3MethodStatistics.INVOCATIONS).asString(), 
							prop.getValue().get(Ejb3MethodStatistics.WAIT_TIME).asString());
				} catch (final IllegalArgumentException ex) {
					// TODO log this
					model.addMethodStatistics(methodName, "", "", "");
				}
			}
		}
	}

}
