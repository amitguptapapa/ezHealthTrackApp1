package com.ezhealthtrack.physiciansoap;

import java.io.File;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.widget.PopupMenu;
import android.text.Editable;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MultiAutoCompleteTextView;
import android.widget.MultiAutoCompleteTextView.Tokenizer;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.ezhealthrack.api.APIs;
import com.ezhealthtrack.EzApp;
import com.ezhealthtrack.R;
import com.ezhealthtrack.DentistSoap.AddDentistNotesActivity;
import com.ezhealthtrack.DentistSoap.Model.DiagnosisModel;
import com.ezhealthtrack.DentistSoap.Model.PastVisitModel;
import com.ezhealthtrack.DentistSoap.Model.SubjectiveModel;
import com.ezhealthtrack.activity.AddPrescriptionActivity;
import com.ezhealthtrack.activity.DashboardActivity;
import com.ezhealthtrack.activity.LabOrderDetailsActivity;
import com.ezhealthtrack.activity.PrevVisitsActivity;
import com.ezhealthtrack.activity.SheduleActivity;
import com.ezhealthtrack.adapter.DoctorsAutoCompleteAdapter;
import com.ezhealthtrack.controller.PatientController;
import com.ezhealthtrack.controller.PatientController.OnResponsePatient;
import com.ezhealthtrack.controller.SoapNotesController;
import com.ezhealthtrack.dialogs.EzDialog;
import com.ezhealthtrack.greendao.Allergy;
import com.ezhealthtrack.greendao.Appointment;
import com.ezhealthtrack.greendao.Icd10Item;
import com.ezhealthtrack.greendao.Patient;
import com.ezhealthtrack.greendao.SoapNote;
import com.ezhealthtrack.model.AllergiesModel;
import com.ezhealthtrack.model.DoctorModel;
import com.ezhealthtrack.model.Document;
import com.ezhealthtrack.model.LabOrder;
import com.ezhealthtrack.model.MedicineModel;
import com.ezhealthtrack.model.PatInfo;
import com.ezhealthtrack.model.ReferFromModel;
import com.ezhealthtrack.model.ReferToModel;
import com.ezhealthtrack.model.gallery.SOAPGallery;
import com.ezhealthtrack.model.laborder.LabOrderDetails;
import com.ezhealthtrack.model.laborder.SOAPLabs;
import com.ezhealthtrack.one.EzCommonViews;
import com.ezhealthtrack.physiciansoap.model.ExaminationModel;
import com.ezhealthtrack.physiciansoap.model.PlanModel;
import com.ezhealthtrack.physiciansoap.model.PrivateNotesModel;
import com.ezhealthtrack.physiciansoap.model.VisitNotesModel;
import com.ezhealthtrack.print.PrintAllInvestigationActivity;
import com.ezhealthtrack.print.PrintLabOrderedActivity;
import com.ezhealthtrack.print.PrintPrescriptionActivity;
import com.ezhealthtrack.print.PrintRadiologyOrderedActivity;
import com.ezhealthtrack.util.BaseActivity;
import com.ezhealthtrack.util.Constants;
import com.ezhealthtrack.util.Log;
import com.ezhealthtrack.util.UploadDocumentRequest;
import com.ezhealthtrack.util.Util;
import com.ezhealthtrack.views.EditUtils;
import com.ezhealthtrack.views.EzSoapGalleryView;
import com.ezhealthtrack.views.TextUtils;
import com.google.gson.Gson;
import com.orleonsoft.android.simplefilechooser.ui.FileChooserActivity;

