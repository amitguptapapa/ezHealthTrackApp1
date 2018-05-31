package com.ezhealthtrack.physiciansoap;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
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
import com.ezhealthtrack.DentistSoap.AddDentistNotesActivity;
import com.ezhealthtrack.activity.DashboardActivity;
import com.ezhealthtrack.controller.EzController.UpdateListner;
import com.ezhealthtrack.controller.EzNetwork;
import com.ezhealthtrack.controller.OrderSetsController;
import com.ezhealthtrack.dialogs.EzDialog;
import com.ezhealthtrack.greendao.Appointment;
import com.ezhealthtrack.greendao.Patient;
import com.ezhealthtrack.model.Document;
import com.ezhealthtrack.physiciansoap.model.OrderSetItems;
import com.ezhealthtrack.physiciansoap.model.RadiologyModel;
import com.ezhealthtrack.print.PrintRadiologyOrderedActivity;
import com.ezhealthtrack.util.BaseActivity;
import com.ezhealthtrack.util.Constants;
import com.ezhealthtrack.util.Log;
import com.ezhealthtrack.util.UploadDocumentRequest;
import com.ezhealthtrack.util.Util;
import com.google.gson.Gson;
import com.orleonsoft.android.simplefilechooser.ui.FileChooserActivity;

public class RadiologyActivity extends BaseActivity {

	OrderSetsController mController;
	private RadiologyModel radiology;
	private Appointment aptModel;
	public static Patient patientModel;
	private ArrayList<String> arrRadi = new ArrayList<String>();
	private SharedPreferences sharedPref;
	private TextView txtDocumentName;
	private TextView txtDocumentType;
	private TextView txtUploadDate;
	private TextView txtAction;
	private AutoCompleteTextView actvOrderSet;
	private Button btnAddOrderSet;
	private OrderSetItems mSelectedOrderSetItems;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mController = new OrderSetsController();
		sharedPref = getApplicationContext().getSharedPreferences(
				Constants.EZ_SHARED_PREF, Context.MODE_PRIVATE);
		setContentView(R.layout.activity_radiology_phy);
		radiology = PhysicianSoapActivityMain.physicianVisitNotes.physicianPlanModel.radiology;
		patientModel = PhysicianSoapActivityMain.patientModel;
		if (PhysicianSoapActivityMain.patientModel.getPgender()
				.contains("Male")) {
			PhysicianSoapActivityMain.patientModel.setPgender("M");
		} else if (PhysicianSoapActivityMain.patientModel.getPgender()
				.contains("Female")) {
			PhysicianSoapActivityMain.patientModel.setPgender("F");
		}
		((TextView) findViewById(R.id.txt_name))
				.setText(PhysicianSoapActivityMain.patientModel.getPfn() + " "
						+ PhysicianSoapActivityMain.patientModel.getPln()
						+ " , "
						+ PhysicianSoapActivityMain.patientModel.getPage()
						+ "/"
						+ PhysicianSoapActivityMain.patientModel.getPgender());
		final AutoCompleteTextView actv = (AutoCompleteTextView) findViewById(R.id.txt_add_test);
		arrRadi.clear();

