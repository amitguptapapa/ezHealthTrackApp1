package com.ezhealthtrack.order;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Reference {

	@Expose
	private String gender;
	@SerializedName("gender_option")
	@Expose
	private String genderOption;
	@SerializedName("age_range_required")
	@Expose
	private Boolean ageRangeRequired;
	@SerializedName("age_range_min")
	@Expose
	private String ageRangeMin;
	@SerializedName("age_range_max")
	@Expose
	private String ageRangeMax;
	@SerializedName("age_range_min_type")
	@Expose
	private String ageRangeMinType;
	@SerializedName("age_range_max_type")
	@Expose
	private String ageRangeMaxType;
	@SerializedName("age_range_option")
	@Expose
	private String ageRangeOption;
	@SerializedName("range_value_min")
	@Expose
	private String rangeValueMin;
	@SerializedName("range_value_min_option")
	@Expose
	private String rangeValueMinOption;
	@SerializedName("range_value_max")
	@Expose
	private String rangeValueMax;
	@SerializedName("range_value_max_option")
	@Expose
	private String rangeValueMaxOption;
	@Expose
	private String notes;

	/**
	 * 
	 * @return The gender
	 */
	public String getGender() {
		return gender;
	}

	/**
	 * 
	 * @param gender
	 *            The gender
	 */
	public void setGender(String gender) {
		this.gender = gender;
	}

	/**
	 * 
	 * @return The genderOption
	 */
	public String getGenderOption() {
		return genderOption;
	}

	/**
	 * 
	 * @param genderOption
	 *            The gender_option
	 */
	public void setGenderOption(String genderOption) {
		this.genderOption = genderOption;
	}

	/**
	 * 
	 * @return The ageRangeRequired
	 */
	public Boolean getAgeRangeRequired() {
		return ageRangeRequired;
	}

	/**
	 * 
	 * @param ageRangeRequired
	 *            The age_range_required
	 */
	public void setAgeRangeRequired(Boolean ageRangeRequired) {
		this.ageRangeRequired = ageRangeRequired;
	}

	/**
	 * 
	 * @return The ageRangeMin
	 */
	public int getAgeRangeMin() {
		try {
			return Integer.parseInt(ageRangeMin);
		} catch (Exception e) {
			return 0;
		}
	}

	/**
	 * 
	 * @param ageRangeMin
	 *            The age_range_min
	 */
	public void setAgeRangeMin(String ageRangeMin) {
		this.ageRangeMin = ageRangeMin;
	}

	/**
	 * 
	 * @return The ageRangeMax
	 */
	public int getAgeRangeMax() {
		try {
			return Integer.parseInt(ageRangeMax);
		} catch (Exception e) {
			return 150;
		}
	}

	/**
	 * 
	 * @param ageRangeMax
	 *            The age_range_max
	 */
	public void setAgeRangeMax(String ageRangeMax) {
		this.ageRangeMax = ageRangeMax;
	}

	/**
	 * 
	 * @return The ageRangeMinType
	 */
	public String getAgeRangeMinType() {
		return ageRangeMinType;
	}

	/**
	 * 
	 * @param ageRangeMinType
	 *            The age_range_min_type
	 */
	public void setAgeRangeMinType(String ageRangeMinType) {
		this.ageRangeMinType = ageRangeMinType;
	}

	/**
	 * 
	 * @return The ageRangeMaxType
	 */
	public String getAgeRangeMaxType() {
		return ageRangeMaxType;
	}

	/**
	 * 
	 * @param ageRangeMaxType
	 *            The age_range_max_type
	 */
	public void setAgeRangeMaxType(String ageRangeMaxType) {
		this.ageRangeMaxType = ageRangeMaxType;
	}

	/**
	 * 
	 * @return The ageRangeOption
	 */
	public String getAgeRangeOption() {
		return ageRangeOption;
	}

	/**
	 * 
	 * @param ageRangeOption
	 *            The age_range_option
	 */
	public void setAgeRangeOption(String ageRangeOption) {
		this.ageRangeOption = ageRangeOption;
	}

	/**
	 * 
	 * @return The rangeValueMin
	 */
	public String getRangeValueMin() {
		return rangeValueMin;
	}

	/**
	 * 
	 * @param rangeValueMin
	 *            The range_value_min
	 */
	public void setRangeValueMin(String rangeValueMin) {
		this.rangeValueMin = rangeValueMin;
	}

	/**
	 * 
	 * @return The rangeValueMinOption
	 */
	public String getRangeValueMinOption() {
		return rangeValueMinOption;
	}

	/**
	 * 
	 * @param rangeValueMinOption
	 *            The range_value_min_option
	 */
	public void setRangeValueMinOption(String rangeValueMinOption) {
		this.rangeValueMinOption = rangeValueMinOption;
	}

	/**
	 * 
	 * @return The rangeValueMax
	 */
	public String getRangeValueMax() {
		return rangeValueMax;
	}

	/**
	 * 
	 * @param rangeValueMax
	 *            The range_value_max
	 */
	public void setRangeValueMax(String rangeValueMax) {
		this.rangeValueMax = rangeValueMax;
	}

	/**
	 * 
	 * @return The rangeValueMaxOption
	 */
	public String getRangeValueMaxOption() {
		return rangeValueMaxOption;
	}

	/**
	 * 
	 * @param rangeValueMaxOption
	 *            The range_value_max_option
	 */
	public void setRangeValueMaxOption(String rangeValueMaxOption) {
		this.rangeValueMaxOption = rangeValueMaxOption;
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

}
