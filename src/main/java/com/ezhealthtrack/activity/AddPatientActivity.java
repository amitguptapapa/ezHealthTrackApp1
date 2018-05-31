package com.ezhealthtrack.activity;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.text.Editable;
import android.text.Html;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.ezhealthtrack.EzApp;
import com.ezhealthtrack.R;
import com.ezhealthtrack.controller.LocalityController;
import com.ezhealthtrack.controller.PatientController;
import com.ezhealthtrack.controller.PatientController.OnResponsePatientCopy;
import com.ezhealthtrack.greendao.Area;
import com.ezhealthtrack.greendao.City;
import com.ezhealthtrack.greendao.Country;
import com.ezhealthtrack.greendao.Patient;
import com.ezhealthtrack.greendao.State;
import com.ezhealthtrack.model.PatientCopyAddress.DefaultAddress;
import com.ezhealthtrack.util.BaseActivity;
import com.ezhealthtrack.util.Constants;
import com.ezhealthtrack.util.Log;
import com.ezhealthtrack.util.NetworkCallController.OnResponse;
import com.ezhealthtrack.util.Util;

public class AddPatientActivity extends BaseActivity {
	private ActionBar actionBar;
	private Spinner spinnerGender;
	private TextView spinnerCountry;
	private AutoCompleteTextView spinnerState;
	private AutoCompleteTextView spinnerCity;
	private AutoCompleteTextView spinnerLocality;
	private Spinner spinnerId;
	private Spinner spinnerHeight;
	private Spinner spinnerHairColor;
	private Spinner spinnerEyeColor;
	private ArrayList<String> arrGender;
	private ArrayList<String> arrCountry;
	private ArrayList<String> arrState;
	private ArrayList<String> arrCity;
	private ArrayList<String> arrLocality;
	private HashMap<String, State> hashState = new HashMap<String, State>();
	private HashMap<String, City> hashCity = new HashMap<String, City>();
	private HashMap<String, Area> hashLocality = new HashMap<String, Area>();
	private ArrayList<String> arrId;
	private ArrayList<String> arrHeight;
	private ArrayList<String> arrEyeColor;
	private ArrayList<String> arrHairColor;
	private EditText editFirstName;
	private EditText editMiddleName;
	private EditText editLastName;
	private EditText editDob;
	private EditText editEmail;
	private EditText editPhone;
	private EditText editAddress1;
	private EditText editAddress2;
	private EditText editZipcode;
	private EditText editId;
	private EditText editMark;
	private EditText editHeight;
	private TextView txtFirstName;
	private TextView txtLastName;
	private TextView txtGender;
	private TextView txtDOB;
	private TextView txtEmail;
	private TextView txtAddress1;
	private TextView txtCountry;
	private TextView txtState;
	private TextView txtCity;
	private TextView txtLocality;
	private TextView txtZipcode;
	private Button btnAddPatient;
	private final String TAG = this.getClass().getSimpleName();
	private SharedPreferences sharedPref;
	private ArrayAdapter<String> adapterCountry;
	private ArrayAdapter<String> adapterState;
	private ArrayAdapter<String> adapterCity;
	private ArrayAdapter<String> adapterLocality;
	private int age;
	private boolean flag = true;

