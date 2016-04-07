package com.tiscover.logging.observer;

public class DefaultEnabledObserver implements EnabledObserver {
	private final boolean value;

	public DefaultEnabledObserver(boolean defaultValue) {
		value = defaultValue;
	}

	public DefaultEnabledObserver() {
		this(true);
	}

	@Override
	public boolean isEnabled() {
		return value;
	}
}
