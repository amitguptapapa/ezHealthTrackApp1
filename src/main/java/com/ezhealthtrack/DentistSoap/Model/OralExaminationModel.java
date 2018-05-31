package com.ezhealthtrack.DentistSoap.Model;

import org.json.JSONException;
import org.json.JSONObject;

public class OralExaminationModel {
	public SoftTissueExaminationModel softTissue = new SoftTissueExaminationModel();
	public HardTissueExaminationModel hardTissue = new HardTissueExaminationModel();

	public void JsonParse(final JSONObject oe) {
		try {
			if(oe.has("ste"))
			softTissue.JsonParse(oe.getJSONObject("ste"));
			if(oe.has("hte"))
			hardTissue.jsonParseHte(oe.getJSONObject("hte"));
		} catch (final JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void updateJson(final JSONObject obje) {
		try {
			hardTissue.updateJsonHte(obje.getJSONObject("oe"));
			softTissue.updateJson(obje.getJSONObject("oe"));
		} catch (final JSONException e) {
			try {
				obje.put("oe", new JSONObject());
				hardTissue.updateJsonHte(obje.getJSONObject("oe"));
				softTissue.updateJson(obje.getJSONObject("oe"));
			} catch (final JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}
	}
}
