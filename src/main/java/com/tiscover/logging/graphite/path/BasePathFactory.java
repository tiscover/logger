package com.tiscover.logging.graphite.path;

import java.util.List;

public abstract class BasePathFactory {
    public abstract List<String> getBasePathElements();

    public String getGraphiteBasePath() {
        List<String> elements = getBasePathElements();
        if (elements == null || elements.isEmpty()) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        for (String element : elements) {
            if (element != null && !element.isEmpty()) {
                sb.append(element).append(".");
            }
        }
        return sb.toString();
    }
}
