package com.ezhealthtrack.DentistSoap;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.print.PrintHelper;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
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
import com.ezhealthtrack.DentistSoap.Model.RadiologyModel;
import com.ezhealthtrack.activity.DashboardActivity;
import com.ezhealthtrack.activity.EditAccountActivity;
import com.ezhealthtrack.adapter.CheckedListAdapter;
import com.ezhealthtrack.controller.PatientController;
import com.ezhealthtrack.greendao.Appointment;
import com.ezhealthtrack.labs.controller.OrderController.OnResponseData;
import com.ezhealthtrack.model.Document;
import com.ezhealthtrack.model.PatientShow;
import com.ezhealthtrack.util.BaseActivity;
import com.ezhealthtrack.util.Constants;
import com.ezhealthtrack.util.Log;
import com.ezhealthtrack.util.UploadDocumentRequest;
import com.ezhealthtrack.util.Util;
import com.ezhealthtrack.views.EditUtils;
import com.google.gson.Gson;
import com.orleonsoft.android.simplefilechooser.ui.FileChooserActivity;

public class DentistRadiologyActivity extends BaseActivity {
	private Appointment aptModel;
	private RadiologyModel radiology;
	private SharedPreferences sharedPref;
	private ListView listRadiology;
	private final ArrayList<String> arrRadiology = new ArrayList<String>();
	private ArrayList<String> arrSelected;
	private CheckedListAdapter adapterLabOrder;
	private EditText editAddOrder;
	private CheckBox cbIopa;
	private Button btnSubmit;
	private TextView txtDocumentName;
	private TextView txtDocumentType;
	private TextView txtUploadDate;
	private TextView txtAction;

