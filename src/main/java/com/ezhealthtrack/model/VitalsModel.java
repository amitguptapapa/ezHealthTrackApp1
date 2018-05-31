package com.ezhealthtrack.model;

public class VitalsModel {
	private String bpLow = "";
	private String bpHigh = "";
	private String heartRate = "";
	private String temp = "";
	private String respiratory = "";
	private String pain = "";
	private String fstSugar = "";
	private String postPrandial = "";
	private String random = "";
	private String tolerance = "";
	private String weight = "";
	private String info = "";

	public void setBpLow(String bpLow) {
		this.bpLow = bpLow;
	}

	public String getBpLow() {
		return bpLow;
	}

	public void setBpHigh(String bpHigh) {
		this.bpHigh = bpHigh;
	}

	public String getBpHigh() {
		return bpHigh;
	}

	public void setHeartRate(String heartRate) {
		this.heartRate = heartRate;
	}

	public String getHeartRate() {
		return heartRate;
	}

	public void setTemp(String temp) {
		this.temp = temp;
	}

	public String getTemp() {
		return temp;
	}

	public void setRespiratory(String respiratory) {
		this.respiratory = respiratory;
	}

	public String getRespiratory() {
		return respiratory;
	}

	public void setPain(String pain) {
		this.pain = pain;
	}

	public String getPain() {
		return pain;
	}

	public void setFstSugar(String fstSugar) {
		this.fstSugar = fstSugar;
	}

	public String getFstSugar() {
		return fstSugar;
	}

	public void setPostPrandial(String postPrandial) {
		this.postPrandial = postPrandial;
	}

	public String getPostPrandial() {
		return postPrandial;
	}

	public void setRandom(String random) {
		this.random = random;
	}

	public String getRandom() {
		return random;
	}

	public void setTolerance(String tolerance) {
		this.tolerance = tolerance;
	}

	public String getTolerance() {
		return tolerance;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public String getWeight() {
		return weight;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getInfo() {
		return info;
	}

	public String getVitalsString() {
		return "Blood Pressure = " + bpLow + "--" + bpHigh + ", Body Temp = "
				+ temp + ",Weight= " + weight + ", Heart Rate= " + heartRate
				+ ", Respiratory Rate= " + respiratory
				+ ", Pain Scale [1-10] = " + pain;

	}
}
