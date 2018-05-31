package com.ezhealthtrack.activity;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
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
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.ezhealthrack.api.APIs;
import com.ezhealthtrack.EzApp;
import com.ezhealthtrack.R;
import com.ezhealthtrack.adapter.MedicineAdapter;
import com.ezhealthtrack.controller.PatientController;
import com.ezhealthtrack.controller.PatientController.OnResponsePatient;
import com.ezhealthtrack.greendao.Appointment;
import com.ezhealthtrack.greendao.Patient;
import com.ezhealthtrack.labs.controller.OrderController.OnResponseData;
import com.ezhealthtrack.model.Frequency;
import com.ezhealthtrack.model.MedicineModel;
import com.ezhealthtrack.model.PatientShow;
import com.ezhealthtrack.model.PrescriptionModel;
import com.ezhealthtrack.one.EzUtils;
import com.ezhealthtrack.util.Constants;
import com.ezhealthtrack.util.EzActivity;
import com.ezhealthtrack.util.Log;
import com.ezhealthtrack.util.Util;
import com.ezhealthtrack.views.EditUtils;
import com.google.gson.Gson;

public class PrescriptionActivity extends EzActivity {
	private Appointment aptModel;
	private PrescriptionModel prescription = new PrescriptionModel();
	private Patient patientModel;
	private Button btnSubmit;
	private TextView btnOrderSet;
	private ListView listPrescription;
	private AutoCompleteTextView actvUnit;
	private AutoCompleteTextView actvRoute;
	private AutoCompleteTextView editFrequency;
	private AutoCompleteTextView editName;
	private EditText editOrderSet;
	private EditText editTimes;
	private EditText editQuantity;
	private EditText editRefillsTime;
	private EditText editStrength;
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
	private final ArrayList<String> arrDrugsName = new ArrayList<String>();
	private ArrayAdapter<String> adapter;

