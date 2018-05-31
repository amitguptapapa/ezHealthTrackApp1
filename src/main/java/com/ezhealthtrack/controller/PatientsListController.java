package com.ezhealthtrack.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ezhealthrack.api.APIs;
import com.ezhealthtrack.EzApp;
import com.ezhealthtrack.controller.EzNetwork.ResponseHandler;
import com.ezhealthtrack.fragments.PatientsListFragment;
import com.ezhealthtrack.greendao.Patient;
import com.ezhealthtrack.util.Log;
import com.ezhealthtrack.util.Util;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class PatientsListController extends EzController {

	public final String TAG = getClass().getSimpleName();

	public PatientsListController(int page) {
		super(page);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void getPage(final int page, Map<String, String> params,
			final UpdateListner listner) {
		if (params == null)
			params = new HashMap<String, String>();

		String url = APIs.PATIENT_LIST();
		params.put("page", Integer.toString(page));

		mNetwork.POST(url, params, new ResponseHandler() {

			@Override
			public void cmdResponseError(Integer code) {
				listner.onDataUpdateError(page);

			}

			@Override
			public void cmdResponse(JSONObject response, String result) {
				Log.i("PatientsController", "PC : " + response);
				List<Patient> patient = new ArrayList<Patient>();

				try {
					JSONArray data = response.getJSONArray("data");
					for (int i = 0; i < data.length(); ++i) {
						JSONObject item = data.getJSONObject(i);
						if (Util.isEmptyString(item.getString("last-visit"))) {
							item.remove("last-visit");
						}
						Gson gson = new GsonBuilder().setDateFormat(
								"yyyy-MM-dd").create();
						Patient pat = gson.fromJson(item.toString(),
								Patient.class);
						pat.setId((long) (Integer.parseInt(pat.getPid()) + 100000 * Integer
								.parseInt(pat.getFid())));
						patient.add(pat);

						// adding the patient data to DB for local search {
						EzApp.patientDao.insertOrReplace(pat);
						// }
					}
					listner.onDataUpdate(page, patient);
					listner.onCmdResponse(response, result);
				} catch (JSONException e) {
					Log.i("LOC", "Error : " + e.getMessage());
				}

			}
		});
	}

	@Override
	public List<?> getRecords(int count, int offset) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<?> getPage(int page, int offset, Map<String, String> params,
			UpdateListner listner) {
		// TODO Auto-generated method stub
		return null;
	}

}
