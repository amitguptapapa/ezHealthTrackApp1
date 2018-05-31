package com.ezhealthtrack.model;

public class DayPlanModel {
	private String id = "";
	private String day = "";
	private String startTime = "09:00 AM";
	private String endTime = "09:15 AM";
	private boolean isOff = false;
	private String lOut = "09:00 AM";
	private String lIn = "09:15 AM";

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public String getDay() {
		return day;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setOff(boolean isOff) {
		this.isOff = isOff;
	}

	public boolean isOff() {
		return isOff;
	}
	
	public void setLout(String lOut) {
		this.lOut = lOut;
	}

	public String getLout() {
		return lOut;
	}

	public void setLin(String lIn) {
		this.lIn = lIn;
	}

	public String getLin() {
		return lIn;
	}

}
