package com.ezhealthtrack.fragments;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.json.JSONObject;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.ezhealthtrack.EzApp;
import com.ezhealthtrack.R;
import com.ezhealthtrack.adapter.ScheduleListAdapter;
import com.ezhealthtrack.controller.PatientsListController;
import com.ezhealthtrack.fragments.PatientsListFragment.OnCallback;
import com.ezhealthtrack.greendao.Patient;
import com.ezhealthtrack.greendao.PatientDao.Properties;
import com.ezhealthtrack.util.Util;

import de.greenrobot.dao.query.QueryBuilder;

public class ScheduleListFragment extends EzGridFragment implements
		OnClickListener {

	private TextView mCountView;
	private EditText mEditFilter;
	private TextView txtFilter;

	private OnCallback onCallback;

	public ScheduleListFragment() {
		super(false);
	}

	@Override
	public void onCmdResponse(JSONObject response, String result) {
		String count = response.optString("totalItemCount");
		if (count.isEmpty()) {
			mCountView.setVisibility(View.GONE);
			return;
		}
		mCountView
				.setText(Html.fromHtml("<b>Total Results: " + count + "</b>"));

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mController = new PatientsListController(0);
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(final LayoutInflater inflater,
			final ViewGroup container, final Bundle savedInstanceState) {

		final View view = inflater.inflate(R.layout.fragment_patients_list,
				container, false);
		super.onCreateListView(view, inflater);

		mGridAdapter = new ScheduleListAdapter(mDataList, this, onCallback);
		mGridView.setAdapter(mGridAdapter);

		TextView title = (TextView) view.findViewById(R.id.txt_screen_title);
		title.setText("Appointments - Schedule");

		view.findViewById(R.id.edit_name_view).setVisibility(View.VISIBLE);
		view.findViewById(R.id.start_date_view).setVisibility(View.GONE);
		view.findViewById(R.id.end_date_view).setVisibility(View.GONE);
		view.findViewById(R.id.edit_date_view).setVisibility(View.VISIBLE);
		view.findViewById(R.id.btn_search).setVisibility(View.GONE);
		view.findViewById(R.id.line_2).setVisibility(View.GONE);

		// unread/total item count
		mCountView = (TextView) view.findViewById(R.id.txt_count);

		// filter text
		mEditFilter = (AutoCompleteTextView) view.findViewById(R.id.edit_name);
		mEditFilter.setHint("Patient Name | Patient ID");

		mEditFilter.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(final Editable arg0) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void onTextChanged(final CharSequence cs, final int arg1,
					final int arg2, final int arg3) {
				// When user changed the Text
				Util.filterClear(mEditFilter,
						getActivity().findViewById(R.id.img_clear_filter));

				notifylist();
			}
		});

		txtFilter = (TextView) view.findViewById(R.id.edit_date);
		txtFilter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showCalender(txtFilter);
			}
		});
		txtFilter.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				Util.filterClear(txtFilter,
						view.findViewById(R.id.img_date_clear_filter));

				if (Util.isEmptyString(s.toString()))
					view.findViewById(R.id.img_date_clear_filter)
							.setVisibility(View.GONE);
				else
					view.findViewById(R.id.img_date_clear_filter)
							.setVisibility(View.VISIBLE);

				notifylist();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});

		view.findViewById(R.id.img_date_clear_filter).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						txtFilter.setText("");
						notifylist();
					}
				});

		// setup UI
		Util.setupUI(getActivity(), view.findViewById(R.id.rl_patients));

		// Get initial data
		mController.getPage(0, null, this);
		return view;
	}

	@Override
	public void onActivityCreated(final Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onResume() {
		updateCount();
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
			TextView txtCount = (TextView) getActivity().findViewById(
					R.id.txt_count);
			txtCount.setText(Html.fromHtml("<b>Total Patients : "
					+ mGridAdapter.getCount() + "</b>"));
		} catch (Exception e) {

		}
	}

	@Override
	public void onStop() {
		Log.i("LOF", "onStop(): Size: " + mDataList.size());
		// save records for later use
		if (isRemoving())
			mEditFilter.setText("");
		Log.i("LOF", "onStop(): Size: " + mDataList.size());
		super.onStop();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.edit_date:
			showCalender(txtFilter);
			break;
		}

	}

	private List<Patient> getQuery() {
		String cs = mEditFilter.getText().toString();
		QueryBuilder<Patient> qb = EzApp.patientDao.queryBuilder();
		qb.where(Properties.P_detail.like("%" + cs + "%")).orderDesc(
				Properties.Lastvisit);

		final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date startDate;
		try {
			startDate = sdf.parse(txtFilter.getText().toString());

			qb.where(Properties.P_detail.like("%" + cs + "%"),
					Properties.Lastvisit.ge(startDate)).orderDesc(
					Properties.Lastvisit);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return qb.list();
	}

	public void notifylist() {
		mDataList.clear();
		List<Patient> qb = getQuery();
		mDataList.addAll(qb);
		mGridAdapter.notifyDataSetChanged();
		updateCount();
	}

	private void showCalender(final TextView txtView) {
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
						notifylist();
					}
				}, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
				cal.get(Calendar.DAY_OF_MONTH));
		datepicker.show();
	}
}
