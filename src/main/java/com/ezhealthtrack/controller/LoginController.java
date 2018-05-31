package com.ezhealthtrack.controller;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences.Editor;
import android.widget.Toast;

import com.ezhealthrack.api.APIs;
import com.ezhealthtrack.EzApp;
import com.ezhealthtrack.activity.DashboardActivity;
import com.ezhealthtrack.controller.EzNetwork.ResponseHandler;
import com.ezhealthtrack.gcm.GcmController;
import com.ezhealthtrack.greendao.User;
import com.ezhealthtrack.model.BranchInfo;
import com.ezhealthtrack.one.EzActivities;
import com.ezhealthtrack.one.EzUtils;
import com.ezhealthtrack.util.Constants;
import com.ezhealthtrack.util.Log;
import com.ezhealthtrack.util.NetworkCallController.OnResponse;
import com.ezhealthtrack.util.Util;

public class LoginController implements OnResponse {
	// public User user;
	private Activity mContext;
	private Dialog mLoaddialog;
	private String mUserId;
	private String mPassword;

	static boolean mUserAction;
	static boolean mLoggedIn = false;

	public LoginController(Activity context) {
		mContext = context;
	}

	public boolean autoLogin() {

		// Log.i(TAG, ""+EzHealthApplication.areaDao.count());
		boolean loggedIn = EzApp.sharedPref.getBoolean(Constants.IS_LOGIN,
				false);
		if (loggedIn == false)
			return false;

		String userType = EzApp.sharedPref.getString(Constants.USER_TYPE, "D");
		if ((userType.equals("D") || userType.equals("LT"))) {
			mLoggedIn = false;
			startNextActivity();
			this.login(EzApp.sharedPref.getString(Constants.USER_NAME, ""),
					EzApp.sharedPref.getString(Constants.USER_PASSWORD, ""),
					false);
			return true;
		} else {
			Util.Alertdialog(mContext,
					"App is only for Doctor and Lab Technician");
		}
		return false;
	}

	public void login(final String userId, final String password,
			boolean userAction) {

		// EzUtils.hideKeyBoard(activity);
		mUserId = userId;
		mPassword = password;
		mUserAction = userAction;

		String savedId = EzApp.sharedPref.getString(Constants.USER_NAME, "");

		if (!savedId.equals(mUserId)) {
			clearUserData();
		}

		mLoaddialog = null;
		if (mUserAction)
			mLoaddialog = Util.showLoadDialog(mContext);

		final String url = APIs.LOGIN();
		final HashMap<String, String> params = new HashMap<String, String>();
		params.put("format", "json");
		params.put("username", userId);
		params.put("password", Util.getBase64String(password));
		EzApp.networkController.networkCall(mContext, url, params, this);
	}

	// clean user specific
	public void clearUserData() {

		mLoggedIn = false;
		EzApp.patientDao.deleteAll();
		DashboardActivity.arrRefferedByPatients.clear();
		DashboardActivity.arrRefferedToPatients.clear();
		DashboardActivity.arrScheduledPatients.clear();
		EzApp.medRecAllergyDao.deleteAll();
		EzApp.medRecVisitNotesDao.deleteAll();
		EzApp.messageDao.deleteAll();

		final Editor editor = EzApp.sharedPref.edit();
		// editor.putString(Constants.USER_NAME, "");
		editor.putString(Constants.USER_PASSWORD, "");
		editor.putString(Constants.USER_TOKEN, "");
		editor.putBoolean(Constants.IS_LOGIN, false);
		editor.commit();
	}

	private void onLoginFailed() {
		clearUserData();
		if (!mUserAction) {
			EzActivities.startLoginActivity(mContext);
		} else {
			Toast.makeText(mContext, "Incorrect Username or Password !!!",
					Toast.LENGTH_SHORT).show();
		}
		if (mLoaddialog != null) {
			mLoaddialog.dismiss();
		}
	}

