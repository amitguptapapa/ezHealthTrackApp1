package com.ezhealthtrack.controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.ezhealthrack.api.APIs;
import com.ezhealthtrack.EzApp;
import com.ezhealthtrack.R;
import com.ezhealthtrack.DentistSoap.Model.DiagnosisModel;
import com.ezhealthtrack.DentistSoap.Model.PastVisitModel;
import com.ezhealthtrack.DentistSoap.Model.SubjectiveModel;
import com.ezhealthtrack.greendao.Appointment;
import com.ezhealthtrack.greendao.Patient;
import com.ezhealthtrack.model.AllergiesModel;
import com.ezhealthtrack.model.Document;
import com.ezhealthtrack.model.MedicineModel;
import com.ezhealthtrack.model.PatInfo;
import com.ezhealthtrack.model.ReferFromModel;
import com.ezhealthtrack.model.ReferToModel;
import com.ezhealthtrack.model.gallery.SOAPGallery;
import com.ezhealthtrack.model.gallery.SOAPPhoto;
import com.ezhealthtrack.model.laborder.LabOrderDetails;
import com.ezhealthtrack.model.laborder.SOAPLabs;
import com.ezhealthtrack.physiciansoap.PhysicianSoapActivityMain;
import com.ezhealthtrack.physiciansoap.model.ExaminationModel;
import com.ezhealthtrack.physiciansoap.model.PlanModel;
import com.ezhealthtrack.physiciansoap.model.PrivateNotesModel;
import com.ezhealthtrack.physiciansoap.model.VisitNotesModel;
import com.ezhealthtrack.util.Constants;
import com.ezhealthtrack.util.Log;
import com.ezhealthtrack.util.Util;
import com.ezhealthtrack.views.TextUtils;
import com.google.gson.Gson;

// This class is NOT IN USE yet
public class SoapNotesController {

	public interface SoapNotesResponseHandler {
		public void onGetDataFinished(boolean dataLoaded);
	}

	static private Appointment mAppointment = PhysicianSoapActivityMain.Appointment;
	static public Patient mPatient;
	static public VisitNotesModel visitNotes;
	static public ArrayList<Document> arrDocuments = new ArrayList<Document>();
	static public ArrayList<MedicineModel> arrMed = new ArrayList<MedicineModel>();
	static public final ArrayList<PastVisitModel> arrPastVisit = new ArrayList<PastVisitModel>();
	static public ArrayList<AllergiesModel> arrallergies = new ArrayList<AllergiesModel>();

	static public SOAPLabs mSOAPLabs;
	static public SOAPGallery mSOAPGallery;

	private final ArrayList<String> arrIcd = new ArrayList<String>();
	private final HashMap<String, String> hashFollowUp = new HashMap<String, String>();
	private final ArrayList<ReferToModel> arrReferTo = new ArrayList<ReferToModel>();
	private final ArrayList<ReferFromModel> arrReferFrom = new ArrayList<ReferFromModel>();

	private PatInfo mPatientInfo;
	private LabOrderDetails mOrderDetails;
	private ArrayList<AllergiesModel> mArrallergies = new ArrayList<AllergiesModel>();

	static public void reset() {
		mSOAPGallery = null;
	}

	static public String getGalleryId() {
		return (mSOAPGallery == null) ? "" : mSOAPGallery.getGalleryId();
	}

	// update photo in SOAP Gallery
	static public void setSOAPGalleryId(String id) {
		if (mSOAPGallery == null)
			return;
		mSOAPGallery.setGalleryId(id);
	}

	// update photo in SOAP Gallery
	static public void updateSOAPGalleryPhoto(SOAPPhoto photo) {
		if (mSOAPGallery == null)
			return;
		mSOAPGallery.getPhotos().clear();
		mSOAPGallery.getPhotos().add(photo);
	}

	// update photo in SOAP Gallery
	static public void updateSOAPGalleryPhotos(List<SOAPPhoto> photos) {
		if (mSOAPGallery == null)
			return;
		mSOAPGallery.getPhotos().clear();
		if (photos == null || photos.size() < 1)
			return;
		mSOAPGallery.getPhotos().add(photos.get(0));
	}

	static public void setAppointment(Appointment appointment) {
		mAppointment = appointment;
	}

	static public Appointment getAppointment() {
		return mAppointment;
	}

