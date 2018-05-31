package com.ezhealthtrack.model.laborder;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result {

	@Expose
	private String name;
	@Expose
	private String unit;
	@Expose
	private String notes;
	@SerializedName("unit_options")
	@Expose
	private List<String> unitOptions = new ArrayList<String>();
	@SerializedName("unit_type")
	@Expose
	private String unitType;
	@Expose
	private List<Reference> references = new ArrayList<Reference>();
	@Expose
	private String value;
	@Expose
	private String status;

	/**
	 * 
	 * @return The name
	 */
	public String getName() {
		return name;
	}

	/**
	 * 
	 * @param name
	 *            The name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 
	 * @return The unit
	 */
	public String getUnit() {
		return unit;
	}

	/**
	 * 
	 * @param unit
	 *            The unit
	 */
	public void setUnit(String unit) {
		this.unit = unit;
	}

	/**
	 * 
	 * @return The notes
	 */
	public String getNotes() {
		return notes;
	}

	/**
	 * 
	 * @param notes
	 *            The notes
	 */
	public void setNotes(String notes) {
		this.notes = notes;
	}

	/**
	 * 
	 * @return The unitOptions
	 */
	public List<String> getUnitOptions() {
		return unitOptions;
	}

	/**
	 * 
	 * @param unitOptions
	 *            The unit_options
	 */
	public void setUnitOptions(List<String> unitOptions) {
		this.unitOptions = unitOptions;
	}

	/**
	 * 
	 * @return The unitType
	 */
	public String getUnitType() {
		return unitType;
	}

	/**
	 * 
	 * @param unitType
	 *            The unit_type
	 */
	public void setUnitType(String unitType) {
		this.unitType = unitType;
	}

	/**
	 * 
	 * @return The references
	 */
	public List<Reference> getReferences() {
		return references;
	}

	/**
	 * 
	 * @param references
	 *            The references
	 */
	public void setReferences(List<Reference> references) {
		this.references = references;
	}

	/**
	 * 
	 * @return The value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * 
	 * @param value
	 *            The value
	 */
	public void setValue(String value) {
		this.value = value;
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

}