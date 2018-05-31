package com.ezhealthtrack.model.laborder;

import java.util.ArrayList;
import java.util.List;

import com.ezhealthtrack.model.LabOrder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

	@SerializedName("lab_name")
	@Expose
	private String labName;
	@SerializedName("lab_moto")
	@Expose
	private String labMoto;
	@SerializedName("lab_address")
	@Expose
	private String labAddress;
	@SerializedName("lab_contact_number")
	@Expose
	private List<LabContactNumber> labContactNumber = new ArrayList<LabContactNumber>();
	@SerializedName("patient_detail")
	@Expose
	private String patientDetail;
	@SerializedName("patient_address")
	@Expose
	private String patientAddress;
	@SerializedName("patient_contact_number")
	@Expose
	private String patientContactNumber;
	@SerializedName("patient_email")
	@Expose
	private String patientEmail;
	@SerializedName("order_date")
	@Expose
	private String orderDate;
	@SerializedName("display_order_id")
	@Expose
	private String displayOrderId;
	@SerializedName("technician_name")
	@Expose
	private String technicianName;
	@SerializedName("order_status")
	@Expose
	private String orderStatus;
	@SerializedName("doctor_detail")
	@Expose
	private String doctorDetail;
	@SerializedName("approval_status")
	@Expose
	private String approvalStatus;
	@SerializedName("patient_location_appointment")
	@Expose
	private String patientLocationAppointment;
	@SerializedName("lab_image")
	@Expose
	private String labImage;
	@SerializedName("test_reports")
	@Expose
	private List<TestReport> testReports = new ArrayList<TestReport>();

	public String toStringSummary() {
		// Order Id 1507-0910004422-00035 on Wed, Jul 22, 2015 with Lab:
		// anuj_lab2, order status: Pending and Approval status :
		// Pending

		String details = "Order Id " + displayOrderId;
		details += " created on " + orderDate;
		details += " with Lab : " + labName;
		details += ", Order Status : " + orderStatus;
		details += ", Approval Status : " + approvalStatus;
		return details;
	}

	public LabOrder getLabOrder() {
		LabOrder order = new LabOrder();

		order.setDisplayId(displayOrderId);
		order.setPatientDetail(patientDetail);
		order.setAssignTo(technicianName);
		order.setOrderDate(orderDate);
		order.setOrderStatus(orderStatus);
		order.setApprovalStatus(approvalStatus);
		order.setLabName(labName);
		order.setTestReports(testReports);
		return order;
	}

	/**
	 * 
	 * @return The labName
	 */
	public String getLabName() {
		return labName;
	}

	/**
	 * 
	 * @param labName
	 *            The lab_name
	 */
	public void setLabName(String labName) {
		this.labName = labName;
	}

	/**
	 * 
	 * @return The labMoto
	 */
	public String getLabMoto() {
		return labMoto;
	}

	/**
	 * 
	 * @param labMoto
	 *            The lab_moto
	 */
	public void setLabMoto(String labMoto) {
		this.labMoto = labMoto;
	}

	/**
	 * 
	 * @return The labAddress
	 */
	public String getLabAddress() {
		return labAddress;
	}

	/**
	 * 
	 * @param labAddress
	 *            The lab_address
	 */
	public void setLabAddress(String labAddress) {
		this.labAddress = labAddress;
	}

	/**
	 * 
	 * @return The labContactNumber
	 */
	public List<LabContactNumber> getLabContactNumber() {
		return labContactNumber;
	}

	/**
	 * 
	 * @param labContactNumber
	 *            The lab_contact_number
	 */
	public void setLabContactNumber(List<LabContactNumber> labContactNumber) {
		this.labContactNumber = labContactNumber;
	}

	/**
	 * 
	 * @return The patientDetail
	 */
	public String getPatientDetail() {
		return patientDetail;
	}

	/**
	 * 
	 * @param patientDetail
	 *            The patient_detail
	 */
	public void setPatientDetail(String patientDetail) {
		this.patientDetail = patientDetail;
	}

	/**
	 * 
	 * @return The patientAddress
	 */
	public String getPatientAddress() {
		return patientAddress;
	}

	/**
	 * 
	 * @param patientAddress
	 *            The patient_address
	 */
	public void setPatientAddress(String patientAddress) {
		this.patientAddress = patientAddress;
	}

	/**
	 * 
	 * @return The patientContactNumber
	 */
	public String getPatientContactNumber() {
		return patientContactNumber;
	}

	/**
	 * 
	 * @param patientContactNumber
	 *            The patient_contact_number
	 */
	public void setPatientContactNumber(String patientContactNumber) {
		this.patientContactNumber = patientContactNumber;
	}

	/**
	 * 
	 * @return The patientEmail
	 */
	public String getPatientEmail() {
		return patientEmail;
	}

	/**
	 * 
	 * @param patientEmail
	 *            The patient_email
	 */
	public void setPatientEmail(String patientEmail) {
		this.patientEmail = patientEmail;
	}

	/**
	 * 
	 * @return The orderDate
	 */
	public String getOrderDate() {
		return orderDate;
	}

	/**
	 * 
	 * @param orderDate
	 *            The order_date
	 */
	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}

	/**
	 * 
	 * @return The displayOrderId
	 */
	public String getDisplayOrderId() {
		return displayOrderId;
	}

	/**
	 * 
	 * @param displayOrderId
	 *            The display_order_id
	 */
	public void setDisplayOrderId(String displayOrderId) {
		this.displayOrderId = displayOrderId;
	}

	/**
	 * 
	 * @return The technicianName
	 */
	public String getTechnicianName() {
		return technicianName;
	}

	/**
	 * 
	 * @param technicianName
	 *            The technician_name
	 */
	public void setTechnicianName(String technicianName) {
		this.technicianName = technicianName;
	}

	/**
	 * 
	 * @return The orderStatus
	 */
	public String getOrderStatus() {
		return orderStatus;
	}

	/**
	 * 
	 * @param orderStatus
	 *            The order_status
	 */
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	/**
	 * 
	 * @return The doctorDetail
	 */
	public String getDoctorDetail() {
		return doctorDetail;
	}

	/**
	 * 
	 * @param doctorDetail
	 *            The doctor_detail
	 */
	public void setDoctorDetail(String doctorDetail) {
		this.doctorDetail = doctorDetail;
	}

	/**
	 * 
	 * @return The approvalStatus
	 */
	public String getApprovalStatus() {
		return approvalStatus;
	}

	/**
	 * 
	 * @param approvalStatus
	 *            The approval_status
	 */
	public void setApprovalStatus(String approvalStatus) {
		this.approvalStatus = approvalStatus;
	}

	/**
	 * 
	 * @return The patientLocationAppointment
	 */
	public String getPatientLocationAppointment() {
		return patientLocationAppointment;
	}

	/**
	 * 
	 * @param patientLocationAppointment
	 *            The patient_location_appointment
	 */
	public void setPatientLocationAppointment(String patientLocationAppointment) {
		this.patientLocationAppointment = patientLocationAppointment;
	}

	/**
	 * 
	 * @return The testReports
	 */
	public List<TestReport> getTestReports() {
		return testReports;
	}

	/**
	 * 
	 * @param testReports
	 *            The test_reports
	 */
	public void setTestReports(List<TestReport> testReports) {
		this.testReports = testReports;
	}

	/**
	 * 
	 * @return The labImage
	 */
	public String getLabImage() {
		return labImage;
	}

	/**
	 * 
	 * @param patientLocationAppointment
	 *            The patient_location_appointment
	 */
	public void setLabImage(String labImage) {
		this.labImage = labImage;
	}

}
