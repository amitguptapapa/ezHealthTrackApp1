package com.ezhealthtrack.fragments;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ezhealthrack.api.APIs;
import com.ezhealthtrack.EzApp;
import com.ezhealthtrack.R;
import com.ezhealthtrack.activity.DashboardActivity;
import com.ezhealthtrack.adapter.VacationAdapter;
import com.ezhealthtrack.model.DayPlanModel;
import com.ezhealthtrack.model.VacationModel;
import com.ezhealthtrack.util.Constants;
import com.ezhealthtrack.util.Log;
import com.ezhealthtrack.util.Util;
import com.google.gson.Gson;

public class SchedulePlanFragment extends Fragment implements OnClickListener,
		OnCheckedChangeListener {
	private TextView txtStartDate;
	private TextView txtEndDate;
	private TextView btnAddVacation;
	private TextView txtMondayFrom;
	private TextView txtMondayTo;
	private CheckBox cbMonday;
	private TextView txtMondayLout;
	private TextView txtMondayLin;
	private TextView txtTuesdayFrom;
	private TextView txtTuesdayTo;
	private CheckBox cbTuesday;
	private TextView txtTuesdayLout;
	private TextView txtTuesdayLin;
	private TextView txtWednesdayFrom;
	private TextView txtWednesdayTo;
	private CheckBox cbWednesday;
	private TextView txtWednesdayLout;
	private TextView txtWednesdayLin;
	private TextView txtThursdayFrom;
	private TextView txtThursdayTo;
	private CheckBox cbThursday;
	private TextView txtThursdayLout;
	private TextView txtThursdayLin;
	private TextView txtFridayFrom;
	private TextView txtFridayTo;
	private CheckBox cbFriday;
	private TextView txtFridayLin;
	private TextView txtFridayLout;
	private TextView txtSaturdayFrom;
	private TextView txtSaturdayTo;
	private CheckBox cbSaturday;
	private TextView txtSaturdayLout;
	private TextView txtSaturdayLin;
	private TextView txtSundayFrom;
	private TextView txtSundayTo;
	private CheckBox cbSunday;
	private TextView txtSundayLout;
	private TextView txtSundayLin;
	private TextView btnSubmit;
	private ListView listVacation;
	private VacationAdapter adapterVacation;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_schedule_plan, container,
				false);
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		txtStartDate = (TextView) getActivity().findViewById(
				R.id.txt_start_date);
		txtStartDate.setOnClickListener(this);
		txtEndDate = (TextView) getActivity().findViewById(R.id.txt_end_date);
		txtEndDate.setOnClickListener(this);

		txtMondayFrom = (TextView) getActivity().findViewById(
				R.id.txt_monday_from);
		txtMondayFrom.setOnClickListener(this);
		txtMondayFrom.setText(DashboardActivity.dayPlan[1].getStartTime());
		txtMondayTo = (TextView) getActivity().findViewById(R.id.txt_monday_to);
		txtMondayTo.setOnClickListener(this);
		txtMondayTo.setText(DashboardActivity.dayPlan[1].getEndTime());
		cbMonday = (CheckBox) getActivity().findViewById(R.id.cb_monday);
		cbMonday.setChecked(DashboardActivity.dayPlan[1].isOff());
		cbMonday.setOnCheckedChangeListener(this);
		txtMondayLout = (TextView) getActivity().findViewById(
				R.id.txt_monday_lout);
		txtMondayLout.setOnClickListener(this);
		txtMondayLout.setText(DashboardActivity.dayPlan[1].getLout());
		txtMondayLin = (TextView) getActivity().findViewById(
				R.id.txt_monday_lin);
		txtMondayLin.setOnClickListener(this);
		txtMondayLin.setText(DashboardActivity.dayPlan[1].getLin());

		txtTuesdayFrom = (TextView) getActivity().findViewById(
				R.id.txt_tuesday_from);
		txtTuesdayFrom.setOnClickListener(this);
		txtTuesdayFrom.setText(DashboardActivity.dayPlan[2].getStartTime());
		txtTuesdayTo = (TextView) getActivity().findViewById(
				R.id.txt_tuesday_to);
		txtTuesdayTo.setOnClickListener(this);
		txtTuesdayTo.setText(DashboardActivity.dayPlan[2].getEndTime());
		cbTuesday = (CheckBox) getActivity().findViewById(R.id.cb_tuesday);
		cbTuesday.setChecked(DashboardActivity.dayPlan[2].isOff());
		cbTuesday.setOnCheckedChangeListener(this);
		txtTuesdayLout = (TextView) getActivity().findViewById(
				R.id.txt_tuesday_lout);
		txtTuesdayLout.setOnClickListener(this);
		txtTuesdayLout.setText(DashboardActivity.dayPlan[2].getLout());
		txtTuesdayLin = (TextView) getActivity().findViewById(
				R.id.txt_tuesday_lin);
		txtTuesdayLin.setOnClickListener(this);
		txtTuesdayLin.setText(DashboardActivity.dayPlan[2].getLin());

		txtWednesdayFrom = (TextView) getActivity().findViewById(
				R.id.txt_wednesday_from);
		txtWednesdayFrom.setOnClickListener(this);
		txtWednesdayFrom.setText(DashboardActivity.dayPlan[3].getStartTime());
		txtWednesdayTo = (TextView) getActivity().findViewById(
				R.id.txt_wednesday_to);
		txtWednesdayTo.setOnClickListener(this);
		txtWednesdayTo.setText(DashboardActivity.dayPlan[3].getEndTime());
		cbWednesday = (CheckBox) getActivity().findViewById(R.id.cb_wednesday);
		cbWednesday.setChecked(DashboardActivity.dayPlan[3].isOff());
		cbWednesday.setOnCheckedChangeListener(this);
		txtWednesdayLout = (TextView) getActivity().findViewById(
				R.id.txt_wednesday_lout);
		txtWednesdayLout.setOnClickListener(this);
		txtWednesdayLout.setText(DashboardActivity.dayPlan[3].getLout());
		txtWednesdayLin = (TextView) getActivity().findViewById(
				R.id.txt_wednesday_lin);
		txtWednesdayLin.setOnClickListener(this);
		txtWednesdayLin.setText(DashboardActivity.dayPlan[3].getLin());

		txtThursdayFrom = (TextView) getActivity().findViewById(
				R.id.txt_thursday_from);
		txtThursdayFrom.setOnClickListener(this);
		txtThursdayFrom.setText(DashboardActivity.dayPlan[4].getStartTime());
		txtThursdayTo = (TextView) getActivity().findViewById(
				R.id.txt_thursday_to);
		txtThursdayTo.setOnClickListener(this);
		txtThursdayTo.setText(DashboardActivity.dayPlan[4].getEndTime());
		cbThursday = (CheckBox) getActivity().findViewById(R.id.cb_thursday);
		cbThursday.setChecked(DashboardActivity.dayPlan[4].isOff());
		cbThursday.setOnCheckedChangeListener(this);
		txtThursdayLout = (TextView) getActivity().findViewById(
				R.id.txt_thursday_lout);
		txtThursdayLout.setOnClickListener(this);
		txtThursdayLout.setText(DashboardActivity.dayPlan[4].getLout());
		txtThursdayLin = (TextView) getActivity().findViewById(
				R.id.txt_thursday_lin);
		txtThursdayLin.setOnClickListener(this);
		txtThursdayLin.setText(DashboardActivity.dayPlan[4].getLin());

		txtFridayFrom = (TextView) getActivity().findViewById(
				R.id.txt_friday_from);
		txtFridayFrom.setOnClickListener(this);
		txtFridayFrom.setText(DashboardActivity.dayPlan[5].getStartTime());
		txtFridayTo = (TextView) getActivity().findViewById(R.id.txt_friday_to);
		txtFridayTo.setOnClickListener(this);
		txtFridayTo.setText(DashboardActivity.dayPlan[5].getEndTime());
		cbFriday = (CheckBox) getActivity().findViewById(R.id.cb_friday);
		cbFriday.setChecked(DashboardActivity.dayPlan[5].isOff());
		cbFriday.setOnCheckedChangeListener(this);
		txtFridayLout = (TextView) getActivity().findViewById(
				R.id.txt_friday_lout);
		txtFridayLout.setOnClickListener(this);
		txtFridayLout.setText(DashboardActivity.dayPlan[5].getLout());
		txtFridayLin = (TextView) getActivity().findViewById(
				R.id.txt_friday_lin);
		txtFridayLin.setOnClickListener(this);
		txtFridayLin.setText(DashboardActivity.dayPlan[5].getLin());

		txtSaturdayFrom = (TextView) getActivity().findViewById(
				R.id.txt_saturday_from);
		txtSaturdayFrom.setOnClickListener(this);
		txtSaturdayFrom.setText(DashboardActivity.dayPlan[6].getStartTime());
		txtSaturdayTo = (TextView) getActivity().findViewById(
				R.id.txt_saturday_to);
		txtSaturdayTo.setOnClickListener(this);
		txtSaturdayTo.setText(DashboardActivity.dayPlan[6].getEndTime());
		cbSaturday = (CheckBox) getActivity().findViewById(R.id.cb_saturday);
		cbSaturday.setChecked(DashboardActivity.dayPlan[6].isOff());
		cbSaturday.setOnCheckedChangeListener(this);
		txtSaturdayLout = (TextView) getActivity().findViewById(
				R.id.txt_saturday_lout);
		txtSaturdayLout.setOnClickListener(this);
		txtSaturdayLout.setText(DashboardActivity.dayPlan[6].getLout());
		txtSaturdayLin = (TextView) getActivity().findViewById(
				R.id.txt_saturday_lin);
		txtSaturdayLin.setOnClickListener(this);
		txtSaturdayLin.setText(DashboardActivity.dayPlan[6].getLin());

		txtSundayFrom = (TextView) getActivity().findViewById(
				R.id.txt_sunday_from);
		txtSundayFrom.setOnClickListener(this);
		txtSundayFrom.setText(DashboardActivity.dayPlan[0].getStartTime());
		txtSundayTo = (TextView) getActivity().findViewById(R.id.txt_sunday_to);
		txtSundayTo.setOnClickListener(this);
		txtSundayTo.setText(DashboardActivity.dayPlan[0].getEndTime());
		cbSunday = (CheckBox) getActivity().findViewById(R.id.cb_sunday);
		cbSunday.setChecked(DashboardActivity.dayPlan[0].isOff());
		cbSunday.setOnCheckedChangeListener(this);
		txtSundayLout = (TextView) getActivity().findViewById(
				R.id.txt_sunday_lout);
		txtSundayLout.setOnClickListener(this);
		txtSundayLout.setText(DashboardActivity.dayPlan[0].getLout());
		txtSundayLin = (TextView) getActivity().findViewById(
				R.id.txt_sunday_lin);
		txtSundayLin.setOnClickListener(this);
		txtSundayLin.setText(DashboardActivity.dayPlan[0].getLin());

		btnSubmit = (TextView) getActivity().findViewById(R.id.btn_submit);
		btnSubmit.setOnClickListener(this);

		btnAddVacation = (TextView) getActivity().findViewById(
				R.id.btn_add_vacation);
		btnAddVacation.setOnClickListener(this);
		listVacation = (ListView) getActivity()
				.findViewById(R.id.list_vacation);
		adapterVacation = new VacationAdapter(getActivity(),
				R.layout.row_vacation, DashboardActivity.arrVacations);
		listVacation.setAdapter(adapterVacation);

	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.txt_start_date:
			showCalender(txtStartDate);
			break;

		case R.id.txt_end_date:
			showCalender(txtEndDate);
			break;

		case R.id.txt_monday_from:
			showTimePicker(txtMondayFrom);
			break;

		case R.id.txt_monday_to:
			showTimePicker(txtMondayTo);
			break;

		case R.id.txt_monday_lout:
			showTimePicker(txtMondayLout);
			break;

		case R.id.txt_monday_lin:
			showTimePicker(txtMondayLin);
			break;

		case R.id.txt_tuesday_from:
			showTimePicker(txtTuesdayFrom);
			break;

		case R.id.txt_tuesday_lin:
			showTimePicker(txtTuesdayLin);
			break;

		case R.id.txt_tuesday_lout:
			showTimePicker(txtTuesdayLout);
			break;

		case R.id.txt_tuesday_to:
			showTimePicker(txtTuesdayTo);
			break;

		case R.id.txt_wednesday_from:
			showTimePicker(txtWednesdayFrom);
			break;

		case R.id.txt_wednesday_lin:
			showTimePicker(txtWednesdayLin);
			break;

		case R.id.txt_wednesday_lout:
			showTimePicker(txtWednesdayLout);
			break;

		case R.id.txt_wednesday_to:
			showTimePicker(txtWednesdayTo);
			break;

		case R.id.txt_thursday_from:
			showTimePicker(txtThursdayFrom);
			break;

		case R.id.txt_thursday_lin:
			showTimePicker(txtThursdayLin);
			break;

		case R.id.txt_thursday_lout:
			showTimePicker(txtThursdayLout);
			break;

		case R.id.txt_thursday_to:
			showTimePicker(txtThursdayTo);
			break;

		case R.id.txt_friday_from:
			showTimePicker(txtFridayFrom);
			break;

		case R.id.txt_friday_lin:
			showTimePicker(txtFridayLin);
			break;

		case R.id.txt_friday_lout:
			showTimePicker(txtFridayLout);
			break;

		case R.id.txt_friday_to:
			showTimePicker(txtFridayTo);
			break;

		case R.id.txt_saturday_from:
			showTimePicker(txtSaturdayFrom);
			break;

		case R.id.txt_saturday_lin:
			showTimePicker(txtSaturdayLin);
			break;

		case R.id.txt_saturday_lout:
			showTimePicker(txtSaturdayLout);
			break;

		case R.id.txt_saturday_to:
			showTimePicker(txtSaturdayTo);
			break;

		case R.id.txt_sunday_from:
			showTimePicker(txtSundayFrom);
			break;

		case R.id.txt_sunday_lin:
			showTimePicker(txtSundayLin);
			break;

		case R.id.txt_sunday_lout:
			showTimePicker(txtSundayLout);
			break;

		case R.id.txt_sunday_to:
			showTimePicker(txtSundayTo);
			break;

		case R.id.btn_submit:
			DashboardActivity.dayPlan[1].setDay("Monday");
			DashboardActivity.dayPlan[1].setStartTime(txtMondayFrom.getText()
					.toString());
			DashboardActivity.dayPlan[1].setEndTime(txtMondayTo.getText()
					.toString());
			DashboardActivity.dayPlan[1].setLout(txtMondayLout.getText()
					.toString());
			DashboardActivity.dayPlan[1].setLin(txtMondayLin.getText()
					.toString());

			Log.i("", new Gson().toJson(DashboardActivity.dayPlan[1])
					.toString());

			DashboardActivity.dayPlan[2].setDay("Tuesday");
			DashboardActivity.dayPlan[2].setStartTime(txtTuesdayFrom.getText()
					.toString());
			DashboardActivity.dayPlan[2].setEndTime(txtTuesdayTo.getText()
					.toString());
			DashboardActivity.dayPlan[2].setLout(txtTuesdayLout.getText()
					.toString());
			DashboardActivity.dayPlan[2].setLin(txtTuesdayLin.getText()
					.toString());

			DashboardActivity.dayPlan[3].setDay("Wednesday");
			DashboardActivity.dayPlan[3].setStartTime(txtWednesdayFrom
					.getText().toString());
			DashboardActivity.dayPlan[3].setEndTime(txtWednesdayTo.getText()
					.toString());
			DashboardActivity.dayPlan[3].setLout(txtWednesdayLout.getText()
					.toString());
			DashboardActivity.dayPlan[3].setLin(txtWednesdayLin.getText()
					.toString());

			DashboardActivity.dayPlan[4].setDay("Thursday");
			DashboardActivity.dayPlan[4].setStartTime(txtThursdayFrom.getText()
					.toString());
			DashboardActivity.dayPlan[4].setEndTime(txtThursdayTo.getText()
					.toString());
			DashboardActivity.dayPlan[4].setLout(txtThursdayLout.getText()
					.toString());
			DashboardActivity.dayPlan[4].setLin(txtThursdayLin.getText()
					.toString());

			DashboardActivity.dayPlan[5].setDay("Friday");
			DashboardActivity.dayPlan[5].setStartTime(txtFridayFrom.getText()
					.toString());
			DashboardActivity.dayPlan[5].setEndTime(txtFridayTo.getText()
					.toString());
			DashboardActivity.dayPlan[5].setLout(txtFridayLout.getText()
					.toString());
			DashboardActivity.dayPlan[5].setLin(txtFridayLin.getText()
					.toString());

			DashboardActivity.dayPlan[6].setDay("Saturday");
			DashboardActivity.dayPlan[6].setStartTime(txtSaturdayFrom.getText()
					.toString());
			DashboardActivity.dayPlan[6].setEndTime(txtSaturdayTo.getText()
					.toString());
			DashboardActivity.dayPlan[6].setLout(txtSaturdayLout.getText()
					.toString());
			DashboardActivity.dayPlan[6].setLin(txtSaturdayLin.getText()
					.toString());

			DashboardActivity.dayPlan[0].setDay("Sunday");
			DashboardActivity.dayPlan[0].setStartTime(txtSundayFrom.getText()
					.toString());
			DashboardActivity.dayPlan[0].setEndTime(txtSundayTo.getText()
					.toString());
			DashboardActivity.dayPlan[0].setLout(txtSundayLout.getText()
					.toString());
			DashboardActivity.dayPlan[0].setLin(txtSundayLin.getText()
					.toString());
			addVacationValidation(true, true);
			break;

		case R.id.btn_add_vacation:
			addVacationValidation(true, false);
			break;

		default:
			break;
		}

	}

	private void showCalender(final TextView txtView) {
		Calendar cal = Calendar.getInstance();
		DatePickerDialog datepicker = new DatePickerDialog(getActivity(),
				new OnDateSetListener() {

					@Override
					public void onDateSet(DatePicker view, int year,
							int monthOfYear, int dayOfMonth) {
						if (dayOfMonth < 10 && monthOfYear < 9) {
							txtView.setText("0" + ++monthOfYear + "/0"
									+ dayOfMonth + "/" + year);
						} else if (dayOfMonth < 10 && monthOfYear > 8) {
							txtView.setText(++monthOfYear + "/0" + dayOfMonth
									+ "/" + year);
						} else if (dayOfMonth > 9 && monthOfYear < 9) {
							txtView.setText("0" + ++monthOfYear + "/"
									+ dayOfMonth + "/" + year);
						} else {
							txtView.setText(++monthOfYear + "/" + dayOfMonth
									+ "/" + year);
						}

					}
				}, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
				cal.get(Calendar.DAY_OF_MONTH));
		datepicker.show();
	}

	private void showTimePicker(final TextView txtView) {
		TimePickerDialog timePicker = new TimePickerDialog(getActivity(),
				new OnTimeSetListener() {

					@Override
					public void onTimeSet(TimePicker view, int hourOfDay,
							int minute) {
						String hr;
						String min;
						String amOrPm;
						minute = minute - minute % 15;
						// if (hourOfDay > 21) {
						// hourOfDay = 21;
						// minute = 0;
						// } else if (hourOfDay < 9) {
						// hourOfDay = 9;
						// minute = 0;
						// }

						if (hourOfDay > 21) {
							hourOfDay = hourOfDay - 12;
							amOrPm = "PM";
							hr = "" + hourOfDay;
						} else if (hourOfDay > 12) {
							hourOfDay = hourOfDay - 12;
							amOrPm = "PM";
							hr = "0" + hourOfDay;
						} else if (hourOfDay == 12) {
							amOrPm = "PM";
							hr = "" + hourOfDay;
						} else if (hourOfDay > 9) {
							amOrPm = "AM";
							hr = "" + hourOfDay;
						} else if (hourOfDay == 0) {
							amOrPm = "AM";
							hr = "12";

						} else {
							amOrPm = "AM";
							hr = "0" + hourOfDay;
						}
						if (minute > 9) {
							min = "" + minute;
						} else {
							min = "0" + minute;
						}
						txtView.setText(hr + ":" + min + " " + amOrPm);

					}
				}, 9, 0, false);
		timePicker.show();
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		switch (buttonView.getId()) {
		case R.id.cb_monday:
			DashboardActivity.dayPlan[1].setOff(isChecked);
			txtMondayTo.setClickable(!isChecked);
			txtMondayFrom.setClickable(!isChecked);
			break;

		case R.id.cb_tuesday:
			DashboardActivity.dayPlan[2].setOff(isChecked);
			txtTuesdayTo.setClickable(!isChecked);
			txtTuesdayFrom.setClickable(!isChecked);
			break;

		case R.id.cb_wednesday:
			DashboardActivity.dayPlan[3].setOff(isChecked);
			txtWednesdayTo.setClickable(!isChecked);
			txtWednesdayFrom.setClickable(!isChecked);
			break;

		case R.id.cb_thursday:
			DashboardActivity.dayPlan[4].setOff(isChecked);
			txtThursdayTo.setClickable(!isChecked);
			txtThursdayFrom.setClickable(!isChecked);
			break;

		case R.id.cb_friday:
			DashboardActivity.dayPlan[5].setOff(isChecked);
			txtFridayTo.setClickable(!isChecked);
			txtFridayFrom.setClickable(!isChecked);
			break;

		case R.id.cb_saturday:
			DashboardActivity.dayPlan[6].setOff(isChecked);
			txtSaturdayTo.setClickable(!isChecked);
			txtSaturdayFrom.setClickable(!isChecked);
			break;

		case R.id.cb_sunday:
			DashboardActivity.dayPlan[0].setOff(isChecked);
			txtSundayTo.setClickable(!isChecked);
			txtSundayFrom.setClickable(!isChecked);
			break;

		default:
			break;
		}

	}

	private void submitSchedulePlan() {
		final String url = APIs.UPDATE_SCHEDULE_PLAN();
		final StringRequest patientListRequest = new StringRequest(
				Request.Method.POST, url, new Response.Listener<String>() {
					@Override
					public void onResponse(final String response) {
						Log.i("get alerts", response);
						try {
							Util.Alertdialog(getActivity(),
									"Request Complete");
						} catch (Exception e) {
							if(isVisible())
							Util.Alertdialog(getActivity(), e.toString());
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(final VolleyError error) {
						Util.Alertdialog(getActivity(), error.toString());

						Log.e("Error.Response", error);
					}
				}) {
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				final HashMap<String, String> loginParams = new HashMap<String, String>();
				loginParams.put("auth-token", Util
						.getBase64String(EzApp.sharedPref
								.getString(Constants.USER_TOKEN, "")));
				return loginParams;
			}

			@Override
			protected Map<String, String> getParams() {
				final HashMap<String, String> params = new HashMap<String, String>();
				for (int i = 0; i < 7; i++) {
					int j = i;
					if (i == 0)
						j = 7;
					DayPlanModel plan = DashboardActivity.dayPlan[i];
					params.put("day_" + j, plan.getDay());
					if (plan.isOff())
						params.put("off_" + j, "1");
					else
						params.put("off_" + j, "0");
					Log.i(plan.getDay(),
							plan.getEndTime() + ""
									+ Util.getSlotId(plan.getEndTime()));
					params.put("start_" + j,
							"" + Util.getSlotId(plan.getStartTime()));
					params.put("end_" + j,
							"" + (Util.getSlotId(plan.getEndTime()) - 1));
					params.put("lunch_start_" + j,
							"" + Util.getSlotId(plan.getLout()));
					params.put("lunch_end_" + j,
							"" + (Util.getSlotId(plan.getLin()) - 1));

					// Log.i("", ""+new Gson().toJson(plan));
				}
				Log.i("", params.toString());
				return params;
			}

		};
		patientListRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 5,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		EzApp.mVolleyQueue.add(patientListRequest);

	}

	private void addVacation(final VacationModel vacation) {
		final String url = APIs.ADD_DELETE_VACATIONS();
		final StringRequest patientListRequest = new StringRequest(
				Request.Method.POST, url, new Response.Listener<String>() {
					@Override
					public void onResponse(final String response) {
						try {
							JSONObject jObj = new JSONObject(response);
							Log.i("get alerts", response);
							vacation.setId(jObj.getString("vacation-id"));
							DashboardActivity.arrVacations.add(vacation);
							if (SchedulePlanFragment.this.isVisible()) {
								txtStartDate.setText("");
								txtEndDate.setText("");
								adapterVacation.notifyDataSetChanged();
							}
						} catch (JSONException e) {
							Util.Alertdialog(getActivity(), e.toString());
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(final VolleyError error) {
						Util.Alertdialog(getActivity(), error.toString());

						Log.e("Error.Response", error);
					}
				}) {
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				final HashMap<String, String> loginParams = new HashMap<String, String>();
				loginParams.put("auth-token", Util
						.getBase64String(EzApp.sharedPref
								.getString(Constants.USER_TOKEN, "")));
				return loginParams;
			}

			@Override
			protected Map<String, String> getParams() {
				final HashMap<String, String> params = new HashMap<String, String>();
				params.put("type", "add");
				params.put("fdate", vacation.getStartDate());
				params.put("tdate", vacation.getEndDate());
				return params;
			}

		};
		patientListRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 5,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		EzApp.mVolleyQueue.add(patientListRequest);

	}

	private void addVacationValidation(boolean check, boolean isSubmit) {
		Date date = new Date();
		Date startDate = null;
		Date endDate = null;
		try {
			startDate = EzApp.sdfMmddyy.parse(txtStartDate
					.getText().toString());
			endDate = EzApp.sdfMmddyy.parse(txtEndDate.getText()
					.toString());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Log.i(startDate.toString(), date.toString());
		if (!Util.isEmptyString(txtStartDate.getText().toString())
				&& !Util.isEmptyString(txtEndDate.getText().toString())) {
			if (startDate.after(date)) {
				if (startDate.before(endDate)) {
					VacationModel vacation = new VacationModel();
					vacation.setStartDate(txtStartDate.getText().toString());
					vacation.setEndDate(txtEndDate.getText().toString());
					addVacation(vacation);
					if (isSubmit) {
						submitSchedulePlan();
					}
				} else if (check) {
					Util.Alertdialog(getActivity(),
							"Please enter correct dates as end date is before start date.");
				}
			} else if (check) {
				Util.Alertdialog(getActivity(),
						"Please enter correct start date as start date is in past.");
			}
		} else if (Util.isEmptyString(txtStartDate.getText().toString())
				&& Util.isEmptyString(txtEndDate.getText().toString())
				&& isSubmit) {
			submitSchedulePlan();
		} else if (check) {
			Util.Alertdialog(getActivity(), "Please enter start and end dates");
		}

	}
}
