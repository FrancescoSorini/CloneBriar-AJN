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
import org.briarproject.briar.android.fragment.BaseFragment;
import org.briarproject.nullsafety.MethodsNotNullByDefault;
import org.briarproject.nullsafety.ParametersNotNullByDefault;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

@MethodsNotNullByDefault
@ParametersNotNullByDefault
public class AJN_ProfileFragment extends BaseFragment {

	public static AJN_ProfileFragment newInstance() {
		return new AJN_ProfileFragment();
	}

	@Override
	public String getUniqueTag() {
		return "AjnProfileFragment";
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
			@Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_ajn_profile, container, false);

		TextView textAuthorName = view.findViewById(R.id.textAuthorName);
		TextView textRoleBadge = view.findViewById(R.id.textRoleBadge);
		TextView textScore = view.findViewById(R.id.textScore);
		ProgressBar progressScore = view.findViewById(R.id.progressScore);
		TextView textVote = view.findViewById(R.id.textVotePermission);
		TextView textSensitive = view.findViewById(R.id.textSensitiveInfoPermission);
		TextView textRateLimit = view.findViewById(R.id.textRateLimit);

		// Esempio di aggiornamento dati (connesso al ViewModel)
		// viewModel.getAjnAuthor().observe(getViewLifecycleOwner(), ajnAuthor -> ...);

		return view;
	}

	@Override
	public void onStart() {
		super.onStart();
		requireActivity().setTitle("Profilo AJN");
	}
}