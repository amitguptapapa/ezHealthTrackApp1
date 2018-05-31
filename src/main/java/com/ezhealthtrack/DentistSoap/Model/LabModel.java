package com.ezhealthtrack.DentistSoap.Model;

import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.ezhealthtrack.util.Util;

public class LabModel {
	public HashMap<String, String> hashLab = new HashMap<String, String>();

	public String getLabString() {
		String s = " ";
		try {
			if (hashLab.get("bs").equals("on")) {
				s = s + "Blood Sugar (fasting = " + hashLab.get("fast")
						+ ",random = " + hashLab.get("rand") + ") , ";
			}
			if (hashLab.get("haem").equals("on")) {
				s = s + "Haemoglobin ,";
			}
			if (hashLab.get("cbc").equals("on")) {
				s = s + "Complete Blood Count ,";
			}
			if (hashLab.get("tlc").equals("on")) {
				s = s + "Total Leukocyte Count ,";
			}
			if (hashLab.get("dlc").equals("on")) {
				s = s + "Differential Leukocyte count ,";
			}
			if (hashLab.get("rbcc").equals("on")) {
				s = s + "RBC Count ,";
			}
			if (hashLab.get("pc").equals("on")) {
				s = s + "Platelet Count ,";
			}
			if (hashLab.get("he").equals("on")) {
				if (hashLab.get("biop").equals("on")) {
					s = s + " Histopathological Examination (Biopsy) ,";
				}else{
					s = s + " Histopathological Examination ,";
				}
			}
			if (Util.isEmptyString(hashLab.get("othe"))) {
				s = s + ".";
			} else {
				s = s + hashLab.get("othe");
			}
		} catch (final Exception e) {
		}

		return s;
	}

	public void JsonParse(final JSONObject lab) {
		Log.i("dentist", "dentist:"+lab.toString());
		Iterator<String> iter;
		try {
			iter = lab.keys();
			while (iter.hasNext()) {
				final String key = iter.next();
				String value = "";
				if (key.equals("othe")) {
					if (lab.get(key) instanceof JSONObject&&lab.getJSONObject(key).has("value"))
						value = lab.getJSONObject(key).getString("value");
				} else if (key.equals("bs")) {
					if (lab.get(key) instanceof JSONObject&&lab.getJSONObject(key).has("cb"))
						value = lab.getJSONObject(key).getString("cb");
					if (lab.get(key) instanceof JSONObject&&lab.getJSONObject(key).getJSONObject("fast")
							.has("value"))
						hashLab.put("fast", lab.getJSONObject(key)
								.getJSONObject("fast").getString("value"));
					if (lab.get(key) instanceof JSONObject&&lab.getJSONObject(key).getJSONObject("rand")
							.has("value"))
						hashLab.put("rand", lab.getJSONObject(key)
								.getJSONObject("rand").getString("value"));
				} else if (key.equals("he")) {
					if (lab.get(key) instanceof JSONObject&&lab.getJSONObject(key).has("cb"))
						value = lab.getJSONObject(key).getString("cb");
					hashLab.put("biop", lab.getJSONObject(key)
							.getString("biop"));
				} else {
					if (lab.get(key) instanceof JSONObject
							&& lab.getJSONObject(key).has("cb"))
						value = lab.getJSONObject(key).getString("cb");
					else
						value = "";
				}
				hashLab.put(key, value);
			}
		} catch (final JSONException e1) {
			e1.printStackTrace();
		}
	}

	public void updateJson(final JSONObject plan) {
		try {
			final JSONObject lab = new JSONObject();
			final JSONObject bs = new JSONObject();
			bs.put("cb", hashLab.get("bs"));
			final JSONObject fast = new JSONObject();
			fast.put("value", hashLab.get("fast"));
			bs.put("fast", fast);
			final JSONObject rand = new JSONObject();
			rand.put("value", hashLab.get("rand"));
			bs.put("rand", rand);
			lab.put("bs", bs);
			final JSONObject othe = new JSONObject();
			othe.put("value", hashLab.get("othe"));
			lab.put("othe", othe);
			final JSONObject haem = new JSONObject();
			haem.put("cb", hashLab.get("haem"));
			lab.put("haem", haem);
			final JSONObject cbc = new JSONObject();
			cbc.put("cb", hashLab.get("cbc"));
			lab.put("cbc", cbc);
			final JSONObject tlc = new JSONObject();
			tlc.put("cb", hashLab.get("tlc"));
			lab.put("tlc", tlc);
			final JSONObject dlc = new JSONObject();
			dlc.put("cb", hashLab.get("dlc"));
			lab.put("dlc", dlc);
			final JSONObject rbcc = new JSONObject();
			rbcc.put("cb", hashLab.get("rbcc"));
			lab.put("rbcc", rbcc);
			final JSONObject pc = new JSONObject();
			pc.put("cb", hashLab.get("pc"));
			lab.put("pc", pc);
			final JSONObject he = new JSONObject();
			he.put("cb", hashLab.get("he"));
			final JSONObject biop = new JSONObject();
			biop.put("biop", hashLab.get("biop"));
			he.put("biop", biop);
			lab.put("he", he);
			plan.put("lab", lab);

		} catch (final JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
