package com.ezhealthtrack.DentistSoap.Model;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

import org.json.JSONException;
import org.json.JSONObject;

import com.ezhealthtrack.EzApp;
import com.ezhealthtrack.util.Util;

public class TreatmentDoneModel {
	public HashMap<String, String> hashTd = new HashMap<String, String>();

	public String getTreatmentDone() {
		String s = "";
		int i = 1;
		Map<String,String> hm = new TreeMap<String, String>(hashTd);
		for (final Map.Entry<String, String> entry : hm.entrySet()) {
			if (!entry.getKey().equals("td")
					&& !Util.isEmptyString(entry.getValue())) {
				String a;
				try {
					a = EzApp.sdfOnmmm
							.format(EzApp.sdfyymmddhhmmss
									.parse(entry.getKey()));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					a = "";
				}
				s = s + "Treatment note " +(i)+ " : " + entry.getValue() + a
						+ " " + "\n";
				i = i + 1;
			}
		}
		return s;
	}

	public void JsonParse(final JSONObject td) {
		hashTd.clear();
		Iterator<String> iter;
		try {
			iter = td.keys();
			while (iter.hasNext()) {
				final String key = iter.next();
				String date = "";
				if (td.getJSONObject(key).has("date")) {
					date = td.getJSONObject(key).getString("date");
				}
				String value = "";
				if(td.getJSONObject(key).has("value"))
				value = td.getJSONObject(key).getString("value");
				hashTd.put(date, value);
			}
		} catch (final JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	public void updateJson(final JSONObject plan) {
		final JSONObject td = new JSONObject();
		int i = 1;
		for (final Entry entry : hashTd.entrySet()) {
			final JSONObject tditem = new JSONObject();
			try {
				if (!entry.getKey().equals("td")) {
					tditem.put("date", entry.getKey());
				} else {
					tditem.put("date", EzApp.sdfyymmddhhmmss
							.format(new Date()));
				}
				tditem.put("value", entry.getValue());
				td.put("" + i, tditem);
				i++;
			} catch (final JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			plan.put("td", td);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
