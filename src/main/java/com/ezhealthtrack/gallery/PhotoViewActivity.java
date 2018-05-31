package com.ezhealthtrack.gallery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.WindowCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.ezhealthrack.api.APIs;
import com.ezhealthtrack.R;
import com.ezhealthtrack.controller.EzNetwork;
import com.ezhealthtrack.controller.EzNetwork.ResponseHandler;
import com.ezhealthtrack.model.gallery.SOAPPhoto;
import com.ezhealthtrack.one.EzUtils;

public class PhotoViewActivity extends AppCompatActivity {

	// static boolean mUpdated;
	private PhotoViewAdapter mAdapter;
	private ViewPager mViewPager;

	protected List<Object> mDataList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.v("PVA: ", "onCreate");

		supportRequestWindowFeature(WindowCompat.FEATURE_ACTION_BAR_OVERLAY);
		setContentView(R.layout.activity_photo_view);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);

		// OyeUtils.hideStstusBar(this);
		mViewPager = (ViewPager) findViewById(R.id.pager);

		Intent i = getIntent();
		int position = i.getIntExtra("position", 0);

		// Gridview adapter
		mDataList = new ArrayList<Object>();
		mAdapter = new PhotoViewAdapter(this, mDataList);

		mViewPager.setAdapter(mAdapter);

		// displaying selected image first
		// loadGalleryData();
		mViewPager.setCurrentItem(position);
		this.getSupportActionBar().hide();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_gallery_photo_view, menu);
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Toast.makeText(this, "SELECTED", Toast.LENGTH_LONG).show();
		int itemId = item.getItemId();
		if (itemId == android.R.id.home) {
			finish();// NavUtils.navigateUpFromSameTask(this);
			return true;
		} else if (itemId == R.id.menu_edit_title) {
			// Toast.makeText(this, "EDIT POST", Toast.LENGTH_LONG).show();
			this.editPost();
			return true;
		} else if (itemId == R.id.menu_delete_photo) {
			// Toast.makeText(this, "DELETE", Toast.LENGTH_LONG).show();
			this.deletePost();
			return true;
		} else if (itemId == R.id.menu_save_photo) {
			// Toast.makeText(this, "SAVE", Toast.LENGTH_LONG).show();
			int index = mViewPager.getCurrentItem();
			View view = mViewPager.findViewWithTag(index);
			if (view == null) {
				EzUtils.showLong("Ops! Can not save Photo!");
				return true;
			}
			ImageView image = (ImageView) view.findViewById(R.id.id_niv);
			if (image == null) {
				EzUtils.showLong("Ops! Can not save Photo!");
				return true;
			}
			Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
			if (bitmap == null) {
				EzUtils.showLong("Ops! Can not save Photo!");
				return true;
			}
			EzUtils.savePhotoToAlbum(this, bitmap, this.getPhotoId());
			return true;
		}
		return false;
	}

	// load gallery data
	private void loadGalleryData() {
		mDataList.clear();
		List<SOAPPhoto> photos = GalleryViewActivity.mSOAPGallery.getPhotos();
		if (photos == null)
			return;
		for (int i = 0; i < photos.size(); i++) {
			mDataList.add(photos.get(i));
		}
		mAdapter.notifyDataSetChanged();
	}

	/** Called when the user clicks on Delete Post */
	private void editPost() {
		SOAPPhoto photo = this.getPost();

		// Start new Activity
		Intent intent = new Intent(this, PostPhotoActivity.class);
		PostPhotoActivity.mEditId = photo.getId();
		PostPhotoActivity.mEditTitle = photo.getName();
		PostPhotoActivity.mEditDescription = photo.getDescription();
		this.startActivity(intent);
	}

	// get photo details
	private SOAPPhoto getPost() {
		int index = mViewPager.getCurrentItem();

		List<SOAPPhoto> photos = GalleryViewActivity.mSOAPGallery.getPhotos();
		if (photos == null || photos.size() <= index) {
			Log.w("PVA:getPost()", "Photo details not found for i: " + index);
			return null;
		}
		return photos.get(index);
	}

	// get photo id
	private String getPhotoId() {
		int index = mViewPager.getCurrentItem();

		List<SOAPPhoto> photos = GalleryViewActivity.mSOAPGallery.getPhotos();
		if (photos == null || photos.size() <= index) {
			Log.w("PVA:getPostId()", "Phot details not found for i: " + index);
			return null;
		}
		return photos.get(index).getId();
	}

	/** Called when the user confirms Delete Post */
	public void onDeletePostConfirmed() {

		final String id = this.getPhotoId();
		if (id == null) {
			EzUtils.showLong("Bad Photo Id");
			return;
		}
		JSONArray ids = new JSONArray();
		ids.put(id);

		Map<String, String> params = new HashMap<String, String>();
		params.put("id", ids.toString());

		EzNetwork network = new EzNetwork();
		network.POST(APIs.DELETE_GALLERY_PHOTO(), params,
				new ResponseHandler() {

					@Override
					public void cmdResponseError(Integer code) {
						// TODO Auto-generated method stub
						Log.v("PVA:cmdResponseError()", "Some error..");
						EzUtils.showLong("Photo could not be deleted");
					}

					@Override
					public void cmdResponse(JSONObject response, String result) {
						// TODO Auto-generated method stub
						EzUtils.showLong("Photo has been deleted");
						int index = mViewPager.getCurrentItem();
						Log.v("PVA:cmdResponse()", "Removing object at : "
								+ index);
						List<SOAPPhoto> photos = GalleryViewActivity.mSOAPGallery
								.getPhotos();
						photos.remove(index);
						finish();
					}
				});
	}

	/** Called when the user clicks on Delete Post */
	private void deletePost() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Are you sure you want to delete this Photo?")
				.setCancelable(false)
				.setPositiveButton("Delete Post",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								onDeletePostConfirmed();
							}
						})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// mListener.onConfigDataReceived(false);
								// do nothing
							}
						});
		AlertDialog alert = builder.create();
		alert.show();
	}

	@Override
	public void onStart() {
		loadGalleryData();
		super.onStart();
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
