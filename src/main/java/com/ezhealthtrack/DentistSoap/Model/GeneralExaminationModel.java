package com.ezhealthtrack.DentistSoap.Model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.json.JSONException;
import org.json.JSONObject;

import android.widget.RadioButton;

public class GeneralExaminationModel {
	public HashMap<String, String> hashState = new HashMap<String, String>();
	public HashMap<String, String> hashCb = new HashMap<String, String>();

	public void JsonParse(final JSONObject ge) {
		Iterator<String> iter;
		try {
			iter = ge.keys();
			while (iter.hasNext()) {
				final String key = iter.next();
				String value = "";
				if (ge.get(key) instanceof JSONObject
						&& ge.getJSONObject(key).has("value"))
					value = ge.getJSONObject(key).getString("value");
				hashState.put(key, value);
				if (ge.get(key) instanceof JSONObject
						&& ge.getJSONObject(key).has("cb")) {
					hashCb.put(key, ge.getJSONObject(key).getString("cb"));
				}
			}
		} catch (final JSONException e1) {
			e1.printStackTrace();
		}
		// Log.e("", ge.toString());
	}

	public void updateJson(final JSONObject obje) {
		try {
			final JSONObject ge = new JSONObject();
			for (final Entry<String, String> entry : hashState.entrySet()) {
				final JSONObject obj = new JSONObject();
				obj.put("value", entry.getValue());
				if (entry.getKey().equals("swd")) {
					obj.put("cb", hashCb.get("swd"));
				}
				ge.put(entry.getKey(), obj);
			}
			obje.remove("ge");
			obje.put("ge", ge);
			// Log.i("", ge.toString());
		} catch (final JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
