package org.statistic.dmr.logger.ejb;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PreDestroy;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;

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


public abstract class AbstractEjb3Logger {
	private static final Log _Logger = LogFactory.getLog(AbstractEjb3Logger.class);
	private final List<IDmrStatisticUpdater> _updaters = new ArrayList<>();
	private final List<IDmrStatisticFormatter<String>> _formatters = new ArrayList<IDmrStatisticFormatter<String>>();
	private DmrStatisticConfiguration _rootModel;
	private DmrClient _client;
	private Timer _timer;
	
	public abstract TimerService getTimerService();
	public abstract long getLogInterval();
	  
	public void startLogging() {
		try {
			_rootModel = DmrStatisticConfiguration.loadFromResource("META-INF/stat.xml");
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
			startTimer();
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}

	public void stopLogging() {
		cleanup();
		logLine();
	}
	
	@Timeout
	public void onTimeout(final Timer timer) {
		updateModels();
		logLine();
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
	
	private void startTimer() {
		final TimerConfig timerConfig = new TimerConfig();
		timerConfig.setPersistent(false);
		timerConfig.setInfo("PeriodicStatisticLogger");
		_timer = getTimerService().createIntervalTimer(getLogInterval(), getLogInterval(), timerConfig);
	}

	private void stopTimer() {
		if (_timer != null) {
			_timer.cancel();
		}
	}
	
	private void updateModels() {
		try {
			for (final IDmrStatisticUpdater modelUpdater :_updaters) {
				modelUpdater.updateModel(_client.getModelController(), _rootModel);
			}
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}		
	}

	private void logHeader() {
		final StringBuffer buf = new StringBuffer();
		for (final IDmrStatisticFormatter<String> formatter : _formatters) {
			if (buf.length() > 0) {
				buf.append(',');
			}
			buf.append(formatter.formatHeader(_rootModel));
		}	
		_Logger.info(buf.toString());	
	}
	
	private void logLine() {
		final StringBuffer buf = new StringBuffer();
		for (final IDmrStatisticFormatter<String> formatter : _formatters) {
			if (buf.length() > 0) {
				buf.append(',');
			}
			buf.append(formatter.formatLine(_rootModel));
		}	
		_Logger.info(buf.toString());	
	}
	
}
