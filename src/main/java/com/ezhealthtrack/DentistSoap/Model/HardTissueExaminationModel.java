package com.ezhealthtrack.DentistSoap.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ezhealthtrack.EzApp;
import com.ezhealthtrack.DentistSoap.AddDentistNotesActivity;
import com.ezhealthtrack.greendao.Appointment;
import com.ezhealthtrack.util.Constants;
import com.ezhealthtrack.util.Log;
import com.ezhealthtrack.util.Util;

public class HardTissueExaminationModel {
	public ArrayList<TeethModel> arrTeeth = new ArrayList<TeethModel>();
	public String[] arrayKey = new String[] { "status", "othe", "miss", "mobi",
			"cari", "gc", "rest", "frac", "rs", "rct", "pp", "stai", "calc",
			"ep" };
	public String[] arrayValue = new String[] { "", "", "Missing", "Mobile",
			"Carious", "Grossly Carious", "Restoration", "Fracture",
			"Root Stump", "Root Canal Treatment", "Periodontal Pocket",
			"Stains", "Calculus", "Existing Prosthesis" };
	private final HashMap<String, String> keyValue = new HashMap<String, String>();
	private final HashMap<String, String> valueKey = new HashMap<String, String>();
	public final HashMap<String, String> hashNote = new HashMap<String, String>();
	public String tp = "";

	HardTissueExaminationModel() {
		for (int i = 0; i < arrayKey.length; i++) {
			keyValue.put(arrayKey[i], arrayValue[i]);
			valueKey.put(arrayValue[i], arrayKey[i]);
		}
	}

