package com.tiscover.logging.logstash;

import java.io.IOException;

import com.tiscover.logging.logstash.messages.LogstashMessage;
import com.tiscover.logging.observer.DefaultEnabledObserver;
import com.tiscover.logging.observer.EnabledObserver;

public class LogstashSocket extends com.tiscover.logging.AbstractLoggingSocket {
	public LogstashSocket() {
		this(new DefaultEnabledObserver());
	}

	public LogstashSocket(EnabledObserver observer) {
		super(observer);
	}

	public void send(LogstashMessage o) throws IOException {
		send(o.toSocketMessage());
	}
}