	boolean getDataFromResponse(JSONObject data) {
		try {
			// Get Labs & Orders
			mSOAPLabs = new Gson().fromJson(data.toString(), SOAPLabs.class);
			mSOAPGallery = new Gson().fromJson(data.toString(),
					SOAPGallery.class);

			Log.i("PSA:showSOAPGallery()",
					"Gallery: " + new Gson().toJson(mSOAPGallery));

			if (data.has("mp_ep_id")) {
				mAppointment.setEpid(data.getString("mp_ep_id"));
			}

			mArrallergies.clear();
			if (data.has("allergies")) {
				JSONArray arr = data.getJSONArray("allergies");
				for (int i = 0; i < arr.length(); i++) {
					AllergiesModel allergy = new Gson().fromJson(arr.get(i)
							.toString(), AllergiesModel.class);
					mArrallergies.add(allergy);
				}
			}

			if (data.has("documents")) {
				JSONArray arr = data.getJSONArray("documents");
				for (int i = 0; i < arr.length(); i++) {
					Document doc = new Gson().fromJson(arr.get(i).toString(),
							Document.class);
					arrDocuments.add(doc);
				}
				// radiologyTable();
				// labTable();
				// ekgTable();
			}

			if (data.has("p-info")) {
				String s = data.getString("p-info");
				mPatientInfo = new Gson().fromJson(s, PatInfo.class);
				mAppointment.setFollowid(mPatientInfo.getFollowupId());
			}

			getPastVisits();
			if (!mAppointment.getVisit().equals("1")) {
				// pastFollowUp();
			}
			// getReferPatient();
			return true;
		} catch (final JSONException e) {
			Log.e("", e);
			return false;
		}
	}

