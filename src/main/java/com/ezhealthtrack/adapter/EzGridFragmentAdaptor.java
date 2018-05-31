package com.ezhealthtrack.adapter;

import java.util.List;

import android.widget.BaseAdapter;

import com.ezhealthtrack.fragments.EzGridFragment;
import com.ezhealthtrack.fragments.PatientsListFragment.OnCallback;

public abstract class EzGridFragmentAdaptor extends BaseAdapter {

	final int NEXT_TRIGGER = 5;

	// data
	private int mLastViewed; // last view row
	protected List<Object> mDataList;

	// fragment
	protected EzGridFragment mFragment;

	protected OnCallback onCallback;

	public EzGridFragmentAdaptor(List<Object> dataList,
			EzGridFragment fragment, OnCallback onCallback) {
		this.mDataList = dataList;
		this.mFragment = fragment;
		this.onCallback = onCallback;
	}

	@Override
	public int getCount() {
		if (mDataList == null)
			return 0;
		return mDataList.size();
	}

	@Override
	public Object getItem(int position) {
		if (mDataList == null)
			return null;
		return (Object) mDataList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	protected void onReload() {
		mLastViewed = 0;
	}

	public void resetLastViewedToZero() {
		mLastViewed = 0;
	}

	public void resetLastViewed() {
		if (mLastViewed > NEXT_TRIGGER) {
			mLastViewed = mLastViewed - NEXT_TRIGGER;
		} else {
			mLastViewed = 0;
		}
	}

	protected void getView(int position) {
		if (position > mLastViewed && (position + NEXT_TRIGGER) == getCount()) {
			mLastViewed = position;
			EzGridFragment fragment = (EzGridFragment) mFragment;
			fragment.loadData();
		}
	}

	public void onDestroy() {
		this.mFragment = null;
		if (mDataList != null)
			this.mDataList.clear();
		this.mDataList = null;
	}

}
