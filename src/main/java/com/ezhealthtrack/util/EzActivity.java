package com.ezhealthtrack.util;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.flurry.android.FlurryAgent;

public class EzActivity extends AppCompatActivity {

	ActionBar mActionBar;
	protected int mScreenRotation;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mActionBar = getSupportActionBar();
		mScreenRotation = ActivityInfo.SCREEN_ORIENTATION_NOSENSOR;
	}

	void setActionBarTitle(String title) {
		if (mActionBar != null)
			mActionBar.setTitle(title);
	}

	protected void setDisplayHomeAsUpEnabled(boolean flag) {
		if (mActionBar != null)
			mActionBar.setDisplayHomeAsUpEnabled(flag);
		// mActionBar.setHomeButtonEnabled(true);
	}

	@Override
	protected void onResume() {
		super.onResume();
		this.setRequestedOrientation(mScreenRotation);
	}

	@Override
	protected void onStart() {
		super.onStart();
		FlurryAgent.onStartSession(this, "D2Z8WP9VVBJ6N9YSMZPW");
	}

	@Override
	protected void onStop() {
		super.onStop();
		FlurryAgent.onEndSession(this);
	}

}
