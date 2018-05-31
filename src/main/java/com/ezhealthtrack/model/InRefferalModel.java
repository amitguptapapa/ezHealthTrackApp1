package com.ezhealthtrack.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InRefferalModel {

	@SerializedName("ref-id")
	@Expose
	private String refId = "0";
	@SerializedName("pat-id")
	@Expose
	private String patId;
	@SerializedName("fam-id")
	@Expose
	private String famId;
	@Expose
	private String pfn;
	@Expose
	private String pln;
	@SerializedName("refer-from")
	@Expose
	private String referFrom;
	@SerializedName("refer-from-type")
	@Expose
	private String referFromType;
	@SerializedName("refer-to")
	@Expose
	private String referTo;
	@SerializedName("refer-to-type")
	@Expose
	private String referToType;
	@SerializedName("refer-reason")
	@Expose
	private String referReason;
	@SerializedName("ep-id")
	@Expose
	private String epId = "0";
	@SerializedName("ref-flag")
	@Expose
	private String refFlag;
	@Expose
	private String photo;
	@Expose
	private String mobile;
	@Expose
	private String email;
	@SerializedName("from-name")
	@Expose
	private String fromName;
	@SerializedName("cdate")
	@Expose
	private String cdate;
	@SerializedName("gender")
	@Expose
	private String gender;
	@SerializedName("age")
	@Expose
	private String age;
	@SerializedName("pID")
	@Expose
	private String pID;

	public String getRefId() {
		return refId;
	}

	public void setRefId(String refId) {
		this.refId = refId;
	}

	public String getPatId() {
		return patId;
	}

	public void setPatId(String patId) {
		this.patId = patId;
	}

	public String getFamId() {
		return famId;
	}

	public void setFamId(String famId) {
		this.famId = famId;
	}

	public String getPfn() {
		return pfn;
	}

	public void setPfn(String pfn) {
		this.pfn = pfn;
	}

	public String getPln() {
		return pln;
	}

	public void setPln(String pln) {
		this.pln = pln;
	}

	public String getReferFrom() {
		return referFrom;
	}

	public void setReferFrom(String referFrom) {
		this.referFrom = referFrom;
	}

	public String getReferFromType() {
		return referFromType;
	}

	public void setReferFromType(String referFromType) {
		this.referFromType = referFromType;
	}

	public String getReferTo() {
		return referTo;
	}

	public void setReferTo(String referTo) {
		this.referTo = referTo;
	}

	public String getReferToType() {
		return referToType;
	}

	public void setReferToType(String referToType) {
		this.referToType = referToType;
	}

	public String getReferReason() {
		return referReason;
	}

	public void setReferReason(String referReason) {
		this.referReason = referReason;
	}

	public String getEpId() {
		return epId;
	}

	public void setEpId(String epId) {
		this.epId = epId;
	}

	public String getRefFlag() {
		return refFlag;
	}

	public void setRefFlag(String refFlag) {
		this.refFlag = refFlag;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFromName() {
		return fromName;
	}

	public void setFromName(String fromName) {
		this.fromName = fromName;
	}

	public String getcdate() {
		return cdate;
	}

	public void setcdate(String cdate) {
		this.cdate = cdate;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getpID() {
		return pID;
	}

	public void setpID(String pID) {
		this.pID = pID;
	}

}