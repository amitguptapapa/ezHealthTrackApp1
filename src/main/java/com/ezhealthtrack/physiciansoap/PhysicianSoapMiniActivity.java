package com.ezhealthtrack.physiciansoap;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ezhealthrack.api.APIs;
import com.ezhealthtrack.EzApp;
import com.ezhealthtrack.R;
import com.ezhealthtrack.DentistSoap.Model.PastVisitModel;
import com.ezhealthtrack.controller.SoapNotesController;
import com.ezhealthtrack.controller.SoapNotesController.SoapNotesResponseHandler;
import com.ezhealthtrack.greendao.Appointment;
import com.ezhealthtrack.model.AllergiesModel;
import com.ezhealthtrack.model.Document;
import com.ezhealthtrack.model.MedicineModel;
import com.ezhealthtrack.one.EzUtils;
import com.ezhealthtrack.physiciansoap.model.VisitNotesModel;
import com.ezhealthtrack.util.Constants;
import com.ezhealthtrack.util.EzActivity;
import com.ezhealthtrack.util.Log;
import com.ezhealthtrack.util.Util;
import com.ezhealthtrack.views.EzSoapGalleryView;
import com.ezhealthtrack.views.TextUtils;
import com.google.gson.Gson;

public class PhysicianSoapMiniActivity extends EzActivity {
	SoapNotesController mController;
	EzSoapGalleryView mGalleryView;

	public static Appointment Appointment;
	public static VisitNotesModel visitNotes;
	private SharedPreferences sharedPref;
	public static final ArrayList<PastVisitModel> arrPastVisit = new ArrayList<PastVisitModel>();
	private static com.ezhealthtrack.physiciansoap.model.VisitNotesModel physicianVisitNotes = new com.ezhealthtrack.physiciansoap.model.VisitNotesModel();

	private String visit;
	private TextView txtFahrenheit;

	// Subjective
	private TextView txtHeaderSubjective;
	private RelativeLayout rlSubjective;

	// H&P
	private TextView txtHeaderHP;
	private RelativeLayout rlHistoryOfPresent;
	private RelativeLayout rlPastMedicalhistory;
	private RelativeLayout rlFamilyHistory;
	private RelativeLayout rlPersonalHistory;
	private RelativeLayout rlDrugHistory;
	private CheckBox cbSmoke;
	private CheckBox cbAlcohol;
	private RelativeLayout rlSmoke;
	private RelativeLayout rlAlcohol;
	private RelativeLayout rlNote;
	private RelativeLayout rlSymptoms;

	// Examination
	private TextView txtExamination;

	// Vitals
	private TextView txtVitals;
	private RelativeLayout rlBP;
	private RelativeLayout rlPulse;
	private RelativeLayout rlRespiratoryRate;
	private RelativeLayout rlTemperature;
	private RelativeLayout rlWeight;
	private RelativeLayout rlHeight;
	private RelativeLayout rlWaist;
	private RelativeLayout rlNoteVital;
	// private LinearLayout llNoteVital;
	private RelativeLayout rlBMI;

	// General Examination
	private RelativeLayout rlGeneralExamination;
	private TextView txtGeneralExamination;
	private RelativeLayout rlSystemicExamination;
	private RelativeLayout rlNoteGE;

	// Diagnosis
	private TextView txtDiagnosis;
	// private RelativeLayout rlDiagnosis;
	private TextView txtProvisonalDiagnosis;
	private TextView txtProvisonalDiagnosisDisplay;
	private TextView txtFinalDiagnosis;
	private TextView txtFinalDiagnosisDisplay;

	// Plan
	private TextView txtPlan;
	private TextView txtRadiology;
	private TextView txtLabOrder;
	private TextView txtEKG;
	private TextView txtPrescription;
	private TextView txtSpecialInstruction;
	private TextView txtAllergy;
	private TextView txtTreatmentDone;
	private RelativeLayout rlTreatmentPlan;
	private LinearLayout llPrivateNotes;

	public static ArrayList<Document> arrDocuments = new ArrayList<Document>();
	private ArrayList<AllergiesModel> arrallergies = new ArrayList<AllergiesModel>();

