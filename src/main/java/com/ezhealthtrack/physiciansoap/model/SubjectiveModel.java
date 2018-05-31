package com.ezhealthtrack.physiciansoap.model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.json.JSONException;
import org.json.JSONObject;

import com.ezhealthtrack.util.Log;

public class SubjectiveModel {
	public HashMap<String, String> hashHP = new HashMap<String, String>();
	public HashMap<String, String> hashCC = new HashMap<String, String>();
	public Boolean smoke = false;
	public Boolean alcohol = false;

	public void JsonParse(final JSONObject subj) {
		Iterator<String> iter;
		try {
			iter = subj.getJSONObject("hp").keys();
			while (iter.hasNext()) {
				final String key = iter.next();
				final String value = subj.getJSONObject("hp")
						.getJSONObject(key).getString("value");
				hashHP.put(key, value);
				Log.i("SM", "K:" + key + ", V:" + value);
			}
			if (subj.has("smok") && subj.getString("smok").equals("on")) {
				smoke = true;
			} else {
				smoke = false;
			}

			if (subj.has("alco") && subj.get("alco").equals("on")) {
				alcohol = true;
			} else {
				alcohol = false;
			}
			hashCC.put("cc", subj.getJSONObject("cc").getString("value"));
		} catch (final JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	public void updateJson(final JSONObject soap) {
		final JSONObject subj = new JSONObject();
		final JSONObject cc = new JSONObject();
		try {
			cc.put("value", hashCC.get("cc"));
			subj.put("cc", cc);
			final JSONObject hp = new JSONObject();
			for (final Entry<String, String> entry : hashHP.entrySet()) {
				final JSONObject obj = new JSONObject();
				obj.put("value", entry.getValue());
				hp.put(entry.getKey(), obj);
			}
			if (smoke) {
				soap.getJSONObject("subj").put("smok", "on");
			} else {
				soap.getJSONObject("subj").put("smok", "off");
			}

			if (alcohol) {
				soap.getJSONObject("subj").put("alco", "on");
			} else {
				soap.getJSONObject("subj").put("alco", "off");
			}

			subj.put("hp", hp);
			soap.put("subj", subj);
		} catch (final JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
