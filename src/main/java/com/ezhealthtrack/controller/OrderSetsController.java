package com.ezhealthtrack.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ezhealthrack.api.APIs;
import com.ezhealthtrack.controller.EzNetwork.ResponseHandler;
import com.ezhealthtrack.model.OrderSets;
import com.ezhealthtrack.model.PrescriptionModel;
import com.ezhealthtrack.physiciansoap.model.OrderSetItems;
import com.ezhealthtrack.util.Log;
import com.google.gson.Gson;

public class OrderSetsController extends EzController {

	public final String TAG = getClass().getSimpleName();

	final static public String ORDER_SET_PRESCRIPTION = "prescription";
	final static public String ORDER_SET_RADIOLOGY = "radiology";
	final static public String ORDER_SET_LAB = "lab_tests";

	static private List<OrderSets> mPrescriptionOrderSets;
	static private List<OrderSets> mRadiologyOrderSets;
	static private List<OrderSets> mLabOrderSets;

	public OrderSetsController() {
		super(0);
		if (mPrescriptionOrderSets == null) {
			mPrescriptionOrderSets = new ArrayList<OrderSets>();
			mRadiologyOrderSets = new ArrayList<OrderSets>();
			mLabOrderSets = new ArrayList<OrderSets>();
		}
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
		return;
	}

	// get latest records (from server)
	public void getOrderSets(final Map<String, String> params,
			final UpdateListner listner) {

		String url = APIs.ORDERSETS();
		mNetwork.POST(url, params, new ResponseHandler() {

			@Override
			public void cmdResponseError(Integer code) {
				listner.onDataUpdateError(0);
			}

			@Override
			public void cmdResponse(JSONObject response, String result) {

				Log.i("LabOrderController", "OSC : " + response);

				List<OrderSets> sets = null;
				if (params.get("type").equals(ORDER_SET_PRESCRIPTION))
					sets = mPrescriptionOrderSets;
				else if (params.get("type").equals(ORDER_SET_RADIOLOGY)) {
					sets = mRadiologyOrderSets;
				} else if (params.get("type").equals(ORDER_SET_LAB)) {
					sets = mLabOrderSets;
				}

				try {
					JSONArray data = response.getJSONArray("data");
					sets.clear();
					for (int i = 0; i < data.length(); ++i) {
						JSONObject item = data.getJSONObject(i);

						OrderSets order = new Gson().fromJson(item.toString(),
								OrderSets.class);
						sets.add(order);
					}
					listner.onDataUpdate(0, sets);
					listner.onCmdResponse(response, result);
				} catch (JSONException e) {
					Log.i("LOC", "Error : " + e.getMessage());
				}

				// List<OrderSets> sets = mPrescriptionOrderSets;
				Log.i("OSC-CmdResponse()", "Sets=" + sets.size());
			}
		});
	}

	// get list of names
	public List<String> getOrderSetNames(String type) {
		List<OrderSets> sets = null;
		if (type.equals(ORDER_SET_PRESCRIPTION))
			sets = mPrescriptionOrderSets;
		else if (type.equals(ORDER_SET_RADIOLOGY)) {
			sets = mRadiologyOrderSets;
		} else if (type.equals(ORDER_SET_LAB)) {
			sets = mLabOrderSets;
		}

		List<String> names = new ArrayList<String>();
		for (int i = 0; i < sets.size(); ++i) {
			names.add(sets.get(i).getName());
		}
		return names;
	}

	// NOT TESTED: get data related to a name
	// public PrescriptionModel getPrescriptionModel(String name) {
	// List<OrderSets> sets = mPrescriptionOrderSets;
	//
	// try {
	// for (int i = 0; i < sets.size(); ++i) {
	// if (sets.get(i).getName().equals(name)) {
	// String pm = sets.get(i).getData();
	// JSONObject json = new JSONObject(pm);
	// PrescriptionModel model = new PrescriptionModel();
	// model.JsonParse(json);
	// return model;
	// }
	// }
	// } catch (JSONException e) {
	// Log.e("OSC", "Error: " + e.getMessage());
	// }
	// return null;
	// }

	// get data related to a name
	public PrescriptionModel getPrescriptionModel(int index) {
		List<OrderSets> sets = mPrescriptionOrderSets;
		Log.i("OSC-getPrescriptionModel()", "Sets=" + sets.size());
		Log.i("OSC-getPrescriptionModel()", "mPrescriptionOrderSets="
				+ mPrescriptionOrderSets.size());
		if (sets.size() <= index) {
			return null;
		}
		try {
			Object pm = sets.get(index).getData();
			JSONObject json = new JSONObject((Map<?, ?>) pm);
			Log.i("OSC-getPrescriptionModel()", json.toString());

			JSONObject drugObject = new JSONObject();

			JSONArray drug = json.getJSONArray("drug");
			for (int i = 0; i < drug.length(); ++i) {
				JSONObject item = drug.getJSONObject(i);
				drugObject.put("" + i, item);
			}

			json.put("drug", drugObject);
			Log.i("OSC-getPrescriptionModel()-NEW", json.toString());

			PrescriptionModel model = new PrescriptionModel();
			model.JsonParse(json);

			android.util.Log.i("return", "returning model");
			return model;
		} catch (JSONException e) {
			Log.e("OSC", "Error: " + e.getMessage());
		}

		return null;
	}

	// get data related to a name
	public OrderSetItems getLabOrderSetItems(int index) {
		List<OrderSets> sets = mLabOrderSets;
		Log.i("OSC-getLabModel()", "Sets=" + sets.size());
		if (sets.size() <= index) {
			return null;
		}
		try {
			Object data = sets.get(index).getData();
			JSONObject dataJson = new JSONObject((Map<?, ?>) data);
			OrderSetItems model = new OrderSetItems();
			model.JsonParse(dataJson.getJSONObject("lab"));
			return model;
		} catch (JSONException e) {
			Log.e("OSC", "Error: " + e.getMessage());
		}

		return null;
	}

	public OrderSetItems getRadiologyOrderSetItems(int index) {
		List<OrderSets> sets = mRadiologyOrderSets;
		Log.i("OSC:getRadiologyModel()", "Count=" + mRadiologyOrderSets.size());
		if (sets.size() <= index) {
			return null;
		}
		try {
			Object data = sets.get(index).getData();
			JSONObject dataJson = new JSONObject((Map<?, ?>) data);
			OrderSetItems model = new OrderSetItems();
			model.JsonParse(dataJson.getJSONObject("radiology"));
			return model;
		} catch (JSONException e) {
			Log.e("OSC", "Error: " + e.getMessage());
		}
		return null;
	}
}
