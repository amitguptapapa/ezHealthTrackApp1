package com.ezhealthtrack.labs.activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.ezhealthtrack.EzApp;
import com.ezhealthtrack.R;
import com.ezhealthtrack.controller.AutoSuggestController;
import com.ezhealthtrack.controller.AutoSuggestController.OnAutoSuggest;
import com.ezhealthtrack.greendao.LabAppointment;
import com.ezhealthtrack.greendao.PatientShow;
import com.ezhealthtrack.greendao.PatientShowDao.Properties;
import com.ezhealthtrack.labs.adapter.LabsLabPanelAdapter;
import com.ezhealthtrack.labs.adapter.LabsLabSampleAdapter;
import com.ezhealthtrack.labs.adapter.LabsLabTestsAdapter;
import com.ezhealthtrack.labs.adapter.LabsTechnicianAdapter;
import com.ezhealthtrack.labs.controller.OrderController;
import com.ezhealthtrack.labs.model.LabTestAutoSuggest;
import com.ezhealthtrack.order.LabPanel;
import com.ezhealthtrack.order.LabTest;
import com.ezhealthtrack.order.OrderProduct;
import com.ezhealthtrack.order.SampleMetum;
import com.ezhealthtrack.util.BaseActivity;
import com.ezhealthtrack.util.Log;
import com.ezhealthtrack.util.NetworkCallController.OnResponse;
import com.ezhealthtrack.util.Util;
import com.flurry.android.FlurryAgent;
import com.google.gson.Gson;

