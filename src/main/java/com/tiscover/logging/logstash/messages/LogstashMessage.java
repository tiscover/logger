package com.tiscover.logging.logstash.messages;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONObject;

public class LogstashMessage {
	private final List<MessageEntity<String>> messages = new ArrayList<>();
	private String publicMessage = null;
	private boolean valuesChanged = false;

	public void put(String key, String value) {
		putInternal(key, value);
	}

	public void put(String key, Number value) {
		putInternal(key, value.toString());
	}

	public void put(String key, Date value) {
		putInternal(key, value.toString());
	}

	public void put(String key, LogstashMessage value) {
		putInternal(key, value.toSocketMessage());
	}

	public void put(String key, Serializable value) {
		putInternal(key, value.toString());
	}

	private void putInternal(String key, String value) {
		MessageEntity<String> message = new MessageEntity<>();
		message.setKey(key);
		message.setValue(value);
		getMessages().add(message);
		valuesChanged = true;
	}

	public String toSocketMessage() {
		if (valuesChanged || publicMessage == null) {
			JSONObject object = new JSONObject();
			for (MessageEntity<String> message : getMessages()) {
				object.put(message.getKey(), message.getValue());
			}
			publicMessage = object.toString();
			valuesChanged = false;
		}
		return publicMessage;
	}

  public List<MessageEntity<String>> getMessages() {
    return messages;
  }
}
