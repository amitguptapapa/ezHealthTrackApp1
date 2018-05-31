package com.ezhealthtrack.order;

import java.util.ArrayList;
import java.util.List;

import com.ezhealthtrack.util.Util;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LabOrderReport {

	@Expose
	private String id;
	@SerializedName("order_id")
	@Expose
	private String orderId;
	@SerializedName("order_product_id")
	@Expose
	private String orderProductId;
	@SerializedName("lab_test_id")
	@Expose
	private String labTestId;
	@SerializedName("lab_panel_id")
	@Expose
	private Object labPanelId;
	@SerializedName("lab_test_name")
	@Expose
	private String labTestName;
	@SerializedName("sample_meta")
	@Expose
	private List<SampleMetum> sampleMeta = new ArrayList<SampleMetum>();
	@SerializedName("report_prepared_by")
	@Expose
	private String reportPreparedBy;
	@SerializedName("report_prepared_on")
	@Expose
	private String reportPreparedOn;
	@SerializedName("report_available_on")
	@Expose
	private String reportAvailableOn;
	@Expose
	private String status;
	@Expose
	private Object notes;
	@SerializedName("refund_status")
	@Expose
	private String refundStatus;
	@SerializedName("lab_id")
	@Expose
	private String labId;
	@SerializedName("error_action")
	@Expose
	private Object errorAction;
	@Expose
	private Boolean isEditable;
	@Expose
	private Boolean transitionPermission;
	@SerializedName("workflow_label")
	@Expose
	private String workflowLabel;
	@SerializedName("report_workflow_id")
	@Expose
	private String reportWorkflowId;
	@SerializedName("report_template_view")
	@Expose
	private String reportTemplateView;
	@SerializedName("display_sample")
	@Expose
	private String displaySample;
	@SerializedName("display_method")
	@Expose
	private String displayMethod;

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
	 * @return The orderProductId
	 */
	public String getOrderProductId() {
		return orderProductId;
	}

	/**
	 * 
	 * @param orderProductId
	 *            The order_product_id
	 */
	public void setOrderProductId(String orderProductId) {
		this.orderProductId = orderProductId;
	}

	/**
	 * 
	 * @return The labTestId
	 */
	public String getLabTestId() {
		return labTestId;
	}

	/**
	 * 
	 * @param labTestId
	 *            The lab_test_id
	 */
	public void setLabTestId(String labTestId) {
		this.labTestId = labTestId;
	}

	/**
	 * 
	 * @return The labPanelId
	 */
	public Object getLabPanelId() {
		return labPanelId;
	}

	/**
	 * 
	 * @param labPanelId
	 *            The lab_panel_id
	 */
	public void setLabPanelId(Object labPanelId) {
		this.labPanelId = labPanelId;
	}

	/**
	 * 
	 * @return The labTestName
	 */
	public String getLabTestName() {
		return labTestName;
	}

	/**
	 * 
	 * @param labTestName
	 *            The lab_test_name
	 */
	public void setLabTestName(String labTestName) {
		this.labTestName = labTestName;
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
	 * @return The reportPreparedBy
	 */
	public String getReportPreparedBy() {
		return reportPreparedBy;
	}

	/**
	 * 
	 * @param reportPreparedBy
	 *            The report_prepared_by
	 */
	public void setReportPreparedBy(String reportPreparedBy) {
		this.reportPreparedBy = reportPreparedBy;
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
	 * @return The labId
	 */
	public String getLabId() {
		return labId;
	}

	/**
	 * 
	 * @param labId
	 *            The lab_id
	 */
	public void setLabId(String labId) {
		this.labId = labId;
	}

	/**
	 * 
	 * @return The errorAction
	 */
	public Object getErrorAction() {
		return errorAction;
	}

	/**
	 * 
	 * @param errorAction
	 *            The error_action
	 */
	public void setErrorAction(Object errorAction) {
		this.errorAction = errorAction;
	}

	/**
	 * 
	 * @return The isEditable
	 */
	public Boolean getIsEditable() {
		return isEditable;
	}

	/**
	 * 
	 * @param isEditable
	 *            The isEditable
	 */
	public void setIsEditable(Boolean isEditable) {
		this.isEditable = isEditable;
	}

	/**
	 * 
	 * @return The transitionPermission
	 */
	public Boolean getTransitionPermission() {
		return transitionPermission;
	}

	/**
	 * 
	 * @param transitionPermission
	 *            The transitionPermission
	 */
	public void setTransitionPermission(Boolean transitionPermission) {
		this.transitionPermission = transitionPermission;
	}

	/**
	 * 
	 * @return The workflowLabel
	 */
	public String getWorkflowLabel() {
		return workflowLabel;
	}

	/**
	 * 
	 * @param workflowLabel
	 *            The workflow_label
	 */
	public void setWorkflowLabel(String workflowLabel) {
		this.workflowLabel = workflowLabel;
	}

	/**
	 * 
	 * @return The reportWorkflowId
	 */
	public String getReportWorkflowId() {
		return reportWorkflowId;
	}

	/**
	 * 
	 * @param reportWorkflowId
	 *            The report_workflow_id
	 */
	public void setReportWorkflowId(String reportWorkflowId) {
		this.reportWorkflowId = reportWorkflowId;
	}

	/**
	 * 
	 * @return The reportTemplateView
	 */
	public String getReportTemplateView() {
		return reportTemplateView;
	}

	/**
	 * 
	 * @param reportTemplateView
	 *            The report_template_view
	 */
	public void setReportTemplateView(String reportTemplateView) {
		this.reportTemplateView = reportTemplateView;
	}

	/**
	 * 
	 * @return The displaySample
	 */
	public String getDisplaySample() {
		return displaySample;
	}

	/**
	 * 
	 * @param displaySample
	 *            The display_sample
	 */
	public void setDisplaySample(String displaySample) {
		this.displaySample = displaySample;
	}

	/**
	 * 
	 * @return The displayMethod
	 */
	public String getDisplayMethod() {
		if (Util.isEmptyString(displayMethod))
			displayMethod = "";
		return displayMethod;
	}

	/**
	 * 
	 * @param displayMethod
	 *            The display_method
	 */
	public void setDisplayMethod(String displayMethod) {
		this.displayMethod = displayMethod;
	}

}