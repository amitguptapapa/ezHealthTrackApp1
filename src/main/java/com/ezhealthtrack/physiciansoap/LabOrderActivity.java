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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Point;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
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
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.ezhealthtrack.activity.LabOrderDetailsActivity;
import com.ezhealthtrack.controller.AutoSuggestController;
import com.ezhealthtrack.controller.AutoSuggestController.OnAutoSuggest;
import com.ezhealthtrack.controller.EzController.UpdateListner;
import com.ezhealthtrack.controller.EzNetwork;
import com.ezhealthtrack.controller.LabOrderController;
import com.ezhealthtrack.controller.OrderSetsController;
import com.ezhealthtrack.dialogs.EzDialog;
import com.ezhealthtrack.greendao.Appointment;
import com.ezhealthtrack.labs.model.LabTestAutoSuggest;
import com.ezhealthtrack.model.Document;
import com.ezhealthtrack.model.LabOrder;
import com.ezhealthtrack.model.laborder.LabInfo;
import com.ezhealthtrack.model.laborder.LabOrderDetails;
import com.ezhealthtrack.model.laborder.SOAPLabs;
import com.ezhealthtrack.one.EzUtils;
import com.ezhealthtrack.physiciansoap.model.LabModel;
import com.ezhealthtrack.physiciansoap.model.OrderSetItems;
import com.ezhealthtrack.print.PrintLabOrderedActivity;
import com.ezhealthtrack.util.BaseActivity;
import com.ezhealthtrack.util.Constants;
import com.ezhealthtrack.util.UploadDocumentRequest;
import com.ezhealthtrack.util.Util;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.orleonsoft.android.simplefilechooser.ui.FileChooserActivity;

public class LabOrderActivity extends BaseActivity {

	final static public String TEST_ID_SPLIT_KEY = "__";
	OrderSetsController mOrderSetController;
	LabOrderController mController;
	private LabModel lab;
	private TextView txtDocumentName;
	private TextView txtDocumentType;
	private TextView txtUploadDate;
	private TextView txtAction;
	private AutoCompleteTextView actvOrderSet;
	private Button btnAddOrderSet;
	private ProgressBar mProgressBar;
	private RadioGroup mLabsView;

	private ArrayAdapter<LabTestAutoSuggest> mLabTestAdapter;
	private AutoCompleteTextView mAddTestFilter;

	private OrderSetItems mSelectedOrderSetItems;
	private LabTestAutoSuggest mSelectedLabTest;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mController = new LabOrderController(0);
		mOrderSetController = new OrderSetsController();
		setContentView(R.layout.activity_lab_phy);
		mProgressBar = (ProgressBar) findViewById(R.id.id_progressbar);
		mLabsView = ((RadioGroup) findViewById(R.id.lab_options));

		lab = PhysicianSoapActivityMain.physicianVisitNotes.physicianPlanModel.lab;
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

		mAddTestFilter = (AutoCompleteTextView) findViewById(R.id.actv_add_test);

