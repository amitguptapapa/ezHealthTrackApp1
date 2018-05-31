package com.ezhealthtrack.controller;

import java.util.List;
import java.util.Map;

import org.json.JSONObject;

abstract public class EzController {

	public final String TAG = getClass().getSimpleName();

	//
	// Interface - Update Listener
	// Caller Fragment may override / implement one or more functions
	//
	public interface UpdateListner {
		// new or updated records received
		public void onDataUpdate(int page, List<?> records);

		// error while loading records
		public void onDataUpdateError(int page);

		// no more updates / records
		public void onDataUpdateFinished(int page);

		// raw response received from server
		public void onCmdResponse(JSONObject response, String result);
	}

	int mPage;
	int mPageSize;
	EzNetwork mNetwork;

	final private int PAGE_SIZE = 10;

	//
	// Functions to be implemented by Entity Controllers
	//

	// get latest records (from server)
	abstract public void getPage(int page, Map<String, String> params,
			UpdateListner listner);

	// get records from local database (if offline data is supported)
	abstract public List<?> getRecords(int count, int offset);

	// get records from local database (if offline data is supported),
	// also get latest records (from server)
	abstract public List<?> getPage(int page, int offset,
			Map<String, String> params, UpdateListner listner);

	// get update listeners
	// abstract public List<UpdateListner> getUpdateListeners();

	public EzController(int page) {
		mPage = page;
		mPageSize = PAGE_SIZE;
		mNetwork = new EzNetwork();
	}

	// check if any network call is in progress
	public boolean callInProgress() {
		return mNetwork.callInProgress();
	}

	public int getPageSzie() {
		return mPageSize;
	}

	static public void initControllers() {
	}

	public void onDestroy() {
		System.gc();
	}
}