	public void getMergeData(final Appointment Appointment,
			final Context context, final Dialog dialog) {

		final String url = "http://dev.ezhealthtrack.com/SOAPInstance/mergeExamination/cli/api";
		// Log.i("", url);
		final StringRequest patientListRequest = new StringRequest(
				Request.Method.POST, url, new Response.Listener<String>() {
					@Override
					public void onResponse(final String response) {
						// Log.i("", response);
						try {
							final JSONObject jObj = new JSONObject(response);
							if (jObj.getString("s").equals("200")
									&& jObj.get("hte") instanceof JSONObject) {
								final JSONObject hte = jObj
										.getJSONObject("hte");
								Iterator<String> iter;
								try {
									iter = hte.keys();
									while (iter.hasNext()) {
										TeethModel teeth = new TeethModel();
										final String key = iter.next();
										for (final TeethModel teeth1 : arrTeeth) {
											if (teeth1.name
													.equalsIgnoreCase("tooth"
															+ key)) {
												teeth = teeth1;
											}
										}
										if (hte.get(key) instanceof JSONObject) {
											final Iterator<String> iter1 = hte
													.getJSONObject(key).keys();
											while (iter1.hasNext()) {
												final String key1 = iter1
														.next();
												String value1;
												if (key1.equals("othe")) {
													value1 = hte
															.getJSONObject(key)
															.getJSONObject(key1)
															.getString("value")
															.toString();
												} else {
													value1 = hte.getJSONObject(
															key)
															.getString(key1);
												}
												// Log.i(key, key1 + " " +
												// value1);
												teeth.hashState.put(key1,
														value1);
												if (value1.equals("on")) {
													if (!teeth.arrTeethState
															.contains(keyValue
																	.get(key1))) {
														teeth.arrTeethState.add(keyValue
																.get(key1));
													}
													// Log.i(teeth.name,
													// teeth.arrTeethState
													// .toString());
												}
											}

										}
									}
									((AddDentistNotesActivity) context)
											.refreshToothDialog(dialog);
								} catch (final JSONException e1) {
									e1.printStackTrace();
								}

							} else {
								Toast.makeText(
										context,
										"There is some error while merging please try again.",
										Toast.LENGTH_SHORT).show();
							}
						} catch (final JSONException e) {
							Toast.makeText(
									context,
									"There is some error while merging please try again.",
									Toast.LENGTH_SHORT).show();
							Log.e("", e);
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(final VolleyError error) {
						Toast.makeText(
								context,
								"There is some error while fetching merge list please try again",
								Toast.LENGTH_SHORT).show();

						Log.e("Error.Response", error);
					}
				}) {
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				final HashMap<String, String> loginParams = new HashMap<String, String>();
				loginParams.put(
						"auth-token",
						Util.getBase64String(context
								.getApplicationContext()
								.getSharedPreferences(Constants.EZ_SHARED_PREF,
										Context.MODE_PRIVATE)
								.getString(Constants.USER_TOKEN, "")));
				return loginParams;
			}

			@Override
			protected Map<String, String> getParams() {
				final HashMap<String, String> loginParams = new HashMap<String, String>();
				loginParams.put("format", "json");
				loginParams.put("bk-id", Appointment.getBkid());
				return loginParams;
			}

		};
		patientListRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 5,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		EzApp.mVolleyQueue.add(patientListRequest);

	}

	public void jsonParseHte(final JSONObject hte) {
		if (arrTeeth.size() == 0) {
			for (int i = 1; i < 5; i++) {
				for (int j = 1; j < 9; j++) {
					final TeethModel teeth = new TeethModel();
					teeth.name = "tooth" + i + "" + j;
					arrTeeth.add(teeth);
				}
			}
			for (int i = 5; i < 9; i++) {
				for (int j = 1; j < 6; j++) {
					final TeethModel teeth = new TeethModel();
					teeth.name = "tooth" + i + "" + j;
					arrTeeth.add(teeth);
				}
			}
		}
		Iterator<String> iter;
		try {
			if (hte.has("tooth") && hte.get("tooth") instanceof JSONObject) {
				iter = hte.getJSONObject("tooth").keys();
				while (iter.hasNext()) {
					final String key = iter.next();
					for (final TeethModel teeth1 : arrTeeth) {
						if (teeth1.name.equalsIgnoreCase("tooth" + key)) {
							teeth1.arrTeethState.clear();
							teeth1.arrTeethTreatmentPlan.clear();
							if (hte.getJSONObject("tooth").get(key) instanceof JSONObject) {
								final Iterator<String> iter1 = hte
										.getJSONObject("tooth")
										.getJSONObject(key).keys();
								while (iter1.hasNext()) {
									final String key1 = iter1.next();
									String value1 = "";
									if (key1.equals("othe")) {
										if (hte.getJSONObject("tooth")
												.getJSONObject(key)
												.getJSONObject(key1)
												.has("value"))
											value1 = hte.getJSONObject("tooth")
													.getJSONObject(key)
													.getJSONObject(key1)
													.getString("value")
													.toString();
										if(!Util.isEmptyString(value1))
										teeth1.arrTeethState.add(value1);
									} else {
										value1 = hte.getJSONObject("tooth")
												.getJSONObject(key)
												.getString(key1);
									}
									// Log.i(key, key1 + " " + value1);
									teeth1.hashState.put(key1, value1);
									if (value1.equals("on")&&!key1.equals("status")) {
										teeth1.arrTeethState.add(keyValue
												.get(key1));
										// Log.i(teeth1.name,
										// teeth1.arrTeethState.toString());
									}
								}
							}
						}
					}
				}
			}
			if (hte.has(tp))
			if (hte.get("tp") instanceof JSONObject
					&& hte.getJSONObject("tp").has("value"))
				if (hte.getJSONObject("tp").getString("value")
						.contains("permanent")) {
					tp = "Permanent";
				} else {
					tp = "Deciduous";
				}
			//Log.i(tp, "tp=" + hte.getJSONObject("tp").getString("value"));
			if (hte.has("note") && hte.get("note") instanceof JSONObject
					&& hte.getJSONObject("note").has("value"))
				hashNote.put("note",
						hte.getJSONObject("note").getString("value"));
		} catch (final JSONException e1) {
			e1.printStackTrace();
		}
	}

	public void updateJsonHte(final JSONObject oe) {
		try {
			final JSONObject hte = new JSONObject();
			final JSONObject tooth = new JSONObject();
			for (final TeethModel teeth : arrTeeth) {
				for (final String key : arrayKey) {
					if (!key.equals("othe")) {
						teeth.hashState.put(key, "off");
					} else {
					}
				}
				for (final String s : teeth.arrTeethState) {
					if (!Util.isEmptyString(s)&&valueKey.containsKey(s)) {
						teeth.hashState.put(valueKey.get(s), "on");
					}
				}
				if (teeth.arrTeethState.size() > 0) {
					teeth.hashState.put("status", "on");
				}
				final JSONObject tooth1 = new JSONObject();
				for (final Entry<String, String> entry : teeth.hashState
						.entrySet()) {
					if (entry.getKey().equals("othe")) {
						final JSONObject obj = new JSONObject();
						obj.put("value", entry.getValue());
						tooth1.put("othe", obj);
					} else {
						tooth1.put(entry.getKey(), entry.getValue());
					}
				}
				tooth.put(teeth.name.replace("tooth", ""), tooth1);
				Log.i(teeth.name, tooth.toString());
				
			}
			// Log.i(tp, tooth.toString());
			hte.put("tooth", tooth);
			final JSONObject obj = new JSONObject();
			obj.put("value", tp);
			hte.put("tp", obj);
			final JSONObject note = new JSONObject();
			note.put("value", hashNote.get("note"));
			hte.put("note", note);
			oe.put("hte", hte);

		} catch (final Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
