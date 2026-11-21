package org.briarproject.briar.identity;

import org.briarproject.bramble.api.db.DatabaseComponent;
import org.briarproject.bramble.api.db.DbException;
import org.briarproject.bramble.api.db.Transaction;
import org.briarproject.bramble.api.identity.Author;
import org.briarproject.bramble.api.identity.AuthorId;
import org.briarproject.briar.api.identity.AJN_Author;
import org.briarproject.briar.api.identity.AJN_ScoreManager;
import org.briarproject.nullsafety.NotNullByDefault;

import javax.annotation.concurrent.ThreadSafe;
import javax.inject.Inject;

@ThreadSafe
@NotNullByDefault
public class AJN_ScoreManagerImpl implements AJN_ScoreManager {

	private final DatabaseComponent db;

	@Inject
	public AJN_ScoreManagerImpl(DatabaseComponent db) {
		this.db = db;
	}

	@Override
	public AJN_Author getAjnAuthor(Author author) throws DbException {
		return db.transactionWithResult(true, txn -> getAjnAuthor(txn, author));
	}

	@Override
	public AJN_Author getAjnAuthor(Transaction txn, Author author) throws DbException {
		int score = db.getScore(txn, author.getId());
		boolean isGenesis = db.isGenesis(txn, author.getId());
		return new AJN_Author(author, score, isGenesis);
	}

	@Override
	public void updateScore(AuthorId authorId, int newScore) throws DbException {
		db.transaction(false, txn -> db.updateScore(txn, authorId, newScore));
	}

	@Override
	public void modifyScore(AuthorId authorId, int delta) throws DbException {
		db.transaction(false, txn -> {
			int currentScore = db.getScore(txn, authorId);
			int newScore = Math.max(0, currentScore + delta);
			db.updateScore(txn, authorId, newScore);
		});
	}
}
