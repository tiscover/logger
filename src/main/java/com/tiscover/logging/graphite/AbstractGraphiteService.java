package com.tiscover.logging.graphite;

import java.io.IOException;

import com.tiscover.logging.AbstractLoggingService;
import com.tiscover.logging.DummySocket;
import com.tiscover.logging.graphite.path.BasePathFactory;
import com.tiscover.logging.observer.EnabledObserver;

public abstract class AbstractGraphiteService extends AbstractLoggingService {

    private static ThreadLocal<String> graphitePath = new ThreadLocal<>();

    public AbstractGraphiteService(String host, int port, EnabledObserver observer, BasePathFactory factory) {
        super(generateSocket(host, port, observer, factory));
    }

    public AbstractGraphiteService() {
        super(new DummySocket());
    }

    private static GraphiteSocket generateSocket(String host, int port, EnabledObserver observer, BasePathFactory factory) {
        GraphiteSocket socket = new GraphiteSocket(observer);
        socket.setHost(host);
        socket.setPort(port);
        socket.setBasePathFactory(factory);
        return socket;
    }

    public static void setGraphiteBasePath(String... pathEntries) {
        StringBuilder path = new StringBuilder();
        if (pathEntries != null) {
            for (String entry : pathEntries) {
                if (entry != null && !entry.isEmpty()) {
                    path.append(entry).append(".");
                }
            }
        }
        graphitePath.set(path.toString());
    }

    public static String getGraphiteBasePath() {
        String path = graphitePath.get();
        if (path == null || path.isEmpty()) {
            return "";
        }
        return path;
    }

    @Override
    public GraphiteSocket getSocket() throws IOException {
        return getSocket(GraphiteSocket.class);
    }

    public static void disableService() {
        AbstractLoggingService.disableService(GraphiteSocket.class);
    }
}
