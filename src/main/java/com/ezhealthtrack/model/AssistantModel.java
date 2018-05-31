package com.ezhealthtrack.model;

import com.google.gson.annotations.Expose;

public class AssistantModel {

	@Expose
	private String asst_id;
	@Expose
	private String assist_name;
	@Expose
	private String dob;
	@Expose
	private String email;
	@Expose
	private String mobile;
	@Expose
	private String photo;
	@Expose
	private Integer age;
	@Expose
	private String address;

	public String getAddress() {
		return address;
	}

	public Integer getAge() {
		return age;
	}

	public String getAssist_name() {
		return assist_name;
	}

	public String getAsst_id() {
		return asst_id;
	}

	public String getDob() {
		return dob;
	}

	public String getEmail() {
		return email;
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

	public void setAge(final Integer age) {
		this.age = age;
	}

	public void setAssist_name(final String assist_name) {
		this.assist_name = assist_name;
	}

	public void setAsst_id(final String asst_id) {
		this.asst_id = asst_id;
	}

	public void setDob(final String dob) {
		this.dob = dob;
	}

	public void setEmail(final String email) {
		this.email = email;
	}

	public void setMobile(final String mobile) {
		this.mobile = mobile;
	}

	public void setPhoto(final String photo) {
		this.photo = photo;
	}

}