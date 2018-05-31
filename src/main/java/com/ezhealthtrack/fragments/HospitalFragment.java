package com.ezhealthtrack.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.ezhealthtrack.R;
import com.ezhealthtrack.activity.DashboardActivity;
import com.ezhealthtrack.adapter.HospitalAdapter;
import com.ezhealthtrack.util.Util;

public class HospitalFragment extends Fragment {

	private ListView listPatients;
	private HospitalAdapter adapter;

	public void notifyList() {
		adapter.notifyDataSetChanged();
	}

	@Override
	public void onActivityCreated(final Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		adapter = new HospitalAdapter(getActivity(), R.layout.row_hospital,
				DashboardActivity.arrHospital);
		listPatients = (ListView) getActivity()
				.findViewById(R.id.list_patients);
		listPatients.setAdapter(adapter);
		
	}

	@Override
	public View onCreateView(final LayoutInflater inflater,
			final ViewGroup container, final Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_hospital, container, false);
	}

	@Override
	public void onResume() {
		adapter.notifyDataSetChanged();
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
		adapter.notifyDataSetChanged();	
	}

}
