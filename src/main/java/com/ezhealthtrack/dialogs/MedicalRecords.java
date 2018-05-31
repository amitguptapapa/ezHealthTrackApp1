package com.ezhealthtrack.dialogs;

import java.text.ParseException;
import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.ezhealthrack.api.APIs;
import com.ezhealthtrack.EzApp;
import com.ezhealthtrack.R;
import com.ezhealthtrack.adapter.AllergiesAllergyAdapter;
import com.ezhealthtrack.adapter.AllergiesAllergyDetailAdapter;
import com.ezhealthtrack.model.AllergyModel;
import com.ezhealthtrack.model.Document;
import com.ezhealthtrack.model.MedRecClinicalLab;
import com.ezhealthtrack.model.MedRecEKG;
import com.ezhealthtrack.model.MedRecPresModel;
import com.ezhealthtrack.model.MedRecRadiology;
import com.ezhealthtrack.model.MedRecRadiology.Datum;
import com.ezhealthtrack.model.VitalModel;
import com.ezhealthtrack.util.Util;

public class MedicalRecords {

	public static void dialogEditAllergies(Context context, AllergyModel model) {
		final Dialog loaddialog = Util.showLoadDialog(context);
		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		int width = (int) (metrics.widthPixels);
		int height = (int) (metrics.heightPixels);
		final Dialog dialogEditAllergies = new Dialog(context);
		dialogEditAllergies.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialogEditAllergies.setContentView(R.layout.dialog_mrecords_allergies);
		dialogEditAllergies.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		LayoutParams params = dialogEditAllergies.getWindow().getAttributes();
		dialogEditAllergies.getWindow().setAttributes(
				(android.view.WindowManager.LayoutParams) params);
		ListView listAllergies = (ListView) dialogEditAllergies
				.findViewById(R.id.list_allergy);
		View listFooter = ((LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
				R.layout.list_progressbar, null, false);
		listAllergies.addFooterView(listFooter);

		AllergiesAllergyAdapter adapter;
		adapter = new AllergiesAllergyAdapter(context,
				R.layout.row_mrecords_allergies, model.getData(),
				dialogEditAllergies);
		listAllergies.setAdapter(adapter);

		dialogEditAllergies.setCancelable(false);
		dialogEditAllergies.findViewById(R.id.txt_close).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						dialogEditAllergies.dismiss();

					}
				});
		loaddialog.dismiss();
		dialogEditAllergies.show();
		dialogEditAllergies.getWindow().setLayout((6 * width) / 7,
				(4 * height) / 5);
	}

	public static void dialogDetailAllergies(Context context, AllergyModel model) {
		final Dialog loading = Util.showLoadDialog(context);
		final Dialog dialog = EzDialog.getDialog(context,
				R.layout.dialog_mrecords_allergies, "Allergies");

		ListView listAllergies = (ListView) dialog
				.findViewById(R.id.list_allergy);
		View listFooter = ((LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
				R.layout.list_progressbar, null);
		listAllergies.addFooterView(listFooter);

		AllergiesAllergyDetailAdapter adapter;
		adapter = new AllergiesAllergyDetailAdapter(context, model.getData());
		listAllergies.setAdapter(adapter);
		loading.dismiss();
		dialog.show();
	}

	public static void dialogDetailVitals(Context context, VitalModel model) {
		final Dialog loading = Util.showLoadDialog(context);
		final Dialog dialog = EzDialog.getDialog(context,
				R.layout.dialog_mrecords_vitals, "Vitals");
		TextView details = (TextView) dialog.findViewById(R.id.txt_vitals);
		details.setText(Html.fromHtml(model.getDataString()));
		loading.dismiss();
		dialog.show();
	}

	public static void dialogDetailPrescription(Context context,
			MedRecPresModel model) {
		final Dialog loading = Util.showLoadDialog(context);
		final Dialog dialog = EzDialog.getDialog(context,
				R.layout.dialog_mrecords_prescriptions, "Prescriptions");
		TextView details = (TextView) dialog
				.findViewById(R.id.txt_prescription);
		details.setText(Html.fromHtml(model.getDataString()));
		loading.dismiss();
		dialog.show();
	}

	public static void dialogDetailRadiology(Context context,
			MedRecRadiology model) {
		final Dialog loading = Util.showLoadDialog(context);
		final Dialog dialog = EzDialog.getDialog(context,
				R.layout.dialog_mrecords_radiology, "Radiology");

		LinearLayout llRadiology = (LinearLayout) dialog
				.findViewById(R.id.ll_radiology);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		for (int i = 0; i < model.getData().size(); i++) {
			Datum data = model.getData().get(i);
			final View v = inflater.inflate(R.layout.medrec_table, null);
			TableLayout tbleView = (TableLayout) v.findViewById(R.id.table);
			if (data.getDocuments().size() > 0) {
				MedicalRecords.labTable(tbleView, data.getDocuments(), context);
			} else {
				tbleView.setVisibility(View.GONE);
			}
			((TextView) v.findViewById(R.id.txt_radiology)).setText(Html
					.fromHtml(data.getRadiologyString()));
			llRadiology.addView(v);
		}
		loading.dismiss();
		dialog.show();
	}

	public static void dialogDetailClinicalLab(Context context,
			MedRecClinicalLab model) {
		final Dialog loading = Util.showLoadDialog(context);
		final Dialog dialog = EzDialog.getDialog(context,
				R.layout.dialog_mrecords_clinical_lab, "Clinical Labs");

		LinearLayout llClinicalLab = (LinearLayout) dialog
				.findViewById(R.id.ll_clinicalLab);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		for (int i = 0; i < model.getData().size(); i++) {
			com.ezhealthtrack.model.MedRecClinicalLab.Datum data = model
					.getData().get(i);
			final View v = inflater.inflate(R.layout.medrec_table, null);

			TableLayout tbleView = (TableLayout) v.findViewById(R.id.table);
			if (data.getDocuments().size() > 0) {
				MedicalRecords.labTable(tbleView, data.getDocuments(), context);
			} else {
				tbleView.setVisibility(View.GONE);
			}
			((TextView) v.findViewById(R.id.txt_radiology)).setText(Html
					.fromHtml(data.getCinicalLabString()));
			llClinicalLab.addView(v);
		}
		loading.dismiss();
		dialog.show();
	}

	public static void dialogDetailEkg(Context context, MedRecEKG model) {
		final Dialog loading = Util.showLoadDialog(context);
		final Dialog dialog = EzDialog.getDialog(context,
				R.layout.dialog_mrecords_ekg, "EKG / ECG");

		LinearLayout llEKG = (LinearLayout) dialog.findViewById(R.id.ll_ekg);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		for (int i = 0; i < model.getData().size(); i++) {
			MedRecEKG.Datum data = model.getData().get(i);
			final View v = inflater.inflate(R.layout.medrec_table, null);
			TableLayout tbleView = (TableLayout) v.findViewById(R.id.table);
			if (data.getDocuments().size() > 0) {
				MedicalRecords.labTable(tbleView, data.getDocuments(), context);
			} else {
				tbleView.setVisibility(View.GONE);
			}
			((TextView) v.findViewById(R.id.txt_radiology)).setText(Html
					.fromHtml(data.getEKGString()));
			llEKG.addView(v);
		}
		loading.dismiss();
		dialog.show();
	}

	private static void labTable(TableLayout tl,
			ArrayList<Document> arrRadiDocuments, Context context) {
		tl.removeAllViews();
		if (arrRadiDocuments.size() > 0) {
			final LayoutParams params = new LayoutParams(
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
			final TableRow row = (TableRow) ((Activity) context)
					.getLayoutInflater().inflate(R.layout.row_medrec_table,
							null, false);
			row.setLayoutParams(params);
			TextView txtDocumentName = (TextView) row
					.findViewById(R.id.txt_name);
			TextView txtDocumentType = (TextView) row
					.findViewById(R.id.txt_type);
			TextView txtUploadDate = (TextView) row
					.findViewById(R.id.txt_upload_date);
			txtDocumentName.setText(Html.fromHtml("<b>Document Name</b>"));
			txtDocumentType.setText(Html.fromHtml("<b>Document Type</b>"));
			txtUploadDate.setText(Html.fromHtml("<b>Upload Date</b>"));
			tl.addView(row);
			for (final Document doc : arrRadiDocuments) {
				final LayoutParams params1 = new LayoutParams(
						android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
						android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
				final TableRow row1 = (TableRow) ((Activity) context)
						.getLayoutInflater().inflate(R.layout.row_medrec_table,
								null, false);
				row1.setLayoutParams(params1);
				txtDocumentName = (TextView) row1.findViewById(R.id.txt_name);
				txtDocumentType = (TextView) row1.findViewById(R.id.txt_type);
				txtUploadDate = (TextView) row1
						.findViewById(R.id.txt_upload_date);
				txtDocumentName.setText(Html.fromHtml("<a href='" + APIs.VIEW()
						+ doc.getDid() + "'>" + doc.getDName() + "</a>"));
				txtDocumentName.setClickable(true);
				txtDocumentName.setMovementMethod(LinkMovementMethod
						.getInstance());
				txtDocumentType.setText(doc.getDType());

				String a;
				try {
					a = EzApp.sdfemmm
							.format(EzApp.sdfyymmddhhmmss
									.parse(doc.getDate()));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					a = "";
				}
				txtUploadDate.setText(a);
				tl.addView(row1);
			}
			tl.setVisibility(View.VISIBLE);
		} else {
			tl.setVisibility(View.GONE);
		}
	}

}
