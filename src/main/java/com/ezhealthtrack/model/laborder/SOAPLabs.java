package com.ezhealthtrack.model.laborder;

import java.util.ArrayList;
import java.util.List;

import com.ezhealthtrack.model.LabOrder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SOAPLabs {

	@SerializedName("lab_orders")
	@Expose
	private List<LabOrder> labOrders = new ArrayList<LabOrder>();
	@SerializedName("internal_labs")
	@Expose
	private List<LabInfo> internalLabs = new ArrayList<LabInfo>();
	@SerializedName("external_labs")
	@Expose
	private List<LabInfo> externalLabs = new ArrayList<LabInfo>();
	@SerializedName("available_labs")
	@Expose
	private List<LabInfo> availableLabs = new ArrayList<LabInfo>();

	/**
	 * 
	 * @return The labOrders
	 */
	public List<LabOrder> getLabOrders() {
		return labOrders;
	}

	/**
	 * 
	 * @param labOrders
	 *            The lab_orders
	 */
	public void setLabOrders(List<LabOrder> labOrders) {
		this.labOrders = labOrders;
	}

	/**
	 * 
	 * @return The internalLabs
	 */
	public List<LabInfo> getInternalLabs() {
		return internalLabs;
	}

	/**
	 * 
	 * @param internalLabs
	 *            The internal_labs
	 */
	public void setInternalLabs(List<LabInfo> internalLabs) {
		this.internalLabs = internalLabs;
	}

	/**
	 * 
	 * @return The externalLabs
	 */
	public List<LabInfo> getExternalLabs() {
		return externalLabs;
	}

	/**
	 * 
	 * @param externalLabs
	 *            The external_labs
	 */
	public void setExternalLabs(List<LabInfo> externalLabs) {
		this.externalLabs = externalLabs;
	}

	/**
	 * 
	 * @return The availableLabs
	 */
	public List<LabInfo> getAvailableLabs() {
		return availableLabs;
	}

	/**
	 * 
	 * @param availableLabs
	 *            The available_labs
	 */
	public void setAvailableLabs(List<LabInfo> availableLabs) {
		this.availableLabs = availableLabs;
	}

}