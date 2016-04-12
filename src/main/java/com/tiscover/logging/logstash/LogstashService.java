package com.tiscover.logging.logstash;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.tiscover.logging.logstash.messages.LogstashMessage;
import com.tiscover.logging.observer.DefaultEnabledObserver;
import com.tiscover.logging.observer.EnabledObserver;

public class LogstashService extends AbstractLogstashService {

	private volatile static LogstashService instance = new LogstashService();

	private List<LogstashMessage> values = new ArrayList<>();

	public LogstashService(String host, int port) {
		this(host, port, new DefaultEnabledObserver());
	}

	public LogstashService(String host, int port, EnabledObserver observer) {
		super(host, port, observer);
	}

	private LogstashService() {
		super();
	}

	public boolean isEmpty() {
		return values.isEmpty();
	}

	public void send(LogstashMessage message) {
		if (!isEnabled()) {
			return;
		}
		synchronized (values) {
			values.add(message);
		}
	}

	@Override
	protected void executeTimerTask() throws IOException {
		List<LogstashMessage> messages = values;
		values = new ArrayList<>();
		for (LogstashMessage message : messages) {
			getSocket().send(message);
		}
	}

	public static LogstashService get() {
		return instance;
	}

	public static void set(LogstashService instance) {
		if (LogstashService.instance != instance) {
			LogstashService.instance.stopTimer();
		}

		LogstashService.instance = instance;
	}
}