	private void getDrugNames(final String s) {
		final String url = APIs.DRUG_NAME() + s;
		final StringRequest schedulePlanRequest = new StringRequest(
				Request.Method.POST, url, new Response.Listener<String>() {
					@Override
					public void onResponse(final String response) {
						// Log.i("Drug Names", response);
						try {
							final JSONArray jArr = new JSONArray(response);
							arrDrugsName.clear();
							for (int i = 0; i < jArr.length(); i++) {
								final JSONObject jObj = jArr.getJSONObject(i);
								if (!arrDrugsName.contains(jObj
										.getString("STR").toLowerCase()))
									arrDrugsName.add(jObj.getString("STR")
											.toLowerCase());

							}
							adapter = new ArrayAdapter<String>(
									PrescriptionActivity.this,
									android.R.layout.simple_spinner_dropdown_item,
									arrDrugsName);
							// arrDrugsName.add("aspddssssssssssssssssssssssss");
							editName.setAdapter(adapter);
							if (editName.hasFocus())
								editName.showDropDown();

							adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
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
		this.setDisplayHomeAsUpEnabled(true);

		if (!EzUtils.getDeviceSize(this).equals(EzUtils.EZ_SCREEN_LARGE)) {
			mScreenRotation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
		}

		if (Util.isEmptyString(getIntent().getStringExtra("from")))
			aptModel = DashboardActivity.arrConfirmedPatients.get(getIntent()
					.getIntExtra("position", 0));
		else
			aptModel = DashboardActivity.arrHistoryPatients.get(getIntent()
					.getIntExtra("position", 0));
		PatientController.getPatient(aptModel.getPid(), aptModel.getPfId(),
				this, new OnResponsePatient() {

					@Override
					public void onResponseListner(Patient response) {
						patientModel = response;

					}
				});
		listPrescription = (ListView) findViewById(R.id.list_prescription);
		final View footerView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE))
				.inflate(R.layout.footer_prescription, null, false);
		listPrescription.addFooterView(footerView);
		final View headerView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE))
				.inflate(R.layout.header_prescription, null, false);
		listPrescription.addHeaderView(headerView);
		btnSubmit = (Button) findViewById(R.id.btn_submit);
		for (PatientShow p : DashboardActivity.arrPatientShow) {
			if (p.getPId().equalsIgnoreCase(aptModel.getPid())
					&& p.getPfId().equalsIgnoreCase(aptModel.getPfId()))
				((TextView) findViewById(R.id.txt_name)).setText(p.getPfn()
						+ " " + p.getPln() + " , " + p.getAge() + "/"
						+ p.getGender());
		}
		final SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM dd, yyyy");
		final Date date;

		date = aptModel.aptDate;

		((TextView) findViewById(R.id.txt_date)).setText(sdf.format(date));
		editName = (AutoCompleteTextView) findViewById(R.id.edit_drug_name);
		final Point pointSize = new Point();
		getWindowManager().getDefaultDisplay().getSize(pointSize);

		editOrderSet = (EditText) findViewById(R.id.actv_order_set);
		editOrderSet.setVisibility(View.GONE);

		btnOrderSet = (TextView) findViewById(R.id.btn_add_order_set);
		btnOrderSet.setVisibility(View.GONE);

		editName.setDropDownWidth(pointSize.x);
		editName.setVisibility(View.GONE);

		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, arrDrugsName);
		editStrength = (EditText) findViewById(R.id.edit_strength);
		editStrength.setVisibility(View.GONE);
		editTimes = (EditText) findViewById(R.id.edit_time);
		editTimes.setVisibility(View.GONE);
		editRefillsTime = (EditText) findViewById(R.id.edit_refill_times);
		editRefillsTime.setVisibility(View.GONE);
		editNotes = (EditText) findViewById(R.id.edit_notes);
		editNotes.setVisibility(View.GONE);
		cbRefills = (CheckBox) findViewById(R.id.cb_refills);
		cbRefills.setVisibility(View.GONE);
		findViewById(R.id.actv_formulations).setVisibility(View.GONE);
		final ArrayList<String> arrUnit = new ArrayList<String>();
		arrUnit.add("mg");
		arrUnit.add("gm");
		final ArrayAdapter<String> adapterUnit = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_dropdown_item, arrUnit);
		actvUnit = (AutoCompleteTextView) findViewById(R.id.actv_unit);
		actvUnit.setVisibility(View.GONE);
		actvUnit.setAdapter(adapterUnit);
		final ArrayList<String> arrRoute = new ArrayList<String>();
		arrRoute.add("Capsule");
		arrRoute.add("Tablet");
		arrRoute.add("I V");
		arrRoute.add("Injection");
		final ArrayAdapter<String> adapterRoute = new ArrayAdapter<String>(
				this, android.R.layout.simple_spinner_dropdown_item, arrRoute);
		actvRoute = (AutoCompleteTextView) findViewById(R.id.actv_route);
		actvRoute.setAdapter(adapterRoute);
		actvRoute.setVisibility(View.GONE);
		editFrequency = (AutoCompleteTextView) findViewById(R.id.edit_frequency);
		editFrequency.setDropDownWidth(500);
		editFrequency.setVisibility(View.GONE);

		final ArrayList<String> arrFrequency = new ArrayList<String>();
		final ArrayList<Frequency> arrFreq = new ArrayList<Frequency>();
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
			for (int i = 0; i < jsonArray.length(); i++) {
				Frequency freq = new Gson().fromJson(jsonArray.getJSONObject(i)
						.toString(), Frequency.class);
				arrFrequency.add(freq.getFrequencyName());
				arrFreq.add(freq);
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
		editQuantity.setVisibility(View.GONE);
		btnAddMedicine = (TextView) findViewById(R.id.btn_add_medicine);
		btnAddMedicine.setVisibility(View.GONE);
		btnSubmit = (Button) findViewById(R.id.btn_submit);
		btnSubmit.setVisibility(View.GONE);
		adapterMedicine = new MedicineAdapter(this, R.layout.row_medicine,
				prescription.arrMedicine, false);
		listPrescription.setAdapter(adapterMedicine);
		btnAddMedicine.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(final View v) {
				final MedicineModel medicine = new MedicineModel();
				medicine.name = editName.getText().toString();
				editName.setText("");
				medicine.strength = editStrength.getText().toString();
				editStrength.setText("");
				medicine.unit = actvUnit.getText().toString();
				medicine.route = actvRoute.getText().toString();
				medicine.frequency = editFrequency.getText().toString();
				editFrequency.setText("");

				medicine.quantity = editQuantity.getText().toString();
				medicine.times = ((EditText) findViewById(R.id.edit_time))
						.getText().toString();
				medicine.refillsTime = ((EditText) findViewById(R.id.edit_refill_times))
						.getText().toString();
				// medicine.refills = findViewById(R.id.cb_refills);
				editQuantity.setText("");
				prescription.arrMedicine.add(0, medicine);
				adapterMedicine.notifyDataSetChanged();
			}
		});
		btnSubmit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(final View v) {
				submitPrescription();

			}
		});
		EditUtils.autoSaveEditText((EditText) findViewById(R.id.edit_note),
				prescription.si);
		findViewById(R.id.edit_note).setEnabled(false);
		editName.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(final Editable s) {
				if (s.length() > 2)
					getDrugNames(s.toString());

			}

			@Override
			public void beforeTextChanged(final CharSequence s,
					final int start, final int count, final int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onTextChanged(final CharSequence s, final int start,
					final int before, final int count) {
				// TODO Auto-generated method stub

			}
		});
		getPrescription(getIntent().getStringExtra("bkId"));
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		// Handle presses on the action bar items

		switch (item.getItemId()) {

		case android.R.id.home:
			finish();
			return true;

		case R.id.print:
			// try {
			// intent = new Intent(this, PrintPrescriptionActivity.class);
			// intent.putExtra("position",
			// getIntent().getIntExtra("position", 0));
			// startActivity(intent);
			// } catch (Exception e) {
			//
			// }
			dialogPrintPrescription();
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

	private void getPrescription(final String bkId) {
		final String url = APIs.PRESCRIPTION();
		final Dialog loaddialog = Util
				.showLoadDialog(PrescriptionActivity.this);
		Log.i("Prescription Activity", url);
		final StringRequest schedulePlanRequest = new StringRequest(
				Request.Method.POST, url, new Response.Listener<String>() {
					@Override
					public void onResponse(final String response) {
						Log.i("Prescription Activity", response);
						try {
							prescription.JsonParse(new JSONObject(response)
									.getJSONObject("Pres"));
							adapterMedicine.notifyDataSetChanged();
							EditUtils.autoSaveEditText(
									(EditText) findViewById(R.id.edit_note),
									prescription.si);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						loaddialog.dismiss();
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(final VolleyError error) {
						// Toast.makeText(
						// DashboardActivity.this,
						// "There is some error while fetching schedule plan please try again",
						// Toast.LENGTH_SHORT).show();

						Log.e("Error.Response", error);
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
				loginParams.put("format", "json");
				loginParams.put("bk_id", bkId);
				return loginParams;
			}

		};
		schedulePlanRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 5,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		EzApp.mVolleyQueue.add(schedulePlanRequest);

	}

	private void submitPrescription() {
		if (Util.isEmptyString(((EditText) findViewById(R.id.edit_drug_name))
				.getText().toString())) {

		} else {
			final MedicineModel medicine = new MedicineModel();
			medicine.name = editName.getText().toString();
			editName.setText("");
			medicine.strength = editStrength.getText().toString();
			editStrength.setText("");
			medicine.unit = actvUnit.getText().toString();
			medicine.route = actvRoute.getText().toString();
			medicine.frequency = editFrequency.getText().toString();
			editFrequency.setText("");
			medicine.quantity = editQuantity.getText().toString();
			medicine.times = ((EditText) findViewById(R.id.edit_time))
					.getText().toString();
			medicine.refillsTime = ((EditText) findViewById(R.id.edit_refill_times))
					.getText().toString();
			editQuantity.setText("");
			prescription.arrMedicine.add(0, medicine);
			adapterMedicine.notifyDataSetChanged();
		}
		JSONObject pres = new JSONObject();
		prescription.updateJson2(pres);
		final String url = APIs.PRESCRIPTION() + "/bk_id/" + aptModel.getBkid()
				+ "/sid/" + aptModel.getSiid() + "/form/submit/format/json";
		Log.i(url, pres.toString());
		final JsonObjectRequest patientListRequest = new JsonObjectRequest(
				Request.Method.POST, url, pres,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(final JSONObject jObj) {
						Log.i("", jObj.toString());
						String s = jObj.toString();
						while (s.length() > 100) {
							s = s.substring(100);
						}
						try {
							if (jObj.getString("s").equals("200")) {

								Util.AlertdialogWithFinish(
										PrescriptionActivity.this,
										"Prescription updated successfully");

							} else {
								Log.e("", jObj.toString());
								Util.Alertdialog(PrescriptionActivity.this,
										jObj.getString("m"));
							}
						} catch (final JSONException e) {
							Util.Alertdialog(PrescriptionActivity.this,
									e.toString());
							Log.e("", e);
						}
					}

					private View findViewById(int editReason) {
						// TODO Auto-generated method stub
						return null;
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(final VolleyError error) {
						Util.Alertdialog(PrescriptionActivity.this,
								"Network error, try again later");

						Log.e("Error.Response", error);
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
				final HashMap<String, String> params = new HashMap<String, String>();
				params.put("format", "json");
				params.put("bk_id", aptModel.getBkid());
				params.put("sid", aptModel.getSiid());
				params.put("form", "submit");
				return params;
			}

		};
		patientListRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 5,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		EzApp.mVolleyQueue.add(patientListRequest);

	}

	private void dialogPrintPrescription() {
		final Dialog dialog = new Dialog(this, R.style.Theme_AppCompat_Light);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_print_prescription);
		dialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));

		final SimpleDateFormat df = new SimpleDateFormat(" EEE, MMM dd, yyyy");
		final Date date;

		date = aptModel.aptDate;

		String drName = EzApp.sharedPref.getString(
				Constants.DR_NAME, "");
		((TextView) dialog.findViewById(R.id.txt_drname)).setText(drName);
		String drAddress = EzApp.sharedPref.getString(
				Constants.DR_ADDRESS, "");
		((TextView) dialog.findViewById(R.id.txt_doc_address))
				.setText(drAddress + " - "
						+ EditAccountActivity.profile.getZip());
		((TextView) dialog.findViewById(R.id.txt_patient_name)).setText(Html
				.fromHtml("<b>Patient Name :</b> " + patientModel.getPfn()
						+ " " + patientModel.getPln()));
		((TextView) dialog.findViewById(R.id.txt_patient_gender)).setText(Html
				.fromHtml("<b>Gender :</b> " + patientModel.getPgender()));

		((TextView) dialog.findViewById(R.id.txt_patient_age)).setText(Html
				.fromHtml("<b>Age :</b> " + patientModel.getPage()));
		((TextView) dialog.findViewById(R.id.txt_date)).setText(Html
				.fromHtml("<b>Date :</b> " + df.format(date)));
		((TextView) dialog.findViewById(R.id.txt_ptaddress)).setText(Html
				.fromHtml("<b>Address :</b> " + patientModel.getPadd1() + " "
						+ patientModel.getPadd2() + ", "
						+ patientModel.getParea() + ", "
						+ patientModel.getPcity() + ", "
						+ patientModel.getPstate() + ", "
						+ patientModel.getPcountry() + " - "
						+ patientModel.getPzip()));

		((TextView) dialog.findViewById(R.id.txt_prescription)).setText(Html
				.fromHtml("<b>Prescription: </b>"));
		((TextView) dialog.findViewById(R.id.txt_special_notes)).setText(Html
				.fromHtml("<b>Special Instructions :</b> "
						+ prescription.si.get("si")));
		// this section need to be modified {
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
		// }

		PatientController.patientBarcode(PrescriptionActivity.this,
				new OnResponseData() {

					@Override
					public void onResponseListner(Object response) {

						Util.getImageFromUrl(response.toString(),
								DashboardActivity.imgLoader, (ImageView) dialog
										.findViewById(R.id.img_barcode));
					}
				}, patientModel);

		if (!Util.isEmptyString(EzApp.sharedPref.getString(
				Constants.SIGNATURE, "signature"))) {
			Util.getImageFromUrl(EzApp.sharedPref.getString(
					Constants.SIGNATURE, ""), DashboardActivity.imgLoader,
					(ImageView) dialog.findViewById(R.id.img_signature));
		}

		dialog.findViewById(R.id.btn_print).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						View view = dialog.findViewById(R.id.id_scrollview);
						EzUtils.printView(view, "Prescription",
								PrescriptionActivity.this);
					}
				});
		dialog.show();
	}
}
