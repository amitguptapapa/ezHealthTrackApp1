package com.ezhealthtrack.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.string;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.text.Editable;
import android.text.Html;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ezhealthrack.api.APIs;
import com.ezhealthtrack.EzApp;
import com.ezhealthtrack.R;
import com.ezhealthtrack.controller.LocalityController;
import com.ezhealthtrack.greendao.Area;
import com.ezhealthtrack.greendao.AreaDao;
import com.ezhealthtrack.greendao.City;
import com.ezhealthtrack.greendao.CityDao;
import com.ezhealthtrack.greendao.Country;
import com.ezhealthtrack.greendao.State;
import com.ezhealthtrack.greendao.StateDao.Properties;
import com.ezhealthtrack.model.AccountModel;
import com.ezhealthtrack.util.BaseActivity;
import com.ezhealthtrack.util.Constants;
import com.ezhealthtrack.util.Log;
import com.ezhealthtrack.util.MultipartRequest;
import com.ezhealthtrack.util.Util;
import com.google.gson.Gson;

public class EditAssistantActivity extends BaseActivity {
	private AccountModel model = new AccountModel();
	private ActionBar actionBar;
	private Spinner spinnerGender;
	private TextView spinnerCountry;
	private AutoCompleteTextView spinnerState;
	private AutoCompleteTextView spinnerCity;
	private AutoCompleteTextView spinnerLocality;
	private Spinner spinnerHeight;
	private Spinner spinnerHairColor;
	private Spinner spinnerEyeColor;
	private Spinner spinnerBloodGroup;
	private ArrayList<String> arrGender;
	private ArrayList<String> arrCountry;
	private ArrayList<String> arrState;
	private ArrayList<String> arrCity;
	private ArrayList<String> arrLocality;
	private HashMap<String, State> hashState = new HashMap<String, State>();
	private HashMap<String, City> hashCity = new HashMap<String, City>();
	private HashMap<String, Area> hashLocality = new HashMap<String, Area>();
	private ArrayList<String> arrHeight;
	private ArrayList<String> arrEyeColor;
	private ArrayList<String> arrHairColor;
	private ArrayList<String> arrBloodGroup;
	private TextView txtFirstName;
	private TextView txtLastName;
	private TextView txtGender;
	private TextView txtDOB;
	private TextView txtEmail;
	private TextView txtPhone;
	private TextView txtAddress1;
	private TextView txtCountry;
	private TextView txtState;
	private TextView txtCity;
	private TextView txtLocality;
	private TextView txtZipcode;
	private TextView txtUsername;
	private TextView txtPassword;
	private TextView txtConfirmPassword;
	private EditText editFirstName;
	private EditText editMiddleName;
	private EditText editLastName;
	private EditText editDob;
	private EditText editEmail;
	private EditText editPhone;
	private EditText editAddress1;
	private EditText editAddress2;
	private EditText editZipcode;
	private EditText editHeight;
	private EditText editUsername;
	private EditText editPassword;
	private EditText editCpassword;
	private EditText editMark;
	private EditText editVaccinations;
	private Button btnAddPatient;
	private RequestQueue mVolleyQueue;
	private final String TAG = this.getClass().getSimpleName();
	private SharedPreferences sharedPref;
	private ArrayAdapter<String> adapterCountry;
	private ArrayAdapter<String> adapterState;
	private ArrayAdapter<String> adapterCity;
	private ArrayAdapter<String> adapterLocality;
	private int age;
	private boolean changeFlag;

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
		arrBloodGroup.add("Select Blood Group");
		arrBloodGroup.add("A+");
		arrBloodGroup.add("A-");
		arrBloodGroup.add("B+");
		arrBloodGroup.add("B-");
		arrBloodGroup.add("O+");
		arrBloodGroup.add("O-");
		arrBloodGroup.add("AB+");
		arrBloodGroup.add("AB-");
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

	}

	private void getAssistantProfile(final String id) {
		final String url = APIs.ASSISTANTPROFILE();
		final Dialog loaddialog = Util
				.showLoadDialog(EditAssistantActivity.this);
		final StringRequest patientListRequest = new StringRequest(
				Request.Method.POST, url, new Response.Listener<String>() {
					@Override
					public void onResponse(final String response) {
						Log.i("get alerts", response);
						try {
							final JSONObject jObj = new JSONObject(response);
							final JSONObject data = jObj.getJSONObject("data");

							model = new Gson().fromJson(data.toString(),
									AccountModel.class);
							hashState.put(model.cmbState, new State(0l,
									model.cmbState_id, "", model.cmbState, ""));
							hashCity.put(model.cmbCity, new City(0l,
									model.cmbCity_id, model.cmbCity, "", ""));
							hashLocality.put(model.cmbArea,
									new Area(0l, model.cmbArea_id,
											model.cmbArea, "", "", ""));
							editFirstName.setText(model.getFname());
							editMiddleName.setText(model.getMname());
							editLastName.setText(model.getLname());
							spinnerGender.setSelection(arrGender.indexOf(model
									.getGender()));
							SimpleDateFormat sdf = new SimpleDateFormat(
									"yyyy-mm-dd");
							SimpleDateFormat sdf1 = new SimpleDateFormat(
									"mm/dd/yyyy");
							try {
								Date dob = sdf.parse(model.getDob());
								editDob.setText(sdf1.format(dob));
							} catch (ParseException e) {
								editDob.setText(model.getDob());
							}
							editEmail.setText(model.getEmail());
							editEmail.setEnabled(false);
							editPhone.setText(model.getMobile());
							editAddress1.setText(model.getAddress());
							editAddress2.setText(model.getAddress2());
							editZipcode.setText(model.getZip());
							if (!Util.isEmptyString(model.getHeight())) {
								try {
									int i = (Integer.valueOf(model.getHeight()
											.substring(0, 1)) * 12)
											+ Integer.valueOf(model.getHeight()
													.substring(1)) + -11;
									if (i >= 0 && i < 74)
										spinnerHeight.setSelection((Integer
												.valueOf(model.getHeight()
														.substring(0, 1)) * 12)
												+ Integer.valueOf(model
														.getHeight().substring(
																1)) + -11);
									else {
										spinnerHeight.setSelection(arrHeight
												.size() - 1);
										editHeight.setVisibility(View.VISIBLE);
										editHeight.setText(model.getHeight());
									}

								} catch (Exception e) {
									e.printStackTrace();
									spinnerHeight.setSelection(arrHeight.size() - 1);
									editHeight.setVisibility(View.VISIBLE);
									editHeight.setText(model.getHeight());
								}
							}
							spinnerHairColor.setSelection(arrHairColor
									.indexOf(model.getHaircolor()));
							spinnerEyeColor.setSelection(arrEyeColor
									.indexOf(model.getEyecolor()));
							spinnerBloodGroup.setSelection(arrBloodGroup
									.indexOf(model.getBlood()));
							editHeight.setText(model.getHeight());
							editUsername.setText(model.getUsername());
							editUsername.setEnabled(false);
							editPassword.setText(model.getPwd());

							editCpassword.setText(model.getPwd());

							editMark.setText(model.getVisiblemark());
							editVaccinations.setText(model.getVaccinations());
							spinnerCountry.setText("India");
							spinnerState.setText(model.getCmbState());
							spinnerCity.setText(model.getCmbCity());
							spinnerLocality.setText(model.getCmbArea());
							if (!Util.isEmptyString(model.getPhoto())) {
								Util.getImageFromUrl(
										APIs.ROOT() + "/documents/show/id/"
												+ model.getPhoto(),
										DashboardActivity.imgLoader,
										(ImageView) findViewById(R.id.img_user));
							} else {
								Util.getImageFromUrl(APIs.ROOT()
										+ "/uploads/noImage.gif",
										DashboardActivity.imgLoader,
										(ImageView) findViewById(R.id.img_user));

							}
							setTextWatchers();

						} catch (final JSONException e) {
							Log.e(TAG, e);
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
				loginParams.put("asst_id", id);
				return loginParams;
			}

		};
		patientListRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 5,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		EzApp.mVolleyQueue.add(patientListRequest);

		function1((ViewGroup) findViewById(R.id.rl_ass));
	}

	@Override
	public void onBackPressed() {
		if (changeFlag)
			Util.changeAlertDialog(this);
		else
			super.onBackPressed();
	}

	private void function1(final ViewGroup v) {
		for (int i = 0; i < v.getChildCount(); i++) {
			if (v.getChildAt(i) instanceof EditText) {
				((EditText) v.getChildAt(i))
						.addTextChangedListener(new TextWatcher() {

							@Override
							public void onTextChanged(CharSequence arg0,
									int arg1, int arg2, int arg3) {
								changeFlag = true;
							}

							@Override
							public void beforeTextChanged(CharSequence arg0,
									int arg1, int arg2, int arg3) {
								// TODO Auto-generated method stub

							}

							@Override
							public void afterTextChanged(Editable arg0) {
								// TODO Auto-generated method stub

							}
						});

			} else if (v.getChildAt(i) instanceof ViewGroup) {
				function1((ViewGroup) v.getChildAt(i));
			}
		}

	}

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_assistant);
		if (mVolleyQueue == null) {
			mVolleyQueue = Volley.newRequestQueue(this);
		}
		actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		if (Util.isEmptyString(getIntent().getStringExtra("id"))) {
			actionBar.setTitle("Add Assistant");
		} else {
			actionBar.setTitle("Edit Assistant");
		}
		sharedPref = getApplicationContext().getSharedPreferences(
				Constants.EZ_SHARED_PREF, Context.MODE_PRIVATE);
		spinnerGender = (Spinner) findViewById(R.id.spinner_gender);
		spinnerCountry = (TextView) findViewById(R.id.spinner_country);
		spinnerState = (AutoCompleteTextView) findViewById(R.id.spinner_state);
		spinnerCity = (AutoCompleteTextView) findViewById(R.id.spinner_city);
		spinnerLocality = (AutoCompleteTextView) findViewById(R.id.spinner_locality);
		spinnerHeight = (Spinner) findViewById(R.id.spinner_height);
		spinnerEyeColor = (Spinner) findViewById(R.id.spinner_eye_color);
		spinnerHairColor = (Spinner) findViewById(R.id.spinner_hair_color);
		spinnerBloodGroup = (Spinner) findViewById(R.id.spinner_blood_group);
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
		txtEmail = (TextView) findViewById(R.id.txt_email);
		((TextView) findViewById(R.id.txt_email)).append(Html
				.fromHtml("<sup><font color='#EE0000'>*</font></sup>"));
		txtPhone = (TextView) findViewById(R.id.txt_phone);
		((TextView) findViewById(R.id.txt_phone)).append(Html
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
		txtUsername = (TextView) findViewById(R.id.txt_username);
		((TextView) findViewById(R.id.txt_username)).append(Html
				.fromHtml("<sup><font color='#EE0000'>*</font></sup>"));

		editFirstName = (EditText) findViewById(R.id.edit_first_name);
		editLastName = (EditText) findViewById(R.id.edit_last_name);
		editMiddleName = (EditText) findViewById(R.id.edit_middle_name);
		editDob = (EditText) findViewById(R.id.edit_dob);
		editEmail = (EditText) findViewById(R.id.edit_email);
		editPhone = (EditText) findViewById(R.id.edit_phone);
		editPhone.setFilters(new InputFilter[] { new InputFilter.LengthFilter(
				10) });
		editAddress1 = (EditText) findViewById(R.id.edit_address1);
		editAddress2 = (EditText) findViewById(R.id.edit_address2);
		editZipcode = (EditText) findViewById(R.id.edit_zipcode);
		editZipcode
				.setFilters(new InputFilter[] { new InputFilter.LengthFilter(6) });
		editHeight = (EditText) findViewById(R.id.edit_other_height);
		editUsername = (EditText) findViewById(R.id.edit_username);
		editPassword = (EditText) findViewById(R.id.edit_password);
		editCpassword = (EditText) findViewById(R.id.edit_cpassword);
		editMark = (EditText) findViewById(R.id.edit_mark);
		editVaccinations = (EditText) findViewById(R.id.edit_vaccination);
		btnAddPatient = (Button) findViewById(R.id.btn_add_patient);
		arrGender = new ArrayList<String>();
		arrCountry = new ArrayList<String>();
		arrState = new ArrayList<String>();
		arrCity = new ArrayList<String>();
		arrLocality = new ArrayList<String>();
		arrHeight = new ArrayList<String>();
		arrHairColor = new ArrayList<String>();
		arrEyeColor = new ArrayList<String>();
		arrBloodGroup = new ArrayList<String>();

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
		final ArrayAdapter<String> adapterBlood = new ArrayAdapter<String>(
				this, android.R.layout.simple_spinner_item, arrBloodGroup);
		adapterBlood
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		adapterCountry = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, arrCountry);
		adapterCountry
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		adapterState = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, arrState);
		adapterState
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		adapterCity = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, arrCity);
		adapterCity
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		adapterLocality = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, arrLocality);
		adapterLocality
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerGender.setAdapter(adapterGender);
		spinnerCountry.setText("India");
		spinnerState.setAdapter(adapterState);
		spinnerCity.setAdapter(adapterCity);
		spinnerLocality.setAdapter(adapterLocality);
		spinnerHeight.setAdapter(adapterHeight);
		spinnerHairColor.setAdapter(adapterHairColor);
		spinnerBloodGroup.setAdapter(adapterBlood);
		spinnerEyeColor.setAdapter(adapterEyeColor);
		btnAddPatient.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(final View v) {
				if (!Util.isEmptyString(editFirstName.getText().toString())) {
					if (!Util.isEmptyString(editLastName.getText().toString())) {
						if (!Util.isEmptyString(editAddress1.getText()
								.toString())) {
							if (spinnerGender.getSelectedItemPosition() != 0) {
								if (!Util.isEmptyString(editDob.getText()
										.toString())) {
									if (!Util.isEmptyString(editEmail.getText()
											.toString())) {
										if (!Util.isEmptyString(editPhone
												.getText().toString())) {
											if (Util.isNumeric(editPhone
													.getText().toString())) {
												if (spinnerGender
														.getSelectedItemPosition() != 0) {
													if (hashState
															.containsKey(spinnerState
																	.getText()
																	.toString())) {
														if (hashCity
																.containsKey(spinnerCity
																		.getText()
																		.toString())) {
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
																		if (editPassword
																				.getText()
																				.toString()
																				.equalsIgnoreCase(
																						editCpassword
																								.getText()
																								.toString())) {
																			if (Util.isEmptyString(getIntent()
																					.getStringExtra(
																							"id"))) {
																				updateAssistantData("");
																			} else {
																				updateAssistantData(getIntent()
																						.getStringExtra(
																								"id"));
																			}
																		} else {
																			Util.Alertdialog(
																					EditAssistantActivity.this,
																					"Password did not match.");
																		}
																	} else {
																		Util.Alertdialog(
																				EditAssistantActivity.this,
																				"Only Numeric Value allowed in Pin/Zipcode");
																	}

																} else {
																	Util.Alertdialog(
																			EditAssistantActivity.this,
																			"Please enter Assistant's Pin/Zipcode");
																}
															} else {
																Util.Alertdialog(
																		EditAssistantActivity.this,
																		"Please select Assistant's Locality");
															}
														} else {
															Util.Alertdialog(
																	EditAssistantActivity.this,
																	"Please select Assistant's City");
														}
													} else {
														Util.Alertdialog(
																EditAssistantActivity.this,
																"Please select Assistant's State");
													}

												} else {
													Util.Alertdialog(
															EditAssistantActivity.this,
															"Please select Assistant's Gender");
												}
											} else {
												Util.Alertdialog(
														EditAssistantActivity.this,
														"Only Numeric Value allowed in Phone");
											}
										} else {
											Util.Alertdialog(
													EditAssistantActivity.this,
													"Please enter Assistant's Phone number");
										}
									} else {
										Util.Alertdialog(
												EditAssistantActivity.this,
												"Please enter Assistant's Email");
									}
								} else {
									Util.Alertdialog(
											EditAssistantActivity.this,
											"Please enter Assistant's Date of Birth");
								}
							} else {
								Util.Alertdialog(EditAssistantActivity.this,
										"Please select Assistant's Gender");
							}
						} else {
							Util.Alertdialog(EditAssistantActivity.this,
									"Please enter Assistant's Address");
						}

					} else {
						Util.Alertdialog(EditAssistantActivity.this,
								"Please enter Assistant's Last Name");
					}

				} else {
					Util.Alertdialog(EditAssistantActivity.this,
							"Please enter Assistant's First Name");
				}
			}
		});

		findViewById(R.id.img_user).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				imageDialog();

			}
		});

		editDob.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(final View v) {
				final Calendar cal = Calendar.getInstance();
				final DatePickerDialog datepicker = new DatePickerDialog(
						EditAssistantActivity.this, new OnDateSetListener() {

							@Override
							public void onDateSet(final DatePicker view,
									final int year, int monthOfYear,
									final int dayOfMonth) {
								if ((dayOfMonth < 10) && (monthOfYear < 9)) {
									editDob.setText("0" + ++monthOfYear + "/0"
											+ dayOfMonth + "/" + year);
								} else if ((dayOfMonth < 10)
										&& (monthOfYear > 8)) {
									editDob.setText(++monthOfYear + "/" + "0"
											+ dayOfMonth + "/" + year);
								} else if ((dayOfMonth > 9)
										&& (monthOfYear < 9)) {
									editDob.setText("0" + ++monthOfYear + "/"
											+ dayOfMonth + "/" + year);
								} else {
									editDob.setText(++monthOfYear + "/"
											+ dayOfMonth + "/" + year);
								}
								age = cal.get(Calendar.YEAR) - year;

							}
						}, cal.get(Calendar.YEAR) - 18,
						cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
				datepicker.show();

			}
		});

		if (!Util.isEmptyString(getIntent().getStringExtra("id"))) {
			getAssistantProfile(getIntent().getStringExtra("id"));
		} else {
			setTextWatchers();
		}
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {

		case android.R.id.home:

			finish();
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

	private void updateAssistantData(final String id) {
		final String url = APIs.UPDATEASSISTANT();
		final Dialog loaddialog = Util
				.showLoadDialog(EditAssistantActivity.this);
		Log.i(TAG, url);
		final StringRequest postNewPatientRequest = new StringRequest(
				Request.Method.POST, url, new Response.Listener<String>() {
					@Override
					public void onResponse(final String response) {
						Log.i(TAG, response);
						try {
							final JSONObject jObj = new JSONObject(response);
							if (jObj.getString("s").equals("200")) {
								try {

									InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
									imm.hideSoftInputFromWindow(
											findViewById(R.id.btn_add_patient)
													.getWindowToken(), 0);

								} catch (Exception e) {

								}
								try {
									if (Util.isEmptyString(getIntent()
											.getStringExtra("id"))) {
										actionBar.setTitle("Add Assistant");
										Util.AlertdialogWithFinish(
												EditAssistantActivity.this,
												editFirstName.getText()
														+ " "
														+ editLastName
																.getText()
														+ " added successfully");
									} else {
										actionBar.setTitle("Edit Assistant");
										Util.AlertdialogWithFinish(
												EditAssistantActivity.this,
												editFirstName.getText()
														+ " "
														+ editLastName
																.getText()
														+ " updated successfully");
									}
								} catch (Exception e) {

								}

							} else {
								Util.Alertdialog(EditAssistantActivity.this,
										jObj.getString("m"));
								// editEmail.setBackgroundColor(Color
								// .parseColor("#dd0000"));
							}
						} catch (final JSONException e) {
							Util.Alertdialog(EditAssistantActivity.this,
									e.toString());
							Log.e(TAG, e);
						}
						loaddialog.dismiss();
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(final VolleyError error) {
						Util.Alertdialog(EditAssistantActivity.this,
								"Network error, try again later");

						Log.e("Error.Response", error);
						loaddialog.dismiss();
					}
				}) {
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				final HashMap<String, String> headerParams = new HashMap<String, String>();
				headerParams.put("auth-token", Util.getBase64String(sharedPref
						.getString(Constants.USER_TOKEN, "")));
				return headerParams;
			}

			@Override
			protected Map<String, String> getParams() {
				final HashMap<String, String> postParams = new HashMap<String, String>();
				postParams.put("cli", "api");
				postParams.put("asst_id", id);
				postParams.put("fname", editFirstName.getText().toString());
				postParams.put("mname", editMiddleName.getText().toString());
				postParams.put("lname", editLastName.getText().toString());
				postParams.put("gender",
						(String) spinnerGender.getSelectedItem());
				postParams.put("dob", editDob.getText().toString());
				postParams.put("email", editEmail.getText().toString());
				postParams.put("mobile", editPhone.getText().toString());
				postParams.put("dob", editDob.getText().toString());
				postParams.put("address", editAddress1.getText().toString());
				postParams.put("address2", editAddress2.getText().toString());
				postParams.put("country", "1");
				postParams.put("cmbState", (hashState.get(spinnerState
						.getText().toString())).getSTATE_ID());
				postParams.put("cmbCity", (hashCity.get(spinnerCity.getText()
						.toString())).getCITY_ID());
				if (hashLocality.containsKey(spinnerLocality.getText()
						.toString())) {
					postParams.put("cmbArea", (hashLocality.get(spinnerLocality
							.getText().toString())).getAREA_ID());
				} else {
					postParams.put("cmbArea", "");
				}
				postParams.put("cmbArea_text", spinnerLocality.getText()
						.toString());
				postParams.put("zip", editZipcode.getText().toString());
				if (spinnerHeight.getSelectedItemPosition() != 0
						&& spinnerHeight.getSelectedItemPosition() != arrHeight
								.size() - 1) {
					postParams.put("height", spinnerHeight.getSelectedItem()
							.toString().replaceAll(" ", "").replaceAll("'", "")
							.replaceAll("\"", ""));
				} else if (spinnerHeight.getSelectedItemPosition() == 0) {
					postParams.put("height", "");
				} else if (spinnerHeight.getSelectedItemPosition() == (arrHeight
						.size() - 1)) {
					postParams.put("height", editHeight.getText().toString());
				}
				if (spinnerHairColor.getSelectedItemPosition() != 0) {
					postParams.put("haircolor", spinnerHairColor
							.getSelectedItem().toString());
				} else {
					postParams.put("haircolor", "");
				}
				if (spinnerEyeColor.getSelectedItemPosition() != 0) {
					postParams.put("eyecolor", spinnerEyeColor
							.getSelectedItem().toString());
				} else {
					postParams.put("eyecolor", "");
				}
				if (spinnerBloodGroup.getSelectedItemPosition() != 0) {
					postParams.put("blood", spinnerBloodGroup.getSelectedItem()
							.toString());
				} else {
					postParams.put("blood", "");
				}
				postParams.put("visiblemark", editMark.getText().toString());
				postParams.put("username", editUsername.getText().toString());
				if (Util.isEmptyString(editPassword.getText().toString())) {
					postParams.put("pwd", model.getPwd());
					postParams.put("cnf_pwd", model.getPwd());
				} else {
					postParams.put("pwd", editPassword.getText().toString());
					postParams
							.put("cnf_pwd", editPassword.getText().toString());
				}

				postParams.put("vaccinations", editVaccinations.getText()
						.toString());
				Log.i("", postParams.toString());
				return postParams;
			}

		};
		postNewPatientRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 5,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		mVolleyQueue.add(postNewPatientRequest);

	}

	private File image;
	private static final int CAMERA_REQUEST = 1888;
	private static final int SELECT_PICTURE = 1;
	private Uri selectedImageUri;
	public static Bitmap bitmapImage;

	private void imageDialog() {
		String[] addPhoto;
		addPhoto = new String[] { "Camera", "Gallery" };
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
				} else if (id == 1) {
					if (android.os.Build.VERSION.SDK_INT == 18) {
						Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
						intent.addCategory(Intent.CATEGORY_OPENABLE);
						intent.setType("image/jpeg");
						startActivityForResult(intent, SELECT_PICTURE);
					} else {
						Intent intent = new Intent();
						intent.setType("image/*");
						intent.setAction(Intent.ACTION_GET_CONTENT);
						startActivityForResult(
								Intent.createChooser(intent, "Select Picture"),
								SELECT_PICTURE);
					}
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

	@SuppressLint("NewApi")
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		try {

			super.onActivityResult(requestCode, resultCode, data);
			if (resultCode == RESULT_OK) {
				if (requestCode == CAMERA_REQUEST) {
					selectedImageUri = Uri.parse("d");
					if (bitmapImage != null)
						bitmapImage.recycle();
					bitmapImage = decodeFile(image.getPath());
					bitmapImage = Util.getResizedBitmap(bitmapImage, 400, 400);
					((ImageView) findViewById(R.id.img_user))
							.setImageBitmap(bitmapImage);

				} else if (requestCode == SELECT_PICTURE) {
					try {
						if (android.os.Build.VERSION.SDK_INT == 18) {
							selectedImageUri = data.getData();
							String id = selectedImageUri.getLastPathSegment()
									.split(":")[1];
							String state = Environment
									.getExternalStorageState();
							String uri = ""
									+ MediaStore.Images.Media.EXTERNAL_CONTENT_URI
									+ "/" + id;
							if (!state
									.equalsIgnoreCase(Environment.MEDIA_MOUNTED)) {
								uri = ""
										+ MediaStore.Images.Media.INTERNAL_CONTENT_URI
										+ "/" + id;

							} else {
								uri = ""
										+ MediaStore.Images.Media.EXTERNAL_CONTENT_URI
										+ "/" + id;
							}
							selectedImageUri = Uri.parse(uri);

						} else {
							selectedImageUri = data.getData();
						}

						Cursor cursor = this
								.getContentResolver()
								.query(selectedImageUri,
										new String[] { MediaStore.Images.ImageColumns.ORIENTATION },
										null, null, null);
						cursor.moveToFirst();
						Log.i(TAG, " " + cursor.getInt(0));
						if (bitmapImage != null)
							bitmapImage.recycle();
						bitmapImage = Util.getGalleryBitmap(
								EditAssistantActivity.this, selectedImageUri,
								cursor.getInt(0));
						((ImageView) findViewById(R.id.img_user))
								.setImageBitmap(bitmapImage);

					} catch (Exception e) {
						Log.e(TAG, e);
					}
				}
				uploadPhoto();
			}
		} catch (Exception e) {
			Log.e(TAG, e);
		} catch (Error e) {
			Log.e(TAG, e);
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

	private void uploadPhoto() {
		try {
			FileOutputStream fOut = new FileOutputStream(image);

			bitmapImage.compress(Bitmap.CompressFormat.PNG, 85, fOut);
			fOut.flush();
			fOut.close();
		} catch (Exception e) {

		}

		String requestURL = APIs.URL()
				+ "/documents/imageUpload/cli/api?type=assistantPhoto&asst_id="
				+ model.getAsst_id();
		MultipartRequest request = new MultipartRequest(requestURL,
				new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError e) {
						Log.e(TAG, e);

					}
				}, new Listener<String>() {

					@Override
					public void onResponse(String arg0) {
						Log.i("", arg0);

					}
				}, image, "doctorPhoto") {
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				final HashMap<String, String> headerParams = new HashMap<String, String>();
				headerParams.put("auth-token", Util.getBase64String(sharedPref
						.getString(Constants.USER_TOKEN, "")));
				headerParams.put("Accept-Encoding", "gzip, deflate");
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
				params.put("type", "assistantPhoto");
				params.put("asst_id", model.getAsst_id());
				return params;
			}

		};
		request.setRetryPolicy(new DefaultRetryPolicy(200000, 5,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

		Log.i(TAG, new Gson().toJson(request));

		mVolleyQueue.add(request);

	}

	private void setTextWatchers() {

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
	}

}
