package com.ezhealthtrack.physiciansoap.model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.json.JSONException;
import org.json.JSONObject;

public class ExaminationModel {
	public HashMap<String, String> hashVitals = new HashMap<String, String>();
	public HashMap<String, String> hashSymptoms = new HashMap<String, String>();
	public HashMap<String, String> hashGE = new HashMap<String, String>();
	public HashMap<String, String> hashSE = new HashMap<String, String>();
	public HashMap<String, String> hashNote = new HashMap<String, String>();

	public void JsonParse(final JSONObject obje) {
		Iterator<String> iter;
		try {
			if (obje.has("vita")) {
				iter = obje.getJSONObject("vita").keys();
				while (iter.hasNext()) {
					final String key = iter.next();
					if (!key.equals("bp")) {
						final String value = obje.getJSONObject("vita")
								.getJSONObject(key).getString("value");
						hashVitals.put(key, value);
					} else {
						if(obje.getJSONObject("vita").get(key) instanceof JSONObject){
						if (obje.getJSONObject("vita").getJSONObject(key)
								.has("low")
								&& obje.getJSONObject("vita")
										.getJSONObject(key)
										.getJSONObject("low").has("value"))
							hashVitals.put("low", obje.getJSONObject("vita")
									.getJSONObject(key).getJSONObject("low")
									.getString("value"));
						if (obje.getJSONObject("vita").getJSONObject(key)
								.has("high")
								&& obje.getJSONObject("vita")
										.getJSONObject(key)
										.getJSONObject("high").has("value"))
							hashVitals.put("high", obje.getJSONObject("vita")
									.getJSONObject(key).getJSONObject("high")
									.getString("value"));
						}

					}
				}
			}

			hashGE.put("ge", obje.getJSONObject("ge").getString("value"));
			hashSymptoms.put("symp",
					obje.getJSONObject("symp").getString("value"));
			hashSE.put("se", obje.getJSONObject("se").getString("value"));
			hashNote.put("note", obje.getJSONObject("note").getString("value"));
		} catch (final JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	public void updateJson(final JSONObject soap) {
		try {
			final JSONObject obje = soap.getJSONObject("obje");
			final JSONObject vita = new JSONObject();
			final JSONObject bp = new JSONObject();
			for (final Entry<String, String> entry : hashVitals.entrySet()) {
				final JSONObject obj = new JSONObject();
				obj.put("value", entry.getValue());
				if (!entry.getKey().equals("low")
						&& !entry.getKey().equals("high"))
					vita.put(entry.getKey(), obj);
				else
					bp.put(entry.getKey(), obj);
			}
			vita.put("bp", bp);
			obje.remove("vita");
			obje.put("vita", vita);
			final JSONObject ge = new JSONObject();
			ge.put("value", hashGE.get("ge"));
			obje.put("ge", ge);
			final JSONObject symp = new JSONObject();
			symp.put("value", hashSymptoms.get("symp"));
			obje.put("symp", symp);
			final JSONObject se = new JSONObject();
			se.put("value", hashSE.get("se"));
			obje.put("se", se);
			final JSONObject note = new JSONObject();
			note.put("value", hashNote.get("note"));
			obje.put("note", note);

		} catch (final JSONException e) {

			e.printStackTrace();
		}
	}

}
