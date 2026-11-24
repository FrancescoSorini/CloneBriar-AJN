package org.briarproject.briar.api.topic;

import org.briarproject.bramble.test.BrambleTestCase;
import org.junit.Test;

import static org.briarproject.briar.api.topic.AJN_TopicStatus.ACTIVE;
import static org.briarproject.briar.api.topic.AJN_TopicStatus.ARCHIVED;
import static org.junit.Assert.assertEquals;

public class AJN_TopicStatusTest extends BrambleTestCase {

	@Test
	public void testFromCodeValid() {
		assertEquals(ACTIVE, AJN_TopicStatus.fromCode(0));
		assertEquals(ARCHIVED, AJN_TopicStatus.fromCode(1));

		assertEquals(0, ACTIVE.getCode());
		assertEquals(1, ARCHIVED.getCode());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testFromCodeInvalidThrowsException() {
		AJN_TopicStatus.fromCode(99);
	}
}
