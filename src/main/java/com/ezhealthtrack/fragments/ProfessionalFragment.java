package com.ezhealthtrack.fragments;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.app.Dialog;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ezhealthrack.api.APIs;
import com.ezhealthtrack.EzApp;
import com.ezhealthtrack.R;
import com.ezhealthtrack.activity.DashboardActivity;
import com.ezhealthtrack.model.Professional;
import com.ezhealthtrack.util.Constants;
import com.ezhealthtrack.util.Util;

public class ProfessionalFragment extends Fragment implements OnClickListener {
	private static Professional model;
	private EditText editExpertise;
	private EditText editExpertise1;
	private EditText editExperience;
	private EditText editExpdec;
	private EditText editawards;
	private Spinner spinnerSpecal;
	private Spinner spinnerTemplate;

	@Override
	public void onActivityCreated(final Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		model = DashboardActivity.profile.getProfessional();
		editExpertise = (EditText) getActivity().findViewById(
				R.id.edit_expertise);
		editExpertise.setText(model.getExpertise1());
		editExpertise1 = (EditText) getActivity().findViewById(
				R.id.edit_expertise1);
		editExpertise1.setText(model.getExpertise2());
		editExperience = (EditText) getActivity().findViewById(
				R.id.edit_experience);
		editExperience.setText(model.getDoc_exp());
		editExpdec = (EditText) getActivity().findViewById(R.id.edit_expdec);
		editExpdec.setText(model.getExp_desc());
		editawards = (EditText) getActivity().findViewById(R.id.edit_awards);
		editawards.setText(model.getAwards());

		spinnerSpecal = (Spinner) getActivity().findViewById(R.id.spinner1);
		final String[] items = new String[] { "Select", "Dentist",
				"Cardiac Surgeon", "Cardiologist", "Consultant Physician",
				"Dermatologists", "Ear-Nose-Throat", "Endocrinologists",
				"Eye/ Ophthalmologist", "Gastroenterologists",
				"Gynecologist/ Obstetrician", "Infertility Specialist",
				"Nephrologists", "Neurologists", "Neurosurgeon", "Oncologists",
				"Orthopedics", "Pain Management", "Pathologist",
				"Pediatric Neurologist", "Pediatricians", "Phlebologist",
				"Plastic Surgeon", "Psychiatrists", "Pulmonologists",
				"Physiotherapist", "Radiologists", "Rheumatology", "Surgeon",
				"Urologists" };
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_spinner_item, items);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerSpecal.setAdapter(adapter);
		spinnerTemplate = (Spinner) getActivity().findViewById(R.id.spinner2);
		spinnerTemplate.setActivated(false);
		spinnerTemplate.setEnabled(false);
		String[] item = new String[] { " Choose Template ",
				" Dentist Template ", "Physiotherapist Template",
				" Physician Template " };
		ArrayAdapter<String> adapters = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_spinner_item, item);
		adapters.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerTemplate.setAdapter(adapters);

		Log.i(model.getSpecal(), model.getTemplate());
		if (model.getSpecal() != null)
			for (int i = 0; i < items.length; i++) {
				if (model.getSpecal().equals(items[i])) {
					spinnerSpecal.setSelection(i);
				}
			}

		if (model.getTemplate().equals("soap_dentist_v1")) {
			spinnerTemplate.setSelection(1);
		} else if (model.getTemplate().equals("soap_physiotherapist_v1")) {
			spinnerTemplate.setSelection(2);
		} else if (model.getTemplate().equals("soap_physician_v1")) {
			spinnerTemplate.setSelection(3);
		}
		spinnerSpecal.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				model.setSpecal(items[position]);
				if (items[position].equals("Dentist")) {
					spinnerTemplate.setSelection(1);
				} else if (items[position].equals("physiotherapist")) {
					spinnerTemplate.setSelection(2);
				} else if (items[position].equals("physician")) {
					spinnerTemplate.setSelection(3);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

		getActivity().findViewById(R.id.button_prof).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						model.setExpertise1(editExpertise.getText().toString());
						model.setExpertise2(editExpertise1.getText().toString());
						model.setDoc_exp(editExperience.getText().toString());
						model.setExp_desc(editExpdec.getText().toString());
						model.setAwards(editawards.getText().toString());
						model.setSpecal(spinnerSpecal.getSelectedItem()
								.toString());
						if (spinnerTemplate.getSelectedItem().equals("dentist")) {
							model.setTemplate("soap_dentist_v1");
						} else {
							model.setTemplate("soap_physician_v1");
						}
						submit();

					}
				});

	}

	@Override
	public void onClick(final View v) {
		switch (v.getId()) {
		}

	}

	@Override
	public View onCreateView(final LayoutInflater inflater,
			final ViewGroup container, final Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_professional, container,
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

	private void submit() {
		final String url = APIs.UPDATE_PROFESSIONAL();
		Log.i(url, url);
		final Dialog loaddialog = Util.showLoadDialog(getActivity());
		final StringRequest schedulePlanRequest = new StringRequest(
				Request.Method.POST, url, new Response.Listener<String>() {
					@Override
					public void onResponse(final String response) {
						Log.i("", response);
						JSONObject jObj;
						try {
							jObj = new JSONObject(response);
							if (jObj.getString("s").equals("200")) {
								Editor editor = EzApp.sharedPref.edit();
								editor.putString(Constants.DR_SPECIALITY,
										model.getSpecal());
								editor.commit();
								Util.Alertdialog(getActivity(),
										"Profile  updated successfully");

							} else {
								Util.Alertdialog(getActivity(),
										jObj.getString("m"));
							}
						} catch (Exception e) {
							Util.Alertdialog(getActivity(), e.toString());
						}
						loaddialog.dismiss();
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(final VolleyError error) {
						Util.Alertdialog(getActivity(), error.toString());
						loaddialog.dismiss();
					}
				}) {
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				final HashMap<String, String> loginParams = new HashMap<String, String>();
				loginParams.put("auth-token", Util
						.getBase64String(EzApp.sharedPref.getString(
								Constants.USER_TOKEN, "")));
				return loginParams;
			}

			@Override
			protected Map<String, String> getParams() {
				final HashMap<String, String> loginParams = new HashMap<String, String>();
				loginParams.put("specal", model.getSpecal());
				loginParams.put("template", model.getTemplate());
				loginParams.put("expertise1", model.getExpertise1());
				loginParams.put("expertise2", model.getExpertise2());
				loginParams.put("doc_exp", model.getDoc_exp());
				loginParams.put("exp_desc", model.getExp_desc());
				loginParams.put("awards", model.getAwards());

				return loginParams;
			}

		};
		schedulePlanRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 5,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		EzApp.mVolleyQueue.add(schedulePlanRequest);
	}

	public void updateData() {
		model = DashboardActivity.profile.getProfessional();
		editExpertise.setText(model.getExpertise1());
		editExpertise1.setText(model.getExpertise2());
		editExperience.setText(model.getDoc_exp());
		editExpdec.setText(model.getExp_desc());
		editawards.setText(model.getAwards());

		final String[] items = new String[] { "Select", "Dentist",
				"Cardiac Surgeon", "Cardiologist", "Consultant Physician",
				"Dermatologists", "Ear-Nose-Throat", "Endocrinologists",
				"Eye/ Ophthalmologist", "Gastroenterologists",
				"Gynecologist/ Obstetrician", "Infertility Specialist",
				"Nephrologists", "Neurologists", "Neurosurgeon", "Oncologists",
				"Orthopedics", "Pain Management", "Pathologist",
				"Pediatric Neurologist", "Pediatricians", "Phlebologist",
				"Plastic Surgeon", "Psychiatrists", "Pulmonologists",
				"Radiologists", "Rheumatology", "Surgeon", "Urologists" };
		if (model.getSpecal() != null)
			for (int i = 0; i < items.length; i++) {
				if (model.getSpecal().equals(items[i])) {
					spinnerSpecal.setSelection(i);
				}
			}

		if (model.getTemplate().equals("soap_dentist_v1")) {
			spinnerTemplate.setSelection(1);
		} else {
			spinnerTemplate.setSelection(2);
		}
	}

}
