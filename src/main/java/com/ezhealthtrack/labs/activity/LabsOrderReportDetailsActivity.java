package com.ezhealthtrack.labs.activity;

import java.text.SimpleDateFormat;

import android.os.Bundle;
import android.view.MenuItem;

import com.ezhealthrack.api.NetworkCalls;
import com.ezhealthtrack.R;
import com.ezhealthtrack.labs.fragments.SideFragment.OnActionSelectedListner;
import com.ezhealthtrack.util.BaseActivity;

public class LabsOrderReportDetailsActivity extends BaseActivity implements
		OnActionSelectedListner, NetworkCalls.OnResponse {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.labs_activity_order_report_details);
		SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM dd', 'yyyy");
		getActionBar().setHomeButtonEnabled(true);
	}

	@Override
	public void onResponseListner(String api) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onActionSelected(int position) {
		// TODO Auto-generated method stub

	}

	public boolean onOptionsItemSelected(final MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {

		case android.R.id.home:
			finish();
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

}
