package com.ezhealthtrack.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.widget.Toast;

public class Dialog extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.main);

		PopIt("Exit Application", "Are you sure you want to exit?");

	}

	public void PopIt(String title, String message) {
		new AlertDialog.Builder(this).setTitle(title).setMessage(message)
				.setPositiveButton("YES", new OnClickListener() {
					public void onClick(DialogInterface arg0, int arg1) {
						// do stuff onclick of YES
						finish();
					}
				}).setNegativeButton("NO", new OnClickListener() {
					public void onClick(DialogInterface arg0, int arg1) {
						// do stuff onclick of CANCEL
						Toast.makeText(getBaseContext(), "You touched CANCEL",
								Toast.LENGTH_SHORT).show();
					}
				}).show();
	}
}