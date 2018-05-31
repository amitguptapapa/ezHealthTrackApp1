package com.ezhealthtrack;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

// Must set 
// OyeSharedData.USER_DATA_SGID and OyeSharedData.USER_DATA_STATUS_ID

public class EzLaunchFragment extends Fragment {

	private String mStatusId;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.v("SDsF", "onCreate");

		this.setHasOptionsMenu(true);
		Log.v("SDA:onCreate()", "mStatusId = " + mStatusId);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.v("SDsF: ", "onCreateView");

		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.activity_ez_launch, container,
				false);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// mOptionsMenu = menu;
		menu.clear();
		inflater.inflate(R.menu.ez_launch, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onPause() {
		super.onPause(); // Always call the superclass method first
		Log.v("SDF: ", "onPause");
	}

	@Override
	public void onStop() {
		super.onStop(); // Always call the superclass method first
		Log.v("SDF: ", "onStop");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

}
