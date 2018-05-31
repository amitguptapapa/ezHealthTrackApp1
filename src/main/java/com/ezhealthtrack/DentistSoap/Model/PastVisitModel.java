package com.ezhealthtrack.DentistSoap.Model;

import org.json.JSONException;
import org.json.JSONObject;

public class PastVisitModel {
	public String bkId = "";
	public String siId = "";
	public String slotId = "";
	public String aptDate = "";
	public String time = "";
	public String reason = "";

	public PastVisitModel(final JSONObject json) {
		try {
			if (json.has("bk-id"))
				bkId = json.getString("bk-id");
			if (json.has("si-id"))
				siId = json.getString("si-id");
			if (json.has("slot-id"))
				slotId = json.getString("slot-id");
			if (json.has("apt-date"))
				aptDate = json.getString("apt-date");
			if (json.has("time"))
				time = json.getString("time");
			if (json.has("reason"))
				reason = json.getString("reason");
		} catch (final JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
