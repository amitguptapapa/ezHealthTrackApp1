package com.ezhealthtrack.physiciansoap.model;

import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

import com.ezhealthtrack.activity.DashboardActivity;
import com.ezhealthtrack.model.LabPreference;
import com.ezhealthtrack.physiciansoap.LabOrderActivity;
import com.google.gson.Gson;

public class LabModel {
	public HashMap<String, String> hashLab = new HashMap<String, String>();

	public void JsonParse(final JSONObject lab) {
		for (LabPreference s : DashboardActivity.arrLabSelected) {
			hashLab.put(
					s.getName() + LabOrderActivity.TEST_ID_SPLIT_KEY
							+ s.getId(), "off");
		}
		Iterator<String> iter;
		try {
			iter = lab.keys();
			while (iter.hasNext()) {
				final String key = iter.next();
				final String value = lab.getString(key);
				hashLab.put(key, value);
			}
		} catch (final JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	public void updateJson(final JSONObject plan) {
		try {
			plan.put("lab", new JSONObject(new Gson().toJson(hashLab)));
		} catch (final JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public String toString() {
		String s = "";
		for (String e : hashLab.keySet()) {
			s = s + e + ", ";
		}
		return s;
	}

}
