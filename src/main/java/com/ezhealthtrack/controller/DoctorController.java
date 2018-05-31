package com.ezhealthtrack.controller;

import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.ezhealthrack.api.APIs;
import com.ezhealthrack.api.NetworkCalls;
import com.ezhealthtrack.EzApp;
import com.ezhealthtrack.DentistSoap.AddDentistNotesActivity;
import com.ezhealthtrack.activity.DashboardActivity;
import com.ezhealthtrack.greendao.Appointment;
import com.ezhealthtrack.greendao.Patient;
import com.ezhealthtrack.model.AssistantModel;
import com.ezhealthtrack.model.LabPreference;
import com.ezhealthtrack.one.EzUtils;
import com.ezhealthtrack.physiciansoap.PhysicianSoapActivityMain;
import com.ezhealthtrack.util.Constants;
import com.ezhealthtrack.util.Log;
import com.ezhealthtrack.util.NetworkCallController.OnResponse;
import com.ezhealthtrack.util.Util;
import com.google.gson.Gson;

public class DoctorController {

	public static void getLabPreference(
			final HashMap<String, String> hashLabPref, Context context) {
		final String url = APIs.LAB_PREFERENCES();
		final HashMap<String, String> params = new HashMap<String, String>();
		params.put("format", "json");
		EzApp.networkController.networkCall(context, url, params,
				new OnResponse() {

					@Override
					public void onResponseListner(String response) {
						// //Log.i(" labs :", response);
						try {
							final JSONObject jObj = new JSONObject(response);
							if (jObj.getString("s").equals("200")) {
								// DashboardActivity.arrLabPreferences.clear();
								// final JSONObject list = jObj
								// .getJSONObject("lab_preferences");
								// Iterator<?> keys = list.keys();
								//
								// while (keys.hasNext()) {
								// String key = (String) keys.next();
								// if (list.get(key) instanceof JSONObject) {
								// LabPreference pref = new Gson()
								// .fromJson(list.getString(key),
								// LabPreference.class);
								// DashboardActivity.arrLabPreferences
								// .add(pref);
								// hashLabPref.put(pref.getName(),
								// list.getString(key));
								// }
								// }
								final JSONObject list = new JSONObject(jObj
										.getString("selected_lab_preferences"));
								DashboardActivity.arrLabSelected.clear();
								Iterator<?> keys1 = list.keys();

								while (keys1.hasNext()) {
									String key1 = (String) keys1.next();
									if (list.get(key1) instanceof JSONObject) {
										LabPreference pref = new Gson()
												.fromJson(list.getString(key1),
														LabPreference.class);
										pref.isChecked = true;
										DashboardActivity.arrLabSelected
												.add(pref);
									}
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
				});
	}

	public static void updateLabPreference(LabPreference pref,
			final Context context) {
		final String url = APIs.UPDATE_LAB_PREF();
		final HashMap<String, String> params = new HashMap<String, String>();
		params.put("cli", "api");
		params.put("format", "json");
		params.put("user_un_id",
				EzApp.sharedPref.getString(Constants.ROLE_ID, ""));
		params.put("test_id", pref.getId());
		params.put("test_name", pref.getName());
		Log.i("", params.toString());
		EzApp.networkController.networkCall(context, url, params,
				new OnResponse() {

					@Override
					public void onResponseListner(String response) {
						Log.i(" labs :", response);
						try {
							final JSONObject jObj = new JSONObject(response);
							if (jObj.getString("s").equals("200")) {
								Toast.makeText(context, "Preferences Updated",
										Toast.LENGTH_SHORT).show();
							} else {
								// Toast.makeText(context,
								// "Preferences could not be updated",
								// Toast.LENGTH_SHORT).show();
							}
						} catch (final JSONException e) {
							// Toast.makeText(context,
							// "Preferences could not be updated",
							// Toast.LENGTH_SHORT).show();
							// Log.e("", e);
						}

					}
				});
	}

	public static void directCheckin(final Patient pat, final Context context) {
		if (Util.isEmptyString(pat.getPid())) {
			pat.setPid("0");
		}
		final String url = APIs.DIRECTCHECKIN()
				.replace("{patid}", pat.getPid())
				.replace("{famid}", pat.getFid());
		final HashMap<String, String> params = new HashMap<String, String>();
		params.put("cli", "api");
		params.put("format", "json");
		Log.i("", url);
		final Dialog dialog = Util.showLoadDialog(context);
		EzApp.networkController.networkCall(context, url, params,
				new OnResponse() {

					@Override
					public void onResponseListner(String response) {
						Log.i(" labs :", response);
						try {
							final JSONObject jObj = new JSONObject(response);
							if (jObj.getString("s").equals("200")) {

								String patientBKID = "";
								try {
									patientBKID = jObj.getString("bk_id");
								} catch (JSONException e) {
									e.printStackTrace();
									EzUtils.showLong(context,
											"Couldn't checkin the Patient. Please try again.");
								}
								final String bkid = patientBKID;

								DashboardActivity.calls.getConfirmedList(1, "",
										new NetworkCalls.OnResponse() {

											@Override
											public void onResponseListner(
													String api) {
												dialog.dismiss();

												int position = -1;
												Intent intent;

												// get position
												for (Appointment apt : DashboardActivity.arrConfirmedPatients) {
													if (apt.getBkid().equals(
															bkid)) {
														position = DashboardActivity.arrConfirmedPatients
																.lastIndexOf(apt);
													}
												}

												if (position < 0) {
													EzUtils.showLong(context,
															"Couldn't find Appointment for the Patient. Please try again.");
													return;
												}

												// get intent
												if (EzApp.sharedPref
														.getString(
																Constants.DR_SPECIALITY,
																"")
														.equalsIgnoreCase(
																"Dentist")) {
													intent = new Intent(
															context,
															AddDentistNotesActivity.class);
												} else {
													intent = new Intent(
															context,
															PhysicianSoapActivityMain.class);
												}

												// start activity
												intent.putExtra("position",
														position);
												context.startActivity(intent);
											}
										}, null, null, null);

							} else if (jObj.getString("s").equals("403")) {
								dialog.dismiss();
								Toast.makeText(
										context,
										"Slot already booked for same patient.",
										Toast.LENGTH_SHORT).show();
							}
						} catch (final JSONException e) {
							dialog.dismiss();
							Toast.makeText(
									context,
									"There is some error while checkin please try again.",
									Toast.LENGTH_SHORT).show();
							Log.e("", e);
						}

					}
				});
	}

	public static void directCheckinConfirmed(final Appointment pat,
			final Context context) {
		if (Util.isEmptyString(pat.getPid())) {
			pat.setPid("0");
		}
		final String url = APIs.DIRECTCHECKIN()
				.replace("{patid}", pat.getPid())
				.replace("{famid}", pat.getPfId());
		final HashMap<String, String> params = new HashMap<String, String>();
		params.put("cli", "api");
		params.put("format", "json");
		Log.i("", url);
		final Dialog dialog = Util.showLoadDialog(context);
		EzApp.networkController.networkCall(context, url, params,
				new OnResponse() {

					@Override
					public void onResponseListner(String response) {
						Log.i(" labs :", response);
						try {
							final JSONObject jObj = new JSONObject(response);
							if (jObj.getString("s").equals("200")) {
								DashboardActivity.calls.getConfirmedList(1, "",
										new NetworkCalls.OnResponse() {

											@Override
											public void onResponseListner(
													String api) {
												dialog.dismiss();
												Intent intent;
												if (EzApp.sharedPref
														.getString(
																Constants.DR_SPECIALITY,
																"")
														.equalsIgnoreCase(
																"Dentist")) {
													intent = new Intent(
															context,
															AddDentistNotesActivity.class);
												} else {
													intent = new Intent(
															context,
															PhysicianSoapActivityMain.class);
												}

												for (Appointment apt : DashboardActivity.arrConfirmedPatients)
													try {
														if (apt.getBkid()
																.equals(jObj
																		.getString("bk_id")))
															intent.putExtra(
																	"position",
																	DashboardActivity.arrConfirmedPatients
																			.lastIndexOf(apt));
													} catch (JSONException e) {
														// TODO Auto-generated
														// catch block
														e.printStackTrace();
													}
												context.startActivity(intent);
											}
										}, null, null, null);

							} else {
								dialog.dismiss();
								Toast.makeText(
										context,
										"There is some error while checkin please try again.",
										Toast.LENGTH_SHORT).show();
								Toast.makeText(context, jObj.optString("s"),
										Toast.LENGTH_SHORT).show();
							}
						} catch (final JSONException e) {
							dialog.dismiss();
							Toast.makeText(
									context,
									"There is some error while checkin please try again.",
									Toast.LENGTH_SHORT).show();
							Log.e("", e);
						}

					}
				});
	}

	public static void getCurrentAvlAssistants(Context context,
			final OnResponse onresponse) {
		final String url = APIs.CURRENT_ASSISTANTS();
		final HashMap<String, String> params = new HashMap<String, String>();
		params.put("format", "json");
		params.put("cli", "api");
		EzApp.networkController.networkCall(context, url, params,
				new OnResponse() {

					@Override
					public void onResponseListner(String response) {
						Log.i(" labs :", response);
						try {
							final JSONObject jObj = new JSONObject(response);
							if (jObj.getString("s").equals("200")) {
								DashboardActivity.arrCurrentAssistants.clear();
								if (jObj.get("data") instanceof JSONObject) {
									final JSONObject list = jObj
											.getJSONObject("data");
									Iterator<?> keys = list.keys();

									while (keys.hasNext()) {
										String key = (String) keys.next();
										AssistantModel asst = new AssistantModel();
										asst.setAssist_name(list.getString(key));
										asst.setAsst_id(key);
										DashboardActivity.arrCurrentAssistants
												.add(asst);
									}
								}
								onresponse.onResponseListner("success");

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
				});
	}
}
