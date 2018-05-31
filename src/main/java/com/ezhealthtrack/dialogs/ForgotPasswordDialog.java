package com.ezhealthtrack.dialogs;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

import com.ezhealthrack.api.APIs;
import com.ezhealthtrack.EzApp;
import com.ezhealthtrack.R;
import com.ezhealthtrack.util.NetworkCallController.OnResponse;
import com.ezhealthtrack.util.Util;
import com.flurry.android.FlurryAgent;

public class ForgotPasswordDialog extends Dialog {

	public ForgotPasswordDialog(Context context) {
		super(context);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		final Dialog dialog = EzDialog.getDialog(context,
				R.layout.dialog_forgotpassword, "Forgot Password?");
		this.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		final EditText email = (EditText) dialog.findViewById(R.id.edit_email);
		final EditText username = (EditText) dialog
				.findViewById(R.id.edit_username);
		dialog.findViewById(R.id.btn_send).setOnClickListener(
				new android.view.View.OnClickListener() {

					@Override
					public void onClick(final View v) {
						FlurryAgent
								.logEvent("ForgotPasswordDialog - Send Button Clicked");
						final Dialog loaddialog = Util
								.showLoadDialog(getContext());
						if (Util.isEmptyString(email.getText().toString())
								&& Util.isEmptyString(username.getText()
										.toString())) {
							Util.Alertdialog(getContext(),
									"Please enter email or username");
						} else if (!Util.isEmptyString(email.getText()
								.toString())) {
							forgotPassword("email", email.getText().toString(),
									ForgotPasswordDialog.this, getContext());
						} else {
							forgotPassword("username", username.getText()
									.toString(), ForgotPasswordDialog.this,
									getContext());
						}
						loaddialog.dismiss();

					}
				});
		dialog.setCancelable(false);
		dialog.show();
	}

	private void forgotPassword(final String type, final String s,
			final ForgotPasswordDialog dialog, Context activity) {
		final String url = APIs.FORGOT_PASSWORD();
		final HashMap<String, String> params = new HashMap<String, String>();
		// loginParams.put("format", "json");
		params.put("type", type);
		if (type.equalsIgnoreCase("email")) {
			params.put("email", s);
		} else {
			params.put("username", s);
		}
		EzApp.networkController.networkCall(getContext(), url,
				params, new OnResponse() {

					@Override
					public void onResponseListner(String response) {
						try {
							Util.Alertdialog(getContext(), new JSONObject(
									response).getString("m"));
							if (new JSONObject(response).getString("s")
									.equalsIgnoreCase("200")) {
								dialog.dismiss();
							}
						} catch (final JSONException e) {
							e.printStackTrace();
						}

					}
				});

	}

}
