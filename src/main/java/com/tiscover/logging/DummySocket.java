package com.tiscover.logging;

import com.tiscover.logging.observer.DefaultEnabledObserver;

public class DummySocket extends AbstractLoggingSocket {

	public DummySocket() {
		super(new DefaultEnabledObserver(false));
	}

}