		for (com.ezhealthtrack.model.RadiologyModel m : DashboardActivity.arrRadiPreferences) {
			arrRadi.add(m.getTestName());
			// android.util.Log.i("Radio:", m.getTestName());
		}
		final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				R.layout.support_simple_spinner_dropdown_item, arrRadi);
		actv.setAdapter(adapter);
		setData();
		findViewById(R.id.btn_upload).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				uploadDialog();

			}
		});
		findViewById(R.id.btn_add_test).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(final View v) {
						if (!Util.isEmptyString(actv.getText().toString())) {
							if (arrRadi.indexOf(actv.getText().toString()) > 0) {
								radiology.hashRadiology.put(actv.getText()
										.toString(), "on");
								actv.setText("");
								setData();
							} else {
								Util.Alertdialog(RadiologyActivity.this,
										"Invalid Test name");
							}
						} else {
							Util.Alertdialog(RadiologyActivity.this,
									"Search/Select field cannot be empty.");
						}
						actv.setText("");
					}
				});

		actvOrderSet = (AutoCompleteTextView) findViewById(R.id.actv_orderset);
		final Point pointSize1 = new Point();
		getWindowManager().getDefaultDisplay().getSize(pointSize1);
		actvOrderSet.setDropDownWidth(pointSize1.x);

		btnAddOrderSet = (Button) findViewById(R.id.btn_add_orderset);

		actvOrderSet.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(final Editable s) {

			}

			@Override
			public void beforeTextChanged(final CharSequence s,
					final int start, final int count, final int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onTextChanged(final CharSequence s2, final int start,
					final int before, final int count) {
				theCall(s2.toString());
			}
		});

		actvOrderSet.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				mSelectedOrderSetItems = mController
						.getRadiologyOrderSetItems(arg2);
				android.util.Log.v("RA",
						"OSI: " + mSelectedOrderSetItems.toDisplayString());
			}
		});

		btnAddOrderSet.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (mSelectedOrderSetItems == null)
					return;
				for (String key : mSelectedOrderSetItems.items.keySet()) {
					radiology.hashRadiology.put(key, "on");
				}
				mSelectedOrderSetItems = null;
				actvOrderSet.setText("");
				setData();
			}
		});
		radiologyTable();
	}

	void theCall(String name) {
		if (name == null)
			name = "";
		Log.i("APA", "Name :" + name);

		// TEST CODE - TO BE DELETED : START
		Map<String, String> params = new HashMap<String, String>();
		params.put("type", "radiology");
		params.put("limit", "100");
		params.put("name", name);
		params.put(EzNetwork.RAW_RESPONSE_JSONARRAY, "1");
		mController.getOrderSets(params, new UpdateListner() {

			@Override
			public void onDataUpdateFinished(int page) {
				// TODO Auto-generated method stub
				Log.i("OrderSets", "Finished : " + page);
			}

			@Override
			public void onDataUpdateError(int page) {
				// TODO Auto-generated method stub
				Log.i("OrderSets", "Error : " + page);
			}

			@Override
			public void onDataUpdate(int page, List<?> records) {
				// TODO Auto-generated method stub

				// get sets
				for (int i = 0; i < records.size(); ++i) {
					Log.i("OrderSets",
							"Set " + i + " : "
									+ new Gson().toJson(records.get(i)));
				}

				// get names
				// final List<String> names = controller
				// .getOrderSetNames(OrderSetsController.ORDER_SET_PRESCRIPTION);
				// for (int i = 0; i < names.size(); ++i) {
				// Log.i("OrderSets", "Name " + i + " : " + names.get(i));
				//
				// }

				final List<String> names = mController
						.getOrderSetNames(OrderSetsController.ORDER_SET_RADIOLOGY);
				// final ArrayList<String> arrOrderSets = new
				// ArrayList<String>();
				// for (int i = 0; i < names.size(); ++i) {
				// Log.i("APA", "S-Names :" + names.get(i));
				// arrOrderSets.add(names.get(i));
				// }
				final ArrayList<String> arrOrderSets = new ArrayList<String>(
						names);
				// arrOrderSets.addAll(names);

				final ArrayAdapter<String> adapterOrderSets = new ArrayAdapter<String>(
						RadiologyActivity.this,
						android.R.layout.simple_spinner_item, arrOrderSets);
				adapterOrderSets
						.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				actvOrderSet.setAdapter(adapterOrderSets);
			}

			@Override
			public void onCmdResponse(JSONObject response, String result) {
				// TODO Auto-generated method stub
				Log.i("OrderSets", "Response : " + response.toString());
			}
		});
		// TEST CODE : END

	}

	private void setData() {
		((LinearLayout) findViewById(R.id.ll_options)).removeAllViews();
		final LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		for (final Entry<String, String> entry : radiology.hashRadiology
				.entrySet()) {
			final View v = inflater.inflate(R.layout.row_checked_item, null);
			final CheckBox cb = (CheckBox) v.findViewById(R.id.checked_item);
			cb.setText(entry.getKey());
			if (entry.getValue().equals("on")) {
				cb.setChecked(true);
			} else {
				cb.setChecked(false);
			}
			cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(final CompoundButton buttonView,
						final boolean isChecked) {
					if (isChecked) {
						entry.setValue("on");
					} else {
						entry.setValue("off");
					}

				}
			});
			((LinearLayout) findViewById(R.id.ll_options)).addView(v);
		}
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		// Handle presses on the action bar items
		Intent intent;
		switch (item.getItemId()) {

		case android.R.id.home:
			finish();
			return true;

		case R.id.print:
			try {
				intent = new Intent(this, PrintRadiologyOrderedActivity.class);
				intent.putExtra("position",
						getIntent().getIntExtra("position", 0));
				startActivity(intent);
			} catch (Exception e) {

			}
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		// Inflate the menu items for use in the action bar
		final MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.radio_logy, menu);
		return super.onCreateOptionsMenu(menu);
	}

	private Dialog dialog;
	final int FILE_CHOOSER = 1;

	private void uploadDialog() {
		dialog = EzDialog.getDialog(RadiologyActivity.this,
				R.layout.dialog_lab_upload, "Radiology Document upload");
		LinearLayout ll = (LinearLayout) dialog.findViewById(R.id.ll_head_view);
		final ArrayList<String> dt = new ArrayList<String>();
		onPause();
		for (Entry<String, String> s : radiology.hashRadiology.entrySet()) {
			View v = getLayoutInflater().inflate(R.layout.item_upload, null);
			CheckBox cb = (CheckBox) v.findViewById(R.id.cb_upload);
			if (s.getValue().equals("on")) {
				cb.setText(s.getKey());
				ll.addView(v);
			} else if (s.getKey().equals("othe")
					&& !Util.isEmptyString(radiology.hashRadiology.get("othe"))) {
				cb.setText(radiology.hashRadiology.get("othe"));
				ll.addView(v);
			}
			cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView,
						boolean isChecked) {
					if (isChecked)
						dt.add(buttonView.getText().toString());
					else
						dt.remove(buttonView.getText().toString());
				}
			});

		}

		dialog.findViewById(R.id.btn_upload).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(RadiologyActivity.this,
								FileChooserActivity.class);
						startActivityForResult(intent, FILE_CHOOSER);

					}
				});
		dialog.findViewById(R.id.btn_click).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						String dn = ((EditText) dialog
								.findViewById(R.id.edit_document_name))
								.getText().toString();
						if (Util.isEmptyString(dn)) {
							Util.Alertdialog(RadiologyActivity.this,
									"Please enter document name before taking an Image.");
						} else {
							imageDialog();
						}

					}
				});
		dialog.findViewById(R.id.btn_submit).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						String stdt = "";
						for (String st : dt) {
							if (Util.isEmptyString(stdt))
								stdt = st;
							else
								stdt = stdt + "," + st;
						}
						stdt = stdt.replaceAll(" ", "+");
						String dn = ((EditText) dialog
								.findViewById(R.id.edit_document_name))
								.getText().toString();
						File f = new File(((TextView) dialog
								.findViewById(R.id.txt_upload)).getText()
								.toString());
						if (Util.isEmptyString(stdt)) {
							Util.Alertdialog(RadiologyActivity.this,
									"Please select radiology upload type.");
						} else if (Util.isEmptyString(dn)) {
							Util.Alertdialog(RadiologyActivity.this,
									"Please enter document name.");
						} else if (!f.exists()) {
							Util.Alertdialog(RadiologyActivity.this,
									"Please select document file.");
						} else {

							uploadDocument(
									new File(((TextView) dialog
											.findViewById(R.id.txt_upload))
											.getText().toString()), stdt, dn);
						}

					}
				});
		dialog.setCancelable(false);
		dialog.show();
	}

	private void radiologyTable() {
		final TableLayout tl = (TableLayout) findViewById(R.id.table);
		tl.removeAllViews();
		ArrayList<Document> arrRadiDocuments = new ArrayList<Document>();
		for (final Document doc : PhysicianSoapActivityMain.arrDocuments) {
			if (doc.getSection().equalsIgnoreCase("radiology"))
				arrRadiDocuments.add(doc);
		}
		if (arrRadiDocuments.size() > 0) {
			final TableRow row = (TableRow) getLayoutInflater().inflate(
					R.layout.row_document, null, false);
			tl.addView(row);

			for (final Document doc : arrRadiDocuments) {
				final TableRow row1 = (TableRow) getLayoutInflater().inflate(
						R.layout.row_document, null, false);

				txtAction = (TextView) row1.findViewById(R.id.txt_action);
				txtDocumentName = (TextView) row1.findViewById(R.id.txt_name);
				txtDocumentType = (TextView) row1.findViewById(R.id.txt_type);
				txtUploadDate = (TextView) row1
						.findViewById(R.id.txt_upload_date);
				txtAction
						.setText(Html
								.fromHtml("<u><font color='#FF0000'>Delete</font></u>"));
				txtDocumentName.setText(Html.fromHtml("<a href='" + APIs.VIEW()
						+ doc.getDid() + "'>" + doc.getDName() + "</a>"));
				txtDocumentName.setClickable(true);
				txtDocumentName.setMovementMethod(LinkMovementMethod
						.getInstance());
				txtDocumentType.setText(doc.getDType());

				String a;
				try {
					a = EzApp.sdfemmm.format(EzApp.sdfyymmddhhmmss.parse(doc
							.getDate()));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					a = "";
				}
				Date date = new Date();
				String z = EzApp.sdfemmm.format(date);
				if (Util.isEmptyString(txtUploadDate.getText().toString())) {
					txtUploadDate.setText(z);
				} else {
					txtUploadDate.setText(a);
				}
				txtAction.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
								RadiologyActivity.this);

						// set title
						alertDialogBuilder
								.setTitle("Do you want to delete this Document ?");
						// set dialog message
						alertDialogBuilder
								.setCancelable(false)
								.setPositiveButton("Yes",
										new DialogInterface.OnClickListener() {

											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {
												deleteDocument(doc);

											}

										})
								.setNegativeButton("No",
										new DialogInterface.OnClickListener() {
											@Override
											public void onClick(
													final DialogInterface dialog,
													final int id) {
												// if this button is clicked,
												// just close
												// the dialog box and do nothing
												dialog.cancel();
											}
										});

						final AlertDialog alertDialog = alertDialogBuilder
								.create();

						alertDialog.show();
					}
				});
				tl.addView(row1);
			}
			tl.setVisibility(View.VISIBLE);
		} else {
			tl.setVisibility(View.GONE);
		}
	}

	private void deleteDocument(final Document doc) {
		final String url = APIs.DELETE_DOCUMENT() + doc.getId();
		final Dialog loaddialog = Util.showLoadDialog(RadiologyActivity.this);
		final StringRequest patientListRequest = new StringRequest(
				Request.Method.POST, url, new Response.Listener<String>() {
					@Override
					public void onResponse(final String response) {
						try {
							Log.i("delete document", response);
							final JSONObject jObj = new JSONObject(response);
							if (jObj.getString("s").equals("200")) {
								PhysicianSoapActivityMain.arrDocuments
										.remove(doc);
								radiologyTable();

								Util.Alertdialog(RadiologyActivity.this,
										"Document deleted successfully");
							} else {
								Util.Alertdialog(RadiologyActivity.this,
										"There is some error while deleting, please try again.");
							}
						} catch (final JSONException e) {
							Util.Alertdialog(RadiologyActivity.this,
									"There is some error while deleting, please try again.");
							Log.e("", e);
						}
						loaddialog.dismiss();
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(final VolleyError error) {
						Util.Alertdialog(RadiologyActivity.this,
								"Network error, try again later");

						error.printStackTrace();
						loaddialog.dismiss();
					}
				}) {
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				final HashMap<String, String> loginParams = new HashMap<String, String>();
				loginParams.put("auth-token", Util
						.getBase64String(EzApp.sharedPref.getString(
								Constants.USER_TOKEN, "")));
				return loginParams;
			}

			@Override
			protected Map<String, String> getParams() {
				final HashMap<String, String> loginParams = new HashMap<String, String>();
				final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				loginParams.put("format", "json");
				return loginParams;
			}

		};
		patientListRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 5,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		EzApp.mVolleyQueue.add(patientListRequest);

	}

	private void uploadDocument(final File image, final String dt,
			final String dn) {
		Log.i("", dn);
		final Dialog dialog1 = Util.showLoadDialog(this);

		String requestURL = APIs.URL()
				+ "/documents/reportUpload/cli/api?context_type=soap&context_id="
				+ Integer.parseInt(PhysicianSoapActivityMain.Appointment
						.getBkid()) + "&document_type=" + dt
				+ "&section_name=radiology&document_name=" + dn;

		Log.i("", requestURL);
		UploadDocumentRequest request = new UploadDocumentRequest(requestURL,
				new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError e) {
						dialog1.dismiss();
						Util.Alertdialog(RadiologyActivity.this,
								"Network error, try again later");
						Log.e("", e);

					}
				}, new Listener<String>() {

					@Override
					public void onResponse(String arg0) {
						try {
							JSONObject jObj = new JSONObject(arg0);
							Document doc = new Gson().fromJson(jObj
									.getJSONObject("data").toString(),
									Document.class);
							doc.setSection(jObj.getJSONObject("data")
									.getString("section-name"));
							PhysicianSoapActivityMain.arrDocuments.add(doc);
							radiologyTable();
							dialog1.dismiss();
							dialog.dismiss();
							Util.Alertdialog(RadiologyActivity.this,
									"Document uploaded successfully.");
						} catch (JSONException e) {
							dialog1.dismiss();
							Util.Alertdialog(RadiologyActivity.this,
									"There is some error in uploading document, please try again");
							Log.e("", e);
						}
						Log.i("", arg0);

					}
				}, image, "Documents[document]", "Documents[document]") {
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				final HashMap<String, String> headerParams = new HashMap<String, String>();
				headerParams.put("auth-token", Util
						.getBase64String(EzApp.sharedPref.getString(
								Constants.USER_TOKEN, "")));
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
				params.put("context_type", "soap");
				params.put("context_id",
						AddDentistNotesActivity.appointment.getBkid());
				params.put("document_type", "IOPA");
				params.put("section_name", "radiology");
				params.put("document_name", image.getName());
				return params;
			}

		};
		request.setRetryPolicy(new DefaultRetryPolicy(200000, 5,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

		Log.i("", new Gson().toJson(request));

		EzApp.mVolleyQueue.add(request);

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if ((requestCode == FILE_CHOOSER) && (resultCode == RESULT_OK)) {
			String fileSelected = data
					.getStringExtra(com.orleonsoft.android.simplefilechooser.Constants.KEY_FILE_SELECTED);
			try {
				((TextView) dialog.findViewById(R.id.txt_upload))
						.setText(fileSelected);
			} catch (Exception e) {

			}
		} else if ((requestCode == CAMERA_REQUEST) && (resultCode == RESULT_OK)) {
			selectedImageUri = Uri.parse("d");
			if (bitmapImage != null)
				bitmapImage.recycle();
			bitmapImage = decodeFile(image.getPath());
			bitmapImage = Util.getResizedBitmap(bitmapImage, 400, 400);
			// ((ImageView) findViewById(R.id.img_user))
			// .setImageBitmap(bitmapImage);
			String dn = ((EditText) dialog
					.findViewById(R.id.edit_document_name)).getText()
					.toString();
			uploadDocument(image, "Radiology", dn);

		}
	}

	public static Bitmap decodeFile(String path) {// you can provide file path
		// here
		int orientation;
		try {
			if (path == null) {
				return null;
			}
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			// Find the correct scale value. It should be the power of 2.
			final int REQUIRED_SIZE = 70;
			int width_tmp = o.outWidth, height_tmp = o.outHeight;
			int scale = 0;
			while (true) {
				if (width_tmp / 2 < REQUIRED_SIZE
						|| height_tmp / 2 < REQUIRED_SIZE)
					break;
				width_tmp /= 2;
				height_tmp /= 2;
				scale++;
			}
			// decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			// Log.i(TAG, "" + scale);
			o2.inSampleSize = scale;
			o2.inPreferredConfig = Bitmap.Config.ARGB_8888;
			Bitmap bm = BitmapFactory.decodeFile(path, o2);
			Bitmap bitmap = bm;

			ExifInterface exif = new ExifInterface(path);

			orientation = exif
					.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);

			Log.e("ExifInteface .........", "rotation =" + orientation);

			// exif.setAttribute(ExifInterface.ORIENTATION_ROTATE_90, 90);

			Log.e("orientation", "" + orientation);
			Matrix m = new Matrix();

			if ((orientation == ExifInterface.ORIENTATION_ROTATE_180)) {
				m.postRotate(180);
				// m.postScale((float) bm.getWidth(), (float) bm.getHeight());
				// if(m.preRotate(90)){
				Log.e("in orientation", "" + orientation);
				bitmap = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
						bm.getHeight(), m, true);
				return bitmap;
			} else if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
				m.postRotate(90);
				Log.e("in orientation", "" + orientation);
				bitmap = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
						bm.getHeight(), m, true);
				return bitmap;
			} else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
				m.postRotate(270);
				Log.e("in orientation", "" + orientation);
				bitmap = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
						bm.getHeight(), m, true);
				return bitmap;
			}
			return bitmap;
		} catch (Exception e) {
			return null;
		}

	}

	private File image;
	private static final int CAMERA_REQUEST = 1888;
	private Uri selectedImageUri;
	public static Bitmap bitmapImage;

	private void imageDialog() {
		String[] addPhoto;
		addPhoto = new String[] { "Camera" };
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		dialog.setTitle("Select Image for your profile");

		dialog.setItems(addPhoto, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				File imagesFolder = new File(Environment
						.getExternalStorageDirectory(), "MyImages");
				imagesFolder.mkdirs();
				String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
						.format(new Date());
				image = new File(imagesFolder, timeStamp + ".jpg");

				if (id == 0) {
					Intent cameraIntent = new Intent(
							android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
					selectedImageUri = Uri.fromFile(image);
					cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,
							selectedImageUri);
					startActivityForResult(cameraIntent, CAMERA_REQUEST);
					dialog.dismiss();
				}
			}
		});

		dialog.setNeutralButton("Cancel",
				new android.content.DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

						dialog.dismiss();
					}
				});
		dialog.show();
	}
}
