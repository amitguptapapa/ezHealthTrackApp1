package com.ezhealthtrack.model.laborder;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TestReport {

	@SerializedName("report_id")
	@Expose
	private String reportId;
	@SerializedName("report_name")
	@Expose
	private String reportName;
	@Expose
	private String status;
	@Expose
	private String date;
	@SerializedName("sample_meta")
	@Expose
	// private String sampleMeta;
	private List<SampleMetum> sampleMeta = new ArrayList<SampleMetum>();
	@SerializedName("report_prepared_on")
	@Expose
	private String reportPreparedOn;
	@SerializedName("report_available_on")
	@Expose
	private String reportAvailableOn;
	@Expose
	private Object notes;

	/**
	 * 
	 * @return The reportId
	 */
	public String getReportId() {
		return reportId;
	}

	/**
	 * 
	 * @param reportId
	 *            The report_id
	 */
	public void setReportId(String reportId) {
		this.reportId = reportId;
	}

	/**
	 * 
	 * @return The reportName
	 */
	public String getReportName() {
		return reportName;
	}

	/**
	 * 
	 * @param reportName
	 *            The report_name
	 */
	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

	/**
	 * 
	 * @return The status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * 
	 * @param status
	 *            The status
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * 
	 * @return The date
	 */
	public String getDate() {
		return date;
	}

	/**
	 * 
	 * @param date
	 *            The date
	 */
	public void setDate(String date) {
		this.date = date;
	}

	/**
	 * 
	 * @return The sampleMeta
	 */
	public List<SampleMetum> getSampleMeta() {
		return sampleMeta;
	}

	/**
	 * 
	 * @param sampleMeta
	 *            The sample_meta
	 */
	public void setSampleMeta(List<SampleMetum> sampleMeta) {
		this.sampleMeta = sampleMeta;
	}

	/**
	 * 
	 * @return The reportPreparedOn
	 */
	public String getReportPreparedOn() {
		return reportPreparedOn;
	}

	/**
	 * 
	 * @param reportPreparedOn
	 *            The report_prepared_on
	 */
	public void setReportPreparedOn(String reportPreparedOn) {
		this.reportPreparedOn = reportPreparedOn;
	}

	/**
	 * 
	 * @return The reportAvailableOn
	 */
	public String getReportAvailableOn() {
		return reportAvailableOn;
	}

	/**
	 * 
	 * @param reportAvailableOn
	 *            The report_available_on
	 */
	public void setReportAvailableOn(String reportAvailableOn) {
		this.reportAvailableOn = reportAvailableOn;
	}

	/**
	 * 
	 * @return The notes
	 */
	public Object getNotes() {
		return notes;
	}

	/**
	 * 
	 * @param notes
	 *            The notes
	 */
	public void setNotes(Object notes) {
		this.notes = notes;
	}

}