package com.ezhealthtrack.one;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ezhealthrack.api.APIs;
import com.ezhealthtrack.EzApp;
import com.ezhealthtrack.R;
import com.ezhealthtrack.model.Document;
import com.ezhealthtrack.model.MedicineModel;
import com.ezhealthtrack.physiciansoap.PhysicianSoapActivityMain;
import com.ezhealthtrack.util.Constants;
import com.ezhealthtrack.util.Util;

public class EzCommonViews {
	private static SharedPreferences sharedPref;
	private static ArrayList<Document> arrDocuments = new ArrayList<Document>();

	// private static VisitNotesModel visitNotes = new VisitNotesModel();

	// Radiology Table for Dialogs without delete action
	public static void radiologyTable(Dialog dialog,
			ArrayList<Document> arrDocuments, Activity context) {
		final TableLayout tl = (TableLayout) dialog.findViewById(R.id.table);
		tl.removeAllViews();
		ArrayList<Document> arrRadiDocuments = new ArrayList<Document>();
		for (final Document doc : arrDocuments) {
			if (doc.getSection().equalsIgnoreCase("radiology"))
				arrRadiDocuments.add(doc);
		}
		if (arrRadiDocuments.size() > 0) {
			final LayoutParams params = new LayoutParams(
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
			final TableRow row = (TableRow) context.getLayoutInflater()
					.inflate(R.layout.row_document_past, null, false);
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
				final TableRow row1 = (TableRow) context.getLayoutInflater()
						.inflate(R.layout.row_document_past, null, false);
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
					a = EzApp.sdfemmm.format(EzApp.sdfyymmddhhmmss.parse(doc
							.getDate()));
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

	// Radiology Table for activities without delete action
	// Temporarily disabled
	public static void radiologyTable(Activity context) {
		final TableLayout tl = (TableLayout) context.findViewById(R.id.table);
		tl.removeAllViews();
		ArrayList<Document> arrRadiDocuments = new ArrayList<Document>();
		for (final Document doc : arrDocuments) {
			if (doc.getSection().equalsIgnoreCase("radiology"))
				arrRadiDocuments.add(doc);
		}
		if (arrRadiDocuments.size() > 0) {
			final LayoutParams params = new LayoutParams(
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
			final TableRow row = (TableRow) context.getLayoutInflater()
					.inflate(R.layout.row_document_past, null, false);
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
				final TableRow row1 = (TableRow) context.getLayoutInflater()
						.inflate(R.layout.row_document_past, null, false);
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
					a = EzApp.sdfemmm.format(EzApp.sdfyymmddhhmmss.parse(doc
							.getDate()));
				} catch (ParseException e) {
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

	// Radiology Table for activities with delete action
	public static void radiologyTable(final Activity context,
			final ArrayList<Document> arrDocuments) {
		final TableLayout tl = (TableLayout) context.findViewById(R.id.table);
		tl.removeAllViews();
		ArrayList<Document> arrRadiDocuments = new ArrayList<Document>();
		for (final Document doc : arrDocuments) {
			if (doc.getSection().equalsIgnoreCase("radiology"))
				arrRadiDocuments.add(doc);
		}
		if (arrRadiDocuments.size() > 0) {
			final TableRow row = (TableRow) context.getLayoutInflater()
					.inflate(R.layout.row_document, null, false);
			tl.addView(row);

			for (final Document doc : arrRadiDocuments) {
				final TableRow row1 = (TableRow) context.getLayoutInflater()
						.inflate(R.layout.row_document, null, false);

				TextView txtAction = (TextView) row1
						.findViewById(R.id.txt_action);
				TextView txtDocumentName = (TextView) row1
						.findViewById(R.id.txt_name);
				TextView txtDocumentType = (TextView) row1
						.findViewById(R.id.txt_type);
				TextView txtUploadDate = (TextView) row1
						.findViewById(R.id.txt_upload_date);
				txtAction
						.setText(Html
								.fromHtml("<u><font color='#FF0000'>Delete</font></u>"));
				txtDocumentName.setText(Html.fromHtml("<a href='" + APIs.VIEW()
						+ doc.getDid() + "'>" + doc.getDName() + "</a>"));
				txtDocumentName.setClickable(true);
				txtDocumentName.setMovementMethod(LinkMovementMethod
						.getInstance());
				txtDocumentType.setText(doc.getDType());

				String a;
				try {
					a = EzApp.sdfemmm.format(EzApp.sdfyymmddhhmmss.parse(doc
							.getDate()));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					a = "";
				}
				Date date = new Date();
				String z = EzApp.sdfemmm.format(date);
				if (Util.isEmptyString(txtUploadDate.getText().toString())) {
					txtUploadDate.setText(z);
				} else {
					txtUploadDate.setText(a);
				}
				txtAction.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
								context);

						// set title
						alertDialogBuilder
								.setTitle("Do you want to delete this Document ?");
						// set dialog message
						alertDialogBuilder
								.setCancelable(false)
								.setPositiveButton("Yes",
										new DialogInterface.OnClickListener() {

											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {
												deleteDocument(doc, context,
														arrDocuments);

											}

										})
								.setNegativeButton("No",
										new DialogInterface.OnClickListener() {
											@Override
											public void onClick(
													final DialogInterface dialog,
													final int id) {
												// if this button is clicked,
												// just close
												// the dialog box and do nothing
												dialog.cancel();
											}
										});

						final AlertDialog alertDialog = alertDialogBuilder
								.create();

						alertDialog.show();
					}
				});
				tl.addView(row1);
			}
			tl.setVisibility(View.VISIBLE);
		} else {
			tl.setVisibility(View.GONE);
		}
	}

	// Lab Table for Dialogs
	public static void labTable(Dialog dialog,
			ArrayList<Document> arrDocuments, Activity context) {
		final TableLayout tl = (TableLayout) dialog
				.findViewById(R.id.table_lab);
		tl.removeAllViews();
		ArrayList<Document> arrRadiDocuments = new ArrayList<Document>();
		for (final Document doc : arrDocuments) {
			if (doc.getSection().equalsIgnoreCase("labs"))
				arrRadiDocuments.add(doc);
			// Log.i("",doc.getSection());
		}
		if (arrRadiDocuments.size() > 0) {
			final LayoutParams params = new LayoutParams(
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
			final TableRow row = (TableRow) context.getLayoutInflater()
					.inflate(R.layout.row_document_past, null, false);
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
				final TableRow row1 = (TableRow) context.getLayoutInflater()
						.inflate(R.layout.row_document_past, null, false);
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
					a = EzApp.sdfemmm.format(EzApp.sdfyymmddhhmmss.parse(doc
							.getDate()));
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

	// Lab Table for activities without delete action
	// Temporarily disabled
	public static void labTable(Activity context) {
		final TableLayout tl = (TableLayout) context
				.findViewById(R.id.table_lab);
		tl.removeAllViews();
		ArrayList<Document> arrRadiDocuments = new ArrayList<Document>();
		for (final Document doc : arrDocuments) {
			if (doc.getSection().equalsIgnoreCase("labs"))
				arrRadiDocuments.add(doc);
		}
		if (arrRadiDocuments.size() > 0) {
			final LayoutParams params = new LayoutParams(
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
			final TableRow row = (TableRow) context.getLayoutInflater()
					.inflate(R.layout.row_document_past, null, false);
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
				final TableRow row1 = (TableRow) context.getLayoutInflater()
						.inflate(R.layout.row_document_past, null, false);
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
					a = EzApp.sdfemmm.format(EzApp.sdfyymmddhhmmss.parse(doc
							.getDate()));
				} catch (ParseException e) {
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

	// Lab Table with delete action
	public static void labTable(final Activity context,
			final ArrayList<Document> arrDocuments) {
		final TableLayout tl = (TableLayout) context
				.findViewById(R.id.table_lab);
		tl.removeAllViews();
		ArrayList<Document> arrRadiDocuments = new ArrayList<Document>();
		for (final Document doc : arrDocuments) {
			if (doc.getSection().equalsIgnoreCase("labs"))
				arrRadiDocuments.add(doc);
			// Log.i("",doc.getSection());
		}
		if (arrRadiDocuments.size() > 0) {
			final TableRow row = (TableRow) context.getLayoutInflater()
					.inflate(R.layout.row_document, null, false);
			tl.addView(row);

			for (final Document doc : arrRadiDocuments) {
				final TableRow row1 = (TableRow) context.getLayoutInflater()
						.inflate(R.layout.row_document, null, false);

				TextView txtAction = (TextView) row1
						.findViewById(R.id.txt_action);
				TextView txtDocumentName = (TextView) row1
						.findViewById(R.id.txt_name);
				TextView txtDocumentType = (TextView) row1
						.findViewById(R.id.txt_type);
				TextView txtUploadDate = (TextView) row1
						.findViewById(R.id.txt_upload_date);
				txtAction
						.setText(Html
								.fromHtml("<u><font color='#FF0000'>Delete</font></u>"));
				txtDocumentName.setText(Html.fromHtml("<a href='" + APIs.VIEW()
						+ doc.getDid() + "'>" + doc.getDName() + "</a>"));
				txtDocumentName.setClickable(true);
				txtDocumentName.setMovementMethod(LinkMovementMethod
						.getInstance());
				txtDocumentType.setText(doc.getDType());

				String a;
				try {
					a = EzApp.sdfemmm.format(EzApp.sdfyymmddhhmmss.parse(doc
							.getDate()));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					a = "";
				}
				Date date = new Date();
				String z = EzApp.sdfemmm.format(date);
				if (Util.isEmptyString(txtUploadDate.getText().toString())) {
					txtUploadDate.setText(z);
				} else {
					txtUploadDate.setText(a);
				}
				txtAction.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
								context);

						// set title
						alertDialogBuilder
								.setTitle("Do you want to delete this Document ?");
						// set dialog message
						alertDialogBuilder
								.setCancelable(false)
								.setPositiveButton("Yes",
										new DialogInterface.OnClickListener() {

											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {
												deleteDocument(doc, context,
														arrDocuments);
											}

										})
								.setNegativeButton("No",
										new DialogInterface.OnClickListener() {
											@Override
											public void onClick(
													final DialogInterface dialog,
													final int id) {
												// if this button is clicked,
												// just close
												// the dialog box and do nothing
												dialog.cancel();
											}
										});

						final AlertDialog alertDialog = alertDialogBuilder
								.create();

						alertDialog.show();
					}
				});
				tl.addView(row1);
			}
			tl.setVisibility(View.VISIBLE);
		} else {
			tl.setVisibility(View.GONE);
		}
	}

	// EKG Table for Dialogs
	public static void ekgTable(Dialog dialog,
			ArrayList<Document> arrDocuments, Activity context) {
		final TableLayout tl = (TableLayout) dialog
				.findViewById(R.id.table_ekg);
		tl.removeAllViews();
		ArrayList<Document> arrRadiDocuments = new ArrayList<Document>();
		for (final Document doc : arrDocuments) {
			if (doc.getSection().equalsIgnoreCase("ekg"))
				arrRadiDocuments.add(doc);
			// Log.i("",doc.getSection());
		}
		if (arrRadiDocuments.size() > 0) {
			final LayoutParams params = new LayoutParams(
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
			final TableRow row = (TableRow) context.getLayoutInflater()
					.inflate(R.layout.row_document_past, null, false);
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
				final TableRow row1 = (TableRow) context.getLayoutInflater()
						.inflate(R.layout.row_document_past, null, false);
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
					a = EzApp.sdfemmm.format(EzApp.sdfyymmddhhmmss.parse(doc
							.getDate()));
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

	// EKG Table for activities without delete action
	// Temporarily disabled
	public static void ekgTable(Activity context) {
		final TableLayout tl = (TableLayout) context
				.findViewById(R.id.table_ekg);
		tl.removeAllViews();
		ArrayList<Document> arrRadiDocuments = new ArrayList<Document>();
		for (final Document doc : arrDocuments) {
			if (doc.getSection().equalsIgnoreCase("ekg"))
				arrRadiDocuments.add(doc);
		}
		if (arrRadiDocuments.size() > 0) {
			final LayoutParams params = new LayoutParams(
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
			final TableRow row = (TableRow) context.getLayoutInflater()
					.inflate(R.layout.row_document_past, null, false);
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
				final TableRow row1 = (TableRow) context.getLayoutInflater()
						.inflate(R.layout.row_document_past, null, false);
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
					a = EzApp.sdfemmm.format(EzApp.sdfyymmddhhmmss.parse(doc
							.getDate()));
				} catch (ParseException e) {
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

	// EKG Table with delete action
	public static void ekgTable(final Activity context,
			final ArrayList<Document> arrDocuments) {
		final TableLayout tl = (TableLayout) context
				.findViewById(R.id.table_ekg);
		tl.removeAllViews();
		ArrayList<Document> arrRadiDocuments = new ArrayList<Document>();
		for (final Document doc : arrDocuments) {
			if (doc.getSection().equalsIgnoreCase("ekg"))
				arrRadiDocuments.add(doc);
		}
		if (arrRadiDocuments.size() > 0) {
			final TableRow row = (TableRow) context.getLayoutInflater()
					.inflate(R.layout.row_document, null, false);
			tl.addView(row);
			for (final Document doc : arrRadiDocuments) {
				final TableRow row1 = (TableRow) context.getLayoutInflater()
						.inflate(R.layout.row_document, null, false);

				TextView txtAction = (TextView) row1
						.findViewById(R.id.txt_action);
				TextView txtDocumentName = (TextView) row1
						.findViewById(R.id.txt_name);
				TextView txtDocumentType = (TextView) row1
						.findViewById(R.id.txt_type);
				TextView txtUploadDate = (TextView) row1
						.findViewById(R.id.txt_upload_date);
				txtAction
						.setText(Html
								.fromHtml("<u><font color='#FF0000'>Delete</font></u>"));
				txtDocumentName.setText(Html.fromHtml("<a href='" + APIs.VIEW()
						+ doc.getDid() + "'>" + doc.getDName() + "</a>"));
				txtDocumentName.setClickable(true);
				txtDocumentName.setMovementMethod(LinkMovementMethod
						.getInstance());
				txtDocumentType.setText(doc.getDType());

				String a;
				try {
					a = EzApp.sdfemmm.format(EzApp.sdfyymmddhhmmss.parse(doc
							.getDate()));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					a = "";
				}
				Date date = new Date();
				String z = EzApp.sdfemmm.format(date);
				if (Util.isEmptyString(txtUploadDate.getText().toString())) {
					txtUploadDate.setText(z);
				} else {
					txtUploadDate.setText(a);
				}

				txtAction.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
								context);

						// set title
						alertDialogBuilder
								.setTitle("Do you want to delete this Document ?");
						// set dialog message
						alertDialogBuilder
								.setCancelable(false)
								.setPositiveButton("Yes",
										new DialogInterface.OnClickListener() {

											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {
												deleteDocument(doc, context,
														arrDocuments);
											}

										})
								.setNegativeButton("No",
										new DialogInterface.OnClickListener() {
											@Override
											public void onClick(
													final DialogInterface dialog,
													final int id) {
												// if this button is clicked,
												// just close
												// the dialog box and do nothing
												dialog.cancel();
											}
										});

						final AlertDialog alertDialog = alertDialogBuilder
								.create();

						alertDialog.show();
					}
				});
				tl.addView(row1);
			}
			tl.setVisibility(View.VISIBLE);
		} else {
			tl.setVisibility(View.GONE);
		}
	}

	// Function to delete Radiology, Lab & EKG documents
	private static void deleteDocument(final Document doc,
			final Activity context, final ArrayList<Document> arrDocuments) {
		final String url = APIs.DELETE_DOCUMENT() + doc.getId();
		final Dialog loaddialog = Util.showLoadDialog(context
				.getApplicationContext());
		final StringRequest patientListRequest = new StringRequest(
				Request.Method.POST, url, new Response.Listener<String>() {
					@Override
					public void onResponse(final String response) {
						try {
							Log.i("delete document", response);
							final JSONObject jObj = new JSONObject(response);
							if (jObj.getString("s").equals("200")) {
								arrDocuments.remove(doc);
								radiologyTable(context, arrDocuments);
								labTable(context,arrDocuments);
								ekgTable(context,arrDocuments);
								Util.Alertdialog(context,
										"Document deleted successfully");
							} else {
								Util.Alertdialog(context,
										"There is some error while deleting, please try again.");
							}
						} catch (final JSONException e) {
							Util.Alertdialog(context,
									"There is some error while deleting, please try again.");
						}
						loaddialog.dismiss();
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(final VolleyError error) {
						Util.Alertdialog(context,
								"Network error, try again later!");

						error.printStackTrace();
						loaddialog.dismiss();
					}
				}) {
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				final HashMap<String, String> loginParams = new HashMap<String, String>();
				loginParams.put("auth-token", Util.getBase64String(EzApp.sharedPref
						.getString(Constants.USER_TOKEN, "")));
				return loginParams;
			}

			@Override
			protected Map<String, String> getParams() {
				final HashMap<String, String> loginParams = new HashMap<String, String>();
				loginParams.put("format", "json");
				return loginParams;
			}

		};
		patientListRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 5,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		EzApp.mVolleyQueue.add(patientListRequest);

	}

	// Prescription Table to display on all the places/activities except
	// Visit/SOAP Notes (for print screens)
	public static void prescriptionTable(Activity context) {
		final TableLayout tl = (TableLayout) context
				.findViewById(R.id.table_prescription);
		tl.removeAllViews();
		final LayoutParams params = new LayoutParams(
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		final TableRow row = (TableRow) context.getLayoutInflater().inflate(
				R.layout.row_prescription_table, null, false);
		row.setLayoutParams(params);
		TextView txtSno = (TextView) row.findViewById(R.id.txt_sno);
		TextView txtFormulation = (TextView) row
				.findViewById(R.id.txt_formulation);
		TextView txtDrugName = (TextView) row.findViewById(R.id.txt_name);
		TextView txtStrength = (TextView) row.findViewById(R.id.txt_strength);
		TextView txtUnitType = (TextView) row.findViewById(R.id.txt_unit);
		TextView txtRoute = (TextView) row.findViewById(R.id.txt_route);
		TextView txtFrequency = (TextView) row.findViewById(R.id.txt_frequency);
		TextView txtDays = (TextView) row.findViewById(R.id.txt_days);
		TextView txtQuantity = (TextView) row.findViewById(R.id.txt_quantity);
		TextView txtRefills = (TextView) row.findViewById(R.id.txt_refills);
		TextView txtRefillTime = (TextView) row
				.findViewById(R.id.txt_refill_time);
		TextView txtNotes = (TextView) row.findViewById(R.id.txt_notes);

		row.removeAllViews();
		txtSno.setText(Html.fromHtml("<b>S.No.</b>"));
		row.addView(txtSno);
		txtFormulation.setText(Html.fromHtml("<b>Formulation</b>"));
		row.addView(txtFormulation);
		txtDrugName.setText(Html.fromHtml("<b>Drug Name</b>"));
		row.addView(txtDrugName);
		txtStrength.setText(Html.fromHtml("<b>Strength</b>"));
		row.addView(txtStrength);
		txtUnitType.setText(Html.fromHtml("<b>Unit Type</b>"));
		row.addView(txtUnitType);
		txtRoute.setText(Html.fromHtml("<b>Route</b>"));
		row.addView(txtRoute);
		txtFrequency.setText(Html.fromHtml("<b>Frequency</b>"));
		row.addView(txtFrequency);
		txtDays.setText(Html.fromHtml("<b># of Days</b>"));
		row.addView(txtDays);
		txtQuantity.setText(Html.fromHtml("<b>Quantity</b>"));
		row.addView(txtQuantity);
		txtRefills.setText(Html.fromHtml("<b>Refills</b>"));
		row.addView(txtRefills);
		txtRefillTime.setText(Html.fromHtml("<b>Refill Time</b>"));
		row.addView(txtRefillTime);
		txtNotes.setText(Html.fromHtml("<b>Notes</b>"));
		row.addView(txtNotes);

		tl.addView(row);

		int i = 1;
		for (final MedicineModel doc : PhysicianSoapActivityMain.visitNotes.physicianPlanModel.prescription.arrMedicine) {

			if (doc.getMedicineString().length() > 0) {
				final LayoutParams params1 = new LayoutParams(
						android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
						android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
				final TableRow row1 = (TableRow) context.getLayoutInflater()
						.inflate(R.layout.row_prescription_table, null, false);
				row1.setLayoutParams(params1);
				txtSno = (TextView) row1.findViewById(R.id.txt_sno);
				txtFormulation = (TextView) row1
						.findViewById(R.id.txt_formulation);
				txtDrugName = (TextView) row1.findViewById(R.id.txt_name);
				txtStrength = (TextView) row1.findViewById(R.id.txt_strength);
				txtUnitType = (TextView) row1.findViewById(R.id.txt_unit);
				txtRoute = (TextView) row1.findViewById(R.id.txt_route);
				txtFrequency = (TextView) row1.findViewById(R.id.txt_frequency);
				txtDays = (TextView) row1.findViewById(R.id.txt_days);
				txtQuantity = (TextView) row1.findViewById(R.id.txt_quantity);
				txtRefills = (TextView) row1.findViewById(R.id.txt_refills);
				txtRefillTime = (TextView) row1
						.findViewById(R.id.txt_refill_time);
				txtNotes = (TextView) row1.findViewById(R.id.txt_notes);

				txtSno.setText(String.valueOf(i));
				txtFormulation.setText(Html.fromHtml(doc.formulations));
				txtDrugName.setText(Html.fromHtml(doc.name));
				txtStrength.setText(Html.fromHtml(doc.strength));
				txtUnitType.setText(Html.fromHtml(doc.unit));
				txtRoute.setText(Html.fromHtml(doc.route));
				txtFrequency.setText(Html.fromHtml(doc.frequency));
				txtDays.setText(Html.fromHtml(doc.times));
				txtQuantity.setText(Html.fromHtml(doc.quantity));
				txtRefills.setText(Html.fromHtml(doc.refills));
				txtRefillTime.setText(Html.fromHtml(doc.refillsTime));
				txtNotes.setText(Html.fromHtml(doc.notes));

				tl.addView(row1);

				tl.setVisibility(View.VISIBLE);
				i++;
			} else {
				tl.setVisibility(View.GONE);
			}
		}
	}

	// Prescription Table for Visit/SOAP notes
	// Temporarily disabled
	// start {

	// public static void prescriptionTableSOAPNotes(Activity context) {
	// final TableLayout tl = (TableLayout) context
	// .findViewById(R.id.table_prescription);
	// tl.removeAllViews();
	//
	// // if (visitNotes.physicianPlanModel.prescription.arrMedicine
	// // .size() < 1) {
	// // tl.setVisibility(View.GONE);
	// // return;
	// // }
	// final TableRow row = (TableRow) context.getLayoutInflater().inflate(
	// R.layout.row_prescription_table_vnotes, null, false);
	//
	// tl.addView(row);
	//
	// int i = 1;
	// for (final MedicineModel doc :
	// visitNotes.physicianPlanModel.prescription.arrMedicine) {
	//
	// if (doc.getMedicineString().length() > 0) {
	// final TableRow row1 = (TableRow) context.getLayoutInflater()
	// .inflate(R.layout.row_prescription_table_vnotes, null,
	// false);
	//
	// TextView txtSno = (TextView) row1.findViewById(R.id.txt_sno);
	// TextView txtFormulation = (TextView) row1
	// .findViewById(R.id.txt_formulation);
	// TextView txtDrugName = (TextView) row1
	// .findViewById(R.id.txt_name);
	// TextView txtDetails = (TextView) row1
	// .findViewById(R.id.txt_detail);
	// TextView txtFreqDetail = (TextView) row1
	// .findViewById(R.id.txt_frequency_detail);
	// TextView txtRefills = (TextView) row1
	// .findViewById(R.id.txt_refills);
	// TextView txtNotes = (TextView) row1
	// .findViewById(R.id.txt_notes);
	// TextView txtHistory = (TextView) row1
	// .findViewById(R.id.txt_history);
	//
	// txtSno.setText(String.valueOf(i));
	// txtFormulation.setText(Html.fromHtml(doc.formulations));
	// txtDrugName.setText(Html.fromHtml(doc.name));
	// txtDetails.setText(Html.fromHtml(doc.strength + " " + doc.unit
	// + "(" + doc.route + ")"));
	// txtFreqDetail.setText(Html.fromHtml(doc.frequency + " (Days-"
	// + doc.times + ")"));
	// txtRefills.setText(Html.fromHtml(doc.refills));
	// txtNotes.setText(Html.fromHtml(doc.notes));
	//
	// // get current date
	// // start{
	// Calendar c = Calendar.getInstance();
	// System.out.println("Current time => " + c.getTime());
	// SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	// String formattedDate = df.format(c.getTime());
	// // }end
	//
	// if (doc.addedon == null || doc.addedon.equals("")) {
	// txtHistory.setText("Added on " + formattedDate);
	// } else {
	// if (!Util.isEmptyString(doc.updatedon)) {
	// txtHistory.setText("Added On: " + doc.addedon
	// + ", Updated on " + doc.updatedon);
	// } else {
	// txtHistory.setText("Added On: " + doc.addedon);
	// }
	// }
	//
	// tl.addView(row1);
	// i++;
	// }
	// }
	// }

	// } end

}