public class PhysicianSoapActivity extends BaseActivity implements
		OnClickListener {
	public static VisitNotesModel visitNotes;
	private SharedPreferences sharedPref;
	public static Appointment Appointment;
	LabOrderDetails mOrderDetails;
	private static PatInfo pat;
	public static com.ezhealthtrack.physiciansoap.model.VisitNotesModel physicianVisitNotes = new com.ezhealthtrack.physiciansoap.model.VisitNotesModel();
	private static TextView txtPatientName;
	private TextView txtDate;
	private TextView txtVisitCount;
	private TextView txtReason;
	private static TextView txtFollowup;
	private TextView txtFahrenheit;
	private TextView txtAllergy;
	private EditText editYearSmoke;
	private EditText editMonthSmoke;
	private EditText editDaySmoke;
	private EditText editYearAlcohol;
	private EditText editMonthAlcohol;
	private EditText editDayAlcohol;
	private CheckBox cbComplete;
	private CheckBox cbSmoke;
	private CheckBox cbAlcohol;
	private RelativeLayout rlSmoke;
	private RelativeLayout rlAlcohol;
	public static MultiAutoCompleteTextView actvFinal;
	private MultiAutoCompleteTextView actvProv;
	private ArrayAdapter<String> adapter;
	private final ArrayList<String> arrIcd = new ArrayList<String>();
	public static Patient patientModel;
	public static final ArrayList<PastVisitModel> arrPastVisit = new ArrayList<PastVisitModel>();
	private final HashMap<String, String> hashFollowUp = new HashMap<String, String>();
	private static final ArrayList<ReferToModel> arrReferTo = new ArrayList<ReferToModel>();
	private static final ArrayList<ReferFromModel> arrReferFrom = new ArrayList<ReferFromModel>();
	private static ArrayList<AllergiesModel> arrallergies = new ArrayList<AllergiesModel>();
	public static ArrayList<Document> arrDocuments = new ArrayList<Document>();
	public static ArrayList<MedicineModel> arrMed = new ArrayList<MedicineModel>();
	static public SOAPLabs mSOAPLabs;
	EzSoapGalleryView mGalleryView;

	private void AutoSave() {
		EditUtils.autoSaveEditTextLayout(
				(LinearLayout) findViewById(R.id.ll_hp),
				visitNotes.subjectiveModel.hashHP);
		EditUtils.autoSaveEditText(
				(EditText) findViewById(R.id.edit_cheif_complaint),
				visitNotes.subjectiveModel.hashCC);
		if (Util.isEmptyString(((EditText) findViewById(R.id.edit_cheif_complaint))
				.getText().toString()))
			((EditText) findViewById(R.id.edit_cheif_complaint))
					.setText(Appointment.getReason());
		EditUtils.autoSaveEditTextLayout(
				(LinearLayout) findViewById(R.id.ll_vitals),
				visitNotes.examinationModel.hashVitals);
		EditUtils.autoSaveEditText((EditText) findViewById(R.id.edit_symptoms),
				visitNotes.examinationModel.hashSymptoms);
		EditUtils.autoSaveEditText(
				(EditText) findViewById(R.id.edit_general_exam),
				visitNotes.examinationModel.hashGE);
		EditUtils.autoSaveEditText((EditText) findViewById(R.id.edit_se),
				visitNotes.examinationModel.hashSE);
		EditUtils.autoSaveEditText((EditText) findViewById(R.id.edit_ex_note),
				visitNotes.examinationModel.hashNote);
		EditUtils.autoSaveEditText((EditText) findViewById(R.id.edit_td),
				visitNotes.physicianPlanModel.treatmentDone.hashTd);
		EditUtils.autoSaveEditText((EditText) findViewById(R.id.edit_tp),
				visitNotes.physicianPlanModel.hashTp);
		((TextView) findViewById(R.id.txt_treatment_done))
				.setText(visitNotes.physicianPlanModel.treatmentDone
						.getTreatmentDone());

		((TextView) findViewById(R.id.txt_prescription)).setText(Html
				.fromHtml("<b>Prescription :</b> "));

		prescriptionTable(this);

		if (!Util.isEmptyString(visitNotes.physicianPlanModel.prescription.si
				.get("si"))
				&& visitNotes.physicianPlanModel.prescription.arrMedicine
						.size() > 0)
			((TextView) findViewById(R.id.txt_special_instruction))
					.setText(Html.fromHtml("<b>Special Instruction :</b> "
							+ visitNotes.physicianPlanModel.prescription.si
									.get("si")));

		// Handling Print Prescription button visibility
		if (visitNotes.physicianPlanModel.prescription.arrMedicine.size() > 0) {
			try {
				findViewById(R.id.txt_print_prescription).setActivated(true);
				findViewById(R.id.txt_print_prescription).setEnabled(true);
				findViewById(R.id.txt_print_prescription).setClickable(true);
			} catch (Exception e) {

			}
		} else {
			try {
				findViewById(R.id.txt_print_prescription).setActivated(false);
				findViewById(R.id.txt_print_prescription).setEnabled(false);
				findViewById(R.id.txt_print_prescription).setClickable(false);
			} catch (Exception e) {

			}
		}
		boolean radiologyflag = false;
		((TextView) findViewById(R.id.txt_radiology)).setText(Html
				.fromHtml("<b>Radiology :</b> "));
		for (final Entry<String, String> entry : visitNotes.physicianPlanModel.radiology.hashRadiology
				.entrySet()) {
			if (entry.getValue().equals("on")) {
				((TextView) findViewById(R.id.txt_radiology)).append("\n      "
						+ entry.getKey());
				radiologyflag = true;
			}
		}
		try {
			findViewById(R.id.txt_print_radiology).setActivated(radiologyflag);
			findViewById(R.id.txt_print_radiology).setEnabled(radiologyflag);
			findViewById(R.id.txt_print_radiology).setClickable(radiologyflag);
		} catch (Exception e) {

		}
		boolean labflag = false;
		((TextView) findViewById(R.id.txt_lab)).setText(Html
				.fromHtml("<b>Lab Order :</b> "));
		for (final Entry<String, String> entry : visitNotes.physicianPlanModel.lab.hashLab
				.entrySet()) {
			if (entry.getValue().equals("on")) {
				String[] result = entry.getKey().split("_");
				((TextView) findViewById(R.id.txt_lab)).append("\n      "
						+ entry.getKey().replace(
								"_" + result[result.length - 1], "") + ", ");
				labflag = true;
			}
		}
		try {
			findViewById(R.id.txt_print_laborder).setActivated(labflag);
			findViewById(R.id.txt_print_laborder).setEnabled(labflag);
			findViewById(R.id.txt_print_laborder).setClickable(labflag);
		} catch (Exception e) {

		}

		// Handling Print all Investigation button visibility {
		if (radiologyflag == true
				|| labflag == true
				|| visitNotes.physicianPlanModel.prescription.arrMedicine
						.size() > 0) {
			try {
				findViewById(R.id.txt_print_all).setActivated(true);
				findViewById(R.id.txt_print_all).setEnabled(true);
				findViewById(R.id.txt_print_all).setClickable(true);
			} catch (Exception e) {

			}
		} else {
			try {
				findViewById(R.id.txt_print_all).setActivated(false);
				findViewById(R.id.txt_print_all).setEnabled(false);
				findViewById(R.id.txt_print_all).setClickable(false);
			} catch (Exception e) {

			}
		}
		// }
		EditUtils.autoSaveEditText(
				(EditText) findViewById(R.id.edit_private_notes),
				visitNotes.privateNote.hashNote);

		findViewById(R.id.rg_private_notes_1).setVisibility(View.GONE);
		findViewById(R.id.txt_shadow_type).setVisibility(View.GONE);

		final RadioGroup rgPrivateNote = (RadioGroup) findViewById(R.id.rg_private_notes);
		try {
			if (Util.isEmptyString(visitNotes.privateNote.hashNote
					.get("status")))
				visitNotes.privateNote.hashNote.put("status", "Pr");
			((RadioButton) rgPrivateNote
					.findViewWithTag(visitNotes.privateNote.hashNote
							.get("status"))).setChecked(true);
		} catch (final Exception e) {
			e.printStackTrace();
		}

		rgPrivateNote.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				RadioButton rb = (RadioButton) group.findViewById(checkedId);
				visitNotes.privateNote.hashNote.put("status", rb.getTag()
						.toString());
			}
		});

		final CheckBox cbEcg = (CheckBox) findViewById(R.id.cb_ecg);
		cbEcg.setChecked(visitNotes.physicianPlanModel.ecg);
		cbEcg.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(final CompoundButton buttonView,
					final boolean isChecked) {
				visitNotes.physicianPlanModel.ecg = isChecked;
			}
		});
	}

	private void createSoap() {
		final String url = APIs.CREATE_SOAP() + "/cli/api";
		final Dialog loaddialog = Util
				.showLoadDialog(PhysicianSoapActivity.this);
		final JsonObjectRequest patientListRequest = new JsonObjectRequest(
				Request.Method.POST, url, visitNotes.jsonData,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(final JSONObject jObj) {
						String s = jObj.toString();
						while (s.length() > 100) {
							s = s.substring(100);
						}
						try {
							if (jObj.getString("s").equals("200")) {
								Util.AlertdialogWithFinish(
										PhysicianSoapActivity.this,
										"Visit Notes updated successfully");

							} else {
								Log.e("", jObj.toString());
								Util.Alertdialog(PhysicianSoapActivity.this,
										"Network Error, try again later");
							}

						} catch (final JSONException e) {
							Util.Alertdialog(PhysicianSoapActivity.this,
									"There is some error while fetching data, please try again");
							Log.e("", e);
						}
						loaddialog.dismiss();
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(final VolleyError error) {
						Util.Alertdialog(PhysicianSoapActivity.this,
								"There is some error while fetching data, please try again");

						Log.e("Error.Response", error);
						loaddialog.dismiss();
					}
				}) {
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				final HashMap<String, String> loginParams = new HashMap<String, String>();
				loginParams.put("auth-token", Util.getBase64String(sharedPref
						.getString(Constants.USER_TOKEN, "")));
				return loginParams;
			}

			@Override
			protected Map<String, String> getParams() {
				final HashMap<String, String> loginParams = new HashMap<String, String>();
				loginParams.put("format", "json");
				if (cbComplete.isChecked())
					loginParams.put("complete", "on");
				else
					loginParams.put("complete", "off");
				return loginParams;
			}

		};
		patientListRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 5,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		EzApp.mVolleyQueue.add(patientListRequest);

	}

	private void dialogDiagnosis(final String id) {
		final String url = APIs.SOAP_HISTORY() + "/cli/api";
		final Dialog loaddialog = Util
				.showLoadDialog(PhysicianSoapActivity.this);
		final StringRequest patientListRequest = new StringRequest(
				Request.Method.POST, url, new Response.Listener<String>() {
					@Override
					public void onResponse(final String response) {
						try {
							// Log.i("past visits", response);
							final JSONObject jObj = new JSONObject(response);
							try {
								final DiagnosisModel diag = new DiagnosisModel();
								final Dialog dialog = new Dialog(
										PhysicianSoapActivity.this);
								dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
								dialog.setContentView(R.layout.dialog_diagnosis);
								dialog.getWindow()
										.setBackgroundDrawable(
												new ColorDrawable(
														android.graphics.Color.TRANSPARENT));
								if (jObj.getString("s").equals("200")) {
									if (jObj.get("data") instanceof JSONObject)
										diag.JsonParse(
												jObj.getJSONObject("data"),
												(MultiAutoCompleteTextView) dialog
														.findViewById(R.id.actv_final_diagnosis),
												(MultiAutoCompleteTextView) dialog
														.findViewById(R.id.actv_provisonal_diagnosis));
								}
								dialog.setCancelable(false);
								dialog.findViewById(R.id.txt_close)
										.setOnClickListener(
												new OnClickListener() {

													@Override
													public void onClick(
															final View v) {
														dialog.dismiss();
													}
												});
								dialog.show();

							} catch (final JSONException e1) {
								e1.printStackTrace();
							}

						} catch (final JSONException e) {
							Util.Alertdialog(PhysicianSoapActivity.this,
									"There is some error while fetching data, please try again");
							Log.e("", e);
						}
						loaddialog.dismiss();
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(final VolleyError error) {
						Util.Alertdialog(PhysicianSoapActivity.this,
								"Network Error, try again later");
						error.printStackTrace();
						loaddialog.dismiss();
					}
				}) {
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				final HashMap<String, String> loginParams = new HashMap<String, String>();
				loginParams.put("auth-token", Util.getBase64String(sharedPref
						.getString(Constants.USER_TOKEN, "")));
				return loginParams;
			}

			@Override
			protected Map<String, String> getParams() {
				final HashMap<String, String> loginParams = new HashMap<String, String>();
				loginParams.put("format", "json");
				loginParams.put("id", id);
				loginParams.put("sec", "asse");
				// Log.i("Login Params", loginParams.toString());
				return loginParams;
			}

		};
		patientListRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 5,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		EzApp.mVolleyQueue.add(patientListRequest);
	}

	private void dialogExam(final String id) {
		final String url = APIs.SOAP_HISTORY() + "/cli/api";
		final Dialog loaddialog = Util
				.showLoadDialog(PhysicianSoapActivity.this);
		final StringRequest patientListRequest = new StringRequest(
				Request.Method.POST, url, new Response.Listener<String>() {
					@Override
					public void onResponse(final String response) {
						try {
							// Log.i("past visits", response);
							final JSONObject jObj = new JSONObject(response);
							try {
								final Dialog dialog = new Dialog(
										PhysicianSoapActivity.this);
								dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
								dialog.setContentView(R.layout.dialog_exam_phy);
								dialog.getWindow()
										.setBackgroundDrawable(
												new ColorDrawable(
														android.graphics.Color.TRANSPARENT));
								if (jObj.getString("s").equals("200")) {
									final ExaminationModel exam = new ExaminationModel();
									if (jObj.get("data") instanceof JSONObject)
										exam.JsonParse(jObj
												.getJSONObject("data"));
									TextUtils.autoSaveTextViewLayout(
											(LinearLayout) dialog
													.findViewById(R.id.ll_vitals),
											exam.hashVitals);
									TextUtils.autoSaveTextView(
											(TextView) dialog
													.findViewById(R.id.edit_symptoms),
											exam.hashSymptoms);
									TextUtils.autoSaveTextView(
											(TextView) dialog
													.findViewById(R.id.edit_general_exam),
											exam.hashGE);
									TextUtils.autoSaveTextView(
											(TextView) dialog
													.findViewById(R.id.edit_se),
											exam.hashSE);
									TextUtils.autoSaveTextView(
											(TextView) dialog
													.findViewById(R.id.edit_ex_note),
											exam.hashNote);

								}
								dialog.setCancelable(false);
								dialog.findViewById(R.id.txt_close)
										.setOnClickListener(
												new OnClickListener() {

													@Override
													public void onClick(
															final View v) {
														dialog.dismiss();
													}
												});
								dialog.show();

							} catch (final JSONException e1) {
								e1.printStackTrace();
							}
						} catch (final JSONException e) {
							Util.Alertdialog(PhysicianSoapActivity.this,
									"There is some error while fetching data, please try again");
							Log.e("", e);
						}
						loaddialog.dismiss();
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(final VolleyError error) {
						Util.Alertdialog(PhysicianSoapActivity.this,
								"Network Error, try again later");

						error.printStackTrace();
						loaddialog.dismiss();
					}
				}) {
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				final HashMap<String, String> loginParams = new HashMap<String, String>();
				loginParams.put("auth-token", Util.getBase64String(sharedPref
						.getString(Constants.USER_TOKEN, "")));
				return loginParams;
			}

			@Override
			protected Map<String, String> getParams() {
				final HashMap<String, String> loginParams = new HashMap<String, String>();
				loginParams.put("format", "json");
				loginParams.put("id", id);
				loginParams.put("sec", "obje");
				// Log.i("Login Params", loginParams.toString());
				return loginParams;
			}

		};
		patientListRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 5,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		EzApp.mVolleyQueue.add(patientListRequest);
	}

	private void dialogPlan(final String id) {
		final String url = APIs.SOAP_HISTORY() + "/cli/api";
		final Dialog loaddialog = Util
				.showLoadDialog(PhysicianSoapActivity.this);
		final StringRequest patientListRequest = new StringRequest(
				Request.Method.POST, url, new Response.Listener<String>() {
					@Override
					public void onResponse(final String response) {
						try {
							Log.i("past visits", response);
							final JSONObject jObj = new JSONObject(response);
							try {
								final PlanModel plan = new PlanModel();
								final PrivateNotesModel pNote = new PrivateNotesModel();
								final ArrayList<Document> arrDocuments = new ArrayList<Document>();

								final Dialog dialog = new Dialog(
										PhysicianSoapActivity.this,
										R.style.Theme_AppCompat_Light);
								dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
								dialog.setContentView(R.layout.dialog_plan_phy);
								dialog.getWindow()
										.setBackgroundDrawable(
												new ColorDrawable(
														android.graphics.Color.TRANSPARENT));

								dialog.setCancelable(false);

								EzCommonViews.radiologyTable(dialog,
										arrDocuments,
										PhysicianSoapActivity.this);
								EzCommonViews.labTable(dialog, arrDocuments,
										PhysicianSoapActivity.this);
								EzCommonViews.ekgTable(dialog, arrDocuments,
										PhysicianSoapActivity.this);

								dialog.findViewById(R.id.txt_close)
										.setOnClickListener(
												new OnClickListener() {

													@Override
													public void onClick(View v) {

														dialog.dismiss();

													}
												});
								if (jObj.getString("s").equals("200")) {
									if (jObj.get("data") instanceof JSONObject) {
										plan.JsonParse(jObj
												.getJSONObject("data"));
										if (jObj.getJSONObject("data").get(
												"private-note") instanceof JSONObject) {
											pNote.jsonParse(jObj.getJSONObject(
													"data").getJSONObject(
													"private-note"));
										}
										try {
											if (jObj.has("documents")) {
												JSONArray arr = jObj
														.getJSONArray("documents");
												for (int i = 0; i < arr
														.length(); i++) {
													Document doc = new Gson()
															.fromJson(
																	arr.get(i)
																			.toString(),
																	Document.class);
													arrDocuments.add(doc);
												}
											}
										} catch (Exception e) {
											e.printStackTrace();
										}

									}
									if (pNote.hashNote
											.containsKey("private-note")) {
										((TextView) dialog
												.findViewById(R.id.txt_private_notes))
												.setText(pNote.hashNote
														.get("private-note"));
									}
									TextUtils.autoSaveTextView(
											(TextView) dialog
													.findViewById(R.id.edit_tp),
											plan.hashTp);
									((TextView) dialog
											.findViewById(R.id.txt_treatment_done))
											.setText(plan.treatmentDone
													.getTreatmentDone());
									((TextView) dialog
											.findViewById(R.id.txt_prescription)).setText(Html
											.fromHtml("<b>Prescription :</b> "));

									int i = 1;
									for (final MedicineModel medicine : plan.prescription.arrMedicine) {
										((TextView) dialog
												.findViewById(R.id.txt_prescription)).append(Html.fromHtml("<br> "
												+ "<b>"
												+ i
												+ ". </b>"
												+ medicine
														.getMedicineStringExtra()));
										i++;
									}

									// prescriptionTablePast(dialog);

									if (!Util
											.isEmptyString(plan.prescription.si
													.get("si"))
											|| visitNotes.physicianPlanModel.prescription.arrMedicine
													.size() > 0)
										((TextView) dialog
												.findViewById(R.id.txt_prescription))
												.append("\n Special Instruction : "
														+ plan.prescription.si
																.get("si"));
									((TextView) dialog
											.findViewById(R.id.txt_radiology)).setText(Html
											.fromHtml("<b>Radiology :</b> "));
									for (final Entry<String, String> entry : plan.radiology.hashRadiology
											.entrySet()) {
										if (entry.getValue().equals("on")) {
											((TextView) dialog
													.findViewById(R.id.txt_radiology))
													.append("\n      "
															+ entry.getKey());
										}
									}

									((TextView) dialog
											.findViewById(R.id.txt_lab)).setText(Html
											.fromHtml("<b>Lab Order :</b> "));
									for (final Entry<String, String> entry : plan.lab.hashLab
											.entrySet()) {
										if (entry.getValue().equals("on")) {
											String[] result = entry.getKey()
													.split("_");
											((TextView) dialog
													.findViewById(R.id.txt_lab))
													.append(entry
															.getKey()
															.replace(
																	"_"
																			+ result[result.length - 1],
																	"")
															+ ", ");
										}
									}
									((TextView) dialog
											.findViewById(R.id.txt_allergy)).setText(Html
											.fromHtml("<b>Allergy :</b> "));
									for (final AllergiesModel allergy : arrallergies) {
										((TextView) dialog
												.findViewById(R.id.txt_allergy))
												.append("\n "
														+ allergy.getMCatName()
														+ " -->"
														+ allergy.getSCatName()
														+ "\n "
														+ allergy.getAddiInfo());
									}

								}
								dialog.show();

							} catch (final JSONException e1) {
								e1.printStackTrace();
							}

						} catch (final JSONException e) {
							Util.Alertdialog(PhysicianSoapActivity.this,
									"There is some error while fetching data, please try again");
							Log.e("", e);
						}
						loaddialog.dismiss();
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(final VolleyError error) {
						Util.Alertdialog(PhysicianSoapActivity.this,
								"Network Error, try again later");

						error.printStackTrace();
						loaddialog.dismiss();
					}
				}) {
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				final HashMap<String, String> loginParams = new HashMap<String, String>();
				loginParams.put("auth-token", Util.getBase64String(sharedPref
						.getString(Constants.USER_TOKEN, "")));
				return loginParams;
			}

			@Override
			protected Map<String, String> getParams() {
				final HashMap<String, String> loginParams = new HashMap<String, String>();
				loginParams.put("format", "json");
				loginParams.put("id", id);
				loginParams.put("sec", "plan");
				// Log.i("Login Params", loginParams.toString());
				return loginParams;
			}

		};
		patientListRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 5,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		EzApp.mVolleyQueue.add(patientListRequest);
	}

	private void dialogSubjective(final String id) {
		final String url = APIs.SOAP_HISTORY() + "/cli/api";
		final StringRequest patientListRequest = new StringRequest(
				Request.Method.POST, url, new Response.Listener<String>() {
					@Override
					public void onResponse(final String response) {
						Log.i("", response);
						try {
							Log.i("past visits", response);
							final JSONObject jObj = new JSONObject(response);
							try {
								final Dialog dialog = new Dialog(
										PhysicianSoapActivity.this);
								dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
								dialog.setContentView(R.layout.dialog_subjective_phy);
								dialog.getWindow()
										.setBackgroundDrawable(
												new ColorDrawable(
														android.graphics.Color.TRANSPARENT));
								if (jObj.getString("s").equals("200")) {
									final SubjectiveModel sub = new SubjectiveModel();
									if (jObj.get("data") instanceof JSONObject)
										sub.JsonParse(jObj
												.getJSONObject("data"));
									for (final Entry entry : sub.hashCC
											.entrySet()) {
										((TextView) dialog
												.findViewById(
														R.id.ll_subjective)
												.findViewWithTag(entry.getKey()))
												.setText("  "
														+ entry.getValue()
																.toString());
										if (Util.isEmptyString(((EditText) findViewById(R.id.edit_cheif_complaint))
												.getText().toString()))
											((EditText) dialog
													.findViewById(R.id.edit_cheif_complaint))
													.setText(Appointment
															.getReason());
									}
									for (final Entry entry : sub.hashHP
											.entrySet()) {
										if (!Util.isEmptyString(entry.getKey()
												.toString())) {
											((TextView) dialog.findViewById(
													R.id.ll_subjective)
													.findViewWithTag(
															entry.getKey()))
													.setText(entry.getValue()
															.toString());
										}
									}
								}

								final CheckBox cbSmoke = (CheckBox) dialog
										.findViewById(R.id.cb_smoke);
								final CheckBox cbAlcohol = (CheckBox) dialog
										.findViewById(R.id.cb_alcohol);
								final RelativeLayout rlAlcohol = (RelativeLayout) dialog
										.findViewById(R.id.rl_alcohol);
								final RelativeLayout rlSmoke = (RelativeLayout) dialog
										.findViewById(R.id.rl_smoke);
								cbSmoke.setText("Smoking(Yes/No)");
								cbAlcohol.setText("Alcohol(Yes/No)");
								cbSmoke.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View arg0) {
										if (cbSmoke.isChecked()) {
											rlSmoke.setVisibility(View.VISIBLE);
										} else {
											rlSmoke.setVisibility(View.GONE);
										}

									}
								});

								cbAlcohol
										.setOnClickListener(new OnClickListener() {

											@Override
											public void onClick(View arg0) {
												if (cbAlcohol.isChecked()) {
													rlAlcohol
															.setVisibility(View.VISIBLE);
												} else {
													rlAlcohol
															.setVisibility(View.GONE);
												}
											}
										});

								dialog.setCancelable(false);
								dialog.findViewById(R.id.txt_close)
										.setOnClickListener(
												new OnClickListener() {

													@Override
													public void onClick(
															final View v) {
														dialog.dismiss();
													}
												});
								dialog.show();

							} catch (final JSONException e1) {
								e1.printStackTrace();
							}
						} catch (final JSONException e) {
							Util.Alertdialog(PhysicianSoapActivity.this,
									"There is some error while fetching data, please try again");
							Log.e("", e);
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(final VolleyError error) {
						Util.Alertdialog(PhysicianSoapActivity.this,
								"Network Error, try again later");
						error.printStackTrace();
					}
				}) {
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				final HashMap<String, String> loginParams = new HashMap<String, String>();
				loginParams.put("auth-token", Util.getBase64String(sharedPref
						.getString(Constants.USER_TOKEN, "")));
				return loginParams;
			}

			@Override
			protected Map<String, String> getParams() {
				final HashMap<String, String> loginParams = new HashMap<String, String>();
				loginParams.put("format", "json");
				loginParams.put("id", id);
				loginParams.put("sec", "subj");
				// Log.i("Login Params", loginParams.toString());
				return loginParams;
			}

		};
		patientListRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 5,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		EzApp.mVolleyQueue.add(patientListRequest);
	}

	public void onShowGallery(View view) {
		mGalleryView.onShowGallery(this);
	}

	public void onAddPhoto(View view) {
		mGalleryView.onAddPhoto(this);
	}

	private void showSOAPOrders() {
		if (mSOAPLabs == null)
			return;

		LinearLayout orderLayOut = (LinearLayout) findViewById(R.id.ll_labtest);
		orderLayOut.removeAllViews();

		// add one view for each order
		for (int i = 0; i < mSOAPLabs.getLabOrders().size(); ++i) {
			LinearLayout orderView = (LinearLayout) getLayoutInflater()
					.inflate(R.layout.row_order_summary, null, false);
			orderLayOut.clearFocus();

			final LabOrder order = mSOAPLabs.getLabOrders().get(i);

			// Order ID clicked | start{
			SpannableString ss = new SpannableString(order.getOrderSummary());
			ClickableSpan clickableSpan = new ClickableSpan() {
				@Override
				public void onClick(View textView) {
					// LabOrder order = new LabOrder();
					Intent intent;
					intent = new Intent(PhysicianSoapActivity.this,
							LabOrderDetailsActivity.class);
					intent.putExtra("id", order.getId());
					startActivity(intent);
				}

				@Override
				public void updateDrawState(TextPaint ds) {
					super.updateDrawState(ds);
					ds.setUnderlineText(false);
				}
			};
			ss.setSpan(clickableSpan, 9, 30, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			TextView detailsView = (TextView) orderView
					.findViewById(R.id.order_details);
			detailsView.setText(ss);
			detailsView.setMovementMethod(LinkMovementMethod.getInstance());
			detailsView.setHighlightColor(Color.TRANSPARENT);
			// }end
			orderLayOut.addView(orderView);

			// add test reports status
			// temporarily disabled | start{

			// List<TestReport> reports = order.getTestReports();
			// LinearLayout testReports = (LinearLayout) orderView
			// .findViewById(R.id.ll_test_list);
			// testReports.removeAllViews();
			// for (int j = 0; j < reports.size(); ++j) {
			// final TestReport report = reports.get(j);
			// LayoutInflater inflater = (LayoutInflater)
			// getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			// final View view = inflater.inflate(
			// R.layout.row_order_details_list, null, false);
			// testReports.addView(view);
			// TextView txtName = (TextView) view
			// .findViewById(R.id.txt_test_name);
			// TextView txtDate = (TextView) view.findViewById(R.id.txt_date);
			// Button viewBtn = (Button) view
			// .findViewById(R.id.action_btn_view);
			// viewBtn.setOnClickListener(new OnClickListener() {
			//
			// @Override
			// public void onClick(View arg0) {
			// dialogPrintReport(report);
			// }
			// });
			//
			// // final Date date;
			// txtName.setText(report.getReportName());
			// try {
			// Date date;
			// String theDate = order.getOrderDate();
			//
			// if (!Util.isEmptyString(theDate)) {
			// date = EzHealthApplication.sdfddmmyyhhmmss
			// .parse(theDate);
			// txtDate.setText(EzHealthApplication.sdfddMmyy
			// .format(date));
			// }
			// } catch (ParseException e) {
			// e.printStackTrace();
			// }
			// if (report.getStatus().equalsIgnoreCase("View")) {
			// txtDate.setVisibility(View.VISIBLE);
			// viewBtn.setVisibility(View.VISIBLE);
			// } else {
			// txtDate.setVisibility(View.INVISIBLE);
			// viewBtn.setVisibility(View.INVISIBLE);
			// }
			// }

			// }end
		}

	}

	private void showSOAPGallery() {
		mGalleryView.showSOAPGallery(SoapNotesController.mSOAPGallery);
	}

	@Override
	public void onStart() {
		showSOAPGallery();
		showSOAPOrders();
		super.onStart();
	}

	private void getData() {

		final String url = APIs.SOAP_SHOW() + Appointment.getSiid() + "/bk_id/"
				+ Appointment.getBkid();
		Log.i("SOAP API", url);
		final Dialog loaddialog = Util
				.showLoadDialog(PhysicianSoapActivity.this);
		final StringRequest patientListRequest = new StringRequest(
				Request.Method.POST, url, new Response.Listener<String>() {
					@Override
					public void onResponse(final String response) {
						Log.i("", response);
						try {
							final JSONObject jObj = new JSONObject(response);
							JSONObject data = jObj.getJSONObject("data");

							// Get Labs & Orders
							JSONObject copy = new JSONObject(data.toString());
							copy.remove("p-info");
							copy.remove("tmpl-inst");
							copy.remove("allergies");
							copy.remove("masterPlan");
							copy.remove("documents");
							copy.remove("copy_tmpl");

							// Log.i("PSA", "INPUT SOAPLabs:" +
							// copy.toString());
							mSOAPLabs = new Gson().fromJson(copy.toString(),
									SOAPLabs.class);
							PhysicianSoapActivity.this.showSOAPOrders();

							SoapNotesController.mSOAPGallery = new Gson()
									.fromJson(copy.toString(),
											SOAPGallery.class);
							PhysicianSoapActivity.this.showSOAPGallery();

							// Log.i("PSA:showSOAPGallery()", "Gallery: "
							// + new Gson().toJson(mSOAPGallery));

							if (jObj.getJSONObject("data").has(
									"followup_appointment"))
								txtFollowup.setText(jObj.getJSONObject("data")
										.getString("followup_appointment"));

							arrallergies.clear();
							if (jObj.getJSONObject("data").has("allergies")) {
								JSONArray arr = jObj.getJSONObject("data")
										.getJSONArray("allergies");
								for (int i = 0; i < arr.length(); i++) {
									AllergiesModel allergy = new Gson()
											.fromJson(arr.get(i).toString(),
													AllergiesModel.class);
									arrallergies.add(allergy);
								}
							}

							if (jObj.getJSONObject("data").has("documents")) {
								JSONArray arr = jObj.getJSONObject("data")
										.getJSONArray("documents");
								for (int i = 0; i < arr.length(); i++) {
									Document doc = new Gson().fromJson(
											arr.get(i).toString(),
											Document.class);
									arrDocuments.add(doc);
								}
								EzCommonViews.radiologyTable(
										PhysicianSoapActivity.this,
										arrDocuments);
								EzCommonViews.labTable(
										PhysicianSoapActivity.this,
										arrDocuments);
								EzCommonViews.ekgTable(
										PhysicianSoapActivity.this,
										arrDocuments);

							}

							if (jObj.getJSONObject("data").has("mp_ep_id")) {
								Appointment.setEpid(jObj.getJSONObject("data")
										.getString("mp_ep_id"));
							}

							if (jObj.getJSONObject("data").has("p-info")) {
								String s = jObj.getJSONObject("data")
										.getString("p-info");
								pat = new Gson().fromJson(s, PatInfo.class);
								Appointment.setFollowid(pat.getFollowupId());
								txtPatientName.setText(pat.getPName() + " , "
										+ pat.getAge() + "/"
										+ pat.getGender().charAt(0));

							}

							txtAllergy = (TextView) findViewById(R.id.txt_allergy_top);
							txtAllergy.setText(Html
									.fromHtml("<b>Allergy :</b> "));
							txtAllergy.setVisibility(View.GONE);
							for (final AllergiesModel allergy : arrallergies) {
								if (!Util.isEmptyString(allergy.getMCatName())
										|| !Util.isEmptyString(allergy
												.getSCatName())) {
									txtAllergy.setVisibility(View.VISIBLE);
									if (!Util.isEmptyString(allergy
											.getAddiInfo())) {
										txtAllergy.append(allergy.getMCatName()
												+ " -->"
												+ allergy.getSCatName() + "("
												+ allergy.getAddiInfo() + "), ");
									} else {
										txtAllergy.append(allergy.getMCatName()
												+ " -->"
												+ allergy.getSCatName() + ", ");
									}
								} else {
									txtAllergy.setText("");
								}
							}

							getPastVisits();
							if (!Appointment.getVisit().equals("1")) {
								pastFollowUp();
							} else {
								findViewById(R.id.btn_past_visit)
										.setVisibility(View.GONE);
								findViewById(R.id.btn_past_subjective)
										.setVisibility(View.GONE);
								findViewById(R.id.btn_past_diagnosis)
										.setVisibility(View.GONE);
								findViewById(R.id.btn_past_plan).setVisibility(
										View.GONE);
								findViewById(R.id.btn_past_examination)
										.setVisibility(View.GONE);
								txtVisitCount.setText("No. of visit : " + 1);
							}
							getReferPatient();
							onResume();
							prescriptionTable(PhysicianSoapActivity.this);
							if (jObj.getString("s").equals("200")) {
								// List<SoapNote> arrSoap =
								// EzHealthApplication.soapNoteDao
								// .queryBuilder()
								// .where(Properties.Id.eq((long) Integer
								// .parseInt(Appointment.getBkid())))
								// .list();
								// SimpleDateFormat sdf = new SimpleDateFormat(
								// "yyyy-MM-dd hh:mm:ss");
								// Date updated = new Date();
								// try {
								// updated = sdf.parse(jObj
								// .getJSONObject("data")
								// .getJSONObject("tmpl-inst")
								// .getString("date_updated"));
								// } catch (ParseException e1) {
								// // TODO Auto-generated catch block
								// Log.e("", e1);
								// }
								if (true) {
									final JSONObject soap;
									if (Util.isEmptyString(Appointment
											.getSiid())) {
										soap = visitNotes.jsonData
												.getJSONObject("Soap");
										if (jObj.getJSONObject("data").get(
												"copy_tmpl") instanceof JSONObject)
											soap.getJSONObject("subj")
													.put("hp",
															jObj.getJSONObject(
																	"data")
																	.getJSONObject(
																			"copy_tmpl")
																	.getJSONObject(
																			"subj")
																	.getJSONObject(
																			"hp"));
										soap.getJSONObject("subj")
												.getJSONObject("cc")
												.put("value",
														Appointment.getReason());
										if (soap.has("subj"))
											visitNotes.subjectiveModel.JsonParse(soap
													.getJSONObject("subj"));
										AutoSave();
									} else {
										soap = jObj.getJSONObject("data")
												.getJSONObject("tmpl-inst")
												.getJSONObject("Soap");
										visitNotes.jsonData = jObj
												.getJSONObject("data")
												.getJSONObject("tmpl-inst");
										if (soap.has("subj"))
											visitNotes.subjectiveModel.JsonParse(soap
													.getJSONObject("subj"));
										if (soap.has("private-note"))
											visitNotes.privateNote.jsonParse(soap
													.getJSONObject("private-note"));
										if (soap.has("asse"))
											visitNotes.diagnosisModel.JsonParse(
													soap.getJSONObject("asse"),
													actvFinal, actvProv);
										if (soap.has("obje"))
											visitNotes.examinationModel.JsonParse(soap
													.getJSONObject("obje"));
										if (soap.has("plan"))
											visitNotes.physicianPlanModel.JsonParse(soap
													.getJSONObject("plan"));
									}

									for (final Map.Entry<String, String> entry : visitNotes.subjectiveModel.hashHP
											.entrySet()) {
										if (!Util.isEmptyString(entry
												.getValue())) {
											findViewById(R.id.img_hp_tick)
													.setVisibility(View.VISIBLE);
										}
									}
									AutoSave();
								}

							} else {

							}

							// String smoke = visitNotes.subjectiveModel.hashHP
							// .get("smok");
							// String alcohol =
							// visitNotes.subjectiveModel.hashHP
							// .get("alco");
							// EzUtils.showLong("Smoke = " + smoke);
							// if (smoke.equals("1")) {
							//
							// rlSmoke.setVisibility(View.VISIBLE);
							// cbSmoke.setChecked(true);
							// } else {
							// rlSmoke.setVisibility(View.GONE);
							// cbSmoke.setChecked(false);
							// }
							//
							// if (alcohol.equals("1")) {
							// rlAlcohol.setVisibility(View.VISIBLE);
							// cbAlcohol.setChecked(true);
							// } else {
							// rlAlcohol.setVisibility(View.GONE);
							// cbAlcohol.setChecked(false);
							// }

						} catch (final JSONException e) {
							Util.Alertdialog(PhysicianSoapActivity.this,
									"There is some error while fetching data, please try again");
							Log.e("", e);
						}
						loaddialog.dismiss();
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(final VolleyError error) {
						Util.Alertdialog(PhysicianSoapActivity.this,
								"Network Error, try again later");
						Log.e("Error.Response", error);
						loaddialog.dismiss();
					}
				}) {
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				final HashMap<String, String> loginParams = new HashMap<String, String>();
				loginParams.put("auth-token", Util.getBase64String(sharedPref
						.getString(Constants.USER_TOKEN, "")));
				return loginParams;
			}

			@Override
			protected Map<String, String> getParams() {
				final HashMap<String, String> loginParams = new HashMap<String, String>();
				loginParams.put("format", "json");
				loginParams.put("tenant_id",
						EzApp.sharedPref.getString(Constants.TENANT_ID, ""));
				loginParams.put("branch_id", EzApp.sharedPref.getString(
						Constants.USER_BRANCH_ID, ""));
				return loginParams;
			}

		};
		patientListRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 5,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		EzApp.mVolleyQueue.add(patientListRequest);

	}

	private void getLastSubjective(final String id) {
		final String url = APIs.SOAP_HISTORY() + "/cli/api";
		final Dialog loaddialog = Util
				.showLoadDialog(PhysicianSoapActivity.this);
		final StringRequest patientListRequest = new StringRequest(
				Request.Method.POST, url, new Response.Listener<String>() {
					@Override
					public void onResponse(final String response) {
						try {
							// Log.i("past visits", response);
							final JSONObject jObj = new JSONObject(response);
							if (jObj.getString("s").equals("200")) {
								try {
									final com.ezhealthtrack.physiciansoap.model.SubjectiveModel sub = visitNotes.subjectiveModel;
									sub.JsonParse(jObj.getJSONObject("data"));
									AutoSave();
								} catch (final JSONException e1) {
									e1.printStackTrace();
								}

							} else {

							}
						} catch (final JSONException e) {
							Util.Alertdialog(PhysicianSoapActivity.this,
									"There is some error while fetching data, please try again");

							Log.e("", e);
						}
						loaddialog.dismiss();
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(final VolleyError error) {
						Util.Alertdialog(PhysicianSoapActivity.this,
								"Network Error, try again later");

						error.printStackTrace();
						loaddialog.dismiss();
					}
				}) {
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				final HashMap<String, String> loginParams = new HashMap<String, String>();
				loginParams.put("auth-token", Util.getBase64String(sharedPref
						.getString(Constants.USER_TOKEN, "")));
				return loginParams;
			}

			@Override
			protected Map<String, String> getParams() {
				final HashMap<String, String> loginParams = new HashMap<String, String>();
				loginParams.put("format", "json");
				loginParams.put("id", id);
				loginParams.put("sec", "subj");
				// Log.i("Login Params", loginParams.toString());
				return loginParams;
			}

		};
		patientListRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 5,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		EzApp.mVolleyQueue.add(patientListRequest);
	}

	private void getPastVisits() {

		final String url = APIs.PAST_VISIT();
		final Dialog loaddialog = Util
				.showLoadDialog(PhysicianSoapActivity.this);
		final StringRequest patientListRequest = new StringRequest(
				Request.Method.POST, url, new Response.Listener<String>() {
					@Override
					public void onResponse(final String response) {
						try {
							// Log.i("past visits", response);
							final JSONObject jObj = new JSONObject(response);
							if (jObj.getString("s").equals("200")) {
								try {
									PhysicianSoapActivity.arrPastVisit.clear();
									for (int i = 0; i < jObj.getJSONArray(
											"data").length(); i++) {
										final JSONObject jsonVisit = jObj
												.getJSONArray("data")
												.getJSONObject(i);
										final PastVisitModel pastVisit = new PastVisitModel(
												jsonVisit);
										PhysicianSoapActivity.arrPastVisit
												.add(pastVisit);
									}
								} catch (final JSONException e1) {

									e1.printStackTrace();
								}
								if (jObj.getJSONArray("data").length() > 0) {
									findViewById(R.id.btn_past_visit)
											.setVisibility(View.VISIBLE);
								} else {
									findViewById(R.id.btn_past_visit)
											.setVisibility(View.GONE);
								}

							} else {

							}
						} catch (final JSONException e) {
							Util.Alertdialog(PhysicianSoapActivity.this,
									"There is some error while fetching data, please try again");
							Log.e("", e);
						}
						loaddialog.dismiss();
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(final VolleyError error) {
						Util.Alertdialog(PhysicianSoapActivity.this,
								"Network Error, try again later");

						error.printStackTrace();
						loaddialog.dismiss();
					}
				}) {
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				final HashMap<String, String> loginParams = new HashMap<String, String>();
				loginParams.put("auth-token", Util.getBase64String(sharedPref
						.getString(Constants.USER_TOKEN, "")));
				return loginParams;
			}

			@Override
			protected Map<String, String> getParams() {
				final HashMap<String, String> loginParams = new HashMap<String, String>();
				loginParams.put("pid", Appointment.getPid());
				loginParams.put("fid", Appointment.getPfId());
				loginParams.put("format", "json");
				loginParams.put("bk-id", Appointment.getBkid());
				loginParams.put("follow-id", Appointment.getFollowid());
				Log.i("Login Params", loginParams.toString());
				return loginParams;
			}

		};
		patientListRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 5,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		EzApp.mVolleyQueue.add(patientListRequest);

	}

	private void getReferPatient() {
		final String url = APIs.REFER_PATIENT_DETAIL();
		final Dialog loaddialog = Util
				.showLoadDialog(PhysicianSoapActivity.this);
		final StringRequest patientListRequest = new StringRequest(
				Request.Method.POST, url, new Response.Listener<String>() {
					@Override
					public void onResponse(final String response) {
						try {
							Log.i("refer response", response);
							final JSONObject jObj = new JSONObject(response);
							if (jObj.getString("s").equals("200")) {
								final JSONArray jsonReferTo = jObj
										.getJSONArray("refer-to");
								final ReferToModel referTo = new ReferToModel();
								for (int i = 0; i < jsonReferTo.length(); i++) {
									String date = referTo.refer_date = jsonReferTo
											.getJSONObject(i).getString(
													"refer-date");
									try {
										date = EzApp.sdfmmm
												.format(EzApp.sdfMmddyy
														.parse(date));
									} catch (ParseException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}

									referTo.ep_id = jsonReferTo
											.getJSONObject(i)
											.getString("ep-id");
									referTo.ep_status = jsonReferTo
											.getJSONObject(i).getString(
													"ep-status");
									referTo.refer_ep_id = jsonReferTo
											.getJSONObject(i).getString(
													"refer-ep-id");
									referTo.refer_to_id = jsonReferTo
											.getJSONObject(i).getString(
													"refer-to-id");
									referTo.refer_date = date;
									referTo.refer_to_type = jsonReferTo
											.getJSONObject(i).getString(
													"refer-to-type");
									referTo.refer_name = jsonReferTo
											.getJSONObject(i).getString(
													"refer-name");
									if (!referTo.ep_id.equals("0")) {
										arrReferTo.add(referTo);
									}
								}
								if (arrReferTo.size() > 0) {
									findViewById(R.id.btn_refer_to)
											.setVisibility(View.VISIBLE);
								} else {
									findViewById(R.id.btn_refer_to)
											.setVisibility(View.GONE);
								}
								final JSONArray jsonReferFrom = jObj
										.getJSONArray("refer-from");
								for (int i = 0; i < jsonReferFrom.length(); i++) {
									final ReferFromModel referFrom = new ReferFromModel();
									String date = referFrom.refer_date = jsonReferFrom
											.getJSONObject(i).getString(
													"refer-date");
									try {
										date = EzApp.sdfmmm
												.format(EzApp.sdfMmddyy
														.parse(date));
									} catch (ParseException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									referFrom.ep_id = jsonReferFrom
											.getJSONObject(i)
											.getString("ep-id");
									referFrom.refer_date = date;
									referFrom.refer_name = jsonReferFrom
											.getJSONObject(i).getString(
													"refer-name");
									arrReferFrom.add(referFrom);
								}

								if (arrReferFrom.size() > 0) {
									findViewById(R.id.btn_refer_from)
											.setVisibility(View.VISIBLE);
								} else {
									findViewById(R.id.btn_refer_from)
											.setVisibility(View.GONE);
								}

							} else {

							}
						} catch (final JSONException e) {
							Util.Alertdialog(PhysicianSoapActivity.this,
									"There is some error while fetching data, please try again");
							Log.e("", e);
						}
						loaddialog.dismiss();
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(final VolleyError error) {
						Util.Alertdialog(PhysicianSoapActivity.this,
								"Network Error, try again later");

						error.printStackTrace();
						loaddialog.dismiss();
					}
				}) {
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				final HashMap<String, String> loginParams = new HashMap<String, String>();
				loginParams.put("auth-token", Util.getBase64String(sharedPref
						.getString(Constants.USER_TOKEN, "")));
				return loginParams;
			}

			@Override
			protected Map<String, String> getParams() {
				final HashMap<String, String> loginParams = new HashMap<String, String>();
				loginParams.put("format", "json");
				loginParams.put("ep-id", Appointment.getEpid());
				Log.i(url, loginParams.toString());
				return loginParams;
			}

		};
		patientListRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 5,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		EzApp.mVolleyQueue.add(patientListRequest);
	}

	private TextWatcher getTextWatcher(final String str) {
		return new TextWatcher() {

			@Override
			public void afterTextChanged(final Editable editable) {

			}

			@Override
			public void beforeTextChanged(final CharSequence s,
					final int start, final int count, final int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, final int start,
					final int before, final int count) {
				arrIcd.clear();
				final String newText = s.toString();
				if (str.equals("pd")) {
					visitNotes.diagnosisModel.pd = newText;
				} else if (str.equals("fd")) {
					visitNotes.diagnosisModel.fd = newText;
				}
				s = s.toString().replaceAll(",,", ",");
				final String[] parts = newText.split(",");
				if (parts.length > 0)
					if (parts[parts.length - 1].length() > 2) {
						ArrayList<Icd10Item> temp;
						if (parts.length > 0)
							temp = (ArrayList<Icd10Item>) EzApp.icdDao
									.queryBuilder()
									.where(com.ezhealthtrack.greendao.Icd10ItemDao.Properties.Diag_desc
											.like("%" + parts[parts.length - 1]
													+ "%")).list();
						else
							temp = new ArrayList<Icd10Item>();
						adapter.clear();
						for (final Icd10Item item : temp) {
							Log.i("tag", "" + item.getChapter_desc());
							adapter.add(item.getDiag_desc());
						}
					}

			}

		};
	}

	private Tokenizer getTokenizer() {
		return new MultiAutoCompleteTextView.Tokenizer() {

			@Override
			public int findTokenEnd(final CharSequence text, final int cursor) {
				int i = cursor;
				final int len = text.length();

				while (i < len) {
					if (text.charAt(i) == ',') {
						return i;
					} else {
						i++;
					}
				}

				return len;
			}

			@Override
			public int findTokenStart(final CharSequence text, final int cursor) {
				int i = cursor;

				while ((i > 0) && (text.charAt(i - 1) != ',')) {
					i--;
				}
				while ((i < cursor) && (text.charAt(i) == ' ')) {
					i++;
				}

				return i;
			}

			@Override
			public CharSequence terminateToken(final CharSequence text) {
				int i = text.length();

				while ((i > 0) && (text.charAt(i - 1) == ' ')) {
					i--;
				}

				if ((i > 0) && (text.charAt(i - 1) == ',')) {
					return text + ",";
				} else {
					if (text instanceof Spanned) {
						final SpannableString sp = new SpannableString(text);
						android.text.TextUtils.copySpansFrom((Spanned) text, 0,
								text.length(), Object.class, sp, 0);
						return sp + ",";
					} else {
						return text + ",";
					}
				}
			}
		};

	}

	@Override
	public void onClick(final View v) {
		Intent intent;
		switch (v.getId()) {

		case R.id.btn_followup:
			try {
				DashboardActivity.arrScheduledPatients.add(Appointment);
				Appointment.setType("followup");
				intent = new Intent(this, SheduleActivity.class);
				intent.putExtra(
						"pos",
						""
								+ DashboardActivity.arrScheduledPatients
										.lastIndexOf(Appointment));
				intent.putExtra("pid", Appointment.getPid());
				intent.putExtra("fid", Appointment.getPfId());
				intent.putExtra("type", "follow_up");
				intent.putExtra("from", "history");
				startActivity(intent);

			} catch (Exception e) {

			}
			break;

		case R.id.btn_refer_from:
			try {
				final PopupMenu popupReferFrom = new PopupMenu(this,
						findViewById(R.id.btn_refer_from));
				// Inflating the Popup using xml file
				// popup.getMenuInflater().inflate(R.menu.shedule,
				// popup.getMenu());
				for (final ReferFromModel referFrom : arrReferFrom) {

					popupReferFrom.getMenu().add(
							referFrom.refer_name + " On "
									+ referFrom.refer_date);
				}
				// registering popup with OnMenuItemClickListener
				popupReferFrom
						.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
							@Override
							public boolean onMenuItemClick(final MenuItem item) {
								for (final ReferFromModel referFrom : arrReferFrom) {
									if (item.getTitle().toString()
											.contains(referFrom.refer_name)) {
										final Intent intent = new Intent(
												PhysicianSoapActivity.this,
												ReferPhysicianSoapActivity.class);
										intent.putExtra("epid", referFrom.ep_id);
										startActivity(intent);
									}

								}

								return true;
							}
						});
				popupReferFrom.show();

			} catch (Exception e) {

			}
			break;

		case R.id.btn_refer_to:
			try {
				final PopupMenu popupReferTo = new PopupMenu(this,
						findViewById(R.id.btn_refer_to));
				// Inflating the Popup using xml file
				// popup.getMenuInflater().inflate(R.menu.shedule,
				// popup.getMenu());
				for (final ReferToModel referTo : arrReferTo) {
					popupReferTo.getMenu().add(
							referTo.refer_name + " On " + referTo.refer_date);
				}
				// registering popup with OnMenuItemClickListener
				popupReferTo
						.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
							@Override
							public boolean onMenuItemClick(final MenuItem item) {
								for (final ReferToModel referTo : arrReferTo) {
									if (item.getTitle().toString()
											.contains(referTo.refer_name)) {
										final Intent intent = new Intent(
												PhysicianSoapActivity.this,
												ReferPhysicianSoapActivity.class);
										intent.putExtra("epid",
												referTo.refer_ep_id);
										startActivity(intent);
									}

								}
								return true;
							}
						});
				popupReferTo.show();

			} catch (Exception e) {

			}
			break;

		case R.id.btn_past_visit:
			try {
				intent = new Intent(this, PrevVisitsActivity.class);
				startActivity(intent);

			} catch (Exception e) {

			}
			break;

		case R.id.btn_submit:
			if (Util.isNumeric(editYearSmoke.getText().toString())
					|| Util.isEmptyString(editYearSmoke.getText().toString())) {
				if (Util.isNumeric(editMonthSmoke.getText().toString())
						|| Util.isEmptyString(editMonthSmoke.getText()
								.toString())) {
					if (Util.isNumeric(editDaySmoke.getText().toString())
							|| Util.isEmptyString(editDaySmoke.getText()
									.toString())) {
						if (Util.isNumeric(editYearAlcohol.getText().toString())
								|| Util.isEmptyString(editYearAlcohol.getText()
										.toString())) {
							if (Util.isNumeric(editMonthAlcohol.getText()
									.toString())
									|| Util.isEmptyString(editMonthAlcohol
											.getText().toString())) {
								if (Util.isNumeric(editDayAlcohol.getText()
										.toString())
										|| Util.isEmptyString(editDayAlcohol
												.getText().toString())) {
									try {
										visitNotes.updateJson();
										Log.i("soap",
												visitNotes.jsonData.toString());
										if (Util.isEmptyString(Appointment
												.getSiid())) {

											createSoap();
										} else {
											postData();
										}

									} catch (Exception e) {

									}

								} else {
									Util.Alertdialog(
											PhysicianSoapActivity.this,
											"Only numeric fields allowed in Alcohol Day(s)");
								}
							} else {
								Util.Alertdialog(PhysicianSoapActivity.this,
										"Only numeric fields allowed in Alcohol Month(s)");
							}
						} else {
							Util.Alertdialog(PhysicianSoapActivity.this,
									"Only numeric fields allowed in Alcohol Year(s)");
						}
					} else {
						Util.Alertdialog(PhysicianSoapActivity.this,
								"Only numeric fields allowed in Smoking Day(s)");
					}
				} else {
					Util.Alertdialog(PhysicianSoapActivity.this,
							"Only numeric fields allowed in Smoking Month(s)");
				}
			} else {
				Util.Alertdialog(PhysicianSoapActivity.this,
						"Only numeric fields allowed in Smoking Year(s)");
			}
			break;

		case R.id.btn_past_subjective:
			try {
				final PopupMenu popupSubjective = new PopupMenu(this,
						findViewById(R.id.btn_past_subjective));
				// Inflating the Popup using xml file
				// popup.getMenuInflater().inflate(R.menu.shedule,
				// popup.getMenu());
				for (final Entry entry : hashFollowUp.entrySet()) {
					popupSubjective.getMenu().add(
							(CharSequence) entry.getValue());
					popupSubjective.getMenu().add(Menu.NONE,
							Integer.parseInt((String) entry.getKey()),
							Menu.NONE, (CharSequence) entry.getValue());
				}
				// registering popup with OnMenuItemClickListener
				popupSubjective
						.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
							@Override
							public boolean onMenuItemClick(final MenuItem item) {

								dialogSubjective("" + item.getItemId());

								return true;
							}
						});
				popupSubjective.show();

			} catch (Exception e) {

			}
			break;

		case R.id.btn_past_examination:
			try {
				final PopupMenu popupExam = new PopupMenu(this,
						findViewById(R.id.btn_past_examination));
				// Inflating the Popup using xml file
				// popup.getMenuInflater().inflate(R.menu.shedule,
				// popup.getMenu());
				for (final Entry entry : hashFollowUp.entrySet()) {
					// popupExam.getMenu().add((CharSequence) entry.getValue());
					popupExam.getMenu().add(Menu.NONE,
							Integer.parseInt((String) entry.getKey()),
							Menu.NONE, (CharSequence) entry.getValue());
				}
				// registering popup with OnMenuItemClickListener
				popupExam
						.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
							@Override
							public boolean onMenuItemClick(final MenuItem item) {
								dialogExam("" + item.getItemId());

								return true;
							}
						});
				popupExam.show();

			} catch (Exception e) {

			}
			break;

		case R.id.btn_past_diagnosis:
			try {
				final PopupMenu popupDiagnosis = new PopupMenu(this,
						findViewById(R.id.btn_past_diagnosis));
				// Inflating the Popup using xml file
				// popup.getMenuInflater().inflate(R.menu.shedule,
				// popup.getMenu());
				for (final Entry entry : hashFollowUp.entrySet()) {
					popupDiagnosis.getMenu().add(Menu.NONE,
							Integer.parseInt((String) entry.getKey()),
							Menu.NONE, (CharSequence) entry.getValue());
				}
				// registering popup with OnMenuItemClickListener
				popupDiagnosis
						.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
							@Override
							public boolean onMenuItemClick(final MenuItem item) {
								dialogDiagnosis("" + item.getItemId());
								return true;
							}
						});
				popupDiagnosis.show();

			} catch (Exception e) {

			}
			break;

		case R.id.btn_past_plan:
			try {
				final PopupMenu popupPlan = new PopupMenu(this,
						findViewById(R.id.btn_past_plan));
				// Inflating the Popup using xml file
				// popup.getMenuInflater().inflate(R.menu.shedule,
				// popup.getMenu());
				for (final Entry entry : hashFollowUp.entrySet()) {
					popupPlan.getMenu().add(Menu.NONE,
							Integer.parseInt((String) entry.getKey()),
							Menu.NONE, (CharSequence) entry.getValue());
				}
				// registering popup with OnMenuItemClickListener
				popupPlan
						.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
							@Override
							public boolean onMenuItemClick(final MenuItem item) {
								dialogPlan("" + item.getItemId());
								return true;
							}
						});
				popupPlan.show();

			} catch (Exception e) {

			}
			break;

		case R.id.txt_subjective:
			Util.showHideView(findViewById(R.id.ll_subjective_1),
					(ImageView) findViewById(R.id.img_subjective));
			break;

		case R.id.txt_hp:
			Util.showHideView(findViewById(R.id.ll_hp),
					(ImageView) findViewById(R.id.img_hp));
			break;

		case R.id.txt_diagnosis:
			Util.showHideView(findViewById(R.id.ll_diagnosis),
					(ImageView) findViewById(R.id.img_diagnosis));
			break;

		case R.id.txt_objective:
			Util.showHideView(findViewById(R.id.ll_objective),
					(ImageView) findViewById(R.id.img_objective));
			break;

		case R.id.txt_vitals:
			Util.showHideView(findViewById(R.id.ll_vitals),
					(ImageView) findViewById(R.id.img_vitals));
			break;

		case R.id.txt_general_exam:
			Util.showHideView(findViewById(R.id.ll_general_exam),
					(ImageView) findViewById(R.id.img_general_exam));
			break;

		case R.id.txt_plan:
			Util.showHideView(findViewById(R.id.ll_plan),
					(ImageView) findViewById(R.id.img_plan));
			break;

		case R.id.btn_prescription:
			try {
				intent = new Intent(this, AddPrescriptionActivity.class);
				intent.putExtra("position",
						getIntent().getIntExtra("position", 0));
				startActivityForResult(intent, 4);

			} catch (Exception e) {

			}
			break;

		case R.id.btn_xray:
			try {
				intent = new Intent(this, RadiologyActivity.class);
				intent.putExtra("position",
						getIntent().getIntExtra("position", 0));
				startActivity(intent);

			} catch (Exception e) {

			}
			break;

		case R.id.btn_lab:
			try {
				intent = new Intent(this, LabOrderActivity.class);
				intent.putExtra("position",
						getIntent().getIntExtra("position", 0));
				startActivity(intent);

			} catch (Exception e) {

			}
			break;

		case R.id.txt_print_prescription:
			try {
				intent = new Intent(this, PrintPrescriptionActivity.class);
				intent.putExtra("position",
						getIntent().getIntExtra("position", 0));
				startActivity(intent);
			} catch (Exception e) {

			}
			break;

		case R.id.txt_print_laborder:
			try {
				intent = new Intent(this, PrintLabOrderedActivity.class);
				intent.putExtra("position",
						getIntent().getIntExtra("position", 0));
				startActivity(intent);
			} catch (Exception e) {

			}
			break;

		case R.id.txt_print_radiology:
			try {
				intent = new Intent(this, PrintRadiologyOrderedActivity.class);
				intent.putExtra("position",
						getIntent().getIntExtra("position", 0));
				startActivity(intent);
			} catch (Exception e) {

			}
			break;

		case R.id.txt_print_all:
			try {
				intent = new Intent(this, PrintAllInvestigationActivity.class);
				intent.putExtra("position",
						getIntent().getIntExtra("position", 0));
				startActivity(intent);
			} catch (Exception e) {

			}
			break;

		case R.id.btn_allergies:
			try {
				final Dialog dialogAllergies = EzDialog.getDialog(
						PhysicianSoapActivity.this, R.layout.dialog_allergies,
						"Add Allergy");
				dialogAllergies.getWindow().setBackgroundDrawable(
						new ColorDrawable(android.graphics.Color.TRANSPARENT));
				((TextView) dialogAllergies.findViewById(R.id.txt_name))
						.setText(pat.getPName() + " , " + pat.getAge() + "/"
								+ pat.getGender().charAt(0));
				final Spinner spinnerMainCategory = (Spinner) dialogAllergies
						.findViewById(R.id.spinner_main_catogery);
				final ArrayList<Allergy> arrMainCategory = (ArrayList<Allergy>) EzApp.allergyDao
						.queryBuilder()
						.where(com.ezhealthtrack.greendao.AllergyDao.Properties.ParentID
								.eq("0")).list();
				final ArrayAdapter<Allergy> adapterMainCatogery = new ArrayAdapter<Allergy>(
						this, android.R.layout.simple_spinner_item,
						arrMainCategory);
				adapterMainCatogery
						.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				spinnerMainCategory.setAdapter(adapterMainCatogery);
				final Spinner spinnerSubCategory = (Spinner) dialogAllergies
						.findViewById(R.id.spinner_sub_category);
				final ArrayList<Allergy> arrSubCatogery = (ArrayList<Allergy>) EzApp.allergyDao
						.queryBuilder()
						.where(com.ezhealthtrack.greendao.AllergyDao.Properties.ParentID
								.eq("1")).list();

				final ArrayAdapter<Allergy> adapterSubCatogery = new ArrayAdapter<Allergy>(
						this, android.R.layout.simple_spinner_item,
						arrSubCatogery);
				adapterSubCatogery
						.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				spinnerSubCategory.setAdapter(adapterSubCatogery);

				spinnerMainCategory
						.setOnItemSelectedListener(new OnItemSelectedListener() {

							@Override
							public void onItemSelected(
									final AdapterView<?> arg0, final View arg1,
									final int position, final long arg3) {
								// visitModel.dentistPlanModel.allergy.hashState.put(
								// "mc", arrMainCategory.get(position));
								arrSubCatogery.clear();
								final ArrayList<Allergy> arr = (ArrayList<Allergy>) EzApp.allergyDao
										.queryBuilder()
										.where(com.ezhealthtrack.greendao.AllergyDao.Properties.ParentID
												.eq(arrMainCategory.get(
														position).getID()))
										.list();
								arrSubCatogery.addAll(arr);
								adapterSubCatogery.notifyDataSetChanged();

							}

							@Override
							public void onNothingSelected(
									final AdapterView<?> arg0) {
								// TODO Auto-generated method stub

							}
						});
				spinnerSubCategory
						.setOnItemSelectedListener(new OnItemSelectedListener() {

							@Override
							public void onItemSelected(
									final AdapterView<?> arg0, final View arg1,
									final int position, final long arg3) {
								// visitModel.dentistPlanModel.allergy.hashState.put(
								// "sc", arrSubCatogery.get(position));
							}

							@Override
							public void onNothingSelected(
									final AdapterView<?> arg0) {
								// TODO Auto-generated method stub

							}
						});
				dialogAllergies.findViewById(R.id.btn_submit)
						.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(final View v) {
								arrMainCategory.get(
										spinnerMainCategory
												.getSelectedItemPosition())
										.getID();
								// if (Util.isEmptyString(((EditText)
								// dialogAllergies
								// .findViewById(R.id.edit_note))
								// .getText().toString())) {
								// Util.Alertdialog(
								// PhysicianSoapActivity.this,
								// "Additional info field cannot be empty.");

								// } else {
								addAllergy(
										arrMainCategory
												.get(spinnerMainCategory
														.getSelectedItemPosition())
												.getID(),
										arrSubCatogery
												.get(spinnerSubCategory
														.getSelectedItemPosition())
												.getID(),
										((EditText) dialogAllergies
												.findViewById(R.id.edit_note))
												.getText().toString(),
										arrMainCategory
												.get(spinnerMainCategory
														.getSelectedItemPosition())
												.getNAME(),
										arrSubCatogery
												.get(spinnerSubCategory
														.getSelectedItemPosition())
												.getNAME());

								dialogAllergies.dismiss();
								// }
							}
						});
				dialogAllergies.setCancelable(false);
				dialogAllergies.show();

			} catch (Exception e) {

			}
			break;

		case R.id.btn_refer:
			try {
				final Dialog dialog = EzDialog.getDialog(
						PhysicianSoapActivity.this, R.layout.dialog_refer,
						"Refer-Patient");
				dialog.getWindow().setBackgroundDrawable(
						new ColorDrawable(android.graphics.Color.TRANSPARENT));
				((TextView) dialog.findViewById(R.id.txt_patient)).setText(pat
						.getPName()
						+ " , "
						+ pat.getAge()
						+ "/"
						+ pat.getGender().charAt(0));
				final AutoCompleteTextView actvDoctor = (AutoCompleteTextView) dialog
						.findViewById(R.id.actv_doctor);
				final ArrayList<String> arrHospital = new ArrayList<String>();
				arrHospital.add("SELECT HOSPITAL");
				arrHospital.add("Appolo Hospital");
				arrHospital.add("Fortis Hospital");
				arrHospital.add("Agarsen Hospital");
				final ArrayList<DoctorModel> arrDoctor = new ArrayList<DoctorModel>();
				final DoctorsAutoCompleteAdapter adapterDoctor = new DoctorsAutoCompleteAdapter(
						this, R.layout.support_simple_spinner_dropdown_item,
						arrDoctor, sharedPref, Appointment.getBkid());
				adapterDoctor
						.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				final ArrayAdapter<String> adapterHospital = new ArrayAdapter<String>(
						this, android.R.layout.simple_spinner_item, arrHospital);
				adapterHospital
						.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
				actvDoctor.setAdapter(adapterDoctor);
				actvDoctor.setThreshold(2);
				final DoctorModel selectedDoctor = new DoctorModel();
				actvDoctor.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(final AdapterView<?> arg0,
							final View arg1, final int arg2, final long arg3) {
						selectedDoctor.name = arrDoctor.get(arg2).name;
						selectedDoctor.id = arrDoctor.get(arg2).id;
						selectedDoctor.speciality = arrDoctor.get(arg2).speciality;
						actvDoctor.setText(selectedDoctor.name + ","
								+ selectedDoctor.speciality);

					}
				});
				final RadioGroup rgRefer = (RadioGroup) dialog
						.findViewById(R.id.rg_refer);
				rgRefer.check(1);
				rgRefer.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(final RadioGroup group,
							final int checkedId) {
						if (checkedId == group.getChildAt(0).getId()) {
							actvDoctor.setAdapter(adapterDoctor);
						}
						// } else {
						// spinnerDoctor.setAdapter(adapterHospital);
						// }

					}
				});
				final RadioButton rb = (RadioButton) dialog
						.findViewById(R.id.rb_doc);
				rb.setChecked(true);
				final Button button = (Button) dialog
						.findViewById(R.id.btn_submit);
				button.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(final View v) {

						if (!Util
								.isEmptyString(actvDoctor.getText().toString())) {
							// final Patient patientModel = new Patient();
							// DashboardActivity.arrRefferedToPatients
							// .add(patientModel);
							if (actvDoctor
									.getText()
									.toString()
									.equals(selectedDoctor.name + ","
											+ selectedDoctor.speciality)) {
								referPatient(
										selectedDoctor.id,
										((EditText) dialog
												.findViewById(R.id.edit_reason))
												.getText().toString(), dialog);
							} else {
								Util.Alertdialog(PhysicianSoapActivity.this,
										"Please select doctor from the list");
							}
						} else {
							Util.Alertdialog(PhysicianSoapActivity.this,
									"Please select doctor");
						}

					}
				});
				dialog.setCancelable(false);
				dialog.show();

			} catch (Exception e) {

			}
			break;

		default:
			break;
		}

	}

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_add_physician_notes);

		SoapNotesController.reset();
		mGalleryView = new EzSoapGalleryView(this);

		mSOAPLabs = null;
		mOrderDetails = new LabOrderDetails();
		PhysicianSoapActivity.arrDocuments.clear();
		physicianVisitNotes = new VisitNotesModel();

		sharedPref = getApplicationContext().getSharedPreferences(
				Constants.EZ_SHARED_PREF, Context.MODE_PRIVATE);
		if (getIntent().hasExtra("position")) {
			if (Util.isEmptyString(getIntent().getStringExtra("from")))
				Appointment = DashboardActivity.arrConfirmedPatients
						.get(getIntent().getIntExtra("position", 0));
			else
				Appointment = DashboardActivity.arrHistoryPatients
						.get(getIntent().getIntExtra("position", 0));
		}
		visitNotes = physicianVisitNotes;
		PatientController.getPatient(Appointment.getPid(),
				Appointment.getPfId(), this, new OnResponsePatient() {

					@Override
					public void onResponseListner(Patient response) {
						// TODO Auto-generated method stub
						patientModel = response;
					}
				});

		cbComplete = (CheckBox) findViewById(R.id.cb_complete);
		txtPatientName = (TextView) findViewById(R.id.txt_patient_name);
		txtDate = (TextView) findViewById(R.id.txt_date);
		txtVisitCount = (TextView) findViewById(R.id.txt_visit_count);
		txtReason = (TextView) findViewById(R.id.txt_reason);
		txtFollowup = (TextView) findViewById(R.id.txt_followup_display);
		cbSmoke = (CheckBox) findViewById(R.id.cb_smoke);
		cbAlcohol = (CheckBox) findViewById(R.id.cb_alcohol);
		rlAlcohol = (RelativeLayout) findViewById(R.id.rl_alcohol);
		rlSmoke = (RelativeLayout) findViewById(R.id.rl_smoke);
		editYearSmoke = (EditText) findViewById(R.id.edit_year);
		editMonthSmoke = (EditText) findViewById(R.id.edit_Month);
		editDaySmoke = (EditText) findViewById(R.id.edit_day);
		editYearAlcohol = (EditText) findViewById(R.id.edit_year1);
		editMonthAlcohol = (EditText) findViewById(R.id.edit_Month1);
		editDayAlcohol = (EditText) findViewById(R.id.edit_day1);
		txtFahrenheit = (TextView) findViewById(R.id.txt_fahrenheit);
		txtFahrenheit.setText(Html.fromHtml("&#176;F"));

		final SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM dd, yyyy");
		final Date date = Appointment.aptDate;
		txtDate.setText(sdf.format(date));
		txtReason.setText("Reason : " + Appointment.getReason());

		findViewById(R.id.txt_subjective).setOnClickListener(this);
		findViewById(R.id.txt_hp).setOnClickListener(this);
		findViewById(R.id.txt_diagnosis).setOnClickListener(this);
		findViewById(R.id.txt_objective).setOnClickListener(this);
		findViewById(R.id.txt_vitals).setOnClickListener(this);
		findViewById(R.id.txt_general_exam).setOnClickListener(this);
		findViewById(R.id.btn_prescription).setOnClickListener(this);
		findViewById(R.id.btn_allergies).setOnClickListener(this);
		findViewById(R.id.btn_refer).setOnClickListener(this);
		findViewById(R.id.txt_plan).setOnClickListener(this);
		findViewById(R.id.btn_xray).setOnClickListener(this);
		findViewById(R.id.btn_lab).setOnClickListener(this);
		findViewById(R.id.btn_past_subjective).setOnClickListener(this);
		findViewById(R.id.btn_past_diagnosis).setOnClickListener(this);
		findViewById(R.id.btn_past_examination).setOnClickListener(this);
		findViewById(R.id.btn_past_plan).setOnClickListener(this);
		findViewById(R.id.btn_submit).setOnClickListener(this);
		findViewById(R.id.btn_past_visit).setOnClickListener(this);
		findViewById(R.id.btn_refer_from).setOnClickListener(this);
		findViewById(R.id.btn_refer_to).setOnClickListener(this);
		findViewById(R.id.txt_print_prescription).setOnClickListener(this);
		findViewById(R.id.txt_print_radiology).setOnClickListener(this);
		findViewById(R.id.txt_print_laborder).setOnClickListener(this);
		findViewById(R.id.txt_print_all).setOnClickListener(this);
		findViewById(R.id.btn_followup).setOnClickListener(this);
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, arrIcd);
		actvProv = (MultiAutoCompleteTextView) findViewById(R.id.actv_provisonal_diagnosis);
		actvProv.setText(visitNotes.diagnosisModel.pd);
		actvProv.setAdapter(adapter);
		actvProv.setTokenizer(getTokenizer());
		actvProv.addTextChangedListener(getTextWatcher("pd"));

		actvFinal = (MultiAutoCompleteTextView) findViewById(R.id.actv_final_diagnosis);
		actvFinal.setText(visitNotes.diagnosisModel.fd);
		actvFinal.setAdapter(adapter);
		actvFinal.setTokenizer(getTokenizer());
		actvFinal.addTextChangedListener(getTextWatcher("fd"));

		cbSmoke.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (cbSmoke.isChecked()) {
					rlSmoke.setVisibility(View.VISIBLE);
				} else {
					rlSmoke.setVisibility(View.GONE);
				}

			}
		});

		cbAlcohol.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (cbAlcohol.isChecked()) {
					rlAlcohol.setVisibility(View.VISIBLE);
				} else {
					rlAlcohol.setVisibility(View.GONE);
				}
			}
		});
		// if (cbSmoke.getTag() != null) {
		// cbSmoke.setChecked(true);
		// cbSmoke.setSelected(true);
		// rlSmoke.setVisibility(View.VISIBLE);
		// } else {
		// cbSmoke.setChecked(false);
		// rlSmoke.setVisibility(View.GONE);
		// }
		//
		// if (cbAlcohol.getTag() != null) {
		// cbAlcohol.setChecked(true);
		// rlAlcohol.setVisibility(View.VISIBLE);
		// } else {
		// cbAlcohol.setChecked(false);
		// rlAlcohol.setVisibility(View.GONE);
		// }

		getData();
		try {
			// List<SoapNote> arrSoap = EzHealthApplication.soapNoteDao
			// .queryBuilder()
			// .where(Properties.Id.eq((long) Integer.parseInt(Appointment
			// .getBkid()))).list();
			// Log.i("", "arr Soap =" + arrSoap.size());
			// if (arrSoap.size() == 0) {
			InputStream fis;
			fis = getAssets().open("physiciansoapjson.txt");
			final StringBuffer fileContent = new StringBuffer("");
			final byte[] buffer = new byte[1024];
			while (fis.read(buffer) != -1) {
				fileContent.append(new String(buffer));
			}
			final String s = String.valueOf(fileContent);
			final JSONObject jObj = new JSONObject(s);
			jObj.put("template_id", "soap_physician_v1");
			jObj.put("user_id", sharedPref.getString(Constants.USER_ID, ""));
			jObj.put("encounter_id", Appointment.getBkid());
			jObj.put("episode_id", Appointment.getEpid());
			// Log.i("", Appointment.epId);
			visitNotes.jsonData = jObj;
			final JSONObject soap = jObj.getJSONObject("Soap");
			visitNotes.subjectiveModel.JsonParse(soap.getJSONObject("subj"));
			visitNotes.privateNote
					.jsonParse(soap.getJSONObject("private-note"));
			visitNotes.diagnosisModel.JsonParse(soap.getJSONObject("asse"),
					actvFinal, actvProv);
			visitNotes.examinationModel.JsonParse(soap.getJSONObject("obje"));
			visitNotes.physicianPlanModel.JsonParse(soap.getJSONObject("plan"));
			AutoSave();
			// } else {
			// visitNotes.jsonData = new
			// JSONObject(arrSoap.get(0).getNote());
			// final JSONObject soap = visitNotes.jsonData
			// .getJSONObject("Soap");
			// visitNotes.subjectiveModel
			// .JsonParse(soap.getJSONObject("subj"));
			// visitNotes.privateNote.jsonParse(soap
			// .getJSONObject("private-note"));
			// visitNotes.diagnosisModel.JsonParse(soap.getJSONObject("asse"),
			// actvFinal, actvProv);
			// visitNotes.examinationModel.JsonParse(soap
			// .getJSONObject("obje"));
			// visitNotes.physicianPlanModel.JsonParse(soap
			// .getJSONObject("plan"));
			// for (final Map.Entry<String, String> entry :
			// visitNotes.subjectiveModel.hashHP
			// .entrySet()) {
			// if (!Util.isEmptyString(entry.getValue())) {
			// findViewById(R.id.img_hp_tick).setVisibility(
			// View.VISIBLE);
			// }
			// }
			// AutoSave();
			// }
		} catch (final Exception e) {

			e.printStackTrace();
		}
		AutoSave();
		EzCommonViews.radiologyTable(PhysicianSoapActivity.this, arrDocuments);
		EzCommonViews.labTable(PhysicianSoapActivity.this, arrDocuments);
		EzCommonViews.ekgTable(PhysicianSoapActivity.this, arrDocuments);
		prescriptionTable(this);
		findViewById(R.id.btn_ekg).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ekgUploadDialog();

			}
		});

		final EditText editBMI = (EditText) findViewById(R.id.edit_bmi);
		final EditText editHeight = (EditText) findViewById(R.id.edit_height);
		final EditText editWeight = (EditText) findViewById(R.id.edit_weight);
		TextView txtBMIUnit = (TextView) findViewById(R.id.txt_bmi_unit);

		txtBMIUnit.setText(Html.fromHtml("kg/m<sup>2</sup>"));

		editHeight.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				Double s;
				try {
					s = Double.parseDouble(editHeight.getText().toString()) * 0.01;
					s = Math.pow(s, 2);
					s = Double.parseDouble(editWeight.getText().toString()) / s;
					editBMI.setText(Util.doubleFormat(s.toString()));
				} catch (Exception e) {
					editBMI.setText("");
				}

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub

			}
		});

		editWeight.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
				Double s;
				try {
					s = Double.parseDouble(editHeight.getText().toString()) * 0.01;
					s = Math.pow(s, 2);
					s = Double.parseDouble(editWeight.getText().toString()) / s;
					editBMI.setText(Util.doubleFormat(s.toString()));
				} catch (Exception e) {
					editBMI.setText("");
				}
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub

			}
		});
	}

	@Override
	protected void onResume() {
		EzCommonViews.radiologyTable(PhysicianSoapActivity.this, arrDocuments);
		EzCommonViews.labTable(PhysicianSoapActivity.this, arrDocuments);
		prescriptionTable(this);
		boolean radiologyflag = false;
		((TextView) findViewById(R.id.txt_radiology)).setText(Html
				.fromHtml("<b>Radiology :  </b>"));
		for (final Entry<String, String> entry : visitNotes.physicianPlanModel.radiology.hashRadiology
				.entrySet()) {
			if (entry.getValue().equals("on")) {
				((TextView) findViewById(R.id.txt_radiology)).append("\n      "
						+ entry.getKey() + ", ");
				radiologyflag = true;
			}

		}
		try {
			findViewById(R.id.txt_print_radiology).setActivated(radiologyflag);
			findViewById(R.id.txt_print_radiology).setEnabled(radiologyflag);
			findViewById(R.id.txt_print_radiology).setClickable(radiologyflag);
		} catch (Exception e) {

		}

		((TextView) findViewById(R.id.txt_allergy)).setText(Html
				.fromHtml("<b>Allergy :  </b>"));
		for (final AllergiesModel allergy : arrallergies) {
			((TextView) findViewById(R.id.txt_allergy)).append("\n "
					+ allergy.getMCatName() + " -->" + allergy.getSCatName()
					+ "\n " + allergy.getAddiInfo());
		}
		boolean labflag = false;
		((TextView) findViewById(R.id.txt_lab)).setText(Html
				.fromHtml("<b>Lab Order :  </b>"));
		for (final Entry<String, String> entry : visitNotes.physicianPlanModel.lab.hashLab
				.entrySet()) {
			if (entry.getValue().equals("on")) {
				String[] result = entry.getKey().split("_");
				((TextView) findViewById(R.id.txt_lab)).append("\n      "
						+ entry.getKey().replace(
								"_" + result[result.length - 1], "") + ", ");
				labflag = true;
			}
		}
		try {
			findViewById(R.id.txt_print_laborder).setActivated(labflag);
			findViewById(R.id.txt_print_laborder).setEnabled(labflag);
			findViewById(R.id.txt_print_laborder).setClickable(labflag);
		} catch (Exception e) {

		}

		((TextView) findViewById(R.id.txt_prescription)).setText(Html
				.fromHtml("<b>Prescription :  </b>"));

		if (!Util.isEmptyString(visitNotes.physicianPlanModel.prescription.si
				.get("si"))
				&& visitNotes.physicianPlanModel.prescription.arrMedicine
						.size() > 0)
			((TextView) findViewById(R.id.txt_special_instruction))
					.setText(Html.fromHtml("<b>Special Instruction :</b> "
							+ visitNotes.physicianPlanModel.prescription.si
									.get("si")));

		// Handling Print Prescription button visibility
		if (visitNotes.physicianPlanModel.prescription.arrMedicine.size() > 0) {
			try {
				findViewById(R.id.txt_print_prescription).setActivated(true);
				findViewById(R.id.txt_print_prescription).setEnabled(true);
				findViewById(R.id.txt_print_prescription).setClickable(true);
			} catch (Exception e) {

			}
		} else {
			try {
				findViewById(R.id.txt_print_prescription).setActivated(false);
				findViewById(R.id.txt_print_prescription).setEnabled(false);
				findViewById(R.id.txt_print_prescription).setClickable(false);
			} catch (Exception e) {

			}
		}

		// Handling Print all Investigation button visibility
		if (radiologyflag == true
				|| labflag == true
				|| visitNotes.physicianPlanModel.prescription.arrMedicine
						.size() > 0) {
			try {
				findViewById(R.id.txt_print_all).setActivated(true);
				findViewById(R.id.txt_print_all).setEnabled(true);
				findViewById(R.id.txt_print_all).setClickable(true);
			} catch (Exception e) {

			}
		} else {
			try {
				findViewById(R.id.txt_print_all).setActivated(false);
				findViewById(R.id.txt_print_all).setEnabled(false);
				findViewById(R.id.txt_print_all).setClickable(false);
			} catch (Exception e) {

			}
		}
		final String url = APIs.SOAP_SHOW() + Appointment.getSiid() + "/bk_id/"
				+ Appointment.getBkid();
		final StringRequest patientListRequest = new StringRequest(
				Request.Method.POST, url, new Response.Listener<String>() {
					@Override
					public void onResponse(final String response) {
						try {
							JSONObject jObj = new JSONObject(response);

							jObj = new JSONObject(response);

							if (jObj.getString("s").equals("200")) {
								Log.i("tag", response);

								if (jObj.getJSONObject("data").has(
										"followup_appointment"))
									txtFollowup.setText(jObj.getJSONObject(
											"data").getString(
											"followup_appointment"));

							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(final VolleyError error) {
						Util.Alertdialog(PhysicianSoapActivity.this,
								"Network Error, try again later");

						Log.e("Error.Response", error);

					}
				}) {
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				final HashMap<String, String> loginParams = new HashMap<String, String>();
				loginParams.put("auth-token", Util.getBase64String(sharedPref
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
		super.onResume();
	}

	private void pastFollowUp() {
		final String url = APIs.PAST_FOLLOWUP() + Appointment.getBkid()
				+ "/follow-id/" + Appointment.getFollowid();
		final Dialog loaddialog = Util
				.showLoadDialog(PhysicianSoapActivity.this);
		final StringRequest patientListRequest = new StringRequest(
				Request.Method.POST, url, new Response.Listener<String>() {
					@Override
					public void onResponse(final String response) {
						try {
							// Log.i("past visits", response);
							final JSONObject jObj = new JSONObject(response);
							if (jObj.getString("s").equals("200")) {
								try {
									txtVisitCount.setText("No. of visit : "
											+ (jObj.getJSONArray("data")
													.length() + 1));
									for (int i = 0; i < jObj.getJSONArray(
											"data").length(); i++) {
										String date = jObj.getJSONArray("data")
												.getJSONObject(i)
												.getString("date");
										try {
											date = EzApp.sdfOnmmm
													.format(EzApp.sdfyyMmdd
															.parse(date));
										} catch (ParseException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}

										hashFollowUp.put(
												jObj.getJSONArray("data")
														.getJSONObject(i)
														.getString("id"), date);

									}

									if (Util.isEmptyString(Appointment
											.getSiid())) {
										getLastSubjective(jObj
												.getJSONArray("data")
												.getJSONObject(
														jObj.getJSONArray(
																"data")
																.length() - 1)
												.getString("id"));
									}
								} catch (final JSONException e1) {

									e1.printStackTrace();
								}

							} else {

							}
						} catch (final JSONException e) {
							Util.Alertdialog(PhysicianSoapActivity.this,
									"There is some error while fetching data, please try again");
							Log.e("", e);
						}
						loaddialog.dismiss();
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(final VolleyError error) {
						Util.Alertdialog(PhysicianSoapActivity.this,
								"Network Error, try again later");

						error.printStackTrace();
						loaddialog.dismiss();
					}
				}) {
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				final HashMap<String, String> loginParams = new HashMap<String, String>();
				loginParams.put("auth-token", Util.getBase64String(sharedPref
						.getString(Constants.USER_TOKEN, "")));
				return loginParams;
			}

			@Override
			protected Map<String, String> getParams() {
				final HashMap<String, String> loginParams = new HashMap<String, String>();
				loginParams.put("format", "json");
				Log.i("Login Params", loginParams.toString());
				return loginParams;
			}

		};
		patientListRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 5,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		EzApp.mVolleyQueue.add(patientListRequest);
	}

	private void postData() {
		final String url = APIs.SOAP_UPDATE() + Appointment.getSiid()
				+ "/cli/api";
		final Dialog loaddialog = Util
				.showLoadDialog(PhysicianSoapActivity.this);
		final JsonObjectRequest patientListRequest = new JsonObjectRequest(
				Request.Method.POST, url, visitNotes.jsonData,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(final JSONObject jObj) {
						String s = jObj.toString();
						while (s.length() > 100) {
							s = s.substring(100);
						}
						try {
							if (jObj.getString("s").equals("200")) {
								Util.AlertdialogWithFinish(
										PhysicianSoapActivity.this,
										"Visit Notes updated successfully");
								// Toast.makeText(PhysicianSoapActivity.this,
								// "Template Updated Successfully.",
								// Toast.LENGTH_SHORT).show();

							} else {
								Log.e("", jObj.toString());
								Util.Alertdialog(PhysicianSoapActivity.this,
										"Network Error, try again later");
							}
						} catch (final JSONException e) {
							Util.Alertdialog(PhysicianSoapActivity.this,
									"There is some error while fetching data, please try again");
							Log.e("", e);
						}
						loaddialog.dismiss();
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(final VolleyError error) {
						Util.Alertdialog(PhysicianSoapActivity.this,
								"Network Error, try again later");

						Log.e("Error.Response", error);
						loaddialog.dismiss();
					}
				}) {
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				final HashMap<String, String> loginParams = new HashMap<String, String>();
				loginParams.put("auth-token", Util.getBase64String(sharedPref
						.getString(Constants.USER_TOKEN, "")));
				return loginParams;
			}

			@Override
			protected Map<String, String> getParams() {
				final HashMap<String, String> loginParams = new HashMap<String, String>();
				loginParams.put("format", "json");
				if (cbComplete.isChecked())
					loginParams.put("complete", "on");
				else
					loginParams.put("complete", "off");
				return loginParams;
			}

		};
		patientListRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 5,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		EzApp.mVolleyQueue.add(patientListRequest);

	}

	private void referPatient(final String drId, final String reason,
			final Dialog dialog) {
		final String url = APIs.REFER_PATIENT_CREATE();
		final Dialog loaddialog = Util
				.showLoadDialog(PhysicianSoapActivity.this);
		final StringRequest patientListRequest = new StringRequest(
				Request.Method.POST, url, new Response.Listener<String>() {
					@Override
					public void onResponse(final String response) {
						try {
							Log.i("refer response", response);
							final JSONObject jObj = new JSONObject(response);
							if (jObj.getString("s").equals("200")) {
								Util.Alertdialog(PhysicianSoapActivity.this,
										"Patient has been referred successfully");
								dialog.dismiss();
							} else {
								Util.Alertdialog(PhysicianSoapActivity.this,
										"There is some error while referring, please try again.");
							}
						} catch (final JSONException e) {
							Util.Alertdialog(PhysicianSoapActivity.this,
									"There is some error while referring, please try again.");
							Log.e("", e);
						}
						loaddialog.dismiss();
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(final VolleyError error) {
						Util.Alertdialog(PhysicianSoapActivity.this,
								"Network error, try again later");

						error.printStackTrace();
						loaddialog.dismiss();
					}
				}) {
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				final HashMap<String, String> loginParams = new HashMap<String, String>();
				loginParams.put("auth-token", Util.getBase64String(sharedPref
						.getString(Constants.USER_TOKEN, "")));
				return loginParams;
			}

			@Override
			protected Map<String, String> getParams() {
				final HashMap<String, String> loginParams = new HashMap<String, String>();
				loginParams.put("format", "json");
				loginParams.put("pat-id", Appointment.getPid());
				loginParams.put("fam-id", patientModel.getFid());
				loginParams.put("ep-id", Appointment.getEpid());
				loginParams.put("doc-id",
						sharedPref.getString(Constants.ROLE_ID, ""));// role_id
				loginParams.put("user-id",
						sharedPref.getString(Constants.USER_ID, ""));
				loginParams.put("pfn", patientModel.getPfn());
				loginParams.put("pln", patientModel.getPln());
				loginParams.put("reason", reason);
				loginParams.put("refer_type", "doctor");
				loginParams.put("doctor", drId);
				Log.i("Login Params", loginParams.toString());
				return loginParams;
			}

		};
		patientListRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 5,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		EzApp.mVolleyQueue.add(patientListRequest);
	}

	@Override
	protected void onPause() {
		visitNotes.updateJson();
		SoapNote note = new SoapNote();
		note.setDate(new Date());
		note.setNote(visitNotes.jsonData.toString());
		note.setBk_id(Appointment.getBkid());
		note.setId((long) Integer.parseInt(Appointment.getBkid()));
		EzApp.soapNoteDao.insertOrReplace(note);
		super.onPause();
	}

	private void addAllergy(final String mc, final String sc, final String ai,
			final String mName, final String sName) {
		final String url = APIs.ADD_ALLERGIES();
		final Dialog loaddialog = Util
				.showLoadDialog(PhysicianSoapActivity.this);
		final StringRequest patientListRequest = new StringRequest(
				Request.Method.POST, url, new Response.Listener<String>() {
					@Override
					public void onResponse(final String response) {
						try {
							Log.i("add allergy", response);
							final JSONObject jObj = new JSONObject(response);
							if (jObj.getString("s").equals("200")) {
								AllergiesModel model = new AllergiesModel();
								model.setAddiInfo(ai);
								model.setMCatName(mName);
								model.setSCatName(sName);
								arrallergies.add(model);
								Util.Alertdialog(PhysicianSoapActivity.this,
										"Allergies added successfully");
								onResume();
							} else {
								Util.Alertdialog(PhysicianSoapActivity.this,
										"There is some error in adding allergies, please try again.");
							}
						} catch (final JSONException e) {
							Util.Alertdialog(PhysicianSoapActivity.this,
									"There is some error in adding allergies, please try again.");
							Log.e("", e);
						}
						loaddialog.dismiss();
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(final VolleyError error) {
						Util.Alertdialog(PhysicianSoapActivity.this,
								"Network error,try again later");
						error.printStackTrace();
						loaddialog.dismiss();
					}
				}) {
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				final HashMap<String, String> loginParams = new HashMap<String, String>();
				loginParams.put("auth-token", Util.getBase64String(sharedPref
						.getString(Constants.USER_TOKEN, "")));
				return loginParams;
			}

			@Override
			protected Map<String, String> getParams() {
				final HashMap<String, String> params = new HashMap<String, String>();
				params.put("format", "json");
				params.put("bk_id", Appointment.getBkid());
				params.put("main_cat", mc);
				params.put("sub_cat", sc);
				params.put("addi_info", ai);
				return params;
			}

		};
		patientListRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 5,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		EzApp.mVolleyQueue.add(patientListRequest);
	}

	private Dialog dialog;
	final int FILE_CHOOSER = 1;

	private void ekgUploadDialog() {
		dialog = EzDialog.getDialog(PhysicianSoapActivity.this,
				R.layout.dialog_ekg_upload, "EKG/ECG Upload Document");

		dialog.findViewById(R.id.btn_upload).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(PhysicianSoapActivity.this,
								FileChooserActivity.class);
						startActivityForResult(intent, FILE_CHOOSER);

					}
				});
		dialog.findViewById(R.id.btn_click).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						String dn = ((EditText) dialog
								.findViewById(R.id.edit_document_name))
								.getText().toString();
						if (Util.isEmptyString(dn)) {
							Util.Alertdialog(PhysicianSoapActivity.this,
									"Please enter document name before taking an Image.");
						} else {
							imageDialog();
						}

					}
				});
		dialog.findViewById(R.id.btn_submit).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						String stdt = "ekg";
						stdt = stdt.replaceAll(" ", "+");
						String dn = ((EditText) dialog
								.findViewById(R.id.edit_document_name))
								.getText().toString();
						File f = new File(((TextView) dialog
								.findViewById(R.id.txt_upload)).getText()
								.toString());
						if (Util.isEmptyString(dn)) {
							Util.Alertdialog(PhysicianSoapActivity.this,
									"Please enter document name.");
						} else if (!f.exists()) {
							Util.Alertdialog(PhysicianSoapActivity.this,
									"Please select document file.");
						} else {

							uploadEkgDocument(
									new File(((TextView) dialog
											.findViewById(R.id.txt_upload))
											.getText().toString()), stdt, dn);
						}

					}
				});
		dialog.setCancelable(false);
		dialog.show();
	}

	private void uploadEkgDocument(final File image, final String dt,
			final String dn) {
		Log.i("", dn);
		final Dialog dialog1 = Util.showLoadDialog(this);

		String requestURL = APIs.URL()
				+ "/documents/reportUpload/cli/api?context_type=soap&context_id="
				+ Integer.parseInt(PhysicianSoapActivity.Appointment.getBkid())
				+ "&document_type=ECG/EKG&section_name=ekg&document_name=" + dn;

		Log.i("", requestURL);
		UploadDocumentRequest request = new UploadDocumentRequest(requestURL,
				new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError e) {
						dialog1.dismiss();
						Util.Alertdialog(PhysicianSoapActivity.this,
								"Network error,try again later");
						Log.e("", e);

					}
				}, new Listener<String>() {

					@Override
					public void onResponse(String arg0) {
						try {
							JSONObject jObj = new JSONObject(arg0);
							Document doc = new Gson().fromJson(jObj
									.getJSONObject("data").toString(),
									Document.class);
							doc.setSection(jObj.getJSONObject("data")
									.getString("section-name"));
							PhysicianSoapActivity.arrDocuments.add(doc);
							EzCommonViews.ekgTable(PhysicianSoapActivity.this,
									arrDocuments);
							dialog1.dismiss();
							dialog.dismiss();
							Util.Alertdialog(PhysicianSoapActivity.this,
									"Document uploaded successfully.");
						} catch (JSONException e) {
							dialog1.dismiss();
							Util.Alertdialog(PhysicianSoapActivity.this,
									"There is some error in uploading document, please try again");
							Log.e("", e);
						}
						Log.i("", arg0);

					}
				}, image, "Documents[document]", "Documents[document]") {
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				final HashMap<String, String> headerParams = new HashMap<String, String>();
				headerParams.put("auth-token", Util
						.getBase64String(EzApp.sharedPref.getString(
								Constants.USER_TOKEN, "")));
				headerParams.put("Accept-Encoding", "deflate");
				headerParams.put("Accept", "application/json");
				headerParams.put("Content-Type",
						"multipart/form-data; boundary=" + "--eriksboundry--");
				return headerParams;
			}

			@Override
			public String getBodyContentType() {
				String BOUNDARY = "--eriksboundry--";
				return "multipart/form-data; boundary=" + BOUNDARY;
			}

			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				final HashMap<String, String> params = new HashMap<String, String>();
				params.put("context_type", "soap");
				params.put("context_id",
						AddDentistNotesActivity.appointment.getBkid());
				params.put("document_type", "IOPA");
				params.put("section_name", "radiology");
				params.put("document_name", image.getName());
				return params;
			}

		};
		request.setRetryPolicy(new DefaultRetryPolicy(200000, 5,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

		Log.i("", new Gson().toJson(request));

		EzApp.mVolleyQueue.add(request);

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if ((requestCode == FILE_CHOOSER) && (resultCode == RESULT_OK)) {
			String fileSelected = data
					.getStringExtra(com.orleonsoft.android.simplefilechooser.Constants.KEY_FILE_SELECTED);
			try {
				((TextView) dialog.findViewById(R.id.txt_upload))
						.setText(fileSelected);
			} catch (Exception e) {

			}
		} else if ((requestCode == CAMERA_REQUEST) && (resultCode == RESULT_OK)) {
			selectedImageUri = Uri.parse("d");
			if (bitmapImage != null)
				bitmapImage.recycle();
			bitmapImage = decodeFile(image.getPath());
			bitmapImage = Util.getResizedBitmap(bitmapImage, 400, 400);
			// ((ImageView) findViewById(R.id.img_user))
			// .setImageBitmap(bitmapImage);
			String dn = ((EditText) dialog
					.findViewById(R.id.edit_document_name)).getText()
					.toString();
			uploadEkgDocument(image, "Self", dn);

		}
	}

	public static Bitmap decodeFile(String path) {// you can provide file path
		// here
		int orientation;
		try {
			if (path == null) {
				return null;
			}
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			// Find the correct scale value. It should be the power of 2.
			final int REQUIRED_SIZE = 70;
			int width_tmp = o.outWidth, height_tmp = o.outHeight;
			int scale = 0;
			while (true) {
				if (width_tmp / 2 < REQUIRED_SIZE
						|| height_tmp / 2 < REQUIRED_SIZE)
					break;
				width_tmp /= 2;
				height_tmp /= 2;
				scale++;
			}
			// decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			// Log.i(TAG, "" + scale);
			o2.inSampleSize = scale;
			o2.inPreferredConfig = Bitmap.Config.ARGB_8888;
			Bitmap bm = BitmapFactory.decodeFile(path, o2);
			Bitmap bitmap = bm;

			ExifInterface exif = new ExifInterface(path);

			orientation = exif
					.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);

			Log.e("ExifInteface .........", "rotation =" + orientation);

			// exif.setAttribute(ExifInterface.ORIENTATION_ROTATE_90, 90);

			Log.e("orientation", "" + orientation);
			Matrix m = new Matrix();

			if ((orientation == ExifInterface.ORIENTATION_ROTATE_180)) {
				m.postRotate(180);
				// m.postScale((float) bm.getWidth(), (float) bm.getHeight());
				// if(m.preRotate(90)){
				Log.e("in orientation", "" + orientation);
				bitmap = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
						bm.getHeight(), m, true);
				return bitmap;
			} else if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
				m.postRotate(90);
				Log.e("in orientation", "" + orientation);
				bitmap = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
						bm.getHeight(), m, true);
				return bitmap;
			} else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
				m.postRotate(270);
				Log.e("in orientation", "" + orientation);
				bitmap = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
						bm.getHeight(), m, true);
				return bitmap;
			}
			return bitmap;
		} catch (Exception e) {
			return null;
		}

	}

	public boolean onOptionsItemSelected(final MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {

		case android.R.id.home:
			final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					this);

			// set title
			alertDialogBuilder
					.setTitle("Changes will be discarded, Do you want to continue ?");
			// set dialog message
			alertDialogBuilder
					.setCancelable(false)
					.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									finish();

								}

							})
					.setNegativeButton("No",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(
										final DialogInterface dialog,
										final int id) {
									// if this button is clicked, just close
									// the dialog box and do nothing
									dialog.cancel();
								}
							});

			final AlertDialog alertDialog = alertDialogBuilder.create();

			alertDialog.show();

			return true;

		case R.id.action_close:
			final AlertDialog.Builder alertDialogBuilder1 = new AlertDialog.Builder(
					this);

			// set title
			alertDialogBuilder1
					.setTitle("Changes will be discarded, Do you want to continue ?");
			// set dialog message
			alertDialogBuilder1
					.setCancelable(false)
					.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									finish();

								}

							})
					.setNegativeButton("No",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(
										final DialogInterface dialog,
										final int id) {
									// if this button is clicked, just close
									// the dialog box and do nothing
									dialog.cancel();
								}
							});

			final AlertDialog alertDialog1 = alertDialogBuilder1.create();

			alertDialog1.show();

			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onBackPressed() {
		final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				this);

		// set title
		alertDialogBuilder
				.setTitle("Changes will be discarded, Do you want to continue ?");
		// set dialog message
		alertDialogBuilder
				.setCancelable(false)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								finish();

							}

						})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(final DialogInterface dialog,
							final int id) {
						// if this button is clicked, just close
						// the dialog box and do nothing
						dialog.cancel();
					}
				});

		final AlertDialog alertDialog = alertDialogBuilder.create();

		alertDialog.show();

	}

	private File image;
	private static final int CAMERA_REQUEST = 1888;
	private Uri selectedImageUri;
	public static Bitmap bitmapImage;

	private void imageDialog() {
		String[] addPhoto;
		addPhoto = new String[] { "Camera" };
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		dialog.setTitle("Select Image for your profile");

		dialog.setItems(addPhoto, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				File imagesFolder = new File(Environment
						.getExternalStorageDirectory(), "MyImages");
				imagesFolder.mkdirs();
				String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
						.format(new Date());
				image = new File(imagesFolder, timeStamp + ".jpg");

				if (id == 0) {
					Intent cameraIntent = new Intent(
							android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
					selectedImageUri = Uri.fromFile(image);
					cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,
							selectedImageUri);
					startActivityForResult(cameraIntent, CAMERA_REQUEST);
					dialog.dismiss();
				}
			}
		});

		dialog.setNeutralButton("Cancel",
				new android.content.DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

						dialog.dismiss();
					}
				});
		dialog.show();
	}

	// Report Print

	// private void dialogPrintReport(final TestReport report) {
	// final Dialog dialogPrint = new Dialog(this);
	// dialogPrint.requestWindowFeature(Window.FEATURE_NO_TITLE);
	//
	// dialogPrint.setContentView(R.layout.labs_dialog_print_report);
	// dialogPrint.getWindow().setBackgroundDrawable(
	// new ColorDrawable(android.graphics.Color.TRANSPARENT));
	// LayoutParams params = dialogPrint.getWindow().getAttributes();
	// params.width = 2500;
	// dialogPrint.getWindow().setAttributes(
	// (android.view.WindowManager.LayoutParams) params);
	// TextView txtLabName = (TextView) dialogPrint
	// .findViewById(R.id.txt_lab_name);
	// TextView txtLabMotto = (TextView) dialogPrint
	// .findViewById(R.id.txt_lab_motto);
	// TextView txtLabAddress = (TextView) dialogPrint
	// .findViewById(R.id.txt_lab_address);
	// TextView txtLabPhone = (TextView) dialogPrint
	// .findViewById(R.id.txt_phone_lab);
	// TextView txtPatientName = (TextView) dialogPrint
	// .findViewById(R.id.txt_patient_name);
	// TextView txtPatientAddress = (TextView) dialogPrint
	// .findViewById(R.id.txt_patient_address);
	// TextView txtPhone = (TextView) dialogPrint.findViewById(R.id.txt_phone);
	// TextView txtEmail = (TextView) dialogPrint.findViewById(R.id.txt_email);
	// TextView txtOrderId = (TextView) dialogPrint
	// .findViewById(R.id.txt_order_id_display);
	// TextView txtReportingDate = (TextView) dialogPrint
	// .findViewById(R.id.txt_date_display);
	// TextView txtReportAvailDate = (TextView) dialogPrint
	// .findViewById(R.id.txt_report_avai_date_display);
	// TextView txtTech = (TextView) dialogPrint
	// .findViewById(R.id.txt_tech_name);
	// TextView txtPanel = (TextView) dialogPrint.findViewById(R.id.txt_panel);
	// RelativeLayout rlLabInfo = (RelativeLayout) dialogPrint
	// .findViewById(R.id.rl_lab_info);
	// rlLabInfo.setVisibility(View.VISIBLE);
	//
	// try {
	// Data data = mOrderDetails.getData();
	//
	// txtLabName.setText(data.getLabName());
	// txtLabMotto.setText(data.getLabMoto());
	// txtLabAddress.setText(data.getLabAddress());
	// for (LabContactNumber cn : data.getLabContactNumber()) {
	// txtLabPhone.setText(cn.getNum());
	// }
	// txtPatientName.setText(data.getPatientDetail());
	// txtPatientAddress.setText(data.getPatientAddress());
	// txtPhone.setText(data.getPatientContactNumber());
	// txtEmail.setText(data.getPatientEmail());
	// txtOrderId.setText(data.getDisplayOrderId());
	//
	// Date date;
	// String theDate = report.getReportPreparedOn();
	// if (!Util.isEmptyString(theDate)) {
	// date = EzHealthApplication.sdfyymmddhhmmss.parse(theDate);
	// txtReportingDate.setText(EzHealthApplication.sdfddMmyy
	// .format(date));
	// }
	// theDate = report.getReportAvailableOn();
	// if (!Util.isEmptyString(theDate)) {
	// date = EzHealthApplication.sdfyymmddhhmmss.parse(theDate);
	// txtReportAvailDate.setText(EzHealthApplication.sdfddMmyy
	// .format(date));
	// }
	//
	// txtTech.setText(data.getTechnicianName());
	// // if (!Util.isEmptyString(EzHealthApplication.sharedPref.getString(
	// // Constants.SIGNATURE, "signature"))) {
	// // Util.getImageFromUrl(EzHealthApplication.sharedPref.getString(
	// // Constants.SIGNATURE, ""), DashboardActivity.imgLoader,
	// // (ImageView) dialogPrint
	// // .findViewById(R.id.img_lab_signature));
	// // }
	//
	// } catch (ParseException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	//
	// LinearLayout llReportValues = (LinearLayout) dialogPrint
	// .findViewById(R.id.ll_test_name_display);
	// llReportValues.removeAllViews();
	//
	// if (report.getSampleMeta().size() > 1) {
	// txtPanel.setVisibility(View.VISIBLE);
	// txtPanel.setText(Html.fromHtml("<b>Lab Panel: </b>"
	// + report.getReportName()));
	// }
	//
	// for (final SampleMetum sm1 : report.getSampleMeta()) {
	// final SampleMetum sm;
	// if (sm1.getResults().size() == 0) {
	// sm = sm1.sample_meta.get(0);
	// } else {
	// sm = sm1;
	// }
	//
	// final View v = getLayoutInflater().inflate(
	// R.layout.labs_row_print_report_test_list, null);
	// final TextView txtTestName = (TextView) v
	// .findViewById(R.id.txt_test_name_display);
	// final TextView txtMethod = (TextView) v
	// .findViewById(R.id.txt_method);
	// final TextView txtMethodDisplay = (TextView) v
	// .findViewById(R.id.txt_method_display);
	// final TextView txtSample1 = (TextView) v
	// .findViewById(R.id.txt_sample);
	// final TextView txtSampleDisplay = (TextView) v
	// .findViewById(R.id.txt_sample_display);
	// final TextView InterpretationText = (TextView) v
	// .findViewById(R.id.txt_interpretation_text);
	// txtMethod.setVisibility(View.GONE);
	// txtMethodDisplay.setVisibility(View.GONE);
	// txtSample1.setVisibility(View.GONE);
	// txtSampleDisplay.setVisibility(View.GONE);
	// InterpretationText.setVisibility(View.GONE);
	//
	// txtTestName.setText(report.getReportName());
	//
	// // if (!report.getSampleMeta().equals("0")) {
	// // if (!Util.isEmptyString(sm.getMethod())) {
	// // txtMethodDisplay.setText(sm.getMethod());
	// // }
	// // } else {
	// // txtMethod.setVisibility(View.GONE);
	// // txtMethodDisplay.setVisibility(View.GONE);
	// // }
	// // if (!report.getSampleMeta().equals("0")) {
	// // txtSampleDisplay.setText(sm.getName());
	// // } else {
	// // txtSample1.setVisibility(View.GONE);
	// // txtSampleDisplay.setVisibility(View.GONE);
	// // }
	// // WebView txt = (WebView) v.findViewById(R.id.wv_interpret);
	//
	// // final List<Result> arrResults;
	//
	// // if (sm.getdisplay_interpretation() == true) {
	// // txt.setVisibility(View.VISIBLE);
	// // InterpretationText.setVisibility(View.VISIBLE);
	// // } else {
	// // txt.setVisibility(View.GONE);
	// // InterpretationText.setVisibility(View.GONE);
	// // }
	//
	// // if (sm.getResults().size() > 0) {
	// // arrResults = sm.getResults();
	// // // txt.loadDataWithBaseURL("file:///android_asset/",
	// // // sm.result_interpretation, "text/html", "UTF-8", null);
	// // } else {
	// // arrResults = sm.sample_meta.get(0).getResults();
	// // // txt.loadDataWithBaseURL("file:///android_asset/",
	// // // sm.sample_meta.get(0).result_interpretation,
	// // // "text/html", "UTF-8", null);
	// // }
	//
	// final LinearLayout llResults = (LinearLayout) v
	// .findViewById(R.id.ll_test_name_display_list);
	// for (final Result res : sm.getResults()) {
	// if (!Util.isEmptyString(res.getValue())) {
	// final View v1 = getLayoutInflater()
	// .inflate(
	// R.layout.labs_row_print_report_test_list_list,
	// null);
	// llResults.addView(v1);
	// final TextView txtTestNameMain1 = (TextView) v1
	// .findViewById(R.id.txt_test_name_display);
	// final TextView txtUnit = (TextView) v1
	// .findViewById(R.id.txt_unit_display);
	// final TextView txtReferenceRange = (TextView) v1
	// .findViewById(R.id.txt_r_range_display);
	// final TextView txtNotes = (TextView) v1
	// .findViewById(R.id.txt_notes_display);
	// final TextView editObservedValue = (TextView) v1
	// .findViewById(R.id.txt_observed_value_display);
	// final TextView editNotes = (TextView) v1
	// .findViewById(R.id.txt_note_display);
	// final TextView txtnote = (TextView) v1
	// .findViewById(R.id.txt_note);
	// // final TextView txtGroupName = (TextView) v1
	// // .findViewById(R.id.txt_group_name);
	//
	// txtUnit.setText(res.getUnit());
	// txtTestNameMain1.setText(res.getName());
	// editObservedValue.setText(res.getValue());
	//
	// if (!Util.isEmptyString(res.getNotes())) {
	// txtnote.setVisibility(View.VISIBLE);
	// editNotes.setVisibility(View.VISIBLE);
	// editNotes.setText(res.getNotes());
	// } else {
	// txtnote.setVisibility(View.GONE);
	// editNotes.setVisibility(View.GONE);
	// }
	//
	// // if (!Util.isEmptyString(res.getGroupName())) {
	// // txtGroupName.setVisibility(View.VISIBLE);
	// // txtGroupName.setText(res.getGroupName());
	// // } else {
	// // txtGroupName.setText("");
	// // txtGroupName.setVisibility(View.GONE);
	// // }
	//
	// for (Reference result : res.getReferences()) {
	// // if (result.getGender().equals(data)
	// // || result.getGender().equals("Both")) {
	// // // Log.e("age", ""+pat.getage());
	// // if (true) {
	// if (!result.getRangeValueMinOption().contains("None")) {
	// txtReferenceRange.append(result
	// .getRangeValueMinOption());
	// }
	// if (result.getRangeValueMin().length() > 0) {
	// txtReferenceRange.append(" "
	// + result.getRangeValueMin() + " (min)");
	// }
	// if (!result.getRangeValueMaxOption().contains("None")) {
	// txtReferenceRange.append(" "
	// + result.getRangeValueMaxOption());
	// }
	// if (result.getRangeValueMax().length() > 0) {
	// txtReferenceRange.append(" "
	// + result.getRangeValueMax() + " (max)");
	// }
	// if (!Util.isEmptyString(result.getNotes()))
	// txtNotes.append(result.getNotes());
	// txtReferenceRange.append("\n");
	// txtNotes.append("\n");
	// }
	// }
	// }
	// // }
	// // }
	//
	// llReportValues.addView(v);
	// }
	//
	// dialogPrint.setCancelable(false);
	// dialogPrint.findViewById(R.id.txt_close).setOnClickListener(
	// new OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	//
	// dialogPrint.dismiss();
	//
	// }
	// });
	// // dialogPrint.findViewById(R.id.btn_print).performClick();
	// dialogPrint.findViewById(R.id.btn_print).setOnClickListener(
	// new OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	// FlurryAgent
	// .logEvent("LabsOrderDetailsActivity - Print Report(Print) Button Clicked");
	// PrintHelper photoPrinter = new PrintHelper(
	// PhysicianSoapActivity.this);
	//
	// photoPrinter.setScaleMode(PrintHelper.SCALE_MODE_FIT);
	// View view = (dialogPrint.findViewById(R.id.rl_print));
	// // view.setDrawingCacheEnabled(true);
	// // view.buildDrawingCache();
	// Bitmap bm = loadBitmapFromView(view, 1190, 1684);
	// // view.destroyDrawingCache();
	// // Bitmap bitmap =
	// // BitmapFactory.decodeResource(getResources(),
	// // R.drawable.rs_15);
	//
	// photoPrinter.printBitmap("Report", bm);
	// }
	// });
	//
	// dialogPrint.show();
	//
	// }
	//
	// public static Bitmap loadBitmapFromView(View v, int width, int height) {
	// Bitmap b = Bitmap.createBitmap(v.getWidth(), v.getHeight(),
	// Bitmap.Config.ARGB_8888);
	// Canvas c = new Canvas(b);
	// v.layout(0, 0, v.getWidth(), v.getHeight());
	// v.draw(c);
	// return b;
	// }

	// public static void prescriptionTableSOAPNotes(Activity context) {
	// final TableLayout tl = (TableLayout) context
	// .findViewById(R.id.table_prescription);
	// tl.removeAllViews();
	//
	// if (visitNotes.physicianPlanModel.prescription.arrMedicine.size() < 1) {
	// tl.setVisibility(View.GONE);
	// return;
	// }
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

	// Prescription Table
	public static void prescriptionTable(Activity context) {
		final TableLayout tl = (TableLayout) context
				.findViewById(R.id.table_prescription);
		tl.removeAllViews();

		// if (visitNotes.physicianPlanModel.prescription.arrMedicine
		// .size() < 1) {
		// tl.setVisibility(View.GONE);
		// return;
		// }
		final TableRow row = (TableRow) context.getLayoutInflater().inflate(
				R.layout.row_prescription_table_vnotes, null, false);

		tl.addView(row);

		int i = 1;
		for (final MedicineModel doc : visitNotes.physicianPlanModel.prescription.arrMedicine) {

			if (doc.getMedicineString().length() > 0) {
				final TableRow row1 = (TableRow) context.getLayoutInflater()
						.inflate(R.layout.row_prescription_table_vnotes, null,
								false);

				TextView txtSno = (TextView) row1.findViewById(R.id.txt_sno);
				TextView txtFormulation = (TextView) row1
						.findViewById(R.id.txt_formulation);
				TextView txtDrugName = (TextView) row1
						.findViewById(R.id.txt_name);
				TextView txtDetails = (TextView) row1
						.findViewById(R.id.txt_detail);
				TextView txtFreqDetail = (TextView) row1
						.findViewById(R.id.txt_frequency_detail);
				TextView txtRefills = (TextView) row1
						.findViewById(R.id.txt_refills);
				TextView txtNotes = (TextView) row1
						.findViewById(R.id.txt_notes);
				TextView txtHistory = (TextView) row1
						.findViewById(R.id.txt_history);

				txtSno.setText(String.valueOf(i));
				txtFormulation.setText(Html.fromHtml(doc.formulations));
				txtDrugName.setText(Html.fromHtml(doc.name));
				txtDetails.setText(Html.fromHtml(doc.strength + " " + doc.unit
						+ "(" + doc.route + ")"));
				txtFreqDetail.setText(Html.fromHtml(doc.frequency + " (Days-"
						+ doc.times + ")"));
				txtRefills.setText(Html.fromHtml(doc.refills));
				txtNotes.setText(Html.fromHtml(doc.notes));

				// get current date
				// start{
				Calendar c = Calendar.getInstance();
				System.out.println("Current time => " + c.getTime());
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				String formattedDate = df.format(c.getTime());
				// }end

				if (doc.addedon == null || doc.addedon.equals("")) {
					txtHistory.setText("Added on " + formattedDate);
				} else {
					if (!Util.isEmptyString(doc.updatedon)) {
						txtHistory.setText("Added On: " + doc.addedon
								+ ", Updated on " + doc.updatedon);
					} else {
						txtHistory.setText("Added On: " + doc.addedon);
					}
				}

				tl.addView(row1);
				i++;
			}
		}
	}
}
