package com.ezhealthtrack;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

// Must set 
// OyeSharedData.USER_DATA_SGID and OyeSharedData.USER_DATA_STATUS_ID

public class EzFragment extends Fragment {

	protected Activity mActivity;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mActivity = activity;
		Log.v("EzFragment", "onAttach()");
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mActivity = null;
		Log.v("EzFragment", "onDetach()");
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Retain this fragment across configuration changes.
		setRetainInstance(true);
		Log.v("EzFragment", "onCreate:setRetainInstance()");
	}

}
