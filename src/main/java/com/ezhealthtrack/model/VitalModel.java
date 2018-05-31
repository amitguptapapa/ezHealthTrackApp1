package com.ezhealthtrack.model;

import java.util.ArrayList;
import java.util.List;

import com.ezhealthtrack.util.Util;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VitalModel {

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

				s = s + "<br> Date: " + datum.getBookingDate();
				Soap soap = datum.getSoap();
				s = s
						+ "<br> &nbsp&nbsp&nbsp&nbsp&nbsp<b>Blood Pressure : </b>"
						+ soap.getBp().getHigh().getValue()
						+ " / "
						+ soap.getBp().getLow().getValue()
						+ " mm of Hg"
						+ "<br> &nbsp&nbsp&nbsp&nbsp&nbsp<b>Pulse : </b>"
						+ soap.getPuls().getValue()
						+ " beats/min "
						+ "<br> &nbsp&nbsp&nbsp&nbsp&nbsp<b>Respiratory Rate : </b>"
						+ soap.getRr().getValue() + " breathes/min "
						+ "<br> &nbsp&nbsp&nbsp&nbsp&nbsp<b>Temperature : </b>"
						+ soap.getTemp().getValue() + " &#176;F"
						+ "<br> &nbsp&nbsp&nbsp&nbsp&nbsp<b>Weight : </b>"
						+ soap.getWeig().getValue() + " kg "
						+ "<br> &nbsp&nbsp&nbsp&nbsp&nbsp<b>Height : </b>"
						+ soap.getHeig().getValue() + " cm "
						+ "<br> &nbsp&nbsp&nbsp&nbsp&nbsp<b>Waist : </b>"
						+ soap.getHeig().getValue() + " cm "
						+ "<br> &nbsp&nbsp&nbsp&nbsp&nbsp<b>BMI : </b>"
						+ soap.getHeig().getValue() + " kg/m<sup>2</sup> "
						+ "<br> &nbsp&nbsp&nbsp&nbsp&nbsp<b>Note : </b>"
						+ soap.getNote().getValue() + "<br>";
			}
		} else {
			s = s + "No Vitals added";
		}
		return s;
	}

	public class Weig {

		@Expose
		private String value;

		/**
		 * 
		 * @return The value
		 */
		public String getValue() {
			if (Util.isEmptyString(value))
				return "__";
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

	}

	public class Temp {

		@Expose
		private String value;

		/**
		 * 
		 * @return The value
		 */
		public String getValue() {
			if (Util.isEmptyString(value))
				return "__";
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

	}

	public class Soap {

		@Expose
		private Bp bp = new Bp();
		@Expose
		private Puls puls = new Puls();
		@Expose
		private Rr rr = new Rr();
		@Expose
		private Temp temp = new Temp();
		@Expose
		private Weig weig = new Weig();
		@Expose
		private Heig heig = new Heig();
		@Expose
		private Note note = new Note();

		/**
		 * 
		 * @return The bp
		 */
		public Bp getBp() {
			if (bp == null)
				bp = new Bp();
			return bp;
		}

		/**
		 * 
		 * @param bp
		 *            The bp
		 */
		public void setBp(Bp bp) {
			this.bp = bp;
		}

		/**
		 * 
		 * @return The puls
		 */
		public Puls getPuls() {
			if (puls == null)
				puls = new Puls();
			return puls;
		}

		/**
		 * 
		 * @param puls
		 *            The puls
		 */
		public void setPuls(Puls puls) {
			this.puls = puls;
		}

		/**
		 * 
		 * @return The rr
		 */
		public Rr getRr() {
			if (rr == null)
				rr = new Rr();
			return rr;
		}

		/**
		 * 
		 * @param rr
		 *            The rr
		 */
		public void setRr(Rr rr) {
			this.rr = rr;
		}

		/**
		 * 
		 * @return The temp
		 */
		public Temp getTemp() {
			if (temp == null)
				temp = new Temp();
			return temp;
		}

		/**
		 * 
		 * @param temp
		 *            The temp
		 */
		public void setTemp(Temp temp) {
			this.temp = temp;
		}

		/**
		 * 
		 * @return The weig
		 */
		public Weig getWeig() {
			if (weig == null)
				weig = new Weig();
			return weig;
		}

		/**
		 * 
		 * @param weig
		 *            The weig
		 */
		public void setWeig(Weig weig) {
			this.weig = weig;
		}

		/**
		 * 
		 * @return The heig
		 */
		public Heig getHeig() {
			if (heig == null)
				heig = new Heig();
			return heig;
		}

		/**
		 * 
		 * @param heig
		 *            The heig
		 */
		public void setHeig(Heig heig) {
			this.heig = heig;
		}

		/**
		 * 
		 * @return The note
		 */
		public Note getNote() {
			if (note == null)
				note = new Note();
			return note;
		}

		/**
		 * 
		 * @param note
		 *            The note
		 */
		public void setNote(Note note) {
			this.note = note;
		}

	}

	public class Rr {

		@Expose
		private String value;

		/**
		 * 
		 * @return The value
		 */
		public String getValue() {
			if (Util.isEmptyString(value))
				return "__";
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

	}

	public class Puls {

		@Expose
		private String value;

		/**
		 * 
		 * @return The value
		 */
		public String getValue() {
			if (Util.isEmptyString(value))
				return "__";
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

	}

	public class Note {

		@Expose
		private String value;

		/**
		 * 
		 * @return The value
		 */
		public String getValue() {
			if (Util.isEmptyString(value))
				return "__";
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

	}

	public class Low {

		@Expose
		private String value;

		/**
		 * 
		 * @return The value
		 */
		public String getValue() {
			if (Util.isEmptyString(value))
				return "__";
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

	}

	public class High {

		@Expose
		private String value;

		/**
		 * 
		 * @return The value
		 */
		public String getValue() {
			if (Util.isEmptyString(value))
				return "__";
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

	}

	public class Heig {

		@Expose
		private String value;

		/**
		 * 
		 * @return The value
		 */
		public String getValue() {
			if (Util.isEmptyString(value))
				return "__";
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

	}

	public class Datum {

		@SerializedName("booking_date")
		@Expose
		private String bookingDate;
		@Expose
		private Soap soap = new Soap();
		@Expose
		private List<Object> documents = new ArrayList<Object>();
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
		public Soap getSoap() {
			return soap;
		}

		/**
		 * 
		 * @param soap
		 *            The soap
		 */
		public void setSoap(Soap soap) {
			this.soap = soap;
		}

		/**
		 * 
		 * @return The documents
		 */
		public List<Object> getDocuments() {
			return documents;
		}

		/**
		 * 
		 * @param documents
		 *            The documents
		 */
		public void setDocuments(List<Object> documents) {
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

	}

	public class Bp {

		@Expose
		private Low low = new Low();
		@Expose
		private High high = new High();

		/**
		 * 
		 * @return The low
		 */
		public Low getLow() {
			if (low == null)
				low = new Low();
			return low;
		}

		/**
		 * 
		 * @param low
		 *            The low
		 */
		public void setLow(Low low) {
			this.low = low;
		}

		/**
		 * 
		 * @return The high
		 */
		public High getHigh() {
			if (high == null)
				high = new High();
			return high;
		}

		/**
		 * 
		 * @param high
		 *            The high
		 */
		public void setHigh(High high) {
			this.high = high;
		}

	}

}