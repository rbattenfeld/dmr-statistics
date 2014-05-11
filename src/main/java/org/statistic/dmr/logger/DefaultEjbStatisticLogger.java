package org.statistic.dmr.logger;

import javax.annotation.Resource;
import javax.ejb.EJBException;
import javax.ejb.Singleton;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;


@Singleton
public class DefaultEjbStatisticLogger extends AbstractLogger {
	private static final String _TIMER_INFO = "PeriodicStatisticLogger";
	
	@Resource
	private TimerService _timerService;	
	
	@Timeout
	protected void onTimeout(final Timer timer) {
		logNewLine();
	}

	@Override
	protected void startTimer() {
		final TimerConfig timerConfig = new TimerConfig();
		timerConfig.setPersistent(false);
		timerConfig.setInfo(_TIMER_INFO);
		_timerService.createIntervalTimer(getIntervall(), getIntervall(), timerConfig);
	}

	@Override
	protected void stopTimer() {		
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
