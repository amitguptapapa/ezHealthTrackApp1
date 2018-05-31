package com.ezhealthtrack.views;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Handler;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

import com.ezhealthtrack.EzApp;
import com.ezhealthtrack.R;
import com.ezhealthtrack.activity.DashboardActivity;
import com.ezhealthtrack.model.AppointmentCalenderModel;
import com.ezhealthtrack.model.PatientShow;
import com.ezhealthtrack.util.Log;
import com.ezhealthtrack.util.Util;
import com.google.gson.Gson;

public class AppointmentCalender {
	private TableLayout table;
	private TableLayout tableHeader;
	private Date date;
	private Calendar cal;
	private Activity activity;
	private int userType;
	public static final int USER_DOC = 1;
	public static final int USER_LT = 1;

	public interface OnCellClicked {
		public void onOnCellClickedListner(Date date,String slot);
	}

	public AppointmentCalender(TableLayout table, TableLayout tableHeader,
			Date date, Calendar cal, Activity activity, int userType,
			OnCellClicked onCellClicked) {
		this.table = table;
		this.tableHeader = tableHeader;
		this.date = date;
		this.cal = cal;
		this.activity = activity;
		this.userType = userType;
		table.removeAllViews();
		tableHeader.removeAllViews();
		createTable(onCellClicked);
	}

	private void createTable(final OnCellClicked onCellClicked) {
		final LayoutParams params = new LayoutParams(0,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		params.setMargins(1, 1, 1, 1);
		final TableRow trHeader = new TableRow(activity);
		final TextView timeHeader = new TextView(activity);
		timeHeader.setText("Time");
		timeHeader.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
		timeHeader.setBackgroundColor(Color.parseColor("#83B6FF"));
		timeHeader.setGravity(Gravity.CENTER);
		timeHeader.setPadding(5, 10, 5, 10);
		timeHeader.setTextColor(Color.BLACK);
		timeHeader.setLayoutParams(params);
		trHeader.addView(timeHeader);

		for (int i = 0; i < 7; i++) {
			final TextView timeHeader1 = new TextView(activity);
			cal.setTime(date);
			cal.add(Calendar.DATE, i);
			final Date newDate = cal.getTime();
			timeHeader1.setText(EzApp.sdfeeemmmddyy
					.format(newDate));
			timeHeader1.setBackgroundColor(Color.parseColor("#83B6FF"));
			timeHeader1.setGravity(Gravity.CENTER);
			timeHeader1.setPadding(5, 10, 5, 10);
			timeHeader1.setTextColor(Color.BLACK);
			timeHeader1.setLayoutParams(params);
			timeHeader1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
			trHeader.addView(timeHeader1);
		}
		tableHeader.addView(trHeader);

		for (int i = 0; i < 96; i++) {
			final TableRow tr = new TableRow(activity);
			tr.setTag("" + (i + 1));
			for (int j = 0; j < 8; j++) {
				final TextView c1 = new TextView(activity);
				c1.setTag("c" + j);
				c1.setGravity(Gravity.CENTER);
				c1.setBackgroundColor(Color.parseColor("#F1F7FF"));
				c1.setPadding(10, 10, 10, 10);
				c1.setTextColor(Color.BLACK);
				c1.setLayoutParams(params);
				c1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
				tr.addView(c1);
			}

			table.addView(tr);
		}
		final Dialog loaddialog = Util.showLoadDialog(activity);
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				setTable(onCellClicked);
				loaddialog.dismiss();
			}
		}, 100);

	}

	private void setTable(final OnCellClicked onCellClicked) {
		for (int i = 0; i < 96; i++) {
			final TableRow tr = (TableRow) table.findViewWithTag("" + (i + 1));
			for (int j = 0; j < 8; j++) {
				final TextView c1 = (TextView) (tr.findViewWithTag("c" + j));
				int hr = (i / 4);
				final int min = (i * 15) % 60;
				cal.setTime(date);
				cal.add(Calendar.DATE, j - 1);
				cal.set(Calendar.HOUR_OF_DAY, hr);
				cal.set(Calendar.MINUTE, min);
				cal.set(Calendar.SECOND, 0);
				final Date newDate = cal.getTime();
				if (j == 0) {
					String amorpm = "AM";
					if (hr > 12) {
						hr = hr - 12;
						amorpm = "PM";
					} else if (hr == 12) {
						amorpm = "PM";
					}
					if (hr == 0) {
						hr = 12;
					}
					if (min < 10) {
						c1.setText("" + hr + ":0" + min + amorpm);
					} else {
						c1.setText("" + hr + ":" + min + amorpm);
					}
				} else {
					c1.setTag(newDate.toString());
					c1.setText("");
					c1.setBackgroundColor(Color.parseColor("#FFFFFF"));
					if (newDate.compareTo(new Date()) > 0) {
						c1.setBackgroundColor(Color.parseColor("#ABA2FF"));
						c1.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								onCellClicked.onOnCellClickedListner(newDate,tr.getTag().toString());

							}
						});
					}
				}
			}
		}
		focusOnView();
		activity.findViewById(R.id.loading_progressbar)
				.setVisibility(View.GONE);
	}

	public void setAppointments(
			ArrayList<AppointmentCalenderModel> arrAppointmentCalenderModel) {
		for (final AppointmentCalenderModel pm : arrAppointmentCalenderModel) {
			final Date date = pm.getAptDate();
			final TextView txt = (TextView) table.findViewWithTag(date
					.toString());
			if (txt != null) {
				for (PatientShow p : DashboardActivity.arrPatientShow) {
					if (p.getPId().equalsIgnoreCase(pm.getPid()))
						txt.setText(p.getPfn());
				}
				if ((pm.getType()).equals("tentative")) {
					txt.setBackgroundColor(Color.parseColor("#0B600B"));
					txt.setTextColor(Color.WHITE);
				} else if ((pm.getType()).equals("booked")) {
					txt.setBackgroundColor(Color.parseColor("#F8C65B"));
					txt.setTextColor(Color.WHITE);
				} else if ((pm.getType()).equals("reschedule")) {
					// aptReschedule = pm;
					txt.setBackgroundColor(Color.parseColor("#02ACEF"));
					txt.setTextColor(Color.WHITE);
				} else if ((pm.getType().toLowerCase()).contains("followup")) {
					Log.i("", new Gson().toJson(pm));
					// aptReschedule = pm;
					txt.setBackgroundColor(Color.parseColor("#F8C65B"));
					txt.setTextColor(Color.WHITE);
				} else if ((pm.getType().toLowerCase()).contains("deleted")) {
					if (pm.getAptDate().before(new Date()))
						txt.setBackgroundColor(Color.parseColor("#FFFFFF"));
					else {
						txt.setBackgroundColor(Color.parseColor("#ABA2FF"));
					}
					txt.setText("");

				}
			}

		}
	}
	
	private final void focusOnView(){
		final ScrollView sv = (ScrollView) activity.findViewById(R.id.sv_cal);
        new Handler().post(new Runnable() {
            @Override
            public void run() {
            	Date date = new Date();
            	final int min = (date.getMinutes()/15)*15;
            	cal.setTime(date);
				cal.set(Calendar.MINUTE, min);
				cal.set(Calendar.SECOND, 0);
				final Date newDate = cal.getTime();
				if(table.findViewWithTag(newDate.toString())!=null)
                sv.scrollTo(0,((View)(table.findViewWithTag(newDate.toString()).getParent())).getTop());
            }
        });
    }

}
