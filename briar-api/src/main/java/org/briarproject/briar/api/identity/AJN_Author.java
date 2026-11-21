package org.briarproject.briar.api.identity;

import org.briarproject.bramble.api.identity.Author;
import org.briarproject.bramble.api.identity.AuthorId;
import org.briarproject.nullsafety.NotNullByDefault;

import javax.annotation.concurrent.ThreadSafe;

@ThreadSafe
@NotNullByDefault
public class AJN_Author{

	private final Author author;
	private final boolean isGenesis;
	private int score;

	public AJN_Author(Author author, int score, boolean isGenesis) {
		this.author = author;
		this.score = score;
		this.isGenesis = isGenesis;
	}

	/**
	 * Costruttore per nuovi utenti SANDBOX con score iniziale 0.
	 */
	public AJN_Author(Author author) {
		this(author, AJN_Role.DEFAULT_SANDBOX_SCORE, false);
	}

	/** Ritorna l'oggetto Author sottostante di Briar */
	public Author getAuthor() {
		return author;
	}

	/** Ritorna l'AuthorId di Briar */
	public AuthorId getId() {
		return author.getId();
	}

	/** Ritorna il nome dell'utente */
	public String getName() {
		return author.getName();
	}

	/** Ritorna score dell'utente */
	public synchronized int getScore() {
		return score;
	}

	/** Imposta score utente */
	public synchronized void setScore(int score) {
		this.score = score;
	}

	/** Incremento dello score (positivo o negativo) */
	public synchronized void addScore(int delta) {
		this.score += delta;
	}

	public boolean isGenesis() {
		return isGenesis;
	}

	/** Ritorna il ruolo corrente in base allo score */
	public synchronized AJN_Role getRole() {
		return AJN_Role.calculateRole(score, isGenesis);
	}

	// =========================================================================
	// REGOLE E PERMESSI AJN
	// =========================================================================

	/**
	 * I nodi SANDBOX non possono partecipare alle votazioni dei post di informazioni.
	 */
	public boolean canVoteInfo() {
		return getRole() != AJN_Role.SANDBOX;
	}

	/**
	 * I nodi SANDBOX non possono valutare un utente e variarne lo score.
	 */
	public boolean canEvaluateUser() {
		return getRole() != AJN_Role.SANDBOX;
	}

	/**
	 * I nodi SANDBOX non possono introdurre nuovi nodi nella comunità.
	 */
	public boolean canIntroduceNodes() {
		return getRole() != AJN_Role.SANDBOX;
	}

	/**
	 * I nodi SANDBOX non possono accedere ad informazioni sensibili.
	 */
	public boolean canAccessSensitiveInfo() {
		return getRole() != AJN_Role.SANDBOX;
	}

	/**
	 * I nodi SANDBOX hanno limitazioni sulla frequenza (rate limit) dei post.
	 */
	public boolean hasRateLimit() {
		return getRole() == AJN_Role.SANDBOX;
	}
}