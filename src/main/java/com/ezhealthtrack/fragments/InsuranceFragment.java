package com.ezhealthtrack.fragments;

import java.io.File;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ezhealthrack.api.APIs;
import com.ezhealthtrack.EzApp;
import com.ezhealthtrack.R;
import com.ezhealthtrack.activity.DashboardActivity;
import com.ezhealthtrack.model.Consultation_Charges;
import com.ezhealthtrack.model.Insurance;
import com.ezhealthtrack.util.Constants;
import com.ezhealthtrack.util.UploadDocumentRequest;
import com.ezhealthtrack.util.Util;
import com.google.gson.Gson;
import com.orleonsoft.android.simplefilechooser.ui.FileChooserActivity;

public class InsuranceFragment extends Fragment {
	Consultation_Charges model = new Consultation_Charges();
	private LayoutInflater inflater;
	private LinearLayout llInsurance;
	private EditText editName;
	private EditText editDescription;
	private EditText editDateIssued;
	private EditText editDateExpiry;
	private EditText editPremium;
	private EditText editInitialVisit;
	private EditText editRepeatVisit;
	private EditText editLanguage;
	private TextView txtUpload;
	private TextView txtName;
	private TextView txtDescription;
	private TextView txtDateIssued;
	private TextView txtDateExpiry;
	private TextView txtPremium;
	private TextView txtInitialVisit;
	private TextView txtRepeatVisit;
	private TextView txtLanguage;
	//private CheckBox cbCopyAddress;
	private Spinner spinnerAddedList;
	private Button btnUpload;
	public final int FILE_CHOOSER = 1;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.fragment_insurance, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		inflater = (LayoutInflater) getActivity().getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);
		llInsurance = (LinearLayout) getActivity().findViewById(
				R.id.ll_insurance);
		editName = (EditText) getActivity().findViewById(R.id.edit_name);
		editDescription = (EditText) getActivity().findViewById(R.id.edit_desc);
		editDateIssued = (EditText) getActivity().findViewById(
				R.id.edit_date_issuedi);
		editDateExpiry = (EditText) getActivity().findViewById(
				R.id.edit_date_expiryi);
		editPremium = (EditText) getActivity().findViewById(R.id.edit_premium);
		txtUpload = (TextView) getActivity().findViewById(R.id.txt_upload);
		model = DashboardActivity.profile.getConsultation_Charges();
		editInitialVisit = (EditText) getActivity().findViewById(
				R.id.edit_initial_visit);

		editInitialVisit.setText(model.getInitial_visit());
		editRepeatVisit = (EditText) getActivity().findViewById(
				R.id.edit_repeat_visit);
		editRepeatVisit.setText(model.getRepeat_visit());
		editLanguage = (EditText) getActivity()
				.findViewById(R.id.edit_language);
		txtName = (TextView) getActivity().findViewById(R.id.txt_name);
		((TextView) getActivity().findViewById(R.id.txt_name)).append(Html
				.fromHtml("<sup><font color='#EE0000'>*</font></sup>"));
		txtDescription = (TextView) getActivity().findViewById(R.id.txt_desc);
		((TextView) getActivity().findViewById(R.id.txt_desc)).append(Html
				.fromHtml("<sup><font color='#EE0000'>*</font></sup>"));
		txtDateIssued = (TextView) getActivity().findViewById(
				R.id.txt_date_issuedi);
		((TextView) getActivity().findViewById(R.id.txt_date_issuedi))
				.append(Html
						.fromHtml("<sup><font color='#EE0000'>*</font></sup>"));
		txtDateExpiry = (TextView) getActivity().findViewById(
				R.id.txt_date_expiryi);
		((TextView) getActivity().findViewById(R.id.txt_date_expiryi))
				.append(Html
						.fromHtml("<sup><font color='#EE0000'>*</font></sup>"));
		txtPremium = (TextView) getActivity().findViewById(R.id.txt_premium);
		((TextView) getActivity().findViewById(R.id.txt_premium)).append(Html
				.fromHtml("<sup><font color='#EE0000'>*</font></sup>"));
		txtInitialVisit = (TextView) getActivity().findViewById(
				R.id.txt_initial_visit);
		((TextView) getActivity().findViewById(R.id.txt_initial_visit))
				.append(Html
						.fromHtml("<sup><font color='#EE0000'>*</font></sup>"));
		txtRepeatVisit = (TextView) getActivity().findViewById(
				R.id.txt_repeat_visit);
		((TextView) getActivity().findViewById(R.id.txt_repeat_visit))
				.append(Html
						.fromHtml("<sup><font color='#EE0000'>*</font></sup>"));
		txtLanguage = (TextView) getActivity().findViewById(R.id.txt_language);
		((TextView) getActivity().findViewById(R.id.txt_language)).append(Html
				.fromHtml("<sup><font color='#EE0000'>*</font></sup>"));
		spinnerAddedList = (Spinner) getActivity().findViewById(
				R.id.spinner_added_list);
