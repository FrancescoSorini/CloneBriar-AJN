package org.briarproject.briar.identity;

import org.briarproject.briar.api.identity.AJN_ScoreManager;
import org.briarproject.briar.api.identity.AuthorManager;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class IdentityModule {

	public static class EagerSingletons {
		@Inject
		AuthorManager authorManager;

		/** MODIFICHE PROGETTO DI TESI AJN*/
		@Inject
		AJN_ScoreManager scoreManager;
		/** MODIFICHE PROGETTO DI TESI AJN*/
	}

	@Provides
	@Singleton
	AuthorManager provideAuthorManager(AuthorManagerImpl authorManager) {
		return authorManager;
	}

	/** MODIFICHE PROGETTO DI TESI AJN*/
	@Provides
	@Singleton
	AJN_ScoreManager provideAjnScoreManager(AJN_ScoreManagerImpl scoreManager) {
		return scoreManager;
	}
	/** MODIFICHE PROGETTO DI TESI AJN*/
}
