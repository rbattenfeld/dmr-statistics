package org.statistic.dmr.logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.statistic.dmr.api.IDmrStatisticFormatter;
import org.statistic.dmr.api.IDmrStatisticUpdater;
import org.statistic.dmr.client.DmrClient;
import org.statistic.dmr.conf.DmrStatisticConfiguration;
import org.statistic.dmr.stat.ejb3.Ejb3StatisticCSVFormatter;
import org.statistic.dmr.stat.ejb3.Ejb3StatisticUpdater;
import org.statistic.dmr.stat.platform.PlatformStatisticCSVFormatter;
import org.statistic.dmr.stat.platform.PlatformStatisticUpdater;

/**
 * Abstract class implementing the boilerplate stuff for implementing an own statistical logger.
 */
public abstract class AbstractLogger {
	private static final Log _Logger = LogFactory.getLog(AbstractLogger.class);
	private final List<IDmrStatisticUpdater> _updaters = new ArrayList<>();
	private final List<IDmrStatisticFormatter<String>> _formatters = new ArrayList<IDmrStatisticFormatter<String>>();
	private Log _statisticCategoryLogger;
	private DmrStatisticConfiguration _rootModel;
	private DmrClient _client;
	
	/**
	 * Starts the timer.
	 */
	protected abstract void startTimer();
	
	/**
	 * Stops the timer.
	 */
	protected abstract void stopTimer();
	
	/**
	 * Start this statistical logger.
	 */
	public void startLogging(final String configResourcePath) {
		initAndStart(configResourcePath);
		startTimer();
	}
		
	/**
	 * Stops this statistical logger.
	 */
	public void stopLogging() {
		stopTimer();
		cleanup();
	}
	
	/**
	 * Updates the model and then logs a new line.
	 */
	public void updateAndLog() {
		updateModels();
		logLine();
	}
	
    //-----------------------------------------------------------------------||
    //-- Private Methods ----------------------------------------------------||
    //-----------------------------------------------------------------------||
	
	private void initAndStart(final String configResourcePath) {
		try {
			_rootModel = DmrStatisticConfiguration.loadFromResource(configResourcePath);
			_statisticCategoryLogger = LogFactory.getLog(_rootModel.getLogCategory());
			_client = new DmrClient(true);
			if (_rootModel.getEjbStatisticModels() != null && !_rootModel.getEjbStatisticModels().isEmpty()) {
				_updaters.add(new Ejb3StatisticUpdater(_rootModel.getDeploymentName()));
				_formatters.add(new Ejb3StatisticCSVFormatter());
			}	
			if (_rootModel.getPlatformStatisticModels() != null && !_rootModel.getPlatformStatisticModels().isEmpty()) {
				_updaters.add(new PlatformStatisticUpdater());
				_formatters.add(new PlatformStatisticCSVFormatter());
			}					
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
		
	private void updateModels() {
		try {
			for (final IDmrStatisticUpdater modelUpdater :_updaters) {
				modelUpdater.updateModel(_client.getModelController(), _rootModel);
			}
		} catch (final IOException ex) {
			throw new RuntimeException(ex);
		}		
	}

	private void logHeader() {
		final StringBuffer buf = new StringBuffer();
		for (final IDmrStatisticFormatter<String> formatter : _formatters) {
			if (buf.length() > 0) {
				buf.append(_rootModel.getCsvSeparator());
			}
			buf.append(formatter.formatHeader(_rootModel));
		}	
		logStatistics(buf.toString());	
	}
	
	private void logLine() {
		final StringBuffer buf = new StringBuffer();
		for (final IDmrStatisticFormatter<String> formatter : _formatters) {
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
