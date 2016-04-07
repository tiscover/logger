package com.tiscover.logging.logstash;

import com.tiscover.logging.AbstractLoggingService;
import com.tiscover.logging.DummySocket;
import com.tiscover.logging.observer.EnabledObserver;

public abstract class AbstractLogstashService extends AbstractLoggingService {

	public AbstractLogstashService(String host, int port, EnabledObserver observer) {
		super(generateSocket(host, port, observer));
	}

	public AbstractLogstashService() {
		super(new DummySocket());
	}

	private static LogstashSocket generateSocket(String host, int port, EnabledObserver observer) {
		LogstashSocket socket = new LogstashSocket(observer);
		socket.setHost(host);
		socket.setPort(port);
		return socket;
	}

	@Override
	public LogstashSocket getSocket() {
		return getSocket(LogstashSocket.class);
	}

	public static void disableService() {
		disableService(LogstashSocket.class);
	}
}
