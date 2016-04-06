package com.tiscover.logging.graphite;

public class GraphiteDataPoint {
	private final String name;
	private final long timestamp;
	private final double value;

	public GraphiteDataPoint(String name, double value) {
		this.name = name;
		this.value = value;
		timestamp = System.currentTimeMillis();
	}

	public String getName() {
		return name;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public double getValue() {
		return value;
	}
}
