package com.ezhealthtrack.fragments;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ezhealthtrack.EzApp;
import com.ezhealthtrack.R;
import com.ezhealthtrack.adapter.ReportAdapter;
import com.ezhealthtrack.greendao.MedRecVisitNotes;
import com.ezhealthtrack.greendao.MedRecVisitNotesDao.Properties;
import com.ezhealthtrack.util.EndlessScrollListener;
import com.ezhealthtrack.util.Log;
import com.ezhealthtrack.util.Util;

import de.greenrobot.dao.query.QueryBuilder;

public class ReportFragment extends Fragment implements OnClickListener {
	private ListView listReport;
	private ReportAdapter adapter;
	private TextView txtStartDate;
	private TextView txtEndDate;
	private Date startDate;
	private Date endDate;
	private TextView txtCount;
	public static int totalCount = 0;
	private ProgressBar listPb;

	@Override
	public void onActivityCreated(final Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		adapter = new ReportAdapter(getActivity());
		listReport = (ListView) getActivity().findViewById(R.id.list_patients);
		View listFooter = ((LayoutInflater) getActivity().getSystemService(
				Context.LAYOUT_INFLATER_SERVICE)).inflate(
				R.layout.list_progressbar, null, false);
		listReport.addFooterView(listFooter);
		listPb = (ProgressBar) listFooter.findViewById(R.id.list_progressbar);
		listReport.setAdapter(adapter);

		txtStartDate = (TextView) getActivity().findViewById(
				R.id.txt_start_date);
		txtStartDate.setOnClickListener(this);
		Util.filterClear(txtStartDate,
				getActivity().findViewById(R.id.img_start_date_clear_filter));
		txtEndDate = (TextView) getActivity().findViewById(R.id.txt_end_date);
		txtEndDate.setOnClickListener(this);
		Util.filterClear(txtEndDate,
				getActivity().findViewById(R.id.img_end_date_clear_filter));
		getActivity().findViewById(R.id.btn_search).setOnClickListener(this);
		Util.setupUI(getActivity(),
				getActivity().findViewById(R.id.rl_confirmed));
		updateCount();

	}

	private void setScrollListner() {
		listReport.setOnScrollListener(new EndlessScrollListener(4, 0) {

			@Override
			public void onLoadMore(final int page, int totalItemsCount) {
				Log.i("", "" + page);
				if (Util.isEmptyString(txtStartDate.getText().toString()))
					startDate = null;
				if (Util.isEmptyString(txtEndDate.getText().toString()))
					endDate = null;
				// listPb.setVisibility(View.VISIBLE);
				// MedicalRecordsController.getVisitNotesList(getActivity(),
				// page,
				// editFilter.getText().toString(), startDate, endDate,
				// new OnResponse() {
				//
				// @Override
				// public void onResponseListner(String response) {
				// totalCount = Integer.parseInt(response);
				// updateCount();
				// adapter.replaceLazyList(getQuery().limit(
				// 15 * page).listLazy());
				// listPb.setVisibility(View.GONE);
				// }
				// });

			}
		});
	}

	@Override
	public void onClick(final View v) {
		switch (v.getId()) {
		case R.id.txt_start_date:
			showCalender(txtStartDate, "startDate");
			break;

		case R.id.txt_end_date:
			showCalender(txtEndDate, "endDate");
			break;

		case R.id.btn_search:

			if (Util.isEmptyString(txtStartDate.getText().toString()))
				startDate = null;
			if (Util.isEmptyString(txtEndDate.getText().toString()))
				endDate = null;

			// listPb.setVisibility(View.VISIBLE);
			// MedicalRecordsController.getVisitNotesList(getActivity(), 1,
			// editFilter.getText().toString(), startDate, endDate,
			// new OnResponse() {
			//
			// @Override
			// public void onResponseListner(String response) {
			// totalCount = Integer.parseInt(response);
			// updateCount();
			// adapter.replaceLazyList(getQuery().limit(15)
			// .listLazy());
			// listPb.setVisibility(View.GONE);
			// setScrollListner();
			// }
			// });
			break;
		}

	}

	@Override
	public View onCreateView(final LayoutInflater inflater,
			final ViewGroup container, final Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_report, container, false);
	}

	@Override
	public void onResume() {
		adapter.notifyDataSetChanged();
		// listPb.setVisibility(View.VISIBLE);
		// MedicalRecordsController.getVisitNotesList(getActivity(), 1,
		// editFilter
		// .getText().toString(), null, null, new OnResponse() {
		//
		// @Override
		// public void onResponseListner(String response) {
		// totalCount = Integer.parseInt(response);
		// updateCount();
		// adapter.replaceLazyList(getQuery().limit(15).listLazy());
		// listPb.setVisibility(View.GONE);
		// setScrollListner();
		// }
		// });
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

	public void updateCount() {
		try {
			txtCount = (TextView) getActivity().findViewById(
					R.id.txt_count_confirmed);
			txtCount.setText(Html.fromHtml("<b>" + "Total Messages Report: "
					+ ReportFragment.totalCount + "</b>"));
		} catch (Exception e) {

		}
	}

	private void showCalender(final TextView txtView, final String date) {
		final Calendar cal = Calendar.getInstance();
		final DatePickerDialog datepicker = new DatePickerDialog(getActivity(),
				new OnDateSetListener() {

					@Override
					public void onDateSet(final DatePicker view,
							final int year, int monthOfYear,
							final int dayOfMonth) {
						if ((dayOfMonth < 10) && (monthOfYear < 9)) {
							txtView.setText("0" + dayOfMonth + "/0"
									+ ++monthOfYear + "/" + year);
						} else if ((dayOfMonth < 10) && (monthOfYear > 8)) {
							txtView.setText("0" + dayOfMonth + "/"
									+ ++monthOfYear + "/" + year);
						} else if ((dayOfMonth > 9) && (monthOfYear < 9)) {
							txtView.setText(dayOfMonth + "/0" + ++monthOfYear
									+ "/" + year);
						} else {
							txtView.setText(dayOfMonth + "/" + ++monthOfYear
									+ "/" + year);
						}
						final SimpleDateFormat sdf = new SimpleDateFormat(
								"dd/MM/yyyy");
						if (date.equals("startDate")) {
							try {
								startDate = sdf.parse(txtView.getText()
										.toString());
							} catch (final ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						} else {
							try {
								endDate = sdf.parse(txtView.getText()
										.toString());
								endDate.setTime(endDate.getTime());
							} catch (final ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}

					}
				}, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
				cal.get(Calendar.DAY_OF_MONTH));
		datepicker.show();
	}

	private QueryBuilder<MedRecVisitNotes> getQuery() {
		QueryBuilder<MedRecVisitNotes> qb = EzApp.medRecVisitNotesDao
				.queryBuilder();
		if (startDate != null)
			qb.where(Properties.Apt_date.ge(startDate));
		if (endDate != null)
			qb.where(Properties.Apt_date.le(endDate));
		qb.orderDesc(Properties.Last_visit);
		return qb;
	}

}
