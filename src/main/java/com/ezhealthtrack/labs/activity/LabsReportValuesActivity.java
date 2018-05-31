package com.ezhealthtrack.labs.activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.ezhealthrack.api.NetworkCalls;
import com.ezhealthtrack.EzApp;
import com.ezhealthtrack.R;
import com.ezhealthtrack.greendao.LabAppointment;
import com.ezhealthtrack.greendao.Order;
import com.ezhealthtrack.greendao.OrderDao;
import com.ezhealthtrack.greendao.Patient;
import com.ezhealthtrack.greendao.PatientDao.Properties;
import com.ezhealthtrack.labs.controller.LabsController;
import com.ezhealthtrack.labs.controller.OrderController;
import com.ezhealthtrack.labs.controller.OrderController.OnResponseData;
import com.ezhealthtrack.labs.fragments.SideFragment.OnActionSelectedListner;
import com.ezhealthtrack.order.LabOrderReport;
import com.ezhealthtrack.order.OrderView;
import com.ezhealthtrack.order.Reference;
import com.ezhealthtrack.order.Result;
import com.ezhealthtrack.order.SampleMetum;
import com.ezhealthtrack.util.BaseActivity;
import com.ezhealthtrack.util.Log;
import com.ezhealthtrack.util.NetworkCallController.OnResponse;
import com.ezhealthtrack.util.Util;
import com.flurry.android.FlurryAgent;
import com.google.gson.Gson;

