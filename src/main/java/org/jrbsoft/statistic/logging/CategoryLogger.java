package org.jrbsoft.statistic.logging;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jrbsoft.statistic.model.IModelUpdater;
import org.jrbsoft.statistic.model.IRootModel;

/**
 * Abstract class implementing the boilerplate stuff for implementing an own statistic logger.
 */
public class CategoryLogger {
    private final List<IModelUpdater> _updaters = new ArrayList<>();
    private final IRootModel _rootModel;
    private IFormatter<String> _csvFormatter = new CsvFormatter();
    private Log _categoryLogger;

    //-----------------------------------------------------------------------||
    //-- Public Methods -----------------------------------------------------||
    //-----------------------------------------------------------------------||

    /**
     *  Returns the model.
     */
    public CategoryLogger(final IRootModel rootModel) {
        _rootModel = rootModel;
        _categoryLogger = LogFactory.getLog(_rootModel.getLogCategory());
    }

    /**
     * Set an external formatter. If not set, the default <code>CsvFormatter</code> is used for formatting the logs.
     * @param formatter
     */
    public void setFormatter(final IFormatter<String> formatter) {
        _csvFormatter = formatter;
    }

    /**
     * Adds a new updater instance to the updater list.
     * @param updater
     */
    public void addUpdaters(final IModelUpdater updater) {
        _updaters.add(updater);
    }

    /**
     * Adds a new updater instance to the updater list.
     * @param updater
     */
    public void addUpdaters(final List<IModelUpdater> updaters) {
        _updaters.addAll(updaters);
    }

    /**
     * Updates the model and then logs a new line.
     */
    public void logNewLine() {
        updateModels();
        logLine();
    }

    /**
     * Updates the model with all registered updaters.
     */
    public void updateModels() {
        try {
            for (final IModelUpdater modelUpdater :_updaters) {
                modelUpdater.updateModel(_rootModel);
            }
        } catch (final IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Returns the desired log interval.
     * @return
     */
    public long getInterval() {
        return _rootModel.getInterval();
    }

    public void startLogging() {
        updateModels();
        logHeader();
    }

    //-----------------------------------------------------------------------||
    //-- Private Methods ----------------------------------------------------||
    //-----------------------------------------------------------------------||

    private void logHeader() {
        final StringBuffer buf = new StringBuffer();
        if (buf.length() > 0) {
            buf.append(_rootModel.getCsvSeparator());
        }
        buf.append(_csvFormatter.formatHeader(_rootModel));
        logStatistics(buf.toString());
    }

    private void logLine() {
        final StringBuffer buf = new StringBuffer();
        if (buf.length() > 0) {
            buf.append(_rootModel.getCsvSeparator());
        }
        buf.append(_csvFormatter.formatLine(_rootModel));
        logStatistics(buf.toString());
    }

    private void logStatistics(final String line) {
        if (_categoryLogger != null) {
            _categoryLogger.info(line);
        } else {
            throw new UnsupportedOperationException("This logger is not initialized!");
        }
    }
}
