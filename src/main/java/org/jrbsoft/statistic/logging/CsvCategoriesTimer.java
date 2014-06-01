package org.jrbsoft.statistic.logging;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.EJBException;
import javax.ejb.Singleton;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;

import org.jrbsoft.statistic.model.IModelUpdater;
import org.jrbsoft.statistic.model.IRootModel;


@Singleton
public class CsvCategoriesTimer {
    private static final String _TIMER_INFO = "PeriodicStatisticLogger";
    private List<ModelLogger> _modelLoggers = null;
    private AtomicBoolean _isTimerStarted = new AtomicBoolean(false);

    @Resource
    private TimerService _timerService;

    @Timeout
    protected void onTimeout(final Timer timer) {
        for (ModelLogger modelLogger : _modelLoggers) {
            modelLogger.getCategoryLogger().logNewLine();
        }
    }

    /**
     * Starts this logger.
     */
    public void startLogging(final List<IRootModel> models, final List<IModelUpdater> updaters) {
        _modelLoggers = new ArrayList<ModelLogger>();
        for (IRootModel model : models) {
            final ModelLogger modelLogger = new ModelLogger(model);
            modelLogger.getCategoryLogger().addUpdaters(updaters);
            modelLogger.getCategoryLogger().startLogging();
            _modelLoggers.add(modelLogger);
        }
        startTimer();
    }

    /**
     * Stops this logger.
     */
    public void stopLogging() {
        stopTimer();
    }

    //-----------------------------------------------------------------------||
    //-- Private Methods ----------------------------------------------------||
    //-----------------------------------------------------------------------||

    @PreDestroy
    private void cleanup() {
        stopTimer();
    }

    private void startTimer() {
        if (!_modelLoggers.isEmpty()) {
            final TimerConfig timerConfig = new TimerConfig();
            timerConfig.setPersistent(false);
            timerConfig.setInfo(_TIMER_INFO);
            _timerService.createIntervalTimer(
                    _modelLoggers.get(0).getRootModel().getInterval(),
                    _modelLoggers.get(0).getRootModel().getInterval(),
                    timerConfig);
            _isTimerStarted.set(true);
        }
    }

    private void stopTimer() {
        if (_isTimerStarted.get()) {
            for (Timer timer : _timerService.getTimers()) {
                if (_TIMER_INFO.equals(timer.getInfo())) {
                    try {
                        timer.cancel();
                        _isTimerStarted.set(false);
                    } catch (IllegalStateException | EJBException ex) {
                        // ignore
                    }
                }
            }
        }
    }

    private class ModelLogger {
        private final CategoryLogger _categoryLogger;
        private final IRootModel _rootModel;

        public ModelLogger(final IRootModel rootModel) {
            _rootModel = rootModel;
            _categoryLogger = new CategoryLogger(_rootModel);
        }

        public CategoryLogger getCategoryLogger() {
            return _categoryLogger;
        }

        public IRootModel getRootModel() {
            return _rootModel;
        }
    }
}
