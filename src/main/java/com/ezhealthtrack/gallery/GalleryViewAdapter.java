package com.ezhealthtrack.gallery;

import java.util.List;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.ezhealthtrack.R;
import com.ezhealthtrack.activity.DashboardActivity;
import com.ezhealthtrack.util.Util;

public class GalleryViewAdapter extends BaseAdapter {
	private Activity _activity;
	protected List<String> _dataList;
	int _imageWidth;

	public GalleryViewAdapter(Activity activity, List<String> lsit,
			int imageWidth) {
		this._activity = activity;
		this._dataList = lsit;
		this._imageWidth = imageWidth;
	}

	@Override
	public int getCount() {
		return _dataList.size();
		// return this._filePaths.size();
	}

	@Override
	public Object getItem(int position) {
		return _dataList.get(position);
		// return this._filePaths.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		// TextView view = new TextView(mActivity);
		// view.setText(mDataList.get(position));
		// return view;

		ImageView imageView;
		if (convertView == null) {
			imageView = new ImageView(_activity);
			imageView.setBackgroundColor(Color.GRAY);
		} else {
			imageView = (ImageView) convertView;
		}
		// imageView.setDefaultImageResId(R.drawable.loading_blackbg);
		// imageView.setErrorImageResId(R.drawable.loading_error);

		// imageView.setMaxHeight(maxHeight)
		// imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
		imageView.setLayoutParams(new GridView.LayoutParams(_imageWidth,
				_imageWidth * 3 / 4));

		Util.getImageFromUrl(_dataList.get(position),
				DashboardActivity.imgLoader, imageView);

		// _cmdHandler.loadDiskImage(
		// OyeCmdHandler.mPhotoUrlPath + _dataList.get(position),
		// imageView);
		return imageView;
	}
}
