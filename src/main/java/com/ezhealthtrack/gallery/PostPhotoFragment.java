package com.ezhealthtrack.gallery;

import org.json.JSONObject;

import android.support.v4.app.Fragment;
import android.util.Log;

import com.ezhealthtrack.controller.EzNetwork.ResponseHandler;

/*
 * IMPORTANT - ShareDataActivity.java provides similar functionality
 * Any change in this file may require update in that file.
 */

public class PostPhotoFragment extends Fragment implements ResponseHandler {



	@Override
	public void cmdResponse(JSONObject response, String result) {
		if (!result.equals("1")) {
			return;
		}
		// Toast.makeText(this, "Status posted", Toast.LENGTH_LONG).show();
		getActivity().finish();
	}

	@Override
	public void cmdResponseError(Integer code) {
		// super.cmdResponseError(code);
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onPause() {
		super.onPause(); // Always call the superclass method first
		Log.v("PSA: ", "onPause");
	}

	@Override
	public void onStop() {
		super.onStop(); // Always call the superclass method first
		Log.v("PSA: ", "onStop");
	}

	@Override
	public void onDestroy() {
		Log.v("PSA: ", "onDestroy");
		super.onDestroy();
	}

}
