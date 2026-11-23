package org.briarproject.bramble.api.db;

import org.briarproject.bramble.api.identity.AuthorId;
import org.briarproject.bramble.api.sync.GroupId;
import org.briarproject.nullsafety.NotNullByDefault;

import javax.annotation.concurrent.Immutable;

/**
 * Contenitore dati AJN Topic per persistenza nel database.
 */
@Immutable
@NotNullByDefault
public class AJN_TopicRecord {

	public final byte[] topicId;
	public final GroupId communityId;
	public final String name;
	public final String description;
	public final AuthorId authorId;
	public final long createdAt;
	public final int status;
	public final int accessRules;
	public final GroupId forumGroupId;

	public AJN_TopicRecord(byte[] topicId, GroupId communityId, String name,
			String description, AuthorId authorId, long createdAt, int status,
			int accessRules, GroupId forumGroupId) {
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
}