	// restart activity if login fails
	@Override
	public void onResponseListner(String response) {
		try {
			final JSONObject jObj = new JSONObject(response);
			if (jObj.getString("s").equals("403")) {
				onLoginFailed();
				return;
			}
			if (!jObj.getString("s").equals("200")) {
				onLoginFailed();
				return;
			}

			// server-id must persist even after clear()
			int serverId = EzApp.sharedPref.getInt(Constants.EZ_SERVER_ID, 0);

			final Editor ed = EzApp.sharedPref.edit();
			ed.clear();
			ed.putInt(Constants.EZ_SERVER_ID, serverId);
			ed.putString(Constants.USER_NAME, mUserId);
			ed.putString(Constants.USER_PASSWORD, mPassword);
			ed.putString(Constants.USER_TOKEN, jObj.getString("auth-token"));

			JSONObject profile = jObj.getJSONObject("user_profile");

			User user = new User();
			user.setUser_id(profile.optString("main-id"));
			user.setId(Long.parseLong(profile.optString("main-id")));
			EzApp.userDao.insertOrReplace(user);

			ed.putString(Constants.DR_NAME, profile.optString("name"));
			ed.putString(Constants.USER_ID, profile.optString("main-id"));
			ed.putString(Constants.ROLE_ID, profile.optString("role-id"));
			ed.putString(Constants.LAB_ADDRESS, profile.optString("labAddress"));
			ed.putString(Constants.LAB_LOCALITY,
					profile.optString("labLocality"));
			ed.putString(Constants.DR_SPECIALITY,
					profile.optString("speciality"));

			if (profile.has("signature")) {
				ed.putString(
						Constants.SIGNATURE,
						APIs.ROOT() + "/documents/show/id/"
								+ profile.optString("signature"));
			}
			if (!Util.isEmptyString(profile.getString("photo"))) {
				ed.putString(Constants.DR_IMAGE, APIs.ROOT()
						+ "/documents/show/id/" + profile.optString("photo"));
			} else {
				ed.putString(Constants.DR_IMAGE, APIs.ROOT()
						+ "/uploads/noImage.gif");
			}
			if (profile.has("labPhoto")
					&& !Util.isEmptyString(profile.getString("labPhoto"))) {
				ed.putString(Constants.LAB_IMAGE, APIs.ROOT()
						+ "/documents/show/id/" + profile.getString("labPhoto"));
			} else {
				ed.putString(Constants.LAB_IMAGE, APIs.ROOT()
						+ "/uploads/noImage.gif");
			}

			ed.putString(Constants.LAB_NAME, profile.optString("labName"));
			ed.putString(Constants.USER_UN_ID, profile.optString("user_un_id"));

			// get tenant id
			ed.putString(Constants.TENANT_ID,
					profile.optString("tenant_id", "0"));

			// get branch info
			JSONArray branchInfo = profile.optJSONArray("branchInfo");
			com.ezhealthtrack.model.BranchInfo.updateBranchInfo(branchInfo);

			ed.putString(Constants.TENANT_TYPE,
					profile.optString("tenant_type"));

			ed.putString(Constants.USER_TYPE, jObj.getString("user_type"));
			ed.putBoolean(Constants.IS_LOGIN, true);
			ed.commit();

			// set default branch
			BranchInfo.setBranchByIndex(0);
			this.startNextActivity();

		} catch (final JSONException e) {
			Log.e("Error", "" + e.getMessage());
			onLoginFailed();
		}
		if (mLoaddialog != null)
			mLoaddialog.dismiss();
	}

	private void startNextActivity() {

		if (mLoggedIn == true)
			return;
		mLoggedIn = true;

		EzActivities.startDashBoardActivity(mContext);
		mContext.finish();

		String userType = EzApp.sharedPref.getString(Constants.USER_TYPE, "D");

		if (userType.equals("D")) {
			EzApp.userController.getWorkFlow(mContext);
			ConfigController.getConfiguration(mContext);
		}
		
		//GcmController.sendRegistrationIdToBackend(mContext);
	}

	public void onLogout() {
		final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setTitle("Do you want to logout ?");

		// set dialog message
		builder.setCancelable(false)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(final DialogInterface dialog,
									final int id) {
								onLogoutConfirmed();
							}
						})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(final DialogInterface dialog,
							final int id) {
						dialog.cancel();
					}
				});

		AlertDialog alertDialog = builder.create();
		alertDialog.show();
	}

	private void onLogoutConfirmed() {

		String url = APIs.LOGOUT();
		Map<String, String> params = new HashMap<String, String>();

		EzNetwork network = new EzNetwork();
		network.POST(url, params, new ResponseHandler() {

			@Override
			public void cmdResponseError(Integer code) {
				// TODO Auto-generated method stub
			}

			@Override
			public void cmdResponse(JSONObject response, String result) {
				// TODO Auto-generated method stub
			}
		});

		// clear user data and start login activity
		clearUserData();
		DashboardActivity.resetOnLogout();
		EzActivities.startLoginActivity(mContext);
		mContext.finish();

		// if (mListner != null)
		// mListner.onLogout();
	}
}
