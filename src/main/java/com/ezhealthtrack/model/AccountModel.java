package com.ezhealthtrack.model;

import com.google.gson.annotations.Expose;

public class AccountModel {

	@Expose
	private String photo = "";
	@Expose
	private String fname = "";
	@Expose
	private String mname = "";
	@Expose
	private String lname = "";
	@Expose
	private String mobile = "";
	@Expose
	private String email = "";
	@Expose
	private String username = "";
	@Expose
	private String pwd = "";
	@Expose
	private String dob = "";
	@Expose
	private String gender = "";
	@Expose
	private String blood = "";
	@Expose
	private String address = "";
	@Expose
	private String address2 = "";
	@Expose
	public String cmbArea = "";
	@Expose
	public String cmbCity = "";
	@Expose
	public String cmbState = "";
	@Expose
	public String country = "";
	@Expose
	public String cmbArea_id = "";
	@Expose
	public String cmbCity_id = "";
	@Expose
	public String cmbState_id = "";
	@Expose
	public String country_id = "";
	@Expose
	private String zip = "";
	@Expose
	private String eyecolor = "";
	@Expose
	private String haircolor = "";
	@Expose
	private String visiblemark = "";
	@Expose
	private String height = "";
	@Expose
	private String vaccinations = "";
	@Expose
	private String asst_id = "";

	public String getAddress() {
		return address;
	}

	public String getAddress2() {
		return address2;
	}

	public String getAsst_id() {
		return asst_id;
	}

	public String getBlood() {
		return blood;
	}

	public String getCmbArea() {
		return cmbArea;
	}

	public String getCmbCity() {
		return cmbCity;
	}

	public String getCmbState() {
		return cmbState;
	}

	public String getCountry() {
		return country;
	}

	public String getDob() {
		return dob;
	}

	public String getEmail() {
		return email;
	}

	public String getEyecolor() {
		return eyecolor;
	}

	public String getFname() {
		return fname;
	}

	public String getGender() {
		return gender;
	}

	public String getHaircolor() {
		return haircolor;
	}

	public String getHeight() {
		return height;
	}

	public String getLname() {
		return lname;
	}

	public String getMname() {
		return mname;
	}

	public String getMobile() {
		return mobile;
	}

	public String getPhoto() {
		return photo;
	}

	public String getPwd() {
		return pwd;
	}

	public String getUsername() {
		return username;
	}

	public String getVaccinations() {
		return vaccinations;
	}

	public String getVisiblemark() {
		return visiblemark;
	}

	public String getZip() {
		return zip;
	}

	public void setAddress(final String address) {
		this.address = address;
	}

	public void setAddress2(final String address2) {
		this.address2 = address2;
	}

	public void setAsst_id(final String asst_id) {
		this.asst_id = asst_id;
	}

	public void setBlood(final String blood) {
		this.blood = blood;
	}

	public void setCmbArea(final String cmbArea) {
		this.cmbArea = cmbArea;
	}

	public void setCmbCity(final String cmbCity) {
		this.cmbCity = cmbCity;
	}

	public void setCmbState(final String cmbState) {
		this.cmbState = cmbState;
	}

	public void setCountry(final String country) {
		this.country = country;
	}

	public void setDob(final String dob) {
		this.dob = dob;
	}

	public void setEmail(final String email) {
		this.email = email;
	}

	public void setEyecolor(final String eyecolor) {
		this.eyecolor = eyecolor;
	}

	public void setFname(final String fname) {
		this.fname = fname;
	}

	public void setGender(final String gender) {
		this.gender = gender;
	}

	public void setHaircolor(final String haircolor) {
		this.haircolor = haircolor;
	}

	public void setHeight(final String height) {
		this.height = height;
	}

	public void setLname(final String lname) {
		this.lname = lname;
	}

	public void setMname(final String mname) {
		this.mname = mname;
	}

	public void setMobile(final String mobile) {
		this.mobile = mobile;
	}

	public void setPhoto(final String photo) {
		this.photo = photo;
	}

	public void setPwd(final String pwd) {
		this.pwd = pwd;
	}

	public void setUsername(final String username) {
		this.username = username;
	}

	public void setVaccinations(final String vaccinations) {
		this.vaccinations = vaccinations;
	}

	public void setVisiblemark(final String visiblemark) {
		this.visiblemark = visiblemark;
	}

	public void setZip(final String zip) {
		this.zip = zip;
	}

}