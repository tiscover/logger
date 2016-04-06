package com.tiscover.logging.logstash;

import java.util.Date;

import com.tiscover.logging.logstash.messages.LogstashMessage;

public class LogstashRunner {

  public static void main(String[] args)  {
    LogstashMessage o = new LogstashMessage();
    o.put("testmessage", "test");
    o.put("date", new Date().toString());
    LogstashService.get().send(o);
    LogstashService.get().flush();
  }
}
