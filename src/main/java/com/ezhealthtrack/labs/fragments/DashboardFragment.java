package com.ezhealthtrack.labs.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ezhealthtrack.R;
import com.ezhealthtrack.labs.model.LabsDashboardModel;

public class DashboardFragment extends Fragment implements OnClickListener {
	public LabsDashboardModel model = new LabsDashboardModel();
	private TextView txtAptMessage;
	private TextView txtMedicine;
	private TextView txtPrescription;
	private TextView txtInfo;

	@Override
	public void onActivityCreated(final Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		txtAptMessage = (TextView) getActivity().findViewById(
				R.id.txt_message_apt);
		txtMedicine = (TextView) getActivity().findViewById(R.id.txt_medicine);
		txtPrescription = (TextView) getActivity().findViewById(
				R.id.txt_prescriptions);
		((TextView) getActivity().findViewById(
				R.id.txt_pres)).setText("Lab Tests");
		txtInfo = (TextView) getActivity().findViewById(R.id.txt_info);
		updateData();
	}

	@Override
	public void onClick(final View v) {
		switch (v.getId()) {

		}

	}

	@Override
	public View onCreateView(final LayoutInflater inflater,
			final ViewGroup container, final Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_dashboard_old, container, false);
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

	public void updateData() {
		txtAptMessage.setText("");
		for (final String s : model
				.getAppointmentsMessages()) {
			txtAptMessage.append("\n" + s);
		}
		txtMedicine.setText("");
		for (final String s : model.getMedicineResearch()) {
			txtMedicine.append("\n" + s);
		}
		txtPrescription.setText("");
		for (final String s : model.getLabTests()) {
			txtPrescription.append("\n" + s);
		}
		txtInfo.setText("");
		for (final String s : model
				.getEzHealthTrackInformation()) {
			txtInfo.append("\n" + s);
		}
	}
	
	
}
