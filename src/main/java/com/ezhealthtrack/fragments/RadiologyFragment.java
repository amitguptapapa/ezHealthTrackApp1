package com.ezhealthtrack.fragments;

import java.util.ArrayList;
import java.util.Map.Entry;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.ezhealthtrack.R;
import com.ezhealthtrack.activity.DashboardActivity;
import com.ezhealthtrack.adapter.RadiologyExpandableAdapter;
import com.ezhealthtrack.model.RadiologyModel;

public class RadiologyFragment extends Fragment {
	// Container Activity must implement this interface
	// public interface NextPageListner {
	// public void nextPageListner(int position);
	// }

	private ExpandableListView listRadiology;
	private RadiologyExpandableAdapter adapter1;
	private RadiologyModel radio;
	private ArrayList<String> arrRadi = new ArrayList<String>();
	private AutoCompleteTextView editFilter;
	private ArrayList<RadiologyModel> parentItems = new ArrayList<RadiologyModel>();
	private ArrayList<ArrayList<RadiologyModel>> childItems = new ArrayList<ArrayList<RadiologyModel>>();

	@Override
	public void onActivityCreated(final Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		listRadiology = (ExpandableListView) getActivity().findViewById(
				R.id.list_radiology);
		// Set the Items of Parent
		setGroupParents();
		// Set The Child Data
		setChildData();
		adapter1 = new RadiologyExpandableAdapter(parentItems, childItems,
				listRadiology);

		adapter1.setInflater(
				(LayoutInflater) getActivity().getSystemService(
						Context.LAYOUT_INFLATER_SERVICE), getActivity());
		listRadiology.setAdapter(adapter1);

		editFilter = (AutoCompleteTextView) getActivity().findViewById(
				R.id.edit_name);
		editFilter.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(final Editable arg0) {
			}

			@Override
			public void beforeTextChanged(final CharSequence arg0,
					final int arg1, final int arg2, final int arg3) {
			}

			@Override
			public void onTextChanged(final CharSequence cs, final int arg1,
					final int arg2, final int arg3) {
				// When user changed the Text
				
				adapter1.getChild(arg1, arg2);
			}
		});

		arrRadi.clear();

		for (com.ezhealthtrack.model.RadiologyModel m : DashboardActivity.arrRadiPreferences) {
			arrRadi.add(m.getTestName());
		}

		final ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				getActivity(), R.layout.support_simple_spinner_dropdown_item,
				arrRadi);
		editFilter.setAdapter(adapter);

		getActivity().findViewById(R.id.button_add_radi).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						
						editFilter.setText("");
					}
				});

		getActivity().findViewById(R.id.button_submit_radiology)
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						((DashboardActivity) getActivity()).postRadiologyPref();
					}
				});
	}

	// @Override
	// public void onAttach(final Activity activity) {
	// super.onAttach(activity);
	//
	// // This makes sure that the container activity has implemented
	// // the callback interface. If not, it throws an exception
	// try {
	// mCallback = (NextPageListner) activity;
	// } catch (final ClassCastException e) {
	// throw new ClassCastException(activity.toString()
	// + " must implement NextPageListner");
	// }
	// }

	@Override
	public View onCreateView(final LayoutInflater inflater,
			final ViewGroup container, final Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_radiology, container, false);
	}

	@Override
	public void onResume() {
		adapter1.notifyDataSetChanged();
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

	public void setGroupParents() {
		parentItems = new ArrayList<RadiologyModel>();
		for (RadiologyModel rm : DashboardActivity.arrRadiPreferences)
			if (rm.getParentId().equals("0"))
				parentItems.add(rm);
	}

	public void setChildData() {
		for (RadiologyModel s : parentItems) {
			ArrayList<RadiologyModel> child = new ArrayList<RadiologyModel>();
			for (RadiologyModel d : DashboardActivity.arrRadiPreferences) {
				// Add Child Items for Fruits
				if (d.getParentId().equals(s.getCodid()))
					child.add(d);
			}
			childItems.add(child);
		}

	}
}
