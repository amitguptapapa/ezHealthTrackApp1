package com.ezhealthtrack.DentistSoap.Model;

import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

import com.ezhealthtrack.util.Util;

//import android.util.Log;

public class RadiologyModel {
	public HashMap<String, String> hashRadiology = new HashMap<String, String>();

	public String getLabString() {
		String s = " ";
		try {
			if (hashRadiology.get("iopa").equals("on")) {
				if (!Util.isEmptyString(hashRadiology.get("value")))
					s = s + "IOPA (" + hashRadiology.get("value") + ") , ";
				else
					s = s + "IOPA ,";
			}
			if (hashRadiology.get("opg").equals("on")) {
				s = s + "OPG ,";
			}
			if (hashRadiology.get("lc").equals("on")) {
				s = s + "Lateral cephalogram ,";
			}
			if (hashRadiology.get("cts").equals("on")) {
				s = s + "CT Scan ,";
			}
			if (hashRadiology.get("mri").equals("on")) {
				s = s + "MRI ,";
			}
			if (Util.isEmptyString(hashRadiology.get("othe"))) {
				s = s + ".";
			} else {
				s = s + hashRadiology.get("othe") + " .";
				// Log.i(s, hashRadiology.get("othe"));
			}
		} catch (final Exception e) {
		}

		return s;
	}

	public void JsonParse(final JSONObject radi) {
		Iterator<String> iter;
		try {
			iter = radi.keys();
			while (iter.hasNext()) {
				final String key = iter.next();
				String value = "";
				if (key.equals("othe")) {
					if (radi.get(key) instanceof JSONObject
							&& radi.getJSONObject(key).has("value"))
						value = radi.getJSONObject(key).getString("value");
				} else if (key.equals("iopa")) {
					if (radi.getJSONObject(key).has("cb"))
						value = radi.getJSONObject(key).getString("cb");
					if (radi.getJSONObject(key).has("value"))
						hashRadiology.put("value", radi.getJSONObject(key)
								.getString("value"));
				} else {
					value = radi.getString(key);
				}
				hashRadiology.put(key, value);
			}
		} catch (final JSONException e1) {
			e1.printStackTrace();
		}
	}

	public void updateJson(final JSONObject plan) {
		try {
			final JSONObject radi = new JSONObject();
			final JSONObject iopa = new JSONObject();
			iopa.put("cb", hashRadiology.get("iopa"));
			iopa.put("value", hashRadiology.get("value"));
			radi.put("iopa", iopa);
			final JSONObject othe = new JSONObject();
			othe.put("value", hashRadiology.get("othe"));
			radi.put("othe", othe);
			radi.put("opg", hashRadiology.get("opg"));
			radi.put("lc", hashRadiology.get("lc"));
			radi.put("cts", hashRadiology.get("cts"));
			radi.put("mri", hashRadiology.get("mri"));
			plan.put("radi", radi);
		} catch (final JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
