package com.ezhealthtrack.physiciansoap.model;

import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import com.ezhealthtrack.model.DayPlanModel;

//import android.util.Log;

public class VisitNotesModel {
	private String patientName = "";
	private Date visitDate = new Date();
	private int noOfVisits = 1;
	private String visitReason = "";
	private String symptoms = "";

	private String diagnosis = "";
	private DayPlanModel planModel = new DayPlanModel();
	public SubjectiveModel subjectiveModel = new SubjectiveModel();
	public DiagnosisModel diagnosisModel = new DiagnosisModel();
	public ExaminationModel examinationModel = new ExaminationModel();
	public PlanModel physicianPlanModel = new PlanModel();
	public JSONObject jsonData = new JSONObject();
	public PrivateNotesModel privateNote = new PrivateNotesModel();

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

	public void updateJson() {
		try {
			subjectiveModel.updateJson(jsonData.getJSONObject("Soap"));
			diagnosisModel.updateJson(jsonData.getJSONObject("Soap"));
			examinationModel.updateJson(jsonData.getJSONObject("Soap"));
			physicianPlanModel.updateJson(jsonData.getJSONObject("Soap"));
			privateNote.jsonUpdate(jsonData.getJSONObject("Soap")
					.getJSONObject("private-note"));
			// Log.i("", jsonData.toString());
		} catch (final JSONException e) {
			e.printStackTrace();
		}
	}

}
