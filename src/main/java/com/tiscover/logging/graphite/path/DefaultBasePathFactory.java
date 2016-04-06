package com.tiscover.logging.graphite.path;

import java.util.Collections;
import java.util.List;

public class DefaultBasePathFactory extends BasePathFactory {

  @Override
  public List<String> getBasePathElements() {
    return Collections.emptyList();
  }

}
