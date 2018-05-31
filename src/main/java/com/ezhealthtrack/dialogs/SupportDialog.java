package com.ezhealthtrack.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.text.Html;
import android.widget.TextView;

import com.ezhealthtrack.R;

public class SupportDialog extends Dialog {
	public SupportDialog(Context context) {
		super(context);
		final Dialog dialog = EzDialog.getDialog(context,
				R.layout.support_dialog, "Support");

		// set id's
		TextView txtEmail = (TextView) dialog.findViewById(R.id.txt_email);
		TextView txtPhone = (TextView) dialog.findViewById(R.id.txt_phone);

		// set text
		txtEmail.setText(Html
				.fromHtml("<b>Email us at</b> support@ezhealthtrack.com"));
		txtPhone.setText(Html
				.fromHtml("<b>Phone</b> +91 80-87-343840 or +91 98-71-154389"));
		dialog.show();
	}
}
