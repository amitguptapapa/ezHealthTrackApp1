package com.ezhealthtrack.dialogs;

import android.app.Dialog;
import android.content.Context;

import com.ezhealthtrack.R;

public class TermsOfUse extends Dialog {

	public TermsOfUse(Context context) {
		super(context);
		final Dialog dialog = EzDialog.getDialog(context,
				R.layout.termsofuse_dialog, "Terms & Conditions");

		dialog.show();
	}
}
