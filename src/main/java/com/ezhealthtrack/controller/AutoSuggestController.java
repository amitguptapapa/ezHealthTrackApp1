package com.ezhealthtrack.controller;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.Context;
import android.util.Log;

import com.ezhealthrack.api.APIs;
import com.ezhealthtrack.EzApp;
import com.ezhealthtrack.labs.model.LabTestAutoSuggest;
import com.ezhealthtrack.model.DoctorAutosuggest;
import com.ezhealthtrack.model.PatientAutoSuggest;
import com.ezhealthtrack.util.NetworkCallController.OnResponse;
import com.google.gson.Gson;

public class AutoSuggestController {
	public interface OnAutoSuggest {
		public void OnAutoSuggestListner(Object list);
	}

	public static void autoSuggestDoctor(String s, Context context,
			final OnAutoSuggest autosuggest) {
		String url = APIs.DOCTOR_AUTOSUGGEST();
		final HashMap<String, String> params = new HashMap<String, String>();
		params.put("cli", "api");
		params.put("name", s);
		EzApp.networkController.networkCall(context, url, params,
				new OnResponse() {

					@Override
					public void onResponseListner(String response) {
						JSONArray jArr;
						try {
							Log.i("", response);
							jArr = new JSONArray(response);
							ArrayList<String> arrDoc = new ArrayList<String>();
							for (int i = 0; i < jArr.length(); i++) {
								DoctorAutosuggest doc = new Gson().fromJson(
										jArr.getJSONObject(i).toString(),
										DoctorAutosuggest.class);
								arrDoc.add(doc.getName() + ", "
										+ doc.getAddress());
							}
							autosuggest.OnAutoSuggestListner(arrDoc);

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				});
	}

	public static void autoSuggestLabTest(String s, Context context,
			final OnAutoSuggest autosuggest) {
		String url = APIs.LABTEST_AUTOSUGGEST();
		final HashMap<String, String> params = new HashMap<String, String>();
		params.put("cli", "api");
		params.put("term", s);
		EzApp.networkController.networkCall(context, url, params,
				new OnResponse() {

					@Override
					public void onResponseListner(String response) {
						JSONArray jArr;
						try {
							jArr = new JSONArray(response);
							ArrayList<LabTestAutoSuggest> arrTest = new ArrayList<LabTestAutoSuggest>();
							for (int i = 0; i < jArr.length(); i++) {
								jArr.getJSONObject(i).put(
										"samples",
										jArr.getJSONObject(i)
												.getJSONObject("samples")
												.toString());
								LabTestAutoSuggest test = new Gson().fromJson(
										jArr.getJSONObject(i).toString(),
										LabTestAutoSuggest.class);
								arrTest.add(test);
							}
							autosuggest.OnAutoSuggestListner(arrTest);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				});
	}

	public static void autoSuggestLabPanel(String s, Context context,
			final OnAutoSuggest autosuggest) {
		String url = APIs.LABPANEL_AUTOSUGGEST() + "?tenant=L&term=" + s;
		final HashMap<String, String> params = new HashMap<String, String>();
		// params.put("cli", "api");
		EzApp.networkController.networkCall(context, url, params,
				new OnResponse() {

					@Override
					public void onResponseListner(String response) {
						JSONArray jArr;
						try {
							Log.i("", response);
							jArr = new JSONArray(response);
							ArrayList<LabTestAutoSuggest> arrTest = new ArrayList<LabTestAutoSuggest>();
							for (int i = 0; i < jArr.length(); i++) {
								LabTestAutoSuggest test = new Gson().fromJson(
										jArr.getJSONObject(i).toString(),
										LabTestAutoSuggest.class);
								arrTest.add(test);
							}
							autosuggest.OnAutoSuggestListner(arrTest);

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				});
	}

	public static void autoSuggestPatient(String s, Context context,
			final OnAutoSuggest autosuggest) {
		String url = APIs.PATIENT_AUTOSUGGEST() + s;
		Log.i("", url);
		final HashMap<String, String> params = new HashMap<String, String>();
		params.put("cli", "api");
		EzApp.networkController.networkCall(context, url, params,
				new OnResponse() {

					@Override
					public void onResponseListner(String response) {
						JSONArray jArr;

						try {
							Log.i("", response);
							jArr = new JSONArray(response);
							ArrayList<PatientAutoSuggest> arrPat = new ArrayList<PatientAutoSuggest>();
							for (int i = 0; i < jArr.length(); i++) {
								PatientAutoSuggest pat = new Gson().fromJson(
										jArr.getJSONObject(i).toString(),
										PatientAutoSuggest.class);
								arrPat.add(pat);
							}
							autosuggest.OnAutoSuggestListner(arrPat);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				});
	}

}
