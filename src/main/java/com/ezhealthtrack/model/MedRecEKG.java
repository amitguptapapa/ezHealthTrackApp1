package com.ezhealthtrack.model;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MedRecEKG {

	@Expose
	private String s;
	@Expose
	private String count;
	@Expose
	private List<Datum> data = new ArrayList<Datum>();

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
	public List<Datum> getData() {
		return data;
	}

	/**
	 * 
	 * @param data
	 *            The data
	 */
	public void setData(List<Datum> data) {
		this.data = data;
	}

	public class Datum {

		@SerializedName("booking_date")
		@Expose
		private String bookingDate;
		@Expose
		private String soap;
		@Expose
		private ArrayList<Document> documents = new ArrayList<Document>();
		@SerializedName("doctor_type")
		@Expose
		private String doctorType;

		/**
		 * 
		 * @return The bookingDate
		 */
		public String getBookingDate() {
			return bookingDate;
		}

		/**
		 * 
		 * @param bookingDate
		 *            The booking_date
		 */
		public void setBookingDate(String bookingDate) {
			this.bookingDate = bookingDate;
		}

		/**
		 * 
		 * @return The soap
		 */
		public String getSoap() {
			return soap;
		}

		/**
		 * 
		 * @param soap
		 *            The soap
		 */
		public void setSoap(String soap) {
			this.soap = soap;
		}

		/**
		 * 
		 * @return The documents
		 */
		public ArrayList<Document> getDocuments() {
			return documents;
		}

		/**
		 * 
		 * @param documents
		 *            The documents
		 */
		public void setDocuments(ArrayList<Document> documents) {
			this.documents = documents;
		}

		/**
		 * 
		 * @return The doctorType
		 */
		public String getDoctorType() {
			return doctorType;
		}

		/**
		 * 
		 * @param doctorType
		 *            The doctor_type
		 */
		public void setDoctorType(String doctorType) {
			this.doctorType = doctorType;
		}

		public String getEKGString() {
			int count = 0;
			String s = "<br><b>Date : " + getBookingDate() + "</b>";
			if (count == 0) {
				s = s + "<br> -- No EKG/ECG Test Added -- ";
			}
			return s;
		}

	}

}