package com.ezhealthtrack.labs.adapter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ezhealthtrack.EzApp;
import com.ezhealthtrack.R;
import com.ezhealthtrack.activity.SheduleActivityNew;
import com.ezhealthtrack.controller.MessageController;
import com.ezhealthtrack.controller.PatientController;
import com.ezhealthtrack.controller.PatientController.OnResponsePatient;
import com.ezhealthtrack.greendao.LabAppointment;
import com.ezhealthtrack.greendao.Patient;
import com.ezhealthtrack.greendao.PatientShow;
import com.ezhealthtrack.greendao.PatientShowDao.Properties;
import com.ezhealthtrack.labs.activity.LabsOrderCreateActivity;
import com.ezhealthtrack.labs.activity.LabsOrderDetailsActivity;
import com.ezhealthtrack.labs.controller.LabsAppointmentController;
import com.ezhealthtrack.util.Constants;
import com.ezhealthtrack.util.LazyListAdapter;
import com.ezhealthtrack.util.Log;
import com.ezhealthtrack.util.Util;
import com.flurry.android.FlurryAgent;

public class LabsAppointmentAdapter extends LazyListAdapter<LabAppointment> {
	/* private view holder class */
	private static class ViewHolder {
		private TextView txtConfirmed;
		private RelativeLayout rlActions;
		private Button btnReSchedule;
		private Button btnSendMessage;
		private Button btnCheckin;
		private Button btnReject;
		private Button btnOrder;
		private Button btnNoShow;
		private Button btnFollowUp;
		private Button btnNew;
		private Button btnAccept;
	}

	Context context;

	int type;

	public LabsAppointmentAdapter(final Context context, int type) {
		this.context = context;
		this.type = type;
	}

