package com.ezhealthtrack.model;

import com.ezhealthtrack.util.Util;

public class MedicineModel {
	public String name;
	public String strength;
	public String unit;
	public String formulations;
	public String route;
	public String frequency;
	public String quantity = "0";
	public String times = "0";
	public String refills = "No";
	public String refillsTime = "0";
	public String rf;
	public String notes;
	public String addedon;
	public String updatedon;

	public String getMedicineString() {
		if (Util.isEmptyString(refills) || refills.equalsIgnoreCase("No")) {
			refills = "No";
			refillsTime = "";
		}
		// if (refills != "No") {
		// if (Util.isEmptyString(notes)) {
		return formulations + " - " + name + " - " + strength + " - " + unit
				+ " - " + route + " - " + frequency + " - " + times + " - "
				+ refills + " - " + refillsTime + " - " + notes;
		// } else {
		// return formulations + " - " + name + " " + strength + " "
		// + unit + " (" + route + ") -----------/ " + frequency
		// + " (" + "# of Days: " + times + "), Refill: "
		// + refills + ", Number of Refill: " + refillsTime
		// + " (Note:" + notes + ")";
		// }
		// } else {
		// if (Util.isEmptyString(notes)) {
		// return formulations + " - " + name + " " + strength + " "
		// + unit + " (" + route + ") -----------/ " + frequency
		// + " (" + "# of Days: " + times + "), Refill: "
		// + refills;
		// } else {
		// return formulations + " - " + name + " " + strength + " "
		// + unit + " (" + route + ") -----------/ " + frequency
		// + " (" + "# of Days: " + times + "), Refill: "
		// + refills + " (Note:" + notes + ")";
		// }
		// }
	}

	public String getMedicineStringExtra() {
		if (Util.isEmptyString(refills) || refills.equalsIgnoreCase("No")) {
			refills = "No";
			refillsTime = "";
		}
		if (refills != "No") {
			if (Util.isEmptyString(notes)) {
				return formulations + " - " + name + " " + strength + " "
						+ unit + " (" + route + ") -----------/ " + frequency
						+ " (" + "# of Days: " + times + "), Refill: "
						+ refills + ", Number of Refill: " + refillsTime;
			} else {
				return formulations + " - " + name + " " + strength + " "
						+ unit + " (" + route + ") -----------/ " + frequency
						+ " (" + "# of Days: " + times + "), Refill: "
						+ refills + ", Number of Refill: " + refillsTime
						+ " (Note:" + notes + ")";
			}
		} else {
			if (Util.isEmptyString(notes)) {
				return formulations + " - " + name + " " + strength + " "
						+ unit + " (" + route + ") -----------/ " + frequency
						+ " (" + "# of Days: " + times + "), Refill: "
						+ refills;
			} else {
				return formulations + " - " + name + " " + strength + " "
						+ unit + " (" + route + ") -----------/ " + frequency
						+ " (" + "# of Days: " + times + "), Refill: "
						+ refills + " (Note:" + notes + ")";
			}
		}
	}

}