	public boolean getData(final SoapNotesResponseHandler listner) {

		final String url = APIs.SOAP_SHOW() + mAppointment.getSiid()
				+ "/bk_id/" + mAppointment.getBkid();
		Log.i("SOAP API", url);
		final StringRequest patientListRequest = new StringRequest(
				Request.Method.POST, url, new Response.Listener<String>() {
					@Override
					public void onResponse(final String response) {
						Log.i("", response);
						try {
							final JSONObject jObj = new JSONObject(response);
							if (!jObj.getString("s").equals("200")) {
								// log error & return
								listner.onGetDataFinished(false);
								return;
							}
							JSONObject data = jObj.getJSONObject("data");
							getDataFromResponse(data);
							listner.onGetDataFinished(true);
						} catch (final JSONException e) {
							Log.e("", e);
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(final VolleyError error) {
						Log.e("Error.Response", error);
						listner.onGetDataFinished(false);
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
				loginParams.put("tenant_id",
						EzApp.sharedPref.getString(Constants.TENANT_ID, ""));
				loginParams.put("branch_id", EzApp.sharedPref.getString(
						Constants.USER_BRANCH_ID, ""));
				return loginParams;
			}

		};
		patientListRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 5,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		EzApp.mVolleyQueue.add(patientListRequest);
		return true;
	}

	public void getPastVisits() {

		final String url = APIs.PAST_VISIT();
		final StringRequest patientListRequest = new StringRequest(
				Request.Method.POST, url, new Response.Listener<String>() {
					@Override
					public void onResponse(final String response) {
						try {
							// Log.i("past visits", response);
							final JSONObject jObj = new JSONObject(response);
							if (!jObj.getString("s").equals("200")) {
								// log error and return
								return;
							}
							PhysicianSoapActivityMain.arrPastVisit.clear();
							for (int i = 0; i < jObj.getJSONArray("data")
									.length(); i++) {
								final JSONObject jsonVisit = jObj.getJSONArray(
										"data").getJSONObject(i);
								final PastVisitModel pastVisit = new PastVisitModel(
										jsonVisit);
								PhysicianSoapActivityMain.arrPastVisit
										.add(pastVisit);
							}
						} catch (final JSONException e) {
							e.printStackTrace();
							Log.e("", e);
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(final VolleyError error) {
						error.printStackTrace();
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
				loginParams.put("pid", mAppointment.getPid());
				loginParams.put("fid", mAppointment.getPfId());
				loginParams.put("format", "json");
				loginParams.put("bk-id", mAppointment.getBkid());
				loginParams.put("follow-id", mAppointment.getFollowid());
				Log.i("Login Params", loginParams.toString());
				return loginParams;
			}

		};
		patientListRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 5,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		EzApp.mVolleyQueue.add(patientListRequest);
	}

	public void getLastSubjective(final String id) {

		final String url = APIs.SOAP_HISTORY() + "/cli/api";
		final StringRequest patientListRequest = new StringRequest(
				Request.Method.POST, url, new Response.Listener<String>() {
					@Override
					public void onResponse(final String response) {
						try {
							// Log.i("past visits", response);
							final JSONObject jObj = new JSONObject(response);
							if (!jObj.getString("s").equals("200")) {
								// log error and return
								return;
							} else {
								final com.ezhealthtrack.physiciansoap.model.SubjectiveModel sub = visitNotes.subjectiveModel;
								sub.JsonParse(jObj.getJSONObject("data"));
								// AutoSave();
							}
						} catch (final JSONException e) {
							e.printStackTrace();
							Log.e("", e);
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(final VolleyError error) {
						error.printStackTrace();
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
				loginParams.put("id", id);
				loginParams.put("sec", "subj");
				// Log.i("Login Params", loginParams.toString());
				return loginParams;
			}

		};
		patientListRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 5,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		EzApp.mVolleyQueue.add(patientListRequest);
	}

	public void getReferPatient(final Activity context) {

		final String url = APIs.REFER_PATIENT_DETAIL();
		final StringRequest patientListRequest = new StringRequest(
				Request.Method.POST, url, new Response.Listener<String>() {
					@Override
					public void onResponse(final String response) {
						try {
							Log.i("refer response", response);
							final JSONObject jObj = new JSONObject(response);
							if (jObj.getString("s").equals("200")) {
								final JSONArray jsonReferTo = jObj
										.getJSONArray("refer-to");
								final ReferToModel referTo = new ReferToModel();
								for (int i = 0; i < jsonReferTo.length(); i++) {
									String date = referTo.refer_date = jsonReferTo
											.getJSONObject(i).getString(
													"refer-date");
									try {
										date = EzApp.sdfmmm
												.format(EzApp.sdfMmddyy
														.parse(date));
									} catch (ParseException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}

									referTo.ep_id = jsonReferTo
											.getJSONObject(i)
											.getString("ep-id");
									referTo.ep_status = jsonReferTo
											.getJSONObject(i).getString(
													"ep-status");
									referTo.refer_ep_id = jsonReferTo
											.getJSONObject(i).getString(
													"refer-ep-id");
									referTo.refer_to_id = jsonReferTo
											.getJSONObject(i).getString(
													"refer-to-id");
									referTo.refer_date = date;
									referTo.refer_to_type = jsonReferTo
											.getJSONObject(i).getString(
													"refer-to-type");
									referTo.refer_name = jsonReferTo
											.getJSONObject(i).getString(
													"refer-name");
									if (!referTo.ep_id.equals("0")) {
										arrReferTo.add(referTo);
									}
								}
								if (arrReferTo.size() > 0) {
									context.findViewById(R.id.btn_refer_to)
											.setVisibility(View.VISIBLE);
								} else {
									context.findViewById(R.id.btn_refer_to)
											.setVisibility(View.GONE);
								}

								final JSONArray jsonReferFrom = jObj
										.getJSONArray("refer-from");
								for (int i = 0; i < jsonReferFrom.length(); i++) {
									final ReferFromModel referFrom = new ReferFromModel();
									String date = referFrom.refer_date = jsonReferFrom
											.getJSONObject(i).getString(
													"refer-date");
									try {
										date = EzApp.sdfmmm
												.format(EzApp.sdfMmddyy
														.parse(date));
									} catch (ParseException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									referFrom.ep_id = jsonReferFrom
											.getJSONObject(i)
											.getString("ep-id");
									referFrom.refer_date = date;
									referFrom.refer_name = jsonReferFrom
											.getJSONObject(i).getString(
													"refer-name");
									arrReferFrom.add(referFrom);
								}
								if (arrReferFrom.size() > 0) {
									context.findViewById(R.id.btn_refer_from)
											.setVisibility(View.VISIBLE);
								} else {
									context.findViewById(R.id.btn_refer_from)
											.setVisibility(View.GONE);
								}
							} else {

							}
						} catch (final JSONException e) {
							Log.e("", e);
						}

					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(final VolleyError error) {
						error.printStackTrace();
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
				loginParams.put("ep-id", mAppointment.getEpid());
				Log.i(url, loginParams.toString());
				return loginParams;
			}

		};
		patientListRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 5,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		EzApp.mVolleyQueue.add(patientListRequest);
	}

	public void postData(final Activity context) {

		final String url = APIs.SOAP_UPDATE() + mAppointment.getSiid()
				+ "/cli/api";
		final Dialog loaddialog = Util.showLoadDialog(context);
		final JsonObjectRequest patientListRequest = new JsonObjectRequest(
				Request.Method.POST, url,
				PhysicianSoapActivityMain.visitNotes.jsonData,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(final JSONObject jObj) {
						String s = jObj.toString();
						while (s.length() > 100) {
							s = s.substring(100);
						}
						try {
							if (jObj.getString("s").equals("200")) {
								Util.AlertdialogWithFinish(context,
										"Visit Notes updated successfully");
								// Toast.makeText(PhysicianSoapActivity.this,
								// "Template Updated Successfully.",
								// Toast.LENGTH_SHORT).show();

							} else {
								Log.e("", jObj.toString());
								Util.Alertdialog(context,
										"Network Error, try again later");
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						loaddialog.dismiss();
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(final VolleyError error) {
						Log.e("Error.Response", error);
						Util.Alertdialog(context,
								"Network Error, try again later");
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
				// if (cbComplete.isChecked())
				// loginParams.put("complete", "on");
				// else
				// loginParams.put("complete", "off");
				return loginParams;
			}

		};
		patientListRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 5,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		EzApp.mVolleyQueue.add(patientListRequest);

	}

	public void pastFollowUp() {

		final String url = APIs.PAST_FOLLOWUP() + mAppointment.getBkid()
				+ "/follow-id/" + mAppointment.getFollowid();
		final StringRequest patientListRequest = new StringRequest(
				Request.Method.POST, url, new Response.Listener<String>() {
					@Override
					public void onResponse(final String response) {
						try {
							// Log.i("past visits", response);
							final JSONObject jObj = new JSONObject(response);
							if (jObj.getString("s").equals("200")) {
								try {
									// context.findViewById(R.id.txt_visit_count).setText("No. of visit : "
									// + (jObj.getJSONArray("data")
									// .length() + 1));
									for (int i = 0; i < jObj.getJSONArray(
											"data").length(); i++) {
										String date = jObj.getJSONArray("data")
												.getJSONObject(i)
												.getString("date");
										try {
											date = EzApp.sdfOnmmm
													.format(EzApp.sdfyyMmdd
															.parse(date));
										} catch (ParseException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}

										hashFollowUp.put(
												jObj.getJSONArray("data")
														.getJSONObject(i)
														.getString("id"), date);

									}

									if (Util.isEmptyString(mAppointment
											.getSiid())) {
										getLastSubjective(jObj
												.getJSONArray("data")
												.getJSONObject(
														jObj.getJSONArray(
																"data")
																.length() - 1)
												.getString("id"));
									}
								} catch (final JSONException e1) {

									e1.printStackTrace();
								}

							} else {

							}
						} catch (final JSONException e) {
							Log.e("", e);
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(final VolleyError error) {
						error.printStackTrace();
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
				Log.i("Login Params", loginParams.toString());
				return loginParams;
			}

		};
		patientListRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 5,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		EzApp.mVolleyQueue.add(patientListRequest);
	}

	public void referPatient(final String drId, final String reason,
			final Dialog dialog) {

		final String url = APIs.REFER_PATIENT_CREATE();
		final StringRequest patientListRequest = new StringRequest(
				Request.Method.POST, url, new Response.Listener<String>() {
					@Override
					public void onResponse(final String response) {
						// try {
						// Log.i("refer response", response);
						// final JSONObject jObj = new JSONObject(response);
						// if (jObj.getString("s").equals("200")) {
						// // Util.Alertdialog(
						// // PhysicianSoapActivityMain.this,
						// // "Patient has been referred successfully");
						// dialog.dismiss();
						// } else {
						// Log.e("", response);
						// }
						// } catch (final JSONException e) {
						// Log.e("", e);
						// }
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(final VolleyError error) {
						error.printStackTrace();
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
				loginParams.put("pat-id", mAppointment.getPid());
				loginParams.put("fam-id",
						PhysicianSoapActivityMain.patientModel.getFid());
				loginParams.put("ep-id", mAppointment.getEpid());
				loginParams.put("doc-id",
						EzApp.sharedPref.getString(Constants.ROLE_ID, ""));// role_id
				loginParams.put("user-id",
						EzApp.sharedPref.getString(Constants.USER_ID, ""));
				loginParams.put("pfn",
						PhysicianSoapActivityMain.patientModel.getPfn());
				loginParams.put("pln",
						PhysicianSoapActivityMain.patientModel.getPln());
				loginParams.put("reason", reason);
				loginParams.put("refer_type", "doctor");
				loginParams.put("doctor", drId);
				Log.i("Login Params", loginParams.toString());
				return loginParams;
			}

		};
		patientListRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 5,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		EzApp.mVolleyQueue.add(patientListRequest);
	}

	public void addAllergy(final String mc, final String sc, final String ai,
			final String mName, final String sName) {

		final String url = APIs.ADD_ALLERGIES();
		final StringRequest patientListRequest = new StringRequest(
				Request.Method.POST, url, new Response.Listener<String>() {
					@Override
					public void onResponse(final String response) {
						try {
							Log.i("add allergy", response);
							final JSONObject jObj = new JSONObject(response);
							if (jObj.getString("s").equals("200")) {
								AllergiesModel model = new AllergiesModel();
								model.setAddiInfo(ai);
								model.setMCatName(mName);
								model.setSCatName(sName);
								arrallergies.add(model);
							} else {
								Log.e("", response);
							}
						} catch (final JSONException e) {
							Log.e("", e);
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(final VolleyError error) {
						error.printStackTrace();
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
				final HashMap<String, String> params = new HashMap<String, String>();
				params.put("format", "json");
				params.put("bk_id", mAppointment.getBkid());
				params.put("main_cat", mc);
				params.put("sub_cat", sc);
				params.put("addi_info", ai);
				return params;
			}

		};
		patientListRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 5,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		EzApp.mVolleyQueue.add(patientListRequest);
	}

	public void createSoap(final Activity context) {

		final String url = APIs.CREATE_SOAP() + "/cli/api";
		final Dialog loaddialog = Util.showLoadDialog(context);
		final JsonObjectRequest patientListRequest = new JsonObjectRequest(
				Request.Method.POST, url, PhysicianSoapActivityMain.visitNotes.jsonData,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(final JSONObject jObj) {
						String s = jObj.toString();
						while (s.length() > 100) {
							s = s.substring(100);
						}
						try {
							if (jObj.getString("s").equals("200")) {
								Util.AlertdialogWithFinish(context,
										"Visit Notes created successfully");

							} else {
								Log.e("", jObj.toString());
								Util.Alertdialog(context,
										"Network Error, try again later");
							}

						} catch (final JSONException e) {
							Log.e("", e);
						}
						loaddialog.dismiss();
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(final VolleyError error) {
						Log.e("Error.Response", error);
						Util.Alertdialog(context,
								"Network Error, try again later");
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
				// if (cbComplete.isChecked())
				// loginParams.put("complete", "on");
				// else
				// loginParams.put("complete", "off");
				return loginParams;
			}

		};
		patientListRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 5,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		EzApp.mVolleyQueue.add(patientListRequest);

	}

	public void getPastVisitsMain(final Activity context) {

		final String url = APIs.PAST_VISIT();
		final StringRequest patientListRequest = new StringRequest(
				Request.Method.POST, url, new Response.Listener<String>() {
					@Override
					public void onResponse(final String response) {
						try {
							// Log.i("past visits", response);
							final JSONObject jObj = new JSONObject(response);
							if (jObj.getString("s").equals("200")) {
								try {
									PhysicianSoapActivityMain.arrPastVisit
											.clear();
									for (int i = 0; i < jObj.getJSONArray(
											"data").length(); i++) {
										final JSONObject jsonVisit = jObj
												.getJSONArray("data")
												.getJSONObject(i);
										final PastVisitModel pastVisit = new PastVisitModel(
												jsonVisit);
										PhysicianSoapActivityMain.arrPastVisit
												.add(pastVisit);
									}
								} catch (final JSONException e1) {

									e1.printStackTrace();
								}
								if (jObj.getJSONArray("data").length() > 0) {
									context.findViewById(R.id.btn_past_visit)
											.setVisibility(View.VISIBLE);
								} else {
									context.findViewById(R.id.btn_past_visit)
											.setVisibility(View.GONE);
								}

							} else {

							}
						} catch (final JSONException e) {
							Log.e("", e);
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(final VolleyError error) {
						error.printStackTrace();
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
				loginParams.put("pid", mAppointment.getPid());
				loginParams.put("fid", mAppointment.getPfId());
				loginParams.put("format", "json");
				loginParams.put("bk-id", mAppointment.getBkid());
				loginParams.put("follow-id", mAppointment.getFollowid());
				Log.i("Login Params", loginParams.toString());
				return loginParams;
			}

		};
		patientListRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 5,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		EzApp.mVolleyQueue.add(patientListRequest);

	}

	public void dialogDiagnosis(final String id, final Activity context) {
		final String url = APIs.SOAP_HISTORY() + "/cli/api";
		final StringRequest patientListRequest = new StringRequest(
				Request.Method.POST, url, new Response.Listener<String>() {
					@Override
					public void onResponse(final String response) {
						try {
							// Log.i("past visits", response);
							final JSONObject jObj = new JSONObject(response);
							try {
								final DiagnosisModel diag = new DiagnosisModel();
								final Dialog dialog = new Dialog(context);
								dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
								dialog.setContentView(R.layout.dialog_diagnosis);
								dialog.getWindow()
										.setBackgroundDrawable(
												new ColorDrawable(
														android.graphics.Color.TRANSPARENT));
								if (jObj.getString("s").equals("200")) {
									if (jObj.get("data") instanceof JSONObject)
										diag.JsonParse(
												jObj.getJSONObject("data"),
												(MultiAutoCompleteTextView) dialog
														.findViewById(R.id.actv_final_diagnosis),
												(MultiAutoCompleteTextView) dialog
														.findViewById(R.id.actv_provisonal_diagnosis));
								}
								dialog.setCancelable(false);
								dialog.findViewById(R.id.txt_close)
										.setOnClickListener(
												new OnClickListener() {

													@Override
													public void onClick(
															final View v) {
														dialog.dismiss();
													}
												});
								dialog.show();

							} catch (final JSONException e1) {
								e1.printStackTrace();
							}

						} catch (final JSONException e) {
							Log.e("", e);
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(final VolleyError error) {
						error.printStackTrace();
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
				loginParams.put("id", id);
				loginParams.put("sec", "asse");
				// Log.i("Login Params", loginParams.toString());
				return loginParams;
			}

		};
		patientListRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 5,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		EzApp.mVolleyQueue.add(patientListRequest);
	}

	public void dialogExam(final String id, final Activity context) {
		final String url = APIs.SOAP_HISTORY() + "/cli/api";
		final StringRequest patientListRequest = new StringRequest(
				Request.Method.POST, url, new Response.Listener<String>() {
					@Override
					public void onResponse(final String response) {
						try {
							// Log.i("past visits", response);
							final JSONObject jObj = new JSONObject(response);
							try {
								final Dialog dialog = new Dialog(context);
								dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
								dialog.setContentView(R.layout.dialog_exam_phy);
								dialog.getWindow()
										.setBackgroundDrawable(
												new ColorDrawable(
														android.graphics.Color.TRANSPARENT));
								if (jObj.getString("s").equals("200")) {
									final ExaminationModel exam = new ExaminationModel();
									if (jObj.get("data") instanceof JSONObject)
										exam.JsonParse(jObj
												.getJSONObject("data"));
									TextUtils.autoSaveTextViewLayout(
											(LinearLayout) dialog
													.findViewById(R.id.ll_vitals),
											exam.hashVitals);
									TextUtils.autoSaveTextView(
											(TextView) dialog
													.findViewById(R.id.edit_symptoms),
											exam.hashSymptoms);
									TextUtils.autoSaveTextView(
											(TextView) dialog
													.findViewById(R.id.edit_general_exam),
											exam.hashGE);
									TextUtils.autoSaveTextView(
											(TextView) dialog
													.findViewById(R.id.edit_se),
											exam.hashSE);
									TextUtils.autoSaveTextView(
											(TextView) dialog
													.findViewById(R.id.edit_ex_note),
											exam.hashNote);

								}
								dialog.setCancelable(false);
								dialog.findViewById(R.id.txt_close)
										.setOnClickListener(
												new OnClickListener() {

													@Override
													public void onClick(
															final View v) {
														dialog.dismiss();
													}
												});
								dialog.show();

							} catch (final JSONException e1) {
								e1.printStackTrace();
							}
						} catch (final JSONException e) {
							Log.e("", e);
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(final VolleyError error) {
						error.printStackTrace();
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
				loginParams.put("id", id);
				loginParams.put("sec", "obje");
				// Log.i("Login Params", loginParams.toString());
				return loginParams;
			}

		};
		patientListRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 5,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		EzApp.mVolleyQueue.add(patientListRequest);
	}

	public void dialogPlan(final String id, final Activity context) {
		final String url = APIs.SOAP_HISTORY() + "/cli/api";
		final StringRequest patientListRequest = new StringRequest(
				Request.Method.POST, url, new Response.Listener<String>() {
					@Override
					public void onResponse(final String response) {
						try {
							Log.i("past visits", response);
							final JSONObject jObj = new JSONObject(response);
							try {
								final PlanModel plan = new PlanModel();
								final PrivateNotesModel pNote = new PrivateNotesModel();
								final ArrayList<Document> arrDocuments = new ArrayList<Document>();

								final Dialog dialog = new Dialog(context,
										R.style.Theme_AppCompat_Light);
								dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
								dialog.setContentView(R.layout.dialog_plan_phy);
								dialog.getWindow()
										.setBackgroundDrawable(
												new ColorDrawable(
														android.graphics.Color.TRANSPARENT));

								dialog.setCancelable(false);

								// EzCommonViews.radiologyTable(dialog,
								// arrDocuments,
								// PhysicianSoapActivityMain.this);
								// EzCommonViews.labTable(dialog, arrDocuments,
								// PhysicianSoapActivityMain.this);
								// EzCommonViews.ekgTable(dialog, arrDocuments,
								// PhysicianSoapActivityMain.this);

								dialog.findViewById(R.id.txt_close)
										.setOnClickListener(
												new OnClickListener() {

													@Override
													public void onClick(View v) {

														dialog.dismiss();

													}
												});
								if (jObj.getString("s").equals("200")) {
									if (jObj.get("data") instanceof JSONObject) {
										plan.JsonParse(jObj
												.getJSONObject("data"));
										if (jObj.getJSONObject("data").get(
												"private-note") instanceof JSONObject) {
											pNote.jsonParse(jObj.getJSONObject(
													"data").getJSONObject(
													"private-note"));
										}
										try {
											if (jObj.has("documents")) {
												JSONArray arr = jObj
														.getJSONArray("documents");
												for (int i = 0; i < arr
														.length(); i++) {
													Document doc = new Gson()
															.fromJson(
																	arr.get(i)
																			.toString(),
																	Document.class);
													arrDocuments.add(doc);
												}
											}
										} catch (Exception e) {
											e.printStackTrace();
										}

									}
									if (pNote.hashNote
											.containsKey("private-note")) {
										((TextView) dialog
												.findViewById(R.id.txt_private_notes))
												.setText(pNote.hashNote
														.get("private-note"));
									}
									TextUtils.autoSaveTextView(
											(TextView) dialog
													.findViewById(R.id.edit_tp),
											plan.hashTp);
									((TextView) dialog
											.findViewById(R.id.txt_treatment_done))
											.setText(plan.treatmentDone
													.getTreatmentDone());
									((TextView) dialog
											.findViewById(R.id.txt_prescription)).setText(Html
											.fromHtml("<b>Prescription :</b> "));

									int i = 1;
									for (final MedicineModel medicine : plan.prescription.arrMedicine) {
										((TextView) dialog
												.findViewById(R.id.txt_prescription)).append(Html.fromHtml("<br> "
												+ "<b>"
												+ i
												+ ". </b>"
												+ medicine
														.getMedicineStringExtra()));
										i++;
									}

									// prescriptionTablePast(dialog);

									if (!Util
											.isEmptyString(plan.prescription.si
													.get("si"))
											|| visitNotes.physicianPlanModel.prescription.arrMedicine
													.size() > 0)
										((TextView) dialog
												.findViewById(R.id.txt_prescription))
												.append("\n Special Instruction : "
														+ plan.prescription.si
																.get("si"));
									((TextView) dialog
											.findViewById(R.id.txt_radiology)).setText(Html
											.fromHtml("<b>Radiology :</b> "));
									for (final Entry<String, String> entry : plan.radiology.hashRadiology
											.entrySet()) {
										if (entry.getValue().equals("on")) {
											((TextView) dialog
													.findViewById(R.id.txt_radiology))
													.append("\n      "
															+ entry.getKey());
										}
									}

									((TextView) dialog
											.findViewById(R.id.txt_lab)).setText(Html
											.fromHtml("<b>Lab Order :</b> "));
									for (final Entry<String, String> entry : plan.lab.hashLab
											.entrySet()) {
										if (entry.getValue().equals("on")) {
											String[] result = entry.getKey()
													.split("_");
											((TextView) dialog
													.findViewById(R.id.txt_lab))
													.append(entry
															.getKey()
															.replace(
																	"_"
																			+ result[result.length - 1],
																	"")
															+ ", ");
										}
									}
									((TextView) dialog
											.findViewById(R.id.txt_allergy)).setText(Html
											.fromHtml("<b>Allergy :</b> "));
									for (final AllergiesModel allergy : arrallergies) {
										((TextView) dialog
												.findViewById(R.id.txt_allergy))
												.append("\n "
														+ allergy.getMCatName()
														+ " -->"
														+ allergy.getSCatName()
														+ "\n "
														+ allergy.getAddiInfo());
									}

								}
								dialog.show();

							} catch (final JSONException e1) {
								e1.printStackTrace();
							}

						} catch (final JSONException e) {
							Log.e("", e);
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(final VolleyError error) {
						error.printStackTrace();
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
				loginParams.put("id", id);
				loginParams.put("sec", "plan");
				// Log.i("Login Params", loginParams.toString());
				return loginParams;
			}

		};
		patientListRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 5,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		EzApp.mVolleyQueue.add(patientListRequest);
	}

	public void dialogSubjective(final String id, final Activity context) {
		final String url = APIs.SOAP_HISTORY() + "/cli/api";
		final StringRequest patientListRequest = new StringRequest(
				Request.Method.POST, url, new Response.Listener<String>() {
					@Override
					public void onResponse(final String response) {
						Log.i("", response);
						try {
							Log.i("past visits", response);
							final JSONObject jObj = new JSONObject(response);
							try {
								final Dialog dialog = new Dialog(context);
								dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
								dialog.setContentView(R.layout.dialog_subjective_phy);
								dialog.getWindow()
										.setBackgroundDrawable(
												new ColorDrawable(
														android.graphics.Color.TRANSPARENT));
								if (jObj.getString("s").equals("200")) {
									final SubjectiveModel sub = new SubjectiveModel();
									if (jObj.get("data") instanceof JSONObject)
										sub.JsonParse(jObj
												.getJSONObject("data"));
									for (final Entry entry : sub.hashCC
											.entrySet()) {
										((TextView) dialog
												.findViewById(
														R.id.ll_subjective)
												.findViewWithTag(entry.getKey()))
												.setText("  "
														+ entry.getValue()
																.toString());
										if (Util.isEmptyString(((EditText) context
												.findViewById(R.id.edit_cheif_complaint))
												.getText().toString()))
											((EditText) dialog
													.findViewById(R.id.edit_cheif_complaint))
													.setText(mAppointment
															.getReason());
									}
									for (final Entry entry : sub.hashHP
											.entrySet()) {
										if (!Util.isEmptyString(entry.getKey()
												.toString())) {
											((TextView) dialog.findViewById(
													R.id.ll_subjective)
													.findViewWithTag(
															entry.getKey()))
													.setText(entry.getValue()
															.toString());
										}
									}
								}

								final CheckBox cbSmoke = (CheckBox) dialog
										.findViewById(R.id.cb_smoke);
								final CheckBox cbAlcohol = (CheckBox) dialog
										.findViewById(R.id.cb_alcohol);
								final RelativeLayout rlAlcohol = (RelativeLayout) dialog
										.findViewById(R.id.rl_alcohol);
								final RelativeLayout rlSmoke = (RelativeLayout) dialog
										.findViewById(R.id.rl_smoke);
								cbSmoke.setText("Smoking(Yes/No)");
								cbAlcohol.setText("Alcohol(Yes/No)");
								cbSmoke.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View arg0) {
										if (cbSmoke.isChecked()) {
											rlSmoke.setVisibility(View.VISIBLE);
										} else {
											rlSmoke.setVisibility(View.GONE);
										}

									}
								});

								cbAlcohol
										.setOnClickListener(new OnClickListener() {

											@Override
											public void onClick(View arg0) {
												if (cbAlcohol.isChecked()) {
													rlAlcohol
															.setVisibility(View.VISIBLE);
												} else {
													rlAlcohol
															.setVisibility(View.GONE);
												}
											}
										});

								dialog.setCancelable(false);
								dialog.findViewById(R.id.txt_close)
										.setOnClickListener(
												new OnClickListener() {

													@Override
													public void onClick(
															final View v) {
														dialog.dismiss();
													}
												});
								dialog.show();

							} catch (final JSONException e1) {
								e1.printStackTrace();
							}
						} catch (final JSONException e) {
							Log.e("", e);
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(final VolleyError error) {
						error.printStackTrace();
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
				loginParams.put("id", id);
				loginParams.put("sec", "subj");
				// Log.i("Login Params", loginParams.toString());
				return loginParams;
			}

		};
		patientListRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 5,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		EzApp.mVolleyQueue.add(patientListRequest);
	}
}
