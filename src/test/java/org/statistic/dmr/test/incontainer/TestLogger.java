package org.statistic.dmr.test.incontainer;

import java.io.IOException;

import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.statistic.dmr.client.DmrClient;
import org.statistic.dmr.conf.DmrStatisticConfiguration;
import org.statistic.dmr.stat.ejb3.Ejb3StatisticCSVFormatter;
import org.statistic.dmr.stat.ejb3.Ejb3StatisticUpdater;
import org.statistic.dmr.stat.platform.PlatformStatisticCSVFormatter;
import org.statistic.dmr.stat.platform.PlatformStatisticUpdater;


@Singleton
public class TestLogger {
	private static final Log _Logger = LogFactory.getLog(TestLogger.class);
	private final Ejb3StatisticCSVFormatter _ejbFormatter = new Ejb3StatisticCSVFormatter();
	private final PlatformStatisticCSVFormatter _platformFormatter = new PlatformStatisticCSVFormatter();
	private PlatformStatisticUpdater _platformStatUpdater;
	private Ejb3StatisticUpdater _ejb3StatUpdater;
	private DmrStatisticConfiguration _configurer;
	private DmrClient _client;
	private Timer _timer;

	@Resource
	private TimerService _timerService;
	
	@Timeout
	public void logStatistics(final Timer timer) {
		updateModels();
		logLine();
	}
	  
	public void startLogging() {
		try {
			_configurer = DmrStatisticConfiguration.loadFromResource("META-INF/stat.xml");
			_client = new DmrClient(true);
			_client.enableStatisticLogFile(_client.getModelController(), "statLog.log");
			_platformStatUpdater = new PlatformStatisticUpdater();
			_ejb3StatUpdater = new Ejb3StatisticUpdater(_configurer.getDeploymentName());
			updateModels();
			logHeader();
			startTimer();
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}

	public void stopLogging() {
		cleanup();
	}
	
    //-----------------------------------------------------------------------||
    //-- Private Methods ----------------------------------------------------||
    //-----------------------------------------------------------------------||
	
	
	@PreDestroy
	private void cleanup() {
		stopTimer();
		try {
			if (_client != null) {
				_client.close();
			}
		} catch (IOException ex) {
			// ignoring
		}
	}
	
	//-----------------------------------------------------------------------||
	//-- Private Methods ----------------------------------------------------||
	//-----------------------------------------------------------------------||
	
	private void startTimer() {
		final TimerConfig timerConfig = new TimerConfig();
		timerConfig.setPersistent(false);
		timerConfig.setInfo("PeriodicStatisticLogger");
		_timer = _timerService.createIntervalTimer(5000, 5000, timerConfig);
	}

	private void stopTimer() {
		if (_timer != null) {
			_timer.cancel();
		}
	}
	
	private void updateModels() {
		try {
			_platformStatUpdater.updateModel(_client.getModelController(), _configurer.getPlatformDetailsList());
			_ejb3StatUpdater.updateModel(_client.getModelController(),  _configurer.getEjbStatistics());
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}		
	}

	private void logHeader() {
		final String platformStatHeader = _platformFormatter.formatHeader(_configurer.getPlatformDetailsList());
		final String ejb3StatHeader = _ejbFormatter.formatHeader(_configurer.getEjbStatistics());
		_Logger.info(ejb3StatHeader + "," + platformStatHeader);		
	}
	
	private void logLine() {
		final String platformStatLine = _platformFormatter.formatLine(_configurer.getPlatformDetailsList());
		final String ejb3StatLine = _ejbFormatter.formatLine(_configurer.getEjbStatistics());
		_Logger.info(ejb3StatLine + "," + platformStatLine);		
	}
}
