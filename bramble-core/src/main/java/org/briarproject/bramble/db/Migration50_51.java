package org.briarproject.bramble.db;

import org.briarproject.bramble.api.db.DbException;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

import static java.util.logging.Level.WARNING;
import static java.util.logging.Logger.getLogger;
import static org.briarproject.bramble.db.JdbcUtils.tryToClose;

/** Creates the AJN topic tables. */
class Migration50_51 implements Migration<Connection> {

	private static final Logger LOG = getLogger(Migration50_51.class.getName());

	private final DatabaseTypes dbTypes;

	Migration50_51(DatabaseTypes dbTypes) {
		this.dbTypes = dbTypes;
	}

	@Override
	public int getStartVersion() {
		return 50;
	}

	@Override
	public int getEndVersion() {
		return 51;
	}

	@Override
	public void migrate(Connection txn) throws DbException {
		Statement s = null;
		try {
			s = txn.createStatement();
			s.execute(dbTypes.replaceTypes("CREATE TABLE ajn_topics"
					+ " (topicId _HASH NOT NULL,"
					+ " communityId _HASH NOT NULL,"
					+ " name _STRING NOT NULL,"
					+ " description _STRING NOT NULL,"
					+ " authorId _HASH NOT NULL,"
					+ " createdAt BIGINT NOT NULL,"
					+ " status INT NOT NULL,"
					+ " accessRules INT NOT NULL,"
					+ " forumGroupId _HASH NOT NULL,"
					+ " PRIMARY KEY (topicId),"
					+ " FOREIGN KEY (forumGroupId)"
					+ " REFERENCES groups (groupId)"
					+ " ON DELETE CASCADE)"));
			s.execute(dbTypes.replaceTypes("CREATE TABLE ajn_topic_subscriptions"
					+ " (topicId _HASH NOT NULL,"
					+ " state INT NOT NULL,"
					+ " joinedAt BIGINT NOT NULL,"
					+ " PRIMARY KEY (topicId),"
					+ " FOREIGN KEY (topicId)"
					+ " REFERENCES ajn_topics (topicId)"
					+ " ON DELETE CASCADE)"));
		} catch (SQLException e) {
			tryToClose(s, LOG, WARNING);
			throw new DbException(e);
		}
	}
}
