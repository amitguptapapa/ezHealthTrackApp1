package com.ezhealthtrack.model;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AllergyModel {

	@Expose
	private String s;
	@Expose
	private String count;
	@Expose
	private ArrayList<Datum> data = new ArrayList<Datum>();

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
	 * @return The count
	 */
	public String getCount() {
		return count;
	}

	/**
	 * 
	 * @param count
	 *            The count
	 */
	public void setCount(String count) {
		this.count = count;
	}

	/**
	 * 
	 * @return The data
	 */
	public ArrayList<Datum> getData() {
		return data;
	}

	/**
	 * 
	 * @param data
	 *            The data
	 */
	public void setData(ArrayList<Datum> data) {
		this.data = data;
	}

	public class Datum {

		@SerializedName("pat_name")
		@Expose
		private String patName;
		@SerializedName("sub_cat")
		@Expose
		private SubCat subCat;
		@SerializedName("main_cat")
		@Expose
		private MainCat mainCat;
		@SerializedName("additional_info")
		@Expose
		private String additionalInfo;
		@SerializedName("allergie_id")
		@Expose
		private String allergieId;
		@SerializedName("date_created")
		@Expose
		private String dateCreated;

		/**
		 * 
		 * @return The patName
		 */
		public String getPatName() {
			return patName;
		}

		/**
		 * 
		 * @param patName
		 *            The pat_name
		 */
		public void setPatName(String patName) {
			this.patName = patName;
		}

		/**
		 * 
		 * @return The subCat
		 */
		public SubCat getSubCat() {
			return subCat;
		}

		/**
		 * 
		 * @param subCat
		 *            The sub_cat
		 */
		public void setSubCat(SubCat subCat) {
			this.subCat = subCat;
		}

		/**
		 * 
		 * @return The mainCat
		 */
		public MainCat getMainCat() {
			return mainCat;
		}

		/**
		 * 
		 * @param mainCat
		 *            The main_cat
		 */
		public void setMainCat(MainCat mainCat) {
			this.mainCat = mainCat;
		}

		/**
		 * 
		 * @return The additionalInfo
		 */
		public String getAdditionalInfo() {
			return additionalInfo;
		}

		/**
		 * 
		 * @param additionalInfo
		 *            The additional_info
		 */
		public void setAdditionalInfo(String additionalInfo) {
			this.additionalInfo = additionalInfo;
		}

		/**
		 * 
		 * @return The allergieId
		 */
		public String getAllergieId() {
			return allergieId;
		}

		/**
		 * 
		 * @param allergieId
		 *            The allergie_id
		 */
		public void setAllergieId(String allergieId) {
			this.allergieId = allergieId;
		}

		/**
		 * 
		 * @return The dateCreated
		 */
		public String getDateCreated() {
			return dateCreated;
		}

		/**
		 * 
		 * @param dateCreated
		 *            The date_created
		 */
		public void setDateCreated(String dateCreated) {
			this.dateCreated = dateCreated;
		}

	}

	public class MainCat {

		@Expose
		private String ID;
		@Expose
		private String NAME;
		@Expose
		private String ParentID;

		/**
		 * 
		 * @return The ID
		 */
		public String getID() {
			return ID;
		}

		/**
		 * 
		 * @param ID
		 *            The ID
		 */
		public void setID(String ID) {
			this.ID = ID;
		}

		/**
		 * 
		 * @return The NAME
		 */
		public String getNAME() {
			return NAME;
		}

		/**
		 * 
		 * @param NAME
		 *            The NAME
		 */
		public void setNAME(String NAME) {
			this.NAME = NAME;
		}

		/**
		 * 
		 * @return The ParentID
		 */
		public String getParentID() {
			return ParentID;
		}

		/**
		 * 
		 * @param ParentID
		 *            The ParentID
		 */
		public void setParentID(String ParentID) {
			this.ParentID = ParentID;
		}

	}

	public class SubCat {

		@Expose
		private String ID;
		@Expose
		private String NAME;
		@Expose
		private String ParentID;

		/**
		 * 
		 * @return The ID
		 */
		public String getID() {
			return ID;
		}

		/**
		 * 
		 * @param ID
		 *            The ID
		 */
		public void setID(String ID) {
			this.ID = ID;
		}

		/**
		 * 
		 * @return The NAME
		 */
		public String getNAME() {
			return NAME;
		}

		/**
		 * 
		 * @param NAME
		 *            The NAME
		 */
		public void setNAME(String NAME) {
			this.NAME = NAME;
		}

		/**
		 * 
		 * @return The ParentID
		 */
		public String getParentID() {
			return ParentID;
		}

		/**
		 * 
		 * @param ParentID
		 *            The ParentID
		 */
		public void setParentID(String ParentID) {
			this.ParentID = ParentID;
		}

	}

}