	@Override
	public View getView(final int position, final View convertView,
			final ViewGroup parent) {

		final LabAppointment rowItem = getItem(position);

		View row = null;
		if (convertView == null) {
			final ViewHolder holder = new ViewHolder();
			final LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.row_appointment, null);
			holder.txtConfirmed = (TextView) row
					.findViewById(R.id.txt_confirmed);
			if (rowItem.getApflag().equals("0")
					&& rowItem.getFlag().equals("0")) {
				holder.rlActions = (RelativeLayout) row
						.findViewById(R.id.rl_actions_newtentative);
				holder.btnSendMessage = (Button) row
						.findViewById(R.id.btn_sendmessage_new);
				holder.btnFollowUp = (Button) row
						.findViewById(R.id.btn_followup);
				holder.btnOrder = (Button) row.findViewById(R.id.btn_order);
				holder.btnReSchedule = (Button) row
						.findViewById(R.id.btn_reschedule_new);
				holder.btnReject = (Button) row
						.findViewById(R.id.btn_reject_new);
			} else if (rowItem.getAptdate().after(Util.getCurrentDate())) {
				holder.rlActions = (RelativeLayout) row
						.findViewById(R.id.rl_actions_confirmed);
				holder.btnSendMessage = (Button) row
						.findViewById(R.id.btn_sendmessage);
				holder.btnFollowUp = (Button) row
						.findViewById(R.id.btn_followup);
				holder.btnOrder = (Button) row.findViewById(R.id.btn_order);
				holder.btnReSchedule = (Button) row
						.findViewById(R.id.btn_reschedule);
				holder.btnReject = (Button) row.findViewById(R.id.btn_reject);
			} else {
				holder.rlActions = (RelativeLayout) row
						.findViewById(R.id.rl_actions_history);
				holder.btnSendMessage = (Button) row
						.findViewById(R.id.btn_sendmessage_hist);
				holder.btnFollowUp = (Button) row
						.findViewById(R.id.btn_followups);
				holder.btnOrder = (Button) row.findViewById(R.id.btn_orders);
				holder.btnReSchedule = (Button) row
						.findViewById(R.id.btn_reschedule);
				holder.btnReject = (Button) row.findViewById(R.id.btn_reject);
			}
			holder.btnAccept = (Button) row.findViewById(R.id.btn_accept);

			holder.btnCheckin = (Button) row.findViewById(R.id.btn_checkin);

			holder.btnNoShow = (Button) row.findViewById(R.id.btn_noshow);

			holder.btnNew = (Button) row.findViewById(R.id.btn_new);

			row.setTag(holder);
		} else {
			row = convertView;
		}
		final ViewHolder holder = (ViewHolder) row.getTag();
		holder.btnCheckin.setVisibility(View.GONE);
		holder.btnReject.setVisibility(View.GONE);
		holder.btnReSchedule.setVisibility(View.GONE);
		holder.btnOrder.setVisibility(View.GONE);
		holder.btnNoShow.setVisibility(View.GONE);
		holder.btnFollowUp.setVisibility(View.GONE);
		holder.btnNew.setVisibility(View.GONE);
		if (!rowItem.getFlag().equals("2")) {
			holder.btnReSchedule.setVisibility(View.VISIBLE);
			holder.btnReject.setVisibility(View.VISIBLE);
			holder.btnNew.setVisibility(View.VISIBLE);
			holder.btnNoShow.setVisibility(View.VISIBLE);
		}
		if (rowItem.getFlag().equals("0") && rowItem.getApflag().equals("1")) {
			Log.i("", "" + TimeZone.getDefault().getRawOffset());
			int offsetCurrentTimeZone = TimeZone.getDefault().getRawOffset();
			int offsetIST = 11 * 30 * 60 * 1000;
			Date date = new Date();
			date.setTime(date.getTime() + offsetIST - offsetCurrentTimeZone);
			date.setHours(0);
			date.setMinutes(0);
			Date date1 = new Date();
			date1.setTime(date.getTime() + 86400000 + offsetIST
					- offsetCurrentTimeZone);
			if (rowItem.getAptdate().after(date)
					&& rowItem.getAptdate().before(date1))
				holder.btnCheckin.setVisibility(View.VISIBLE);
			holder.btnReject.setVisibility(View.VISIBLE);
		} else if (rowItem.getFlag().equals("2")) {
			holder.btnOrder.setVisibility(View.VISIBLE);
			holder.btnFollowUp.setVisibility(View.VISIBLE);
		}
		PatientShow pat1 = new PatientShow();
		try {
			if (EzApp.patientShowDao
					.queryBuilder()
					.where(Properties.P_id.eq(rowItem.getPid()),
							Properties.Pf_id.eq(rowItem.getPfid())).count() > 0) {
				pat1 = (EzApp.patientShowDao
						.queryBuilder()
						.where(Properties.P_id.eq(rowItem.getPid()),
								Properties.Pf_id.eq(rowItem.getPfid())).list()
						.get(0));
			}

		} catch (Exception e) {
			Log.e("", e);
		}
		Date currentDate = Calendar.getInstance().getTime();
		currentDate.setHours(0);
		currentDate.setMinutes(0);
		currentDate.setSeconds(0);
		Date tomorrowDate = new Date(currentDate.getTime() + 24 * 60 * 60000
				- 60000);

