package com.ezhealthtrack.labs.activity;

import java.util.Date;

import android.content.Context;
import android.content.Intent;
import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import com.ezhealthtrack.util.Util;
import com.ezhealthtrack.util.NetworkCallController.OnResponse;
import com.flurry.android.FlurryAgent;
import com.google.gson.Gson;

public class LabsReportValuesViewActivity extends BaseActivity implements
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
	private LinearLayout llReportValues;
	private LayoutInflater inflater;
	private Patient pat = new Patient();

	// private Result res = new Result();

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.labs_activity_report_values_view);
		getActionBar().setHomeButtonEnabled(true);
		inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		final Dialog loaddialog = Util
				.showLoadDialog(LabsReportValuesViewActivity.this);
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

		txtOrder.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				FlurryAgent
						.logEvent("LabsReportValuesViewActivity - Order Details Button Clicked");
				final Dialog loaddialog = Util
						.showLoadDialog(LabsReportValuesViewActivity.this);
				OrderController.getOrderList("1",
						LabsReportValuesViewActivity.this, new OnResponse() {

							@Override
							public void onResponseListner(String response) {
								Intent intent = new Intent(
										LabsReportValuesViewActivity.this,
										LabsOrderDetailsActivity.class);
								intent.putExtra("orderid", apt.orderView
										.getOrder().getOrder_id());
								startActivity(intent);
								loaddialog.dismiss();
							}
						}, null, null, null, -1, -1, "");

			}
		});
		btnSubmit = (Button) findViewById(R.id.btn_submit);
		btnSubmit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				FlurryAgent
						.logEvent("LabsReportValuesActivity - Close window Button Clicked");
				finish();

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
			txtDoctor.setText(apt.orderView.getLabOrderDetail().getDoctor());
			txtTechnician.setText(LabsController.getLabTechnician(
					apt.orderView.getLabOrderDetail().getTechnicianId())
					.getName());
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

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void setReportValuesTable() {
		llReportValues.removeAllViews();
		for (final SampleMetum sm : report.getSampleMeta()) {
			Log.i("", new Gson().toJson(sm));
			final View v = inflater.inflate(
					R.layout.labs_row_report_values_view, null);
			final TextView txtSample = (TextView) v
					.findViewById(R.id.txt_sample_display);
			final TextView txtMethod = (TextView) v
					.findViewById(R.id.txt_method_display);
			final TextView txtTestNameMain = (TextView) v
					.findViewById(R.id.txt_main_test_name);
			final TextView txtUnit = (TextView) v
					.findViewById(R.id.txt_unit_display);
			final TextView txtReferenceRange = (TextView) v
					.findViewById(R.id.txt_reference_range_display);
			final TextView txtNotes = (TextView) v
					.findViewById(R.id.txt_notes_display);
			final TextView txtResultNotes = (TextView) v
					.findViewById(R.id.txt_result_notes);
			final TextView txtObservedValue = (TextView) v
					.findViewById(R.id.txt_observed_value);
			final TextView txtResultNotesDisplay = (TextView) v
					.findViewById(R.id.txt_notes);
			final TextView txtTestName = (TextView) v
					.findViewById(R.id.txt_test_name_display);
			if (sm.getResults().size() > 0) {
				final Result res = sm.getResults().get(0);
				txtUnit.setText(res.getUnit());
				txtTestNameMain.setText(sm.getResults().get(0).getName());
				if (!Util.isEmptyString(sm.getResults().get(0).getValue()))
					txtObservedValue.setText(sm.getResults().get(0).getValue());
				if (!Util.isEmptyString(sm.getResults().get(0).getNotes())) {
					txtResultNotes.setVisibility(View.VISIBLE);
					txtResultNotesDisplay.setVisibility(View.VISIBLE);
					txtResultNotesDisplay.setText(sm.getResults().get(0)
							.getNotes());
				} else {
					txtResultNotes.setVisibility(View.GONE);
					txtResultNotesDisplay.setVisibility(View.GONE);
				}
				txtSample.setText(sm.getName());
				txtTestName.setText(report.getLabTestName());

				for (Reference result : sm.getResults().get(0).getReferences()) {
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

			} else {
				final Result res = sm.sample_meta.get(0).getResults().get(0);
				txtUnit.setText(res.getUnit());
				txtTestNameMain.setText(res.getName());
				if (sm.getResults().size() == 0) {
					sm.getResults().add(new Result());
				}
				if (!Util.isEmptyString(res.getValue())) {
					txtObservedValue.setText(res.getValue());
					sm.getResults().get(0).setValue(res.getValue());
				} else {
					sm.getResults().get(0).setValue("");
				}
				if (!Util.isEmptyString(res.getNotes())) {
					txtResultNotes.setVisibility(View.VISIBLE);
					txtResultNotesDisplay.setVisibility(View.VISIBLE);
					txtResultNotesDisplay.setText(res.getNotes());
					sm.getResults().get(0).setNotes(res.getNotes());
				} else {
					txtResultNotes.setVisibility(View.GONE);
					txtResultNotesDisplay.setVisibility(View.GONE);
					sm.getResults().get(0).setNotes("");
				}
				txtSample.setText(sm.sample_meta.get(0).getName());
				txtTestName.setText(sm.getName());

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
			}

			txtObservedValue.addTextChangedListener(new TextWatcher() {

				@Override
				public void onTextChanged(CharSequence arg0, int arg1,
						int arg2, int arg3) {
					if (sm.getResults().size() == 0)
						sm.getResults().add(new Result());
					sm.getResults().get(0).setValue(arg0.toString());

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

			txtResultNotesDisplay.addTextChangedListener(new TextWatcher() {

				@Override
				public void onTextChanged(CharSequence s, int start,
						int before, int count) {
					if (sm.getResults().size() == 0)
						sm.getResults().add(new Result());
					sm.getResults().get(0).setNotes(s.toString());

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
			llReportValues.addView(v);
		}

	}

}
