package com.ezhealthtrack.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.ezhealthtrack.EzApp;
import com.ezhealthtrack.one.EzUtils;
import com.ezhealthtrack.util.Constants;

public class BranchInfo {

	//
	// set Branch Id by index
	//
	public static boolean setBranchByIndex(int index) {
		// get selected and previous branch id
		String branchId = BranchInfo.getBranchId(index);
		String preBranchId = EzApp.sharedPref.getString(
				Constants.USER_BRANCH_ID, "");

		// changed ?
		if (branchId.equals(preBranchId)) {
			return false;
		}

		Log.v("BranchInfo", "New branchId=" + branchId);

		// clear branch data
		BranchInfo.clearBranchData();

		// set selected branch id
		final Editor editor = EzApp.sharedPref.edit();
		editor.putString(Constants.USER_BRANCH_ID, branchId);
		editor.commit();
		return true;
	}

	//
	// get Branch Url as string - "/branch/BRANCH-NAME"
	//
	public static String branchUrl() {
		String url = "";
		String branch = EzApp.sharedPref.getString(
				Constants.USER_BRANCH_ID, "");
		if (branch != null && branch.length() > 1) {
			url = "/branch_id/" + branch;
		}
		return url;
	}

	//
	// get Branch Id from branch info list
	//
	private static String getBranchId(int index) {
		JSONArray branches = EzUtils
				.readJSONArryFromFile(EzUtils.BRANCH_INFO_FILENAME);
		if (index < branches.length()) {
			JSONObject branch = branches.optJSONObject(index);
			if (branch != null) {
				return branch.optString("id");
			}
		}
		return "";
	}

	//
	// read branch info file
	//
	public static JSONArray getBranchInfo() {
		return EzUtils.readJSONArryFromFile(EzUtils.BRANCH_INFO_FILENAME);
	}

	//
	// clear previous branch data on new branch selection
	//
	static void clearBranchData() {
		// TODO:

	}

	//
	// dummy branch info - testing
	//
	public static void updateBranchInfo(JSONArray branchInfo) {

		try {
			if (branchInfo == null) {
				branchInfo = new JSONArray();

				JSONObject branchA = new JSONObject();
				branchA.put("id", "777");
				branchA.put("name", "First Branch");
				branchInfo.put(0, branchA);

				JSONObject branchB = new JSONObject();
				branchB.put("id", "888");
				branchB.put("name", "Second Branch");
				branchInfo.put(1, branchB);
			}
			EzUtils.writeToFile(EzUtils.BRANCH_INFO_FILENAME, branchInfo);
		} catch (JSONException e) {

		}
	}

}
