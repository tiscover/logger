package com.tiscover.logging.logstash.messages;

import org.junit.Assert;
import org.junit.Test;

public class LogstashMessageTest {
	@Test
	public void testMessageOrder() {
		LogstashMessage message = getBasicMessageSet();

		Assert.assertEquals("zzzmsg1", message.getMessages().get(0).getKey());
		Assert.assertEquals("aaamsg2", message.getMessages().get(1).getKey());
	}

	@Test
	public void testSortedMessageOrder() {
		LogstashMessage message = getBasicMessageSet();
		message.sortMessages();
		Assert.assertEquals("aaamsg2", message.getMessages().get(0).getKey());
		Assert.assertEquals("zzzmsg1", message.getMessages().get(1).getKey());

	}

	@Test
	public void testMessageNullKeyAndNullValue() {
		LogstashMessage message = getBasicMessageSet();
		message.put(null, (String) null);
		message.sortMessages();
		Assert.assertEquals(2, message.getMessages().size());
		Assert.assertEquals("aaamsg2", message.getMessages().get(0).getKey());
		Assert.assertEquals("zzzmsg1", message.getMessages().get(1).getKey());

	}

	@Test
	public void testMessageNullKey() {
		LogstashMessage message = getBasicMessageSet();
		message.put(null, "123");
		message.sortMessages();
		Assert.assertEquals(2, message.getMessages().size());
		Assert.assertEquals("aaamsg2", message.getMessages().get(0).getKey());
		Assert.assertEquals("zzzmsg1", message.getMessages().get(1).getKey());

	}

	@Test
	public void testMessageEmptyKey() {
		LogstashMessage message = getBasicMessageSet();
		message.put("", "123");
		message.sortMessages();
		Assert.assertEquals(2, message.getMessages().size());
		Assert.assertEquals("aaamsg2", message.getMessages().get(0).getKey());
		Assert.assertEquals("zzzmsg1", message.getMessages().get(1).getKey());

	}

	@Test
	public void testMessageNullValue() {
		LogstashMessage message = getBasicMessageSet();
		message.put("aabbcc", (String) null);
		message.sortMessages();
		Assert.assertEquals(2, message.getMessages().size());
		Assert.assertEquals("aaamsg2", message.getMessages().get(0).getKey());
		Assert.assertEquals("zzzmsg1", message.getMessages().get(1).getKey());

	}

	@Test
	public void testMessageEmptyValue() {
		LogstashMessage message = getBasicMessageSet();
		message.put("aabbcc", "");
		message.sortMessages();
		Assert.assertEquals(3, message.getMessages().size());
		Assert.assertEquals("aaamsg2", message.getMessages().get(0).getKey());
		Assert.assertEquals("aabbcc", message.getMessages().get(1).getKey());
		Assert.assertEquals("zzzmsg1", message.getMessages().get(2).getKey());

	}

	private LogstashMessage getBasicMessageSet() {
		LogstashMessage message = new LogstashMessage();
		message.put("zzzmsg1", "val1");
		message.put("aaamsg2", 2);
		return message;
	}

	@Test
	public void testNumericValue() {
		LogstashMessage message = getBasicMessageSet();
		Assert.assertTrue(message.toSocketMessage().contains("\"aaamsg2\":2"));
	}
}
