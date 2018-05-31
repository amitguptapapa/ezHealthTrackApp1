package com.ezhealthtrack.DentistSoap.Model;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.ezhealthtrack.model.PrescriptionModel;
import com.ezhealthtrack.util.Log;

public class PlanModel {
	public PrescriptionModel prescription = new PrescriptionModel();
	public TreatmentDoneModel treatmentDone = new TreatmentDoneModel();
	public LabModel lab = new LabModel();
	public RadiologyModel radiology = new RadiologyModel();
	public TreatmentPlanModel treatmentPlan = new TreatmentPlanModel();

	public void JsonParse(final JSONObject plan,
			final ArrayList<TeethModel> arrTeeth) {
		try {
			Log.i("", plan.getString("td"));
			if (plan.has("td") && plan.get("td") instanceof JSONObject)
				treatmentDone.JsonParse(plan.getJSONObject("td"));
			if (plan.has("lab") && plan.get("lab") instanceof JSONObject)
				lab.JsonParse(plan.getJSONObject("lab"));
			if (plan.has("radi") && plan.get("radi") instanceof JSONObject)
				radiology.JsonParse(plan.getJSONObject("radi"));
			if (plan.has("pres") && plan.get("pres") instanceof JSONObject)
				prescription.JsonParse(plan.getJSONObject("pres"));
			if (plan.has("tp") && plan.get("tp") instanceof JSONObject)
				treatmentPlan.JsonParse(plan.getJSONObject("tp"), arrTeeth);
		} catch (final JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	public void updateJson(final JSONObject soap,
			final ArrayList<TeethModel> arrTeeth, final Context context) {
		try {
			treatmentDone.updateJson(soap.getJSONObject("plan"));
			prescription.updateJson(soap.getJSONObject("plan"));
			lab.updateJson(soap.getJSONObject("plan"));
			radiology.updateJson(soap.getJSONObject("plan"));
			treatmentPlan.updateJson(soap.getJSONObject("plan"), arrTeeth,
					context);
		} catch (final JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
