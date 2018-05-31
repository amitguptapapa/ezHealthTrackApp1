package com.ezhealthtrack.labs.fragments;

import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.ezhealthtrack.EzApp;
import com.ezhealthtrack.R;
import com.ezhealthtrack.activity.DashboardActivity;
import com.ezhealthtrack.controller.AutoSuggestController;
import com.ezhealthtrack.controller.AutoSuggestController.OnAutoSuggest;
import com.ezhealthtrack.greendao.Appointment;
import com.ezhealthtrack.greendao.LabAppointment;
import com.ezhealthtrack.greendao.LabAppointmentDao.Properties;
import com.ezhealthtrack.labs.adapter.LabsAppointmentAdapter;
import com.ezhealthtrack.labs.adapter.LabsNewTentativeAdapter;
import com.ezhealthtrack.labs.controller.LabsAppointmentController;
import com.ezhealthtrack.labs.controller.LabsAppointmentController.OnResponseApt;
import com.ezhealthtrack.model.PatientAutoSuggest;
import com.ezhealthtrack.util.Constants;
import com.ezhealthtrack.util.EndlessScrollListener;
import com.ezhealthtrack.util.Util;
import com.ezhealthtrack.util.Util.DateResponse;
import com.flurry.android.FlurryAgent;

import de.greenrobot.dao.query.QueryBuilder;

public class NewTentativeFragment extends Fragment {
	// Container Activity must implement this interface
	private ArrayList<String> arrConsultationType = new ArrayList<String>();
	private ArrayList<String> arrAssignedType = new ArrayList<String>();
	private Spinner spinnerConsultationType;
	private Spinner spinnerAssignedType;
	private Date startDate;
	private Date endDate;
	private PatientAutoSuggest selPat;
	private ListView listAppointments;
	private LabsAppointmentAdapter adapter;
	private TextView txtStartDate;
	private TextView txtEndDate;
	private AutoCompleteTextView editFilter;
	private TextView txtCount;
	public static int tCount = 0;
	private final ArrayList<Appointment> arrAppointment = new ArrayList<Appointment>();
	private String cond;
	private ProgressBar listPb;

