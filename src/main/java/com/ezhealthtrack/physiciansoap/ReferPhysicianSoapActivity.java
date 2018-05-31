package com.ezhealthtrack.physiciansoap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.ezhealthtrack.greendao.Appointment;
import com.ezhealthtrack.model.AllergiesModel;
import com.ezhealthtrack.model.Document;
import com.ezhealthtrack.model.MedicineModel;
import com.ezhealthtrack.one.EzCommonViews;
import com.ezhealthtrack.physiciansoap.model.VisitNotesModel;
import com.ezhealthtrack.util.Constants;
import com.ezhealthtrack.util.EzActivity;
import com.ezhealthtrack.util.Log;
import com.ezhealthtrack.util.Util;
import com.ezhealthtrack.views.TextUtils;
import com.google.gson.Gson;

public class ReferPhysicianSoapActivity extends EzActivity {
	private Appointment Appointment;
	private VisitNotesModel visitNotes;
	private SharedPreferences sharedPref;
	public static final ArrayList<PastVisitModel> arrPastVisit = new ArrayList<PastVisitModel>();
	private String prevId;
	private String nextId = "0";
	private String visit;
	private CheckBox cbSmoke;
	private CheckBox cbAlcohol;
	private RelativeLayout rlSmoke;
	private RelativeLayout rlAlcohol;
	private TextView txtFahrenheit;
	public static ArrayList<Document> arrDocuments = new ArrayList<Document>();
	private ArrayList<AllergiesModel> arrallergies = new ArrayList<AllergiesModel>();

	private TextView txtSubjective;
	private TextView txtHP;
	private TextView txtDiagnosis;
	private TextView txtObjective;
	private TextView txtVitals;
	private TextView txtGeneralExam;
	private TextView txtPlan;

	private Button btnNext;
	private Button btnPrev;

