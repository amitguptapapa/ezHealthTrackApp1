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
import com.ezhealthtrack.model.LabOrder;
import com.ezhealthtrack.model.laborder.LabInfo;
import com.ezhealthtrack.util.Log;
import com.google.gson.Gson;

public class LabOrderController extends EzController {

	public final String TAG = getClass().getSimpleName();

	public LabOrderController(int page) {
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

		String url = APIs.LAB_ORDERS_LIST();

		// set post parameters
		// search=Rohit+Sharma+IND-266970160&patient_id=3199
		// order_status=0
		params.put("page", Integer.toString(page));

		mNetwork.POST(url, params, new ResponseHandler() {

			@Override
			public void cmdResponseError(Integer code) {
				listner.onDataUpdateError(page);
			}

			@Override
			public void cmdResponse(JSONObject response, String result) {

				Log.i("LabOrderController", "LOC : " + response);

				List<LabOrder> orders = new ArrayList<LabOrder>();
				try {
					JSONArray data = response.getJSONArray("data");
					for (int i = 0; i < data.length(); i++) {
						JSONObject item = data.getJSONObject(i);
						LabOrder order = new Gson().fromJson(item.toString(),
								LabOrder.class);
						orders.add(order);
					}
					listner.onDataUpdate(page, orders);
					listner.onCmdResponse(response, result);
				} catch (JSONException e) {
					Log.i("LOC", "Error : " + e.getMessage());
				}
			}
		});
	}

	// get latest records (from server)
	public void getOrderDetails(Map<String, String> params,
			final UpdateListner listner) {

		if (params == null)
			params = new HashMap<String, String>();

		String url = APIs.LAB_ORDERS_LIST();

		// set post parameters

		mNetwork.POST(url, params, new ResponseHandler() {

			@Override
			public void cmdResponseError(Integer code) {
				listner.onDataUpdateError(0);
			}

			@Override
			public void cmdResponse(JSONObject response, String result) {

				Log.i("LabOrderController", "LOC : " + response);

				List<LabOrder> orders = new ArrayList<LabOrder>();
				try {
					JSONArray data = response.getJSONArray("data");
					for (int i = 0; i < data.length(); ++i) {
						JSONObject item = data.getJSONObject(i);
						LabOrder order = new Gson().fromJson(item.toString(),
								LabOrder.class);
						orders.add(order);
					}
					listner.onDataUpdate(0, orders);
					listner.onCmdResponse(response, result);
				} catch (JSONException e) {
					Log.i("LOC", "Error : " + e.getMessage());
				}
			}
		});
	}

	// get latest records (from server)
	public void getConnectedLabs(Map<String, String> params,
			final UpdateListner listner) {

		String url = APIs.LABS_FOR_LAB_TESTS();
		mNetwork.POST(url, params, new ResponseHandler() {

			@Override
			public void cmdResponseError(Integer code) {
				listner.onDataUpdateError(0);
			}

			@Override
			public void cmdResponse(JSONObject response, String result) {
				Log.i("getConnectedLabs()", "" + response.toString());

				List<LabInfo> labs = new ArrayList<LabInfo>();
				try {
					JSONArray data = response.getJSONArray("data");
					for (int i = 0; i < data.length(); ++i) {
						JSONObject item = data.getJSONObject(i);
						LabInfo lab = new Gson().fromJson(item.toString(),
								LabInfo.class);
						labs.add(lab);
					}
					listner.onDataUpdate(0, labs);
					listner.onCmdResponse(response, result);
				} catch (JSONException e) {
					Log.i("LOC", "Error : " + e.getMessage());
					listner.onDataUpdateError(0);
				}
			}
		});
	}
}