		if (rowItem.getAptdate().compareTo(currentDate) >= 0
				&& rowItem.getAptdate().compareTo(tomorrowDate) <= 0) {
			final SimpleDateFormat df1 = new SimpleDateFormat(
					"' @ 'hh:mm a' Today '");
			PatientShow p = new PatientShow();
			try {
				if (EzApp.patientShowDao
						.queryBuilder()
						.where(Properties.P_id.eq(rowItem.getPid()),
								Properties.Pf_id.eq(rowItem.getPfid())).count() > 0) {
					p = (EzApp.patientShowDao
							.queryBuilder()
							.where(Properties.P_id.eq(rowItem.getPid()),
									Properties.Pf_id.eq(rowItem.getPfid()))
							.list().get(0));
				}

			} catch (Exception e) {
				Log.e("", e);
			}
			holder.txtConfirmed.setText(Html.fromHtml(p.getPfn() + " "
					+ p.getPln() + ", " + p.getAge() + "/" + p.getGender()
					+ ", " + p.getP_type() + " Patient"
					+ df1.format(rowItem.getAptdate()) + " for " + "<b>" + "'"
					+ rowItem.getReason() + "'" + "</b>"));

		} else {
			final SimpleDateFormat df = new SimpleDateFormat(
					"' @ 'hh:mm a' on 'EEE, MMM dd', 'yyyy");
			PatientShow p = new PatientShow();
			try {
				if (EzApp.patientShowDao
						.queryBuilder()
						.where(Properties.P_id.eq(rowItem.getPid()),
								Properties.Pf_id.eq(rowItem.getPfid())).count() > 0) {
					p = (EzApp.patientShowDao
							.queryBuilder()
							.where(Properties.P_id.eq(rowItem.getPid()),
									Properties.Pf_id.eq(rowItem.getPfid()))
							.list().get(0));
				}

			} catch (Exception e) {
				Log.e("", e);
			}
			holder.txtConfirmed.setText(Html.fromHtml(p.getPfn() + " "
					+ p.getPln() + ", " + p.getAge() + "/" + p.getGender()
					+ ", " + p.getP_type() + " Patient"
					+ df.format(rowItem.getAptdate()) + " for " + "<b>" + "'"
					+ rowItem.getReason() + "'" + "</b>"));
		}
		holder.txtConfirmed.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(final View v) {
				if (holder.rlActions.getVisibility() == View.GONE) {
					holder.rlActions.setVisibility(View.VISIBLE);
				} else {
					holder.rlActions.setVisibility(View.GONE);
				}

			}
		});

		holder.btnSendMessage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(final View v) {
				FlurryAgent
						.logEvent("LabsAppointmentAdapter - Send Message Button Clicked");
				MessageController.showSendMessageDialog(context,
						rowItem.getBkid(), rowItem.getPid(), rowItem.getPfid(),
						rowItem.getAptdate(), "booking");
			}
		});

		holder.btnCheckin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(final View v) {
				FlurryAgent
						.logEvent("LabsAppointmentAdapter - Checkin Button Clicked");
				Intent intent;
				intent = new Intent(context, LabsOrderCreateActivity.class);
				intent.putExtra("bkid", rowItem.getBkid());
				context.startActivity(intent);
			}
		});

		holder.btnReject.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(final View v) {
				FlurryAgent
						.logEvent("LabsAppointmentAdapter - Reject Button Clicked");
				LabsAppointmentController.showRejectDialog(rowItem, context);
			}
		});

		holder.btnOrder.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(final View v) {
				FlurryAgent
						.logEvent("LabsAppointmentAdapter - Order Details Button Clicked");
				Intent intent;
				intent = new Intent(context, LabsOrderDetailsActivity.class);
				intent.putExtra("bkid", rowItem.getBkid());
				context.startActivity(intent);
			}
		});

		holder.btnReSchedule.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(final View v) {
				FlurryAgent
						.logEvent("LabsAppointmentAdapter - ReSchedule Button Clicked");
				final Dialog loaDialog = Util.showLoadDialog(context);
				PatientController.getPatient(rowItem.getPid(), context,
						new OnResponsePatient() {

							@Override
							public void onResponseListner(Patient response) {
								loaDialog.dismiss();
								// rowItem.setType("reschedule");
								// DashboardActivity.arrScheduledPatients.add(rowItem);
								final Intent intent = new Intent(context,
										SheduleActivityNew.class);
								intent.putExtra("type", "reschedule");
								intent.putExtra("bkid", rowItem.getBkid());
								context.startActivity(intent);

							}
						});

			}
		});

		holder.btnFollowUp.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				FlurryAgent
						.logEvent("LabsAppointmentAdapter - Followup Button Clicked");
				final Dialog loaDialog = Util.showLoadDialog(context);
				PatientController.getPatient(rowItem.getPid(), context,
						new OnResponsePatient() {

							@Override
							public void onResponseListner(Patient response) {
								loaDialog.dismiss();
								final Intent intent = new Intent(context,
										SheduleActivityNew.class);
								intent.putExtra("type", "followup");
								intent.putExtra("bkid", rowItem.getBkid());
								context.startActivity(intent);

							}
						});

			}
		});

		holder.btnNew.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				FlurryAgent
						.logEvent("LabsAppointmentAdapter - New (Appointment) Button Clicked");
				final Dialog loaDialog = Util.showLoadDialog(context);
				PatientController.getPatient(rowItem.getPid(), context,
						new OnResponsePatient() {

							@Override
							public void onResponseListner(Patient response) {
								loaDialog.dismiss();
								final Intent intent = new Intent(context,
										SheduleActivityNew.class);
								intent.putExtra("pid", rowItem.getPid());
								intent.putExtra("fid", rowItem.getPfid());
								intent.putExtra("type", "schedule");
								context.startActivity(intent);
							}
						});
			}
		});

		holder.btnAccept.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				LabsAppointmentController.showAcceptDialog(rowItem, context);

			}
		});

		return row;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	private void showViewDialog(final LabAppointment rowItem) {
		final Dialog dialog = new Dialog(context);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_view);
		dialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));

		Date currentDate = Calendar.getInstance().getTime();
		currentDate.setHours(0);
		currentDate.setMinutes(0);
		currentDate.setSeconds(0);
		Date tomorrowDate = new Date(currentDate.getTime() + 24 * 60 * 60000
				- 60000);
		if (rowItem.getAptdate().compareTo(currentDate) > 0
				&& rowItem.getAptdate().compareTo(tomorrowDate) < 0) {
			final SimpleDateFormat df1 = new SimpleDateFormat(
					"' @ 'hh:mm a' Today '");
			try {
				if (EzApp.patientShowDao
						.queryBuilder()
						.where(Properties.P_id.eq(rowItem.getPid()),
								Properties.Pf_id.eq(rowItem.getPfid())).count() > 0) {
					PatientShow p = (EzApp.patientShowDao
							.queryBuilder()
							.where(Properties.P_id.eq(rowItem.getPid()),
									Properties.Pf_id.eq(rowItem.getPfid()))
							.list().get(0));
					((TextView) dialog.findViewById(R.id.txt_desc))
							.setText(Html.fromHtml(p.getPfn() + " "
									+ p.getPln() + ", " + p.getAge() + "/"
									+ p.getGender() + ", " + p.getP_type()
									+ " Patient"
									+ df1.format(rowItem.getAptdate())
									+ " for " + "<b>" + "'"
									+ rowItem.getReason() + "'" + "</b>"));
				}

			} catch (Exception e) {
				Log.e("", e);
			}

		} else {
			final SimpleDateFormat df = new SimpleDateFormat(
					"' @ 'hh:mm a' on 'EEE, MMM dd', 'yyyy");
			try {
				if (EzApp.patientShowDao
						.queryBuilder()
						.where(Properties.P_id.eq(rowItem.getPid()),
								Properties.Pf_id.eq(rowItem.getPfid())).count() > 0) {
					PatientShow p = (EzApp.patientShowDao
							.queryBuilder()
							.where(Properties.P_id.eq(rowItem.getPid()),
									Properties.Pf_id.eq(rowItem.getPfid()))
							.list().get(0));
					((TextView) dialog.findViewById(R.id.txt_desc))
							.setText(Html.fromHtml(p.getPfn() + " "
									+ p.getPln() + ", " + p.getAge() + "/"
									+ p.getGender() + ", " + p.getP_type()
									+ " Patient"
									+ df.format(rowItem.getAptdate()) + " for "
									+ "<b>" + "'" + rowItem.getReason() + "'"
									+ "</b>"));
				}

			} catch (Exception e) {
				Log.e("", e);
			}

		}

		((TextView) dialog.findViewById(R.id.txt_reason_1)).setText(": "
				+ rowItem.getReason());
		dialog.setCancelable(false);
		dialog.findViewById(R.id.btn_close).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(final View v) {
						dialog.dismiss();

					}
				});

		dialog.show();

	}

}