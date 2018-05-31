package com.ezhealthtrack.order;

import java.util.ArrayList;
import java.util.List;

import com.ezhealthtrack.greendao.LabTechnician;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CreateOrder {

	@Expose
	private List<LabTechnician> technician = new ArrayList<LabTechnician>();
	@SerializedName("lab_tests")
	@Expose
	private List<LabTest> labTests = new ArrayList<LabTest>();

	@SerializedName("lab_panels")
	@Expose
	private List<LabPanel> labPanels = new ArrayList<LabPanel>();

	private String fkey;

	private String homeCounsultationCharges;

	/**
	 * 
	 * @return The technician
	 */
	public List<LabTechnician> getTechnician() {
		return technician;
	}

	/**
	 * 
	 * @param technician
	 *            The technician
	 */
	public void setTechnician(List<LabTechnician> technician) {
		this.technician = technician;
	}

	/**
	 * 
	 * @return The labTests
	 */
	public List<LabTest> getLabTests() {
		return labTests;
	}

	/**
	 * 
	 * @param labTests
	 *            The lab_tests
	 */
	public void setLabTests(List<LabTest> labTests) {
		this.labTests = labTests;
	}

	/**
	 * 
	 * @return The labPanel
	 */
	public List<LabPanel> getLabPanel() {
		return labPanels;
	}

	/**
	 * 
	 * @param labPanel
	 *            The lab_panel
	 */
	public void setLabPanel(List<LabPanel> labPanels) {
		this.labPanels = labPanels;
	}

	public String getFkey() {
		return fkey;
	}

	public void setFkey(String fkey) {
		this.fkey = fkey;
	}

	public String getHomeConsultation() {
		return homeCounsultationCharges;
	}

	public void setHomeConsultation(String homeCounsultationCharges) {
		this.homeCounsultationCharges = homeCounsultationCharges;
	}

}
