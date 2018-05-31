package com.ezhealthtrack.util;

import java.util.List;

import android.widget.BaseAdapter;

abstract public class EzListAdapter<T> extends BaseAdapter {
	List<T> mDataList = null;

	public EzListAdapter() {

	}

	public EzListAdapter(List<T> initialList) {
		mDataList = initialList;
	}

	public void replaceList(List<T> newList) {
		mDataList = newList;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mDataList == null ? 0 : mDataList.size();
	}

	@Override
	public T getItem(int i) {
		return mDataList == null ? null : mDataList.get(i);
	}

}