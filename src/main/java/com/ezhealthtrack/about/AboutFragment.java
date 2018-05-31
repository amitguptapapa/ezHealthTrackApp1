package com.ezhealthtrack.about;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ezhealthtrack.EzFragment;
import com.ezhealthtrack.R;
import com.ezhealthtrack.dialogs.PrivacyPolicy;
import com.ezhealthtrack.dialogs.SupportDialog;
import com.ezhealthtrack.dialogs.TermsOfUse;

public class AboutFragment extends EzFragment {

	private TextView txtVersion;
	private TextView txtTerms;
	private TextView txtPrivacyPolicy;

	private Button btnRate;
	private Button btnSupport;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		final View view = inflater.inflate(R.layout.about_fragment, container,
				false);
		super.onCreateView(inflater, container, savedInstanceState);
		// assigning id's
		txtVersion = (TextView) view.findViewById(R.id.txt_version);
		txtTerms = (TextView) view.findViewById(R.id.txt_terms);
		txtPrivacyPolicy = (TextView) view
				.findViewById(R.id.txt_privacy_policy);

		btnRate = (Button) view.findViewById(R.id.btn_rate_app);
		btnSupport = (Button) view.findViewById(R.id.btn_support);

		// Setting text
		txtVersion.setText(Html.fromHtml("Version 9"));
		txtTerms.setText(Html.fromHtml("<u>Terms of Use</u>"));
		txtPrivacyPolicy.setText(Html.fromHtml("<u>Privacy Policy</u>"));

		// action
		txtTerms.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				actionTermsOfUseDialog(view);
			}
		});

		txtPrivacyPolicy.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				actionPrivacyPolicy(view);
			}
		});

		btnRate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				// redirect to google play
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setData(Uri
						.parse("market://details?id=com.ezhealthtrack"));
				startActivity(intent);
			}
		});

		btnSupport.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				actionSupportDialog(view);
			}
		});
		return view;

	}

	@Override
	public void onActivityCreated(final Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onSaveInstanceState(final Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	public void actionSupportDialog(View view) {
		new SupportDialog(getActivity());
	}

	public void actionTermsOfUseDialog(View view) {
		new TermsOfUse(getActivity());
	}

	public void actionPrivacyPolicy(View view) {
		new PrivacyPolicy(getActivity());
	}
}
