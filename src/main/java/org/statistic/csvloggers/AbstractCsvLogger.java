package org.statistic.csvloggers;

import java.io.IOException;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.statistic.connectors.IDmrControllerClient;
import org.statistic.models.IModelUpdater;
import org.statistic.models.IRootModel;

/**
 * Abstract class implementing the boilerplate stuff for implementing an own statistical logger.
 */
public abstract class AbstractCsvLogger {
	private static final Log _Logger = LogFactory.getLog(AbstractCsvLogger.class);
//	private final List<IModelUpdater> _updaters = new ArrayList<>();
//	private final List<ICsvFormatter<String>> _formatters = new ArrayList<ICsvFormatter<String>>();
	private Log _statisticCategoryLogger;
	private IRootModel _rootModel;
	private IDmrControllerClient _client; // TODO
	
	@Inject
	private Instance<IModelUpdater> _updaters; // todo producer needed!
	
	@Inject @Csv
	private Instance<ICsvFormatter<String>> _formatters;
	
	/**
	 * Start this statistical logger.
	 */
	public void startLogging(final String host, final int port, final String configResourcePath) {
		initialize(host, port, configResourcePath);
		startTimer();
	}
		
	/**
	 * Stops this statistical logger.
	 */
	public void stopLogging() {
		stopTimer();
		cleanup();
	}
	
    //-----------------------------------------------------------------------||
    //-- Protected Methods --------------------------------------------------||
    //-----------------------------------------------------------------------||

	/**
	 * Starts the timer.
	 */
	protected abstract void startTimer();
	
	/**
	 * Stops the timer.
	 */
	protected abstract void stopTimer();
	
	/**
	 * Updates the model and then logs a new line.
	 */
	protected void logNewLine() {
		checkState();
		updateModels();
		logLine();
	}
	
	protected long getIntervall() {
		checkState();
		return _rootModel.getIntervall();
	}
	
	protected void updateModels() {
		checkState();
		try {
			for (final IModelUpdater modelUpdater :_updaters) {
				modelUpdater.updateModel(_rootModel);
			}
		} catch (final IOException ex) {
			throw new RuntimeException(ex);
		}		
	}
	
    //-----------------------------------------------------------------------||
    //-- Private Methods ----------------------------------------------------||
    //-----------------------------------------------------------------------||
	
	private void checkState() {
		if (_client == null) {
			throw new UnsupportedOperationException("Not started");
		}
	}
	
	private void initialize(final String host, final int port, final String configResourcePath) {
		try {
			_rootModel = IRootModel.Factory.createFromResource(configResourcePath);
			_statisticCategoryLogger = LogFactory.getLog(_rootModel.getLogCategory());
			_client = IDmrControllerClient.Factory.create(host, port);
//			if (_rootModel.getDmrModels() != null && !_rootModel.getDmrModels().isEmpty()) {
//				_updaters.add(new DmrModelUpdater());
//				_formatters.add(new CsvFormatter());
//			}	
			updateModels();
			logHeader();
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}
	
	private void cleanup() {
		try {
			if (_client != null) {
				_client.close();
			}
		} catch (final IOException ex) {
			// ignoring
		}
	}
	
	private void logHeader() {
		final StringBuffer buf = new StringBuffer();
		for (final ICsvFormatter<String> formatter : _formatters) {
			if (buf.length() > 0) {
				buf.append(_rootModel.getCsvSeparator());
			}
			buf.append(formatter.formatHeader(_rootModel));
		}	
		logStatistics(buf.toString());	
	}
	
	private void logLine() {
		final StringBuffer buf = new StringBuffer();
		for (final ICsvFormatter<String> formatter : _formatters) {
			if (buf.length() > 0) {
				buf.append(_rootModel.getCsvSeparator());
			}
			buf.append(formatter.formatLine(_rootModel));
		}	
		logStatistics(buf.toString());	
	}
	
	private void logStatistics(final String line) {
		if ( _statisticCategoryLogger != null) {
			_statisticCategoryLogger.info(line);
		} else {
			_Logger.error("This instance is not started!");
		}
	}
}
