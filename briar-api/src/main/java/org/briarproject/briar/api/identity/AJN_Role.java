package org.briarproject.briar.api.identity;

public enum AJN_Role {
	GENESIS,
	SANDBOX,
	TRUSTED;

	/** Punteggio di default per il nodo GENESIS */
	public static final int DEFAULT_GENESIS_SCORE = 100;

	/** Punteggio iniziale per i nuovi utenti SANDBOX */
	public static final int DEFAULT_SANDBOX_SCORE = 0;

	/** Soglia di punteggio necessaria per sbloccare il livello TRUSTED */
	public static final int TRUSTED_THRESHOLD = 50;

	/**
	 * Calcola il ruolo dell'utente in base al punteggio e allo stato di GENESIS.
	 */
	public static AJN_Role calculateRole(int score, boolean isGenesis) {
		if (isGenesis) return GENESIS;
		if (score >= TRUSTED_THRESHOLD) return TRUSTED;
		return SANDBOX;
	}
}
