package com.ezhealthtrack.model;

import java.util.ArrayList;
import java.util.List;

import com.ezhealthtrack.model.laborder.TestReport;

public class LabOrder extends Object {

	private String order_id;
	private String order_display_id;
	private String patient_detail;
	private String assign_to;
	private String order_date;
	private String order_status;
	private String approval_status;
	private String approved_by;
	private String lab_name;
	private List<TestReport> test_reports = new ArrayList<TestReport>();

	public boolean isChecked;

	public String getOrderSummary() {
		// Order Id 1507-0910004422-00035 on Wed, Jul 22, 2015 with Lab:
		// anuj_lab2, order status: Pending and Approval status :
		// Pending

		String details = "Order Id " + order_display_id;
		details += " created on " + order_date;
		details += " with Lab : " + lab_name;
		if (order_status != null && order_status.length() > 0)
			details += ", Order Status : " + order_status;
		details += ", Approval Status : " + approval_status;
		return details;
	}

	public List<TestReport> getTestReports() {
		return test_reports;
	}

	public void setTestReports(List<TestReport> testReports) {
		this.test_reports = testReports;
	}

	public String getId() {
		return order_id;
	}

	public void setId(String id) {
		this.order_id = id;
	}

	public String getDisplayId() {
		return order_display_id;
	}

	public void setDisplayId(String id) {
		this.order_display_id = id;
	}

	public String getPatientDetail() {
		return patient_detail;
	}

	public void setPatientDetail(String detail) {
		this.patient_detail = detail;
	}

	public String getAssignTo() {
		return assign_to;
	}

	public void setAssignTo(String name) {
		this.assign_to = name;
	}

	public String getOrderDate() {
		return order_date;
	}

	public void setOrderDate(String note) {
		this.order_date = note;
	}

	public String getOrderStatus() {
		return order_status;
	}

	public void setOrderStatus(String note) {
		this.order_status = note;
	}

	public String getApprovalStatus() {
		return approval_status;
	}

	public void setApprovalStatus(String note) {
		this.approval_status = note;
	}

	public String getApprovedBy() {
		return approved_by;
	}

	public void setApprovedBy(String note) {
		this.approved_by = note;
	}

	public String getLabName() {
		return lab_name;
	}

	public void setLabName(String note) {
		this.lab_name = note;
	}
}