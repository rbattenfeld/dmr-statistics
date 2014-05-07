package org.statistic.dmr.logger;

import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;


@Singleton
public class DefaultEjbStatisticLogger extends AbstractLogger {
	
	private Timer _timer;
	
	@Resource
	private TimerService _timerService;	
	
	@Timeout
	public void onTimeout(final Timer timer) {
		updateAndLog();
	}

	@Override
	protected void startTimer() {
		final TimerConfig timerConfig = new TimerConfig();
		timerConfig.setPersistent(false);
		timerConfig.setInfo("PeriodicStatisticLogger");
		_timer = _timerService.createIntervalTimer(5000, 5000, timerConfig);
	}

	@Override
	protected void stopTimer() {
		if (_timer != null) {
			_timer.cancel();
		}
	}
	
    //-----------------------------------------------------------------------||
    //-- Private Methods ----------------------------------------------------||
    //-----------------------------------------------------------------------||
	
	@PreDestroy
	private void cleanup() {
		stopLogging();
	}

}
