package com.ezhealthtrack.physiciansoap.model;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.ezhealthtrack.model.PrescriptionModel;

public class PlanModel {
	public PrescriptionModel prescription = new PrescriptionModel();
	public TreatmentDoneModel treatmentDone = new TreatmentDoneModel();
	public RadiologyModel radiology = new RadiologyModel();
	public LabModel lab = new LabModel();
	public Boolean ecg = false;
	public HashMap<String, String> hashTp = new HashMap<String, String>();

	public void JsonParse(final JSONObject plan) {
		try {
			if (plan.get("td") instanceof JSONObject)
				treatmentDone.JsonParse(plan.getJSONObject("td"));
			if (plan.get("pres") instanceof JSONObject)
				prescription.JsonParse(plan.getJSONObject("pres"));
			if (plan.get("radi") instanceof JSONObject)
				radiology.JsonParse(plan.getJSONObject("radi"));
			if (plan.get("lab") instanceof JSONObject)
				lab.JsonParse(plan.getJSONObject("lab"));
			if (plan.has("ecg") && plan.getString("ecg").equals("on")) {
				ecg = true;
			} else {
				ecg = false;
			}
			hashTp.put("tp", plan.getJSONObject("tp").getString("value"));
		} catch (final JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	public void updateJson(final JSONObject soap) {
		try {
			treatmentDone.updateJson(soap.getJSONObject("plan"));
			prescription.updateJson(soap.getJSONObject("plan"));
			lab.updateJson(soap.getJSONObject("plan"));
			radiology.updateJson(soap.getJSONObject("plan"));
			if (ecg) {
				soap.getJSONObject("plan").put("ecg", "on");
			} else {
				soap.getJSONObject("plan").put("ecg", "off");
			}
			soap.getJSONObject("plan").getJSONObject("tp")
					.put("value", hashTp.get("tp"));

		} catch (final JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
