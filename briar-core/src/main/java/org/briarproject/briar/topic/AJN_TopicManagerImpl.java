package org.briarproject.briar.topic;

import org.briarproject.bramble.api.crypto.CryptoComponent;
import org.briarproject.bramble.api.db.DatabaseComponent;
import org.briarproject.bramble.api.db.DbException;
import org.briarproject.bramble.api.db.Transaction;
import org.briarproject.bramble.api.identity.AuthorId;
import org.briarproject.bramble.api.identity.IdentityManager;
import org.briarproject.bramble.api.identity.LocalAuthor;
import org.briarproject.bramble.api.sync.GroupId;
import org.briarproject.bramble.api.sync.MessageId;
import org.briarproject.bramble.api.db.AJN_TopicRecord;
import org.briarproject.briar.api.forum.Forum;
import org.briarproject.briar.api.forum.ForumFactory;
import org.briarproject.briar.api.forum.ForumManager;
import org.briarproject.briar.api.forum.ForumPost;
import org.briarproject.briar.api.forum.ForumPostHeader;
import org.briarproject.briar.api.identity.AJN_Author;
import org.briarproject.briar.api.identity.AJN_ScoreManager;
import org.briarproject.briar.api.topic.AJN_AccessRules;
import org.briarproject.briar.api.topic.AJN_SubscriptionState;
import org.briarproject.briar.api.topic.AJN_Topic;
import org.briarproject.briar.api.topic.AJN_TopicId;
import org.briarproject.briar.api.topic.AJN_TopicManager;
import org.briarproject.briar.api.topic.AJN_TopicStatus;
import org.briarproject.nullsafety.NotNullByDefault;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;
import javax.inject.Inject;

@ThreadSafe
@NotNullByDefault
public class AJN_TopicManagerImpl implements AJN_TopicManager {

	private final DatabaseComponent db;
	private final ForumFactory forumFactory;
	private final ForumManager forumManager;
	private final AJN_ScoreManager scoreManager;
	private final IdentityManager identityManager;
	private final CryptoComponent cryptoComponent;

	@Inject
	AJN_TopicManagerImpl(DatabaseComponent db,
			ForumFactory forumFactory,
			ForumManager forumManager,
			AJN_ScoreManager scoreManager,
			IdentityManager identityManager,
			CryptoComponent cryptoComponent) {
		this.db = db;
		this.forumFactory = forumFactory;
		this.forumManager = forumManager;
		this.scoreManager = scoreManager;
		this.identityManager = identityManager;
		this.cryptoComponent = cryptoComponent;
	}

	@Override
	public AJN_Topic createTopic(GroupId communityId, String name, String description,
			AJN_AccessRules accessRules) throws DbException {
		return db.transactionWithResult(false,
				txn -> createTopic(txn, communityId, name, description, accessRules));
	}

	@Override
	public AJN_Topic createTopic(Transaction txn, GroupId communityId, String name,
			String description, AJN_AccessRules accessRules) throws DbException {

		LocalAuthor localAuthor = identityManager.getLocalAuthor(txn);
		AJN_Author ajnAuthor = scoreManager.getAjnAuthor(txn, localAuthor);

		// Verifica se l'utente ha i permessi necessari secondo le regole del Topic
		if (!accessRules.isAllowed(ajnAuthor)) {
			throw new IllegalStateException("L'utente non possiede i permessi sufficienti per creare questo Topic.");
		}

		// 1. Crea il Forum Briar nativo in background
		Forum forum = forumFactory.createForum(name);
		forumManager.addForum(txn, forum);

		// 2. Genera l'ID unico del Topic AJN
		byte[] rawTopicId = cryptoComponent.hash(AJN_TopicId.LABEL, forum.getGroup().getId().getBytes());
		AJN_TopicId topicId = new AJN_TopicId(rawTopicId);
		long createdAt = System.currentTimeMillis();

		// 3. Crea il DTO per la persistenza DB
		AJN_TopicRecord record = new AJN_TopicRecord(
				topicId.getBytes(),
				communityId,
				name,
				description,
				localAuthor.getId(),
				createdAt,
				AJN_TopicStatus.ACTIVE.getCode(),
				accessRules.getCode(),
				forum.getGroup().getId()
		);

		// 4. Salva il Topic e assegna l'iscrizione all'autore come MEMBER
		db.addAjnTopic(txn, record);
		db.setAjnTopicSubscription(txn, topicId.getBytes(), AJN_SubscriptionState.MEMBER.getCode());

		return new AJN_Topic(
				topicId,
				communityId,
				name,
				description,
				localAuthor.getId(),
				createdAt,
				AJN_TopicStatus.ACTIVE,
				accessRules,
				forum.getGroup().getId()
		);
	}

