package org.briarproject.briar.api.identity;

import org.briarproject.bramble.test.BrambleTestCase;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.briarproject.briar.api.identity.AJN_Role.*;

public class AJN_RoleTest extends BrambleTestCase {

	@Test
	public void testSandboxScores() {
		assertEquals(SANDBOX, calculateRole(0, false));
		assertEquals(SANDBOX, calculateRole(49, false));
	}

	@Test
	public void testTrustedScores() {
		assertEquals(TRUSTED, calculateRole(TRUSTED_THRESHOLD, false));
		assertEquals(TRUSTED, calculateRole(100, false));
	}

	@Test
	public void testGenesisTakesPrecedenceOverScore() {
		assertEquals(GENESIS, calculateRole(0, true));
		assertEquals(GENESIS, calculateRole(100, true));
	}
}
