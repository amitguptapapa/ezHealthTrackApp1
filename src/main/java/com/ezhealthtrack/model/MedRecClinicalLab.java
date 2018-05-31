package com.ezhealthtrack.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.ezhealthtrack.EzApp;
import com.ezhealthtrack.DentistSoap.Model.LabModel;
import com.ezhealthtrack.util.Constants;
import com.ezhealthtrack.util.Log;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MedRecClinicalLab {

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

		public String getCinicalLabString() {
			final Iterator<String> iter;
			String s = "<br><b>Date : " + getBookingDate() + "</b>";
			Log.i("soap", soap);
			try {
				JSONObject jobj = new JSONObject(soap);
				if (EzApp.sharedPref.getString(
						Constants.DR_SPECIALITY, "")
						.equalsIgnoreCase("Dentist")) {
					LabModel model = new LabModel();
					model.JsonParse(jobj);
					s = s + "<br> " + model.getLabString();
				} else {
					int count = 0;
					iter = jobj.keys();
					while (iter.hasNext()) {
						final String key = iter.next();
						if (jobj.get(key).toString().equalsIgnoreCase("on")) {
							String[] result = key.split("_");
							s += "<br> "
									+ key.replace("_"
											+ result[result.length - 1], "");
							count++;
						}

					}
					if (count == 0) {
						s = s + "<br> -- No Lab Test Added -- ";
					}
				}
			} catch (JSONException e) {
				s = s + "<br> -- No Lab Test Added -- ";
				e.printStackTrace();
			}
			return s;
		}
	}
}
