package com.ezhealthtrack.fragments;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.ezhealthtrack.R;
import com.ezhealthtrack.adapter.HistoryListAdapter;
import com.ezhealthtrack.controller.HistoryListController;
import com.ezhealthtrack.util.Util;

public class HistoryListFragment extends EzListFragment implements
		OnClickListener {
	private TextView mCountView;
	private AutoCompleteTextView mEditFilter;
	public static String nextUrl;

	private TextView txtStartDate;
	private TextView txtEndDate;
	private Date startDate;
	private Date endDate;

	View mButtonsBar;

	public HistoryListFragment() {
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
		mController = new HistoryListController(1);
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(final LayoutInflater inflater,
			final ViewGroup container, final Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_history_list, container,
				false);
		super.onCreateListView(view, inflater);

		mButtonsBar = view.findViewById(R.id.id_buttons_bar);
		mButtonsBar.setVisibility(View.GONE);

		mListAdapter = new HistoryListAdapter(mDataList, this);
		mListView.setAdapter(mListAdapter);

		TextView title = (TextView) view.findViewById(R.id.txt_screen_title);
		title.setText("Appointments - History");

		view.findViewById(R.id.edit_name_view).setVisibility(View.VISIBLE);
		view.findViewById(R.id.start_date_view).setVisibility(View.VISIBLE);
		view.findViewById(R.id.end_date_view).setVisibility(View.VISIBLE);
		view.findViewById(R.id.edit_date_view).setVisibility(View.GONE);
		view.findViewById(R.id.btn_search).setVisibility(View.VISIBLE);
		view.findViewById(R.id.line_2).setVisibility(View.VISIBLE);
		// search button
		view.findViewById(R.id.btn_search).setOnClickListener(this);

		// unread/total item count
		mCountView = (TextView) view.findViewById(R.id.txt_count);

		// filter text
		mEditFilter = (AutoCompleteTextView) view.findViewById(R.id.edit_name);
		mEditFilter.setHint("Patient Name");

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
			}
		});

		txtStartDate = (TextView) view.findViewById(R.id.txt_start_date);
		txtStartDate.setOnClickListener(this);
		Util.filterClear(txtStartDate,
				view.findViewById(R.id.img_start_date_clear_filter));
		txtEndDate = (TextView) view.findViewById(R.id.txt_end_date);
		txtEndDate.setOnClickListener(this);
		Util.filterClear(txtEndDate,
				view.findViewById(R.id.img_end_date_clear_filter));

		// setup UI
		Util.setupUI(getActivity(), view.findViewById(R.id.rl_history));

		// Get initial data
		mController.getPage(0, null, this);

		// 1. View Appointment Details
		Button btnView = (Button) view.findViewById(R.id.btn_view);
		btnView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				EzCommonActions.appointmentHistoryView(getActivity(),
						mListAdapter.getSelectedAppointment());
			}
		});

		// 2. Reschedule
		Button btnReschedule = (Button) view.findViewById(R.id.btn_reschedule);
		btnReschedule.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				EzCommonActions.appointmentHistoryReschedule(getActivity(),
						mListAdapter.getSelectedAppointment());
			}
		});

		// 3. Follow up
		Button btnFollowUp = (Button) view.findViewById(R.id.btn_follow);
		btnFollowUp.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				EzCommonActions.appointmentHistoryFollowUp(getActivity(),
						mListAdapter.getSelectedAppointment());
			}
		});

		// 4. Send Message
		Button btnSendMessage = (Button) view
				.findViewById(R.id.btn_sendmessage);
		btnSendMessage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				EzCommonActions.appointmentHistorySendMessage(getActivity(),
						mListAdapter.getSelectedAppointment());
			}
		});

		// 5. Prescription
		Button btnPrescription = (Button) view
				.findViewById(R.id.btn_prescription);
		btnPrescription.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				EzCommonActions.appointmentHistoryPrescription(getActivity(),
						mListAdapter.getSelectedAppointment());
			}
		});

		// 6. Visit Notes
		Button btnVisitNotes = (Button) view.findViewById(R.id.btn_visitnotes);
		btnVisitNotes.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				EzCommonActions.appointmentHistoryVisitNotes(getActivity(),
						mListAdapter.getSelectedAppointment());
			}
		});

		return view;
	}

	@Override
	public void onActivityCreated(final Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onResume() {

		// updateCount();
		super.onResume();
	}

	@Override
	public void onSaveInstanceState(final Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onStart() {
		// EzUtils.hideKeyBoard(getActivity());
		super.onStart();
	}

	// public void updateCount() {
	// try {
	// List<Appointment> qb = getQuery();
	// mCountView = (TextView) getActivity().findViewById(R.id.txt_count);
	// mCountView.setText(Html.fromHtml("<b>Total Results: " + qb.size()
	// + "</b>"));
	// } catch (Exception e) {
	//
	// }
	// }

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
	public void onClick(final View v) {
		switch (v.getId()) {
		case R.id.txt_start_date:
			showCalender(txtStartDate, "startDate");
			break;

		case R.id.txt_end_date:
			showCalender(txtEndDate, "endDate");
			break;

		case R.id.btn_search:
			break;
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
								endDate.setTime(endDate.getTime() + 24 * 60
										* 60 * 1000);
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

	public void onListItemClick(boolean selected) {
		// show / hide action buttons
		if (selected == true) {
			mButtonsBar.setVisibility(View.VISIBLE);
		} else {
			mButtonsBar.setVisibility(View.GONE);
		}
	}

	// private QueryBuilder<Appointment> getQuery() {
	// QueryBuilder<Appointment> qb = EzApp.aptDao
	// .queryBuilder();
	// if (startDate != null)
	// qb.where(Properties.Aptdate.ge(startDate));
	// if (endDate != null)
	// qb.where(Properties.Aptdate.le(endDate));
	// String s = mEditFilter.getText().toString().replace("  ", " ");
	// qb.whereOr(Properties..like("%" + s + "%"),
	// Properties.Pat_unique_id.like("%" + s + "%"));
	// qb.orderDesc(Properties.Last_visit);
	// return qb;
	// }

}
