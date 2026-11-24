package org.briarproject.briar.api.topic;

import org.briarproject.bramble.test.BrambleTestCase;
import org.junit.Test;

import static org.briarproject.bramble.test.TestUtils.getRandomId;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class AJN_TopicIdTest extends BrambleTestCase {

	@Test
	public void testValidTopicIdCreation() {
		byte[] rawId = getRandomId(); // Array da 32 byte casuali
		AJN_TopicId topicId = new AJN_TopicId(rawId);
		assertEquals(32, topicId.getBytes().length);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInvalidTopicIdLengthThrowsException() {
		byte[] invalidId = new byte[16]; // Dimensione errata
		new AJN_TopicId(invalidId);
	}

	@Test
	public void testEqualsAndHashCode() {
		byte[] rawId = getRandomId();
		AJN_TopicId id1 = new AJN_TopicId(rawId);
		AJN_TopicId id2 = new AJN_TopicId(rawId);
		AJN_TopicId id3 = new AJN_TopicId(getRandomId());

		assertEquals(id1, id2);
		assertEquals(id1.hashCode(), id2.hashCode());
		assertNotEquals(id1, id3);
	}
}
