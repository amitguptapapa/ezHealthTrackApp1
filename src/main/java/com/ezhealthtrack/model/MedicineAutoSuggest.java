package com.ezhealthtrack.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MedicineAutoSuggest {

	@Expose
	private String id;
	@SerializedName("display_name")
	@Expose
	private String displayName;
	@Expose
	private String name;
	@Expose
	private String strength;
	@SerializedName("unit_type")
	@Expose
	private String unitType;
	@Expose
	private String formulations;
	@Expose
	private String route;
	@Expose
	private String frequency;
	@SerializedName("number_of_days")
	@Expose
	private String numberOfDays;
	@Expose
	private String quantity;
	@Expose
	private String refills;
	@SerializedName("refills_times")
	@Expose
	private String refillsTimes;
	@Expose
	private String type;
	@Expose
	private String country;
	@SerializedName("profile_id")
	@Expose
	private String profileId;
	@SerializedName("profile_type")
	@Expose
	private String profileType;
	@SerializedName("created_on")
	@Expose
	private String createdOn;
	@SerializedName("created_by")
	@Expose
	private String createdBy;
	@SerializedName("updated_on")
	@Expose
	private String updatedOn;
	@SerializedName("updated_by")
	@Expose
	private String updatedBy;
	@Expose
	private String status;
	@Expose
	private String notes;

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
	 * @return The displayName
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * 
	 * @param displayName
	 *            The display_name
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

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
	 * @return The strength
	 */
	public String getStrength() {
		return strength;
	}

	/**
	 * 
	 * @param strength
	 *            The strength
	 */
	public void setStrength(String strength) {
		this.strength = strength;
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
	 * @return The formulations
	 */
	public String getFormulations() {
		return formulations;
	}

	/**
	 * 
	 * @param formulations
	 *            The formulations
	 */
	public void setFormulations(String formulations) {
		this.formulations = formulations;
	}

	/**
	 * 
	 * @return The route
	 */
	public String getRoute() {
		return route;
	}

	/**
	 * 
	 * @param route
	 *            The route
	 */
	public void setRoute(String route) {
		this.route = route;
	}

	/**
	 * 
	 * @return The frequency
	 */
	public String getFrequency() {
		return frequency;
	}

	/**
	 * 
	 * @param frequency
	 *            The frequency
	 */
	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}

	/**
	 * 
	 * @return The numberOfDays
	 */
	public String getNumberOfDays() {
		return numberOfDays;
	}

	/**
	 * 
	 * @param numberOfDays
	 *            The number_of_days
	 */
	public void setNumberOfDays(String numberOfDays) {
		this.numberOfDays = numberOfDays;
	}

	/**
	 * 
	 * @return The quantity
	 */
	public String getQuantity() {
		return quantity;
	}

	/**
	 * 
	 * @param quantity
	 *            The quantity
	 */
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	/**
	 * 
	 * @return The refills
	 */
	public String getRefills() {
		return refills;
	}

	/**
	 * 
	 * @param refills
	 *            The refills
	 */
	public void setRefills(String refills) {
		this.refills = refills;
	}

	/**
	 * 
	 * @return The refillsTimes
	 */
	public String getRefillsTimes() {
		return refillsTimes;
	}

	/**
	 * 
	 * @param refillsTimes
	 *            The refills_times
	 */
	public void setRefillsTimes(String refillsTimes) {
		this.refillsTimes = refillsTimes;
	}

	/**
	 * 
	 * @return The type
	 */
	public String getType() {
		return type;
	}

	/**
	 * 
	 * @param type
	 *            The type
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * 
	 * @return The country
	 */
	public String getCountry() {
		return country;
	}

	/**
	 * 
	 * @param country
	 *            The country
	 */
	public void setCountry(String country) {
		this.country = country;
	}

	/**
	 * 
	 * @return The profileId
	 */
	public String getProfileId() {
		return profileId;
	}

	/**
	 * 
	 * @param profileId
	 *            The profile_id
	 */
	public void setProfileId(String profileId) {
		this.profileId = profileId;
	}

	/**
	 * 
	 * @return The profileType
	 */
	public String getProfileType() {
		return profileType;
	}

	/**
	 * 
	 * @param profileType
	 *            The profile_type
	 */
	public void setProfileType(String profileType) {
		this.profileType = profileType;
	}

	/**
	 * 
	 * @return The createdOn
	 */
	public String getCreatedOn() {
		return createdOn;
	}

	/**
	 * 
	 * @param createdOn
	 *            The created_on
	 */
	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}

	/**
	 * 
	 * @return The createdBy
	 */
	public String getCreatedBy() {
		return createdBy;
	}

	/**
	 * 
	 * @param createdBy
	 *            The created_by
	 */
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	/**
	 * 
	 * @return The updatedOn
	 */
	public String getUpdatedOn() {
		return updatedOn;
	}

	/**
	 * 
	 * @param updatedOn
	 *            The updated_on
	 */
	public void setUpdatedOn(String updatedOn) {
		this.updatedOn = updatedOn;
	}

	/**
	 * 
	 * @return The updatedBy
	 */
	public String getUpdatedBy() {
		return updatedBy;
	}

	/**
	 * 
	 * @param updatedBy
	 *            The updated_by
	 */
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
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

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return name;
	}

}