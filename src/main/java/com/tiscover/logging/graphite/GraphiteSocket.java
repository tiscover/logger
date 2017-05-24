package com.tiscover.logging.graphite;

import java.io.IOException;

import com.tiscover.logging.AbstractLoggingSocket;
import com.tiscover.logging.graphite.path.BasePathFactory;
import com.tiscover.logging.observer.EnabledObserver;

public class GraphiteSocket extends AbstractLoggingSocket {
    private BasePathFactory basePathFactory;

    GraphiteSocket(EnabledObserver observer) {
        super(observer);
    }

    public void send(GraphiteDataPoint d) throws IOException {
        if (d != null) {
            send(d.getName(), d.getValue(), d.getTimestamp());
        }
    }

    public void send(String name, double value, long timestamp) throws IOException {
        long tm = timestamp / 1000;
        send(basePathFactory.getGraphiteBasePath() + name + " " + value + " " + tm);
    }

    public BasePathFactory getBasePathFactory() {
        return basePathFactory;
    }

    public void setBasePathFactory(BasePathFactory basePathFactory) {
        this.basePathFactory = basePathFactory;
    }
}