	@Override
	@Nullable
	public AJN_Topic getTopic(AJN_TopicId topicId) throws DbException {
		return db.transactionWithResult(true, txn -> getTopic(txn, topicId));
	}

	@Override
	@Nullable
	public AJN_Topic getTopic(Transaction txn, AJN_TopicId topicId) throws DbException {
		AJN_TopicRecord record = db.getAjnTopic(txn, topicId.getBytes());
		if (record == null) return null;
		return parseRecord(record);
	}

	@Override
	@Nullable
	public AJN_Topic getTopicByForumId(GroupId forumGroupId) throws DbException {
		return db.transactionWithResult(true, txn -> getTopicByForumId(txn, forumGroupId));
	}

	@Override
	@Nullable
	public AJN_Topic getTopicByForumId(Transaction txn, GroupId forumGroupId) throws DbException {
		AJN_TopicRecord record = db.getAjnTopicByForumId(txn, forumGroupId);
		if (record == null) return null;
		return parseRecord(record);
	}

	@Override
	public Collection<AJN_Topic> getTopics() throws DbException {
		return db.transactionWithResult(true, this::getTopics);
	}

	@Override
	public Collection<AJN_Topic> getTopics(Transaction txn) throws DbException {
		Collection<AJN_TopicRecord> records = db.getAjnTopics(txn);
		List<AJN_Topic> topics = new ArrayList<>();
		for (AJN_TopicRecord record : records) {
			topics.add(parseRecord(record));
		}
		return topics;
	}

	@Override
	public AJN_SubscriptionState getSubscriptionState(AJN_TopicId topicId) throws DbException {
		return db.transactionWithResult(true, txn -> getSubscriptionState(txn, topicId));
	}

	@Override
	public AJN_SubscriptionState getSubscriptionState(Transaction txn, AJN_TopicId topicId) throws DbException {
		int code = db.getAjnTopicSubscription(txn, topicId.getBytes());
		return AJN_SubscriptionState.fromCode(code);
	}

	@Override
	public void setSubscriptionState(AJN_TopicId topicId, AJN_SubscriptionState state) throws DbException {
		db.transaction(false, txn -> setSubscriptionState(txn, topicId, state));
	}

	@Override
	public void setSubscriptionState(Transaction txn, AJN_TopicId topicId, AJN_SubscriptionState state) throws DbException {
		db.setAjnTopicSubscription(txn, topicId.getBytes(), state.getCode());
	}

	@Override
	public void archiveTopic(AJN_TopicId topicId) throws DbException {
		db.transaction(false, txn -> archiveTopic(txn, topicId));
	}

	@Override
	public void archiveTopic(Transaction txn, AJN_TopicId topicId) throws DbException {
		db.updateAjnTopicStatus(txn, topicId.getBytes(), AJN_TopicStatus.ARCHIVED.getCode());
	}

	@Override
	public ForumPostHeader addTopicPost(AJN_TopicId topicId, String text,
			@Nullable MessageId parentId) throws DbException {
		return db.transactionWithResult(false,
				txn -> addTopicPost(txn, topicId, text, parentId));
	}

	@Override
	public ForumPostHeader addTopicPost(Transaction txn, AJN_TopicId topicId,
			String text, @Nullable MessageId parentId) throws DbException {
		AJN_Topic topic = getTopic(txn, topicId);
		if (topic == null) {
			throw new IllegalArgumentException("Topic non trovato");
		}

		// BLOCCO SCRITTURA LOCALE SE ARCHIVIATO
		if (topic.isReadOnly()) {
			throw new IllegalStateException("Impossibile pubblicare: il Topic è archiviato ed è in sola lettura.");
		}

		LocalAuthor author = identityManager.getLocalAuthor(txn);
		ForumPost post = forumManager.createLocalPost(topic.getForumGroupId(), text,
				System.currentTimeMillis(), parentId, author);
		return forumManager.addLocalPost(txn, post);
	}

	private AJN_Topic parseRecord(AJN_TopicRecord record) throws DbException {
		try {
			return new AJN_Topic(
					new AJN_TopicId(record.getTopicId()),
					record.communityId,
					record.name,
					record.description,
					record.authorId,
					record.createdAt,
					AJN_TopicStatus.fromCode(record.status),
					AJN_AccessRules.fromCode(record.accessRules),
					record.forumGroupId
			);
		} catch (IllegalArgumentException e) {
			throw new DbException(e);
		}
	}
}
