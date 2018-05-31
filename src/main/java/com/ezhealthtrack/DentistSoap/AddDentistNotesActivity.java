package com.ezhealthtrack.DentistSoap;

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
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.print.PrintHelper;
import android.support.v7.widget.PopupMenu;
import android.text.Editable;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
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
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.ezhealthrack.api.APIs;
import com.ezhealthtrack.EzApp;
import com.ezhealthtrack.R;
import com.ezhealthtrack.DentistSoap.Model.DiagnosisModel;
import com.ezhealthtrack.DentistSoap.Model.ExaminationModel;
import com.ezhealthtrack.DentistSoap.Model.MasterPlanModel;
import com.ezhealthtrack.DentistSoap.Model.PastVisitModel;
import com.ezhealthtrack.DentistSoap.Model.PlanModel;
import com.ezhealthtrack.DentistSoap.Model.SubjectiveModel;
import com.ezhealthtrack.DentistSoap.Model.TeethModel;
import com.ezhealthtrack.DentistSoap.Model.VisitNotesModel;
import com.ezhealthtrack.activity.AddPrescriptionActivity;
import com.ezhealthtrack.activity.DashboardActivity;
import com.ezhealthtrack.activity.EditAccountActivity;
import com.ezhealthtrack.activity.PrevVisitsActivity;
import com.ezhealthtrack.activity.SheduleActivity;
import com.ezhealthtrack.adapter.CheckedListAdapter;
import com.ezhealthtrack.adapter.DoctorsAutoCompleteAdapter;
import com.ezhealthtrack.controller.PatientController;
import com.ezhealthtrack.greendao.Allergy;
import com.ezhealthtrack.greendao.Appointment;
import com.ezhealthtrack.greendao.Icd10Item;
import com.ezhealthtrack.greendao.Patient;
import com.ezhealthtrack.greendao.SoapNote;
import com.ezhealthtrack.labs.controller.OrderController.OnResponseData;
import com.ezhealthtrack.model.AllergiesModel;
import com.ezhealthtrack.model.DoctorModel;
import com.ezhealthtrack.model.Document;
import com.ezhealthtrack.model.MedicineModel;
import com.ezhealthtrack.model.PatInfo;
import com.ezhealthtrack.model.PatientShow;
import com.ezhealthtrack.model.ReferFromModel;
import com.ezhealthtrack.model.ReferToModel;
import com.ezhealthtrack.physiciansoap.model.PrivateNotesModel;
import com.ezhealthtrack.util.BaseActivity;
import com.ezhealthtrack.util.Constants;
import com.ezhealthtrack.util.Log;
import com.ezhealthtrack.util.Util;
import com.ezhealthtrack.views.EditUtils;
import com.ezhealthtrack.views.RadioGroupUtils;
import com.ezhealthtrack.views.RadioGroupWithText;
import com.ezhealthtrack.views.TeethView;
import com.ezhealthtrack.views.TextUtils;
import com.google.gson.Gson;

