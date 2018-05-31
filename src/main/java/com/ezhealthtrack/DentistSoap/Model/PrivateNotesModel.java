package com.ezhealthtrack.DentistSoap.Model;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

public class PrivateNotesModel {
	public HashMap<String, String> hashNote = new HashMap<String, String>();

	public void jsonParse(final JSONObject pnote) {
		try {
			if(pnote.has("value"))
			hashNote.put("private-note", pnote.getString("value"));
			if(pnote.has("status"))
			hashNote.put("status", pnote.getString("status"));
			if(pnote.has("SType"))
			hashNote.put("SType", pnote.getString("SType"));
		} catch (final JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void jsonUpdate(final JSONObject soap) {
		try {
			JSONObject pnote = new JSONObject();
			pnote.put("value", hashNote.get("private-note"));
			pnote.put("status", hashNote.get("status"));
			pnote.put("SType", hashNote.get("SType"));
			soap.put("private-note",pnote);
		} catch (final JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
