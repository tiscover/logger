package com.tiscover.logging;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

import com.tiscover.logging.observer.EnabledObserver;

public abstract class AbstractLoggingSocket {
	private boolean enabled = false;
	private Socket socket;
	private String host;
	private int port;
	private volatile long sendCount = 0;

	private BufferedWriter socketWriter;
	private final EnabledObserver observer;

	protected AbstractLoggingSocket(EnabledObserver observer) {
		if (observer == null) {
			throw new IllegalArgumentException("EnabledObserver must not be null");
		}
		this.observer = observer;
	}

	protected void send(String o) throws IOException {
		if (!isEnabled()) {
			return;
		}

		try {
			setUpSocket();
			if (socketWriter != null) {
				socketWriter.write(o + "\n");
				socketWriter.flush();
				sendCount++;
			}
		} catch (IOException e) {
			closeSocket();
		}
	}

	private synchronized void closeSocket() throws IOException {
		if (socketWriter != null) {
			socketWriter.close();
		}
		socketWriter = null;
		if (socket != null && !socket.isClosed()) {
			socket.close();
		}
		socket = null;
	}

	private synchronized void setUpSocket() throws IOException {
		if (socket == null || socket.isClosed() || !socket.isConnected()) {
			socket = null;
			socketWriter = null;
			initSocket();
		}
	}

	private synchronized void initSocket() throws IOException {
		if (!isEnabled()) {
			return;
		}
		sendCount = 0;
		socket = new Socket(getHost(), getPort());
		socket.setKeepAlive(true);
		socket.setSendBufferSize(1024);
		socket.setSoTimeout(2000);
		socket.setTcpNoDelay(true);
		socketWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
	}

	public boolean isEnabled() {
		return enabled && observer.isEnabled();
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public long getSendCount() {
		return sendCount;
	}
}
