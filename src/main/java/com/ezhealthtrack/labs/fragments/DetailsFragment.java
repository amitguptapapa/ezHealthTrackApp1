package com.ezhealthtrack.labs.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ezhealthtrack.EzApp;
import com.ezhealthtrack.R;
import com.ezhealthtrack.greendao.LabAppointment;
import com.ezhealthtrack.greendao.LabAppointmentDao.Properties;
import com.ezhealthtrack.labs.adapter.LabsAppointmentAdapter;
import com.ezhealthtrack.labs.controller.LabsAppointmentController;
import com.ezhealthtrack.labs.controller.LabsAppointmentController.OnResponseApt;
import com.ezhealthtrack.util.Constants;
import com.ezhealthtrack.util.Log;

import de.greenrobot.dao.query.QueryBuilder;

public class DetailsFragment extends Fragment {
	// Container Activity must implement this interface
	private ListView listAppointments;
	private LabsAppointmentAdapter adapter;
	private TextView txtCount;
	private ProgressBar listPb;

	@Override
	public void onActivityCreated(final Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		adapter = new LabsAppointmentAdapter(getActivity(),
				Constants.TYPE_HISTORY);
		listAppointments = (ListView) getActivity().findViewById(
				R.id.list_patients);
		View listFooter = ((LayoutInflater) getActivity().getSystemService(
				Context.LAYOUT_INFLATER_SERVICE)).inflate(
				R.layout.list_progressbar, null, false);
		listAppointments.addFooterView(listFooter);
		listPb = (ProgressBar) listFooter.findViewById(R.id.list_progressbar);
		listAppointments.setAdapter(adapter);

	}

	public void onClick(final View v) {
		switch (v.getId()) {
		case R.id.txt_start_date:

			break;

		case R.id.txt_end_date:

			break;

		case R.id.btn_search:

			break;
		}

	}

	@Override
	public void onAttach(final Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(final LayoutInflater inflater,
			final ViewGroup container, final Bundle savedInstanceState) {
		return inflater.inflate(R.layout.labs_fragment_details, container,
				false);
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

	public void updateCount(String count) {
		try {
			txtCount = (TextView) getActivity().findViewById(
					R.id.txt_count_history);
			txtCount.setText(Html.fromHtml("<b>Total Results: " + count
					+ "</b>"));
		} catch (Exception e) {

		}
	}

	private QueryBuilder<LabAppointment> getQuery(String id) {
		QueryBuilder<LabAppointment> qb = EzApp.labAptDao
				.queryBuilder();
		qb.where(Properties.Bkid.eq(id));
		return qb;
	}

	public void showAppointments(final String id) {
		Log.i("", id);
			final Handler handler = new Handler();
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					listPb.setVisibility(View.VISIBLE);
					LabsAppointmentController.getAppointmentListApi(
							getActivity(), new OnResponseApt() {

								@Override
								public void onResponseListner(String response,
										String count) {
									Log.i("", response);
									if (!response.equals("error"))
										try {
											Log.i("", ""+getQuery(id).count());
											adapter.replaceLazyList(getQuery(id)
													.listLazy());
											updateCount("" + adapter.getCount());
											listPb.setVisibility(View.GONE);

										} catch (Exception e) {
											e.printStackTrace();
										}
									else {
										Log.i("", "error");
									}

								}
							}, id);

				}
			}, 200);
	}

}
