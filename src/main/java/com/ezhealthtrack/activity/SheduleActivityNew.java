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
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TableLayout;

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
import com.ezhealthtrack.fragments.DatePickerDialogFragment;
import com.ezhealthtrack.greendao.LabAppointment;
import com.ezhealthtrack.greendao.Patient;
import com.ezhealthtrack.greendao.PatientDao.Properties;
import com.ezhealthtrack.labs.controller.LabsAppointmentController;
import com.ezhealthtrack.model.AppointmentCalenderModel;
import com.ezhealthtrack.model.PatientShow;
import com.ezhealthtrack.util.BaseActivity;
import com.ezhealthtrack.util.Constants;
import com.ezhealthtrack.util.Log;
import com.ezhealthtrack.util.NetworkCallController.OnResponse;
import com.ezhealthtrack.util.Util;
import com.ezhealthtrack.views.AppointmentCalender;
import com.ezhealthtrack.views.AppointmentCalender.OnCellClicked;
import com.google.gson.Gson;

public class SheduleActivityNew extends BaseActivity implements
		OnDateSetListener {
	private TableLayout table;
	private TableLayout tableHeader;
	private Date date;
	private Calendar cal;
	private AppointmentCalender appointmentCalender;
	private ArrayList<AppointmentCalenderModel> arrAppointmentCalenderModel = new ArrayList<AppointmentCalenderModel>();
	private AppointmentCalenderModel aptCal;
	private String type;
	private LabAppointment aptReschedule;
	private Patient patient;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shedule);
		tableHeader = (TableLayout) findViewById(R.id.table_header);
		table = (TableLayout) findViewById(R.id.table);
		tableHeader.setStretchAllColumns(true);
		table.setStretchAllColumns(true);
		cal = Calendar.getInstance();
		date = cal.getTime();
		type = getIntent().getStringExtra("type");
		if (type.equals("reschedule") || type.equals("followup")) {
			aptReschedule = LabsAppointmentController
					.getLabAppointment(getIntent().getStringExtra("bkid"));
			PatientController.getPatient(aptReschedule.getPid(),
					aptReschedule.getPfid(), this, new OnResponsePatient() {

						@Override
						public void onResponseListner(Patient response) {
							patient = response;
							getSupportActionBar().setTitle(
									" Selected Patient: "
											+ patient.getP_detail());
						}
					});
		} else {
			PatientController.getPatient(getIntent().getStringExtra("pid"),
					getIntent().getStringExtra("fid"), this,
					new OnResponsePatient() {

						@Override
						public void onResponseListner(Patient response) {
							patient = response;
							getSupportActionBar().setTitle(
									" Selected Patient: "
											+ patient.getP_detail());
						}
					});
		}
		setCalTable();
		getScheduleData(new Date());
	}

	private void setCalTable() {
		appointmentCalender = new AppointmentCalender(table, tableHeader, date,
				cal, this, AppointmentCalender.USER_LT, new OnCellClicked() {

					@Override
					public void onOnCellClickedListner(final Date date,
							String slot) {
						if (type.equals("reschedule")) {
							LabsAppointmentController.showReScheduleDialog(
									date, slot, aptReschedule,
									SheduleActivityNew.this, patient,
									new OnResponse() {

										@Override
										public void onResponseListner(
												String response) {
											if (!response.equals("error")) {
												AppointmentCalenderModel deletedapt = new AppointmentCalenderModel();
												deletedapt.setType("deleted");
												deletedapt.setAptDate(aptCal
														.getAptDate());
												aptCal.setAptDate(date);
												aptCal.setType("booked");
												arrAppointmentCalenderModel
														.add(deletedapt);
												appointmentCalender
														.setAppointments(arrAppointmentCalenderModel);
												arrAppointmentCalenderModel
														.remove(deletedapt);
												aptReschedule.setAptdate(date);
												aptReschedule.setBkid(response);
												LabsAppointmentController
														.deleteAppointment(aptReschedule);
											} else {
												Util.AlertdialogWithFinish(
														SheduleActivityNew.this,
														"There is some error in rescheduling appointment.");
											}
										}
									});
						} else if (type.equals("followup")) {
							LabsAppointmentController.showFollowupDialog(date,
									slot, aptReschedule,
									SheduleActivityNew.this, patient,
									new OnResponse() {

										@Override
										public void onResponseListner(
												String response) {
											if (!response.equals("error")) {
												AppointmentCalenderModel acm = new AppointmentCalenderModel();
												acm.setType("booked");
												acm.setAptDate(date);
												arrAppointmentCalenderModel
														.add(acm);
												appointmentCalender
														.setAppointments(arrAppointmentCalenderModel);

											} else {
												Util.AlertdialogWithFinish(
														SheduleActivityNew.this,
														"There is some error in scheduling appointment.");
											}
										}
									});
						}

						else if (type.equals("schedule")) {
							LabsAppointmentController.showScheduleDialog(date,
									slot, SheduleActivityNew.this, patient,
									new OnResponse() {

										@Override
										public void onResponseListner(
												String response) {
											if (!response.equals("error")) {
												AppointmentCalenderModel acm = new AppointmentCalenderModel();
												acm.setType("booked");
												acm.setAptDate(date);
												arrAppointmentCalenderModel
														.add(acm);
												appointmentCalender
														.setAppointments(arrAppointmentCalenderModel);
											} else {
												Util.AlertdialogWithFinish(
														SheduleActivityNew.this,
														"There is some error in scheduling appointment.");
											}
										}
									});
						}

					}
				});
	}

	private void getScheduleData(final Date date) {
		final String url = APIs.SCHEDULE_DATA();
		final Dialog loaddialog = Util.showLoadDialog(SheduleActivityNew.this);
		Log.i("", url);
		final StringRequest scheduleDataRequest = new StringRequest(
				Request.Method.POST, url, new Response.Listener<String>() {
					@Override
					public void onResponse(final String response) {
						Log.i("", response);
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
											final String type1 = bookedBy
													.getJSONObject(k)
													.getString("type");
											final AppointmentCalenderModel pm = new AppointmentCalenderModel();
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
												pm.setAptDate(cal.getTime());
												pm.setType(type1);
												pm.setPid(pid);
												pm.setPfId(pfid);
												pm.setReason(bookedBy
														.getJSONObject(k)
														.getString("reason"));
												arrAppointmentCalenderModel
														.add(pm);
												if (type.equals("reschedule")
														&& pm.getAptDate()
																.equals(aptReschedule
																		.getAptdate())) {
													pm.setType("reschedule");
													aptCal = pm;
												}

											} catch (final ParseException e) {
												Log.e("", e);
											}

										}

									}

								}

								appointmentCalender
										.setAppointments(arrAppointmentCalenderModel);

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
							Log.e("", e);
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
				loginParams.put("pat-id", "0");
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

	private void showInfo() {
		final Dialog dialog = new Dialog(this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_info);
		dialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		final Button button = (Button) dialog.findViewById(R.id.btn_close);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(final View v) {
				dialog.dismiss();

			}
		});
		dialog.show();

	}

	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		// Inflate the menu items for use in the action bar
		final MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.schedule, menu);
		return super.onCreateOptionsMenu(menu);
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
	public void onDateSet(final DatePicker view, final int year,
			final int monthOfYear, final int dayOfMonth) {
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, monthOfYear);
		cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
		date = cal.getTime();
		setCalTable();
		getScheduleData(date);
	}

}
