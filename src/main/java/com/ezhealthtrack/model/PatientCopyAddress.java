package com.ezhealthtrack.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PatientCopyAddress {

	@Expose
	private String address;
	@Expose
	private String address2;
	@SerializedName("country_id")
	@Expose
	private String countryId;
	@Expose
	private String cmbState;
	@SerializedName("cmbState_text")
	@Expose
	private String cmbStateText;
	@Expose
	private String cmbCity;
	@SerializedName("cmbCity_text")
	@Expose
	private String cmbCityText;
	@Expose
	private String cmbArea;
	@SerializedName("cmbArea_text")
	@Expose
	private String cmbAreaText;
	@Expose
	private String zip;

	/**
	 * 
	 * @return The address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * 
	 * @param address
	 *            The address
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * 
	 * @return The address2
	 */
	public String getAddress2() {
		return address2;
	}

	/**
	 * 
	 * @param address2
	 *            The address2
	 */
	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	/**
	 * 
	 * @return The countryId
	 */
	public String getCountryId() {
		return countryId;
	}

	/**
	 * 
	 * @param countryId
	 *            The country_id
	 */
	public void setCountryId(String countryId) {
		this.countryId = countryId;
	}

	/**
	 * 
	 * @return The cmbState
	 */
	public String getCmbState() {
		return cmbState;
	}

	/**
	 * 
	 * @param cmbState
	 *            The cmbState
	 */
	public void setCmbState(String cmbState) {
		this.cmbState = cmbState;
	}

	/**
	 * 
	 * @return The cmbStateText
	 */
	public String getCmbStateText() {
		return cmbStateText;
	}

	/**
	 * 
	 * @param cmbStateText
	 *            The cmbState_text
	 */
	public void setCmbStateText(String cmbStateText) {
		this.cmbStateText = cmbStateText;
	}

	/**
	 * 
	 * @return The cmbCity
	 */
	public String getCmbCity() {
		return cmbCity;
	}

	/**
	 * 
	 * @param cmbCity
	 *            The cmbCity
	 */
	public void setCmbCity(String cmbCity) {
		this.cmbCity = cmbCity;
	}

	/**
	 * 
	 * @return The cmbCityText
	 */
	public String getCmbCityText() {
		return cmbCityText;
	}

	/**
	 * 
	 * @param cmbCityText
	 *            The cmbCity_text
	 */
	public void setCmbCityText(String cmbCityText) {
		this.cmbCityText = cmbCityText;
	}

	/**
	 * 
	 * @return The cmbArea
	 */
	public String getCmbArea() {
		return cmbArea;
	}

	/**
	 * 
	 * @param cmbArea
	 *            The cmbArea
	 */
	public void setCmbArea(String cmbArea) {
		this.cmbArea = cmbArea;
	}

	/**
	 * 
	 * @return The cmbAreaText
	 */
	public String getCmbAreaText() {
		return cmbAreaText;
	}

	/**
	 * 
	 * @param cmbAreaText
	 *            The cmbArea_text
	 */
	public void setCmbAreaText(String cmbAreaText) {
		this.cmbAreaText = cmbAreaText;
	}

	/**
	 * 
	 * @return The zip
	 */
	public String getZip() {
		return zip;
	}

	/**
	 * 
	 * @param zip
	 *            The zip
	 */
	public void setZip(String zip) {
		this.zip = zip;
	}

	public class DefaultAddress {

		@Expose
		private String s;
		@Expose
		private PatientCopyAddress params;

		/**
		 * 
		 * @return The s
		 */
		public String getS() {
			return s;
		}

		/**
		 * 
		 * @param s
		 *            The s
		 */
		public void setS(String s) {
			this.s = s;
		}

		/**
		 * 
		 * @return The params
		 */
		public PatientCopyAddress getParams() {
			return params;
		}

		/**
		 * 
		 * @param params
		 *            The params
		 */
		public void setParams(PatientCopyAddress params) {
			this.params = params;
		}

	}
}
