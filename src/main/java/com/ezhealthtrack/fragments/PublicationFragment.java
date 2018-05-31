package com.ezhealthtrack.fragments;

import java.io.File;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DatePickerDialog.OnDateSetListener;
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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;
import com.ezhealthrack.api.APIs;
import com.ezhealthtrack.EzApp;
import com.ezhealthtrack.R;
import com.ezhealthtrack.activity.DashboardActivity;
import com.ezhealthtrack.model.Education;
import com.ezhealthtrack.model.Publication;
import com.ezhealthtrack.util.Constants;
import com.ezhealthtrack.util.UploadDocumentRequest;
import com.ezhealthtrack.util.Util;
import com.orleonsoft.android.simplefilechooser.ui.FileChooserActivity;

public class PublicationFragment extends Fragment {
	private LayoutInflater inflater;
	private LinearLayout llPublication;
	private EditText editName;
	private EditText editDescription;
	private EditText editDate;
	private EditText editPub;
	private TextView txtSource;
	private TextView txtName;
	private TextView txtDescription;
	private TextView txtDate;
	private TextView txtPublished;
	private Button btnUpload;
	public final int FILE_CHOOSER = 2;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.fragment_publications, container,
				false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		inflater = (LayoutInflater) getActivity().getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);
		llPublication = (LinearLayout) getActivity().findViewById(
				R.id.ll_publication);
		editName = (EditText) getActivity().findViewById(R.id.edit_name_desc);
		editDescription = (EditText) getActivity().findViewById(
				R.id.edit_description);
		editDate = (EditText) getActivity().findViewById(R.id.edit_date);
		editPub = (EditText) getActivity().findViewById(R.id.edit_published);
		txtName = (TextView) getActivity().findViewById(R.id.txt_name_desc);
		((TextView) getActivity().findViewById(R.id.txt_name_desc)).append(Html
				.fromHtml("<sup><font color='#EE0000'>*</font></sup>"));
		txtDescription = (TextView) getActivity().findViewById(R.id.txt_descp);
		((TextView) getActivity().findViewById(R.id.txt_descp)).append(Html
				.fromHtml("<sup><font color='#EE0000'>*</font></sup>"));
		txtDate = (TextView) getActivity().findViewById(R.id.txt_date);
		((TextView) getActivity().findViewById(R.id.txt_date)).append(Html
				.fromHtml("<sup><font color='#EE0000'>*</font></sup>"));
		txtPublished = (TextView) getActivity()
				.findViewById(R.id.txt_published);
		((TextView) getActivity().findViewById(R.id.txt_published)).append(Html
				.fromHtml("<sup><font color='#EE0000'>*</font></sup>"));
		// txtSource =(TextView) getActivity().findViewById(R.id.txt_source);

		for (Publication publication : DashboardActivity.profile
				.getPublications()) {
			addPublicationRow(publication);
		}
		getActivity().findViewById(R.id.btn_add_publication)
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {

						if (!Util.isEmptyString(editName.getText().toString())) {
							if (!Util.isEmptyString(editDescription.getText()
									.toString())) {
								if (!Util.isEmptyString(editDate.getText()
										.toString())) {
									if (!Util.isEmptyString(editPub.getText()
											.toString())) {
										publicationValidation(true);
									} else {
										Util.Alertdialog(getActivity(),
												"Please enter Published");
									}
								} else {
									Util.Alertdialog(getActivity(),
											"Please enter Date");
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
		getActivity().findViewById(R.id.button_publication).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						Util.Alertdialog(getActivity(),
								"Profile Updated successfully");

					}
				});

		btnUpload = (Button) getActivity().findViewById(R.id.btn_upload_pub);
		btnUpload.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(),
						FileChooserActivity.class);
				getActivity().startActivityForResult(intent, FILE_CHOOSER);

			}
		});

		Util.datePicker(editDate, getActivity());

	}

	public void onClick(final View v) {
		switch (v.getId()) {

		}

	}

	private void showCalender(final TextView txtView) {
		Calendar cal = Calendar.getInstance();
		DatePickerDialog datepicker = new DatePickerDialog(getActivity(),
				new OnDateSetListener() {

					@Override
					public void onDateSet(DatePicker view, int year,
							int monthOfYear, int dayOfMonth) {
						if (dayOfMonth < 10 && monthOfYear < 9) {
							txtView.setText("0" + ++monthOfYear + "/0"
									+ dayOfMonth + "/" + year);
						} else if (dayOfMonth < 10 && monthOfYear > 8) {
							txtView.setText(++monthOfYear + "/0" + dayOfMonth
									+ "/" + year);
						} else if (dayOfMonth > 9 && monthOfYear < 9) {
							txtView.setText("0" + ++monthOfYear + "/"
									+ dayOfMonth + "/" + year);
						} else {
							txtView.setText(++monthOfYear + "/" + dayOfMonth
									+ "/" + year);
						}

					}
				}, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
				cal.get(Calendar.DAY_OF_MONTH));
		datepicker.show();
	}

	private void addPublicationRow(final Publication publication) {
		final View v = inflater.inflate(R.layout.row_publication, null);
		llPublication.addView(v);
		((TextView) v.findViewById(R.id.txt_name_desc)).setText(publication
				.getPub_name());
		((TextView) v.findViewById(R.id.txt_desc)).setText(publication
				.getPub_desc());

		try {
			((TextView) v.findViewById(R.id.txt_date))
					.setText(EzApp.sdfMmddyy
							.format(EzApp.sdfyyMmdd
									.parse(publication.getPub_date())));
		} catch (ParseException e) {
			((TextView) v.findViewById(R.id.txt_date)).setText(publication
					.getPub_date());
		}
		((TextView) v.findViewById(R.id.txt_published)).setText(publication
				.getPub_published());
		TextView txtSource = ((TextView) v.findViewById(R.id.txt_source));
		ImageView imgRead = (ImageView) v.findViewById(R.id.img_read);
		if (Util.isEmptyString(publication.getSource())) {
			txtSource.setText("Document not available!");
			imgRead.setVisibility(View.GONE);
			txtSource.setVisibility(View.VISIBLE);
		} else {
			imgRead.setVisibility(View.VISIBLE);
			txtSource.setVisibility(View.GONE);
		}

		imgRead.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String url = APIs.VIEW() + publication.getSource();
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setData(Uri.parse(url));
				startActivity(i);

			}
		});

		v.findViewById(R.id.btn_rem_publication).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View a) {
						deletePublication(publication, v);

					}
				});
	}

	private void deletePublication(final Publication publication, final View v) {
		final String url = APIs.PUBLICATION_ADD_DELETE();
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
								DashboardActivity.profile.getPublications()
										.remove(publication);
								llPublication.removeView(v);
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
				loginParams.put("pid", publication.getPid());
				loginParams.put("type", "remove");
				return loginParams;
			}

		};
		schedulePlanRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 5,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		EzApp.mVolleyQueue.add(schedulePlanRequest);

	}

	private void addPublicationWithFile() {
		final Dialog loaddialog = Util.showLoadDialog(getActivity());
		File file = new File(btnUpload.getText().toString());
		String requestURL = APIs.PUBLICATION_ADD_DELETE()
				+ "/cli/api/format/json?type=add&pub_name="
				+ editName.getText().toString() + "&pub_desc="
				+ editDescription.getText().toString() + "&pub_date="
				+ editDate.getText().toString() + "&pub_published="
				+ editPub.getText().toString();

		UploadDocumentRequest request = new UploadDocumentRequest(requestURL,
				new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError e) {
						loaddialog.dismiss();
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
								Publication publication = new Publication();
								publication.setPid(jObj.getString("pid"));
								publication.setPub_name(editName.getText()
										.toString());
								publication.setPub_desc(editDescription
										.getText().toString());
								publication.setPub_date(editDate.getText()
										.toString());
								publication.setPub_published(editPub.getText()
										.toString());
								publication.setSource(jObj.getString("doc_url"));

								DashboardActivity.profile.getPublications()
										.add(publication);
								addPublicationRow(publication);
								;
								editDate.setText("");
								editName.setText("");
								editPub.setText("");
								// txtSource.setText("");
								editDescription.setText("");

							} else {
								Util.Alertdialog(getActivity(),
										jObj.getString("m"));
							}
						} catch (Exception e) {
							Util.Alertdialog(getActivity(), e.toString());
						}
						loaddialog.dismiss();

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
			protected Map<String, String> getParams() {
				final HashMap<String, String> loginParams = new HashMap<String, String>();
				// certification, institute, datei (date issued) , datee (date
				// expired) , cme
				loginParams.put("type", "add");
				loginParams.put("pub_name", editName.getText().toString());
				loginParams.put("pub_desc", editDescription.getText()
						.toString());
				loginParams.put("pub_date", editDate.getText().toString());
				loginParams.put("pub_published", editPub.getText().toString());
				// loginParams.put("pub_source",
				// txtSource.getText().toString());

				return loginParams;
			}

		};
		request.setRetryPolicy(new DefaultRetryPolicy(200000, 5,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

		EzApp.mVolleyQueue.add(request);
	}

	private void addPublication() {
		final String url = APIs.PUBLICATION_ADD_DELETE();
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
								Publication publication = new Publication();
								publication.setPid(jObj.getString("pid"));
								publication.setPub_name(editName.getText()
										.toString());
								publication.setPub_desc(editDescription
										.getText().toString());
								publication.setPub_date(editDate.getText()
										.toString());
								publication.setPub_published(editPub.getText()
										.toString());
								// publication.setSource(txtSource.getText().toString());

								DashboardActivity.profile.getPublications()
										.add(publication);
								addPublicationRow(publication);
								;
								editDate.setText("");
								editName.setText("");
								editPub.setText("");
								// txtSource.setText("");
								editDescription.setText("");

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
				loginParams.put("pub_name", editName.getText().toString());
				loginParams.put("pub_desc", editDescription.getText()
						.toString());
				loginParams.put("pub_date", editDate.getText().toString());
				loginParams.put("pub_published", editPub.getText().toString());
				// loginParams.put("pub_source",
				// txtSource.getText().toString());

				return loginParams;
			}

		};
		schedulePlanRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 5,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		EzApp.mVolleyQueue.add(schedulePlanRequest);
	}

	private void publicationValidation(boolean check) {
		Date date = new Date();
		Date publishedDate = null;

		try {
			publishedDate = EzApp.sdfMmddyy.parse(editDate
					.getText().toString());

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Log.i(passedDate.toString(), date.toString());
		if (!Util.isEmptyString(editDate.getText().toString())) {
			if (publishedDate.before(date)) {
				Publication publication = new Publication();
				publication.setPub_date(editDate.getText().toString());
				if (btnUpload.getText().equals("Choose File"))
					addPublication();
				else {
					addPublicationWithFile();
				}

			} else if (check) {
				Util.Alertdialog(getActivity(),
						"Please enter correct Published Date as Published date cannot be in future");
			}
		} else if (check) {
			Util.Alertdialog(getActivity(), "Please enter start and end dates");
		}

	}

	public void intentReceived(String fileSelected) {
		Log.i("", "intent received");
		btnUpload.setText(fileSelected);

	}

}