	private void AutoSave() {

		// Allergies
		for (final AllergiesModel allergy : arrallergies) {
			if (!allergy.getMCatName().isEmpty()
					&& !allergy.getSCatName().isEmpty()) {
				txtAllergy.setText(Html.fromHtml("<b>Allergy</b> "));
				txtAllergy.setVisibility(View.VISIBLE);
				txtPlan.setVisibility(View.VISIBLE);
				txtAllergy.append("\n " + allergy.getMCatName() + " -->"
						+ allergy.getSCatName() + allergy.getAddiInfo());
			}
		}
		TextUtils.autoSaveTextViewLayout(
				(LinearLayout) findViewById(R.id.ll_hp),
				visitNotes.subjectiveModel.hashHP);
		TextUtils.autoSaveTextView(
				(TextView) findViewById(R.id.edit_cheif_complaint),
				visitNotes.subjectiveModel.hashCC);

		// Subjective
		if (!Util.isEmptyString(visitNotes.subjectiveModel.hashCC.get("cc"))) {
			txtHeaderSubjective.setVisibility(View.VISIBLE);
			rlSubjective.setVisibility(View.VISIBLE);
			((TextView) findViewById(R.id.edit_cheif_complaint))
					.setText(visitNotes.subjectiveModel.hashCC.get("cc"));
		}

		// H&P
		if (!Util.isEmptyString(visitNotes.subjectiveModel.hashHP.get("hpi"))
				|| !Util.isEmptyString(visitNotes.subjectiveModel.hashHP
						.get("pmh"))
				|| !Util.isEmptyString(visitNotes.subjectiveModel.hashHP
						.get("fh"))
				|| !Util.isEmptyString(visitNotes.subjectiveModel.hashHP
						.get("ph"))
				|| !Util.isEmptyString(visitNotes.subjectiveModel.hashHP
						.get("dha"))
				|| !Util.isEmptyString(visitNotes.subjectiveModel.hashHP
						.get("sy"))
				|| !Util.isEmptyString(visitNotes.subjectiveModel.hashHP
						.get("sm"))
				|| !Util.isEmptyString(visitNotes.subjectiveModel.hashHP
						.get("sd"))
				|| !Util.isEmptyString(visitNotes.subjectiveModel.hashHP
						.get("ay"))
				|| !Util.isEmptyString(visitNotes.subjectiveModel.hashHP
						.get("am"))
				|| !Util.isEmptyString(visitNotes.subjectiveModel.hashHP
						.get("ad"))
				|| !Util.isEmptyString(visitNotes.subjectiveModel.hashHP
						.get("note"))) {
			txtHeaderHP.setVisibility(View.VISIBLE);
			((View) findViewById(R.id.rl_hp)).setVisibility(View.VISIBLE);
		}
		if (!Util.isEmptyString(visitNotes.subjectiveModel.hashHP.get("hpi"))) {
			rlHistoryOfPresent.setVisibility(View.VISIBLE);
		}

		if (!Util.isEmptyString(visitNotes.subjectiveModel.hashHP.get("pmh"))) {
			rlPastMedicalhistory.setVisibility(View.VISIBLE);
		}

		if (!Util.isEmptyString(visitNotes.subjectiveModel.hashHP.get("fh"))) {
			rlFamilyHistory.setVisibility(View.VISIBLE);
		}

		if (!Util.isEmptyString(visitNotes.subjectiveModel.hashHP.get("ph"))) {
			rlPersonalHistory.setVisibility(View.VISIBLE);
		}

		if (!Util.isEmptyString(visitNotes.subjectiveModel.hashHP.get("dha"))) {
			rlDrugHistory.setVisibility(View.VISIBLE);
		}

		if (!Util.isEmptyString(visitNotes.subjectiveModel.hashHP.get("sy"))
				|| !Util.isEmptyString(visitNotes.subjectiveModel.hashHP
						.get("sm"))
				|| !Util.isEmptyString(visitNotes.subjectiveModel.hashHP
						.get("sd"))) {
			rlSmoke.setVisibility(View.VISIBLE);
			cbSmoke.setVisibility(View.VISIBLE);
		}

		if (!Util.isEmptyString(visitNotes.subjectiveModel.hashHP.get("ay"))
				|| !Util.isEmptyString(visitNotes.subjectiveModel.hashHP
						.get("am"))
				|| !Util.isEmptyString(visitNotes.subjectiveModel.hashHP
						.get("ad"))) {
			rlAlcohol.setVisibility(View.VISIBLE);
			cbAlcohol.setVisibility(View.VISIBLE);
		}

		if (!Util.isEmptyString(visitNotes.subjectiveModel.hashHP.get("note"))) {
			rlNote.setVisibility(View.VISIBLE);
		}

		// Examination
		if (!Util.isEmptyString(visitNotes.examinationModel.hashVitals
				.get("low"))
				|| !Util.isEmptyString(visitNotes.examinationModel.hashVitals
						.get("high"))
				|| !Util.isEmptyString(visitNotes.examinationModel.hashVitals
						.get("temp"))
				|| !Util.isEmptyString(visitNotes.examinationModel.hashVitals
						.get("rr"))
				|| !Util.isEmptyString(visitNotes.examinationModel.hashVitals
						.get("low"))
				|| !Util.isEmptyString(visitNotes.examinationModel.hashVitals
						.get("puls"))
				|| !Util.isEmptyString(visitNotes.examinationModel.hashVitals
						.get("weig"))
				|| !Util.isEmptyString(visitNotes.examinationModel.hashVitals
						.get("heig"))
				|| !Util.isEmptyString(visitNotes.examinationModel.hashVitals
						.get("waist"))
				|| !Util.isEmptyString(visitNotes.examinationModel.hashVitals
						.get("bmi"))
				|| !Util.isEmptyString(visitNotes.examinationModel.hashVitals
						.get("note"))
				|| !Util.isEmptyString(visitNotes.examinationModel.hashSymptoms
						.get("symp"))
				|| !Util.isEmptyString(visitNotes.examinationModel.hashGE
						.get("ge"))
				|| !Util.isEmptyString(visitNotes.examinationModel.hashSE
						.get("se"))
				|| !Util.isEmptyString(visitNotes.examinationModel.hashNote
						.get("note"))) {
			txtExamination.setVisibility(View.VISIBLE);
		}

		// Vitals
		if (!Util.isEmptyString(visitNotes.examinationModel.hashVitals
				.get("low"))
				|| !Util.isEmptyString(visitNotes.examinationModel.hashVitals
						.get("high"))
				|| !Util.isEmptyString(visitNotes.examinationModel.hashVitals
						.get("temp"))
				|| !Util.isEmptyString(visitNotes.examinationModel.hashVitals
						.get("rr"))
				|| !Util.isEmptyString(visitNotes.examinationModel.hashVitals
						.get("low"))
				|| !Util.isEmptyString(visitNotes.examinationModel.hashVitals
						.get("puls"))
				|| !Util.isEmptyString(visitNotes.examinationModel.hashVitals
						.get("weig"))
				|| !Util.isEmptyString(visitNotes.examinationModel.hashVitals
						.get("heig"))
				|| !Util.isEmptyString(visitNotes.examinationModel.hashVitals
						.get("waist"))
				|| !Util.isEmptyString(visitNotes.examinationModel.hashVitals
						.get("bmi"))
				|| !Util.isEmptyString(visitNotes.examinationModel.hashVitals
						.get("note"))
				|| !Util.isEmptyString(visitNotes.examinationModel.hashSymptoms
						.get("symp"))) {
			txtVitals.setVisibility(View.VISIBLE);
		}

		if (!Util.isEmptyString(visitNotes.examinationModel.hashVitals
				.get("low"))
				|| !Util.isEmptyString(visitNotes.examinationModel.hashVitals
						.get("high"))) {
			rlBP.setVisibility(View.VISIBLE);
		}

		if (!Util.isEmptyString(visitNotes.examinationModel.hashVitals
				.get("temp"))) {
			rlTemperature.setVisibility(View.VISIBLE);
		}
		if (!Util.isEmptyString(visitNotes.examinationModel.hashVitals
				.get("rr"))) {
			rlRespiratoryRate.setVisibility(View.VISIBLE);
		}
		if (!Util.isEmptyString(visitNotes.examinationModel.hashVitals
				.get("puls"))) {
			rlPulse.setVisibility(View.VISIBLE);
		}
		if (!Util.isEmptyString(visitNotes.examinationModel.hashVitals
				.get("weig"))) {
			rlWeight.setVisibility(View.VISIBLE);
		}

		if (!Util.isEmptyString(visitNotes.examinationModel.hashVitals
				.get("heig"))) {
			rlHeight.setVisibility(View.VISIBLE);
		}

		if (!Util.isEmptyString(visitNotes.examinationModel.hashVitals
				.get("waist"))) {
			rlWaist.setVisibility(View.VISIBLE);
		}

		if (!Util.isEmptyString(visitNotes.examinationModel.hashVitals
				.get("bmi"))) {
			rlBMI.setVisibility(View.VISIBLE);
		}

		if (!Util.isEmptyString(visitNotes.examinationModel.hashVitals
				.get("note"))) {
			rlNoteVital.setVisibility(View.VISIBLE);
		}

		if (!Util.isEmptyString(visitNotes.examinationModel.hashSymptoms
				.get("symp"))) {
			rlSymptoms.setVisibility(View.VISIBLE);
		}

		// General Examination
		if (!Util.isEmptyString(visitNotes.examinationModel.hashGE.get("ge"))) {
			rlGeneralExamination.setVisibility(View.VISIBLE);

		}

		if (!Util.isEmptyString(visitNotes.examinationModel.hashSE.get("se"))) {
			rlSystemicExamination.setVisibility(View.VISIBLE);
		}

		if (!Util.isEmptyString(visitNotes.examinationModel.hashNote
				.get("note"))) {
			rlNoteGE.setVisibility(View.VISIBLE);
		}

		// Diagnosis
		if (!Util.isEmptyString(visitNotes.diagnosisModel.fd)
				&& !Util.isEmptyString(visitNotes.diagnosisModel.pd)) {
			txtDiagnosis.setVisibility(View.VISIBLE);
		}

		if (!Util.isEmptyString(visitNotes.diagnosisModel.fd)) {
			txtFinalDiagnosis.setVisibility(View.VISIBLE);
			txtFinalDiagnosisDisplay.setVisibility(View.VISIBLE);
		}

		if (!Util.isEmptyString(visitNotes.diagnosisModel.pd)) {
			txtProvisonalDiagnosis.setVisibility(View.VISIBLE);
			txtProvisonalDiagnosisDisplay.setVisibility(View.VISIBLE);
		}

		// Plan

		// Radiology
		for (final Entry<String, String> entry : PhysicianSoapActivityMain.physicianVisitNotes.physicianPlanModel.radiology.hashRadiology
				.entrySet()) {
			if (entry.getValue().equals("on")) {
				if (!entry.getKey().isEmpty()) {
					txtRadiology.setVisibility(View.VISIBLE);
					txtPlan.setVisibility(View.VISIBLE);
					txtRadiology.setText(Html.fromHtml("<b>Radiology</b>"));
					txtRadiology.append("\n      " + entry.getKey());
				}
			}

		}

		// EKG
		ArrayList<Document> arrRadiDocuments = new ArrayList<Document>();
		for (final Document doc : arrDocuments) {
			if (doc.getSection().equalsIgnoreCase("ekg"))
				arrRadiDocuments.add(doc);
		}
		if (arrRadiDocuments.size() > 0) {
			txtEKG.setVisibility(View.VISIBLE);
			txtPlan.setVisibility(View.VISIBLE);
		}

		// Prescription

		if (visitNotes.physicianPlanModel.prescription.arrMedicine.size() < 1) {
			txtPrescription.setVisibility(View.GONE);
		} else {
			txtPrescription.setVisibility(View.VISIBLE);
			txtPlan.setVisibility(View.VISIBLE);
			prescriptionTable(PhysicianSoapMiniActivity.this);
		}

		if (!Util.isEmptyString(visitNotes.physicianPlanModel.prescription.si
				.get("si"))
				|| visitNotes.physicianPlanModel.prescription.arrMedicine
						.size() > 0) {
			txtSpecialInstruction.setVisibility(View.VISIBLE);
			txtPlan.setVisibility(View.VISIBLE);
			txtSpecialInstruction.setText("Special Instruction: "
					+ visitNotes.physicianPlanModel.prescription.si.get("si"));
		}

		// Lab Order
		for (final Entry<String, String> entry : visitNotes.physicianPlanModel.lab.hashLab
				.entrySet()) {
			if (entry.getValue().equals("on")) {
				if (!entry.getKey().isEmpty()) {
					txtLabOrder.setText(Html.fromHtml("<b>Lab Order</b> "));
					txtLabOrder.setVisibility(View.VISIBLE);
					txtPlan.setVisibility(View.VISIBLE);
					txtLabOrder.append("\n      " + entry.getKey());
				}
			}
		}
		if (!Util.isEmptyString(getIntent().getStringExtra("type"))
				|| (visitNotes.privateNote.hashNote.containsKey("status") && !visitNotes.privateNote.hashNote
						.get("status").equals("Pr"))) {
			if (visitNotes.privateNote.hashNote.containsKey("private-note")) {
				((TextView) findViewById(R.id.txt_private_notes))
						.setText(visitNotes.privateNote.hashNote
								.get("private-note"));
			}
		} else {
			((TextView) findViewById(R.id.txt_private_notes))
					.setText("Not allowed to view private notes. ");
		}
		((TextView) findViewById(R.id.actv_final_diagnosis))
				.setText(visitNotes.diagnosisModel.fd);
		((TextView) findViewById(R.id.actv_provisonal_diagnosis))
				.setText(visitNotes.diagnosisModel.pd);

		// Treatment Plan
		if (!Util.isEmptyString(visitNotes.physicianPlanModel.hashTp.get("tp"))) {
			rlTreatmentPlan.setVisibility(View.VISIBLE);
			txtPlan.setVisibility(View.VISIBLE);
		}

		// Private Notes
		if (!Util.isEmptyString(visitNotes.privateNote.hashNote
				.get("private-note"))) {
			llPrivateNotes.setVisibility(View.VISIBLE);
		}

		TextUtils.autoSaveTextViewLayout(
				(LinearLayout) findViewById(R.id.ll_vitals),
				visitNotes.examinationModel.hashVitals);
		TextUtils.autoSaveTextView((TextView) findViewById(R.id.edit_symptoms),
				visitNotes.examinationModel.hashSymptoms);
		TextUtils.autoSaveTextView(
				(TextView) findViewById(R.id.edit_general_exam),
				visitNotes.examinationModel.hashGE);
		TextUtils.autoSaveTextView((TextView) findViewById(R.id.edit_se),
				visitNotes.examinationModel.hashSE);
		TextUtils.autoSaveTextView((TextView) findViewById(R.id.edit_ex_note),
				visitNotes.examinationModel.hashNote);
		TextUtils.autoSaveTextView((TextView) findViewById(R.id.edit_tp),
				visitNotes.physicianPlanModel.hashTp);

		if (visitNotes.physicianPlanModel.treatmentDone.getTreatmentDone()
				.length() > 0) {
			txtTreatmentDone.setVisibility(View.VISIBLE);
			txtTreatmentDone
					.setText(visitNotes.physicianPlanModel.treatmentDone
							.getTreatmentDone());
		}

		// if (txtRadiology.getVisibility() == View.GONE
		// && txtLabOrder.getVisibility() == View.GONE
		// && txtEKG.getVisibility() == View.GONE
		// && txtPrescription.getVisibility() == View.GONE
		// && txtSpecialInstruction.getVisibility() == View.GONE
		// && txtAllergy.getVisibility() == View.GONE) {
		// txtPlan.setVisibility(View.GONE);
		// } else {
		// txtPlan.setVisibility(View.VISIBLE);
		// }
	}

