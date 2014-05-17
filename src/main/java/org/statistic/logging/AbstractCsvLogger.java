package org.statistic.logging;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.statistic.models.IModelUpdater;
import org.statistic.models.IRootModel;

/**
 * Abstract class implementing the boilerplate stuff for implementing an own statistic logger.
 */
public abstract class AbstractCsvLogger {
	private final List<IModelUpdater> _updaters = new ArrayList<>();
	private final ICsvFormatter<String> _csvFormatter = new CsvFormatter();
	private Log _statisticCategoryLogger;
		
    //-----------------------------------------------------------------------||
    //-- Abstract Methods ---------------------------------------------------||
    //-----------------------------------------------------------------------||
	
	/**
	 *  Returns the model.
	 */
	protected abstract IRootModel getRootModel();
	
    //-----------------------------------------------------------------------||
    //-- Protected Methods --------------------------------------------------||
    //-----------------------------------------------------------------------||
	
	/** 
	 * Adds a new updater instance to the updater list.
	 * @param updater
	 */
	protected void addUpdaters(final IModelUpdater updater) {
		_updaters.add(updater);
	}
	
	/**
	 * Updates the model and then logs a new line.
	 */
	protected void logNewLine() {
		updateModels();
		logLine();
	}
		
	/**
	 * Updates the model with all registered updaters.
	 */
	protected void updateModels() {
		try {
			for (final IModelUpdater modelUpdater :_updaters) {
				modelUpdater.updateModel(getRootModel());
			}
		} catch (final IOException ex) {
			throw new RuntimeException(ex);
		}		
	}
	
	/**
	 * Returns the desired log interval.
	 * @return
	 */
	protected long getInterval() {
		return getRootModel().getIntervall();
	}
	
	protected void initialize() {
		_statisticCategoryLogger = LogFactory.getLog(getRootModel().getLogCategory());
		updateModels();
		logHeader();
	}
	
    //-----------------------------------------------------------------------||
    //-- Private Methods ----------------------------------------------------||
    //-----------------------------------------------------------------------||
		
	private void logHeader() {
		final StringBuffer buf = new StringBuffer();
		if (buf.length() > 0) {
			buf.append(getRootModel().getCsvSeparator());
		}
		buf.append(_csvFormatter.formatHeader(getRootModel()));
		logStatistics(buf.toString());	
	}
	
	private void logLine() {
		final StringBuffer buf = new StringBuffer();
		if (buf.length() > 0) {
			buf.append(getRootModel().getCsvSeparator());
		}
		buf.append(_csvFormatter.formatLine(getRootModel()));
		logStatistics(buf.toString());	
	}
		
	private void logStatistics(final String line) {
		if ( _statisticCategoryLogger != null) {
			_statisticCategoryLogger.info(line);
		} else {
			throw new UnsupportedOperationException("This logger is not initialized!");
		}
	}
}
