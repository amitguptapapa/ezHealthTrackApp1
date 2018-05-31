package com.ezhealthtrack.fragments;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.view.View.OnClickListener;
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
import com.ezhealthtrack.EzApp;
import com.ezhealthtrack.R;
import com.ezhealthtrack.DentistSoap.AddDentistNotesActivity;
import com.ezhealthtrack.activity.DashboardActivity;
import com.ezhealthtrack.activity.PrescriptionActivity;
import com.ezhealthtrack.activity.SheduleActivity;
import com.ezhealthtrack.controller.SoapNotesController;
import com.ezhealthtrack.dialogs.EzDialog;
import com.ezhealthtrack.greendao.Appointment;
import com.ezhealthtrack.model.PatientShow;
import com.ezhealthtrack.one.EzUtils;
import com.ezhealthtrack.physiciansoap.PhysicianSoapActivityMain;
import com.ezhealthtrack.physiciansoap.PhysicianSoapMiniActivity;
import com.ezhealthtrack.util.Constants;
import com.ezhealthtrack.util.Log;
import com.ezhealthtrack.util.Util;

public class EzCommonActions {

	static SharedPreferences sharedPreferences(final Context context) {
		return context.getSharedPreferences(Constants.EZ_SHARED_PREF,
				Context.MODE_PRIVATE);
	}

	// 1. View Appointment Details
	static public void appointmentHistoryView(final Context context,
			final Appointment aptInfo) {

		final Dialog dialog = EzDialog.getDialog(context, R.layout.dialog_view,
				"Appointment Info");

		final SimpleDateFormat df = new SimpleDateFormat(
				"EEE, MMM dd', 'yyyy '@' hh:mm a");
		for (PatientShow p : DashboardActivity.arrPatientShow) {
			if (p.getPId().equalsIgnoreCase(aptInfo.getPid())
					&& p.getPfId().equalsIgnoreCase(aptInfo.getPfId()))
				((TextView) dialog.findViewById(R.id.txt_desc)).setText(p
						.getPfn()
						+ " "
						+ p.getPln()
						+ " has an appointment on "
						+ df.format(aptInfo.aptDate));
		}

		((TextView) dialog.findViewById(R.id.txt_reason_1)).setText(": "
				+ aptInfo.getReason());

		dialog.show();
	}

	// 2. Reschedule
	static public void appointmentHistoryReschedule(final Context context,
			final Appointment rowItem) {

		rowItem.setType("reschedule");
		DashboardActivity.arrScheduledPatients.add(rowItem);
		final Intent intent = new Intent(context, SheduleActivity.class);
		intent.putExtra("pos",
				DashboardActivity.arrScheduledPatients.lastIndexOf(rowItem));
		intent.putExtra("pid", rowItem.getPid());
		intent.putExtra("fid", rowItem.getPfId());
		intent.putExtra("from", "history");
		context.startActivity(intent);
	}

	// 3. Follow up
	static public void appointmentHistoryFollowUp(final Context context,
			final Appointment rowItem) {
		DashboardActivity.arrScheduledPatients.add(rowItem);
		rowItem.setType("followup");
		final Intent intent = new Intent(context, SheduleActivity.class);
		intent.putExtra("pos",
				DashboardActivity.arrHistoryPatients.lastIndexOf(rowItem));
		// DashboardActivity.arrScheduledPatients.lastIndexOf(rowItem));
		intent.putExtra("pid", rowItem.getPid());
		intent.putExtra("fid", rowItem.getPfId());
		intent.putExtra("type", "follow_up");
		intent.putExtra("from", "history");
		context.startActivity(intent);

	}

