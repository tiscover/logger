package com.tiscover.logging.logstash.messages;

import java.util.Comparator;

public class MessageComparator implements Comparator<MessageEntity> {

	@Override
	public int compare(MessageEntity o1, MessageEntity o2) {
		int o1Val = (o1 == null || o1.getKey() == null) ? 0 : 1;
		int o2Val = (o2 == null || o2.getKey() == null) ? 0 : 1;
		
		if (o1Val > 0 && o2Val > 0) {
			return o1.getKey().compareTo(o2.getKey());
		}
		return Integer.compare(o1Val, o2Val);
	}

}
