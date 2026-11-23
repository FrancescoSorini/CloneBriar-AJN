package org.briarproject.briar.api.topic;

import org.briarproject.nullsafety.NotNullByDefault;

/**
 * Rappresenta lo stato di adesione dell'utente locale a un Topic AJN.
 */
@NotNullByDefault
public enum AJN_SubscriptionState {

	/** L'utente locale non è iscritto al Topic */
	NOT_SUBSCRIBED(0),

	/** L'utente ha richiesto l'accesso al Topic o ha ricevuto un invito ed è in attesa */
	REQUESTED_INVITED(1),

	/** L'utente è membro effettivo del Topic e partecipa alla sincronizzazione dei post */
	MEMBER(2);

	private final int code;

	AJN_SubscriptionState(int code) {
		this.code = code;
	}

	/** Ritorna il codice intero memorizzato nel Database */
	public int getCode() {
		return code;
	}

	/**
	 * Riconverte il codice intero del Database nell'Enum corrispettivo.
	 */
	public static AJN_SubscriptionState fromCode(int code) {
		for (AJN_SubscriptionState state : values()) {
			if (state.code == code) return state;
		}
		throw new IllegalArgumentException("Unknown subscription state code: " + code);
	}
}