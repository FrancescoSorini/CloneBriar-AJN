package org.briarproject.briar.api.topic;

import org.briarproject.bramble.api.identity.AuthorId;
import org.briarproject.bramble.api.sync.GroupId;
import org.briarproject.nullsafety.NotNullByDefault;

import javax.annotation.concurrent.Immutable;

/**
 * Rappresenta l'entità di dominio logica di un Topic AJN.
 * È disaccoppiata dall'API interna del Forum di Briar ma mantiene un riferimento
 * al Gruppo Forum sottostante (forumGroupId).
 */
@Immutable
@NotNullByDefault
public class AJN_Topic {

	private final AJN_TopicId topicId;
	private final GroupId communityId;
	private final String name;
	private final String description;
	private final AuthorId authorId;
	private final long createdAt;
	private final AJN_TopicStatus status;
	private final AJN_AccessRules accessRules;
	private final GroupId forumGroupId;

	public AJN_Topic(AJN_TopicId topicId, GroupId communityId, String name,
			String description, AuthorId authorId, long createdAt,
			AJN_TopicStatus status, AJN_AccessRules accessRules,
			GroupId forumGroupId) {
		this.topicId = topicId;
		this.communityId = communityId;
		this.name = name;
		this.description = description;
		this.authorId = authorId;
		this.createdAt = createdAt;
		this.status = status;
		this.accessRules = accessRules;
		this.forumGroupId = forumGroupId;
	}

	public AJN_TopicId getTopicId() {
		return topicId;
	}

	public GroupId getCommunityId() {
		return communityId;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public AuthorId getAuthorId() {
		return authorId;
	}

	public long getCreatedAt() {
		return createdAt;
	}

	public AJN_TopicStatus getStatus() {
		return status;
	}

	public AJN_AccessRules getAccessRules() {
		return accessRules;
	}

	public GroupId getForumGroupId() {
		return forumGroupId;
	}

	/** Ritorna true se il Topic è archiviato ed è in sola lettura */
	public boolean isReadOnly() {
		return status == AJN_TopicStatus.ARCHIVED;
	}

	/** Ritorna true se il Topic è attivo e può accettare nuovi post */
	public boolean canAcceptPosts() {
		return status == AJN_TopicStatus.ACTIVE;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		AJN_Topic ajnTopic = (AJN_Topic) o;
		return topicId.equals(ajnTopic.topicId);
	}

	@Override
	public int hashCode() {
		return topicId.hashCode();
	}

	@Override
	public String toString() {
		return name;
	}
}