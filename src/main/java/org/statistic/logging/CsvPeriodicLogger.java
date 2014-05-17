package org.statistic.logging;

import javax.annotation.Resource;
import javax.ejb.EJBException;
import javax.ejb.Singleton;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;

import org.statistic.models.IModelUpdater;
import org.statistic.models.IRootModel;


@Singleton
public class CsvPeriodicLogger extends AbstractCsvLogger {
	private static final String _TIMER_INFO = "PeriodicStatisticLogger";
	private IRootModel _rootModel = null;
	
	@Resource
	private TimerService _timerService;	
	
	@Timeout
	protected void onTimeout(final Timer timer) {
		logNewLine();
	}
	
	/**
	 * Adds a new updater.
	 */
	public void addUpdaters(final IModelUpdater updater) {
		super.addUpdaters(updater);
	}
	
	/**
	 * Starts this logger.
	 */
	public void startLogging(final IRootModel model) {
		_rootModel = model;
		initialize();
		startTimer();
	}
			
	/**
	 * Stops this logger.
	 */
	public void stopLogging() {
		stopTimer();
	}
	
	@Override
	protected IRootModel getRootModel() {
		return _rootModel;
	}

    //-----------------------------------------------------------------------||
    //-- Private Methods ----------------------------------------------------||
    //-----------------------------------------------------------------------||
	
	private void startTimer() {
		final TimerConfig timerConfig = new TimerConfig();
		timerConfig.setPersistent(false);
		timerConfig.setInfo(_TIMER_INFO);
		_timerService.createIntervalTimer(getInterval(), getInterval(), timerConfig);
	}

	private void stopTimer() {		
		for (Timer timer : _timerService.getTimers()) {
			if (_TIMER_INFO.equals(timer.getInfo())) {
				try {
					timer.cancel();
				} catch (IllegalStateException | EJBException ex) {
					// ignore
				}
			}
		}
	}
	
}
