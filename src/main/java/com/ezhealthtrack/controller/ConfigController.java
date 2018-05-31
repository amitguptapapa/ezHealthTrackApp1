package com.ezhealthtrack.controller;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.ezhealthrack.api.APIs;
import com.ezhealthtrack.EzApp;
import com.ezhealthtrack.one.EzUtils;
import com.ezhealthtrack.util.Log;
import com.ezhealthtrack.util.NetworkCallController.OnResponse;
import com.ezhealthtrack.util.Util;

public class ConfigController {

	final static String FILE_CONFIG = "biz_config.txt";

	final static public String PRESCRIPTION_REQUIRED = "prescription_field_required";
	final static public String COPY_BRANCH_ADRESS = "branch_address_as_patient_address";
	final static public String CHECKIN_REQUIRED = "checkin_required";
	final static public String PRESCRIPTION_ALERT = "prescription_alert_required";

	static JSONObject mConfig;

	static public void getConfiguration(final Context context) {
		if (mConfig == null) {
			mConfig = EzUtils.readFromFile(FILE_CONFIG);
		}
		final String url = APIs.CONFIGURATIONS();
		final HashMap<String, String> params = new HashMap<String, String>();
		params.put("format", "json");
		params.put("cli", "api");
		EzApp.networkController.networkCall(context, url, params,
				new OnResponse() {

					@Override
					public void onResponseListner(String response) {
						try {
							final JSONObject jObj = new JSONObject(response);
							if (jObj.getString("s").equals("200")) {
								// save latest configuration in memory and file
								mConfig = jObj;
								EzUtils.writeToFile(FILE_CONFIG, jObj);
							}
						} catch (final JSONException e) {
							Util.Alertdialog(context, e.toString());
						}
					}
				});
	}

	static public String getConfig(String key) {
		String value = "";
		try {
			value = mConfig.getJSONObject("configuration").optString(key, "");
		} catch (JSONException e) {
			// do nothing
		}
		Log.i("Config::getConfig()", key + "=" + value);
		return value;
	}
}