public class AddDentistNotesActivity extends BaseActivity implements
		OnClickListener {
	public static MasterPlanModel masterPlan = new MasterPlanModel();
	private SharedPreferences sharedPref;
	public static Patient patientModel;
	public static Appointment appointment;
	public static VisitNotesModel visitNotes = new VisitNotesModel();
	static VisitNotesModel visitModel;
	private TextView txtPatientName;
	private TextView txtDate;
	private TextView txtVisitCount;
	private TextView txtReason;
	private TextView txtVitals;
	private TextView txtSubjective;
	private TextView txtObjective;
	private TextView txtGeneralExam;
	private TextView txtHP;
	private TextView txtDocumentName;
	private TextView txtDocumentType;
	private TextView txtUploadDate;
	private TextView txtAction;
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
	private TextView txtDetails;
	private TextView txtFreqDetail;
	private TextView txtHistory;
	// private TextView txtCheifComplaint;
	private EditText editCheifComplaint;
	private TextView txtOralExam;
	private TextView txtSoftTissueExam;
	private TextView txtHardTissueExam;
	private TextView txtAllergy;
	private EditText editYearSmoke;
	private EditText editMonthSmoke;
	private EditText editDaySmoke;
	private EditText editYearAlcohol;
	private EditText editMonthAlcohol;
	private EditText editDayAlcohol;
	private View llSubjective;
	private Button btnLab;
	private Button btnRefer;
	private Button btnAllergy;
	private Button btnXray;
	private Button btnTreatmentPlan;
	private Button btnPrescription;
	private TextView txtMasterPlan;
	private ArrayList<TeethModel> arrTeeth;
	private TextView txtRadiology;
	private ArrayAdapter<String> adapter;
	private final ArrayList<String> arrIcd = new ArrayList<String>();
	private MultiAutoCompleteTextView actvFinal;
	private MultiAutoCompleteTextView actvProv;
	private TeethModel prevTeeth = new TeethModel();
	private TextView txtPrintRadiology;
	private TextView txtPrintLabOrder;
	private TextView txtPrintPrescription;
	private TextView txtFollowup;
	private CheckBox cbComplete;
	private CheckBox cbSmoke;
	private CheckBox cbAlcohol;
	private RelativeLayout rlSmoke;
	private RelativeLayout rlAlcohol;
	private TextView txtFahrenheit;
	public static final ArrayList<PastVisitModel> arrPastVisit = new ArrayList<PastVisitModel>();
	private final HashMap<String, String> hashFollowUp = new HashMap<String, String>();
	private final ArrayList<ReferToModel> arrReferTo = new ArrayList<ReferToModel>();
	private final ArrayList<ReferFromModel> arrReferFrom = new ArrayList<ReferFromModel>();
	private ArrayList<AllergiesModel> arrallergies = new ArrayList<AllergiesModel>();
	public static ArrayList<Document> arrDocuments = new ArrayList<Document>();

	void alternateTooth(final TextView txtTooth,
			final RadioGroup rgToothSelection, final GridView listHardTissue,
			final ImageView img, final TeethModel teeth, final int no,
			final Dialog dialog, final View view) {
		txtTooth.setText("");
		if (visitModel.dentistExaminationModel.oralExamination.hardTissue.tp
				.contains("Permanent")) {
			itemsChecked(listHardTissue, img, teeth, view);
			((EditText) dialog.findViewById(R.id.edit_others))
					.setText(teeth.hashState.get("othe"));
			prevTeeth = teeth;
		} else {
			itemsChecked(listHardTissue, img, arrTeeth.get(no), view);
			((EditText) dialog.findViewById(R.id.edit_others)).setText(arrTeeth
					.get(no).hashState.get("othe"));
			prevTeeth = arrTeeth.get(no);
		}
		rgToothSelection.setVisibility(View.VISIBLE);
		rgToothSelection
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(final RadioGroup group,
							final int checkedId) {
						String s = ((EditText) dialog
								.findViewById(R.id.edit_others)).getText()
								.toString();
						prevTeeth.arrTeethState.remove(prevTeeth.hashState
								.get("othe"));
						prevTeeth.hashState.put("othe", s);
						if (!Util.isEmptyString(s))
							prevTeeth.arrTeethState.add(s);
						final RadioButton rb = (RadioButton) group
								.findViewById(checkedId);
						if (rb.getText().toString()
								.equalsIgnoreCase(img.getTag().toString())) {
							itemsChecked(listHardTissue, img, teeth, view);
							((EditText) dialog.findViewById(R.id.edit_others))
									.setText(teeth.hashState.get("othe"));
							prevTeeth = teeth;
						} else {
							itemsChecked(listHardTissue, img, arrTeeth.get(no),
									view);
							((EditText) dialog.findViewById(R.id.edit_others))
									.setText(arrTeeth.get(no).hashState
											.get("othe"));
							prevTeeth = arrTeeth.get(no);
						}
					}
				});

	}

	private void AutoSave() {
		EditUtils.autoSaveEditTextLayout(
				(LinearLayout) findViewById(R.id.ll_hp),
				visitModel.dentistSubjectiveModel.hashHP);
		EditUtils.autoSaveEditText(editCheifComplaint,
				visitModel.dentistSubjectiveModel.hashCC);
		EditUtils.autoSaveEditTextLayout(
				(LinearLayout) findViewById(R.id.ll_vitals),
				visitModel.dentistExaminationModel.hashVitals);
		EditUtils
				.autoSaveEditTextLayout(
						(LinearLayout) findViewById(R.id.ll_general_exam),
						visitModel.dentistExaminationModel.generalExamination.hashState);
		EditUtils
				.autoSaveEditTextLayout(
						(LinearLayout) findViewById(R.id.ll_soft_tissue),
						visitModel.dentistExaminationModel.oralExamination.softTissue.hashState);
		EditUtils.autoSaveEditText(
				(EditText) findViewById(R.id.edit_private_notes),
				visitModel.privateNote.hashNote);
		EditUtils.autoSaveEditText((EditText) findViewById(R.id.edit_td),
				visitModel.dentistPlanModel.treatmentDone.hashTd);
		((TextView) findViewById(R.id.txt_treatment_done))
				.setText(visitNotes.dentistPlanModel.treatmentDone
						.getTreatmentDone());
		// if
		// (Util.isEmptyString(visitModel.dentistExaminationModel.generalExamination.hashCb
		// .get("swd"))) {
		// visitModel.dentistExaminationModel.generalExamination.hashCb.put(
		// "swd", "");
		// }
		RadioGroupUtils.autoSaveRg(
				(RadioGroup) findViewById(R.id.rg_satisfaction),
				visitModel.dentistExaminationModel.generalExamination.hashCb,
				"swd");
		generalExamination();
		// ((TextView) findViewById(R.id.txt_treatment_done))
		// .setText(visitModel.dentistPlanModel.treatmentDone
		// .getTreatmentDone());
		boolean radiologyflag = false;
		((TextView) findViewById(R.id.txt_radiology))
				.setText(Html.fromHtml("<b>Radiology :</b> "
						+ visitModel.dentistPlanModel.radiology.getLabString()));
		if (visitModel.dentistPlanModel.radiology.hashRadiology
				.containsValue("on")
				|| !Util.isEmptyString(visitModel.dentistPlanModel.radiology.hashRadiology
						.get("othe")))
			radiologyflag = true;
		try {
			findViewById(R.id.txt_print_radiology).setActivated(radiologyflag);
			findViewById(R.id.txt_print_radiology).setEnabled(radiologyflag);
			findViewById(R.id.txt_print_radiology).setClickable(radiologyflag);
		} catch (Exception e) {

		}
		boolean labflag = false;
		((TextView) findViewById(R.id.txt_lab)).setText(Html
				.fromHtml("<b>Lab order :</b> "
						+ visitModel.dentistPlanModel.lab.getLabString()));
		if (visitModel.dentistPlanModel.lab.hashLab.containsValue("on")
				|| !Util.isEmptyString(visitModel.dentistPlanModel.lab.hashLab
						.get("othe")))
			labflag = true;
		try {
			findViewById(R.id.txt_print_laborder).setActivated(labflag);
			findViewById(R.id.txt_print_laborder).setEnabled(labflag);
			findViewById(R.id.txt_print_laborder).setClickable(labflag);
		} catch (Exception e) {

		}
		((TextView) findViewById(R.id.txt_treatment_plan))
				.setText(visitModel.dentistPlanModel.treatmentPlan
						.getTpString(arrTeeth));
		boolean prescriptionflag = false;
		((TextView) findViewById(R.id.txt_prescription)).setText(Html
				.fromHtml("<b>Prescription :</b> "));
		int i = 1;
		for (final MedicineModel medicine : visitModel.dentistPlanModel.prescription.arrMedicine) {
			((TextView) findViewById(R.id.txt_prescription)).append(Html
					.fromHtml(""));
			prescriptionflag = true;
			i++;
		}
		prescriptionTable();

		boolean allflag = labflag || radiologyflag || prescriptionflag;
		findViewById(R.id.txt_print_all).setActivated(allflag);
		findViewById(R.id.txt_print_all).setEnabled(allflag);
		findViewById(R.id.txt_print_all).setClickable(allflag);

		if (!Util.isEmptyString(visitModel.dentistPlanModel.prescription.si
				.get("si"))
				&& visitModel.dentistPlanModel.prescription.arrMedicine.size() > 0)
			((TextView) findViewById(R.id.txt_prescription))
					.append("\n Special Instruction : "
							+ visitModel.dentistPlanModel.prescription.si
									.get("si"));

		if (visitModel.privateNote.hashNote.containsValue("status")
				&& visitModel.privateNote.hashNote.get("status").equals("S")) {
			// findViewById(R.id.rg_private_notes_1).setVisibility(View.VISIBLE);
			// findViewById(R.id.txt_shadow_type).setVisibility(View.VISIBLE);
		} else {
			findViewById(R.id.rg_private_notes_1).setVisibility(View.GONE);
			findViewById(R.id.txt_shadow_type).setVisibility(View.GONE);
			if (Util.isEmptyString(visitModel.privateNote.hashNote
					.get("status")))
				visitModel.privateNote.hashNote.put("status", "Pr");
		}
		final RadioGroup rgPrivateNote = (RadioGroup) findViewById(R.id.rg_private_notes);
		try {
			((RadioButton) rgPrivateNote
					.findViewWithTag(visitModel.privateNote.hashNote
							.get("status"))).setChecked(true);
		} catch (final Exception e) {

		}
		rgPrivateNote.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				RadioButton rb = (RadioButton) group.findViewById(checkedId);
				visitModel.privateNote.hashNote.put("status", rb.getTag()
						.toString());
			}
		});

	}

	private void createSoap() {
		Log.i("", "create soap called");
		final String url = APIs.SOAP_CREATE();
		final Dialog loaddialog = Util
				.showLoadDialog(AddDentistNotesActivity.this);
		final JsonObjectRequest patientListRequest = new JsonObjectRequest(
				Request.Method.POST, url, visitModel.jsonData,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(final JSONObject jObj) {
						String s = jObj.toString();
						Log.i("response", s);
						while (s.length() > 100) {
							s = s.substring(100);
						}
						try {
							if (jObj.getString("s").equals("200")) {
								if (!Util
										.isEmptyString(masterPlan.masterPlanType)) {
									Log.i("", masterPlan.masterPlanType);
									masterPlan.updateJson(masterPlan.jsonData,
											AddDentistNotesActivity.this);
									postMasterData();
								} else {
									Util.AlertdialogWithFinish(
											AddDentistNotesActivity.this,
											"Visit Notes updated successfully");
								}

							} else {
								Log.e("", jObj.toString());
								Util.Alertdialog(AddDentistNotesActivity.this,
										"Network Error, try again later");
							}
						} catch (final JSONException e) {
							Util.Alertdialog(AddDentistNotesActivity.this,
									"There is some error while submitting data.");
							Log.e("", e);
						}
						loaddialog.dismiss();
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(final VolleyError error) {
						Util.Alertdialog(AddDentistNotesActivity.this,
								"There is some error while submitting data.");

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
				.showLoadDialog(AddDentistNotesActivity.this);
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
										AddDentistNotesActivity.this);
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
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}

						} catch (final JSONException e) {
							Util.Alertdialog(AddDentistNotesActivity.this,
									"There is some error while fetching data, please try again");
							Log.e("", e);
						}
						loaddialog.dismiss();
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(final VolleyError error) {
						Util.Alertdialog(AddDentistNotesActivity.this,
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

	private void dialogExamination(final String id) {
		final String url = APIs.SOAP_HISTORY() + "/cli/api";
		final Dialog loaddialog = Util
				.showLoadDialog(AddDentistNotesActivity.this);
		final StringRequest patientListRequest = new StringRequest(
				Request.Method.POST, url, new Response.Listener<String>() {
					@Override
					public void onResponse(final String response) {
						try {
							// Log.i("past visits", response);
							final JSONObject jObj = new JSONObject(response);
							try {
								final ExaminationModel exam = new ExaminationModel();
								final Dialog dialog = new Dialog(
										AddDentistNotesActivity.this,
										R.style.Theme_AppCompat_Light);
								dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
								dialog.setContentView(R.layout.dialog_examination);
								dialog.getWindow()
										.setBackgroundDrawable(
												new ColorDrawable(
														android.graphics.Color.TRANSPARENT));
								if (jObj.getString("s").equals("200")) {
									if (jObj.get("data") instanceof JSONObject)
										exam.JsonParse(jObj
												.getJSONObject("data"));
									TextUtils.autoSaveTextViewLayout(
											(LinearLayout) dialog
													.findViewById(R.id.ll_vitals),
											exam.hashVitals);
									TextUtils.autoSaveTextViewLayout(
											(LinearLayout) dialog
													.findViewById(R.id.ll_general_exam),
											exam.generalExamination.hashState);
									TextUtils.autoSaveTextViewLayout(
											(LinearLayout) dialog
													.findViewById(R.id.ll_soft_tissue),
											exam.oralExamination.softTissue.hashState);
									if (Util.isEmptyString(exam.generalExamination.hashCb
											.get("swd"))) {
										exam.generalExamination.hashCb.put(
												"swd", "");
									}
									if (!Util
											.isEmptyString(exam.oralExamination.hardTissue.hashNote
													.get("note")))
										((TextView) dialog
												.findViewById(R.id.txt_hte_note))
												.setText("Note : "
														+ exam.oralExamination.hardTissue.hashNote
																.get("note"));
									RadioGroupUtils.autoSaveRg(
											(RadioGroup) dialog
													.findViewById(R.id.rg_satisfaction),
											exam.generalExamination.hashCb,
											"swd");
									final ArrayList<String> colors = new ArrayList<String>();
									colors.add("#ff0000");
									colors.add("#00000000");
									new RadioGroupWithText(
											AddDentistNotesActivity.this,
											dialog.findViewById(R.id.txt_pallor),
											dialog.findViewById(R.id.rg_pallor),
											2, colors,
											exam.generalExamination.hashState);
									new RadioGroupWithText(
											AddDentistNotesActivity.this,
											dialog.findViewById(R.id.txt_icterus),
											dialog.findViewById(R.id.rg_icterus),
											2, colors,
											exam.generalExamination.hashState);
									new RadioGroupWithText(
											AddDentistNotesActivity.this,
											dialog.findViewById(R.id.txt_clubbing),
											dialog.findViewById(R.id.rg_clubbing),
											2, colors,
											exam.generalExamination.hashState);
									new RadioGroupWithText(
											AddDentistNotesActivity.this,
											dialog.findViewById(R.id.txt_cyanosis),
											dialog.findViewById(R.id.rg_cyanosis),
											2, colors,
											exam.generalExamination.hashState);
									new RadioGroupWithText(
											AddDentistNotesActivity.this,
											dialog.findViewById(R.id.txt_edema),
											dialog.findViewById(R.id.rg_edema),
											2, colors,
											exam.generalExamination.hashState);
									new RadioGroupWithText(
											AddDentistNotesActivity.this,
											dialog.findViewById(R.id.txt_lymphadenopathy),
											dialog.findViewById(R.id.rg_lymphadenopathy),
											2, colors,
											exam.generalExamination.hashState);
									for (final TeethModel teeth : exam.oralExamination.hardTissue.arrTeeth) {
										Log.i("",
												teeth.arrTeethState.toString());
										if ((teeth.arrTeethState.size() > 0)) {
											((TextView) dialog
													.findViewById(R.id.txt_hard_tissue_examination))
													.append("\n       "
															+ teeth.name
															+ " "
															+ teeth.arrTeethState
																	.toString());
										}
									}
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
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						} catch (final JSONException e) {
							Util.Alertdialog(AddDentistNotesActivity.this,
									"There is some error while fetching data, please try again");
							Log.e("", e);
						}
						loaddialog.dismiss();
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(final VolleyError error) {
						Util.Alertdialog(AddDentistNotesActivity.this,
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
				.showLoadDialog(AddDentistNotesActivity.this);
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
								if (jObj.getJSONObject("data").get(
										"private-note") instanceof JSONObject) {
									pNote.jsonParse(jObj.getJSONObject("data")
											.getJSONObject("private-note"));
								}
								final ArrayList<TeethModel> arrTeeth = new ArrayList<TeethModel>();
								final ArrayList<Document> arrDocuments = new ArrayList<Document>();
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
									if (jObj.has("documents")) {
										JSONArray arr = jObj
												.getJSONArray("documents");
										for (int i = 0; i < arr.length(); i++) {
											Document doc = new Gson().fromJson(
													arr.get(i).toString(),
													Document.class);
											arrDocuments.add(doc);
										}
									}
								}
								plan.JsonParse(jObj.getJSONObject("data"),
										arrTeeth);
								final Dialog dialog = new Dialog(
										AddDentistNotesActivity.this,
										R.style.Theme_AppCompat_Light);
								dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
								dialog.setContentView(R.layout.dialog_plan);
								radiologyTable(dialog, arrDocuments);
								labTable(dialog, arrDocuments);
								dialog.getWindow()
										.setBackgroundDrawable(
												new ColorDrawable(
														android.graphics.Color.TRANSPARENT));
								try {
									if (pNote.hashNote
											.containsKey("private-note")) {
										((TextView) dialog
												.findViewById(R.id.txt_private_notes))
												.setText(pNote.hashNote
														.get("private-note"));
									}
								} catch (Exception e) {
									e.printStackTrace();
								}
								((TextView) dialog
										.findViewById(R.id.txt_radiology)).setText(Html
										.fromHtml("<b>Radiology :</b> "
												+ plan.radiology.getLabString()));
								((TextView) dialog.findViewById(R.id.txt_lab)).setText(Html
										.fromHtml("<b>Lab order :</b> "
												+ plan.lab.getLabString()));
								((TextView) dialog
										.findViewById(R.id.txt_treatment_plan))
										.setText(plan.treatmentPlan
												.getTpStringPast(arrTeeth));

								((TextView) dialog
										.findViewById(R.id.txt_prescription)).setText(Html
										.fromHtml("<b>Prescription :</b> "));
								prescriptionTablePast(dialog);

								if (!Util.isEmptyString(plan.prescription.si
										.get("si"))
										&& plan.prescription.arrMedicine.size() > 0)
									((TextView) dialog
											.findViewById(R.id.txt_prescription))
											.append("\n Special Instruction : "
													+ plan.prescription.si
															.get("si"));
								((TextView) dialog
										.findViewById(R.id.txt_allergy))
										.setText(Html
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
								((TextView) dialog
										.findViewById(R.id.txt_treatment_done))
										.setText(plan.treatmentDone
												.getTreatmentDone());

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
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}

						} catch (final JSONException e) {
							Util.Alertdialog(AddDentistNotesActivity.this,
									"There is some error while fetching data, please try again");
							Log.e("", e);
						}
						loaddialog.dismiss();
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(final VolleyError error) {
						Util.Alertdialog(AddDentistNotesActivity.this,
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
		final Dialog loaddialog = Util
				.showLoadDialog(AddDentistNotesActivity.this);
		final StringRequest patientListRequest = new StringRequest(
				Request.Method.POST, url, new Response.Listener<String>() {
					@Override
					public void onResponse(final String response) {
						try {
							// Log.i("past visits", response);
							final JSONObject jObj = new JSONObject(response);
							try {

								final Dialog dialog = new Dialog(
										AddDentistNotesActivity.this);
								dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
								dialog.setContentView(R.layout.dialog_subjective);
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
									}
									for (final Entry entry : sub.hashHP
											.entrySet()) {
										((TextView) dialog
												.findViewById(
														R.id.ll_subjective)
												.findViewWithTag(entry.getKey()))
												.setText(entry.getValue()
														.toString());
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
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}

						} catch (final JSONException e) {
							Util.Alertdialog(AddDentistNotesActivity.this,
									"There is some error while fetching data, please try again");
							Log.e("", e);
						}
						loaddialog.dismiss();
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(final VolleyError error) {
						Util.Alertdialog(AddDentistNotesActivity.this,
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

	private void dialogPrintPrescription() {
		final Dialog dialogPrintPrescription = new Dialog(this,
				R.style.Theme_AppCompat_Light);
		dialogPrintPrescription.requestWindowFeature(Window.FEATURE_NO_TITLE);

		dialogPrintPrescription
				.setContentView(R.layout.dialog_print_prescription);
		dialogPrintPrescription.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		final SimpleDateFormat df = new SimpleDateFormat(" EEE, MMM dd, yyyy");
		String drName = EzApp.sharedPref.getString(
				Constants.DR_NAME, "");
		((TextView) dialogPrintPrescription.findViewById(R.id.txt_drname))
				.setText(drName);
		String drAddress = EzApp.sharedPref.getString(
				Constants.DR_ADDRESS, "");
		((TextView) dialogPrintPrescription.findViewById(R.id.txt_doc_address))
				.setText(drAddress + " - "
						+ EditAccountActivity.profile.getZip());
		((TextView) dialogPrintPrescription.findViewById(R.id.txt_ptname))
				.setText("Patient Name: " + patientModel.getPfn() + " "
						+ patientModel.getPln());
		((TextView) dialogPrintPrescription.findViewById(R.id.txt_gender))
				.setText("Gender: " + patientModel.getPgender());

		((TextView) dialogPrintPrescription.findViewById(R.id.txt_age))
				.setText("Age: " + patientModel.getPage());
		((TextView) dialogPrintPrescription.findViewById(R.id.txt_date))
				.setText("Date: " + df.format(new Date()));
		((TextView) dialogPrintPrescription.findViewById(R.id.txt_ptaddress))
				.setText("Address: " + patientModel.getPadd1() + " "
						+ patientModel.getPadd2() + ", "
						+ patientModel.getParea() + ", "
						+ patientModel.getPcity() + ", "
						+ patientModel.getPstate() + ", "
						+ patientModel.getPcountry() + " - "
						+ patientModel.getPzip());
		// ((TextView) dialogPrintPrescription.findViewById(R.id.txt_vitals))
		// .setText("Vitals: ");
		// if (!Util.isEmptyString(visitNotes.dentistExaminationModel.hashVitals
		// .get("low")))
		// ((TextView) dialogPrintPrescription.findViewById(R.id.txt_vitals))
		// .append("Blood Pressure = "
		// + visitNotes.dentistExaminationModel.hashVitals
		// .get("low")
		// + "-"
		// + visitNotes.dentistExaminationModel.hashVitals
		// .get("high")
		// + " mm of hg, Body Temperature = "
		// + visitNotes.dentistExaminationModel.hashVitals
		// .get("temp")
		// + " Degree Celsius, Respiratory Rate = "
		// + visitNotes.dentistExaminationModel.hashVitals
		// .get("rr")
		// + " cycles per minute, Pulse = "
		// + visitNotes.dentistExaminationModel.hashVitals
		// .get("puls") + " beats per minute");

		((TextView) dialogPrintPrescription.findViewById(R.id.txt_prescription))
				.setText(Html.fromHtml("<b>Prescription: </b>"));
		((TextView) dialogPrintPrescription
				.findViewById(R.id.txt_special_notes))
				.setText("Special Instructions: "
						+ visitModel.dentistPlanModel.prescription.si.get("si"));
		prescriptionTablePast(dialogPrintPrescription);

		PatientController.patientBarcode(AddDentistNotesActivity.this,
				new OnResponseData() {

					@Override
					public void onResponseListner(Object response) {

						Util.getImageFromUrl(response.toString(),
								DashboardActivity.imgLoader,
								(ImageView) dialogPrintPrescription
										.findViewById(R.id.img_barcode));
					}
				}, patientModel);

		if (!Util.isEmptyString(EzApp.sharedPref.getString(
				Constants.SIGNATURE, "signature"))) {
			Util.getImageFromUrl(EzApp.sharedPref.getString(
					Constants.SIGNATURE, ""), DashboardActivity.imgLoader,
					(ImageView) dialogPrintPrescription
							.findViewById(R.id.img_signature));
		}

		dialogPrintPrescription.setCancelable(false);
		dialogPrintPrescription.findViewById(R.id.txt_close)
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						dialogPrintPrescription.dismiss();

					}
				});
		dialogPrintPrescription.findViewById(R.id.btn_print_prescription)
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						PrintHelper photoPrinter = new PrintHelper(
								AddDentistNotesActivity.this);

						photoPrinter.setScaleMode(PrintHelper.SCALE_MODE_FIT);
						View view = (dialogPrintPrescription
								.findViewById(R.id.rl_top));
						view.setDrawingCacheEnabled(true);
						view.buildDrawingCache();
						Bitmap bm = view.getDrawingCache();
						// Bitmap bitmap =
						// BitmapFactory.decodeResource(getResources(),
						// R.drawable.rs_15);

						photoPrinter.printBitmap("Prescription.jpg", bm);

						// dialogPrintRadiology.dismiss();

					}
				});
		dialogPrintPrescription.show();

	}

	private void dialogPrintRadiology() {
		final Dialog dialogPrintRadiology = new Dialog(this,
				R.style.Theme_AppCompat_Light);
		dialogPrintRadiology.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialogPrintRadiology.setContentView(R.layout.dialog_print_radiology);
		dialogPrintRadiology.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		final SimpleDateFormat df = new SimpleDateFormat(" EEE, MMM dd, yyyy");
		final Date date = appointment.aptDate;
		String drName = EzApp.sharedPref.getString(
				Constants.DR_NAME, "");
		((TextView) dialogPrintRadiology.findViewById(R.id.txt_drname))
				.setText(drName);
		String drAddress = EzApp.sharedPref.getString(
				Constants.DR_ADDRESS, "");
		((TextView) dialogPrintRadiology.findViewById(R.id.txt_doc_address))
				.setText(drAddress + " - "
						+ EditAccountActivity.profile.getZip());
		((TextView) dialogPrintRadiology.findViewById(R.id.txt_ptname))
				.setText("Patient Name: " + patientModel.getPfn() + " "
						+ patientModel.getPln());
		((TextView) dialogPrintRadiology.findViewById(R.id.txt_gender))
				.setText("Gender: " + patientModel.getPgender());
		((TextView) dialogPrintRadiology.findViewById(R.id.txt_age))
				.setText("Age: " + patientModel.getPage());
		((TextView) dialogPrintRadiology.findViewById(R.id.txt_date))
				.setText("Date: " + df.format(date));
		((TextView) dialogPrintRadiology.findViewById(R.id.txt_ptaddress))
				.setText("Address: " + patientModel.getPadd1() + " "
						+ patientModel.getPadd2() + ", "
						+ patientModel.getParea() + ", "
						+ patientModel.getPcity() + ", "
						+ patientModel.getPstate() + ", "
						+ patientModel.getPcountry() + " - "
						+ patientModel.getPzip());
		((TextView) dialogPrintRadiology
				.findViewById(R.id.txt_radiology_orderd))
				.setText(Html.fromHtml("<b>Radiology Ordered:</b> "
						+ visitModel.dentistPlanModel.radiology.getLabString()));

		// PatientController.patientBarcode(AddDentistNotesActivity.this,
		// new OnResponseData() {
		//
		// @Override
		// public void onResponseListner(Object response) {
		//
		// Util.getImageFromUrl(response.toString(),
		// DashboardActivity.imgLoader,
		// (ImageView) dialogPrintRadiology
		// .findViewById(R.id.img_barcode));
		// }
		// }, patientModel);

		if (!Util.isEmptyString(EzApp.sharedPref.getString(
				Constants.SIGNATURE, "signature"))) {
			Util.getImageFromUrl(EzApp.sharedPref.getString(
					Constants.SIGNATURE, ""), DashboardActivity.imgLoader,
					(ImageView) dialogPrintRadiology
							.findViewById(R.id.img_lab_signature));
		}
		dialogPrintRadiology.setCancelable(false);
		dialogPrintRadiology.findViewById(R.id.txt_close).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {

						dialogPrintRadiology.dismiss();

					}
				});
		dialogPrintRadiology.findViewById(R.id.btn_print_radiology)
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						PrintHelper photoPrinter = new PrintHelper(
								AddDentistNotesActivity.this);

						photoPrinter.setScaleMode(PrintHelper.SCALE_MODE_FIT);
						View view = (dialogPrintRadiology
								.findViewById(R.id.rl_top));
						view.setDrawingCacheEnabled(true);
						view.buildDrawingCache();
						Bitmap bm = view.getDrawingCache();
						// Bitmap bitmap =
						// BitmapFactory.decodeResource(getResources(),
						// R.drawable.rs_15);

						photoPrinter.printBitmap("Radiology.jpg", bm);

						// dialogPrintRadiology.dismiss();

					}
				});
		dialogPrintRadiology.show();

	}

	private void dialogPrintLabOrdered() {
		final Dialog dialogPrintLabordered = new Dialog(this,
				R.style.Theme_AppCompat_Light);
		dialogPrintLabordered.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialogPrintLabordered.setContentView(R.layout.dialog_print_lab);
		dialogPrintLabordered.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		final SimpleDateFormat df = new SimpleDateFormat(" EEE, MMM dd, yyyy");
		final Date date = appointment.aptDate;
		String drName = EzApp.sharedPref.getString(
				Constants.DR_NAME, "");
		((TextView) dialogPrintLabordered.findViewById(R.id.txt_drname))
				.setText(drName);
		String drAddress = EzApp.sharedPref.getString(
				Constants.DR_ADDRESS, "");
		((TextView) dialogPrintLabordered.findViewById(R.id.txt_doc_address))
				.setText(drAddress + " - "
						+ EditAccountActivity.profile.getZip());
		((TextView) dialogPrintLabordered.findViewById(R.id.txt_ptname))
				.setText("Patient Name: " + patientModel.getPfn() + " "
						+ patientModel.getPln());
		((TextView) dialogPrintLabordered.findViewById(R.id.txt_gender))
				.setText("Gender: " + patientModel.getPgender());
		((TextView) dialogPrintLabordered.findViewById(R.id.txt_age))
				.setText("Age: " + patientModel.getPage());
		((TextView) dialogPrintLabordered.findViewById(R.id.txt_date))
				.setText("Date: " + df.format(date));
		((TextView) dialogPrintLabordered.findViewById(R.id.txt_ptaddress))
				.setText("Address: " + patientModel.getPadd1() + " "
						+ patientModel.getPadd2() + ", "
						+ patientModel.getParea() + ", "
						+ patientModel.getPcity() + ", "
						+ patientModel.getPstate() + ", "
						+ patientModel.getPcountry() + " - "
						+ patientModel.getPzip());
		((TextView) dialogPrintLabordered.findViewById(R.id.txt_lab_orderd))
				.setText(Html.fromHtml("<b>Lab Ordered:</b> "
						+ visitModel.dentistPlanModel.lab.getLabString()));

		if (!Util.isEmptyString(EzApp.sharedPref.getString(
				Constants.SIGNATURE, "signature"))) {
			Util.getImageFromUrl(EzApp.sharedPref.getString(
					Constants.SIGNATURE, ""), DashboardActivity.imgLoader,
					(ImageView) dialogPrintLabordered
							.findViewById(R.id.img_lab_signature_1));
		}
		dialogPrintLabordered.setCancelable(false);
		dialogPrintLabordered.findViewById(R.id.txt_close).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {

						dialogPrintLabordered.dismiss();

					}
				});
		dialogPrintLabordered.findViewById(R.id.btn_print_lab)
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						PrintHelper photoPrinter = new PrintHelper(
								AddDentistNotesActivity.this);

						photoPrinter.setScaleMode(PrintHelper.SCALE_MODE_FIT);
						View view = (dialogPrintLabordered
								.findViewById(R.id.rl_top));
						view.setDrawingCacheEnabled(true);
						view.buildDrawingCache();
						Bitmap bm = view.getDrawingCache();
						// Bitmap bitmap =
						// BitmapFactory.decodeResource(getResources(),
						// R.drawable.rs_15);

						photoPrinter.printBitmap("Lab.jpg", bm);

						// dialogPrintLabordered.dismiss();

					}
				});
		dialogPrintLabordered.show();

	}

	Boolean flag;

	public void dialogTooth() {
		Log.i("",
				visitModel.dentistExaminationModel.oralExamination.hardTissue.tp);
		final Dialog dialogTooth = new Dialog(this,
				R.style.Theme_AppCompat_Light);
		dialogTooth.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialogTooth.setContentView(R.layout.dialog_hard_tissue);
		((CheckBox) dialogTooth.findViewById(R.id.cb_merge))
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(
							final CompoundButton buttonView,
							final boolean isChecked) {
						if (isChecked) {
							visitModel.dentistExaminationModel.oralExamination.hardTissue
									.getMergeData(appointment,
											AddDentistNotesActivity.this,
											dialogTooth);
						} else {
							((EditText) dialogTooth
									.findViewById(R.id.edit_others))
									.setText("");
							for (TeethModel tm : visitModel.dentistExaminationModel.oralExamination.hardTissue.arrTeeth) {
								tm.arrTeethState.clear();
								tm.hashState.clear();
							}
							refreshToothDialog(dialogTooth);
						}

					}
				});
		final GridView listHardTissue = (GridView) dialogTooth
				.findViewById(R.id.list_hard_tissue);
		final TeethView view = (TeethView) dialogTooth
				.findViewById(R.id.toothimage);
		final RadioGroup rg = ((RadioGroup) dialogTooth
				.findViewById(R.id.rg_tp));
		flag = false;
		EditUtils
				.autoSaveEditText(
						(EditText) dialogTooth.findViewById(R.id.edit_note),
						visitModel.dentistExaminationModel.oralExamination.hardTissue.hashNote);
		if (!Util
				.isEmptyString(visitModel.dentistExaminationModel.oralExamination.hardTissue.tp)) {
			((RadioButton) rg
					.findViewWithTag(visitModel.dentistExaminationModel.oralExamination.hardTissue.tp))
					.setChecked(true);
		}
		if (hashFollowUp.size() > 0) {
			dialogTooth.findViewById(R.id.cb_merge).setVisibility(View.VISIBLE);
		} else {
			dialogTooth.findViewById(R.id.cb_merge).setVisibility(View.GONE);
		}
		rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(final RadioGroup group,
					final int checkedId) {
				visitModel.dentistExaminationModel.oralExamination.hardTissue.tp = ((RadioButton) dialogTooth
						.findViewById(checkedId)).getText().toString();

			}
		});
		setToothImage(view);
		for (int i = 1; i < 5; i++) {
			for (int j = 1; j < 9; j++) {
				final TeethModel teeth = arrTeeth.get((j - 1) + ((i - 1) * 8));
				final ImageView img = (ImageView) view.findViewWithTag("tooth"
						+ i + "" + j);
				if (view.findViewWithTag("tooth" + i + "" + j) != null) {
					img.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(final View v) {
							if (flag) {
								String s = ((EditText) dialogTooth
										.findViewById(R.id.edit_others))
										.getText().toString();
								prevTeeth.arrTeethState
										.remove(prevTeeth.hashState.get("othe"));
								prevTeeth.hashState.put("othe", s);
								if (!Util.isEmptyString(s))
									prevTeeth.arrTeethState.add(s);
							}
							flag = true;
							final RadioGroup rgToothSelection = (RadioGroup) dialogTooth
									.findViewById(R.id.rg_teeth_selection);
							final RadioButton rb1 = (RadioButton) rgToothSelection
									.getChildAt(0);
							final RadioButton rb2 = (RadioButton) rgToothSelection
									.getChildAt(1);
							if (visitModel.dentistExaminationModel.oralExamination.hardTissue.tp
									.contains("Permanent")) {
								rgToothSelection.check(rb1.getId());
							} else {
								rgToothSelection.check(rb2.getId());
							}
							final TextView txtTooth = ((TextView) dialogTooth
									.findViewById(R.id.txt_tooth_name));
							if (teeth.name.equals("tooth11")) {
								alternateTooth(txtTooth, rgToothSelection,
										listHardTissue, img, teeth, 32,
										dialogTooth, view);
								rb1.setText("Tooth11");
								rb2.setText("Tooth51");
							} else if (teeth.name.equals("tooth12")) {
								alternateTooth(txtTooth, rgToothSelection,
										listHardTissue, img, teeth, 33,
										dialogTooth, view);
								rb1.setText("Tooth12");
								rb2.setText("Tooth52");
							} else if (teeth.name.equals("tooth13")) {
								alternateTooth(txtTooth, rgToothSelection,
										listHardTissue, img, teeth, 34,
										dialogTooth, view);
								rb1.setText("Tooth13");
								rb2.setText("Tooth53");
							} else if (teeth.name.equals("tooth16")) {
								alternateTooth(txtTooth, rgToothSelection,
										listHardTissue, img, teeth, 35,
										dialogTooth, view);
								rb1.setText("Tooth16");
								rb2.setText("Tooth54");
							} else if (teeth.name.equals("tooth17")) {
								alternateTooth(txtTooth, rgToothSelection,
										listHardTissue, img, teeth, 36,
										dialogTooth, view);
								rb1.setText("Tooth17");
								rb2.setText("Tooth55");
							} else if (teeth.name.equals("tooth21")) {
								alternateTooth(txtTooth, rgToothSelection,
										listHardTissue, img, teeth, 37,
										dialogTooth, view);
								rb1.setText("Tooth21");
								rb2.setText("Tooth61");
							} else if (teeth.name.equals("tooth22")) {
								alternateTooth(txtTooth, rgToothSelection,
										listHardTissue, img, teeth, 38,
										dialogTooth, view);
								rb1.setText("Tooth22");
								rb2.setText("Tooth62");
							} else if (teeth.name.equals("tooth23")) {
								alternateTooth(txtTooth, rgToothSelection,
										listHardTissue, img, teeth, 39,
										dialogTooth, view);
								rb1.setText("Tooth23");
								rb2.setText("Tooth63");
							} else if (teeth.name.equals("tooth26")) {
								alternateTooth(txtTooth, rgToothSelection,
										listHardTissue, img, teeth, 40,
										dialogTooth, view);
								rb1.setText("Tooth26");
								rb2.setText("Tooth64");
							} else if (teeth.name.equals("tooth27")) {
								alternateTooth(txtTooth, rgToothSelection,
										listHardTissue, img, teeth, 41,
										dialogTooth, view);
								rb1.setText("Tooth27");
								rb2.setText("Tooth65");
							} else if (teeth.name.equals("tooth31")) {
								alternateTooth(txtTooth, rgToothSelection,
										listHardTissue, img, teeth, 42,
										dialogTooth, view);
								rb1.setText("Tooth31");
								rb2.setText("Tooth71");
							} else if (teeth.name.equals("tooth32")) {
								alternateTooth(txtTooth, rgToothSelection,
										listHardTissue, img, teeth, 43,
										dialogTooth, view);
								rb1.setText("Tooth32");
								rb2.setText("Tooth72");
							} else if (teeth.name.equals("tooth33")) {
								alternateTooth(txtTooth, rgToothSelection,
										listHardTissue, img, teeth, 44,
										dialogTooth, view);
								rb1.setText("Tooth33");
								rb2.setText("Tooth73");
							} else if (teeth.name.equals("tooth36")) {
								alternateTooth(txtTooth, rgToothSelection,
										listHardTissue, img, teeth, 45,
										dialogTooth, view);
								rb1.setText("Tooth36");
								rb2.setText("Tooth74");
							} else if (teeth.name.equals("tooth37")) {
								alternateTooth(txtTooth, rgToothSelection,
										listHardTissue, img, teeth, 46,
										dialogTooth, view);
								rb1.setText("Tooth37");
								rb2.setText("Tooth75");
							} else if (teeth.name.equals("tooth41")) {
								alternateTooth(txtTooth, rgToothSelection,
										listHardTissue, img, teeth, 47,
										dialogTooth, view);
								rb1.setText("Tooth41");
								rb2.setText("Tooth81");
							} else if (teeth.name.equals("tooth42")) {
								alternateTooth(txtTooth, rgToothSelection,
										listHardTissue, img, teeth, 48,
										dialogTooth, view);
								rb1.setText("Tooth42");
								rb2.setText("Tooth82");
							} else if (teeth.name.equals("tooth43")) {
								alternateTooth(txtTooth, rgToothSelection,
										listHardTissue, img, teeth, 49,
										dialogTooth, view);
								rb1.setText("Tooth43");
								rb2.setText("Tooth83");
							} else if (teeth.name.equals("tooth46")) {
								alternateTooth(txtTooth, rgToothSelection,
										listHardTissue, img, teeth, 50,
										dialogTooth, view);
								rb1.setText("Tooth46");
								rb2.setText("Tooth84");
							} else if (teeth.name.equals("tooth47")) {
								alternateTooth(txtTooth, rgToothSelection,
										listHardTissue, img, teeth, 51,
										dialogTooth, view);
								rb1.setText("Tooth47");
								rb2.setText("Tooth85");
							} else {
								txtTooth.setText(teeth.name.replace("tooth",
										"Tooth"));
								rgToothSelection.setVisibility(View.GONE);
								itemsChecked(listHardTissue, img, teeth, view);
								((EditText) dialogTooth
										.findViewById(R.id.edit_others))
										.setText(teeth.hashState.get("othe"));
								prevTeeth = teeth;
							}
							listHardTissue.setVisibility(View.VISIBLE);
							dialogTooth.findViewById(R.id.edit_others)
									.setVisibility(View.VISIBLE);
							for (int i = 1; i < 5; i++) {
								for (int j = 1; j < 9; j++) {
									final TeethModel teeth1 = arrTeeth
											.get((j - 1) + ((i - 1) * 8));
									final ImageView img1 = (ImageView) view
											.findViewWithTag("tooth" + i + ""
													+ j);
									img1.setBackgroundColor(Color
											.parseColor("#ffffff"));
									if ((teeth1.arrTeethState.size() > 0)
											|| (teeth1.arrTeethTreatmentPlan
													.size() > 0)
											|| !Util.isEmptyString(teeth1.hashState
													.get("othe"))) {
										img1.setBackgroundColor(Color
												.parseColor("#77EA2B7F"));
									}
								}
							}

							for (int i = 5; i < 9; i++) {
								for (int j = 1; j < 6; j++) {
									final TeethModel teeth1 = arrTeeth
											.get(((32 + j) - 1) + ((i - 5) * 5));
									// teeth1.arrTeethState =
									// patientModel.masterPlan.arrTeeth
									// .get(((32 + j) - 1) + ((i - 5) *
									// 5)).arrTeethState;
									ImageView img1;
									if (j < 4) {
										img1 = (ImageView) view
												.findViewWithTag("tooth"
														+ (i - 4) + "" + j);
									} else {
										img1 = (ImageView) view
												.findViewWithTag("tooth"
														+ (i - 4) + ""
														+ (j + 2));
									}
									// img1.setBackgroundColor(Color
									// .parseColor("#ffffff"));
									if ((teeth1.arrTeethState.size() > 0)
											|| (teeth1.arrTeethTreatmentPlan
													.size() > 0)
											|| !Util.isEmptyString(teeth1.hashState
													.get("othe"))) {
										img1.setBackgroundColor(Color
												.parseColor("#77EA2B7F"));
									}
								}
							}

							img.setBackgroundColor(Color
									.parseColor("#77aabbcc"));

						}
					});
				}
			}
		}
		for (int i = 5; i < 9; i++) {
			for (int j = 1; j < 6; j++) {
				final TeethModel teeth1 = arrTeeth.get(((32 + j) - 1)
						+ ((i - 5) * 5));
				ImageView img1;
				if (j < 4) {
					img1 = (ImageView) view.findViewWithTag("tooth" + (i - 4)
							+ "" + j);
				} else {
					img1 = (ImageView) view.findViewWithTag("tooth" + (i - 4)
							+ "" + (j + 2));
				}
				// img1.setBackgroundColor(Color.parseColor("#ffffff"));
				if ((teeth1.arrTeethState.size() > 0)
						|| (teeth1.arrTeethTreatmentPlan.size() > 0)) {
					img1.setBackgroundColor(Color.parseColor("#77EA2B7F"));
				}
			}
		}
		dialogTooth.findViewById(R.id.txt_close).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(final View v) {
						dialogTooth.dismiss();
					}
				});
		dialogTooth.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss(final DialogInterface dialog) {
				String s = ((EditText) dialogTooth
						.findViewById(R.id.edit_others)).getText().toString();
				prevTeeth.arrTeethState.remove(prevTeeth.hashState.get("othe"));
				prevTeeth.hashState.put("othe", s);
				if (!Util.isEmptyString(s))
					prevTeeth.arrTeethState.add(s);
				onResume();
			}
		});
		dialogTooth.show();
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
		// for (PatientShow p : DashboardActivity.arrPatientShow) {
		// if (p.getPId().equalsIgnoreCase(appointment.getPid())
		// && p.getPfId().equalsIgnoreCase(appointment.getPfId()))
		//
		// txtPatientName.setText(p.getPfn() + " " + p.getPln() + " , "
		// + p.getAge() + "/" + p.getGender());
		// }

	}

	private void getData() {

		final String url = APIs.SOAP_SHOW() + appointment.getSiid() + "/bk_id/"
				+ appointment.getBkid();
		Log.i("url", url);
		final Dialog loaddialog = Util
				.showLoadDialog(AddDentistNotesActivity.this);
		final StringRequest patientListRequest = new StringRequest(
				Request.Method.POST, url, new Response.Listener<String>() {
					@Override
					public void onResponse(final String response) {
						try {
							JSONObject jObj = new JSONObject(response);
							if (jObj.getString("s").equals("200")) {
								Log.i("tag", response);
								if (jObj.getJSONObject("data").has(
										"followup_appointment"))
									txtFollowup.setText(jObj.getJSONObject(
											"data").getString(
											"followup_appointment"));
								arrallergies.clear();
								if (jObj.getJSONObject("data").has("mp_ep_id")) {
									masterPlan.mp_ep_id = jObj.getJSONObject(
											"data").getString("mp_ep_id");
									appointment.setEpid(jObj.getJSONObject(
											"data").getString("mp_ep_id"));
								}
								if (jObj.getJSONObject("data").has("allergies")) {
									JSONArray arr = jObj.getJSONObject("data")
											.getJSONArray("allergies");
									for (int i = 0; i < arr.length(); i++) {
										AllergiesModel allergy = new Gson()
												.fromJson(
														arr.get(i).toString(),
														AllergiesModel.class);
										arrallergies.add(allergy);
									}
								}
								if (jObj.getJSONObject("data").has("documents")) {
									JSONArray arr = jObj.getJSONObject("data")
											.getJSONArray("documents");
									for (int i = 0; i < arr.length(); i++) {
										Document doc = new Gson().fromJson(arr
												.get(i).toString(),
												Document.class);
										arrDocuments.add(doc);
									}
									radiologyTable();
									labTable();

								}
								if (jObj.getJSONObject("data").has("p-info")) {
									String s = jObj.getJSONObject("data")
											.getString("p-info");
									PatInfo pat = new Gson().fromJson(s,
											PatInfo.class);
									appointment.setFollowid(pat.getFollowupId());
									txtPatientName.setText(pat.getPName()
											+ " , " + pat.getAge() + "/"
											+ pat.getGender().charAt(0));
								}
								if (!appointment.getVisit().equals("1")) {
									pastFollowUp();
								} else {
									findViewById(R.id.btn_past_subjective)
											.setVisibility(View.GONE);
									findViewById(R.id.btn_past_diagnosis)
											.setVisibility(View.GONE);
									findViewById(R.id.btn_past_plan)
											.setVisibility(View.GONE);
									findViewById(R.id.btn_past_examination)
											.setVisibility(View.GONE);
								}

								txtAllergy = (TextView) findViewById(R.id.txt_allergy_top);
								txtAllergy.setText(Html
										.fromHtml("<b>Allergy :</b> "));
								txtAllergy.setVisibility(View.GONE);
								for (final AllergiesModel allergy : arrallergies) {
									if (!Util.isEmptyString(allergy
											.getMCatName())
											|| !Util.isEmptyString(allergy
													.getSCatName())) {
										txtAllergy.setVisibility(View.VISIBLE);
										if (!Util.isEmptyString(allergy
												.getAddiInfo())) {
											txtAllergy.append(allergy
													.getMCatName()
													+ " -->"
													+ allergy.getSCatName()
													+ "("
													+ allergy.getAddiInfo()
													+ "), ");
										} else {
											txtAllergy.append(allergy
													.getMCatName()
													+ " -->"
													+ allergy.getSCatName()
													+ ", ");
										}
									} else {
										txtAllergy.setText("");
									}
								}
								getPastVisits();
								getReferPatient();
								onResume();
								prescriptionTable();
								// List<SoapNote> arrSoap =
								// EzHealthApplication.soapNoteDao
								// .queryBuilder()
								// .where(Properties.Id.eq((long) Integer
								// .parseInt(appointment.getBkid())))
								// .list();
								// SimpleDateFormat sdf = new SimpleDateFormat(
								// "yyyy-MM-dd hh:mm:ss");
								// Date updated = new Date();
								// try {
								// updated = sdf.parse(jObj
								// .getJSONObject("data")
								// .getJSONObject("tmpl-inst")
								// .getString("date_updated"));
								// Log.i(jObj.getJSONObject("data")
								// .getJSONObject("tmpl-inst")
								// .getString("date_updated"),
								// jObj.getString("current_time"));
								// } catch (ParseException e1) {
								// // TODO Auto-generated catch block
								// Log.e("", e1);
								// }
								final JSONObject soap;
								// if (arrSoap.size() == 0
								// || arrSoap.get(0).getDate()
								// .before(updated)) {
								if (true) {
									Log.i("", "soap is loading from server");
									if (Util.isEmptyString(appointment
											.getSiid())) {
										soap = visitModel.jsonData
												.getJSONObject("Soap");
										soap.getJSONObject("subj").put(
												"hp",
												jObj.getJSONObject("data")
														.getJSONObject(
																"copy_tmpl")
														.getJSONObject("subj")
														.getJSONObject("hp"));
										soap.getJSONObject("subj")
												.getJSONObject("cc")
												.put("value",
														appointment.getReason());
										if (soap.has("subj"))
											visitModel.dentistSubjectiveModel.JsonParse(soap
													.getJSONObject("subj"));
										visitModel.dentistPlanModel = new PlanModel();
										AutoSave();
									} else {
										soap = jObj.getJSONObject("data")
												.getJSONObject("tmpl-inst")
												.getJSONObject("Soap");
										visitModel.jsonData = jObj
												.getJSONObject("data")
												.getJSONObject("tmpl-inst");
									}
									if (soap.has("subj"))
										visitModel.dentistSubjectiveModel
												.JsonParse(soap
														.getJSONObject("subj"));
									if (soap.has("obje"))
										visitModel.dentistExaminationModel
												.JsonParse(soap
														.getJSONObject("obje"));
									if (soap.has("asse"))
										visitModel.dentistDiagnosisModel
												.JsonParse(soap
														.getJSONObject("asse"),
														actvFinal, actvProv);
									if (soap.has("plan"))
										visitModel.dentistPlanModel.JsonParse(
												soap.getJSONObject("plan"),
												arrTeeth);
									try {
										if (soap.has("private-note"))
											visitModel.privateNote.jsonParse(soap
													.getJSONObject("private-note"));
										for (final Map.Entry<String, String> entry : visitModel.dentistSubjectiveModel.hashHP
												.entrySet()) {
											if (!Util.isEmptyString(entry
													.getValue())) {
												findViewById(R.id.img_hp_tick)
														.setVisibility(
																View.VISIBLE);
											}
										}
									} catch (Exception e) {

									}
									getMasterPlanData(response);
									AutoSave();
								}

							} else {

							}
						} catch (final JSONException e) {
							// Util.Alertdialog(AddDentistNotesActivity.this,
							// "Network Error, try again later");
							Log.e("", e);
						}
						loaddialog.dismiss();
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(final VolleyError error) {
						Util.Alertdialog(AddDentistNotesActivity.this,
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
				loginParams.put("tenant_id", EzApp.sharedPref
						.getString(Constants.TENANT_ID, ""));
				loginParams.put("branch_id", EzApp.sharedPref
						.getString(Constants.USER_BRANCH_ID, ""));
				return loginParams;
			}

		};
		patientListRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 5,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		EzApp.mVolleyQueue.add(patientListRequest);

	}

	private void getMasterPlanData(String response) {
		JSONObject jObj;
		try {
			jObj = new JSONObject(response);
			if (jObj.getJSONObject("data").has("masterPlan")
					&& !Util.isEmptyString(jObj.getJSONObject("data")
							.getJSONObject("masterPlan").getString("metadata"))) {
				masterPlan.jsonData = new JSONObject(jObj.getJSONObject("data")
						.getJSONObject("masterPlan").getString("metadata"));
				masterPlan.JsonParse(masterPlan.jsonData.getJSONObject("mast"));
			}
			Log.i("tooth18",
					new Gson().toJson(masterPlan.arrTeeth.get(7).arrTeethTreatmentPlan));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void getPastVisits() {

		final String url = APIs.PAST_VISIT();
		final Dialog loaddialog = Util
				.showLoadDialog(AddDentistNotesActivity.this);
		final StringRequest patientListRequest = new StringRequest(
				Request.Method.POST, url, new Response.Listener<String>() {
					@Override
					public void onResponse(final String response) {
						try {
							// Log.i("past visits", response);
							final JSONObject jObj = new JSONObject(response);
							if (jObj.getString("s").equals("200")) {
								try {
									for (int i = 0; i < jObj.getJSONArray(
											"data").length(); i++) {
										final JSONObject jsonVisit = jObj
												.getJSONArray("data")
												.getJSONObject(i);
										final PastVisitModel pastVisit = new PastVisitModel(
												jsonVisit);
										AddDentistNotesActivity.arrPastVisit
												.add(pastVisit);
										if (jObj.getJSONArray("data").length() > 0) {
											findViewById(R.id.btn_past_visit)
													.setVisibility(View.VISIBLE);
										} else {
											findViewById(R.id.btn_past_visit)
													.setVisibility(View.GONE);
										}
									}
								} catch (final JSONException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}

							} else {

							}
						} catch (final JSONException e) {
							// Util.Alertdialog(AddDentistNotesActivity.this,
							// "Network Error, try again later");
							Log.e("", e);
						}
						loaddialog.dismiss();
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(final VolleyError error) {
						Util.Alertdialog(AddDentistNotesActivity.this,
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
				loginParams.put("pid", appointment.getPid());
				loginParams.put("fid", appointment.getPfId());
				loginParams.put("format", "json");
				loginParams.put("bk-id", appointment.getBkid());
				loginParams.put("follow-id", appointment.getFollowid());
				// Log.i("Login Params", loginParams.toString());
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
				.showLoadDialog(AddDentistNotesActivity.this);
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
								for (int i = 0; i < jsonReferTo.length(); i++) {
									final ReferToModel referTo = new ReferToModel();
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
									referTo.refer_date = date;
									referTo.refer_to_id = jsonReferTo
											.getJSONObject(i).getString(
													"refer-to-id");
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
							Util.Alertdialog(AddDentistNotesActivity.this,
									"There is some error while fetching data, please try again");
							Log.e("", e);
						}
						loaddialog.dismiss();
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(final VolleyError error) {
						Util.Alertdialog(AddDentistNotesActivity.this,
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
				loginParams.put("ep-id", appointment.getEpid());
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
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(final CharSequence s,
					final int start, final int count, final int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onTextChanged(CharSequence s, final int start,
					final int before, final int count) {
				final String newText = s.toString();
				if (str.equals("pd")) {
					visitNotes.dentistDiagnosisModel.pd = newText;
				} else if (str.equals("fd")) {
					visitNotes.dentistDiagnosisModel.fd = newText;
				}
				s = s.toString().replaceAll(",,", ",");
				final String[] parts = newText.split(",");
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
						Log.i("tag", "" + item.getDiag_desc());
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
					return text;
				} else {
					if (text instanceof Spanned) {
						final SpannableString sp = new SpannableString(text);
						android.text.TextUtils.copySpansFrom((Spanned) text, 0,
								text.length(), Object.class, sp, 0);
						return sp;
					} else {
						return text;
					}
				}
			}
		};

	}

	private void itemsChecked(final GridView listHardTissue,
			final ImageView img, final TeethModel teeth, final View view) {
		final ArrayList<String> arrHardTissue = new ArrayList<String>();
		arrHardTissue.add("Missing");
		arrHardTissue.add("Mobile");
		arrHardTissue.add("Carious");
		arrHardTissue.add("Grossly Carious");
		arrHardTissue.add("Restoration");
		arrHardTissue.add("Fracture");
		arrHardTissue.add("Root Stump");
		arrHardTissue.add("Root Canal Treatment");
		arrHardTissue.add("Periodontal Pocket");
		arrHardTissue.add("Stains");
		arrHardTissue.add("Calculus");
		arrHardTissue.add("Existing Prosthesis");
		final CheckedListAdapter adapterHardTissue = new CheckedListAdapter(
				this, R.layout.row_checked_item, arrHardTissue,
				teeth.arrTeethState);
		listHardTissue.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
		listHardTissue.setAdapter(adapterHardTissue);
		adapterHardTissue.notifyDataSetChanged();
		adapterHardTissue.registerDataSetObserver(new DataSetObserver() {
			@Override
			public void onChanged() {
				setToothImage(view);
			}
		});
	}

	@Override
	protected void onActivityResult(final int requestCode,
			final int resultCode, final Intent data) {

		if (requestCode == 1) {

			if (resultCode == Activity.RESULT_OK) {
				txtVitals
						.setText(visitModel.getVitalsModel().getVitalsString());

			} else if (resultCode == Activity.RESULT_CANCELED) {

			}
		}
	}

	@Override
	public void onClick(final View v) {
		Intent intent;
		switch (v.getId()) {

		case R.id.btn_followup:
			try {
				DashboardActivity.arrScheduledPatients.add(appointment);
				appointment.setType("followup");
				intent = new Intent(this, SheduleActivity.class);
				intent.putExtra(
						"pos",
						""
								+ DashboardActivity.arrScheduledPatients
										.lastIndexOf(appointment));
				intent.putExtra("pid", appointment.getPid());
				intent.putExtra("fid", appointment.getPfId());
				intent.putExtra("type", "follow_up");
				intent.putExtra("from", "history");
				startActivity(intent);
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
												AddDentistNotesActivity.this,
												ReferDentistNotesActivity.class);
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
												AddDentistNotesActivity.this,
												ReferDentistNotesActivity.class);
										intent.putExtra("epid",
												referTo.refer_ep_id);
										startActivity(intent);
										break;
									}

								}
								return true;
							}
						});
				popupReferTo.show();

			} catch (Exception e) {

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
				}
				// registering popup with OnMenuItemClickListener
				popupSubjective
						.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
							@Override
							public boolean onMenuItemClick(final MenuItem item) {
								for (final Entry entry : hashFollowUp
										.entrySet()) {
									if (item.getTitle()
											.equals(entry.getValue())) {
										dialogSubjective((String) entry
												.getKey());
									}
								}

								return true;
							}
						});
				popupSubjective.show();

			} catch (Exception e) {

			}
			break;

		case R.id.btn_past_examination:
			try {
				final PopupMenu popupExamination = new PopupMenu(this,
						findViewById(R.id.btn_past_examination));
				// Inflating the Popup using xml file
				// popup.getMenuInflater().inflate(R.menu.shedule,
				// popup.getMenu());
				for (final Entry entry : hashFollowUp.entrySet()) {
					popupExamination.getMenu().add(
							(CharSequence) entry.getValue());
				}
				// registering popup with OnMenuItemClickListener
				popupExamination
						.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
							@Override
							public boolean onMenuItemClick(final MenuItem item) {
								for (final Entry entry : hashFollowUp
										.entrySet()) {
									if (item.getTitle()
											.equals(entry.getValue())) {
										dialogExamination((String) entry
												.getKey());
									}
								}

								return true;
							}
						});
				popupExamination.show();

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
					popupDiagnosis.getMenu().add(
							(CharSequence) entry.getValue());
				}
				// registering popup with OnMenuItemClickListener
				popupDiagnosis
						.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
							@Override
							public boolean onMenuItemClick(final MenuItem item) {
								for (final Entry entry : hashFollowUp
										.entrySet()) {
									if (item.getTitle()
											.equals(entry.getValue())) {
										dialogDiagnosis((String) entry.getKey());
									}
								}

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
					popupPlan.getMenu().add((CharSequence) entry.getValue());
				}
				// registering popup with OnMenuItemClickListener
				popupPlan
						.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
							@Override
							public boolean onMenuItemClick(final MenuItem item) {
								for (final Entry entry : hashFollowUp
										.entrySet()) {
									if (item.getTitle()
											.equals(entry.getValue())) {
										dialogPlan((String) entry.getKey());
									}
								}

								return true;
							}
						});
				popupPlan.show();

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
										visitModel.updateJson(arrTeeth, this);

										if (Util.isEmptyString(appointment
												.getSiid())) {
											createSoap();
										} else {
											postData();
										}

										// dialogSubjective();

									} catch (Exception e) {
										e.printStackTrace();
									}

								} else {
									Util.Alertdialog(
											AddDentistNotesActivity.this,
											"Only numeric fields allowed in Alcohol Day(s)");
								}
							} else {
								Util.Alertdialog(AddDentistNotesActivity.this,
										"Only numeric fields allowed in Alcohol Month(s)");
							}
						} else {
							Util.Alertdialog(AddDentistNotesActivity.this,
									"Only numeric fields allowed in Alcohol Year(s)");
						}
					} else {
						Util.Alertdialog(AddDentistNotesActivity.this,
								"Only numeric fields allowed in Smoking Day(s)");
					}
				} else {
					Util.Alertdialog(AddDentistNotesActivity.this,
							"Only numeric fields allowed in Smoking Month(s)");
				}
			} else {
				Util.Alertdialog(AddDentistNotesActivity.this,
						"Only numeric fields allowed in Smoking Year(s)");
			}
			break;

		case R.id.txt_subjective:
			// txtCheifComplaint.setText("Chief Complaint: "
			// + editCheifComplaint.getText());
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

		case R.id.btn_lab:
			try {
				intent = new Intent(this, DentistLabOrderActivity.class);
				intent.putExtra("position",
						getIntent().getIntExtra("position", 0));
				startActivityForResult(intent, 2);

			} catch (Exception e) {

			}
			break;

		case R.id.btn_treatmentplan:
			try {
				if (!Util.isEmptyString(masterPlan.masterPlanType)) {
					intent = new Intent(this,
							DentistTreatmentPlanActivity.class);
				} else {
					intent = new Intent(this,
							DentistTreatmentPlanActivity.class);
				}
				intent.putExtra("pid", appointment.getPid());
				intent.putExtra("position",
						getIntent().getIntExtra("position", 0));
				startActivityForResult(intent, 3);

			} catch (Exception e) {

			}
			break;

		case R.id.txt_master_plan:
			try {
				intent = new Intent(this,
						DentistToothwiseMasterPlanActivity.class);
				intent.putExtra("pid", appointment.getPid());
				intent.putExtra("position",
						getIntent().getIntExtra("position", 0));
				startActivityForResult(intent, 5);

			} catch (Exception e) {

			}
			break;

		case R.id.btn_refer:
			try {
				final Dialog dialog = new Dialog(this);
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog.setContentView(R.layout.dialog_refer);
				dialog.getWindow().setBackgroundDrawable(
						new ColorDrawable(android.graphics.Color.TRANSPARENT));
				for (PatientShow p : DashboardActivity.arrPatientShow) {
					if (p.getPId().equalsIgnoreCase(appointment.getPid())
							&& p.getPfId().equalsIgnoreCase(
									appointment.getPfId()))

						((TextView) dialog.findViewById(R.id.txt_patient))
								.setText(p.getPfn() + " " + p.getPln() + " , "
										+ p.getAge() + "/" + p.getGender());
				}
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
						arrDoctor, sharedPref, appointment.getBkid());
				adapterDoctor
						.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				final ArrayAdapter<String> adapterHospital = new ArrayAdapter<String>(
						this, android.R.layout.simple_spinner_item, arrHospital);
				adapterHospital
						.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
				actvDoctor.setAdapter(adapterDoctor);
				actvDoctor.setThreshold(2);
				dialog.setCancelable(false);
				dialog.findViewById(R.id.txt_close).setOnClickListener(
						new OnClickListener() {
							private Activity context;

							@Override
							public void onClick(View v) {
								try {
									InputMethodManager imm = (InputMethodManager) context
											.getSystemService(Context.INPUT_METHOD_SERVICE);
									imm.hideSoftInputFromWindow(dialog
											.findViewById(R.id.edit_note)
											.getWindowToken(), 0);
								} catch (Exception e) {

								}
								// TODO Auto-generated method stub
								dialog.dismiss();

							}
						});
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
				final Button button = (Button) dialog
						.findViewById(R.id.btn_submit);
				button.setOnClickListener(new OnClickListener() {

					private Context context;

					@Override
					public void onClick(final View v) {

						if (!Util
								.isEmptyString(actvDoctor.getText().toString())) {
							final Patient patientModel = new Patient();
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
								Util.Alertdialog(AddDentistNotesActivity.this,
										"Please select doctor");

							}
						} else {
							Util.Alertdialog(AddDentistNotesActivity.this,
									"Please select doctor");

						}

					}
				});
				dialog.show();

			} catch (Exception e) {

			}
			break;

		case R.id.btn_allergies:
			try {
				final Dialog dialogAllergies = new Dialog(this);
				dialogAllergies.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialogAllergies.setContentView(R.layout.dialog_allergies);
				dialogAllergies.getWindow().setBackgroundDrawable(
						new ColorDrawable(android.graphics.Color.TRANSPARENT));
				for (PatientShow p : DashboardActivity.arrPatientShow) {
					if (p.getPId().equalsIgnoreCase(appointment.getPid())
							&& p.getPfId().equalsIgnoreCase(
									appointment.getPfId()))

						((TextView) dialogAllergies.findViewById(R.id.txt_name))
								.setText(p.getPfn() + " " + p.getPln() + " , "
										+ p.getAge() + "/" + p.getGender());
				}
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
				dialogAllergies.setCancelable(false);
				dialogAllergies.findViewById(R.id.txt_close)
						.setOnClickListener(new OnClickListener() {

							private Activity context;

							@Override
							public void onClick(View v) {
								try {
									InputMethodManager imm = (InputMethodManager) context
											.getSystemService(Context.INPUT_METHOD_SERVICE);
									imm.hideSoftInputFromWindow(dialogAllergies
											.findViewById(R.id.edit_note)
											.getWindowToken(), 0);
								} catch (Exception e) {

								}
								// TODO Auto-generated method stub

								dialogAllergies.dismiss();

							}
						}

						);
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
								// AddDentistNotesActivity.this,
								// "Additional info field cannot be empty.");
								//
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

				dialogAllergies.show();

			} catch (Exception e) {

			}
			break;

		case R.id.txt_hard_tissue_examination:
			try {
				dialogTooth();

			} catch (Exception e) {

			}
			break;

		case R.id.txt_print_radiology:
			try {
				dialogPrintRadiology();

			} catch (Exception e) {

			}
			break;

		case R.id.txt_print_prescription:
			try {
				dialogPrintPrescription();

			} catch (Exception e) {

			}
			break;

		case R.id.txt_print_all:
			try {
				dialogPrintAllInvestigations();

			} catch (Exception e) {
				e.printStackTrace();
			}
			break;

		case R.id.txt_print_laborder:
			try {
				dialogPrintLabOrdered();

			} catch (Exception e) {
				e.printStackTrace();
			}
			break;

		default:
			break;
		}

	}

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_dentist_notes);
		visitNotes = new VisitNotesModel();
		masterPlan = new MasterPlanModel();
		AddDentistNotesActivity.arrPastVisit.clear();
		AddDentistNotesActivity.arrDocuments.clear();
		cbComplete = (CheckBox) findViewById(R.id.cb_complete);
		cbSmoke = (CheckBox) findViewById(R.id.cb_smoke);
		cbAlcohol = (CheckBox) findViewById(R.id.cb_alcohol);
		rlAlcohol = (RelativeLayout) findViewById(R.id.rl_alcohol);
		rlSmoke = (RelativeLayout) findViewById(R.id.rl_smoke);
		sharedPref = getApplicationContext().getSharedPreferences(
				Constants.EZ_SHARED_PREF, Context.MODE_PRIVATE);
		if (getIntent().hasExtra("position")) {
			if (Util.isEmptyString(getIntent().getStringExtra("from")))
				appointment = DashboardActivity.arrConfirmedPatients
						.get(getIntent().getIntExtra("position", 0));
			else
				appointment = DashboardActivity.arrHistoryPatients
						.get(getIntent().getIntExtra("position", 0));
		}
		if (EzApp.patientDao
				.queryBuilder()
				.where(com.ezhealthtrack.greendao.PatientDao.Properties.Pid.eq(appointment
						.getPid()),
						com.ezhealthtrack.greendao.PatientDao.Properties.Fid
								.eq(appointment.getPfId())).count() > 0)
			patientModel = EzApp.patientDao
					.queryBuilder()
					.where(com.ezhealthtrack.greendao.PatientDao.Properties.Pid.eq(appointment
							.getPid()),
							com.ezhealthtrack.greendao.PatientDao.Properties.Fid
									.eq(appointment.getPfId())).list().get(0);
		else
			patientModel = new Patient();
		visitModel = visitNotes;
		arrTeeth = visitModel.dentistExaminationModel.oralExamination.hardTissue.arrTeeth;
		txtPatientName = (TextView) findViewById(R.id.txt_patient_name);
		txtDate = (TextView) findViewById(R.id.txt_date);
		txtVisitCount = (TextView) findViewById(R.id.txt_visit_count);
		txtReason = (TextView) findViewById(R.id.txt_reason);
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
		editCheifComplaint = (EditText) findViewById(R.id.edit_cheif_complaint);
		editCheifComplaint.setText(appointment.getReason());
		txtOralExam = (TextView) findViewById(R.id.txt_oral_exam);
		txtOralExam.setOnClickListener(this);
		txtSoftTissueExam = (TextView) findViewById(R.id.txt_soft_tissue_examination);
		txtSoftTissueExam.setOnClickListener(this);
		txtHardTissueExam = (TextView) findViewById(R.id.txt_hard_tissue_examination);
		txtHardTissueExam.setOnClickListener(this);
		txtFollowup = (TextView) findViewById(R.id.txt_followup_display);
		txtFollowup.setOnClickListener(this);
		llSubjective = findViewById(R.id.ll_subjective_1);
		btnLab = (Button) findViewById(R.id.btn_lab);
		btnLab.setOnClickListener(this);
		btnRefer = (Button) findViewById(R.id.btn_refer);
		btnRefer.setOnClickListener(this);
		btnAllergy = (Button) findViewById(R.id.btn_allergies);
		btnAllergy.setOnClickListener(this);
		btnXray = (Button) findViewById(R.id.btn_xray);
		btnXray.setOnClickListener(this);
		btnPrescription = (Button) findViewById(R.id.btn_prescription);
		btnPrescription.setOnClickListener(this);
		btnTreatmentPlan = (Button) findViewById(R.id.btn_treatmentplan);
		btnTreatmentPlan.setOnClickListener(this);
		findViewById(R.id.btn_submit).setOnClickListener(this);
		findViewById(R.id.btn_past_visit).setOnClickListener(this);
		findViewById(R.id.btn_past_subjective).setOnClickListener(this);
		findViewById(R.id.btn_past_examination).setOnClickListener(this);
		findViewById(R.id.btn_past_diagnosis).setOnClickListener(this);
		findViewById(R.id.btn_past_plan).setOnClickListener(this);
		findViewById(R.id.btn_refer_to).setOnClickListener(this);
		findViewById(R.id.btn_refer_from).setOnClickListener(this);
		findViewById(R.id.txt_print_radiology).setOnClickListener(this);
		findViewById(R.id.txt_print_prescription).setOnClickListener(this);
		findViewById(R.id.txt_print_laborder).setOnClickListener(this);
		findViewById(R.id.btn_followup).setOnClickListener(this);
		findViewById(R.id.txt_print_all).setOnClickListener(this);
		txtMasterPlan = (TextView) findViewById(R.id.txt_master_plan);
		txtMasterPlan.setOnClickListener(this);
		txtRadiology = (TextView) findViewById(R.id.txt_radiology);
		final SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM dd, yyyy");
		final Date date = appointment.aptDate;
		txtDate.setText(sdf.format(date));
		txtVisitCount.setText("No. of visit : " + appointment.getVisit());
		txtReason.setText("Reason : " + appointment.getReason());
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, arrIcd);
		actvProv = (MultiAutoCompleteTextView) findViewById(R.id.actv_provisonal_diagnosis);
		actvProv.setText(visitModel.dentistDiagnosisModel.pd);
		actvProv.setAdapter(adapter);
		actvProv.setTokenizer(getTokenizer());
		actvProv.addTextChangedListener(getTextWatcher("pd"));

		actvFinal = (MultiAutoCompleteTextView) findViewById(R.id.actv_final_diagnosis);
		actvFinal.setText(visitModel.dentistDiagnosisModel.fd);
		actvFinal.setAdapter(adapter);
		actvFinal.setTokenizer(getTokenizer());
		actvFinal.addTextChangedListener(getTextWatcher("fd"));
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

		editYearSmoke = (EditText) findViewById(R.id.edit_year);
		editMonthSmoke = (EditText) findViewById(R.id.edit_Month);
		editDaySmoke = (EditText) findViewById(R.id.edit_day);
		editYearAlcohol = (EditText) findViewById(R.id.edit_year1);
		editMonthAlcohol = (EditText) findViewById(R.id.edit_Month1);
		editDayAlcohol = (EditText) findViewById(R.id.edit_day1);

		getData();
		prescriptionTable();
		// try {
		// List<SoapNote> arrSoap = EzHealthApplication.soapNoteDao
		// .queryBuilder()
		// .where(Properties.Id.eq((long) Integer.parseInt(appointment
		// .getBkid()))).list();
		// Log.i("bk-id", "arr Soap =" + appointment.getBkid());
		//
		// if (arrSoap.size() == 0) {
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
			jObj.put("encounter_id", appointment.getBkid());
			jObj.put("episode_id", appointment.getEpid());
			// Log.i("", Appointment.epId);
			visitModel.jsonData = jObj;
			final JSONObject soap = visitModel.jsonData.getJSONObject("Soap");
			soap.getJSONObject("subj").getJSONObject("cc")
					.put("value", appointment.getReason());
			visitModel.dentistSubjectiveModel.JsonParse(soap
					.getJSONObject("subj"));
			visitModel.dentistExaminationModel.JsonParse(soap
					.getJSONObject("obje"));
			visitModel.dentistDiagnosisModel.JsonParse(
					soap.getJSONObject("asse"), actvFinal, actvProv);
			visitModel.dentistPlanModel.JsonParse(soap.getJSONObject("plan"),
					arrTeeth);
			if (soap.has("private-note")
					&& soap.get("private-note") instanceof JSONObject)
				visitModel.privateNote.jsonParse(soap
						.getJSONObject("private-note"));
			for (final Map.Entry<String, String> entry : visitModel.dentistSubjectiveModel.hashHP
					.entrySet()) {
				if (!Util.isEmptyString(entry.getValue())) {
					findViewById(R.id.img_hp_tick).setVisibility(View.VISIBLE);
				}

			}
			AutoSave();
		} catch (Exception e) {
			e.printStackTrace();
		}
		//
		// } else {
		// visitModel.jsonData = new JSONObject(arrSoap.get(0).getNote());
		// final JSONObject soap = visitModel.jsonData
		// .getJSONObject("Soap");
		//
		// visitModel.dentistSubjectiveModel.JsonParse(soap
		// .getJSONObject("subj"));
		// visitModel.dentistExaminationModel.JsonParse(soap
		// .getJSONObject("obje"));
		// visitModel.dentistDiagnosisModel.JsonParse(
		// soap.getJSONObject("asse"), actvFinal, actvProv);
		// visitModel.dentistPlanModel.JsonParse(
		// soap.getJSONObject("plan"), arrTeeth);
		// if (soap.has("private-note")
		// && soap.get("private-note") instanceof JSONObject)
		// visitModel.privateNote.jsonParse(soap
		// .getJSONObject("private-note"));
		// for (final Map.Entry<String, String> entry :
		// visitModel.dentistSubjectiveModel.hashHP
		// .entrySet()) {
		// if (!Util.isEmptyString(entry.getValue())) {
		// findViewById(R.id.img_hp_tick).setVisibility(
		// View.VISIBLE);
		// }
		// }
		// }
		// } catch (final Exception e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		AutoSave();
		// Log.i("mpId = ", Appointment.mpId);
		if (!Util.isEmptyString(appointment.getMpid())) {
			masterPlan = new MasterPlanModel();
			// getMasterPlanData();
		} else {
			masterPlan = new MasterPlanModel();
			try {
				InputStream fis;
				fis = getAssets().open("mastdatablank.txt");
				final StringBuffer fileContent = new StringBuffer("");
				final byte[] buffer = new byte[1024];
				while (fis.read(buffer) != -1) {
					fileContent.append(new String(buffer));
				}
				final String s = String.valueOf(fileContent);
				final JSONObject jObj = new JSONObject(s);
				jObj.put("context_id", appointment.getEpid());
				jObj.put("content_type", "episode");
				jObj.put("created_by",
						sharedPref.getString(Constants.USER_ID, ""));
				masterPlan.jsonData = jObj;
				masterPlan.JsonParse(jObj.getJSONObject("mast"));
			} catch (final Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

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

		radiologyTable();
		labTable();

	}

	@Override
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
	protected void onResume() {
		try {
			radiologyTable();
			labTable();
			prescriptionTable();
			boolean radiologyflag = false;
			txtRadiology.setText(Html.fromHtml("<b>Radiology :</b>  "
					+ visitModel.dentistPlanModel.radiology.getLabString()));
			if (visitModel.dentistPlanModel.radiology.hashRadiology
					.containsValue("on")
					|| !Util.isEmptyString(visitModel.dentistPlanModel.radiology.hashRadiology
							.get("othe")))
				radiologyflag = true;
			try {
				findViewById(R.id.txt_print_radiology).setActivated(
						radiologyflag);
				findViewById(R.id.txt_print_radiology)
						.setEnabled(radiologyflag);
				findViewById(R.id.txt_print_radiology).setClickable(
						radiologyflag);
			} catch (Exception e) {

			}
			boolean labflag = false;
			((TextView) findViewById(R.id.txt_lab)).setText(Html
					.fromHtml("<b>Lab order :</b>  "
							+ visitModel.dentistPlanModel.lab.getLabString()));
			if (visitModel.dentistPlanModel.lab.hashLab.containsValue("on")
					|| !Util.isEmptyString(visitModel.dentistPlanModel.lab.hashLab
							.get("othe")))
				labflag = true;

			try {
				findViewById(R.id.txt_print_laborder).setActivated(labflag);
				findViewById(R.id.txt_print_laborder).setEnabled(labflag);
				findViewById(R.id.txt_print_laborder).setClickable(labflag);
			} catch (Exception e) {

			}

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
			boolean prescriptionflag = false;
			((TextView) findViewById(R.id.txt_prescription)).setText(Html
					.fromHtml("<b>Prescription :</b>  "));
			int i = 1;
			for (final MedicineModel medicine : visitModel.dentistPlanModel.prescription.arrMedicine) {

				((TextView) findViewById(R.id.txt_prescription)).append(Html
						.fromHtml(""));
				prescriptionflag = true;
				i++;
			}
			if (!Util.isEmptyString(visitModel.dentistPlanModel.prescription.si
					.get("si"))
					&& visitNotes.dentistPlanModel.prescription.arrMedicine
							.size() > 0)
				((TextView) findViewById(R.id.txt_prescription))
						.append("\n Special Instruction : "
								+ visitNotes.dentistPlanModel.prescription.si
										.get("si"));

			try {
				findViewById(R.id.txt_print_prescription).setActivated(
						prescriptionflag);
				findViewById(R.id.txt_print_prescription).setEnabled(
						prescriptionflag);
				findViewById(R.id.txt_print_prescription).setClickable(
						prescriptionflag);
			} catch (Exception e) {

			}
			boolean allflag = labflag || radiologyflag || prescriptionflag;
			findViewById(R.id.txt_print_all).setActivated(allflag);
			findViewById(R.id.txt_print_all).setEnabled(allflag);
			findViewById(R.id.txt_print_all).setClickable(allflag);
		} catch (Exception e) {
			Log.e("", e);
		}

		final String url = APIs.SOAP_SHOW() + appointment.getSiid() + "/bk_id/"
				+ appointment.getBkid();
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
						Util.Alertdialog(AddDentistNotesActivity.this,
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
		final String url = APIs.PAST_FOLLOWUP() + appointment.getBkid()
				+ "/follow-id/" + appointment.getFollowid();
		final Dialog loaddialog = Util
				.showLoadDialog(AddDentistNotesActivity.this);
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
								} catch (final JSONException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}

							} else {
								// Toast.makeText(
								// AddDentistNotesActivity.this,
								// "There is some error while fetching data please try again.",
								// Toast.LENGTH_SHORT).show();
							}
						} catch (final JSONException e) {
							// Toast.makeText(
							// AddDentistNotesActivity.this,
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
						// AddDentistNotesActivity.this,
						// "There is some error while fetching data please try again",
						// Toast.LENGTH_SHORT).show();

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
				// Log.i("Login Params", loginParams.toString());
				return loginParams;
			}

		};
		patientListRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 5,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		EzApp.mVolleyQueue.add(patientListRequest);
	}

	private void postData() {
		Log.i("", "post soap called");
		final String url = APIs.SOAP_UPDATE() + appointment.getSiid()
				+ "/cli/api";
		final Dialog loaddialog = Util
				.showLoadDialog(AddDentistNotesActivity.this);
		final JsonObjectRequest patientListRequest = new JsonObjectRequest(
				Request.Method.POST, url, visitModel.jsonData,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(final JSONObject jObj) {
						String s = jObj.toString();
						while (s.length() > 100) {
							s = s.substring(100);
						}
						try {
							if (jObj.getString("s").equals("200")) {
								if (jObj.has("si-id"))
									appointment.setSiid(jObj.getString("si-id"));
								loaddialog.dismiss();
								if (appointment.getMpid() == null) {
									appointment.setMpid("0");
								}
								if (!Util
										.isEmptyString(masterPlan.masterPlanType)) {
									Log.i("", masterPlan.masterPlanType);
									masterPlan.updateJson(masterPlan.jsonData,
											AddDentistNotesActivity.this);
									postMasterData();
								} else {
									Util.AlertdialogWithFinish(
											AddDentistNotesActivity.this,
											"Visit Notes updated successfully");
								}

							} else {
								Log.e("", jObj.toString());
								loaddialog.dismiss();
								Util.Alertdialog(AddDentistNotesActivity.this,
										"There is some error in posting your note, please try again");
							}
						} catch (final JSONException e) {
							Util.Alertdialog(AddDentistNotesActivity.this,
									"There is some error in posting your note, please try again");
							Log.e("", e);
						}
						try {
							loaddialog.dismiss();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(final VolleyError error) {
						Util.Alertdialog(AddDentistNotesActivity.this,
								"There is network error, please try again");

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

	private void postMasterData() {
		final String url;
		if (Util.isEmptyString(appointment.getMpid()))
			url = APIs.MASTERPLAN_UPDATE() + masterPlan.mp_ep_id;
		else
			url = APIs.MASTERPLAN_UPDATE() + appointment.getMpid();

		final Dialog loaddialog = Util
				.showLoadDialog(AddDentistNotesActivity.this);
		Log.i("", masterPlan.jsonData.toString());
		final JsonObjectRequest patientListRequest = new JsonObjectRequest(
				Request.Method.POST, url, masterPlan.jsonData,
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
										AddDentistNotesActivity.this,
										"Visit Notes updated successfully");
							} else {
								Log.e("", jObj.toString());
								Util.Alertdialog(AddDentistNotesActivity.this,
										"There is some error in posting your note, please try again");
							}
						} catch (final JSONException e) {
							Util.Alertdialog(AddDentistNotesActivity.this,
									"There is some error in posting your note, please try again");
							Log.e("", e);
						}
						try {
							loaddialog.dismiss();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(final VolleyError error) {
						Util.Alertdialog(AddDentistNotesActivity.this,
								"There is network error, please try again.");

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

	private void referPatient(final String drId, final String reason,
			final Dialog dialog) {
		final String url = APIs.REFER_PATIENT_CREATE();
		final Dialog loaddialog = Util
				.showLoadDialog(AddDentistNotesActivity.this);
		final StringRequest patientListRequest = new StringRequest(
				Request.Method.POST, url, new Response.Listener<String>() {
					@Override
					public void onResponse(final String response) {
						try {
							// Log.i("refer response", response);
							final JSONObject jObj = new JSONObject(response);
							if (jObj.getString("s").equals("200")) {
								Util.Alertdialog(AddDentistNotesActivity.this,
										"Patient has been referred successfully");
								dialog.dismiss();
							} else {
								Util.Alertdialog(AddDentistNotesActivity.this,
										"There is some error while referring patient, please try again.");
							}
						} catch (final JSONException e) {
							Util.Alertdialog(AddDentistNotesActivity.this,
									"There is some error while referring patient, please try again.");
							Log.e("", e);
						}
						loaddialog.dismiss();
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(final VolleyError error) {
						Util.Alertdialog(AddDentistNotesActivity.this,
								"Network error, try again later");
						// Toast.makeText(
						// AddDentistNotesActivity.this,
						// "There is some error while fetching data please try again",
						// Toast.LENGTH_SHORT).show();

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
				final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				loginParams.put("format", "json");
				loginParams.put("pat-id", appointment.getPid());
				loginParams.put("fam-id", patientModel.getFid());
				loginParams.put("ep-id", appointment.getEpid());
				loginParams.put("doc-id",
						sharedPref.getString(Constants.ROLE_ID, ""));// role_id
				loginParams.put("user-id",
						sharedPref.getString(Constants.USER_ID, ""));
				loginParams.put("pfn", patientModel.getPfn());
				loginParams.put("pln", patientModel.getPln());
				loginParams.put("reason", reason);
				loginParams.put("refer_type", "doctor");
				loginParams.put("doctor", drId);
				// Log.i("Login Params", loginParams.toString());
				return loginParams;
			}

		};
		patientListRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 5,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		EzApp.mVolleyQueue.add(patientListRequest);
	}

	public void refreshToothDialog(final Dialog dialogTooth) {
		final GridView listHardTissue = (GridView) dialogTooth
				.findViewById(R.id.list_hard_tissue);
		final TeethView view = (TeethView) dialogTooth
				.findViewById(R.id.toothimage);
		listHardTissue.setVisibility(View.GONE);
		dialogTooth.findViewById(R.id.edit_others).setVisibility(View.GONE);
		for (int i = 1; i < 5; i++) {
			for (int j = 1; j < 9; j++) {
				final TeethModel teeth1 = arrTeeth.get((j - 1) + ((i - 1) * 8));
				final ImageView img1 = (ImageView) view.findViewWithTag("tooth"
						+ i + "" + j);
				img1.setBackgroundColor(Color.parseColor("#ffffff"));
				if ((teeth1.arrTeethState.size() > 0)
						|| (teeth1.arrTeethTreatmentPlan.size() > 0)) {
					img1.setBackgroundColor(Color.parseColor("#77EA2B7F"));
				}
			}
		}

		for (int i = 5; i < 9; i++) {
			for (int j = 1; j < 6; j++) {
				final TeethModel teeth1 = arrTeeth.get(((32 + j) - 1)
						+ ((i - 5) * 5));
				// teeth1.arrTeethState =
				// patientModel.masterPlan.arrTeeth
				// .get(((32 + j) - 1) + ((i - 5) *
				// 5)).arrTeethState;
				ImageView img1;
				if (j < 4) {
					img1 = (ImageView) view.findViewWithTag("tooth" + (i - 4)
							+ "" + j);
				} else {
					img1 = (ImageView) view.findViewWithTag("tooth" + (i - 4)
							+ "" + (j + 2));
				}
				// img1.setBackgroundColor(Color
				// .parseColor("#ffffff"));
				if ((teeth1.arrTeethState.size() > 0)
						|| (teeth1.arrTeethTreatmentPlan.size() > 0)) {
					img1.setBackgroundColor(Color.parseColor("#77EA2B7F"));
				}
			}
		}
		setToothImage(view);
	}

	@Override
	protected void onPause() {

		visitModel.updateJson(arrTeeth, this);
		if (appointment.getMpid() == null) {
			appointment.setMpid("0");
		}
		try {
			masterPlan.updateJson(masterPlan.jsonData, this);
		} catch (Exception e) {
			e.printStackTrace();
		}
		SoapNote note = new SoapNote();
		note.setDate(new Date());
		note.setNote(visitModel.jsonData.toString());
		note.setBk_id(appointment.getBkid());
		note.setId((long) Integer.parseInt(appointment.getBkid()));
		EzApp.soapNoteDao.insertOrReplace(note);
		super.onPause();
	}

	private void addAllergy(final String mc, final String sc, final String ai,
			final String mName, final String sName) {
		final String url = APIs.ADD_ALLERGIES();
		final Dialog loaddialog = Util
				.showLoadDialog(AddDentistNotesActivity.this);
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
								Util.Alertdialog(AddDentistNotesActivity.this,
										"Allergies added successfully");
								onResume();
							} else {
								Util.Alertdialog(AddDentistNotesActivity.this,
										"This Allergy is already added");
							}
						} catch (final JSONException e) {
							Util.Alertdialog(AddDentistNotesActivity.this,
									"There is some error in adding allergies, please try again.");
							Log.e("", e);
						}
						loaddialog.dismiss();
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(final VolleyError error) {
						Util.Alertdialog(AddDentistNotesActivity.this,
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
				final HashMap<String, String> params = new HashMap<String, String>();
				params.put("format", "json");
				params.put("bk_id", appointment.getBkid());
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
					R.layout.row_document, null, false);
			row.setLayoutParams(params);
			txtAction = (TextView) row.findViewById(R.id.txt_action);
			txtDocumentName = (TextView) row.findViewById(R.id.txt_name);
			txtDocumentType = (TextView) row.findViewById(R.id.txt_type);
			txtUploadDate = (TextView) row.findViewById(R.id.txt_upload_date);
			txtAction.setText(Html.fromHtml("<b>Action</b>"));
			txtDocumentName.setText(Html.fromHtml("<b>Document Name</b>"));
			txtDocumentType.setText(Html.fromHtml("<b>Document Type</b>"));
			txtUploadDate.setText(Html.fromHtml("<b>Upload Date</b>"));
			tl.addView(row);
			for (final Document doc : arrRadiDocuments) {
				final LayoutParams params1 = new LayoutParams(
						android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
						android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
				final TableRow row1 = (TableRow) getLayoutInflater().inflate(
						R.layout.row_document, null, false);
				row1.setLayoutParams(params1);
				txtAction = (TextView) row1.findViewById(R.id.txt_action);
				txtDocumentName = (TextView) row1.findViewById(R.id.txt_name);
				txtDocumentType = (TextView) row1.findViewById(R.id.txt_type);
				txtUploadDate = (TextView) row1
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
					a = EzApp.sdfemmm
							.format(EzApp.sdfyymmddhhmmss
									.parse(doc.getDate()));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					a = "";
				}
				txtUploadDate.setText(a);
				txtAction.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
								AddDentistNotesActivity.this);

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
												deleteDocument(doc);

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
					R.layout.row_document, null, false);
			row.setLayoutParams(params);
			txtAction = (TextView) row.findViewById(R.id.txt_action);
			txtDocumentName = (TextView) row.findViewById(R.id.txt_name);
			txtDocumentType = (TextView) row.findViewById(R.id.txt_type);
			txtUploadDate = (TextView) row.findViewById(R.id.txt_upload_date);
			txtAction.setText(Html.fromHtml("<b>Action</b>"));
			txtDocumentName.setText(Html.fromHtml("<b>Document Name</b>"));
			txtDocumentType.setText(Html.fromHtml("<b>Document Type</b>"));
			txtUploadDate.setText(Html.fromHtml("<b>Upload Date</b>"));
			tl.addView(row);
			for (final Document doc : arrRadiDocuments) {
				final LayoutParams params1 = new LayoutParams(
						android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
						android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
				final TableRow row1 = (TableRow) getLayoutInflater().inflate(
						R.layout.row_document, null, false);
				row1.setLayoutParams(params1);
				txtAction = (TextView) row1.findViewById(R.id.txt_action);
				txtDocumentName = (TextView) row1.findViewById(R.id.txt_name);
				txtDocumentType = (TextView) row1.findViewById(R.id.txt_type);
				txtUploadDate = (TextView) row1
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
					a = EzApp.sdfemmm
							.format(EzApp.sdfyymmddhhmmss
									.parse(doc.getDate()));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					a = "";
				}
				txtUploadDate.setText(a);
				txtAction.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
								AddDentistNotesActivity.this);

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
												deleteDocument(doc);

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

	private void deleteDocument(final Document doc) {
		final String url = APIs.DELETE_DOCUMENT() + doc.getId();
		final Dialog loaddialog = Util
				.showLoadDialog(AddDentistNotesActivity.this);
		final StringRequest patientListRequest = new StringRequest(
				Request.Method.POST, url, new Response.Listener<String>() {
					@Override
					public void onResponse(final String response) {
						try {
							Log.i("delete document", response);
							final JSONObject jObj = new JSONObject(response);
							if (jObj.getString("s").equals("200")) {
								arrDocuments.remove(doc);
								radiologyTable();
								labTable();

								Util.Alertdialog(AddDentistNotesActivity.this,
										"Document deleted successfully");
							} else {
								Util.Alertdialog(AddDentistNotesActivity.this,
										"There is some error while deleting, please try again.");
							}
						} catch (final JSONException e) {
							Util.Alertdialog(AddDentistNotesActivity.this,
									"There is some error while deleting, please try again.");
							Log.e("", e);
						}
						loaddialog.dismiss();
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(final VolleyError error) {
						Util.Alertdialog(AddDentistNotesActivity.this,
								"Network error, try again later");

						error.printStackTrace();
						loaddialog.dismiss();
					}
				}) {
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				final HashMap<String, String> loginParams = new HashMap<String, String>();
				loginParams.put("auth-token", Util
						.getBase64String(EzApp.sharedPref
								.getString(Constants.USER_TOKEN, "")));
				return loginParams;
			}

			@Override
			protected Map<String, String> getParams() {
				final HashMap<String, String> loginParams = new HashMap<String, String>();
				final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				loginParams.put("format", "json");
				return loginParams;
			}

		};
		patientListRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 5,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		EzApp.mVolleyQueue.add(patientListRequest);

	}

	private void setToothImage(View view) {
		for (int i = 1; i < 5; i++) {
			for (int j = 1; j < 9; j++) {
				final String s = "tooth" + i + "" + j + "_a";
				final TeethModel teeth = arrTeeth.get((j - 1) + ((i - 1) * 8));
				final ImageView img = (ImageView) view.findViewWithTag("tooth"
						+ i + "" + j);
				// teeth.arrTeethState = patientModel.masterPlan.arrTeeth
				// .get((j - 1) + ((i - 1) * 8)).arrTeethState;
				img.setImageResource(getResources().getIdentifier(
						"tooth" + i + "" + j, "drawable", getPackageName()));
				if ((teeth.arrTeethState.size() > 0)
						|| (teeth.arrTeethTreatmentPlan.size() > 0)
						|| !Util.isEmptyString(teeth.hashState.get("othe"))) {
					img.setBackgroundColor(Color.parseColor("#77EA2B7F"));
				}
				TeethModel teeth1;
				if (teeth.name.equals("tooth11")) {
					teeth1 = arrTeeth.get(32);
				} else if (teeth.name.equals("tooth12")) {
					teeth1 = arrTeeth.get(33);
				} else if (teeth.name.equals("tooth13")) {
					teeth1 = arrTeeth.get(34);
				} else if (teeth.name.equals("tooth16")) {
					teeth1 = arrTeeth.get(35);
				} else if (teeth.name.equals("tooth17")) {
					teeth1 = arrTeeth.get(36);
				} else if (teeth.name.equals("tooth21")) {
					teeth1 = arrTeeth.get(37);
				} else if (teeth.name.equals("tooth22")) {
					teeth1 = arrTeeth.get(38);
				} else if (teeth.name.equals("tooth23")) {
					teeth1 = arrTeeth.get(39);
				} else if (teeth.name.equals("tooth26")) {
					teeth1 = arrTeeth.get(40);
				} else if (teeth.name.equals("tooth27")) {
					teeth1 = arrTeeth.get(41);
				} else if (teeth.name.equals("tooth31")) {
					teeth1 = arrTeeth.get(42);
				} else if (teeth.name.equals("tooth32")) {
					teeth1 = arrTeeth.get(43);
				} else if (teeth.name.equals("tooth33")) {
					teeth1 = arrTeeth.get(44);
				} else if (teeth.name.equals("tooth36")) {
					teeth1 = arrTeeth.get(45);
				} else if (teeth.name.equals("tooth37")) {
					teeth1 = arrTeeth.get(46);
				} else if (teeth.name.equals("tooth41")) {
					teeth1 = arrTeeth.get(47);
				} else if (teeth.name.equals("tooth42")) {
					teeth1 = arrTeeth.get(48);
				} else if (teeth.name.equals("tooth43")) {
					teeth1 = arrTeeth.get(49);
				} else if (teeth.name.equals("tooth46")) {
					teeth1 = arrTeeth.get(50);
				} else if (teeth.name.equals("tooth47")) {
					teeth1 = arrTeeth.get(51);
				} else {
					teeth1 = teeth;
				}
				if (teeth.arrTeethState.contains("Missing")
						|| teeth1.arrTeethState.contains("Missing")) {
					img.setImageResource(getResources().getIdentifier(
							"m_" + i + "" + j, "drawable", getPackageName()));
				} else if (teeth.arrTeethState.contains("Root Stump")
						|| teeth1.arrTeethState.contains("Root Stump")) {
					img.setImageResource(getResources().getIdentifier(
							"rs_" + i + "" + j, "drawable", getPackageName()));
				} else if (teeth.arrTeethState.contains("Restoration")
						|| teeth.arrTeethState.contains("Restoration")) {
					img.setImageResource(getResources().getIdentifier(s,
							"drawable", getPackageName()));
				}
			}
		}

	}

	private void radiologyTable(Dialog dialog, ArrayList<Document> arrDocuments) {
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

	private void labTable(Dialog dialog, ArrayList<Document> arrDocuments) {
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

	public void dialogPrintAllInvestigations() {
		final Dialog dialogPrintAllInvestigation = new Dialog(this,
				R.style.Theme_AppCompat_Light);
		dialogPrintAllInvestigation
				.requestWindowFeature(Window.FEATURE_NO_TITLE);

		dialogPrintAllInvestigation
				.setContentView(R.layout.dialog_print_all_investigations);
		dialogPrintAllInvestigation.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		final SimpleDateFormat df = new SimpleDateFormat(" EEE, MMM dd, yyyy");
		String drName = EzApp.sharedPref.getString(
				Constants.DR_NAME, "");
		((TextView) dialogPrintAllInvestigation.findViewById(R.id.txt_drname))
				.setText(drName);
		String drAddress = EzApp.sharedPref.getString(
				Constants.DR_ADDRESS, "");
		((TextView) dialogPrintAllInvestigation
				.findViewById(R.id.txt_doc_address)).setText(drAddress + " - "
				+ EditAccountActivity.profile.getZip());
		((TextView) dialogPrintAllInvestigation.findViewById(R.id.txt_ptname))
				.setText(Html.fromHtml("<b>Patient Name:</b> "
						+ patientModel.getPfn() + " " + patientModel.getPln()));
		((TextView) dialogPrintAllInvestigation.findViewById(R.id.txt_gender))
				.setText(Html.fromHtml("<b>Gender:</b> "
						+ patientModel.getPgender()));

		((TextView) dialogPrintAllInvestigation.findViewById(R.id.txt_age))
				.setText(Html.fromHtml("<b>Age:</b> " + patientModel.getPage()));
		((TextView) dialogPrintAllInvestigation.findViewById(R.id.txt_date))
				.setText(Html.fromHtml("<b>Date:</b> " + df.format(new Date())));
		((TextView) dialogPrintAllInvestigation
				.findViewById(R.id.txt_ptaddress)).setText(Html
				.fromHtml("<b>Address:</b> " + patientModel.getPadd1() + " "
						+ patientModel.getPadd2() + ", "
						+ patientModel.getParea() + ", "
						+ patientModel.getPcity() + ", "
						+ patientModel.getPstate() + ", "
						+ patientModel.getPcountry() + " - "
						+ patientModel.getPzip()));

		if (visitModel.dentistPlanModel.lab.getLabString().contains(" .")) {
			((TextView) dialogPrintAllInvestigation
					.findViewById(R.id.txt_lab_ordered))
					.setVisibility(View.GONE);
		} else {

			((TextView) dialogPrintAllInvestigation
					.findViewById(R.id.txt_lab_ordered)).setText(Html
					.fromHtml("<b>Lab Order:</b> "
							+ visitModel.dentistPlanModel.lab.getLabString()));
		}

		if (visitModel.dentistPlanModel.radiology.getLabString().contains(" .")) {
			((TextView) dialogPrintAllInvestigation
					.findViewById(R.id.txt_radiology_orderd))
					.setVisibility(View.GONE);
		} else {
			((TextView) dialogPrintAllInvestigation
					.findViewById(R.id.txt_radiology_orderd)).setText(Html
					.fromHtml("<b>Radiology Ordered:</b> "
							+ visitModel.dentistPlanModel.radiology
									.getLabString()));
		}

		TextView txtFinalDiagnosis = (TextView) dialogPrintAllInvestigation
				.findViewById(R.id.txt_final_diagnosis);
		if (!Util.isEmptyString(actvFinal.getText().toString())) {
			txtFinalDiagnosis.setText(Html.fromHtml("<b>Diagnosis:</b> "
					+ actvFinal.getText().toString()));
		} else {
			txtFinalDiagnosis.setVisibility(View.GONE);
		}

		MedicineModel med = new MedicineModel();
		if (Util.isEmptyString(med.getMedicineString())) {
			((TextView) dialogPrintAllInvestigation
					.findViewById(R.id.txt_prescription))
					.setVisibility(View.GONE);
		} else {
			((TextView) dialogPrintAllInvestigation
					.findViewById(R.id.txt_prescription)).setText(Html
					.fromHtml("<b>Prescription:</b> "));
		}
		((TextView) dialogPrintAllInvestigation
				.findViewById(R.id.txt_special_instructions))
				.setText(Html.fromHtml("<b>Special Instructions:</b> "
						+ visitModel.dentistPlanModel.prescription.si.get("si")));

		if (!Util.isEmptyString(visitNotes.dentistExaminationModel.hashVitals
				.get("low"))
				|| !Util.isEmptyString(visitNotes.dentistExaminationModel.hashVitals
						.get("high"))
				|| !Util.isEmptyString(visitNotes.dentistExaminationModel.hashVitals
						.get("temp"))
				|| !Util.isEmptyString(visitNotes.dentistExaminationModel.hashVitals
						.get("rr"))
				|| !Util.isEmptyString(visitNotes.dentistExaminationModel.hashVitals
						.get("low"))
				|| !Util.isEmptyString(visitNotes.dentistExaminationModel.hashVitals
						.get("puls"))
				|| !Util.isEmptyString(visitNotes.dentistExaminationModel.hashVitals
						.get("weig"))
				|| !Util.isEmptyString(visitNotes.dentistExaminationModel.hashVitals
						.get("heig"))
				|| !Util.isEmptyString(visitNotes.dentistExaminationModel.hashVitals
						.get("waist"))
				|| !Util.isEmptyString(visitNotes.dentistExaminationModel.hashVitals
						.get("bmi"))) {
			((TextView) dialogPrintAllInvestigation
					.findViewById(R.id.txt_vitals)).setText(Html
					.fromHtml("<b>Vitals: </b>"));
		} else {
			((TextView) dialogPrintAllInvestigation
					.findViewById(R.id.txt_vitals)).setVisibility(View.GONE);
		}

		if (!Util.isEmptyString(visitNotes.dentistExaminationModel.hashVitals
				.get("low"))
				|| !Util.isEmptyString(visitNotes.dentistExaminationModel.hashVitals
						.get("high")))
			((TextView) dialogPrintAllInvestigation
					.findViewById(R.id.txt_vitals)).append(Html
					.fromHtml("<b>Blood Pressure</b> = "
							+ visitNotes.dentistExaminationModel.hashVitals
									.get("high")
							+ "-"
							+ visitNotes.dentistExaminationModel.hashVitals
									.get("low") + " mm of Hg"));
		if (!Util.isEmptyString(visitNotes.dentistExaminationModel.hashVitals
				.get("temp"))) {
			((TextView) dialogPrintAllInvestigation
					.findViewById(R.id.txt_vitals)).append(Html
					.fromHtml(", <b>Body Temperature</b> = "
							+ visitNotes.dentistExaminationModel.hashVitals
									.get("temp") + " &#176; F"));
		}
		if (!Util.isEmptyString(visitNotes.dentistExaminationModel.hashVitals
				.get("rr"))) {
			((TextView) dialogPrintAllInvestigation
					.findViewById(R.id.txt_vitals)).append(Html
					.fromHtml(", <b>Respiratory Rate</b> = "
							+ visitNotes.dentistExaminationModel.hashVitals
									.get("rr") + " breathes/minute"));
		}
		if (!Util.isEmptyString(visitNotes.dentistExaminationModel.hashVitals
				.get("puls"))) {
			((TextView) dialogPrintAllInvestigation
					.findViewById(R.id.txt_vitals)).append(Html
					.fromHtml(", <b>Pulse</b> = "
							+ visitNotes.dentistExaminationModel.hashVitals
									.get("puls") + " beats/minute"));
		}
		if (!Util.isEmptyString(visitNotes.dentistExaminationModel.hashVitals
				.get("weig"))) {
			((TextView) dialogPrintAllInvestigation
					.findViewById(R.id.txt_vitals)).append(Html
					.fromHtml(", <b>Weight<b> = "
							+ visitNotes.dentistExaminationModel.hashVitals
									.get("weig") + " kg"));
		}

		if (!Util.isEmptyString(visitNotes.dentistExaminationModel.hashVitals
				.get("heig"))) {
			((TextView) dialogPrintAllInvestigation
					.findViewById(R.id.txt_vitals)).append(Html
					.fromHtml(", <b>Height</b> = "
							+ visitNotes.dentistExaminationModel.hashVitals
									.get("heig") + " cm"));
		}

		if (!Util.isEmptyString(visitNotes.dentistExaminationModel.hashVitals
				.get("waist"))) {
			((TextView) dialogPrintAllInvestigation
					.findViewById(R.id.txt_vitals)).append(Html
					.fromHtml(", <b>Waist</b> = "
							+ visitNotes.dentistExaminationModel.hashVitals
									.get("waist") + " cm"));
		}

		if (!Util.isEmptyString(visitNotes.dentistExaminationModel.hashVitals
				.get("bmi"))) {
			((TextView) dialogPrintAllInvestigation
					.findViewById(R.id.txt_vitals)).append(Html
					.fromHtml(", <b>BMI</b> = "
							+ visitNotes.dentistExaminationModel.hashVitals
									.get("bmi") + " kg/m<sup>2</sup>"));
		}

		prescriptionTablePast(dialogPrintAllInvestigation);

		final String url = APIs.SOAP_SHOW() + appointment.getSiid() + "/bk_id/"
				+ appointment.getBkid();
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
									((TextView) dialogPrintAllInvestigation
											.findViewById(R.id.txt_followup)).setText(Html
											.fromHtml("<b>Followup Appointment: </b>"));
								((TextView) dialogPrintAllInvestigation
										.findViewById(R.id.txt_followup))
										.append(jObj.getJSONObject("data")
												.getString(
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
						Util.Alertdialog(AddDentistNotesActivity.this,
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

		// PatientController.patientBarcode(AddDentistNotesActivity.this,
		// new OnResponseData() {
		//
		// @Override
		// public void onResponseListner(Object response) {
		//
		// Util.getImageFromUrl(response.toString(),
		// DashboardActivity.imgLoader,
		// (ImageView) dialogPrintAllInvestigation
		// .findViewById(R.id.img_barcode));
		// }
		// }, patientModel);

		if (!Util.isEmptyString(EzApp.sharedPref.getString(
				Constants.SIGNATURE, "signature"))) {
			Util.getImageFromUrl(EzApp.sharedPref.getString(
					Constants.SIGNATURE, ""), DashboardActivity.imgLoader,
					(ImageView) dialogPrintAllInvestigation
							.findViewById(R.id.img_signature));
		}
		dialogPrintAllInvestigation.setCancelable(false);
		dialogPrintAllInvestigation.findViewById(R.id.txt_close)
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						dialogPrintAllInvestigation.dismiss();

					}
				});
		dialogPrintAllInvestigation.findViewById(R.id.btn_print)
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						PrintHelper photoPrinter = new PrintHelper(
								AddDentistNotesActivity.this);

						photoPrinter.setScaleMode(PrintHelper.SCALE_MODE_FIT);
						View view = (dialogPrintAllInvestigation
								.findViewById(R.id.rl_top));
						view.setDrawingCacheEnabled(true);
						view.buildDrawingCache();
						Bitmap bm = view.getDrawingCache();
						// Bitmap bitmap =
						// BitmapFactory.decodeResource(getResources(),
						// R.drawable.rs_15);

						photoPrinter.printBitmap("Lab.jpg", bm);

						// dialogPrintLabordered.dismiss();

					}
				});
		dialogPrintAllInvestigation.show();

	}

	private void prescriptionTable() {
		final TableLayout tl = (TableLayout) findViewById(R.id.table_prescription);
		tl.removeAllViews();
		final LayoutParams params = new LayoutParams(
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		final TableRow row = (TableRow) getLayoutInflater().inflate(
				R.layout.row_prescription_table_vnotes, null, false);
		row.setLayoutParams(params);
		txtSno = (TextView) row.findViewById(R.id.txt_sno);
		txtFormulation = (TextView) row.findViewById(R.id.txt_formulation);
		txtDrugName = (TextView) row.findViewById(R.id.txt_name);
		txtDetails = (TextView) row.findViewById(R.id.txt_detail);
		txtFreqDetail = (TextView) row.findViewById(R.id.txt_frequency_detail);
		txtRefills = (TextView) row.findViewById(R.id.txt_refills);
		txtNotes = (TextView) row.findViewById(R.id.txt_notes);
		txtHistory = (TextView) row.findViewById(R.id.txt_history);

		row.removeAllViews();
		txtSno.setText(Html.fromHtml("<b>S.No.</b>"));
		row.addView(txtSno);
		txtFormulation.setText(Html.fromHtml("<b>Formulation</b>"));
		row.addView(txtFormulation);
		txtDrugName.setText(Html.fromHtml("<b>Drug Name</b>"));
		row.addView(txtDrugName);
		txtDetails.setText(Html.fromHtml("<b>Detail</b>"));
		row.addView(txtDetails);
		txtFreqDetail.setText(Html.fromHtml("<b>Frequency Detail</b>"));
		row.addView(txtFreqDetail);
		txtRefills.setText(Html.fromHtml("<b>Refill</b>"));
		row.addView(txtRefills);
		txtNotes.setText(Html.fromHtml("<b>Notes</b>"));
		row.addView(txtNotes);
		txtHistory.setText(Html.fromHtml("<b>History</b>"));
		row.addView(txtHistory);

		tl.addView(row);

		Boolean prescriptonflag = false;
		int i = 1;
		for (final MedicineModel doc : visitModel.dentistPlanModel.prescription.arrMedicine) {

			if (doc.getMedicineString().length() > 0) {
				final LayoutParams params1 = new LayoutParams(
						android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
						android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
				final TableRow row1 = (TableRow) getLayoutInflater().inflate(
						R.layout.row_prescription_table_vnotes, null, false);
				row1.setLayoutParams(params1);
				txtSno = (TextView) row1.findViewById(R.id.txt_sno);
				txtFormulation = (TextView) row1
						.findViewById(R.id.txt_formulation);
				txtDrugName = (TextView) row1.findViewById(R.id.txt_name);
				txtDetails = (TextView) row1.findViewById(R.id.txt_detail);
				txtFreqDetail = (TextView) row1
						.findViewById(R.id.txt_frequency_detail);
				txtRefills = (TextView) row1.findViewById(R.id.txt_refills);
				txtNotes = (TextView) row1.findViewById(R.id.txt_notes);
				txtHistory = (TextView) row1.findViewById(R.id.txt_history);

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
				Calendar c = Calendar.getInstance();
				System.out.println("Current time => " + c.getTime());
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				String formattedDate = df.format(c.getTime());

				if (doc.addedon == null || doc.addedon.equals("")) {
					txtHistory.setText("Added On: " + formattedDate);
				} else {
					if (!Util.isEmptyString(doc.updatedon)) {
						txtHistory.setText("Added On: " + doc.addedon
								+ ", Updated On: " + doc.updatedon);
					} else {
						txtHistory.setText("Added On: " + doc.addedon);
					}
				}

				tl.addView(row1);

				tl.setVisibility(View.VISIBLE);
				prescriptonflag = true;
				i++;
			} else {
				tl.setVisibility(View.GONE);
			}
		}
		try {
			findViewById(R.id.txt_print_prescription).setActivated(
					prescriptonflag);
			findViewById(R.id.txt_print_prescription).setEnabled(
					prescriptonflag);
			findViewById(R.id.txt_print_prescription).setClickable(
					prescriptonflag);
		} catch (Exception e) {

		}
	}

	private void prescriptionTablePast(Dialog dialog) {
		final TableLayout tl = (TableLayout) dialog
				.findViewById(R.id.table_prescription);
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
