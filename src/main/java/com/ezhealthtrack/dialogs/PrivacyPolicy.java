package com.ezhealthtrack.dialogs;

import android.app.Dialog;
import android.content.Context;

import com.ezhealthtrack.R;

public class PrivacyPolicy extends Dialog {

	public PrivacyPolicy(Context context) {
		super(context);
		final Dialog dialog = EzDialog.getDialog(context,
				R.layout.privacy_policy_dialog, "Privacy Policy");

		dialog.show();
	}
}
