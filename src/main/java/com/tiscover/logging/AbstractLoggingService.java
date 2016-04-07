package com.tiscover.logging;

import java.io.IOException;
import java.util.Map;
import java.util.Timer;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractLoggingService {
	private long timerIdleTime = 60000;
	private long timerShortIdleTime = 1000;
	private volatile long lastTimerRun = 0;
	private boolean stop = false;

	private static Map<Class<? extends AbstractLoggingSocket>, AbstractLoggingSocket> instances = new ConcurrentHashMap<>();

	private final Timer timer;

	protected abstract void executeTimerTask() throws IOException;

	public abstract AbstractLoggingSocket getSocket();

	public AbstractLoggingService(AbstractLoggingSocket socket) {
		initService(socket);
		if (socket.isEnabled()) {
			timer = new Timer(socket.getClass().getSimpleName() + "-sender-task");
			scheduleTimer(getTimerIdleTime());
		} else {
			timer = null;
		}
	}

	protected static void disableService(Class<? extends AbstractLoggingSocket> socketClass) {
		if (socketClass == null) {
			throw new IllegalArgumentException("socketClass must not be null");
		}

		if (getSocket(socketClass) != null) {
			getSocket(socketClass).setEnabled(false);
		}
	}

	protected void initService(AbstractLoggingSocket socket) {
		if (socket == null) {
			throw new IllegalArgumentException("socket must not be null");
		}

		synchronized (AbstractLoggingSocket.class) {
			if (socket.getHost() == null || socket.getHost().isEmpty() || socket.getPort() <= 0) {
				socket.setEnabled(false);
			} else {
				socket.setEnabled(true);
			}
			setSocket(socket);
		}
	}

	@SuppressWarnings("unchecked")
	protected static <T> T getSocket(Class<T> socketClass) {
		AbstractLoggingSocket socket = instances.get(socketClass);
		if (socketClass.isInstance(socket)) {
			return (T) instances.get(socketClass);
		}
		return null;
	}

	protected static void setSocket(AbstractLoggingSocket instance) {
		AbstractLoggingService.instances.put(instance.getClass(), instance);
	}

	public void flush() {
		if (getLastTimerRun() < System.currentTimeMillis() - 3 * getTimerIdleTime()) {
			setLastTimerRun(System.currentTimeMillis());
			scheduleTimer(getTimerShortIdleTime());
		}
	}

	protected void scheduleTimer(long millisec) {
		if (!stop && timer != null) {
			timer.schedule(new LoggingSender(this), millisec);
		}
	}

	public void stopTimer() {
		stop = true;
		if (timer != null) {
			timer.cancel();
		}
	}

	public void stopAndWaitForLastTimer() {
		stop = true;
	}

	public long getLastTimerRun() {
		return lastTimerRun;
	}

	public synchronized void setLastTimerRun(long lastTimerRun) {
		this.lastTimerRun = lastTimerRun;
	}

	public long getTimerIdleTime() {
		return timerIdleTime;
	}

	public void setTimerIdleTime(long timerIdleTime) {
		this.timerIdleTime = timerIdleTime;
	}

	public long getTimerShortIdleTime() {
		return timerShortIdleTime;
	}

	public void setTimerShortIdleTime(long timerShortIdleTime) {
		this.timerShortIdleTime = timerShortIdleTime;
	}

	public static String getSocketInformation(Class<? extends AbstractLoggingSocket> socketClass) {
		StringBuilder sb = new StringBuilder();
		AbstractLoggingSocket socket = getSocket(socketClass);
		if (socket == null) {
			sb.append("Socket is null");
		} else {
			sb.append("Host: ").append(socket.getHost());
			sb.append("\nPort: ").append(socket.getPort());
			sb.append("\nisEnabled: ").append(socket.isEnabled());
			sb.append("\nnoOfStatisticPackages: ").append(socket.getSendCount());
		}
		return sb.toString();
	}

	public boolean isStopped() {
		return stop;
	}

}
