package org.briarproject.briar.api.identity;

import org.briarproject.bramble.test.BrambleTestCase;
import org.junit.Test;

import static org.briarproject.bramble.test.TestUtils.getAuthor;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.briarproject.briar.api.identity.AJN_Role.*;

public class AJN_AuthorTest extends BrambleTestCase {

	@Test
	public void testSandboxPermissions() {
		AJN_Author author = new AJN_Author(getAuthor(), 0, false);
		assertEquals(SANDBOX, author.getRole());
		assertFalse(author.canVoteInfo());
		assertFalse(author.canEvaluateUser());
		assertFalse(author.canIntroduceNodes());
		assertFalse(author.canAccessSensitiveInfo());
		assertTrue(author.hasRateLimit());
	}

	@Test
	public void testTrustedPermissions() {
		assertPermissions(new AJN_Author(getAuthor(), TRUSTED_THRESHOLD, false));
	}

	@Test
	public void testGenesisPermissions() {
		assertPermissions(new AJN_Author(getAuthor(), 0, true));
	}

	@Test
	public void testAddingScoreUpdatesRoleAndPermissions() {
		AJN_Author author = new AJN_Author(getAuthor(), 30, false);
		assertEquals(SANDBOX, author.getRole());
		author.addScore(25);
		assertEquals(55, author.getScore());
		assertEquals(TRUSTED, author.getRole());
		assertTrue(author.canVoteInfo());
	}

	private void assertPermissions(AJN_Author author) {
		assertTrue(author.canVoteInfo());
		assertTrue(author.canEvaluateUser());
		assertTrue(author.canIntroduceNodes());
		assertTrue(author.canAccessSensitiveInfo());
		assertFalse(author.hasRateLimit());
	}
}
