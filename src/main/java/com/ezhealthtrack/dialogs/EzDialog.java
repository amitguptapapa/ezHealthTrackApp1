package com.ezhealthtrack.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;

import com.ezhealthtrack.R;

public class EzDialog {

	public static Dialog getDialog(Context context, int resourceId, String title) {

		final Dialog dialog = new Dialog(context);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(resourceId);

		TextView titleView = (TextView) dialog
				.findViewById(R.id.id_header_title);
		titleView.setText(title);

		dialog.setCancelable(true);
		dialog.findViewById(R.id.btn_close).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						dialog.dismiss();

					}
				});
		return dialog;
	}

	// public static void dialogDetailAllergies(Context context, AllergyModel
	// model) {
	//
	// final Dialog loaddialog = Util.showLoadDialog(context);
	// DisplayMetrics metrics = context.getResources().getDisplayMetrics();
	// int width = (int) (metrics.widthPixels);
	// int height = (int) (metrics.heightPixels);
	//
	// final Dialog dialog = new Dialog(context);
	// dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	// dialog.setContentView(R.layout.dialog_mrecords_allergies);
	// dialog.getWindow().setBackgroundDrawable(
	// new ColorDrawable(android.graphics.Color.TRANSPARENT));
	// LayoutParams params = dialog.getWindow().getAttributes();
	// dialog.getWindow().setAttributes(
	// (android.view.WindowManager.LayoutParams) params);
	//
	// TextView header = (TextView) dialog.findViewById(R.id.id_header_text);
	// header.setText("Allergies");
	//
	// ListView listAllergies = (ListView) dialog
	// .findViewById(R.id.list_allergy);
	// View listFooter = ((LayoutInflater) context
	// .getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
	// R.layout.list_progressbar, null, false);
	// listAllergies.addFooterView(listFooter);
	//
	// AllergiesAllergyDetailAdapter adapter;
	// adapter = new AllergiesAllergyDetailAdapter(context, model.getData());
	// listAllergies.setAdapter(adapter);
	//
	// dialog.setCancelable(true);
	// dialog.findViewById(R.id.btn_close).setOnClickListener(
	// new OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	// dialog.dismiss();
	//
	// }
	// });
	// loaddialog.dismiss();
	// dialog.show();
	// dialog.getWindow().setLayout((6 * width) / 7, (4 * height) / 5);
	// }

}
