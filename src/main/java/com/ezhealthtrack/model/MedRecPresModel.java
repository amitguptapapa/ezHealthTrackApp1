package com.ezhealthtrack.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONObject;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MedRecPresModel {

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

	public String getDataString() {
		String s = "";

		if (data.size() > 0) {
			for (Datum datum : data) {
				s = s + datum.getPrescriptionString();
			}
		} else {
			s = s + "No Prescription added";
		}
		return s;
	}

	public class Datum {

		@SerializedName("booking_date")
		@Expose
		private String bookingDate;
		@Expose
		private String soap;
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

		public String getPrescriptionString() {
			final Iterator<String> iter;
			String s = "<br><b>Date : " + getBookingDate() + "</b><br>";
			try {
				final JSONObject drug = new JSONObject(getSoap());
				iter = drug.keys();
				ArrayList<MedicineModel> arrMedicine = new ArrayList<MedicineModel>();
				int i = 1;
				while (iter.hasNext()) {
					final String key = iter.next();
					final MedicineModel medicine = new MedicineModel();
					medicine.name = drug.getJSONObject(key).getString("dn");
					medicine.strength = drug.getJSONObject(key).getString(
							"stre");
					medicine.unit = drug.getJSONObject(key).getString("ut");
					medicine.formulations = drug.getJSONObject(key).getString(
							"form");
					medicine.route = drug.getJSONObject(key).getString("rout");
					medicine.frequency = drug.getJSONObject(key).getString(
							"freq");
					medicine.times = drug.getJSONObject(key)
							.getString("ftimes");
					medicine.quantity = drug.getJSONObject(key).getString(
							"quan");
					medicine.refillsTime = drug.getJSONObject(key).getString(
							"rt");
					if (drug.getJSONObject(key).has("refi")) {
						if (drug.getJSONObject(key).getString("refi")
								.equals("on"))
							medicine.refills = "Yes";
						else
							medicine.refills = "No";
					}
					if (drug.getJSONObject(key).has("rf"))
						medicine.rf = drug.getJSONObject(key).getString("rf");

					s = s + "<b>" + i + ". </b>"
							+ medicine.getMedicineStringExtra() + "<br>";

					arrMedicine.add(medicine);
					i++;

				}
			} catch (Exception e) {
				s = s + " -- No Prescription Added -- <br>";
				e.printStackTrace();
			}
			return s;
		}

	}

}