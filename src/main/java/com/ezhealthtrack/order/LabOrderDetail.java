package com.ezhealthtrack.order;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LabOrderDetail {

	@Expose
	private String id;
	@SerializedName("order_id")
	@Expose
	private String orderId;
	@SerializedName("technician_id")
	@Expose
	private String technicianId;
	@Expose
	private String doctor;
	@SerializedName("appointment_id")
	@Expose
	private String appointmentId;
	@SerializedName("episode_id")
	@Expose
	private String episodeId;
	@SerializedName("order_notes")
	@Expose
	private String orderNotes;
	@SerializedName("sampling_notes")
	@Expose
	private String samplingNotes;
	@SerializedName("sampling_on")
	@Expose
	private String samplingOn;
	@SerializedName("sampling_done_notes")
	@Expose
	private String samplingDoneNotes;
	@SerializedName("sampling_done_on")
	@Expose
	private String samplingDoneOn;
	@SerializedName("refund_status")
	@Expose
	private String refundStatus;
	@SerializedName("publish_report")
	@Expose
	private String publishReport;
	@SerializedName("cancel_action")
	@Expose
	private Object cancelAction;

	/**
	 * 
	 * @return The id
	 */
	public String getId() {
		return id;
	}

	/**
	 * 
	 * @param id
	 *            The id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 
	 * @return The orderId
	 */
	public String getOrderId() {
		return orderId;
	}

	/**
	 * 
	 * @param orderId
	 *            The order_id
	 */
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	/**
	 * 
	 * @return The technicianId
	 */
	public String getTechnicianId() {
		return technicianId;
	}

	/**
	 * 
	 * @param technicianId
	 *            The technician_id
	 */
	public void setTechnicianId(String technicianId) {
		this.technicianId = technicianId;
	}

	/**
	 * 
	 * @return The doctor
	 */
	public String getDoctor() {
		return doctor;
	}

	/**
	 * 
	 * @param doctor
	 *            The doctor
	 */
	public void setDoctor(String doctor) {
		this.doctor = doctor;
	}

	/**
	 * 
	 * @return The appointmentId
	 */
	public String getAppointmentId() {
		return appointmentId;
	}

	/**
	 * 
	 * @param appointmentId
	 *            The appointment_id
	 */
	public void setAppointmentId(String appointmentId) {
		this.appointmentId = appointmentId;
	}

	/**
	 * 
	 * @return The episodeId
	 */
	public String getEpisodeId() {
		return episodeId;
	}

	/**
	 * 
	 * @param episodeId
	 *            The episode_id
	 */
	public void setEpisodeId(String episodeId) {
		this.episodeId = episodeId;
	}

	/**
	 * 
	 * @return The orderNotes
	 */
	public String getOrderNotes() {
		return orderNotes;
	}

	/**
	 * 
	 * @param orderNotes
	 *            The order_notes
	 */
	public void setOrderNotes(String orderNotes) {
		this.orderNotes = orderNotes;
	}

	/**
	 * 
	 * @return The samplingNotes
	 */
	public String getSamplingNotes() {
		return samplingNotes;
	}

	/**
	 * 
	 * @param samplingNotes
	 *            The sampling_notes
	 */
	public void setSamplingNotes(String samplingNotes) {
		this.samplingNotes = samplingNotes;
	}

	/**
	 * 
	 * @return The samplingOn
	 */
	public String getSamplingOn() {
		return samplingOn;
	}

	/**
	 * 
	 * @param samplingOn
	 *            The sampling_on
	 */
	public void setSamplingOn(String samplingOn) {
		this.samplingOn = samplingOn;
	}

	/**
	 * 
	 * @return The samplingDoneNotes
	 */
	public String getSamplingDoneNotes() {
		return samplingDoneNotes;
	}

	/**
	 * 
	 * @param samplingDoneNotes
	 *            The sampling_done_notes
	 */
	public void setSamplingDoneNotes(String samplingDoneNotes) {
		this.samplingDoneNotes = samplingDoneNotes;
	}

	/**
	 * 
	 * @return The samplingDoneOn
	 */
	public String getSamplingDoneOn() {
		return samplingDoneOn;
	}

	/**
	 * 
	 * @param samplingDoneOn
	 *            The sampling_done_on
	 */
	public void setSamplingDoneOn(String samplingDoneOn) {
		this.samplingDoneOn = samplingDoneOn;
	}

	/**
	 * 
	 * @return The refundStatus
	 */
	public String getRefundStatus() {
		return refundStatus;
	}

	/**
	 * 
	 * @param refundStatus
	 *            The refund_status
	 */
	public void setRefundStatus(String refundStatus) {
		this.refundStatus = refundStatus;
	}

	/**
	 * 
	 * @return The publishReport
	 */
	public String getPublishReport() {
		return publishReport;
	}

	/**
	 * 
	 * @param publishReport
	 *            The publish_report
	 */
	public void setPublishReport(String publishReport) {
		this.publishReport = publishReport;
	}

	/**
	 * 
	 * @return The cancelAction
	 */
	public Object getCancelAction() {
		return cancelAction;
	}

	/**
	 * 
	 * @param cancelAction
	 *            The cancel_action
	 */
	public void setCancelAction(Object cancelAction) {
		this.cancelAction = cancelAction;
	}

}