	// private final ArrayList<Document> arrDocuments = new
	// ArrayList<Document>();

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_lab_order);
		aptModel = AddDentistNotesActivity.appointment;
		radiology = AddDentistNotesActivity.visitNotes.dentistPlanModel.radiology;
		listRadiology = (ListView) findViewById(R.id.list_lab_order);
		arrRadiology.add("OPG");
		arrRadiology.add("Lateral Cephalogram");
		arrRadiology.add("CT Scan");
		arrRadiology.add("MRI");
		arrSelected = new ArrayList<String>();
		try {
			if (radiology.hashRadiology.get("opg").equals("on")) {
				arrSelected.add("OPG");
			}
			if (radiology.hashRadiology.get("lc").equals("on")) {
				arrSelected.add("Lateral Cephalogram");
			}
			if (radiology.hashRadiology.get("cts").equals("on")) {
				arrSelected.add("CT Scan");
			}
			if (radiology.hashRadiology.get("mri").equals("on")) {
				arrSelected.add("MRI");
			}

		} catch (final Exception e) {
			// TODO: handle exception
		}
		adapterLabOrder = new CheckedListAdapter(this,
				R.layout.row_checked_item, arrRadiology, arrSelected);
		final View v = getLayoutInflater().inflate(R.layout.header_lab_order,
				null);
		listRadiology.addHeaderView(v);
		final View v1 = getLayoutInflater().inflate(R.layout.footer_lab_order,
				null);
		listRadiology.addFooterView(v1);
		listRadiology.setAdapter(adapterLabOrder);
		try {
			for (PatientShow p : DashboardActivity.arrPatientShow) {
				if (p.getPId().equalsIgnoreCase(aptModel.getPid())
						&& p.getPfId().equalsIgnoreCase(aptModel.getPfId()))
					((TextView) findViewById(R.id.txt_name)).setText(p.getPfn()
							+ " " + p.getPln() + " , " + p.getAge() + "/"
							+ p.getGender());
			}
			final SimpleDateFormat sdf = new SimpleDateFormat(
					"EEE, MMM dd, yyyy");
			final Date date = aptModel.aptDate;
			((TextView) findViewById(R.id.txt_date)).setText(sdf.format(date));
		} catch (Exception e) {
			e.printStackTrace();
		}
		editAddOrder = (EditText) findViewById(R.id.edit_add_other);
		cbIopa = (CheckBox) findViewById(R.id.cb_iopa);
		btnSubmit = (Button) findViewById(R.id.btn_submit);
		btnSubmit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(final View v) {
				final Dialog loaddialog = Util
						.showLoadDialog(DentistRadiologyActivity.this);
				Util.AlertdialogWithFinish(DentistRadiologyActivity.this,
						"Radiology added successfully");
				loaddialog.dismiss();
			}
		});

		findViewById(R.id.btn_upload).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				uploadDialog();

			}
		});

		if (radiology.hashRadiology.containsKey("iopa")
				&& radiology.hashRadiology.get("iopa").equals("on")) {
			cbIopa.setChecked(true);
		}
		cbIopa.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(final CompoundButton buttonView,
					final boolean isChecked) {
				if (isChecked) {
					radiology.hashRadiology.put("iopa", "on");
				} else {
					radiology.hashRadiology.put("iopa", "gone");
				}
			}
		});
		EditUtils.autoSaveEditText(
				(EditText) findViewById(R.id.edit_add_other),
				radiology.hashRadiology);
		EditUtils.autoSaveEditText((EditText) findViewById(R.id.edit_iopa),
				radiology.hashRadiology);

		radiologyTable();
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {

		case android.R.id.home:
			finish();
			return true;

		case R.id.print:
			if (arrSelected.contains("OPG")) {
				radiology.hashRadiology.put("opg", "on");
			} else {
				radiology.hashRadiology.put("opg", "off");
			}
			if (arrSelected.contains("Lateral Cephalogram")) {
				radiology.hashRadiology.put("lc", "on");
			} else {
				radiology.hashRadiology.put("lc", "off");
			}
			if (arrSelected.contains("CT Scan")) {
				radiology.hashRadiology.put("cts", "on");
			} else {
				radiology.hashRadiology.put("cts", "off");
			}
			if (arrSelected.contains("MRI")) {
				radiology.hashRadiology.put("mri", "on");
			} else {
				radiology.hashRadiology.put("mri", "off");
			}

			dialogPrintRadiology();
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

	private void dialogPrintRadiology() {
		final Dialog dialogPrintRadiology = new Dialog(this,
				R.style.Theme_AppCompat_Light);
		dialogPrintRadiology.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialogPrintRadiology.setContentView(R.layout.dialog_print_radiology);
		dialogPrintRadiology.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		final SimpleDateFormat df = new SimpleDateFormat(" EEE, MMM dd, yyyy");
		final Date date = AddDentistNotesActivity.appointment.aptDate;
		String drName = EzApp.sharedPref.getString(
				Constants.DR_NAME, "");
		((TextView) dialogPrintRadiology.findViewById(R.id.txt_drname))
				.setText(drName);
		String drAddress = EzApp.sharedPref.getString(
				Constants.DR_ADDRESS, "");
		((TextView) dialogPrintRadiology.findViewById(R.id.txt_doc_address))
				.setText(drAddress + " - "
						+ EditAccountActivity.profile.getZip());
		((TextView) dialogPrintRadiology.findViewById(R.id.txt_ptname))
				.setText("Patient Name: "
						+ AddDentistNotesActivity.patientModel.getPfn() + " "
						+ AddDentistNotesActivity.patientModel.getPln());
		((TextView) dialogPrintRadiology.findViewById(R.id.txt_gender))
				.setText("Gender: "
						+ AddDentistNotesActivity.patientModel.getPgender());

		((TextView) dialogPrintRadiology.findViewById(R.id.txt_age))
				.setText("Age: "
						+ AddDentistNotesActivity.patientModel.getPage());
		((TextView) dialogPrintRadiology.findViewById(R.id.txt_date))
				.setText("Date: " + df.format(date));
		((TextView) dialogPrintRadiology.findViewById(R.id.txt_ptaddress))
				.setText("Address: "
						+ AddDentistNotesActivity.patientModel.getPadd1() + " "
						+ AddDentistNotesActivity.patientModel.getPadd2()
						+ ", "
						+ AddDentistNotesActivity.patientModel.getParea()
						+ ", "
						+ AddDentistNotesActivity.patientModel.getPcity()
						+ ", "
						+ AddDentistNotesActivity.patientModel.getPstate()
						+ ", "
						+ AddDentistNotesActivity.patientModel.getPcountry()
						+ " - "
						+ AddDentistNotesActivity.patientModel.getPzip());
		((TextView) dialogPrintRadiology
				.findViewById(R.id.txt_radiology_orderd))
				.setText(Html
						.fromHtml("<b>Radiology Ordered:</b> "
								+ AddDentistNotesActivity.visitModel.dentistPlanModel.radiology
										.getLabString()));

		PatientController.patientBarcode(DentistRadiologyActivity.this,
				new OnResponseData() {

					@Override
					public void onResponseListner(Object response) {

						Util.getImageFromUrl(response.toString(),
								DashboardActivity.imgLoader,
								(ImageView) dialogPrintRadiology
										.findViewById(R.id.img_barcode));
					}
				}, AddDentistNotesActivity.patientModel);

		if (!Util.isEmptyString(EzApp.sharedPref.getString(
				Constants.SIGNATURE, "signature"))) {
			Util.getImageFromUrl(EzApp.sharedPref.getString(
					Constants.SIGNATURE, ""), DashboardActivity.imgLoader,
					(ImageView) dialogPrintRadiology
							.findViewById(R.id.img_lab_signature));
		}
		dialogPrintRadiology.setCancelable(false);
		dialogPrintRadiology.findViewById(R.id.txt_close).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {

						dialogPrintRadiology.dismiss();

					}
				});
		dialogPrintRadiology.findViewById(R.id.btn_print_radiology)
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						PrintHelper photoPrinter = new PrintHelper(
								DentistRadiologyActivity.this);

						photoPrinter.setScaleMode(PrintHelper.SCALE_MODE_FIT);
						View view = (dialogPrintRadiology
								.findViewById(R.id.rl_top));
						view.setDrawingCacheEnabled(true);
						view.buildDrawingCache();
						Bitmap bm = view.getDrawingCache();
						// Bitmap bitmap =
						// BitmapFactory.decodeResource(getResources(),
						// R.drawable.rs_15);

						photoPrinter.printBitmap("Radiology.jpg", bm);

						// dialogPrintRadiology.dismiss();

					}
				});
		dialogPrintRadiology.show();

	}

	@Override
	protected void onPause() {
		if (arrSelected.contains("OPG")) {
			radiology.hashRadiology.put("opg", "on");
		} else {
			radiology.hashRadiology.put("opg", "off");
		}
		if (arrSelected.contains("Lateral Cephalogram")) {
			radiology.hashRadiology.put("lc", "on");
		} else {
			radiology.hashRadiology.put("lc", "off");
		}
		if (arrSelected.contains("CT Scan")) {
			radiology.hashRadiology.put("cts", "on");
		} else {
			radiology.hashRadiology.put("cts", "off");
		}
		if (arrSelected.contains("MRI")) {
			radiology.hashRadiology.put("mri", "on");
		} else {
			radiology.hashRadiology.put("mri", "off");
		}

		super.onPause();
	}

	final int FILE_CHOOSER = 1;

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

	private Dialog dialog;

	private void uploadDialog() {
		dialog = new Dialog(this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_radiology_upload);
		dialog.setCancelable(false);
		LinearLayout ll = (LinearLayout) dialog.findViewById(R.id.ll_head_view);
		final ArrayList<String> dt = new ArrayList<String>();
		onPause();
		for (Entry<String, String> s : radiology.hashRadiology.entrySet()) {
			View v = getLayoutInflater().inflate(R.layout.item_upload, null);
			CheckBox cb = (CheckBox) v.findViewById(R.id.cb_upload);
			if (s.getValue().equals("on")) {
				if (s.getKey().equals("iopa")) {
					if (Util.isEmptyString(radiology.hashRadiology.get("value"))) {
						cb.setText("IOPA");
					} else {
						cb.setText("IOPA ("
								+ radiology.hashRadiology.get("value") + ")");
					}
				} else if (s.getKey().equals("lc"))
					cb.setText("Lateral cephalogram");
				else if (s.getKey().equals("opg"))
					cb.setText("OPG");
				else if (s.getKey().equals("cts"))
					cb.setText("CT Scan");
				else if (s.getKey().equals("mri"))
					cb.setText("MRI");
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
		dialog.findViewById(R.id.txt_close).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				});
		dialog.findViewById(R.id.btn_upload).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(
								DentistRadiologyActivity.this,
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
							Util.Alertdialog(DentistRadiologyActivity.this,
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
							Util.Alertdialog(DentistRadiologyActivity.this,
									"Please select radiology upload type.");
						} else if (Util.isEmptyString(dn)) {
							Util.Alertdialog(DentistRadiologyActivity.this,
									"Please enter document name.");
						} else if (!f.exists()) {
							Util.Alertdialog(DentistRadiologyActivity.this,
									"Please select document file.");
						} else {

							uploadDocument(
									new File(((TextView) dialog
											.findViewById(R.id.txt_upload))
											.getText().toString()), stdt, dn);
						}

					}
				});

		dialog.show();
	}

	private void radiologyTable() {
		final TableLayout tl = (TableLayout) findViewById(R.id.table);
		tl.removeAllViews();
		ArrayList<Document> arrRadiDocuments = new ArrayList<Document>();
		for (final Document doc : AddDentistNotesActivity.arrDocuments) {
			if (doc.getSection().equalsIgnoreCase("radiology"))
				arrRadiDocuments.add(doc);
		}
		if (arrRadiDocuments.size() > 0) {
			final LayoutParams params = new LayoutParams(
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
			final TableRow row = (TableRow) getLayoutInflater().inflate(
					R.layout.row_document, null, false);
			row.setLayoutParams(params);
			txtAction = (TextView) row.findViewById(R.id.txt_action);
			txtDocumentName = (TextView) row.findViewById(R.id.txt_name);
			txtDocumentType = (TextView) row.findViewById(R.id.txt_type);
			txtUploadDate = (TextView) row.findViewById(R.id.txt_upload_date);
			txtAction.setText(Html.fromHtml("<b>Action</b>"));
			txtDocumentName.setText(Html.fromHtml("<b>Document Name</b>"));
			txtDocumentType.setText(Html.fromHtml("<b>Document Type</b>"));
			txtUploadDate.setText(Html.fromHtml("<b>Upload Date</b>"));
			tl.addView(row);
			for (final Document doc : arrRadiDocuments) {
				final LayoutParams params1 = new LayoutParams(
						android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
						android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
				final TableRow row1 = (TableRow) getLayoutInflater().inflate(
						R.layout.row_document, null, false);
				row1.setLayoutParams(params1);
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
					a = EzApp.sdfemmm
							.format(EzApp.sdfyymmddhhmmss
									.parse(doc.getDate()));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					a = "";
				}
				txtUploadDate.setText(a);
				txtAction.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
								DentistRadiologyActivity.this);

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
		final Dialog loaddialog = Util
				.showLoadDialog(DentistRadiologyActivity.this);
		final StringRequest patientListRequest = new StringRequest(
				Request.Method.POST, url, new Response.Listener<String>() {
					@Override
					public void onResponse(final String response) {
						try {
							Log.i("delete document", response);
							final JSONObject jObj = new JSONObject(response);
							if (jObj.getString("s").equals("200")) {
								AddDentistNotesActivity.arrDocuments
										.remove(doc);
								radiologyTable();
								// labTable();

								Util.Alertdialog(DentistRadiologyActivity.this,
										"Document deleted successfully");
							} else {
								Util.Alertdialog(DentistRadiologyActivity.this,
										"There is some error while deleting, please try again.");
							}
						} catch (final JSONException e) {
							Util.Alertdialog(DentistRadiologyActivity.this,
									"There is some error while deleting, please try again.");
							Log.e("", e);
						}
						loaddialog.dismiss();
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(final VolleyError error) {
						Util.Alertdialog(DentistRadiologyActivity.this,
								"Network error, try again later");

						error.printStackTrace();
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
				+ Integer.parseInt(AddDentistNotesActivity.appointment
						.getBkid()) + "&document_type=" + dt
				+ "&section_name=radiology&document_name=" + dn;

		Log.i("", requestURL);
		UploadDocumentRequest request = new UploadDocumentRequest(requestURL,
				new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError e) {
						dialog1.dismiss();
						Util.Alertdialog(DentistRadiologyActivity.this,
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
							doc.setDate(EzApp.sdfyymmddhhmmss
									.format(new Date()));
							AddDentistNotesActivity.arrDocuments.add(doc);
							radiologyTable();
							dialog1.dismiss();
							dialog.dismiss();
							Util.Alertdialog(DentistRadiologyActivity.this,
									"Document uploaded successfully.");
						} catch (JSONException e) {
							dialog1.dismiss();
							Util.Alertdialog(DentistRadiologyActivity.this,
									"There is some error in uploading document, please try again");
							Log.e("", e);
						}
						Log.i("", arg0);

					}
				}, image, dt, "Documents[document]") {
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				final HashMap<String, String> headerParams = new HashMap<String, String>();
				headerParams.put("auth-token", Util
						.getBase64String(EzApp.sharedPref
								.getString(Constants.USER_TOKEN, "")));
				headerParams.put("Accept-Encoding", "deflate");
				headerParams.put("Accept", "*/*");
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
				params.put("document_type", dt);
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
