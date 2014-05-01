package org.rbattenfeld.statistic.dmr.ejb3;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jboss.as.controller.client.ModelControllerClient;
import org.jboss.as.controller.client.OperationBuilder;
import org.jboss.as.controller.client.helpers.ClientConstants;
import org.jboss.dmr.ModelNode;

public class Ejb3StatisticExtractor {
		
	/**
	 * This class allows to extract EJB 3 statistics via the given <code>ModelControllerClient</code> instance.
	 *  <p>
	 * The possible keys (class level statistics) are: 
	 * <ul>
	 * <li> component-class-name
	 * <li> declared-roles
	 * <li> execution-time
	 * <li> invocations
	 * <li> peak-concurrent-invocations
	 * <li> pool-available-count
	 * <li> pool-create-count
	 * <li> pool-current-size
	 * <li> pool-max-size
	 * <li> pool-name  
	 * <li> pool-remove-count
	 * <li> run-as-role
	 * <li> security-domain
	 * <li> timers
	 * <li> wait-time
	 * <li> service
	 * </ul>
	 * 
	 * @param client the connected controller
	 * @param deployment the name of the deployment
	 * @param type the EJB type
	 * @param beanName the name of the bean
	 * @param keys array of keys. These keys are class level statistics.
	 * @param array of methods to be extracted.
	 * @return
	 * @throws IOException
	 */
	public Ejb3ClassStatistic getStatistic(final ModelControllerClient client, final String deployment, final EjbType type, final String beanName, final String[] keys, final String[] methods) throws IOException {
		final Ejb3ClassStatistic ejb3Stat = new Ejb3ClassStatistic(deployment, beanName, type);
		final ModelNode operation = getEJB3ModelOperation(deployment);
		final ModelNode response = client.execute(new OperationBuilder(operation).build());
		final List<ModelNode> nodeList = response.get(ClientConstants.RESULT).get(type.getName()).asList();		
		for (final ModelNode node : nodeList) {
			if (node.hasDefined(beanName)) {
				extractClassStatistic(node, beanName, ejb3Stat, keys);				
				extractMethodsStatistic(node, beanName, ejb3Stat, methods);
			}
        }	
		return ejb3Stat; 
	}
	
	/**
	 * Returns a list of statistical details for each specified bean.
	 * @param client the connected controller
	 * @param deployment the name of the deployment
	 * @param beanList list of <code>IEjb3StatisticDetails</code>.
	 * @return
	 * @throws IOException
	 */
	public List<Ejb3ClassStatistic> getStatistic(final ModelControllerClient client, final String deployment, final List<? extends IEjb3StatisticDetails> beanList) throws IOException {
		final ModelNode operation = getEJB3ModelOperation(deployment);
		final ModelNode response = client.execute(new OperationBuilder(operation).build());
		return extractStatisticalDetails(response, deployment, beanList); 
	}
	
	//-----------------------------------------------------------------------||
	//-- Private Methods ----------------------------------------------------||
	//-----------------------------------------------------------------------||
	
	private ModelNode getEJB3ModelOperation(final String deployment) {
		final ModelNode operation = new ModelNode();
		operation.get(ClientConstants.OP).set(ClientConstants.READ_RESOURCE_OPERATION);
		operation.get(ClientConstants.RECURSIVE).set(true);
		operation.get(ClientConstants.INCLUDE_RUNTIME).set(true);
		operation.get(ClientConstants.OP_ADDR).add(ClientConstants.DEPLOYMENT, deployment);
		operation.get(ClientConstants.OP_ADDR).add(ClientConstants.SUBSYSTEM, "ejb3");
		return operation;
	}
	
	private List<Ejb3ClassStatistic> extractStatisticalDetails(final ModelNode response, final String deployment,final List<? extends IEjb3StatisticDetails> beanList) {
		final List<Ejb3ClassStatistic> statisticList = new ArrayList<Ejb3ClassStatistic>();
		for (IEjb3StatisticDetails detail : beanList) {
			try {
				final Ejb3ClassStatistic ejb3Stat = new Ejb3ClassStatistic(deployment, detail.getBeanName(), detail.getEjbType());
				final List<ModelNode> nodeList = response.get(ClientConstants.RESULT).get(detail.getEjbType().getName()).asList();		
				for (final ModelNode node : nodeList) {
					if (node.hasDefined(detail.getBeanName())) {
						extractClassStatistic(node, detail.getBeanName(), ejb3Stat, detail.getKeys());				
						extractMethodsStatistic(node, detail.getBeanName(), ejb3Stat, detail.getMethods());
						statisticList.add(ejb3Stat);
					}
		        }	
			} catch (final IllegalArgumentException ex) {
				// not called before, ignoring.
			}
			
		}
		return statisticList;
	}
	
	private void extractClassStatistic(final ModelNode node, final String beanName, final Ejb3ClassStatistic ejb3Stat, final String[] keys) {
		if (keys != null) {
			for (final String key : keys) {
				ejb3Stat.addClassStatistic(key, node.get(beanName).get(key));
			}
		} else {
			final ModelNode beanNode = node.get(beanName);
			for (org.jboss.dmr.Property prop : beanNode.asPropertyList()) {
				if (!prop.getName().equals("methods")) {
					ejb3Stat.addClassStatistic(prop.getName(), prop.getValue());
				}
			}
		}
	}
	
	private void extractMethodsStatistic(final ModelNode node, final String beanName, final Ejb3ClassStatistic ejb3Stat, final String[] methods) {
		if (methods != null) {
			final ModelNode methodNode = node.get(beanName).get("methods");		
			for (final String method : methods) {
				try {
					ejb3Stat.addMethodStatistic(method, 
							methodNode.get(method).get("execution-time").asLong(), 
							methodNode.get(method).get("invocations").asLong(), 
							methodNode.get(method).get("wait-time").asLong());
				} catch (final IllegalArgumentException ex) {
					// not called before, ignoring.
				}
			}
		}
	}
}
