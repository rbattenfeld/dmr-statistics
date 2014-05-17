package org.statistic.models;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

public interface IRootModel {
	
	public String getDeploymentName();

	public void setDeploymentName(final String deploymentName);
	
	public void setLogCategory(final String deploymentName);
	
	public String getLogCategory();
	
	public long getIntervall();

	public void setIntervall(final long intervall);

	public void setCsvSeparator(final char csvSeparator);

	public char getCsvSeparator();

	public List<DmrModel> getDmrModels();

	public void setDmrModels(final List<DmrModel> dmrModels);
	
	public List<MBeanModel> getMBeanModels();

	public void setMBeanModels(final List<MBeanModel> mbeanModels);
	
    class Factory {
		
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
