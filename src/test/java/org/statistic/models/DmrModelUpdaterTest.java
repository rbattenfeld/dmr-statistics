package org.statistic.models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;

import org.jboss.as.controller.client.ModelControllerClient;
import org.jboss.as.controller.client.Operation;
import org.jboss.as.controller.client.OperationMessageHandler;
import org.jboss.dmr.ModelNode;
import org.jboss.dmr.ModelType;
import org.jboss.threads.AsyncFuture;
import org.junit.Test;

public class DmrModelUpdaterTest {

	@Test
    public void testUpdaterWithValidPathAddress() throws Exception {
		final IRootModel rootModel = IRootModel.Factory.createFromFile("src/test/resources/platform-stat-test.xml");		
        final ModelControllerClientMock clientController = getController("src/test/resources/platform-dump.txt");
        final DmrModelUpdater updater = new DmrModelUpdater(clientController);
        updater.updateModel(rootModel);
        
        for (final DmrModel model : rootModel.getDmrModels()) {        	
        	for (final DmrChildElement childElement :model.getChildElements()) {
        		if (childElement.getAbbreviation().equals("heap")) {
        			assertNodeEquals(childElement.getStatisticMap().get("init"), 300000);
        			assertNodeEquals(childElement.getStatisticMap().get("used"), 300001);
        			assertNodeEquals(childElement.getStatisticMap().get("committed"), 300002);
        			assertNodeEquals(childElement.getStatisticMap().get("max"), 300003);
        			
        			assertNodeEquals(childElement.getStatisticMap().get("initXX"), "undefined");
        			assertNodeEquals(childElement.getStatisticMap().get("usedXX"), "undefined");
        			assertNodeEquals(childElement.getStatisticMap().get("committedXX"), "undefined");
        			assertNodeEquals(childElement.getStatisticMap().get("maxXX"), "undefined");        			
        		}
        		
        		if (childElement.getAbbreviation().equals("non-heap")) {
        			assertNodeEquals(childElement.getStatisticMap().get("init"), 300004);
        			assertNodeEquals(childElement.getStatisticMap().get("used"), 300005);
        			assertNodeEquals(childElement.getStatisticMap().get("committed"), 300006);
        			assertNodeEquals(childElement.getStatisticMap().get("max"), 300007);
        		}
        		
        		if (childElement.getAbbreviation().equals("os")) {
        			assertNodeEquals(childElement.getStatisticMap().get("available-processors"), 8);
        			assertNodeEquals(childElement.getStatisticMap().get("system-load-average"), BigDecimal.valueOf(0.01));
        		}
        		
        		if (childElement.getAbbreviation().equals("invalidChilds-1")) {
        			assertNodeEquals(childElement.getStatisticMap().get("available-processors"), "undefined");
        			assertNodeEquals(childElement.getStatisticMap().get("system-load-average"), "undefined");
        		}

        		if (childElement.getAbbreviation().equals("invalidChilds-2")) {
        			assertNodeEquals(childElement.getStatisticMap().get("available-processors"), "undefined");
        			assertNodeEquals(childElement.getStatisticMap().get("system-load-average"), "undefined");
        		}
        	}
        }
    }
	
	@Test
    public void testUpdaterWithInvalidPathAddress() throws Exception {
		final IRootModel rootModel = IRootModel.Factory.createFromFile("src/test/resources/platform-stat-test-invalid-path.xml");		
        final ModelControllerClientMock clientController = getController("src/test/resources/failed-dump.txt");
        final DmrModelUpdater updater = new DmrModelUpdater(clientController);
        updater.updateModel(rootModel);
        
        for (final DmrModel model : rootModel.getDmrModels()) {        	
        	for (final DmrChildElement childElement :model.getChildElements()) {
        		if (childElement.getAbbreviation().equals("non-heap")) {
        			assertNodeEquals(childElement.getStatisticMap().get("init"), "undefined");
        			assertNodeEquals(childElement.getStatisticMap().get("used"), "undefined");
        			assertNodeEquals(childElement.getStatisticMap().get("committed"), "undefined");
        			assertNodeEquals(childElement.getStatisticMap().get("max"), "undefined");
        		}
        		
        		if (childElement.getAbbreviation().equals("os")) {
        			assertNodeEquals(childElement.getStatisticMap().get("available-processors"), "undefined");
        			assertNodeEquals(childElement.getStatisticMap().get("system-load-average"), "undefined");
        		}	
        	}
        }
    }
	
	@Test
    public void testFromJSONString() throws Exception {
        ModelNode json = ModelNode.fromJSONStream(getStream("src/test/resources/resourcestat.txt"));
        assertNotNull(json);
    }

	//-----------------------------------------------------------------------||
	//-- Private Methods ----------------------------------------------------||
	//-----------------------------------------------------------------------||	
	
	private ModelControllerClientMock getController(final String pathToDumpFile) {
		final ModelControllerClientMock clientController = new ModelControllerClientMock();
        clientController.setModelNodeResourcePath(pathToDumpFile);
        return clientController;
	}
	
	private InputStream getStream(final String path) throws FileNotFoundException {
		final File file = new File(path);
		return new FileInputStream(file);
	}

	private void assertNodeEquals(final Object obj, final Object expectedValue) {
		final ModelNode node = (ModelNode)obj;
		if (node.getType() == ModelType.UNDEFINED) {
			assertEquals("undefined", expectedValue);
		} else if (node.getType() == ModelType.BIG_DECIMAL) {
			assertEquals(node.asBigDecimal(), expectedValue);
		} else if (node.getType() == ModelType.BIG_INTEGER) {
			assertEquals(node.asBigInteger(), expectedValue);
		} else if (node.getType() == ModelType.INT) {
			assertEquals(node.asInt(), expectedValue);
		} else if (node.getType() == ModelType.DOUBLE) {
			assertEquals(node.asDouble(), expectedValue);
		} else if (node.getType() == ModelType.LONG) {
			assertEquals(node.asLong(), expectedValue);
		} else if (node.getType() == ModelType.STRING) {
			assertEquals(node.asString(), expectedValue);
		}
	}
	
	//-----------------------------------------------------------------------||
	//-- Private Classes ----------------------------------------------------||
	//-----------------------------------------------------------------------||	
	
	private class ModelControllerClientMock implements ModelControllerClient {
		private String _resourcePath;

		public void setModelNodeResourcePath(final String resourcePath) {
			_resourcePath = resourcePath;
		}
		
		@Override
		public void close() throws IOException {
		}

		@Override
		public ModelNode execute(ModelNode operation) throws IOException {
			return null;
		}

		@Override
		public ModelNode execute(Operation operation) throws IOException {
			return ModelNode.fromJSONStream(getStream(_resourcePath));
		}

		@Override
		public ModelNode execute(ModelNode operation,
				OperationMessageHandler messageHandler) throws IOException {
			return null;
		}

		@Override
		public ModelNode execute(Operation operation,
				OperationMessageHandler messageHandler) throws IOException {
			return null;
		}

		@Override
		public AsyncFuture<ModelNode> executeAsync(ModelNode operation,
				OperationMessageHandler messageHandler) {
			return null;
		}

		@Override
		public AsyncFuture<ModelNode> executeAsync(Operation operation,
				OperationMessageHandler messageHandler) {
			return null;
		}
		
	}
}
