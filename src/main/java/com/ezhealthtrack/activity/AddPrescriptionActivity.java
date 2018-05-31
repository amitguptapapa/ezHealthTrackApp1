package com.ezhealthtrack.activity;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ListView;
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
import com.ezhealthtrack.DentistSoap.AddDentistNotesActivity;
import com.ezhealthtrack.adapter.MedicineAdapter;
import com.ezhealthtrack.adapter.MedicineAutoSuggestAdapter;
import com.ezhealthtrack.controller.ConfigController;
import com.ezhealthtrack.controller.EzController.UpdateListner;
import com.ezhealthtrack.controller.EzNetwork;
import com.ezhealthtrack.controller.OrderSetsController;
import com.ezhealthtrack.greendao.Appointment;
import com.ezhealthtrack.greendao.Patient;
import com.ezhealthtrack.model.Frequency;
import com.ezhealthtrack.model.MedicineAutoSuggest;
import com.ezhealthtrack.model.MedicineModel;
import com.ezhealthtrack.model.OrderSets;
import com.ezhealthtrack.model.PatientShow;
import com.ezhealthtrack.model.PrescriptionModel;
import com.ezhealthtrack.one.EzUtils;
import com.ezhealthtrack.physiciansoap.PhysicianSoapActivityMain;
import com.ezhealthtrack.print.PrintPrescriptionActivity;
import com.ezhealthtrack.util.BaseActivity;
import com.ezhealthtrack.util.Constants;
import com.ezhealthtrack.util.Log;
import com.ezhealthtrack.util.Util;
import com.ezhealthtrack.views.EditUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class AddPrescriptionActivity extends BaseActivity {

	OrderSetsController mController;

	private Appointment aptModel;
	private HashMap<String, String> hashVitals = new HashMap<String, String>();
	private PrescriptionModel pres;
	private PrescriptionModel prescription = new PrescriptionModel();
	private PrescriptionModel selectedPrescription;
	private Button btnSubmit;
	private Button btnAddOrderSet;
	private Patient patientModel = new Patient();
	private ListView listPrescription;
	private AutoCompleteTextView editName;
	private AutoCompleteTextView editFrequency;
	private AutoCompleteTextView actvOrderSet;
	private AutoCompleteTextView actvUnit;
	private AutoCompleteTextView actvRoute;
	private AutoCompleteTextView actvFormulations;
	private EditText editStrength;
	private EditText editQuantity;
	private EditText editTimes;
	private EditText editRefillsTime;
	private EditText editNotes;
	private CheckBox cbRefills;
	private TextView btnAddMedicine;
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
	private MedicineAdapter adapterMedicine;
	private final ArrayList<MedicineAutoSuggest> arrDrugsName = new ArrayList<MedicineAutoSuggest>();
	private MedicineAutoSuggestAdapter adapter;
	private OrderSets orderSetAdapter;
	private ArrayList<String> arrDrugs = new ArrayList<String>();
	private ArrayList<String> arrFrequency = new ArrayList<String>();
	private ArrayAdapter<OrderSets> orderSetAutoSuggestAdapter;
	private HashMap<String, Frequency> arrFreq = new HashMap<String, Frequency>();
	private LayoutInflater inflater;
	int selected_pos = -1;
	private String mFlag; // always check mFlag.equals("1") or
							// !mFlag.equals("1")

	private void getDrugNames(final String s) {
		final String url = APIs.URL() + "/prescribeRx/search/name/" + s;
		final StringRequest schedulePlanRequest = new StringRequest(
				Request.Method.POST, url, new Response.Listener<String>() {
					@Override
					public void onResponse(final String response) {
						Log.i("Drug Names", response);
						try {
							Type listType = new TypeToken<List<MedicineAutoSuggest>>() {
							}.getType();
							ArrayList<MedicineAutoSuggest> arrMed = new Gson()
									.fromJson(response, listType);
							for (MedicineAutoSuggest med : arrMed) {
								if (!arrDrugs.contains(med.getDisplayName())) {
									adapter.add(med);
									arrDrugsName.add(med);
									arrDrugs.add(med.getDisplayName());
								}

							}
							// arrDrugsName.clear();
							// final JSONArray jArr = new JSONArray(response);
							// for (int i = 0; i < jArr.length(); i++) {
							// final JSONObject jObj = jArr.getJSONObject(i);
							// if (!arrDrugsName.contains(jObj
							// .getString("STR").toLowerCase())) {
							// adapter.add(jObj.getString("STR")
							// .toLowerCase());
							// arrDrugsName.add(jObj.getString("STR")
							// .toLowerCase());
							// }
							//
							// }
							Log.i("", arrDrugsName.toString());
						} catch (final Exception e) {
							Log.e("", e);
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(final VolleyError error) {
						// Toast.makeText(
						// DashboardActivity.this,
						// "There is some error while fetching schedule plan please try again",
						// Toast.LENGTH_SHORT).show();
						//
						// Log.e("Error.Response", error);
					}
				}) {
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				final HashMap<String, String> loginParams = new HashMap<String, String>();
				// loginParams.put("auth-token", Util.getBase64String(sharedPref
				// .getString(Constants.USER_TOKEN, "")));
				return loginParams;
			}

			@Override
			protected Map<String, String> getParams() {
				final HashMap<String, String> loginParams = new HashMap<String, String>();
				loginParams.put("format", "json");
				return loginParams;
			}

		};
		schedulePlanRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 5,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		EzApp.mVolleyQueue.add(schedulePlanRequest);

	}

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_prescription);
		mController = new OrderSetsController();

		mFlag = ConfigController
				.getConfig(ConfigController.PRESCRIPTION_REQUIRED);
		Log.i("AddPrescription::onCreate()", "mFlag=" + mFlag);

		final SharedPreferences sharedPref = getApplicationContext()
				.getSharedPreferences(Constants.EZ_SHARED_PREF,
						Context.MODE_PRIVATE);
		if (sharedPref.getString(Constants.DR_SPECIALITY, "dentist")
				.equalsIgnoreCase("dentist")) {
			pres = AddDentistNotesActivity.visitNotes.dentistPlanModel.prescription;
			aptModel = AddDentistNotesActivity.appointment;
			patientModel = AddDentistNotesActivity.patientModel;
			hashVitals = AddDentistNotesActivity.visitNotes.dentistExaminationModel.hashVitals;

		} else {
			pres = PhysicianSoapActivityMain.physicianVisitNotes.physicianPlanModel.prescription;
			aptModel = PhysicianSoapActivityMain.Appointment;
			patientModel = PhysicianSoapActivityMain.patientModel;
			hashVitals = PhysicianSoapActivityMain.physicianVisitNotes.examinationModel.hashVitals;
		}
		prescription.arrMedicine.clear();
		for (MedicineModel model : pres.arrMedicine) {
			prescription.arrMedicine.add(model);
		}
		prescription.si.put("si", pres.si.get("si"));
		listPrescription = (ListView) findViewById(R.id.list_prescription);
		final View footerView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE))
				.inflate(R.layout.footer_prescription, null, false);
		listPrescription.addFooterView(footerView);
		final View headerView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE))
				.inflate(R.layout.header_prescription, null, false);
		listPrescription.addHeaderView(headerView);
		btnSubmit = (Button) findViewById(R.id.btn_submit);
		cbRefills = (CheckBox) findViewById(R.id.cb_refills);
		cbRefills.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				editRefillsTime.setEnabled(isChecked);
				if (isChecked) {
					editRefillsTime.setBackgroundColor(Color
							.parseColor("#ffffff"));
				} else {
					editRefillsTime.setBackgroundColor(Color
							.parseColor("#C0C0C0"));
					editRefillsTime.setText("");
				}

			}
		});
		for (PatientShow p : DashboardActivity.arrPatientShow) {
			if (p.getPId().equalsIgnoreCase(aptModel.getPid())
					&& p.getPfId().equalsIgnoreCase(aptModel.getPfId()))
				((TextView) findViewById(R.id.txt_name)).setText(p.getPfn()
						+ " " + p.getPln() + " , " + p.getAge() + "/"
						+ p.getGender());
		}

		try {
			final SimpleDateFormat sdf = new SimpleDateFormat(
					"EEE, MMM dd, yyyy");
			final Date date;
			if (sharedPref.getString(Constants.DR_SPECIALITY, "")
					.equalsIgnoreCase("Dentist")) {
				date = AddDentistNotesActivity.appointment.aptDate;
			} else {
				date = PhysicianSoapActivityMain.Appointment.aptDate;
			}

			((TextView) findViewById(R.id.txt_date)).setText(sdf.format(date));
		} catch (Exception e) {
			e.printStackTrace();
		}
		editTimes = (EditText) findViewById(R.id.edit_time);
		showQuantity(editTimes);
		editName = (AutoCompleteTextView) findViewById(R.id.edit_drug_name);
		final Point pointSize = new Point();
		getWindowManager().getDefaultDisplay().getSize(pointSize);
		editName.setDropDownWidth(pointSize.x);

		actvOrderSet = (AutoCompleteTextView) findViewById(R.id.actv_order_set);
		final Point pointSize1 = new Point();
		getWindowManager().getDefaultDisplay().getSize(pointSize1);
		actvOrderSet.setDropDownWidth(pointSize1.x);

		btnAddOrderSet = (Button) findViewById(R.id.btn_add_order_set);
		btnAddOrderSet.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// selectedPrescription;

				EzUtils.showLong("Called");
				if (selectedPrescription == null) {
					EzUtils.showLong("Nothing to add");
					return;
				}
				ArrayList<MedicineModel> ms = selectedPrescription.arrMedicine;
				EzUtils.showLong("Length : " + ms.size());

				for (int i = 0; i < ms.size(); ++i) {
					final MedicineModel medicine = ms.get(i);
					prescription.arrMedicine.add(0, medicine);
					EzUtils.showLong("Adding : " + i);
				}

				prescription.si.putAll(selectedPrescription.si);
				adapterMedicine.notifyDataSetChanged();
				actvOrderSet.setText("");
			}
		});

		String mandatory = (mFlag.equals("1") ? "*" : "");

		((TextView) findViewById(R.id.txt_drug_name)).append(Html
				.fromHtml("<sup><font color='#EE0000'>*</font></sup>"));
		((TextView) findViewById(R.id.txt_strength)).append(Html
				.fromHtml("<sup><font color='#EE0000'>" + mandatory
						+ "</font></sup>"));
		((TextView) findViewById(R.id.txt_unit)).append(Html
				.fromHtml("<sup><font color='#EE0000'>" + mandatory
						+ "</font></sup>"));
		((TextView) findViewById(R.id.txt_route)).append(Html
				.fromHtml("<sup><font color='#EE0000'>" + mandatory
						+ "</font></sup>"));
		((TextView) findViewById(R.id.txt_frequency)).append(Html
				.fromHtml("<sup><font color='#EE0000'>" + mandatory
						+ "</font></sup>"));
		((TextView) findViewById(R.id.txt_quantity)).append(Html
				.fromHtml("<sup><font color='#EE0000'>" + mandatory
						+ "</font></sup>"));
		((TextView) findViewById(R.id.txt_formulations)).append(Html
				.fromHtml("<sup><font color='#EE0000'>" + mandatory
						+ "</font></sup>"));
		((TextView) findViewById(R.id.txt_quantity_2)).append(Html
				.fromHtml("<sup><font color='#EE0000'>" + mandatory
						+ "</font></sup>"));
		adapter = new MedicineAutoSuggestAdapter(AddPrescriptionActivity.this,
				android.R.layout.simple_spinner_dropdown_item, arrDrugsName,
				true);
		adapter.setNotifyOnChange(true);
		editName.setAdapter(adapter);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		editStrength = (EditText) findViewById(R.id.edit_strength);
		final ArrayList<String> arrUnit = new ArrayList<String>();
		arrUnit.clear();
		arrUnit.add("mg");
		arrUnit.add("gm");
		arrUnit.add("mcg");
		arrUnit.add("ML");
		arrUnit.add("Teaspoon");
		arrUnit.add("MG/ML");
		arrUnit.add("MG/ACTUAT");
		arrUnit.add("MG/MG");
		arrUnit.add("MG/HR");
		arrUnit.add("MCI/ML");
		arrUnit.add("UNT");
		arrUnit.add("UNT/ML");
		arrUnit.add("UNT/MG");
		arrUnit.add("UNT/ACTUAT");
		arrUnit.add("MEQ/ML");
		arrUnit.add("CELLS/ML");
		arrUnit.add("MEQ");
		arrUnit.add("%");
		arrUnit.add("BAU/ML");
		arrUnit.add("AU/ML");
		final ArrayAdapter<String> adapterUnit = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, arrUnit);
		adapterUnit
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		actvUnit = (AutoCompleteTextView) findViewById(R.id.actv_unit);
		actvUnit.setAdapter(adapterUnit);
		actvUnit.setDropDownWidth(300);

		final ArrayList<String> arrRoute = new ArrayList<String>();
		arrRoute.clear();
		arrRoute.add("Oral");
		arrRoute.add("Sublingual(Under the Tounge)");
		arrRoute.add("Buccal(between the gums and teeth)");
		arrRoute.add("Injection");
		arrRoute.add("Injection - Subcutaneous");
		arrRoute.add("Injection - Intramuscular");
		arrRoute.add("Injection - Intravenous");
		arrRoute.add("Injection - Transdermal");
		arrRoute.add("Injection - Implantation");
		arrRoute.add("Rectal");
		arrRoute.add("Vaginal");
		arrRoute.add("Ocular(Placed in the eye)");
		arrRoute.add("Otic(ear)");
		arrRoute.add("Nasal");
		arrRoute.add("PO");
		arrRoute.add("Inhalation");
		arrRoute.add("Nebulization");
		arrRoute.add("Cutaneous");
		arrRoute.add("Transdermal");
		actvRoute = (AutoCompleteTextView) findViewById(R.id.actv_route);
		final ArrayAdapter<String> adapterRoute = new ArrayAdapter<String>(
				this, android.R.layout.simple_spinner_item, arrRoute);
		adapterRoute
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		actvRoute.setAdapter(adapterRoute);
		actvRoute.setDropDownWidth(600);

		final ArrayList<String> arrFormulations = new ArrayList<String>();
		arrFormulations.clear();
		arrFormulations.add("Cap");
		arrFormulations.add("Cream");
		arrFormulations.add("Douche");
		arrFormulations.add("Drug Implant");
		arrFormulations.add("EC Cap");
		arrFormulations.add("EC Tab");
		arrFormulations.add("Enema");
		arrFormulations.add("Foam");
		arrFormulations.add("Gel");
		arrFormulations.add("Granules");
		arrFormulations.add("Gum");
		arrFormulations.add("Irrig Sol");
		arrFormulations.add("Lotion");
		arrFormulations.add("Lozenge");
		arrFormulations.add("Medicated Bar Soap");
		arrFormulations.add("Medicated Liquid Soap");
		arrFormulations.add("Medicated Shampoo");
		arrFormulations.add("Ointment");
		arrFormulations.add("Paste");
		arrFormulations.add("Powder");
		arrFormulations.add("Spray");
		arrFormulations.add("Sol");
		arrFormulations.add("Susp");
		arrFormulations.add("Suppository");
		arrFormulations.add("Tab");
		arrFormulations.add("XR Cap");
		arrFormulations.add("XR Tab");
		arrFormulations.add("24 Hrs XR Tab");
		arrFormulations.add("24 Hrs Patch");
		arrFormulations.add("72 Hrs Patch");

		actvFormulations = (AutoCompleteTextView) findViewById(R.id.actv_formulations);
		final ArrayAdapter<String> adapterFormulations = new ArrayAdapter<String>(
				AddPrescriptionActivity.this,
				android.R.layout.simple_spinner_item, arrFormulations);
		adapterFormulations
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		actvFormulations.setAdapter(adapterFormulations);
		actvFormulations.setDropDownWidth(500);

		editFrequency = (AutoCompleteTextView) findViewById(R.id.edit_frequency);
		showQuantity(editFrequency);
		editFrequency.setDropDownWidth(500);
		InputStream fis;
		try {
			fis = getAssets().open("frequency.txt");
			final StringBuffer fileContent = new StringBuffer("");

			final byte[] buffer = new byte[1024];

			while (fis.read(buffer) != -1) {
				fileContent.append(new String(buffer));
			}
			final String s = String.valueOf(fileContent);
			Log.i("", s);
			final JSONArray jsonArray = new JSONArray(s);
			arrFreq.clear();
			arrFrequency.clear();
			for (int i = 0; i < jsonArray.length(); i++) {
				Frequency freq = new Gson().fromJson(jsonArray.getJSONObject(i)
						.toString(), Frequency.class);
				arrFrequency.add(freq.getFrequencyName());
				arrFreq.put(freq.getFrequencyName(), freq);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		final ArrayAdapter<String> adapterFrequency = new ArrayAdapter<String>(
				this, android.R.layout.simple_spinner_dropdown_item,
				arrFrequency);
		adapterFrequency
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		editFrequency.setAdapter(adapterFrequency);
		editQuantity = (EditText) findViewById(R.id.edit_quantity);
		editRefillsTime = (EditText) findViewById(R.id.edit_refill_times);
		editRefillsTime.setEnabled(false);
		editNotes = (EditText) findViewById(R.id.edit_notes);
		btnAddMedicine = (TextView) findViewById(R.id.btn_add_medicine);
		adapterMedicine = new MedicineAdapter(this, R.layout.row_medicine,
				prescription.arrMedicine, true);
		listPrescription.setAdapter(adapterMedicine);

		final TextView btnUpdateMedicine = (TextView) findViewById(R.id.btn_update_medicine);

		listPrescription.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				selected_pos = arg2 - 1;
				MedicineModel med = adapterMedicine.getItem(arg2 - 1);
				editName.setText(med.name);
				editStrength.setText(med.strength);
				actvUnit.setText(med.unit);
				actvFormulations.setText(med.formulations);
				actvRoute.setText(med.route);
				editFrequency.setText(med.frequency);
				editTimes.setText(med.times);
				editQuantity.setText(med.quantity);
				editNotes.setText(med.notes);
				if (med.refills.equalsIgnoreCase("yes")) {
					editRefillsTime.setText(med.refillsTime);
					cbRefills.setChecked(true);
				} else {
					editRefillsTime.setText("");
					cbRefills.setChecked(false);
				}
				btnUpdateMedicine.setVisibility(View.VISIBLE);
			}
		});

		btnUpdateMedicine.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				final MedicineModel medicine = new MedicineModel();

				adapterMedicine.remove(adapterMedicine.getItem(selected_pos));
				adapterMedicine.insert(medicine, selected_pos);

				medicine.name = editName.getText().toString();
				medicine.strength = editStrength.getText().toString();
				medicine.unit = actvUnit.getText().toString();
				medicine.formulations = actvFormulations.getText().toString();
				medicine.route = actvRoute.getText().toString();
				medicine.frequency = editFrequency.getText().toString();
				medicine.quantity = editQuantity.getText().toString();
				medicine.times = editTimes.getText().toString();
				medicine.refillsTime = editRefillsTime.getText().toString();
				medicine.notes = editNotes.getText().toString();
				try {
					int i = Integer.parseInt(medicine.quantity)
							* Integer.parseInt(arrFreq.get(medicine.frequency)
									.getFrequencyMultiplier());
					medicine.rf = i
							+ " "
							+ arrFreq.get(medicine.frequency)
									.getFrequencyPeriod();
				} catch (Exception e) {
					medicine.rf = "";
				}
				if (cbRefills.isChecked()) {
					medicine.refills = "Yes";
				} else {
					medicine.refills = "No";
				}
				actvFormulations.setText("");
				editName.setText("");
				editStrength.setText("");
				actvUnit.setText("");
				actvRoute.setText("");
				editFrequency.setText("");
				editQuantity.setText("");
				editTimes.setText("");
				editRefillsTime.setText("");
				editNotes.setText("");
				cbRefills.setChecked(false);
				adapterMedicine.notifyDataSetChanged();
				btnUpdateMedicine.setVisibility(View.GONE);
			}
		});

		editName.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				MedicineAutoSuggest med = adapter.getItem(arg2);
				editStrength.setText(med.getStrength());
				actvUnit.setText(med.getUnitType());
				actvFormulations.setText(med.getFormulations());
				actvRoute.setText(med.getRoute());
				editFrequency.setText(med.getFrequency());
				editTimes.setText(med.getNumberOfDays());
				editQuantity.setText(med.getQuantity());
				editNotes.setText(med.getNotes());
				if (med.getRefills().equalsIgnoreCase("true")) {
					editRefillsTime.setText(med.getRefillsTimes());
					cbRefills.setChecked(true);
				} else {
					editRefillsTime.setText("");
					cbRefills.setChecked(false);
				}

			}
		});
		btnAddMedicine.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(final View v) {
				final Dialog loadDialog = Util
						.showLoadDialog(AddPrescriptionActivity.this);

				if (!mFlag.equals("1")
						|| !Util.isEmptyString(actvFormulations.getText()
								.toString())) {
					if (!Util.isEmptyString(editName.getText().toString())) {
						if (!mFlag.equals("1")
								|| !Util.isEmptyString(editStrength.getText()
										.toString())) {
							if (!mFlag.equals("1")
									|| !Util.isEmptyString(actvUnit.getText()
											.toString())) {
								if (!mFlag.equals("1")
										|| !Util.isEmptyString(actvRoute
												.getText().toString())) {
									if (!mFlag.equals("1")
											|| !Util.isEmptyString(editFrequency
													.getText().toString())) {
										if (!mFlag.equals("1")
												|| !Util.isEmptyString(editTimes
														.getText().toString())) {
											if (!mFlag.equals("1")
													|| Util.isNumeric(editTimes
															.getText()
															.toString())) {

												if (!cbRefills.isChecked()
														|| !Util.isEmptyString(editRefillsTime
																.getText()
																.toString())) {
													if (!cbRefills.isChecked()
															|| Util.isNumeric(editRefillsTime
																	.getText()
																	.toString())) {
														final MedicineModel medicine = new MedicineModel();
														medicine.name = editName
																.getText()
																.toString();
														medicine.strength = editStrength
																.getText()
																.toString();
														medicine.unit = actvUnit
																.getText()
																.toString();
														medicine.formulations = actvFormulations
																.getText()
																.toString();
														medicine.route = actvRoute
																.getText()
																.toString();
														medicine.frequency = editFrequency
																.getText()
																.toString();
														medicine.quantity = editQuantity
																.getText()
																.toString();
														medicine.times = editTimes
																.getText()
																.toString();
														medicine.refillsTime = editRefillsTime
																.getText()
																.toString();
														medicine.notes = editNotes
																.getText()
																.toString();
														try {
															int i = Integer
																	.parseInt(medicine.quantity)
																	* Integer
																			.parseInt(arrFreq
																					.get(medicine.frequency)
																					.getFrequencyMultiplier());
															medicine.rf = i
																	+ " "
																	+ arrFreq
																			.get(medicine.frequency)
																			.getFrequencyPeriod();
														} catch (Exception e) {
															medicine.rf = "";
														}
														if (cbRefills
																.isChecked()) {
															medicine.refills = "Yes";
														} else {
															medicine.refills = "No";
														}
														actvFormulations
																.setText("");
														editName.setText("");
														editStrength
																.setText("");
														actvUnit.setText("");
														actvRoute.setText("");
														editFrequency
																.setText("");
														editQuantity
																.setText("");
														editTimes.setText("");
														editRefillsTime
																.setText("");
														editNotes.setText("");
														prescription.arrMedicine
																.add(0,
																		medicine);
														cbRefills
																.setChecked(false);
														adapterMedicine
																.notifyDataSetChanged();

													} else {
														Util.Alertdialog(
																AddPrescriptionActivity.this,
																"Only Numeric Value allowed in Refills Time");
													}

												} else {
													Util.Alertdialog(
															AddPrescriptionActivity.this,
															"Please enter Refills Time");
												}

											} else {
												Util.Alertdialog(
														AddPrescriptionActivity.this,
														"Only Numeric Value allowed in Days");
											}

										} else {
											Util.Alertdialog(
													AddPrescriptionActivity.this,
													"Please enter Days");
										}

									} else {
										Util.Alertdialog(
												AddPrescriptionActivity.this,
												"Please enter Frequency");
									}
								} else {
									Util.Alertdialog(
											AddPrescriptionActivity.this,
											"Please enter Route");
								}
							} else {
								Util.Alertdialog(AddPrescriptionActivity.this,
										"Please enter Unit Type");
							}

						} else {
							Util.Alertdialog(AddPrescriptionActivity.this,
									"Please enter Strength");
						}

					} else {
						Util.Alertdialog(AddPrescriptionActivity.this,
								"Please enter Drug Name");
					}
				} else {
					Util.Alertdialog(AddPrescriptionActivity.this,
							"Please enter Formulation");
				}
				loadDialog.dismiss();

			}

		});
		btnSubmit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(final View v) {
				final Dialog loadDialog = Util
						.showLoadDialog(AddPrescriptionActivity.this);
				String flag = "0";

				if (Util.isEmptyString(((EditText) findViewById(R.id.edit_drug_name))
						.getText().toString())) {
					pres.arrMedicine.clear();
					for (MedicineModel model : prescription.arrMedicine) {
						pres.arrMedicine.add(model);
					}
					pres.si.put("si", prescription.si.get("si"));
					finish();
				} else if (!mFlag.equals("1")
						|| !Util.isEmptyString(actvFormulations.getText()
								.toString())) {
					if (!Util.isEmptyString(editName.getText().toString())) {
						if (!mFlag.equals("1")
								|| !Util.isEmptyString(editStrength.getText()
										.toString())) {
							if (!mFlag.equals("1")
									|| !Util.isEmptyString(actvUnit.getText()
											.toString())) {
								if (!mFlag.equals("1")
										|| !Util.isEmptyString(actvRoute
												.getText().toString())) {
									if (!mFlag.equals("1")
											|| !Util.isEmptyString(editFrequency
													.getText().toString())) {
										if (!mFlag.equals("1")
												|| !Util.isEmptyString(editTimes
														.getText().toString())) {
											if (!mFlag.equals("1")
													|| Util.isNumeric(editTimes
															.getText()
															.toString())) {
												if (!cbRefills.isChecked()
														|| !Util.isEmptyString(editRefillsTime
																.getText()
																.toString())) {
													if (!cbRefills.isChecked()
															|| Util.isNumeric(editRefillsTime
																	.getText()
																	.toString())) {
														final MedicineModel medicine = new MedicineModel();
														medicine.name = editName
																.getText()
																.toString();
														medicine.strength = editStrength
																.getText()
																.toString();
														medicine.unit = actvUnit
																.getText()
																.toString();
														medicine.formulations = actvFormulations
																.getText()
																.toString();
														medicine.route = actvRoute
																.getText()
																.toString();
														medicine.frequency = editFrequency
																.getText()
																.toString();
														medicine.quantity = editQuantity
																.getText()
																.toString();
														medicine.times = editTimes
																.getText()
																.toString();
														medicine.refillsTime = editRefillsTime
																.getText()
																.toString();
														medicine.notes = editNotes
																.getText()
																.toString();
														try {
															int i = Integer
																	.parseInt(medicine.quantity)
																	* Integer
																			.parseInt(arrFreq
																					.get(medicine.frequency)
																					.getFrequencyMultiplier());
															medicine.rf = i
																	+ " "
																	+ arrFreq
																			.get(medicine.frequency)
																			.getFrequencyPeriod();
														} catch (Exception e) {
															medicine.rf = "";
														}
														if (cbRefills
																.isChecked()) {
															medicine.refills = "Yes";
														} else {
															medicine.refills = "No";
														}
														actvFormulations
																.setText("");
														editName.setText("");
														editStrength
																.setText("");
														actvUnit.setText("");
														actvRoute.setText("");
														editFrequency
																.setText("");
														editQuantity
																.setText("");
														editTimes.setText("");
														editRefillsTime
																.setText("");
														editNotes.setText("");
														prescription.arrMedicine
																.add(0,
																		medicine);
														adapterMedicine
																.notifyDataSetChanged();
														pres.arrMedicine
																.clear();
														for (MedicineModel model : prescription.arrMedicine) {
															pres.arrMedicine
																	.add(model);
														}
														pres.si.put(
																"si",
																prescription.si
																		.get("si"));

													} else {
														Util.Alertdialog(
																AddPrescriptionActivity.this,
																"Only Numeric Value allowed in Refills Time");
													}

												} else {
													Util.Alertdialog(
															AddPrescriptionActivity.this,
															"Please enter Refills Time");
												}
											} else {
												Util.Alertdialog(
														AddPrescriptionActivity.this,
														"Only Numeric Value allowed in Days");
											}

										} else {
											Util.Alertdialog(
													AddPrescriptionActivity.this,
													"Please enter Days");
										}

									} else {
										Util.Alertdialog(
												AddPrescriptionActivity.this,
												"Please enter Frequency");
									}
								} else {
									Util.Alertdialog(
											AddPrescriptionActivity.this,
											"Please enter Route");
								}
							} else {
								Util.Alertdialog(AddPrescriptionActivity.this,
										"Please enter Unit Type");
							}
						} else {
							Util.Alertdialog(AddPrescriptionActivity.this,
									"Please enter Strength");
						}
					} else {
						Util.Alertdialog(AddPrescriptionActivity.this,
								"Please enter Drug Name");
					}
				} else {
					Util.Alertdialog(AddPrescriptionActivity.this,
							"Please enter Formulation");
				}

				// }
				loadDialog.dismiss();

			}

		});
		EditUtils.autoSaveEditText((EditText) findViewById(R.id.edit_note),
				prescription.si);
		editName.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(final Editable s) {

			}

			@Override
			public void beforeTextChanged(final CharSequence s,
					final int start, final int count, final int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onTextChanged(final CharSequence s, final int start,
					final int before, final int count) {
				if (s.length() > 1)
					getDrugNames(s.toString());

			}
		});

		actvOrderSet.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(final Editable s) {

			}

			@Override
			public void beforeTextChanged(final CharSequence s,
					final int start, final int count, final int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onTextChanged(final CharSequence s2, final int start,
					final int before, final int count) {
				theCall(s2.toString());
			}
		});

		actvOrderSet.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				EzUtils.showLong("" + arg2);
				selectedPrescription = mController.getPrescriptionModel(arg2);
				if (selectedPrescription == null) {
					Log.i("APA", "SP is NULL");
					EzUtils.showLong("APA: SP is NULL");
				} else {
					EzUtils.showLong("APA: SP is NOT NULL");
				}
			}
		});
	}

	void theCall(String name) {
		if (name == null)
			name = "";
		Log.i("APA", "Name :" + name);

		// TEST CODE - TO BE DELETED : START
		Map<String, String> params = new HashMap<String, String>();
		params.put("type", "prescription");
		params.put("limit", "100");
		params.put("name", name);
		params.put(EzNetwork.RAW_RESPONSE_JSONARRAY, "1");
		mController.getOrderSets(params, new UpdateListner() {

			@Override
			public void onDataUpdateFinished(int page) {
				// TODO Auto-generated method stub
				Log.i("OrderSets", "Finished : " + page);
			}

			@Override
			public void onDataUpdateError(int page) {
				// TODO Auto-generated method stub
				Log.i("OrderSets", "Error : " + page);
			}

			@Override
			public void onDataUpdate(int page, List<?> records) {
				// TODO Auto-generated method stub

				// get sets
				for (int i = 0; i < records.size(); ++i) {
					Log.i("OrderSets",
							"Set " + i + " : "
									+ new Gson().toJson(records.get(i)));
				}

				// get names
				// final List<String> names = controller
				// .getOrderSetNames(OrderSetsController.ORDER_SET_PRESCRIPTION);
				// for (int i = 0; i < names.size(); ++i) {
				// Log.i("OrderSets", "Name " + i + " : " + names.get(i));
				//
				// }

				final List<String> names = mController
						.getOrderSetNames(OrderSetsController.ORDER_SET_PRESCRIPTION);
				// final ArrayList<String> arrOrderSets = new
				// ArrayList<String>();
				// for (int i = 0; i < names.size(); ++i) {
				// Log.i("APA", "S-Names :" + names.get(i));
				// arrOrderSets.add(names.get(i));
				// }
				final ArrayList<String> arrOrderSets = new ArrayList<String>(
						names);
				// arrOrderSets.addAll(names);

				final ArrayAdapter<String> adapterOrderSets = new ArrayAdapter<String>(
						AddPrescriptionActivity.this,
						android.R.layout.simple_spinner_item, arrOrderSets);
				adapterOrderSets
						.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				actvOrderSet.setAdapter(adapterOrderSets);
			}

			@Override
			public void onCmdResponse(JSONObject response, String result) {
				// TODO Auto-generated method stub
				Log.i("OrderSets", "Response : " + response.toString());
			}
		});
		// TEST CODE : END

	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		// Handle presses on the action bar items
		Intent intent;
		switch (item.getItemId()) {

		case android.R.id.home:
			onBackPressed();
			return true;

		case R.id.print:

			try {
				intent = new Intent(this, PrintPrescriptionActivity.class);
				intent.putExtra("position",
						getIntent().getIntExtra("position", 0));
				startActivity(intent);
			} catch (Exception e) {

			}
			return true;
		case R.id.action_close:
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
		default:
			return super.onOptionsItemSelected(item);
		}

	}

	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		// Inflate the menu items for use in the action bar
		final MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.lab_order, menu);
		return super.onCreateOptionsMenu(menu);
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

	private void showQuantity(EditText editTimes) {
		editTimes.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				try {
					if (!arrFreq
							.containsKey(editFrequency.getText().toString())
							|| AddPrescriptionActivity.this.editTimes.getText()
									.length() < 1) {
						editQuantity.setText("0");
					} else {
						int i = Integer
								.parseInt(AddPrescriptionActivity.this.editTimes
										.getText().toString())
								* Integer.parseInt(arrFreq.get(
										editFrequency.getText().toString())
										.getFrequencyQuantity());
						editQuantity.setText("" + i);
					}
				} catch (Exception e) {

				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});
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
		for (final MedicineModel doc : prescription.arrMedicine) {

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
