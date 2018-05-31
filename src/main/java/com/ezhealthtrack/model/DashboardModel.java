package com.ezhealthtrack.model;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DashboardModel {

	@SerializedName("Appointments_Messages")
	@Expose
	private List<String> appointments_Messages = new ArrayList<String>();
	@SerializedName("Medicine_Research")
	@Expose
	private List<String> medicine_Research = new ArrayList<String>();
	@SerializedName("Prescriptions")
	@Expose
	private List<String> prescriptions = new ArrayList<String>();
	@Expose
	private List<String> ezHealthTrack_Information = new ArrayList<String>();

	public List<String> getAppointments_Messages() {
		return appointments_Messages;
	}

	public List<String> getEzHealthTrack_Information() {
		return ezHealthTrack_Information;
	}

	public List<String> getMedicine_Research() {
		return medicine_Research;
	}

	public List<String> getPrescriptions() {
		return prescriptions;
	}

	public void setAppointments_Messages(
			final List<String> appointments_Messages) {
		this.appointments_Messages = appointments_Messages;
	}

	public void setEzHealthTrack_Information(
			final List<String> ezHealthTrack_Information) {
		this.ezHealthTrack_Information = ezHealthTrack_Information;
	}

	public void setMedicine_Research(final List<String> medicine_Research) {
		this.medicine_Research = medicine_Research;
	}

	public void setPrescriptions(final List<String> prescriptions) {
		this.prescriptions = prescriptions;
	}

}