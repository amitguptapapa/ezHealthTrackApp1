package com.ezhealthtrack.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OutReferralModel {

	@SerializedName("ref-id")
	@Expose
	private String ref_id;
	@SerializedName("pat-id")
	@Expose
	private String pat_id;
	@SerializedName("fam-id")
	@Expose
	private String fam_id;
	@Expose
	private String pfn;
	@Expose
	private String pln;
	@SerializedName("refer-from")
	@Expose
	private String refer_from;
	@SerializedName("refer-from-type")
	@Expose
	private String refer_from_type;
	@SerializedName("refer-to")
	@Expose
	private String refer_to;
	@SerializedName("refer-to-type")
	@Expose
	private String refer_to_type;
	@SerializedName("refer-reason")
	@Expose
	private String refer_reason;
	@SerializedName("ep-id")
	@Expose
	private String ep_id;
	@Expose
	private String photo;
	@Expose
	private String mobile;
	@Expose
	private String email;
	@SerializedName("to-name")
	@Expose
	private String to_name;
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

	public String getEmail() {
		return email;
	}

	public String getEp_id() {
		return ep_id;
	}

	public String getFam_id() {
		return fam_id;
	}

	public String getMobile() {
		return mobile;
	}

	public String getPat_id() {
		return pat_id;
	}

	public String getPfn() {
		return pfn;
	}

	public String getPhoto() {
		return photo;
	}

	public String getPln() {
		return pln;
	}

	public String getRef_id() {
		return ref_id;
	}

	public String getRefer_from() {
		return refer_from;
	}

	public String getRefer_from_type() {
		return refer_from_type;
	}

	public String getRefer_reason() {
		return refer_reason;
	}

	public String getRefer_to() {
		return refer_to;
	}

	public String getRefer_to_type() {
		return refer_to_type;
	}

	public String getTo_name() {
		return to_name;
	}

	public void setEmail(final String email) {
		this.email = email;
	}

	public void setEp_id(final String ep_id) {
		this.ep_id = ep_id;
	}

	public void setFam_id(final String fam_id) {
		this.fam_id = fam_id;
	}

	public void setMobile(final String mobile) {
		this.mobile = mobile;
	}

	public void setPat_id(final String pat_id) {
		this.pat_id = pat_id;
	}

	public void setPfn(final String pfn) {
		this.pfn = pfn;
	}

	public void setPhoto(final String photo) {
		this.photo = photo;
	}

	public void setPln(final String pln) {
		this.pln = pln;
	}

	public void setRef_id(final String ref_id) {
		this.ref_id = ref_id;
	}

	public void setRefer_from(final String refer_from) {
		this.refer_from = refer_from;
	}

	public void setRefer_from_type(final String refer_from_type) {
		this.refer_from_type = refer_from_type;
	}

	public void setRefer_reason(final String refer_reason) {
		this.refer_reason = refer_reason;
	}

	public void setRefer_to(final String refer_to) {
		this.refer_to = refer_to;
	}

	public void setRefer_to_type(final String refer_to_type) {
		this.refer_to_type = refer_to_type;
	}

	public void setTo_name(final String to_name) {
		this.to_name = to_name;
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