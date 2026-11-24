package org.briarproject.briar.api.topic;

import org.briarproject.bramble.test.BrambleTestCase;
import org.junit.Test;

import static org.briarproject.briar.api.topic.AJN_SubscriptionState.*;
import static org.junit.Assert.assertEquals;

public class AJN_SubscriptionStateTest extends BrambleTestCase {

	@Test
	public void testFromCodeValid() {
		assertEquals(NOT_SUBSCRIBED, AJN_SubscriptionState.fromCode(0));
		assertEquals(REQUESTED_INVITED, AJN_SubscriptionState.fromCode(1));
		assertEquals(MEMBER, AJN_SubscriptionState.fromCode(2));

		assertEquals(0, NOT_SUBSCRIBED.getCode());
		assertEquals(1, REQUESTED_INVITED.getCode());
		assertEquals(2, MEMBER.getCode());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testFromCodeInvalidThrowsException() {
		AJN_SubscriptionState.fromCode(-1);
	}
}