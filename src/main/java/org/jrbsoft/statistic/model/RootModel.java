package org.jrbsoft.statistic.model;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;


@Root(name = "statistic")
class RootModel implements IRootModel {

    @Attribute(name = "deploymentName")
    private String _deploymentName;

    @Attribute(name = "logCategory")
    private String _logCategory;

    @Attribute(name = "intervall")
    private long _intervall;

    @Attribute(name = "csvSeparator")
    private char _csvSeparator;

    @ElementList(entry = "model", inline = true, required = false)
    private List<IProtocolModel> _models;

    @Override
    public String getDeploymentName() {
        return _deploymentName;
    }

    @Override
    public void setDeploymentName(final String deploymentName) {
        _deploymentName = deploymentName;
    }

    @Override
    public String getLogCategory() {
        return _logCategory;
    }

    @Override
    public void setLogCategory(final String logCategory) {
        _logCategory = logCategory;
    }

    @Override
    public char getCsvSeparator() {
        return _csvSeparator;
    }

    @Override
    public void setCsvSeparator(final char csvSeparator) {
        _csvSeparator = csvSeparator;
    }

    @Override
    public long getInterval() {
        return _intervall;
    }

    @Override
    public void setInterval(final long intervall) {
        _intervall = intervall;
    }

    public List<IProtocolModel> getModels() {
        return _models;
    }

    public void setModels(final List<IProtocolModel> models) {
        _models = models;
    }

    /**
     * Returns a <code>DmrStatisticConfiguration</code> instance read from the given resource file.
     * @param resourceName
     * @return
     * @throws IOException
     */
    public static RootModel loadFromResource(final String resourceName) throws IOException {
        final Serializer serializer = new Persister();
        final InputStream resourceStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(resourceName);
        try {
            return serializer.read(RootModel.class, resourceStream);
        } catch (final Exception ex) {
            throw new IOException(ex);
        }
    }
}
