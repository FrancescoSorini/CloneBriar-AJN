package org.briarproject.briar.topic;

import org.briarproject.briar.api.topic.AJN_TopicManager;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AJN_TopicModule {

	public static class EagerSingletons {
		@Inject
		AJN_TopicManager topicManager;
	}

	@Provides
	@Singleton
	AJN_TopicManager provideAjnTopicManager(AJN_TopicManagerImpl topicManager) {
		return topicManager;
	}
}