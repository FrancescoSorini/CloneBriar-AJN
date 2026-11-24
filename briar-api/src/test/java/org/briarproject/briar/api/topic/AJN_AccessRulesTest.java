package org.briarproject.briar.api.topic;

import org.briarproject.bramble.test.BrambleTestCase;
import org.briarproject.briar.api.identity.AJN_Author;
import org.junit.Test;

import static org.briarproject.bramble.test.TestUtils.getAuthor;
import static org.briarproject.briar.api.topic.AJN_AccessRules.PUBLIC;
import static org.briarproject.briar.api.topic.AJN_AccessRules.RESTRICTED_TRUSTED;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AJN_AccessRulesTest extends BrambleTestCase {

	@Test
	public void testPublicAccessRulesAllowedForAllRoles() {
		AJN_Author sandboxUser = new AJN_Author(getAuthor(), 0, false);
		AJN_Author trustedUser = new AJN_Author(getAuthor(), 60, false);
		AJN_Author genesisUser = new AJN_Author(getAuthor(), 0, true);

		assertTrue(PUBLIC.isAllowed(sandboxUser));
		assertTrue(PUBLIC.isAllowed(trustedUser));
		assertTrue(PUBLIC.isAllowed(genesisUser));
	}

	@Test
	public void testRestrictedTrustedRulesEnforcesScoreAndRole() {
		AJN_Author sandboxUser = new AJN_Author(getAuthor(), 20, false);
		AJN_Author trustedUser = new AJN_Author(getAuthor(), 50, false);
		AJN_Author genesisUser = new AJN_Author(getAuthor(), 0, true);

		assertFalse(RESTRICTED_TRUSTED.isAllowed(sandboxUser));
		assertTrue(RESTRICTED_TRUSTED.isAllowed(trustedUser));
		assertTrue(RESTRICTED_TRUSTED.isAllowed(genesisUser));
	}
}
