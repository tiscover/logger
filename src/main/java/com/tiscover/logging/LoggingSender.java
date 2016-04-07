package com.tiscover.logging;

import java.util.TimerTask;

public class LoggingSender extends TimerTask {
	private final AbstractLoggingService service;

	public LoggingSender(AbstractLoggingService service) {
		this.service = service;
	}

	@Override
	public void run() {
		long dur = 0;
		try {
			if (!service.getSocket().isEnabled()) {
				return;
			}

			long tm = System.currentTimeMillis();

			service.executeTimerTask();

			service.setLastTimerRun(System.currentTimeMillis());
			dur = service.getLastTimerRun() - tm;
		} catch (Exception e) {

		} finally {
			if (!service.isStopped()) {
				if (dur < service.getTimerIdleTime()) {
					service.scheduleTimer(service.getTimerIdleTime() - dur);
				} else {
					service.scheduleTimer(service.getTimerShortIdleTime() - dur);
				}
			} else {
				service.stopTimer();
			}
		}
	}
}
