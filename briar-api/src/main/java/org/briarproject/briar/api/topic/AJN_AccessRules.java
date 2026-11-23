package org.briarproject.briar.api.topic;

import org.briarproject.briar.api.identity.AJN_Author;
import org.briarproject.nullsafety.NotNullByDefault;

/**
 * Definisce le regole di visibilità e accesso di un Topic AJN.
 */
@NotNullByDefault
public enum AJN_AccessRules {

	/** Topic pubblico: visibile e accessibile a tutti gli utenti della comunità (inclusi i nodi SANDBOX) */
	PUBLIC(0),

	/** Topic riservato: accessibile solo agli utenti con ruolo TRUSTED (score >= 50) o GENESIS */
	RESTRICTED_TRUSTED(1);

	private final int code;

	AJN_AccessRules(int code) {
		this.code = code;
	}

	/** Ritorna il codice intero memorizzato nel Database */
	public int getCode() {
		return code;
	}

	/**
	 * Riconverte il codice intero del Database nell'Enum corrispettivo.
	 */
	public static AJN_AccessRules fromCode(int code) {
		for (AJN_AccessRules rule : values()) {
			if (rule.code == code) return rule;
		}
		throw new IllegalArgumentException("Unknown access rules code: " + code);
	}

	/**
	 * Verifica se l'utente fornito ha i permessi necessari per accedere al Topic.
	 */
	public boolean isAllowed(AJN_Author author) {
		if (this == RESTRICTED_TRUSTED) {
			return author.canAccessSensitiveInfo();
		}
		return true;
	}
}
