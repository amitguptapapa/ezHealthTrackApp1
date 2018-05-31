package com.ezhealthtrack.templates.physiotherapist;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;

import com.ezhealthtrack.R;
import com.ezhealthtrack.util.EzActivity;

public class WebPageActivity extends EzActivity {

	static private String mURL;
	protected Fragment mFragment;

	static public void setWebPageURL(String url) {
		mURL = url;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Log.v("PPA:", "onCreate");
		setContentView(R.layout.activity_fragment_physio);
		this.setDisplayHomeAsUpEnabled(true);
		this.showFragment();
	}

	protected Fragment getFragment() {
		WebPageFragment fragment = new WebPageFragment();
		fragment.setWebPageURL(mURL);
		return fragment;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.web_view, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {

		case android.R.id.home:
			final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					this);

			// set title
			alertDialogBuilder
					.setTitle("Changes will be discarded, Do you want to Exit ?");
			// set dialog message
			alertDialogBuilder
					.setCancelable(false)
					.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									finish();

								}

							})
					.setNegativeButton("No",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(
										final DialogInterface dialog,
										final int id) {
									// if this button is clicked, just close
									// the dialog box and do nothing
									dialog.cancel();
								}
							});

			final AlertDialog alertDialog = alertDialogBuilder.create();
			alertDialog.show();
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onBackPressed() {
		final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				this);

		// set title
		alertDialogBuilder
				.setTitle("Changes will be discarded, Do you want to Exit ?");
		// set dialog message
		alertDialogBuilder
				.setCancelable(false)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								finish();

							}

						})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(final DialogInterface dialog,
							final int id) {
						// if this button is clicked, just close
						// the dialog box and do nothing
						dialog.cancel();
					}
				});

		final AlertDialog alertDialog = alertDialogBuilder.create();

		alertDialog.show();

	}

	protected void showFragment() {

		FragmentManager fm = getSupportFragmentManager();
		mFragment = fm.findFragmentById(R.id.content_frame);
		if (mFragment == null) {
			Log.d("OA::showFragment()", "Get New Fragment");
			mFragment = this.getFragment();
		}
		fm.beginTransaction().replace(R.id.content_frame, mFragment).commit();
	}

	@SuppressWarnings("deprecation")
	public void onReceivedError(WebView webView, int errorCode,
			String description, String failingUrl) {
		try {
			webView.stopLoading();
		} catch (Exception e) {
		}
		try {
			webView.clearView();
		} catch (Exception e) {
		}
		if (webView.canGoBack()) {
			webView.goBack();
		}
		webView.loadUrl("about:blank");
		AlertDialog alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setTitle("Error");
		alertDialog.setMessage("No internet connection was found!");
		alertDialog.setButton("Again", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				finish();
				startActivity(getIntent());
			}
		});

		alertDialog.show();
		onReceivedError(webView, errorCode, description, failingUrl);
	}

}
