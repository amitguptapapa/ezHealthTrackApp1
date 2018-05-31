package com.ezhealthtrack.fragments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ezhealthrack.api.APIs;
import com.ezhealthtrack.EzApp;
import com.ezhealthtrack.R;
import com.ezhealthtrack.adapter.HospitalAdapter;
import com.ezhealthtrack.model.HospitalModel;
import com.ezhealthtrack.util.Constants;
import com.ezhealthtrack.util.Log;
import com.ezhealthtrack.util.Util;
import com.google.gson.Gson;

public class HospitalSearchFragment extends Fragment {

	private ListView listPatients;
	private HospitalAdapter adapter;
	private final ArrayList<HospitalModel> arrHospital = new ArrayList<HospitalModel>();
	private int totalHospitals = 0;

	private void getHospital(final String s, final String condval,
			final int pageno) {
		final SharedPreferences sharedPref = EzApp.sharedPref;
		final String url = APIs.HOSPITALSEARCH();
		final StringRequest patientListRequest = new StringRequest(
				Request.Method.POST, url, new Response.Listener<String>() {
					@Override
					public void onResponse(final String response) {
						Log.i("get alerts", response);
						try {
							final JSONObject jObj = new JSONObject(response);
							final JSONArray data = jObj.getJSONArray("data");
							if (pageno == 1) {
								arrHospital.clear();
							}
							for (int i = 0; i < data.length(); i++) {
								final HospitalModel model = new Gson()
										.fromJson(data.getJSONObject(i)
												.toString(),
												HospitalModel.class);
								arrHospital.add(model);
							}
							if(isVisible())
							notifyList();
							if (jObj.has("count")) {
								totalHospitals = jObj.getInt("count");
							}
							// Log.i("couunt", pageno + "" + totalHospitals);
							if ((pageno * 6) < totalHospitals) {
								getHospital(s, jObj.getString("condval"),
										pageno + 1);
							}

						} catch (final JSONException e) {
							Toast.makeText(
									getActivity(),
									"There is some error while fetching patient list please try again.",
									Toast.LENGTH_SHORT).show();
							Log.e("error", e);
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(final VolleyError error) {

						Log.e("Error.Response", error);
					}
				}) {
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				final HashMap<String, String> loginParams = new HashMap<String, String>();
				loginParams.put("auth-token", Util.getBase64String(sharedPref
						.getString(Constants.USER_TOKEN, "")));
				return loginParams;
			}

			@Override
			protected Map<String, String> getParams() {
				final HashMap<String, String> loginParams = new HashMap<String, String>();
				loginParams.put("cli", "api");
				loginParams.put("search", s);
				if (pageno > 1) {
					loginParams.put("page_num", "" + pageno);
				}
				loginParams.put("condval", condval);
				return loginParams;
			}

		};
		patientListRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 5,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		EzApp.mVolleyQueue.add(patientListRequest);

	}

	public void notifyList() {
		adapter.notifyDataSetChanged();
	}

	@Override
	public void onActivityCreated(final Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		adapter = new HospitalAdapter(getActivity(), R.layout.row_hospital,
				arrHospital);
		listPatients = (ListView) getActivity()
				.findViewById(R.id.list_patients);
		listPatients.setAdapter(adapter);
		Util.setupUI(getActivity(), getActivity().findViewById(R.id.rl_hospital_search));
		getActivity().findViewById(R.id.btn_search).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(final View v) {
						getHospital(
								((TextView) getActivity().findViewById(
										R.id.edit_filter)).getText().toString(),
								"", 1);

					}
				});
	}

	@Override
	public View onCreateView(final LayoutInflater inflater,
			final ViewGroup container, final Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_hospital_search, container,
				false);
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

}
