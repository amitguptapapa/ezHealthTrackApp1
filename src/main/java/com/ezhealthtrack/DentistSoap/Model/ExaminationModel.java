package com.ezhealthtrack.DentistSoap.Model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class ExaminationModel {
	public HashMap<String, String> hashVitals = new HashMap<String, String>();
	public GeneralExaminationModel generalExamination = new GeneralExaminationModel();
	public OralExaminationModel oralExamination = new OralExaminationModel();

	@SuppressWarnings("unchecked")
	public void JsonParse(final JSONObject obje) {
		hashVitals.clear();
		Iterator<String> iter;
		try {
			if (obje.has("vita") && obje.get("vita") instanceof JSONObject) {
				iter = obje.getJSONObject("vita").keys();
				while (iter.hasNext()) {
					final String key = iter.next();
					if (!key.equals("bp")) {
						final String value = obje.getJSONObject("vita")
								.getJSONObject(key).getString("value");
						hashVitals.put(key, value);
					} else {
						try{
						if (obje.getJSONObject("vita").getJSONObject(key)
								.has("low")
								&& obje.getJSONObject("vita")
										.getJSONObject(key)
										.getJSONObject("low").has("value"))
							hashVitals.put("low", obje.getJSONObject("vita")
									.getJSONObject(key).getJSONObject("low")
									.getString("value"));
						}catch(Exception e){
							e.printStackTrace();
						}try{
						if (obje.getJSONObject("vita").getJSONObject(key)
								.has("high")
								&& obje.getJSONObject("vita")
										.getJSONObject(key)
										.getJSONObject("high").has("value"))
							hashVitals.put("high", obje.getJSONObject("vita")
									.getJSONObject(key).getJSONObject("high")
									.getString("value"));
						}catch(Exception e){
							
						}

					}
				}
			}
			generalExamination.JsonParse(obje.getJSONObject("ge"));
			oralExamination.JsonParse(obje.getJSONObject("oe"));
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
			// Log.i("", obje.toString());
			generalExamination.updateJson(obje);
			oralExamination.updateJson(obje);
		} catch (final JSONException e) {
			JSONObject obje;
			try {
				obje = new JSONObject();
				soap.put("obje", obje);
				final JSONObject vita = new JSONObject();
				for (final Entry<String, String> entry : hashVitals.entrySet()) {
					final JSONObject obj = new JSONObject();
					obj.put("value", entry.getValue());
					vita.put(entry.getKey(), obj);
				}
				obje.put("vita", vita);
				generalExamination.updateJson(obje);
				oralExamination.updateJson(obje);
			} catch (final JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
}
