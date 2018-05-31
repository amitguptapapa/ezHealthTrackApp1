package com.ezhealthtrack.model;

import com.google.gson.annotations.Expose;

public class HospitalModel {

	@Expose
	private String photo;
	@Expose
	private String location_title;
	@Expose
	private String email;
	@Expose
	private String mobile;
	@Expose
	private String address;

	public String getAddress() {
		return address;
	}

	public String getEmail() {
		return email;
	}

	public String getLocation_title() {
		return location_title;
	}

	public String getMobile() {
		return mobile;
	}

	public String getPhoto() {
		return photo;
	}

	public void setAddress(final String address) {
		this.address = address;
	}

	public void setEmail(final String email) {
		this.email = email;
	}

	public void setLocation_title(final String location_title) {
		this.location_title = location_title;
	}

	public void setMobile(final String mobile) {
		this.mobile = mobile;
	}

	public void setPhoto(final String photo) {
		this.photo = photo;
	}

}