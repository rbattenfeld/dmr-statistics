package org.rbattenfeld.statistic.dmr.platform;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jboss.as.controller.client.ModelControllerClient;
import org.jboss.as.controller.client.OperationBuilder;
import org.jboss.as.controller.client.helpers.ClientConstants;
import org.jboss.dmr.ModelNode;

public class PlatformStatistics {
	
	private Map<String, ModelNode> _nodeMap = new HashMap<String, ModelNode>();
	
	/**
	 * "operating-system" : {
     * "  name" : "Linux",
     * "  arch" : "amd64",
     * "  version" : "3.13.9-200.fc20.x86_64",
     * "  available-processors" : 8,
     * "  system-load-average" : 0.25,
     * "  object-name" : "java.lang:type=OperatingSystem"
     * "},
	 */
	private ModelNode _osNode;
	
	/**
	 * "heap-memory-usage" : {
     * "  init" : 67108864,
     * "  used" : 127838680,
     * "  committed" : 201326592,
     * "  max" : 477102080
     * "},
	 */
	private ModelNode _heapMemoryNode;
	
	/**
	 * "non-heap-memory-usage" : {
     * "  init" : 67108864,
     * "  used" : 127838680,
     * "  committed" : 201326592,
     * "  max" : 477102080
     * "},
	 */
	private ModelNode _nonHeapMemoryNode;
	
	/**
	 * "threading" : {
     * "  all-thread-ids" : [
     * "    365, .....
     * "  ],
     * "  thread-contention-monitoring-supported" : true,
     * "  thread-cpu-time-supported" : true,
     * "  current-thread-cpu-time-supported" : true,
     * "  object-monitor-usage-supported" : true,
     * "  synchronizer-usage-supported" : true,
     * "  thread-contention-monitoring-enabled" : false,
     * "  thread-cpu-time-enabled" : true,
     * "  thread-count" : 218,
     * "  peak-thread-count" : 218,
     * "  total-started-thread-count" : 354,
     * "  daemon-thread-count" : 17,
     * "  current-thread-cpu-time" : 13075417,
     * "  current-thread-user-time" : 10000000,
     * "  object-name" : "java.lang:type=Threading"
     * "},
	 */
	private ModelNode _threading;	

	void setHeapMemory(final ModelNode node) {
		_heapMemoryNode = node;
	}
	
	void setNonHeapMemory(final ModelNode node) {
		_nonHeapMemoryNode = node;
	}
	
	void setOS(final ModelNode node) {
		_osNode = node;
	}
	
	void setThreading(final ModelNode node) {
		_threading = node;
	}
	
	public void addNode() {
		
	}
	
	public static PlatformStatistics getDefaultStatistics(final ModelControllerClient client, final List<? extends IPlatformStatisticDetails> platformDetails) throws IOException {
		final PlatformStatistics stat = new PlatformStatistics();		
		for (final IPlatformStatisticDetails details : platformDetails) {
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
		
		
		stat.setHeapMemory(getPlatformOperation(client, "memory").get(ClientConstants.RESULT).get("heap-memory-usage"));
		stat.setNonHeapMemory(getPlatformOperation(client, "memory").get(ClientConstants.RESULT).get("non-heap-memory-usage"));
		stat.setOS(getPlatformOperation(client, "operating-system").get(ClientConstants.RESULT));
		stat.setThreading(getPlatformOperation(client, "threading").get(ClientConstants.RESULT));		
		return stat;
	}
	
	// -----------------------------------------------------------------------||
	// -- Private Methods ----------------------------------------------------||
	// -----------------------------------------------------------------------||

	private static ModelNode getPlatformOperation(final ModelControllerClient client, final String type) throws IOException {
		final ModelNode operation = new ModelNode();
		operation.get(ClientConstants.OP).set(ClientConstants.READ_RESOURCE_OPERATION);
		operation.get(ClientConstants.RECURSIVE).set(true);
		operation.get(ClientConstants.INCLUDE_RUNTIME).set(true);
		operation.get(ClientConstants.OP_ADDR).add("core-service", "platform-mbean");
		operation.get(ClientConstants.OP_ADDR).add("type", type);
		return client.execute(new OperationBuilder(operation).build());
	}
}
