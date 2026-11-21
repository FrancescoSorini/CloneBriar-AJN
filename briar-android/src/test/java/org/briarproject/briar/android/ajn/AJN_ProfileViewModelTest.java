package org.briarproject.briar.android.ajn;

import android.app.Application;

import org.briarproject.bramble.api.db.DbException;
import org.briarproject.bramble.api.db.TransactionManager;
import org.briarproject.bramble.api.identity.IdentityManager;
import org.briarproject.bramble.api.identity.LocalAuthor;
import org.briarproject.bramble.api.lifecycle.LifecycleManager;
import org.briarproject.bramble.api.system.AndroidExecutor;
import org.briarproject.bramble.test.BrambleMockTestCase;
import org.briarproject.bramble.test.ImmediateExecutor;
import org.briarproject.briar.api.identity.AJN_Author;
import org.briarproject.briar.api.identity.AJN_ScoreManager;
import org.jmock.Expectations;
import org.jmock.imposters.ByteBuddyClassImposteriser;
import org.junit.Rule;
import org.junit.Test;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import static org.briarproject.bramble.test.TestUtils.getLocalAuthor;
import static org.briarproject.briar.android.viewmodel.LiveDataTestUtil.getOrAwaitValue;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

public class AJN_ProfileViewModelTest extends BrambleMockTestCase {
	@Rule
	public final InstantTaskExecutorRule testRule =
			new InstantTaskExecutorRule();

	private final Application app;
	private final LifecycleManager lifecycleManager =
			context.mock(LifecycleManager.class);
	private final TransactionManager db = context.mock(TransactionManager.class);
	private final AndroidExecutor androidExecutor =
			context.mock(AndroidExecutor.class);
	private final IdentityManager identityManager =
			context.mock(IdentityManager.class);
	private final AJN_ScoreManager scoreManager =
			context.mock(AJN_ScoreManager.class);

	public AJN_ProfileViewModelTest() {
		context.setImposteriser(ByteBuddyClassImposteriser.INSTANCE);
		app = context.mock(Application.class);
	}

	@Test
	public void testLoadsLocalProfileOnConstruction() throws Exception {
		LocalAuthor localAuthor = getLocalAuthor();
		AJN_Author expected = new AJN_Author(localAuthor, 58, false);
		context.checking(new Expectations() {{
			oneOf(lifecycleManager).waitForDatabase();
			oneOf(identityManager).getLocalAuthor();
			will(returnValue(localAuthor));
			oneOf(scoreManager).getAjnAuthor(localAuthor);
			will(returnValue(expected));
		}});

		AJN_ProfileViewModel viewModel = createViewModel();
		assertSame(expected, getOrAwaitValue(viewModel.getAjnAuthor()));
	}

	@Test
	public void testDatabaseExceptionIsHandled() throws Exception {
		DbException exception = new DbException();
		LocalAuthor localAuthor = getLocalAuthor();
		context.checking(new Expectations() {{
			oneOf(lifecycleManager).waitForDatabase();
			oneOf(identityManager).getLocalAuthor();
			will(returnValue(localAuthor));
			oneOf(scoreManager).getAjnAuthor(localAuthor);
			will(throwException(exception));
			allowing(androidExecutor).runOnUiThread(with(any(Runnable.class)));
		}});

		AJN_ProfileViewModel viewModel = createViewModel();
		// The failed read is reported through handleException; no profile is posted.
		assertNull(viewModel.getAjnAuthor().getValue());
	}

	private AJN_ProfileViewModel createViewModel() {
		return new AJN_ProfileViewModel(app, new ImmediateExecutor(),
				lifecycleManager, db, androidExecutor, identityManager,
				scoreManager);
	}
}
