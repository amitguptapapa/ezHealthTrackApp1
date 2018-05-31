package com.ezhealthrack.api;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences.Editor;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ezhealthtrack.EzApp;
import com.ezhealthtrack.activity.DashboardActivity;
import com.ezhealthtrack.activity.EditAccountActivity;
import com.ezhealthtrack.fragments.AlertsFragment;
import com.ezhealthtrack.fragments.AssistantFragment;
import com.ezhealthtrack.fragments.ConfirmedFragment;
import com.ezhealthtrack.fragments.HistoryFragment;
import com.ezhealthtrack.fragments.InboxFragment;
import com.ezhealthtrack.fragments.NewTentativeFragment;
import com.ezhealthtrack.fragments.OutboxFragment;
import com.ezhealthtrack.greendao.Appointment;
import com.ezhealthtrack.greendao.MessageModel;
import com.ezhealthtrack.greendao.Patient;
import com.ezhealthtrack.model.AccountModel;
import com.ezhealthtrack.model.AssistantModel;
import com.ezhealthtrack.model.InRefferalModel;
import com.ezhealthtrack.model.LabPreference;
import com.ezhealthtrack.model.OutReferralModel;
import com.ezhealthtrack.model.PatientAutoSuggest;
import com.ezhealthtrack.model.PatientShow;
import com.ezhealthtrack.model.ProfileModel;
import com.ezhealthtrack.model.RadiologyModel;
import com.ezhealthtrack.one.EzUtils;
import com.ezhealthtrack.util.Constants;
import com.ezhealthtrack.util.Log;
import com.ezhealthtrack.util.Util;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class NetworkCalls {
	OnResponse mCallback;
	Activity activity;
	public static String tag = "tag";

	public interface OnResponse {
		public void onResponseListner(String api);
	}

	public NetworkCalls(Activity activity) {
		this.activity = activity;
		try {
			mCallback = (OnResponse) activity;
		} catch (final ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnHeadlineSelectedListener");
		}
	}

	public void networkCall(final String url, final Map<String, String> params,
			final int pageno, final int type) {
		android.util.Log.v("URL:", url);
		final StringRequest patientListRequest = new StringRequest(
				Request.Method.POST, url, new Response.Listener<String>() {
					@Override
					public void onResponse(final String response) {
						if (url.contains(APIs.PATIENT_LIST())) {
							responsePatientList(url, response);
						} else if (url.contains(APIs.ACCOUNTVIEW())) {
							responseAccount(response);
						} else if (url.contains(APIs.PROFILE())) {
							responseProfile(response);
						} else if (url.contains(APIs.POST_LAB_RADIOLOGY_PREF())
								&& !Util.isEmptyString(params.get("type"))
								&& params.get("type").equals("radiology")) {
							responsePostRadiologyPref(response);
						} else if (url.contains(APIs.INBOX_MESSAGES())) {
							responseInbox(response, pageno);
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(final VolleyError error) {
						Log.e("Error.Response", error);
					}
				}) {
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				final HashMap<String, String> headers = new HashMap<String, String>();
				headers.put("auth-token", Util
						.getBase64String(EzApp.sharedPref
								.getString(Constants.USER_TOKEN, "")));
				return headers;
			}

			@Override
			protected Map<String, String> getParams() {
				return params;
			}

		};
		patientListRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 5,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		patientListRequest.setTag(tag);
		EzApp.mVolleyQueue.add(patientListRequest);

	}

	public void getPatientList(final String url) {
		final HashMap<String, String> params = new HashMap<String, String>();
		params.put("format", "json");
		params.put("limit_count", "1000");
		networkCall(url, params, 0, 0);
		Log.i("", url);
	}

	private void responsePatientList(String url, String response) {
		Log.i(url, response);
		try {
			final JSONObject jObj = new JSONObject(response);
			if (jObj.getString("s").equals("200")) {
				// if (url.equals(APIs.PATIENT_LIST + "0")) {
				// EzHealthApplication.patientDao.deleteAll();
				// }
				final JSONArray patientList = jObj.getJSONArray("data");
				for (int i = 0; i < patientList.length(); i++) {
					final JSONObject patientData = patientList.getJSONObject(i);
					if (Util.isEmptyString(patientData.getString("last-visit"))) {
						patientData.remove("last-visit");
					}
					Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd")
							.create();
					final Patient pateint = gson.fromJson(
							patientData.toString(), Patient.class);
					pateint.setId((long) (Integer.parseInt(pateint.getPid()) + 100000 * Integer
							.parseInt(pateint.getFid())));
					// pateint.setId((long) Integer.parseInt(pateint.getPid()));
					EzApp.patientDao.insertOrReplace(pateint);
					// DashboardActivity.arrPatients.add(pateint);
					Log.i("", pateint.getPusername());

				}
				mCallback.onResponseListner(APIs.PATIENT_LIST());
				if (url.equals(APIs.PATIENT_LIST() + 0)) {
					// getScheduleData();
				}
				// PatientsFragment.nextUrl = jObj.getString("next_page");
			} else {
			}
		} catch (final JSONException e) {
			Log.e("", e);
		}
	}

	public void getOutboxMessages(final int pageno, final OnResponse mCallBack1) {
		final HashMap<String, String> params = new HashMap<String, String>();
		params.put("cli", "api");
		params.put("page_num", "" + pageno);
		params.put("condval", "");
		final String url = APIs.OUTBOX_MESSAGES();
		final StringRequest patientListRequest = new StringRequest(
				Request.Method.POST, url, new Response.Listener<String>() {
					@Override
					public void onResponse(final String response) {
						try {
							if (pageno == 1) {
								EzApp.messageDao
										.queryBuilder()
										.where(com.ezhealthtrack.greendao.MessageModelDao.Properties.Message_type
												.eq(Constants.OUTBOX))
										.buildDelete()
										.executeDeleteWithoutDetachingEntities();
								// EzHealthApplication.messageDao.deleteAll();
							}
							final JSONObject jObj = new JSONObject(response);
							final JSONArray data = jObj.getJSONArray("data");
							OutboxFragment.totalCount = jObj.getInt("count");
							Gson gson = new GsonBuilder().setDateFormat(
									"yyyy-MM-dd hh:mm").create();
							for (int i = 0; i < data.length(); i++) {

								final MessageModel model = gson.fromJson(data
										.getJSONObject(i).toString(),
										MessageModel.class);
								model.setId((long) Integer.parseInt(model
										.getNid()));
								model.setMessage_type(Constants.OUTBOX);
								EzApp.messageDao
										.insertOrReplace(model);
							}
							// mCallBack1.onResponseListner(APIs.OUTBOX_MESSAGES);
						} catch (final JSONException e) {
							Log.e("", e);
						}

					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(final VolleyError error) {
						Log.e("Error.Response", error);
					}
				}) {
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				final HashMap<String, String> headers = new HashMap<String, String>();
				headers.put("auth-token", Util
						.getBase64String(EzApp.sharedPref
								.getString(Constants.USER_TOKEN, "")));
				return headers;
			}

			@Override
			protected Map<String, String> getParams() {
				return params;
			}

		};
		patientListRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 5,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		patientListRequest.setTag(tag);
		EzApp.mVolleyQueue.add(patientListRequest);

	}

	public void getAccount() {

		final String url = APIs.ACCOUNTVIEW();
		final HashMap<String, String> params = new HashMap<String, String>();
		networkCall(url, params, 0, 0);

	}

	private void responseAccount(String response) {
		Log.i(tag, "profile = " + response);
		try {
			final JSONObject jObj = new JSONObject(response);
			final JSONObject data = jObj.getJSONObject("data");

			EditAccountActivity.profile = new Gson().fromJson(data.toString(),
					AccountModel.class);
			try {
				EditAccountActivity.profile
						.setDob(EzApp.sdfddMmyy
								.format(EzApp.sdfMmddyy
										.parse(EditAccountActivity.profile
												.getDob())));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Editor editor = EzApp.sharedPref.edit();
			editor.putString(Constants.DR_ADDRESS,
					EditAccountActivity.profile.getAddress() + " "
							+ EditAccountActivity.profile.getAddress2() + ", "
							+ EditAccountActivity.profile.getCmbArea() + ", "
							+ EditAccountActivity.profile.getCmbCity() + ", "
							+ EditAccountActivity.profile.getCmbState() + ", "
							+ EditAccountActivity.profile.getCountry());
			editor.putString(Constants.DR_LOCALITY,
					EditAccountActivity.profile.getCmbArea());
			editor.commit();

		} catch (final JSONException e) {
			Log.e("", e);
		}
	}

	public void getProfile() {
		final String url = APIs.PROFILE();
		final HashMap<String, String> params = new HashMap<String, String>();
		params.put("cli", "api");
		networkCall(url, params, 0, 0);
	}

	private void responseProfile(String response) {
		try {
			final JSONObject jObj = new JSONObject(response);
			final JSONObject data = jObj.getJSONObject("data");
			DashboardActivity.profile = new Gson().fromJson(data.toString(),
					ProfileModel.class);
			mCallback.onResponseListner(APIs.PROFILE());
		} catch (final Exception e) {
			Log.e("", e);
		}
	}

	public void postRadiologyPref(final HashMap<String, String> hashRadiPref) {
		final String url = APIs.POST_LAB_RADIOLOGY_PREF();
		final HashMap<String, String> params = new HashMap<String, String>();
		params.put("format", "json");
		params.put("type", "radiology");
		final JSONArray list = new JSONArray();
		for (final String s : DashboardActivity.arrRadiSelected) {
			list.put(hashRadiPref.get(s));
			Log.i(s, hashRadiPref.get(s));
		}
		params.put("labtest", list.toString());
		Log.i("params", params.toString());
		networkCall(url, params, 0, 0);
	}

	private void responsePostRadiologyPref(String response) {
		try {
			final JSONObject jObj = new JSONObject(response);
			if (jObj.getString("s").equals("200")) {
				Util.Alertdialog(activity,
						"Radiology Preferences updated successfully");
			} else {
				Util.Alertdialog(activity,
						"There is some error while sending radiology preferences, please try again.");
			}
		} catch (final JSONException e) {
			Util.Alertdialog(activity,
					"There is some error while sending radiology preferences, please try again.");
			Log.e("", e);
		}
	}

	public void postLabPref() {
		final String url = APIs.POST_LAB_PREF();
		Log.i("postLabPref", url);
		final Dialog loaddialog = Util.showLoadDialog(activity);
		final StringRequest schedulePlanRequest = new StringRequest(
				Request.Method.POST, url, new Response.Listener<String>() {
					@Override
					public void onResponse(final String response) {
						Log.i("postLabPref:Response", response);
						try {
							final JSONObject jObj = new JSONObject(response);
							if (jObj.getString("s").equals("200")) {
								loaddialog.dismiss();
								Util.Alertdialog(activity,
										"Lab Preferences updated successfully");

							} else {
								Util.Alertdialog(activity,
										"Error! Lab Preferences could not be updated");
							}
						} catch (final JSONException e) {
							Util.Alertdialog(activity,
									"Error! Lab Preferences could not be updated");
							Log.e("", e);
						}
						loaddialog.dismiss();
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(final VolleyError error) {
						Util.Alertdialog(activity,
								"There is network error while sending lab preferences please try again.");

						Log.e("Error.Response", error);
					}
				}) {
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				final HashMap<String, String> loginParams = new HashMap<String, String>();
				loginParams.put("auth-token", Util
						.getBase64String(EzApp.sharedPref
								.getString(Constants.USER_TOKEN, "")));
				Log.i("Headers:", loginParams.toString());
				return loginParams;
			}

			@Override
			protected Map<String, String> getParams() {
				final HashMap<String, String> loginParams = new HashMap<String, String>();
				loginParams.put("format", "json");
				loginParams.put("submit", "TRUE");
				int i = 0;
				for (final LabPreference s : DashboardActivity.arrLabSelected) {
					if (s.isChecked) {
						loginParams.put("lab_preference[" + i + "]", s.getId());
						i++;
					}
				}
				Log.i("Params:", loginParams.toString());
				return loginParams;
			}

		};
		schedulePlanRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 5,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		EzApp.mVolleyQueue.add(schedulePlanRequest);

	}

	public void getAlerts(final int pageno) {
		final String url = APIs.ALERTS();
		final StringRequest patientListRequest = new StringRequest(
				Request.Method.POST, url, new Response.Listener<String>() {
					@Override
					public void onResponse(final String response) {
						// //Log.i("get alerts", response);
						try {
							final JSONObject jObj = new JSONObject(response);
							final JSONArray data = jObj.getJSONArray("data");
							AlertsFragment.totalCount = jObj.getInt("count");
							AlertsFragment.unreadCount = jObj.getInt("unread");
							if (pageno == 1) {
								DashboardActivity.arrAlerts.clear();
							}
							Gson gson = new GsonBuilder().setDateFormat(
									"yyyy-MM-dd hh:mm").create();
							for (int i = 0; i < data.length(); i++) {

								final MessageModel model = gson.fromJson(data
										.getJSONObject(i).toString(),
										MessageModel.class);
								DashboardActivity.arrAlerts.add(model);
							}
							mCallback.onResponseListner(APIs.ALERTS());
							if ((pageno * 9) < AlertsFragment.totalCount) {
								getAlerts(pageno + 1);
							}

						} catch (final JSONException e) {
							// Toast.makeText(
							// DashboardActivity.this,
							// "There is some error while fetching patient list please try again.",
							// Toast.LENGTH_SHORT).show();
							Log.e("", e);
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(final VolleyError error) {
						// loadingProgressBar.setVisibility(View.GONE);
						// Toast.makeText(
						// DashboardActivity.this,
						// "There is some error while fetching patient list please try again",
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
				loginParams.put("cli", "api");
				loginParams.put("page_num", "" + pageno);
				loginParams.put("condval", "");
				return loginParams;
			}

		};
		patientListRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 5,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		EzApp.mVolleyQueue.add(patientListRequest);

	}

	public void getAssistants(final int pageno, final String cond) {
		final String url = APIs.ASSISTANTS();
		final StringRequest patientListRequest = new StringRequest(
				Request.Method.POST, url, new Response.Listener<String>() {
					@Override
					public void onResponse(final String response) {
						Log.i("get assistants", response);
						try {
							final JSONObject jObj = new JSONObject(response);
							final JSONArray data = jObj.getJSONArray("data");
							if (pageno == 1) {
								DashboardActivity.arrAssistants.clear();
							}
							for (int i = 0; i < data.length(); i++) {

								final AssistantModel model = new Gson()
										.fromJson(data.getJSONObject(i)
												.toString(),
												AssistantModel.class);
								DashboardActivity.arrAssistants.add(model);
							}
							mCallback.onResponseListner(APIs.ASSISTANTS());
							AssistantFragment.isAddAssistant = jObj
									.getBoolean("addAssistants");
							if ((pageno * 6) < jObj.getInt("count")) {
								getAssistants(pageno + 1,
										jObj.getString("condval"));
							}

						} catch (final JSONException e) {
							// Toast.makeText(
							// DashboardActivity.this,
							// "There is some error while fetching patient list please try again.",
							// Toast.LENGTH_SHORT).show();
							Log.e("", e);
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(final VolleyError error) {
						// loadingProgressBar.setVisibility(View.GONE);
						// Toast.makeText(
						// DashboardActivity.this,
						// "There is some error while fetching patient list please try again",
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
				loginParams.put("cli", "api");
				loginParams.put("page_num", "" + pageno);
				if (!Util.isEmptyString(cond)) {
					loginParams.put("condval", cond);
				}
				return loginParams;
			}

		};
		patientListRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 5,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		EzApp.mVolleyQueue.add(patientListRequest);

	}

	public void getConfirmedList(final int page_num, final String cond,
			final OnResponse mCallback, final PatientAutoSuggest pat,
			final Date fdate, final Date tdate) {

		final String url = APIs.CONFIRMED_LIST();
		Log.i("", url);
		final StringRequest patientListRequest = new StringRequest(
				Request.Method.POST, url, new Response.Listener<String>() {
					@Override
					public void onResponse(final String response) {
						Log.i("Confirmed =", response);
						try {
							final JSONObject jObj = new JSONObject(response);
							if (jObj.getString("s").equals("200")) {

								// set direct check-in flag
								EzUtils.setSharedPreference(
										Constants.CFG_DIRECT_CHECKIN,
										jObj.optString("direct_checkin"));

								ConfirmedFragment.totalCount = jObj
										.getInt("count");
								final JSONArray patientList = jObj
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

								if (page_num == 1) {
									DashboardActivity.arrConfirmedPatients
											.clear();
								}
								for (int i = 0; i < patientList.length(); i++) {
									final JSONObject patientData = patientList
											.getJSONObject(i);
									Appointment appointment = new Appointment();
									// boolean haveAppointment = false;
									for (final Appointment apt : DashboardActivity.arrConfirmedPatients) {
										if (apt.getBkid().equals(
												patientData.getString("bk-id"))) {
											// haveAppointment = true;
											appointment = apt;
										}
									}
									appointment = new Appointment();
									DashboardActivity.arrConfirmedPatients
											.add(appointment);
									appointment.setBkid(patientData
											.getString("bk-id"));
									appointment.setPid(patientData
											.getString("p-id"));
									appointment.setPfId(patientData
											.getString("pf-id"));
									// appointment.setPfn(patientData
									// .getString("pfn"));
									// appointment.setPln(patientData
									// .getString("pln"));
									appointment.setReason(patientData
											.getString("reason"));
									appointment.setWid(patientData
											.getString("w-id"));
									appointment.setWiid(patientData
											.getString("wi-id"));
									appointment.setWistep(patientData
											.getString("wi-step"));
									appointment.setFlag(patientData
											.getString("flag"));
									appointment.setVisit(patientData
											.getString("visit"));
									if (patientData.has("ep-id")) {
										appointment.setEpid(patientData
												.getString("ep-id"));
									}
									if (patientData.has("si-id")) {
										appointment.setSiid(patientData
												.getString("si-id"));
									}
									if (patientData.has("mp-id")) {
										appointment.setMpid(patientData
												.getString("mp-id"));
									}
									if (patientData.has("follow-id")) {
										appointment.setFollowid(patientData
												.getString("follow-id"));
									}
									// TODO: remove it
									if (!patientData.has("apt_start_time")) {
										patientData.put("apt_start_time", 600);
									}
									appointment.aptDate = EzUtils.getDisplayDateTime(
											patientData.getString("apt-date"),
											patientData
													.getInt("apt_start_time"));

									// final SimpleDateFormat sdf = new
									// SimpleDateFormat(
									// "MM/dd/yyyy");
									// try {
									// final Date date = sdf
									// .parse(patientData
									// .getString("apt-date"));
									// final Calendar cal = Calendar
									// .getInstance();
									// cal.setTime(date);
									// cal.set(Calendar.HOUR_OF_DAY, 0);
									// cal.set(Calendar.MINUTE, 0);
									// cal.add(Calendar.MINUTE,
									// (patientData
									// .getInt("slot-id") - 1) * 15);
									// appointment.aptDate = cal.getTime();
									// } catch (final Exception e) {
									// Log.e("", e);
									// }

								}
								ConfirmedFragment.condVal = jObj
										.getString("condval");
								mCallback.onResponseListner(APIs
										.CONFIRMED_LIST());
							} else {
								// Toast.makeText(
								// DashboardActivity.this,
								// "There is some error while fetching confirmed list please try again.",
								// Toast.LENGTH_SHORT).show();
							}
						} catch (final JSONException e) {
							// Toast.makeText(
							// DashboardActivity.this,
							// "There is some error while fetching confirmed list please try again.",
							// Toast.LENGTH_SHORT).show();
							Log.e("", e);
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(final VolleyError error) {
						// loadingProgressBar.setVisibility(View.GONE);
						// Toast.makeText(
						// DashboardActivity.this,
						// "There is some error while fetching confirmed list please try again",
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
				loginParams.put("page_num", "" + page_num);
				loginParams.put("limit_count", "50");
				if (!Util.isEmptyString(cond))
					loginParams.put("condval", cond);
				if (pat != null)
					loginParams.put("sel_pat", pat.getId());
				if (fdate != null)
					loginParams.put("fdate",
							EzApp.sdfddMmyy1.format(fdate));
				if (tdate != null)
					loginParams.put("tdate",
							EzApp.sdfddMmyy1.format(tdate));
				return loginParams;

			}

		};
		patientListRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 5,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		EzApp.mVolleyQueue.add(patientListRequest);

	}

	public void getHistoryList(final int page_num, final String cond,
			final OnResponse mCallback, final PatientAutoSuggest pat,
			final Date fdate, final Date tdate) {

		final String url = APIs.HISTORY_LIST();
		// //Log.i(TAG, url);
		final StringRequest patientListRequest = new StringRequest(
				Request.Method.POST, url, new Response.Listener<String>() {
					@Override
					public void onResponse(final String response) {
						Log.i(tag, "History =" + response);
						try {
							final JSONObject jObj = new JSONObject(response);
							if (jObj.getString("s").equals("200")) {
								HistoryFragment.totalCount = jObj
										.getInt("count");
								final JSONArray patientList = jObj
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
								if (page_num == 1) {
									DashboardActivity.arrHistoryPatients
											.clear();
								}
								for (int i = 0; i < patientList.length(); i++) {
									final JSONObject patientData = patientList
											.getJSONObject(i);
									Appointment appointment = new Appointment();
									DashboardActivity.arrHistoryPatients
											.add(appointment);
									appointment.setBkid(patientData
											.getString("bk-id"));
									appointment.setPid(patientData
											.getString("p-id"));
									if (patientData.has("pf-id"))
										appointment.setPfId(patientData
												.getString("pf-id"));
									else
										appointment.setPfId("0");

									// appointment.setPfn(patientData
									// .getString("pfn"));
									// appointment.setPln(patientData
									// .getString("pln"));
									appointment.setReason(patientData
											.getString("reason"));
									appointment.setWid(patientData
											.getString("w-id"));
									appointment.setWiid(patientData
											.getString("wi-id"));
									appointment.setWistep(patientData
											.getString("wi-step"));
									appointment.setFlag(patientData
											.getString("flag"));
									appointment.setVisit(patientData
											.getString("visit"));
									if (patientData.has("ep-id")) {
										appointment.setEpid(patientData
												.getString("ep-id"));
									}
									if (patientData.has("si-id")) {
										appointment.setSiid(patientData
												.getString("si-id"));
									}
									if (patientData.has("mp-id")) {
										appointment.setMpid(patientData
												.getString("mp-id"));
									}
									if (patientData.has("follow-id")) {
										appointment.setFollowid(patientData
												.getString("follow-id"));
									}
									// TODO: remove it
									if (!patientData.has("apt_start_time")) {
										patientData.put("apt_start_time", 600);
									}
									appointment.aptDate = EzUtils.getDisplayDateTime(
											patientData.getString("apt-date"),
											patientData
													.getInt("apt_start_time"));

									// final SimpleDateFormat sdf = new
									// SimpleDateFormat(
									// "MM/dd/yyyy");
									// try {
									// final Date date = sdf.parse(patientData
									// .getString("apt-date"));
									// final Calendar cal = Calendar
									// .getInstance();
									// cal.setTime(date);
									// cal.set(Calendar.HOUR_OF_DAY, 0);
									// cal.set(Calendar.MINUTE, 0);
									// cal.add(Calendar.MINUTE,
									// (patientData.getInt("slot-id") - 1) *
									// 15);
									// appointment.aptDate = cal.getTime();
									// } catch (final Exception e) {
									// Log.e("", e);
									// }

								}
								mCallback.onResponseListner(APIs.HISTORY_LIST());
								HistoryFragment.condVal = jObj
										.getString("condval");
							} else {
								// Toast.makeText(
								// DashboardActivity.this,
								// "There is some error while fetching confirmed list please try again.",
								// Toast.LENGTH_SHORT).show();
							}
						} catch (final JSONException e) {
							// Toast.makeText(
							// DashboardActivity.this,
							// "There is some error while fetching confirmed list please try again.",
							// Toast.LENGTH_SHORT).show();
							Log.e("", e);
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(final VolleyError error) {
						// loadingProgressBar.setVisibility(View.GONE);
						// Toast.makeText(
						// DashboardActivity.this,
						// "There is some error while fetching confirmed list please try again",
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
				loginParams.put("page_num", "" + page_num);
				if (!Util.isEmptyString(cond))
					loginParams.put("condval", cond);
				loginParams.put("limit_count", "50");
				if (pat != null)
					loginParams.put("sel_pat", pat.getId());
				if (fdate != null)
					loginParams.put("fdate",
							EzApp.sdfddMmyy1.format(fdate));
				if (tdate != null)
					loginParams.put("tdate",
							EzApp.sdfddMmyy1.format(tdate));
				Log.i("", loginParams.toString());
				return loginParams;
			}

		};
		patientListRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 5,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		EzApp.mVolleyQueue.add(patientListRequest);

	}

	public void getInReferralList(final int pageno) {
		final String url = APIs.IN_REFFERRAL_LIST();
		final StringRequest patientListRequest = new StringRequest(
				Request.Method.POST, url, new Response.Listener<String>() {
					@Override
					public void onResponse(final String response) {
						Log.i("", response);
						try {
							final JSONObject jObj = new JSONObject(response);
							final JSONArray data = jObj.getJSONArray("data");
							if (pageno == 1) {
								DashboardActivity.arrRefferedByPatients.clear();
							}
							for (int i = 0; i < data.length(); i++) {
								final InRefferalModel model = new Gson()
										.fromJson(data.getJSONObject(i)
												.toString(),
												InRefferalModel.class);
								DashboardActivity.arrRefferedByPatients
										.add(model);
							}
							if ((pageno * 6) < jObj.getInt("totalItemCount")) {
								getInReferralList(pageno + 1);
							}
//							mCallback.onResponseListner(APIs
//									.IN_REFFERRAL_LIST());
						} catch (final JSONException e) {
							// Toast.makeText(
							// DashboardActivity.this,
							// "There is some error while fetching patient list please try again.",
							// Toast.LENGTH_SHORT).show();
							Log.e("", e);
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(final VolleyError error) {
						// loadingProgressBar.setVisibility(View.GONE);
						// Toast.makeText(
						// DashboardActivity.this,
						// "There is some error while fetching patient list please try again",
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
				loginParams.put("page_num", "" + pageno);
				loginParams.put("condval", "");
				return loginParams;
			}

		};
		patientListRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 5,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		EzApp.mVolleyQueue.add(patientListRequest);

	}

	public void getInboxMessages(final int pageno) {
		final String url = APIs.INBOX_MESSAGES();
		final HashMap<String, String> params = new HashMap<String, String>();
		params.put("cli", "api");
		params.put("page_num", "" + pageno);
		params.put("condval", "");
		networkCall(url, params, pageno, 0);
	}

	private void responseInbox(String response, int pageno) {
		try {
			if (pageno == 1) {
				EzApp.messageDao
						.queryBuilder()
						.where(com.ezhealthtrack.greendao.MessageModelDao.Properties.Message_type
								.eq(Constants.INBOX)).buildDelete()
						.executeDeleteWithoutDetachingEntities();
				// EzHealthApplication.messageDao.deleteAll();
			}
			final JSONObject jObj = new JSONObject(response);
			final JSONArray data = jObj.getJSONArray("data");
			InboxFragment.totalCount = jObj.getInt("count");
			InboxFragment.unreadCount = jObj.getInt("unread");
			Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd hh:mm")
					.create();
			for (int i = 0; i < data.length(); i++) {
				final MessageModel model = gson.fromJson(data.getJSONObject(i)
						.toString(), MessageModel.class);
				model.setId((long) Integer.parseInt(model.getNid()));
				model.setMessage_type(Constants.INBOX);
				EzApp.messageDao.insertOrReplace(model);
				// DashboardActivity.arrInboxMessages.add(model);
			}
			mCallback.onResponseListner(APIs.INBOX_MESSAGES());

		} catch (final JSONException e) {
			// Toast.makeText(
			// DashboardActivity.this,
			// "There is some error while fetching patient list please try again.",
			// Toast.LENGTH_SHORT).show();
			Log.e("", e);
		}
	}

	public void getNewTentativeList(final OnResponse mCallback) {

		final String url = APIs.NEW_PATIENT_LIST();
		// Log.i(TAG, url);
		final StringRequest patientListRequest = new StringRequest(
				Request.Method.POST, url, new Response.Listener<String>() {
					@Override
					public void onResponse(final String response) {
						Log.i("", response);
						try {
							final JSONObject jObj = new JSONObject(response);
							if (jObj.getString("s").equals("200")) {
								NewTentativeFragment.totalCount = jObj
										.getInt("count");
								final JSONArray patientList = jObj
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
								DashboardActivity.arrNewTentativePatients
										.clear();
								for (int i = 0; i < patientList.length(); i++) {
									final JSONObject patientData = patientList
											.getJSONObject(i);
									final Appointment appointment = new Appointment();
									appointment.setBkid(patientData
											.getString("bk-id"));
									appointment.setPid(patientData
											.getString("p-id"));
									if (patientData.has("pf-id"))
										appointment.setPfId(patientData
												.getString("pf-id"));
									else
										appointment.setPfId("0");
									// appointment.setPfn(patientData
									// .getString("pfn"));
									// appointment.setPln(patientData
									// .getString("pln"));
									appointment.setReason(patientData
											.getString("reason"));
									appointment.setWid(patientData
											.getString("w-id"));
									appointment.setWiid(patientData
											.getString("wi-id"));
									appointment.setWistep(patientData
											.getString("wi-step"));
									// TODO: remove it
									if (!patientData.has("apt_start_time")) {
										patientData.put("apt_start_time", 600);
									}
									appointment.aptDate = EzUtils.getDisplayDateTime(
											patientData.getString("apt-date"),
											patientData
													.getInt("apt_start_time"));

									// final SimpleDateFormat sdf = new
									// SimpleDateFormat(
									// "MM/dd/yyyy");
									// try {
									// final Date date = sdf
									// .parse(patientData
									// .getString("apt-date"));
									// final Calendar cal = Calendar
									// .getInstance();
									// cal.setTime(date);
									// cal.set(Calendar.HOUR_OF_DAY, 0);
									// cal.set(Calendar.MINUTE, 0);
									// cal.add(Calendar.MINUTE,
									// (patientData
									// .getInt("slot-id") - 1) * 15);
									// appointment.aptDate = cal.getTime();
									// } catch (final Exception e) {
									// Log.e("", e);
									// }

									if (!appointment.getWistep().equals(
											"approved"))
										DashboardActivity.arrNewTentativePatients
												.add(appointment);

								}
								mCallback.onResponseListner(APIs
										.NEW_PATIENT_LIST());
								// newTentativeFragment.onResume();
							} else {
								// Toast.makeText(
								// DashboardActivity.this,
								// "There is some error while fetching confirmed list please try again.",
								// Toast.LENGTH_SHORT).show();
							}
						} catch (final JSONException e) {
							// Toast.makeText(
							// DashboardActivity.this,
							// "There is some error while fetching confirmed list please try again.",
							// Toast.LENGTH_SHORT).show();
							Log.e("", e);
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(final VolleyError error) {
						// loadingProgressBar.setVisibility(View.GONE);
						// Toast.makeText(
						// DashboardActivity.this,
						// "There is some error while fetching confirmed list please try again",
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
				loginParams.put("limit_count", "50");
				return loginParams;
			}

		};
		patientListRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 5,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		EzApp.mVolleyQueue.add(patientListRequest);

	}

	public void getOutReferralList(final int pageno) {
		final String url = APIs.OUT_REFFERRAL_LIST();
		final StringRequest patientListRequest = new StringRequest(
				Request.Method.POST, url, new Response.Listener<String>() {
					@Override
					public void onResponse(final String response) {
						// Log.i(TAG, "refer to " + response);
						try {
							final JSONObject jObj = new JSONObject(response);
							final JSONArray data = jObj.getJSONArray("data");
							if (pageno == 1) {
								DashboardActivity.arrRefferedToPatients.clear();
							}
							for (int i = 0; i < data.length(); i++) {
								final OutReferralModel model = new Gson()
										.fromJson(data.getJSONObject(i)
												.toString(),
												OutReferralModel.class);
								DashboardActivity.arrRefferedToPatients
										.add(model);
							}
							if ((pageno * 6) < jObj.getInt("totalItemCount")) {
								getOutReferralList(pageno + 1);
							}
							mCallback.onResponseListner(APIs
									.OUT_REFFERRAL_LIST());

						} catch (final JSONException e) {
							// Toast.makeText(
							// DashboardActivity.this,
							// "There is some error while fetching patient list please try again.",
							// Toast.LENGTH_SHORT).show();
							Log.e("", e);
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(final VolleyError error) {
						// loadingProgressBar.setVisibility(View.GONE);
						// Toast.makeText(
						// DashboardActivity.this,
						// "There is some error while fetching patient list please try again",
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
				loginParams.put("cli", "api");
				loginParams.put("page_num", "" + pageno);
				loginParams.put("condval", "");
				return loginParams;
			}

		};
		patientListRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 5,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		EzApp.mVolleyQueue.add(patientListRequest);

	}

	public void getRadiPreference(final HashMap<String, String> hashRadiPref) {
		final String url = APIs.LAB_RADIOLOGY_PREF();
		final StringRequest patientListRequest = new StringRequest(
				Request.Method.POST, url, new Response.Listener<String>() {
					@Override
					public void onResponse(final String response) {
						Log.i(" radi :", response);
						try {
							final JSONObject jObj = new JSONObject(response);
							if (jObj.getString("s").equals("200")) {
								DashboardActivity.arrRadiPreferences.clear();
								final JSONArray list = jObj
										.getJSONArray("list");
								final JSONArray subcategory = jObj
										.getJSONArray("subcategory");

								for (int i = 0; i < list.length(); i++) {

									DashboardActivity.arrRadiPreferences
											.add(new Gson().fromJson(list
													.getJSONObject(i)
													.toString(),
													RadiologyModel.class));
									hashRadiPref.put(list.getJSONObject(i)
											.getString("test_name"), list
											.getJSONObject(i).toString());
								}
								for (int i = 0; i < subcategory.length(); i++) {

									DashboardActivity.arrRadiPreferences
											.add(new Gson().fromJson(
													subcategory
															.getJSONObject(i)
															.toString(),
													RadiologyModel.class));
									hashRadiPref.put(
											subcategory.getJSONObject(i)
													.getString("test_name"),
											subcategory.getJSONObject(i)
													.toString());
								}
								final JSONArray checked = jObj
										.getJSONArray("checked");
								DashboardActivity.arrRadiSelected.clear();
								for (int i = 0; i < checked.length(); i++) {
									DashboardActivity.arrRadiSelected
											.add(checked.getString(i));
								}

							} else {
								// Toast.makeText(
								// DashboardActivity.this,
								// "There is some error while fetching patient list please try again.",
								// Toast.LENGTH_SHORT).show();
							}
						} catch (final JSONException e) {
							// Toast.makeText(
							// DashboardActivity.this,
							// "There is some error while fetching patient list please try again.",
							// Toast.LENGTH_SHORT).show();
							Log.e("", e);
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(final VolleyError error) {
						// loadingProgressBar.setVisibility(View.GONE);
						// Toast.makeText(
						// DashboardActivity.this,
						// "There is some error while fetching patient list please try again",
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
				loginParams.put("type", "radiology");
				return loginParams;
			}

		};
		patientListRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 5,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		EzApp.mVolleyQueue.add(patientListRequest);

	}

}
