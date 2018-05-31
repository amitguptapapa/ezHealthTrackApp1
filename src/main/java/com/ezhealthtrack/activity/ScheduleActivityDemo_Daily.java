package com.ezhealthtrack.activity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.Dialog;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.ezhealthtrack.EzApp;
import com.ezhealthtrack.R;
import com.ezhealthtrack.dialogs.EzDialog;
import com.ezhealthtrack.greendao.Appointment;
import com.ezhealthtrack.one.EzUtils;
import com.ezhealthtrack.util.Constants;
import com.ezhealthtrack.util.Util;

public class ScheduleActivityDemo_Daily extends ScheduleActivityDemo {

	private TableLayout table;
	private TableLayout tableHeader;
	private static Date date;
	private static Calendar cal;
	View mButtonsBar;
	private int selectedIndex = -1;
	int slot_count = 288;
	Date mDate;

	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shedule_demo_daily);
		this.setDisplayHomeAsUpEnabled(true);

		// set orientation for non-large devices
		if (!EzUtils.getDeviceSize(this).equals(EzUtils.EZ_SCREEN_LARGE)) {
			mScreenRotation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
		}

		// set title
		this.setTitle("Daily Schedule");

		// get date from monthly schedule
		final Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			mDate = (Date) bundle.getSerializable("date");
		}

		tableHeader = (TableLayout) findViewById(R.id.table_header);
		table = (TableLayout) findViewById(R.id.table);

		tableHeader.setStretchAllColumns(true);
		table.setStretchAllColumns(true);
		cal = Calendar.getInstance();
		date = cal.getTime();
		createTable();
	}

	private void createTable() {
		final TableRow.LayoutParams params = new TableRow.LayoutParams(0,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		params.setMargins(1, 1, 1, 1);

		final TableRow trHeader = new TableRow(this);
		final TextView timeHeader = new TextView(this);

		// set first cell
		timeHeader.setText("Time");
		timeHeader.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
		timeHeader.setBackgroundColor(Color.parseColor("#35759B"));
		timeHeader.setGravity(Gravity.CENTER);
		timeHeader.setPadding(5, 10, 5, 10);
		timeHeader.setTextColor(Color.WHITE);
		timeHeader.setLayoutParams(params);
		trHeader.addView(timeHeader);

		// set second(date) cell
		TextView timeHeader1 = new TextView(this);
		cal.setTime(date);
		cal.add(Calendar.DATE, 0);
		final Date newDate = cal.getTime();

		if (mDate != null) {
			timeHeader1.setText(EzApp.sdfeeemmmddyy.format(mDate));
		} else {
			timeHeader1.setText(EzApp.sdfeeemmmddyy.format(newDate));
		}

		timeHeader1.setBackgroundColor(Color.parseColor("#35759B"));
		timeHeader1.setGravity(Gravity.CENTER);
		timeHeader1.setPadding(5, 10, 5, 10);
		timeHeader1.setTextColor(Color.WHITE);
		timeHeader1.setLayoutParams(params);
		timeHeader1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
		trHeader.addView(timeHeader1);

		tableHeader.addView(trHeader);

		// time specified increment
		for (int i = 0; i < slot_count; i += 5) {

			TableRow tr = new TableRow(this);
			TextView timeBase = new TextView(this);

			// Set Default Calendar | reset hour, minutes, seconds and
			// milliseconds
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);

			cal.add(Calendar.MINUTE, i);

			final Date newTime = cal.getTime();
			timeBase.setText(EzApp.sdfTime.format(newTime));
			timeBase.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
			timeBase.setBackgroundColor(Color.parseColor("#F1F7FF"));
			timeBase.setGravity(Gravity.CENTER);
			timeBase.setPadding(10, 10, 10, 10);
			timeBase.setTextColor(Color.BLACK);
			timeBase.setHeight(100);
			timeBase.setLayoutParams(params);
			// tr.setTag("" + (i + 1));
			tr.addView(timeBase);

			final TextView c1 = new TextView(this);
			c1.setTag(0);
			c1.setGravity(Gravity.CENTER);
			c1.setBackgroundColor(Color.parseColor("#D9EDF6"));
			c1.setPadding(5, 5, 5, 5);
			c1.setTextColor(Color.BLACK);
			c1.setLayoutParams(params);
			c1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
			c1.setClickable(true);
			c1.setHeight(100);
			tr.addView(c1);

			c1.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					v.setBackgroundColor(Color.parseColor("#35759B"));
					// Integer index = (Integer) v.getTag();
					// if (index != null) {
					// selectRow(index);
					// }
					showScheduleDialog();
				}

			});

			table.addView(tr);
		}
	}

	private void selectRow(int index) {
		if (index != selectedIndex) {
			if (selectedIndex >= 0) {
				deselectRow(selectedIndex);
			}
			TableRow tr = (TableRow) table.getChildAt(index);
			TextView txt = (TextView) tr.getChildAt(index);
			txt.setBackgroundColor(Color.parseColor("#83B6FF"));
			selectedIndex = index;
		}
	}

	private void deselectRow(int index) {
		if (index >= 0) {
			TableRow tr = (TableRow) table.getChildAt(index);
			TextView txt = (TextView) tr.getChildAt(index);
			txt.setBackgroundColor(Color.parseColor("F1F7FF"));
		}
	}

	private void setTable() {

	}

	private void showScheduleDialog() {
		if (EzApp.sharedPref.getString(Constants.USER_TYPE, "D").equals("D")) {
			final Dialog dialog = EzDialog
					.getDialog(this, R.layout.dialog_shedule_appointment,
							"Schedule Appointment");
			dialog.getWindow().setBackgroundDrawable(
					new ColorDrawable(android.graphics.Color.TRANSPARENT));
			final Appointment pm = new Appointment();
			Date currentDate = Calendar.getInstance().getTime();
			currentDate.setHours(0);
			currentDate.setMinutes(0);
			currentDate.setSeconds(0);
			Date tomorrowDate = new Date(currentDate.getTime() + 24 * 60
					* 60000 - 60000);
			final SimpleDateFormat sd;
			if (date.after(currentDate) && date.before(tomorrowDate)) {
				sd = new SimpleDateFormat("' @ 'hh:mm a' Today '");
			} else {
				sd = new SimpleDateFormat(
						"' @ 'hh:mm a' on 'EEE, MMM dd', 'yyyy");
			}

			((TextView) dialog.findViewById(R.id.txt_desc)).setText("NOIDA");

			final Button button = (Button) dialog
					.findViewById(R.id.btn_schedule);
			button.setText("Schedule");
			button.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(final View v) {
					// Temporarily implemented
					Util.AlertdialogWithFinish(ScheduleActivityDemo_Daily.this,
							"Appointment Scheduled successfully");

					// if (Util.isEmptyString(((EditText) dialog
					// .findViewById(R.id.edit_reason)).getText()
					// .toString())) {
					// Util.Alertdialog(ScheduleActivityDemo.this,
					// "Enter reason for visit");
					// } else {
					// pm.aptDate = date;
					// pm.setType("booked");
					// pm.setNote(((EditText) dialog
					// .findViewById(R.id.edit_reason)).getText()
					// .toString());
					// DashboardActivity.arrScheduledPatients.add(pm);
					// // TODO DashboardActivity.arrConfirmedPatients.add(0,
					// // pm);
					// final TextView txt = (TextView) table
					// .findViewWithTag("");
					// for (PatientShow p : DashboardActivity.arrPatientShow) {
					// if (p.getPId().equalsIgnoreCase(pm.getPid())
					// && p.getPfId().equalsIgnoreCase(
					// pm.getPfId()))
					// txt.setText(p.getPfn() + " " + p.getPln());
					// }
					// txt.setBackgroundColor(Color.parseColor("#F8C65B"));
					// txt.setTextColor(Color.WHITE);
					//
					// dialog.dismiss();
					// }

				}
			});
			dialog.setCancelable(false);
			dialog.show();
		}
	}
}
