package org.jrbsoft.statistic.junit;

import static org.junit.Assert.assertEquals;

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
import org.jboss.threads.AsyncFuture;
import org.jrbsoft.statistic.model.IProtocolModel;
import org.jrbsoft.statistic.model.IRootModel;
import org.jrbsoft.statistic.protocol.dmr.model.DmrChildElement;
import org.jrbsoft.statistic.protocol.dmr.model.DmrModel;
import org.jrbsoft.statistic.protocol.dmr.model.DmrModelUpdater;
import org.junit.Test;

public class DmrModelUpdaterTest {

    @Test
    public void testUpdaterWithValidPathAddress() throws Exception {
        final IRootModel rootModel = IRootModel.Factory.createFromFile("src/test/resources/platform-stat-test.xml");        
        final ModelControllerClientMock clientController = getController("src/test/resources/platform-dump.txt");
        final DmrModelUpdater updater = new DmrModelUpdater(clientController);
        updater.updateModel(rootModel);
        

        for (final IProtocolModel protocolModel : rootModel.getModels()) {            
            if (protocolModel instanceof DmrModel) {
                final DmrModel model = (DmrModel)protocolModel;
                for (final DmrChildElement childElement :model.getChildElements()) {
                    if (childElement.getAbbreviation().equals("heap")) {
                    	assertEquals(childElement.getCurrentValue("init"), 300000);
                        assertEquals(childElement.getCurrentValue("used"), 300001);
                        assertEquals(childElement.getCurrentValue("committed"), 300002);
                        assertEquals(childElement.getCurrentValue("max"), 300003);
                        
                        assertEquals(childElement.getCurrentValue("initXX"), "undefined");
                        assertEquals(childElement.getCurrentValue("usedXX"), "undefined");
                        assertEquals(childElement.getCurrentValue("committedXX"), "undefined");
                        assertEquals(childElement.getCurrentValue("maxXX"), "undefined");                    
                    }
                    
                    if (childElement.getAbbreviation().equals("non-heap")) {
                        assertEquals(childElement.getCurrentValue("init"), 300004);
                        assertEquals(childElement.getCurrentValue("used"), 300005);
                        assertEquals(childElement.getCurrentValue("committed"), 300006);
                        assertEquals(childElement.getCurrentValue("max"), 300007);
                    }
                    
                    if (childElement.getAbbreviation().equals("os")) {
                        assertEquals(childElement.getCurrentValue("available-processors"), 8);
                        assertEquals(childElement.getCurrentValue("system-load-average"), BigDecimal.valueOf(0.01));
                    }
                    
                    if (childElement.getAbbreviation().equals("invalidChilds-1")) {
                        assertEquals(childElement.getCurrentValue("available-processors"), "undefined");
                        assertEquals(childElement.getCurrentValue("system-load-average"), "undefined");
                    }
    
                    if (childElement.getAbbreviation().equals("invalidChilds-2")) {
                        assertEquals(childElement.getCurrentValue("available-processors"), "undefined");
                        assertEquals(childElement.getCurrentValue("system-load-average"), "undefined");
                    }
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

        for (final IProtocolModel protocolModel : rootModel.getModels()) {            
            if (protocolModel instanceof DmrModel) {
                final DmrModel model = (DmrModel)protocolModel;
                for (final DmrChildElement childElement : model.getChildElements()) {                    
                    if (childElement.getAbbreviation().equals("non-heap")) {
                        assertEquals(childElement.getCurrentValue("init"), "undefined");
                        assertEquals(childElement.getCurrentValue("used"), "undefined");
                        assertEquals(childElement.getCurrentValue("committed"), "undefined");
                        assertEquals(childElement.getCurrentValue("max"), "undefined");
                    }

                    if (childElement.getAbbreviation().equals("os")) {
                        assertEquals(childElement.getCurrentValue("available-processors"), "undefined");
                        assertEquals(childElement.getCurrentValue("system-load-average"), "undefined");
                    }    
                }
            }
        }
    }
    
//    @Test
//    public void testFromJSONString() throws Exception {
//        ModelNode json = ModelNode.fromJSONStream(getStream("src/test/resources/resourcestat.txt"));
//        assertNotNull(json);
//    }

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
