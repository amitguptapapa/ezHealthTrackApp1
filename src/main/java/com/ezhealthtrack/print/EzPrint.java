package com.ezhealthtrack.print;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.v4.print.PrintHelper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.ezhealthtrack.R;
import com.ezhealthtrack.activity.PrescriptionActivity;
import com.ezhealthtrack.one.EzUtils;
import com.ezhealthtrack.util.EzActivity;
import com.flurry.android.FlurryAgent;

public class EzPrint extends EzActivity {

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		FlurryAgent.onStartSession(this, "D2Z8WP9VVBJ6N9YSMZPW");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// final View headerView = ((LayoutInflater)
		// getSystemService(Context.LAYOUT_INFLATER_SERVICE))
		// .inflate(R.layout.ez_print_header, null, false);
		// final View footerView = ((LayoutInflater)
		// getSystemService(Context.LAYOUT_INFLATER_SERVICE))
		// .inflate(R.layout.ez_print_footer, null, false);
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		FlurryAgent.onEndSession(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub

		final MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.ez_print, menu);

		return super.onCreateOptionsMenu(menu);
	}

	// Used for printing large screens
	public static Bitmap loadBitmapFromView(View v, int width, int height) {
		Bitmap b = Bitmap.createBitmap(v.getWidth(), v.getHeight(),
				Bitmap.Config.ARGB_8888);
		Canvas c = new Canvas(b);
		v.layout(0, 0, v.getWidth(), v.getHeight());
		v.draw(c);
		return b;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {

		case android.R.id.home:
			finish();
			return true;

		case R.id.print:

			View view = findViewById(R.id.id_scrollview);
			EzUtils.printView(view, "ezHealthTrack_print", this);

			// PrintHelper photoPrinter = new PrintHelper(EzPrint.this);
			// photoPrinter.setScaleMode(PrintHelper.SCALE_MODE_FIT);
			// View view = findViewById(R.id.rl_top);
			// Bitmap bm = loadBitmapFromView(view, 0, 0);
			// photoPrinter.printBitmap("ezHealthTrack_print", bm);
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
