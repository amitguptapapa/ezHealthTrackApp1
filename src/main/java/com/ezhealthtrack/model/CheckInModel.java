package com.ezhealthtrack.model;

public class CheckInModel {
	private boolean isCheckIn = false;
	private String note = "";
	private String nurseAssigned;

	public void isCheckIn(boolean isCheckIn) {
		this.isCheckIn = isCheckIn;
	}

	public boolean isCheckIn() {
		return isCheckIn;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getNote() {
		return note;
	}

	public void setNurseAssigned(String nurseAssigned) {
		this.nurseAssigned = nurseAssigned;
	}

	public String getNurseAssigned() {
		return nurseAssigned;
	}

}
