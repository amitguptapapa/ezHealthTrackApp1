package com.ezhealthtrack.controller;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ezhealthrack.api.APIs;
import com.ezhealthrack.api.NetworkCalls.OnResponse;
import com.ezhealthtrack.EzApp;
import com.ezhealthtrack.activity.DashboardActivity;
import com.ezhealthtrack.controller.EzNetwork.ResponseHandler;
import com.ezhealthtrack.fragments.DashboardFragment;
import com.ezhealthtrack.fragments.InboxFragment;
import com.ezhealthtrack.model.DashboardModel;
import com.ezhealthtrack.model.HospitalModel;
import com.ezhealthtrack.one.EzUtils;
import com.ezhealthtrack.util.Constants;
import com.ezhealthtrack.util.Log;
import com.ezhealthtrack.util.Util;
import com.google.gson.Gson;

public class DataController {

	static String FILENAME_DASHBOARD_RESPONSE = "dashboard_response.text";

	static public void getDashboard(final OnResponse listner) {

		// read from file
		JSONObject data = EzUtils.readFromFile(FILENAME_DASHBOARD_RESPONSE);
		processDashboardResponse(listner, data.toString());

		// refresh data from network
		EzNetwork network = new EzNetwork();
		final String url = APIs.DASHBOARD();

		Map<String, String> params = new HashMap<String, String>();

		network.POST(url, params, new ResponseHandler() {

			@Override
			public void cmdResponseError(Integer code) {
				Log.e("DC::getDashboard()", "Error loading dashboard data");
			}

			@Override
			public void cmdResponse(JSONObject response, String result) {
				Log.v("NC::getDashboard()", "RespURL: " + url);
				Log.v("NC::getDashboard()",
						"RespResponse: " + response.toString());
				boolean resp = processDashboardResponse(listner,
						response.toString());
				if (resp == true) {
					EzUtils.writeToFile(FILENAME_DASHBOARD_RESPONSE, response);
				}
			}
		});

	}

	static boolean processDashboardResponse(final OnResponse listner,
			String response) {
		try {
			final JSONObject jObj = new JSONObject(response);
			InboxFragment.unreadCount = Integer.parseInt(jObj
					.getString("inbox_unread"));
			listner.onResponseListner(APIs.INBOX_MESSAGES());

			final JSONObject data = jObj.getJSONObject("data");
			DashboardFragment.model = new Gson().fromJson(data.toString(),
					DashboardModel.class);

			listner.onResponseListner(APIs.DASHBOARD());
			try {
				if (jObj.get("data") instanceof JSONObject) {
					final JSONObject hospital = jObj.getJSONObject("hospital");

					if (hospital.has("photo")) {
						final HospitalModel model = new Gson().fromJson(
								hospital.toString(), HospitalModel.class);
						DashboardActivity.arrHospital.clear();
						DashboardActivity.arrHospital.add(model);
						listner.onResponseListner(APIs.HOSPITAL());
					}
				}

			} catch (final JSONException e) {
				DashboardActivity.arrHospital.clear();
				Log.e("DC::processDashboardResponse()",
						"Error processing hospital data");
				return false;
			}

		} catch (final JSONException e) {
			EzUtils.showLong("Dashboard data cannot be processed");
			Log.e("DC::processDashboardResponse()",
					"Error processing dashboard data");
			return false;
		}
		return true;
	}

	static public void getDashboard2(final OnResponse listner) {
		// final Dialog loadDialog = Util.showLoadDialog(activity);
		final String url = APIs.DASHBOARD();
		final StringRequest patientListRequest = new StringRequest(
				Request.Method.POST, url, new Response.Listener<String>() {
					@Override
					public void onResponse(final String response) {
						Log.v("NC::getDashboard()", "RespURL: " + url);
						Log.v("NC::getDashboard()", "RespResponse: " + response);
						try {
							final JSONObject jObj = new JSONObject(response);
							InboxFragment.unreadCount = Integer.parseInt(jObj
									.getString("inbox_unread"));
							listner.onResponseListner(APIs.INBOX_MESSAGES());

							final JSONObject data = jObj.getJSONObject("data");
							DashboardFragment.model = new Gson().fromJson(
									data.toString(), DashboardModel.class);

							listner.onResponseListner(APIs.DASHBOARD());
							try {
								if (jObj.get("data") instanceof JSONObject) {
									final JSONObject hospital = jObj
											.getJSONObject("hospital");

									if (hospital.has("photo")) {
										final HospitalModel model = new Gson()
												.fromJson(hospital.toString(),
														HospitalModel.class);
										DashboardActivity.arrHospital.clear();
										DashboardActivity.arrHospital
												.add(model);
										listner.onResponseListner(APIs
												.HOSPITAL());
									}
								}

							} catch (final JSONException e) {
								DashboardActivity.arrHospital.clear();
								Log.e("", e);
							}

						} catch (final JSONException e) {
							// Toast.makeText(
							// DashboardActivity.this,
							// "There is some error while fetching patient list please try again.",
							// Toast.LENGTH_SHORT).show();
							Log.e("", e);
						}
						// loadDialog.dismiss();
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
				return loginParams;
			}

		};
		patientListRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 5,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		EzApp.mVolleyQueue.add(patientListRequest);

	}

}
