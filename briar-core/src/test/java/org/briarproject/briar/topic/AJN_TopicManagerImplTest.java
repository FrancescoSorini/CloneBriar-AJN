package org.briarproject.briar.topic;

import org.briarproject.bramble.api.crypto.CryptoComponent;
import org.briarproject.bramble.api.db.AJN_TopicRecord;
import org.briarproject.bramble.api.db.DatabaseComponent;
import org.briarproject.bramble.api.db.Transaction;
import org.briarproject.bramble.api.identity.AuthorId;
import org.briarproject.bramble.api.identity.IdentityManager;
import org.briarproject.bramble.api.identity.LocalAuthor;
import org.briarproject.bramble.api.sync.Group;
import org.briarproject.bramble.api.sync.GroupId;
import org.briarproject.bramble.test.BrambleMockTestCase;
import org.briarproject.bramble.test.DbExpectations;
import org.briarproject.briar.api.forum.Forum;
import org.briarproject.briar.api.forum.ForumFactory;
import org.briarproject.briar.api.forum.ForumManager;
import org.briarproject.briar.api.identity.AJN_Author;
import org.briarproject.briar.api.identity.AJN_ScoreManager;
import org.briarproject.briar.api.topic.AJN_AccessRules;
import org.briarproject.briar.api.topic.AJN_Topic;
import org.briarproject.briar.api.topic.AJN_TopicId;
import org.briarproject.briar.api.topic.AJN_TopicStatus;
import org.jmock.Expectations;
import org.junit.Test;

import static org.briarproject.bramble.test.TestUtils.getAuthor;
import static org.briarproject.bramble.test.TestUtils.getLocalAuthor;
import static org.briarproject.bramble.test.TestUtils.getRandomId;
import static org.briarproject.bramble.test.TestUtils.getSecretKey;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class AJN_TopicManagerImplTest extends BrambleMockTestCase {

	private final DatabaseComponent db = context.mock(DatabaseComponent.class);
	private final ForumFactory forumFactory = context.mock(ForumFactory.class);
	private final ForumManager forumManager = context.mock(ForumManager.class);
	private final AJN_ScoreManager scoreManager = context.mock(AJN_ScoreManager.class);
	private final IdentityManager identityManager = context.mock(IdentityManager.class);
	private final CryptoComponent cryptoComponent = context.mock(CryptoComponent.class);

	private final AJN_TopicManagerImpl manager = new AJN_TopicManagerImpl(
			db, forumFactory, forumManager, scoreManager, identityManager, cryptoComponent
	);

	@Test
	public void testCreateTopicSuccess() throws Exception {
		Transaction txn = new Transaction(null, false);
		LocalAuthor localAuthor = getLocalAuthor();
		AJN_Author ajnAuthor = new AJN_Author(localAuthor, 60, false); // TRUSTED User

		GroupId forumGroupId = new GroupId(getRandomId());
		Group group = new Group(forumGroupId, ForumManager.CLIENT_ID, 0, new byte[0]);
		Forum forum = new Forum(group, "Topic Test", new byte[0]);

		context.checking(new DbExpectations() {{
			oneOf(db).transactionWithResult(with(false), withDbCallable(txn));
			oneOf(cryptoComponent).generateSecretKey();
			will(returnValue(getSecretKey()));
			oneOf(identityManager).getLocalAuthor(txn);
			will(returnValue(localAuthor));
			oneOf(scoreManager).getAjnAuthor(txn, localAuthor);
			will(returnValue(ajnAuthor));
			oneOf(forumFactory).createForum("Topic Test");
			will(returnValue(forum));
			oneOf(forumManager).addForum(txn, forum);
			oneOf(cryptoComponent).hash(AJN_TopicId.LABEL, forumGroupId.getBytes());
			will(returnValue(getRandomId()));
			oneOf(db).addAjnTopic(with(txn), with(any(AJN_TopicRecord.class)));
			oneOf(db).setAjnTopicSubscription(with(txn), with(any(byte[].class)), with(2)); // MEMBER code = 2
		}});

		AJN_Topic topic = manager.createTopic("Topic Test", "Descrizione", AJN_AccessRules.PUBLIC);
		assertNotNull(topic);
		assertEquals("Topic Test", topic.getName());
	}

	@Test(expected = IllegalStateException.class)
	public void testCreateTopicFailsForSandboxUserOnRestrictedTopic() throws Exception {
		Transaction txn = new Transaction(null, false);
		LocalAuthor localAuthor = getLocalAuthor();
		AJN_Author sandboxAuthor = new AJN_Author(localAuthor, 10, false); // SANDBOX User

		context.checking(new DbExpectations() {{
			oneOf(db).transactionWithResult(with(false), withDbCallable(txn));
			oneOf(cryptoComponent).generateSecretKey();
			will(returnValue(getSecretKey()));
			oneOf(identityManager).getLocalAuthor(txn);
			will(returnValue(localAuthor));
			oneOf(scoreManager).getAjnAuthor(txn, localAuthor);
			will(returnValue(sandboxAuthor));
			// Nessun'altra interazione DB o Forum deve avvenire a causa del blocco permessi
		}});

		manager.createTopic("Topic Riservato", "Descrizione", AJN_AccessRules.RESTRICTED_TRUSTED);
	}

	@Test
	public void testArchiveTopic() throws Exception {
		Transaction txn = new Transaction(null, false);
		AJN_TopicId topicId = new AJN_TopicId(getRandomId());

		context.checking(new DbExpectations() {{
			oneOf(db).transaction(with(false), withDbRunnable(txn));
			oneOf(db).updateAjnTopicStatus(txn, topicId.getBytes(), AJN_TopicStatus.ARCHIVED.getCode());
		}});

		manager.archiveTopic(topicId);
	}
}
