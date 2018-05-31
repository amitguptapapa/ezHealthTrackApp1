package com.ezhealthtrack.activity;

import java.util.Calendar;
import java.util.Date;

import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;

import com.ezhealthtrack.R;
import com.ezhealthtrack.dialogs.EzDialog;
import com.ezhealthtrack.fragments.DatePickerDialogFragment;
import com.ezhealthtrack.one.EzUtils;
import com.ezhealthtrack.util.EzActivity;

public class ScheduleActivityDemo extends EzActivity implements
		OnDateSetListener {

	private Calendar cal;
	private Date mDate;
	View mButtonsBar;

	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shedule_demo);
		this.setDisplayHomeAsUpEnabled(true);

		// mButtonsBar = findViewById(R.id.id_buttons_bar);
		// mButtonsBar.setVisibility(View.GONE);

		// set orientation for non-large devices
		if (!EzUtils.getDeviceSize(this).equals(EzUtils.EZ_SCREEN_LARGE)) {
			mScreenRotation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
		}

		// View Slots
		// LinearLayout layout = (LinearLayout) findViewById(R.id.ll_buttons);
		// LinearLayout row = new LinearLayout(this);
		// TextView txt = new TextView(this);
		// row.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
		// LayoutParams.WRAP_CONTENT));
		// txt.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
		// txt.setBackgroundColor(Color.parseColor("#33B5E5"));
		// txt.setGravity(Gravity.CENTER);
		// txt.setPadding(10, 10, 10, 10);
		// txt.setTextColor(Color.WHITE);
		// txt.setText("07:00 PM");
		// row.addView(txt);
		//
		// for (int i = 0; i <= 11; i++) {
		// Button btnView = new Button(this);
		// btnView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
		// LayoutParams.WRAP_CONTENT));
		// String s = "07:";
		// btnView.setText(s + (i + 5));
		// btnView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
		// btnView.setBackgroundColor(Color.parseColor("#F1F7FF"));
		// btnView.setGravity(Gravity.CENTER);
		// btnView.setPadding(10, 10, 10, 10);
		// btnView.setTextColor(Color.BLACK);
		// btnView.setClickable(true);
		//
		// row.addView(btnView);
		//
		// btnView.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// v.setBackgroundColor(Color.parseColor("#83B6FF"));
		// showScheduleDialog();
		// }
		// });
		// }
		// layout.addView(row);
		//
		// tableHeader = (TableLayout) findViewById(R.id.table_header);
		// table = (TableLayout) findViewById(R.id.table);
		//
		// tableHeader.setStretchAllColumns(true);
		// table.setStretchAllColumns(true);
		// createTable();

		cal = Calendar.getInstance();
	}

	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		// Inflate the menu items for use in the action bar
		final MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.schedule, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		Intent intent;
		switch (item.getItemId()) {
		case R.id.action_month:
			final Bundle b = new Bundle();
			b.putInt(DatePickerDialogFragment.YEAR, cal.get(Calendar.YEAR));
			b.putInt(DatePickerDialogFragment.MONTH, cal.get(Calendar.MONTH));
			b.putInt(DatePickerDialogFragment.DATE,
					cal.get(Calendar.DAY_OF_MONTH));
			final DialogFragment picker = new DatePickerDialogFragment();
			picker.setArguments(b);
			picker.show(getSupportFragmentManager(), "fragment_date_picker");
			return true;

		case R.id.action_day:
			intent = new Intent(ScheduleActivityDemo.this,
					ScheduleActivityDemo_Daily.class);
			startActivity(intent);
			finish();
			return true;

		case R.id.action_info:
			showInfo();
			return true;

			// Temporarily Disabled
			// case R.id.action_week:
			// intent = new Intent(ScheduleActivityDemo.this,
			// ScheduleActivityDemo_Weekly.class);
			// startActivity(intent);
			// finish();
			// return true;

		case android.R.id.home:
			finish();
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onDateSet(final DatePicker view, final int year,
			final int monthOfYear, final int dayOfMonth) {
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, monthOfYear);
		cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
		mDate = cal.getTime();

		Intent intent = new Intent(ScheduleActivityDemo.this,
				ScheduleActivityDemo_Daily.class);
		intent.putExtra("date", mDate);
		startActivity(intent);
		finish();
		// getScheduleData(date);
	}

	private void showInfo() {
		final Dialog dialog = EzDialog.getDialog(this, R.layout.dialog_info,
				"Info - Color codes");
		dialog.show();
	}

	// private void createTable() {
	// final TableRow.LayoutParams params = new TableRow.LayoutParams(0,
	// android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
	// params.setMargins(1, 1, 1, 1);
	// final TableRow trHeader = new TableRow(this);
	// final TextView timeHeader = new TextView(this);
	// timeHeader.setText("Time");
	// timeHeader.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
	// timeHeader.setBackgroundColor(Color.parseColor("#83B6FF"));
	// timeHeader.setGravity(Gravity.CENTER);
	// timeHeader.setPadding(5, 10, 5, 10);
	// timeHeader.setTextColor(Color.BLACK);
	// timeHeader.setLayoutParams(params);
	// timeHeader.setWidth(50);
	// trHeader.addView(timeHeader);
	//
	// for (int i = 0; i < 7; i++) {
	// final TextView timeHeader1 = new TextView(this);
	// cal.setTime(date);
	// cal.add(Calendar.DATE, i);
	// final Date newDate = cal.getTime();
	// timeHeader1.setText(EzApp.sdfeeemmmddyy.format(newDate));
	// timeHeader1.setBackgroundColor(Color.parseColor("#83B6FF"));
	// timeHeader1.setGravity(Gravity.CENTER);
	// timeHeader1.setPadding(5, 10, 5, 10);
	// timeHeader1.setTextColor(Color.BLACK);
	// timeHeader1.setLayoutParams(params);
	// timeHeader1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
	// trHeader.addView(timeHeader1);
	// }
	// tableHeader.addView(trHeader);
	//
	// for (int i = 0; i < 24; i++) {
	// TableRow tr = new TableRow(this);
	// final TextView timeBase = new TextView(this);
	//
	// // Set Default Calendar | reset hour, minutes, seconds and
	// // milliseconds
	// cal.set(Calendar.HOUR_OF_DAY, 0);
	// cal.set(Calendar.MINUTE, 0);
	// cal.set(Calendar.SECOND, 0);
	// cal.set(Calendar.MILLISECOND, 0);
	//
	// cal.add(Calendar.HOUR, i);
	// final Date newTime = cal.getTime();
	// timeBase.setText(EzApp.sdfTime.format(newTime));
	// timeBase.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
	// timeBase.setBackgroundColor(Color.parseColor("#F1F7FF"));
	// timeBase.setGravity(Gravity.CENTER);
	// timeBase.setPadding(10, 10, 10, 10);
	// timeBase.setTextColor(Color.BLACK);
	// timeBase.setHeight(100);
	// timeBase.setWidth(50);
	// timeBase.setLayoutParams(params);
	// // tr.setTag("" + (i + 1));
	// tr.addView(timeBase);
	// for (int j = 0; j < 7; j++) {
	// final TextView c1 = new TextView(this);
	// c1.setTag(0);
	// // c1.setText("5");
	// c1.setGravity(Gravity.CENTER);
	// c1.setBackgroundColor(Color.parseColor("#ABA2FF"));
	// c1.setPadding(10, 10, 10, 10);
	// c1.setTextColor(Color.BLACK);
	// c1.setLayoutParams(params);
	// c1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
	// c1.setClickable(true);
	// c1.setHeight(100);
	// tr.addView(c1);
	//
	// c1.setOnClickListener(new OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	// v.setBackgroundColor(Color.parseColor("#83B6FF"));
	// mButtonsBar.setVisibility(View.VISIBLE);
	//
	// // Integer index = (Integer) v.getTag();
	// // mButtonsBar.setVisibility(View.VISIBLE);
	// // selectRow(index);
	// }
	// });
	// }
	// table.addView(tr);
	// }
	// }
	//
	// private void selectRow(int index) {
	// if (index != selectedIndex) {
	// if (selectedIndex >= 0) {
	// deselectRow(index);
	// }
	// TableRow tr = (TableRow) table.getChildAt(index);
	// TextView txt = (TextView) tr.getChildAt(index);
	// txt.setBackgroundColor(Color.parseColor("#83B6FF"));
	// }
	// }
	//
	// private void deselectRow(int index) {
	// if (index >= 0) {
	// TableRow tr = (TableRow) table.getChildAt(index);
	// TextView txt = (TextView) tr.getChildAt(index);
	// txt.setBackgroundColor(Color.parseColor("F1F7FF"));
	// }
	// }
	//
	// private void setTable() {
	// for (int i = 0; i < 24; i++) {
	// final TableRow tr = (TableRow) table.findViewWithTag("" + (i + 1));
	// for (int j = 0; j < 7; j++) {
	// final TextView c1 = (TextView) (tr.findViewWithTag("c" + j));
	// c1.setClickable(true);
	// c1.setOnClickListener(new OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	//
	// }
	// });
	// }
	// }
	// }
	//
	//
	//
	// private void showScheduleDialog() {
	// if (EzApp.sharedPref.getString(Constants.USER_TYPE, "D").equals("D")) {
	// final Dialog dialog = EzDialog
	// .getDialog(this, R.layout.dialog_shedule_appointment,
	// "Schedule Appointment");
	// dialog.getWindow().setBackgroundDrawable(
	// new ColorDrawable(android.graphics.Color.TRANSPARENT));
	// final Appointment pm = new Appointment();
	// Date currentDate = Calendar.getInstance().getTime();
	// currentDate.setHours(0);
	// currentDate.setMinutes(0);
	// currentDate.setSeconds(0);
	// Date tomorrowDate = new Date(currentDate.getTime() + 24 * 60
	// * 60000 - 60000);
	// final SimpleDateFormat sd;
	// if (date.after(currentDate) && date.before(tomorrowDate)) {
	// sd = new SimpleDateFormat("' @ 'hh:mm a' Today '");
	// } else {
	// sd = new SimpleDateFormat(
	// "' @ 'hh:mm a' on 'EEE, MMM dd', 'yyyy");
	// }
	//
	// ((TextView) dialog.findViewById(R.id.txt_desc)).setText("NOIDA");
	//
	// final Button button = (Button) dialog
	// .findViewById(R.id.btn_schedule);
	// button.setText("Schedule");
	// button.setOnClickListener(new OnClickListener() {
	//
	// @Override
	// public void onClick(final View v) {
	// // Temporarily implemented
	// Util.AlertdialogWithFinish(ScheduleActivityDemo.this,
	// "Appointment Scheduled successfully");
	//
	// // if (Util.isEmptyString(((EditText) dialog
	// // .findViewById(R.id.edit_reason)).getText()
	// // .toString())) {
	// // Util.Alertdialog(ScheduleActivityDemo.this,
	// // "Enter reason for visit");
	// // } else {
	// // pm.aptDate = date;
	// // pm.setType("booked");
	// // pm.setNote(((EditText) dialog
	// // .findViewById(R.id.edit_reason)).getText()
	// // .toString());
	// // DashboardActivity.arrScheduledPatients.add(pm);
	// // // TODO DashboardActivity.arrConfirmedPatients.add(0,
	// // // pm);
	// // final TextView txt = (TextView) table
	// // .findViewWithTag("");
	// // for (PatientShow p : DashboardActivity.arrPatientShow) {
	// // if (p.getPId().equalsIgnoreCase(pm.getPid())
	// // && p.getPfId().equalsIgnoreCase(
	// // pm.getPfId()))
	// // txt.setText(p.getPfn() + " " + p.getPln());
	// // }
	// // txt.setBackgroundColor(Color.parseColor("#F8C65B"));
	// // txt.setTextColor(Color.WHITE);
	// //
	// // dialog.dismiss();
	// // }
	//
	// }
	// });
	// dialog.setCancelable(false);
	// dialog.show();
	// }
	// }
	//
	//
}
