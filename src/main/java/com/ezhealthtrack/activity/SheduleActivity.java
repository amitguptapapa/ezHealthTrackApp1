package com.ezhealthtrack.activity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.text.Html;
import android.text.InputFilter;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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
import com.android.volley.toolbox.StringRequest;
import com.ezhealthrack.api.APIs;
import com.ezhealthtrack.EzApp;
import com.ezhealthtrack.R;
import com.ezhealthtrack.controller.PatientController;
import com.ezhealthtrack.controller.PatientController.OnResponsePatient;
import com.ezhealthtrack.dialogs.EzDialog;
import com.ezhealthtrack.fragments.DatePickerDialogFragment;
import com.ezhealthtrack.greendao.Appointment;
import com.ezhealthtrack.greendao.Patient;
import com.ezhealthtrack.labs.controller.LabsAppointmentController;
import com.ezhealthtrack.model.InRefferalModel;
import com.ezhealthtrack.model.PatientShow;
import com.ezhealthtrack.model.VacationModel;
import com.ezhealthtrack.one.EzUtils;
import com.ezhealthtrack.util.Constants;
import com.ezhealthtrack.util.EzActivity;
import com.ezhealthtrack.util.Log;
import com.ezhealthtrack.util.Util;
import com.google.gson.Gson;

public class SheduleActivity extends EzActivity implements OnDateSetListener {
	private TableLayout table;
	private TableLayout tableHeader;
	private Date date;
	private Calendar cal;
	private final String TAG = this.getClass().getSimpleName();
	private Appointment aptReschedule;
	private Patient patient;

