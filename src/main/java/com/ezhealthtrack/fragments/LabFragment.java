package com.ezhealthtrack.fragments;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.ListView;

import com.ezhealthtrack.R;
import com.ezhealthtrack.activity.DashboardActivity;
import com.ezhealthtrack.adapter.LabPreferenceAdapter;
import com.ezhealthtrack.controller.AutoSuggestController;
import com.ezhealthtrack.controller.AutoSuggestController.OnAutoSuggest;
import com.ezhealthtrack.controller.DoctorController;
import com.ezhealthtrack.labs.model.LabTestAutoSuggest;
import com.ezhealthtrack.model.LabPreference;
import com.ezhealthtrack.util.Util;

public class LabFragment extends Fragment {
	// Container Activity must implement this interface
	// public interface NextPageListner {
	// public void nextPageListner(int position);
	// }

	private ListView listLab;
	private LabPreferenceAdapter adapterPreference;
	private ArrayAdapter<LabTestAutoSuggest> adapter;

	// NextPageListner mCallback;

	private AutoCompleteTextView editFilter;
	private LabTestAutoSuggest selectedPref;

	@Override
	public void onActivityCreated(final Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		selectedPref = null;
		adapterPreference = new LabPreferenceAdapter(getActivity(),
				R.layout.row_checked_item, DashboardActivity.arrLabSelected);
		listLab = (ListView) getActivity().findViewById(R.id.list_lab);
		listLab.setAdapter(adapterPreference);
		listLab.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				CheckBox cb = (CheckBox) arg1.findViewById(R.id.checked_item);
				cb.setChecked(!cb.isChecked());
				DashboardActivity.arrLabSelected.get(arg2).isChecked = cb
						.isChecked();
				Log.i("" + arg2, "" + cb.isChecked());
			}
		});
		editFilter = (AutoCompleteTextView) getActivity().findViewById(
				R.id.edit_name);
		editFilter.setText("");
		adapter = new ArrayAdapter<LabTestAutoSuggest>(getActivity(),
				android.R.layout.simple_dropdown_item_1line);
		editFilter.setAdapter(adapter);
		editFilter.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				selectedPref = adapter.getItem(arg2);
				Log.i(selectedPref.getName(), selectedPref.getId());
			}
		});
		editFilter.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(final Editable arg0) {
				// Log.i("LabFragment:", "afterTextChanged");
			}

			@Override
			public void beforeTextChanged(final CharSequence arg0,
					final int arg1, final int arg2, final int arg3) {
				// Log.i("LabFragment:", "beforeTextChanged");
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

				// Log.i("LabFragment", "onTextChanged" +
				// s.toString().length());

				AutoSuggestController.autoSuggestLabTest(s.toString(),
						getActivity(), new OnAutoSuggest() {

							@Override
							public void OnAutoSuggestListner(Object list) {
								ArrayList<LabTestAutoSuggest> arr = (ArrayList<LabTestAutoSuggest>) list;
								adapter.clear();
								adapter.addAll(arr);
								adapter.notifyDataSetChanged();
							}
						});
			}

		});

		getActivity().findViewById(R.id.button_add_lab).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						try {
							boolean flag = false;
							for (LabPreference pref : DashboardActivity.arrLabSelected) {
								if (pref.getName().equalsIgnoreCase(
										editFilter.getText().toString()))
									flag = true;
							}
							if (Util.isEmptyString(editFilter.getText()
									.toString())) {
								Util.Alertdialog(getActivity(),
										"Please provide a lab test name.");
							} else if (!editFilter.getText().toString()
									.contains(selectedPref.getName())) {
								Util.Alertdialog(getActivity(),
										"Please select a lab test from list.");
							} else if (flag) {
								Util.Alertdialog(getActivity(),
										"Lab test already added.");
							} else {
								LabPreference lp = new LabPreference();
								lp.isChecked = true;
								lp.setName(selectedPref.getName());
								lp.setId(selectedPref.getId());

								// update on server
								DoctorController.updateLabPreference(lp,
										getActivity());

								// update local
								DashboardActivity.arrLabSelected.add(lp);
								adapterPreference = new LabPreferenceAdapter(
										getActivity(),
										R.layout.row_checked_item,
										DashboardActivity.arrLabSelected);
								Log.i("" + adapterPreference.getCount(),
										""
												+ DashboardActivity.arrLabSelected
														.size());
								listLab.setAdapter(adapterPreference);
								editFilter.setText("");
								//selectedPref = null;
							}
						} catch (Exception e) {
							Util.Alertdialog(getActivity(),
									"Please select a lab test from list.");
						}
					}
				});

		getActivity().findViewById(R.id.button_submit_lab).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						((DashboardActivity) getActivity()).postLabPref();

						// clear from local objects
						ArrayList<LabPreference> list = new ArrayList<LabPreference>();
						for (final LabPreference lp : DashboardActivity.arrLabSelected) {
							if (lp.isChecked) {
								list.add(lp);
								// Log.i("Submit", "Keepinging LP:" +
								// lp.getName());
							} else {
								// Log.i("Submit", "Removing LP:" +
								// lp.getName());
							}
						}
						DashboardActivity.arrLabSelected.clear();
						for (final LabPreference lp : list) {
							DashboardActivity.arrLabSelected.add(lp);
						}
						adapterPreference.notifyDataSetChanged();
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
		return inflater.inflate(R.layout.fragment_lab, container, false);
	}

	@Override
	public void onResume() {
		adapterPreference.notifyDataSetChanged();
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

}