	@Override
	public void onActivityCreated(final Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		// arrAppointment.clear();
		// arrAppointment.addAll(DashboardActivity.arrNewTentativePatients);
		editFilter = (AutoCompleteTextView) getActivity().findViewById(
				R.id.edit_name);
		adapter = new LabsAppointmentAdapter(getActivity(),
				Constants.TYPE_NEW_TENTATIVE);
		listAppointments = (ListView) getActivity().findViewById(
				R.id.list_patients);
		View listFooter = ((LayoutInflater) getActivity().getSystemService(
				Context.LAYOUT_INFLATER_SERVICE)).inflate(
				R.layout.list_progressbar, null, false);
		listAppointments.addFooterView(listFooter);
		listPb = (ProgressBar) listFooter.findViewById(R.id.list_progressbar);
		listAppointments.setAdapter(adapter);

		arrConsultationType.clear();
		arrConsultationType.add("Consultation Type");
		arrConsultationType.add("Clinic");
		arrConsultationType.add("Patient Location");
		arrAssignedType.clear();
		arrAssignedType.add("Assigned Type");
		arrAssignedType.add("Self");
		arrAssignedType.add("All");

		spinnerConsultationType = (Spinner) getActivity().findViewById(
				R.id.spinner_consultation_type);
		final ArrayAdapter<String> adapterConsultationType = new ArrayAdapter<String>(
				getActivity(), android.R.layout.simple_spinner_item,
				arrConsultationType);
		adapterConsultationType
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerConsultationType.setAdapter(adapterConsultationType);

		spinnerAssignedType = (Spinner) getActivity().findViewById(
				R.id.spinner_assigned_type);
		final ArrayAdapter<String> adapterAssignedType = new ArrayAdapter<String>(
				getActivity(), android.R.layout.simple_spinner_item,
				arrAssignedType);
		adapterAssignedType
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerAssignedType.setAdapter(adapterAssignedType);

		final TextView txtStartDate = (TextView) getActivity().findViewById(
				R.id.txt_start_date);
		txtStartDate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				FlurryAgent
						.logEvent("NewTentativeFragment - Start Date Field Clicked");
				Util.showCalender(txtStartDate, getActivity(),
						new DateResponse() {

							@Override
							public void dateResponseListner(Date date) {
								startDate = date;

							}
						});
			}
		});
		Util.filterClear(txtStartDate,
				getActivity().findViewById(R.id.img_start_date_clear_filter));

		final TextView txtEndDate = (TextView) getActivity().findViewById(
				R.id.txt_end_date);
		txtEndDate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				FlurryAgent
						.logEvent("NewTentativeFragment - End Date Field Clicked");
				Util.showCalender(txtEndDate, getActivity(),
						new DateResponse() {

							@Override
							public void dateResponseListner(Date date) {
								endDate = date;
							}
						});
			}
		});
		Util.filterClear(txtEndDate,
				getActivity().findViewById(R.id.img_end_date_clear_filter));
		String s;
		if (spinnerAssignedType.getSelectedItemPosition() == 1)
			s = EzApp.sharedPref.getString(Constants.USER_UN_ID,
					"0");
		else
			s = "";
		if (Util.isEmptyString(txtStartDate.getText().toString()))
			startDate = null;
		if (Util.isEmptyString(txtEndDate.getText().toString()))
			endDate = null;
		listPb.setVisibility(View.VISIBLE);
		LabsAppointmentController.getAppointmentListApi(
				Constants.TYPE_NEW_TENTATIVE,
				"1",
				"",
				getActivity(),
				new OnResponseApt() {

					@Override
					public void onResponseListner(String response, String count) {
						Log.i(response, count);
						if (!response.equals("error"))
							try {
								adapter.replaceLazyList(getQuery()
										.orderAsc(Properties.Aptdate).limit(50)
										.listLazy());
								tCount = (int) getQuery().count();
								updateCount();
								cond = response;
								listPb.setVisibility(View.GONE);
							} catch (Exception e) {

							}
					}
				}, selPat, startDate, endDate,
				spinnerConsultationType.getSelectedItemPosition(), s);

		final ArrayAdapter<PatientAutoSuggest> adapterPatient = new ArrayAdapter<PatientAutoSuggest>(
				getActivity(), android.R.layout.simple_dropdown_item_1line);
		editFilter.setAdapter(adapterPatient);
		editFilter.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				selPat = null;

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				AutoSuggestController.autoSuggestPatient(s.toString(),
						getActivity(), new OnAutoSuggest() {

							@Override
							public void OnAutoSuggestListner(Object list) {
								ArrayList<PatientAutoSuggest> arrPat = (ArrayList<PatientAutoSuggest>) list;
								adapterPatient.clear();
								adapterPatient.addAll(arrPat);
							}
						});
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});

		editFilter.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				selPat = adapterPatient.getItem(position);
			}
		});

		getActivity().findViewById(R.id.btn_search).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// if (startDate.after(endDate)) {
						// Util.Alertdialog(getActivity(),
						// "Please enter correct Dates as End Date cannot be before the Start Date!");
						// } else {
						FlurryAgent
								.logEvent("ConfirmedFragment - Search Button Clicked");
						listPb.setVisibility(View.VISIBLE);
						String s;
						if (spinnerAssignedType.getSelectedItemPosition() == 1)
							s = EzApp.sharedPref.getString(
									Constants.USER_UN_ID, "0");
						else
							s = "";
						if (Util.isEmptyString(txtStartDate.getText()
								.toString()))
							startDate = null;
						if (Util.isEmptyString(txtEndDate.getText().toString()))
							endDate = null;
						LabsAppointmentController.getAppointmentListApi(
								Constants.TYPE_NEW_TENTATIVE,
								"1",
								"",
								getActivity(),
								new OnResponseApt() {

									@Override
									public void onResponseListner(
											String response, String count) {
										if (!response.equals("error"))
											try {
												adapter.replaceLazyList(getQuery()
														.orderAsc(
																Properties.Aptdate)
														.limit(50).listLazy());
												tCount = Integer
														.parseInt(count);
												updateCount();
												cond = response;
												listPb.setVisibility(View.GONE);
											} catch (Exception e) {

											}

									}
								}, selPat, startDate, endDate,
								spinnerConsultationType
										.getSelectedItemPosition(), s);

						// }
					}
				});
		
		listAppointments.setOnScrollListener(new EndlessScrollListener() {

			@Override
			public void onLoadMore(final int page, int totalItemsCount) {
				listPb.setVisibility(View.VISIBLE);
				String s;
				if (spinnerAssignedType.getSelectedItemPosition() == 1)
					s = EzApp.sharedPref.getString(
							Constants.USER_UN_ID, "0");
				else
					s = "";
				if (Util.isEmptyString(txtStartDate.getText().toString()))
					startDate = null;
				if (Util.isEmptyString(txtEndDate.getText().toString()))
					endDate = null;
				LabsAppointmentController.getAppointmentListApi(
						Constants.TYPE_NEW_TENTATIVE, "" + (page), cond,
						getActivity(), new OnResponseApt() {

							@Override
							public void onResponseListner(String response,
									String count) {
								if (!response.equals("error"))
									try {
										adapter.replaceLazyList(getQuery()
												.orderAsc(Properties.Aptdate)
												.limit(50 * (page + 1))
												.listLazy());
										tCount = Integer.parseInt(count);
										updateCount();
										cond = response;
										listPb.setVisibility(View.GONE);
									} catch (Exception e) {

									}

							}
						}, selPat, startDate, endDate, spinnerConsultationType
								.getSelectedItemPosition(), s);

			}
		});

	}

	@Override
	public void onAttach(final Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(final LayoutInflater inflater,
			final ViewGroup container, final Bundle savedInstanceState) {
		return inflater.inflate(R.layout.labs_fragment_new_tentative,
				container, false);
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onSaveInstanceState(final Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	private QueryBuilder<LabAppointment> getQuery() {
		QueryBuilder<LabAppointment> qb = EzApp.labAptDao
				.queryBuilder();
		if (startDate != null)
			qb.where(Properties.Aptdate.ge(startDate));
		if (endDate != null)
			qb.where(Properties.Aptdate.le(new Date(endDate.getTime() + 24 * 60
					* 60 * 1000)));
		if (spinnerConsultationType.getSelectedItemPosition() > 0)
			qb.where(Properties.Visit_location.eq(spinnerConsultationType
					.getSelectedItemPosition()));
		if (spinnerAssignedType.getSelectedItemPosition() == 1)
			qb.where(Properties.Assigned_user_id
					.eq(EzApp.sharedPref.getString(
							Constants.USER_UN_ID, "")));
		String s = editFilter.getText().toString();
		if (selPat != null && selPat.getName().equals(s))
			qb.where(Properties.Pid.eq(selPat.getId()));
		qb.where(Properties.Apflag.eq("0"), Properties.Flag.eq("0"));
		Log.i("count", "" + qb.count());
		return qb;
	}

	public void updateCount() {
		if (isVisible()) {
			txtCount = (TextView) getActivity().findViewById(
					R.id.txt_count_tentative);
			txtCount.setText(Html.fromHtml("<b>Total Results: " + tCount
					+ "</b>"));
		}
	}

}
