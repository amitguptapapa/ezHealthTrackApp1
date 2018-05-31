package com.ezhealthtrack.fragments;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
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
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ezhealthrack.api.APIs;
import com.ezhealthtrack.EzApp;
import com.ezhealthtrack.R;
import com.ezhealthtrack.adapter.VacationAdapter;
import com.ezhealthtrack.model.DayPlanModel;
import com.ezhealthtrack.model.VacationModel;
import com.ezhealthtrack.util.Constants;
import com.ezhealthtrack.util.Log;
import com.ezhealthtrack.util.Util;
import com.google.gson.Gson;

public class AssistantSchedulePlanFragment extends Fragment implements
		OnClickListener, OnCheckedChangeListener {
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
	private DayPlanModel[] arrDayPlan = new DayPlanModel[7];
	public static ArrayList<VacationModel> arrVacations = new ArrayList<VacationModel>();
	public static String asstId = "";
	public static String asst_name = "";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_assistant_sp, container,
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
		for (int i = 0; i < 7; i++) {
			arrDayPlan[i] = new DayPlanModel();
		}
		((TextView) getActivity().findViewById(R.id.txt_header))
				.setText(asst_name + " - Schedule Plan");
		// setData();
		getSchedulePlan();
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
			arrDayPlan[1].setDay("Monday");
			arrDayPlan[1].setStartTime(txtMondayFrom.getText().toString());
			arrDayPlan[1].setEndTime(txtMondayTo.getText().toString());
			arrDayPlan[1].setLout(txtMondayLout.getText().toString());
			arrDayPlan[1].setLin(txtMondayLin.getText().toString());

			Log.i("", new Gson().toJson(arrDayPlan[1]).toString());

			arrDayPlan[2].setDay("Tuesday");
			arrDayPlan[2].setStartTime(txtTuesdayFrom.getText().toString());
			arrDayPlan[2].setEndTime(txtTuesdayTo.getText().toString());
			arrDayPlan[2].setLout(txtTuesdayLout.getText().toString());
			arrDayPlan[2].setLin(txtTuesdayLin.getText().toString());

			arrDayPlan[3].setDay("Wednesday");
			arrDayPlan[3].setStartTime(txtWednesdayFrom.getText().toString());
			arrDayPlan[3].setEndTime(txtWednesdayTo.getText().toString());
			arrDayPlan[3].setLout(txtWednesdayLout.getText().toString());
			arrDayPlan[3].setLin(txtWednesdayLin.getText().toString());

			arrDayPlan[4].setDay("Thursday");
			arrDayPlan[4].setStartTime(txtThursdayFrom.getText().toString());
			arrDayPlan[4].setEndTime(txtThursdayTo.getText().toString());
			arrDayPlan[4].setLout(txtThursdayLout.getText().toString());
			arrDayPlan[4].setLin(txtThursdayLin.getText().toString());

			arrDayPlan[5].setDay("Friday");
			arrDayPlan[5].setStartTime(txtFridayFrom.getText().toString());
			arrDayPlan[5].setEndTime(txtFridayTo.getText().toString());
			arrDayPlan[5].setLout(txtFridayLout.getText().toString());
			arrDayPlan[5].setLin(txtFridayLin.getText().toString());

			arrDayPlan[6].setDay("Saturday");
			arrDayPlan[6].setStartTime(txtSaturdayFrom.getText().toString());
			arrDayPlan[6].setEndTime(txtSaturdayTo.getText().toString());
			arrDayPlan[6].setLout(txtSaturdayLout.getText().toString());
			arrDayPlan[6].setLin(txtSaturdayLin.getText().toString());

			arrDayPlan[0].setDay("Sunday");
			arrDayPlan[0].setStartTime(txtSundayFrom.getText().toString());
			arrDayPlan[0].setEndTime(txtSundayTo.getText().toString());
			arrDayPlan[0].setLout(txtSundayLout.getText().toString());
			arrDayPlan[0].setLin(txtSundayLin.getText().toString());
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
							hr = "12S";

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
			arrDayPlan[1].setOff(isChecked);
			txtMondayTo.setClickable(!isChecked);
			txtMondayFrom.setClickable(!isChecked);
			break;

		case R.id.cb_tuesday:
			arrDayPlan[2].setOff(isChecked);
			txtTuesdayTo.setClickable(!isChecked);
			txtTuesdayFrom.setClickable(!isChecked);
			break;

		case R.id.cb_wednesday:
			arrDayPlan[3].setOff(isChecked);
			txtWednesdayTo.setClickable(!isChecked);
			txtWednesdayFrom.setClickable(!isChecked);
			break;

		case R.id.cb_thursday:
			arrDayPlan[4].setOff(isChecked);
			txtThursdayTo.setClickable(!isChecked);
			txtThursdayFrom.setClickable(!isChecked);
			break;

		case R.id.cb_friday:
			arrDayPlan[5].setOff(isChecked);
			txtFridayTo.setClickable(!isChecked);
			txtFridayFrom.setClickable(!isChecked);
			break;

		case R.id.cb_saturday:
			arrDayPlan[6].setOff(isChecked);
			txtSaturdayTo.setClickable(!isChecked);
			txtSaturdayFrom.setClickable(!isChecked);
			break;

		case R.id.cb_sunday:
			arrDayPlan[0].setOff(isChecked);
			txtSundayTo.setClickable(!isChecked);
			txtSundayFrom.setClickable(!isChecked);
			break;

		default:
			break;
		}

	}

	private void submitSchedulePlan() {
		final String url = APIs.UPDATE_SCHEDULE_PLAN_ASSISTANT();
		final StringRequest patientListRequest = new StringRequest(
				Request.Method.POST, url, new Response.Listener<String>() {
					@Override
					public void onResponse(final String response) {
						Log.i("get alerts", response);
						try {
							Util.Alertdialog(getActivity(), new JSONObject(
									response).getString("m"));
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
				params.put("asst_id", asstId);
				for (int i = 0; i < 7; i++) {
					int j = i;
					if (i == 0)
						j = 7;
					DayPlanModel plan = arrDayPlan[i];
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
		final String url = APIs.ADD_DELETE_ASST_VACATIONS();
		final StringRequest patientListRequest = new StringRequest(
				Request.Method.POST, url, new Response.Listener<String>() {
					@Override
					public void onResponse(final String response) {
						try {
							JSONObject jObj = new JSONObject(response);
							Log.i("get alerts", response);
							txtStartDate.setText("");
							txtEndDate.setText("");
							vacation.setId(jObj.getString("vacation-id"));
							arrVacations.add(vacation);
							adapterVacation.notifyDataSetChanged();
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
				params.put("asst_id", asstId);
				return params;
			}

		};
		patientListRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 5,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		EzApp.mVolleyQueue.add(patientListRequest);

	}

	private void setData() {
		txtStartDate = (TextView) getActivity().findViewById(
				R.id.txt_start_date);
		txtStartDate.setOnClickListener(this);
		txtEndDate = (TextView) getActivity().findViewById(R.id.txt_end_date);
		txtEndDate.setOnClickListener(this);

		txtMondayFrom = (TextView) getActivity().findViewById(
				R.id.txt_monday_from);
		txtMondayFrom.setOnClickListener(this);
		txtMondayFrom.setText(arrDayPlan[1].getStartTime());
		txtMondayTo = (TextView) getActivity().findViewById(R.id.txt_monday_to);
		txtMondayTo.setOnClickListener(this);
		txtMondayTo.setText(arrDayPlan[1].getEndTime());
		cbMonday = (CheckBox) getActivity().findViewById(R.id.cb_monday);
		cbMonday.setChecked(arrDayPlan[1].isOff());
		cbMonday.setOnCheckedChangeListener(this);
		txtMondayLout = (TextView) getActivity().findViewById(
				R.id.txt_monday_lout);
		txtMondayLout.setOnClickListener(this);
		txtMondayLout.setText(arrDayPlan[1].getLout());
		txtMondayLin = (TextView) getActivity().findViewById(
				R.id.txt_monday_lin);
		txtMondayLin.setOnClickListener(this);
		txtMondayLin.setText(arrDayPlan[1].getLin());

		txtTuesdayFrom = (TextView) getActivity().findViewById(
				R.id.txt_tuesday_from);
		txtTuesdayFrom.setOnClickListener(this);
		txtTuesdayFrom.setText(arrDayPlan[2].getStartTime());
		txtTuesdayTo = (TextView) getActivity().findViewById(
				R.id.txt_tuesday_to);
		txtTuesdayTo.setOnClickListener(this);
		txtTuesdayTo.setText(arrDayPlan[2].getEndTime());
		cbTuesday = (CheckBox) getActivity().findViewById(R.id.cb_tuesday);
		cbTuesday.setChecked(arrDayPlan[2].isOff());
		cbTuesday.setOnCheckedChangeListener(this);
		txtTuesdayLout = (TextView) getActivity().findViewById(
				R.id.txt_tuesday_lout);
		txtTuesdayLout.setOnClickListener(this);
		txtTuesdayLout.setText(arrDayPlan[2].getLout());
		txtTuesdayLin = (TextView) getActivity().findViewById(
				R.id.txt_tuesday_lin);
		txtTuesdayLin.setOnClickListener(this);
		txtTuesdayLin.setText(arrDayPlan[2].getLin());

		txtWednesdayFrom = (TextView) getActivity().findViewById(
				R.id.txt_wednesday_from);
		txtWednesdayFrom.setOnClickListener(this);
		txtWednesdayFrom.setText(arrDayPlan[3].getStartTime());
		txtWednesdayTo = (TextView) getActivity().findViewById(
				R.id.txt_wednesday_to);
		txtWednesdayTo.setOnClickListener(this);
		txtWednesdayTo.setText(arrDayPlan[3].getEndTime());
		cbWednesday = (CheckBox) getActivity().findViewById(R.id.cb_wednesday);
		cbWednesday.setChecked(arrDayPlan[3].isOff());
		cbWednesday.setOnCheckedChangeListener(this);
		txtWednesdayLout = (TextView) getActivity().findViewById(
				R.id.txt_wednesday_lout);
		txtWednesdayLout.setOnClickListener(this);
		txtWednesdayLout.setText(arrDayPlan[3].getLout());
		txtWednesdayLin = (TextView) getActivity().findViewById(
				R.id.txt_wednesday_lin);
		txtWednesdayLin.setOnClickListener(this);
		txtWednesdayLin.setText(arrDayPlan[3].getLin());

		txtThursdayFrom = (TextView) getActivity().findViewById(
				R.id.txt_thursday_from);
		txtThursdayFrom.setOnClickListener(this);
		txtThursdayFrom.setText(arrDayPlan[4].getStartTime());
		txtThursdayTo = (TextView) getActivity().findViewById(
				R.id.txt_thursday_to);
		txtThursdayTo.setOnClickListener(this);
		txtThursdayTo.setText(arrDayPlan[4].getEndTime());
		cbThursday = (CheckBox) getActivity().findViewById(R.id.cb_thursday);
		cbThursday.setChecked(arrDayPlan[4].isOff());
		cbThursday.setOnCheckedChangeListener(this);
		txtThursdayLout = (TextView) getActivity().findViewById(
				R.id.txt_thursday_lout);
		txtThursdayLout.setOnClickListener(this);
		txtThursdayLout.setText(arrDayPlan[4].getLout());
		txtThursdayLin = (TextView) getActivity().findViewById(
				R.id.txt_thursday_lin);
		txtThursdayLin.setOnClickListener(this);
		txtThursdayLin.setText(arrDayPlan[4].getLin());

		txtFridayFrom = (TextView) getActivity().findViewById(
				R.id.txt_friday_from);
		txtFridayFrom.setOnClickListener(this);
		txtFridayFrom.setText(arrDayPlan[5].getStartTime());
		txtFridayTo = (TextView) getActivity().findViewById(R.id.txt_friday_to);
		txtFridayTo.setOnClickListener(this);
		txtFridayTo.setText(arrDayPlan[5].getEndTime());
		cbFriday = (CheckBox) getActivity().findViewById(R.id.cb_friday);
		cbFriday.setChecked(arrDayPlan[5].isOff());
		cbFriday.setOnCheckedChangeListener(this);
		txtFridayLout = (TextView) getActivity().findViewById(
				R.id.txt_friday_lout);
		txtFridayLout.setOnClickListener(this);
		txtFridayLout.setText(arrDayPlan[5].getLout());
		txtFridayLin = (TextView) getActivity().findViewById(
				R.id.txt_friday_lin);
		txtFridayLin.setOnClickListener(this);
		txtFridayLin.setText(arrDayPlan[5].getLin());

		txtSaturdayFrom = (TextView) getActivity().findViewById(
				R.id.txt_saturday_from);
		txtSaturdayFrom.setOnClickListener(this);
		txtSaturdayFrom.setText(arrDayPlan[6].getStartTime());
		txtSaturdayTo = (TextView) getActivity().findViewById(
				R.id.txt_saturday_to);
		txtSaturdayTo.setOnClickListener(this);
		txtSaturdayTo.setText(arrDayPlan[6].getEndTime());
		cbSaturday = (CheckBox) getActivity().findViewById(R.id.cb_saturday);
		cbSaturday.setChecked(arrDayPlan[6].isOff());
		cbSaturday.setOnCheckedChangeListener(this);
		txtSaturdayLout = (TextView) getActivity().findViewById(
				R.id.txt_saturday_lout);
		txtSaturdayLout.setOnClickListener(this);
		txtSaturdayLout.setText(arrDayPlan[6].getLout());
		txtSaturdayLin = (TextView) getActivity().findViewById(
				R.id.txt_saturday_lin);
		txtSaturdayLin.setOnClickListener(this);
		txtSaturdayLin.setText(arrDayPlan[6].getLin());

		txtSundayFrom = (TextView) getActivity().findViewById(
				R.id.txt_sunday_from);
		txtSundayFrom.setOnClickListener(this);
		txtSundayFrom.setText(arrDayPlan[0].getStartTime());
		txtSundayTo = (TextView) getActivity().findViewById(R.id.txt_sunday_to);
		txtSundayTo.setOnClickListener(this);
		txtSundayTo.setText(arrDayPlan[0].getEndTime());
		cbSunday = (CheckBox) getActivity().findViewById(R.id.cb_sunday);
		cbSunday.setChecked(arrDayPlan[0].isOff());
		cbSunday.setOnCheckedChangeListener(this);
		txtSundayLout = (TextView) getActivity().findViewById(
				R.id.txt_sunday_lout);
		txtSundayLout.setOnClickListener(this);
		txtSundayLout.setText(arrDayPlan[0].getLout());
		txtSundayLin = (TextView) getActivity().findViewById(
				R.id.txt_sunday_lin);
		txtSundayLin.setOnClickListener(this);
		txtSundayLin.setText(arrDayPlan[0].getLin());

		btnSubmit = (TextView) getActivity().findViewById(R.id.btn_submit);
		btnSubmit.setOnClickListener(this);

		btnAddVacation = (TextView) getActivity().findViewById(
				R.id.btn_add_vacation);
		btnAddVacation.setOnClickListener(this);
		listVacation = (ListView) getActivity()
				.findViewById(R.id.list_vacation);
		adapterVacation = new VacationAdapter(getActivity(),
				R.layout.row_vacation, arrVacations, asstId);
		listVacation.setAdapter(adapterVacation);
	}

	private void getSchedulePlan() {
		final String url = APIs.ASSISTANT_SCHEDULE_PLAN();
		Log.i("", url);
		final StringRequest schedulePlanRequest = new StringRequest(
				Request.Method.POST, url, new Response.Listener<String>() {
					@Override
					public void onResponse(final String response) {
						Log.i("", response);
						try {
							final JSONObject jObj = new JSONObject(response);
							if (jObj.getString("s").equals("200")) {
								final JSONObject data = jObj
										.getJSONObject("data");
								final JSONArray arrVacation = data
										.getJSONArray("vacation");
								final JSONArray arrSchedule = data
										.getJSONArray("schedule");
								final SimpleDateFormat sdfDatePre = new SimpleDateFormat(
										"yyyy-mm-dd");
								final SimpleDateFormat sdfDatePost = new SimpleDateFormat(
										"dd/mm/yyyy");
								arrVacations.clear();
								for (int i = 0; i < arrVacation.length(); i++) {
									final JSONObject vacation = arrVacation
											.getJSONObject(i);
									final VacationModel vacationModel = new VacationModel();
									vacationModel.setId(vacation
											.getString("vacation-id"));
									try {
										vacationModel.setStartDate(sdfDatePost.format(sdfDatePre.parse(vacation
												.getString("start-date"))));
										vacationModel.setEndDate(sdfDatePost.format(sdfDatePre
												.parse(vacation
														.getString("end-date"))));
									} catch (final ParseException e) {
										Log.e("", e);
									}
									arrVacations.add(vacationModel);
								}

								for (int i = 0; i < arrSchedule.length(); i++) {
									final JSONObject schedule = arrSchedule
											.getJSONObject(i);
									final DayPlanModel dayPlan = new DayPlanModel();
									dayPlan.setId(schedule
											.getString("schedule-plan-id"));
									dayPlan.setDay(schedule.getString("day"));
									final SimpleDateFormat df = new SimpleDateFormat(
											"hh:mm a");
									final Calendar calStart = Calendar
											.getInstance();
									final Calendar calEnd = Calendar
											.getInstance();
									final Calendar calLin = Calendar
											.getInstance();
									final Calendar calLout = Calendar
											.getInstance();
									try {
										if (schedule.getInt("start-slot") == 0) {
											dayPlan.setOff(true);

										} else {
											dayPlan.setOff(false);
											final Date d1 = df
													.parse("12:00 AM");
											final Date d2 = df
													.parse("12:00 AM");
											final Date d3 = df
													.parse("12:00 AM");
											final Date d4 = df
													.parse("12:00 AM");
											calStart.setTime(d1);
											calStart.add(Calendar.MINUTE, -15);
											calEnd.setTime(d2);
											calLout.setTime(d3);
											calLout.add(Calendar.MINUTE, -15);
											calLin.setTime(d4);
											int minutes = schedule
													.getInt("start-slot") * 15;
											calStart.add(Calendar.MINUTE,
													minutes);
											dayPlan.setStartTime(df
													.format(calStart.getTime()));
											minutes = schedule
													.getInt("end-slot") * 15;
											calEnd.add(Calendar.MINUTE, minutes);
											dayPlan.setEndTime(df.format(calEnd
													.getTime()));
											minutes = schedule
													.getInt("lunch_start") * 15;
											calLout.add(Calendar.MINUTE,
													minutes);
											dayPlan.setLout(df.format(calLout
													.getTime()));
											minutes = schedule
													.getInt("lunch_end") * 15;
											calLin.add(Calendar.MINUTE, minutes);
											dayPlan.setLin(df.format(calLin
													.getTime()));
										}
									} catch (final ParseException e) {
										Log.e("", e);
									}
									if (i == 6) {
										arrDayPlan[0] = dayPlan;
									} else {
										arrDayPlan[i + 1] = dayPlan;
									}
								}
								setData();
							} else {
								Toast.makeText(
										getActivity(),
										"There is some error while fetching schedule plan please try again.",
										Toast.LENGTH_SHORT).show();
							}
						} catch (final JSONException e) {
							Toast.makeText(
									getActivity(),
									"There is some error while fetching schedule plan please try again.",
									Toast.LENGTH_SHORT).show();
							Log.e("", e);
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(final VolleyError error) {
						Toast.makeText(
								getActivity(),
								"There is some error while fetching schedule plan please try again",
								Toast.LENGTH_SHORT).show();

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
				params.put("format", "json");
				params.put("asst_id", asstId);
				return params;
			}

		};
		schedulePlanRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 5,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		EzApp.mVolleyQueue.add(schedulePlanRequest);

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