	private void AutoSave() {
		((TextView) findViewById(R.id.txt_allergy)).setText(Html
				.fromHtml("<b>Allergy :</b> "));
		for (final AllergiesModel allergy : arrallergies) {
			((TextView) findViewById(R.id.txt_allergy)).append("\n "
					+ allergy.getMCatName() + " -->" + allergy.getSCatName()
					+ "\n " + allergy.getAddiInfo());
		}
		TextUtils.autoSaveTextViewLayout(
				(LinearLayout) findViewById(R.id.ll_hp),
				visitNotes.subjectiveModel.hashHP);
		TextUtils.autoSaveTextView(
				(TextView) findViewById(R.id.edit_cheif_complaint),
				visitNotes.subjectiveModel.hashCC);

		if (Util.isEmptyString(((TextView) findViewById(R.id.edit_cheif_complaint))
				.getText().toString()))
			((TextView) findViewById(R.id.edit_cheif_complaint))
					.setText(Appointment.getReason());
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
		((TextView) findViewById(R.id.txt_treatment_done))
				.setText(visitNotes.physicianPlanModel.treatmentDone
						.getTreatmentDone());

		((TextView) findViewById(R.id.txt_prescription)).setText(Html
				.fromHtml("<b>Prescription :</b>"));
		// EzUtils.prescriptionTableSOAPNotes(this);
		int i = 1;
		for (final MedicineModel medicine : visitNotes.physicianPlanModel.prescription.arrMedicine) {
			((TextView) findViewById(R.id.txt_prescription)).append(Html
					.fromHtml("<br> " + "<b>" + i + ". </b>"
							+ medicine.getMedicineStringExtra()));
			i++;
		}
		if (!Util.isEmptyString(visitNotes.physicianPlanModel.prescription.si
				.get("si"))
				|| visitNotes.physicianPlanModel.prescription.arrMedicine
						.size() > 0)
			((TextView) findViewById(R.id.txt_prescription))
					.append("\n Special Instruction : "
							+ visitNotes.physicianPlanModel.prescription.si
									.get("si"));
		((TextView) findViewById(R.id.txt_radiology)).setText(Html
				.fromHtml("<b>Radiology :</b> "));
		for (final Entry<String, String> entry : visitNotes.physicianPlanModel.radiology.hashRadiology
				.entrySet()) {
			if (entry.getValue().equals("on")) {
				((TextView) findViewById(R.id.txt_radiology)).append("\n      "
						+ entry.getKey());
			}
		}

		((TextView) findViewById(R.id.txt_lab)).setText(Html
				.fromHtml("<b>Lab Order :</b> "));
		for (final Entry<String, String> entry : visitNotes.physicianPlanModel.lab.hashLab
				.entrySet()) {
			if (entry.getValue().equals("on")) {
				((TextView) findViewById(R.id.txt_lab)).append("\n      "
						+ entry.getKey());
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
	}

	private void getData() {
		final String url = APIs.REFER_VIEW() + Appointment.getEpid()
				+ "/cli/api";
		final Dialog loaddialog = Util
				.showLoadDialog(ReferPhysicianSoapActivity.this);
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
											ReferPhysicianSoapActivity.this,
											"Referred Appointment not started yet");
									loaddialog.dismiss();
								} else {
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

										EzCommonViews
												.radiologyTable(ReferPhysicianSoapActivity.this);
										EzCommonViews
												.labTable(ReferPhysicianSoapActivity.this);
										EzCommonViews
												.ekgTable(ReferPhysicianSoapActivity.this);

									}
									if (Util.isEmptyString(jObj
											.getString("tmpl-inst")))
										Util.AlertdialogWithFinish(
												ReferPhysicianSoapActivity.this,
												"No notes to share yet.");
									loaddialog.dismiss();
									final JSONObject followUpList = jObj
											.getJSONObject("followupList");
									prevId = followUpList.getString("prev");
									nextId = followUpList.getString("next");
									visit = followUpList.getString("visit");
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
									final JSONObject soap = jObj.getJSONObject(
											"tmpl-inst").getJSONObject("Soap");
									visitNotes.jsonData = jObj
											.getJSONObject("tmpl-inst");
									visitNotes.subjectiveModel.JsonParse(soap
											.getJSONObject("subj"));
									visitNotes.privateNote.jsonParse(soap
											.getJSONObject("private-note"));
									visitNotes.diagnosisModel.JsonParse(soap
											.getJSONObject("asse"));

									visitNotes.examinationModel.JsonParse(soap
											.getJSONObject("obje"));
									visitNotes.physicianPlanModel
											.JsonParse(soap
													.getJSONObject("plan"));

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
								Util.AlertdialogWithFinish(
										ReferPhysicianSoapActivity.this,
										"Referred Appointment not started yet");
							}
						} catch (final JSONException e) {
							Util.AlertdialogWithFinish(
									ReferPhysicianSoapActivity.this,
									"Referred Appointment not started yet");
							Log.e("", e);
						}
						loaddialog.dismiss();
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(final VolleyError error) {
						Util.AlertdialogWithFinish(
								ReferPhysicianSoapActivity.this,
								"Referred Appointment not started yet");
						loaddialog.dismiss();
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

	}

	// @Override
	// public void onClick(final View v) {
	// switch (v.getId()) {
	// case R.id.btn_prev:
	// try {
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
	// } catch (Exception e) {
	// Appointment.setEpid(prevId);
	// getData();
	// }
	// break;
	//
	// case R.id.btn_next:
	// try {
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
	// } catch (Exception e) {
	// Appointment.setEpid(nextId);
	// getData();
	// }
	// break;
	//
	// case R.id.txt_subjective:
	// Util.showHideView(findViewById(R.id.ll_subjective_1),
	// (ImageView) findViewById(R.id.img_subjective));
	// break;
	//
	// case R.id.txt_hp:
	// Util.showHideView(findViewById(R.id.ll_hp),
	// (ImageView) findViewById(R.id.img_hp));
	// break;
	//
	// case R.id.txt_diagnosis:
	// Util.showHideView(findViewById(R.id.ll_diagnosis),
	// (ImageView) findViewById(R.id.img_diagnosis));
	// break;
	//
	// case R.id.txt_objective:
	// Util.showHideView(findViewById(R.id.ll_objective),
	// (ImageView) findViewById(R.id.img_objective));
	// break;
	//
	// case R.id.txt_vitals:
	// Util.showHideView(findViewById(R.id.ll_vitals),
	// (ImageView) findViewById(R.id.img_vitals));
	// break;
	//
	// case R.id.txt_general_exam:
	// Util.showHideView(findViewById(R.id.ll_general_exam),
	// (ImageView) findViewById(R.id.img_general_exam));
	// break;
	//
	// case R.id.txt_plan:
	// Util.showHideView(findViewById(R.id.ll_plan),
	// (ImageView) findViewById(R.id.img_plan));
	// break;
	//
	// default:
	// break;
	// }
	//
	// }

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_refer_physician_notes);
		arrDocuments.clear();
		sharedPref = getApplicationContext().getSharedPreferences(
				Constants.EZ_SHARED_PREF, Context.MODE_PRIVATE);
		Appointment = new Appointment();
		Appointment.setEpid(getIntent().getStringExtra("epid"));
		Appointment.setBkid(getIntent().getStringExtra("bkid"));
		Appointment.setSiid(getIntent().getStringExtra("siid"));
		// findViewById(R.id.txt_subjective).setOnClickListener(this);
		// findViewById(R.id.txt_hp).setOnClickListener(this);
		// findViewById(R.id.txt_diagnosis).setOnClickListener(this);
		// findViewById(R.id.txt_objective).setOnClickListener(this);
		// findViewById(R.id.txt_vitals).setOnClickListener(this);
		// findViewById(R.id.txt_general_exam).setOnClickListener(this);
		// findViewById(R.id.txt_plan).setOnClickListener(this);

		txtSubjective = (TextView) findViewById(R.id.txt_subjective);
		txtHP = (TextView) findViewById(R.id.txt_hp);
		txtDiagnosis = (TextView) findViewById(R.id.txt_diagnosis);
		txtObjective = (TextView) findViewById(R.id.txt_objective);
		txtVitals = (TextView) findViewById(R.id.txt_vitals);
		txtGeneralExam = (TextView) findViewById(R.id.txt_general_exam);
		txtPlan = (TextView) findViewById(R.id.txt_plan);
		btnPrev = (Button) findViewById(R.id.btn_prev);
		btnNext = (Button) findViewById(R.id.btn_next);

		cbSmoke = (CheckBox) findViewById(R.id.cb_smoke);
		cbAlcohol = (CheckBox) findViewById(R.id.cb_alcohol);
		rlAlcohol = (RelativeLayout) findViewById(R.id.rl_alcohol);
		rlSmoke = (RelativeLayout) findViewById(R.id.rl_smoke);
		txtFahrenheit = (TextView) findViewById(R.id.txt_Fahrenheit);
		txtFahrenheit.setText(Html.fromHtml("&#176;F"));

		visitNotes = new VisitNotesModel();
		if (getIntent().hasExtra("type")
				&& getIntent().getStringExtra("type").equals("past")) {
			// getActionBar().setTitle("Past Episode");
			getPastData();
		} else if (!Util.isEmptyString(Appointment.getEpid())) {
			Log.i("", "get data 1");
			getData();
		}

		txtPlan.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Util.showHideView(findViewById(R.id.ll_plan),
						(ImageView) findViewById(R.id.img_plan));
			}
		});

		txtGeneralExam.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Util.showHideView(findViewById(R.id.ll_general_exam),
						(ImageView) findViewById(R.id.img_general_exam));
			}
		});

		txtVitals.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Util.showHideView(findViewById(R.id.ll_vitals),
						(ImageView) findViewById(R.id.img_vitals));
			}
		});

		txtObjective.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Util.showHideView(findViewById(R.id.ll_objective),
						(ImageView) findViewById(R.id.img_objective));
			}
		});

		txtDiagnosis.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Util.showHideView(findViewById(R.id.ll_diagnosis),
						(ImageView) findViewById(R.id.img_diagnosis));
			}
		});

		txtHP.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Util.showHideView(findViewById(R.id.ll_hp),
						(ImageView) findViewById(R.id.img_hp));
			}
		});

		txtSubjective.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Util.showHideView(findViewById(R.id.ll_subjective_1),
						(ImageView) findViewById(R.id.img_subjective));
			}
		});

		btnPrev.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					if (!prevId.equals("0")) {
						if (getIntent().getExtras().getString("type")
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

			}
		});

		btnNext.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					if (!nextId.equals("0")) {
						if (getIntent().getExtras().getString("type")
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

			}
		});

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

		EzCommonViews.radiologyTable(this);
		EzCommonViews.labTable(this);
		EzCommonViews.ekgTable(this);
		// EzUtils.prescriptionTableSOAPNotes(this);
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

	private void getPastData() {
		final String url = APIs.PAST_VISIT_VIEW();
		final Dialog loaddialog = Util
				.showLoadDialog(ReferPhysicianSoapActivity.this);
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
									EzCommonViews
											.radiologyTable(ReferPhysicianSoapActivity.this);
									EzCommonViews
											.labTable(ReferPhysicianSoapActivity.this);
									EzCommonViews
											.ekgTable(ReferPhysicianSoapActivity.this);
									// EzUtils.prescriptionTableSOAPNotes(ReferPhysicianSoapActivity.this);
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

								for (final Map.Entry<String, String> entry : visitNotes.subjectiveModel.hashHP
										.entrySet()) {
									if (!Util.isEmptyString(entry.getValue())) {
										findViewById(R.id.img_hp_tick)
												.setVisibility(View.VISIBLE);
									}
								}
								AutoSave();

							} else {
								// Toast.makeText(
								// PastPhysicianSoapActivity.this,
								// "There is some error while fetching data please try again.",
								// Toast.LENGTH_SHORT).show();
							}
						} catch (final JSONException e) {
							// Toast.makeText(
							// PastPhysicianSoapActivity.this,
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
						// PastPhysicianSoapActivity.this,
						// "There is some error while fetching data please try again",
						// Toast.LENGTH_SHORT).show();

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
}
