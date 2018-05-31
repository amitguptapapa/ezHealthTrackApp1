package com.ezhealthtrack.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ezhealthrack.api.APIs;
import com.ezhealthtrack.EzApp;
import com.ezhealthtrack.activity.DashboardActivity;
import com.ezhealthtrack.controller.EzNetwork.ResponseHandler;
import com.ezhealthtrack.greendao.Appointment;
import com.ezhealthtrack.model.PatientAutoSuggest;
import com.ezhealthtrack.model.PatientShow;
import com.ezhealthtrack.one.EzUtils;
import com.ezhealthtrack.util.Log;
import com.google.gson.Gson;

public class HistoryListController extends EzController {
	PatientAutoSuggest pat;

	public HistoryListController(int page) {
		super(page);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void getPage(final int page, Map<String, String> params,
			final UpdateListner listner) {
		if (params == null)
			params = new HashMap<String, String>();

		Date fdate = new Date();
		Date tdate = new Date();
		String url = APIs.HISTORY_LIST();
		params.put("page_num", Integer.toString(page));
		params.put("condval", "");
		if (pat != null)
			params.put("sel_pat", pat.getId());
		params.put("fdate", EzApp.sdfddMmyy1.format(fdate));
		params.put("tdate", EzApp.sdfddMmyy1.format(tdate));

		mNetwork.POST(url, params, new ResponseHandler() {

			@Override
			public void cmdResponseError(Integer code) {
				listner.onDataUpdateError(page);

			}

			@Override
			public void cmdResponse(JSONObject response, String result) {
				Log.i("PatientsController", "PC : " + response);
				try {
					final JSONArray patientList = response.getJSONArray("data");
					final JSONArray patientList1 = response
							.getJSONArray("patients");
					for (int i = 0; i < patientList1.length(); i++) {
						PatientShow pat = new Gson().fromJson(
								patientList1.get(i).toString(),
								PatientShow.class);
						Iterator<PatientShow> iter = DashboardActivity.arrPatientShow
								.iterator();
						while (iter.hasNext()) {
							PatientShow pats = iter.next();
							if (pats.getPfId().equals(pat.getPfId())
									&& pats.getPId().equals(pat.getPId()))
								iter.remove();
						}
						DashboardActivity.arrPatientShow.add(pat);
					}
					if (page == 1) {
						DashboardActivity.arrHistoryPatients.clear();
					}
					for (int i = 0; i < patientList.length(); i++) {
						final JSONObject patientData = patientList
								.getJSONObject(i);
						Appointment appointment = new Appointment();
						DashboardActivity.arrHistoryPatients.add(appointment);
						appointment.setBkid(patientData.getString("bk-id"));
						appointment.setPid(patientData.getString("p-id"));
						if (patientData.has("pf-id"))
							appointment.setPfId(patientData.getString("pf-id"));
						else
							appointment.setPfId("0");

						// appointment.setPfn(patientData
						// .getString("pfn"));
						// appointment.setPln(patientData
						// .getString("pln"));
						appointment.setReason(patientData.getString("reason"));
						appointment.setWid(patientData.getString("w-id"));
						appointment.setWiid(patientData.getString("wi-id"));
						appointment.setWistep(patientData.getString("wi-step"));
						appointment.setFlag(patientData.getString("flag"));
						appointment.setVisit(patientData.getString("visit"));
						if (patientData.has("ep-id")) {
							appointment.setEpid(patientData.getString("ep-id"));
						}
						if (patientData.has("si-id")) {
							appointment.setSiid(patientData.getString("si-id"));
						}
						if (patientData.has("mp-id")) {
							appointment.setMpid(patientData.getString("mp-id"));
						}
						if (patientData.has("follow-id")) {
							appointment.setFollowid(patientData
									.getString("follow-id"));
						}
						// TODO: remove it
						if (!patientData.has("apt_start_time")) {
							patientData.put("apt_start_time", 600);
						}
						appointment.aptDate = EzUtils.getDisplayDateTime(
								patientData.getString("apt-date"),
								patientData.getInt("apt_start_time"));
					}
					// listner.onDataUpdate(page, apt);
					listner.onCmdResponse(response, result);
				} catch (JSONException e) {
					Log.i("LOC", "Error : " + e.getMessage());
				}

			}
		});

	}

	@Override
	public List<?> getRecords(int count, int offset) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<?> getPage(int page, int offset, Map<String, String> params,
			UpdateListner listner) {
		// TODO Auto-generated method stub
		return null;
	}

}
