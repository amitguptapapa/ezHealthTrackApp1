package com.ezhealthtrack.DentistSoap.Model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.json.JSONException;
import org.json.JSONObject;

import com.ezhealthtrack.util.Log;

public class SoftTissueExaminationModel {
	public HashMap<String, String> hashState = new HashMap<String, String>();

	public void JsonParse(final JSONObject ste) {
		Iterator<String> iter;
		try {
			iter = ste.keys();
			while (iter.hasNext()) {
				final String key = iter.next();
				if (key.equals("ging")) {
					final JSONObject ging = ste.getJSONObject("ging");
					final Iterator<String> iter1 = ging.keys();
					while (iter1.hasNext()) {
						final String key1 = iter1.next();
						String value1 = "";
						if(ging.getJSONObject(key1)
								.has("value"))
						value1 = ging.getJSONObject(key1)
								.getString("value");
						hashState.put(key1, value1);
					}
				} else {
					String value = "";
					if(ste.getJSONObject(key).has(
							"value"))
					value = ste.getJSONObject(key).getString(
							"value");
					hashState.put(key, value);
				}
			}
			// Log.i("", hashState.toString());
		} catch (final JSONException e1) {
			e1.printStackTrace();
		}
		Log.e("", ste.toString());
	}

	public void updateJson(final JSONObject oe) {
		try {
			final JSONObject ste = new JSONObject();
			final JSONObject ging = new JSONObject();
			for (final Entry<String, String> entry : hashState.entrySet()) {
				final JSONObject obj = new JSONObject();
				obj.put("value", entry.getValue());
				if (entry.getKey().equals("colo")
						|| entry.getKey().equals("cont")
						|| entry.getKey().equals("cons")) {
					ging.put(entry.getKey(), obj);
				} else {
					ste.put(entry.getKey(), obj);
				}
			}
			ste.put("ging", ging);
			oe.remove("ste");
			oe.put("ste", ste);
			// Log.i("", ste.toString());
		} catch (final JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
