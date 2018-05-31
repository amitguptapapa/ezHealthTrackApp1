package com.ezhealthtrack.util;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader.ImageContainer;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.ezhealthtrack.EzApp;
import com.ezhealthtrack.R;
import com.ezhealthtrack.activity.DashboardActivity;
import com.flurry.android.FlurryAgent;

public class BaseActivity extends AppCompatActivity {

	@Override
	protected void onStart() {
		super.onStart();
		FlurryAgent.onStartSession(this, "D2Z8WP9VVBJ6N9YSMZPW");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			ActionBar actionBar = getSupportActionBar();
			actionBar.setCustomView(R.layout.custom_action_bar);
			actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM
					| ActionBar.DISPLAY_SHOW_HOME
					| ActionBar.DISPLAY_SHOW_TITLE);
			actionBar.setHomeButtonEnabled(true);
			actionBar.setDisplayHomeAsUpEnabled(true);

			if (EzApp.sharedPref.getString(Constants.USER_TYPE,
					"D").equals("D")) {
				((TextView) findViewById(R.id.mytitle)).setText("");
				((ImageView) findViewById(R.id.img_lab))
						.setVisibility(View.GONE);
			} else if (EzApp.sharedPref.getString(
					Constants.USER_TYPE, "D").equals("LT")) {
				((TextView) findViewById(R.id.mytitle))
						.setText(EzApp.sharedPref.getString(
								Constants.LAB_NAME, ""));

				if (!Util.isEmptyString(EzApp.sharedPref
						.getString(Constants.LAB_IMAGE, ""))) {
					DashboardActivity.imgLoader.get(
							EzApp.sharedPref.getString(
									Constants.LAB_IMAGE, ""),
							new ImageListener() {

								@Override
								public void onErrorResponse(
										final VolleyError arg0) {
									Log.i("", arg0.getMessage());
									((ImageView) findViewById(R.id.img_lab))
											.setVisibility(View.VISIBLE);
									((ImageView) findViewById(R.id.img_lab))
											.setImageResource(R.drawable.labphoto);
								}

								@Override
								public void onResponse(final ImageContainer ic,
										final boolean arg1) {
									((ImageView) findViewById(R.id.img_lab))
											.setVisibility(View.VISIBLE);
									((ImageView) findViewById(R.id.img_lab))
											.setImageBitmap(ic.getBitmap());

								}
							});
				} else {
					((ImageView) findViewById(R.id.img_lab))
							.setVisibility(View.VISIBLE);
					((ImageView) findViewById(R.id.img_lab))
							.setImageResource(R.drawable.labphoto);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		FlurryAgent.onEndSession(this);
	}

	public boolean onCreateOptionsMenu(final Menu menu) {
		// Inflate the menu items for use in the action bar
		if (menu.size() < 3) {
			final MenuInflater inflater = getMenuInflater();
			inflater.inflate(R.menu.add_patient, menu);
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {

		case R.id.action_close:
			finish();
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

}
