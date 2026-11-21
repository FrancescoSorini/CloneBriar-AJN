package org.briarproject.briar.android.ajn;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.briarproject.briar.R;
import org.briarproject.briar.api.identity.AJN_Author;
import org.briarproject.briar.api.identity.AJN_Role;
import org.briarproject.briar.android.activity.ActivityComponent;
import org.briarproject.briar.android.fragment.BaseFragment;
import org.briarproject.nullsafety.MethodsNotNullByDefault;
import org.briarproject.nullsafety.ParametersNotNullByDefault;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

@MethodsNotNullByDefault
@ParametersNotNullByDefault
public class AJN_ProfileFragment extends BaseFragment {

	@Inject
	ViewModelProvider.Factory viewModelFactory;

	private AJN_ProfileViewModel viewModel;

	private TextView textAuthorName;
	private TextView textRoleBadge;
	private TextView textScore;
	private ProgressBar progressScore;
	private TextView textVote;
	private TextView textEvaluate;
	private TextView textSensitive;
	private TextView textRateLimit;

	public static AJN_ProfileFragment newInstance() {
		return new AJN_ProfileFragment();
	}

	@Override
	public String getUniqueTag() {
		return "AJN_ProfileFragment";
	}

	@Override
	public void injectFragment(ActivityComponent component) {
		component.inject(this);
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
			@Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_ajn_profile, container, false);

		textAuthorName = view.findViewById(R.id.textAuthorName);
		textRoleBadge = view.findViewById(R.id.textRoleBadge);
		textScore = view.findViewById(R.id.textScore);
		progressScore = view.findViewById(R.id.progressScore);
		textVote = view.findViewById(R.id.textVotePermission);
		textEvaluate = view.findViewById(R.id.textEvaluateUsers);
		textSensitive = view.findViewById(R.id.textSensitiveInfoPermission);
		textRateLimit = view.findViewById(R.id.textRateLimit);

		return view;
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		// Inizializza il ViewModel
		viewModel = new ViewModelProvider(this, viewModelFactory).get(AJN_ProfileViewModel.class);

		// Osserva i dati caricati dal Database e aggiorna la grafica
		viewModel.getAjnAuthor().observe(getViewLifecycleOwner(), this::updateUi);
	}

	private void updateUi(AJN_Author author) {
		// Nome Utente
		textAuthorName.setText(author.getName());

		// Ruolo e Badge
		AJN_Role role = author.getRole();
		textRoleBadge.setText("RUOLO: " + role.name());

		// Score e ProgressBar
		textScore.setText("Punteggio Score: " + author.getScore() + " / " + AJN_Role.TRUSTED_THRESHOLD);
		progressScore.setMax(AJN_Role.TRUSTED_THRESHOLD);
		progressScore.setProgress(author.getScore());

		// Permessi
		textVote.setText(author.canVoteInfo() ?
				"• Votazione Post: Abilitato" : "• Votazione Post: Disabilitato (SANDBOX)");

		textEvaluate.setText(author.canEvaluateUser() ?
				"• Valutazione Utenti: Abilitato" : "• Valutazione Utenti: Disabilitato (SANDBOX)");

		textSensitive.setText(author.canAccessSensitiveInfo() ?
				"• Info Sensibili: Abilitato" : "• Info Sensibili: Bloccato");

		textRateLimit.setText(author.hasRateLimit() ?
				"• Rate Limit Post: Attivo (SANDBOX)" : "• Rate Limit Post: Nessuna Limitazione");
	}

	@Override
	public void onStart() {
		super.onStart();
		requireActivity().setTitle("Profilo AJN");
	}
}