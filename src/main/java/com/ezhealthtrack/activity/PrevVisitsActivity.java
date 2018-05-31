package com.ezhealthtrack.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;

import com.ezhealthtrack.R;
import com.ezhealthtrack.DentistSoap.AddDentistNotesActivity;
import com.ezhealthtrack.adapter.PastVisitAdapter;
import com.ezhealthtrack.physiciansoap.PhysicianSoapActivityMain;
import com.ezhealthtrack.util.BaseActivity;
import com.ezhealthtrack.util.Constants;

public class PrevVisitsActivity extends BaseActivity {
	private ListView listPrev;
	PastVisitAdapter adapter;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_prev_notes);
		listPrev = (ListView) findViewById(R.id.list_prev);
		final SharedPreferences sharedPref = getApplicationContext()
				.getSharedPreferences(Constants.EZ_SHARED_PREF,
						Context.MODE_PRIVATE);
		if (sharedPref.getString(Constants.DR_SPECIALITY, "").equalsIgnoreCase(
				"dentist")) {
			adapter = new PastVisitAdapter(this, R.layout.row_pastvisits,
					AddDentistNotesActivity.arrPastVisit);
			// getActionBar().setTitle(AddDentistNotesActivity.arrPastVisit.size()+" Past Episode");
		} else {
			adapter = new PastVisitAdapter(this, R.layout.row_pastvisits,
					PhysicianSoapActivityMain.arrPastVisit);
			// getActionBar().setTitle(PhysicianSoapActivity.arrPastVisit.size()+" Past Episode");
		}

		listPrev.setAdapter(adapter);
	}

	@Override
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
