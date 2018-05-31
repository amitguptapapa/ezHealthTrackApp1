package com.ezhealthtrack.physiciansoap.model;

import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

import com.ezhealthtrack.activity.DashboardActivity;
import com.google.gson.Gson;

public class RadiologyModel {
	public static HashMap<String, String> hashRadiology = new HashMap<String, String>();

	public void JsonParse(final JSONObject radiology) {
		for(String s : DashboardActivity.arrRadiSelected){
			hashRadiology.put(s, "off");
		}
		Iterator<String> iter;
		try {
			iter = radiology.keys();
			while (iter.hasNext()) {
				final String key = iter.next();
				final String value = radiology.getString(key);
				hashRadiology.put(key, value);
			}
		} catch (final JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	public void updateJson(final JSONObject plan) {
		try {
			plan.put("radi", new JSONObject(new Gson().toJson(hashRadiology)));
		} catch (final JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	 public String toString() {
	  String s = "";
	  for(String e:hashRadiology.keySet()){
	   s = s + e + ", ";
	  }
	  return s;
	 }

}
