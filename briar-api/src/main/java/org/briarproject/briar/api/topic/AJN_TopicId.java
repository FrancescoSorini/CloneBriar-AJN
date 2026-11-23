package org.briarproject.briar.api.topic;

import org.briarproject.bramble.api.UniqueId;
import org.briarproject.nullsafety.NotNullByDefault;

import javax.annotation.concurrent.ThreadSafe;

/**
 * Wrapper per un array di 32 byte che identifica in modo univoco un argomento AJN.
 */
@ThreadSafe
@NotNullByDefault
public class AJN_TopicId extends UniqueId {

	/**
	 * Label per il calcolo dell'hash dell'identificatore del Topic.
	 */
	public static final String LABEL = "org.briarproject.briar/AJN_TOPIC_ID";

	public AJN_TopicId(byte[] id) {
		super(id);
	}

	@Override
	public boolean equals(Object o) {
		return o instanceof AJN_TopicId && super.equals(o);
	}
}