package com.tiscover.logging.logstash;

import java.util.Date;

import com.tiscover.logging.logstash.messages.LogstashMessage;
import com.tiscover.logging.observer.DefaultEnabledObserver;

public class LogstashRunner {

	public static void main(String[] args) {
		LogstashService service = new LogstashService("10.20.4.169", 9250, new DefaultEnabledObserver(true));
		LogstashService.set(service);

		sendMessage("tis", 10);
		sendMessage("api", 10);

		System.out.println("flush");
		LogstashService.get().flush();
		System.out.println("wait for timer");
		LogstashService.get().stopAndWaitForLastTimer();
	}

	private static void sendMessage(String type, int cnt) {

		for (int i = 0; i < cnt; i++) {
			LogstashMessage msg = new LogstashMessage(type);
			msg.put("date", new Date());
			msg.put("number", Math.random());

			LogstashService.get().send(msg);
		}

	}

}
