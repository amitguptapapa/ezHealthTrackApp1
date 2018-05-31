package com.ezhealthtrack.labs.adapter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
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
import com.ezhealthtrack.labs.controller.LabsAppointmentController;
import com.ezhealthtrack.util.LazyListAdapter;
import com.ezhealthtrack.util.Log;
import com.ezhealthtrack.util.Util;
import com.flurry.android.FlurryAgent;

public class LabsNewTentativeAdapter extends LazyListAdapter<LabAppointment> {
	/* private view holder class */
	private static class ViewHolder {
		private TextView txtNewTentative;
		private RelativeLayout rlActions;
		private Button btnAccept;
		private Button btnReSchedule;
		private Button btnSendMessage;
		private Button btnReject;

	}

	Context context;

	int type;

	public LabsNewTentativeAdapter(final Context context, int type) {
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
			row = inflater.inflate(R.layout.labs_row_new_tentative, null);
			holder.rlActions = (RelativeLayout) row
					.findViewById(R.id.rl_actions);
			holder.txtNewTentative = (TextView) row
					.findViewById(R.id.txt_new_tentative);
			holder.btnAccept = (Button) row.findViewById(R.id.btn_accept);
			holder.btnReSchedule = (Button) row
					.findViewById(R.id.btn_reschedule);
			holder.btnSendMessage = (Button) row
					.findViewById(R.id.btn_sendmessage);
			holder.btnReject = (Button) row.findViewById(R.id.btn_reject);
			row.setTag(holder);
		} else {
			row = convertView;
		}
		final ViewHolder holder = (ViewHolder) row.getTag();

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
			holder.txtNewTentative.setText(Html.fromHtml(p.getPfn() + " "
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
			holder.txtNewTentative.setText(Html.fromHtml(p.getPfn() + " "
					+ p.getPln() + ", " + p.getAge() + "/" + p.getGender()
					+ ", " + p.getP_type() + " Patient"
					+ df.format(rowItem.getAptdate()) + " for " + "<b>" + "'"
					+ rowItem.getReason() + "'" + "</b>"));
		}
		holder.txtNewTentative.setOnClickListener(new OnClickListener() {

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
						.logEvent("LabsNewTentativeAdapter - Send Message Button Clicked");
				MessageController.showSendMessageDialog(context,
						rowItem.getBkid(), rowItem.getPid(), rowItem.getPfid(),
						rowItem.getAptdate(), "booking");
			}
		});

		holder.btnAccept.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(final View v) {
				FlurryAgent
						.logEvent("LabsNewTentativeAdapter - Accept Button Clicked");
				// Intent intent;
				// intent = new Intent(context, LabsOrderCreateActivity.class);
				// intent.putExtra("bkid", rowItem.getBkid());
				// context.startActivity(intent);
			}
		});

		holder.btnReject.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(final View v) {
				FlurryAgent
						.logEvent("LabsNewTentativeAdapter - Reject Button Clicked");
				LabsAppointmentController.showRejectDialog(rowItem, context);
			}
		});

		holder.btnReSchedule.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(final View v) {
				FlurryAgent
						.logEvent("LabsNewTentativeAdapter - ReSchedule Button Clicked");
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

		return row;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

}