public class LabsReportValuesActivity extends BaseActivity implements
		OnActionSelectedListner, NetworkCalls.OnResponse {

	private LabAppointment apt;
	// private OrderProduct orderProduct;
	private LabOrderReport report;
	private SampleMetum sampleMetum;
	private Button btnSubmit;
	private TextView txtPname;
	private TextView txtOrderDate;
	private TextView txtOrder;
	private TextView txtOrderStatus;
	private TextView txtBill;
	private TextView txtBillStatus;
	private TextView txtDoctor;
	private TextView txtTechnician;
	private TextView txtHomeVisit;
	private LinearLayout llReportValues;
	private LayoutInflater inflater;
	private TextView txtPanel;
	private Patient pat = new Patient();

	// private Result res = new Result();

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.labs_activity_report_values);
		getActionBar().setHomeButtonEnabled(true);
		inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		final Dialog loaddialog = Util
				.showLoadDialog(LabsReportValuesActivity.this);
		if (getIntent().hasExtra("bkid")
				&& !Util.isEmptyString(getIntent().getStringExtra("bkid"))) {
			apt = EzApp.labAptDao
					.queryBuilder()
					.where(com.ezhealthtrack.greendao.LabAppointmentDao.Properties.Bkid
							.eq(getIntent().getStringExtra("bkid"))).list()
					.get(0);
			OrderController.getViewOrder(this, new OnResponseData() {

				@Override
				public void onResponseListner(Object response) {
					apt.orderView = (OrderView) response;
					for (LabOrderReport rep : apt.orderView
							.getLabOrderReports()) {
						if (rep.getId().equals(
								getIntent().getStringExtra("reportid")))
							report = rep;
					}
					details();
					loaddialog.dismiss();
				}
			}, apt.orderView, getIntent().getStringExtra("bkid"));

		} else {
			apt = new LabAppointment();
			Log.i("", "" + getIntent().getLongExtra("orderid", 0));
			apt.orderView.setOrder(EzApp.orderDao
					.queryBuilder()
					.where(OrderDao.Properties.Id.eq(getIntent().getLongExtra(
							"orderid", 0))).list().get(0));
			OrderController.getViewOrder1(this, new OnResponseData() {

				@Override
				public void onResponseListner(Object response) {
					apt.orderView = (OrderView) response;
					for (LabOrderReport rep : apt.orderView
							.getLabOrderReports()) {
						if (rep.getId().equals(
								getIntent().getStringExtra("reportid")))
							report = rep;
					}
					Log.i("", "" + apt.orderView.getLabOrderReports().size());
					details();
					loaddialog.dismiss();
				}
			}, apt.orderView, "" + apt.orderView.getOrder().getOrder_id());
		}

		txtPname = (TextView) findViewById(R.id.txt_name_display);
		txtOrderDate = (TextView) findViewById(R.id.txt_order_date_display);
		txtOrder = (TextView) findViewById(R.id.txt_order_display);
		txtTechnician = (TextView) findViewById(R.id.txt_technician_display);
		txtBill = (TextView) findViewById(R.id.txt_bill_status);
		txtBillStatus = (TextView) findViewById(R.id.txt_bill_status_display);
		txtOrderStatus = (TextView) findViewById(R.id.txt_order_status_display);
		txtDoctor = (TextView) findViewById(R.id.txt_doctor_display);
		llReportValues = (LinearLayout) findViewById(R.id.ll_report_values);
		txtPanel = (TextView) findViewById(R.id.txt_panel_name_display);
		txtHomeVisit = (TextView) findViewById(R.id.txt_home_visit_display);

		btnSubmit = (Button) findViewById(R.id.btn_submit);
		btnSubmit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				FlurryAgent
						.logEvent("LabsReportValuesActivity - Submit Button Clicked");

				final Dialog loaddialog = Util
						.showLoadDialog(LabsReportValuesActivity.this);
				OrderController.reportValues(LabsReportValuesActivity.this,
						new OnResponseData() {

							@Override
							public void onResponseListner(Object response) {
								finish();
								loaddialog.dismiss();
							}
						}, report);

			}
		});

	}

	protected void details() {
		try {
			if (EzApp.patientDao
					.queryBuilder()
					.where(Properties.Pid.eq(apt.orderView.getOrder()
							.getCustomer_id())).count() > 0) {
				pat = (EzApp.patientDao
						.queryBuilder()
						.where(Properties.Pid.eq(apt.orderView.getOrder()
								.getCustomer_id())).list().get(0));
				txtPname.setText(pat.getP_detail());
			}
			if (apt.orderView.getOrder().getGenerate_bill().equals("0")) {

				txtBill.setVisibility(View.GONE);
				txtBillStatus.setVisibility(View.GONE);

			} else {
				txtBill.setVisibility(View.VISIBLE);
				txtBillStatus.setVisibility(View.VISIBLE);
			}
			final Date date = apt.orderView.getOrder().getDate_added();
			txtOrderDate.setText(EzApp.sdfddMmyy.format(date));
			txtPname.setText(pat.getP_detail());
			txtOrder.setText(apt.orderView.getOrder().getOrder_display_id());
			txtOrderStatus.setText(Order.getOrderStatus(apt.orderView
					.getOrder().getOrder_status_id()));
			txtBillStatus.setText(Order.getBillStatus(apt.orderView.getOrder()
					.getBill_status_id()));
			if (!Util.isEmptyString(apt.orderView.getLabOrderDetail()
					.getDoctor())) {
				txtDoctor
						.setText(apt.orderView.getLabOrderDetail().getDoctor());
			} else {
				txtDoctor.setText("--");
			}
			txtTechnician.setText(LabsController.getLabTechnician(
					apt.orderView.getLabOrderDetail().getTechnicianId())
					.getName());
			txtPanel.setText(report.getLabTestName());
			txtHomeVisit.setText(apt.orderView.getPatientLocAppointment()
					.toString());
			txtOrder.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					FlurryAgent
							.logEvent("LabsReportValuesActivity - Order Details Button Clicked");
					final Dialog loaddialog = Util
							.showLoadDialog(LabsReportValuesActivity.this);
					OrderController.getOrderList("1",
							LabsReportValuesActivity.this, new OnResponse() {

								@Override
								public void onResponseListner(String response) {
									Intent intent = new Intent(
											LabsReportValuesActivity.this,
											LabsOrderDetailsActivity.class);
									intent.putExtra("orderid", apt.orderView
											.getOrder().getOrder_id());
									startActivity(intent);
									loaddialog.dismiss();
								}
							}, null, null, null, -1, -1, "");

				}
			});
			setReportValuesTable();

		} catch (Exception e) {
			Log.e("", e);

		}

	}

	@Override
	public void onResponseListner(String api) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onActionSelected(int position) {
		// TODO Auto-generated method stub

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

	private void setReportValuesTable() {
		llReportValues.removeAllViews();
		for (final SampleMetum sm1 : report.getSampleMeta()) {
			final SampleMetum sm;
			if (sm1.getResults().size() == 0) {
				sm = sm1.sample_meta.get(0);
			} else {
				sm = sm1;
			}
			Log.i("", new Gson().toJson(sm));
			final View v = inflater.inflate(R.layout.labs_row_report_values,
					null);
			final TextView txtSample = (TextView) v
					.findViewById(R.id.txt_sample_display);
			final TextView txtMethod = (TextView) v
					.findViewById(R.id.txt_method_display);
			final TextView txtTestName = (TextView) v
					.findViewById(R.id.txt_test_name_display);
			final LinearLayout llResults = (LinearLayout) v
					.findViewById(R.id.ll_results);
			llResults.removeAllViews();
			txtSample.setText(sm.getName());
			// txtMethod.setText();
			txtTestName.setText(sm1.getName());
			txtMethod.setText(sm.getMethod());
			final List<Result> arrResults = sm.getResults();
			for (final Result res : arrResults) {
				final View v1 = inflater.inflate(
						R.layout.labs_row_report_results, null);
				llResults.addView(v1);
				final TextView txtTestNameMain = (TextView) v1
						.findViewById(R.id.txt_main_test_name);
				final TextView txtUnit = (TextView) v1
						.findViewById(R.id.txt_unit_display);
				final TextView txtReferenceRange = (TextView) v1
						.findViewById(R.id.txt_reference_range_display);
				final TextView txtNotes = (TextView) v1
						.findViewById(R.id.txt_notes_display);
				final TextView txtResultNotes = (TextView) v1
						.findViewById(R.id.txt_result_notes);
				final EditText editObservedValue = (EditText) v1
						.findViewById(R.id.edit_observed_value);
				final EditText editNotes = (EditText) v1
						.findViewById(R.id.edit_notes);
				final TextView txtGroupName = (TextView) v1
						.findViewById(R.id.txt_group_name);
				final Spinner spinnerObservedValue = (Spinner) v1
						.findViewById(R.id.spinner_observed_value);
				final ArrayList<String> arrObservedValue = new ArrayList<String>();

				arrObservedValue.add("Not Calculated");
				arrObservedValue.add("In Range");
				arrObservedValue.add("Out Range");
				final ArrayAdapter<String> adapterObservedValue = new ArrayAdapter<String>(
						this, android.R.layout.simple_spinner_item,
						arrObservedValue);
				adapterObservedValue
						.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				spinnerObservedValue.setAdapter(adapterObservedValue);
				// Log.i("results size",""+ sm.getResults().size());
				txtUnit.setText(res.getUnit());
				txtTestNameMain.setText(res.getName());
				if (!Util.isEmptyString(res.getValue()))
					editObservedValue.setText(res.getValue());
				Log.i("result", res.getGroupName());
				if (!Util.isEmptyString(res.getNotes())) {
					txtResultNotes.setVisibility(View.GONE);
					editNotes.setVisibility(View.VISIBLE);
					editNotes.setText(res.getNotes());
				} else {
					txtResultNotes.setVisibility(View.VISIBLE);
					editNotes.setVisibility(View.GONE);
				}
				if (!Util.isEmptyString(res.getGroupName())) {
					txtGroupName.setVisibility(View.VISIBLE);
					txtGroupName.setText(res.getGroupName());
				} else {
					txtGroupName.setText("");
					txtGroupName.setVisibility(View.GONE);
				}

				for (Reference result : res.getReferences()) {
					if (result.getGender().equals(pat.getPgender())
							|| result.getGender().equals("Both")) {
						// Log.e("age", ""+pat.getage());
						if (true) {
							if (!result.getRangeValueMinOption().contains(
									"None")) {
								txtReferenceRange.append(result
										.getRangeValueMinOption());
							}
							if (result.getRangeValueMin().length() > 0) {
								txtReferenceRange.append(" "
										+ result.getRangeValueMin() + " (min)");
							}
							if (!result.getRangeValueMaxOption().contains(
									"None")) {
								txtReferenceRange.append(" "
										+ result.getRangeValueMaxOption());
							}
							if (result.getRangeValueMax().length() > 0) {
								txtReferenceRange.append(" "
										+ result.getRangeValueMax() + " (max)");
							}
							if (!Util.isEmptyString(result.getNotes()))
								txtNotes.append(result.getNotes());
							txtReferenceRange.append("\n");
							txtNotes.append("\n");
						}
					}
				}

				txtResultNotes.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						txtResultNotes.setVisibility(View.GONE);
						editNotes.setVisibility(View.VISIBLE);

					}
				});
				editObservedValue.addTextChangedListener(new TextWatcher() {

					@Override
					public void onTextChanged(CharSequence arg0, int arg1,
							int arg2, int arg3) {
						res.setValue(arg0.toString());

					}

					@Override
					public void beforeTextChanged(CharSequence arg0, int arg1,
							int arg2, int arg3) {
						// TODO Auto-generated method stub

					}

					@Override
					public void afterTextChanged(Editable arg0) {
						// TODO Auto-generated method stub

					}
				});

				editNotes.addTextChangedListener(new TextWatcher() {

					@Override
					public void onTextChanged(CharSequence s, int start,
							int before, int count) {
						res.setNotes(s.toString());

					}

					@Override
					public void beforeTextChanged(CharSequence s, int start,
							int count, int after) {
						// TODO Auto-generated method stub

					}

					@Override
					public void afterTextChanged(Editable s) {
						// TODO Auto-generated method stub

					}
				});

				spinnerObservedValue
						.setOnItemSelectedListener(new OnItemSelectedListener() {

							@Override
							public void onItemSelected(AdapterView<?> arg0,
									View arg1, int arg2, long arg3) {
								res.setStatus("" + arg2);

							}

							@Override
							public void onNothingSelected(AdapterView<?> arg0) {
								// TODO Auto-generated method stub

							}

						});
			}

			llReportValues.addView(v);
		}

	}
}
