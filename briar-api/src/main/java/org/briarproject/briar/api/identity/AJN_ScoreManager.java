package org.briarproject.briar.api.identity;

import org.briarproject.bramble.api.db.DbException;
import org.briarproject.bramble.api.db.Transaction;
import org.briarproject.bramble.api.identity.Author;
import org.briarproject.bramble.api.identity.AuthorId;
import org.briarproject.nullsafety.NotNullByDefault;

@NotNullByDefault
public interface AJN_ScoreManager{

	/**
	 * Ritorna l'AJN_Author (wrapper) per l'autore specificato.
	 */
	AJN_Author getAjnAuthor(Author author) throws DbException;

	/**
	 * Ritorna l'AJN_Author per la transazione DB specificata.
	 */
	AJN_Author getAjnAuthor(Transaction txn, Author author) throws DbException;

	/**
	 * Aggiorna lo score di un autore salvandolo nel Database.
	 */
	void updateScore(AuthorId authorId, int newScore) throws DbException;

	/**
	 * Incrementa o decrementa lo score a seguito di un evento o votazione.
	 */
	void modifyScore(AuthorId authorId, int delta) throws DbException;
}
