package com.ezhealthtrack.views;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.ezhealthrack.api.APIs;
import com.ezhealthtrack.R;
import com.ezhealthtrack.activity.DashboardActivity;
import com.ezhealthtrack.gallery.GalleryViewActivity;
import com.ezhealthtrack.gallery.PostPhotoActivity;
import com.ezhealthtrack.model.gallery.SOAPGallery;
import com.ezhealthtrack.model.gallery.SOAPPhoto;
import com.ezhealthtrack.one.EzUtils;
import com.ezhealthtrack.util.Util;
import com.google.gson.Gson;

public class EzSoapGalleryView {

	private Activity mActivity;
	private ImageView mPhotoView;
	private Button mPhotosView;
	private Button mBtnAdd;
	private Button mBtnAddSmall;

	public EzSoapGalleryView(Activity activity) {
		mActivity = activity;

		mPhotoView = (ImageView) mActivity.findViewById(R.id.img_gallery_photo);
		mPhotosView = (Button) mActivity.findViewById(R.id.btn_photos_view);
		mBtnAdd = (Button) mActivity.findViewById(R.id.btn_photo_add);
		mBtnAddSmall = (Button) mActivity
				.findViewById(R.id.btn_photo_add_small);

		if (mPhotoView == null)
			return;
		mPhotoView.setVisibility(View.GONE);
		mPhotosView.setVisibility(View.GONE);
		mBtnAdd.setVisibility(View.GONE);
		mBtnAddSmall.setVisibility(View.VISIBLE);
	}

	public void showSOAPGallery(SOAPGallery gallery) {
		Log.v("ESGV:showSOAPGallery()", "IN...");
		if (gallery == null) {
			Log.v("ESGV:showSOAPGallery()", "Gallery in NULL ");
			return;
		}
		if (mPhotoView == null) {
			Log.v("ESGV:showSOAPGallery()", "PhotoView is NULL");
			return;
		}
		if (gallery == null || mPhotoView == null)
			return;

		List<SOAPPhoto> photos = gallery.getPhotos();
		if (photos == null || photos.size() < 1) {
			Log.v("ESGV:showSOAPGallery()", "No Photos");
			return;
		}
		String imageURL = photos.get(0).getPreview();
		if (imageURL == null || imageURL.length() < 3) {
			Log.v("ESGV:showSOAPGallery()", "No Photos");
			return;
		}

		mPhotoView.setVisibility(View.VISIBLE);
		mBtnAdd.setVisibility(View.VISIBLE);
		mBtnAddSmall.setVisibility(View.GONE);
		if (EzUtils.getDeviceSize(null).equals(EzUtils.EZ_SCREEN_SMALL)) {
			mPhotosView.setVisibility(View.GONE);
		} else {
			mPhotosView.setVisibility(View.VISIBLE);
		}

		Util.getImageFromUrl(APIs.URL() + imageURL,
				DashboardActivity.imgLoader, mPhotoView);
	}

	public void onAddPhoto(Context context) {
		Intent intent = new Intent(context, PostPhotoActivity.class);
		PostPhotoActivity.mEditId = null;
		context.startActivity(intent);
	}

	public void onShowGallery(Context context) {
		Intent intent = new Intent(context, GalleryViewActivity.class);
		context.startActivity(intent);
	}

}
