package org.briarproject.briar.api.topic;

import org.briarproject.bramble.api.db.DbException;
import org.briarproject.bramble.api.db.Transaction;
import org.briarproject.bramble.api.sync.ClientId;
import org.briarproject.bramble.api.sync.GroupId;
import org.briarproject.bramble.api.sync.MessageId;
import org.briarproject.briar.api.forum.ForumPostHeader;
import org.briarproject.nullsafety.NotNullByDefault;

import java.util.Collection;

import javax.annotation.Nullable;

@NotNullByDefault
public interface AJN_TopicManager {

	/**
	 * Identificatore unico del client Topic per il sistema di Sync.
	 */
	ClientId CLIENT_ID = new ClientId("org.briarproject.briar.topic");

	/**
	 * Crea un nuovo Topic AJN (e il Forum Briar associato in background).
	 */
	AJN_Topic createTopic(GroupId communityId, String name, String description,
			AJN_AccessRules accessRules) throws DbException;

	/**
	 * Crea un nuovo Topic AJN all'interno di una transazione DB esistente.
	 */
	AJN_Topic createTopic(Transaction txn, GroupId communityId, String name,
			String description, AJN_AccessRules accessRules) throws DbException;

	/**
	 * Ritorna il Topic associato all'ID specificato.
	 */
	@Nullable
	AJN_Topic getTopic(AJN_TopicId topicId) throws DbException;

	/**
	 * Ritorna il Topic associato all'ID specificato all'interno di una transazione DB.
	 */
	@Nullable
	AJN_Topic getTopic(Transaction txn, AJN_TopicId topicId) throws DbException;

	/**
	 * Ritorna il Topic associato all'ID del Forum Briar nativo sottostante.
	 */
	@Nullable
	AJN_Topic getTopicByForumId(GroupId forumGroupId) throws DbException;

	/**
	 * Ritorna il Topic associato all'ID del Forum Briar nativo sottostante in una transazione.
	 */
	@Nullable
	AJN_Topic getTopicByForumId(Transaction txn, GroupId forumGroupId)
			throws DbException;

	/**
	 * Ritorna tutti i Topic registrati nel client.
	 */
	Collection<AJN_Topic> getTopics() throws DbException;

	/**
	 * Ritorna tutti i Topic registrati nel client all'interno di una transazione.
	 */
	Collection<AJN_Topic> getTopics(Transaction txn) throws DbException;

	/**
	 * Ritorna lo stato di iscrizione dell'utente locale per il Topic specificato.
	 */
	AJN_SubscriptionState getSubscriptionState(AJN_TopicId topicId)
			throws DbException;

	/**
	 * Ritorna lo stato di iscrizione dell'utente locale all'interno di una transazione.
	 */
	AJN_SubscriptionState getSubscriptionState(Transaction txn,
			AJN_TopicId topicId) throws DbException;

	/**
	 * Aggiorna lo stato di iscrizione dell'utente locale per il Topic specificato.
	 */
	void setSubscriptionState(AJN_TopicId topicId, AJN_SubscriptionState state)
			throws DbException;

	/**
	 * Aggiorna lo stato di iscrizione dell'utente locale all'interno di una transazione.
	 */
	void setSubscriptionState(Transaction txn, AJN_TopicId topicId,
			AJN_SubscriptionState state) throws DbException;

	/**
	 * Archivia un Topic rendendolo in sola lettura.
	 */
	void archiveTopic(AJN_TopicId topicId) throws DbException;

	/**
	 * Archivia un Topic all'interno di una transazione.
	 */
	void archiveTopic(Transaction txn, AJN_TopicId topicId) throws DbException;

	/**
	 * Pubblica un nuovo post all'interno del Topic specificato se attivo.
	 */
	ForumPostHeader addTopicPost(AJN_TopicId topicId, String text,
			@Nullable MessageId parentId) throws DbException;

	/**
	 * Pubblica un nuovo post all'interno del Topic specificato all'interno di una transazione.
	 */
	ForumPostHeader addTopicPost(Transaction txn, AJN_TopicId topicId, String text,
			@Nullable MessageId parentId) throws DbException;
}
