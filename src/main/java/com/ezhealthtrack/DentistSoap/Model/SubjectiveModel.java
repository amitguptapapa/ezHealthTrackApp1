package com.ezhealthtrack.DentistSoap.Model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.json.JSONException;
import org.json.JSONObject;

public class SubjectiveModel {
	public HashMap<String, String> hashHP = new HashMap<String, String>();
	public HashMap<String, String> hashCC = new HashMap<String, String>();

	public void JsonParse(final JSONObject subj) {
		hashHP.clear();
		hashCC.clear();
		Iterator<String> iter;
		try {
			if (subj.has("hp")) {
				iter = subj.getJSONObject("hp").keys();
				while (iter.hasNext()) {
					final String key = iter.next();
					final String value = subj.getJSONObject("hp")
							.getJSONObject(key).getString("value");
					hashHP.put(key, value);
				}
			}
			if (subj.has("cc")&&subj.getJSONObject("cc").has("value"))
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
			subj.put("hp", hp);
			soap.put("subj", subj);
		} catch (final JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
