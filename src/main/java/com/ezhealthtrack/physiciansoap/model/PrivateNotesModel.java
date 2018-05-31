package com.ezhealthtrack.physiciansoap.model;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.ezhealthtrack.util.Log;
import com.google.gson.Gson;

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
			Log.i("", ""+new Gson().toJson(pnote));
		} catch (final JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void jsonUpdate(final JSONObject pnote) {
		try {
			pnote.put("value", hashNote.get("private-note"));
			pnote.put("status", hashNote.get("status"));
			pnote.put("SType", hashNote.get("SType"));
		} catch (final JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
