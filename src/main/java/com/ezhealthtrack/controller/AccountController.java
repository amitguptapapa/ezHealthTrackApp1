package com.ezhealthtrack.controller;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView.FindListener;
import android.widget.EditText;
import android.widget.TextView;

import com.ezhealthrack.api.LabApis;
import com.ezhealthtrack.EzApp;
import com.ezhealthtrack.R;
import com.ezhealthtrack.activity.EditAccountActivity;
import com.ezhealthtrack.labs.activity.LabsOrderDetailsActivity;
import com.ezhealthtrack.model.AccountModel;
import com.ezhealthtrack.util.Log;
import com.ezhealthtrack.util.NetworkCallController.OnResponse;
import com.ezhealthtrack.util.Util;

public class AccountController {
	public interface GetAccount {
		public void onResponseListner(AccountModel account);
	}

	public static void getLabAccount(final GetAccount responseAcc,
			final Context context) {
		final String url = LabApis.ACCOUNTVIEW;
		final HashMap<String, String> params = new HashMap<String, String>();
		params.put("cli", "api");
		EzApp.networkController.networkCall(context, url, params,
				new OnResponse() {

					@Override
					public void onResponseListner(String response) {
						Log.i(url, response);
						try {
							final JSONObject jObj = new JSONObject(response);
							final JSONObject model = jObj.getJSONObject("data")
									.getJSONObject("model");
							AccountModel account = new AccountModel();
							if (model.has("first_name"))
								account.setFname(model.getString("first_name"));
							if (model.has("middle_name"))
								account.setMname(model.getString("middle_name"));
							if (model.has("last_name"))
								account.setLname(model.getString("last_name"));
							if (model.has("mobile"))
								account.setMobile(model.getString("mobile"));
							if (model.has("email"))
								account.setEmail(model.getString("email"));
							if (model.has("username"))
								account.setUsername(model.getString("username"));
							if (model.has("photo"))
								account.setPhoto(model.getString("photo"));
							if (model.has("dob"))
								account.setDob(model.getString("dob"));
							if (model.has("gender"))
								account.setGender(model.getString("gender"));
							if (model.has("blood_group"))
								account.setBlood(model.getString("blood_group"));
							if (model.has("address1"))
								account.setAddress(model.getString("address1"));
							if (model.has("address2"))
								account.setAddress2(model.getString("address2"));
							if (model.has("area"))
								account.setCmbArea(account.cmbArea_id);
							if (model.has("area_name"))
								account.setCmbArea(model.getString("area_name"));
							if (model.has("city"))
								account.setCmbCity(account.cmbCity_id);
							if (model.has("city_name"))
								account.setCmbCity(model.getString("city_name"));
							if (model.has("state"))
								account.setCmbState(account.cmbState_id);
							if (model.has("state_name"))
								account.setCmbState(model
										.getString("state_name"));
							if (model.has("country"))
								account.setCountry(account.country_id);
							if (model.has("country_name"))
								account.setCountry(model
										.getString("country_name"));
							if (model.has("zip"))
								account.setZip(model.getString("zip"));
							if (model.has("eye_color"))
								account.setEyecolor(model
										.getString("eye_color"));
							if (model.has("hair_color"))
								account.setHaircolor(model
										.getString("hair_color"));
							if (model.has("visible_mark"))
								account.setVisiblemark(model
										.getString("visible_mark"));
							if (model.has("height"))
								account.setHeight(model.getString("height"));
							if (model.has("vaccination"))
								account.setVaccinations(model
										.getString("vaccination"));
							// if (model.has("created_on"))
							// account.set(model.getString("created_on"));
							// if (model.has("updated_on"))
							// account.setFname(model.getString("updated_on"));

							responseAcc.onResponseListner(account);

						} catch (final JSONException e) {
							Log.e("", e);
							Util.Alertdialog(context,
									"There is some error while fetching data, please try again.");
						}

					}
				});
	}

	public static void updateLabAccountPassword(final Context context,
			AccountModel account2, String password, String cpassword,
			String current_password) {

		final String url = LabApis.UPDATEACCOUNT;
		final HashMap<String, String> params = new HashMap<String, String>();
		params.put("cli", "api");
		params.put("LabRegister[password]", password);
		params.put("LabRegister[confirm_password]", cpassword);
		params.put("LabRegister[current_password]", current_password);

		EzApp.networkController.networkCall(context, url, params,
				new OnResponse() {

					@Override
					public void onResponseListner(String response) {
						Log.i(url, response);

						try {
							final JSONObject jObj = new JSONObject(response);
							if (jObj.getString("s").equals("200")) {
								Util.Alertdialog(context,
										"Password updated successfully");
							} else {
								Util.Alertdialog(context,
										"There is some error while updating password, please try again.");
							}

						} catch (final JSONException e) {
							Log.e("", e);
							Util.Alertdialog(context,
									"There is some error while updating password, please try again.");
						}

					}
				});
	}

	public static void updateLabAccountData(final Context context,
			AccountModel account3) {

		final String url = LabApis.UPDATEACCOUNT;
		final HashMap<String, String> params = new HashMap<String, String>();
		params.put("cli", "api");
		params.put("LabRegister[first_name]", account3.getFname());
		params.put("LabRegister[middle_name]", account3.getMname());
		params.put("LabRegister[last_name]", account3.getLname());
		params.put("LabRegister[gender]", account3.getGender());
		params.put("LabRegister[dob]", account3.getDob());
		params.put("LabRegister[mobile]", account3.getMobile());
		params.put("LabRegister[address1]", account3.getAddress());
		params.put("LabRegister[address2]", account3.getAddress2());
		params.put("LabRegister[country]", account3.country_id);
		params.put("LabRegister[state]", account3.cmbState_id);
		params.put("LabRegister[city]", account3.cmbCity_id);
		if (!Util.isEmptyString(account3.cmbArea_id))
			params.put("LabRegister[area]", account3.cmbArea_id);
		params.put("LabRegister[area_name]", account3.getCmbArea());
		params.put("LabRegister[zip] ", account3.getZip());
		params.put("LabRegister[blood_group]", account3.getBlood());
		params.put("LabRegister[height]", account3.getHeight());
		params.put("LabRegister[eye_color]", account3.getEyecolor());
		params.put("LabRegister[hair_color]", account3.getHaircolor());
		params.put("LabRegister[visible_mark]", account3.getVisiblemark());
		params.put("LabRegister[vaccination]", account3.getVaccinations());
		Log.i(url, params.toString());
		final Dialog loaddialog = Util.showLoadDialog(context);
		EzApp.networkController.networkCall(context, url, params,
				new OnResponse() {

					@Override
					public void onResponseListner(String response) {
						Log.i(url, response);
						loaddialog.dismiss();

						try {
							final JSONObject jObj = new JSONObject(response);
							if (jObj.getString("s").equals("200")) {
								Util.Alertdialog(context,
										"Account updated successfully");
							} else {
								Util.Alertdialog(context,
										"There is some error while updating data, please try again.");
							}

						} catch (final JSONException e) {
							Log.e("", e);
							Util.Alertdialog(context,
									"There is some error while updating data, please try again.");
						}

					}
				});
	}

}
