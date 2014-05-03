package org.statistic.dmr.stat.ejb3;

import java.io.IOException;
import java.util.List;

import org.jboss.as.controller.client.ModelControllerClient;
import org.jboss.as.controller.client.OperationBuilder;
import org.jboss.as.controller.client.helpers.ClientConstants;
import org.jboss.dmr.ModelNode;
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
	
	private void update(final ModelControllerClient client, final String deployment, final List<? extends Ejb3StatisticModel> ejb3Details) throws IOException {
		for (final Ejb3StatisticModel detail : ejb3Details) {
			try {
				final ModelNode operation = getEJB3ModelOperation(deployment);
				final ModelNode response = client.execute(new OperationBuilder(operation).build());
				final List<ModelNode> nodeList = response.get(ClientConstants.RESULT).get(detail.getEjbType().getName()).asList();		
				for (final ModelNode node : nodeList) {
					if (node.hasDefined(detail.getBeanName())) {
						detail.setKeyValues(extractClassStatistics(node, detail.getBeanName(), detail.getKeys()));
						detail.setMethodValues(extractMethodsStatistics(node, detail.getBeanName(), detail.getMethods()));
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
		
	private String[] extractClassStatistics(final ModelNode node, final String beanName, final String[] keys) {
		String[] values = null;
		if (keys != null) {
			values = new String[keys.length];
			for (int i = 0; i < keys.length; i++) {
				values[i] = node.get(beanName).get(keys[i]).toString();
			}
		} else {
			final ModelNode beanNode = node.get(beanName);
			values = new String[beanNode.asPropertyList().size()];
			for (int i = 0; i < beanNode.asPropertyList().size(); i++) {
				final org.jboss.dmr.Property prop = beanNode.asPropertyList().get(i);
				if (!prop.getName().equals("methods")) {
					values[i] = prop.getValue().toString();
				}
			}
		}
		return values;
	}
	
	private String[] extractMethodsStatistics(final ModelNode node, final String beanName, final String[] methods) {
		if (methods != null) {
			final String[] methodsStatArray = new String[methods.length];
			if (methods != null) {
				final ModelNode methodNode = node.get(beanName).get("methods");		
				for (int i = 0; i < methods.length; i++) {
					try {
						methodsStatArray[i] =  String.format("%d:%d:%d", 
								methodNode.get(methods[i]).get("execution-time").asLong(), 
								methodNode.get(methods[i]).get("invocations").asLong(), 
								methodNode.get(methods[i]).get("wait-time").asLong());
					} catch (final IllegalArgumentException ex) {
						// TODO log this
						methodsStatArray[i]= "::";
					}
				}
			}
			return methodsStatArray;
		} else {
			final List<ModelNode> methodList = node.get(beanName).get("methods").asList();
			final String[] methodsStatArray = new String[methodList.size()];
			for (int i = 0; i < methodsStatArray.length; i++) {
				try {
					methodsStatArray[i] =  String.format("%d:%d:%d", 
							methodList.get(i).get("execution-time").asLong(), 
							methodList.get(i).get("invocations").asLong(), 
							methodList.get(i).get("wait-time").asLong());
				} catch (final IllegalArgumentException ex) {
					// TODO log this
					methodsStatArray[i]= "::";
				}
			}
	
			return methodsStatArray;
		}
	}

}
