package com.ezhealthtrack.controller;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ezhealthrack.api.APIs;
import com.ezhealthtrack.EzApp;
import com.ezhealthtrack.greendao.Patient;
import com.ezhealthtrack.greendao.PatientDao.Properties;
import com.ezhealthtrack.labs.controller.OrderController.OnResponseData;
import com.ezhealthtrack.model.PatientCopyAddress.DefaultAddress;
import com.ezhealthtrack.one.EzUtils;
import com.ezhealthtrack.util.Constants;
import com.ezhealthtrack.util.Log;
import com.ezhealthtrack.util.NetworkCallController.OnResponse;
import com.ezhealthtrack.util.Util;
import com.google.gson.Gson;

public class PatientController {
	public interface OnResponsePatient {
		public void onResponseListner(Patient response);
	}

	public interface OnResponsePatientCopy {
		public void onResponseListner(DefaultAddress response);
	}

	private static void getNewPatient(final String pid, final String fid,
			final OnResponsePatient onresponse, Activity context) {
		final String url = APIs.PATIENT_VIEW() + "/pat_id/" + pid + "/fam_id/"
				+ fid + "/format/json";
		final HashMap<String, String> params = new HashMap<String, String>();
		params.put("pat_id", pid);
		params.put("fam_id", fid);
		EzApp.networkController.networkCall(context, url, params,
				new OnResponse() {

					@Override
					public void onResponseListner(String response) {
						JSONObject jObj;
						try {
							jObj = new JSONObject(response);
							if (jObj.getString("s").equals("200")) {
								JSONObject patData = jObj
										.getJSONObject("pat_data");
								Patient pat = new Patient();
								pat.setPid(pid);
								pat.setFid(fid);
								pat.setId((long) (Integer.parseInt(pat.getPid()) + 100000 * Integer
										.parseInt(pat.getFid())));
								pat.setPfn(patData.getString("fname"));
								pat.setPmn(patData.getString("mname"));
								pat.setPln(patData.getString("lname"));
								pat.setPgender(patData.getString("gender"));
								pat.setPdob(patData.getString("dob"));
								pat.setPemail(patData.getString("email"));
								pat.setPmobnum(patData.getString("mobile"));
								pat.setPadd1(patData.getString("address"));
								pat.setPadd2(patData.getString("address2"));
								pat.setPcountryid(patData.getString("country"));
								pat.setPstateid(patData.getString("cmbState"));
								pat.setPcityid(patData.getString("cmbCity"));
								pat.setPareaid(patData.getString("cmbArea"));
								pat.setHeight(patData.getString("height"));
								pat.setHaircolor(patData.getString("haircolor"));
								pat.setEyecolor(patData.getString("eyecolor"));
								pat.setVisiblemark(patData
										.getString("visiblemark"));
								pat.setPzip(patData.getString("zip"));
								pat.setUid(patData.getString("uid"));
								pat.setUid_type(patData.getString("uid_type"));

								pat.setDisplay_id(patData
										.getString("unique_id"));
								pat.setPcountry(patData
										.getString("country_name"));
								pat.setPstate(patData
										.getString("cmbState_name"));
								pat.setPcity(patData.getString("cmbCity_name"));
								pat.setParea(patData.getString("cmbArea_name"));

								if (Util.isEmptyString(pat.getPmn()))
									pat.setP_detail(pat.getPfn() + " "
											+ pat.getPln());
								else
									pat.setP_detail(pat.getPfn() + " "
											+ pat.getPmn() + " " + pat.getPln());

								EzApp.patientDao.insertOrReplace(pat);
								onresponse.onResponseListner(pat);
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});

	}

	public static void postNewPatientData(final Activity activity,
			final Patient patient, final OnResponse onresponse) {
		final String url = APIs.ADD_NEW_PATIENT_SUBMIT();
		final HashMap<String, String> postParams = new HashMap<String, String>();
		postParams.put("format", "json");
		postParams.put("fname", patient.getPfn());
		postParams.put("mname", patient.getPmn());
		postParams.put("lname", patient.getPln());
		postParams.put("gender", patient.getPgender());
		postParams.put("dob", patient.getPdob());
		postParams.put("email", patient.getPemail());
		postParams.put("mobile", patient.getPmobnum());
		postParams.put("address", patient.getPadd1());
		postParams.put("address2", patient.getPadd2());
		postParams.put("country", patient.getPcountryid());
		postParams.put("cmbState", patient.getPstateid());
		postParams.put("cmbCity", patient.getPcityid());
		if (!Util.isEmptyString(patient.getPareaid()))
			postParams.put("cmbArea", patient.getPareaid());
		postParams.put("cmbArea_text", patient.getParea());
		postParams.put("height", patient.getHeight());
		postParams.put("haircolor", patient.getHaircolor());
		postParams.put("eyecolor", patient.getEyecolor());
		postParams.put("visiblemark", patient.getVisiblemark());
		postParams.put("zip", patient.getPzip());
		postParams.put("uid", patient.getUid());
		postParams.put("uid_type", patient.getUid_type());
		Log.i("PARAMS:", postParams.toString());
		final Dialog loaddialog = Util.showLoadDialog(activity);
		EzApp.networkController.networkCall(activity, url, postParams,
				new OnResponse() {

					@Override
					public void onResponseListner(String response) {
						Log.i("RESPONSE-URL:", url);
						Log.i("RESPONSE:", response);
						onresponse.onResponseListner("");
						loaddialog.dismiss();
						if (!Util.isEmptyString(response)) {
							try {
								final JSONObject jObj = new JSONObject(response);
								if (jObj.getString("s").equals("200")) {
									Log.i(response, jObj.getString("pid"));
									if (Util.isEmptyString(patient.getPmn()))
										patient.setP_detail(patient.getPfn()
												+ " " + patient.getPln());
									else
										patient.setP_detail(patient.getPfn()
												+ " " + patient.getPmn() + " "
												+ patient.getPln());
									if (!Util.isEmptyString(jObj
											.getString("pid")))
										addPatient(jObj.getString("pid"),
												patient, activity);
									else
										Util.AlertdialogWithFinish(
												activity,
												"Patient '"
														+ patient.getP_detail()
														+ "' created successfully");

								} else {
									Util.Alertdialog(activity,
											"Network Error, please try again later.");
								}
							} catch (final JSONException e) {
								loaddialog.dismiss();
								Util.Alertdialog(activity,
										"Network Error, please try again later.");
								Log.e("", e);
							}
						}

					}
				});
	}

	public static void addPatient(String pid, final Patient pateint,
			Activity activity) {
		pateint.setPid(pid);
		pateint.setFid("0");
		if (Util.isEmptyString(pateint.getPmn()))
			pateint.setP_detail(pateint.getPfn() + " " + pateint.getPln());
		else
			pateint.setP_detail(pateint.getPfn() + " " + pateint.getPmn() + " "
					+ pateint.getPln());
		pateint.setId((long) (Integer.parseInt(pateint.getPid()) + 100000 * Integer
				.parseInt(pateint.getFid())));
		EzApp.patientDao.insertOrReplace(pateint);
		Util.AlertdialogWithFinish(activity,
				"Patient '" + pateint.getP_detail() + "' created successfully");
	}

	public static void getPatient(final String customerId, Context context,
			OnResponsePatient onresponse) {
		Patient patient = new Patient();

		if (EzApp.patientDao.queryBuilder()
				.where(Properties.Pid.eq(customerId)).count() > 0) {
			patient = (EzApp.patientDao.queryBuilder()
					.where(Properties.Pid.eq(customerId)).list().get(0));
			onresponse.onResponseListner(patient);
		} else {
			getNewPatient(customerId, "0", onresponse, (Activity) context);
		}
	}

	public static void updatePatient(Patient pat) {
		EzApp.patientDao.insertOrReplace(pat);
	}

	public static void getPatient(final String customerId, final String fid,
			Context context, OnResponsePatient onresponse) {
		Patient patient = new Patient();

		if (EzApp.patientDao.queryBuilder()
				.where(Properties.Pid.eq(customerId), Properties.Fid.eq(fid))
				.count() > 0) {
			patient = (EzApp.patientDao
					.queryBuilder()
					.where(Properties.Pid.eq(customerId),
							Properties.Fid.eq(fid)).list().get(0));
			try {
				patient.setPdob(EzApp.sdfddMmyy.format(EzApp.sdfMmddyy
						.parse(patient.getPdob())));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			onresponse.onResponseListner(patient);
		} else {
			getNewPatient(customerId, fid, onresponse, (Activity) context);
		}
	}

	public static void deletePatient(Patient patient) {
		EzApp.patientDao.delete(patient);
	}

	public static void copyPatientAddress(final Activity activity,
			final OnResponsePatientCopy onresponse) {
		final String url = APIs.COPY_PATIENT_ADDRESS();
		final HashMap<String, String> postParams = new HashMap<String, String>();
		postParams.put("format", "json");

		Log.i("", postParams.toString());
		final Dialog loaddialog = Util.showLoadDialog(activity);
		EzApp.networkController.networkCall(activity, url, postParams,
				new OnResponse() {

					@Override
					public void onResponseListner(String response) {
						Log.i(url, response);
						loaddialog.dismiss();
						if (!Util.isEmptyString(response)) {
							try {
								final JSONObject jObj = new JSONObject(response);
								if (jObj.getString("s").equals("200")) {
									DefaultAddress pcopy = new Gson().fromJson(
											response, DefaultAddress.class);
									onresponse.onResponseListner(pcopy);
								} else {
									Util.Alertdialog(activity,
											"Network Error, please try again later.");
								}
							} catch (final JSONException e) {
								loaddialog.dismiss();
								Util.Alertdialog(activity,
										"Network Error, please try again later.");
								Log.e("", e);
							}
						}

					}
				});
	}

	public static void patientBarcode(final Context context,
			final OnResponseData responsee, final Patient patient) {

		// Thread.dumpStack();
		String displayID = patient.getDisplay_id();
		// Log.i("patientBarcode", "Patient Info:" + new
		// Gson().toJson(patient));
		// Log.i("patientBarcode", "Patient DID:" + displayID);

		if (displayID == null) {
			Log.i("patientBarcode",
					"Could not get the Bar Code: Display ID is NULL");
			EzUtils.showLong(context, "Could not get the Bar Code");
			return;
		}
		String url = APIs.PATIENT_BARCODE().replaceAll("display_id",
				patient.getDisplay_id());
		final HashMap<String, String> params = new HashMap<String, String>();
		EzApp.networkController.networkCall(context, url, params,
				new OnResponse() {

					@Override
					public void onResponseListner(String response) {
						try {
							JSONObject jobj = new JSONObject(response);

							Log.i("", response);
							String i = jobj.getString("relative_url");
							responsee.onResponseListner(APIs.URL() + i);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
	}
	
	public void referPatient(final String drId, final String reason,
			final Patient pm, final Dialog dialog, final Context context) {
		final String url = APIs.REFER_PATIENT_CREATE();
		final Dialog loaddialog = Util.showLoadDialog(context);
		final StringRequest patientListRequest = new StringRequest(
				Request.Method.POST, url, new Response.Listener<String>() {
					@Override
					public void onResponse(final String response) {
						try {
							Log.i("refer response", response);
							loaddialog.dismiss();
							final JSONObject jObj = new JSONObject(response);
							if (jObj.getString("s").equals("200")) {
								dialog.dismiss();
								Util.Alertdialog(context,
										"Patient referred successfully");

							} else {
								Util.Alertdialog(context,
										"There is some error in referring patient, please try again");
							}
						} catch (final JSONException e) {
							Util.Alertdialog(context,
									"There is some error in referring patient, please try again");
							Log.e("", e);
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(final VolleyError error) {
						Util.Alertdialog(context,
								"There is network error, please try again");

						error.printStackTrace();
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
				loginParams.put("pat-id", pm.getPid());
				loginParams.put("fam-id", pm.getFid());
				loginParams.put("ep-id", "0");
				loginParams.put("doc-id",
						EzApp.sharedPref.getString(Constants.ROLE_ID, ""));// role_id
				loginParams.put("user-id",
						EzApp.sharedPref.getString(Constants.USER_ID, ""));
				loginParams.put("pfn", pm.getPfn());
				loginParams.put("pln", pm.getPln());
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
}
