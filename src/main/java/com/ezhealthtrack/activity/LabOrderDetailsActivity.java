package com.ezhealthtrack.activity;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ezhealthrack.api.APIs;
import com.ezhealthrack.api.NetworkCalls;
import com.ezhealthtrack.EzApp;
import com.ezhealthtrack.R;
import com.ezhealthtrack.controller.EzNetwork;
import com.ezhealthtrack.controller.EzNetwork.ResponseHandler;
import com.ezhealthtrack.dialogs.EzDialog;
import com.ezhealthtrack.greendao.Appointment;
import com.ezhealthtrack.labs.fragments.SideFragment.OnActionSelectedListner;
import com.ezhealthtrack.model.laborder.Data;
import com.ezhealthtrack.model.laborder.LabOrderDetails;
import com.ezhealthtrack.model.laborder.TestReport;
import com.ezhealthtrack.one.EzActivities;
import com.ezhealthtrack.one.EzUtils;
import com.ezhealthtrack.util.Constants;
import com.ezhealthtrack.util.EzActivity;
import com.ezhealthtrack.util.Util;
import com.flurry.android.FlurryAgent;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class LabOrderDetailsActivity extends EzActivity implements
		OnActionSelectedListner, NetworkCalls.OnResponse {

	private String mOrderId;

	private Button btnSendMessage;
	private Button btnViewAllReport;

	private TextView txtPname;
	private TextView txtOrderDate;
	private TextView txtOrder;
	private TextView txtTechnician;
	private TextView txtOrderStatus;
	private TextView txtDoctor;
	private TextView txtApprovalStatus;
	private TextView txtHomeVisit;

	private LinearLayout mTestReports;
	private Appointment rowItem = new Appointment();
	LabOrderDetails mOrderDetails;
	private SharedPreferences sharedPref;
	private LinearLayout mLLProgressBar;

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lab_order_details);
		this.setDisplayHomeAsUpEnabled(true);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			mOrderId = extras.getString("id");
			if (mOrderId == null) {
				EzUtils.showLong("Bad Order Id");
				mOrderId = "001";
			}
		} else {
			EzUtils.showLong("No Order Id");
			mOrderId = "002";
		}

		mLLProgressBar = (LinearLayout) findViewById(R.id.idll_progressbar);
		mLLProgressBar.setVisibility(View.VISIBLE);
		txtPname = (TextView) findViewById(R.id.txt_name_display);
		txtOrderDate = (TextView) findViewById(R.id.txt_order_date_display);
		txtOrder = (TextView) findViewById(R.id.txt_order_display);
		txtTechnician = (TextView) findViewById(R.id.txt_technician_display);
		txtOrderStatus = (TextView) findViewById(R.id.txt_order_status_display);
		txtDoctor = (TextView) findViewById(R.id.txt_doctor_display);
		txtApprovalStatus = (TextView) findViewById(R.id.txt_approval_status_display);
		txtHomeVisit = (TextView) findViewById(R.id.txt_home_visit_display);

		mTestReports = (LinearLayout) findViewById(R.id.ll_test_list);

		btnSendMessage = (Button) findViewById(R.id.btn_message);
		btnViewAllReport = (Button) findViewById(R.id.btn_view_all_reports);

		btnSendMessage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				FlurryAgent
						.logEvent("LabsOrderDetailsActivity - Print Label Button Clicked");
				showSendMessageDialog(rowItem);

			}
		});

		btnViewAllReport.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				EzActivities.startPrintAllLabReportActivity(
						LabOrderDetailsActivity.this, mOrderDetails);
			}
		});

	}

	public boolean onOptionsItemSelected(final MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {

		case android.R.id.home:
			finish();
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onStart() {
		super.onStart();

		Log.i("LOD-A", "onStart()");
		// TODO Auto-generated method stub
		if (mOrderDetails != null)
			return;

		String url = APIs.LAB_ORDER_DETAILS().replace("{order_id}", mOrderId);
		Map<String, String> params = new HashMap<String, String>();

		new EzNetwork().POST(url, params, new ResponseHandler() {

			@Override
			public void cmdResponseError(Integer code) {
				// TODO Auto-generated method stub
				Log.i("LOD-A", "Error: " + code);
				mLLProgressBar.setVisibility(View.GONE);
				EzUtils.showLong("Report can't be download. Please retry..");
				finish();
			}

			@Override
			public void cmdResponse(JSONObject response, String result) {
				// TODO Auto-generated method stub

				// NOT REQUIRED WITH NEW RESPONSE
				// try {
				// JSONObject data = response.getJSONObject("data");
				// JSONArray reports = data.getJSONArray("test_reports");
				//
				// for (int i = 0; i < reports.length(); i++) {
				// Log.e("LOD-A", "Parse SAMPLE_META for i=" + i
				// + ", Data="
				// + reports.getJSONObject(i).toString());
				//
				// String sampleStr = reports.getJSONObject(i).getString(
				// "sample_meta");
				// Log.e("LOD-A", "Parsed SAMPLE_META=" + sampleStr);
				//
				// if (sampleStr != null && !sampleStr.isEmpty()
				// && !sampleStr.equals("null")) {
				// Log.e("LOD-A", "sample_meta is NOT NULL for i=" + i);
				// } else {
				// sampleStr = "[{}]";
				// Log.e("LOD-A", "sample_meta is NULL for i=" + i);
				// }
				// JSONArray sample = new JSONArray(sampleStr);
				// reports.getJSONObject(i).put("sample_meta", sample);
				// }
				// } catch (JSONException e) {
				// Log.e("LOD-A", "Parse Error:" + e.getMessage());
				// }

				Log.i("LOD-A:::", "" + response.toString());
				mLLProgressBar.setVisibility(View.GONE);
				try {
					mOrderDetails = new Gson().fromJson(response.toString(),
							LabOrderDetails.class);
				} catch (JsonSyntaxException e) {
					EzUtils.showLong("Report can't be download. Please retry..");
					EzUtils.showLong("" + e.getMessage());
					Log.e("LOD-A", "" + e.getMessage());

					try {
						JSONObject data = response.getJSONObject("data");
						JSONArray reports = data.getJSONArray("test_reports");
						for (int i = 0; i < reports.length(); i++) {
							// String s = reports.getJSONObject(i).getString(
							// "sample_meta");
							// Log.e("LOD-A", "" + i + " : " + s);
						}
					} catch (JSONException e2) {
						Log.e("LOD-A", "Parse Error:" + e2.getMessage());
					}
					return;
				}
				onDataUpdate();
			}
		});
	}

	void onDataUpdate() {
		// mOrderDetails
		if (mOrderDetails == null)
			return;
		Data data = mOrderDetails.getData();
		txtPname.setText(data.getPatientDetail());
		txtOrderDate.setText(data.getOrderDate());
		txtOrder.setText(data.getDisplayOrderId());
		txtTechnician.setText(data.getTechnicianName());
		txtOrderStatus.setText(data.getOrderStatus());
		txtDoctor.setText(data.getDoctorDetail());
		txtApprovalStatus.setText(data.getApprovalStatus());
		Log.i("APPr St", data.getApprovalStatus());
		txtHomeVisit.setText(data.getPatientLocationAppointment());

		// add test reports status
		List<TestReport> reports = mOrderDetails.getData().getTestReports();

		mTestReports.removeAllViews();
		for (int i = 0; i < reports.size(); ++i) {

			final TestReport report = reports.get(i);
			LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			final View view = inflater.inflate(R.layout.row_order_details_list,
					null);

			TextView txtName = (TextView) view.findViewById(R.id.txt_test_name);

			TextView txtDate = (TextView) view.findViewById(R.id.txt_date);

			Button viewBtn = (Button) view.findViewById(R.id.action_btn_view);
			viewBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {

					EzActivities.startPrintLabReportActivity(
							LabOrderDetailsActivity.this, mOrderDetails);
				}
			});

			txtName.setText(report.getReportName());
			try {
				Date date;
				String theDate = report.getReportPreparedOn();
				theDate = report.getReportAvailableOn();
				if (!Util.isEmptyString(theDate)) {
					date = EzApp.sdfyymmddhhmmss.parse(theDate);
					txtDate.setText(EzApp.sdfddMmyy.format(date));
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
			if (report.getStatus().equalsIgnoreCase("View")) {
				txtDate.setVisibility(View.VISIBLE);
				viewBtn.setVisibility(View.VISIBLE);

			} else {
				txtDate.setVisibility(View.INVISIBLE);
				viewBtn.setVisibility(View.INVISIBLE);

			}

			if (data.getTestReports().size() > 1
					&& report.getStatus().equalsIgnoreCase("View")) {
				btnViewAllReport.setVisibility(View.VISIBLE);
			} else {
				btnViewAllReport.setVisibility(View.GONE);
			}
			mTestReports.addView(view);
		}
	}

	@Override
	public void onResponseListner(String api) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onActionSelected(int position) {
		// TODO Auto-generated method stub
	}

	private void showSendMessageDialog(final Appointment rowItem) {
		final Dialog dialog = EzDialog.getDialog(LabOrderDetailsActivity.this,
				R.layout.dialog_sendmessage, "Send Message");

		Data data = mOrderDetails.getData();

		// Getting Patient first and last name from patient details
		StringTokenizer main = new StringTokenizer(data.getPatientDetail(), " ");
		String PatFirstName = main.nextToken();
		String PatLastName = main.nextToken();

		((TextView) dialog.findViewById(R.id.txt_patient_name_display))
				.setText(PatFirstName + " " + PatLastName);

		final Button button = (Button) dialog.findViewById(R.id.btn_submit);

		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(final View v) {
				if (Util.isEmptyString(((EditText) dialog
						.findViewById(R.id.edit_subject)).getText().toString())) {
					Util.Alertdialog(LabOrderDetailsActivity.this,
							"Please enter Subject");
				} else if (Util.isEmptyString(((EditText) dialog
						.findViewById(R.id.edit_body)).getText().toString())) {
					Util.Alertdialog(LabOrderDetailsActivity.this,
							"Please enter Body");
				} else {
					final String url = APIs.SENDMESSAGE();
					final Dialog loaddialog = Util
							.showLoadDialog(LabOrderDetailsActivity.this);
					Log.i("", url);
					final StringRequest logoutRequest = new StringRequest(
							Request.Method.POST, url,
							new Response.Listener<String>() {
								@Override
								public void onResponse(final String response) {
									Log.i("", response);

									dialog.dismiss();
									loaddialog.dismiss();
								}

							}, new Response.ErrorListener() {
								@Override
								public void onErrorResponse(
										final VolleyError error) {
									dialog.dismiss();
									loaddialog.dismiss();
								}
							}) {
						@Override
						public Map<String, String> getHeaders()
								throws AuthFailureError {

							sharedPref = LabOrderDetailsActivity.this
									.getSharedPreferences(
											Constants.EZ_SHARED_PREF,
											Context.MODE_PRIVATE);
							final HashMap<String, String> loginParams = new HashMap<String, String>();
							loginParams.put("auth-token", Util
									.getBase64String(sharedPref.getString(
											Constants.USER_TOKEN, "")));
							return loginParams;
						}

						@Override
						protected Map<String, String> getParams() {
							final HashMap<String, String> loginParams = new HashMap<String, String>();
							loginParams.put("format", "json");
							loginParams.put("context", rowItem.getBkid());
							loginParams.put("context_type", "booking");
							loginParams.put("to_id", rowItem.getPid());
							loginParams.put("to_fam_id", rowItem.getPfId());
							loginParams.put("to_type", "P");
							return loginParams;
						}

					};
					logoutRequest.setRetryPolicy(new DefaultRetryPolicy(20000,
							5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
					EzApp.mVolleyQueue.add(logoutRequest);
					dialog.dismiss();
					loaddialog.dismiss();
					Util.Alertdialog(LabOrderDetailsActivity.this,
							"Message send successfully");
				}
			}
		});
		dialog.setCancelable(false);
		dialog.show();

	}

}
