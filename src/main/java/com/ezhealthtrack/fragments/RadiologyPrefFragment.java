package com.ezhealthtrack.fragments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import com.ezhealthrack.api.APIs;
import com.ezhealthtrack.R;
import com.ezhealthtrack.activity.DashboardActivity;
import com.ezhealthtrack.adapter.RadiologyPrefAdapter;
import com.ezhealthtrack.controller.EzNetwork;
import com.ezhealthtrack.controller.EzNetwork.ResponseHandler;
import com.ezhealthtrack.model.RadiologyModel;
import com.ezhealthtrack.one.EzUtils;
import com.google.gson.Gson;

public class RadiologyPrefFragment extends Fragment implements OnClickListener {

	private ListView mListView;
	private RadiologyPrefAdapter mListAdapter;
	private List<RadiologyModel> mDataList;

	private EditText editName;

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(final LayoutInflater inflater,
			final ViewGroup container, final Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_radiology_pref,
				container, false);

		mDataList = new ArrayList<RadiologyModel>();
		mListAdapter = new RadiologyPrefAdapter(getActivity(),
				R.layout.row_checked_item, mDataList);
		mListView = (ListView) view.findViewById(R.id.list_radiology);
		mListView.setAdapter(mListAdapter);

		editName = (EditText) view.findViewById(R.id.edit_name);
		editName.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(final Editable arg0) {
			}

			@Override
			public void beforeTextChanged(final CharSequence arg0,
					final int arg1, final int arg2, final int arg3) {
			}

			@Override
			public void onTextChanged(final CharSequence cs, final int arg1,
					final int arg2, final int arg3) {
				// When user changed the Text
				mListAdapter.getFilter().filter(cs);
				// Util.filterClear(editName,
				// getActivity().findViewById(R.id.img_clear_filter));
			}
		});

		view.findViewById(R.id.button_submit_radiology).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub

						((DashboardActivity) getActivity()).postRadiologyPref();
					}
				});

		return view;
	}

	@Override
	public void onActivityCreated(final Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

	}

	public void getData() {

		EzNetwork network = new EzNetwork();
		String url = APIs.LAB_RADIOLOGY_PREF();
		Map<String, String> params = new HashMap<String, String>();
		params.put("type", "radiology");

		mDataList.clear();
		network.POST(url, params, new ResponseHandler() {

			@Override
			public void cmdResponseError(Integer code) {
				// TODO Auto-generated method stub

			}

			@Override
			public void cmdResponse(JSONObject response, String result) {
				// TODO Auto-generated method stub
				try {
					if (!response.getString("s").equals("200")) {
						EzUtils.showLong("Please retry..");
						return;
					}

					final JSONArray list = response.getJSONArray("list");
					Log.i("RP:cmdResponse()", "List Size =" + list.length());
					for (int i = 0; i < list.length(); i++) {
						RadiologyModel model = new Gson().fromJson(list
								.getJSONObject(i).toString(),
								RadiologyModel.class);
						mDataList.add(model);
					}
					final JSONArray checked = response.getJSONArray("checked");
					for (int i = 0; i < checked.length(); i++) {
						String name = checked.getString(i);
						Log.i("RP:cmdResponse()", "Checked Item =" + name);
						for (RadiologyModel model : mDataList) {
							if (model.getTestName().equals(name)) {
								model.setIsChecked(true);
							}
						}
					}
					refreshSreen();
				} catch (final JSONException e) {
					EzUtils.showLong("Please retry..");
					Log.e("RP:cmdResponse()", "Parse error..");
				}
			}
		});

	}

	void refreshSreen() {
		EzUtils.showLong("RP:refreshSreen(), Size =" + mDataList.size());
		Log.i("RP:refreshSreen()", "Size =" + mDataList.size());
		mListAdapter.notifyDataSetChanged();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onResume() {
		// adapter.notifyDataSetChanged();
		super.onResume();
	}

	@Override
	public void onSaveInstanceState(final Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onStart() {
		super.onStart();
		getData();
	}

}
