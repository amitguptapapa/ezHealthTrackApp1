package com.ezhealthtrack.labs.controller;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.ezhealthrack.api.LabApis;
import com.ezhealthtrack.EzApp;
import com.ezhealthtrack.activity.DashboardActivity;
import com.ezhealthtrack.activity.LoginActivity;
import com.ezhealthtrack.fragments.InboxFragment;
import com.ezhealthtrack.greendao.LabTechnician;
import com.ezhealthtrack.greendao.LabTechnicianDao.Properties;
import com.ezhealthtrack.labs.fragments.DashboardFragment;
import com.ezhealthtrack.labs.model.LabsDashboardModel;
import com.ezhealthtrack.util.Log;
import com.ezhealthtrack.util.Util;
import com.ezhealthtrack.util.NetworkCallController.OnResponse;
import com.google.gson.Gson;

public class LabsController {

	public static void getTechnicianList(Context context) {
		String url = LabApis.TECHNICIANLIST;
		final HashMap<String, String> params = new HashMap<String, String>();
		params.put("cli", "api");
		EzApp.networkController.networkCall(context, url, params,
				new OnResponse() {

					@Override
					public void onResponseListner(String response) {
						try {
							JSONArray jobj = new JSONObject(response)
									.getJSONArray("technician");
							for (int i = 0; i < jobj.length(); i++) {
								LabTechnician tech = new Gson().fromJson(
										jobj.getString(i), LabTechnician.class);
								EzApp.labTechnicianDao
										.insertOrReplace(tech);
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				});
	}

	public static LabTechnician getLabTechnician(String id) {
		if (!Util.isEmptyString(id)
				&& EzApp.labTechnicianDao.queryBuilder()
						.where(Properties.Id.eq(id)).count() > 0)
			return EzApp.labTechnicianDao.queryBuilder()
					.where(Properties.Id.eq(id)).list().get(0);
		else {
			LabTechnician lTech = new LabTechnician();
			lTech.setName("");
			return lTech;
		}

	}

	public static void getDashboard(Context context,
			final DashboardFragment fragment, final OnResponse responsee) {
		String url = LabApis.DASHBOARD;
		final HashMap<String, String> params = new HashMap<String, String>();
		params.put("format", "json");
		params.put("cli", "api");
		EzApp.networkController.networkCall(context, url, params,
				new OnResponse() {

					@Override
					public void onResponseListner(String response) {
						try {
							final JSONObject jObj = new JSONObject(response);
							InboxFragment.unreadCount = Integer.parseInt(jObj
									.getString("inbox_unread"));
							final JSONObject data = jObj.getJSONObject("data");
							fragment.model = new Gson().fromJson(
									data.toString(), LabsDashboardModel.class);
							if (fragment.isVisible())
								fragment.updateData();
							responsee.onResponseListner(response);

						} catch (final JSONException e) {
							if (fragment.isVisible())
								fragment.updateData();
							Log.e("", e);
							responsee.onResponseListner("error");
						}

					}
				});
	}

}
