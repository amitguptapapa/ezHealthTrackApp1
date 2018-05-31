package com.ezhealthtrack.gallery;

import java.util.List;

import uk.co.senab.photoview.PhotoViewAttacher;
import uk.co.senab.photoview.PhotoViewAttacher.OnPhotoTapListener;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ezhealthrack.api.APIs;
import com.ezhealthtrack.R;
import com.ezhealthtrack.activity.DashboardActivity;
import com.ezhealthtrack.model.gallery.SOAPPhoto;
import com.ezhealthtrack.util.Util;

public class PhotoViewAdapter extends PagerAdapter {

	static boolean mPVAAcionBarFlag = true;

	private AppCompatActivity _activity;
	private List<Object> _dataList;
	private LayoutInflater inflater;
	private PhotoViewAttacher mAttacher;
	private LinearLayout mDetailsView;
	private TextView mTitleView;
	private TextView mDescriptionView;

	// constructor
	public PhotoViewAdapter(AppCompatActivity activity, List<Object> dataList) {
		this._activity = activity;
		this._dataList = dataList;
		mPVAAcionBarFlag = true;
	}

	@Override
	public int getCount() {
		return _dataList.size();
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == ((RelativeLayout) object);
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {

		SOAPPhoto record = (SOAPPhoto) _dataList.get(position);

		inflater = (LayoutInflater) _activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View viewLayout = inflater.inflate(R.layout.activity_photo_view_page,
				container, false);

		ImageView imageView = (ImageView) viewLayout.findViewById(R.id.id_niv);

		// imageView.setDefaultImageResId(R.drawable.loading_blackbg);
		// imageView.setErrorImageResId(R.drawable.loading_error);
		mDetailsView = (LinearLayout) viewLayout.findViewById(R.id.id_details);

		mTitleView = (TextView) viewLayout.findViewById(R.id.id_title);
		mTitleView.setText(record.getName());
		mTitleView.setMovementMethod(new ScrollingMovementMethod());

		mDescriptionView = (TextView) viewLayout
				.findViewById(R.id.id_description);
		mDescriptionView.setText(record.getDescription());
		mDescriptionView.setMovementMethod(new ScrollingMovementMethod());

		String url = record.getPreview();
		String imageURL = APIs.URL() + url;
		Log.v("PVA:cmdResponse()", "Image URL: " + imageURL);
		Util.getImageFromUrl(imageURL, DashboardActivity.imgLoader, imageView);

		// The MAGIC happens here!
		mAttacher = new PhotoViewAttacher(imageView);
		mAttacher.setOnPhotoTapListener(new PhotoTapListener(this._activity,
				mDetailsView));

		((ViewPager) container).addView(viewLayout);

		viewLayout.setTag(position);
		return viewLayout;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		((ViewPager) container).removeView((RelativeLayout) object);

	}

	private class PhotoTapListener implements OnPhotoTapListener {

		AppCompatActivity _theActivity;
		LinearLayout _detailsView;

		public PhotoTapListener(AppCompatActivity activity,
				LinearLayout textView) {
			_theActivity = activity;
			_detailsView = textView;
		}

		@Override
		public void onPhotoTap(ImageView view, float x, float y) {

			if (mPVAAcionBarFlag == false) {
				mPVAAcionBarFlag = true;
				_detailsView.setVisibility(View.INVISIBLE);
				_theActivity.getSupportActionBar().hide();
				Log.e("FSIA:", "Tap - Hide");
			} else {
				mPVAAcionBarFlag = false;
				_detailsView.setVisibility(View.VISIBLE);
				_theActivity.getSupportActionBar().show();
				Log.e("FSIA:", "Tap - Show");
			}
		}
	}

	// ???
	public void onDestroy() {
		// Need to call clean-up
		// if (mAttacher != null)
		// mAttacher.cleanup();
	}

}
