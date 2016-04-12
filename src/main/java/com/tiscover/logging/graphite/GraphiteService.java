package com.tiscover.logging.graphite;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.tiscover.logging.graphite.path.BasePathFactory;
import com.tiscover.logging.graphite.path.DefaultBasePathFactory;
import com.tiscover.logging.observer.DefaultEnabledObserver;
import com.tiscover.logging.observer.EnabledObserver;

public class GraphiteService extends AbstractGraphiteService {

	private volatile static GraphiteService instance = new GraphiteService();

	private Map<String, List<GraphiteDataPoint>> values = new ConcurrentHashMap<>();
	private Map<String, Double> maxValues = new ConcurrentHashMap<>();
	private Map<String, Double> sumValues = new ConcurrentHashMap<>();
	private Map<String, Double> minValues = new ConcurrentHashMap<>();

	public GraphiteService(String host, int port, EnabledObserver observer, BasePathFactory factory) {
		super(host, port, observer, factory);
	}

	public GraphiteService(String host, int port, BasePathFactory factory) {
		this(host, port, new DefaultEnabledObserver(), factory);
	}

	public GraphiteService(String host, int port, EnabledObserver observer) {
		this(host, port, observer, new DefaultBasePathFactory());
	}

	public GraphiteService(String host, int port) {
		this(host, port, new DefaultBasePathFactory());
	}

	private GraphiteService() {
		super();
	}

	public boolean isEmpty() {
		return maxValues.isEmpty() && sumValues.isEmpty() && values.isEmpty();
	}

	public void sendMax(String path, double value) {
		if (!isEnabled()) {
			return;
		}
		Double max = maxValues.get(getGraphiteBasePath() + path);

		if (max == null || max < value) {
			maxValues.put(getGraphiteBasePath() + path, value);
		}
	}

	public void sendMin(String path, double val) {
		if (!isEnabled()) {
			return;
		}
		Double min = minValues.get(getGraphiteBasePath() + path);
		if (min == null || min > val) {
			minValues.put(getGraphiteBasePath() + path, val);
		}
	}

	/*
	 * summarizes value in one minute
	 */
	public void sendMinuteSum(String path, double value) {
		if (!isEnabled()) {
			return;
		}

		Double sum = sumValues.get(getGraphiteBasePath() + path);
		if (sum == null) {
			sumValues.put(getGraphiteBasePath() + path, value);
		} else {
			sumValues.put(getGraphiteBasePath() + path, sum + value);
		}
	}

	public void send(String path, double value) {
		if (!isEnabled()) {
			return;
		}
		if (values.get(getGraphiteBasePath() + path) == null) {
			values.put(getGraphiteBasePath() + path, new ArrayList<GraphiteDataPoint>());
		}
		values.get(getGraphiteBasePath() + path).add(new GraphiteDataPoint(getGraphiteBasePath() + path, value));
	}

	@Override
	protected void executeTimerTask() throws IOException {
		Map<String, Double> maxValTmp = maxValues;
		maxValues = new ConcurrentHashMap<>();
		long tm = System.currentTimeMillis();

		if (getSocket() instanceof GraphiteSocket) {
			for (Map.Entry<String, Double> e : maxValTmp.entrySet()) {
				getSocket().send(e.getKey(), e.getValue(), tm);
			}

			Map<String, Double> sumValTmp = sumValues;
			sumValues = new ConcurrentHashMap<>();
			for (Map.Entry<String, Double> e : sumValTmp.entrySet()) {
				getSocket().send(e.getKey(), e.getValue(), tm);
			}

			Map<String, Double> minValTmp = minValues;
			minValues = new ConcurrentHashMap<>();
			for (Map.Entry<String, Double> e : minValTmp.entrySet()) {
				getSocket().send(e.getKey(), e.getValue(), tm);
			}

			Map<String, List<GraphiteDataPoint>> m = values;
			values = new ConcurrentHashMap<>();
			for (Map.Entry<String, List<GraphiteDataPoint>> e : m.entrySet()) {
				for (GraphiteDataPoint d : e.getValue()) {
					getSocket().send(d);
				}
			}
		}

	}

	public static GraphiteService get() {
		return instance;
	}

	public static void set(GraphiteService instance) {
		if (GraphiteService.instance != instance) {
			GraphiteService.instance.stopTimer();
		}

		GraphiteService.instance = instance;
	}
}