	// 4. Send Message
	static public void appointmentHistorySendMessage(final Context context,
			final Appointment rowItem) {
		final Dialog dialog = EzDialog.getDialog(context,
				R.layout.dialog_sendmessage, "Send Message");

		for (PatientShow p : DashboardActivity.arrPatientShow) {
			if (p.getPId().equalsIgnoreCase(rowItem.getPid())
					&& p.getPfId().equalsIgnoreCase(rowItem.getPfId()))
				((TextView) dialog.findViewById(R.id.txt_patient_name_display))
						.setText(p.getPfn() + " " + p.getPln());
		}

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
					final String url = APIs.SENDMESSAGE();
					final Dialog loaddialog = Util.showLoadDialog(context);
					// Log.i("", url);
					final StringRequest logoutRequest = new StringRequest(
							Request.Method.POST, url,
							new Response.Listener<String>() {
								@Override
								public void onResponse(final String response) {
									// Log.i("", response);
									dialog.dismiss();
									loaddialog.dismiss();
								}
							}, new Response.ErrorListener() {
								@Override
								public void onErrorResponse(
										final VolleyError error) {
									Util.Alertdialog(context,
											"There is some error while sending message");

									Log.e("Error.Response", error);
									loaddialog.dismiss();
								}
							}) {
						@Override
						public Map<String, String> getHeaders()
								throws AuthFailureError {
							final HashMap<String, String> loginParams = new HashMap<String, String>();
							loginParams.put(
									"auth-token",
									Util.getBase64String(sharedPreferences(
											context).getString(
											Constants.USER_TOKEN, "")));
							return loginParams;
						}

						@Override
						protected Map<String, String> getParams() {
							final HashMap<String, String> loginParams = new HashMap<String, String>();
							loginParams.put("format", "json");
							loginParams.put("subject", ((EditText) dialog
									.findViewById(R.id.edit_subject)).getText()
									.toString());
							loginParams.put("body_msg", ((EditText) dialog
									.findViewById(R.id.edit_body)).getText()
									.toString());
							loginParams.put("context", rowItem.getBkid());
							loginParams.put("context_type", "booking");
							loginParams.put("to_id", rowItem.getPid());
							loginParams.put("to_fam_id", rowItem.getPfId());
							loginParams.put("to_type", "P");
							Log.i(url, loginParams.toString());
							return loginParams;
						}

					};
					logoutRequest.setRetryPolicy(new DefaultRetryPolicy(20000,
							5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
					EzApp.mVolleyQueue.add(logoutRequest);
					// dialog.dismiss();
					try {
						InputMethodManager imm = (InputMethodManager) context
								.getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(
								dialog.findViewById(R.id.edit_body)
										.getWindowToken(), 0);
					} catch (Exception e) {

					}
					dialog.dismiss();

					Util.Alertdialog(context, "Message send successfully");

				}
			}

		});

		dialog.show();
	}

	// 5. Prescription
	static public void appointmentHistoryPrescription(final Context context,
			final Appointment rowItem) {

		Intent intent;
		intent = new Intent(context, PrescriptionActivity.class);
		intent.putExtra("position",
				DashboardActivity.arrHistoryPatients.lastIndexOf(rowItem));
		intent.putExtra("bkId", rowItem.getBkid());
		intent.putExtra("from", "history");
		context.startActivity(intent);
	}

	// 6. Visit Notes
	static public void appointmentHistoryVisitNotes(final Context context,
			final Appointment rowItem) {
		Appointment apt = new Appointment();
		apt.aptDate = rowItem.getApt_Date();
		apt.setBkid(rowItem.getBkid());
		apt.setSiid(rowItem.getSiid());
		apt.setPid(rowItem.getPid());
		apt.setPfId("0");
		apt.setVisit(rowItem.getVisit());
		apt.setReason(rowItem.getReason());
		Intent intent;
		if (sharedPreferences(context).getString(Constants.DR_SPECIALITY, "")
				.equalsIgnoreCase("Dentist")) {
			intent = new Intent(context, AddDentistNotesActivity.class);
			intent.putExtra("position",
					DashboardActivity.arrHistoryPatients.lastIndexOf(rowItem));
			intent.putExtra("from", "history");
			context.startActivity(intent);
		} else {
			String deviceType = EzUtils.getDeviceSize(null);
			if (deviceType.equals(EzUtils.EZ_SCREEN_SMALL)) {
				intent = new Intent(context, PhysicianSoapMiniActivity.class);
				intent.putExtra("bkid", rowItem.getBkid());
				intent.putExtra("siid", rowItem.getSiid());
				intent.putExtra("type", "past");

				PhysicianSoapMiniActivity.Appointment = apt;
				SoapNotesController.setAppointment(apt);
				context.startActivity(intent);
			} else {
				PhysicianSoapActivityMain.Appointment = apt;
				SoapNotesController.setAppointment(apt);
				intent = new Intent(context, PhysicianSoapActivityMain.class);
				context.startActivity(intent);
			}
		}
	}

}