//		cbCopyAddress = (CheckBox) getActivity().findViewById(R.id.cb_copy_address);
//		Log.i("copy flag", ""+DashboardActivity.profile.BRANCH_ADDR_AS_PATIENT_ADDR);
//		cbCopyAddress.setChecked(DashboardActivity.profile.BRANCH_ADDR_AS_PATIENT_ADDR);
		ArrayAdapter<String> adapters = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_spinner_item,
				DashboardActivity.profile.getLanguages_Known());
		adapters.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerAddedList.setAdapter(adapters);

		Util.datePicker(editDateIssued, getActivity());
		Util.datePicker(editDateExpiry, getActivity());
		for (Insurance insurance : DashboardActivity.profile.getInsurance()) {
			addInsuranceRow(insurance);
		}
		getActivity().findViewById(R.id.btn_add_insurance).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {

						if (!Util.isEmptyString(editName.getText().toString())) {
							if (!Util.isEmptyString(editDescription.getText()
									.toString())) {
								if (!Util.isEmptyString(editDateIssued
										.getText().toString())) {
									if (!Util.isEmptyString(editDateExpiry
											.getText().toString())) {
										if (!Util.isEmptyString(editPremium
												.getText().toString())) {
											insuranceValidation(true);
										} else {
											Util.Alertdialog(getActivity(),
													"Please enter Premium");
										}
									} else {
										Util.Alertdialog(getActivity(),
												"Please enter Expiry Date");
									}
								} else {
									Util.Alertdialog(getActivity(),
											"Please enter Issued Date");
								}
							} else {
								Util.Alertdialog(getActivity(),
										"Please enter Description");
							}
						} else {
							Util.Alertdialog(getActivity(), "Please enter Name");
						}

					}
				});
		getActivity().findViewById(R.id.btn_add_consultation)
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						model.setInitial_visit(editInitialVisit.getText()
								.toString());
						model.setRepeat_visit(editRepeatVisit.getText()
								.toString());
						if (!Util.isEmptyString(editInitialVisit.getText()
								.toString())) {
							if (Util.isNumeric(editInitialVisit.getText()
									.toString())) {
								if (!Util.isEmptyString(editRepeatVisit
										.getText().toString())) {
									if (Util.isNumeric(editRepeatVisit
											.getText().toString())) {
										addConsultation();
									} else {
										Util.Alertdialog(getActivity(),
												"Only Numeric Value allowed in Repeat Visit");
									}
								} else {
									Util.Alertdialog(getActivity(),
											"Please enter Repeat Visit Charges");
								}
							} else {
								Util.Alertdialog(getActivity(),
										"Only Numeric Value allowed in Initial Visit");
							}
						} else {
							Util.Alertdialog(getActivity(),
									"Please enter Initial Visit Charges");
						}

					}
				});

		getActivity().findViewById(R.id.btn_add_languages).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						boolean flag = true;
						for(String s:DashboardActivity.profile.getLanguages_Known()){
							if(s.equalsIgnoreCase(editLanguage.getText()
								.toString()))
								flag = false;
						}
						if (!Util.isEmptyString(editLanguage.getText()
								.toString())) {
							if(flag)
							addLanguages();
							else
								Util.Alertdialog(getActivity(),
										"Language Already added.");
						} else {
							Util.Alertdialog(getActivity(),
									"Please select a Language");
						}

					}
				});
		getActivity().findViewById(R.id.button_insurance).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						Util.Alertdialog(getActivity(),
								"Profile Updated successfully");

					}
				});
		btnUpload = (Button) getActivity().findViewById(R.id.btn_upload);
		btnUpload.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(),
						FileChooserActivity.class);
				getActivity().startActivityForResult(intent, FILE_CHOOSER);

			}
		});

	}

	public void onClick(final View v) {
		switch (v.getId()) {

		}

	}

	@Override
	public void onStart() {
		super.onStart();
	}

	private void addInsuranceRow(final Insurance insurance) {
		final View v = inflater.inflate(R.layout.row_insurance, null);
		llInsurance.addView(v);
		((TextView) v.findViewById(R.id.txt_name)).setText(insurance
				.getProvider());
		((TextView) v.findViewById(R.id.txt_desc)).setText(insurance.getDesc());

		try {
			((TextView) v.findViewById(R.id.txt_date_issued))
					.setText(EzApp.sdfMmddyy
							.format(EzApp.sdfyyMmdd
									.parse(insurance.getDateis())));
		} catch (ParseException e) {
			((TextView) v.findViewById(R.id.txt_date_issued)).setText(insurance
					.getDateis());
		}

		try {
			((TextView) v.findViewById(R.id.txt_date_expiry))
					.setText(EzApp.sdfMmddyy
							.format(EzApp.sdfyyMmdd
									.parse(insurance.getDateex())));
		} catch (ParseException e) {
			((TextView) v.findViewById(R.id.txt_date_expiry)).setText(insurance
					.getDateex());
		}
		((TextView) v.findViewById(R.id.txt_premium)).setText(insurance
				.getPremium());
		TextView txtUpload = ((TextView) v.findViewById(R.id.txt_upload));
		ImageView imgRead = (ImageView) v.findViewById(R.id.img_read);
		if (Util.isEmptyString(insurance.getIfile())) {
			txtUpload.setText("Document not available!");
			imgRead.setVisibility(View.GONE);
			txtUpload.setVisibility(View.VISIBLE);
		} else {
			imgRead.setVisibility(View.VISIBLE);
			txtUpload.setVisibility(View.GONE);
		}

		imgRead.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String url = APIs.VIEW() + insurance.getIfile();
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setData(Uri.parse(url));
				startActivity(i);

			}
		});

		v.findViewById(R.id.btn_rem_insurance).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View a) {
						deleteInsurance(insurance, v);

					}
				});
	}

	private void deleteInsurance(final Insurance insurance, final View v) {
		final String url = APIs.INSURANCE_ADD_DELETE();
		final Dialog loaddialog = Util.showLoadDialog(getActivity());
		final StringRequest schedulePlanRequest = new StringRequest(
				Request.Method.POST, url, new Response.Listener<String>() {
					@Override
					public void onResponse(final String response) {
						Log.i("", response);
						JSONObject jObj;
						try {
							jObj = new JSONObject(response);
							if (jObj.getString("s").equals("200")) {
								DashboardActivity.profile.getInsurance()
										.remove(insurance);
								llInsurance.removeView(v);
							} else {
								Util.Alertdialog(getActivity(),
										jObj.getString("m"));
							}
						} catch (Exception e) {
							Util.Alertdialog(getActivity(), e.toString());
						}
						loaddialog.dismiss();
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(final VolleyError error) {
						Util.Alertdialog(getActivity(), error.toString());
						loaddialog.dismiss();
					}
				}) {
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				final HashMap<String, String> loginParams = new HashMap<String, String>();
				loginParams.put("auth-token", Util
						.getBase64String(EzApp.sharedPref
								.getString(Constants.USER_TOKEN, "")));
				return loginParams;
			}

			@Override
			protected Map<String, String> getParams() {
				final HashMap<String, String> loginParams = new HashMap<String, String>();
				loginParams.put("iid", insurance.getIid());
				loginParams.put("type", "remove");
				return loginParams;
			}

		};
		schedulePlanRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 5,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		EzApp.mVolleyQueue.add(schedulePlanRequest);

	}

	private void addInsuranceWithFile() {
		Log.i("", "upload insurance with file called");
		final Dialog dialog1 = Util.showLoadDialog(getActivity());
		File file = new File(btnUpload.getText().toString());
		String requestURL = APIs.INSURANCE_ADD_DELETE()
				+ "/cli/api/format/json?type=add&provider="
				+ editName.getText().toString() + "&desc="
				+ editDescription.getText().toString() + "&dateis="
				+ editDateIssued.getText().toString() + "&dateex="
				+ editDateExpiry.getText().toString() + "&premium="
				+ editPremium.getText().toString();

		Log.i("", requestURL);
		UploadDocumentRequest request = new UploadDocumentRequest(requestURL,
				new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError e) {
						dialog1.dismiss();
						Util.Alertdialog(getActivity(),
								"There is some network error, please try again");
						e.printStackTrace();

					}
				}, new Listener<String>() {

					@Override
					public void onResponse(String response) {
						Log.i("", response);
						JSONObject jObj;
						try {
							jObj = new JSONObject(response);
							if (jObj.getString("s").equals("200")) {
								Insurance insurance = new Insurance();
								insurance.setIid(jObj.getString("iid"));
								insurance.setProvider(editName.getText()
										.toString());
								insurance.setDesc(editDescription.getText()
										.toString());
								insurance.setDateis(editDateIssued.getText()
										.toString());
								insurance.setDateex(editDateExpiry.getText()
										.toString());
								insurance.setPremium(editPremium.getText()
										.toString());
								insurance.setIfile(jObj.getString("doc_url"));

								DashboardActivity.profile.getInsurance().add(
										insurance);
								addInsuranceRow(insurance);
								;
								editDateIssued.setText("");
								editName.setText("");
								editDateExpiry.setText("");
								txtUpload.setText("");
								editDescription.setText("");
								editPremium.setText("");

							} else {
								Util.Alertdialog(getActivity(),
										jObj.getString("m"));
							}
						} catch (Exception e) {
							Util.Alertdialog(getActivity(), e.toString());
						}
						dialog1.dismiss();

					}
				}, file, "uploadfile", "uploadfile") {
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				final HashMap<String, String> headerParams = new HashMap<String, String>();
				headerParams.put("auth-token", Util
						.getBase64String(EzApp.sharedPref
								.getString(Constants.USER_TOKEN, "")));
				headerParams.put("Accept-Encoding", "deflate");
				headerParams.put("Accept", "application/json");
				headerParams.put("Content-Type",
						"multipart/form-data; boundary=" + "--eriksboundry--");
				return headerParams;
			}

			@Override
			public String getBodyContentType() {
				String BOUNDARY = "--eriksboundry--";
				return "multipart/form-data; boundary=" + BOUNDARY;
			}

			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				final HashMap<String, String> params = new HashMap<String, String>();
				params.put("type", "add");
				params.put("provider", editName.getText().toString());
				params.put("desc", editDescription.getText().toString());
				params.put("dateis", editDateIssued.getText().toString());
				params.put("dateex", editDateExpiry.getText().toString());
				params.put("premium", editPremium.getText().toString());
				return params;
			}

		};
		request.setRetryPolicy(new DefaultRetryPolicy(200000, 5,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

		Log.i("", new Gson().toJson(request));

		EzApp.mVolleyQueue.add(request);

	}

	private void addInsurance() {
		final String url = APIs.INSURANCE_ADD_DELETE();
		final Dialog loaddialog = Util.showLoadDialog(getActivity());
		final StringRequest schedulePlanRequest = new StringRequest(
				Request.Method.POST, url, new Response.Listener<String>() {
					@Override
					public void onResponse(final String response) {
						Log.i("", response);
						JSONObject jObj;
						try {
							jObj = new JSONObject(response);
							if (jObj.getString("s").equals("200")) {
								Insurance insurance = new Insurance();
								insurance.setIid(jObj.getString("iid"));
								insurance.setProvider(editName.getText()
										.toString());
								insurance.setDesc(editDescription.getText()
										.toString());
								insurance.setDateis(editDateIssued.getText()
										.toString());
								insurance.setDateex(editDateExpiry.getText()
										.toString());
								insurance.setPremium(editPremium.getText()
										.toString());
								insurance.setIfile(txtUpload.getText()
										.toString());

								DashboardActivity.profile.getInsurance().add(
										insurance);
								addInsuranceRow(insurance);
								;
								editDateIssued.setText("");
								editName.setText("");
								editDateExpiry.setText("");
								txtUpload.setText("");
								editDescription.setText("");
								editPremium.setText("");

							} else {
								Util.Alertdialog(getActivity(),
										jObj.getString("m"));
							}
						} catch (Exception e) {
							Util.Alertdialog(getActivity(), e.toString());
						}
						loaddialog.dismiss();

					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(final VolleyError error) {
						Util.Alertdialog(getActivity(), error.toString());
						loaddialog.dismiss();
					}
				}) {

			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				final HashMap<String, String> loginParams = new HashMap<String, String>();
				loginParams.put("auth-token", Util
						.getBase64String(EzApp.sharedPref
								.getString(Constants.USER_TOKEN, "")));
				return loginParams;
			}

			@Override
			protected Map<String, String> getParams() {
				final HashMap<String, String> loginParams = new HashMap<String, String>();
				// certification, institute, datei (date issued) , datee (date
				// expired) , cme
				loginParams.put("type", "add");
				loginParams.put("provider", editName.getText().toString());
				loginParams.put("desc", editDescription.getText().toString());
				loginParams.put("dateis", editDateIssued.getText().toString());
				loginParams.put("dateex", editDateExpiry.getText().toString());
				loginParams.put("premium", editPremium.getText().toString());
				loginParams.put("upload", txtUpload.getText().toString());
				return loginParams;
			}

		};
		schedulePlanRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 5,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		EzApp.mVolleyQueue.add(schedulePlanRequest);
	}

	private void addLanguages() {
		final String url = APIs.UPDATE_LANGUAGES();
		final Dialog loaddialog = Util.showLoadDialog(getActivity());
		final StringRequest schedulePlanRequest = new StringRequest(
				Request.Method.POST, url, new Response.Listener<String>() {
					@Override
					public void onResponse(final String response) {
						Log.i("", response);
						JSONObject jObj;
						try {
							jObj = new JSONObject(response);
							if (jObj.getString("s").equals("200")) {
								DashboardActivity.profile.getLanguages_Known()
										.add(editLanguage.getText().toString());
								editLanguage.setText("");

							} else {
								Util.Alertdialog(getActivity(),
										jObj.getString("m"));
							}
						} catch (Exception e) {
							Util.Alertdialog(getActivity(), e.toString());
						}
						loaddialog.dismiss();

					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(final VolleyError error) {
						Util.Alertdialog(getActivity(), error.toString());
						loaddialog.dismiss();
					}
				}) {

			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				final HashMap<String, String> loginParams = new HashMap<String, String>();
				loginParams.put("auth-token", Util
						.getBase64String(EzApp.sharedPref
								.getString(Constants.USER_TOKEN, "")));
				return loginParams;
			}

			@Override
			protected Map<String, String> getParams() {
				final HashMap<String, String> loginParams = new HashMap<String, String>();
				// certification, institute, datei (date issued) , datee (date
				// expired) , cme
				loginParams.put("type", "add");
				loginParams.put("lang", editLanguage.getText().toString());

				return loginParams;
			}

		};
		schedulePlanRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 5,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		EzApp.mVolleyQueue.add(schedulePlanRequest);
	}

	private void addConsultation() {
		final String url = APIs.UPDATE_CONSULTATION_CHARGES();
		final Dialog loaddialog = Util.showLoadDialog(getActivity());
		final StringRequest schedulePlanRequest = new StringRequest(
				Request.Method.POST, url, new Response.Listener<String>() {
					@Override
					public void onResponse(final String response) {
						Log.i("", response);
						JSONObject jObj;
						try {
							jObj = new JSONObject(response);
							if (jObj.getString("s").equals("200")) {
							//	DashboardActivity.profile.BRANCH_ADDR_AS_PATIENT_ADDR = cbCopyAddress.isChecked();
							} else {
								Util.Alertdialog(getActivity(),
										jObj.getString("m"));
							}
						} catch (Exception e) {
							Util.Alertdialog(getActivity(), e.toString());
						}
						loaddialog.dismiss();

					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(final VolleyError error) {
						Util.Alertdialog(getActivity(), error.toString());
						loaddialog.dismiss();
					}
				}) {

			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				final HashMap<String, String> loginParams = new HashMap<String, String>();
				loginParams.put("auth-token", Util
						.getBase64String(EzApp.sharedPref
								.getString(Constants.USER_TOKEN, "")));
				return loginParams;
			}

			@Override
			protected Map<String, String> getParams() {
				final HashMap<String, String> loginParams = new HashMap<String, String>();
				loginParams.put("initial_visit", editInitialVisit.getText()
						.toString());
				loginParams.put("repeat_visit", editRepeatVisit.getText()
						.toString());
				// if(cbCopyAddress.isChecked())
				// loginParams.put("BRANCH_ADDR_AS_PATIENT_ADDR", "1");
				// else
				// loginParams.put("BRANCH_ADDR_AS_PATIENT_ADDR", "0");
				return loginParams;
			}

		};
		schedulePlanRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 5,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		EzApp.mVolleyQueue.add(schedulePlanRequest);
	}

	private void insuranceValidation(boolean check) {
		Date date = new Date();
		Date issueDate = null;
		Date expiryDate = null;

		try {
			issueDate = EzApp.sdfMmddyy.parse(editDateIssued
					.getText().toString());
			expiryDate = EzApp.sdfMmddyy.parse(editDateExpiry
					.getText().toString());

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Log.i(passedDate.toString(), date.toString());
		if (!Util.isEmptyString(editDateIssued.getText().toString())
				&& !Util.isEmptyString(editDateExpiry.getText().toString())) {
			if (issueDate.before(date) || issueDate.equals(date)) {
				if (issueDate.before(expiryDate)) {
					if (expiryDate.after(date)) {
						if (btnUpload.getText().equals("Choose File"))
							addInsurance();
						else
							addInsuranceWithFile();
					} else if (check) {
						Util.Alertdialog(getActivity(),
								"Please enter correct dates as Expiry Date can be in future only");
					}
				} else if (check) {
					Util.Alertdialog(getActivity(),
							"Please enter correct dates as Expiry Date is before or same as Date Issued");
				}
			} else if (check) {
				Util.Alertdialog(getActivity(), "Please enter correct Dates");
			}
		} else if (check) {
			Util.Alertdialog(getActivity(), "Please enter  dates");
		}

	}

	public void intentReceived(String fileSelected) {
		Log.i("", "intent received");
		btnUpload.setText(fileSelected);

	}

}
