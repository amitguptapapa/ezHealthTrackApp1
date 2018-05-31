package com.ezhealthtrack.labs.controller;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.ezhealthrack.api.LabApis;
import com.ezhealthtrack.EzApp;
import com.ezhealthtrack.greendao.AppointmentDao.Properties;
import com.ezhealthtrack.greendao.LabWorkFlow;
import com.ezhealthtrack.order.WorkFlow;
import com.ezhealthtrack.util.NetworkCallController;
import com.ezhealthtrack.util.NetworkCallController.OnResponse;
import com.google.gson.Gson;

public class LabWorkFlowController {
	public static WorkFlow orderWorkFlow;
	public static WorkFlow reportWorkFlow;
	public static final String lab_order = "lab-order";
	public static final String lab_report = "lab-report";

	public static void getLabWorkFlows(final Context context) {
		String url = LabApis.GET_LAB_WORK_FLOW;
		final HashMap<String, String> params = new HashMap<String, String>();
		params.put("cli", "api");
		EzApp.networkController.networkCall(context, url, params,
				new OnResponse() {

					@Override
					public void onResponseListner(String response) {
						if (!response
								.equals(NetworkCallController.NOCONNECTION)) {
							try {
								JSONObject jObj = new JSONObject(response);
								if (jObj.getString("s").equals("200")) {
									JSONArray jArr = jObj.getJSONArray("data");
									for (int i = 0; i < jArr.length(); i++) {
										JSONObject jWorkflow = jArr
												.getJSONObject(i);
										LabWorkFlow workFlow = new LabWorkFlow(
												jWorkflow.getLong("id"),
												jWorkflow.getString("type"),
												jWorkflow.getString("meta"));
										Log.i("", jWorkflow.getString("meta"));
										EzApp.labWorkflowDao
												.insertOrReplace(workFlow);
										Editor editor = EzApp.sharedPref
												.edit();
										editor.putLong(workFlow.getType(),
												workFlow.getId());
										editor.commit();
										if (workFlow.getType()
												.equals(lab_order))
										getWorkFlowById(workFlow
													.getId(),context,new OnResponseWorkFlow() {
														
														@Override
														public void onResponseWorkFlow(WorkFlow workFlow) {
															orderWorkFlow = workFlow;
															
														}
													});
										else if (workFlow.getType().equals(
												lab_report))
											getWorkFlowById(workFlow
													.getId(),context,new OnResponseWorkFlow() {
														
														@Override
														public void onResponseWorkFlow(WorkFlow workFlow) {
															reportWorkFlow = workFlow;
															
														}
													});
									}
								}
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}

					}
				});
	}
	
	public interface OnResponseWorkFlow {
		public void onResponseWorkFlow(WorkFlow workFlow);
	}

	public static void getWorkFlowById(final Long id,final Context context,final OnResponseWorkFlow onresponse) {
		if (EzApp.labWorkflowDao.queryBuilder()
				.where(Properties.Id.eq(id)).list().size() > 0) {
			try {
				JSONObject jobj = new JSONObject(
						EzApp.labWorkflowDao.queryBuilder()
								.where(Properties.Id.eq(id)).list().get(0)
								.getMeta());
				JSONArray jSteps = jobj.getJSONArray("steps");
				for (int i = 0; i < jSteps.length(); i++) {
					JSONArray jTransitions = jSteps.getJSONObject(i)
							.getJSONArray("transitions");
					for (int j = 0; j < jTransitions.length(); j++) {
						if (jTransitions.getJSONObject(j).has(
								"includePermission")
								&& jTransitions.getJSONObject(j).get(
										"includePermission") instanceof JSONArray)
							jTransitions.getJSONObject(j).remove(
									"includePermission");

					}
				}

				WorkFlow workFlow = new Gson().fromJson(jobj.toString(),
						WorkFlow.class);
				Log.i("workflow", new Gson().toJson(workFlow));
				onresponse.onResponseWorkFlow(workFlow);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else{
			String url = LabApis.GET_LAB_WORK_FLOW_ID+id;
			final HashMap<String, String> params = new HashMap<String, String>();
			params.put("cli", "api");
			EzApp.networkController.networkCall(context, url, params,
					new OnResponse() {

						@Override
						public void onResponseListner(String response) {
							if (!response
									.equals(NetworkCallController.NOCONNECTION)) {
								try {
									JSONObject jObj = new JSONObject(response);
									if (jObj.getString("s").equals("200")) {
											LabWorkFlow labWorkFlow = new LabWorkFlow(
													id,
													jObj.getString("workflow_type"),
													jObj.getString("workflow_meta"));
											Log.i("", jObj.getString("workflow_meta"));
											EzApp.labWorkflowDao
													.insertOrReplace(labWorkFlow);
										getWorkFlowById(id, context, onresponse);
										
									}
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}

						}
					});
		}
	}

}