public class LabsOrderUpdateActivity extends BaseActivity {
	private Button btnSubmit;
	private TextView txtPName;
	private TextView txtOrder;
	private TextView txtOrderStatus;
	private TextView txtOrderDate;
	private TextView txtLabTest;
	private TextView txtLabPanel;
	private TextView txtHomeVisit;
	private EditText editExternalOrderId;
	private RelativeLayout rlLabTest;
	private RelativeLayout rlLabPanel;
	private AutoCompleteTextView actvDocDetails;
	private AutoCompleteTextView actvSearchLabTest;
	private AutoCompleteTextView actvSearchLabPanel;
	private Spinner spinnerHomeVisit;
	private Spinner spinnerGenerateBill;
	private Spinner spinnerTechnician;
	private CheckBox cbSendPatNot;
	private LinearLayout llOrderItems;
	private GridView listLabTests;
	private GridView listLabPanel;
	private LabsLabTestsAdapter adapterTest;
	private LabsLabPanelAdapter adapterPanel;
	private ArrayAdapter<String> docAutoSuggestAdapter;
	private ArrayAdapter<LabTestAutoSuggest> labTestAutoSuggestAdapter;
	private LayoutInflater inflater;
	private LabAppointment apt;
	private ArrayList<String> arrGenerateBill = new ArrayList<String>();
	private ArrayList<String> arrHomeVisit = new ArrayList<String>();
	private boolean changeFlag;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.labs_activity_order_update);
		// getActionBar().setHomeButtonEnabled(true);

		PatientShow pat1 = new PatientShow();
		apt = (LabAppointment) EzApp.labAptDao
				.queryBuilder()
				.where(com.ezhealthtrack.greendao.LabAppointmentDao.Properties.Bkid
						.eq(getIntent().getStringExtra("bkid"))).list().get(0);
		final Dialog loaddialog = Util
				.showLoadDialog(LabsOrderUpdateActivity.this);
		OrderController.getCreateOrder(apt.getBkid(), this, new OnResponse() {

			@Override
			public void onResponseListner(String response) {
				Log.i("", response);
				if (!Util.isEmptyString(response))
					apt.setEpid(response);
				apt.orderView.getOrder().setFkey(
						OrderController.order.getFkey());
				adapterTest = new LabsLabTestsAdapter(
						LabsOrderUpdateActivity.this,
						R.layout.labs_row_lab_tests, OrderController.order
								.getLabTests());
				adapterPanel = new LabsLabPanelAdapter(
						LabsOrderUpdateActivity.this,
						R.layout.labs_row_lab_tests, OrderController.order
								.getLabPanel());
				listLabTests.setAdapter(adapterTest);
				listLabPanel.setAdapter(adapterPanel);
				spinnerTechnician = (Spinner) findViewById(R.id.spinner_technician);
				LabsTechnicianAdapter technicianAdapter = new LabsTechnicianAdapter(
						LabsOrderUpdateActivity.this,
						android.R.layout.simple_spinner_item,
						OrderController.order.getTechnician());
				technicianAdapter
						.setDropDownViewResource(R.layout.labs_row_technician);
				spinnerTechnician.setAdapter(technicianAdapter);
				loaddialog.dismiss();

			}
		});
		try {
			if (EzApp.patientShowDao
					.queryBuilder()
					.where(Properties.P_id.eq(apt.getPid()),
							Properties.Pf_id.eq(apt.getPfid())).count() > 0) {
				pat1 = (EzApp.patientShowDao
						.queryBuilder()
						.where(Properties.P_id.eq(apt.getPid()),
								Properties.Pf_id.eq(apt.getPfid())).list()
						.get(0));
			}

		} catch (Exception e) {
			Log.e("", e);
		}

		adapterTest = new LabsLabTestsAdapter(this,
				R.layout.labs_row_lab_tests,
				OrderController.order.getLabTests());
		adapterPanel = new LabsLabPanelAdapter(this,
				R.layout.labs_row_lab_tests,
				OrderController.order.getLabPanel());

		arrGenerateBill.add("Yes");
		arrGenerateBill.add("No");

		spinnerGenerateBill = (Spinner) findViewById(R.id.spinner_generate_bill);
		final ArrayAdapter<String> adapterGenerateBill = new ArrayAdapter<String>(
				this, android.R.layout.simple_spinner_item, arrGenerateBill);
		adapterGenerateBill
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerGenerateBill.setAdapter(adapterGenerateBill);

		arrHomeVisit.add("Yes");
		arrHomeVisit.add("No");

		spinnerHomeVisit = (Spinner) findViewById(R.id.spinner_home_visit);
		final ArrayAdapter<String> adapterPatientLocation = new ArrayAdapter<String>(
				this, android.R.layout.simple_spinner_item, arrHomeVisit);
		adapterPatientLocation
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerHomeVisit.setAdapter(adapterPatientLocation);
		spinnerHomeVisit.setSelection(1);

		txtPName = (TextView) findViewById(R.id.txt_pname_display);
		txtOrder = (TextView) findViewById(R.id.txt_order_display);
		txtOrderStatus = (TextView) findViewById(R.id.txt_order_status_display);
		txtOrderDate = (TextView) findViewById(R.id.txt_order_date_display);
		txtLabTest = (TextView) findViewById(R.id.txt_lab_tests);
		txtLabPanel = (TextView) findViewById(R.id.txt_lab_panel);
		txtHomeVisit = (TextView) findViewById(R.id.txt_home_visit);
		editExternalOrderId = (EditText) findViewById(R.id.edit_external_order_id);
		rlLabTest = (RelativeLayout) findViewById(R.id.rl_lab_tests);
		rlLabPanel = (RelativeLayout) findViewById(R.id.rl_lab_panel);
		btnSubmit = (Button) findViewById(R.id.btn_submit);
		llOrderItems = (LinearLayout) findViewById(R.id.ll_test_list);
		listLabTests = (GridView) findViewById(R.id.grid_lab_tests_list);
		listLabPanel = (GridView) findViewById(R.id.grid_lab_panels_list);
		actvDocDetails = (AutoCompleteTextView) findViewById(R.id.actv_doc_details_note);
		docAutoSuggestAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item);
		actvDocDetails.setAdapter(docAutoSuggestAdapter);
		actvSearchLabTest = (AutoCompleteTextView) findViewById(R.id.actv_search_lab_tests);
		actvSearchLabPanel = (AutoCompleteTextView) findViewById(R.id.actv_search_lab_panels);
		cbSendPatNot = (CheckBox) findViewById(R.id.cb_send_pat_not);
		labTestAutoSuggestAdapter = new ArrayAdapter<LabTestAutoSuggest>(this,
				android.R.layout.simple_spinner_item);
		actvSearchLabTest.setAdapter(labTestAutoSuggestAdapter);
		actvSearchLabPanel.setAdapter(labTestAutoSuggestAdapter);
		inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		if (!Util.isEmptyString(OrderController.order.getHomeConsultation())) {
			txtHomeVisit.setVisibility(View.VISIBLE);
			spinnerHomeVisit.setVisibility(View.VISIBLE);
		} else {
			txtHomeVisit.setVisibility(View.GONE);
			spinnerHomeVisit.setVisibility(View.GONE);
		}

		actvDocDetails.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				AutoSuggestController.autoSuggestDoctor(s.toString(),
						LabsOrderUpdateActivity.this, new OnAutoSuggest() {

							@Override
							public void OnAutoSuggestListner(Object list) {
								ArrayList<String> arr = ((ArrayList<String>) list);
								docAutoSuggestAdapter.clear();
								docAutoSuggestAdapter.addAll(arr);

							}
						});

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});

		actvSearchLabTest.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				AutoSuggestController.autoSuggestLabTest(s.toString(),
						LabsOrderUpdateActivity.this, new OnAutoSuggest() {

							@Override
							public void OnAutoSuggestListner(Object list) {
								ArrayList<LabTestAutoSuggest> arr = (ArrayList<LabTestAutoSuggest>) list;
								labTestAutoSuggestAdapter.clear();
								labTestAutoSuggestAdapter.addAll(arr);
							}
						});

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});

		actvSearchLabTest.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {
				LabTestAutoSuggest item1 = labTestAutoSuggestAdapter
						.getItem(pos);
				Boolean flag = true;
				for (OrderProduct item : apt.orderView.getOrder().getData()
						.getOrderProducts()) {
					if (item.getName().equals(item1.getName()))
						flag = false;
				}
				if (flag) {
					OrderProduct orderProduct = new OrderProduct();
					LabTest ltest = new Gson().fromJson(
							new Gson().toJson(item1), LabTest.class);
					try {
						Log.i("", item1.getSamples().toString());
						JSONObject samples = new JSONObject(item1.getSamples()
								.toString());
						Iterator<?> keys = samples.keys();

						while (keys.hasNext()) {
							String key = (String) keys.next();
							SampleMetum sample = new SampleMetum();
							sample.setName(samples.getString(key));
							ltest.getSampleMeta().add(sample);
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					orderProduct.labTest = ltest;
					orderProduct.setName(orderProduct.labTest.getName());
					orderProduct.setProductId(orderProduct.labTest.getId());
					apt.orderView.getOrder().getData().getOrderProducts()
							.add(orderProduct);
					OrderItems();
				} else {
					Util.Alertdialog(LabsOrderUpdateActivity.this,
							"Lab test already added!");
				}

			}
		});

		actvSearchLabPanel.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				AutoSuggestController.autoSuggestLabPanel(s.toString(),
						LabsOrderUpdateActivity.this, new OnAutoSuggest() {

							@Override
							public void OnAutoSuggestListner(Object list) {
								ArrayList<LabTestAutoSuggest> arr = (ArrayList<LabTestAutoSuggest>) list;
								labTestAutoSuggestAdapter.clear();
								labTestAutoSuggestAdapter.addAll(arr);
							}
						});

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});

		actvSearchLabPanel.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {
				LabTestAutoSuggest item1 = labTestAutoSuggestAdapter
						.getItem(pos);
				Boolean flag = true;
				for (OrderProduct item : apt.orderView.getOrder().getData()
						.getOrderProducts()) {
					if (item.getName().equals(item1.getName()))
						flag = false;
				}
				if (flag) {
					OrderProduct orderProduct = new OrderProduct();
					LabPanel ltest = new Gson().fromJson(
							new Gson().toJson(item1), LabPanel.class);

					orderProduct.labPanel = ltest;
					orderProduct.setName(orderProduct.labPanel.getName());
					orderProduct.setProductId(orderProduct.labPanel.getId());
					apt.orderView.getOrder().getData().getOrderProducts()
							.add(orderProduct);
					OrderItems();
				} else {
					Util.Alertdialog(LabsOrderUpdateActivity.this,
							"Lab panel already added!");
				}

			}
		});

		txtLabTest.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				FlurryAgent
						.logEvent("LabsOrderCreateActivity - Lab Test Button Clicked");
				rlLabTest.setVisibility(View.VISIBLE);
				rlLabPanel.setVisibility(View.GONE);
				txtLabTest.setTextColor(Color.parseColor("#A0A0A0"));
				txtLabPanel.setTextColor(Color.parseColor("#000000"));
			}
		});

		txtLabPanel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				FlurryAgent
						.logEvent("LabsOrderCreateActivity - Lab Panel Button Clicked");
				rlLabTest.setVisibility(View.GONE);
				rlLabPanel.setVisibility(View.VISIBLE);
				txtLabTest.setTextColor(Color.parseColor("#000000"));
				txtLabPanel.setTextColor(Color.parseColor("#A0A0A0"));
			}
		});

		final Date date = new Date();
		txtOrderDate.setText(EzApp.sdfddmmyyhhmmss.format(date));
		txtPName.setText(pat1.getPfn() + " " + pat1.getPln() + ", "
				+ pat1.getAge() + "/" + pat1.getGender());
		// txtOrder.setText(apt.orderView.getOrder().getOrder_display_id());
		// txtOrderStatus.setText(Order.getOrderStatus(apt.orderView.getOrder()
		// .getOrder_status_id()));

		btnSubmit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				FlurryAgent
						.logEvent("LabsOrderCreateActivity - Submit Button Clicked");
				boolean emptyCheck = true;
				for (OrderProduct product : apt.orderView.getOrder().getData()
						.getOrderProducts()) {
					if (Util.isEmptyString(product.getPrice()))
						emptyCheck = false;
				}

				if (apt.orderView.getOrder().getData().getOrderProducts()
						.size() > 0) {
					if (emptyCheck) {
						if (Util.isNumeric(apt.orderView.getOrder().getData()
								.getOrderProducts().get(0).getPrice())) {
							Util.Alertdialog(
									LabsOrderUpdateActivity.this,
									"Please confirm, if order should be created.",
									new OnResponse() {

										@Override
										public void onResponseListner(
												String response) {
											final Dialog loaddialog = Util
													.showLoadDialog(LabsOrderUpdateActivity.this);
											// Log.i("spinnerbill",
											// apt.orderView
											// .getOrder().getGenerate_bill());
											if (spinnerGenerateBill
													.getSelectedItemPosition() == 0) {
												apt.orderView.getOrder()
														.setGenerate_bill("1");
											} else {
												apt.orderView.getOrder()
														.setGenerate_bill("0");
											}

											// Log.i("spinner", apt.orderView
											// .getPatientLocAppointment());
											if (spinnerHomeVisit
													.getSelectedItemPosition() == 0) {
												apt.orderView
														.setPatientLocAppointment("1");

											} else {
												apt.orderView
														.setPatientLocAppointment("0");

											}
											apt.orderView
													.getLabOrderDetail()
													.setDoctor(
															actvDocDetails
																	.getText()
																	.toString());
											apt.orderView
													.getLabOrderDetail()
													.setOrderNotes(
															((EditText) findViewById(R.id.edit_note))
																	.getText()
																	.toString());
											apt.orderView
													.getLabOrderDetail()
													.setTechnicianId(
															""
																	+ OrderController.order
																			.getTechnician()
																			.get(spinnerTechnician
																					.getSelectedItemPosition())
																			.getId());

											// apt.orderView
											// .setPatientLocAppointment(spinnerHomeVisit
											// .getSelectedItem()
											// .toString());
											OrderController
													.createOrder(
															apt,
															LabsOrderUpdateActivity.this,
															new OnResponse() {

																@Override
																public void onResponseListner(
																		String response) {
																	apt.orderView
																			.getOrder()
																			.sum();
																	Intent intent = new Intent(
																			LabsOrderUpdateActivity.this,
																			LabsOrderSuccessActivity.class);
																	intent.putExtra(
																			"bkid",
																			apt.getBkid());
																	startActivity(intent);
																	loaddialog
																			.dismiss();
																	finish();

																}
															},
															editExternalOrderId
																	.getText()
																	.toString());

										}
									});
						} else {
							Util.Alertdialog(LabsOrderUpdateActivity.this,
									"Price should be in numeric only!");
						}
					} else {
						Util.Alertdialog(LabsOrderUpdateActivity.this,
								"Please enter Lab Test Price.");
					}

				} else {
					Util.Alertdialog(LabsOrderUpdateActivity.this,
							"Please add Lab Test");
				}
			}

		});
		LabTests();
		LabPanel();
		OrderItems();
		homeVisit();
	}

	private void OrderItems() {
		llOrderItems.removeAllViews();

		for (final OrderProduct orderProduct : apt.orderView.getOrder()
				.getData().getOrderProducts()) {
			final View v = inflater
					.inflate(R.layout.labs_row_order_items, null);
			llOrderItems.addView(v);
			final Spinner spinnerSample = (Spinner) v
					.findViewById(R.id.spinner_sample);
			TextView TestName = (TextView) v.findViewById(R.id.txt_test_name);
			EditText editPrice = (EditText) v.findViewById(R.id.edit_price);

			if (orderProduct.labTest != null) {
				try {
					LabsLabSampleAdapter sampleAdapter = new LabsLabSampleAdapter(
							this, android.R.layout.simple_spinner_item,
							orderProduct.labTest.getSampleMeta());

					sampleAdapter
							.setDropDownViewResource(R.layout.labs_row_lab_tests_sample);
					spinnerSample.setAdapter(sampleAdapter);

					spinnerSample
							.setOnItemSelectedListener(new OnItemSelectedListener() {

								@Override
								public void onItemSelected(AdapterView<?> arg0,
										View arg1, int pos, long arg3) {
									Log.i("", orderProduct.labTest
											.getSampleMeta().get(pos).getName());
									orderProduct
											.setProductModel(orderProduct.labTest
													.getSampleMeta().get(pos)
													.getName());

								}

								@Override
								public void onNothingSelected(
										AdapterView<?> arg0) {
									// TODO Auto-generated method stub

								}
							});
				} catch (Exception e) {

				}

				TestName.setText(orderProduct.getName());
				if (Util.isEmptyString(orderProduct.getPrice()))
					orderProduct.setPrice(orderProduct.labTest.getPriceMin());
			} else {
				spinnerSample.setVisibility(View.INVISIBLE);
				TestName.setText(orderProduct.getName());
				if (Util.isEmptyString(orderProduct.getPrice()))
					orderProduct.setPrice(orderProduct.labPanel.getPriceMin());

			}
			editPrice.setText("" + orderProduct.getPrice());
			editPrice.addTextChangedListener(new TextWatcher() {

				@Override
				public void onTextChanged(CharSequence arg0, int arg1,
						int arg2, int arg3) {
					try {
						orderProduct.setPrice(arg0.toString());
					} catch (Exception e) {
						orderProduct.setPrice("0.0");
					}

				}

				@Override
				public void beforeTextChanged(CharSequence arg0, int arg1,
						int arg2, int arg3) {

				}

				@Override
				public void afterTextChanged(Editable arg0) {
					// TODO Auto-generated method stub

				}

			});

			ImageView imgDelete = (ImageView) v.findViewById(R.id.img_delete);
			imgDelete.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View a) {
					FlurryAgent
							.logEvent("LabsOrderCreateActivity - Delete Button Clicked");
					final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
							LabsOrderUpdateActivity.this);

					// set title
					alertDialogBuilder.setTitle("Are you sure !");
					// set dialog message
					alertDialogBuilder
							.setCancelable(false)
							.setPositiveButton("Yes",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											llOrderItems.removeView(v);
											apt.orderView.getOrder().getData()
													.getOrderProducts()
													.remove(orderProduct);

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

					final AlertDialog alertDialog = alertDialogBuilder.create();

					alertDialog.show();
				}

			});
		}

	}

	private void LabTests() {

		listLabTests.setAdapter(adapterTest);
		listLabTests.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {
				Boolean flag = true;
				LabTest lTest = OrderController.order.getLabTests().get(pos);
				for (OrderProduct item : apt.orderView.getOrder().getData()
						.getOrderProducts()) {
					if (item.getName().equals(lTest.getName()))
						flag = false;
				}
				if (flag) {
					OrderProduct orderProduct = new OrderProduct();
					orderProduct.labTest = lTest;
					orderProduct.setName(orderProduct.labTest.getName());
					orderProduct.setProductId(orderProduct.labTest.getId());
					apt.orderView.getOrder().getData().getOrderProducts()
							.add(orderProduct);

					OrderItems();
				} else {
					Util.Alertdialog(LabsOrderUpdateActivity.this,
							"Lab test already added!");
				}

			}
		});

	}

	private void LabPanel() {

		listLabPanel.setAdapter(adapterPanel);
		listLabPanel.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {
				Boolean flag = true;
				LabPanel pTest = OrderController.order.getLabPanel().get(pos);
				for (OrderProduct item : apt.orderView.getOrder().getData()
						.getOrderProducts()) {
					if (item.getName().equals(pTest.getName()))
						flag = false;
				}
				if (flag) {
					OrderProduct orderProduct = new OrderProduct();
					orderProduct.labPanel = pTest;
					orderProduct.setName(orderProduct.labPanel.getName());
					orderProduct.setProductId(orderProduct.labPanel.getId());
					apt.orderView.getOrder().getData().getOrderProducts()
							.add(orderProduct);
					OrderItems();
				} else {
					Util.Alertdialog(LabsOrderUpdateActivity.this,
							"Lab panel already added!");
				}

			}
		});

	}

	public boolean onOptionsItemSelected(final MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {

		case android.R.id.home:
			finish();
			return true;

		case R.id.action_close:
			final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					this);

			// set title
			alertDialogBuilder
					.setTitle("Changes will be discarded, Do you want to continue ?");
			// set dialog message
			alertDialogBuilder
					.setCancelable(false)
					.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									finish();

								}

							})
					.setNegativeButton("No",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(
										final DialogInterface dialog,
										final int id) {
									// if this button is clicked, just close
									// the dialog box and do nothing
									dialog.cancel();
								}
							});

			final AlertDialog alertDialog = alertDialogBuilder.create();

			alertDialog.show();

			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onBackPressed() {
		final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				this);

		// set title
		alertDialogBuilder
				.setTitle("Changes will be discarded, Do you want to continue ?");
		// set dialog message
		alertDialogBuilder
				.setCancelable(false)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								finish();

							}

						})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(final DialogInterface dialog,
							final int id) {
						// if this button is clicked, just close
						// the dialog box and do nothing
						dialog.cancel();
					}
				});

		final AlertDialog alertDialog = alertDialogBuilder.create();

		alertDialog.show();

	}

	private void homeVisit() {
		spinnerHomeVisit
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						if (spinnerHomeVisit.getSelectedItemPosition() == 0) {
							OrderProduct orderProduct = new OrderProduct();
							orderProduct.setName("Home consultation fee");
							orderProduct.setProductId("1");
							orderProduct.setPrice(OrderController.order
									.getHomeConsultation().toString());
							orderProduct.setQuantity("1");
							orderProduct.setType("lab_charges");
							apt.orderView.getOrder().getData()
									.getOrderProducts().add(orderProduct);
							OrderItems();
						}
						// else if (spinnerHomeVisit.getSelectedItemPosition()
						// == 1) {
						// for (final OrderProduct orderProduct : apt.orderView
						// .getOrder().getData().getOrderProducts()) {
						// final View v = inflater.inflate(
						// R.layout.labs_row_order_items, null);
						// apt.orderView.getOrder().getData()
						// .getOrderProducts()
						// .remove(orderProduct);
						// llOrderItems.removeView(v);
						// }
						// }
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub

					}
				});
	}
}
