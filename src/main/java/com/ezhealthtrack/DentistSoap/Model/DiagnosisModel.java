package com.ezhealthtrack.DentistSoap.Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ezhealthtrack.util.Util;

import android.util.Log;
import android.widget.MultiAutoCompleteTextView;

public class DiagnosisModel {
	public String pd = "";
	public String fd = "";

	public void JsonParse(final JSONObject asse) {
		pd = "";
		fd = "";
		try {
			if (asse.has("pd")
					&& asse.getJSONObject("pd").get("value") instanceof JSONArray) {
				JSONArray arrPd = asse.getJSONObject("pd")
						.getJSONArray("value");
				for (int i = 0; i < arrPd.length(); i++) {
					if (Util.isEmptyString(arrPd.getString(i))) {
						pd = pd + arrPd.getString(i);
					} else {
						pd = pd + arrPd.getString(i) + ",";
					}
				}
			}
			if (asse.has("fd")
					&& asse.getJSONObject("fd").get("value") instanceof JSONArray) {
				JSONArray arrFd = asse.getJSONObject("fd")
						.getJSONArray("value");
				for (int i = 0; i < arrFd.length(); i++) {
					if (Util.isEmptyString(arrFd.getString(i))) {
						fd = fd + arrFd.getString(i);
					} else {
						fd = fd + arrFd.getString(i) + ",";
					}
				}
			}
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	public void JsonParse(final JSONObject asse,
			final MultiAutoCompleteTextView actvFinal,
			final MultiAutoCompleteTextView actvProv) {
		pd = "";
		fd = "";
		Log.i("asse", asse.toString());

		try {
			if (asse.has("pd")
					&& asse.getJSONObject("pd").get("value") instanceof JSONArray) {
				JSONArray arrPd = asse.getJSONObject("pd")
						.getJSONArray("value");
				for (int i = 0; i < arrPd.length(); i++) {
					pd = pd + arrPd.getString(i) + ",";
				}
			}
			if (asse.has("fd")
					&& asse.getJSONObject("fd").get("value") instanceof JSONArray) {
				JSONArray arrFd = asse.getJSONObject("fd")
						.getJSONArray("value");
				for (int i = 0; i < arrFd.length(); i++) {
					fd = fd + arrFd.getString(i) + ",";
				}
			}
			actvFinal.setText(fd);
			actvProv.setText(pd);
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	public void updateJson(final JSONObject soap) {
		try {
			if (!soap.has("asse"))
				soap.put("asse", new JSONObject());
			final JSONObject asse = soap.getJSONObject("asse");
			final JSONArray arrPd = new JSONArray();
			final String[] partsPd = pd.split(",");
			for (int i = 0; i < partsPd.length; i++) {
				arrPd.put(partsPd[i]);
			}
			JSONObject pd = new JSONObject();
			pd.put("value", arrPd);
			asse.put("pd", pd);
			final JSONArray arrFd = new JSONArray();
			final String[] partsFd = fd.split(",");
			for (int i = 0; i < partsFd.length; i++) {
				arrFd.put(partsFd[i]);
			}
			JSONObject fd = new JSONObject();
			fd.put("value", arrFd);
			asse.put("fd", fd);
		} catch (final JSONException e) {
			e.printStackTrace();
		}
	}
}
