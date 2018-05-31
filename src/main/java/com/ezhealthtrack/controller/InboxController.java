package com.ezhealthtrack.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ezhealthrack.api.APIs;
import com.ezhealthtrack.controller.EzNetwork.ResponseHandler;
import com.ezhealthtrack.greendao.MessageModel;
import com.ezhealthtrack.util.Log;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class InboxController extends EzController {

	public final String TAG = getClass().getSimpleName();

	public InboxController(int page) {
		super(page);
	}

	// get records from local database (if offline data is supported)
	public List<?> getRecords(int count, int offset) {
		return null;
	}

	// get records from local database (if offline data is supported),
	// also get latest records (from server)
	public List<?> getPage(int page, int offset, Map<String, String> params,
			UpdateListner listner) {
		return null;
	}

	// get latest records (from server)
	public void getPage(final int page, Map<String, String> params,
			final UpdateListner listner) {

		if (params == null)
			params = new HashMap<String, String>();

		String url = APIs.INBOX_MESSAGES();

		// set post parameters
		// search=Rohit+Sharma+IND-266970160&patient_id=3199
		// order_status=0
		// String condVal = "";
		params.put("page_num", Integer.toString(page));
		params.put("condval", "");
		mNetwork.POST(url, params, new ResponseHandler() {

			@Override
			public void cmdResponseError(Integer code) {
				listner.onDataUpdateError(page);
			}

			@Override
			public void cmdResponse(JSONObject response, String result) {

				Log.i("LabOrderController", "LOC : " + response);

				List<MessageModel> message = new ArrayList<MessageModel>();

				try {
					JSONArray data = response.getJSONArray("data");
					Gson gson = new GsonBuilder().setDateFormat(
							"yyyy-MM-dd hh:mm").create();
					for (int i = 0; i < data.length(); ++i) {
						JSONObject item = data.getJSONObject(i);
						MessageModel msg = gson.fromJson(item.toString(),
								MessageModel.class);
						message.add(msg);
					}
					listner.onDataUpdate(page, message);
					listner.onCmdResponse(response, result);
				} catch (JSONException e) {
					Log.i("LOC", "Error : " + e.getMessage());
				}
			}
		});
	}

	// get latest records (from server)
	public void getMessagerDetails(Map<String, String> params,
			final UpdateListner listner) {

		if (params == null)
			params = new HashMap<String, String>();

		String url = APIs.INBOX_MESSAGES();

		// set post parameters
		//params.put("condVal", "");
		mNetwork.POST(url, params, new ResponseHandler() {

			@Override
			public void cmdResponseError(Integer code) {
				listner.onDataUpdateError(0);
			}

			@Override
			public void cmdResponse(JSONObject response, String result) {

				Log.i("LabOrderController", "LOC : " + response);

				List<MessageModel> message = new ArrayList<MessageModel>();
				try {
					JSONArray data = response.getJSONArray("data");
					for (int i = 0; i < data.length(); ++i) {
						JSONObject item = data.getJSONObject(i);
						MessageModel msg = new Gson().fromJson(item.toString(),
								MessageModel.class);
						message.add(msg);
					}
					listner.onDataUpdate(0, message);
					listner.onCmdResponse(response, result);
				} catch (JSONException e) {
					Log.i("LOC", "Error : " + e.getMessage());
				}
			}
		});
	}

	// get latest records (from server)
	// public void getConnectedLabs(Map<String, String> params,
	// final UpdateListner listner) {
	//
	// String url = APIs.LABS_FOR_LAB_TESTS();
	// mNetwork.POST(url, params, new ResponseHandler() {
	//
	// @Override
	// public void cmdResponseError(Integer code) {
	// listner.onDataUpdateError(0);
	// }
	//
	// @Override
	// public void cmdResponse(JSONObject response, String result) {
	// Log.i("getConnectedLabs()", "" + response.toString());
	//
	// List<LabInfo> labs = new ArrayList<LabInfo>();
	// try {
	// JSONArray data = response.getJSONArray("data");
	// for (int i = 0; i < data.length(); ++i) {
	// JSONObject item = data.getJSONObject(i);
	// LabInfo lab = new Gson().fromJson(item.toString(),
	// LabInfo.class);
	// labs.add(lab);
	// }
	// listner.onDataUpdate(0, labs);
	// listner.onCmdResponse(response, result);
	// } catch (JSONException e) {
	// Log.i("LOC", "Error : " + e.getMessage());
	// listner.onDataUpdateError(0);
	// }
	// }
	// });
	// }
}