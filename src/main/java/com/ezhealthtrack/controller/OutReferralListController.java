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
import com.ezhealthtrack.model.OutReferralModel;
import com.ezhealthtrack.util.Log;
import com.google.gson.Gson;

public class OutReferralListController extends EzController {

	public final String TAG = getClass().getSimpleName();

	public OutReferralListController(int page) {
		super(page);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void getPage(final int page, Map<String, String> params,
			final UpdateListner listner) {
		if (params == null)
			params = new HashMap<String, String>();

		final String url = APIs.OUT_REFFERRAL_LIST();
		params.put("page_num", Integer.toString(page));
		params.put("condval", "");

		mNetwork.POST(url, params, new ResponseHandler() {

			@Override
			public void cmdResponseError(Integer code) {
				listner.onDataUpdateError(page);

			}

			@Override
			public void cmdResponse(JSONObject response, String result) {
				Log.i("OutReferralListConroller", "ORLC : " + response);
				ArrayList<OutReferralModel> outReferral = new ArrayList<OutReferralModel>();

				try {
					// final JSONObject jObj = new JSONObject("" + response);
					// final JSONArray data = jObj.getJSONArray("data");
					// if (page == 1) {
					// outReferral.clear();
					// }
					// for (int i = 0; i < data.length(); i++) {
					// JSONObject item = data.getJSONObject(i);
					// OutReferralModel model = new Gson().fromJson(
					// item.toString(), OutReferralModel.class);
					// outReferral.add(model);
					// }
					// if ((page * 6) < jObj.getInt("totalItemCount")) {
					// Map<String, String> params = new HashMap<String,
					// String>();
					// getPage(page + 1, params, listner);
					// }

					JSONArray data = response.getJSONArray("data");
					for (int i = 0; i < data.length(); ++i) {
						JSONObject item = data.getJSONObject(i);
						OutReferralModel model = new Gson().fromJson(
								item.toString(), OutReferralModel.class);
						outReferral.add(model);
					}

					listner.onDataUpdate(page, outReferral);
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
