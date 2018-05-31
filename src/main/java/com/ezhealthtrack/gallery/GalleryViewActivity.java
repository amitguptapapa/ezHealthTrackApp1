package com.ezhealthtrack.gallery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.ezhealthrack.api.APIs;
import com.ezhealthtrack.R;
import com.ezhealthtrack.controller.EzNetwork;
import com.ezhealthtrack.controller.EzNetwork.ResponseHandler;
import com.ezhealthtrack.controller.SoapNotesController;
import com.ezhealthtrack.model.gallery.SOAPGallery;
import com.ezhealthtrack.model.gallery.SOAPPhoto;
import com.ezhealthtrack.one.EzUtils;
import com.ezhealthtrack.util.EzActivity;
import com.google.gson.Gson;

public class GalleryViewActivity extends EzActivity {

	final static public String FILE_GALLERY_DATA = "gallery_data.txt";

	private GUtils mUtils;
	private GalleryViewAdapter mAdapter;
	private GridView mGridView;
	private int mColumnWidth;
	protected ArrayList<String> mDataList;

	public static SOAPGallery mSOAPGallery;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.v("GVA: ", "onCreate");

		setContentView(R.layout.activity_gallery_view);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);

		mGridView = (GridView) findViewById(R.id.grid_view);

		// Initializing Grid View
		InitilizeGridLayout();

		// Grid view adapter
		mSOAPGallery = null;
		mDataList = new ArrayList<String>();
		mAdapter = new GalleryViewAdapter(GalleryViewActivity.this, mDataList,
				mColumnWidth);

		// setting grid view adapter
		mGridView.setAdapter(mAdapter);
		mGridView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {

				OnImageClickListener listner = new OnImageClickListener(
						GalleryViewActivity.this, position);
				listner.onClick(null);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_gallery_view, menu);
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		return super.onPrepareOptionsMenu(menu);
	}

	// load data
	private void loadGallery() {

		EzNetwork network = new EzNetwork();
		Map<String, String> params = new HashMap<String, String>();

		String id = SoapNotesController.getGalleryId();
		if (id == null) {
			EzUtils.showLong("Bad Gallery Id");
			return;
		}
		params.put("id", id);
		params.put("order", "desc");
		params.put("page", "1");
		params.put("limit", "25");

		network.POST(APIs.GET_GALLERY(), params, new ResponseHandler() {

			@Override
			public void cmdResponseError(Integer code) {
				// TODO Auto-generated method stub
				Log.v("GVA:cmdResponseError()", "Some error..");
			}

			@Override
			public void cmdResponse(JSONObject response, String result) {
				// TODO Auto-generated method stub
				Log.v("GVA:cmdResponse()",
						"Gallery Response: " + response.toString());

				mSOAPGallery = new Gson().fromJson(response.toString(),
						SOAPGallery.class);
				// update parent
				SoapNotesController.updateSOAPGalleryPhotos(mSOAPGallery
						.getPhotos());
				updateScreen();
			}
		});
	}

	void updateScreen() {
		if (mSOAPGallery == null)
			return;
		List<SOAPPhoto> photos = mSOAPGallery.getPhotos();
		if(mDataList!=null) {
			mDataList.clear();
		}
		if (photos == null || photos.size() < 1) {
			Log.v("GVA:cmdResponse()", "No photos in Gallery");
			EzUtils.showLong("Photos not found");
			mAdapter.notifyDataSetChanged();
			return;
		}
		for (int i = 0; i < photos.size(); i++) {
			String url = photos.get(i).getPreview();
			if (url == null)
				continue;
			String imageURL = APIs.URL() + url;
			if(mDataList!=null) {
				mDataList.add(imageURL);
			}
			Log.v("GVA:cmdResponse()", "Image URL: " + imageURL);
		}
		mAdapter.notifyDataSetChanged();
	}

	// on photo delete
	static public void onPhotoRemoved(int index) {
		List<SOAPPhoto> photos = mSOAPGallery.getPhotos();
		if (index < photos.size()) {
			// remove photo and update parent
			photos.remove(index);
			SoapNotesController.updateSOAPGalleryPhotos(photos);
		}
	}

	// on photo details update
	static public void onPhotoUpdated(SOAPPhoto photo) {
		List<SOAPPhoto> photos = mSOAPGallery.getPhotos();
		for (int i = 0; i < photos.size(); ++i) {
			SOAPPhoto record = photos.get(i);
			if (record.getId().equals(photo.getId())) {
				photos.set(i, photo);
				return;
			}
		}
	}

	private void InitilizeGridLayout() {
		mColumnWidth = 0;
		int numColumns = 3;
		Resources r = getResources();

		mUtils = new GUtils(this);
		mUtils.setColumnCount(numColumns);
		mColumnWidth = mUtils.getColumnWidth();

		float padding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				GConstants.GRID_PADDING, r.getDisplayMetrics());

		mGridView.setNumColumns(mUtils.getColumnCount());
		mGridView.setColumnWidth(mColumnWidth);
		mGridView.setStretchMode(GridView.NO_STRETCH);
		mGridView.setPadding((int) padding, (int) padding, (int) padding,
				(int) padding);
		mGridView.setHorizontalSpacing((int) padding);
		mGridView.setVerticalSpacing((int) padding);
	}

	class OnImageClickListener implements OnClickListener {

		int _postion;
		Context context;

		// constructor
		public OnImageClickListener(Context c, int position) {
			Log.d("GVIA", "OnImageClickListener");
			this._postion = position;
			this.context = c;
		}

		@Override
		public void onClick(View v) {
			Log.d("GVIA", "OnClick");
			// on selecting grid view image
			// launch full screen activity
			Intent i = new Intent(context, PhotoViewActivity.class);
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
					| Intent.FLAG_ACTIVITY_SINGLE_TOP);
			i.putExtra("position", _postion);
			context.startActivity(i);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int itemId = item.getItemId();
		if (itemId == android.R.id.home) {
			finish();
			return true;
		} else if (itemId == R.id.menu_add_photo) {
			// Start Activity
			Intent intent = new Intent(this, PostPhotoActivity.class);
			PostPhotoActivity.mEditId = null;
			startActivity(intent);
			return true;
		}
		return false;
	}

	@Override
	public void onStart() {
		Log.v("GVA: ", "onStart..");
		loadGallery();
		updateScreen();
		super.onStart();
	}

	@Override
	public void onStop() {
		Log.v("GVA: ", "onStop..");
		super.onStop();
	}

	@Override
	public void onDestroy() {
		if (mDataList != null) {
			mDataList.clear();
			mDataList = null;
		}
		System.gc();
		super.onDestroy();
	}

}
