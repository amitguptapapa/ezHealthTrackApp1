package com.ezhealthtrack.DentistSoap.Model;

import java.util.ArrayList;
import java.util.HashMap;

import com.ezhealthtrack.util.Util;

//import android.util.Log;

public class TeethModel {
	public String name = "";
	public HashMap<String, String> hashStatus = new HashMap<String, String>();
	public ArrayList<String> arrTeethState = new ArrayList<String>();
	public ArrayList<String> arrTeethTreatmentPlan = new ArrayList<String>();
	public String scaling1 = "";
	public String scaling2 = "";
	public String crown = "";
	public String remLesion = "";
	public String restOther = "";
	public String periOther = "";
	public String other;
	public int scaling1Tag;
	public int scaling2Tag;
	public int crownTag;
	public int remLesionTag;
	public HashMap<String, Task> hashTask = new HashMap<String, Task>();
	public HashMap<String, String> hashState = new HashMap<String, String>();
	public HashMap<String, Object> hashTreatmentPlan = new HashMap<String, Object>();
	private final String[] arrPeriodontal = new String[] { "gingivoplasty",
			"osteoplasty", "gingivectomy", "ostectomy",
			"apically displaced flap", "modified widman flap",
			"undisplaced flap", "palatal flap", "double papilla flap",
			"papilla preservation flap", "distal molar surgery" };

	public String getTreatmentPlanString() {
		String s = "\n        " + name;
		// Log.i("str = ", "" + arrTeethTreatmentPlan.toString());
		for (final String str : arrTeethTreatmentPlan) {
			if (str.contains("scaling")) {
				s = s + "\n           " + "- " +str + " (" + scaling1 + ","
						+ scaling2 + ") ("+hashStatus.get(str)+")";
			} else if (str.contains("crown")) {
				s = s + "\n           " + "- " +str + " (" + crown + ") ("+hashStatus.get(str)+")";
			} else if (str
					.contains("oral and maxillofacial surgery")) {
				s = s + "\n           " + "- " +str + " [ removal of lesion ("
						+ remLesion + ") ] ("+hashStatus.get(str)+")";
			} else if (str.contains("gingivoplasty")) {
				s = s + "\n           " + "- " +"periodontal surgery - " + str+" ("+hashStatus.get(str)+")";
			} else if (str.contains("gingivectomy")) {
				s = s + "\n           " + "- " +"peridoontal surgery - " + str+" ("+hashStatus.get(str)+")";
			} else if (str.contains("osteoplasty")) {
				s = s + "\n           " + "- " +"periodontal surgery - " + str+" ("+hashStatus.get(str)+")";
			} else if (str.contains("ostectomy")) {
				s = s + "\n           " + "- " +"periodontal surgery - " + str+" ("+hashStatus.get(str)+")";
			} else if (str.contains("modified widman flap")) {
				s = s + "\n           " + "- " +"periodontal surgery - " + str+" ("+hashStatus.get(str)+")";
			} else if (str
					.contains("apically displaced flap")) {
				s = s + "\n           " + "- " +"periodontal surgery - " + str+" ("+hashStatus.get(str)+")";
			} else if (str.contains("palatal flap")) {
				s = s + "\n           " + "- " +"periodontal surgery - " + str+" ("+hashStatus.get(str)+")";
			} else if (str.contains("undisplaced flap")) {
				s = s + "\n           " + "- " +"periodontal surgery - " + str+" ("+hashStatus.get(str)+")";
			} else if (str
					.contains("papilla preservation flap")) {
				s = s + "\n           " + "- " +"periodontal surgery - " + str+" ("+hashStatus.get(str)+")";
			} else if (str.contains("double papilla flap")) {
				s = s + "\n           " + "- " +"periodontal surgery - " + str+" ("+hashStatus.get(str)+")";
			} else if (str.contains("distal molar surgery")) {
				s = s + "\n           " + "- " +"periodontal surgery - " + str+" ("+hashStatus.get(str)+")";
			} else if (str.contains("glass ionomer cement")) {
				s = s + "\n           " + "- " +"restoration - " + str+" ("+hashStatus.get(str)+")";
			} else if (str.contains("composite")) {
				s = s + "\n           " + "- " +"restoration - " + str+" ("+hashStatus.get(str)+")";
			} else if (str.contains("periodontal surgery")) {
				if(!Util.isEmptyString(periOther))
				s = s + "\n           " + "- " +"periodontal surgery - "
						+ periOther+" ("+hashStatus.get(str)+")";
			} else if (str.contains("restoration")) {
				if(!Util.isEmptyString(restOther))
				s = s + "\n           " + "- " +"restoration - " + restOther+" ("+hashStatus.get(str)+")";
			} else {
				s = s + "\n           " + "- " +str+" ("+hashStatus.get(str)+")";
			}
		}
		if(!Util.isEmptyString(other)){
			if(Util.isEmptyString(hashStatus.get(other)))
				hashStatus.put(other, "pending");
			s = s + "\n           " + "- " + other +" ("+hashStatus.get(other)+")";
		}
		s= s.replaceAll("null", "pending");
		return s;
	}
}
