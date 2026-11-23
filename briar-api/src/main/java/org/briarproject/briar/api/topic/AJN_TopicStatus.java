package org.briarproject.briar.api.topic;

import org.briarproject.nullsafety.NotNullByDefault;

/**
 * Rappresenta lo stato del ciclo di vita di un Topic AJN.
 */
@NotNullByDefault
public enum AJN_TopicStatus {

	/** Topic attivo e aperto a contributi, post e discussioni */
	ACTIVE(0),

	/** Topic archiviato: consultabile in sola lettura, non accetta più nuovi post */
	ARCHIVED(1);

	private final int code;

	AJN_TopicStatus(int code) {
		this.code = code;
	}

	/** Ritorna il codice intero memorizzato nel Database */
	public int getCode() {
		return code;
	}

	/**
	 * Riconverte il codice intero del Database nell'Enum corrispettivo.
	 */
	public static AJN_TopicStatus fromCode(int code) {
		for (AJN_TopicStatus status : values()) {
			if (status.code == code) return status;
		}
		throw new IllegalArgumentException("Unknown topic status code: " + code);
	}
}
