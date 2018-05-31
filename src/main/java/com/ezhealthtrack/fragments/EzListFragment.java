package com.ezhealthtrack.fragments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.ezhealthtrack.R;
import com.ezhealthtrack.adapter.EzListFragmentAdaptor;
import com.ezhealthtrack.controller.EzController;
import com.ezhealthtrack.controller.EzController.UpdateListner;

public abstract class EzListFragment extends ListFragment implements
		UpdateListner {

	// view
	protected ListView mListView;
	protected EzListFragmentAdaptor mListAdapter;
	protected LinearLayout mInitialLoading;
	protected View mListFooterLoadingView;

	// controller
	protected EzController mController;
	protected boolean mOfflineSupport;

	// data
	protected List<Object> mDataList;
	protected int mCurrentPage;
	protected Map<String, String> mUrlParams;
	private ArrayList<Integer> mBadPages;
	private boolean mDataUpdateFinished;

	public EzListFragment(boolean offline) {
		mOfflineSupport = offline;
		mDataList = new ArrayList<Object>();
		Log.i("LOF", "EzListFragment(): Size: " + mDataList.size());
		mCurrentPage = 0;
		mUrlParams = new HashMap<String, String>();
		mBadPages = new ArrayList<Integer>();
		mOfflineSupport = false;
	}

	// initialize DataList -- add initial data, if required
	protected void initDataList() {
		// sub-class may override it
		mDataList.clear();
		Log.i("LOF", "initDataList(): Size: " + mDataList.size());
	}

	// called by sub-class
	public void onCreateListView(View parent, LayoutInflater inflater) {

		mListView = (ListView) parent.findViewById(android.R.id.list);
		mInitialLoading = (LinearLayout) parent
				.findViewById(R.id.idlist_progressbar);
		mInitialLoading.setVisibility(View.VISIBLE);
		mListFooterLoadingView = parent.findViewById(R.id.id_progressbar_main);
		if (mListFooterLoadingView != null) {
			mListFooterLoadingView.setVisibility(View.INVISIBLE);
			mListFooterLoadingView.setVisibility(View.GONE);
		}
	}

	// local data got updated
	public void onDataUpdate(int page, List<?> newRecords) {
		if (mOfflineSupport) {
			// TODO: may not work in all cases
			List<?> records = mController.getRecords((mCurrentPage + 1)
					* mController.getPageSzie(), 0);
			this.initDataList();
			mDataList.addAll(records);
		} else {
			mDataList.addAll(newRecords);
		}
		Log.i("LOF", "onDataUpdate(): Size: " + mDataList.size());
		this.setInProgressActivity();
	}

	// a network call to refresh data failed
	public void onDataUpdateError(int page) {
		// reset to load more
		mListAdapter.resetLastViewed();
		if (!mBadPages.contains(page)) {
			mBadPages.add(page);
		}
		this.setInProgressActivity();
	}

	public void onDataUpdateFinished(int page) {
		mDataUpdateFinished = true;
	}

	// refresh this page if previous load had failed
	// public void refreshPage(int page) {
	// if (mBadPages.contains(page)) {
	// mBadPages.remove(Integer.valueOf(page));
	// if (mController != null) {
	// Log.i("OFL:onRefreshPage()", "Refresh Bad Page:" + page);
	// mController.getPage(page, mUrlParams);
	// }
	// }
	// }

	public void loadData() {

		if (mDataUpdateFinished == true) {
			Log.w("ECFL::loadData()", "Data Update finished");
			return;
		}

		mCurrentPage++;

		if (mOfflineSupport) {
			// TODO: call network
			List<?> records = mController.getPage(mCurrentPage,
					mDataList.size(), mUrlParams, this);

			if (records != null && records.size() > 0) {
				if (mDataList.isEmpty())
					this.initDataList();
				mDataList.addAll(records);
			}
		} else {
			// call network
			mController.getPage(mCurrentPage, mUrlParams, this);
		}

		Log.i("LOF", "loadData(): Size: " + mDataList.size());
		this.setInProgressActivity();
	}

	protected void setInProgressActivity() {
		mListAdapter.notifyDataSetChanged();
		if (mController.callInProgress()) {
			if (mDataList.size() < 1) {
				mInitialLoading.setVisibility(View.VISIBLE);
				mListFooterLoadingView.setVisibility(View.GONE);
			} else {
				mInitialLoading.setVisibility(View.GONE);
				mListFooterLoadingView.setVisibility(View.VISIBLE);
			}
		} else {
			mInitialLoading.setVisibility(View.GONE);
			mListFooterLoadingView.setVisibility(View.GONE);
		}
	}
}
