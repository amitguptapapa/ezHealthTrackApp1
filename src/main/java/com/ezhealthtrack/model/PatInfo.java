package com.ezhealthtrack.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PatInfo {

	@SerializedName("doc-id")
	@Expose
	private String docId;
	@SerializedName("pat-id")
	@Expose
	private String patId;
	@SerializedName("fam-id")
	@Expose
	private String famId;
	@SerializedName("followup-id")
	@Expose
	private String followupId;
	@SerializedName("apt-date")
	@Expose
	private String aptDate;
	@SerializedName("q_apt_date")
	@Expose
	private String qAptDate;
	@SerializedName("apt-reason")
	@Expose
	private String aptReason;
	@Expose
	private String gender;
	@Expose
	private String age;
	@SerializedName("total-visit")
	@Expose
	private Integer totalVisit;
	@SerializedName("p-name")
	@Expose
	private String pName;

	/**
	 * 
	 * @return The docId
	 */
	public String getDocId() {
		return docId;
	}

	/**
	 * 
	 * @param docId
	 *            The doc-id
	 */
	public void setDocId(String docId) {
		this.docId = docId;
	}

	/**
	 * 
	 * @return The patId
	 */
	public String getPatId() {
		return patId;
	}

	/**
	 * 
	 * @param patId
	 *            The pat-id
	 */
	public void setPatId(String patId) {
		this.patId = patId;
	}

	/**
	 * 
	 * @return The famId
	 */
	public String getFamId() {
		return famId;
	}

	/**
	 * 
	 * @param famId
	 *            The fam-id
	 */
	public void setFamId(String famId) {
		this.famId = famId;
	}

	/**
	 * 
	 * @return The followupId
	 */
	public String getFollowupId() {
		return followupId;
	}

	/**
	 * 
	 * @param followupId
	 *            The followup-id
	 */
	public void setFollowupId(String followupId) {
		this.followupId = followupId;
	}

	/**
	 * 
	 * @return The aptDate
	 */
	public String getAptDate() {
		return aptDate;
	}

	/**
	 * 
	 * @param aptDate
	 *            The apt-date
	 */
	public void setAptDate(String aptDate) {
		this.aptDate = aptDate;
	}

	/**
	 * 
	 * @return The qAptDate
	 */
	public String getQAptDate() {
		return qAptDate;
	}

	/**
	 * 
	 * @param qAptDate
	 *            The q_apt_date
	 */
	public void setQAptDate(String qAptDate) {
		this.qAptDate = qAptDate;
	}

	/**
	 * 
	 * @return The aptReason
	 */
	public String getAptReason() {
		return aptReason;
	}

	/**
	 * 
	 * @param aptReason
	 *            The apt-reason
	 */
	public void setAptReason(String aptReason) {
		this.aptReason = aptReason;
	}

	/**
	 * 
	 * @return The gender
	 */
	public String getGender() {
		return gender;
	}

	/**
	 * 
	 * @param gender
	 *            The gender
	 */
	public void setGender(String gender) {
		this.gender = gender;
	}

	/**
	 * 
	 * @return The age
	 */
	public String getAge() {
		return age;
	}

	/**
	 * 
	 * @param age
	 *            The age
	 */
	public void setAge(String age) {
		this.age = age;
	}

	/**
	 * 
	 * @return The totalVisit
	 */
	public Integer getTotalVisit() {
		return totalVisit;
	}

	/**
	 * 
	 * @param totalVisit
	 *            The total-visit
	 */
	public void setTotalVisit(Integer totalVisit) {
		this.totalVisit = totalVisit;
	}

	/**
	 * 
	 * @return The pName
	 */
	public String getPName() {
		return pName;
	}

	/**
	 * 
	 * @param pName
	 *            The p-name
	 */
	public void setPName(String pName) {
		this.pName = pName;
	}

}