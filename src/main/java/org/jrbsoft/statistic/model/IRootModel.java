package org.jrbsoft.statistic.model;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

public interface IRootModel {

    String getDeploymentName();

    void setDeploymentName(final String deploymentName);

    void setLogCategory(final String deploymentName);

    String getLogCategory();

    long getInterval();

    void setInterval(final long interval);

    void setCsvSeparator(final char csvSeparator);

    char getCsvSeparator();

    List<IProtocolModel> getModels();

    void setModels(final List<IProtocolModel> models);

    class Factory {

        /**
         * Returns a <code>IRootModel</code> instance read from the given full path to the file.
         * @param resourceName
         * @return
         * @throws IOException
         */
        public static IRootModel createFromFile(final String fullPathToFile) throws IOException {
            final Serializer serializer = new Persister();
            try {
                return serializer.read(RootModel.class, new File(fullPathToFile));
            } catch (final Exception ex) {
                throw new IOException(ex);
            }
        }

        /**
         * Returns a <code>IRootModel</code> instance read from the given resource file.
         * @param resourceName
         * @return
         * @throws IOException
         */
        public static IRootModel createFromResource(final String resourceName) throws IOException {
            final Serializer serializer = new Persister();
            final InputStream resourceStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(resourceName);
            try {
                return serializer.read(RootModel.class, resourceStream);
            } catch (final Exception ex) {
                throw new IOException(ex);
            }
        }

        /**
         * Returns a <code>IRootModel</code> instance read from the given resource file.
         * @param resourceName
         * @return
         * @throws IOException
         */
        public static IRootModel createFromResource(final Class<? extends IRootModel> clazz, final String resourceName) throws IOException {
            final Serializer serializer = new Persister();
            final InputStream resourceStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(resourceName);
            try {
                return serializer.read(clazz, resourceStream);
            } catch (final Exception ex) {
                throw new IOException(ex);
            }
        }

        /**
         * Returns a new and empty model.
         * @return
         * @throws IOException
         */
        public static IRootModel create() throws IOException {
            return new RootModel();
        }
    }
}
