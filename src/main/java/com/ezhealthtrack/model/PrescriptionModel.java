package com.ezhealthtrack.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class PrescriptionModel {
	public ArrayList<MedicineModel> arrMedicine = new ArrayList<MedicineModel>();
	public HashMap<String, String> si = new HashMap<String, String>();
	// public MedicineModel tempMedicine = new MedicineModel();

	// get current date
	Calendar c = Calendar.getInstance();
	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	String formattedDate = df.format(c.getTime());

	public void JsonParse(final JSONObject pres) {

		Log.i("JsonParse-121", pres.toString());

		final Iterator<String> iter;
		try {
			arrMedicine.clear();
			if (pres.has("si") && pres.getJSONObject("si").has("value"))
				si.put("si", pres.getJSONObject("si").getString("value"));
			if (pres.has("drug")) {
				final JSONObject drug = pres.getJSONObject("drug");
				iter = drug.keys();
				while (iter.hasNext()) {
					final String key = iter.next();
					final MedicineModel medicine = new MedicineModel();
					medicine.name = drug.getJSONObject(key).getString("dn");
					medicine.strength = drug.getJSONObject(key).getString(
							"stre");
					medicine.unit = drug.getJSONObject(key).getString("ut");
					medicine.formulations = drug.getJSONObject(key).getString(
							"form");
					medicine.route = drug.getJSONObject(key).getString("rout");
					medicine.frequency = drug.getJSONObject(key).getString(
							"freq");
					medicine.times = drug.getJSONObject(key)
							.getString("ftimes");
					medicine.quantity = drug.getJSONObject(key).getString(
							"quan");
					medicine.refillsTime = drug.getJSONObject(key).getString(
							"rt");
					medicine.notes = drug.getJSONObject(key).getString("notes");
					medicine.addedon = drug.getJSONObject(key).optString("ao");
					medicine.updatedon = drug.getJSONObject(key)
							.optString("uo");
					if (drug.getJSONObject(key).has("refi")) {
						if (drug.getJSONObject(key).getString("refi")
								.equals("on"))
							medicine.refills = "Yes";
						else
							medicine.refills = "No";
					}
					if (drug.getJSONObject(key).has("rf"))
						medicine.rf = drug.getJSONObject(key).getString("rf");
					arrMedicine.add(medicine);

				}
			}
		} catch (final JSONException e1) {
			e1.printStackTrace();
		}
	}

	public void updateJson(final JSONObject plan) {
		try {
			final JSONObject pres = new JSONObject();
			final JSONObject drug = new JSONObject();
			for (int i = 0; i < arrMedicine.size(); i++) {
				final MedicineModel medicine = arrMedicine.get(i);
				final JSONObject med = new JSONObject();
				med.put("dn", medicine.name);
				med.put("stre", medicine.strength);
				med.put("ut", medicine.unit);
				med.put("rout", medicine.route);
				med.put("freq", medicine.frequency);
				med.put("ftimes", medicine.times);
				med.put("quan", medicine.quantity);
				med.put("rt", medicine.refillsTime);
				med.put("notes", medicine.notes);
				med.put("ao", formattedDate);
				med.put("uo", medicine.updatedon);
				if (medicine.refills.equals("Yes"))
					med.put("refi", "on");
				else
					med.put("refi", "off");
				med.put("rf", medicine.rf);
				med.put("form", medicine.formulations);
				final int j = i + 1;
				drug.put("" + j, med);
			}
			pres.put("drug", drug);

			final JSONObject jsSi = new JSONObject();
			jsSi.put("value", si.get("si"));
			pres.put("si", jsSi);
			plan.put("pres", pres);
		} catch (final JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void updateJson2(final JSONObject plan) {
		try {
			final JSONObject pres = new JSONObject();
			final JSONObject drug = new JSONObject();
			for (int i = 0; i < arrMedicine.size(); i++) {
				final MedicineModel medicine = arrMedicine.get(i);
				final JSONObject med = new JSONObject();
				med.put("dn", medicine.name);
				med.put("stre", medicine.strength);
				med.put("ut", medicine.unit);
				med.put("rout", medicine.route);
				med.put("freq", medicine.frequency);
				med.put("ftimes", medicine.times);
				med.put("quan", medicine.quantity);
				med.put("rt", medicine.refillsTime);
				med.put("refi", medicine.refills);
				med.put("rf", medicine.rf);
				med.put("notes", medicine.notes);
				med.put("ao", formattedDate);
				med.put("uo", medicine.updatedon);
				final int j = i + 1;
				drug.put("" + j, med);
			}
			pres.put("drug", drug);

			final JSONObject jsSi = new JSONObject();
			jsSi.put("value", si.get("si"));
			pres.put("si", jsSi);
			plan.put("Pres", pres);
		} catch (final JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
