package com.ezhealthtrack.DentistSoap.Model;

import com.google.gson.annotations.Expose;

public class Task {

	@Expose
	private String date;
	@Expose
	private String status;
	
	private String name;

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}