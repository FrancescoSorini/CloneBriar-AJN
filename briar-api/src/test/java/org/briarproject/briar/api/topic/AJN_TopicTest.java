package org.briarproject.briar.api.topic;

import org.briarproject.bramble.api.identity.AuthorId;
import org.briarproject.bramble.api.sync.GroupId;
import org.briarproject.bramble.test.BrambleTestCase;
import org.junit.Test;

import static org.briarproject.bramble.test.TestUtils.getRandomId;
import static org.junit.Assert.assertEquals;

public class AJN_TopicTest extends BrambleTestCase {

	@Test
	public void testTopicGettersAndEquals() {
		AJN_TopicId topicId = new AJN_TopicId(getRandomId());
		GroupId communityId = new GroupId(getRandomId());
		AuthorId authorId = new AuthorId(getRandomId());
		GroupId forumGroupId = new GroupId(getRandomId());
		long now = System.currentTimeMillis();

		AJN_Topic topic1 = new AJN_Topic(
				topicId, communityId, "Topic Test", "Descrizione Test",
				authorId, now, AJN_TopicStatus.ACTIVE, AJN_AccessRules.PUBLIC, forumGroupId
		);

		AJN_Topic topic2 = new AJN_Topic(
				topicId, communityId, "Nome Modificato", "Altra Descrizione",
				authorId, now, AJN_TopicStatus.ARCHIVED, AJN_AccessRules.RESTRICTED_TRUSTED, forumGroupId
		);

		assertEquals(topicId, topic1.getTopicId());
		assertEquals("Topic Test", topic1.getName());
		assertEquals("Descrizione Test", topic1.getDescription());
		assertEquals(forumGroupId, topic1.getForumGroupId());

		// L'uguaglianza deve basarsi unicamente sul topicId
		assertEquals(topic1, topic2);
		assertEquals(topic1.hashCode(), topic2.hashCode());
	}
}