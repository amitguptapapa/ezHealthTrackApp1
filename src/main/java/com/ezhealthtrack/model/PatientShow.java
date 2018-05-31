package com.ezhealthtrack.model;

import com.ezhealthtrack.util.Util;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PatientShow {

	@SerializedName("p_id")
	@Expose
	private String pId;
	@SerializedName("pf_id")
	@Expose
	private String pfId;
	@SerializedName("p_type")
	@Expose
	private String pType;
	@Expose
	private String pfn;
	@Expose
	private String pln;
	@Expose
	private String gender;
	@Expose
	private String age;

	public String getPId() {
		return pId;
	}

	public void setPId(String pId) {
		this.pId = pId;
	}

	public String getPfId() {
		if(Util.isEmptyString(pfId))
    		pfId = "0";
		return pfId;
	}

	public void setPfId(String pfId) {
		this.pfId = pfId;
	}

	public String getPType() {
		return pType;
	}

	public void setPType(String pType) {
		this.pType = pType;
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

}