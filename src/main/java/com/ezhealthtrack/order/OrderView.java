package com.ezhealthtrack.order;

import java.util.ArrayList;
import java.util.List;

import com.ezhealthtrack.greendao.Order;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderView {

	@Expose
	private Order order = new Order();
	@SerializedName("lab_order_detail")
	@Expose
	private LabOrderDetail labOrderDetail = new LabOrderDetail();
	@SerializedName("lab_order_reports")
	@Expose
	private List<LabOrderReport> labOrderReports = new ArrayList<LabOrderReport>();

	@Expose
	private ContactInfo contactInfo;

	@Expose
	private String patientLocAppointment;

	/**
	 * 
	 * @return The contactInfo
	 */
	public ContactInfo getContactInfo() {
		return contactInfo;
	}

	/**
	 * 
	 * @param contactInfo
	 *            The contactInfo
	 */
	public void setContactInfo(ContactInfo contactInfo) {
		this.contactInfo = contactInfo;
	}

	/**
	 * 
	 * @return The order
	 */
	public Order getOrder() {
		return order;
	}

	/**
	 * 
	 * @param order
	 *            The order
	 */
	public void setOrder(Order order) {
		this.order = order;
	}

	/**
	 * 
	 * @return The labOrderDetail
	 */
	public LabOrderDetail getLabOrderDetail() {
		return labOrderDetail;
	}

	/**
	 * 
	 * @param labOrderDetail
	 *            The lab_order_detail
	 */
	public void setLabOrderDetail(LabOrderDetail labOrderDetail) {
		this.labOrderDetail = labOrderDetail;
	}

	/**
	 * 
	 * @return The labOrderReports
	 */
	public List<LabOrderReport> getLabOrderReports() {
		return labOrderReports;
	}

	/**
	 * 
	 * @param labOrderReports
	 *            The lab_order_reports
	 */
	public void setLabOrderReports(List<LabOrderReport> labOrderReports) {
		this.labOrderReports = labOrderReports;
	}

	/**
	 * 
	 * @return The patientLocAppointment
	 */
	public String getPatientLocAppointment() {
		return patientLocAppointment;
	}

	/**
	 * 
	 * @param patientLocAppointment
	 *            The patientLocAppointment
	 */
	public void setPatientLocAppointment(String patientLocAppointment) {
		this.patientLocAppointment = patientLocAppointment;
	}

}