		mLabTestAdapter = new ArrayAdapter<LabTestAutoSuggest>(this,
				android.R.layout.simple_dropdown_item_1line);
		mAddTestFilter.setAdapter(mLabTestAdapter);
		mAddTestFilter.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				mSelectedLabTest = mLabTestAdapter.getItem(arg2);
			}
		});

		mAddTestFilter.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(final Editable arg0) {
				// Log.i("LabFragment:", "afterTextChanged");
			}

			@Override
			public void beforeTextChanged(final CharSequence arg0,
					final int arg1, final int arg2, final int arg3) {
				// Log.i("LabFragment:", "beforeTextChanged");
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

				AutoSuggestController.autoSuggestLabTest(s.toString(),
						LabOrderActivity.this, new OnAutoSuggest() {

							@Override
							public void OnAutoSuggestListner(Object list) {
								ArrayList<LabTestAutoSuggest> arr = (ArrayList<LabTestAutoSuggest>) list;
								mLabTestAdapter.clear();
								mLabTestAdapter.addAll(arr);
								mLabTestAdapter.notifyDataSetChanged();
							}
						});
			}

		});

		refreshTestsAndLabs();
		refreshLabOrders();
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
						if (!Util.isEmptyString(mAddTestFilter.getText()
								.toString())) {
							if (mSelectedLabTest != null
									&& mSelectedLabTest.getName()
											.equals(mAddTestFilter.getText()
													.toString())) {
								String key = mSelectedLabTest.getName()
										+ TEST_ID_SPLIT_KEY
										+ mSelectedLabTest.getId();
								lab.hashLab.put(key, "off");
								// Log.i("", "Adding Key: " + key);
								refreshTestsAndLabs();
								mSelectedLabTest = null;
								mAddTestFilter.setText("");
							} else {
								Util.Alertdialog(LabOrderActivity.this,
										"Invalid Test name");
							}
						} else {
							Util.Alertdialog(LabOrderActivity.this,
									"Search/Select field cannot be empty.");
						}

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
				// EzUtils.showLong("" + arg2);
				mSelectedOrderSetItems = mOrderSetController
						.getLabOrderSetItems(arg2);
				// android.util.Log.v("LOA",
				// "OSI: " + mSelectedOrderSetItems.toDisplayString());
				if (mSelectedOrderSetItems == null) {
					Log.i("APA", "SP is NULL");
					EzUtils.showLong("APA: SP is NULL");
				} else {
					EzUtils.showLong("APA: SP is NOT NULL");
				}
			}
		});

		btnAddOrderSet.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (mSelectedOrderSetItems == null)
					return;
				for (String key : mSelectedOrderSetItems.items.keySet()) {
					lab.hashLab.put(key, "on");
				}
				mSelectedOrderSetItems = null;
				actvOrderSet.setText("");
				refreshTestsAndLabs();
			}
		});

		labTable();
	}

	void theCall(String name) {
		if (name == null)
			name = "";
		Log.i("APA", "Name :" + name);

		// TEST CODE - TO BE DELETED : START
		Map<String, String> params = new HashMap<String, String>();
		params.put("type", "lab_tests");
		params.put("limit", "100");
		params.put("name", name);
		params.put(EzNetwork.RAW_RESPONSE_JSONARRAY, "1");
		mOrderSetController.getOrderSets(params, new UpdateListner() {

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

				final List<String> names = mOrderSetController
						.getOrderSetNames(OrderSetsController.ORDER_SET_LAB);
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
						LabOrderActivity.this,
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

	static LabInfo mSelectedLab;
	static JSONArray mSelectedTestIds;
	static List<LabInfo> mConnectedLabs;

	// generate order
	public void onGenerateOrder(View view) {

		JSONObject data = new JSONObject();
		JSONArray labTests = new JSONArray();
		for (final Entry<String, String> item : lab.hashLab.entrySet()) {
			Log.i("LOA:onGO()",
					"Key: " + item.getKey() + ", Value: " + item.getValue());
			String[] pair = item.getKey().split(TEST_ID_SPLIT_KEY);
			String name = item.getKey().replace(
					TEST_ID_SPLIT_KEY + pair[pair.length - 1], "");
			if (pair.length < 2) {
				Log.e("LOA:onGO()", "Bad value : " + item.getKey());
				continue;
			}
			if (item.getValue().equals("on")) {
				int id = Integer.parseInt(pair[pair.length - 1]);
				JSONObject labTest = new JSONObject();
				try {
					labTest.put("product_id", id);
					labTest.put("product_name", name);
					labTest.put("product_quantity", "1");
					labTest.put("product_type", "lab_test");
					labTests.put(labTest);
				} catch (JSONException e) {
					EzUtils.showLong("Order can't be created - "
							+ e.getMessage());
					return;
				}
			}

		}

		if (labTests.length() < 1) {
			EzUtils.showLong("Please select Lab Test..");
			return;
		}

		if (mSelectedLab == null) {
			EzUtils.showLong("Please select Connected Lab..");
			return;
		}

		try {
			// set parameters
			Appointment aptmt = PhysicianSoapActivityMain.Appointment;
			data.put("patient_id", aptmt.getPid());
			data.put("source_appointment_id", aptmt.getBkid());
			data.put("source_episode_id", aptmt.getEpid());

			data.put("order_type", "doctor_lab_direct_order");
			data.put("branch_id", mSelectedLab.getBranchId());
			data.put("tenant_id", mSelectedLab.getTenantId());
			data.put("source_items", labTests);

			EditText ev = ((EditText) findViewById(R.id.edit_note));
			data.put("source_order_notes", ev.getText().toString().trim());
		} catch (JSONException e) {
			EzUtils.showLong("Order can't be created : " + e.getMessage());
			return;
		}

		EzNetwork network = new EzNetwork();
		String url = APIs.LAB_TEST_ORDER_CREATE();
		Map<String, String> params = new HashMap<String, String>();
		params.put("LabOrderDetail", data.toString());

		final Button btnOrder = (Button) findViewById(R.id.btn_order);
		final ProgressBar btnOrderProgress = (ProgressBar) findViewById(R.id.btn_order_progressbar);

		btnOrder.setVisibility(View.GONE);
		btnOrderProgress.setVisibility(View.VISIBLE);

		network.POST(url, params, new EzNetwork.ResponseHandler() {

			@Override
			public void cmdResponseError(Integer code) {
				btnOrder.setVisibility(View.VISIBLE);
				btnOrderProgress.setVisibility(View.GONE);
				EzUtils.showLong("Order can't be generated");
			}

			@Override
			public void cmdResponse(JSONObject response, String result) {
				LabOrderDetails order;

				btnOrder.setVisibility(View.VISIBLE);
				btnOrderProgress.setVisibility(View.GONE);

				Log.i("LOD-A:::", "" + response.toString());
				try {
					order = new Gson().fromJson(response.toString(),
							LabOrderDetails.class);
				} catch (JsonSyntaxException e) {
					EzUtils.showLong("Bad response. Please retry..");
					// TODO: refresh page
					return;
				}

				// add new order to SOAP
				PhysicianSoapActivityMain.mSOAPLabs.getLabOrders().add(
						order.getData().getLabOrder());

				refreshLabOrders();
			}

		});
	}

	private void refreshTestsAndLabs() {

		// reinitialize selected lab ids
		mSelectedTestIds = new JSONArray();

		// refresh lab test
		this.refreshLabTests();
		this.reloadConnectedLabs();

	}

	private void reloadConnectedLabs() {
		if (mConnectedLabs == null)
			mConnectedLabs = new ArrayList<LabInfo>();

		Map<String, String> params = new HashMap<String, String>();

		// set list of available labs
		String availableLabs = new Gson()
				.toJson(PhysicianSoapActivityMain.mSOAPLabs.getAvailableLabs());
		params.put("labs", availableLabs);
		params.put("lab_tests", mSelectedTestIds.toString());
		params.put(EzNetwork.RAW_RESPONSE_JSONARRAY, "1");

		LabOrderActivity.mConnectedLabs.clear();
		mProgressBar.setVisibility(View.VISIBLE);
		mLabsView.setVisibility(View.GONE);
		mController.getConnectedLabs(params, new UpdateListner() {

			@Override
			public void onDataUpdateFinished(int page) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onDataUpdateError(int page) {
				mProgressBar.setVisibility(View.GONE);
				mLabsView.setVisibility(View.VISIBLE);
			}

			@Override
			public void onDataUpdate(int page, List<?> records) {
				mProgressBar.setVisibility(View.GONE);
				mLabsView.setVisibility(View.VISIBLE);

				LabOrderActivity.mConnectedLabs.addAll((List<LabInfo>) records);
				refreshConnectedLabs();
			}

			@Override
			public void onCmdResponse(JSONObject response, String result) {
				// TODO Auto-generated method stub

			}
		});
		// LABS_FOR_LAB_TESTS
		// get and refresh connected labs
	}

	// refresh connected lab view
	private void refreshConnectedLabs() {
		mLabsView.removeAllViews();

		SOAPLabs labs = PhysicianSoapActivityMain.mSOAPLabs;
		this.refreshConnectedLabsFor(labs.getInternalLabs(), "Internal");
		this.refreshConnectedLabsFor(labs.getExternalLabs(), "External");
	}

	private void refreshConnectedLabsFor(List<LabInfo> labs, String type) {

		final LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		for (int i = 1; i < mConnectedLabs.size(); ++i) {
			LabInfo lab = mConnectedLabs.get(i);

			for (int j = 0; j < labs.size(); j++) {
				LabInfo theLab = labs.get(j);
				if (theLab.getBranchId().equals(lab.getBranchId())
						&& theLab.getTenantId().equals(lab.getTenantId())) {

					final int index = i;
					Log.i("setLabOptions()", "Set Item : " + index);
					final LinearLayout v = (LinearLayout) inflater.inflate(
							R.layout.row_radio_labs, null);
					final RadioButton rb = (RadioButton) v
							.findViewById(R.id.radio_item);
					rb.setId(index);
					v.removeView(rb);
					rb.setTag(theLab);
					mLabsView.addView(rb);
					rb.setText(" " + i + ": " + theLab.getName() + " (" + type
							+ ")");
					rb.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View view) {
							// EzUtils.showLong("Selected Item : " + index);
							mSelectedLab = (LabInfo) view.getTag();
						}
					});
				}
			}
		}
	}

	private void addSelectedTestId(int id) {
		for (int i = 0; i < mSelectedTestIds.length(); ++i) {
			if (mSelectedTestIds.optInt(i) == id)
				return;
		}
		LabOrderActivity.mSelectedTestIds.put(id);
	}

	private void refreshLabOrders() {

		LinearLayout llView = ((LinearLayout) findViewById(R.id.orders_summary));
		llView.removeAllViews();
		final LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		for (int i = 0; i < PhysicianSoapActivityMain.mSOAPLabs.getLabOrders()
				.size(); ++i) {
			final View v = inflater.inflate(R.layout.row_order_summary, null,
					false);
			llView.clearFocus();

			final LabOrder order = PhysicianSoapActivityMain.mSOAPLabs
					.getLabOrders().get(i);

			// Order ID clicked | start{
			SpannableString ss = new SpannableString(order.getOrderSummary());
			ClickableSpan clickableSpan = new ClickableSpan() {
				@Override
				public void onClick(View textView) {

					Intent intent;
					intent = new Intent(LabOrderActivity.this,
							LabOrderDetailsActivity.class);
					intent.putExtra("id", order.getId());
					startActivity(intent);
				}

				@Override
				public void updateDrawState(TextPaint ds) {
					super.updateDrawState(ds);
					ds.setUnderlineText(false);
				}
			};
			ss.setSpan(clickableSpan, 9, 30, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			TextView dv = ((TextView) v.findViewById(R.id.order_details));
			dv.setText(ss);
			dv.setMovementMethod(LinkMovementMethod.getInstance());
			dv.setHighlightColor(Color.TRANSPARENT);
			// }end

			llView.addView(v);
		}
	}

	private void refreshLabTests() {

		((LinearLayout) findViewById(R.id.ll_options)).removeAllViews();

		final LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		for (final Entry<String, String> entry : lab.hashLab.entrySet()) {

			Log.i("LOA:rLT()",
					"Key: " + entry.getKey() + ", Value: " + entry.getValue());

			final View v = inflater.inflate(R.layout.row_checked_item, null);
			final CheckBox cb = (CheckBox) v.findViewById(R.id.checked_item);

			String[] result = entry.getKey().split(TEST_ID_SPLIT_KEY);
			cb.setText(entry.getKey().replace(
					TEST_ID_SPLIT_KEY + result[result.length - 1], ""));

			if (result.length < 2) {
				Log.e("LOA:refreshLabTests()", "Bad value : " + entry.getKey());
				continue;
			}

			final int labTestId = Integer.parseInt(result[result.length - 1]);
			if (entry.getValue().equals("on")) {
				cb.setChecked(true);
				Log.i("refreshLabTests()", "adding id : " + labTestId);

				// get ids of selected lab tests
				addSelectedTestId(labTestId);
			} else {
				cb.setChecked(false);
			}
			cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(final CompoundButton buttonView,
						final boolean isChecked) {
					if (isChecked) {
						entry.setValue("on");
						// add id of selected lab tests
						addSelectedTestId(labTestId);
						Log.i("refreshLabTests::onCLick()", "ON : "
								+ buttonView.getId());
					} else {
						entry.setValue("off");
						// remove id of selected lab tests
						for (int i = 0; i < mSelectedTestIds.length(); ++i) {
							if (mSelectedTestIds.optInt(i) == labTestId) {
								mSelectedTestIds = EzUtils.removeInt(
										mSelectedTestIds, labTestId);
							}
						}
						Log.i("refreshLabTests::onCLick()", "OFF : "
								+ buttonView.getId());
					}
					LabOrderActivity.this.reloadConnectedLabs();
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
				intent = new Intent(this, PrintLabOrderedActivity.class);
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
		inflater.inflate(R.menu.lab_order, menu);
		return super.onCreateOptionsMenu(menu);
	}

	private Dialog dialog;
	final int FILE_CHOOSER = 1;

	private void uploadDialog() {
		dialog = EzDialog.getDialog(LabOrderActivity.this,
				R.layout.dialog_lab_upload, "Lab Document upload");
		LinearLayout ll = (LinearLayout) dialog.findViewById(R.id.ll_head_view);
		final ArrayList<String> dt = new ArrayList<String>();
		onPause();
		for (Entry<String, String> s : lab.hashLab.entrySet()) {
			View v = getLayoutInflater().inflate(R.layout.item_upload, null);
			CheckBox cb = (CheckBox) v.findViewById(R.id.cb_upload);
			if (s.getValue().equals("on")) {
				String[] result = s.getKey().split(TEST_ID_SPLIT_KEY);
				cb.setText(s.getKey().replace(
						TEST_ID_SPLIT_KEY + result[result.length - 1], ""));
				ll.addView(v);
			} else if (s.getKey().equals("othe")
					&& !Util.isEmptyString(lab.hashLab.get("othe"))) {
				cb.setText(lab.hashLab.get("othe"));
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
						Intent intent = new Intent(LabOrderActivity.this,
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
							Util.Alertdialog(LabOrderActivity.this,
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
							Util.Alertdialog(LabOrderActivity.this,
									"Please select lab upload type.");
						} else if (Util.isEmptyString(dn)) {
							Util.Alertdialog(LabOrderActivity.this,
									"Please enter document name.");
						} else if (!f.exists()) {
							Util.Alertdialog(LabOrderActivity.this,
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

	private void labTable() {
		final TableLayout tl = (TableLayout) findViewById(R.id.table);
		tl.removeAllViews();
		ArrayList<Document> arrRadiDocuments = new ArrayList<Document>();
		for (final Document doc : PhysicianSoapActivityMain.arrDocuments) {
			if (doc.getSection().equalsIgnoreCase("labs"))
				arrRadiDocuments.add(doc);
			// Log.i("",doc.getSection());
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
								LabOrderActivity.this);

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
		final Dialog loaddialog = Util.showLoadDialog(LabOrderActivity.this);
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
								// radiologyTable();
								labTable();

								Util.Alertdialog(LabOrderActivity.this,
										"Document deleted successfully");
							} else {
								Util.Alertdialog(LabOrderActivity.this,
										"There is some error while deleting, please try again.");
							}
						} catch (final JSONException e) {
							Util.Alertdialog(LabOrderActivity.this,
									"There is some error while deleting, please try again.");
							Log.e("", "" + e.getMessage());
						}
						loaddialog.dismiss();
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(final VolleyError error) {
						Util.Alertdialog(LabOrderActivity.this,
								"There is some network error, please try again.");

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
				+ "&section_name=labs&document_name=" + dn;

		Log.i("", requestURL);
		UploadDocumentRequest request = new UploadDocumentRequest(requestURL,
				new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError e) {
						dialog1.dismiss();
						Util.Alertdialog(LabOrderActivity.this,
								"There is some network error, please try again");
						Log.e("", "" + e.getMessage());

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
							labTable();
							dialog1.dismiss();
							dialog.dismiss();
							Util.Alertdialog(LabOrderActivity.this,
									"Document uploaded successfully.");
						} catch (JSONException e) {
							dialog1.dismiss();
							Util.Alertdialog(LabOrderActivity.this,
									"There is some error in uploading document, please try again");
							Log.e("", "" + e.getMessage());
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
			uploadDocument(image, "Lab", dn);

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
