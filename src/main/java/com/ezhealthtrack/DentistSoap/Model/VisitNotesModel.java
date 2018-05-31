package com.ezhealthtrack.DentistSoap.Model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.ezhealthtrack.model.DayPlanModel;
import com.ezhealthtrack.model.VitalsModel;
import com.ezhealthtrack.util.Log;

//import android.util.Log;

public class VisitNotesModel {
	private String patientName = "";
	private Date visitDate = new Date();
	private int noOfVisits = 1;
	private String visitReason = "";
	private String symptoms = "";
	private VitalsModel vitalsModel = new VitalsModel();
	private String diagnosis = "";
	private DayPlanModel planModel = new DayPlanModel();
	public SubjectiveModel dentistSubjectiveModel = new SubjectiveModel();
	public ExaminationModel dentistExaminationModel = new ExaminationModel();
	public DiagnosisModel dentistDiagnosisModel = new DiagnosisModel();
	public PlanModel dentistPlanModel = new PlanModel();
	public PrivateNotesModel privateNote = new PrivateNotesModel();
	public JSONObject jsonData = new JSONObject();

	public String getDiagnosis() {
		return diagnosis;
	}

	public int getNoOfVisits() {
		return noOfVisits;
	}

	public String getPatientName() {
		return patientName;
	}

	public DayPlanModel getPlanModel() {
		return planModel;
	}

	public String getSymptoms() {
		return symptoms;
	}

	public Date getVisitDate() {
		return visitDate;
	}

	public String getVisitReason() {
		return visitReason;
	}

	public VitalsModel getVitalsModel() {
		return vitalsModel;
	}

	public void setDiagnosis(final String diagnosis) {
		this.diagnosis = diagnosis;
	}

	public void setNoOfVisits(final int noOfVisits) {
		this.noOfVisits = noOfVisits;
	}

	public void setPatientName(final String patientName) {
		this.patientName = patientName;
	}

	public void setplanModel(final DayPlanModel planModel) {
		this.planModel = planModel;
	}

	public void setSymptoms(final String symptoms) {
		this.symptoms = symptoms;
	}

	public void setVisitDate(final Date visitDate) {
		this.visitDate = visitDate;
	}

	public void setVisitReason(final String visitReason) {
		this.visitReason = visitReason;
	}

	public void setVitalsModel(final VitalsModel vitalsModel) {
		this.vitalsModel = vitalsModel;
	}

	public void updateJson(final ArrayList<TeethModel> arrTeeth,
			final Context context) {
		try {
			if (jsonData.has("Soap")
					&& jsonData.get("Soap") instanceof JSONObject) {
				dentistSubjectiveModel.updateJson(jsonData
						.getJSONObject("Soap"));
				dentistExaminationModel.updateJson(jsonData
						.getJSONObject("Soap"));
				dentistDiagnosisModel
						.updateJson(jsonData.getJSONObject("Soap"));
				dentistPlanModel.updateJson(jsonData.getJSONObject("Soap"),
						arrTeeth, context);
				privateNote.jsonUpdate(jsonData.getJSONObject("Soap"));
			}
			// Log.i("", jsonData.toString());
		} catch (final JSONException e) {
			e.printStackTrace();
		}
	}

}
