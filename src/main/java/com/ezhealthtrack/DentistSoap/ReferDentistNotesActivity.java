package com.ezhealthtrack.DentistSoap;

import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
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
import com.ezhealthtrack.DentistSoap.Model.PastVisitModel;
import com.ezhealthtrack.DentistSoap.Model.TeethModel;
import com.ezhealthtrack.DentistSoap.Model.VisitNotesModel;
import com.ezhealthtrack.activity.AddPrescriptionActivity;
import com.ezhealthtrack.greendao.Appointment;
import com.ezhealthtrack.model.AllergiesModel;
import com.ezhealthtrack.model.Document;
import com.ezhealthtrack.model.MedicineModel;
import com.ezhealthtrack.util.BaseActivity;
import com.ezhealthtrack.util.Constants;
import com.ezhealthtrack.util.Log;
import com.ezhealthtrack.util.Util;
import com.ezhealthtrack.views.RadioGroupUtils;
import com.ezhealthtrack.views.RadioGroupWithText;
import com.ezhealthtrack.views.TextUtils;
import com.google.gson.Gson;

public class ReferDentistNotesActivity extends BaseActivity implements
		OnClickListener {
	private SharedPreferences sharedPref;
	public static Appointment Appointment;
	private VisitNotesModel visitModel;
	private TextView txtVitals;
	private TextView txtSubjective;
	private TextView txtObjective;
	private TextView txtGeneralExam;
	private TextView txtHP;
	private TextView txtDocumentName;
	private TextView txtDocumentType;
	private TextView txtUploadDate;
	private TextView editCheifComplaint;
	private TextView txtOralExam;
	private TextView txtSoftTissueExam;
	private TextView txtHardTissueExam;
	private TextView txtSno;
	private TextView txtFormulation;
	private TextView txtDrugName;
	private TextView txtStrength;
	private TextView txtUnitType;
	private TextView txtRoute;
	private TextView txtFrequency;
	private TextView txtDays;
	private TextView txtQuantity;
	private TextView txtRefills;
	private TextView txtRefillTime;
	private TextView txtNotes;
	private TextView txtFahrenheit;
	private View llSubjective;
	private ArrayList<TeethModel> arrTeeth = new ArrayList<TeethModel>();
	private MultiAutoCompleteTextView actvProv;
	private TextView txtRadiology;
	private MultiAutoCompleteTextView actvFinal;
	public static final ArrayList<PastVisitModel> arrPastVisit = new ArrayList<PastVisitModel>();
	private String prevId;
	private String nextId;
	private CheckBox cbSmoke;
	private CheckBox cbAlcohol;
	private RelativeLayout rlSmoke;
	private RelativeLayout rlAlcohol;
	public static ArrayList<Document> arrDocuments = new ArrayList<Document>();
	private ArrayList<AllergiesModel> arrallergies = new ArrayList<AllergiesModel>();

	private void AutoSave() {
		TextUtils.autoSaveTextViewLayout(
				(LinearLayout) findViewById(R.id.ll_hp),
				visitModel.dentistSubjectiveModel.hashHP);
		TextUtils.autoSaveTextView(editCheifComplaint,
				visitModel.dentistSubjectiveModel.hashCC);
		if (Util.isEmptyString(((TextView) findViewById(R.id.edit_cheif_complaint))
				.getText().toString()))
			((TextView) findViewById(R.id.edit_cheif_complaint))
					.setText(Appointment.getReason());
		TextUtils.autoSaveTextViewLayout(
				(LinearLayout) findViewById(R.id.ll_vitals),
				visitModel.dentistExaminationModel.hashVitals);
		TextUtils
				.autoSaveTextViewLayout(
						(LinearLayout) findViewById(R.id.ll_general_exam),
						visitModel.dentistExaminationModel.generalExamination.hashState);
		TextUtils
				.autoSaveTextViewLayout(
						(LinearLayout) findViewById(R.id.ll_soft_tissue),
						visitModel.dentistExaminationModel.oralExamination.softTissue.hashState);
		if (!Util.isEmptyString(getIntent().getStringExtra("type"))
				|| (visitModel.privateNote.hashNote.containsKey("status") && !visitModel.privateNote.hashNote
						.get("status").equals("Pr"))) {
			Log.i("", visitModel.privateNote.hashNote.get("status"));
			if (visitModel.privateNote.hashNote.containsKey("private-note")) {
				((TextView) findViewById(R.id.txt_private_notes))
						.setText(visitModel.privateNote.hashNote
								.get("private-note"));
			}
		} else {
			((TextView) findViewById(R.id.txt_private_notes))
					.setText("Not allowed to view private notes. ");
		}
		if (Util.isEmptyString(visitModel.dentistExaminationModel.generalExamination.hashCb
				.get("swd"))) {
			visitModel.dentistExaminationModel.generalExamination.hashCb.put(
					"swd", "");
		}
		RadioGroupUtils.autoSaveRg(
				(RadioGroup) findViewById(R.id.rg_satisfaction),
				visitModel.dentistExaminationModel.generalExamination.hashCb,
				"swd");
		if (!Util
				.isEmptyString(visitModel.dentistExaminationModel.oralExamination.hardTissue.hashNote
						.get("note")))
			((TextView) findViewById(R.id.txt_hte_note))
					.setText("Note : "
							+ visitModel.dentistExaminationModel.oralExamination.hardTissue.hashNote
									.get("note"));
		else {
			((TextView) findViewById(R.id.txt_hte_note)).setText("");
		}
		generalExamination();
		((TextView) findViewById(R.id.txt_treatment_done))
				.setText(visitModel.dentistPlanModel.treatmentDone
						.getTreatmentDone());
		txtRadiology.setText(Html.fromHtml("<b>Radiology :</b> "
				+ visitModel.dentistPlanModel.radiology.getLabString()));
		((TextView) findViewById(R.id.txt_lab)).setText(Html
				.fromHtml("<b>Lab order :</b> "
						+ visitModel.dentistPlanModel.lab.getLabString()));
		((TextView) findViewById(R.id.txt_treatment_plan))
				.setText(visitModel.dentistPlanModel.treatmentPlan
						.getTpString(arrTeeth));
		((TextView) findViewById(R.id.txt_allergy)).setText(Html
				.fromHtml("<b>Allergy :</b> "));
		for (final AllergiesModel allergy : arrallergies) {
			((TextView) findViewById(R.id.txt_allergy)).append("\n "
					+ allergy.getMCatName() + " -->" + allergy.getSCatName()
					+ "\n " + allergy.getAddiInfo());
		}

		((TextView) findViewById(R.id.txt_prescription)).setText(Html
				.fromHtml("<b>Prescription :</b> "));
		prescriptionTable();
		if (!Util.isEmptyString(visitModel.dentistPlanModel.prescription.si
				.get("si"))
				&& visitModel.dentistPlanModel.prescription.arrMedicine.size() > 0)
			((TextView) findViewById(R.id.txt_prescription))
					.append("\n Special Instruction : "
							+ visitModel.dentistPlanModel.prescription.si
									.get("si"));
	}

	private void generalExamination() {
		final ArrayList<String> colors = new ArrayList<String>();
		colors.add("#ff0000");
		colors.add("#00000000");
		new RadioGroupWithText(this, findViewById(R.id.txt_pallor),
				findViewById(R.id.rg_pallor), 2, colors,
				visitModel.dentistExaminationModel.generalExamination.hashState);
		new RadioGroupWithText(this, findViewById(R.id.txt_icterus),
				findViewById(R.id.rg_icterus), 2, colors,
				visitModel.dentistExaminationModel.generalExamination.hashState);
		new RadioGroupWithText(this, findViewById(R.id.txt_clubbing),
				findViewById(R.id.rg_clubbing), 2, colors,
				visitModel.dentistExaminationModel.generalExamination.hashState);
		new RadioGroupWithText(this, findViewById(R.id.txt_cyanosis),
				findViewById(R.id.rg_cyanosis), 2, colors,
				visitModel.dentistExaminationModel.generalExamination.hashState);
		new RadioGroupWithText(this, findViewById(R.id.txt_edema),
				findViewById(R.id.rg_edema), 2, colors,
				visitModel.dentistExaminationModel.generalExamination.hashState);
		new RadioGroupWithText(this, findViewById(R.id.txt_lymphadenopathy),
				findViewById(R.id.rg_lymphadenopathy), 2, colors,
				visitModel.dentistExaminationModel.generalExamination.hashState);

	}

	private void getData() {
		try {
			InputStream fis;
			fis = getAssets().open("dentistTemplateJson.txt");
			final StringBuffer fileContent = new StringBuffer("");
			final byte[] buffer = new byte[1024];
			while (fis.read(buffer) != -1) {
				fileContent.append(new String(buffer));
			}
			final String s = String.valueOf(fileContent);
			final JSONObject jObj = new JSONObject(s);
			jObj.put("template_id", "soap_dentist_v1");
			jObj.put("user_id", sharedPref.getString(Constants.USER_ID, ""));
			// Log.i("", Appointment.epId);
			visitModel.jsonData = jObj;
			final JSONObject soap = visitModel.jsonData.getJSONObject("Soap");
			visitModel.dentistSubjectiveModel.JsonParse(soap
					.getJSONObject("subj"));
			visitModel.dentistExaminationModel.JsonParse(soap
					.getJSONObject("obje"));
			visitModel.dentistDiagnosisModel.JsonParse(
					soap.getJSONObject("asse"), actvFinal, actvProv);
			visitModel.dentistPlanModel.JsonParse(soap.getJSONObject("plan"),
					arrTeeth);
		} catch (Exception e) {
			e.printStackTrace();
		}
		final String url = APIs.REFER_VIEW() + Appointment.getEpid()
				+ "/cli/api";
		final Dialog loaddialog = Util
				.showLoadDialog(ReferDentistNotesActivity.this);
		Log.i("Url", url);
		final StringRequest patientListRequest = new StringRequest(
				Request.Method.POST, url, new Response.Listener<String>() {
					@Override
					public void onResponse(final String response) {
						try {
							final JSONObject jObj = new JSONObject(response);
							if (jObj.getString("s").equals("200")) {
								if (Util.isEmptyString(jObj
										.getString("tmpl-inst"))) {
									Util.AlertdialogWithFinish(
											ReferDentistNotesActivity.this,
											"Referred Appointment not started yet");
									loaddialog.dismiss();
								} else {
									final JSONObject followUpList = jObj
											.getJSONObject("followupList");
									Log.i("", followUpList.toString());
									prevId = followUpList.getString("prev");
									nextId = followUpList.getString("next");
									if (prevId.equals("0")) {
										findViewById(R.id.btn_prev)
												.setVisibility(View.GONE);
									} else {
										findViewById(R.id.btn_prev)
												.setVisibility(View.VISIBLE);
									}
									if (nextId.equals("0")) {
										findViewById(R.id.btn_next)
												.setVisibility(View.GONE);
									} else {
										findViewById(R.id.btn_next)
												.setVisibility(View.VISIBLE);
									}
									arrallergies.clear();
									if (jObj.has("allergies")) {
										JSONArray arr = jObj
												.getJSONArray("allergies");
										for (int i = 0; i < arr.length(); i++) {
											AllergiesModel allergy = new Gson()
													.fromJson(
															arr.get(i)
																	.toString(),
															AllergiesModel.class);
											arrallergies.add(allergy);
										}
									}
									arrDocuments.clear();
									if (jObj.has("documents")) {
										JSONArray arr = jObj
												.getJSONArray("documents");
										for (int i = 0; i < arr.length(); i++) {
											Document doc = new Gson().fromJson(
													arr.get(i).toString(),
													Document.class);
											arrDocuments.add(doc);
										}
										radiologyTable();
										labTable();

									}
									final JSONObject soap = jObj.getJSONObject(
											"tmpl-inst").getJSONObject("Soap");
									visitModel.jsonData = jObj
											.getJSONObject("tmpl-inst");
									visitModel.dentistSubjectiveModel
											.JsonParse(soap
													.getJSONObject("subj"));
									visitModel.dentistExaminationModel
											.JsonParse(soap
													.getJSONObject("obje"));
									visitModel.dentistDiagnosisModel.JsonParse(
											soap.getJSONObject("asse"),
											actvFinal, actvProv);
									visitModel.dentistPlanModel.JsonParse(
											soap.getJSONObject("plan"),
											arrTeeth);
									if (soap.has("private-note"))
										visitModel.privateNote.jsonParse(soap
												.getJSONObject("private-note"));
									for (final Map.Entry<String, String> entry : visitModel.dentistSubjectiveModel.hashHP
											.entrySet()) {
										if (!Util.isEmptyString(entry
												.getValue())) {
											findViewById(R.id.img_hp_tick)
													.setVisibility(View.VISIBLE);
										}
									}
									AutoSave();
									((TextView) findViewById(R.id.txt_hard_tissue_examination))
											.setText("Hard Tissue Examination");

									for (final TeethModel teeth : arrTeeth) {
										if ((teeth.arrTeethState.size() > 0)
												|| (teeth.arrTeethTreatmentPlan
														.size() > 0)) {
											((TextView) findViewById(R.id.txt_hard_tissue_examination))
													.append("\n       "
															+ teeth.name
															+ " "
															+ teeth.arrTeethState
																	.toString());
										}
									}
								}

							} else {
								Util.AlertdialogWithFinish(
										ReferDentistNotesActivity.this,
										"Referred Appointment not started yet");
							}
						} catch (final JSONException e) {
							Util.AlertdialogWithFinish(
									ReferDentistNotesActivity.this,
									"Referred Appointment not started yet");
							Log.e("", e);
						}
						loaddialog.dismiss();

					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(final VolleyError error) {
						Util.AlertdialogWithFinish(
								ReferDentistNotesActivity.this,
								"Referred Appointment not started yet");

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
				return loginParams;
			}

		};
		patientListRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 5,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		EzApp.mVolleyQueue.add(patientListRequest);

	}

	@Override
	public void onClick(final View v) {
		Intent intent;
		switch (v.getId()) {

		case R.id.btn_prev:
			try {
				if (!prevId.equals("0")) {
					if (getIntent().getExtras().containsKey("type")
							&& getIntent().getExtras().getString("type")
									.equals("past")) {
						Appointment.setSiid(prevId);
						getPastData();
					} else {
						Appointment.setEpid(prevId);
						getData();
					}
				}
			} catch (Exception e) {
				Appointment.setEpid(prevId);
				getData();
			}
			break;

		case R.id.btn_next:
			try {
				if (!nextId.equals("0")) {
					if (getIntent().getExtras().containsKey("type")
							&& getIntent().getExtras().getString("type")
									.equals("past")) {
						Appointment.setSiid(nextId);
						getPastData();
					} else {
						Appointment.setEpid(nextId);
						getData();
					}
				}

			} catch (Exception e) {
				Appointment.setEpid(nextId);
				getData();
			}
			break;

		case R.id.txt_subjective:
			Util.showHideView(llSubjective,
					(ImageView) findViewById(R.id.img_subjective));
			break;

		case R.id.txt_objective:
			Util.showHideView(findViewById(R.id.ll_objective),
					(ImageView) findViewById(R.id.img_objective));
			break;

		case R.id.txt_hp:
			Util.showHideView(findViewById(R.id.ll_hp),
					(ImageView) findViewById(R.id.img_hp));
			break;

		case R.id.txt_vitals:
			Util.showHideView(findViewById(R.id.ll_vitals),
					(ImageView) findViewById(R.id.img_vitals));
			break;

		case R.id.txt_denture_question:
			Util.showHideView(findViewById(R.id.ll_denture_questions),
					(ImageView) findViewById(R.id.img_denture_questions));
			break;

		case R.id.txt_general_exam:
			Util.showHideView(findViewById(R.id.ll_general_exam),
					(ImageView) findViewById(R.id.img_general_exam));
			break;

		case R.id.txt_oral_exam:
			Util.showHideView(findViewById(R.id.ll_oral_exam),
					(ImageView) findViewById(R.id.img_oral_exam));
			break;

		case R.id.txt_soft_tissue_examination:
			Util.showHideView(findViewById(R.id.ll_soft_tissue),
					(ImageView) findViewById(R.id.img_soft_tissue_examination));
			break;

		case R.id.txt_diagnosis:
			Util.showHideView(findViewById(R.id.ll_diagnosis),
					(ImageView) findViewById(R.id.img_diagnosis));
			break;

		case R.id.txt_plan:
			Util.showHideView(findViewById(R.id.ll_plan),
					(ImageView) findViewById(R.id.img_plan));
			break;

		case R.id.btn_xray:
			try {
				intent = new Intent(this, DentistRadiologyActivity.class);
				intent.putExtra("position",
						getIntent().getIntExtra("position", 0));
				startActivityForResult(intent, 1);

			} catch (Exception e) {

			}
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

		default:
			break;
		}

	}

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_refer_dentist_notes);
		arrDocuments.clear();
		sharedPref = getApplicationContext().getSharedPreferences(
				Constants.EZ_SHARED_PREF, Context.MODE_PRIVATE);
		Appointment = new Appointment();
		Appointment.setEpid(getIntent().getStringExtra("epid"));
		Appointment.setBkid(getIntent().getStringExtra("bkid"));
		Appointment.setSiid(getIntent().getStringExtra("siid"));
		visitModel = new VisitNotesModel();
		arrTeeth = visitModel.dentistExaminationModel.oralExamination.hardTissue.arrTeeth;
		txtVitals = (TextView) findViewById(R.id.txt_vitals);
		txtVitals.setOnClickListener(this);
		txtSubjective = (TextView) findViewById(R.id.txt_subjective);
		txtSubjective.setOnClickListener(this);
		txtObjective = (TextView) findViewById(R.id.txt_objective);
		txtObjective.setOnClickListener(this);
		txtGeneralExam = (TextView) findViewById(R.id.txt_general_exam);
		txtGeneralExam.setOnClickListener(this);
		txtHP = (TextView) findViewById(R.id.txt_hp);
		txtHP.setOnClickListener(this);
		findViewById(R.id.txt_diagnosis).setOnClickListener(this);
		findViewById(R.id.txt_plan).setOnClickListener(this);
		findViewById(R.id.txt_denture_question).setOnClickListener(this);
		editCheifComplaint = (TextView) findViewById(R.id.edit_cheif_complaint);
		editCheifComplaint.setText(Appointment.getReason());
		txtOralExam = (TextView) findViewById(R.id.txt_oral_exam);
		txtOralExam.setOnClickListener(this);
		txtSoftTissueExam = (TextView) findViewById(R.id.txt_soft_tissue_examination);
		txtSoftTissueExam.setOnClickListener(this);
		txtHardTissueExam = (TextView) findViewById(R.id.txt_hard_tissue_examination);
		txtHardTissueExam.setOnClickListener(this);
		llSubjective = findViewById(R.id.ll_subjective_1);
		txtRadiology = (TextView) findViewById(R.id.txt_radiology);
		actvProv = (MultiAutoCompleteTextView) findViewById(R.id.actv_provisonal_diagnosis);
		actvProv.setText(visitModel.dentistDiagnosisModel.pd);
		actvProv.setActivated(false);
		actvFinal = (MultiAutoCompleteTextView) findViewById(R.id.actv_final_diagnosis);
		actvFinal.setText(visitModel.dentistDiagnosisModel.fd);
		actvFinal.setActivated(false);
		cbSmoke = (CheckBox) findViewById(R.id.cb_smoke);
		cbAlcohol = (CheckBox) findViewById(R.id.cb_alcohol);
		rlAlcohol = (RelativeLayout) findViewById(R.id.rl_alcohol);
		rlSmoke = (RelativeLayout) findViewById(R.id.rl_smoke);
		txtFahrenheit = (TextView) findViewById(R.id.txt_Fahrenheit);
		txtFahrenheit.setText(Html.fromHtml("&#176;F"));

		if (arrTeeth.size() == 0) {
			for (int i = 1; i < 5; i++) {
				for (int j = 1; j < 9; j++) {
					final TeethModel teeth = new TeethModel();
					teeth.name = "tooth" + i + "" + j;
					arrTeeth.add(teeth);
				}
			}
			for (int i = 5; i < 9; i++) {
				for (int j = 1; j < 6; j++) {
					final TeethModel teeth = new TeethModel();
					teeth.name = "tooth" + i + "" + j;
					arrTeeth.add(teeth);
				}
			}
		}
		if (getIntent().hasExtra("type")
				&& getIntent().getStringExtra("type").equals("past")) {
			// getActionBar().setTitle("Past Episode");
			getPastData();
		} else if (!Util.isEmptyString(Appointment.getEpid())) {
			Log.i("", "get data 1");
			getData();
		} else {
			try {
				InputStream fis;
				fis = getAssets().open("dentistTemplateJson.txt");
				final StringBuffer fileContent = new StringBuffer("");
				final byte[] buffer = new byte[1024];
				while (fis.read(buffer) != -1) {
					fileContent.append(new String(buffer));
				}
				final String s = String.valueOf(fileContent);
				final JSONObject jObj = new JSONObject(s);
				jObj.put("template_id", "soap_dentist_v1");
				jObj.put("user_id", sharedPref.getString(Constants.USER_ID, ""));
				jObj.put("encounter_id", Appointment.getBkid());

				jObj.put("episode_id", Appointment.getEpid());
				// Log.i("", Appointment.epId);
				visitModel.jsonData = jObj;
			} catch (final Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			AutoSave();
		}

		// findViewById(R.id.full_layout).setOnTouchListener(
		// new OnSwipeTouchListener(ReferDentistNotesActivity.this) {
		// @Override
		// public void onSwipeBottom() {
		// // Toast.makeText(ReferDentistNotesActivity.this,
		// // "bottom", Toast.LENGTH_SHORT).show();
		// }
		//
		// @Override
		// public void onSwipeLeft() {
		// if (!nextId.equals("0")) {
		// if (getIntent().getExtras().getString("type")
		// .equals("past")) {
		// Appointment.setSiid(nextId);
		// getPastData();
		// } else {
		// Appointment.setEpid(nextId);
		// getData();
		// }
		// }
		// }
		//
		// @Override
		// public void onSwipeRight() {
		// if (!prevId.equals("0")) {
		// if (getIntent().getExtras().getString("type")
		// .equals("past")) {
		// Appointment.setSiid(prevId);
		// getPastData();
		// } else {
		// Appointment.setEpid(prevId);
		// getData();
		// }
		// }
		// }
		//
		// @Override
		// public void onSwipeTop() {
		// }
		//
		// @Override
		// public boolean onTouch(final View v, final MotionEvent event) {
		// return gestureDetector.onTouchEvent(event);
		// }
		// });
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
		findViewById(R.id.btn_prev).setOnClickListener(this);
		findViewById(R.id.btn_next).setOnClickListener(this);
		radiologyTable();
		labTable();
		prescriptionTable();
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

	@Override
	protected void onResume() {
		try {
			txtRadiology.setText("Radiology : "
					+ visitModel.dentistPlanModel.radiology.getLabString());
			((TextView) findViewById(R.id.txt_lab)).setText("Lab order :"
					+ visitModel.dentistPlanModel.lab.getLabString());
			if (arrTeeth != null)
				((TextView) findViewById(R.id.txt_treatment_plan))
						.setText(visitModel.dentistPlanModel.treatmentPlan
								.getTpString(arrTeeth));
			((TextView) findViewById(R.id.txt_allergy)).setText(Html
					.fromHtml("<b>Allergy :</b> "));
			for (final AllergiesModel allergy : arrallergies) {
				((TextView) findViewById(R.id.txt_allergy))
						.append("\n " + allergy.getMCatName() + " -->"
								+ allergy.getSCatName() + "\n "
								+ allergy.getAddiInfo());
			}

			((TextView) findViewById(R.id.txt_prescription))
					.setText("Prescription :");
			prescriptionTable();
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.onResume();
	}

	private void radiologyTable() {
		final TableLayout tl = (TableLayout) findViewById(R.id.table);
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
			final TableRow row = (TableRow) getLayoutInflater().inflate(
					R.layout.row_document_past, null, false);
			row.setLayoutParams(params);
			txtDocumentName = (TextView) row.findViewById(R.id.txt_name);
			txtDocumentType = (TextView) row.findViewById(R.id.txt_type);
			txtUploadDate = (TextView) row.findViewById(R.id.txt_upload_date);
			txtDocumentName.setText(Html.fromHtml("<b>Document Name</b>"));
			txtDocumentType.setText(Html.fromHtml("<b>Document Type</b>"));
			txtUploadDate.setText(Html.fromHtml("<b>Upload Date</b>"));
			tl.addView(row);
			for (final Document doc : arrRadiDocuments) {
				final LayoutParams params1 = new LayoutParams(
						android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
						android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
				final TableRow row1 = (TableRow) getLayoutInflater().inflate(
						R.layout.row_document_past, null, false);
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

	private void labTable() {
		final TableLayout tl = (TableLayout) findViewById(R.id.table_lab);
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
			final TableRow row = (TableRow) getLayoutInflater().inflate(
					R.layout.row_document_past, null, false);
			row.setLayoutParams(params);
			txtDocumentName = (TextView) row.findViewById(R.id.txt_name);
			txtDocumentType = (TextView) row.findViewById(R.id.txt_type);
			txtUploadDate = (TextView) row.findViewById(R.id.txt_upload_date);
			txtDocumentName.setText(Html.fromHtml("<b>Document Name</b>"));
			txtDocumentType.setText(Html.fromHtml("<b>Document Type</b>"));
			txtUploadDate.setText(Html.fromHtml("<b>Upload Date</b>"));
			tl.addView(row);
			for (final Document doc : arrRadiDocuments) {
				final LayoutParams params1 = new LayoutParams(
						android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
						android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
				final TableRow row1 = (TableRow) getLayoutInflater().inflate(
						R.layout.row_document_past, null, false);
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

	private void getPastData() {
		try {
			InputStream fis;
			fis = getAssets().open("dentistTemplateJson.txt");
			final StringBuffer fileContent = new StringBuffer("");
			final byte[] buffer = new byte[1024];
			while (fis.read(buffer) != -1) {
				fileContent.append(new String(buffer));
			}
			final String s = String.valueOf(fileContent);
			final JSONObject jObj = new JSONObject(s);
			jObj.put("template_id", "soap_dentist_v1");
			jObj.put("user_id", sharedPref.getString(Constants.USER_ID, ""));
			// Log.i("", Appointment.epId);
			visitModel.jsonData = jObj;
			final JSONObject soap = visitModel.jsonData.getJSONObject("Soap");
			visitModel.dentistSubjectiveModel.JsonParse(soap
					.getJSONObject("subj"));
			visitModel.dentistExaminationModel.JsonParse(soap
					.getJSONObject("obje"));
			visitModel.dentistDiagnosisModel.JsonParse(
					soap.getJSONObject("asse"), actvFinal, actvProv);
			visitModel.dentistPlanModel.JsonParse(soap.getJSONObject("plan"),
					arrTeeth);
		} catch (Exception e) {
			e.printStackTrace();
		}
		final String url = APIs.PAST_VISIT_VIEW();
		final Dialog loaddialog = Util
				.showLoadDialog(ReferDentistNotesActivity.this);
		final StringRequest patientListRequest = new StringRequest(
				Request.Method.POST, url, new Response.Listener<String>() {
					@Override
					public void onResponse(final String response) {
						try {
							final JSONObject jObj = new JSONObject(response);

							if (jObj.getString("s").equals("200")) {
								Log.i("", response);
								final JSONObject followUpList = jObj
										.getJSONObject("followupList");
								prevId = followUpList.getString("prev");
								nextId = followUpList.getString("next");
								if (prevId.equals("0")) {
									findViewById(R.id.btn_prev).setVisibility(
											View.GONE);
								} else {
									findViewById(R.id.btn_prev).setVisibility(
											View.VISIBLE);
								}
								if (nextId.equals("0")) {
									findViewById(R.id.btn_next).setVisibility(
											View.GONE);
								} else {
									findViewById(R.id.btn_next).setVisibility(
											View.VISIBLE);
								}
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
									radiologyTable();
									labTable();
									prescriptionTable();

								}
								final JSONObject soap = jObj.getJSONObject(
										"tmpl-inst").getJSONObject("Soap");
								visitModel.jsonData = jObj
										.getJSONObject("tmpl-inst");
								if (soap.has("subj"))
									visitModel.dentistSubjectiveModel
											.JsonParse(soap
													.getJSONObject("subj"));
								if (soap.has("obje"))
									visitModel.dentistExaminationModel
											.JsonParse(soap
													.getJSONObject("obje"));
								if (soap.has("asse"))
									visitModel.dentistDiagnosisModel.JsonParse(
											soap.getJSONObject("asse"),
											actvFinal, actvProv);
								if (soap.has("plan"))
									visitModel.dentistPlanModel.JsonParse(
											soap.getJSONObject("plan"),
											arrTeeth);
								if (soap.has("private-note"))
									visitModel.privateNote.jsonParse(soap
											.getJSONObject("private-note"));

								for (final Map.Entry<String, String> entry : visitModel.dentistSubjectiveModel.hashHP
										.entrySet()) {
									if (!Util.isEmptyString(entry.getValue())) {
										findViewById(R.id.img_hp_tick)
												.setVisibility(View.VISIBLE);
									}
								}
								AutoSave();
								((TextView) findViewById(R.id.txt_hard_tissue_examination))
										.setText("Hard Tissue Examination");
								for (final TeethModel teeth : arrTeeth) {
									if ((teeth.arrTeethState.size() > 0)
											|| (teeth.arrTeethTreatmentPlan
													.size() > 0)) {
										((TextView) findViewById(R.id.txt_hard_tissue_examination))
												.append("\n       "
														+ teeth.name
														+ " "
														+ teeth.arrTeethState
																.toString());
									}
								}

							} else {
								// Toast.makeText(
								// PastDentistNotesActivity.this,
								// "There is some error while fetching data please try again.",
								// Toast.LENGTH_SHORT).show();
							}
						} catch (final JSONException e) {
							// Toast.makeText(
							// PastDentistNotesActivity.this,
							// "There is some error while fetching data please try again.",
							// Toast.LENGTH_SHORT).show();
							Log.e("", e);
						}
						loaddialog.dismiss();
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(final VolleyError error) {
						// Toast.makeText(
						// PastDentistNotesActivity.this,
						// "There is some error while fetching data please try again",
						// Toast.LENGTH_SHORT).show();

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
				loginParams.put("cli", "api");
				loginParams.put("siId", Appointment.getSiid());
				loginParams.put("userId",
						sharedPref.getString(Constants.USER_ID, ""));
				Log.i("", loginParams.toString());
				return loginParams;
			}

		};
		patientListRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 5,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		EzApp.mVolleyQueue.add(patientListRequest);

	}

	private void prescriptionTable() {
		final TableLayout tl = (TableLayout) findViewById(R.id.table_prescription);
		tl.removeAllViews();
		final LayoutParams params = new LayoutParams(
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		final TableRow row = (TableRow) getLayoutInflater().inflate(
				R.layout.row_prescription_table, null, false);
		row.setLayoutParams(params);
		txtSno = (TextView) row.findViewById(R.id.txt_sno);
		txtFormulation = (TextView) row.findViewById(R.id.txt_formulation);
		txtDrugName = (TextView) row.findViewById(R.id.txt_name);
		txtStrength = (TextView) row.findViewById(R.id.txt_strength);
		txtUnitType = (TextView) row.findViewById(R.id.txt_unit);
		txtRoute = (TextView) row.findViewById(R.id.txt_route);
		txtFrequency = (TextView) row.findViewById(R.id.txt_frequency);
		txtDays = (TextView) row.findViewById(R.id.txt_days);
		txtQuantity = (TextView) row.findViewById(R.id.txt_quantity);
		txtRefills = (TextView) row.findViewById(R.id.txt_refills);
		txtRefillTime = (TextView) row.findViewById(R.id.txt_refill_time);
		txtNotes = (TextView) row.findViewById(R.id.txt_notes);

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
		for (final MedicineModel doc : visitModel.dentistPlanModel.prescription.arrMedicine) {

			if (doc.getMedicineString().length() > 0) {
				final LayoutParams params1 = new LayoutParams(
						android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
						android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
				final TableRow row1 = (TableRow) getLayoutInflater().inflate(
						R.layout.row_prescription_table, null, false);
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
}
