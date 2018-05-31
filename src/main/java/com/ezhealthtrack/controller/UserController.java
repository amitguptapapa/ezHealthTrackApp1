package com.ezhealthtrack.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;

import com.ezhealthrack.api.APIs;
import com.ezhealthtrack.EzApp;
import com.ezhealthtrack.db.DatabaseHelper;
import com.ezhealthtrack.greendao.Allergy;
import com.ezhealthtrack.greendao.Icd10Item;
import com.ezhealthtrack.model.Permission;
import com.ezhealthtrack.model.State;
import com.ezhealthtrack.model.ToStep;
import com.ezhealthtrack.util.Log;
import com.ezhealthtrack.util.NetworkCallController.OnResponse;
import com.ezhealthtrack.util.Util;
import com.google.gson.Gson;

public class UserController {

	static int mWorkFlowRetryCount = 0;

	private void retryGetWorkFlow(final Context context) {
		if (mWorkFlowRetryCount > 2) {
			Util.Alertdialog(context, "Error while fetching workflow");
			Log.v("UC:retry()", "Error while fetching workflow");
			return;
		}
		mWorkFlowRetryCount++;
		this.getWorkFlow(context);
	}

	public void getWorkFlow(final Context context) {
		final String url = APIs.WORKFLOW();
		final HashMap<String, String> params = new HashMap<String, String>();
		params.put("format", "json");
		params.put("w-type", "appointment");
		EzApp.networkController.networkCall(context, url, params,
				new OnResponse() {

					@Override
					public void onResponseListner(String response) {
						try {
							final JSONObject jObj = new JSONObject(response);
							if (!jObj.getString("s").equals("200")) {
								retryGetWorkFlow(context);
								return;
							}
							DatabaseHelper.db.clearAllTables();
							final JSONArray data = jObj.getJSONArray("data");
							for (int i = 0; i < data.length(); i++) {
								final JSONObject jsonState = data
										.getJSONObject(i);
								final State state = new State();
								state.setId(i);
								state.setName(jsonState.getString("name"));
								DatabaseHelper.db.createState(state);
								if (jsonState.has("permission")) {
									final JSONArray jperm = jsonState
											.getJSONArray("permission");
									for (int j = 0; j < jperm.length(); j++) {
										final Permission perm = new Permission();
										perm.setId(i);
										perm.setName((String) jperm.get(j));
										DatabaseHelper.db
												.createPermission(perm);
									}
								}
								if (jsonState.has("transitions")) {
									final JSONArray jsonToStep = jsonState
											.getJSONArray("transitions");
									for (int j = 0; j < jsonToStep.length(); j++) {
										final ToStep toStep = new ToStep();
										toStep.setId(i);
										toStep.setName(jsonToStep
												.getJSONObject(j).getString(
														"name"));
										DatabaseHelper.db.createToStep(toStep);
									}
								}
							}
							logWorkflowData();
						} catch (final JSONException e) {
							Log.e("UC::getWorkFlow()", "" + e.toString());
							// Util.Alertdialog(context, e.toString());
							retryGetWorkFlow(context);
						}
					}
				});
	}

	public void icd10(final Context activity) {
		new AsyncTask<String, String, String>() {

			@Override
			protected String doInBackground(final String... params) {
				Log.i("", "icd10 is called");
				InputStream fis;
				try {
					fis = activity.getAssets().open("IcdJson.txt");
					final StringBuffer fileContent = new StringBuffer("");

					final byte[] buffer = new byte[1024];

					while (fis.read(buffer) != -1) {
						fileContent.append(new String(buffer));
					}
					final String s = String.valueOf(fileContent);
					Log.i("", s);
					try {
						final JSONArray jsonArray = new JSONArray(s);
						for (int i = 0; i < jsonArray.length(); i++) {
							final JSONObject json = jsonArray.getJSONObject(i);
							final Icd10Item icd = new Icd10Item();
							icd.setItem_id(json.getString("id"));
							icd.setChapter_name(json.getString("chapter_name"));
							icd.setChapter_desc(json.getString("chapter_desc"));
							icd.setSection_id(json.getString("section_id"));
							icd.setSection_desc(json.getString("section_desc"));
							icd.setDiag_parent_name(json
									.getString("diag_parent_name"));
							icd.setDiag_parent_desc(json
									.getString("diag_parent_desc"));
							icd.setDiag_name(json.getString("diag_name"));
							icd.setDiag_desc(json.getString("diag_desc"));
							icd.setStatus(json.getString("status"));
							EzApp.icdDao.insert(icd);
						}
					} catch (final JSONException e) {
						e.printStackTrace();
					}
				} catch (final IOException e) {
					e.printStackTrace();
				}
				return null;
			}

			@Override
			protected void onPostExecute(final String result) {
				super.onPostExecute(result);
			}
		}.execute("");
	}

	public void allergy(final Context activity) {
		new AsyncTask<String, String, String>() {

			@Override
			protected String doInBackground(final String... params) {
				try {
					final JSONArray jsonAllergy = new JSONArray(
							Util.readJsonFromAssets("allergy.json", activity));
					for (int i = 0; i < jsonAllergy.length(); i++) {
						final JSONObject json = jsonAllergy.getJSONObject(i);
						Allergy allergy = new Gson().fromJson(json.toString(),
								Allergy.class);
						EzApp.allergyDao.insert(allergy);
					}
				} catch (final JSONException e) {
					e.printStackTrace();
				}
				return null;
			}

			@Override
			protected void onPostExecute(final String result) {
				super.onPostExecute(result);
			}
		}.execute("");
	}

	private void logWorkflowData() {
		for (final State s : DatabaseHelper.db.getAllState()) {
			Log.i("State: " + s.getId(), s.getName());
		}
		for (final ToStep s : DatabaseHelper.db.getAllToStep()) {
			Log.i("ToStep: " + s.getId(), s.getName());
		}
		for (final Permission p : DatabaseHelper.db.getPermissions()) {
			Log.i("Permissions: " + p.getId(), p.getName());
		}

	}
}