	private void appointmentFollowup(final Date date, final String reason,
			final String slot, final Appointment apt) {
		final String url = APIs.APPOINTMENT_FOLLOWUP().replace("bk-id",
				apt.getBkid());
		final Dialog loaddialog = Util.showLoadDialog(this);
		Log.i(TAG, url);
		final StringRequest scheduleDataRequest = new StringRequest(
				Request.Method.POST, url, new Response.Listener<String>() {
					@Override
					public void onResponse(final String response) {
						Log.i(TAG, response);
						try {
							final JSONObject jObj = new JSONObject(response);
							if (jObj.getString("s").equals("200")) {
								loaddialog.dismiss();
								Util.AlertdialogWithFinish(
										SheduleActivity.this,
										"Appointment has been scheduled successfully.");
								DashboardActivity.calls.getInReferralList(1);
							} else if (jObj.getString("s").equals("403")) {
								loaddialog.dismiss();
								Util.Alertdialog(SheduleActivity.this,
										"Slot already booked for same patient");
							}
						} catch (final JSONException e) {
							loaddialog.dismiss();
							Util.Alertdialog(SheduleActivity.this,
									"There is network error in scheduling this appointment, please try again");
							Log.e(TAG, e);
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(final VolleyError error) {
						Util.Alertdialog(SheduleActivity.this,
								"Network error, try again later");

						Log.e("Error.Response", error);
					}
				}) {
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				final HashMap<String, String> loginParams = new HashMap<String, String>();
				loginParams.put("auth-token", Util
						.getBase64String(EzApp.sharedPref.getString(
								Constants.USER_TOKEN, "")));
				return loginParams;
			}

			@Override
			protected Map<String, String> getParams() {
				final HashMap<String, String> loginParams = new HashMap<String, String>();
				loginParams.put("format", "json");
				loginParams.put("pid", patient.getPid());
				loginParams.put("fid", patient.getFid());
				loginParams.put("tenant_id",
						EzApp.sharedPref.getString(Constants.TENANT_ID, ""));
				loginParams.put("branch_id", EzApp.sharedPref.getString(
						Constants.USER_BRANCH_ID, ""));
				Log.i(TAG, "slot=" + slot);
				loginParams.put("sid", slot);
				final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				loginParams.put("date", sdf.format(date));
				loginParams.put("p-date", sdf.format(apt.aptDate));
				loginParams.put("apt-reason", reason);
				return loginParams;
			}

		};
		scheduleDataRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 5,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		EzApp.mVolleyQueue.add(scheduleDataRequest);

	}

	private void appointmentRegister(final Date date, final String reason,
			final String slot) {
		final String url = APIs.APPOINTMENT_REGISTER();
		final Dialog loaddialog = Util.showLoadDialog(this);
		Log.i(TAG, url);
		final StringRequest scheduleDataRequest = new StringRequest(
				Request.Method.POST, url, new Response.Listener<String>() {
					@Override
					public void onResponse(final String response) {
						Log.i(TAG, response);
						try {
							final JSONObject jObj = new JSONObject(response);
							if (jObj.getString("s").equals("200")) {
								loaddialog.dismiss();
								Util.AlertdialogWithFinish(
										SheduleActivity.this,
										"Appointment has been scheduled successfully");
								DashboardActivity.calls.getInReferralList(1);

							} else if (jObj.getString("s").equals("403")) {
								loaddialog.dismiss();
								Util.Alertdialog(SheduleActivity.this,
										"Slot already booked for same patient");
							}
						} catch (final JSONException e) {
							loaddialog.dismiss();
							Util.Alertdialog(SheduleActivity.this,
									"There is some error in scheduling appointment, please try again.");
							Log.e(TAG, e);
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(final VolleyError error) {
						Util.Alertdialog(SheduleActivity.this,
								"Network error, try again later");

						Log.e("Error.Response", error);
					}
				}) {
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				final HashMap<String, String> loginParams = new HashMap<String, String>();
				loginParams.put("auth-token", Util
						.getBase64String(EzApp.sharedPref.getString(
								Constants.USER_TOKEN, "")));
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
				loginParams.put("pid", patient.getPid());
				loginParams.put("fid", patient.getFid());
				Log.i(TAG, "slot=" + slot);
				loginParams.put("sid", slot);
				final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				loginParams.put("date", sdf.format(date));
				loginParams.put("p-date", sdf.format(date));
				loginParams.put("apt-reason", reason);

				if (getIntent().hasExtra("type")) {
					if (getIntent().getStringExtra("type")
							.equals("in referral")) {
						loginParams.put("ref-ep",
								getIntent().getStringExtra("ref-ep"));
						loginParams.put("ref-id",
								getIntent().getStringExtra("ref-id"));

					}
				}
				return loginParams;
			}

		};
		scheduleDataRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 5,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		EzApp.mVolleyQueue.add(scheduleDataRequest);

	}

	private void appointmentReschedule(final Date date, final String reason,
			final String slot, final Appointment apt) {
		final String url = APIs.APPOINTMENT_RESCHEDULE().replace("bk-id",
				apt.getBkid());
		final Dialog loaddialog = Util.showLoadDialog(this);
		Log.i(TAG, url);
		final StringRequest scheduleDataRequest = new StringRequest(
				Request.Method.POST, url, new Response.Listener<String>() {
					@Override
					public void onResponse(final String response) {
						Log.i(TAG, response);
						try {
							final JSONObject jObj = new JSONObject(response);
							if (jObj.getString("s").equals("200")) {
								loaddialog.dismiss();
								Util.AlertdialogWithFinish(
										SheduleActivity.this,
										"Appointment has been rescheduled successfully.");
								DashboardActivity.arrScheduledPatients
										.remove(apt);
								table.removeAllViews();
								tableHeader.removeAllViews();
								createTable();
								DashboardActivity.calls.getInReferralList(1);
							} else if (jObj.getString("s").equals("403")) {
								loaddialog.dismiss();
								Util.Alertdialog(SheduleActivity.this,
										"Slot already booked for same patient");
							}
						} catch (final JSONException e) {
							loaddialog.dismiss();
							Util.AlertdialogWithFinish(SheduleActivity.this,
									"There is some error in rescheduling, please try again.");
							Log.e(TAG, e);
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(final VolleyError error) {
						Util.AlertdialogWithFinish(SheduleActivity.this,
								"Network error, try again later");

						Log.e("Error.Response", error);
					}
				}) {
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				final HashMap<String, String> loginParams = new HashMap<String, String>();
				loginParams.put("auth-token", Util
						.getBase64String(EzApp.sharedPref.getString(
								Constants.USER_TOKEN, "")));
				return loginParams;
			}

			@Override
			protected Map<String, String> getParams() {
				final HashMap<String, String> loginParams = new HashMap<String, String>();
				loginParams.put("format", "json");
				loginParams.put("pid", patient.getPid());
				loginParams.put("fid", patient.getFid());
				loginParams.put("tenant_id",
						EzApp.sharedPref.getString(Constants.TENANT_ID, ""));
				loginParams.put("branch_id", EzApp.sharedPref.getString(
						Constants.USER_BRANCH_ID, ""));
				Log.i(TAG, "slot=" + slot);
				loginParams.put("sid", slot);
				final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				loginParams.put("date", sdf.format(date));
				loginParams.put("p-date", sdf.format(apt.aptDate));
				loginParams.put("apt-reason", reason);
				return loginParams;
			}

		};
		scheduleDataRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 5,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		EzApp.mVolleyQueue.add(scheduleDataRequest);

	}

	private void createTable() {
		final LayoutParams params = new LayoutParams(0,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		params.setMargins(1, 1, 1, 1);
		final TableRow trHeader = new TableRow(this);
		final TextView timeHeader = new TextView(this);
		timeHeader.setText("Time");
		timeHeader.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
		timeHeader.setBackgroundColor(Color.parseColor("#83B6FF"));
		timeHeader.setGravity(Gravity.CENTER);
		timeHeader.setPadding(5, 10, 5, 10);
		timeHeader.setTextColor(Color.BLACK);
		timeHeader.setLayoutParams(params);
		trHeader.addView(timeHeader);

		for (int i = 0; i < 7; i++) {
			final TextView timeHeader1 = new TextView(this);
			cal.setTime(date);
			cal.add(Calendar.DATE, i);
			final Date newDate = cal.getTime();
			timeHeader1.setText(EzApp.sdfeeemmmddyy.format(newDate));
			timeHeader1.setBackgroundColor(Color.parseColor("#83B6FF"));
			timeHeader1.setGravity(Gravity.CENTER);
			timeHeader1.setPadding(5, 10, 5, 10);
			timeHeader1.setTextColor(Color.BLACK);
			timeHeader1.setLayoutParams(params);
			timeHeader1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
			trHeader.addView(timeHeader1);
		}
		tableHeader.addView(trHeader);

		for (int i = 0; i < 96; i++) {
			final TableRow tr = new TableRow(this);
			tr.setTag("" + (i + 1));
			for (int j = 0; j < 8; j++) {
				final TextView c1 = new TextView(this);
				c1.setTag("c" + j);
				c1.setGravity(Gravity.CENTER);
				c1.setBackgroundColor(Color.parseColor("#F1F7FF"));
				c1.setPadding(10, 10, 10, 10);
				c1.setTextColor(Color.BLACK);
				c1.setLayoutParams(params);
				c1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
				tr.addView(c1);
			}

			table.addView(tr);
		}
		final Dialog loaddialog = Util.showLoadDialog(SheduleActivity.this);
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				setTable();
				loaddialog.dismiss();
			}
		}, 100);

	}

	private void getScheduleData(final Date date) {
		final String url = APIs.SCHEDULE_DATA();
		final Dialog loaddialog = Util.showLoadDialog(SheduleActivity.this);
		Log.i(TAG, url);
		final StringRequest scheduleDataRequest = new StringRequest(
				Request.Method.POST, url, new Response.Listener<String>() {
					@Override
					public void onResponse(final String response) {
						Log.i(TAG, response);
						try {
							final JSONObject jObj = new JSONObject(response);
							if (jObj.getString("s").equals("200")) {
								final JSONArray data = jObj
										.getJSONArray("data");
								final JSONArray patientList1 = jObj
										.getJSONArray("patients");
								for (int i = 0; i < patientList1.length(); i++) {
									PatientShow pat = new Gson().fromJson(
											patientList1.get(i).toString(),
											PatientShow.class);
									Iterator<PatientShow> iter = DashboardActivity.arrPatientShow
											.iterator();
									while (iter.hasNext()) {
										PatientShow pats = iter.next();
										if (pats.getPfId()
												.equals(pat.getPfId())
												&& pats.getPId().equals(
														pat.getPId()))
											iter.remove();
									}
									DashboardActivity.arrPatientShow.add(pat);
								}
								for (int i = 0; i < data.length(); i++) {
									final JSONObject jdata = data
											.getJSONObject(i);
									final String dateString = jdata
											.getString("date");

									final JSONArray slots = jdata
											.getJSONArray("slot");
									for (int j = 0; j < slots.length(); j++) {
										final int sid = slots.getJSONObject(j)
												.getInt("sid");
										final JSONArray bookedBy = slots
												.getJSONObject(j).getJSONArray(
														"booked-by");
										for (int k = 0; k < bookedBy.length(); k++) {
											final String pid = bookedBy
													.getJSONObject(k)
													.getString("pid");
											final String pfid = bookedBy
													.getJSONObject(k)
													.getString("pfid");
											final String type = bookedBy
													.getJSONObject(k)
													.getString("type");
											final Appointment pm = new Appointment();
											final SimpleDateFormat sdf = new SimpleDateFormat(
													"yyyy-MM-dd");
											try {
												final Date date = sdf
														.parse(dateString);
												cal.setTime(date);
												cal.set(Calendar.HOUR_OF_DAY, 0);
												cal.set(Calendar.MINUTE, 0);
												cal.add(Calendar.MINUTE,
														(sid - 1) * 15);
												pm.aptDate = cal.getTime();
												pm.setType(type);
												pm.setPid(pid);
												pm.setPfId(pfid);
												pm.setReason(bookedBy
														.getJSONObject(k)
														.getString("reason"));
												Log.i(TAG, type);
												DashboardActivity.arrScheduledPatients
														.add(pm);

											} catch (final ParseException e) {
												Log.e(TAG, e);
											}

										}

									}

								}
								table.removeAllViews();
								tableHeader.removeAllViews();
								createTable();

							} else {
								// Toast.makeText(
								// SheduleActivity.this,
								// "There is some error while fetching schedle data please try again.",
								// Toast.LENGTH_SHORT).show();
							}
						} catch (final JSONException e) {
							// Toast.makeText(
							// SheduleActivity.this,
							// "There is some error while fetching schedule data please try again.",
							// Toast.LENGTH_SHORT).show();
							Log.e(TAG, e);
						}
						loaddialog.dismiss();
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(final VolleyError error) {
						// Toast.makeText(
						// SheduleActivity.this,
						// "There is some error while fetching schedle data please try again",
						// Toast.LENGTH_SHORT).show();

						Log.e("Error.Response", error);
						loaddialog.dismiss();
					}
				}) {
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				final HashMap<String, String> loginParams = new HashMap<String, String>();
				loginParams.put("auth-token", Util
						.getBase64String(EzApp.sharedPref.getString(
								Constants.USER_TOKEN, "")));
				return loginParams;
			}

			@Override
			protected Map<String, String> getParams() {
				final HashMap<String, String> loginParams = new HashMap<String, String>();
				loginParams.put("format", "json");
				loginParams.put("pat-id", patient.getPid());
				loginParams.put("family-id", "0");
				final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				loginParams.put("date", sdf.format(date));
				return loginParams;
			}

		};
		scheduleDataRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 5,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		EzApp.mVolleyQueue.add(scheduleDataRequest);

	}

	static private boolean WORK_IN_PROGRESS = true;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (WORK_IN_PROGRESS) {
			setContentView(R.layout.activity_shedule_not_implemented);
			this.setDisplayHomeAsUpEnabled(true);
			this.setTitle("Schedule Patient");
			return;
		}

		setContentView(R.layout.activity_shedule);
		this.setDisplayHomeAsUpEnabled(true);

		// set orientation for non-large devices
		if (!EzUtils.getDeviceSize(this).equals(EzUtils.EZ_SCREEN_LARGE)) {
			mScreenRotation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
		}

		tableHeader = (TableLayout) findViewById(R.id.table_header);
		table = (TableLayout) findViewById(R.id.table);
		if (Util.isEmptyString(getIntent().getStringExtra("type"))) {
			// actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
			// final ArrayAdapter<String> aAdpt = new ArrayAdapter<String>(this,
			// android.R.layout.simple_list_item_1, android.R.id.text1,
			// patientList);
			// actionBar.setListNavigationCallbacks(aAdpt, this);
			this.setTitle("Schedule Patient");
		}
		PatientController.getPatient(getIntent().getStringExtra("pid"),
				getIntent().getStringExtra("fid"), this,
				new OnResponsePatient() {

					@Override
					public void onResponseListner(Patient response) {
						patient = response;
						SheduleActivity.this.setTitle("Schedule: "
								+ patient.getP_detail());
					}
				});

		if (getIntent().hasExtra("type")) {
			if (getIntent().getStringExtra("type").equals("in referral")) {
				PatientController.deletePatient(patient);
			}
		}
		tableHeader.setStretchAllColumns(true);
		table.setStretchAllColumns(true);
		cal = Calendar.getInstance();
		date = cal.getTime();
		createTable();
	}

	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		// Inflate the menu items for use in the action bar
		final MenuInflater inflater = getMenuInflater();
		if (!WORK_IN_PROGRESS)
			inflater.inflate(R.menu.schedule, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public void onDateSet(final DatePicker view, final int year,
			final int monthOfYear, final int dayOfMonth) {
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, monthOfYear);
		cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
		date = cal.getTime();
		getScheduleData(date);
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case R.id.action_month:
			final Bundle b = new Bundle();
			b.putInt(DatePickerDialogFragment.YEAR, cal.get(Calendar.YEAR));
			b.putInt(DatePickerDialogFragment.MONTH, cal.get(Calendar.MONTH));
			b.putInt(DatePickerDialogFragment.DATE,
					cal.get(Calendar.DAY_OF_MONTH));
			final DialogFragment picker = new DatePickerDialogFragment();
			picker.setArguments(b);
			picker.show(getSupportFragmentManager(), "fragment_date_picker");
			return true;

		case android.R.id.home:
			finish();
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onStop() {
		// if (getIntent().hasExtra("type")) {
		// if (getIntent().getStringExtra("type").equals("in referral")) {
		// DashboardActivity.arrPatients
		// .remove(DashboardActivity.arrPatients.size() - 1);
		// }
		// }
		super.onStop();
	}

	private void setTable() {
		// Log.i(TAG, "set table called");

		for (int i = 0; i < 96; i++) {
			final TableRow tr = (TableRow) table.findViewWithTag("" + (i + 1));
			for (int j = 0; j < 8; j++) {
				final TextView c1 = (TextView) (tr.findViewWithTag("c" + j));
				int hr = (i / 4);
				final int min = (i * 15) % 60;
				cal.setTime(date);
				cal.add(Calendar.DATE, j - 1);
				cal.set(Calendar.HOUR_OF_DAY, hr);
				cal.set(Calendar.MINUTE, min);
				cal.set(Calendar.SECOND, 0);
				final Date newDate = cal.getTime();
				if (j == 0) {
					String amorpm = "AM";
					if (hr > 12) {
						hr = hr - 12;
						amorpm = "PM";
					} else if (hr == 12) {
						amorpm = "PM";
					}
					if (hr == 0) {
						hr = 12;
					}
					if (min < 10) {
						c1.setText("" + hr + ":0" + min + amorpm);
					} else {
						c1.setText("" + hr + ":" + min + amorpm);
					}
				} else {
					c1.setTag(newDate.toString());
				}
				if (EzApp.sharedPref.getString(Constants.USER_TYPE, "")
						.equalsIgnoreCase("D")) {
					try {
						final Date startTime = EzApp.sdfTime
								.parse(DashboardActivity.dayPlan[cal
										.get(Calendar.DAY_OF_WEEK) - 1]
										.getStartTime());
						final Date endTime;
						if (DashboardActivity.dayPlan[cal
								.get(Calendar.DAY_OF_WEEK) - 1].getEndTime()
								.equals("12:00 AM")) {
							endTime = EzApp.sdfTime.parse("11:59 PM");
							endTime.setTime(endTime.getTime() + 60000);

						} else {
							endTime = EzApp.sdfTime
									.parse(DashboardActivity.dayPlan[cal
											.get(Calendar.DAY_OF_WEEK) - 1]
											.getEndTime());
							// Log.i(TAG, endTime.toString());
						}
						final Date lOutTime = EzApp.sdfTime
								.parse(DashboardActivity.dayPlan[cal
										.get(Calendar.DAY_OF_WEEK) - 1]
										.getLout());
						final Date lInTime = EzApp.sdfTime
								.parse(DashboardActivity.dayPlan[cal
										.get(Calendar.DAY_OF_WEEK) - 1]
										.getLin());
						final Date currTime = EzApp.sdfTime.parse(EzApp.sdfTime
								.format(newDate));
						cal = Calendar.getInstance();
						if (((currTime.compareTo(startTime) >= 0)
								&& (currTime.compareTo(endTime) < 0)
								&& (j != 0)
								&& (newDate.compareTo(cal.getTime()) >= 0) && !((currTime
								.compareTo(lInTime) < 0) && (currTime
								.compareTo(lOutTime) >= 0)))) {
							c1.setBackgroundColor(Color.parseColor("#ABA2FF"));
							c1.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(final View v) {
									ArrayList<Appointment> arrAptSlot = new ArrayList<Appointment>();
									for (Appointment apt : DashboardActivity.arrScheduledPatients) {
										// Log.i(apt.aptDate.toString(),
										// newDate.toString());
										if (apt.aptDate.toString()
												.equalsIgnoreCase(
														newDate.toString())) {
											arrAptSlot.add(apt);
										}
									}
									// Log.i("", ""+arrAptSlot.size());
									if (arrAptSlot.size() < 3) {
										if (!Util.isEmptyString(getIntent()
												.getStringExtra("pos"))) {
											if (!Util.isEmptyString(getIntent()
													.getStringExtra("type"))) {
												showFollowupDialog(v.getTag()
														.toString(), newDate,
														(String) tr.getTag(),
														aptReschedule);

											} else {
												showReScheduleDialog(v.getTag()
														.toString(), newDate,
														(String) tr.getTag(),
														aptReschedule);
											}

										} else {
											showScheduleDialog(v.getTag()
													.toString(), newDate,
													(String) tr.getTag());
										}
									} else {
										showMaxAptDialog(arrAptSlot);
									}
								}
							});
						} else if ((currTime.compareTo(startTime) >= 0)
								&& (currTime.compareTo(endTime) < 0)
								&& (j != 0)
								&& (newDate.compareTo(cal.getTime()) >= 0)) {
							c1.setBackgroundColor(Color.parseColor("#A9D6E7"));
						}
						if (j != 0) {

							final String s = EzApp.sdfddMmyy.format(newDate);
							final Date currDate = EzApp.sdfddmmyyyyhhmmss
									.parse(s + " 00:00:00");
							for (final VacationModel vacation : DashboardActivity.arrVacations) {
								final Date startDate = EzApp.sdfddmmyyyyhhmmss
										.parse(vacation.getStartDate()
												+ " 00:00:00");
								final Date endDate = EzApp.sdfddmmyyyyhhmmss
										.parse(vacation.getEndDate()
												+ " 00:00:00");
								if ((currDate.compareTo(startDate) >= 0)
										&& (currDate.compareTo(endDate) <= 0)) {
									c1.setBackgroundColor(Color
											.parseColor("#FAC0F5"));
									c1.setClickable(false);
								}
							}

							cal.setTime(currDate);
							final int dayofweek = cal.get(Calendar.DAY_OF_WEEK);
							if (DashboardActivity.dayPlan[dayofweek - 1]
									.isOff()) {
								c1.setBackgroundColor(Color
										.parseColor("#FAC0F5"));
								c1.setClickable(false);
							}
						}
					} catch (final Exception e) {
						Log.e("", e);
					}
				} else if (EzApp.sharedPref.getString(Constants.USER_TYPE, "")
						.equalsIgnoreCase("LT") && (j != 0)) {
					c1.setBackgroundColor(Color.parseColor("#ABA2FF"));
					c1.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(final View v) {
							if (!Util.isEmptyString(getIntent().getStringExtra(
									"pos"))) {
								if (!Util.isEmptyString(getIntent()
										.getStringExtra("type"))) {
									// TODO
									showFollowupDialog(v.getTag().toString(),
											newDate, (String) tr.getTag(),
											aptReschedule);

								} else {
									showReScheduleDialog(v.getTag().toString(),
											newDate, (String) tr.getTag(),
											aptReschedule);
								}

							} else {
								showScheduleDialog(v.getTag().toString(),
										newDate, (String) tr.getTag());
							}
						}
					});
				}
			}
		}
		Log.i(TAG,
				"" + new Gson().toJson(DashboardActivity.arrScheduledPatients));

		for (final Appointment pm : DashboardActivity.arrScheduledPatients) {
			final Date date = pm.aptDate;
			final TextView txt = (TextView) table.findViewWithTag(date
					.toString());
			if ((pm.getType()).equals("reschedule")) {
				aptReschedule = pm;
			} else if ((pm.getType().toLowerCase()).contains("followup")) {
				// Log.i("", new Gson().toJson(pm));
				aptReschedule = pm;
			}
			if (txt != null) {
				for (PatientShow p : DashboardActivity.arrPatientShow) {
					if (p.getPId().equalsIgnoreCase(pm.getPid()))
						txt.setText(p.getPfn());
				}
				if ((pm.getType()).equals("tentative")) {
					txt.setBackgroundColor(Color.parseColor("#0B600B"));
					txt.setTextColor(Color.WHITE);
				} else if ((pm.getType()).equals("booked")) {
					txt.setBackgroundColor(Color.parseColor("#F8C65B"));
					txt.setTextColor(Color.WHITE);
				} else if ((pm.getType()).equals("reschedule")) {
					// aptReschedule = pm;
					txt.setBackgroundColor(Color.parseColor("#02ACEF"));
					txt.setTextColor(Color.WHITE);
				} else if ((pm.getType().toLowerCase()).contains("followup")) {
					Log.i("", new Gson().toJson(pm));
					// aptReschedule = pm;
					txt.setBackgroundColor(Color.parseColor("#F8C65B"));
					txt.setTextColor(Color.WHITE);
				}
			}

		}
		findViewById(R.id.loading_progressbar).setVisibility(View.GONE);
	}

	protected void showMaxAptDialog(ArrayList<Appointment> arrAptSlot) {
		final Dialog dialog = new Dialog(this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_max_schedule_apt);
		dialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		Date currentDate = Calendar.getInstance().getTime();
		currentDate.setHours(0);
		currentDate.setMinutes(0);
		currentDate.setSeconds(0);
		Date tomorrowDate = new Date(currentDate.getTime() + 24 * 60 * 60000
				- 60000);
		int i = 0;
		for (Appointment pm : arrAptSlot) {
			try {
				final SimpleDateFormat sd;
				if (pm.aptDate.after(currentDate)
						&& pm.aptDate.before(tomorrowDate)) {
					sd = new SimpleDateFormat("' @ 'hh:mm a' Today '");
				} else {
					sd = new SimpleDateFormat(
							"' @ 'hh:mm a' on 'EEE, MMM dd', 'yyyy");
				}
				PatientShow pat = new PatientShow();
				for (PatientShow ps : DashboardActivity.arrPatientShow) {
					if (pm.getPid().equals(ps.getPId())
							&& pm.getPfId().equals(ps.getPfId())) {
						pat = ps;
					}
				}
				if (i == 0) {
					((TextView) dialog.findViewById(R.id.txt_apt_1))
							.setText(Html.fromHtml(pat.getPfn()
									+ " "
									+ pat.getPln()
									+ ", "
									+ pat.getAge()
									+ "/"
									+ pat.getGender()
									+ sd.format(pm.aptDate)
									+ " at '"
									+ EzApp.sharedPref.getString(
											Constants.DR_LOCALITY, "")
									+ "' for " + "<b>\" " + pm.getReason()
									+ " \"</b>"));
				} else if (i == 1) {
					((TextView) dialog.findViewById(R.id.txt_apt_2))
							.setText(Html.fromHtml(pat.getPfn()
									+ " "
									+ pat.getPln()
									+ ", "
									+ pat.getAge()
									+ "/"
									+ pat.getGender()
									+ sd.format(pm.aptDate)
									+ " at '"
									+ EzApp.sharedPref.getString(
											Constants.DR_LOCALITY, "")
									+ "' for " + "<b>\" " + pm.getReason()
									+ " \"</b>"));
				} else if (i == 2) {
					((TextView) dialog.findViewById(R.id.txt_apt_3))
							.setText(Html.fromHtml(pat.getPfn()
									+ " "
									+ pat.getPln()
									+ ", "
									+ pat.getAge()
									+ "/"
									+ pat.getGender()
									+ sd.format(pm.aptDate)
									+ " at '"
									+ EzApp.sharedPref.getString(
											Constants.DR_LOCALITY, "")
									+ "' for " + "<b>\" " + pm.getReason()
									+ " \"</b>"));
				}
				i++;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		dialog.setCancelable(false);
		dialog.findViewById(R.id.txt_close).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				});
		dialog.show();

	}

	private void showFollowupDialog(final String tag, final Date date,
			final String slot, final Appointment apt) {
		if (EzApp.sharedPref.getString(Constants.USER_TYPE, "D").equals("D")) {
			final Dialog dialog = EzDialog
					.getDialog(this, R.layout.dialog_shedule_appointment,
							"Followup Appointment");
			dialog.getWindow().setBackgroundDrawable(
					new ColorDrawable(android.graphics.Color.TRANSPARENT));
			final Appointment pm = new Appointment();
			Date currentDate = Calendar.getInstance().getTime();
			currentDate.setHours(0);
			currentDate.setMinutes(0);
			currentDate.setSeconds(0);
			Date tomorrowDate = new Date(currentDate.getTime() + 24 * 60
					* 60000 - 60000);
			final SimpleDateFormat sd;
			if (date.after(currentDate) && date.before(tomorrowDate)) {
				sd = new SimpleDateFormat("' @ 'hh:mm a' Today '");
			} else {
				sd = new SimpleDateFormat(
						"' @ 'hh:mm a' on 'EEE, MMM dd', 'yyyy");
			}
			if (Util.isEmptyString(patient.getPgender())) {
				patient.setPgender(" ");
			}
			((TextView) dialog.findViewById(R.id.txt_desc)).setText(patient
					.getP_detail()
					+ sd.format(date)
					+ " at '"
					+ EzApp.sharedPref.getString(Constants.DR_LOCALITY, "")
					+ "'.");
			((EditText) dialog.findViewById(R.id.edit_reason)).setText(apt
					.getReason());
			((EditText) dialog.findViewById(R.id.edit_reason))
					.setEnabled(false);

			final Button button = (Button) dialog
					.findViewById(R.id.btn_schedule);
			button.setText("Followup");
			button.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(final View v) {

					pm.aptDate = date;
					pm.setType("booked");
					pm.setNote(((EditText) dialog
							.findViewById(R.id.edit_reason)).getText()
							.toString());
					DashboardActivity.arrScheduledPatients.add(pm);
					// TODO DashboardActivity.arrConfirmedPatients.add(0,
					// pm);
					final TextView txt = (TextView) table.findViewWithTag(tag);
					for (PatientShow p : DashboardActivity.arrPatientShow) {
						if (p.getPId().equalsIgnoreCase(pm.getPid())
								&& p.getPfId().equalsIgnoreCase(pm.getPfId()))
							txt.setText(p.getPfn() + " " + p.getPln());
					}
					txt.setBackgroundColor(Color.parseColor("#F8C65B"));
					txt.setTextColor(Color.WHITE);
					appointmentFollowup(date, ((EditText) dialog
							.findViewById(R.id.edit_reason)).getText()
							.toString(), ((View) txt.getParent()).getTag()
							.toString(), apt);
					dialog.dismiss();
				}
			});
			dialog.setCancelable(false);
			dialog.show();

		} else if (EzApp.sharedPref.getString(Constants.USER_TYPE, "D").equals(
				"LT")) {
			AutoCompleteTextView actvCountry;
			AutoCompleteTextView actvState;
			AutoCompleteTextView actvCity;
			AutoCompleteTextView actvLocality;
			final Spinner spinnerVisitLocation;
			final Spinner spinnerTechnician;
			final ArrayList<String> arrVisitLocation = new ArrayList<String>();
			final ArrayList<String> arrTechnician = new ArrayList<String>();

			final Dialog dialoglab = new Dialog(this);
			dialoglab.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialoglab.setContentView(R.layout.labs_dialog_schedule_appointment);
			dialoglab.getWindow().setBackgroundDrawable(
					new ColorDrawable(android.graphics.Color.TRANSPARENT));
			final Appointment pm = new Appointment();
			Date currentDate = Calendar.getInstance().getTime();
			currentDate.setHours(0);
			currentDate.setMinutes(0);
			currentDate.setSeconds(0);
			Date tomorrowDate = new Date(currentDate.getTime() + 24 * 60
					* 60000 - 60000);
			final SimpleDateFormat sd;
			if (date.after(currentDate) && date.before(tomorrowDate)) {
				sd = new SimpleDateFormat("' @ 'hh:mm a' Today '");
			} else {
				sd = new SimpleDateFormat(
						"' @ 'hh:mm a' on 'EEE, MMM dd', 'yyyy");
			}
			if (Util.isEmptyString(patient.getPgender())) {
				patient.setPgender(" ");
			}
			((TextView) dialoglab.findViewById(R.id.txt_shedule_desc))
					.setText(patient.getP_detail()
							+ sd.format(date)
							+ " at '"
							+ EzApp.sharedPref.getString(Constants.DR_LOCALITY,
									"") + "'.");
			((TextView) dialoglab.findViewById(R.id.txt_patient_address))
					.setText(patient.getPadd1() + ", " + patient.getPadd2()
							+ ", " + patient.getParea() + ", "
							+ patient.getPcity() + ", " + patient.getPstate()
							+ ", " + patient.getPcountry() + " - "
							+ patient.getPzip());

			((TextView) dialoglab.findViewById(R.id.txt_address_1)).append(Html
					.fromHtml("<sup><font color='#EE0000'>*</font></sup>"));

			((TextView) dialoglab.findViewById(R.id.txt_country)).append(Html
					.fromHtml("<sup><font color='#EE0000'>*</font></sup>"));

			((TextView) dialoglab.findViewById(R.id.txt_state)).append(Html
					.fromHtml("<sup><font color='#EE0000'>*</font></sup>"));

			((TextView) dialoglab.findViewById(R.id.txt_locality)).append(Html
					.fromHtml("<sup><font color='#EE0000'>*</font></sup>"));

			((TextView) dialoglab.findViewById(R.id.txt_city)).append(Html
					.fromHtml("<sup><font color='#EE0000'>*</font></sup>"));

			((TextView) dialoglab.findViewById(R.id.txt_pincode)).append(Html
					.fromHtml("<sup><font color='#EE0000'>*</font></sup>"));
			((TextView) dialoglab.findViewById(R.id.txt_pincode))
					.setFilters(new InputFilter[] { new InputFilter.LengthFilter(
							6) });

			((TextView) dialoglab.findViewById(R.id.txt_reason)).append(Html
					.fromHtml("<sup><font color='#EE0000'>*</font></sup>"));

			((TextView) dialoglab.findViewById(R.id.txt_technician))
					.append(Html
							.fromHtml("<sup><font color='#EE0000'>*</font></sup>"));

			((EditText) dialoglab.findViewById(R.id.actv_address_1))
					.setText(patient.getPadd1());
			((EditText) dialoglab.findViewById(R.id.actv_address_2))
					.setText(patient.getPadd2());
			((AutoCompleteTextView) dialoglab.findViewById(R.id.actv_country))
					.setText(patient.getPcountry());
			((AutoCompleteTextView) dialoglab.findViewById(R.id.actv_state))
					.setText(patient.getPstate());
			((AutoCompleteTextView) dialoglab.findViewById(R.id.actv_city))
					.setText(patient.getPcity());
			((AutoCompleteTextView) dialoglab.findViewById(R.id.actv_locality))
					.setText(patient.getParea());
			((EditText) dialoglab.findViewById(R.id.actv_pincode))
					.setText(patient.getPzip());
			spinnerVisitLocation = (Spinner) dialoglab
					.findViewById(R.id.spinner_visit_location);
			spinnerTechnician = (Spinner) dialoglab
					.findViewById(R.id.spinner_technician);
			arrVisitLocation.clear();
			arrVisitLocation.add("Clinic");
			arrVisitLocation.add("Patient Location");
			arrTechnician.clear();
			arrTechnician.add("Select Technician");

			final ArrayAdapter<String> adapterVisitLocation = new ArrayAdapter<String>(
					this, android.R.layout.simple_spinner_item,
					arrVisitLocation);
			adapterVisitLocation
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinnerVisitLocation.setAdapter(adapterVisitLocation);
			spinnerVisitLocation
					.setOnItemSelectedListener(new OnItemSelectedListener() {

						@Override
						public void onItemSelected(AdapterView<?> arg0,
								View arg1, int pos, long arg3) {
							// TODO Auto-generated method stub
							if (pos == 0) {
								dialoglab.findViewById(R.id.rl_p_details)
										.setVisibility(View.GONE);

							} else {
								dialoglab.findViewById(R.id.rl_p_details)
										.setVisibility(View.VISIBLE);
							}
						}

						@Override
						public void onNothingSelected(AdapterView<?> arg0) {
							// TODO Auto-generated method stub

						}
					});
			final ArrayAdapter<String> adapterTechnician = new ArrayAdapter<String>(
					this, android.R.layout.simple_spinner_item, arrTechnician);
			adapterTechnician
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinnerTechnician.setAdapter(adapterTechnician);

			final Button button = (Button) dialoglab
					.findViewById(R.id.btn_schedule);
			button.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(final View v) {
					if (Util.isEmptyString(((EditText) dialoglab
							.findViewById(R.id.edit_reason)).getText()
							.toString())) {
						Util.Alertdialog(SheduleActivity.this,
								"Enter reason for visit");
					} else {
						pm.aptDate = date;
						pm.setType("booked");
						pm.setNote(((EditText) dialoglab
								.findViewById(R.id.edit_reason)).getText()
								.toString());
						DashboardActivity.arrScheduledPatients.add(pm);
						// TODO DashboardActivity.arrConfirmedPatients.add(0,
						// pm);
						final TextView txt = (TextView) table
								.findViewWithTag(tag);
						for (PatientShow p : DashboardActivity.arrPatientShow) {
							if (p.getPId().equalsIgnoreCase(pm.getPid())
									&& p.getPfId().equalsIgnoreCase(
											pm.getPfId()))
								txt.setText(p.getPfn() + " " + p.getPln());
						}
						txt.setBackgroundColor(Color.parseColor("#F8C65B"));
						txt.setTextColor(Color.WHITE);
						appointmentRegister(date, ((EditText) dialoglab
								.findViewById(R.id.edit_reason)).getText()
								.toString(), ((View) txt.getParent()).getTag()
								.toString());
						dialoglab.dismiss();
					}

				}
			});
			dialoglab.setCancelable(false);
			dialoglab.findViewById(R.id.txt_close).setOnClickListener(
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							dialoglab.dismiss();
						}
					});
			dialoglab.setCancelable(false);
			dialoglab.show();
		}
	}

	private void showInfo() {
		final Dialog dialog = EzDialog.getDialog(this, R.layout.dialog_info,
				"Info - Color codes");
		dialog.show();
	}

	private void showReScheduleDialog(final String tag, final Date date,
			final String slot, final Appointment apt) {
		if (EzApp.sharedPref.getString(Constants.USER_TYPE, "D").equals("D")) {
			final Dialog dialog = EzDialog.getDialog(this,
					R.layout.dialog_shedule_appointment,
					"Re-Schedule Appointment");
			dialog.getWindow().setBackgroundDrawable(
					new ColorDrawable(android.graphics.Color.TRANSPARENT));
			final Appointment pm = new Appointment();
			Date currentDate = Calendar.getInstance().getTime();
			currentDate.setHours(0);
			currentDate.setMinutes(0);
			currentDate.setSeconds(0);
			Date tomorrowDate = new Date(currentDate.getTime() + 24 * 60
					* 60000 - 60000);
			final SimpleDateFormat sd;
			if (date.after(currentDate) && date.before(tomorrowDate)) {
				sd = new SimpleDateFormat("' @ 'hh:mm a' Today '");
			} else {
				sd = new SimpleDateFormat(
						"' @ 'hh:mm a' on 'EEE, MMM dd', 'yyyy");
			}
			try {
				if (Util.isEmptyString(patient.getPgender())) {
					patient.setPgender(" ");
				}

				((TextView) dialog.findViewById(R.id.txt_desc)).setText(patient
						.getP_detail()
						+ sd.format(date)
						+ " at '"
						+ EzApp.sharedPref.getString(Constants.DR_LOCALITY, "")
						+ "'.");
				((EditText) dialog.findViewById(R.id.edit_reason)).setText(apt
						.getReason());
				((EditText) dialog.findViewById(R.id.edit_reason))
						.setEnabled(false);
			} catch (Exception e) {

			}
			final Button button = (Button) dialog
					.findViewById(R.id.btn_schedule);
			button.setText("Re-Schedule");
			button.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(final View v) {
					pm.aptDate = date;
					pm.setType("booked");
					pm.setNote(((EditText) dialog
							.findViewById(R.id.edit_reason)).getText()
							.toString());
					DashboardActivity.arrScheduledPatients.add(pm);
					// TODO DashboardActivity.arrConfirmedPatients.add(0,
					// pm);
					final TextView txt = (TextView) table.findViewWithTag(tag);
					for (PatientShow p : DashboardActivity.arrPatientShow) {
						if (p.getPId().equalsIgnoreCase(pm.getPid())
								&& p.getPfId().equalsIgnoreCase(pm.getPfId()))
							txt.setText(p.getPfn() + " " + p.getPln());
					}
					txt.setBackgroundColor(Color.parseColor("#F8C65B"));
					txt.setTextColor(Color.WHITE);
					appointmentReschedule(date, ((EditText) dialog
							.findViewById(R.id.edit_reason)).getText()
							.toString(), ((View) txt.getParent()).getTag()
							.toString(), apt);
					dialog.dismiss();
				}
			});
			dialog.setCancelable(false);
			dialog.show();

		} else if (EzApp.sharedPref.getString(Constants.USER_TYPE, "D").equals(
				"LT")) {
			AutoCompleteTextView actvCountry;
			AutoCompleteTextView actvState;
			AutoCompleteTextView actvCity;
			AutoCompleteTextView actvLocality;
			final Spinner spinnerVisitLocation;
			final Spinner spinnerTechnician;
			final ArrayList<String> arrVisitLocation = new ArrayList<String>();
			final ArrayList<String> arrTechnician = new ArrayList<String>();

			final Dialog dialoglab = new Dialog(this);
			dialoglab.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialoglab.setContentView(R.layout.labs_dialog_schedule_appointment);
			dialoglab.getWindow().setBackgroundDrawable(
					new ColorDrawable(android.graphics.Color.TRANSPARENT));
			final Appointment pm = new Appointment();
			Date currentDate = Calendar.getInstance().getTime();
			currentDate.setHours(0);
			currentDate.setMinutes(0);
			currentDate.setSeconds(0);
			Date tomorrowDate = new Date(currentDate.getTime() + 24 * 60
					* 60000 - 60000);
			final SimpleDateFormat sd;
			if (date.after(currentDate) && date.before(tomorrowDate)) {
				sd = new SimpleDateFormat("' @ 'hh:mm a' Today '");
			} else {
				sd = new SimpleDateFormat(
						"' @ 'hh:mm a' on 'EEE, MMM dd', 'yyyy");
			}
			if (Util.isEmptyString(patient.getPgender())) {
				patient.setPgender(" ");
			}
			((TextView) dialoglab.findViewById(R.id.txt_shedule_desc))
					.setText(patient.getP_detail()
							+ sd.format(date)
							+ " at '"
							+ EzApp.sharedPref.getString(Constants.DR_LOCALITY,
									"") + "'.");
			((TextView) dialoglab.findViewById(R.id.txt_patient_address))
					.setText(patient.getPadd1() + ", " + patient.getPadd2()
							+ ", " + patient.getParea() + ", "
							+ patient.getPcity() + ", " + patient.getPstate()
							+ ", " + patient.getPcountry() + " - "
							+ patient.getPzip());

			((TextView) dialoglab.findViewById(R.id.txt_address_1)).append(Html
					.fromHtml("<sup><font color='#EE0000'>*</font></sup>"));

			((TextView) dialoglab.findViewById(R.id.txt_country)).append(Html
					.fromHtml("<sup><font color='#EE0000'>*</font></sup>"));

			((TextView) dialoglab.findViewById(R.id.txt_state)).append(Html
					.fromHtml("<sup><font color='#EE0000'>*</font></sup>"));

			((TextView) dialoglab.findViewById(R.id.txt_locality)).append(Html
					.fromHtml("<sup><font color='#EE0000'>*</font></sup>"));

			((TextView) dialoglab.findViewById(R.id.txt_city)).append(Html
					.fromHtml("<sup><font color='#EE0000'>*</font></sup>"));

			((TextView) dialoglab.findViewById(R.id.txt_pincode)).append(Html
					.fromHtml("<sup><font color='#EE0000'>*</font></sup>"));
			((TextView) dialoglab.findViewById(R.id.txt_pincode))
					.setFilters(new InputFilter[] { new InputFilter.LengthFilter(
							6) });

			((TextView) dialoglab.findViewById(R.id.txt_reason)).append(Html
					.fromHtml("<sup><font color='#EE0000'>*</font></sup>"));

			((TextView) dialoglab.findViewById(R.id.txt_technician))
					.append(Html
							.fromHtml("<sup><font color='#EE0000'>*</font></sup>"));

			((EditText) dialoglab.findViewById(R.id.actv_address_1))
					.setText(patient.getPadd1());
			((EditText) dialoglab.findViewById(R.id.actv_address_2))
					.setText(patient.getPadd2());
			((AutoCompleteTextView) dialoglab.findViewById(R.id.actv_country))
					.setText(patient.getPcountry());
			((AutoCompleteTextView) dialoglab.findViewById(R.id.actv_state))
					.setText(patient.getPstate());
			((AutoCompleteTextView) dialoglab.findViewById(R.id.actv_city))
					.setText(patient.getPcity());
			((AutoCompleteTextView) dialoglab.findViewById(R.id.actv_locality))
					.setText(patient.getParea());
			((EditText) dialoglab.findViewById(R.id.actv_pincode))
					.setText(patient.getPzip());
			spinnerVisitLocation = (Spinner) dialoglab
					.findViewById(R.id.spinner_visit_location);
			spinnerTechnician = (Spinner) dialoglab
					.findViewById(R.id.spinner_technician);
			arrVisitLocation.clear();
			arrVisitLocation.add("Clinic");
			arrVisitLocation.add("Patient Location");
			arrTechnician.clear();
			arrTechnician.add("Select Technician");

			final ArrayAdapter<String> adapterVisitLocation = new ArrayAdapter<String>(
					this, android.R.layout.simple_spinner_item,
					arrVisitLocation);
			adapterVisitLocation
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinnerVisitLocation.setAdapter(adapterVisitLocation);
			spinnerVisitLocation
					.setOnItemSelectedListener(new OnItemSelectedListener() {

						@Override
						public void onItemSelected(AdapterView<?> arg0,
								View arg1, int pos, long arg3) {
							// TODO Auto-generated method stub
							if (pos == 0) {
								dialoglab.findViewById(R.id.rl_p_details)
										.setVisibility(View.GONE);

							} else {
								dialoglab.findViewById(R.id.rl_p_details)
										.setVisibility(View.VISIBLE);
							}
						}

						@Override
						public void onNothingSelected(AdapterView<?> arg0) {
							// TODO Auto-generated method stub

						}
					});
			final ArrayAdapter<String> adapterTechnician = new ArrayAdapter<String>(
					this, android.R.layout.simple_spinner_item, arrTechnician);
			adapterTechnician
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinnerTechnician.setAdapter(adapterTechnician);

			final Button button = (Button) dialoglab
					.findViewById(R.id.btn_schedule);
			button.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(final View v) {
					if (Util.isEmptyString(((EditText) dialoglab
							.findViewById(R.id.edit_reason)).getText()
							.toString())) {
						Util.Alertdialog(SheduleActivity.this,
								"Enter reason for visit");
					} else {
						pm.aptDate = date;
						pm.setType("booked");
						pm.setNote(((EditText) dialoglab
								.findViewById(R.id.edit_reason)).getText()
								.toString());
						DashboardActivity.arrScheduledPatients.add(pm);
						// TODO DashboardActivity.arrConfirmedPatients.add(0,
						// pm);
						final TextView txt = (TextView) table
								.findViewWithTag(tag);
						for (PatientShow p : DashboardActivity.arrPatientShow) {
							if (p.getPId().equalsIgnoreCase(pm.getPid())
									&& p.getPfId().equalsIgnoreCase(
											pm.getPfId()))
								txt.setText(p.getPfn() + " " + p.getPln());
						}
						txt.setBackgroundColor(Color.parseColor("#F8C65B"));
						txt.setTextColor(Color.WHITE);
						appointmentRegister(date, ((EditText) dialoglab
								.findViewById(R.id.edit_reason)).getText()
								.toString(), ((View) txt.getParent()).getTag()
								.toString());
						dialoglab.dismiss();
					}

				}
			});
			dialoglab.setCancelable(false);
			dialoglab.findViewById(R.id.txt_close).setOnClickListener(
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							dialoglab.dismiss();
						}
					});
			dialoglab.show();
		}
	}

	private void showScheduleDialog(final String tag, final Date date,
			final String slot) {
		if (EzApp.sharedPref.getString(Constants.USER_TYPE, "D").equals("D")) {
			final Dialog dialog = EzDialog
					.getDialog(this, R.layout.dialog_shedule_appointment,
							"Schedule Appointment");
			dialog.getWindow().setBackgroundDrawable(
					new ColorDrawable(android.graphics.Color.TRANSPARENT));
			final Appointment pm = new Appointment();
			Date currentDate = Calendar.getInstance().getTime();
			currentDate.setHours(0);
			currentDate.setMinutes(0);
			currentDate.setSeconds(0);
			Date tomorrowDate = new Date(currentDate.getTime() + 24 * 60
					* 60000 - 60000);
			final SimpleDateFormat sd;
			if (date.after(currentDate) && date.before(tomorrowDate)) {
				sd = new SimpleDateFormat("' @ 'hh:mm a' Today '");
			} else {
				sd = new SimpleDateFormat(
						"' @ 'hh:mm a' on 'EEE, MMM dd', 'yyyy");
			}
			if (Util.isEmptyString(patient.getPgender())) {
				patient.setPgender(" ");
			}
			((TextView) dialog.findViewById(R.id.txt_desc)).setText(patient
					.getP_detail()
					+ sd.format(date)
					+ " at '"
					+ EzApp.sharedPref.getString(Constants.DR_LOCALITY, "")
					+ "'.");

			final Button button = (Button) dialog
					.findViewById(R.id.btn_schedule);
			button.setText("Schedule");
			button.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(final View v) {
					if (Util.isEmptyString(((EditText) dialog
							.findViewById(R.id.edit_reason)).getText()
							.toString())) {
						Util.Alertdialog(SheduleActivity.this,
								"Enter reason for visit");
					} else {
						pm.aptDate = date;
						pm.setType("booked");
						pm.setNote(((EditText) dialog
								.findViewById(R.id.edit_reason)).getText()
								.toString());
						DashboardActivity.arrScheduledPatients.add(pm);
						// TODO DashboardActivity.arrConfirmedPatients.add(0,
						// pm);
						final TextView txt = (TextView) table
								.findViewWithTag(tag);
						for (PatientShow p : DashboardActivity.arrPatientShow) {
							if (p.getPId().equalsIgnoreCase(pm.getPid())
									&& p.getPfId().equalsIgnoreCase(
											pm.getPfId()))
								txt.setText(p.getPfn() + " " + p.getPln());
						}
						txt.setBackgroundColor(Color.parseColor("#F8C65B"));
						txt.setTextColor(Color.WHITE);
						appointmentRegister(date, ((EditText) dialog
								.findViewById(R.id.edit_reason)).getText()
								.toString(), ((View) txt.getParent()).getTag()
								.toString());
						dialog.dismiss();
					}

				}
			});
			dialog.setCancelable(false);
			dialog.show();

		} else if (EzApp.sharedPref.getString(Constants.USER_TYPE, "D").equals(
				"LT")) {
			AutoCompleteTextView actvCountry;
			AutoCompleteTextView actvState;
			AutoCompleteTextView actvCity;
			AutoCompleteTextView actvLocality;
			final Spinner spinnerVisitLocation;
			final Spinner spinnerTechnician;
			final ArrayList<String> arrVisitLocation = new ArrayList<String>();
			final ArrayList<String> arrTechnician = new ArrayList<String>();

			final Dialog dialoglab = new Dialog(this);
			dialoglab.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialoglab.setContentView(R.layout.labs_dialog_schedule_appointment);
			dialoglab.getWindow().setBackgroundDrawable(
					new ColorDrawable(android.graphics.Color.TRANSPARENT));
			final Appointment pm = new Appointment();
			Date currentDate = Calendar.getInstance().getTime();
			currentDate.setHours(0);
			currentDate.setMinutes(0);
			currentDate.setSeconds(0);
			Date tomorrowDate = new Date(currentDate.getTime() + 24 * 60
					* 60000 - 60000);
			final SimpleDateFormat sd;
			if (date.after(currentDate) && date.before(tomorrowDate)) {
				sd = new SimpleDateFormat("' @ 'hh:mm a' Today '");
			} else {
				sd = new SimpleDateFormat(
						"' @ 'hh:mm a' on 'EEE, MMM dd', 'yyyy");
			}
			if (Util.isEmptyString(patient.getPgender())) {
				patient.setPgender(" ");
			}
			((TextView) dialoglab.findViewById(R.id.txt_shedule_desc))
					.setText(patient.getP_detail()
							+ sd.format(date)
							+ " at '"
							+ EzApp.sharedPref.getString(Constants.DR_LOCALITY,
									"") + "'.");
			((TextView) dialoglab.findViewById(R.id.txt_patient_address))
					.setText(patient.getPadd1() + ", " + patient.getPadd2()
							+ ", " + patient.getParea() + ", "
							+ patient.getPcity() + ", " + patient.getPstate()
							+ ", " + patient.getPcountry() + " - "
							+ patient.getPzip());

			((TextView) dialoglab.findViewById(R.id.txt_address_1)).append(Html
					.fromHtml("<sup><font color='#EE0000'>*</font></sup>"));

			((TextView) dialoglab.findViewById(R.id.txt_country)).append(Html
					.fromHtml("<sup><font color='#EE0000'>*</font></sup>"));

			((TextView) dialoglab.findViewById(R.id.txt_state)).append(Html
					.fromHtml("<sup><font color='#EE0000'>*</font></sup>"));

			((TextView) dialoglab.findViewById(R.id.txt_locality)).append(Html
					.fromHtml("<sup><font color='#EE0000'>*</font></sup>"));

			((TextView) dialoglab.findViewById(R.id.txt_city)).append(Html
					.fromHtml("<sup><font color='#EE0000'>*</font></sup>"));

			((TextView) dialoglab.findViewById(R.id.txt_pincode)).append(Html
					.fromHtml("<sup><font color='#EE0000'>*</font></sup>"));
			((TextView) dialoglab.findViewById(R.id.txt_pincode))
					.setFilters(new InputFilter[] { new InputFilter.LengthFilter(
							6) });

			((TextView) dialoglab.findViewById(R.id.txt_reason)).append(Html
					.fromHtml("<sup><font color='#EE0000'>*</font></sup>"));

			((TextView) dialoglab.findViewById(R.id.txt_technician))
					.append(Html
							.fromHtml("<sup><font color='#EE0000'>*</font></sup>"));

			((EditText) dialoglab.findViewById(R.id.actv_address_1))
					.setText(patient.getPadd1());
			((EditText) dialoglab.findViewById(R.id.actv_address_2))
					.setText(patient.getPadd2());
			((AutoCompleteTextView) dialoglab.findViewById(R.id.actv_country))
					.setText(patient.getPcountry());
			((AutoCompleteTextView) dialoglab.findViewById(R.id.actv_state))
					.setText(patient.getPstate());
			((AutoCompleteTextView) dialoglab.findViewById(R.id.actv_city))
					.setText(patient.getPcity());
			((AutoCompleteTextView) dialoglab.findViewById(R.id.actv_locality))
					.setText(patient.getParea());
			((EditText) dialoglab.findViewById(R.id.actv_pincode))
					.setText(patient.getPzip());
			spinnerVisitLocation = (Spinner) dialoglab
					.findViewById(R.id.spinner_visit_location);
			spinnerTechnician = (Spinner) dialoglab
					.findViewById(R.id.spinner_technician);
			arrVisitLocation.clear();
			arrVisitLocation.add("Clinic");
			arrVisitLocation.add("Patient Location");
			arrTechnician.clear();
			arrTechnician.add("Select Technician");

			final ArrayAdapter<String> adapterVisitLocation = new ArrayAdapter<String>(
					this, android.R.layout.simple_spinner_item,
					arrVisitLocation);
			adapterVisitLocation
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinnerVisitLocation.setAdapter(adapterVisitLocation);
			spinnerVisitLocation
					.setOnItemSelectedListener(new OnItemSelectedListener() {

						@Override
						public void onItemSelected(AdapterView<?> arg0,
								View arg1, int pos, long arg3) {
							// TODO Auto-generated method stub
							if (pos == 0) {
								dialoglab.findViewById(R.id.rl_p_details)
										.setVisibility(View.GONE);

							} else {
								dialoglab.findViewById(R.id.rl_p_details)
										.setVisibility(View.VISIBLE);
							}
						}

						@Override
						public void onNothingSelected(AdapterView<?> arg0) {
							// TODO Auto-generated method stub

						}
					});
			final ArrayAdapter<String> adapterTechnician = new ArrayAdapter<String>(
					this, android.R.layout.simple_spinner_item, arrTechnician);
			adapterTechnician
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinnerTechnician.setAdapter(adapterTechnician);

			final Button button = (Button) dialoglab
					.findViewById(R.id.btn_schedule);
			button.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(final View v) {
					if (Util.isEmptyString(((EditText) dialoglab
							.findViewById(R.id.edit_reason)).getText()
							.toString())) {
						Util.Alertdialog(SheduleActivity.this,
								"Enter reason for visit");
					} else {
						pm.aptDate = date;
						pm.setType("booked");
						pm.setNote(((EditText) dialoglab
								.findViewById(R.id.edit_reason)).getText()
								.toString());
						DashboardActivity.arrScheduledPatients.add(pm);
						// TODO DashboardActivity.arrConfirmedPatients.add(0,
						// pm);
						final TextView txt = (TextView) table
								.findViewWithTag(tag);
						for (PatientShow p : DashboardActivity.arrPatientShow) {
							if (p.getPId().equalsIgnoreCase(pm.getPid())
									&& p.getPfId().equalsIgnoreCase(
											pm.getPfId()))
								txt.setText(p.getPfn() + " " + p.getPln());
						}
						txt.setBackgroundColor(Color.parseColor("#F8C65B"));
						txt.setTextColor(Color.WHITE);
						InRefferalModel inref = new InRefferalModel();
						LabsAppointmentController.scheduleAppointment(
								patient,
								date,
								slot,
								((EditText) dialoglab
										.findViewById(R.id.edit_reason))
										.getText().toString(),
								spinnerVisitLocation.getSelectedItemPosition() + 1,
								"0", inref, SheduleActivity.this, dialoglab,
								null);
					}

				}
			});
			dialoglab.show();
		}
	}

}
