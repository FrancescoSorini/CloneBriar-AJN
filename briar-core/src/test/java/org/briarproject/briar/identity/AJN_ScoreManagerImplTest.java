package org.briarproject.briar.identity;

import org.briarproject.bramble.api.db.DatabaseComponent;
import org.briarproject.bramble.api.db.Transaction;
import org.briarproject.bramble.api.identity.Author;
import org.briarproject.bramble.api.identity.AuthorId;
import org.briarproject.bramble.test.BrambleMockTestCase;
import org.briarproject.bramble.test.DbExpectations;
import org.briarproject.briar.api.identity.AJN_Author;
import org.jmock.Expectations;
import org.junit.Test;

import static org.briarproject.bramble.test.TestUtils.getAuthor;
import static org.junit.Assert.assertEquals;

public class AJN_ScoreManagerImplTest extends BrambleMockTestCase {
	private final DatabaseComponent db = context.mock(DatabaseComponent.class);
	private final AJN_ScoreManagerImpl manager = new AJN_ScoreManagerImpl(db);
	private final Author author = getAuthor();
	private final AuthorId authorId = author.getId();

	@Test
	public void testGetAjnAuthor() throws Exception {
		Transaction txn = new Transaction(null, true);
		context.checking(new DbExpectations() {{
			oneOf(db).transactionWithResult(with(true), withDbCallable(txn));
			oneOf(db).getScore(txn, authorId);
			will(returnValue(63));
			oneOf(db).isGenesis(txn, authorId);
			will(returnValue(false));
		}});

		AJN_Author result = manager.getAjnAuthor(author);
		assertEquals(authorId, result.getId());
		assertEquals(63, result.getScore());
	}

	@Test
	public void testUpdateScoreUsesWriteTransaction() throws Exception {
		Transaction txn = new Transaction(null, false);
		context.checking(new DbExpectations() {{
			oneOf(db).transaction(with(false), withDbRunnable(txn));
			oneOf(db).updateScore(txn, authorId, 72);
		}});
		manager.updateScore(authorId, 72);
	}

	@Test
	public void testModifyScoreAddsDelta() throws Exception {
		assertModifiedScore(20, 15, 35);
	}

	@Test
	public void testModifyScoreDoesNotGoBelowZero() throws Exception {
		assertModifiedScore(10, -30, 0);
	}

	private void assertModifiedScore(int current, int delta, int expected)
			throws Exception {
		Transaction txn = new Transaction(null, false);
		context.checking(new DbExpectations() {{
			oneOf(db).transaction(with(false), withDbRunnable(txn));
			oneOf(db).getScore(txn, authorId);
			will(returnValue(current));
			oneOf(db).updateScore(txn, authorId, expected);
		}});
		manager.modifyScore(authorId, delta);
	}
}