	private void dummyDataLoad() {
		arrGender.add("Select Gender");
		arrGender.add("Male");
		arrGender.add("Female");
		arrGender.add("Other");
		arrEyeColor.add("Select Eye Color");
		arrEyeColor.add("Blue");
		arrEyeColor.add("Black");
		arrEyeColor.add("Brown");
		arrEyeColor.add("Amber");
		arrEyeColor.add("Gray");
		arrEyeColor.add("Green");
		arrEyeColor.add("Hazel");
		arrEyeColor.add("Red");
		arrEyeColor.add("Violet");
		arrHairColor.add("Select Hair Color");
		arrHairColor.add("Black");
		arrHairColor.add("Brown");
		arrHairColor.add("White");
		arrHairColor.add("Blond");
		arrHairColor.add("Anburn");
		arrHairColor.add("Cheshnut");
		arrHairColor.add("Red");
		arrHairColor.add("Gray");
		arrHeight.add("Select Height");
		for (int i = 12; i < 84; i++) {
			final int foot = i / 12;
			final int inch = i % 12;
			arrHeight.add(foot + "' " + inch + "\"");
		}
		arrHeight.add("Others");
		spinnerHeight.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {
				// TODO Auto-generated method stub
				if (pos == arrHeight.size() - 1) {
					editHeight.setVisibility(View.VISIBLE);
				} else {
					editHeight.setVisibility(View.GONE);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
			}
		});
		arrId.add("Select Id");
		arrId.add("PASSPORT");
		arrId.add("AADHAR CARD");
	}

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_patient);
		actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		sharedPref = getApplicationContext().getSharedPreferences(
				Constants.EZ_SHARED_PREF, Context.MODE_PRIVATE);
		spinnerGender = (Spinner) findViewById(R.id.spinner_gender);
		spinnerCountry = (TextView) findViewById(R.id.spinner_country);
		spinnerState = (AutoCompleteTextView) findViewById(R.id.spinner_state);
		spinnerCity = (AutoCompleteTextView) findViewById(R.id.spinner_city);
		spinnerLocality = (AutoCompleteTextView) findViewById(R.id.spinner_locality);
		spinnerId = (Spinner) findViewById(R.id.spinner_id_type);
		spinnerHeight = (Spinner) findViewById(R.id.spinner_height);
		spinnerEyeColor = (Spinner) findViewById(R.id.spinner_eye_color);
		spinnerHairColor = (Spinner) findViewById(R.id.spinner_hair_color);
		txtFirstName = (TextView) findViewById(R.id.txt_first_name);
		((TextView) findViewById(R.id.txt_first_name)).append(Html
				.fromHtml("<sup><font color='#EE0000'>*</font></sup>"));
		txtLastName = (TextView) findViewById(R.id.txt_last_name);
		((TextView) findViewById(R.id.txt_last_name)).append(Html
				.fromHtml("<sup><font color='#EE0000'>*</font></sup>"));
		txtGender = (TextView) findViewById(R.id.txt_gender);
		((TextView) findViewById(R.id.txt_gender)).append(Html
				.fromHtml("<sup><font color='#EE0000'>*</font></sup>"));
		txtDOB = (TextView) findViewById(R.id.txt_dob);
		((TextView) findViewById(R.id.txt_dob)).append(Html
				.fromHtml("<sup><font color='#EE0000'>*</font></sup>"));
		txtAddress1 = (TextView) findViewById(R.id.txt_address1);
		((TextView) findViewById(R.id.txt_address1)).append(Html
				.fromHtml("<sup><font color='#EE0000'>*</font></sup>"));
		txtCountry = (TextView) findViewById(R.id.txt_country);
		((TextView) findViewById(R.id.txt_country)).append(Html
				.fromHtml("<sup><font color='#EE0000'>*</font></sup>"));
		txtState = (TextView) findViewById(R.id.txt_state);
		((TextView) findViewById(R.id.txt_state)).append(Html
				.fromHtml("<sup><font color='#EE0000'>*</font></sup>"));
		txtCity = (TextView) findViewById(R.id.txt_city);
		((TextView) findViewById(R.id.txt_city)).append(Html
				.fromHtml("<sup><font color='#EE0000'>*</font></sup>"));
		txtLocality = (TextView) findViewById(R.id.txt_locality);
		((TextView) findViewById(R.id.txt_locality)).append(Html
				.fromHtml("<sup><font color='#EE0000'>*</font></sup>"));
		txtZipcode = (TextView) findViewById(R.id.txt_zipcode);
		((TextView) findViewById(R.id.txt_zipcode)).append(Html
				.fromHtml("<sup><font color='#EE0000'>*</font></sup>"));

		editFirstName = (EditText) findViewById(R.id.edit_first_name);
		editLastName = (EditText) findViewById(R.id.edit_last_name);
		editMiddleName = (EditText) findViewById(R.id.edit_middle_name);
		editDob = (EditText) findViewById(R.id.edit_dob);
		editEmail = (EditText) findViewById(R.id.edit_email);
		editPhone = (EditText) findViewById(R.id.edit_phone);
		editPhone.setFilters(new InputFilter[] { new InputFilter.LengthFilter(
				10) });
		editHeight = (EditText) findViewById(R.id.edit_other_height);
		editMark = (EditText) findViewById(R.id.edit_mark);
		editAddress1 = (EditText) findViewById(R.id.edit_address1);
		editAddress2 = (EditText) findViewById(R.id.edit_address2);
		editZipcode = (EditText) findViewById(R.id.edit_zipcode);
		editZipcode
				.setFilters(new InputFilter[] { new InputFilter.LengthFilter(6) });
		editId = (EditText) findViewById(R.id.edit_id);
		btnAddPatient = (Button) findViewById(R.id.btn_add_patient);
		arrGender = new ArrayList<String>();
		arrCountry = new ArrayList<String>();
		arrState = new ArrayList<String>();
		arrCity = new ArrayList<String>();
		arrHeight = new ArrayList<String>();
		arrHairColor = new ArrayList<String>();
		arrEyeColor = new ArrayList<String>();
		arrLocality = new ArrayList<String>();
		arrId = new ArrayList<String>();

		PatientController.copyPatientAddress(AddPatientActivity.this,
				new OnResponsePatientCopy() {

					@Override
					public void onResponseListner(DefaultAddress response) {
						// TODO Auto-generated method stub
						editAddress1.setText(response.getParams().getAddress());
						Country country = new Country();
						country.setCOUNTRY_ID("1");
						country.setCOUNTRY_NAME("India");

						spinnerState.setText(response.getParams()
								.getCmbStateText());
						try {
							for (State state : LocalityController.getStateList(
									response.getParams().getCmbStateText()
											.toString(), country)) {
								if (!arrState.contains(state.getSTATE_NAME())) {
									Log.i("", state.getSTATE_NAME());
									hashState.put(state.getSTATE_NAME(), state);
								}
							}
						} catch (Exception e) {

						}

						spinnerCity.setText(response.getParams()
								.getCmbCityText());
						try {
							for (final City city : LocalityController
									.getCityList(response.getParams()
											.getCmbCityText().toString(),
											hashState.get(spinnerState
													.getText().toString()))) {
								if (!arrCity.contains(city.getCITY_NAME())) {
									Log.i("", city.getCITY_NAME());
									hashCity.put(city.getCITY_NAME(), city);

								}
							}
						} catch (Exception e) {

						}

						spinnerLocality.setText(response.getParams()
								.getCmbAreaText());
						try {
							for (final Area area : LocalityController
									.getAreaList(response.getParams()
											.getCmbAreaText().toString(),
											hashCity.get(spinnerCity.getText()
													.toString()))) {
								if (!arrLocality.contains(area.getAREA_NAME())) {
									// Log.i("", city.getCITY_NAME());
									hashLocality.put(area.getAREA_NAME(), area);
								}
							}
						} catch (Exception e) {

						}

						editZipcode.setText(response.getParams().getZip());
					}
				});

		dummyDataLoad();
		final ArrayAdapter<String> adapterGender = new ArrayAdapter<String>(
				this, android.R.layout.simple_spinner_item, arrGender);
		adapterGender
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		final ArrayAdapter<String> adapterHeight = new ArrayAdapter<String>(
				this, android.R.layout.simple_spinner_item, arrHeight);
		adapterHeight
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		final ArrayAdapter<String> adapterHairColor = new ArrayAdapter<String>(
				this, android.R.layout.simple_spinner_item, arrHairColor);
		adapterHairColor
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		final ArrayAdapter<String> adapterEyeColor = new ArrayAdapter<String>(
				this, android.R.layout.simple_spinner_item, arrEyeColor);
		adapterEyeColor
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		adapterCountry = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, arrCountry);
		adapterCountry
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		adapterCountry.setNotifyOnChange(true);
		adapterState = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, arrState);
		adapterState
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		adapterState.setNotifyOnChange(true);
		adapterCity = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, arrCity);
		adapterCity
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		adapterCity.setNotifyOnChange(true);
		adapterLocality = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, arrLocality);
		adapterLocality
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		adapterLocality.setNotifyOnChange(true);
		final ArrayAdapter<String> adapterId = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, arrId);
		adapterId
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerGender.setAdapter(adapterGender);
		spinnerCountry.setText("India");
		spinnerState.setAdapter(adapterState);
		spinnerCity.setAdapter(adapterCity);
		spinnerLocality.setAdapter(adapterLocality);
		spinnerId.setAdapter(adapterId);
		spinnerHeight.setAdapter(adapterHeight);
		spinnerHairColor.setAdapter(adapterHairColor);
		spinnerEyeColor.setAdapter(adapterEyeColor);
		btnAddPatient.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(final View v) {
				if (!Util.isEmptyString(editFirstName.getText().toString())) {
					if (!Util.isEmptyString(editLastName.getText().toString())) {
						if (!Util.isEmptyString(editDob.getText().toString())) {
							if (!Util.isEmptyString(editAddress1.getText()
									.toString())) {
								if (spinnerGender.getSelectedItemPosition() != 0) {
									if (hashState.containsKey(spinnerState
											.getText().toString())) {
										if (hashCity.containsKey(spinnerCity
												.getText().toString())) {
											if (!Util
													.isEmptyString(spinnerLocality
															.getText()
															.toString())) {
												if (!Util
														.isEmptyString(editZipcode
																.getText()
																.toString())) {
													if (Util.isNumeric(editZipcode
															.getText()
															.toString())) {
														if (flag) {
															flag = false;
															final Dialog loaddialog = Util
																	.showLoadDialog(AddPatientActivity.this);
															PatientController
																	.postNewPatientData(
																			AddPatientActivity.this,
																			getPatient(),
																			new OnResponse() {

																				@Override
																				public void onResponseListner(
																						String response) {
																					flag = true;
																					loaddialog
																							.dismiss();

																				}
																			});
														}
													} else {
														Util.Alertdialog(
																AddPatientActivity.this,
																"Only Numeric Value allowed in Pin/Zipcode");
													}

												} else {
													Util.Alertdialog(
															AddPatientActivity.this,
															"Please enter Patient's Pin/Zipcode");
												}
											} else {
												Util.Alertdialog(
														AddPatientActivity.this,
														"Please select Patient's Locality");
											}

										} else {
											Util.Alertdialog(
													AddPatientActivity.this,
													"Please select Patient's City");
										}
									} else {
										Util.Alertdialog(
												AddPatientActivity.this,
												"Please select Patient's State");
									}

								} else {
									Util.Alertdialog(AddPatientActivity.this,
											"Please select Patients's Gender");
								}

							} else {
								Util.Alertdialog(AddPatientActivity.this,
										"Please enter Patient's Address");
							}

						} else {
							Util.Alertdialog(AddPatientActivity.this,
									"Please enter Patient's Date of Birth");

						}

					} else {
						Util.Alertdialog(AddPatientActivity.this,
								"Please enter Patient's Last Name");
					}

				} else {
					Util.Alertdialog(AddPatientActivity.this,
							"Please enter Patient's First Name");
				}
			}

		});

		editDob.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(final View v) {
				final Calendar cal = Calendar.getInstance();
				final DatePickerDialog datepicker = new DatePickerDialog(
						AddPatientActivity.this, new OnDateSetListener() {

							@Override
							public void onDateSet(final DatePicker view,
									final int year, int monthOfYear,
									final int dayOfMonth) {
								if ((dayOfMonth < 10) && (monthOfYear < 9)) {
									editDob.setText("0" + dayOfMonth + "/0"
											+ ++monthOfYear + "/" + year);
								} else if ((dayOfMonth < 10)
										&& (monthOfYear > 8)) {
									editDob.setText("0" + dayOfMonth + "/"
											+ ++monthOfYear + "/" + year);
								} else if ((dayOfMonth > 9)
										&& (monthOfYear < 9)) {
									editDob.setText(dayOfMonth + "/0"
											+ ++monthOfYear + "/" + year);
								} else {
									editDob.setText(dayOfMonth + "/"
											+ ++monthOfYear + "/" + year);
								}
								age = cal.get(Calendar.YEAR) - year;

							}
						}, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal
								.get(Calendar.DAY_OF_MONTH));
				datepicker.show();

			}
		});

		spinnerState.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				spinnerCity.setText("");
				spinnerLocality.setText("");
				hashCity.clear();
				hashLocality.clear();
				arrCity.clear();
				arrLocality.clear();
				adapterCity.clear();
				adapterLocality.clear();
				Country country = new Country();
				country.setCOUNTRY_ID("1");
				country.setCOUNTRY_NAME("India");
				if (!Util.isEmptyString(s.toString()))
					// getStateList(s.toString());
					for (State state : LocalityController.getStateList(
							s.toString(), country)) {
						if (!arrState.contains(state.getSTATE_NAME())) {
							Log.i("", state.getSTATE_NAME());
							hashState.put(state.getSTATE_NAME(), state);
							// arrState.add(state.getSTATE_NAME());
							adapterState.remove(state.getSTATE_NAME());
							adapterState.add(state.getSTATE_NAME());
						}

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

		spinnerCity.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(final CharSequence s, int start,
					int before, int count) {
				spinnerLocality.setText("");
				hashLocality.clear();
				arrLocality.clear();
				adapterLocality.clear();
				if (!Util.isEmptyString(s.toString())
						&& !Util.isEmptyString(spinnerState.getText()
								.toString()))
					// getCityList(s.toString());
					new Thread(new Runnable() {
						public void run() {
							for (final City city : LocalityController
									.getCityList(s.toString(), hashState
											.get(spinnerState.getText()
													.toString()))) {
								if (!arrCity.contains(city.getCITY_NAME())) {
									Log.i("", city.getCITY_NAME());
									hashCity.put(city.getCITY_NAME(), city);
									arrCity.add(city.getCITY_NAME());
									spinnerCity.post(new Runnable() {

										@Override
										public void run() {
											adapterCity.add(city.getCITY_NAME());

										}
									});

								}

							}
						}
					}).start();

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
		spinnerLocality.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(final CharSequence s, int start,
					int before, int count) {
				if (!Util.isEmptyString(s.toString())
						&& !Util.isEmptyString(spinnerCity.getText().toString()))
					// getLocalityList(s.toString());
					new Thread(new Runnable() {
						public void run() {
							for (final Area area : LocalityController
									.getAreaList(s.toString(), hashCity
											.get(spinnerCity.getText()
													.toString()))) {
								if (!arrLocality.contains(area.getAREA_NAME())) {
									// Log.i("", city.getCITY_NAME());
									hashLocality.put(area.getAREA_NAME(), area);
									arrLocality.add(area.getAREA_NAME());
									spinnerLocality.post(new Runnable() {

										@Override
										public void run() {
											adapterLocality.add(area
													.getAREA_NAME());

										}
									});

								}

							}
						}
					}).start();
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
		Util.setupUI(this, findViewById(R.id.btn_add_patient));
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

	private Patient getPatient() {
		final Patient pateint = new Patient();
		pateint.setPfn(editFirstName.getText().toString());
		pateint.setPmn(editMiddleName.getText().toString());
		pateint.setPln(editLastName.getText().toString());
		pateint.setPage("" + age);
		try {
			pateint.setPdob(EzApp.sdfyyMmdd
					.format(EzApp.sdfddMmyy.parse(editDob
							.getText().toString())));
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		pateint.setPmobnum(editPhone.getText().toString());
		pateint.setPemail(editEmail.getText().toString());
		pateint.setPadd1(editAddress1.getText().toString());
		pateint.setPadd2(editAddress2.getText().toString());
		pateint.setPzip(editZipcode.getText().toString());
		if (spinnerHeight.getSelectedItemPosition() != 0
				&& spinnerHeight.getSelectedItemPosition() != arrHeight.size() - 1) {
			pateint.setHeight(spinnerHeight.getSelectedItem().toString()
					.replaceAll(" ", "").replaceAll("'", "")
					.replaceAll("\"", ""));
		} else if (spinnerHeight.getSelectedItemPosition() == 0) {
			pateint.setHeight("");
		} else if (spinnerHeight.getSelectedItemPosition() == (arrHeight.size() - 1)) {
			pateint.setHeight(editHeight.getText().toString());
		}
		pateint.setVisiblemark(editMark.getText().toString());
		pateint.setEyecolor(spinnerEyeColor.getSelectedItem().toString());
		pateint.setHaircolor(spinnerHairColor.getSelectedItem().toString());
		pateint.setPgender((String) spinnerGender.getSelectedItem());

		pateint.setPcountry("India");
		pateint.setPcountryid("1");
		try {
			if (!Util.isEmptyString(spinnerState.getText().toString())) {
				pateint.setPstate(spinnerState.getText().toString());
				pateint.setPstateid(hashState.get(pateint.getPstate())
						.getSTATE_ID());
			}
			if (!Util.isEmptyString(spinnerCity.getText().toString())) {
				pateint.setPcity(spinnerCity.getText().toString());
				pateint.setPcityid(hashCity.get(pateint.getPcity())
						.getCITY_ID());
			}
			if (!Util.isEmptyString(spinnerLocality.getText().toString())) {
				pateint.setParea(spinnerLocality.getText().toString());
				if (hashLocality.containsKey(pateint.getParea()))
					pateint.setPareaid(hashLocality.get(pateint.getParea())
							.getAREA_ID());
			}
			pateint.setUid(editId.getText().toString());
			pateint.setUid_type((String) spinnerId.getSelectedItem());

		} catch (Exception e) {
			e.printStackTrace();
		}
		return pateint;
	}

}
