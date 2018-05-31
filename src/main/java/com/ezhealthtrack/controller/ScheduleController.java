package com.ezhealthtrack.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ezhealthrack.api.APIs;
import com.ezhealthtrack.EzApp;
import com.ezhealthtrack.activity.DashboardActivity;
import com.ezhealthtrack.greendao.Appointment;
import com.ezhealthtrack.model.DayPlanModel;
import com.ezhealthtrack.model.PatientShow;
import com.ezhealthtrack.model.VacationModel;
import com.ezhealthtrack.util.Constants;
import com.ezhealthtrack.util.Log;
import com.ezhealthtrack.util.Util;
import com.google.gson.Gson;

public class ScheduleController {

	static public void getSchedulePlan() {
		final String url = APIs.SCHEDULE_PLAN();
		// Log.i(TAG, url);
		final StringRequest schedulePlanRequest = new StringRequest(
				Request.Method.POST, url, new Response.Listener<String>() {
					@Override
					public void onResponse(final String response) {
						// Log.i(TAG, response);
						try {
							final JSONObject jObj = new JSONObject(response);
							if (jObj.getString("s").equals("200")) {
								final JSONObject data = jObj
										.getJSONObject("data");
								final JSONArray arrVacation = data
										.getJSONArray("vacation");
								final JSONArray arrSchedule = data
										.getJSONArray("schedule");
								final SimpleDateFormat sdfDatePre = new SimpleDateFormat(
										"yyyy-mm-dd");
								final SimpleDateFormat sdfDatePost = new SimpleDateFormat(
										"dd/mm/yyyy");
								DashboardActivity.arrVacations.clear();
								for (int i = 0; i < arrVacation.length(); i++) {
									final JSONObject vacation = arrVacation
											.getJSONObject(i);
									final VacationModel vacationModel = new VacationModel();
									vacationModel.setId(vacation
											.getString("vacation-id"));
									try {
										vacationModel.setStartDate(sdfDatePost.format(sdfDatePre.parse(vacation
												.getString("start-date"))));
										vacationModel.setEndDate(sdfDatePost.format(sdfDatePre
												.parse(vacation
														.getString("end-date"))));
									} catch (final ParseException e) {
										Log.e("", e);
									}
									DashboardActivity.arrVacations
											.add(vacationModel);
								}

								android.util.Log.v("DA", "Array Schedule = "
										+ arrSchedule.length());
								for (int i = 0; i < arrSchedule.length()
										&& i < DashboardActivity.MAX_PLAN_DAYS; i++) {
									final JSONObject schedule = arrSchedule
											.getJSONObject(i);
									final DayPlanModel dayPlan = new DayPlanModel();
									dayPlan.setId(schedule
											.getString("schedule-plan-id"));
									dayPlan.setDay(schedule.getString("day"));
									final SimpleDateFormat df = new SimpleDateFormat(
											"hh:mm a");
									final Calendar calStart = Calendar
											.getInstance();
									final Calendar calEnd = Calendar
											.getInstance();
									final Calendar calLin = Calendar
											.getInstance();
									final Calendar calLout = Calendar
											.getInstance();
									try {
										if (schedule.getInt("start-slot") == 0) {
											dayPlan.setOff(true);

										} else {
											dayPlan.setOff(false);
											final Date d1 = df
													.parse("12:00 AM");
											final Date d2 = df
													.parse("12:00 AM");
											final Date d3 = df
													.parse("12:00 AM");
											final Date d4 = df
													.parse("12:00 AM");
											calStart.setTime(d1);
											calStart.add(Calendar.MINUTE, -15);
											calEnd.setTime(d2);
											calLout.setTime(d3);
											calLout.add(Calendar.MINUTE, -15);
											calLin.setTime(d4);
											int minutes = schedule
													.getInt("start-slot") * 15;
											calStart.add(Calendar.MINUTE,
													minutes);
											dayPlan.setStartTime(df
													.format(calStart.getTime()));
											minutes = schedule
													.getInt("end-slot") * 15;
											calEnd.add(Calendar.MINUTE, minutes);
											dayPlan.setEndTime(df.format(calEnd
													.getTime()));
											minutes = schedule
													.getInt("lunch_start") * 15;
											calLout.add(Calendar.MINUTE,
													minutes);
											dayPlan.setLout(df.format(calLout
													.getTime()));
											minutes = schedule
													.getInt("lunch_end") * 15;
											calLin.add(Calendar.MINUTE, minutes);
											dayPlan.setLin(df.format(calLin
													.getTime()));
										}
									} catch (final ParseException e) {
										Log.e("", e);
									}
									if (i == 6) {
										DashboardActivity.dayPlan[0] = dayPlan;
									} else {
										DashboardActivity.dayPlan[i + 1] = dayPlan;
									}
								}

							} else {
								// Toast.makeText(
								// DashboardActivity.this,
								// "There is some error while fetching schedule plan please try again.",
								// Toast.LENGTH_SHORT).show();
							}
						} catch (final JSONException e) {
							// Toast.makeText(
							// DashboardActivity.this,
							// "There is some error while fetching schedule plan please try again.",
							// Toast.LENGTH_SHORT).show();
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
				final HashMap<String, String> loginParams = new HashMap<String, String>();
				loginParams.put("format", "json");
				loginParams.put("tenant_id", EzApp.sharedPref
						.getString(Constants.TENANT_ID, ""));
				loginParams.put("branch_id", EzApp.sharedPref
						.getString(Constants.USER_BRANCH_ID, ""));
				return loginParams;
			}

		};
		schedulePlanRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 5,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		EzApp.mVolleyQueue.add(schedulePlanRequest);

	}

	static public void getScheduleData() {
		final String url = APIs.SCHEDULE_DATA();
		final Calendar cal = Calendar.getInstance();
		final Date date = cal.getTime();
		// Log.i(TAG, url);
		final StringRequest scheduleDataRequest = new StringRequest(
				Request.Method.POST, url, new Response.Listener<String>() {
					@Override
					public void onResponse(final String response) {
						// Log.i(TAG, response);
						try {
							final JSONObject jObj = new JSONObject(response);
							if (jObj.getString("s").equals("200")) {
								DashboardActivity.arrScheduledPatients.clear();
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
											bookedBy.getJSONObject(k)
													.getString("pid");
											// final String pfn = bookedBy
											// .getJSONObject(k)
											// .getString("pfn");
											final String type = bookedBy
													.getJSONObject(k)
													.getString("type");
											final Appointment pm = new Appointment();
											// pm.setPfn(pfn);
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
												pm.setPid(bookedBy
														.getJSONObject(k)
														.getString("pid"));
												pm.setPfId(bookedBy
														.getJSONObject(k)
														.getString("pfid"));
												pm.setReason(bookedBy
														.getJSONObject(k)
														.getString("reason"));
												// Log.i(TAG, type);
												DashboardActivity.arrScheduledPatients
														.add(pm);

											} catch (final ParseException e) {
												Log.e("", e);
											}
										}
									}
								}

							} else {
								// Toast.makeText(
								// DashboardActivity.this,
								// "There is some error while fetching schedule data please try again.",
								// Toast.LENGTH_SHORT).show();
							}
						} catch (final JSONException e) {
							// Toast.makeText(
							// DashboardActivity.this,
							// "There is some error while fetching schedule data please try again.",
							// Toast.LENGTH_SHORT).show();
							Log.e("", e);
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(final VolleyError error) {
						// Toast.makeText(
						// DashboardActivity.this,
						// "There is some error while fetching schedle data please try again",
						// Toast.LENGTH_SHORT).show();

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
				final HashMap<String, String> loginParams = new HashMap<String, String>();
				loginParams.put("format", "json");
				loginParams.put("pat-id", EzApp.patientDao
						.loadAll().get(0).getPid());
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

}
