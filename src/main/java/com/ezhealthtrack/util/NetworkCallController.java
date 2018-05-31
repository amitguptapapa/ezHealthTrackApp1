package com.ezhealthtrack.util;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ezhealthtrack.EzApp;

public class NetworkCallController {
	public static final String NOCONNECTION = "no connection";

	public interface OnResponse {
		public void onResponseListner(String response);
	}

	public void networkCall(final Context context, final String url,
			final Map<String, String> params, final OnResponse onresponse) {
		Log.v("NCC::networkCall()", "ReqURL: " + url);
		Log.v("NCC::networkCall()", "ReqParams: " + params.toString());
		final StringRequest patientListRequest = new StringRequest(
				Request.Method.POST, url, new Response.Listener<String>() {
					@Override
					public void onResponse(final String response) {
						Log.v("NCC::networkCall()", "RespURL: " + url);
						Log.v("NCC::networkCall()", "RespResponse: " + response);
						onresponse.onResponseListner(response);
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(final VolleyError error) {
						// onresponse.onResponseListner("");
						Log.i("oER", url);
						Log.e("Error.Response: ", error);
						if ((context != null)
								&& !(((Activity) context).isFinishing())) {
							Util.Alertdialog(context,
									"Network error, try again later");
						}
						onresponse.onResponseListner("error");
					}
				}) {
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				final HashMap<String, String> headers = new HashMap<String, String>();
				String token = EzApp.sharedPref.getString(
						Constants.USER_TOKEN, "");
				if (!Util.isEmptyString(token)) {
					headers.put("auth-token", Util.getBase64String(token));
					Log.i("NCC", headers.toString());
				}
				return headers;
			}

			@Override
			protected Map<String, String> getParams() {
				return params;
			}

		};
		patientListRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 5,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		ConnectionDetector cd = new ConnectionDetector(context);
		if (cd.isConnectingToInternet())
			EzApp.mVolleyQueue.add(patientListRequest);
		else {
			// Util.Alertdialog(context,
			// "Please check your internet Connection.");
			onresponse.onResponseListner(NOCONNECTION);
			Log.i(NOCONNECTION, NOCONNECTION);
		}

	}

}
