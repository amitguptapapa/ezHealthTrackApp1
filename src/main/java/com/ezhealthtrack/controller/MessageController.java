package com.ezhealthtrack.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ezhealthrack.api.APIs;
import com.ezhealthrack.api.LabApis;
import com.ezhealthtrack.EzApp;
import com.ezhealthtrack.R;
import com.ezhealthtrack.activity.ComposeMessageActivity;
import com.ezhealthtrack.fragments.InboxFragment;
import com.ezhealthtrack.fragments.OutboxFragment;
import com.ezhealthtrack.greendao.MessageModel;
import com.ezhealthtrack.greendao.PatientShow;
import com.ezhealthtrack.greendao.PatientShowDao.Properties;
import com.ezhealthtrack.model.PatientAutoSuggest;
import com.ezhealthtrack.one.EzUtils;
import com.ezhealthtrack.util.Constants;
import com.ezhealthtrack.util.Log;
import com.ezhealthtrack.util.NetworkCallController.OnResponse;
import com.ezhealthtrack.util.Util;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class MessageController {
	public static void showSendMessageDialog(final Context context,
			final String bkid, final String pid, final String pfid,
			final Date date, final String context_type) {
		final Dialog dialog = new Dialog(context);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_sendmessage);
		final SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM dd, yyyy");
		// = apt.getAptdate();
		((TextView) dialog.findViewById(R.id.txt_date)).setText(sdf
				.format(date));
		try {
			if (EzApp.patientShowDao.queryBuilder()
					.where(Properties.P_id.eq(pid), Properties.Pf_id.eq(pfid))
					.count() > 0) {
				PatientShow p = (EzApp.patientShowDao
						.queryBuilder()
						.where(Properties.P_id.eq(pid),
								Properties.Pf_id.eq(pfid)).list().get(0));
				((TextView) dialog.findViewById(R.id.txt_patient_name))
						.setText(p.getPfn() + " " + p.getPln());
			}

		} catch (Exception e) {
			Log.e("", e);
		}
		dialog.setCancelable(false);
		dialog.findViewById(R.id.txt_close).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						try {
							InputMethodManager imm = (InputMethodManager) context
									.getSystemService(Context.INPUT_METHOD_SERVICE);
							imm.hideSoftInputFromWindow(
									dialog.findViewById(R.id.edit_body)
											.getWindowToken(), 0);
						} catch (Exception e) {

						}
						dialog.dismiss();

					}
				});
		final Button button = (Button) dialog.findViewById(R.id.btn_submit);

		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(final View v) {
				if (Util.isEmptyString(((EditText) dialog
						.findViewById(R.id.edit_subject)).getText().toString())) {
					Util.Alertdialog(context, "Please enter Subject");
				} else if (Util.isEmptyString(((EditText) dialog
						.findViewById(R.id.edit_body)).getText().toString())) {
					Util.Alertdialog(context, "Please enter Body");
				} else {
					sendLabMessage(((EditText) dialog
							.findViewById(R.id.edit_subject)).getText()
							.toString(),
							((EditText) dialog.findViewById(R.id.edit_body))
									.getText().toString(), context, dialog,
							bkid, pid, pfid, context_type);

					try {
						InputMethodManager imm = (InputMethodManager) context
								.getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(
								dialog.findViewById(R.id.edit_body)
										.getWindowToken(), 0);
					} catch (Exception e) {

					}
				}
			}
		});
		dialog.show();
	}

	public static void sendLabMessage(String subject, String body,
			final Context context, final Dialog dialog, final String bkid,
			final String pid, final String pfid, final String context_type) {

		String url = LabApis.SENDMESSAGE;
		final HashMap<String, String> params = new HashMap<String, String>();
		params.put("format", "json");
		params.put("subject", subject);
		params.put("body_msg", body);
		params.put("context", bkid);
		params.put("context_type", context_type);
		params.put("to_id", pid);
		params.put("to_fam_id", pfid);
		params.put("to_type", "P");
		final Dialog loaddialog = Util.showLoadDialog(context);
		EzApp.networkController.networkCall(context, url, params,
				new OnResponse() {

					@Override
					public void onResponseListner(String response) {
						Log.i("", response);
						try {
							JSONObject jObj = new JSONObject(response);
							if (jObj.getString("s").equals("200")) {
								Util.Alertdialog(context,
										"Message send successfully");

								dialog.dismiss();
							} else {
								Util.Alertdialog(context,
										"There is some error in sending message, Please try again");
							}
						} catch (Exception e) {
							Util.Alertdialog(context,
									"There is some error in sending message, Please try again");
						}
						loaddialog.dismiss();

					}
				});
	}

	public static void getLabInboxMessages(int pageno, Context context,
			final OnResponse onresponse, String cmbMsg, final String condVal,
			final PatientAutoSuggest pat) {
		String url = LabApis.INBOX;
		final HashMap<String, String> params = new HashMap<String, String>();
		params.put("cli", "api");
		if (pageno != 1) {
			params.put("page_num", "" + pageno);
			params.put("condval", condVal);
		}
		params.put("cmb_msg", cmbMsg);
		if (pat != null) {
			params.put("sel_pat", pat.getId());
			params.put("search", "");
		}
		Log.i(url, params.toString());
		EzApp.networkController.networkCall(context, url, params,
				new OnResponse() {

					@Override
					public void onResponseListner(String response) {
						try {

							final JSONObject jObj = new JSONObject(response);
							Log.i("", response);
							final JSONArray data = jObj.getJSONArray("data");
							if (Util.isEmptyString(condVal)) {
								InboxFragment.totalCount = jObj.getInt("count");
								InboxFragment.unreadCount = jObj
										.getInt("unread");
							}
							Gson gson = new GsonBuilder().setDateFormat(
									"yyyy-MM-dd hh:mm").create();
							for (int i = 0; i < data.length(); i++) {
								final MessageModel model = gson.fromJson(data
										.getJSONObject(i).toString(),
										MessageModel.class);
								model.setId((long) Integer.parseInt(model
										.getNid()));
								model.setMessage_type(Constants.INBOX);
								EzApp.messageDao
										.insertOrReplace(model);
								// DashboardActivity.arrInboxMessages.add(model);
							}
							onresponse.onResponseListner(jObj
									.getString("condval"));

						} catch (final JSONException e) {
							onresponse.onResponseListner("error");
							Log.e("", e);
						}

					}
				});
	}

	public static void getLabOutboxMessages(final int pageno, Context context,
			final String condVal, final OnResponse onresponse) {
		String url = LabApis.OUTBOX;
		final HashMap<String, String> params = new HashMap<String, String>();
		params.put("cli", "api");
		params.put("page_num", "" + pageno);
		params.put("condval", "");
		EzApp.networkController.networkCall(context, url, params,
				new OnResponse() {

					@Override
					public void onResponseListner(String response) {
						Log.i("", response);
						try {
							if (pageno == 1) {
								EzApp.messageDao
										.queryBuilder()
										.where(com.ezhealthtrack.greendao.MessageModelDao.Properties.Message_type
												.eq(Constants.OUTBOX))
										.buildDelete()
										.executeDeleteWithoutDetachingEntities();
								// EzHealthApplication.messageDao.deleteAll();
							}
							final JSONObject jObj = new JSONObject(response);
							final JSONArray data = jObj.getJSONArray("data");
							OutboxFragment.totalCount = jObj.getInt("count");
							Gson gson = new GsonBuilder().setDateFormat(
									"yyyy-MM-dd hh:mm").create();
							for (int i = 0; i < data.length(); i++) {

								final MessageModel model = gson.fromJson(data
										.getJSONObject(i).toString(),
										MessageModel.class);
								model.setId((long) Integer.parseInt(model
										.getNid()));
								model.setMessage_type(Constants.OUTBOX);
								EzApp.messageDao
										.insertOrReplace(model);
							}
							onresponse.onResponseListner(jObj
									.getString("condval"));
						} catch (final JSONException e) {
							Log.e("", e.toString());
						}

					}
				});
	}

	public static void sendComposeMessage(String subject, String emailbody,
			final Context context, List<Object> list, Boolean msgtoall,
			String smsbody, int messageType) {

		String url = APIs.SEND_COMPOSE_MESSAGE();
		final HashMap<String, String> params = new HashMap<String, String>();
		params.put("format", "json");
		params.put("Notification[message_to_type]", "P");
		int count = 0;
		for (Object obj : list) {
			PatientAutoSuggest pat = (PatientAutoSuggest) obj;
			params.put("Notification[message_to][" + count + "]", pat.getId());
		}
		if (msgtoall)
			params.put("Notification[message_to_all]", "1");
		else
			params.put("Notification[message_to_all]", "0");
		params.put("Notification[message_subject]", subject);

		if (messageType == ComposeMessageActivity.MTYPE_EMAIL)
			params.put("Notification[message_type][email]", "1");
		else
			params.put("Notification[message_type][email]", "0");

		if (messageType == ComposeMessageActivity.MTYPE_MESSAGE)
			params.put("Notification[message_type][iam]", "1");
		else
			params.put("Notification[message_type][iam]", "0");

		if (messageType == ComposeMessageActivity.MTYPE_SMS)
			params.put("Notification[message_type][sms]", "1");
		else
			params.put("Notification[message_type][sms]", "0");
		params.put("Notification[message_sms_body]", smsbody);
		params.put("Notification[message_body]", emailbody);
		Log.i(url, params.toString());
		final Dialog loaddialog = Util.showLoadDialog(context);
		EzApp.networkController.networkCall(context, url, params,
				new OnResponse() {

					@Override
					public void onResponseListner(String response) {
						Log.i("", response);
						try {
							JSONObject jObj = new JSONObject(response);
							if (jObj.getString("s").equals("200")) {
								Util.AlertdialogWithFinish(context,
										"Message send successfully");

								loaddialog.dismiss();
							} else {
								Util.Alertdialog(context,
										"There is some error in sending message, Please try again");
							}
						} catch (Exception e) {
							Util.Alertdialog(context,
									"There is some error in sending message, Please try again");
						}
						loaddialog.dismiss();

					}
				});
	}

	public interface SendMessageResponse {
		void onSendMessageResponse(boolean success);
	}

	static public void sendMessage(final MessageModel model, final String body,
			final Context context, final SendMessageResponse listner) {

		if (Util.isEmptyString(body)) {
			EzUtils.showLong("Please write a message");
			return;
		}
		android.util.Log.v("MC:sendMessage()",
				"model: " + new Gson().toJson(model) + ", body: " + body);

		final String url = APIs.REPLYMESSAGE();
		// final Dialog loaddialog = Util.showLoadDialog(context);
		Log.i("", url);
		final StringRequest logoutRequest = new StringRequest(
				Request.Method.POST, url, new Response.Listener<String>() {
					@Override
					public void onResponse(final String response) {
						Log.i("MC:sendMessage()", "Response: " + response);
						// loaddialog.dismiss();
						try {
							JSONObject jObj = new JSONObject(response);
							if (jObj.getString("s").equals("200")) {
								int count = 1;
								if (model.getConv_count() != null) {
									count = 1 + model.getConv_count();
								} else {
									android.util.Log.v("MC:sendMessage()",
											"Model.getConv_count() is NULL");
								}
								model.setConv_count(count);
								EzApp.messageDao
										.insertOrReplace(model);
								Util.Alertdialog(context,
										"Your message has been sent successfully.");
								listner.onSendMessageResponse(true);
							} else {
								Util.Alertdialog(context,
										"There is some error while sending message");
								listner.onSendMessageResponse(false);
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							listner.onSendMessageResponse(false);
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(final VolleyError error) {
						Util.Alertdialog(context,
								"There is some error while sending message.");
						Log.e("Error.Response", error);
						listner.onSendMessageResponse(false);
						// loaddialog.dismiss();
					}
				}) {
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				final HashMap<String, String> loginParams = new HashMap<String, String>();
				loginParams.put("auth-token", Util
						.getBase64String(EzApp.sharedPref
								.getString(Constants.USER_TOKEN, "")));
				android.util.Log
						.v("MC:sendMessage()", "Header: " + loginParams);
				return loginParams;
			}

			@Override
			protected Map<String, String> getParams() {
				final HashMap<String, String> loginParams = new HashMap<String, String>();
				loginParams.put("format", "json");
				loginParams.put("cli", "api");
				if (model.getContext_type().equalsIgnoreCase("notification")) {
					loginParams.put("context", model.getContext());
				} else {
					loginParams.put("context", model.getNid());
				}
				loginParams.put("to_id", model.getFrom_id());
				loginParams.put("to_fam_id", model.getFrom_fam_id());
				loginParams.put("to_type", model.getFrom_type());
				String ntid = model.getNt_id();
				loginParams.put("nt_id", (ntid == null ? "0" : ntid));
				loginParams.put("nid", model.getNid());
				try {
					loginParams.put("sub",
							(new JSONObject(model.getData())).getString("sub"));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				loginParams.put("body_msg", body);
				android.util.Log
						.v("MC:sendMessage()", "Params: " + loginParams);
				return loginParams;
			}

		};
		logoutRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 5,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		EzApp.mVolleyQueue.add(logoutRequest);
	}

}