	// private void getData() {
	// final String url = APIs.REFER_VIEW() + Appointment.getEpid()
	// + "/cli/api";
	// final Dialog loaddialog = Util
	// .showLoadDialog(PhysicianSoapMiniActivity.this);
	// Log.i("Url", url);
	// final StringRequest patientListRequest = new StringRequest(
	// Request.Method.POST, url, new Response.Listener<String>() {
	// @Override
	// public void onResponse(final String response) {
	// try {
	// final JSONObject jObj = new JSONObject(response);
	// if (jObj.getString("s").equals("200")) {
	// if (Util.isEmptyString(jObj
	// .getString("tmpl-inst"))) {
	// Util.AlertdialogWithFinish(
	// PhysicianSoapMiniActivity.this,
	// "Referred Appointment not started yet");
	// loaddialog.dismiss();
	// } else {
	// arrallergies.clear();
	// if (jObj.has("allergies")) {
	// JSONArray arr = jObj
	// .getJSONArray("allergies");
	// for (int i = 0; i < arr.length(); i++) {
	// AllergiesModel allergy = new Gson()
	// .fromJson(
	// arr.get(i)
	// .toString(),
	// AllergiesModel.class);
	// arrallergies.add(allergy);
	// }
	// }
	// arrDocuments.clear();
	// if (jObj.has("documents")) {
	// JSONArray arr = jObj
	// .getJSONArray("documents");
	// for (int i = 0; i < arr.length(); i++) {
	// Document doc = new Gson().fromJson(
	// arr.get(i).toString(),
	// Document.class);
	// arrDocuments.add(doc);
	// }
	// radiologyTable(PhysicianSoapMiniActivity.this);
	// labTable(PhysicianSoapMiniActivity.this);
	// ekgTable(PhysicianSoapMiniActivity.this);
	// }
	// if (Util.isEmptyString(jObj
	// .getString("tmpl-inst")))
	// Util.AlertdialogWithFinish(
	// PhysicianSoapMiniActivity.this,
	// "No notes to share yet.");
	// loaddialog.dismiss();
	// final JSONObject followUpList = jObj
	// .getJSONObject("followupList");
	// visit = followUpList.getString("visit");
	//
	// final JSONObject soap = jObj.getJSONObject(
	// "tmpl-inst").getJSONObject("Soap");
	// visitNotes.jsonData = jObj
	// .getJSONObject("tmpl-inst");
	// visitNotes.subjectiveModel.JsonParse(soap
	// .getJSONObject("subj"));
	// visitNotes.privateNote.jsonParse(soap
	// .getJSONObject("private-note"));
	// visitNotes.diagnosisModel.JsonParse(soap
	// .getJSONObject("asse"));
	//
	// visitNotes.examinationModel.JsonParse(soap
	// .getJSONObject("obje"));
	// visitNotes.physicianPlanModel
	// .JsonParse(soap
	// .getJSONObject("plan"));
	//
	// AutoSave();
	// }
	//
	// } else {
	// Util.AlertdialogWithFinish(
	// PhysicianSoapMiniActivity.this,
	// "Referred Appointment not started yet");
	// }
	// } catch (final JSONException e) {
	// Util.AlertdialogWithFinish(
	// PhysicianSoapMiniActivity.this,
	// "Referred Appointment not started yet");
	// Log.e("", e);
	// }
	// loaddialog.dismiss();
	// }
	// }, new Response.ErrorListener() {
	// @Override
	// public void onErrorResponse(final VolleyError error) {
	// Util.AlertdialogWithFinish(
	// PhysicianSoapMiniActivity.this,
	// "Referred Appointment not started yet");
	// loaddialog.dismiss();
	// Log.e("Error.Response", error);
	// }
	// }) {
	// @Override
	// public Map<String, String> getHeaders() throws AuthFailureError {
	// final HashMap<String, String> loginParams = new HashMap<String,
	// String>();
	// loginParams.put("auth-token", Util.getBase64String(sharedPref
	// .getString(Constants.USER_TOKEN, "")));
	// return loginParams;
	// }
	//
	// @Override
	// protected Map<String, String> getParams() {
	// final HashMap<String, String> loginParams = new HashMap<String,
	// String>();
	// loginParams.put("format", "json");
	// return loginParams;
	// }
	//
	// };
	// patientListRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 5,
	// DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
	// EzHealthApplication.mVolleyQueue.add(patientListRequest);
	//
	// }

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_soap_notes_mini);
		this.setDisplayHomeAsUpEnabled(true);

		if (!EzUtils.getDeviceSize(this).equals(EzUtils.EZ_SCREEN_LARGE)) {
			mScreenRotation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
		}

		mController = new SoapNotesController();
		mGalleryView = new EzSoapGalleryView(this);

		arrDocuments.clear();

		sharedPref = getApplicationContext().getSharedPreferences(
				Constants.EZ_SHARED_PREF, Context.MODE_PRIVATE);

		Appointment = new Appointment();
		Appointment.setEpid(getIntent().getStringExtra("epid"));
		Appointment.setBkid(getIntent().getStringExtra("bkid"));
		Appointment.setSiid(getIntent().getStringExtra("siid"));

		txtFahrenheit = (TextView) findViewById(R.id.txt_Fahrenheit);
		txtFahrenheit.setText(Html.fromHtml("&#176;F"));

		// Subjective
		txtHeaderSubjective = (TextView) findViewById(R.id.txt_subjective);
		rlSubjective = (RelativeLayout) findViewById(R.id.rl_sujective);

		// H&P
		txtHeaderHP = (TextView) findViewById(R.id.txt_hp);
		rlHistoryOfPresent = (RelativeLayout) findViewById(R.id.rl_history_of_present_illness);
		rlPastMedicalhistory = (RelativeLayout) findViewById(R.id.rl_past_history);
		rlFamilyHistory = (RelativeLayout) findViewById(R.id.rl_family_history);
		rlPersonalHistory = (RelativeLayout) findViewById(R.id.rl_personal_history);
		rlDrugHistory = (RelativeLayout) findViewById(R.id.rl_drug_histoy);
		rlNote = (RelativeLayout) findViewById(R.id.rl_note_subjective);
		cbSmoke = (CheckBox) findViewById(R.id.cb_smoke);
		cbAlcohol = (CheckBox) findViewById(R.id.cb_alcohol);
		rlAlcohol = (RelativeLayout) findViewById(R.id.rl_alcohol);
		rlSmoke = (RelativeLayout) findViewById(R.id.rl_smoke);

		// Examination
		txtExamination = (TextView) findViewById(R.id.txt_objective);

		// Vitals
		txtVitals = (TextView) findViewById(R.id.txt_vitals);
		rlBP = (RelativeLayout) findViewById(R.id.rl_bp);
		rlPulse = (RelativeLayout) findViewById(R.id.rl_pulse);
		rlRespiratoryRate = (RelativeLayout) findViewById(R.id.rl_respiratory_rate);
		rlTemperature = (RelativeLayout) findViewById(R.id.rl_temperature);
		rlWeight = (RelativeLayout) findViewById(R.id.rl_weight);
		rlHeight = (RelativeLayout) findViewById(R.id.rl_height);
		rlWaist = (RelativeLayout) findViewById(R.id.rl_waist);
		rlNoteVital = (RelativeLayout) findViewById(R.id.ll_note_vital);
		rlBMI = (RelativeLayout) findViewById(R.id.rl_bmi);
		rlSymptoms = (RelativeLayout) findViewById(R.id.rl_symptoms);

		// General Examination
		rlGeneralExamination = (RelativeLayout) findViewById(R.id.rl_general_examination);
		txtGeneralExamination = (TextView) findViewById(R.id.edit_general_exam);
		rlSystemicExamination = (RelativeLayout) findViewById(R.id.rl_systemic_examination);
		rlNoteGE = (RelativeLayout) findViewById(R.id.rl_note_ge);

		// Diagnosis
		txtDiagnosis = (TextView) findViewById(R.id.txt_diagnosis);
		txtProvisonalDiagnosis = (TextView) findViewById(R.id.txt_provisonal_diagnosis);
		txtProvisonalDiagnosisDisplay = (TextView) findViewById(R.id.actv_provisonal_diagnosis);
		txtFinalDiagnosis = (TextView) findViewById(R.id.txt_final_diagnosis);
		txtFinalDiagnosisDisplay = (TextView) findViewById(R.id.actv_final_diagnosis);

		// Plan
		txtPlan = (TextView) findViewById(R.id.txt_plan);
		txtRadiology = (TextView) findViewById(R.id.txt_radiology);
		txtLabOrder = (TextView) findViewById(R.id.txt_lab);
		txtEKG = (TextView) findViewById(R.id.txt_ekg);
		txtPrescription = (TextView) findViewById(R.id.txt_prescription);
		txtSpecialInstruction = (TextView) findViewById(R.id.txt_special_instructions);
		txtAllergy = (TextView) findViewById(R.id.txt_allergy);
		txtTreatmentDone = (TextView) findViewById(R.id.txt_treatment_done);
		rlTreatmentPlan = (RelativeLayout) findViewById(R.id.rl_treatment_plan);
		llPrivateNotes = (LinearLayout) findViewById(R.id.ll_private_notes);

		visitNotes = new VisitNotesModel();
		if (getIntent().hasExtra("type")
				&& getIntent().getStringExtra("type").equals("past")) {
			getPastData();
		} else if (!Util.isEmptyString(Appointment.getEpid())) {
			Log.i("", "get data 1");
			// getData();
			loadData();
		}

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
		radiologyTable(PhysicianSoapMiniActivity.this);
		labTable(PhysicianSoapMiniActivity.this);
		ekgTable(PhysicianSoapMiniActivity.this);
		prescriptionTable(PhysicianSoapMiniActivity.this);
	}

	private void getPastData() {
		final String url = APIs.PAST_VISIT_VIEW();
		final Dialog loaddialog = Util
				.showLoadDialog(PhysicianSoapMiniActivity.this);
		final StringRequest patientListRequest = new StringRequest(
				Request.Method.POST, url, new Response.Listener<String>() {
					@Override
					public void onResponse(final String response) {
						Log.i("", response);
						try {
							final JSONObject jObj = new JSONObject(response);
							if (jObj.getString("s").equals("200")) {
								arrallergies.clear();
								if (jObj.has("allergies")) {
									JSONArray arr = jObj
											.getJSONArray("allergies");
									for (int i = 0; i < arr.length(); i++) {
										AllergiesModel allergy = new Gson()
												.fromJson(
														arr.get(i).toString(),
														AllergiesModel.class);
										arrallergies.add(allergy);
									}
								}
								final JSONObject followUpList = jObj
										.getJSONObject("followupList");
								arrDocuments.clear();
								if (jObj.has("documents")) {
									JSONArray arr = jObj
											.getJSONArray("documents");
									for (int i = 0; i < arr.length(); i++) {
										Document doc = new Gson().fromJson(arr
												.get(i).toString(),
												Document.class);
										arrDocuments.add(doc);
									}
									radiologyTable(PhysicianSoapMiniActivity.this);
									labTable(PhysicianSoapMiniActivity.this);
									ekgTable(PhysicianSoapMiniActivity.this);
									prescriptionTable(PhysicianSoapMiniActivity.this);
								}
								final JSONObject soap = jObj.getJSONObject(
										"tmpl-inst").getJSONObject("Soap");
								visitNotes.jsonData = jObj
										.getJSONObject("tmpl-inst");
								visitNotes.subjectiveModel.JsonParse(soap
										.getJSONObject("subj"));
								if (soap.has("private-note"))
									visitNotes.privateNote.jsonParse(soap
											.getJSONObject("private-note"));
								if (soap.has("asse"))
									visitNotes.diagnosisModel.JsonParse(soap
											.getJSONObject("asse"));
								if (soap.has("obje"))
									visitNotes.examinationModel.JsonParse(soap
											.getJSONObject("obje"));
								if (soap.has("plan"))
									visitNotes.physicianPlanModel
											.JsonParse(soap
													.getJSONObject("plan"));
								AutoSave();
							} else {
								Toast.makeText(
										PhysicianSoapMiniActivity.this,
										"There is some error while fetching data please try again.",
										Toast.LENGTH_SHORT).show();
							}
						} catch (final JSONException e) {
							Toast.makeText(
									PhysicianSoapMiniActivity.this,
									"There is some error while fetching data please try again.",
									Toast.LENGTH_SHORT).show();
							Log.e("", e);
						}
						loaddialog.dismiss();
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(final VolleyError error) {
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
				loginParams.put("cli", "api");
				loginParams.put("siId", Appointment.getSiid());
				loginParams.put("userId",
						sharedPref.getString(Constants.USER_ID, ""));
				return loginParams;
			}
		};
		patientListRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 5,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		EzApp.mVolleyQueue.add(patientListRequest);
	}

	public void onAddPhoto(View view) {
		mGalleryView.onAddPhoto(this);
	}

	public void onShowGallery(View view) {
		mGalleryView.onShowGallery(this);
	}

	private void updateUI() {
		mGalleryView.showSOAPGallery(SoapNotesController.mSOAPGallery);
	}

	private void loadData() {
		final Dialog mLoadingDialog = Util.showLoadDialog(this);
		mController.getData(new SoapNotesResponseHandler() {

			@Override
			public void onGetDataFinished(boolean dataLoaded) {
				mLoadingDialog.dismiss();

				if (dataLoaded == false) {
					// data could not be loaded
					EzUtils.showLong("Data could not be loaded. Please retry..");
					finish();
				} else {
					// data loaded
					PhysicianSoapMiniActivity.this.updateUI();
				}
			}
		});
	}

	@Override
	public void onStart() {
		super.onStart();
		this.loadData();
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {

		case android.R.id.home:
			finish();
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	// Prescription Table
	public static void prescriptionTable(Activity context) {
		final TableLayout tl = (TableLayout) context
				.findViewById(R.id.table_prescription);
		tl.removeAllViews();

		if (visitNotes.physicianPlanModel.prescription.arrMedicine.size() < 1) {
			tl.setVisibility(View.GONE);
		} else {
			tl.setVisibility(View.VISIBLE);
		}
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

	// Radiology Table
	public static void radiologyTable(Activity context) {
		final TableLayout tl = (TableLayout) context.findViewById(R.id.table);
		tl.removeAllViews();
		ArrayList<Document> arrRadiDocuments = new ArrayList<Document>();
		for (final Document doc : arrDocuments) {
			if (doc.getSection().equalsIgnoreCase("radiology"))
				arrRadiDocuments.add(doc);
		}
		if (arrRadiDocuments.size() > 0) {
			final TableRow row = (TableRow) context.getLayoutInflater()
					.inflate(R.layout.row_document_past_mini, null, false);
			tl.addView(row);
			for (final Document doc : arrRadiDocuments) {
				final TableRow row1 = (TableRow) context.getLayoutInflater()
						.inflate(R.layout.row_document_past_mini, null, false);

				TextView txtDocumentName = (TextView) row1
						.findViewById(R.id.txt_name);
				TextView txtDocumentType = (TextView) row1
						.findViewById(R.id.txt_type);
				TextView txtUploadDate = (TextView) row1
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

	// Lab Table
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
			final TableRow row = (TableRow) context.getLayoutInflater()
					.inflate(R.layout.row_document_past_mini, null, false);
			tl.addView(row);
			for (final Document doc : arrRadiDocuments) {
				final TableRow row1 = (TableRow) context.getLayoutInflater()
						.inflate(R.layout.row_document_past_mini, null, false);

				TextView txtDocumentName = (TextView) row1
						.findViewById(R.id.txt_name);
				TextView txtDocumentType = (TextView) row1
						.findViewById(R.id.txt_type);
				TextView txtUploadDate = (TextView) row1
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

	// EKG Table
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
			final TableRow row = (TableRow) context.getLayoutInflater()
					.inflate(R.layout.row_document_past_mini, null, false);
			tl.addView(row);
			for (final Document doc : arrRadiDocuments) {
				final TableRow row1 = (TableRow) context.getLayoutInflater()
						.inflate(R.layout.row_document_past_mini, null, false);

				TextView txtDocumentName = (TextView) row1
						.findViewById(R.id.txt_name);
				TextView txtDocumentType = (TextView) row1
						.findViewById(R.id.txt_type);
				TextView txtUploadDate = (TextView) row1
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

}
