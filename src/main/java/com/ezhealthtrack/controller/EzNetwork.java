package com.ezhealthtrack.controller;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ezhealthrack.api.APIs;
import com.ezhealthtrack.EzApp;
import com.ezhealthtrack.one.EzUtils;
import com.ezhealthtrack.util.Constants;
import com.ezhealthtrack.util.Util;

public class EzNetwork {

	final static public String RAW_RESPONSE_JSONARRAY = "raw_jsonarray";

	protected int mCallsInProgress; // for non-images

	// network response handler
	public interface ResponseHandler {

		void cmdResponse(JSONObject response, String result);

		void cmdResponseError(Integer code);
	}

	public boolean callInProgress() {
		if (mCallsInProgress > 0)
			return true;
		return false;
	}

	public void POST(final String url, final Map<String, String> params,
			final ResponseHandler listner) {

		// TODO: check network availability
		mCallsInProgress++;

		final String authToken = Util
				.getBase64String(EzApp.sharedPref.getString(
						Constants.USER_TOKEN, ""));

		Log.i("EzNetwork", "URL (req) : " + url);
		final StringRequest request = new StringRequest(Request.Method.POST,
				url, new Response.Listener<String>() {

					@Override
					public void onResponse(final String response) {
						Log.i("EzNetwork", "URL (res) : " + url);
						Log.i("EzNetwork", "Response : " + response);

						if (mCallsInProgress > 0)
							mCallsInProgress--;

						try {
							if (params.get(RAW_RESPONSE_JSONARRAY) == "1") {
								JSONArray jObj = new JSONArray(response);
								JSONObject data = new JSONObject();
								data.put("data", jObj);
								listner.cmdResponse(data,
										RAW_RESPONSE_JSONARRAY);
								return;
							}

							final JSONObject jObj = new JSONObject(response);
							if (url.equals(APIs.POST_GALLERY_PHOTO())) {
								listner.cmdResponse(jObj, "200");
								return;
							}
							String result = jObj.getString("s");
							if (result != null && result.equals("200")) {
								listner.cmdResponse(jObj, "200");
							} else {
								EzUtils.showLong("Bad Response : " + result);
								listner.cmdResponseError(501);
							}
						} catch (final JSONException e) {
							EzUtils.showLong("Bad Response, Error: "
									+ e.getMessage());
							Log.e("EzNetowrk", "" + e.getMessage());
							Log.e("EzNetowrk", "" + response.toString());
							listner.cmdResponseError(502);
						}
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(final VolleyError e) {
						if (mCallsInProgress > 0)
							mCallsInProgress--;
						EzUtils.showLong("Bad Response, Error: "
								+ e.getMessage());
						Log.e("EzNetwork", "NError: " + e.getMessage());
						listner.cmdResponseError(500);
					}
				}) {
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				final HashMap<String, String> headers = new HashMap<String, String>();
				headers.put("auth-token", authToken);
				Log.i("EzNetwork", "Headers : " + headers.toString());
				return headers;
			}

			@Override
			protected Map<String, String> getParams() {

				params.put("cli", "api");
				params.put("format", "json");

				if (params.get("limit") == null) {
					params.put("limit", "10");
				}

				Log.i("EzNetwork", "Params : " + params.toString());
				return params;
			}
		};
		request.setRetryPolicy(new DefaultRetryPolicy(20000, 1,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		EzApp.mVolleyQueue.add(request);
	}
}
