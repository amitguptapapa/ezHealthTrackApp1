package com.ezhealthtrack.physiciansoap.model;

import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

public class OrderSetItems {

	public HashMap<String, String> items = new HashMap<String, String>();

	public void JsonParse(final JSONObject radiology) {
		Iterator<String> iter;
		try {
			iter = radiology.keys();
			while (iter.hasNext()) {
				final String key = iter.next();
				final String value = radiology.getString(key);
				items.put(key, value);
			}
		} catch (final JSONException e1) {
			e1.printStackTrace();
		}
	}

	public String toDisplayString() {
		String s = "";
		for (String key : items.keySet()) {
			s = s + key + ":" + items.get(key) + ",";
		}
		return s;
	}

}
