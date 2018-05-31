package com.ezhealthtrack.adapter;

import java.util.List;

import android.widget.BaseAdapter;

import com.ezhealthtrack.activity.DashboardActivity;
import com.ezhealthtrack.fragments.EzListFragment;
import com.ezhealthtrack.greendao.Appointment;

public abstract class EzListFragmentAdaptor extends BaseAdapter {

	final int NEXT_TRIGGER = 5;

	// used for History List
	private int mSelectedRowIndex;

	// data
	private int mLastViewed; // last view row
	protected List<Object> mDataList;

	// fragment
	protected EzListFragment mFragment;

	public EzListFragmentAdaptor(List<Object> dataList, EzListFragment fragment) {
		this.mDataList = dataList;
		this.mFragment = fragment;
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
		return mDataList.get(position);
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
			EzListFragment fragment = (EzListFragment) mFragment;
			fragment.loadData();
		}
	}

	public void onDestroy() {
		this.mFragment = null;
		if (mDataList != null)
			this.mDataList.clear();
		this.mDataList = null;
	}

	public Appointment getSelectedAppointment() {

		return DashboardActivity.arrHistoryPatients.get(mSelectedRowIndex);
	}
}
