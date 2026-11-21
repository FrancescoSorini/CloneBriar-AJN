package org.briarproject.briar.android.ajn;

import android.app.Application;

import org.briarproject.bramble.api.db.DatabaseExecutor;
import org.briarproject.bramble.api.db.DbException;
import org.briarproject.bramble.api.db.TransactionManager;
import org.briarproject.bramble.api.identity.IdentityManager;
import org.briarproject.bramble.api.identity.LocalAuthor;
import org.briarproject.bramble.api.lifecycle.LifecycleManager;
import org.briarproject.bramble.api.system.AndroidExecutor;
import org.briarproject.briar.api.identity.AJN_Author;
import org.briarproject.briar.api.identity.AJN_ScoreManager;
import org.briarproject.briar.android.viewmodel.DbViewModel;
import org.briarproject.nullsafety.MethodsNotNullByDefault;
import org.briarproject.nullsafety.ParametersNotNullByDefault;

import java.util.concurrent.Executor;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

@MethodsNotNullByDefault
@ParametersNotNullByDefault
public class AJN_ProfileViewModel extends DbViewModel {

	private final IdentityManager identityManager;
	private final AJN_ScoreManager ajnScoreManager;

	private final MutableLiveData<AJN_Author> ajnAuthorLiveData = new MutableLiveData<>();

	@Inject
	public AJN_ProfileViewModel(
			Application application,
			@DatabaseExecutor Executor dbExecutor,
			LifecycleManager lifecycleManager,
			TransactionManager db,
			AndroidExecutor androidExecutor,
			IdentityManager identityManager,
			AJN_ScoreManager ajnScoreManager) {
		super(application, dbExecutor, lifecycleManager, db, androidExecutor);
		this.identityManager = identityManager;
		this.ajnScoreManager = ajnScoreManager;

		// Carica i dati del profilo locale all'avvio
		loadProfile();
	}

	public LiveData<AJN_Author> getAjnAuthor() {
		return ajnAuthorLiveData;
	}

	public void loadProfile() {
		// Esegue la lettura nel thread del Database in background
		runOnDbThread(() -> {
			try {
				// 1. Ottiene l'identità dell'utente locale di Briar
				LocalAuthor localAuthor = identityManager.getLocalAuthor();

				// 2. Ottiene l'AJN_Author (con score e ruolo) dal database
				AJN_Author ajnAuthor = ajnScoreManager.getAjnAuthor(localAuthor);

				// 3. Invia il risultato alla grafica (UI Thread)
				ajnAuthorLiveData.postValue(ajnAuthor);
			} catch (DbException e) {
				handleException(e);
			}
		});
	}
}